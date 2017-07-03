/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao;

import java.util.List;

import com.rkylin.wheatfield.pojo.SettleBatchResult;
import com.rkylin.wheatfield.pojo.SettleBatchResultQuery;

public interface SettleBatchResultDao {
	int countByExample(SettleBatchResultQuery example);
	
	int deleteByExample(SettleBatchResultQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(SettleBatchResult record);
	
	int insertSelective(SettleBatchResult record);
	
	List<SettleBatchResult> selectByExample(SettleBatchResultQuery example);
	
	SettleBatchResult selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(SettleBatchResult record);
	
	int updateByPrimaryKey(SettleBatchResult record);
}
