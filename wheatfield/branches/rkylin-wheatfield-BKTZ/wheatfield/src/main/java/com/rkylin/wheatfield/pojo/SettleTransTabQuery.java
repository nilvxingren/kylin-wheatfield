/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.pojo;

import java.io.Serializable;

/**
 * SettleTransTabQuery
 * @author code-generator
 *
 */
public class SettleTransTabQuery implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.Integer tabId;
	private java.lang.String orderNo;
	private java.lang.String tabNo;
	private java.lang.String rootInstCd;
	private java.lang.String groupManage;
	private java.lang.String batchNo;
	private java.lang.String userId;
	private java.lang.String referUserId;
	private java.lang.Long amount;
	private java.lang.String obligate1;
	private java.lang.String obligate2;
	private Integer statusId;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;

	/**
	 * 挂账ID
	 * @param tabId
	 */
	public void setTabId(java.lang.Integer tabId) {
		this.tabId = tabId;
	}
	
	/**
	 * 挂账ID
	 * @return
	 */
	public java.lang.Integer getTabId() {
		return this.tabId;
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
	 * 挂账条目=订单号+0001
	 * @param tabNo
	 */
	public void setTabNo(java.lang.String tabNo) {
		this.tabNo = tabNo;
	}
	
	/**
	 * 挂账条目=订单号+0001
	 * @return
	 */
	public java.lang.String getTabNo() {
		return this.tabNo;
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
	 * 管理分组
	 * @param groupManage
	 */
	public void setGroupManage(java.lang.String groupManage) {
		this.groupManage = groupManage;
	}
	
	/**
	 * 管理分组
	 * @return
	 */
	public java.lang.String getGroupManage() {
		return this.groupManage;
	}
	/**
	 * 批次号
	 * @param batchNo
	 */
	public void setBatchNo(java.lang.String batchNo) {
		this.batchNo = batchNo;
	}
	
	/**
	 * 批次号
	 * @return
	 */
	public java.lang.String getBatchNo() {
		return this.batchNo;
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
	 * 第三方账户ID
	 * @param referUserId
	 */
	public void setReferUserId(java.lang.String referUserId) {
		this.referUserId = referUserId;
	}
	
	/**
	 * 第三方账户ID
	 * @return
	 */
	public java.lang.String getReferUserId() {
		return this.referUserId;
	}
	/**
	 * 挂账金额
	 * @param amount
	 */
	public void setAmount(java.lang.Long amount) {
		this.amount = amount;
	}
	
	/**
	 * 挂账金额
	 * @return
	 */
	public java.lang.Long getAmount() {
		return this.amount;
	}
	/**
	 * 预留字段1
	 * @param obligate1
	 */
	public void setObligate1(java.lang.String obligate1) {
		this.obligate1 = obligate1;
	}
	
	/**
	 * 预留字段1
	 * @return
	 */
	public java.lang.String getObligate1() {
		return this.obligate1;
	}
	/**
	 * 预留字段2
	 * @param obligate2
	 */
	public void setObligate2(java.lang.String obligate2) {
		this.obligate2 = obligate2;
	}
	
	/**
	 * 预留字段2
	 * @return
	 */
	public java.lang.String getObligate2() {
		return this.obligate2;
	}
	/**
	 * 状态,0未处理,1已处理,2不处理
	 * @param statusId
	 */
	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
	
	/**
	 * 状态,0未处理,1已处理,2不处理
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