/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao;

import java.util.List;

import com.rkylin.wheatfield.pojo.CurrencyInfo;
import com.rkylin.wheatfield.pojo.CurrencyInfoQuery;

public interface CurrencyInfoDao {
	int countByExample(CurrencyInfoQuery example);
	
	int deleteByExample(CurrencyInfoQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(CurrencyInfo record);
	
	int insertSelective(CurrencyInfo record);
	
	List<CurrencyInfo> selectByExample(CurrencyInfoQuery example);
	
	CurrencyInfo selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(CurrencyInfo record);
	
	int updateByPrimaryKey(CurrencyInfo record);
}
