/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.pojo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * CreditRepaymentHistory
 * @author code-generator
 *
 */
@XStreamAlias("creditrepaymenthistory")
public class CreditRepaymentHistory implements Serializable{
	private static final long serialVersionUID = 1L;
	@XStreamAlias("repayid")
	private java.lang.String repayId;
	@XStreamAlias("daysumid")
	private java.lang.Integer daysumId;
	@XStreamAlias("accountdate")
	private java.util.Date accountDate;
	@XStreamAlias("userid")
	private java.lang.String userId;
	@XStreamAlias("creditagreementid")
	private java.lang.String creditAgreementId;
	@XStreamAlias("daysinterestrate")
	private java.lang.String daysInterestRate;
	@XStreamAlias("capitaldays")
	private java.lang.Long capitalDays;
	@XStreamAlias("capital")
	private java.lang.Long capital;
	@XStreamAlias("interestrate")
	private java.lang.String interestRate;
	@XStreamAlias("overdueflag")
	private Integer overdueFlag;
	@XStreamAlias("fixedinterest")
	private java.lang.Long fixedInterest;
	@XStreamAlias("repaymentdate")
	private java.util.Date repaymentDate;
	@XStreamAlias("repaydate")
	private java.util.Date repaidDate;
	@XStreamAlias("createdtime")
	private java.util.Date createdTime;
	@XStreamAlias("updatedtime")
	private java.util.Date updatedTime;

	/**
	 * 还款单号,实际还款日期和还款流水号
	 * @param repayId
	 */
	public void setRepayId(java.lang.String repayId) {
		this.repayId = repayId;
	}
	
	/**
	 * 还款单号,实际还款日期和还款流水号
	 * @return
	 */
	public java.lang.String getRepayId() {
		return this.repayId;
	}
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
	 * @param daysInterestRate
	 */
	public void setDaysInterestRate(java.lang.String daysInterestRate) {
		this.daysInterestRate = daysInterestRate;
	}
	
	/**
	 * 日利率
	 * @return
	 */
	public java.lang.String getDaysInterestRate() {
		return this.daysInterestRate;
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
	 * 还款本金(分)
	 * @param capital
	 */
	public void setCapital(java.lang.Long capital) {
		this.capital = capital;
	}
	
	/**
	 * 还款本金(分)
	 * @return
	 */
	public java.lang.Long getCapital() {
		return this.capital;
	}
	/**
	 * 利率
	 * @param interestRate
	 */
	public void setInterestRate(java.lang.String interestRate) {
		this.interestRate = interestRate;
	}
	
	/**
	 * 利率
	 * @return
	 */
	public java.lang.String getInterestRate() {
		return this.interestRate;
	}
	/**
	 * 逾期标志,0未逾期,1逾期
	 * @param overdueFlag
	 */
	public void setOverdueFlag(Integer overdueFlag) {
		this.overdueFlag = overdueFlag;
	}
	
	/**
	 * 逾期标志,0未逾期,1逾期
	 * @return
	 */
	public Integer getOverdueFlag() {
		return this.overdueFlag;
	}
	/**
	 * 固定利息(分)
	 * @param fixedInterest
	 */
	public void setFixedInterest(java.lang.Long fixedInterest) {
		this.fixedInterest = fixedInterest;
	}
	
	/**
	 * 固定利息(分)
	 * @return
	 */
	public java.lang.Long getFixedInterest() {
		return this.fixedInterest;
	}
	/**
	 * 还款日
	 * @param repaymentDate
	 */
	public void setRepaymentDate(java.util.Date repaymentDate) {
		this.repaymentDate = repaymentDate;
	}
	
	/**
	 * 还款日
	 * @return
	 */
	public java.util.Date getRepaymentDate() {
		return this.repaymentDate;
	}
	/**
	 * 实际还款日期
	 * @param repaidDate
	 */
	public void setRepaidDate(java.util.Date repaidDate) {
		this.repaidDate = repaidDate;
	}
	
	/**
	 * 实际还款日期
	 * @return
	 */
	public java.util.Date getRepaidDate() {
		return this.repaidDate;
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