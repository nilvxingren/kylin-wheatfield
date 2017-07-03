/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager;

import java.util.List;

import com.rkylin.wheatfield.pojo.InterestLoanMonth;
import com.rkylin.wheatfield.pojo.InterestLoanMonthQuery;

public interface InterestLoanMonthManager {
	void saveInterestLoanMonth(InterestLoanMonth interestLoanMonth);

	InterestLoanMonth findInterestLoanMonthById(Long id);
	
	List<InterestLoanMonth> queryList(InterestLoanMonthQuery query);
	
	void deleteInterestLoanMonthById(Long id);
	
	void deleteInterestLoanMonth(InterestLoanMonthQuery query);
}
