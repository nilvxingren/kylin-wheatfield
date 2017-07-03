package com.rkylin.wheatfield.pojo;

import java.io.Serializable;

public class TlBankCode implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.Integer bankId;
	private java.lang.String bankCode;
	private java.lang.String bankName;
	private java.lang.String updateBatch;
	private Integer statusId;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;

	/**
	 * 银行编码ID
	 * @param bankId
	 */
	public void setBankId(java.lang.Integer bankId) {
		this.bankId = bankId;
	}
	
	/**
	 * 银行编码ID
	 * @return
	 */
	public java.lang.Integer getBankId() {
		return this.bankId;
	}
	/**
	 * 总行编码
	 * @param bankCode
	 */
	public void setBankCode(java.lang.String bankCode) {
		this.bankCode = bankCode;
	}
	
	/**
	 * 总行编码
	 * @return
	 */
	public java.lang.String getBankCode() {
		return this.bankCode;
	}
	/**
	 * 银行名称
	 * @param bankName
	 */
	public void setBankName(java.lang.String bankName) {
		this.bankName = bankName;
	}
	
	/**
	 * 银行名称
	 * @return
	 */
	public java.lang.String getBankName() {
		return this.bankName;
	}
	/**
	 * 更新批次号
	 * @param updateBatch
	 */
	public void setUpdateBatch(java.lang.String updateBatch) {
		this.updateBatch = updateBatch;
	}
	
	/**
	 * 更新批次号
	 * @return
	 */
	public java.lang.String getUpdateBatch() {
		return this.updateBatch;
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