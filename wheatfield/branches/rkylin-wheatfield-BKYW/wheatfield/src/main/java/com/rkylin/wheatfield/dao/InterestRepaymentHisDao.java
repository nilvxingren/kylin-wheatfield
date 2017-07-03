/*
 * Powered By code-generator
 * Web Site: http://www.rkylin.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao;

import java.util.List;

import com.rkylin.wheatfield.pojo.InterestRepaymentHis;
import com.rkylin.wheatfield.pojo.InterestRepaymentHisQuery;

public interface InterestRepaymentHisDao {
	int countByExample(InterestRepaymentHisQuery example);
	
	int deleteByExample(InterestRepaymentHisQuery example);
	
	int deleteByPrimaryKey(String id);
	
	int insert(InterestRepaymentHis record);
	
	int insertSelective(InterestRepaymentHis record);
	
	List<InterestRepaymentHis> selectByExample(InterestRepaymentHisQuery example);
	
	InterestRepaymentHis selectByPrimaryKey(String id);
	
	int updateByPrimaryKeySelective(InterestRepaymentHis record);
	
	int updateByPrimaryKey(InterestRepaymentHis record);
	
	Long sumRepaidAmountByExample(InterestRepaymentHisQuery example);
}
