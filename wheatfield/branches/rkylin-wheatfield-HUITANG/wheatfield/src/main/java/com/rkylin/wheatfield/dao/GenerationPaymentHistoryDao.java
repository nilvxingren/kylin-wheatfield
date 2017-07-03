/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao;

import java.util.List;

import com.rkylin.wheatfield.pojo.GenerationPaymentHistory;
import com.rkylin.wheatfield.pojo.GenerationPaymentHistoryQuery;

public interface GenerationPaymentHistoryDao {
	int countByExample(GenerationPaymentHistoryQuery example);
	
	int deleteByExample(GenerationPaymentHistoryQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(GenerationPaymentHistory record);
	
	int insertSelective(GenerationPaymentHistory record);
	
	List<GenerationPaymentHistory> selectByExample(GenerationPaymentHistoryQuery example);
	
	GenerationPaymentHistory selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(GenerationPaymentHistory record);
	
	int updateByPrimaryKey(GenerationPaymentHistory record);
	
	List<GenerationPaymentHistory> selectByOnecent();
	
	List<GenerationPaymentHistory> selectPayResOfJudgePubAccount();
}
