/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager;

import java.util.List;

import com.rkylin.wheatfield.pojo.CreditRepaymentMonth;
import com.rkylin.wheatfield.pojo.CreditRepaymentMonthQuery;

public interface CreditRepaymentMonthManager {
	void saveCreditRepaymentMonth(CreditRepaymentMonth creditRepaymentMonth);

	CreditRepaymentMonth findCreditRepaymentMonthById(Long id);
	
	List<CreditRepaymentMonth> queryList(CreditRepaymentMonthQuery query);
	
	void deleteCreditRepaymentMonthById(Long id);
	
	void deleteCreditRepaymentMonth(CreditRepaymentMonthQuery query);
}
