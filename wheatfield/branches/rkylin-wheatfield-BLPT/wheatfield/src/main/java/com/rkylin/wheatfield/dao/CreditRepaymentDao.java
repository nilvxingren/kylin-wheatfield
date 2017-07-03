/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao;

import java.util.List;

import com.rkylin.wheatfield.pojo.CreditRepayment;
import com.rkylin.wheatfield.pojo.CreditRepaymentQuery;

public interface CreditRepaymentDao {
	int countByExample(CreditRepaymentQuery example);
	
	int deleteByExample(CreditRepaymentQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(CreditRepayment record);
	
	int insertSelective(CreditRepayment record);
	
	List<CreditRepayment> selectByExample(CreditRepaymentQuery example);
	
	CreditRepayment selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(CreditRepayment record);
	
	int updateByPrimaryKey(CreditRepayment record);
}
