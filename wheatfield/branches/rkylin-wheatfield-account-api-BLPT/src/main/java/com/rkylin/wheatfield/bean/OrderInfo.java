package com.rkylin.wheatfield.bean;

import java.io.Serializable;

public class OrderInfo  implements Serializable{

    private Long amount;
    private String outProductId;
    private java.lang.String outUserId; 
    private java.lang.String orderNo;
    private java.lang.String requestNo;
    private java.lang.String remark;
    
    public Long getAmount() {
        return amount;
    }
    public void setAmount(Long amount) {
        this.amount = amount;
    }
    public String getOutProductId() {
        return outProductId;
    }
    public void setOutProductId(String outProductId) {
        this.outProductId = outProductId;
    }
    public java.lang.String getOutUserId() {
        return outUserId;
    }
    public void setOutUserId(java.lang.String outUserId) {
        this.outUserId = outUserId;
    }
    public java.lang.String getOrderNo() {
        return orderNo;
    }
    public void setOrderNo(java.lang.String orderNo) {
        this.orderNo = orderNo;
    }
    public java.lang.String getRequestNo() {
        return requestNo;
    }
    public void setRequestNo(java.lang.String requestNo) {
        this.requestNo = requestNo;
    }
    public java.lang.String getRemark() {
        return remark;
    }
    public void setRemark(java.lang.String remark) {
        this.remark = remark;
    }
   
    
}
