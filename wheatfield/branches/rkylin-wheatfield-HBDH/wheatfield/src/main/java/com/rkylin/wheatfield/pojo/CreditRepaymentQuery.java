/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.pojo;

import java.io.Serializable;

/**
 * CreditRepaymentQuery
 * @author code-generator
 *
 */
public class CreditRepaymentQuery implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.Integer credId;
	private java.lang.String userId;
	private java.lang.String creditAgreementId;
	private java.lang.Long capital;
	private java.lang.String interestRate;
	private Integer overdueFlag;
	private java.lang.Long interestFree;
	private java.lang.Long interest;
	private java.util.Date repaymentDate;
	private java.util.Date repaymentRepaidDate;
	private Integer statusId;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;

	/**
	 * 还款记录号
	 * @param credId
	 */
	public void setCredId(java.lang.Integer credId) {
		this.credId = credId;
	}
	
	/**
	 * 还款记录号
	 * @return
	 */
	public java.lang.Integer getCredId() {
		return this.credId;
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
	 * 本金
	 * @param capital
	 */
	public void setCapital(java.lang.Long capital) {
		this.capital = capital;
	}
	
	/**
	 * 本金
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
	 * 免息数(分)，由于系统原因扣款失败此字段不变
	 * @param interestFree
	 */
	public void setInterestFree(java.lang.Long interestFree) {
		this.interestFree = interestFree;
	}
	
	/**
	 * 免息数(分)，由于系统原因扣款失败此字段不变
	 * @return
	 */
	public java.lang.Long getInterestFree() {
		return this.interestFree;
	}
	/**
	 * 利息(分)
	 * @param interest
	 */
	public void setInterest(java.lang.Long interest) {
		this.interest = interest;
	}
	
	/**
	 * 利息(分)
	 * @return
	 */
	public java.lang.Long getInterest() {
		return this.interest;
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
	 * @param repaymentRepaidDate
	 */
	public void setRepaymentRepaidDate(java.util.Date repaymentRepaidDate) {
		this.repaymentRepaidDate = repaymentRepaidDate;
	}
	
	/**
	 * 实际还款日期
	 * @return
	 */
	public java.util.Date getRepaymentRepaidDate() {
		return this.repaymentRepaidDate;
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