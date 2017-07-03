package com.rkylin.wheatfield.service;

import com.rkylin.wheatfield.exception.AccountException;
import com.rkylin.wheatfield.pojo.TransOrderInfo;

public interface TransSettlementService {
	/**
	 * 推送交易信息
	 * @param transOrderInfo 交易订单
	 * @return 
	 * */
	public void transToSettlement(TransOrderInfo transOrderInfo,String[] productIdStrings) throws AccountException;

}
