/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager;

import java.util.List;

import com.rkylin.wheatfield.pojo.CreditRepaymentHistory;
import com.rkylin.wheatfield.pojo.CreditRepaymentHistoryQuery;

public interface CreditRepaymentHistoryManager {
	void saveCreditRepaymentHistory(CreditRepaymentHistory creditRepaymentHistory);

	CreditRepaymentHistory findCreditRepaymentHistoryById(Long id);
	
	List<CreditRepaymentHistory> queryList(CreditRepaymentHistoryQuery query);
	
	void deleteCreditRepaymentHistoryById(Long id);
	
	void deleteCreditRepaymentHistory(CreditRepaymentHistoryQuery query);
}
