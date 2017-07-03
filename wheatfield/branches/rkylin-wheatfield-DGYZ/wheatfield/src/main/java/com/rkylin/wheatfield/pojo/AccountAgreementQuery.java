/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.pojo;

import java.io.Serializable;

/**
 * AccountAgreementQuery
 * @author code-generator
 *
 */
public class AccountAgreementQuery implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.String agmtId;
	private java.lang.String agmtCode;
	private java.lang.String agmtName;
	private java.lang.String firParty;
	private java.lang.String secParty;
	private java.lang.String thdParty;
	private Integer status;
	private java.lang.String agmtContent;
	private java.lang.String productId;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;

	/**
	 * 协议ID
	 * @param agmtId
	 */
	public void setAgmtId(java.lang.String agmtId) {
		this.agmtId = agmtId;
	}
	
	/**
	 * 协议ID
	 * @return
	 */
	public java.lang.String getAgmtId() {
		return this.agmtId;
	}
	/**
	 * 协议编号
	 * @param agmtCode
	 */
	public void setAgmtCode(java.lang.String agmtCode) {
		this.agmtCode = agmtCode;
	}
	
	/**
	 * 协议编号
	 * @return
	 */
	public java.lang.String getAgmtCode() {
		return this.agmtCode;
	}
	/**
	 * 协议名称
	 * @param agmtName
	 */
	public void setAgmtName(java.lang.String agmtName) {
		this.agmtName = agmtName;
	}
	
	/**
	 * 协议名称
	 * @return
	 */
	public java.lang.String getAgmtName() {
		return this.agmtName;
	}
	/**
	 * 甲方
	 * @param firParty
	 */
	public void setFirParty(java.lang.String firParty) {
		this.firParty = firParty;
	}
	
	/**
	 * 甲方
	 * @return
	 */
	public java.lang.String getFirParty() {
		return this.firParty;
	}
	/**
	 * 乙方
	 * @param secParty
	 */
	public void setSecParty(java.lang.String secParty) {
		this.secParty = secParty;
	}
	
	/**
	 * 乙方
	 * @return
	 */
	public java.lang.String getSecParty() {
		return this.secParty;
	}
	/**
	 * 丙方
	 * @param thdParty
	 */
	public void setThdParty(java.lang.String thdParty) {
		this.thdParty = thdParty;
	}
	
	/**
	 * 丙方
	 * @return
	 */
	public java.lang.String getThdParty() {
		return this.thdParty;
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
	 * 协议内容
	 * @param agmtContent
	 */
	public void setAgmtContent(java.lang.String agmtContent) {
		this.agmtContent = agmtContent;
	}
	
	/**
	 * 协议内容
	 * @return
	 */
	public java.lang.String getAgmtContent() {
		return this.agmtContent;
	}
	/**
	 * 产品号
	 * @param productId
	 */
	public void setProductId(java.lang.String productId) {
		this.productId = productId;
	}
	
	/**
	 * 产品号
	 * @return
	 */
	public java.lang.String getProductId() {
		return this.productId;
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