/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.pojo;

import java.io.Serializable;

/**
 * SettleBalanceEntry
 * @author code-generator
 *
 */
public class SettleBalanceEntry implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.Integer settleId;
	private java.lang.String rootInstCd;
	private java.lang.String batchId;
	private java.lang.String transSeqId;
	private Integer transType;
	private java.lang.String userId;
	private Integer statusId;
	private java.lang.String orderNo;
	private java.util.Date transTime;
	private java.lang.Long amount;
	private java.lang.String retriRefNo;
	private java.util.Date settleTime;
	private java.lang.String merchantCode;
	private java.lang.String fee;
	private java.lang.String fee2;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;

	/**
	 * 清算流水ID
	 * @param settleId
	 */
	public void setSettleId(java.lang.Integer settleId) {
		this.settleId = settleId;
	}
	
	/**
	 * 清算流水ID
	 * @return
	 */
	public java.lang.Integer getSettleId() {
		return this.settleId;
	}
	/**
	 * 管理机构代码
	 * @param rootInstCd
	 */
	public void setRootInstCd(java.lang.String rootInstCd) {
		this.rootInstCd = rootInstCd;
	}
	
	/**
	 * 管理机构代码
	 * @return
	 */
	public java.lang.String getRootInstCd() {
		return this.rootInstCd;
	}
	/**
	 * 交易批次号
	 * @param batchId
	 */
	public void setBatchId(java.lang.String batchId) {
		this.batchId = batchId;
	}
	
	/**
	 * 交易批次号
	 * @return
	 */
	public java.lang.String getBatchId() {
		return this.batchId;
	}
	/**
	 * 交易序号
	 * @param transSeqId
	 */
	public void setTransSeqId(java.lang.String transSeqId) {
		this.transSeqId = transSeqId;
	}
	
	/**
	 * 交易序号
	 * @return
	 */
	public java.lang.String getTransSeqId() {
		return this.transSeqId;
	}
	/**
	 * 交易类型
	 * @param transType
	 */
	public void setTransType(Integer transType) {
		this.transType = transType;
	}
	
	/**
	 * 交易类型
	 * @return
	 */
	public Integer getTransType() {
		return this.transType;
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
	 * 交易状态
	 * @param statusId
	 */
	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
	
	/**
	 * 交易状态
	 * @return
	 */
	public Integer getStatusId() {
		return this.statusId;
	}
	/**
	 * 订单号
	 * @param orderId
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
	 * 交易日期
	 * @param transTime
	 */
	public void setTransTime(java.util.Date transTime) {
		this.transTime = transTime;
	}
	
	/**
	 * 交易日期
	 * @return
	 */
	public java.util.Date getTransTime() {
		return this.transTime;
	}
	/**
	 * 交易金额
	 * @param amount
	 */
	public void setAmount(java.lang.Long amount) {
		this.amount = amount;
	}
	
	/**
	 * 交易金额
	 * @return
	 */
	public java.lang.Long getAmount() {
		return this.amount;
	}
	/**
	 * 关联号
	 * @param retriRefNo
	 */
	public void setRetriRefNo(java.lang.String retriRefNo) {
		this.retriRefNo = retriRefNo;
	}
	
	/**
	 * 关联号
	 * @return
	 */
	public java.lang.String getRetriRefNo() {
		return this.retriRefNo;
	}
	/**
	 * 清算日期
	 * @param settleTime
	 */
	public void setSettleTime(java.util.Date settleTime) {
		this.settleTime = settleTime;
	}
	
	/**
	 * 清算日期
	 * @return
	 */
	public java.util.Date getSettleTime() {
		return this.settleTime;
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
	 * 手续费1
	 * @param fee
	 */
	public void setFee(java.lang.String fee) {
		this.fee = fee;
	}
	
	/**
	 * 手续费1
	 * @return
	 */
	public java.lang.String getFee() {
		return this.fee;
	}
	/**
	 * 手续费2
	 * @param fee2
	 */
	public void setFee2(java.lang.String fee2) {
		this.fee2 = fee2;
	}
	
	/**
	 * 手续费2
	 * @return
	 */
	public java.lang.String getFee2() {
		return this.fee2;
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
}