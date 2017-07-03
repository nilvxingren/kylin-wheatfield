package com.rkylin.wheatfield.service;

import java.util.Map;

public interface InterestRepaymentHTService {

	//void repayment(String type);

	void repayment(String repayType, String rootInstCod, String productId,	String userId);

	Map<String, String> uploadIneterestRepaymentFile(String rootInstCd,	String providerId);

}
