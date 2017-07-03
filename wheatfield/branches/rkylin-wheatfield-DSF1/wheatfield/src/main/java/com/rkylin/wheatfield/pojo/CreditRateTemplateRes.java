/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.pojo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * CreditRateTemplate
 * @author code-generator
 *
 */
@XStreamAlias("creditratetemplate")
public class CreditRateTemplateRes implements Serializable{
	private static final long serialVersionUID = 1L;
	@XStreamAlias("rateid")	
	private String rateId;
	@XStreamAlias("rootinstcd")	
	private String rootInstCd;
	@XStreamAlias("productid")	
	private String productId;
	@XStreamAlias("providerid")	
	private String providerId;
	@XStreamAlias("ratetype")	
	private String rateType;
	@XStreamAlias("billday")	
	private String billDay;
	@XStreamAlias("repaymentday")		
	private String repaymentDay;
	@XStreamAlias("ratetimex")	
	private String rateTimeX;
	@XStreamAlias("ratex")	
	private String rateX;
	@XStreamAlias("ratetimey")	
	private String rateTimeY;
	@XStreamAlias("ratey")	
	private String rateY;
	@XStreamAlias("rateproperty")	
	private String rateProperty;
	@XStreamAlias("rateinterestform")	
	private String rateInterestForm;
	@XStreamAlias("rateinteresttype")
	private String rateInterestType;
	@XStreamAlias("rateinterestover")
	private String rateInterestOver;
	@XStreamAlias("rateinterestoverunit")
	private String rateInterestOverUnit;
	@XStreamAlias("overduefees")
	private long overdueFees;
	@XStreamAlias("overduefeesunit")
	private String overdueFeesUnit;
	@XStreamAlias("rateadvaoneoff")
	private String rateAdvaOneoff;
	@XStreamAlias("rateadvaoneoffunit")
	private String rateAdvaOneoffUnit;
	@XStreamAlias("advancefeesoneoff")
	private long advanceFeesOneoff;
	@XStreamAlias("advancefeesoneoffunit")
	private String advanceFeesOneoffUnit;
	@XStreamAlias("rateadvasect")
	private String rateAdvaSect;
	@XStreamAlias("rateadvasectunit")
	private String rateAdvaSectUnit;
	@XStreamAlias("advancefeessect")
	private long advanceFeesSect;
	@XStreamAlias("advancefeessectunit")
	private String advanceFeesSectUnit;
	@XStreamAlias("statusid")	
	private int    statusId;
	@XStreamAlias("expansiona")	
	private String expansion1;
	@XStreamAlias("expansionb")	
	private String expansion2;
	@XStreamAlias("expansionc")	
	private String expansion3;
	@XStreamAlias("expansiond")	
	private String expansion4;
	@XStreamAlias("expansione")	
	private String expansion5;
	@XStreamAlias("expansionf")	
	private String expansion6;
	@XStreamAlias("createdtime")	
	private java.util.Date createdTime;
	@XStreamAlias("updatedtime")	
	private java.util.Date updatedTime;
	@XStreamAlias("rateunit")	
	private String rateUnit;
	@XStreamAlias("ratetimeunit")	
	private String rateTimeUnit;
	
	
	public String getRateInterestOver() {
		return rateInterestOver;
	}

	public void setRateInterestOver(String rateInterestOver) {
		this.rateInterestOver = rateInterestOver;
	}

	public String getRateInterestOverUnit() {
		return rateInterestOverUnit;
	}

	public void setRateInterestOverUnit(String rateInterestOverUnit) {
		this.rateInterestOverUnit = rateInterestOverUnit;
	}

	public long getOverdueFees() {
		return overdueFees;
	}

	public void setOverdueFees(long overdueFees) {
		this.overdueFees = overdueFees;
	}

	public String getOverdueFeesUnit() {
		return overdueFeesUnit;
	}

	public void setOverdueFeesUnit(String overdueFeesUnit) {
		this.overdueFeesUnit = overdueFeesUnit;
	}

	public String getRateAdvaOneoff() {
		return rateAdvaOneoff;
	}

	public void setRateAdvaOneoff(String rateAdvaOneoff) {
		this.rateAdvaOneoff = rateAdvaOneoff;
	}

	public String getRateAdvaOneoffUnit() {
		return rateAdvaOneoffUnit;
	}

	public void setRateAdvaOneoffUnit(String rateAdvaOneoffUnit) {
		this.rateAdvaOneoffUnit = rateAdvaOneoffUnit;
	}

	public long getAdvanceFeesOneoff() {
		return advanceFeesOneoff;
	}

	public void setAdvanceFeesOneoff(long advanceFeesOneoff) {
		this.advanceFeesOneoff = advanceFeesOneoff;
	}

	public String getAdvanceFeesOneoffUnit() {
		return advanceFeesOneoffUnit;
	}

	public void setAdvanceFeesOneoffUnit(String advanceFeesOneoffUnit) {
		this.advanceFeesOneoffUnit = advanceFeesOneoffUnit;
	}

	public String getRateAdvaSect() {
		return rateAdvaSect;
	}

	public void setRateAdvaSect(String rateAdvaSect) {
		this.rateAdvaSect = rateAdvaSect;
	}

	public String getRateAdvaSectUnit() {
		return rateAdvaSectUnit;
	}

	public void setRateAdvaSectUnit(String rateAdvaSectUnit) {
		this.rateAdvaSectUnit = rateAdvaSectUnit;
	}

	public long getAdvanceFeesSect() {
		return advanceFeesSect;
	}

	public void setAdvanceFeesSect(long advanceFeesSect) {
		this.advanceFeesSect = advanceFeesSect;
	}

	public String getAdvanceFeesSectUnit() {
		return advanceFeesSectUnit;
	}

	public void setAdvanceFeesSectUnit(String advanceFeesSectUnit) {
		this.advanceFeesSectUnit = advanceFeesSectUnit;
	}
	
	public String getRateUnit() {
		return rateUnit;
	}

	public void setRateUnit(String rateUnit) {
		this.rateUnit = rateUnit;
	}

	public String getRateTimeUnit() {
		return rateTimeUnit;
	}

	public void setRateTimeUnit(String rateTimeUnit) {
		this.rateTimeUnit = rateTimeUnit;
	}
	/**
	 * 状态： 1：有效  0：失效
	 * @param rateId
	 */	
	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	/**
	 * 费率协议�?
	 * @param rateId
	 */
	public void setRateId(String rateId) {
		this.rateId = rateId;
	}
	
	/**
	 * 费率协议�?
	 * @return
	 */
	public String getRateId() {
		return this.rateId;
	}
	/**
	 * 管理机构代码,例丰�?,会唐,课栈
	 * @param rootInstCd
	 */
	public void setRootInstCd(String rootInstCd) {
		this.rootInstCd = rootInstCd;
	}
	
	/**
	 * 管理机构代码,例丰�?,会唐,课栈
	 * @return
	 */
	public String getRootInstCd() {
		return this.rootInstCd;
	}
	/**
	 * 产品�?
	 * @param productId
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	/**
	 * 产品�?
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
	 * 费率类型:1:等额本息,2:先息后本,3:X+Y
	 * @param rateType
	 */
	public void setRateType(String rateType) {
		this.rateType = rateType;
	}
	
	/**
	 * 费率类型:1:等额本息,2:先息后本,3:X+Y
	 * @return
	 */
	public String getRateType() {
		return this.rateType;
	}
	/**
	 * 账单�?
	 * @param billDay
	 */
	public void setBillDay(String billDay) {
		this.billDay = billDay;
	}
	
	/**
	 * 账单�?
	 * @return
	 */
	public String getBillDay() {
		return this.billDay;
	}
	/**
	 * 还款�?
	 * @param repaymentDay
	 */
	public void setRepaymentDay(String repaymentDay) {
		this.repaymentDay = repaymentDay;
	}
	
	/**
	 * 还款�?
	 * @return
	 */
	public String getRepaymentDay() {
		return this.repaymentDay;
	}
	/**
	 * X时间
	 * @param rateTimeX
	 */
	public void setRateTimeX(String rateTimeX) {
		this.rateTimeX = rateTimeX;
	}
	
	/**
	 * X时间
	 * @return
	 */
	public String getRateTimeX() {
		return this.rateTimeX;
	}
	/**
	 * 日利率X
	 * @param rateX
	 */
	public void setRateX(String rateX) {
		this.rateX = rateX;
	}
	
	/**
	 * 日利率X
	 * @return
	 */
	public String getRateX() {
		return this.rateX;
	}
	/**
	 * Y时间
	 * @param rateTimeY
	 */
	public void setRateTimeY(String rateTimeY) {
		this.rateTimeY = rateTimeY;
	}
	
	/**
	 * Y时间
	 * @return
	 */
	public String getRateTimeY() {
		return this.rateTimeY;
	}
	/**
	 * 日利率Y
	 * @param rateY
	 */
	public void setRateY(String rateY) {
		this.rateY = rateY;
	}
	
	/**
	 * 日利率Y
	 * @return
	 */
	public String getRateY() {
		return this.rateY;
	}
	/**
	 * 费率属�??,�?1:随�?�随�?
	 * @param rateProperty
	 */
	public void setRateProperty(String rateProperty) {
		this.rateProperty = rateProperty;
	}
	
	/**
	 * 费率属�??,�?1:随�?�随�?
	 * @return
	 */
	public String getRateProperty() {
		return this.rateProperty;
	}
	/**
	 * 计息�?始日方式:1:贷款申请�?+3�?,2:16时前贷款申请+2�?
	 * @param rateInterestForm
	 */
	public void setRateInterestForm(String rateInterestForm) {
		this.rateInterestForm = rateInterestForm;
	}
	
	/**
	 * 计息�?始日方式:1:贷款申请�?+3�?,2:16时前贷款申请+2�?
	 * @return
	 */
	public String getRateInterestForm() {
		return this.rateInterestForm;
	}
	/**
	 * 计息�?始日类型:1:消费�?,2:放款�?
	 * @param rateInterestType
	 */
	public void setRateInterestType(String rateInterestType) {
		this.rateInterestType = rateInterestType;
	}
	
	/**
	 * 计息�?始日类型:1:消费�?,2:放款�?
	 * @return
	 */
	public String getRateInterestType() {
		return this.rateInterestType;
	}
	/**
	 * 预留字段1
	 * @param expansion1
	 */
	public void setExpansion1(String expansion1) {
		this.expansion1 = expansion1;
	}
	
	/**
	 * 预留字段1
	 * @return
	 */
	public String getExpansion1() {
		return this.expansion1;
	}
	/**
	 * 预留字段2
	 * @param expansion2
	 */
	public void setExpansion2(String expansion2) {
		this.expansion2 = expansion2;
	}
	
	/**
	 * 预留字段2
	 * @return
	 */
	public String getExpansion2() {
		return this.expansion2;
	}
	/**
	 * 预留字段3
	 * @param expansion3
	 */
	public void setExpansion3(String expansion3) {
		this.expansion3 = expansion3;
	}
	
	/**
	 * 预留字段3
	 * @return
	 */
	public String getExpansion3() {
		return this.expansion3;
	}
	/**
	 * 预留字段4
	 * @param expansion4
	 */
	public void setExpansion4(String expansion4) {
		this.expansion4 = expansion4;
	}
	
	/**
	 * 预留字段4
	 * @return
	 */
	public String getExpansion4() {
		return this.expansion4;
	}
	/**
	 * 预留字段5
	 * @param expansion5
	 */
	public void setExpansion5(String expansion5) {
		this.expansion5 = expansion5;
	}
	
	/**
	 * 预留字段5
	 * @return
	 */
	public String getExpansion5() {
		return this.expansion5;
	}
	/**
	 * 预留字段6
	 * @param expansion6
	 */
	public void setExpansion6(String expansion6) {
		this.expansion6 = expansion6;
	}
	
	/**
	 * 预留字段6
	 * @return
	 */
	public String getExpansion6() {
		return this.expansion6;
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