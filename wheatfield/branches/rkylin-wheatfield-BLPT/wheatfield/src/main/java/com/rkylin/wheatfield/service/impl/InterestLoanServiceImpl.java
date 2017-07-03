package com.rkylin.wheatfield.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.rkylin.wheatfield.manager.InterestLoanMonthManager;
import com.rkylin.wheatfield.pojo.InterestLoanMonth;
import com.rkylin.wheatfield.pojo.InterestLoanMonthQuery;
import com.rkylin.wheatfield.service.InterestLoanService;

public class InterestLoanServiceImpl implements InterestLoanService {
	
	@Autowired
	private InterestLoanMonthManager interestLoanMonthManager;
	
	@Override
	public List<InterestLoanMonth> queryLoan(String rootInstCd, String productId, String userId, String userOrderId){
		InterestLoanMonthQuery query = new InterestLoanMonthQuery();
		query.setRootInstCd(rootInstCd);
		query.setProductId(productId);
		query.setUserId(userId);
		query.setUserOrderId(userOrderId);
		return this.interestLoanMonthManager.queryList(query);
	}

}
