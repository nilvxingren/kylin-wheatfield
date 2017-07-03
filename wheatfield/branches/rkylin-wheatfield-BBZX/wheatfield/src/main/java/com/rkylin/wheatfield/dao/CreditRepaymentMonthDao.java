/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao;

import java.util.List;

import com.rkylin.wheatfield.pojo.CreditRepaymentMonth;
import com.rkylin.wheatfield.pojo.CreditRepaymentMonthQuery;

public interface CreditRepaymentMonthDao {
	int countByExample(CreditRepaymentMonthQuery example);
	
	int deleteByExample(CreditRepaymentMonthQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(CreditRepaymentMonth record);
	
	int insertSelective(CreditRepaymentMonth record);
	
	List<CreditRepaymentMonth> selectByExample(CreditRepaymentMonthQuery example);
	
	CreditRepaymentMonth selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(CreditRepaymentMonth record);
	
	int updateByPrimaryKey(CreditRepaymentMonth record);
}
