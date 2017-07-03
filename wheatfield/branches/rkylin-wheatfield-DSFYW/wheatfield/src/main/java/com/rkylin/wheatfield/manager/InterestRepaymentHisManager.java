/*
 * Powered By code-generator
 * Web Site: http://www.rkylin.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager;

import java.util.List;

import com.rkylin.wheatfield.pojo.InterestRepaymentHis;
import com.rkylin.wheatfield.pojo.InterestRepaymentHisQuery;

public interface InterestRepaymentHisManager {
	void saveInterestRepaymentHis(InterestRepaymentHis interestRepaymentHis);

	InterestRepaymentHis findInterestRepaymentHisById(String id);
	
	List<InterestRepaymentHis> queryList(InterestRepaymentHisQuery query);
	
	void deleteInterestRepaymentHisById(String id);
	
	void deleteInterestRepaymentHis(InterestRepaymentHisQuery query);

	int updateInterestRepaymentHis(InterestRepaymentHis interestRepaymentHis);
	
	long sumRepaidAmountByExample(InterestRepaymentHisQuery example);
}
