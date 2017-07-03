/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao;

import java.util.List;

import com.rkylin.wheatfield.pojo.TransOrderInfo;
import com.rkylin.wheatfield.pojo.TransOrderInfoQuery;

public interface TransOrderInfoDao {
	int countByExample(TransOrderInfoQuery example);
	
	int deleteByExample(TransOrderInfoQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(TransOrderInfo record);
	
	int insertSelective(TransOrderInfo record);
	
	List<TransOrderInfo> selectByExample(TransOrderInfoQuery example);
	
	List<TransOrderInfo> selectByExampleGroup(TransOrderInfoQuery example);
	
	List<TransOrderInfo> selectByExampleGroupByInter(TransOrderInfoQuery example);
	
	TransOrderInfo selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(TransOrderInfo record);
	
	int updateByPrimaryKey(TransOrderInfo record);

	List<TransOrderInfo> selectList(TransOrderInfoQuery example);
	
	int batchUpdate( List<?> list);
	
	List<TransOrderInfo> selectTransOrderInfos(TransOrderInfoQuery query);
	
	List<TransOrderInfo> selectTransOrdersAndSumId(TransOrderInfoQuery query);
	
	/**
	 * 退票查询订单
	 * @param query
	 * @return
	 */
	public List<TransOrderInfo> selectTransOrdersRefund(TransOrderInfoQuery query);
	
}
