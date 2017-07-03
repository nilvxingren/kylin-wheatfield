/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao;

import java.util.List;
import java.util.Map;

import com.rkylin.wheatfield.pojo.CreditApprovalInfo;
import com.rkylin.wheatfield.pojo.CreditApprovalInfoQuery;

public interface CreditApprovalInfoDao {
	int countByExample(CreditApprovalInfoQuery example);
	
	int deleteByExample(CreditApprovalInfoQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(CreditApprovalInfo record);
	
	int insertSelective(CreditApprovalInfo record);
	
	List<CreditApprovalInfo> selectByExample(CreditApprovalInfoQuery example);
	
	CreditApprovalInfo selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(CreditApprovalInfo record);
	
	int updateByPrimaryKey(CreditApprovalInfo record);
	
	int updateInterestDate(Map<String,String> paraMap);
	
	List<String> validateOrderList(Map<String,String> paraMap);
	
	long sumAmountForSqsm();
}
