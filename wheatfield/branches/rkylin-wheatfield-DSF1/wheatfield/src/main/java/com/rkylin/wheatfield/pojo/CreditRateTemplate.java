/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.pojo;

import java.io.Serializable;

/**
 * CreditRateTemplate
 * @author code-generator
 *
 */
public class CreditRateTemplate implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.String rateId;
	private java.lang.String rootInstCd;
	private java.lang.String productId;
	private java.lang.String providerId;
	private java.lang.String rateType;
	private java.lang.String billDay;
	private java.lang.String repaymentDay;
	private java.lang.String rateProperty;
	private java.lang.String rateInterestForm;
	private java.lang.String rateInterestType;
	private java.lang.String rateInterestOver;
	private java.lang.String rateInterestOverUnit;
	private java.lang.Long overdueFees;
	private java.lang.String overdueFeesUnit;
	private java.lang.String rateAdvaOneoff;
	private java.lang.String rateAdvaOneoffUnit;
	private java.lang.Long advanceFeesOneoff;
	private java.lang.String advanceFeesOneoffUnit;
	private java.lang.String rateAdvaSect;
	private java.lang.String rateAdvaSectUnit;
	private java.lang.Long advanceFeesSect;
	private java.lang.String advanceFeesSectUnit;
	private java.lang.String expansion1;
	private java.lang.String expansion2;
	private java.lang.String expansion3;
	private java.lang.String expansion4;
	private java.lang.String expansion5;
	private java.lang.String expansion6;
	private Integer statusId;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;

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
	 * 费率属性,例1:随借随还
	 * @param rateProperty
	 */
	public void setRateProperty(java.lang.String rateProperty) {
		this.rateProperty = rateProperty;
	}
	
	/**
	 * 费率属性,例1:随借随还
	 * @return
	 */
	public java.lang.String getRateProperty() {
		return this.rateProperty;
	}
	/**
	 * 计息开始日方式:1:贷款申请日+3日,2:16时前贷款申请+2日
	 * @param rateInterestForm
	 */
	public void setRateInterestForm(java.lang.String rateInterestForm) {
		this.rateInterestForm = rateInterestForm;
	}
	
	/**
	 * 计息开始日方式:1:贷款申请日+3日,2:16时前贷款申请+2日
	 * @return
	 */
	public java.lang.String getRateInterestForm() {
		return this.rateInterestForm;
	}
	/**
	 * 计息开始日类型:1:消费日,2:放款日
	 * @param rateInterestType
	 */
	public void setRateInterestType(java.lang.String rateInterestType) {
		this.rateInterestType = rateInterestType;
	}
	
	/**
	 * 计息开始日类型:1:消费日,2:放款日
	 * @return
	 */
	public java.lang.String getRateInterestType() {
		return this.rateInterestType;
	}
	/**
	 * 逾期利率
	 * @param rateInterestOver
	 */
	public void setRateInterestOver(java.lang.String rateInterestOver) {
		this.rateInterestOver = rateInterestOver;
	}
	
	/**
	 * 逾期利率
	 * @return
	 */
	public java.lang.String getRateInterestOver() {
		return this.rateInterestOver;
	}
	/**
	 * 逾期利率单位
	 * @param rateInterestOverUnit
	 */
	public void setRateInterestOverUnit(java.lang.String rateInterestOverUnit) {
		this.rateInterestOverUnit = rateInterestOverUnit;
	}
	
	/**
	 * 逾期利率单位
	 * @return
	 */
	public java.lang.String getRateInterestOverUnit() {
		return this.rateInterestOverUnit;
	}
	/**
	 * 逾期手续费(分)
	 * @param overdueFees
	 */
	public void setOverdueFees(java.lang.Long overdueFees) {
		this.overdueFees = overdueFees;
	}
	
	/**
	 * 逾期手续费(分)
	 * @return
	 */
	public java.lang.Long getOverdueFees() {
		return this.overdueFees;
	}
	/**
	 * 逾期手续费单位
	 * @param overdueFeesUnit
	 */
	public void setOverdueFeesUnit(java.lang.String overdueFeesUnit) {
		this.overdueFeesUnit = overdueFeesUnit;
	}
	
	/**
	 * 逾期手续费单位
	 * @return
	 */
	public java.lang.String getOverdueFeesUnit() {
		return this.overdueFeesUnit;
	}
	/**
	 * 一次性提前还款利率
	 * @param rateAdvaOneoff
	 */
	public void setRateAdvaOneoff(java.lang.String rateAdvaOneoff) {
		this.rateAdvaOneoff = rateAdvaOneoff;
	}
	
	/**
	 * 一次性提前还款利率
	 * @return
	 */
	public java.lang.String getRateAdvaOneoff() {
		return this.rateAdvaOneoff;
	}
	/**
	 * 一次性提前还款利率单位
	 * @param rateAdvaOneoffUnit
	 */
	public void setRateAdvaOneoffUnit(java.lang.String rateAdvaOneoffUnit) {
		this.rateAdvaOneoffUnit = rateAdvaOneoffUnit;
	}
	
	/**
	 * 一次性提前还款利率单位
	 * @return
	 */
	public java.lang.String getRateAdvaOneoffUnit() {
		return this.rateAdvaOneoffUnit;
	}
	/**
	 * 一次性提前还款手续费(分)
	 * @param advanceFeesOneoff
	 */
	public void setAdvanceFeesOneoff(java.lang.Long advanceFeesOneoff) {
		this.advanceFeesOneoff = advanceFeesOneoff;
	}
	
	/**
	 * 一次性提前还款手续费(分)
	 * @return
	 */
	public java.lang.Long getAdvanceFeesOneoff() {
		return this.advanceFeesOneoff;
	}
	/**
	 * 一次性提前还款手续费单位
	 * @param advanceFeesOneoffUnit
	 */
	public void setAdvanceFeesOneoffUnit(java.lang.String advanceFeesOneoffUnit) {
		this.advanceFeesOneoffUnit = advanceFeesOneoffUnit;
	}
	
	/**
	 * 一次性提前还款手续费单位
	 * @return
	 */
	public java.lang.String getAdvanceFeesOneoffUnit() {
		return this.advanceFeesOneoffUnit;
	}
	/**
	 * 部分提前还款利率
	 * @param rateAdvaSect
	 */
	public void setRateAdvaSect(java.lang.String rateAdvaSect) {
		this.rateAdvaSect = rateAdvaSect;
	}
	
	/**
	 * 部分提前还款利率
	 * @return
	 */
	public java.lang.String getRateAdvaSect() {
		return this.rateAdvaSect;
	}
	/**
	 * 部分性提前还款利率单位
	 * @param rateAdvaSectUnit
	 */
	public void setRateAdvaSectUnit(java.lang.String rateAdvaSectUnit) {
		this.rateAdvaSectUnit = rateAdvaSectUnit;
	}
	
	/**
	 * 部分性提前还款利率单位
	 * @return
	 */
	public java.lang.String getRateAdvaSectUnit() {
		return this.rateAdvaSectUnit;
	}
	/**
	 * 部分性提前还款手续费(分)
	 * @param advanceFeesSect
	 */
	public void setAdvanceFeesSect(java.lang.Long advanceFeesSect) {
		this.advanceFeesSect = advanceFeesSect;
	}
	
	/**
	 * 部分性提前还款手续费(分)
	 * @return
	 */
	public java.lang.Long getAdvanceFeesSect() {
		return this.advanceFeesSect;
	}
	/**
	 * 部分性提前还款手续费单位
	 * @param advanceFeesSectUnit
	 */
	public void setAdvanceFeesSectUnit(java.lang.String advanceFeesSectUnit) {
		this.advanceFeesSectUnit = advanceFeesSectUnit;
	}
	
	/**
	 * 部分性提前还款手续费单位
	 * @return
	 */
	public java.lang.String getAdvanceFeesSectUnit() {
		return this.advanceFeesSectUnit;
	}
	/**
	 * 预留字段1
	 * @param expansion1
	 */
	public void setExpansion1(java.lang.String expansion1) {
		this.expansion1 = expansion1;
	}
	
	/**
	 * 预留字段1
	 * @return
	 */
	public java.lang.String getExpansion1() {
		return this.expansion1;
	}
	/**
	 * 预留字段2
	 * @param expansion2
	 */
	public void setExpansion2(java.lang.String expansion2) {
		this.expansion2 = expansion2;
	}
	
	/**
	 * 预留字段2
	 * @return
	 */
	public java.lang.String getExpansion2() {
		return this.expansion2;
	}
	/**
	 * 预留字段3
	 * @param expansion3
	 */
	public void setExpansion3(java.lang.String expansion3) {
		this.expansion3 = expansion3;
	}
	
	/**
	 * 预留字段3
	 * @return
	 */
	public java.lang.String getExpansion3() {
		return this.expansion3;
	}
	/**
	 * 预留字段4
	 * @param expansion4
	 */
	public void setExpansion4(java.lang.String expansion4) {
		this.expansion4 = expansion4;
	}
	
	/**
	 * 预留字段4
	 * @return
	 */
	public java.lang.String getExpansion4() {
		return this.expansion4;
	}
	/**
	 * 预留字段5
	 * @param expansion5
	 */
	public void setExpansion5(java.lang.String expansion5) {
		this.expansion5 = expansion5;
	}
	
	/**
	 * 预留字段5
	 * @return
	 */
	public java.lang.String getExpansion5() {
		return this.expansion5;
	}
	/**
	 * 预留字段6
	 * @param expansion6
	 */
	public void setExpansion6(java.lang.String expansion6) {
		this.expansion6 = expansion6;
	}
	
	/**
	 * 预留字段6
	 * @return
	 */
	public java.lang.String getExpansion6() {
		return this.expansion6;
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