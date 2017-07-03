/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.pojo;

import java.io.Serializable;

/**
 * CorporatAccountInfoQuery
 * @author code-generator
 *
 */
public class CorporatAccountInfoScopeQuery implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.Integer corporateAccountId;
	private java.lang.String rootInstCd;
	private java.lang.String accountNumber;
	private java.lang.String accountRealName;
	private java.util.Date openAccountDate;
	private java.lang.String openAccountDescription;
	private java.lang.String currency;
	private java.lang.String bankHead;
	private java.lang.String bankHeadName;
	private java.lang.String bankBranch;
	private java.lang.String bankBranchName;
	private java.lang.String bankProvince;
	private java.lang.String bankCity;
	private java.lang.String certificateType;
	private java.lang.String certificateNumber;
	private Integer statusId;
	private java.util.Date createdTimeFrom;
	private java.util.Date createdTimeTo;
	private java.util.Date updatedTimeFrom;
	private java.util.Date updatedTimeTo;

	/**
	 * 对公账户信息ID
	 * @param corporateAccountId
	 */
	public void setCorporateAccountId(java.lang.Integer corporateAccountId) {
		this.corporateAccountId = corporateAccountId;
	}
	
	/**
	 * 对公账户信息ID
	 * @return
	 */
	public java.lang.Integer getCorporateAccountId() {
		return this.corporateAccountId;
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
	 * 账号
	 * @param accountNumber
	 */
	public void setAccountNumber(java.lang.String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	/**
	 * 账号
	 * @return
	 */
	public java.lang.String getAccountNumber() {
		return this.accountNumber;
	}
	/**
	 * 账户真实名称
	 * @param accountRealName
	 */
	public void setAccountRealName(java.lang.String accountRealName) {
		this.accountRealName = accountRealName;
	}
	
	/**
	 * 账户真实名称
	 * @return
	 */
	public java.lang.String getAccountRealName() {
		return this.accountRealName;
	}
	/**
	 * 开户日期
	 * @param openAccountDate
	 */
	public void setOpenAccountDate(java.util.Date openAccountDate) {
		this.openAccountDate = openAccountDate;
	}
	
	/**
	 * 开户日期
	 * @return
	 */
	public java.util.Date getOpenAccountDate() {
		return this.openAccountDate;
	}
	/**
	 * 账号用途
	 * @param openAccountDescription
	 */
	public void setOpenAccountDescription(java.lang.String openAccountDescription) {
		this.openAccountDescription = openAccountDescription;
	}
	
	/**
	 * 账号用途
	 * @return
	 */
	public java.lang.String getOpenAccountDescription() {
		return this.openAccountDescription;
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
	 * 开户行总行码
	 * @param bankHead
	 */
	public void setBankHead(java.lang.String bankHead) {
		this.bankHead = bankHead;
	}
	
	/**
	 * 开户行总行码
	 * @return
	 */
	public java.lang.String getBankHead() {
		return this.bankHead;
	}
	/**
	 * 开户行总行名称
	 * @param bankHeadName
	 */
	public void setBankHeadName(java.lang.String bankHeadName) {
		this.bankHeadName = bankHeadName;
	}
	
	/**
	 * 开户行总行名称
	 * @return
	 */
	public java.lang.String getBankHeadName() {
		return this.bankHeadName;
	}
	/**
	 * 开户行支行码
	 * @param bankBranch
	 */
	public void setBankBranch(java.lang.String bankBranch) {
		this.bankBranch = bankBranch;
	}
	
	/**
	 * 开户行支行码
	 * @return
	 */
	public java.lang.String getBankBranch() {
		return this.bankBranch;
	}
	/**
	 * 开户行支行名称
	 * @param bankBranchName
	 */
	public void setBankBranchName(java.lang.String bankBranchName) {
		this.bankBranchName = bankBranchName;
	}
	
	/**
	 * 开户行支行名称
	 * @return
	 */
	public java.lang.String getBankBranchName() {
		return this.bankBranchName;
	}
	/**
	 * 开户行所在省
	 * @param bankProvince
	 */
	public void setBankProvince(java.lang.String bankProvince) {
		this.bankProvince = bankProvince;
	}
	
	/**
	 * 开户行所在省
	 * @return
	 */
	public java.lang.String getBankProvince() {
		return this.bankProvince;
	}
	/**
	 * 开户行所在市
	 * @param bankCity
	 */
	public void setBankCity(java.lang.String bankCity) {
		this.bankCity = bankCity;
	}
	
	/**
	 * 开户行所在市
	 * @return
	 */
	public java.lang.String getBankCity() {
		return this.bankCity;
	}
	/**
	 * 开户证件类型
	 * @param certificateType
	 */
	public void setCertificateType(java.lang.String certificateType) {
		this.certificateType = certificateType;
	}
	
	/**
	 * 开户证件类型
	 * @return
	 */
	public java.lang.String getCertificateType() {
		return this.certificateType;
	}
	/**
	 * 证件号
	 * @param certificateNumber
	 */
	public void setCertificateNumber(java.lang.String certificateNumber) {
		this.certificateNumber = certificateNumber;
	}
	
	/**
	 * 证件号
	 * @return
	 */
	public java.lang.String getCertificateNumber() {
		return this.certificateNumber;
	}
	/**
	 * 状态,0失效,1审核成功,2待审核,3审核中,4审核失败
	 * @param statusId
	 */
	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
	
	/**
	 * 状态,0失效,1审核成功,2待审核,3审核中,4审核失败
	 * @return
	 */
	public Integer getStatusId() {
		return this.statusId;
	}
	/**
	 * 记录创建时间
	 * @param createdTime
	 */
	public void setCreatedTimeFrom(java.util.Date createdTimeFrom) {
		this.createdTimeFrom = createdTimeFrom;
	}
	
	/**
	 * 记录创建时间
	 * @return
	 */
	public java.util.Date getCreatedTimeFrom() {
		return this.createdTimeFrom;
	}
	/**
	 * 记录创建时间
	 * @return
	 */
	public java.util.Date getCreatedTimeTo() {
		return createdTimeTo;
	}
	/**
	 * 记录创建时间
	 * @return
	 */
	public void setCreatedTimeTo(java.util.Date createdTimeTo) {
		this.createdTimeTo = createdTimeTo;
	}
	
	/**
	 * 记录更新时间
	 * @param updatedTime
	 */
	public void setUpdatedTimeFrom(java.util.Date updatedTimeFrom) {
		this.updatedTimeFrom = updatedTimeFrom;
	}
	
	/**
	 * 记录更新时间
	 * @return
	 */
	public java.util.Date getUpdatedTimeFrom() {
		return this.updatedTimeFrom;
	}
	/**
	 * 记录更新时间
	 * @return
	 */
	public java.util.Date getUpdatedTimeTo() {
		return updatedTimeTo;
	}
	/**
	 * 记录更新时间
	 * @return
	 */
	public void setUpdatedTimeTo(java.util.Date updatedTimeTo) {
		this.updatedTimeTo = updatedTimeTo;
	}
}