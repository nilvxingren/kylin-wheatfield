/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao;

import java.util.List;

import com.rkylin.wheatfield.pojo.ManageHistory;
import com.rkylin.wheatfield.pojo.ManageHistoryQuery;

public interface ManageHistoryDao {
	int countByExample(ManageHistoryQuery example);
	
	int deleteByExample(ManageHistoryQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(ManageHistory record);
	
	int insertSelective(ManageHistory record);
	
	List<ManageHistory> selectByExample(ManageHistoryQuery example);
	
	ManageHistory selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(ManageHistory record);
	
	int updateByPrimaryKey(ManageHistory record);
}
