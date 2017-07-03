/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao;

import java.util.List;

import com.rkylin.wheatfield.pojo.GenerationPayment;
import com.rkylin.wheatfield.pojo.GenerationPaymentQuery;

public interface GenerationPaymentDao {
	int countByExample(GenerationPaymentQuery example);
	
	int deleteByExample(GenerationPaymentQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(GenerationPayment record);
	
	int insertSelective(GenerationPayment record);
	
	List<GenerationPayment> selectByExample(GenerationPaymentQuery example);
	
	GenerationPayment selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(GenerationPayment record);
	
	int updateByPrimaryKey(GenerationPayment record);

	int batchUpdate(List<?> list);
	
	int batchUpdateByOrderNoRootInstCd(List<?> list);
	
	List<GenerationPayment> selectByOrderType(GenerationPaymentQuery example);
	
	List<GenerationPayment> selectByOrderNo(String[]  orderNo);
	
	public List<GenerationPayment> selectByOrderNoAndBatch(String batch,String[]  orderNo);
	
	public void insertBatch(List<GenerationPayment> list);
}


