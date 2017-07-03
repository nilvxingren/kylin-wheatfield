/**
 * @File name : BankAccountUpdateServiceImpl.java
 * @Package : com.rkylin.wheatfield.service.impl
 * @Description : TODO(银行账户信息完善)
 * @Creator : liuhuan
 * @CreateTime : 2015年12月4日 下午3:20:39
 * @Version : 1.0
 */
package com.rkylin.wheatfield.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rkylin.wheatfield.common.DateUtils;
import com.rkylin.wheatfield.common.ValHasNoParam;
import com.rkylin.wheatfield.constant.BaseConstants;
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.constant.ErrorCodeConstants;
import com.rkylin.wheatfield.enumtype.UserTypeEnum;
import com.rkylin.wheatfield.manager.AccountInfoManager;
import com.rkylin.wheatfield.pojo.AccountInfo;
import com.rkylin.wheatfield.pojo.AccountInfoQuery;
import com.rkylin.wheatfield.pojo.FinanaceAccount;
import com.rkylin.wheatfield.pojo.User;
import com.rkylin.wheatfield.response.AccountInfoGetResponse;
import com.rkylin.wheatfield.response.Response;
import com.rkylin.wheatfield.service.AccountManageService;
import com.rkylin.wheatfield.service.BankAccountUpdateService;
import com.rkylin.wheatfield.service.IAPIService;
import com.rkylin.wheatfield.service.IErrorResponseService;
@Service("ruixue.wheatfield.bankaccount.infoupdate")
public class BankAccountUpdateServiceImpl implements IAPIService,BankAccountUpdateService {
	private static Logger logger = LoggerFactory.getLogger(BankAccountUpdateServiceImpl.class);
	private static Object lock=new Object();
	@Autowired
	IErrorResponseService errorResponseService;
	@Autowired
	AccountManageService accountManageService;
	@Autowired
	AccountInfoManager accountInfoManager;
	
	@Override
	public Response doJob(Map<String, String[]> paramMap, String methodName) {
		if (!ValHasNoParam.hasParam(paramMap, "userid")) {
			return errorResponseService.getErrorResponse("P1", "userid不能为空");
		}
		if (!ValHasNoParam.hasParam(paramMap, "usertype")) {
			return errorResponseService.getErrorResponse("P2", "usertype不能为空");
		}
		if (!ValHasNoParam.hasParam(paramMap, "productid")) {
			return errorResponseService.getErrorResponse("P4", "productid不能为空");
		}
		if (!ValHasNoParam.hasParam(paramMap, "constid")) {
			return errorResponseService.getErrorResponse("P3", "constid不能为空");
		}
		if (!ValHasNoParam.hasParam(paramMap, "accountnumber")) {
			return errorResponseService.getErrorResponse("P5", "accountnumber不能为空");
		}
		AccountInfoGetResponse response = new AccountInfoGetResponse();
		AccountInfo accountInfo = new AccountInfo();
		User user = new User();
		for (Object keyObj : paramMap.keySet().toArray()) {
			String[] strs = paramMap.get(keyObj);
			for (String value : strs) {
				if (keyObj.equals("userid")) {
					user.userId = value;
				} else if (keyObj.equals("usertype")) {
					user.userType = UserTypeEnum.toEnum(value);
				} else if (keyObj.equals("constid")) {
					user.constId = value;
				} else if (keyObj.equals("productid")) {
					user.productId = value;
				} else if (keyObj.equals("role")) {
					user.role = value;
				} else if (keyObj.equals("referuserid")) {
					user.referUserId = value;
				}else if ("accountnumber".equals(keyObj)) {
					accountInfo.setAccountNumber(value);
				}else if ("bankbranchname".equals(keyObj)) {
					accountInfo.setBankBranchName(value);
				}else if ("openaccountdate".equals(keyObj)) {
					try {
						Date date = DateUtils.getDate(value,Constants.DATE_FORMAT_YYYYMMDD);
						accountInfo.setOpenAccountDate(date);
					} catch (Exception e) {
						e.printStackTrace();
						return errorResponseService.getErrorResponse(ErrorCodeConstants.SYS_ERROR_CODE_M1);
					}
				}else if ("openaccountdescription".equals(keyObj)&& value != null && !"".equals(value)) {
					accountInfo.setOpenAccountDescription(value);
				}else if("bank_branch".equals(keyObj)){
					accountInfo.setBankBranch(value);
				}else if("bank_province".equals(keyObj)){
					accountInfo.setBankProvince(value);
				}else if("bank_city".equals(keyObj)){
					accountInfo.setBankCity(value);
				}
			
			}
		}
		String msg = this.updateAccountInfo(user, accountInfo);
		if ("ok".equals(msg)) {
			response.setIs_success(true);
		} else {
			return errorResponseService.getErrorResponse("C1", msg);
		}
		return response;
	}

	
	@Transactional
	private String updateAccountInfo(User user, AccountInfo accountInfo) {
		logger.info("-----修改卡信息start-----");
		logger.info("--用户ID--:"+user.userId+"--机构号--："+user.constId+"--产品号--："+user.productId+"--用户名--"+user.userName+"第三方用户ID"+user.referUserId);
		logger.info("--卡号--:" + accountInfo.getAccountNumber());
		synchronized (lock) {
			try {
				List<FinanaceAccount> faList = accountManageService.getAllAccount(user, "");
				if (faList == null || faList.isEmpty()){
					logger.info("暂无此账户！");
					return "暂无此账户！";
				}
				FinanaceAccount finanaceAccount = faList.get(0);
				if (!BaseConstants.ACCOUNT_STATUS_OK.equals(finanaceAccount.getStatusId())) {
					logger.info("账户已经关闭或冻结！");
					return "账户已经关闭或冻结！";
				}
				AccountInfoQuery accQuery = new AccountInfoQuery();
				accQuery.setAccountNumber(accountInfo.getAccountNumber());
				accQuery.setAccountName(user.userId);
				accQuery.setFinAccountId(finanaceAccount.getFinAccountId());
				accQuery.setStatus(Constants.ACCOUNT_NUM_STATRS_1);
				List<AccountInfo> aList = accountInfoManager.queryList(accQuery);
				if(aList == null || aList.isEmpty()){
					logger.info("银行卡不存在！");
					return "银行卡不存在！";
				}
				AccountInfo existAccountInfo = aList.get(0);
				existAccountInfo.setBankBranchName(accountInfo.getBankBranchName());
				existAccountInfo.setOpenAccountDate(accountInfo.getOpenAccountDate());
				existAccountInfo.setOpenAccountDescription(accountInfo.getOpenAccountDescription());
				existAccountInfo.setBankBranch(accountInfo.getBankBranch());
				existAccountInfo.setBankProvince(accountInfo.getBankProvince());
				existAccountInfo.setBankCity(accountInfo.getBankCity());
				accountInfoManager.saveAccountInfo(existAccountInfo);
			}catch (Exception e) {
				logger.error("更改用户目的失败!"+e.getMessage());
				return "更改用户目的失败!";
			}
			return "ok";
		}
	}
}
