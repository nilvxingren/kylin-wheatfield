/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager;

import java.util.List;
import java.util.Map;

import com.rkylin.wheatfield.pojo.InterestRepayment;
import com.rkylin.wheatfield.pojo.InterestRepaymentQuery;

public interface InterestRepaymentManager {
	void saveInterestRepayment(InterestRepayment interestRepayment);

	InterestRepayment findInterestRepaymentById(Long id);
	
	List<InterestRepayment> queryList(InterestRepaymentQuery query);
	
	List<InterestRepayment> queryListAndParam(InterestRepaymentQuery query);
	
	void deleteInterestRepaymentById(Long id);
	
	void deleteInterestRepayment(InterestRepaymentQuery query);
	
	public List<InterestRepayment> queryInterestRepaymentList(InterestRepaymentQuery query);
		
	List<InterestRepayment> queryForUpload(InterestRepaymentQuery query);
	
	int countByExample(InterestRepaymentQuery query);

	List<InterestRepayment> queryForRepayment(InterestRepaymentQuery query);

	List<InterestRepayment> queryForCapital(String rootInstCd);

	List<InterestRepayment> queryForInterest(String rootInstCd);
	
	
}
