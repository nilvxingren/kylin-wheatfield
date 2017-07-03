package com.rkylin.wheatfield.service;

import java.util.List;

import com.rkylin.wheatfield.pojo.InterestLoanMonth;

public interface InterestLoanService {

	List<InterestLoanMonth> queryLoan(String rootInstCd, String productId, String userId, String userOrderId);
	
}
