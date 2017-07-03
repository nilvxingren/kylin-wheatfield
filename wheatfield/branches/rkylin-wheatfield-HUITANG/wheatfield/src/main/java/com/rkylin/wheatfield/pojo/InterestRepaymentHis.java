
/*
 * Powered By code-generator
 * Web Site: http://www.rkylin.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.pojo;

import java.io.Serializable;

/**
 * InterestRepaymentHis
 * @author code-generator
 *
 */
public class InterestRepaymentHis implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer interId;
	private String rootInstCd;
	private String productId;
	private String providerId;
	private String userId;
	private String creditResultId;
	private String userOrderId;
	private String orderId;
	private String overdueOrderId;
	private Integer orderType;
	private String creditAgreementId;
	private String periodSummary;
	private String periodCurrent;
	private java.util.Date shouldRepaymentDate;
	private Long shouldCapital;
	private Long shouldInterest;
	private Long shouldAmount;
	private java.util.Date repaidRepaymentDate;
	private Long repaidInterest;
	private Long repaidAmount;
	private Long interestFree;
	private Integer overdueFlag1;
	private Integer overdueFlag2;
	private Integer overdueDays;
	private Long overdueFine;
	private Long overdueInterest;
	private Long advanceAmount;
	private Long advanceInterest;
	private Long overplusAmount;
	private Integer interestParty;
	private Integer isSucInterest;
	private Integer isSucCapital;
	private Integer statusId;
	private Integer isEffective;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;

	/**
	 * 还款记录号
	 * @param interId
	 */
	public void setInterId(Integer interId) {
		this.interId = interId;
	}
	
	/**
	 * 还款记录号
	 * @return
	 */
	public Integer getInterId() {
		return this.interId;
	}
	/**
	 * 管理机构代码,例丰年,会唐,课栈
	 * @param rootInstCd
	 */
	public void setRootInstCd(String rootInstCd) {
		this.rootInstCd = rootInstCd;
	}
	
	/**
	 * 管理机构代码,例丰年,会唐,课栈
	 * @return
	 */
	public String getRootInstCd() {
		return this.rootInstCd;
	}
	/**
	 * 产品号
	 * @param productId
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	/**
	 * 产品号
	 * @return
	 */
	public String getProductId() {
		return this.productId;
	}
	/**
	 * 授信提供方ID,例JRD
	 * @param providerId
	 */
	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
	
	/**
	 * 授信提供方ID,例JRD
	 * @return
	 */
	public String getProviderId() {
		return this.providerId;
	}
	/**
	 * 用户ID
	 * @param userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	/**
	 * 用户ID
	 * @return
	 */
	public String getUserId() {
		return this.userId;
	}
	/**
	 * 授信结果订单ID
	 * @param creditResultId
	 */
	public void setCreditResultId(String creditResultId) {
		this.creditResultId = creditResultId;
	}
	
	/**
	 * 授信结果订单ID
	 * @return
	 */
	public String getCreditResultId() {
		return this.creditResultId;
	}
	/**
	 * 用户订单流水号
	 * @param userOrderId
	 */
	public void setUserOrderId(String userOrderId) {
		this.userOrderId = userOrderId;
	}
	
	/**
	 * 用户订单流水号
	 * @return
	 */
	public String getUserOrderId() {
		return this.userOrderId;
	}
	/**
	 * 订单ID
	 * @param orderId
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	/**
	 * 订单ID
	 * @return
	 */
	public String getOrderId() {
		return this.orderId;
	}
	/**
	 * 提前还款订单ID
	 * @param overdueOrderId
	 */
	public void setOverdueOrderId(String overdueOrderId) {
		this.overdueOrderId = overdueOrderId;
	}
	
	/**
	 * 提前还款订单ID
	 * @return
	 */
	public String getOverdueOrderId() {
		return this.overdueOrderId;
	}
	/**
	 * 订单类型,1正常贷款,2一次性提前还款,3部分提前还款,4正常信用消费
	 * @param orderType
	 */
	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}
	
	/**
	 * 订单类型,1正常贷款,2一次性提前还款,3部分提前还款,4正常信用消费
	 * @return
	 */
	public Integer getOrderType() {
		return this.orderType;
	}
	/**
	 * 授信协议ID
	 * @param creditAgreementId
	 */
	public void setCreditAgreementId(String creditAgreementId) {
		this.creditAgreementId = creditAgreementId;
	}
	
	/**
	 * 授信协议ID
	 * @return
	 */
	public String getCreditAgreementId() {
		return this.creditAgreementId;
	}
	/**
	 * 期数汇总
	 * @param periodSummary
	 */
	public void setPeriodSummary(String periodSummary) {
		this.periodSummary = periodSummary;
	}
	
	/**
	 * 期数汇总
	 * @return
	 */
	public String getPeriodSummary() {
		return this.periodSummary;
	}
	/**
	 * 当前期数
	 * @param periodCurrent
	 */
	public void setPeriodCurrent(String periodCurrent) {
		this.periodCurrent = periodCurrent;
	}
	
	/**
	 * 当前期数
	 * @return
	 */
	public String getPeriodCurrent() {
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
	public void setShouldCapital(Long shouldCapital) {
		this.shouldCapital = shouldCapital;
	}
	
	/**
	 * 应还本金(分)
	 * @return
	 */
	public Long getShouldCapital() {
		return this.shouldCapital;
	}
	/**
	 * 应还利息(分)
	 * @param shouldInterest
	 */
	public void setShouldInterest(Long shouldInterest) {
		this.shouldInterest = shouldInterest;
	}
	
	/**
	 * 应还利息(分)
	 * @return
	 */
	public Long getShouldInterest() {
		return this.shouldInterest;
	}
	/**
	 * 应还金额(分)
	 * @param shouldAmount
	 */
	public void setShouldAmount(Long shouldAmount) {
		this.shouldAmount = shouldAmount;
	}
	
	/**
	 * 应还金额(分)
	 * @return
	 */
	public Long getShouldAmount() {
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
	 * 实际还款利息(分)
	 * @param repaidInterest
	 */
	public void setRepaidInterest(Long repaidInterest) {
		this.repaidInterest = repaidInterest;
	}
	
	/**
	 * 实际还款利息(分)
	 * @return
	 */
	public Long getRepaidInterest() {
		return this.repaidInterest;
	}
	/**
	 * 实际还款金额(分)
	 * @param repaidAmount
	 */
	public void setRepaidAmount(Long repaidAmount) {
		this.repaidAmount = repaidAmount;
	}
	
	/**
	 * 实际还款金额(分)
	 * @return
	 */
	public Long getRepaidAmount() {
		return this.repaidAmount;
	}
	/**
	 * 减免利息(分)
	 * @param interestFree
	 */
	public void setInterestFree(Long interestFree) {
		this.interestFree = interestFree;
	}
	
	/**
	 * 减免利息(分)
	 * @return
	 */
	public Long getInterestFree() {
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
	 * 逾期罚金(分)
	 * @param overdueFine
	 */
	public void setOverdueFine(Long overdueFine) {
		this.overdueFine = overdueFine;
	}
	
	/**
	 * 逾期罚金(分)
	 * @return
	 */
	public Long getOverdueFine() {
		return this.overdueFine;
	}
	/**
	 * 逾期利息(分)
	 * @param overdueInterest
	 */
	public void setOverdueInterest(Long overdueInterest) {
		this.overdueInterest = overdueInterest;
	}
	
	/**
	 * 逾期利息(分)
	 * @return
	 */
	public Long getOverdueInterest() {
		return this.overdueInterest;
	}
	/**
	 * 提前还款本金(分)
	 * @param advanceAmount
	 */
	public void setAdvanceAmount(Long advanceAmount) {
		this.advanceAmount = advanceAmount;
	}
	
	/**
	 * 提前还款本金(分)
	 * @return
	 */
	public Long getAdvanceAmount() {
		return this.advanceAmount;
	}
	/**
	 * 提前还款利息(分)
	 * @param advanceInterest
	 */
	public void setAdvanceInterest(Long advanceInterest) {
		this.advanceInterest = advanceInterest;
	}
	
	/**
	 * 提前还款利息(分)
	 * @return
	 */
	public Long getAdvanceInterest() {
		return this.advanceInterest;
	}
	/**
	 * 剩余应还本金(分)
	 * @param overplusAmount
	 */
	public void setOverplusAmount(Long overplusAmount) {
		this.overplusAmount = overplusAmount;
	}
	
	/**
	 * 剩余应还本金(分)
	 * @return
	 */
	public Long getOverplusAmount() {
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
	 * 利息是否还款成功,0未扣款,1成功,2失败
	 * @param isSucInterest
	 */
	public void setIsSucInterest(Integer isSucInterest) {
		this.isSucInterest = isSucInterest;
	}
	
	/**
	 * 利息是否还款成功,0未扣款,1成功,2失败
	 * @return
	 */
	public Integer getIsSucInterest() {
		return this.isSucInterest;
	}
	/**
	 * 本金是否还款成功,0未扣款,1成功,2失败
	 * @param isSucCapital
	 */
	public void setIsSucCapital(Integer isSucCapital) {
		this.isSucCapital = isSucCapital;
	}
	
	/**
	 * 本金是否还款成功,0未扣款,1成功,2失败
	 * @return
	 */
	public Integer getIsSucCapital() {
		return this.isSucCapital;
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
