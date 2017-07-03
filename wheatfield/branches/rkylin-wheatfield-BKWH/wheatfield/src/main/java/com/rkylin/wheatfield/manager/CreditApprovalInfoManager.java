/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager;

import java.util.List;
import java.util.Map;

import com.rkylin.wheatfield.pojo.CreditApprovalInfo;
import com.rkylin.wheatfield.pojo.CreditApprovalInfoQuery;

public interface CreditApprovalInfoManager {
	void saveCreditApprovalInfo(CreditApprovalInfo creditApprovalInfo);

	CreditApprovalInfo findCreditApprovalInfoById(Long id);
	
	List<CreditApprovalInfo> queryList(CreditApprovalInfoQuery query);
	
	void deleteCreditApprovalInfoById(Long id);
	
	void deleteCreditApprovalInfo(CreditApprovalInfoQuery query);
	
	int updateInterestDate(Map<String,String> paraMap);
	
	List<String> validateOrderList(Map<String,String> paraMap);
	
	long sumAmountForSqsm();
}
