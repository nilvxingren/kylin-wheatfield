/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager;

import java.util.List;
import java.util.Map;

import com.rkylin.wheatfield.pojo.TransOrderInfo;
import com.rkylin.wheatfield.pojo.TransOrderInfoQuery;

public interface TransOrderInfoManager {
	int saveTransOrderInfo(TransOrderInfo transOrderInfo);

	TransOrderInfo findTransOrderInfoById(Integer id);
	
	List<TransOrderInfo> queryList(TransOrderInfoQuery query);
	
	List<TransOrderInfo> queryListGroup(TransOrderInfoQuery query);
	
	List<TransOrderInfo> queryListGroupByInter(TransOrderInfoQuery query);
	
	void deleteTransOrderInfoById(Integer id);
	
	void deleteTransOrderInfo(TransOrderInfoQuery query);
	
	List<TransOrderInfo> queryListByCreditDate(TransOrderInfoQuery query);
	
	int batchUpdateByOrderNo(List<?> list);
	
	List<TransOrderInfo> selectTransOrderInfos(TransOrderInfoQuery query);
	
	List<TransOrderInfo> selectTransOrdersAndSumId(TransOrderInfoQuery query);
	
	/**
	 * 退票查询订单
	 * @param query
	 * @return
	 */
	public List<TransOrderInfo> selectTransOrderInfosRefund(TransOrderInfoQuery query) ;
	
	List<Map<String, Object>> selectSummaryInfo(Map summaryMap);
}
