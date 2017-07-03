/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao;

import java.util.List;

import com.rkylin.wheatfield.pojo.SettleBalanceEntry;
import com.rkylin.wheatfield.pojo.SettleBalanceEntryQuery;

public interface SettleBalanceEntryDao {
	int countByExample(SettleBalanceEntryQuery example);
	
	int deleteByExample(SettleBalanceEntryQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(SettleBalanceEntry record);
	
	int insertSelective(SettleBalanceEntry record);
	
	List<SettleBalanceEntry> selectByExample(SettleBalanceEntryQuery example);
	
	SettleBalanceEntry selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(SettleBalanceEntry record);
	
	int updateByPrimaryKey(SettleBalanceEntry record);
}
