/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao;

import java.util.List;

import com.rkylin.wheatfield.pojo.CreditRepaymentMonthSummary;
import com.rkylin.wheatfield.pojo.CreditRepaymentMonthSummaryQuery;

public interface CreditRepaymentMonthSummaryDao {
	int countByExample(CreditRepaymentMonthSummaryQuery example);
	
	int deleteByExample(CreditRepaymentMonthSummaryQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(CreditRepaymentMonthSummary record);
	
	int insertSelective(CreditRepaymentMonthSummary record);
	
	List<CreditRepaymentMonthSummary> selectByExample(CreditRepaymentMonthSummaryQuery example);
	
	CreditRepaymentMonthSummary selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(CreditRepaymentMonthSummary record);
	
	int updateByPrimaryKey(CreditRepaymentMonthSummary record);
}
