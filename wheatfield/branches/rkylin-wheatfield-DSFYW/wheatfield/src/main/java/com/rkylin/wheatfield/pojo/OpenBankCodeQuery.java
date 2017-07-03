/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.pojo;

import java.io.Serializable;

/**
 * OpenBankCodeQuery
 * @author code-generator
 *
 */
public class OpenBankCodeQuery implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.String cityCode;
	private java.lang.String cityName;
	private java.lang.String provinceName;
	private java.lang.String payBankCode;
	private java.lang.String openBankCode;
	private java.lang.String bankCode;
	private java.lang.String bankName;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;

	/**
	 * 地市编码
	 * @param cityCode
	 */
	public void setCityCode(java.lang.String cityCode) {
		this.cityCode = cityCode;
	}
	
	/**
	 * 地市编码
	 * @return
	 */
	public java.lang.String getCityCode() {
		return this.cityCode;
	}
	/**
	 * 地市名称
	 * @param cityName
	 */
	public void setCityName(java.lang.String cityName) {
		this.cityName = cityName;
	}
	
	/**
	 * 地市名称
	 * @return
	 */
	public java.lang.String getCityName() {
		return this.cityName;
	}
	/**
	 * 省份名称
	 * @param provinceName
	 */
	public void setProvinceName(java.lang.String provinceName) {
		this.provinceName = provinceName;
	}
	
	/**
	 * 省份名称
	 * @return
	 */
	public java.lang.String getProvinceName() {
		return this.provinceName;
	}
	/**
	 * 支付行编码
	 * @param payBankCode
	 */
	public void setPayBankCode(java.lang.String payBankCode) {
		this.payBankCode = payBankCode;
	}
	
	/**
	 * 支付行编码
	 * @return
	 */
	public java.lang.String getPayBankCode() {
		return this.payBankCode;
	}
	/**
	 * 开户行编码
	 * @param openBankCode
	 */
	public void setOpenBankCode(java.lang.String openBankCode) {
		this.openBankCode = openBankCode;
	}
	
	/**
	 * 开户行编码
	 * @return
	 */
	public java.lang.String getOpenBankCode() {
		return this.openBankCode;
	}
	/**
	 * 联行号编码
	 * @param bankCode
	 */
	public void setBankCode(java.lang.String bankCode) {
		this.bankCode = bankCode;
	}
	
	/**
	 * 联行号编码
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