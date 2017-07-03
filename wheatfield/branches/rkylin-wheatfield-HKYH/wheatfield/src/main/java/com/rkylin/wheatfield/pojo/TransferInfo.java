package com.rkylin.wheatfield.pojo;

import java.util.Date;

public class TransferInfo {
	
	private String transferType;
	
	private String conditionCode;
	
	/**
	 * 发生方商户编码
	 */
	private String merchantCode;
	
	/**
	 * 发生方产品号
	 */
	private String productId;
	
	/**
	 * 发生方用户号
	 */
	private String userId;
	
	/**
	 * 请求号
	 */
	private String requestNo;
	
	/**
	 * 请求时间
	 */
	private Date requestTime;
	
	/**
	 * 转账金额(单位分)
	 */
	private long amount;
	
	/**
	 * 手续费
	 */
	private long userfee;
	
	/**
	 * 接收方结构号码
	 */
	private String interMerchantCode; 
	
	/**
	 * 接收方产品号
	 */
	private String interProductId;
	
	/**
	 * 接收方用户号
	 */
	private String userRelateId;
	/**
	 * 是否成功
	 */
	private boolean success = false;

	public String getTransferType() {
		return transferType;
	}

	public void setTransferType(String transferType) {
		this.transferType = transferType;
	}

	public String getConditionCode() {
		return conditionCode;
	}

	public void setConditionCode(String conditionCode) {
		this.conditionCode = conditionCode;
	}

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}

	public Date getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public long getUserfee() {
		return userfee;
	}

	public void setUserfee(long userfee) {
		this.userfee = userfee;
	}

	public String getInterMerchantCode() {
		return interMerchantCode;
	}

	public void setInterMerchantCode(String interMerchantCode) {
		this.interMerchantCode = interMerchantCode;
	}

	public String getInterProductId() {
		return interProductId;
	}

	public void setInterProductId(String interProductId) {
		this.interProductId = interProductId;
	}

	public String getUserRelateId() {
		return userRelateId;
	}

	public void setUserRelateId(String userRelateId) {
		this.userRelateId = userRelateId;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

}
