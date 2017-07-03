package com.rkylin.wheatfield.pojo;

import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author  likun
 * @version 创建时间：2015-4-23 下午2:36:15
 * 类说明
 */
@XStreamAlias("transorderinfo")
public class TransOrderInfoNew {
	@XStreamAlias("orderno")
	private String orderNo;
	@XStreamAlias("orderstatus")
	private Integer orderStatus;
	@XStreamAlias("amount")
	private Long amount;
	@XStreamAlias("funccode")
	private String funcCode;
	@XStreamAlias("requestno")
	private String requestNo;
	@XStreamAlias("orderpackageno")
	private String orderPackageNo;
	@XStreamAlias("createdtime")
	private Date createdTime;
	@XStreamAlias("errorcode")
	private String errorCode;
	@XStreamAlias("errormsg")
	private String errorMsg;
	@XStreamAlias("merchantcode")
	private String merchantCode;
	@XStreamAlias("updatedtime")
	private Date updatedTime;
	
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
	
  
}
