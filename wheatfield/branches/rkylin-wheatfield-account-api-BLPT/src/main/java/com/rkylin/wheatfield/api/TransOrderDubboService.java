package com.rkylin.wheatfield.api;

import java.util.List;

import com.rkylin.wheatfield.bean.OrderQuery;
import com.rkylin.wheatfield.bean.TransOrderQuery;
import com.rkylin.wheatfield.bean.TransOrderQuy;
import com.rkylin.wheatfield.bean.TransOrderStatusUpdate;
import com.rkylin.wheatfield.model.APITransOrderInfoResponse;
import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.model.TransOrderInfosResponse;

public interface TransOrderDubboService {

    /**
     * 根据请求号查询订单
     * 
     * @param query
     * @return
     */
    public TransOrderInfosResponse getOrdersByReqNo(OrderQuery query);

    /**
     * 订单查询-订单包号
     * 
     * @param query
     * @return
     */
    public TransOrderInfosResponse getOrdersByPackageNo(com.rkylin.wheatfield.pojo.TransOrderInfoQuery query);

    /**
     * 订单状态修改 Discription:
     * 
     * @param transOrderStatusUpdate
     * @return CommonResponse
     * @author Achilles
     * @since 2016年8月17日
     */
    public CommonResponse updateTransOrderStatus(TransOrderStatusUpdate transOrderStatusUpdate);

    /**
     * 通知订单结果(清结算) Discription:
     * 
     * @param transOrderInfoList
     * @return CommonResponse
     * @author Achilles
     * @since 2016年10月10日
     */
    public CommonResponse notifyTransOrderResults(List<com.rkylin.wheatfield.bean.TransOrderInfo> transOrderInfoList);
}
