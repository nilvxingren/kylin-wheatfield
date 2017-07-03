/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.pojo;

import java.io.Serializable;

/**
 * TransCode
 * @author code-generator
 *
 */
public class TransCode implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.Integer transId;
	private java.lang.String transCode;
	private java.lang.String transName;
	private Integer transType;
	private Integer transLevel;
	private Integer checkLevel;
	private Integer authLevel;
	private Integer status;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;

	/**
	 * 交易编码ID
	 * @param transId
	 */
	public void setTransId(java.lang.Integer transId) {
		this.transId = transId;
	}
	
	/**
	 * 交易编码ID
	 * @return
	 */
	public java.lang.Integer getTransId() {
		return this.transId;
	}
	/**
	 * 交易编码
	 * @param transCode
	 */
	public void setTransCode(java.lang.String transCode) {
		this.transCode = transCode;
	}
	
	/**
	 * 交易编码
	 * @return
	 */
	public java.lang.String getTransCode() {
		return this.transCode;
	}
	/**
	 * 交易编码名称
	 * @param transName
	 */
	public void setTransName(java.lang.String transName) {
		this.transName = transName;
	}
	
	/**
	 * 交易编码名称
	 * @return
	 */
	public java.lang.String getTransName() {
		return this.transName;
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
	 * 交易录入级别
	 * @param transLevel
	 */
	public void setTransLevel(Integer transLevel) {
		this.transLevel = transLevel;
	}
	
	/**
	 * 交易录入级别
	 * @return
	 */
	public Integer getTransLevel() {
		return this.transLevel;
	}
	/**
	 * 交易复核级别
	 * @param checkLevel
	 */
	public void setCheckLevel(Integer checkLevel) {
		this.checkLevel = checkLevel;
	}
	
	/**
	 * 交易复核级别
	 * @return
	 */
	public Integer getCheckLevel() {
		return this.checkLevel;
	}
	/**
	 * 交易授权级别
	 * @param authLevel
	 */
	public void setAuthLevel(Integer authLevel) {
		this.authLevel = authLevel;
	}
	
	/**
	 * 交易授权级别
	 * @return
	 */
	public Integer getAuthLevel() {
		return this.authLevel;
	}
	/**
	 * 状态
	 * @param status
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	/**
	 * 状态
	 * @return
	 */
	public Integer getStatus() {
		return this.status;
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