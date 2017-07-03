/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao;

import java.util.List;

import com.rkylin.wheatfield.pojo.FinanaceEntry;
import com.rkylin.wheatfield.pojo.FinanaceEntryQuery;

public interface FinanaceEntryDao {
	int countByExample(FinanaceEntryQuery example);
	
	int deleteByExample(FinanaceEntryQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(FinanaceEntry record);
	
	int insertSelective(FinanaceEntry record);
	/**
	 * 批量插入数据
	 * @param finanaceEntries
	 */
	void insertSelectiveBatch(List<FinanaceEntry> finanaceEntries);
	
	List<FinanaceEntry> selectByExample(FinanaceEntryQuery example);
	/**
	 * 根据账户编号和记账方向获取当天本帐户的流水总额
	 * @param finAccountId
	 * @param direction
	 * @return
	 */
	List<FinanaceEntry> selectByFinAccountId(FinanaceEntryQuery example);
	
	FinanaceEntry selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(FinanaceEntry record);
	
	int updateByPrimaryKey(FinanaceEntry record);
}
