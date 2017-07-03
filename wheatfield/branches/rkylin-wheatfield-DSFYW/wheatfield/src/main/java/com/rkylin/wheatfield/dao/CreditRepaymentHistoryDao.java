/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao;

import java.util.List;

import com.rkylin.wheatfield.pojo.CreditRepaymentHistory;
import com.rkylin.wheatfield.pojo.CreditRepaymentHistoryQuery;

public interface CreditRepaymentHistoryDao {
	int countByExample(CreditRepaymentHistoryQuery example);
	
	int deleteByExample(CreditRepaymentHistoryQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(CreditRepaymentHistory record);
	
	int insertSelective(CreditRepaymentHistory record);
	
	List<CreditRepaymentHistory> selectByExample(CreditRepaymentHistoryQuery example);
	
	CreditRepaymentHistory selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(CreditRepaymentHistory record);
	
	int updateByPrimaryKey(CreditRepaymentHistory record);
}
