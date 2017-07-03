/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.pojo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * CreditInfo
 * @author code-generator
 *
 */
@XStreamAlias("creditinfo")
public class CreditInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	@XStreamAlias("id")
	private java.lang.Long id;
	@XStreamAlias("merchantid")
	private java.lang.String rootInstCd;
	@XStreamAlias("userid")
	private java.lang.String userId;
	@XStreamAlias("providerid")
	private java.lang.String providerId;
	@XStreamAlias("creditagreementid")
	private java.lang.String creditAgreementId;
	@XStreamAlias("credittypeid")
	private Integer creditTypeId;
	@XStreamAlias("amount")
	private java.lang.Long amount;
	@XStreamAlias("rate")
	private java.lang.String rate;
	@XStreamAlias("currency")
	private java.lang.String currency;
	@XStreamAlias("starttime")
	private java.util.Date startTime;
	@XStreamAlias("endtime")
	private java.util.Date endTime;
	@XStreamAlias("createdtime")
	private java.util.Date createdTime;
	@XStreamAlias("updatedtime")
	private java.util.Date updatedTime;
	@XStreamAlias("billday")
	private String billday;
	@XStreamAlias("repaymentday")
	private String repaymentday;

	/**
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * @return
	 */
	public Long getId() {
		return this.id;
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
	 * 授信利率
	 * @param rate
	 */
	public void setRate(java.lang.String rate) {
		this.rate = rate;
	}
	
	/**
	 * 授信利率
	 * @return
	 */
	public java.lang.String getRate() {
		return this.rate;
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

	public java.lang.String getRootInstCd() {
		return rootInstCd;
	}

	public void setRootInstCd(java.lang.String rootInstCd) {
		this.rootInstCd = rootInstCd;
	}

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
}