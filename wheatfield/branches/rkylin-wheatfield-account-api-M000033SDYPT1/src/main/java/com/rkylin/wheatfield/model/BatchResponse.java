package com.rkylin.wheatfield.model;

import java.util.Map;


public class BatchResponse extends CommonResponse{

	private Map<String,String> orderNoToMsgMap;

    public Map<String, String> getOrderNoToMsgMap() {
        return orderNoToMsgMap;
    }

    public void setOrderNoToMsgMap(Map<String, String> orderNoToMsgMap) {
        this.orderNoToMsgMap = orderNoToMsgMap;
    }

	
}
