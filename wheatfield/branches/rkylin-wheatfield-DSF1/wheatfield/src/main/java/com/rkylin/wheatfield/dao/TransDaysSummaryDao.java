/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao;

import java.util.List;

import com.rkylin.wheatfield.pojo.TransDaysSummary;
import com.rkylin.wheatfield.pojo.TransDaysSummaryQuery;

public interface TransDaysSummaryDao {
	int countByExample(TransDaysSummaryQuery example);
	
	int deleteByExample(TransDaysSummaryQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(TransDaysSummary record);
	
	void insertBatch(List<TransDaysSummary> list);
	
	int insertSelective(TransDaysSummary record);
	
	List<TransDaysSummary> selectByExample(TransDaysSummaryQuery example);
	
	TransDaysSummary selectByPrimaryKey(String id);
	
	int updateByPrimaryKeySelective(TransDaysSummary record);
	
	int updateByPrimaryKey(TransDaysSummary record);
}
