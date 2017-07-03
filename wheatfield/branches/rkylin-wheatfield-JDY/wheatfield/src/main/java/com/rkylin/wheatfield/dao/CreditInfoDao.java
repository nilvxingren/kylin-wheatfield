/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao;

import java.util.List;

import com.rkylin.wheatfield.pojo.CreditInfo;
import com.rkylin.wheatfield.pojo.CreditInfoQuery;

public interface CreditInfoDao {
	int countByExample(CreditInfoQuery example);
	
	int deleteByExample(CreditInfoQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(CreditInfo record);
	
	int insertSelective(CreditInfo record);
	
	List<CreditInfo> selectByExample(CreditInfoQuery example);
	
	CreditInfo selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(CreditInfo record);
	
	int updateByPrimaryKey(CreditInfo record);
}
