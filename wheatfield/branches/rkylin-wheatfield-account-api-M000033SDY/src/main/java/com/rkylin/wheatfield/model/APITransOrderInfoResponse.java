package com.rkylin.wheatfield.model;

import com.rkylin.wheatfield.pojo.TransOrderInfo;

/**
 * 
 * Description:
 * 
 * @author: sun
 * @CreateDate: 2017年2月15日
 * @version: V1.0
 */
public class APITransOrderInfoResponse extends CommonResponse {

    private static final long serialVersionUID = -5014673131640185755L;

    private TransOrderInfo transOrderInfo;

    public TransOrderInfo getTransOrderInfo() {
        return transOrderInfo;
    }

    public void setTransOrderInfo(TransOrderInfo transOrderInfo) {
        this.transOrderInfo = transOrderInfo;
    }
}
