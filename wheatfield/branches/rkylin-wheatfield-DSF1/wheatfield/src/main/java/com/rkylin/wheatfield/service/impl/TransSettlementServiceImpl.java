package com.rkylin.wheatfield.service.impl;


import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkylin.database.BaseDao;
import com.rkylin.settle.pojo.SettleTransDetail;
import com.rkylin.settle.service.TradeService;
import com.rkylin.wheatfield.exception.AccountException;
import com.rkylin.wheatfield.pojo.TransOrderInfo;
import com.rkylin.wheatfield.service.TransSettlementService;

@Service
public class TransSettlementServiceImpl extends BaseDao  implements TransSettlementService{
	
	//@Autowired
	//TradeService tradeService;
	
	private static Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);
	
	@Override
	public void transToSettlement(TransOrderInfo transOrderInfo,String[] productIdStrings) throws AccountException{
		logger.info("向清结算系统推送交易信息————————————START————————————");
		
		Map<String,String> rtnMap = new HashMap<String, String>();
		SettleTransDetail settleTransDetail = new SettleTransDetail();
		settleTransDetail.setRequestNo(transOrderInfo.getRequestNo());
		settleTransDetail.setRequestTime(transOrderInfo.getRequestTime());
		settleTransDetail.setTransFlowNo(transOrderInfo.getTradeFlowNo());
		settleTransDetail.setOrderNo(transOrderInfo.getOrderNo());
		settleTransDetail.setOrderPackageNo(transOrderInfo.getOrderPackageNo());
		settleTransDetail.setOrderDate(transOrderInfo.getOrderDate());
		settleTransDetail.setOrderAmount(transOrderInfo.getOrderAmount());
		settleTransDetail.setOrderCount(transOrderInfo.getOrderCount());
		settleTransDetail.setOrderType(0);
		settleTransDetail.setFuncCode(transOrderInfo.getFuncCode());
		settleTransDetail.setUserId(transOrderInfo.getUserId());
		settleTransDetail.setMerchantCode(transOrderInfo.getMerchantCode());
		settleTransDetail.setInterMerchantCode(transOrderInfo.getInterMerchantCode());
		settleTransDetail.setAmount(transOrderInfo.getAmount());
		settleTransDetail.setFeeAmount(transOrderInfo.getFeeAmount());
		settleTransDetail.setUserFee(transOrderInfo.getUserFee());
		settleTransDetail.setBusinessType(transOrderInfo.getBusiTypeId());
		settleTransDetail.setPayChannelId(transOrderInfo.getPayChannelId());
		settleTransDetail.setBankCode(transOrderInfo.getBankCode());
		settleTransDetail.setUserIpAddress(transOrderInfo.getUserIpAddress());
		settleTransDetail.setErrorCode(transOrderInfo.getErrorCode());
		settleTransDetail.setErrorMsg(transOrderInfo.getErrorMsg());
		
		settleTransDetail.setDataFrom(0);
		if(productIdStrings.length==1) settleTransDetail.setProductId(productIdStrings[0]);
		if(productIdStrings.length==2) {
			settleTransDetail.setProductId(productIdStrings[0]);
			settleTransDetail.setProductWid(productIdStrings[1]);
		}
		settleTransDetail.setCancelInd(0);
		//冲正/抹帐标记都是1
		if(transOrderInfo.getStatus().equals(6)) settleTransDetail.setCancelInd(1);

		settleTransDetail.setStatusId(transOrderInfo.getStatus());
		settleTransDetail.setRemark(transOrderInfo.getRemark());
		settleTransDetail.setAccountDate(transOrderInfo.getAccountDate());
		logger.info("userid =" + settleTransDetail.getUserId() + "productId =" + settleTransDetail.getProductId() + "productWid =" + settleTransDetail.getProductWid() + "merchantCode =" + settleTransDetail.getMerchantCode());
		//rtnMap=tradeService.saveAccountTrans(settleTransDetail);
		logger.info("向清结算系统推送交易信息————————————END————————————");
		if ("0000".equals(rtnMap.get("errCode"))){
			logger.info("*****结果：errCode=" + rtnMap.get("errCode") + "errMsg=" + rtnMap.get("errMsg"));
		}else {
			logger.error("*****结果：errCode=" + rtnMap.get("errCode") + "errMsg=" + rtnMap.get("errMsg"));
			throw new AccountException(rtnMap.get("errMsg"));
		}
	}
}
