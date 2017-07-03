package com.rkylin.wheatfield.service;

import java.util.List;

import com.rkylin.wheatfield.pojo.CreditRepaymentMonth;

public interface CreditRepaymentMonthService {
	/**
	 * 已出账单
	 * @param userId
	 * @param constId
	 * @return
	 */
	List<CreditRepaymentMonth> creditRepaymentMonthList(String userId,String constId);
	

	CreditRepaymentMonth getCreditRepaymentInfo(String userId, String constId,
			String creditDate);
}
