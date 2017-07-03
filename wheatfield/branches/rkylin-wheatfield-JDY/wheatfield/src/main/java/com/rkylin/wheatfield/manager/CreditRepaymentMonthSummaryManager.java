/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager;

import java.util.List;

import com.rkylin.wheatfield.pojo.CreditRepaymentMonthSummary;
import com.rkylin.wheatfield.pojo.CreditRepaymentMonthSummaryQuery;

public interface CreditRepaymentMonthSummaryManager {
	void saveCreditRepaymentMonthSummary(CreditRepaymentMonthSummary creditRepaymentMonthSummary);

	CreditRepaymentMonthSummary findCreditRepaymentMonthSummaryById(Long id);
	
	List<CreditRepaymentMonthSummary> queryList(CreditRepaymentMonthSummaryQuery query);
	
	void deleteCreditRepaymentMonthSummaryById(Long id);
	
	void deleteCreditRepaymentMonthSummary(CreditRepaymentMonthSummaryQuery query);
}
