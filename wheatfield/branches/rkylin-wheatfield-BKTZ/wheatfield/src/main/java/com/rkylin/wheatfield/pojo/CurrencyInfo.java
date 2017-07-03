/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.pojo;

import java.io.Serializable;

/**
 * CurrencyInfo
 * @author code-generator
 *
 */
public class CurrencyInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.Integer currencyId;
	private java.lang.String currency;
	private java.lang.String currencyName;
	private java.lang.Long numberCode;
	private Integer status;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;

	/**
	 * 币种编码ID
	 * @param currencyId
	 */
	public void setCurrencyId(java.lang.Integer currencyId) {
		this.currencyId = currencyId;
	}
	
	/**
	 * 币种编码ID
	 * @return
	 */
	public java.lang.Integer getCurrencyId() {
		return this.currencyId;
	}
	/**
	 * 币种
	 * @param currency
	 */
	public void setCurrency(java.lang.String currency) {
		this.currency = currency;
	}
	
	/**
	 * 币种
	 * @return
	 */
	public java.lang.String getCurrency() {
		return this.currency;
	}
	/**
	 * 币种名称
	 * @param currencyName
	 */
	public void setCurrencyName(java.lang.String currencyName) {
		this.currencyName = currencyName;
	}
	
	/**
	 * 币种名称
	 * @return
	 */
	public java.lang.String getCurrencyName() {
		return this.currencyName;
	}
	/**
	 * 币种数字编码
	 * @param numberCode
	 */
	public void setNumberCode(java.lang.Long numberCode) {
		this.numberCode = numberCode;
	}
	
	/**
	 * 币种数字编码
	 * @return
	 */
	public java.lang.Long getNumberCode() {
		return this.numberCode;
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