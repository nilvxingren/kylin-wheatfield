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

    /**
     * 根据相关条件查询订单列表（机构号、交易状态、创建时间、用户id、交易金额、交易渠道、交易类型）
     * 
     * @param query
     * @return
     */
    public TransOrderInfosResponse getOrdersByQuery(TransOrderQuery query);

    /**
     * 根据requestId查询订单
     * 
     * @param REQUEST_ID
     * @return
     */
    public APITransOrderInfoResponse getOrderByRequestId(Integer requestId);
    
    /**
     * 根据机构,功能码,时间范围,状态等查询订单信息
     * Discription:
     * @param transOrderQuy
     * @return TransOrderInfosResponse
     * @author Achilles
     * @since 2017年2月16日
     */
    public TransOrderInfosResponse getOrderByInst(TransOrderQuy transOrderQuy);
    
    /**
     * 订单查询
     * Discription:
     * @param transOrderQuy
     * @return TransOrderInfosResponse
     * @author Achilles
     * @since 2017年2月17日
     */
    public TransOrderInfosResponse getTransOrderInfos(com.rkylin.wheatfield.bean.TransOrderInfoQuy transOrderQuy);
    
    
      /**
       * 各种交易入口,根据功能码分流   
       * Discription:
       * @param transOrderInfo
       * @return CommonResponse
       * @author Achilles
       * @since 2017年3月22日
       */
      public CommonResponse trade(com.rkylin.wheatfield.bean.TransOrderInfo transOrderInfo);
}
