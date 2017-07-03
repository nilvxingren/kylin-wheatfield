package com.rkylin.wheatfield.bean;

import java.io.Serializable;

public class TransferInfo  implements Serializable{

    private Long amount;
    private String productId;
    private java.lang.String userId; 
    private java.lang.String orderNo;
    private java.lang.String requestNo;
    private java.lang.String busiTypeId;
    private java.lang.String remark;
    public Long getAmount() {
        return amount;
    }
    public void setAmount(Long amount) {
        this.amount = amount;
    }
    public String getProductId() {
        return productId;
    }
    public void setProductId(String productId) {
        this.productId = productId;
    }
    public java.lang.String getUserId() {
        return userId;
    }
    public void setUserId(java.lang.String userId) {
        this.userId = userId;
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
    public java.lang.String getBusiTypeId() {
        return busiTypeId;
    }
    public void setBusiTypeId(java.lang.String busiTypeId) {
        this.busiTypeId = busiTypeId;
    }
    

}
