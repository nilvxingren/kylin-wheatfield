package com.rkylin.wheatfield.bean;

import java.io.Serializable;
import java.util.List;

public class BatchTransferOrderInfo  implements Serializable{

    private java.lang.String merchantCode;
    private String intoProductId;
    private java.lang.String intoUserId; 
    private java.lang.String funcCode;
    private java.lang.String orderPackageNo;
    private java.lang.String remark;
    private java.lang.String userIpAddress;
    private List<com.rkylin.wheatfield.bean.OrderInfo> orderInfoList;
    
    public java.lang.String getMerchantCode() {
        return merchantCode;
    }
    public void setMerchantCode(java.lang.String merchantCode) {
        this.merchantCode = merchantCode;
    }
    public String getIntoProductId() {
        return intoProductId;
    }
    public void setIntoProductId(String intoProductId) {
        this.intoProductId = intoProductId;
    }
    public java.lang.String getIntoUserId() {
        return intoUserId;
    }
    public void setIntoUserId(java.lang.String intoUserId) {
        this.intoUserId = intoUserId;
    }
    public java.lang.String getFuncCode() {
        return funcCode;
    }
    public void setFuncCode(java.lang.String funcCode) {
        this.funcCode = funcCode;
    }
    public java.lang.String getOrderPackageNo() {
        return orderPackageNo;
    }
    public void setOrderPackageNo(java.lang.String orderPackageNo) {
        this.orderPackageNo = orderPackageNo;
    }
    public java.lang.String getRemark() {
        return remark;
    }
    public void setRemark(java.lang.String remark) {
        this.remark = remark;
    }
    public java.lang.String getUserIpAddress() {
        return userIpAddress;
    }
    public void setUserIpAddress(java.lang.String userIpAddress) {
        this.userIpAddress = userIpAddress;
    }
    public List<com.rkylin.wheatfield.bean.OrderInfo> getOrderInfoList() {
        return orderInfoList;
    }
    public void setOrderInfoList(List<com.rkylin.wheatfield.bean.OrderInfo> orderInfoList) {
        this.orderInfoList = orderInfoList;
    }
    
    
}
