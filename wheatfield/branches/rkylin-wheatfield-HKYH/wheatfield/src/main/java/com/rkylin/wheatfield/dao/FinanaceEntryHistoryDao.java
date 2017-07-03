/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao;

import java.util.List;

import com.rkylin.wheatfield.pojo.FinanaceEntryHistory;
import com.rkylin.wheatfield.pojo.FinanaceEntryHistoryQuery;

public interface FinanaceEntryHistoryDao {
	int countByExample(FinanaceEntryHistoryQuery example);
	
	int deleteByExample(FinanaceEntryHistoryQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(FinanaceEntryHistory record);
	
	int insertSelective(FinanaceEntryHistory record);
	
	List<FinanaceEntryHistory> selectByExample(FinanaceEntryHistoryQuery example);
	
	FinanaceEntryHistory selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(FinanaceEntryHistory record);
	
	int updateByPrimaryKey(FinanaceEntryHistory record);
}
