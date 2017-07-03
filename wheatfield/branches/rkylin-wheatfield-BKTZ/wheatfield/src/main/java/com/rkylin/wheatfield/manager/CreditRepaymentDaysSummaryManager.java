/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager;

import java.util.List;

import com.rkylin.wheatfield.pojo.CreditRepaymentDaysSummary;
import com.rkylin.wheatfield.pojo.CreditRepaymentDaysSummaryQuery;

public interface CreditRepaymentDaysSummaryManager {
	void saveCreditRepaymentDaysSummary(CreditRepaymentDaysSummary creditRepaymentDaysSummary);

	CreditRepaymentDaysSummary findCreditRepaymentDaysSummaryById(Long id);
	
	List<CreditRepaymentDaysSummary> queryList(CreditRepaymentDaysSummaryQuery query);
	
	void deleteCreditRepaymentDaysSummaryById(Long id);
	
	void deleteCreditRepaymentDaysSummary(CreditRepaymentDaysSummaryQuery query);
}
