package com.rkylin.wheatfield.service;

import java.util.List;

import com.rkylin.wheatfield.pojo.TransOrder;
import com.rkylin.wheatfield.pojo.TransOrderInfo;
import com.rkylin.wheatfield.pojo.TransOrderInfoNew;


public interface OrderService {

	/**
	 * 根据用户ID和交易类型获取订单条数，以便设置手续费
	 * @param userId 用户ID
	 * @param funcCode 交易类型  4016 提现交易类型
	 * @return
	 */
	int getOrderNum(String userId,String funcCode);
	/**
	 * 订单查询
	 * @param orderNo      订单号
	 * @param merchantCode 商品号/机构号
	 * @param startTime    查询开始时间
	 * @param endTime	        查询结束时间
	 * @return
	 */
	List<TransOrder> getTransOrders(String orderNo,String merchantCode, String startTime,String endTime);
	/**
	 * 向通联发起验证订单信息是否有效
	 * @param transOrder
	 * @return true=有效  false=无效
	 */
	boolean validataOrder(TransOrderInfo transOrderInfo);
	
	/**
	 * 订单查询
	 * @param userId	        用户ID
	 * @param orderNo      订单号
	 * @param merchantCode 商品号/机构号
	 * @param startTime    查询开始时间
	 * @param endTime	        查询结束时间
	 * @param funcCode	        交易类型
	 * @param status       订单状态
	 * @param interMerchantCode	    收款方
	 * @param amount       交易金额
	 * @return
	 */
	public List<TransOrderInfoNew> getTransOrdersNew(String userId,String orderNo,String merchantCode, String startTime, String endTime,String funcCode, String status,String interMerchantCode,String amount);
}
