package com.rkylin.wheatfield.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * TransOrderInfo
 * @author code-generator
 *
 */
public class TransOrderInfoQuery implements Serializable{
	private static final long serialVersionUID = 1L;
	private java.lang.Integer creditDate;
	private java.lang.Integer requestId;
	private java.lang.String requestNo;
	private java.util.Date requestTime;
	private java.lang.String tradeFlowNo;
	private java.lang.String orderPackageNo;
	private java.lang.String orderNo;
	private java.util.Date orderDate;
	private java.lang.Long orderAmount;
	private java.lang.Integer orderCount;
	private Integer transType;
	private java.lang.String funcCode;
	private java.lang.String interMerchantCode;
	private java.lang.String merchantCode;
	private java.lang.String userId;
	private java.lang.Long amount;
	private java.lang.Long feeAmount;
	private java.lang.Long userFee;
	private java.lang.Long profit;
	private java.lang.String busiTypeId;
	private java.lang.String payChannelId;
	private java.lang.String bankCode;
	private java.lang.String userIpAddress;
	private Integer status;
	private java.lang.String errorCode;
	private java.lang.String errorMsg;
	private java.lang.String remark;
	private java.util.Date accountDate;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;
	private Date startTime;
	private Date endTime;
	private Date startAccountDate;
	private Date endAccountDate;

	public Date getStartAccountDate() {
		return startAccountDate;
	}

	public void setStartAccountDate(Date startAccountDate) {
		this.startAccountDate = startAccountDate;
	}

	public Date getEndAccountDate() {
		return endAccountDate;
	}

	public void setEndAccountDate(Date endAccountDate) {
		this.endAccountDate = endAccountDate;
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

	/**
	 * 交易记录编码ID
	 * @param requestId
	 */
	public void setRequestId(java.lang.Integer requestId) {
		this.requestId = requestId;
	}
	
	/**
	 * 交易记录编码ID
	 * @return
	 */
	public java.lang.Integer getRequestId() {
		return this.requestId;
	}
	/**
	 * 交易请求号
	 * @param requestNo
	 */
	public void setRequestNo(java.lang.String requestNo) {
		this.requestNo = requestNo;
	}
	
	/**
	 * 交易请求号
	 * @return
	 */
	public java.lang.String getRequestNo() {
		return this.requestNo;
	}
	/**
	 * 交易请求时间
	 * @param requestTime
	 */
	public void setRequestTime(java.util.Date requestTime) {
		this.requestTime = requestTime;
	}
	
	/**
	 * 交易请求时间
	 * @return
	 */
	public java.util.Date getRequestTime() {
		return this.requestTime;
	}
	/**
	 * 统一交易流水号
	 * @param tradeFlowNo
	 */
	public void setTradeFlowNo(java.lang.String tradeFlowNo) {
		this.tradeFlowNo = tradeFlowNo;
	}
	
	/**
	 * 统一交易流水号
	 * @return
	 */
	public java.lang.String getTradeFlowNo() {
		return this.tradeFlowNo;
	}
	/**
	 * 订单包号
	 * @param orderPackageNo
	 */
	public void setOrderPackageNo(java.lang.String orderPackageNo) {
		this.orderPackageNo = orderPackageNo;
	}
	
	/**
	 * 订单包号
	 * @return
	 */
	public java.lang.String getOrderPackageNo() {
		return this.orderPackageNo;
	}
	/**
	 * 订单号
	 * @param orderNo
	 */
	public void setOrderNo(java.lang.String orderNo) {
		this.orderNo = orderNo;
	}
	
	/**
	 * 订单号
	 * @return
	 */
	public java.lang.String getOrderNo() {
		return this.orderNo;
	}
	/**
	 * 订单日期
	 * @param orderDate
	 */
	public void setOrderDate(java.util.Date orderDate) {
		this.orderDate = orderDate;
	}
	
	/**
	 * 订单日期
	 * @return
	 */
	public java.util.Date getOrderDate() {
		return this.orderDate;
	}
	/**
	 * 订单金额
	 * @param orderAmount
	 */
	public void setOrderAmount(java.lang.Long orderAmount) {
		this.orderAmount = orderAmount;
	}
	
	/**
	 * 订单金额
	 * @return
	 */
	public java.lang.Long getOrderAmount() {
		return this.orderAmount;
	}
	/**
	 * 订单数量
	 * @param orderCount
	 */
	public void setOrderCount(java.lang.Integer orderCount) {
		this.orderCount = orderCount;
	}
	
	/**
	 * 订单数量
	 * @return
	 */
	public java.lang.Integer getOrderCount() {
		return this.orderCount;
	}
	/**
	 * 订单类型
	 * @param transType
	 */
	public void setTransType(Integer transType) {
		this.transType = transType;
	}
	
	/**
	 * 订单类型
	 * @return
	 */
	public Integer getTransType() {
		return this.transType;
	}
	/**
	 * 功能编码
	 * @param funcCode
	 */
	public void setFuncCode(java.lang.String funcCode) {
		this.funcCode = funcCode;
	}
	
	/**
	 * 功能编码
	 * @return
	 */
	public java.lang.String getFuncCode() {
		return this.funcCode;
	}
	/**
	 * 中间商户编码
	 * @param interMerchantCode
	 */
	public void setInterMerchantCode(java.lang.String interMerchantCode) {
		this.interMerchantCode = interMerchantCode;
	}
	
	/**
	 * 中间商户编码
	 * @return
	 */
	public java.lang.String getInterMerchantCode() {
		return this.interMerchantCode;
	}
	/**
	 * 商户编码
	 * @param merchantCode
	 */
	public void setMerchantCode(java.lang.String merchantCode) {
		this.merchantCode = merchantCode;
	}
	
	/**
	 * 商户编码
	 * @return
	 */
	public java.lang.String getMerchantCode() {
		return this.merchantCode;
	}
	/**
	 * 用户ID
	 * @param userId
	 */
	public void setUserId(java.lang.String userId) {
		this.userId = userId;
	}
	
	/**
	 * 用户ID
	 * @return
	 */
	public java.lang.String getUserId() {
		return this.userId;
	}
	/**
	 * 入账金额
	 * @param amount
	 */
	public void setAmount(java.lang.Long amount) {
		this.amount = amount;
	}
	
	/**
	 * 入账金额
	 * @return
	 */
	public java.lang.Long getAmount() {
		return this.amount;
	}
	/**
	 * 手续费金额
	 * @param feeAmount
	 */
	public void setFeeAmount(java.lang.Long feeAmount) {
		this.feeAmount = feeAmount;
	}
	
	/**
	 * 手续费金额
	 * @return
	 */
	public java.lang.Long getFeeAmount() {
		return this.feeAmount;
	}
	/**
	 * 用户手续费
	 * @param userFee
	 */
	public void setUserFee(java.lang.Long userFee) {
		this.userFee = userFee;
	}
	
	/**
	 * 用户手续费
	 * @return
	 */
	public java.lang.Long getUserFee() {
		return this.userFee;
	}
	/**
	 * 利润
	 * @param profit
	 */
	public void setProfit(java.lang.Long profit) {
		this.profit = profit;
	}
	
	/**
	 * 利润
	 * @return
	 */
	public java.lang.Long getProfit() {
		return this.profit;
	}
	/**
	 * 业务类型ID
	 * @param busiTypeId
	 */
	public void setBusiTypeId(java.lang.String busiTypeId) {
		this.busiTypeId = busiTypeId;
	}
	
	/**
	 * 业务类型ID
	 * @return
	 */
	public java.lang.String getBusiTypeId() {
		return this.busiTypeId;
	}
	/**
	 * 支付渠道ID
	 * @param payChannelId
	 */
	public void setPayChannelId(java.lang.String payChannelId) {
		this.payChannelId = payChannelId;
	}
	
	/**
	 * 支付渠道ID
	 * @return
	 */
	public java.lang.String getPayChannelId() {
		return this.payChannelId;
	}
	/**
	 * 银行联行编码
	 * @param bankCode
	 */
	public void setBankCode(java.lang.String bankCode) {
		this.bankCode = bankCode;
	}
	
	/**
	 * 银行联行编码
	 * @return
	 */
	public java.lang.String getBankCode() {
		return this.bankCode;
	}
	/**
	 * 消费者IP地址
	 * @param userIpAddress
	 */
	public void setUserIpAddress(java.lang.String userIpAddress) {
		this.userIpAddress = userIpAddress;
	}
	
	/**
	 * 消费者IP地址
	 * @return
	 */
	public java.lang.String getUserIpAddress() {
		return this.userIpAddress;
	}
	/**
	 * 状态,0失效,1生效
	 * @param status
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	/**
	 * 状态,0失效,1生效
	 * @return
	 */
	public Integer getStatus() {
		return this.status;
	}
	/**
	 * 错误编码
	 * @param errorCode
	 */
	public void setErrorCode(java.lang.String errorCode) {
		this.errorCode = errorCode;
	}
	
	/**
	 * 错误编码
	 * @return
	 */
	public java.lang.String getErrorCode() {
		return this.errorCode;
	}
	/**
	 * 错误信息
	 * @param errorMsg
	 */
	public void setErrorMsg(java.lang.String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	/**
	 * 错误信息
	 * @return
	 */
	public java.lang.String getErrorMsg() {
		return this.errorMsg;
	}
	/**
	 * 备注
	 * @param remark
	 */
	public void setRemark(java.lang.String remark) {
		this.remark = remark;
	}
	
	/**
	 * 备注
	 * @return
	 */
	public java.lang.String getRemark() {
		return this.remark;
	}
	/**
	 * 记账日期
	 * @param accountDate
	 */
	public void setAccountDate(java.util.Date accountDate) {
		this.accountDate = accountDate;
	}
	
	/**
	 * 记账日期
	 * @return
	 */
	public java.util.Date getAccountDate() {
		return this.accountDate;
	}
	/**
	 * 记录创建时间
	 * @param createdTime
	 */
	public void setCreatedTime(java.util.Date createdTime) {
		this.createdTime = createdTime;
	}
	
	/**
	 * 记录创建时间
	 * @return
	 */
	public java.util.Date getCreatedTime() {
		return this.createdTime;
	}
	/**
	 * 记录更新时间
	 * @param updatedTime
	 */
	public void setUpdatedTime(java.util.Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	
	/**
	 * 记录更新时间
	 * @return
	 */
	public java.util.Date getUpdatedTime() {
		return this.updatedTime;
	}

	public java.lang.Integer getCreditDate() {
		return creditDate;
	}

	public void setCreditDate(java.lang.Integer creditDate) {
		this.creditDate = creditDate;
	}
}