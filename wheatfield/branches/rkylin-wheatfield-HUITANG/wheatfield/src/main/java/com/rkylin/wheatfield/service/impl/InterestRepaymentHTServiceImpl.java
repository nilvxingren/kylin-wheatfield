package com.rkylin.wheatfield.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Rop.api.ApiException;
import com.Rop.api.DefaultRopClient;
import com.Rop.api.RopResponse;
import com.Rop.api.request.WheatfieldOrderOperRequest;
import com.Rop.api.request.WheatfieldOrderServiceAuthcodeserviceRequest;
import com.Rop.api.request.WheatfieldOrderServiceCollectionRequest;
import com.Rop.api.request.WheatfieldOrderServiceThawauthcodeRequest;
import com.Rop.api.request.WheatfieldOrderTransferRequest;
import com.Rop.api.response.WheatfieldOrderOperResponse;
import com.Rop.api.response.WheatfieldOrderServiceAuthcodeserviceResponse;
import com.Rop.api.response.WheatfieldOrderServiceCollectionResponse;
import com.Rop.api.response.WheatfieldOrderServiceThawauthcodeResponse;
import com.Rop.api.response.WheatfieldOrderTransferResponse;
import com.rkylin.common.RedisIdGenerator;
import com.rkylin.file.txt.TxtWriter;
import com.rkylin.order.mixservice.InterestToOrderService;
import com.rkylin.order.pojo.OrderItem;
import com.rkylin.order.pojo.OrderItemQuery;
import com.rkylin.order.service.OrderItemService;
import com.rkylin.utils.DateUtil;
import com.rkylin.wheatfield.common.PartyCodeUtil;
import com.rkylin.wheatfield.common.SessionUtils;
import com.rkylin.wheatfield.common.UploadAndDownLoadUtils;
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.constant.SettleConstants;
import com.rkylin.wheatfield.manager.CreditApprovalInfoManager;
import com.rkylin.wheatfield.manager.FinanaceAccountManager;
import com.rkylin.wheatfield.manager.InterestRepaymentHisManager;
import com.rkylin.wheatfield.manager.InterestRepaymentManager;
import com.rkylin.wheatfield.manager.ParameterInfoManager;
import com.rkylin.wheatfield.pojo.CreditApprovalInfo;
import com.rkylin.wheatfield.pojo.CreditApprovalInfoQuery;
import com.rkylin.wheatfield.pojo.FinanaceAccount;
import com.rkylin.wheatfield.pojo.FinanaceAccountQuery;
import com.rkylin.wheatfield.pojo.InterestRepayment;
import com.rkylin.wheatfield.pojo.InterestRepaymentHisQuery;
import com.rkylin.wheatfield.pojo.InterestRepaymentQuery;
import com.rkylin.wheatfield.pojo.ParameterInfoQuery;
import com.rkylin.wheatfield.pojo.TransferInfo;
import com.rkylin.wheatfield.service.InterestRepaymentHTService;
import com.rkylin.wheatfield.settlement.SettlementLogic;
import com.rkylin.wheatfield.utils.JsonUtil;
import com.rkylin.wheatfield.utils.MailUtil;
import com.rkylin.wheatfield.utils.SettlementUtils;

/**
 * 计息的还款业务类
 * 
 * @author mswang
 * 
 */
@Service("interestRepaymentHTService")
public class InterestRepaymentHTServiceImpl implements InterestRepaymentHTService {

	private static Logger logger = LoggerFactory.getLogger(InterestRepaymentHTServiceImpl.class);

	@Autowired
	private InterestRepaymentManager interestRepaymentManager;
	@Autowired
	private FinanaceAccountManager finanaceAccountManager;
	@Autowired
	private Properties userProperties;
	@Autowired
	private SettlementLogic settlementLogic;
	@Autowired
	private CreditApprovalInfoManager creditApprovalInfoManager;
	@Autowired
	private InterestToOrderService interestToOrderService;
	@Autowired
	ParameterInfoManager parameterInfoManager;
	@Autowired
	private RedisIdGenerator redisIdGenerator;
	@Autowired
	private OrderItemService orderItemService;
	@Autowired
	private InterestRepaymentHisManager interestRepaymentHisManager;

	/**
	 * 会唐还款还款业务
	 * 
	 * @return
	 */

	@Override
	public void repayment(String repayType, String rootInstCod, String productId, String userId) {
		//查询本金还款记录进行还款
		this.repaymentsForCapital(rootInstCod);
		
		//查询利息还款记录进行还款
		this.repaymentsForInterest(rootInstCod);
	}
	
	/**
	 * 查询本金还款记录，进行还款
	 * @param rootInstCod
	 */
	public void repaymentsForCapital(String rootInstCod){
		//查询本金还款金额 
		List<InterestRepayment> listCapital = interestRepaymentManager.queryForCapital(rootInstCod);
		if(listCapital != null && listCapital.size() > 0){
			logger.info("===============共有 "+ listCapital.size()+" 条记录进行本金还款====================");
			for(InterestRepayment repayment : listCapital){
				//本金还款
				try{
					this.repayForCapital(repayment);
				}catch(Exception e){
					logger.debug("===================会唐本金还款发生异常  InterId:"+repayment.getInterId()+"============================"+e.getMessage());
					this.sendMail("会唐本金还款发生异常", "InterId:"+repayment.getInterId()+"  错误原因："+e.getMessage());
				}
			}
		}else{
			logger.info("====================本次没有本金还款记录================");
		}
	}
	
	/**
	 * 查询利息还款记录进行还款
	 * @param rootInstCod
	 */
	public void repaymentsForInterest(String rootInstCod){
		//查询利息还款记录
		List<InterestRepayment> listInterest = interestRepaymentManager.queryForInterest(rootInstCod);
		if(listInterest != null && listInterest.size() >= 0){
			logger.info("=================此次共有 "+ listInterest.size() +" 条记录进行利息还款==============");
			for(InterestRepayment repayment : listInterest){
				// 利息还款
				try{
					this.repayForInterest(repayment);
				}catch(Exception e){
					logger.info("===================会唐利息还款发生异常  InterId:"+repayment.getInterId()+"============================"+e.getMessage());
					this.sendMail("会唐利息还款发生异常", "InterId:"+repayment.getInterId()+"  错误原因："+e.getMessage());
				}
			}
		}else{
			logger.info("============================本次没有利息还款记录====================================");
		}
		
	}
	
	/**
	 * 本金还款 新进行本金还款，在更新还款状态和授信额度
	 * @param repayment
	 */
	public void repayForCapital(InterestRepayment repayment){
		boolean flag = this.repayForCapitalDetail(repayment);
		if(flag){
			repayment.setIsSucCapital(1);
			if(2 == repayment.getInterestParty()){
				repayment.setIsSucInterest(1);
			}
		}else{
			repayment.setIsSucCapital(2);
			//设置为逾期
			if(null != repayment.getShouldCapital() && repayment.getShouldCapital() != 0){
				repayment.setOverdueFlag2(1);
			}
			if(2 == repayment.getInterestParty()){
				repayment.setIsSucInterest(2);
			}
		}
		this.updateReaymentStatus(repayment);
		if(flag){
			try{
				//尝试更新授信额度
				tryToRecoverAmount(repayment);
			}catch(Exception e){
				logger.info("==========================InterId:"+repayment.getInterId()+"   恢复授信额度发生异常   "+e.getMessage()+"   ======================================");
				this.sendMail("恢复授信额度发生异常，请及时处理", "InterId:"+repayment.getInterId()+"  错误原因："+e.getMessage());
			}
			
		}
	}
	
	/**
	 * 利息还款，先进行转账，再更新利息还款状态
	 * @param repayment
	 */
	public void repayForInterest(InterestRepayment repayment){
		TransferInfo ts = transferForInterest(repayment);
		 if(ts.isSuccess()){
			 repayment.setIsSucInterest(1);
		 }else{
			 repayment.setIsSucInterest(2);
		 }
		 this.updateReaymentStatus(repayment);
	}
	
	
	/**
	 * 本金还款，新进行贷款余额还款，再进行主账户还款
	 * @param repayment
	 */
	public boolean  repayForCapitalDetail(InterestRepayment repayment){
		long amount = 0;
		if(2 == repayment.getInterestParty()){
			//利息由用户承担,取实际应还金额
			amount = repayment.getRepaidAmount();
		}else if(1 == repayment.getInterestParty()){
			// 应还本金+逾期罚金+逾期利息
			amount = repayment.getShouldCapital()+repayment.getOverdueFine()+repayment.getOverdueInterest();
		}
		if(amount <= 0){
			//没有本金需要还款, 修改还款状态为成功
			logger.info("==================InterId:"+repayment.getInterId()+" 没有本金需要还款, 修改还款状态为成功==========================");
	    	return true;
		}
		//用户主账户金额
		long amountFa = queryfinanaceAmount(repayment.getRootInstCd(), repayment.getUserId(), repayment.getProductId());
		logger.info("==================InterId:"+repayment.getInterId()+" 主账户金额为："+amountFa+"==========================");
		//用户贷款剩余金额
		long amountLoan = 0;
		if(null != repayment.getShouldCapital() && repayment.getShouldCapital() != 0){
			try{
				amountLoan = this.queryResidualLoan(repayment.getOrderId());
			}catch(Exception e){
				logger.info("==================InterId:"+repayment.getInterId()+"获取贷款余额发生异常   错误信息"+e.getMessage()+"==========================");
				this.sendMail("获取贷款余额发生异常,将不进行贷款余额还款", "InterId:"+repayment.getInterId()+"  错误原因："+e.getMessage());
			}
		}
		
		logger.info("==================InterId:"+repayment.getInterId()+" 用户贷款剩余金额为："+amountLoan+"==========================");
		if(amountFa + amountLoan < amount){
			logger.info("==================InterId:"+repayment.getInterId()+"主账户金额不足==========================");
			//设置还款状态为失败
			return false;
		}
		//使用贷款余额转账还款
		if(amountLoan > amount){
			amountLoan = amount;
		}
		TransferInfo tsLoan = transferFromCreditAccount(repayment, amountLoan);
		if(tsLoan.isSuccess() && amountLoan > 0 ){
			amount = amount - amountLoan;
		}
		// 本金还款
		TransferInfo tsCt = this.transferForCapital(repayment, amount);
	    if(tsCt.isSuccess()){
	    	// 修改还款状态为成功
			return true;
	    }else{
	    	//设置还款状态为失败
	    	if(tsLoan.isSuccess() && amountLoan > 0){
	    		//如果贷款余额还款成功，但是本金还款失败，则进行贷款余额还款的反冲处理
	    		reverseTransfer(tsLoan, repayment);
	    	}
	    	return false;
	    }
		
	}

	/**
	 *本金转账
	 */
	public TransferInfo transferForCapital(InterestRepayment repayment, long amount) {
		TransferInfo transferInfo = new TransferInfo();
		if(amount <= 0){
			transferInfo.setSuccess(true);
			return transferInfo;
		}
		logger.info("==================InterId:"+repayment.getInterId()+" 开始本金还款==========================");
		String itemId = null;
		try{
			//itemId = createOrderItemForCapital(repayment);
			itemId = createOrderItem(amount, repayment.getOrderId(),String.valueOf(repayment.getInterId()),"本金还款");
		}catch(Exception e){
			logger.info("==========================InterId:"+repayment.getInterId()+"本金还款落单发生异常 "+e.getMessage()+"=============================");
			this.sendMail("会唐本金还款，调用订单落单接口发生异常", "InterId:"+repayment.getInterId()+"  错误原因："+e.getMessage());
		}
		if (null == itemId) {
			logger.info("==================InterId:"+repayment.getInterId()+" 还款订单落单失败，设置还款状态为失败=======================");
			return transferInfo;
		}
		// 应还金额转账
		String productFa = this.queryFinanaceProduct(repayment.getRootInstCd(), repayment.getUserId());
		if(productFa == null){
			logger.info("==================InterId:"+repayment.getInterId()+" 没有找到主账户的product=======================");
			return transferInfo;
		}
		transferInfo.setAmount(amount);
		transferInfo.setTransferType("2");
		transferInfo.setConditionCode("1");
		transferInfo.setInterMerchantCode(Constants.HT_ID);//会唐机构码
		transferInfo.setInterProductId(Constants.HT_GATHERING_PRODUCT);//会唐保理账户
		transferInfo.setMerchantCode(repayment.getRootInstCd());
		//transferInfo.setProductId(repayment.getProductId());
		transferInfo.setProductId(productFa);
		transferInfo.setRequestNo(itemId);
		transferInfo.setRequestTime(new Date());
		transferInfo.setUserfee(0);
		transferInfo.setUserId(repayment.getUserId());
		transferInfo.setUserRelateId(Constants.HT_GATHERING_USERID);//会唐保理用户ID
		WheatfieldOrderTransferResponse restrans = null;
		try{
			restrans = this.transfer(transferInfo);
		}catch(Exception e){
			logger.info(e.getMessage());
			this.sendMail("会唐本金还款，调用订单转账接口发生异常", "InterId:"+repayment.getInterId()+"  错误原因："+e.getMessage());
		}
		if (restrans != null && "true".equals(restrans.getIs_success())) {
			logger.info("==================InterId:"+repayment.getInterId()+" 转账成功==============================");
			try{
				// 更新订单状态
				this.updateItemSusscess(itemId);
			}catch(Exception e){
				logger.info("==================InterId:"+repayment.getInterId()+" 本金还款成功，但是更新订单状态失败  "+e.getMessage()+" =======================");
				this.sendMail("本金还款成功，但是更新订单状态发生异常", "InterId:"+repayment.getInterId()+"  错误原因："+e.getMessage());
			}
			transferInfo.setSuccess(true);
		} else {
			logger.info("==================InterId:"+repayment.getInterId()+" 转账失败，设置还款状态为失败=======================");
			try{
				this.updateItemFail(itemId);
			}catch(Exception e){
				logger.info("==================InterId:"+repayment.getInterId()+" 本金还款失败，但是更新订单状态失败  "+e.getMessage()+" =======================");
				this.sendMail("本金还款失败，但是更新订单状态发生异常", "InterId:"+repayment.getInterId()+"  错误原因："+e.getMessage());
			}
		}
		return transferInfo;
	}
	
	/**
	 * 贷款余额转账，从授信子账户
	 */
	public TransferInfo transferFromCreditAccount(InterestRepayment repayment, long amountLoan) {
		TransferInfo transferInfo = new TransferInfo();
		if(amountLoan <= 0){
			transferInfo.setSuccess(true);
        	return transferInfo;
        }
        //查询贷款剩余金额
        logger.info("==================InterId:"+repayment.getInterId()+" 剩余贷款金额为："+amountLoan+"==========================");
        
        logger.info("==================InterId:"+repayment.getInterId()+" 开始使用贷款余额还款=============================");
        String itemId = null;
        try{
        	itemId = createOrderItem(amountLoan, repayment.getOrderId(), String.valueOf(repayment.getInterId()),"贷款余额还款");
        }catch(Exception e){
        	logger.info("==================InterId:"+repayment.getInterId()+" 会唐贷款余额还款,调用订单落单接发错异常 "+e.getMessage()+"=======================");
        	this.sendMail("会唐贷款余额还款，调用订单落单接口发生异常", "InterId:"+repayment.getInterId()+"  错误原因："+e.getMessage());
        }
        if (null == itemId) {
			logger.info("==================InterId:"+repayment.getInterId()+" 还款订单落单失败=======================");
			this.updateToFalse(repayment);
			return transferInfo;
		}
        CreditApprovalInfoQuery crquery = new CreditApprovalInfoQuery();
        crquery.setRootInstCd(repayment.getRootInstCd());
        crquery.setProviderId(repayment.getProviderId());
        crquery.setUserId(repayment.getUserId());
		List<CreditApprovalInfo> list = this.creditApprovalInfoManager.queryList(crquery);
		if (list == null || list.size() == 0) {
			// 没有找到授信信息
			logger.info("==================InterId:"+repayment.getInterId()+" 没有找到授信信息=====================================");
			return transferInfo;
		}
		if (list.size() > 1) {
			// 授信结果不唯一
			logger.info("==================InterId:"+repayment.getInterId()+" 授信结果不唯一，异常返回===================================");
			return transferInfo;
		}
		CreditApprovalInfo creditApprovalInfo = list.get(0);
		// 应还金额转账
		transferInfo.setAmount(amountLoan);
		transferInfo.setTransferType("2");
		transferInfo.setConditionCode("1");
		transferInfo.setInterMerchantCode(Constants.HT_ID);//会唐机构码
		transferInfo.setInterProductId(Constants.HT_GATHERING_PRODUCT);//会唐保理账户
		transferInfo.setMerchantCode(repayment.getRootInstCd());
		transferInfo.setProductId(creditApprovalInfo.getProductId());
		transferInfo.setRequestNo(itemId);
		transferInfo.setRequestTime(new Date());
		transferInfo.setUserfee(0);
		transferInfo.setUserId(repayment.getUserId());
		transferInfo.setUserRelateId(Constants.HT_GATHERING_USERID);//会唐保理用户
		
		WheatfieldOrderTransferResponse restrans = null;
		try{
		    restrans = this.transfer(transferInfo);
		}catch(Exception e){
			logger.info(e.getMessage());
			this.sendMail("会唐使用贷款余额还款，调用订单转账接口发生异常", "InterId:"+repayment.getInterId()+"  错误原因："+e.getMessage());
		}
		if (restrans != null && "true".equals(restrans.getIs_success())) {
			logger.info("==================InterId:"+repayment.getInterId()+" 转账成功==============================");
			try{
				// 更新订单状态
				this.updateItemSusscess(itemId);
			}catch(Exception e){
				logger.info("==================InterId:"+repayment.getInterId()+" 贷款余额还款成功，但是更新订单状态失败  "+e.getMessage()+" =======================");
				this.sendMail("贷款余额还款成功，但是更新订单状态发生异常", "InterId:"+repayment.getInterId()+"  错误原因："+e.getMessage());
			}
			try{
				this.updateItemResidualLoan(repayment.getOrderId(), amountLoan);
			}catch(Exception e){
				logger.info("==================InterId:"+repayment.getInterId()+" 本金还款成功，但是更新订单剩余贷款额度失败  "+e.getMessage()+" =======================");
				this.sendMail("贷款余额还款成功，但是更新订单剩余贷款额度发生异常", "InterId:"+repayment.getInterId()+"  错误原因："+e.getMessage());
			}
			transferInfo.setSuccess(true);
		} else {
			logger.info("==================InterId:"+repayment.getInterId()+" 转账失败，设置还款状态为失败=======================");
			try{
				this.updateItemFail(itemId);
			}catch(Exception e){
				logger.info("==================InterId:"+repayment.getInterId()+" 本金还款失败，但是更新订单状态失败  "+e.getMessage()+" =======================");
				this.sendMail("贷款余额还款失败，但是更新订单状态发生异常", "InterId:"+repayment.getInterId()+"  错误原因："+e.getMessage());
			}
			
		}
		logger.info("==================InterId:"+repayment.getInterId()+" 贷款余额还款结束=============================");
		return transferInfo;

	}
	

	/**
	 * 通过转账还款--还利息
	 * 
	 * @param repayments
	 */
	public TransferInfo transferForInterest(InterestRepayment repayment) {
		TransferInfo transferInfo = new TransferInfo();
		logger.info("==================InterId:"+repayment.getInterId()+" 开始利息还款=========================");
		long amount = repayment.getShouldInterest();
		if(amount <= 0){
			logger.info("==================InterId:"+repayment.getInterId()+" 没有利息，退出利息还款=======================");
			transferInfo.setSuccess(true);
			return transferInfo;
		}
		String itemId = null;
		try{
			//itemId = createOrderItemForInterest(repayment);
			itemId = createOrderItem(amount, repayment.getOrderId(), String.valueOf(repayment.getInterId()),"利息还款");
		}catch(Exception e){
			logger.info("==================InterId:"+repayment.getInterId()+" 会唐利息还款,调用订单落单接发错异常 "+e.getMessage()+"=======================");
        	this.sendMail("会唐利息还款，调用订单落单接口发生异常", "InterId:"+repayment.getInterId()+"  错误原因："+e.getMessage());
		}
		if (null == itemId) {
			logger.info("==================InterId:"+repayment.getInterId()+" 还款订单落单失败，设置还款状态为失败=======================");
			return transferInfo;
		}
		String merchantCode = null;
		String productId = null;
		String userId = null;
		if (1 == repayment.getInterestParty()) {
			// 商户承担利息
			merchantCode = Constants.HT_ID;
			productId = Constants.HT_PRODUCT;
			userId = Constants.HT_USERID;
		} else if (2 == repayment.getInterestParty()) {
			// 用户承担利息
			merchantCode = repayment.getRootInstCd();
			//productId = repayment.getProductId();
			productId = Constants.HT_PRODUCT;
			userId = repayment.getUserId();
		} else {
			// 错误的客户承担方
			logger.info("==================InterId:"+repayment.getInterId()+" 利息承担方错误无法进行转账=========");
			return transferInfo;
		}
		long amountfa = queryfinanaceAmount(merchantCode, userId, productId);
		if (amountfa < amount) {
			// 金额不足，不能发起转账
			logger.info("===================利息承担方主账户金额不足不能发起转账 InterId:" + repayment.getInterId()+"=======================");
			return transferInfo;
		}
		// 应还金额转账
		transferInfo.setAmount(amount);
		transferInfo.setTransferType("3");
		transferInfo.setConditionCode("1");
		transferInfo.setInterMerchantCode(Constants.HT_ID);//会唐机构码
		transferInfo.setInterProductId(Constants.HT_GATHERING_PRODUCT);//会唐保理收款账户
		transferInfo.setMerchantCode(merchantCode);
		transferInfo.setProductId(productId);
		transferInfo.setRequestNo(itemId);
		transferInfo.setRequestTime(new Date());
		transferInfo.setUserfee(0);
		transferInfo.setUserId(userId);
		transferInfo.setUserRelateId(Constants.HT_GATHERING_USERID);//会唐保理收款用户
		WheatfieldOrderTransferResponse restrans = null;
		try{
			restrans = this.transfer(transferInfo);
		}catch(Exception e){
			logger.info(e.getMessage());
			this.sendMail("会唐利息还款，调用订单转账接口发生异常", "InterId:"+repayment.getInterId()+"  错误原因："+e.getMessage());
		}
		if (restrans != null && "true".equals(restrans.getIs_success())) {
			logger.info("======================InterId:"+repayment.getInterId()+" 转账成功==============================");
			try{
				// 更新订单状态
				this.updateItemSusscess(itemId);
			}catch(Exception e){
				logger.info("==================InterId:"+repayment.getInterId()+" 利息还款成功，但是更新订单状态失败  "+e.getMessage()+" =======================");
				this.sendMail("利息还款成功，但是更新订单状态发生异常", "InterId:"+repayment.getInterId()+"  错误原因："+e.getMessage());
			}
			transferInfo.setSuccess(true);
		} else {
			logger.info("=====================InterId:"+repayment.getInterId()+" 转账失败=======================");
			try{
				this.updateItemFail(itemId);
			}catch(Exception e){
				logger.info("==================InterId:"+repayment.getInterId()+" 利息还款失败，但是更新订单状态失败  "+e.getMessage()+" =======================");
				this.sendMail("利息还款失败，但是更新订单状态发生异常", "InterId:"+repayment.getInterId()+"  错误原因："+e.getMessage());
			}
		}
		this.interestRepaymentManager.saveInterestRepayment(repayment);
		logger.info("=====================InterId:"+repayment.getInterId()+" 利息还款结束=======================");
		return transferInfo;
	}
	
	/**
	 *贷款余额的冲正处理
	 * @param transferInfo
	 * @param repayment
	 */
	public TransferInfo  reverseTransfer(TransferInfo transferInfo, InterestRepayment repayment){
		logger.info("==================InterId:"+repayment.getInterId()+" 开始冲正=======================");
		TransferInfo transferInfoNew = new TransferInfo();
		String itemId = null;
		try{
			itemId = createOrderItem(transferInfo.getAmount(), repayment.getOrderId(), String.valueOf(repayment.getInterId()),"贷款余额冲正");
		}catch(Exception e){
			logger.info("==================InterId:"+repayment.getInterId()+" 会唐还款冲正,调用订单落单接发错异常 "+e.getMessage()+"=======================");
        	this.sendMail("会唐还款冲正，调用订单落单接口发生异常", "InterId:"+repayment.getInterId()+"  错误原因："+e.getMessage());
		}
		if (null == itemId) {
			logger.info("==================InterId:"+repayment.getInterId()+" 冲正落单失败=======================");
			return transferInfoNew;
		}
		transferInfoNew.setAmount(transferInfo.getAmount());
		transferInfoNew.setTransferType(transferInfo.getTransferType());
		transferInfoNew.setConditionCode(transferInfo.getConditionCode());
		transferInfoNew.setInterMerchantCode(transferInfo.getMerchantCode());
		transferInfoNew.setInterProductId(transferInfo.getProductId());
		transferInfoNew.setMerchantCode(transferInfo.getInterMerchantCode());
		transferInfoNew.setProductId(transferInfo.getInterProductId());
		transferInfoNew.setRequestNo(itemId);
		transferInfoNew.setRequestTime(new Date());
		transferInfoNew.setUserfee(0);
		transferInfoNew.setUserId(transferInfo.getUserRelateId());
		transferInfoNew.setUserRelateId(transferInfo.getUserId());
		WheatfieldOrderTransferResponse restrans = null;
		try{
			restrans = this.transfer(transferInfoNew);
		}catch(Exception e){
			logger.info(e.getMessage());
			this.sendMail("转账冲正，调用订单转账接口发生异常", "InterId:"+repayment.getInterId()+"  错误原因："+e.getMessage());
		}
		if (restrans != null && "true".equals(restrans.getIs_success())) {
			logger.info("======================InterId:"+repayment.getInterId()+" 冲正转账成功==============================");
			try{
				// 更新订单状态
				this.updateItemSusscess(itemId);
			}catch(Exception e){
				logger.info("==================InterId:"+repayment.getInterId()+" 本金还款成功，但是更新订单状态失败  "+e.getMessage()+" =======================");
				this.sendMail("冲正转账成功，但是更新订单状态发生异常", "InterId:"+repayment.getInterId()+"  错误原因："+e.getMessage());
			}
			try{
				this.recoverItemResidualLoan(repayment.getOrderId(), transferInfo.getAmount());
			}catch(Exception e){
				logger.info("==================InterId:"+repayment.getInterId()+" 冲正成功，但是更新订单剩余贷款额度失败  "+e.getMessage()+" =======================");
				this.sendMail("冲正成功，但是更新订单剩余贷款额度发生异常", "InterId:"+repayment.getInterId()+"  错误原因："+e.getMessage());
			}
			transferInfo.setSuccess(true);
			
		} else {
			logger.info("=====================InterId:"+repayment.getInterId()+" 冲正转账转账失败=======================");
			try{
				this.updateItemFail(itemId);
			}catch(Exception e){
				logger.info("==================InterId:"+repayment.getInterId()+" 本金还款失败，但是更新订单状态失败  "+e.getMessage()+" =======================");
				this.sendMail("冲正转账失败，但是更新订单状态发生异常", "InterId:"+repayment.getInterId()+"  错误原因："+e.getMessage());
			}
		}
		return transferInfoNew;
	}

	/**
	 * 通过代收还款
	 */
	public void repaymentByCollection(List<InterestRepayment> repayments) {
		for (InterestRepayment repayment : repayments) {
			WheatfieldOrderOperResponse resorder = createOrder(repayment);
			if (!"true".equals(resorder.getIs_success())) {
				logger.info("=====================还款订单落单失败，设置还款状态为失败=======================");
				this.updateToFalse(repayment);
				continue;
			}
			String orderId = resorder.getOrderid();
			long amountfa = queryfinanaceAmount(repayment.getRootInstCd(),
					repayment.getUserId(), repayment.getProductId());
			WheatfieldOrderServiceAuthcodeserviceResponse responsefr = frozen(
					repayment, amountfa);
			if ("true".equals(responsefr.getIs_success())
					&& null != responsefr.getAuthcode()
					&& !"".equals(responsefr.getAuthcode())) {
				logger.info("=====================冻结成功，保存授权码===============================");
				repayment.setAuthCode(responsefr.getAuthcode());
				interestRepaymentManager.saveInterestRepayment(repayment);
			} else {
				// 账户冻结失败
				logger.info("=====================冻结账户资金失败，设置还款状态为失败=======================");
				this.updateToFalse(repayment);
				continue;
			}
			// 冻结成功,发起代收
			logger.info("=========================冻结成功，发起转账=======================");
			RopResponse response = this.collect(repayment,
					repayment.getRepaidAmount() - amountfa, orderId);
			if (response.isSuccess()) {
				// 调用代收成功,状态设置为扣款中
				logger.info("=========================调用代收成功,状态设置为扣款中=======================");
				this.updateToHanding(repayment);
			} else {
				// 调用代收失败，状态设置为扣款失败,逾期状态设置为逾期
				logger.info("=========================调用代收失败，状态设置为扣款失败=======================");
				this.updateToFalse(repayment);
			}
		}
	}

	/**
	 * 转账
	 * 
	 * @param repayment
	 * @return
	 */
	public WheatfieldOrderTransferResponse transfer(TransferInfo transferInfo) {
		logger.info("======================开始转账==============================");
		WheatfieldOrderTransferRequest request = new WheatfieldOrderTransferRequest();
		request.setTransfertype(transferInfo.getTransferType());
		request.setConditioncode(transferInfo.getConditionCode());
		request.setMerchantcode(transferInfo.getMerchantCode());// 发生方商户码
		request.setProductid(transferInfo.getProductId());// 发生方产品号
		request.setUserid(transferInfo.getUserId());// 发生方用户id
		request.setRequestno(transferInfo.getRequestNo()); // 请求号
		request.setRequesttime(DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", transferInfo.getRequestTime()));
		request.setAmount(String.valueOf(transferInfo.getAmount()));
		request.setUserfee(String.valueOf(transferInfo.getUserfee()));
		request.setIntermerchantcode(transferInfo.getInterMerchantCode());
		// request.setIntermerchantcode(Constants.JRD_ID);//接收方机构号码，这里是君融贷
		request.setInterproductid(transferInfo.getInterProductId()); // 接收方产品ID
		// request.setInterproductid(Constants.JRD_PRODUCT); //接收方产品ID
		request.setUserrelateid(transferInfo.getUserRelateId());
		// request.setUserrelateid("141223100000013"); //接收方用户ID;
		
		DefaultRopClient ropClient = new DefaultRopClient(
				userProperties.getProperty("INVOKE_ROP_URL"),
				userProperties.getProperty("INVOKE_APP_KEY"),
				userProperties.getProperty("INVOKE_APP_SECRET"), "xml");

		WheatfieldOrderTransferResponse response = null;
		try {
			response = ropClient.execute(request, SessionUtils.sessionGet(
					userProperties.getProperty("INVOKE_ROP_URL"),
					userProperties.getProperty("INVOKE_APP_KEY"),
					userProperties.getProperty("INVOKE_APP_SECRET")));
		} catch (ApiException e) {
			//RkylinMailUtil.sendMail("转账失败", JsonUtil.bean2Json(transferInfo), new String[]{"wangmingsheng@rkylin.com.cn","huzijian@rkylin.com.cn","sunruibin@rkylin.com.cn","wanghongliang@rkylin.com.cn"});
			e.printStackTrace();
			logger.info(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
		logger.info("===================================调用转账完成："
				+ JsonUtil.bean2Json(response)
				+ "=======================================");
		return response;
	}

	/**
	 * 利息 转账
	 * 
	 * @param repayment
	 * @return
	 */
	public WheatfieldOrderTransferResponse transferForIns(InterestRepayment repayment, String orderId) {
		logger.info("======================开始转账==============================");
		WheatfieldOrderTransferRequest request = new WheatfieldOrderTransferRequest();
		request.setTransfertype("2");
		request.setConditioncode("1");
		request.setMerchantcode("");// 发生方商户码
		request.setProductid("");// 发生方产品号
		request.setUserid("");// 发生方用户id
		request.setRequestno(orderId); // 请求号
		request.setRequesttime(DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss",new Date()));
		request.setAmount(String.valueOf(repayment.getShouldInterest()));
		request.setUserfee("0");
		request.setIntermerchantcode(Constants.JRD_ID);// 接收方机构号码，这里是君融贷
		request.setInterproductid(Constants.JRD_PRODUCT); // 接收方产品ID
		request.setUserrelateid(Constants.JRD_USERID); // 接收方用户ID;

		DefaultRopClient ropClient = new DefaultRopClient(
				userProperties.getProperty("INVOKE_ROP_URL"),
				userProperties.getProperty("INVOKE_APP_KEY"),
				userProperties.getProperty("INVOKE_APP_SECRET"), "xml");

		WheatfieldOrderTransferResponse response = null;
		try {
			response = ropClient.execute(request, SessionUtils.sessionGet(
					userProperties.getProperty("INVOKE_ROP_URL"),
					userProperties.getProperty("INVOKE_APP_KEY"),
					userProperties.getProperty("INVOKE_APP_SECRET")));
		} catch (ApiException e) {
			e.printStackTrace();
			logger.info(e.getMessage());
		}
		logger.info("===================================调用转账完成："
				+ JsonUtil.bean2Json(response)
				+ "=======================================");
		return response;
	}

	/**
	 * 代收业务
	 * 
	 * @param repayment
	 * @param colamount
	 * @return
	 */
	public WheatfieldOrderServiceCollectionResponse collect(
			InterestRepayment repayment, long colamount, String orderId) {
		logger.info("=============================开始代收========================");
		DefaultRopClient ropClient = new DefaultRopClient(
				userProperties.getProperty("INVOKE_ROP_URL"),
				userProperties.getProperty("INVOKE_APP_KEY"),
				userProperties.getProperty("INVOKE_APP_SECRET"), "xml");
		WheatfieldOrderServiceCollectionRequest request = new WheatfieldOrderServiceCollectionRequest();
		request.setUserid(repayment.getUserId());
		request.setMerchantcode(repayment.getRootInstCd());
		request.setProductid(repayment.getProductId());
		request.setRequestno(orderId);// 落单的订单号
		request.setRequesttime(DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss",
				new Date()));
		request.setConditioncode("2");
		request.setAmount(String.valueOf(colamount));
		request.setUserfee("0");
		request.setFunccode("4013");

		WheatfieldOrderServiceCollectionResponse response = null;
		try {
			response = ropClient.execute(request, SessionUtils.sessionGet(
					userProperties.getProperty("INVOKE_ROP_URL"),
					userProperties.getProperty("INVOKE_APP_KEY"),
					userProperties.getProperty("INVOKE_APP_SECRET")));
		} catch (ApiException e) {
			e.printStackTrace();
			logger.info(e.getMessage());
		}
		logger.info("调用订单代收结束：" + JsonUtil.bean2Json(response)
				+ "================================");
		return response;
	}

	/**
	 * 账户资金冻结
	 * 
	 * @param repayment
	 * @param amount
	 * @return
	 */
	public WheatfieldOrderServiceAuthcodeserviceResponse frozen(
			InterestRepayment repayment, long amount) {
		logger.info("=========================开始账户资金冻结=========================");
		DefaultRopClient ropClient = new DefaultRopClient(
				userProperties.getProperty("INVOKE_ROP_URL"),
				userProperties.getProperty("INVOKE_APP_KEY"),
				userProperties.getProperty("INVOKE_APP_SECRET"), "xml");
		WheatfieldOrderServiceAuthcodeserviceRequest request = new WheatfieldOrderServiceAuthcodeserviceRequest();
		request.setAmount(amount);
		request.setUserid(repayment.getUserId());
		request.setFunccode("40171");
		request.setMerchantcode(repayment.getRootInstCd());
		request.setOrderamount(amount);
		request.setOrdercount(1);
		request.setOrderdate(new Date());
		request.setOrderno(repayment.getOrderId());
		request.setRemark("还款时主账户金额不足，发起代收前的冻结");
		request.setRequesttime(new Date());
		request.setStatus(1);
		request.setUserfee(0L);
		request.setProductid(repayment.getProductId());
		request.setRequestno(repayment.getOrderId());
		request.setUseripaddress("127.0.0.1");
		WheatfieldOrderServiceAuthcodeserviceResponse response = null;
		try {
			response = ropClient.execute(request, SessionUtils.sessionGet(
					userProperties.getProperty("INVOKE_ROP_URL"),
					userProperties.getProperty("INVOKE_APP_KEY"),
					userProperties.getProperty("INVOKE_APP_SECRET")));
		} catch (ApiException e) {
			e.printStackTrace();
			logger.info(e.getMessage());
		}
		logger.info("===================冻结账户完成:" + JsonUtil.bean2Json(response)
				+ "==================================");
		return response;
	}

	/**
	 * 查询主账户金额
	 * 
	 * @param rootInstCd
	 * @param userId
	 * @param productId
	 * @return
	 */
	public long queryfinanaceAmount(String rootInstCd, String userId, String productId) {
		FinanaceAccountQuery faQuery = new FinanaceAccountQuery();
		faQuery.setRootInstCd(rootInstCd);
		faQuery.setAccountRelateId(userId);
		faQuery.setFinAccountTypeId("10001");
		//faQuery.setGroupManage(productId);
		List<FinanaceAccount> list = finanaceAccountManager.queryList(faQuery);
		if (list == null || list.size() == 0) {
			// 没有找到主账户
			logger.info("=====================没有找到主账户=======================");
			throw new RuntimeException("没有找到主账户");
		}
		if (list.size() > 1) {
			// 主账户不唯一
			logger.info("========================主账户不唯一=========================");
			throw new RuntimeException("主账户不唯一");
		}
		long amount = list.get(0).getAmount();
		return amount;
	}
	
	public String queryFinanaceProduct(String rootInstCd, String userId){
		FinanaceAccountQuery faQuery = new FinanaceAccountQuery();
		faQuery.setRootInstCd(rootInstCd);
		faQuery.setAccountRelateId(userId);
		faQuery.setFinAccountTypeId("10001");
		List<FinanaceAccount> list = finanaceAccountManager.queryList(faQuery);
		if (list == null || list.size() == 0) {
			// 没有找到主账户
			logger.info("=====================没有找到主账户=======================");
			return null;
		}
		if (list.size() > 1) {
			// 主账户不唯一
			logger.info("========================主账户不唯一=========================");
			return null;
		}
		return list.get(0).getGroupManage();
	}

	/**
	 * 还款信息在订单系统中落单
	 * 
	 * @return
	 */
	public WheatfieldOrderOperResponse createOrder(InterestRepayment repayment) {
		logger.info("===================开始落单====================");
		WheatfieldOrderOperRequest request = new WheatfieldOrderOperRequest();
		request.setUserid(repayment.getUserId());
		request.setConstid(repayment.getRootInstCd());
		request.setOrdertypeid("M20007");
		request.setProductid(repayment.getProductId());
		request.setOpertype("1");// 新增操作
		request.setOrderdate(new Date());
		request.setOrdertime(new Date());
		request.setGoodsname(String.valueOf(repayment.getInterId()));
		request.setUserorderid(repayment.getUserOrderId());
		request.setAmount(String.valueOf(repayment.getShouldAmount()));
		request.setRemark("还款订单");

		DefaultRopClient ropClient = new DefaultRopClient(
				userProperties.getProperty("INVOKE_ROP_URL"),
				userProperties.getProperty("INVOKE_APP_KEY"),
				userProperties.getProperty("INVOKE_APP_SECRET"), "xml");

		WheatfieldOrderOperResponse response = null;
		try {
			response = ropClient.execute(request, SessionUtils.sessionGet(
					userProperties.getProperty("INVOKE_ROP_URL"),
					userProperties.getProperty("INVOKE_APP_KEY"),
					userProperties.getProperty("INVOKE_APP_SECRET")));
		} catch (ApiException e) {
			e.printStackTrace();
			logger.info(e.getMessage());
		}
		if (response.isSuccess()) {
			logger.info("====================落单成功，订单号：" + response.getOrderid()
					+ "=================");
		} else {
			logger.info("===========================落单失败，msg:"
					+ response.getMsg() + ",code:" + response.getErrorCode()
					+ "===================================");
		}
		return response;
	}

	/**
	 * 订单代收完成后的通知接口
	 */
	public void collectCompletedNotify(Map<String, String> rm) {
		logger.info("=============================开始调用代付完成通知接口, 入参为："
				+ JsonUtil.bean2Json(rm)
				+ "==========================================");
		String loanRequestNo = rm.get("loanrequestno");
		String orderId = rm.get("orderId");
		InterestRepaymentQuery query = new InterestRepaymentQuery();
		query.setOrderId(loanRequestNo);
		query.setStatusId(0);
		List<InterestRepayment> list = interestRepaymentManager
				.queryList(query);
		if (list == null || list.size() == 0) {
			// 没有找到还款信息
			logger.info("==========================没有找到还款信息=====================");
			return;
		}
		if (list.size() > 1) {
			// 还款信息不唯一
			logger.info("==================================还款信息不唯一,无法确定信息======================");
			return;
		}
		InterestRepayment repayment = list.get(0);
		// 解冻
		WheatfieldOrderServiceThawauthcodeResponse resufro = unfrozen(repayment);
		if (!resufro.isSuccess()) {
			// 解冻失败
			logger.info("==============================解冻失败=============================");
			repayment.setStatusId(2);
			repayment.setOverdueFlag2(1);
			repayment.setOverdueDays(repayment.getOverdueDays() + 1);
			interestRepaymentManager.saveInterestRepayment(repayment);
			return;
		}
		TransferInfo transferInfo = new TransferInfo();
		transferInfo.setAmount(repayment.getShouldAmount());
		transferInfo.setTransferType("2");
		transferInfo.setConditionCode("1");
		transferInfo.setInterMerchantCode(Constants.JRD_ID);
		transferInfo.setInterProductId(Constants.JRD_PRODUCT);
		transferInfo.setMerchantCode(repayment.getRootInstCd());
		transferInfo.setProductId(repayment.getProductId());
		transferInfo.setRequestNo(orderId);
		transferInfo.setRequestTime(new Date());
		transferInfo.setUserfee(0);
		transferInfo.setUserId(repayment.getUserId());
		transferInfo.setUserRelateId(Constants.JRD_USERID);
		WheatfieldOrderTransferResponse restran = transfer(transferInfo);
		if (!restran.isSuccess()) {
			// 转账失败
			logger.info("================================转账失败===========================");
			repayment.setStatusId(2);
			repayment.setOverdueFlag2(1);
			repayment.setOverdueDays(repayment.getOverdueDays() + 1);
			interestRepaymentManager.saveInterestRepayment(repayment);
			return;
		}
		// 转账完成
		repayment.setStatusId(1);
		interestRepaymentManager.saveInterestRepayment(repayment);
		this.updateOrderStatus(orderId, "1");
	}

	/**
	 * 解冻接口
	 */
	public WheatfieldOrderServiceThawauthcodeResponse unfrozen(
			InterestRepayment repayment) {
		WheatfieldOrderServiceThawauthcodeRequest request = new WheatfieldOrderServiceThawauthcodeRequest();
		request.setUserid(repayment.getUserId());
		request.setRootinstcd(repayment.getRootInstCd());
		request.setProductid(repayment.getProductId());
		request.setAuthcode(repayment.getAuthCode());// 授权码
		request.setFrozenuserorderid(repayment.getOrderId());
		request.setAmount(String.valueOf(repayment.getShouldAmount()));
		request.setRequesttime(DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss",
				new Date()));
		request.setRequestno(repayment.getOrderId());
		request.setConditioncode("2");

		DefaultRopClient ropClient = new DefaultRopClient(
				userProperties.getProperty("INVOKE_ROP_URL"),
				userProperties.getProperty("INVOKE_APP_KEY"),
				userProperties.getProperty("INVOKE_APP_SECRET"), "xml");
		WheatfieldOrderServiceThawauthcodeResponse response = null;
		try {
			response = ropClient.execute(request, SessionUtils.sessionGet(
					userProperties.getProperty("INVOKE_ROP_URL"),
					userProperties.getProperty("INVOKE_APP_KEY"),
					userProperties.getProperty("INVOKE_APP_SECRET")));
		} catch (ApiException e) {
			e.printStackTrace();
			logger.info(e.getMessage());
		}
		logger.info("===================调用订单解冻接口结束："
				+ JsonUtil.bean2Json(response)
				+ "=================================");
		return response;
	}

	/**
	 * 修改还款状态为失败
	 */
	public void updateToFalse(InterestRepayment repayment) {
		repayment.setStatusId(2);
		// 设置本金还款失败
		repayment.setIsSucCapital(2);
		//setOverdueDays(repayment);
		interestRepaymentManager.saveInterestRepayment(repayment);
	}

	/**
	 * 修改为支付中
	 * 
	 * @param repayment
	 */
	public void updateToHanding(InterestRepayment repayment) {
		repayment.setStatusId(3);
		//setOverdueDays(repayment);
		interestRepaymentManager.saveInterestRepayment(repayment);
	}

	/**
	 * 修改为支付成功
	 * 
	 * @param repayment
	 */
	public void updateToSuccess(InterestRepayment repayment) {
		repayment.setStatusId(1);
		//setOverdueDays(repayment);
		repayment.setRepaidRepaymentDate(new Date());
		// 设置实际还款金额，目前是金额不足不允许还款
		repayment.setRepaidAmount(repayment.getShouldAmount());
		// 设置本金扣款成功
		repayment.setIsSucCapital(1);
		interestRepaymentManager.saveInterestRepayment(repayment);
	}

	/**
	 * 设置逾期参数
	 * 
	 * @param repayment
	 */
	public void setOverdueDays(InterestRepayment repayment) {
		Calendar cal = Calendar.getInstance();
		int dayofmonth = cal.get(Calendar.DAY_OF_MONTH);
		int overdueDays = dayofmonth - 16;
		if (overdueDays > 0) {
			repayment.setOverdueFlag2(1);
		} else {
			repayment.setOverdueFlag2(0);
		}
		//repayment.setOverdueDays(overdueDays);
	}

	@Override
	public Map<String, String> uploadIneterestRepaymentFile(String rootInstCd,	String providerId) {
		logger.info("进入   __________创建扣款信息文件并上传服务器___________" + rootInstCd);
		HashMap<String, String> rtnMap = new HashMap<String, String>();
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String accountDate = ""; // 账期
		String accountDate1 = "";
		List<InterestRepayment> interestRepaymentes = null; // 扣款信息
		SettlementUtils settlementUtils = null; // 清结算工具类
		String fileName = ""; // 上传文件名称
		String rootInstCdStr = "";
		// 实例化
		InterestRepaymentQuery query = new InterestRepaymentQuery();
		ParameterInfoQuery keyList = new ParameterInfoQuery();
		settlementUtils = new SettlementUtils();
		//Date createdTime = null;
		try {
			// 查询账期的条件
			keyList.setParameterCode(SettleConstants.ACCOUNTDATE);
			// 查询当前账期
			accountDate = settlementUtils.getAccountDate(parameterInfoManager.queryList(keyList).get(0).getParameterValue(),"yyyy-MM-dd", -1);
			accountDate1 = accountDate.replace("-", "");
			//createdTime = formatter.parse(accountDate);

			logger.info("获取账期成功, 账期信息 : " + accountDate);
		} catch (Exception e) {
			logger.error("计算账期异常！" + e.getMessage());
			rtnMap.put("errMsg", "账期获取失败");
			rtnMap.put("errCode", "P1");
			return rtnMap;
		}

		if (rootInstCd == null || "".equals(rootInstCd.trim())) {// 判断参数不为空
			rtnMap.put("errMsg", "传递的'管理机构代码'不正确");
			rtnMap.put("errCode", "P2");
			return rtnMap;
		} else {
			logger.info("获取管理机构代码成功:" + rootInstCd);
		}
		// 封装扣款的查询条件
		query.setRootInstCd(rootInstCd);
		query.setProviderId(providerId);
		// query.setCreatedTime(createdTime);
		// 查询扣款信息
		interestRepaymentes = interestRepaymentManager.queryForUpload(query);

		if (interestRepaymentes == null || interestRepaymentes.size() <= 0) {
			rtnMap.put("errMsg", "获取到0条扣款信息");
			rtnMap.put("errCode", "P3");
			logger.info("获取到0条扣款信息");
			return rtnMap;
		} else {
			logger.info("获取 " + interestRepaymentes.size() + " 条扣款信息");
		}

		// 获取商户编号
		rootInstCdStr = interestRepaymentes.get(0).getRootInstCd();
		// 文件名称生成策略: IR:InterestRepayment缩写; rootInstCdStr:商户编号; accountDate:账期;
		// 0~100随机数; 文件后缀
		fileName = "IR_" + rootInstCdStr + "_" + accountDate1 + "_"
				+ PartyCodeUtil.getRandomCode() + "."
				+ SettleConstants.FILE_CSV;
		// 测试信息
		logger.info("文件名称 : " + fileName);

		// 开始生成文件
		logger.info(">>> 开始生成文件");
		List<Map<String, Object>> txtList = new ArrayList<Map<String, Object>>();

		Map<String, Object> paraMap = null;
		Map<String, String> configMap = new HashMap<String, String>();

		for (InterestRepayment ir : interestRepaymentes) {
			paraMap = new HashMap<String, Object>();
			int tdCount = 1;
			// 还款账期
			accountDate = accountDate == null ? "" : accountDate;
			paraMap.put("F_" + (tdCount++), accountDate);
			// 还款时间
			paraMap.put("F_" + (tdCount++),	ir.getRepaidRepaymentDate() == null ? "" : formatter1.format(ir.getRepaidRepaymentDate()));
			// 账户交易流水号
			paraMap.put("F_" + (tdCount++), ir.getInterId());
			// 机构码
			paraMap.put("F_" + (tdCount++), ir.getProviderId());
			// 商户号
			paraMap.put("F_" + (tdCount++), ir.getRootInstCd());
			// 商户用户
			paraMap.put("F_" + (tdCount++), ir.getUserId());
			// 账户贷款订单号
			paraMap.put("F_" + (tdCount++), ir.getOrderId());
			// 商户贷款订单号
			paraMap.put("F_" + (tdCount++), ir.getUserOrderId());
			// 还款总期数
			paraMap.put("F_" + (tdCount++), ir.getPeriodSummary());
			// 当前期数
			paraMap.put("F_" + (tdCount++), ir.getPeriodCurrent());
			// 应还日期
			paraMap.put("F_" + (tdCount++),	ir.getShouldRepaymentDate() == null ? "" : formatter.format(ir.getShouldRepaymentDate()));

			// 应还本金
			paraMap.put("F_" + (tdCount++),	((double) ir.getShouldCapital()) / 100);
			// 应还利息
			paraMap.put("F_" + (tdCount++),	((double) ir.getShouldInterest()) / 100);
			// 减免利息
			paraMap.put("F_" + (tdCount++),	((double) ir.getInterestFree()) / 100);
			// 应还金额
			paraMap.put("F_" + (tdCount++),	((double) ir.getShouldAmount()) / 100);

			// 逾期天数
			paraMap.put("F_" + (tdCount++), ir.getOverdueDays());
			// 逾期罚金
			paraMap.put("F_" + (tdCount++),	((double) ir.getOverdueFine()) / 100);
			// 逾期利息
			paraMap.put("F_" + (tdCount++),	((double) ir.getOverdueInterest()) / 100);
			// 提前还款申请订单号
			paraMap.put("F_" + (tdCount++), ir.getOverdueOrderId());
			// 提前还款本金
			paraMap.put("F_" + (tdCount++), ir.getOrderType() != 2 ? ""	: ((double) ir.getAdvanceAmount()) / 100);
			// 提前还款利息
			paraMap.put("F_" + (tdCount++), ir.getOrderType() != 2 ? ""	: ((double) ir.getAdvanceInterest()) / 100);
			// 剩余应还本金
			paraMap.put("F_" + (tdCount++),	((double) ir.getOverplusAmount()) / 100);

			// 实际还款金额
			paraMap.put("F_" + (tdCount++),	((double) ir.getRepaidAmount()) / 100);
			// 扣款结果
			paraMap.put("F_" + (tdCount++), ir.getStatusId());
			// 还款状态1
			paraMap.put("F_" + (tdCount++), ir.getStatusId());
			// 还款状态2
			paraMap.put("F_" + (tdCount++), ir.getOverdueFlag2());
			// 本金还款状态
			paraMap.put("F_" + (tdCount++), ir.getIsSucCapital());
			// 利息还款状态
			paraMap.put("F_" + (tdCount++), ir.getIsSucInterest());
			// 扩展字段1
			paraMap.put("F_" + (tdCount++), "");
			// 扩展字段2
			paraMap.put("F_" + (tdCount++), "");
			
			// 测试信息
			logger.info("文件内容  >>> >>> >>> " + paraMap);
			txtList.add(paraMap);
		}

		String path = SettleConstants.FILE_UP_PATH + accountDate1;

		File filePath = new File(path);
		if (!filePath.exists()) {
			filePath.mkdirs();
		}

		// 文件却对路径
		path = path + File.separator + fileName;
		configMap.put("FILE", path);

		String Key = userProperties.getProperty("P2P_PUBLIC_KEY");
		try {
			TxtWriter.WriteTxt(txtList, configMap, SettleConstants.DEDT_SPLIT2, "UTF-8"); // SettleConstants.DEDT_SPLIT2

			UploadAndDownLoadUtils.uploadFile(path, // 文件上传路径 String path
					4, // 文件类别 int type
					formatter2.parse(accountDate1), // 账期 格式 (yyyyMMdd) Date
													// invaiceDate
					settlementLogic.getBatchNo(formatter2.parse(accountDate1),
							SettleConstants.ROP_REPAYMENT_BATCH_CODE,
							rootInstCd), // 批次号 正在建设中 ... ... String batch
					SettleConstants.FILE_XML, // 不明 String jsonOrXml
					Key, // 不明 String priOrPubKey <- 他是null
					0, // 不明 int flg
					userProperties.getProperty("FSAPP_KEY"), // 不明 String appKey
					userProperties.getProperty("FSDAPP_SECRET"), // 不明 String
																	// appSecret
					userProperties.getProperty("FSROP_URL") // 不明 String ropUrl
					);
			logger.info("成功!生成还款明细文件");
		} catch (Exception e) {
			rtnMap.put("errMsg", "生成还款明细文件操作异常！");
			rtnMap.put("errCode", "P4");
			logger.error("生成还款明细文件操作异常！" + e.getMessage());
			return rtnMap;
		}
		rtnMap.put("errMsg", "还款信息文件,上传成功!");
		rtnMap.put("errCode", "P0");
		return rtnMap;
	}

	/**
	 * 恢复信用额度
	 */
	public void tryToRecoverAmount(InterestRepayment repayment) {
		if (!repayment.getPeriodCurrent().equals(repayment.getPeriodSummary())) {
			// 当前期数不等于总期数，不予恢复授信额度
			return;
		}
		String rootInstCd = repayment.getRootInstCd();
		String productId = repayment.getProductId();
		String providerId = repayment.getProviderId();
		String userId = repayment.getUserId();
		String orderId = repayment.getOrderId();
		InterestRepaymentQuery query = new InterestRepaymentQuery();
		query.setRootInstCd(rootInstCd);
		query.setProductId(productId);
		query.setProviderId(providerId);
		query.setUserId(userId);
		query.setOrderId(orderId);
		query.setIsSucCapital(2);
		int count = interestRepaymentManager.countByExample(query);
		if (count > 0) {
			logger.info("==================订单 " + orderId + " 有本金还款失败的还款记录,数据异常，不能恢复授信额度");
		}
		CreditApprovalInfoQuery crquery = new CreditApprovalInfoQuery();
		crquery.setRootInstCd(rootInstCd);
		//query.setProductId(productId);
		crquery.setProviderId(providerId);
		crquery.setUserId(userId);
		List<CreditApprovalInfo> list = this.creditApprovalInfoManager.queryList(crquery);
		if (list == null || list.size() == 0) {
			// 没有找到授信信息
			logger.info("==============================没有找到授信信息=====================================");
			return;
		}
		if (list.size() > 1) {
			// 授信结果不唯一
			logger.info("=======================授信结果不唯一，异常返回===================================");
			return;
		}
		CreditApprovalInfo credit = list.get(0);
		if (101 != credit.getCreditTypeId()) {
			// 不是额度授信，返回
			logger.info("==============================不是额度授信，不予恢复授信额度============================");
			return;
		}
		logger.info("========================creditid:"+credit.getCreditId()+" 恢复前的可用信用额度为："+credit.getExpansion6()+"==========================================");
		InterestRepaymentHisQuery queryHis = new InterestRepaymentHisQuery();
		queryHis.setRootInstCd(rootInstCd);
		queryHis.setProductId(productId);
		queryHis.setProviderId(providerId);
		queryHis.setUserId(userId);
		queryHis.setOrderId(orderId);
		long amount = interestRepaymentHisManager.sumRepaidAmountByExample(queryHis);
		amount = amount + repayment.getShouldCapital();
		String expansion6 = credit.getExpansion6();
		if(expansion6 != null && !"".equals(expansion6)){
			amount = Long.parseLong(credit.getExpansion6())+amount;
		}
		credit.setExpansion6(String.valueOf(amount));
		this.creditApprovalInfoManager.saveCreditApprovalInfo(credit);
		logger.info("============================creditid:"+credit.getCreditId()+" 更新授信额度完成,恢复后的授信额度为："+credit.getExpansion6()+"==========================");
	}

	/**
	 * 更新订单状态 通过dubbo实现
	 * 
	 * @param orderId
	 * @param status
	 */
	public void updateOrderStatus(String orderId, String status) {
		this.interestToOrderService.updateOrderStatus(orderId, status);
	}
	
	/**
	 * 本金还款落单，还款信息落单在订单的order_item表中，防止失败不能再次发起代收的情况
	 * 通过dubbo调用订单
	 * @param repayment
	 */
	public String createOrderItemForCapital_del(InterestRepayment repayment){
		OrderItem item = new OrderItem();
		item.setAmount(repayment.getShouldAmount());
		item.setCreatedTime(new Date());
		item.setFeeAmount(0l);
		item.setFeeUser(0l);
		item.setOrderId(repayment.getUserOrderId());
		item.setOrderCount(1);
		item.setOrderItemId(redisIdGenerator.createOrderItemId());
		item.setOrderItemTypeId("M20006");
		item.setStartTime(new Date());
		item.setStatusId("8");
		String itemId = orderItemService.saveOrderItem(item);
		
		return itemId;
	}
	
	/**
	 * 利息还款落单，还款信息落单在订单的order_item表中，防止失败不能再次发起代收的情况
	 * 通过dubbo调用订单
	 * @param repayment
	 */
	public String createOrderItemForInterest_del(InterestRepayment repayment){
		OrderItem item = new OrderItem();
		item.setAmount(repayment.getShouldInterest());
		item.setCreatedTime(new Date());
		item.setFeeAmount(0l);
		item.setFeeUser(0l);
		item.setOrderId(repayment.getUserOrderId());
		item.setOrderCount(1);
		item.setOrderItemId(redisIdGenerator.createOrderItemId());
		item.setOrderItemTypeId("M20006");
		item.setStartTime(new Date());
		item.setStatusId("8");
		String itemId = orderItemService.saveOrderItem(item);
		return itemId;
	}
	
	public String createOrderItem(long amount, String orderId, String interId, String remark){
		OrderItem item = new OrderItem();
		item.setAmount(amount);
		item.setCreatedTime(new Date());
		item.setFeeAmount(0l);
		item.setFeeUser(0l);
		item.setOrderId(orderId);
		item.setOrderCount(1);
		item.setOrderItemId(redisIdGenerator.createOrderItemId());
		item.setOrderItemTypeId("M20006");
		item.setStartTime(new Date());
		item.setStatusId("8");
		item.setGoodsName(interId);
		item.setGoodsDetail(remark);
		String itemId = orderItemService.saveOrderItem(item);
		return itemId;
	}
	/**
	 * 查询剩余的贷款数,从放款订单中找
	 * @param orderItemId
	 * @return -1表示没有找到贷款
	 */
	public long queryResidualLoan(String orderId){
		OrderItem item = this.queryOrderIntem(orderId);
		if(item == null){
			return 0;
		}
		long amount = item.getFeeAmount();//获取批贷金额
		return amount;
	}
	
	public OrderItem  queryOrderIntem(String orderId){
		OrderItemQuery query = new OrderItemQuery();
		query.setOrderId(orderId);
		query.setOrderItemTypeId("M20005");
		List<OrderItem> list = orderItemService.queryList(query);
		if(list == null ||list.size() == 0){
			logger.info("=========================没有找放款订单============================");
			//throw new RuntimeException("没有找放款订单");
			return null;
		}
		if(list.size() > 1){
			logger.info("========================放款订单不唯一=========================");
			//throw new RuntimeException("放款订单不唯一");
			return null;
		}
		return list.get(0);
	}
	
	/**
	 * 修改贷款剩余额度;
	 * @param orderItemId
	 */
	public void updateItemResidualLoan(String orderId,long amount){
		OrderItem item = this.queryOrderIntem(orderId);
		item.setFeeAmount(item.getFeeAmount() - amount);
		orderItemService.updateItem(item);
	}
	
	/**
	 * 恢复贷款剩余额度
	 * @param orderId
	 * @param amount
	 */
	public void recoverItemResidualLoan(String orderId,long amount){
		OrderItem item = this.queryOrderIntem(orderId);
		item.setFeeAmount(item.getFeeAmount() + amount);
		orderItemService.updateItem(item);
	}
	
	/**
	 * 更新订单为成功状态
	 * @param itemId
	 */
	public void updateItemSusscess(String itemId){
		OrderItem item = new OrderItem();
		item.setOrderItemId (itemId);
		item.setStatusId("1");
		orderItemService.updateItem(item);
	}
	/**
	 * 跟新订单为失败状态
	 * @param itemId
	 */
	public void updateItemFail(String itemId){
		OrderItem item = new OrderItem();
		item.setOrderItemId(itemId);
		item.setStatusId("0");
		orderItemService.updateItem(item);
	}
	
	/**
	 * 更新还款状态
	 * @param repayment
	 */
	public void updateReaymentStatus(InterestRepayment repayment){
		if(1 == repayment.getIsSucCapital() && 1 == repayment.getIsSucInterest()){
			//本金和利息都是成功状态，修改status为成功状态
			repayment.setStatusId(1);
		}
		if(2 == repayment.getIsSucCapital() || 2 == repayment.getIsSucInterest()){
			//本金和利息中有一个是失败状态，修改status为失败状态
			repayment.setStatusId(2);
		}
		
		this.interestRepaymentManager.saveInterestRepayment(repayment);
	}
	
	public void sendMail(String subject,String content){
		MailUtil mail = new MailUtil();
		mail.send(subject, content);
	}

}