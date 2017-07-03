package com.rkylin.wheatfield.api;

import com.rkylin.wheatfield.pojo.TransOrderInfoPage;
import com.rkylin.wheatfield.response.TransOrderResponse;


/**
 * Description: dubbo查询订单信息接口
 * @author: liuhuan
 * @CreateDate: 2016年12月9日
 * @version: V1.0
 */
public interface OrderServiceApi {

    /**
     * 
     * Discription: dubbo调用查询订单信息
     * @param query
     * @return List<TransOrderInfo>
     * @author liuhuan
     * @since 2016年12月9日
     */
    public TransOrderResponse getTransOrderInfo(TransOrderInfoPage transOrderInfoPage);
	
}
