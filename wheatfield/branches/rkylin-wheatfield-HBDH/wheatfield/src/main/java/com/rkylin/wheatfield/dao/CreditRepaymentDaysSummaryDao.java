/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao;

import java.util.List;

import com.rkylin.wheatfield.pojo.CreditRepaymentDaysSummary;
import com.rkylin.wheatfield.pojo.CreditRepaymentDaysSummaryQuery;

public interface CreditRepaymentDaysSummaryDao {
	int countByExample(CreditRepaymentDaysSummaryQuery example);
	
	int deleteByExample(CreditRepaymentDaysSummaryQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(CreditRepaymentDaysSummary record);
	
	int insertSelective(CreditRepaymentDaysSummary record);
	
	List<CreditRepaymentDaysSummary> selectByExample(CreditRepaymentDaysSummaryQuery example);
	
	CreditRepaymentDaysSummary selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(CreditRepaymentDaysSummary record);
	
	int updateByPrimaryKey(CreditRepaymentDaysSummary record);
}
