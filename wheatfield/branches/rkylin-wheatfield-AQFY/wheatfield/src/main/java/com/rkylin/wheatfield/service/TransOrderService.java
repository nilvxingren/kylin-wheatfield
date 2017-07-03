package com.rkylin.wheatfield.service;

import com.rkylin.wheatfield.bean.OrderQuery;
import com.rkylin.wheatfield.model.TransOrderInfosResponse;
import com.rkylin.wheatfield.model.TransOrderResponse;

public interface TransOrderService {
	
	/**
	 * 根据请求号查询订单
	 * @param query
	 * @return
	 */
	public TransOrderInfosResponse getOrdersByReqNo(OrderQuery query);
	
	/**
	 * 退票
	 * Discription:
	 * @param instCode
	 * @param orderNo
	 * @param errorMsg
	 * @return TransOrderResponse
	 * @author Achilles
	 * @since 2016年11月30日
	 */
	public TransOrderResponse refund(String instCode,String orderNo,String errorMsg);
}
