package com.rkylin.wheatfield.service;

import com.rkylin.wheatfield.bean.OrderQuery;
import com.rkylin.wheatfield.model.TransOrderInfosResponse;

public interface TransOrderService {
	
	/**
	 * 根据请求号查询订单
	 * @param query
	 * @return
	 */
	public TransOrderInfosResponse getOrdersByReqNo(OrderQuery query);
}
