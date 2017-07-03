package com.rkylin.wheatfield.bean;

import java.io.Serializable;


public class TransOrderInf implements Serializable{
	private static final long serialVersionUID = 1L;
	private java.lang.String requestNo;
	private java.lang.String tradeFlowNo;
	private java.lang.String orderPackageNo;
	private java.lang.String orderNo;
	private java.lang.Long orderAmount;
	private java.lang.Long userFee;
	private java.lang.String funcCode;
	private java.lang.String interMerchantCode;
	private java.lang.String merchantCode;
	private java.lang.String userId;
	private java.lang.Long amount;
	private java.lang.String busiTypeId;
	private java.lang.String remark;
	private String productId;
	private Integer amountFlag;
	
    public Integer getAmountFlag() {
        return amountFlag;
    }
    public void setAmountFlag(Integer amountFlag) {
        this.amountFlag = amountFlag;
    }
    public java.lang.String getRequestNo() {
        return requestNo;
    }
    public void setRequestNo(java.lang.String requestNo) {
        this.requestNo = requestNo;
    }
    public java.lang.String getTradeFlowNo() {
        return tradeFlowNo;
    }
    public void setTradeFlowNo(java.lang.String tradeFlowNo) {
        this.tradeFlowNo = tradeFlowNo;
    }
    public java.lang.String getOrderPackageNo() {
        return orderPackageNo;
    }
    public void setOrderPackageNo(java.lang.String orderPackageNo) {
        this.orderPackageNo = orderPackageNo;
    }
    public java.lang.String getOrderNo() {
        return orderNo;
    }
    public void setOrderNo(java.lang.String orderNo) {
        this.orderNo = orderNo;
    }
    public java.lang.Long getOrderAmount() {
        return orderAmount;
    }
    public void setOrderAmount(java.lang.Long orderAmount) {
        this.orderAmount = orderAmount;
    }
    public java.lang.Long getUserFee() {
        return userFee;
    }
    public void setUserFee(java.lang.Long userFee) {
        this.userFee = userFee;
    }
    public java.lang.String getFuncCode() {
        return funcCode;
    }
    public void setFuncCode(java.lang.String funcCode) {
        this.funcCode = funcCode;
    }
    public java.lang.String getInterMerchantCode() {
        return interMerchantCode;
    }
    public void setInterMerchantCode(java.lang.String interMerchantCode) {
        this.interMerchantCode = interMerchantCode;
    }
    public java.lang.String getMerchantCode() {
        return merchantCode;
    }
    public void setMerchantCode(java.lang.String merchantCode) {
        this.merchantCode = merchantCode;
    }
    public java.lang.String getUserId() {
        return userId;
    }
    public void setUserId(java.lang.String userId) {
        this.userId = userId;
    }
    public java.lang.Long getAmount() {
        return amount;
    }
    public void setAmount(java.lang.Long amount) {
        this.amount = amount;
    }
    public java.lang.String getBusiTypeId() {
        return busiTypeId;
    }
    public void setBusiTypeId(java.lang.String busiTypeId) {
        this.busiTypeId = busiTypeId;
    }
    public java.lang.String getRemark() {
        return remark;
    }
    public void setRemark(java.lang.String remark) {
        this.remark = remark;
    }
    public String getProductId() {
        return productId;
    }
    public void setProductId(String productId) {
        this.productId = productId;
    }
	

}