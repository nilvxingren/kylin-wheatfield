/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao;

import java.util.List;

import com.rkylin.wheatfield.pojo.SettleTransTab;
import com.rkylin.wheatfield.pojo.SettleTransTabQuery;

public interface SettleTransTabDao {
	int countByExample(SettleTransTabQuery example);
	
	int deleteByExample(SettleTransTabQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(SettleTransTab record);
	
	int insertSelective(SettleTransTab record);
	
	List<SettleTransTab> selectByExample(SettleTransTabQuery example);
	
	SettleTransTab selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(SettleTransTab record);
	
	int updateByPrimaryKey(SettleTransTab record);
	/**
	 * 数据批量入库
	 * @param settleTransTabs
	 */
	void insertSelectiveBatch(List<SettleTransTab> settleTransTabs);
}
