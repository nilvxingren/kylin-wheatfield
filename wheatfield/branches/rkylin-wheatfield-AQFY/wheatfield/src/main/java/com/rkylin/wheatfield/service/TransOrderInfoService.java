package com.rkylin.wheatfield.service;

import java.util.List;

import com.rkylin.wheatfield.pojo.TransOrderInfo;

public interface TransOrderInfoService {
	/**
	 * 账单流水
	 * @param userId
	 * @param constId
	 * @param creditDate
	 * @return
	 */
	List<TransOrderInfo> getTransOrderInfos(String userId,String constId ,String creditDate);
}
