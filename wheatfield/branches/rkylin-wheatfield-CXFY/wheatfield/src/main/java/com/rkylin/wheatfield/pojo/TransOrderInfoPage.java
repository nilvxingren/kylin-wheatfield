package com.rkylin.wheatfield.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * TransOrderInfo
 * @author liuhuan
 * @created 2016-12-9 12:20:38
 *
 */
public class TransOrderInfoPage implements Serializable{
	private static final long serialVersionUID = 1L;
	private java.lang.String orderPackageNo;
	private java.lang.String orderNo;
	private java.lang.String funcCode;
	private java.lang.String interMerchantCode;
	private java.lang.String merchantCode;
	private java.lang.String userId;
	private java.lang.Long amount;
	private Integer status;
	private Date startTime;
	private Date endTime;
	private Integer pageSize;
	private Integer pageNum;
	private int startRow;
	
    public int getStartRow() {
        return startRow;
    }
    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }
    public Integer getPageSize() {
        return pageSize;
    }
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
    public Integer getPageNum() {
        return pageNum;
    }
    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
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
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public Date getStartTime() {
        return startTime;
    }
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    public Date getEndTime() {
        return endTime;
    }
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
	
	
}