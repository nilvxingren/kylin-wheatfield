/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.pojo;

import java.io.Serializable;

/**
 * CreditRateTemplateDetailQuery
 * @author code-generator
 *
 */
public class CreditRateTemplateDetailQuery implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.Integer rateDetailId;
	private java.lang.String rateId;
	private java.lang.String rateLevel;
	private java.lang.String rateTime;
	private java.lang.String rateTimeUnit;
	private java.lang.String rate;
	private java.lang.String rateUnit;
	private Integer statusId;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;

	/**
	 * 费率定义号
	 * @param rateDetailId
	 */
	public void setRateDetailId(java.lang.Integer rateDetailId) {
		this.rateDetailId = rateDetailId;
	}
	
	/**
	 * 费率定义号
	 * @return
	 */
	public java.lang.Integer getRateDetailId() {
		return this.rateDetailId;
	}
	/**
	 * 费率协议号
	 * @param rateId
	 */
	public void setRateId(java.lang.String rateId) {
		this.rateId = rateId;
	}
	
	/**
	 * 费率协议号
	 * @return
	 */
	public java.lang.String getRateId() {
		return this.rateId;
	}
	/**
	 * 费率级别
	 * @param rateLevel
	 */
	public void setRateLevel(java.lang.String rateLevel) {
		this.rateLevel = rateLevel;
	}
	
	/**
	 * 费率级别
	 * @return
	 */
	public java.lang.String getRateLevel() {
		return this.rateLevel;
	}
	/**
	 * 时间
	 * @param rateTime
	 */
	public void setRateTime(java.lang.String rateTime) {
		this.rateTime = rateTime;
	}
	
	/**
	 * 时间
	 * @return
	 */
	public java.lang.String getRateTime() {
		return this.rateTime;
	}
	/**
	 * 时间单位
	 * @param rateTimeUnit
	 */
	public void setRateTimeUnit(java.lang.String rateTimeUnit) {
		this.rateTimeUnit = rateTimeUnit;
	}
	
	/**
	 * 时间单位
	 * @return
	 */
	public java.lang.String getRateTimeUnit() {
		return this.rateTimeUnit;
	}
	/**
	 * 利率
	 * @param rate
	 */
	public void setRate(java.lang.String rate) {
		this.rate = rate;
	}
	
	/**
	 * 利率
	 * @return
	 */
	public java.lang.String getRate() {
		return this.rate;
	}
	/**
	 * 利率单位
	 * @param rateUnit
	 */
	public void setRateUnit(java.lang.String rateUnit) {
		this.rateUnit = rateUnit;
	}
	
	/**
	 * 利率单位
	 * @return
	 */
	public java.lang.String getRateUnit() {
		return this.rateUnit;
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