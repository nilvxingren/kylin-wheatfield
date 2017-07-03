package com.rkylin.wheatfield.model;

import java.io.Serializable;
import java.util.List;

import com.rkylin.wheatfield.pojo.TransOrderInfo;

public class TransOrderResponse  extends CommonResponse implements Serializable{

    private List<TransOrderInfo> transOrderInfoList;

    public List<TransOrderInfo> getTransOrderInfoList() {
        return transOrderInfoList;
    }

    public void setTransOrderInfoList(List<TransOrderInfo> transOrderInfoList) {
        this.transOrderInfoList = transOrderInfoList;
    }
    
    
}
