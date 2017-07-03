/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.pojo;

import java.io.Serializable;

/**
 * SettleSplittingEntry
 * @author code-generator
 *
 */
public class SettleSplittingEntry implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.String settleId;
	private java.lang.String rootInstCd;
	private java.util.Date accountDate;
	private java.lang.String batchId;
	private java.lang.String userId;
	private java.lang.Long amount;
	private Integer settleType;
	private Integer statusId;
	private java.lang.String remark;
	private java.lang.String orderno;
	private java.lang.String accountrelateid;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;

	/**
	 * 资金清分ID
	 * @param settleId
	 */
	public void setSettleId(java.lang.String settleId) {
		this.settleId = settleId;
	}
	
	/**
	 * 资金清分ID
	 * @return
	 */
	public java.lang.String getSettleId() {
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
	 * 账期
	 * @param accountDate
	 */
	public void setAccountDate(java.util.Date accountDate) {
		this.accountDate = accountDate;
	}
	
	/**
	 * 账期
	 * @return
	 */
	public java.util.Date getAccountDate() {
		return this.accountDate;
	}
	/**
	 * 结算批次号
	 * @param batchId
	 */
	public void setBatchId(java.lang.String batchId) {
		this.batchId = batchId;
	}
	
	/**
	 * 结算批次号
	 * @return
	 */
	public java.lang.String getBatchId() {
		return this.batchId;
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
	 * 结算类型,1分润;2提现
	 * @param settleType
	 */
	public void setSettleType(Integer settleType) {
		this.settleType = settleType;
	}
	
	/**
	 * 结算类型,1分润;2提现
	 * @return
	 */
	public Integer getSettleType() {
		return this.settleType;
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
	 * 订单号
	 * @param orderno
	 */
	public void setOrderNo(java.lang.String orderno) {
		this.orderno = orderno;
	}
	
	/**
	 * 订单号
	 * @return
	 */
	public java.lang.String getOrderNo() {
		return this.orderno;
	}
	/**
	 * 关联商户号
	 * @param accountrelateid
	 */
	public void setAccountRelateId(java.lang.String accountrelateid) {
		this.accountrelateid = accountrelateid;
	}
	
	/**
	 * 关联商户号
	 * @return
	 */
	public java.lang.String getAccountRelateId() {
		return this.accountrelateid;
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