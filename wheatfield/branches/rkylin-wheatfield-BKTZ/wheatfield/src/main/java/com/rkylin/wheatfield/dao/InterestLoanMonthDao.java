/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao;

import java.util.List;

import com.rkylin.wheatfield.pojo.InterestLoanMonth;
import com.rkylin.wheatfield.pojo.InterestLoanMonthQuery;


public interface InterestLoanMonthDao {
	int countByExample(InterestLoanMonthQuery example);
	
	int deleteByExample(InterestLoanMonthQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(InterestLoanMonth record);
	
	int insertSelective(InterestLoanMonth record);
	
	List<InterestLoanMonth> selectByExample(InterestLoanMonthQuery example);
	
	InterestLoanMonth selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(InterestLoanMonth record);
	
	int updateByPrimaryKey(InterestLoanMonth record);
}
