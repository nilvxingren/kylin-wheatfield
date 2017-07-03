/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.pojo;

import java.io.Serializable;

/**
 * InterestLoanMonth
 * @author code-generator
 *
 */
public class InterestLoanMonth implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.Integer loanMonthId;
	private java.lang.Integer creditDate;
	private java.lang.String rootInstCd;
	private java.lang.String productId;
	private java.lang.String providerId;
	private java.lang.String userId;
	private java.lang.String creditResultId;
	private java.lang.String creditAgreementId;
	private java.lang.String orderId;
	private java.lang.String overdueOrderId;
	private java.lang.String userOrderId;
	private java.lang.Long orderAmount;
	private java.lang.String rateId;
	private java.lang.String rateType;
	private java.lang.String billDay;
	private java.lang.String repaymentDay;
	private java.util.Date interestDateSingle;
	private Integer isEffective;
	private Integer periodSummary;
	private Integer periodCurrent;
	private java.util.Date shouldRepaymentDate;
	private java.lang.Long shouldCapital;
	private java.lang.Long shouldInterest;
	private java.lang.Long shouldAmount;
	private java.lang.Long interestFree;
	private Integer overdueFlag1;
	private Integer overdueFlag2;
	private Integer overdueDays;
	private Integer overdueTime;
	private java.lang.Long overdueFine;
	private java.lang.Long overdueInterest;
	private java.lang.Long overdueShouldAmount;
	private java.lang.Long overplusAmount;
	private Integer interestParty;
	private Integer statusId;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;

	/**
	 * 日汇总记录号
	 * @param loanMonthId
	 */
	public void setLoanMonthId(java.lang.Integer loanMonthId) {
		this.loanMonthId = loanMonthId;
	}
	
	/**
	 * 日汇总记录号
	 * @return
	 */
	public java.lang.Integer getLoanMonthId() {
		return this.loanMonthId;
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
	 * 管理机构代码,例丰年,会唐,课栈
	 * @param rootInstCd
	 */
	public void setRootInstCd(java.lang.String rootInstCd) {
		this.rootInstCd = rootInstCd;
	}
	
	/**
	 * 管理机构代码,例丰年,会唐,课栈
	 * @return
	 */
	public java.lang.String getRootInstCd() {
		return this.rootInstCd;
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
	 * 授信提供方ID,例JRD
	 * @param providerId
	 */
	public void setProviderId(java.lang.String providerId) {
		this.providerId = providerId;
	}
	
	/**
	 * 授信提供方ID,例JRD
	 * @return
	 */
	public java.lang.String getProviderId() {
		return this.providerId;
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
	 * 授信结果订单ID
	 * @param creditResultId
	 */
	public void setCreditResultId(java.lang.String creditResultId) {
		this.creditResultId = creditResultId;
	}
	
	/**
	 * 授信结果订单ID
	 * @return
	 */
	public java.lang.String getCreditResultId() {
		return this.creditResultId;
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
	 * 订单ID
	 * @param orderId
	 */
	public void setOrderId(java.lang.String orderId) {
		this.orderId = orderId;
	}
	
	/**
	 * 订单ID
	 * @return
	 */
	public java.lang.String getOrderId() {
		return this.orderId;
	}
	/**
	 * 提前还款订单ID
	 * @param overdueOrderId
	 */
	public void setOverdueOrderId(java.lang.String overdueOrderId) {
		this.overdueOrderId = overdueOrderId;
	}
	
	/**
	 * 提前还款订单ID
	 * @return
	 */
	public java.lang.String getOverdueOrderId() {
		return this.overdueOrderId;
	}
	/**
	 * 用户订单流水号
	 * @param userOrderId
	 */
	public void setUserOrderId(java.lang.String userOrderId) {
		this.userOrderId = userOrderId;
	}
	
	/**
	 * 用户订单流水号
	 * @return
	 */
	public java.lang.String getUserOrderId() {
		return this.userOrderId;
	}
	/**
	 * 订单金额(分)
	 * @param orderAmount
	 */
	public void setOrderAmount(java.lang.Long orderAmount) {
		this.orderAmount = orderAmount;
	}
	
	/**
	 * 订单金额(分)
	 * @return
	 */
	public java.lang.Long getOrderAmount() {
		return this.orderAmount;
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
	 * 费率类型:1:弹性产品,2:贴息产品,3:先息后本
	 * @param rateType
	 */
	public void setRateType(java.lang.String rateType) {
		this.rateType = rateType;
	}
	
	/**
	 * 费率类型:1:弹性产品,2:贴息产品,3:先息后本
	 * @return
	 */
	public java.lang.String getRateType() {
		return this.rateType;
	}
	/**
	 * 账单日
	 * @param billDay
	 */
	public void setBillDay(java.lang.String billDay) {
		this.billDay = billDay;
	}
	
	/**
	 * 账单日
	 * @return
	 */
	public java.lang.String getBillDay() {
		return this.billDay;
	}
	/**
	 * 还款日
	 * @param repaymentDay
	 */
	public void setRepaymentDay(java.lang.String repaymentDay) {
		this.repaymentDay = repaymentDay;
	}
	
	/**
	 * 还款日
	 * @return
	 */
	public java.lang.String getRepaymentDay() {
		return this.repaymentDay;
	}
	/**
	 * 计息开始时间
	 * @param interestDateSingle
	 */
	public void setInterestDateSingle(java.util.Date interestDateSingle) {
		this.interestDateSingle = interestDateSingle;
	}
	
	/**
	 * 计息开始时间
	 * @return
	 */
	public java.util.Date getInterestDateSingle() {
		return this.interestDateSingle;
	}
	/**
	 * 授信是否生效,0失效,1生效
	 * @param isEffective
	 */
	public void setIsEffective(Integer isEffective) {
		this.isEffective = isEffective;
	}
	
	/**
	 * 授信是否生效,0失效,1生效
	 * @return
	 */
	public Integer getIsEffective() {
		return this.isEffective;
	}
	/**
	 * 期数汇总
	 * @param periodSummary
	 */
	public void setPeriodSummary(Integer periodSummary) {
		this.periodSummary = periodSummary;
	}
	
	/**
	 * 期数汇总
	 * @return
	 */
	public Integer getPeriodSummary() {
		return this.periodSummary;
	}
	/**
	 * 当前期数
	 * @param periodCurrent
	 */
	public void setPeriodCurrent(Integer periodCurrent) {
		this.periodCurrent = periodCurrent;
	}
	
	/**
	 * 当前期数
	 * @return
	 */
	public Integer getPeriodCurrent() {
		return this.periodCurrent;
	}
	/**
	 * 应还日期
	 * @param shouldRepaymentDate
	 */
	public void setShouldRepaymentDate(java.util.Date shouldRepaymentDate) {
		this.shouldRepaymentDate = shouldRepaymentDate;
	}
	
	/**
	 * 应还日期
	 * @return
	 */
	public java.util.Date getShouldRepaymentDate() {
		return this.shouldRepaymentDate;
	}
	/**
	 * 应还本金(分)
	 * @param shouldCapital
	 */
	public void setShouldCapital(java.lang.Long shouldCapital) {
		this.shouldCapital = shouldCapital;
	}
	
	/**
	 * 应还本金(分)
	 * @return
	 */
	public java.lang.Long getShouldCapital() {
		return this.shouldCapital;
	}
	/**
	 * 应还利息(分)
	 * @param shouldInterest
	 */
	public void setShouldInterest(java.lang.Long shouldInterest) {
		this.shouldInterest = shouldInterest;
	}
	
	/**
	 * 应还利息(分)
	 * @return
	 */
	public java.lang.Long getShouldInterest() {
		return this.shouldInterest;
	}
	/**
	 * 应还金额(分)
	 * @param shouldAmount
	 */
	public void setShouldAmount(java.lang.Long shouldAmount) {
		this.shouldAmount = shouldAmount;
	}
	
	/**
	 * 应还金额(分)
	 * @return
	 */
	public java.lang.Long getShouldAmount() {
		return this.shouldAmount;
	}
	/**
	 * 减免利息(分)
	 * @param interestFree
	 */
	public void setInterestFree(java.lang.Long interestFree) {
		this.interestFree = interestFree;
	}
	
	/**
	 * 减免利息(分)
	 * @return
	 */
	public java.lang.Long getInterestFree() {
		return this.interestFree;
	}
	/**
	 * 逾期标志1
	 * @param overdueFlag1
	 */
	public void setOverdueFlag1(Integer overdueFlag1) {
		this.overdueFlag1 = overdueFlag1;
	}
	
	/**
	 * 逾期标志1
	 * @return
	 */
	public Integer getOverdueFlag1() {
		return this.overdueFlag1;
	}
	/**
	 * 逾期标志2,0未逾期,1逾期
	 * @param overdueFlag2
	 */
	public void setOverdueFlag2(Integer overdueFlag2) {
		this.overdueFlag2 = overdueFlag2;
	}
	
	/**
	 * 逾期标志2,0未逾期,1逾期
	 * @return
	 */
	public Integer getOverdueFlag2() {
		return this.overdueFlag2;
	}
	/**
	 * 逾期天数
	 * @param overdueDays
	 */
	public void setOverdueDays(Integer overdueDays) {
		this.overdueDays = overdueDays;
	}
	
	/**
	 * 逾期天数
	 * @return
	 */
	public Integer getOverdueDays() {
		return this.overdueDays;
	}
	/**
	 * 逾期次数
	 * @param overdueTime
	 */
	public void setOverdueTime(Integer overdueTime) {
		this.overdueTime = overdueTime;
	}
	
	/**
	 * 逾期次数
	 * @return
	 */
	public Integer getOverdueTime() {
		return this.overdueTime;
	}
	/**
	 * 逾期罚金(分)
	 * @param overdueFine
	 */
	public void setOverdueFine(java.lang.Long overdueFine) {
		this.overdueFine = overdueFine;
	}
	
	/**
	 * 逾期罚金(分)
	 * @return
	 */
	public java.lang.Long getOverdueFine() {
		return this.overdueFine;
	}
	/**
	 * 逾期利息(分)
	 * @param overdueInterest
	 */
	public void setOverdueInterest(java.lang.Long overdueInterest) {
		this.overdueInterest = overdueInterest;
	}
	
	/**
	 * 逾期利息(分)
	 * @return
	 */
	public java.lang.Long getOverdueInterest() {
		return this.overdueInterest;
	}
	/**
	 * 逾期应还金额(分)
	 * @param overdueShouldAmount
	 */
	public void setOverdueShouldAmount(java.lang.Long overdueShouldAmount) {
		this.overdueShouldAmount = overdueShouldAmount;
	}
	
	/**
	 * 逾期应还金额(分)
	 * @return
	 */
	public java.lang.Long getOverdueShouldAmount() {
		return this.overdueShouldAmount;
	}
	/**
	 * 剩余应还本金(分)
	 * @param overplusAmount
	 */
	public void setOverplusAmount(java.lang.Long overplusAmount) {
		this.overplusAmount = overplusAmount;
	}
	
	/**
	 * 剩余应还本金(分)
	 * @return
	 */
	public java.lang.Long getOverplusAmount() {
		return this.overplusAmount;
	}
	/**
	 * 利息承担方,1商户承担(默认值), 2用户承担
	 * @param interestParty
	 */
	public void setInterestParty(Integer interestParty) {
		this.interestParty = interestParty;
	}
	
	/**
	 * 利息承担方,1商户承担(默认值), 2用户承担
	 * @return
	 */
	public Integer getInterestParty() {
		return this.interestParty;
	}
	/**
	 * 扣款状态,0未扣款,1扣款成功,2扣款失败,3扣款中
	 * @param statusId
	 */
	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
	
	/**
	 * 扣款状态,0未扣款,1扣款成功,2扣款失败,3扣款中
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