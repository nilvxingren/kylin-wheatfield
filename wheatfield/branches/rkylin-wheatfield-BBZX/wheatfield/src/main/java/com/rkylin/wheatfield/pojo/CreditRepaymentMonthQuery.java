/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.pojo;

import java.io.Serializable;

/**
 * CreditRepaymentMonthQuery
 * @author code-generator
 *
 */
public class CreditRepaymentMonthQuery implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.String userId;
	private java.lang.Integer creditDate;
	private java.lang.Long capitalMonth;
	private java.lang.Long interestFix;
	private java.lang.Long capitalAll;
	private java.lang.Long interestAll;
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
	 * 所有费金额汇总(分)
	 * @param capitalAll
	 */
	public void setCapitalAll(java.lang.Long capitalAll) {
		this.capitalAll = capitalAll;
	}
	
	/**
	 * 所有费金额汇总(分)
	 * @return
	 */
	public java.lang.Long getCapitalAll() {
		return this.capitalAll;
	}
	/**
	 * 所有利息(分)
	 * @param interestAll
	 */
	public void setInterestAll(java.lang.Long interestAll) {
		this.interestAll = interestAll;
	}
	
	/**
	 * 所有利息(分)
	 * @return
	 */
	public java.lang.Long getInterestAll() {
		return this.interestAll;
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