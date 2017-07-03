/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.pojo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * CreditApprovalInfo
 * @author code-generator
 *
 */
public class CreditApprovalInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	@XStreamAlias("id")
	private java.lang.Integer creditId;
	@XStreamAlias("merchantid")
	private java.lang.String rootInstCd;
	@XStreamAlias("userid")
	private java.lang.String userId;
	private java.lang.String merchantId;
	@XStreamAlias("providerid")
	private java.lang.String providerId;
	private java.lang.String productId;
	private java.lang.String creditObjectType;
	@XStreamAlias("creditagreementid")
	private java.lang.String creditAgreementId;
	private java.lang.String creditResultId;
	@XStreamAlias("credittypeid")
	private Integer creditTypeId;
	@XStreamAlias("amount")
	private java.lang.Long amount;
	private java.lang.Long amountSingle;
	@XStreamAlias("rate")
	private java.lang.String rateId;
	@XStreamAlias("currency")
	private java.lang.String currency;
	private java.lang.String deadLine;
	@XStreamAlias("starttime")
	private java.util.Date startTime;
	@XStreamAlias("endtime")
	private java.util.Date endTime;
	private java.util.Date applyDate;
	private java.util.Date applyAccountDate;
	private java.util.Date auditCompleteDate;
	private java.util.Date interestDate;
	private java.lang.String version;
	private java.util.Date versionDate;
	private java.lang.String expansion1;
	private java.lang.String expansion2;
	private java.lang.String expansion3;
	private java.lang.String expansion4;
	private java.lang.String expansion5;
	private java.lang.String expansion6;
	private java.lang.String statusId;
	@XStreamAlias("createdtime")
	private java.util.Date createdTime;
	@XStreamAlias("updatedtime")
	private java.util.Date updatedTime;
	@XStreamAlias("billday")
	private String billday;
	@XStreamAlias("repaymentday")
	private String repaymentday;

	public String getBillday() {
		return billday;
	}

	public void setBillday(String billday) {
		this.billday = billday;
	}

	public String getRepaymentday() {
		return repaymentday;
	}

	public void setRepaymentday(String repaymentday) {
		this.repaymentday = repaymentday;
	}

	/**
	 * 授信号
	 * @param creditId
	 */
	public void setCreditId(java.lang.Integer creditId) {
		this.creditId = creditId;
	}
	
	/**
	 * 授信号
	 * @return
	 */
	public java.lang.Integer getCreditId() {
		return this.creditId;
	}
	/**
	 * 管理机构代码
	 * @param rootInstCd
	 */
	public void setRootInstCd(java.lang.String rootInstCd) {
		this.rootInstCd = rootInstCd;
	}
	
	/**
	 * 管理机构代码
	 * @return
	 */
	public java.lang.String getRootInstCd() {
		return this.rootInstCd;
	}
	/**
	 * 商户用户ID
	 * @param userId
	 */
	public void setUserId(java.lang.String userId) {
		this.userId = userId;
	}
	
	/**
	 * 商户用户ID
	 * @return
	 */
	public java.lang.String getUserId() {
		return this.userId;
	}
	/**
	 * 商户商家ID
	 * @param merchantId
	 */
	public void setMerchantId(java.lang.String merchantId) {
		this.merchantId = merchantId;
	}
	
	/**
	 * 商户商家ID
	 * @return
	 */
	public java.lang.String getMerchantId() {
		return this.merchantId;
	}
	/**
	 * 授信提供方ID
	 * @param providerId
	 */
	public void setProviderId(java.lang.String providerId) {
		this.providerId = providerId;
	}
	
	/**
	 * 授信提供方ID
	 * @return
	 */
	public java.lang.String getProviderId() {
		return this.providerId;
	}
	/**
	 * 产品ID
	 * @param productId
	 */
	public void setProductId(java.lang.String productId) {
		this.productId = productId;
	}
	
	/**
	 * 产品ID
	 * @return
	 */
	public java.lang.String getProductId() {
		return this.productId;
	}
	/**
	 * 授信对象类型
	 * @param creditObjectType
	 */
	public void setCreditObjectType(java.lang.String creditObjectType) {
		this.creditObjectType = creditObjectType;
	}
	
	/**
	 * 授信对象类型
	 * @return
	 */
	public java.lang.String getCreditObjectType() {
		return this.creditObjectType;
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
	 * 授信种类ID
	 * @param creditTypeId
	 */
	public void setCreditTypeId(Integer creditTypeId) {
		this.creditTypeId = creditTypeId;
	}
	
	/**
	 * 授信种类ID
	 * @return
	 */
	public Integer getCreditTypeId() {
		return this.creditTypeId;
	}
	/**
	 * 授信金额
	 * @param amount
	 */
	public void setAmount(java.lang.Long amount) {
		this.amount = amount;
	}
	
	/**
	 * 授信金额
	 * @return
	 */
	public java.lang.Long getAmount() {
		return this.amount;
	}
	/**
	 * 单笔授信金额
	 * @param amountSingle
	 */
	public void setAmountSingle(java.lang.Long amountSingle) {
		this.amountSingle = amountSingle;
	}
	
	/**
	 * 单笔授信金额
	 * @return
	 */
	public java.lang.Long getAmountSingle() {
		return this.amountSingle;
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
	 * 币种
	 * @param currency
	 */
	public void setCurrency(java.lang.String currency) {
		this.currency = currency;
	}
	
	/**
	 * 币种
	 * @return
	 */
	public java.lang.String getCurrency() {
		return this.currency;
	}
	/**
	 * 期限
	 * @param deadLine
	 */
	public void setDeadLine(java.lang.String deadLine) {
		this.deadLine = deadLine;
	}
	
	/**
	 * 期限
	 * @return
	 */
	public java.lang.String getDeadLine() {
		return this.deadLine;
	}
	/**
	 * 生效时间
	 * @param startTime
	 */
	public void setStartTime(java.util.Date startTime) {
		this.startTime = startTime;
	}
	
	/**
	 * 生效时间
	 * @return
	 */
	public java.util.Date getStartTime() {
		return this.startTime;
	}
	/**
	 * 失效时间
	 * @param endTime
	 */
	public void setEndTime(java.util.Date endTime) {
		this.endTime = endTime;
	}
	
	/**
	 * 失效时间
	 * @return
	 */
	public java.util.Date getEndTime() {
		return this.endTime;
	}
	/**
	 * 申请日期
	 * @param applyDate
	 */
	public void setApplyDate(java.util.Date applyDate) {
		this.applyDate = applyDate;
	}
	
	/**
	 * 申请日期
	 * @return
	 */
	public java.util.Date getApplyDate() {
		return this.applyDate;
	}
	/**
	 * 申请账期
	 * @param applyAccountDate
	 */
	public void setApplyAccountDate(java.util.Date applyAccountDate) {
		this.applyAccountDate = applyAccountDate;
	}
	
	/**
	 * 申请账期
	 * @return
	 */
	public java.util.Date getApplyAccountDate() {
		return this.applyAccountDate;
	}
	/**
	 * 审核完成日期
	 * @param auditCompleteDate
	 */
	public void setAuditCompleteDate(java.util.Date auditCompleteDate) {
		this.auditCompleteDate = auditCompleteDate;
	}
	
	/**
	 * 审核完成日期
	 * @return
	 */
	public java.util.Date getAuditCompleteDate() {
		return this.auditCompleteDate;
	}
	/**
	 * 计息开始时间
	 * @param interestDate
	 */
	public void setInterestDate(java.util.Date interestDate) {
		this.interestDate = interestDate;
	}
	
	/**
	 * 计息开始时间
	 * @return
	 */
	public java.util.Date getInterestDate() {
		return this.interestDate;
	}
	/**
	 * 版本号
	 * @param version
	 */
	public void setVersion(java.lang.String version) {
		this.version = version;
	}
	
	/**
	 * 版本号
	 * @return
	 */
	public java.lang.String getVersion() {
		return this.version;
	}
	/**
	 * 版本时间
	 * @param versionDate
	 */
	public void setVersionDate(java.util.Date versionDate) {
		this.versionDate = versionDate;
	}
	
	/**
	 * 版本时间
	 * @return
	 */
	public java.util.Date getVersionDate() {
		return this.versionDate;
	}
	/**
	 * 扩张字段1
	 * @param expansion1
	 */
	public void setExpansion1(java.lang.String expansion1) {
		this.expansion1 = expansion1;
	}
	
	/**
	 * 扩张字段1
	 * @return
	 */
	public java.lang.String getExpansion1() {
		return this.expansion1;
	}
	/**
	 * 扩张字段2
	 * @param expansion2
	 */
	public void setExpansion2(java.lang.String expansion2) {
		this.expansion2 = expansion2;
	}
	
	/**
	 * 扩张字段2
	 * @return
	 */
	public java.lang.String getExpansion2() {
		return this.expansion2;
	}
	/**
	 * 扩张字段3
	 * @param expansion3
	 */
	public void setExpansion3(java.lang.String expansion3) {
		this.expansion3 = expansion3;
	}
	
	/**
	 * 扩张字段3
	 * @return
	 */
	public java.lang.String getExpansion3() {
		return this.expansion3;
	}
	/**
	 * 扩张字段4
	 * @param expansion4
	 */
	public void setExpansion4(java.lang.String expansion4) {
		this.expansion4 = expansion4;
	}
	
	/**
	 * 扩张字段4
	 * @return
	 */
	public java.lang.String getExpansion4() {
		return this.expansion4;
	}
	/**
	 * 扩张字段5
	 * @param expansion5
	 */
	public void setExpansion5(java.lang.String expansion5) {
		this.expansion5 = expansion5;
	}
	
	/**
	 * 扩张字段5
	 * @return
	 */
	public java.lang.String getExpansion5() {
		return this.expansion5;
	}
	/**
	 * 扩张字段6
	 * @param expansion6
	 */
	public void setExpansion6(java.lang.String expansion6) {
		this.expansion6 = expansion6;
	}
	
	/**
	 * 扩张字段6
	 * @return
	 */
	public java.lang.String getExpansion6() {
		return this.expansion6;
	}
	/**
	 * 授信状态,0失效,1生效
	 * @param statusId
	 */
	public void setStatusId(java.lang.String statusId) {
		this.statusId = statusId;
	}
	
	/**
	 * 授信状态,0失效,1生效
	 * @return
	 */
	public java.lang.String getStatusId() {
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
	 * 授信结果订单号
	 * @return
	 */
	public java.lang.String getCreditResultId() {
		return creditResultId;
	}

	public void setCreditResultId(java.lang.String creditResultId) {
		this.creditResultId = creditResultId;
	}

}