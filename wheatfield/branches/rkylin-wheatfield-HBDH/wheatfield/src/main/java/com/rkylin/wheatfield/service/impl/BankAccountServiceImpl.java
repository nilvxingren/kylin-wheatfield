package com.rkylin.wheatfield.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkylin.wheatfield.api.BankAccountServiceApi;
import com.rkylin.wheatfield.constant.BaseConstants;
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.manager.AccountInfoManager;
import com.rkylin.wheatfield.manager.FinanaceAccountManager;
import com.rkylin.wheatfield.pojo.AccountInfo;
import com.rkylin.wheatfield.pojo.AccountInfoQuery;
import com.rkylin.wheatfield.pojo.FinanaceAccount;
import com.rkylin.wheatfield.pojo.FinanaceAccountQuery;
import com.rkylin.wheatfield.service.BankAccountService;

@Service("bankAccountService")
public class BankAccountServiceImpl implements BankAccountService,BankAccountServiceApi {
	private static Logger logger = LoggerFactory.getLogger(BankAccountServiceImpl.class);
	@Autowired
	FinanaceAccountManager finanaceAccountManager;
	@Autowired
	AccountInfoManager accountInfoManager;
	
	/**
	 * params :提供者id,共用者id,机构码,产品号,共用卡号
	 */
	@Override
	public String shareBankCardInfo(String providerId, String shareId, String constId,String productId, String bankCardNo) {
		logger.info("调用共用银行账户接口开始----参数：提供者ID-" + providerId + ";共用者ID-" + shareId + ";机构码-" + constId + ";产品号-" + productId + ";共用卡号-" + bankCardNo);

		String checkParamFlag = checkParam(providerId,shareId,constId,productId,bankCardNo);
		if(!"OK".equals(checkParamFlag)){
			return checkParamFlag;
		}
		try{
			//提供者账户有效性校验
			FinanaceAccountQuery query=new FinanaceAccountQuery();
			query.setRootInstCd(constId);
			query.setAccountRelateId(providerId);
			query.setGroupManage(productId);
			List<FinanaceAccount> faList=finanaceAccountManager.queryList(query);
			if(faList == null || faList.isEmpty()){
				logger.info("未查找到提供方信息请核实!");
				return "未查找到提供方信息请核实!";	
			}
			FinanaceAccount finanaceAccount = faList.get(0);
			if(!BaseConstants.ACCOUNT_STATUS_OK.equals(finanaceAccount.getStatusId())){
				logger.info("提供方账户非有效状态!");
				return "提供方账户非有效状态!";	
			}
	
			AccountInfoQuery accountInfoQuery = new AccountInfoQuery();
			accountInfoQuery.setFinAccountId(finanaceAccount.getFinAccountId());
			accountInfoQuery.setAccountName(providerId);
			accountInfoQuery.setStatus(Constants.ACCOUNT_NUM_STATRS_1);
			accountInfoQuery.setAccountPurpose(Constants.ACCOUNT_PURPOSE_1);
			
			List<AccountInfo> accountInfoList = accountInfoManager.queryListPlus(accountInfoQuery);
			if(accountInfoList == null || accountInfoList.isEmpty()){
				logger.info("提供方未查找到有效的结算卡信息!");
				return "提供方未查找到有效的结算卡信息!";
			}
			AccountInfo accountInfo = accountInfoList.get(0);
			if(!accountInfo.getAccountNumber().equals(bankCardNo)){
				logger.info("输入卡号与提供方结算卡号不同,请重新输入!");
				return "输入卡号与提供方结算卡号不同,请重新输入!";	
			}
			
			//共用者账户有效性校验
			query.setRootInstCd(constId);
			query.setAccountRelateId(shareId);
			query.setGroupManage(productId);
			List<FinanaceAccount> faListGyf=finanaceAccountManager.queryList(query);
			if(faListGyf == null || faListGyf.isEmpty()){
				logger.info("未查找到共用方信息请核实!");
				return "未查找到共用方信息请核实!";	
			}
			FinanaceAccount finanaceAccountGyf = faListGyf.get(0);
			if(!BaseConstants.ACCOUNT_STATUS_OK.equals(finanaceAccountGyf.getStatusId())){
				logger.info("共用方账户非有效状态!");
				return "共用方账户非有效状态!";	
			}
			
			String checkMsg = checkAccountInfo(finanaceAccountGyf.getFinAccountId(),shareId);
			if(!"OK".equals(checkMsg)){
				return checkMsg;
			}
	
			accountInfo.setAccountId(null);
			accountInfo.setFinAccountId(finanaceAccountGyf.getFinAccountId());
			accountInfo.setAccountName(finanaceAccountGyf.getAccountRelateId());
			accountInfo.setCreatedTime(null);
			accountInfo.setUpdatedTime(null);
			accountInfoManager.saveAccountInfo(accountInfo);
		}catch(Exception ex){
			logger.error("异常信息：" + ex.getMessage());
			return "调用异常!";
		}
		logger.info("---------------调用共用银行账户接口结束-----------------");
		return "OK";
	}
	
	private String checkAccountInfo(String financeAccountId,String userId){
		AccountInfoQuery accountInfoQuery = new AccountInfoQuery();
		accountInfoQuery.setFinAccountId(financeAccountId);
		accountInfoQuery.setAccountName(userId);
		accountInfoQuery.setStatus(Constants.ACCOUNT_NUM_STATRS_OK_ALL);
		accountInfoQuery.setAccountPurpose(Constants.ACCOUNT_PURPOSE_1);
		
		List<AccountInfo> accountInfoListGyf = accountInfoManager.queryListPlus(accountInfoQuery);
		if(accountInfoListGyf != null && !accountInfoListGyf.isEmpty()){
			AccountInfo accountInfoGyf = accountInfoListGyf.get(0);
			if(accountInfoGyf.getStatus() == Constants.ACCOUNT_NUM_STATRS_1){
				logger.info("共用方已绑定有效结算卡!");
				return "共用方已绑定有效结算卡!";					
			}
			if(accountInfoGyf.getStatus() == Constants.ACCOUNT_NUM_STATRS_2){
				logger.info("共用方已绑定结算卡,状态为待审核");
				return "共用方已绑定有效结算卡,状态为待审核";	
			}
			if(accountInfoGyf.getStatus() == Constants.ACCOUNT_NUM_STATRS_3){
				logger.info("共用方已绑定结算卡,状态为审核中");
				return "共用方已绑定有效结算卡,状态为待审核中";	
			}
			if(accountInfoGyf.getStatus() == Constants.ACCOUNT_NUM_STATRS_4){
				logger.info("共用方已绑定结算卡,状态为审核失败");
				return "共用方已绑定有效结算卡,状态为待审核失败";	
			}
		}
		
		return "OK";
	}
	
	private String checkParam(String providerId, String shareId, String constId,String productId, String bankNo){
		if(providerId == null || "".equals(providerId)){
			logger.info("提供者Id不能为空!");
			return "提供者Id不能为空!";
		}
		if(shareId == null || "".equals(shareId)){
			logger.info("共用者Id不能为空!");
			return "共用者Id不能为空!";
		}
		if(constId == null || "".equals(constId)){
			logger.info("机构码不能为空!");
			return "机构码不能为空!";
		}
		if(productId == null || "".equals(productId)){
			logger.info("产品号不能为空!");
			return "产品号Id不能为空!";
		}
		if(bankNo == null || "".equals(bankNo)){
			logger.info("共用卡号不能为空!");
			return "共用卡号不能为空!";
		}
		return "OK";
	}
}
