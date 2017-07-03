/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.pojo;

import java.io.Serializable;

/**
 * CreditRepaymentMonthSummaryQuery
 * @author code-generator
 *
 */
public class CreditRepaymentMonthSummaryQuery implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.String userId;
	private java.lang.Integer creditDate;
	private java.lang.String creditAgreementId;
	private java.lang.String interestDaysRate;
	private java.util.Date repaymentCurrentDate;
	private java.lang.Long capitalMonth;
	private java.lang.Long interestFix;
	private java.lang.Long interestOverdue;
	private Integer statusId;
	private java.lang.String accountFlag;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;

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
	 * 月消费金额汇总(分)
	 * @param capitalMonth
	 */
	public void setCapitalMonth(java.lang.Long capitalMonth) {
		this.capitalMonth = capitalMonth;
	}
	
	/**
	 * 月消费金额汇总(分)
	 * @return
	 */
	public java.lang.Long getCapitalMonth() {
		return this.capitalMonth;
	}
	/**
	 * 固定利息(分)
	 * @param interestFix
	 */
	public void setInterestFix(java.lang.Long interestFix) {
		this.interestFix = interestFix;
	}
	
	/**
	 * 固定利息(分)
	 * @return
	 */
	public java.lang.Long getInterestFix() {
		return this.interestFix;
	}
	/**
	 * 逾期利息(分)
	 * @param interestOverdue
	 */
	public void setInterestOverdue(java.lang.Long interestOverdue) {
		this.interestOverdue = interestOverdue;
	}
	
	/**
	 * 逾期利息(分)
	 * @return
	 */
	public java.lang.Long getInterestOverdue() {
		return this.interestOverdue;
	}
	/**
	 * 还款结果状态,0未还款,1还款成功,2还款失败
	 * @param statusId
	 */
	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
	
	/**
	 * 还款结果状态,0未还款,1还款成功,2还款失败
	 * @return
	 */
	public Integer getStatusId() {
		return this.statusId;
	}
	/**
	 * 出账标志,0未出账,1出账
	 * @param accountFlag
	 */
	public void setAccountFlag(java.lang.String accountFlag) {
		this.accountFlag = accountFlag;
	}
	
	/**
	 * 出账标志,0未出账,1出账
	 * @return
	 */
	public java.lang.String getAccountFlag() {
		return this.accountFlag;
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