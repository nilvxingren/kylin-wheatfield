package com.rkylin.wheatfield.service;

import java.util.List;
import java.util.Set;

import com.rkylin.wheatfield.bean.OrderQuery;
import com.rkylin.wheatfield.model.CommonResponse;
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
	 * 处理清结算推送的代收付缓存数据
	 * Discription: void
	 * @author Achilles
	 * @since 2016年10月11日
	 */
	public CommonResponse manageRecAndPayCacheResults(Set<String> keySet);
	
	/**
	 * 根据一分钱代付结果修改卡状态
	 * Discription:
	 * @return CommonResponse
	 * @author Achilles
	 * @since 2016年10月17日
	 */
	public void updateAccountInfoByPayResult(List<com.rkylin.wheatfield.pojo.TransOrderInfo> transOrderInfoPennyList);
	
    /**
     * 退票
     * Discription:
     * @param instCode
     * @param orderNo
     * @return CommonResponse
     * @author Achilles
     * @since 2016年10月14日
     */
    public TransOrderResponse refund(String instCode,String orderNo,String errorMsg);
    
    /**
     * 手动退票,如果是一分钱代付,修改卡状态
     * Discription:
     * @param merchantCode
     * @param orderNo
     * @param errorMsg
     * @return CommonResponse
     * @author Achilles
     * @since 2016年10月26日
     */
    public CommonResponse updateAccInfoStatusRefund(String merchantCode,String orderNo,String errorMsg);
    
}
