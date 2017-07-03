/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao;

import java.util.List;

import com.rkylin.wheatfield.pojo.InterestRepayment;
import com.rkylin.wheatfield.pojo.InterestRepaymentQuery;

public interface InterestRepaymentDao {
	int countByExample(InterestRepaymentQuery example);
	
	int deleteByExample(InterestRepaymentQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(InterestRepayment record);
	
	int insertSelective(InterestRepayment record);
	
	List<InterestRepayment> selectByExample(InterestRepaymentQuery example);
	
	InterestRepayment selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(InterestRepayment record);
	
	int updateByPrimaryKey(InterestRepayment record);
	
	public List<InterestRepayment> selectInterAndParamByExample(InterestRepaymentQuery example);
	
	public List<InterestRepayment> selectInterestRepaymentExample(InterestRepaymentQuery example);
}
