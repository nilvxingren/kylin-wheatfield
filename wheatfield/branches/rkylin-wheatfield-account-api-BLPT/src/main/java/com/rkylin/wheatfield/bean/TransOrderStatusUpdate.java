package com.rkylin.wheatfield.bean;

public class TransOrderStatusUpdate   implements java.io.Serializable{

    private java.util.Set<java.lang.Integer> requestIdSet;
    private java.util.Set<java.lang.String> requestNoSet;
    private java.lang.String orderPackageNo;
    private java.util.Set<java.lang.String> orderNoSet;
    private java.lang.String merchantCode;
    private java.lang.Integer status;
    
    public java.util.Set<java.lang.Integer> getRequestIdSet() {
        return requestIdSet;
    }
    public void setRequestIdSet(java.util.Set<java.lang.Integer> requestIdSet) {
        this.requestIdSet = requestIdSet;
    }
    public java.util.Set<java.lang.String> getRequestNoSet() {
        return requestNoSet;
    }
    public void setRequestNoSet(java.util.Set<java.lang.String> requestNoSet) {
        this.requestNoSet = requestNoSet;
    }
    public java.lang.String getOrderPackageNo() {
        return orderPackageNo;
    }
    public void setOrderPackageNo(java.lang.String orderPackageNo) {
        this.orderPackageNo = orderPackageNo;
    }
    public java.util.Set<java.lang.String> getOrderNoSet() {
        return orderNoSet;
    }
    public void setOrderNoSet(java.util.Set<java.lang.String> orderNoSet) {
        this.orderNoSet = orderNoSet;
    }
    public java.lang.String getMerchantCode() {
        return merchantCode;
    }
    public void setMerchantCode(java.lang.String merchantCode) {
        this.merchantCode = merchantCode;
    }
    public java.lang.Integer getStatus() {
        return status;
    }
    public void setStatus(java.lang.Integer status) {
        this.status = status;
    }
    
    
}
