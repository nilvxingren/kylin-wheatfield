/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.pojo;

import java.io.Serializable;

/**
 * CreditRepaymentDaysSummary
 * @author code-generator
 *
 */
public class CreditRepaymentDaysSummary implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.Integer daysumId;
	private java.util.Date accountDate;
	private java.lang.String userId;
	private java.lang.String creditAgreementId;
	private java.lang.String interestDaysRate;
	private java.lang.Long capitalDays;
	private java.util.Date repaymentCurrentDate;
	private java.lang.Integer creditDate;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;

	/**
	 * 日汇总记录号
	 * @param daysumId
	 */
	public void setDaysumId(java.lang.Integer daysumId) {
		this.daysumId = daysumId;
	}
	
	/**
	 * 日汇总记录号
	 * @return
	 */
	public java.lang.Integer getDaysumId() {
		return this.daysumId;
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
	 * 授信协议ID
	 * @param creditAgreementId
	 */
	public void setCreditAgreementId(java.lang.String creditAgreementId) {
		this.creditAgreementId = creditAgreementId;
	}
	
	/**
	 * 授信协议ID
	 * @return
	 */
	public java.lang.String getCreditAgreementId() {
		return this.creditAgreementId;
	}
	/**
	 * 日利率
	 * @param interestDaysRate
	 */
	public void setInterestDaysRate(java.lang.String interestDaysRate) {
		this.interestDaysRate = interestDaysRate;
	}
	
	/**
	 * 日利率
	 * @return
	 */
	public java.lang.String getInterestDaysRate() {
		return this.interestDaysRate;
	}
	/**
	 * 日消费金额汇总(分)
	 * @param capitalDays
	 */
	public void setCapitalDays(java.lang.Long capitalDays) {
		this.capitalDays = capitalDays;
	}
	
	/**
	 * 日消费金额汇总(分)
	 * @return
	 */
	public java.lang.Long getCapitalDays() {
		return this.capitalDays;
	}
	/**
	 * 本期应还款日
	 * @param repaymentCurrentDate
	 */
	public void setRepaymentCurrentDate(java.util.Date repaymentCurrentDate) {
		this.repaymentCurrentDate = repaymentCurrentDate;
	}
	
	/**
	 * 本期应还款日
	 * @return
	 */
	public java.util.Date getRepaymentCurrentDate() {
		return this.repaymentCurrentDate;
	}
	/**
	 * 信用账期
	 * @param creditDate
	 */
	public void setCreditDate(java.lang.Integer creditDate) {
		this.creditDate = creditDate;
	}
	
	/**
	 * 信用账期
	 * @return
	 */
	public java.lang.Integer getCreditDate() {
		return this.creditDate;
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