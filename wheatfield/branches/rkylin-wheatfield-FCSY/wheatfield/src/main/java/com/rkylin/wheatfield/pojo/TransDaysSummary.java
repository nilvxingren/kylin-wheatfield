package com.rkylin.wheatfield.pojo;

import java.io.Serializable;

/**
 * TransDaysSummary
 * @author code-generator
 *
 */
public class TransDaysSummary implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.String transSumId;
	private java.lang.String rootInstCd;
	private java.lang.String userId;
	private java.lang.String orderType;
	private java.util.Date accountDate;
	private java.lang.String summaryOrders;
	private java.lang.Long summaryAmount;
	private Integer statusId;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;

	/**
	 * 汇总条目
	 * @param transSumId
	 */
	public void setTransSumId(java.lang.String transSumId) {
		this.transSumId = transSumId;
	}
	
	/**
	 * 汇总条目
	 * @return
	 */
	public java.lang.String getTransSumId() {
		return this.transSumId;
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
	 * 用户号
	 * @param userId
	 */
	public void setUserId(java.lang.String userId) {
		this.userId = userId;
	}
	
	/**
	 * 用户号
	 * @return
	 */
	public java.lang.String getUserId() {
		return this.userId;
	}
	/**
	 * 订单类型
	 * @param orderType
	 */
	public void setOrderType(java.lang.String orderType) {
		this.orderType = orderType;
	}
	
	/**
	 * 订单类型
	 * @return
	 */
	public java.lang.String getOrderType() {
		return this.orderType;
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
	 * 订单汇总
	 * @param summaryOrders
	 */
	public void setSummaryOrders(java.lang.String summaryOrders) {
		this.summaryOrders = summaryOrders;
	}
	
	/**
	 * 订单汇总
	 * @return
	 */
	public java.lang.String getSummaryOrders() {
		return this.summaryOrders;
	}
	/**
	 * 汇总金额(分)
	 * @param summaryAmount
	 */
	public void setSummaryAmount(java.lang.Long summaryAmount) {
		this.summaryAmount = summaryAmount;
	}
	
	/**
	 * 汇总金额(分)
	 * @return
	 */
	public java.lang.Long getSummaryAmount() {
		return this.summaryAmount;
	}
	/**
	 * 状态,0失效,1生效
	 * @param statusId
	 */
	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
	
	/**
	 * 状态,0失效,1生效
	 * @return
	 */
	public Integer getStatusId() {
		return this.statusId;
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