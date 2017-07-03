/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.pojo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * InterestRepayment
 * @author code-generator
 *
 */
@XStreamAlias("repayment")
public class InterestRepayment implements Serializable{
	private static final long serialVersionUID = 1L;
	@XStreamAlias("interid")
	private java.lang.Integer interId;
	@XStreamAlias("rootinstcd")
	private java.lang.String rootInstCd;
	@XStreamAlias("productid")
	private java.lang.String productId;
	@XStreamAlias("providerid")
	private java.lang.String providerId;
	@XStreamAlias("userid")
	private java.lang.String userId;
	@XStreamAlias("creditresultid")
	private java.lang.String creditResultId;
	@XStreamAlias("userorderid")
	private java.lang.String userOrderId;
	@XStreamAlias("orderid")
	private java.lang.String orderId;
	@XStreamAlias("overdueorderid")
	private java.lang.String overdueOrderId;
	@XStreamAlias("ordertype")
	private Integer orderType;
	@XStreamAlias("creditagreementid")
	private java.lang.String creditAgreementId;
	@XStreamAlias("periodsummary")
	private java.lang.String periodSummary;
	@XStreamAlias("periodcurrent")
	private java.lang.String periodCurrent;
	@XStreamAlias("shouldrepaymentdate")
	private java.util.Date shouldRepaymentDate;
	@XStreamAlias("shouldcapital")
	private java.lang.Long shouldCapital;
	@XStreamAlias("shouldinterest")
	private java.lang.Long shouldInterest;
	@XStreamAlias("shouldamount")
	private java.lang.Long shouldAmount;
	@XStreamAlias("repaidrepaymentdate")
	private java.util.Date repaidRepaymentDate;
	@XStreamAlias("repaidinterest")
	private java.lang.Long repaidInterest;
	@XStreamAlias("repaidamount")
	private java.lang.Long repaidAmount;
	@XStreamAlias("interestfree")
	private java.lang.Long interestFree;
	@XStreamAlias("overdueflag_a")
	private Integer overdueFlag1;
	@XStreamAlias("overdueflag_b")
	private Integer overdueFlag2;
	@XStreamAlias("overduedays")
	private java.lang.Integer overdueDays;
	@XStreamAlias("overduefine")
	private java.lang.Long overdueFine;
	@XStreamAlias("overdueinterest")
	private java.lang.Long overdueInterest;
	@XStreamAlias("advanceamount")
	private java.lang.Long advanceAmount;
	@XStreamAlias("advanceinterest")
	private java.lang.Long advanceInterest;
	@XStreamAlias("overplusamount")
	private java.lang.Long overplusAmount;
	@XStreamAlias("statusid")
	private Integer statusId;
	@XStreamAlias("createdtime")
	private java.util.Date createdTime;
	@XStreamAlias("updatedtime")
	private java.util.Date updatedTime;
	private java.lang.Integer interestParty;
	private java.lang.Integer isSucInterest;
	private java.lang.Integer isSucCapital;
	@XStreamAlias("authCode")
	private String authCode;

	/**
	 * 还款记录号
	 * @param interId
	 */
	public void setInterId(java.lang.Integer interId) {
		this.interId = interId;
	}
	
	/**
	 * 还款记录号
	 * @return
	 */
	public java.lang.Integer getInterId() {
		return this.interId;
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
	 * 订单类型,1正常,2提前还款
	 * @param orderType
	 */
	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}
	
	/**
	 * 订单类型,1正常,2提前还款
	 * @return
	 */
	public Integer getOrderType() {
		return this.orderType;
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
	 * 期数汇总
	 * @param periodSummary
	 */
	public void setPeriodSummary(java.lang.String periodSummary) {
		this.periodSummary = periodSummary;
	}
	
	/**
	 * 期数汇总
	 * @return
	 */
	public java.lang.String getPeriodSummary() {
		return this.periodSummary;
	}
	/**
	 * 当前期数
	 * @param periodCurrent
	 */
	public void setPeriodCurrent(java.lang.String periodCurrent) {
		this.periodCurrent = periodCurrent;
	}
	
	/**
	 * 当前期数
	 * @return
	 */
	public java.lang.String getPeriodCurrent() {
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
	 * 实际还款日期
	 * @param repaidRepaymentDate
	 */
	public void setRepaidRepaymentDate(java.util.Date repaidRepaymentDate) {
		this.repaidRepaymentDate = repaidRepaymentDate;
	}
	
	/**
	 * 实际还款日期
	 * @return
	 */
	public java.util.Date getRepaidRepaymentDate() {
		return this.repaidRepaymentDate;
	}
	/**
	 * 实际利息(分)
	 * @param repaidInterest
	 */
	public void setRepaidInterest(java.lang.Long repaidInterest) {
		this.repaidInterest = repaidInterest;
	}
	
	/**
	 * 实际利息(分)
	 * @return
	 */
	public java.lang.Long getRepaidInterest() {
		return this.repaidInterest;
	}
	/**
	 * 实际还款金额(分)
	 * @param repaidAmount
	 */
	public void setRepaidAmount(java.lang.Long repaidAmount) {
		this.repaidAmount = repaidAmount;
	}
	
	/**
	 * 实际还款金额(分)
	 * @return
	 */
	public java.lang.Long getRepaidAmount() {
		return this.repaidAmount;
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
	 * 逾期标志2,0未逾期,1逾期,2提前还款
	 * @param overdueFlag2
	 */
	public void setOverdueFlag2(Integer overdueFlag2) {
		this.overdueFlag2 = overdueFlag2;
	}
	
	/**
	 * 逾期标志2,0未逾期,1逾期,2提前还款
	 * @return
	 */
	public Integer getOverdueFlag2() {
		return this.overdueFlag2;
	}
	/**
	 * 逾期天数
	 * @param overdueDays
	 */
	public void setOverdueDays(java.lang.Integer overdueDays) {
		this.overdueDays = overdueDays;
	}
	
	/**
	 * 逾期天数
	 * @return
	 */
	public java.lang.Integer getOverdueDays() {
		return this.overdueDays;
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
	 * 提前还款本金(分)
	 * @param advanceAmount
	 */
	public void setAdvanceAmount(java.lang.Long advanceAmount) {
		this.advanceAmount = advanceAmount;
	}
	
	/**
	 * 提前还款本金(分)
	 * @return
	 */
	public java.lang.Long getAdvanceAmount() {
		return this.advanceAmount;
	}
	/**
	 * 提前还款利息(分)
	 * @param advanceInterest
	 */
	public void setAdvanceInterest(java.lang.Long advanceInterest) {
		this.advanceInterest = advanceInterest;
	}
	
	/**
	 * 提前还款利息(分)
	 * @return
	 */
	public java.lang.Long getAdvanceInterest() {
		return this.advanceInterest;
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
	
	/**
	 * 获取利息承担方
	 * @return
	 */
	public java.lang.Integer getInterestParty() {
		return interestParty;
	}
    /**
     * 设置利息承担方
     * @param interestParty
     */
	public void setInterestParty(java.lang.Integer interestParty) {
		this.interestParty = interestParty;
	}

	/**
	 * 利息是否还完
	 * @return
	 */
	public java.lang.Integer getIsSucInterest() {
		return isSucInterest;
	}

	/**
	 * 利息是否还完
	 * @param isSucInterest
	 */
	public void setIsSucInterest(java.lang.Integer isSucInterest) {
		this.isSucInterest = isSucInterest;
	}

	/**
	 * 本金是否还完
	 * @return
	 */
	public java.lang.Integer getIsSucCapital() {
		return isSucCapital;
	}

	/**
	 * 本金是否还完
	 * @param isSucCapital
	 */
	public void setIsSucCapital(java.lang.Integer isSucCapital) {
		this.isSucCapital = isSucCapital;
	}

	/**
	 * 解冻授权码
	 * @return
	 */
	public String getAuthCode() {
		return authCode;
	}
	/**
	 * 解冻授权码
	 * @return
	 */
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	@Override
	public String toString() {
		return "InterestRepayment [interId=" + interId + ", rootInstCd="
				+ rootInstCd + ", productId=" + productId + ", providerId="
				+ providerId + ", userId=" + userId + ", creditResultId="
				+ creditResultId + ", userOrderId=" + userOrderId
				+ ", orderId=" + orderId + ", orderType=" + orderType
				+ ", creditAgreementId=" + creditAgreementId
				+ ", periodSummary=" + periodSummary + ", periodCurrent="
				+ periodCurrent + ", shouldRepaymentDate="
				+ shouldRepaymentDate + ", shouldCapital=" + shouldCapital
				+ ", shouldInterest=" + shouldInterest + ", shouldAmount="
				+ shouldAmount + ", repaidRepaymentDate=" + repaidRepaymentDate
				+ ", repaidInterest=" + repaidInterest + ", repaidAmount="
				+ repaidAmount + ", interestFree=" + interestFree
				+ ", overdueFlag1=" + overdueFlag1 + ", overdueFlag2="
				+ overdueFlag2 + ", overdueDays=" + overdueDays
				+ ", overdueFine=" + overdueFine + ", overdueInterest="
				+ overdueInterest + ", advanceAmount=" + advanceAmount
				+ ", advanceInterest=" + advanceInterest + ", overplusAmount="
				+ overplusAmount + ", statusId=" + statusId + ", createdTime="
				+ createdTime + ", updatedTime=" + updatedTime + "]";
	}
}