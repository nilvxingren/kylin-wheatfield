package com.rkylin.wheatfield.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author  likun
 * @version 创建时间：2015-4-23 下午2:36:15
 * 类说明
 */
public class TransOrderInfoNew implements Serializable{
	/**
     * Description:
     */
    private static final long serialVersionUID = 1L;
	private String orderNo;
	private Integer orderStatus;
	private Long amount;
	private String funcCode;
	private String requestNo;
	private String orderPackageNo;
	private Date createdTime;
	private String errorCode;
	private String errorMsg;
	private String merchantCode;
	private Date updatedTime;
	private String userId;
	private String interMerchantCode;
	
	private String transsumid;//每日交易汇总表id
	
	/**
	 * @return the transsumid
	 */
	public String getTranssumid() {
		return transsumid;
	}
	/**
	 * @param transsumid the transsumid to set
	 */
	public void setTranssumid(String transsumid) {
		this.transsumid = transsumid;
	}
	public String getMerchantCode() {
		return merchantCode;
	}
	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	public String getFuncCode() {
		return funcCode;
	}
	public void setFuncCode(String funcCode) {
		this.funcCode = funcCode;
	}
	public String getRequestNo() {
		return requestNo;
	}
	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}
	public String getOrderPackageNo() {
		return orderPackageNo;
	}
	public void setOrderPackageNo(String orderPackageNo) {
		this.orderPackageNo = orderPackageNo;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public Integer getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}
	public Long getAmount() {
		return amount;
	}
	public void setAmount(Long amount) {
		this.amount = amount;
	}
	public Date getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getInterMerchantCode() {
		return interMerchantCode;
	}
	public void setInterMerchantCode(String interMerchantCode) {
		this.interMerchantCode = interMerchantCode;
	}
	
  
}
