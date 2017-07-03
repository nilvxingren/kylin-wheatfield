package com.rkylin.wheatfield.pojo;

import java.io.Serializable;

/**
 * FinanaceAccountAuth
 * @author code-generator
 *
 */
public class FinanaceAccountAuth implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.Long finAntAuthId;
	private java.lang.String authTypeId;
	private java.lang.String finAccountId;
	private java.lang.String authCode;
	private java.lang.String referEntryId;
	private java.lang.Long amount;
	private java.lang.String statusId;
	private java.util.Date startTime;
	private java.util.Date endTime;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;

	/**
	 * 账户授权ID
	 * @param finAntAuthId
	 */
	public void setFinAntAuthId(java.lang.Long finAntAuthId) {
		this.finAntAuthId = finAntAuthId;
	}
	
	/**
	 * 账户授权ID
	 * @return
	 */
	public java.lang.Long getFinAntAuthId() {
		return this.finAntAuthId;
	}
	/**
	 * 授权类型ID
	 * @param authTypeId
	 */
	public void setAuthTypeId(java.lang.String authTypeId) {
		this.authTypeId = authTypeId;
	}
	
	/**
	 * 授权类型ID
	 * @return
	 */
	public java.lang.String getAuthTypeId() {
		return this.authTypeId;
	}
	/**
	 * 账户ID
	 * @param finAccountId
	 */
	public void setFinAccountId(java.lang.String finAccountId) {
		this.finAccountId = finAccountId;
	}
	
	/**
	 * 账户ID
	 * @return
	 */
	public java.lang.String getFinAccountId() {
		return this.finAccountId;
	}
	/**
	 * 授权码
	 * @param authCode
	 */
	public void setAuthCode(java.lang.String authCode) {
		this.authCode = authCode;
	}
	
	/**
	 * 授权码
	 * @return
	 */
	public java.lang.String getAuthCode() {
		return this.authCode;
	}
	/**
	 * 套录ID
	 * @param referEntryId
	 */
	public void setReferEntryId(java.lang.String referEntryId) {
		this.referEntryId = referEntryId;
	}
	
	/**
	 * 套录ID
	 * @return
	 */
	public java.lang.String getReferEntryId() {
		return this.referEntryId;
	}
	/**
	 * 金额
	 * @param amount
	 */
	public void setAmount(java.lang.Long amount) {
		this.amount = amount;
	}
	
	/**
	 * 金额
	 * @return
	 */
	public java.lang.Long getAmount() {
		return this.amount;
	}
	/**
	 * 账户状态,0失效,1生效
	 * @param statusId
	 */
	public void setStatusId(java.lang.String statusId) {
		this.statusId = statusId;
	}
	
	/**
	 * 账户状态,0失效,1生效
	 * @return
	 */
	public java.lang.String getStatusId() {
		return this.statusId;
	}
	/**
	 * 生效时间
	 * @param startTime
	 */
	public void setStartTime(java.util.Date startTime) {
		this.startTime = startTime;
	}
	
	/**
	 * 生效时间
	 * @return
	 */
	public java.util.Date getStartTime() {
		return this.startTime;
	}
	/**
	 * 失效时间
	 * @param endTime
	 */
	public void setEndTime(java.util.Date endTime) {
		this.endTime = endTime;
	}
	
	/**
	 * 失效时间
	 * @return
	 */
	public java.util.Date getEndTime() {
		return this.endTime;
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