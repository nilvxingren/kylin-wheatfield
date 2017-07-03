/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao;

import java.util.List;

import com.rkylin.wheatfield.pojo.SettleSplittingEntry;
import com.rkylin.wheatfield.pojo.SettleSplittingEntryQuery;

public interface SettleSplittingEntryDao {
	int countByExample(SettleSplittingEntryQuery example);
	
	int deleteByExample(SettleSplittingEntryQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(SettleSplittingEntry record);
	
	int insertSelective(SettleSplittingEntry record);
	
	List<SettleSplittingEntry> selectByExample(SettleSplittingEntryQuery example);
	
	SettleSplittingEntry selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(SettleSplittingEntry record);
	
	int updateByPrimaryKey(SettleSplittingEntry record);
	
	int batchUpdate( List<?> list);
}
