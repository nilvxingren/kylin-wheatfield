/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao;

import java.util.List;

import com.rkylin.wheatfield.pojo.FinanaceEntryLast;
import com.rkylin.wheatfield.pojo.FinanaceEntryLastQuery;

public interface FinanaceEntryLastDao {
	int countByExample(FinanaceEntryLastQuery example);
	
	int deleteByExample(FinanaceEntryLastQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(FinanaceEntryLast record);
	
	int insertSelective(FinanaceEntryLast record);
	
	List<FinanaceEntryLast> selectByExample(FinanaceEntryLastQuery example);
	
	FinanaceEntryLast selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(FinanaceEntryLast record);
	
	int updateByPrimaryKey(FinanaceEntryLast record);
	
	/**
	 * 批量插入数据
	 * @param finanaceEntries
	 */
	void insertSelectiveBatch(List<FinanaceEntryLast> finanaceEntrieLasts);
}
