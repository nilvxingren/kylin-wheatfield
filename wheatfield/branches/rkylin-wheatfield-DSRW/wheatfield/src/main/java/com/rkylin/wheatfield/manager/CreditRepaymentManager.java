/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager;

import java.util.List;

import com.rkylin.wheatfield.pojo.CreditRepayment;
import com.rkylin.wheatfield.pojo.CreditRepaymentQuery;

public interface CreditRepaymentManager {
	void saveCreditRepayment(CreditRepayment creditRepayment);

	CreditRepayment findCreditRepaymentById(Long id);
	
	List<CreditRepayment> queryList(CreditRepaymentQuery query);
	
	void deleteCreditRepaymentById(Long id);
	
	void deleteCreditRepayment(CreditRepaymentQuery query);
}
