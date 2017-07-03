/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.pojo;

import java.io.Serializable;

/**
 * GenerationPaymentHistory
 * @author code-generator
 *
 */
public class GenerationPaymentHistory implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.Long geneId;
	private java.lang.String requestNo;
	private java.lang.String bussinessCode;
	private java.lang.String rootInstcd;
	private java.lang.String orderNo;
	private Integer orderType;
	private java.lang.String geneSeq;
	private java.lang.String userId;
	private java.lang.String bankCode;
	private java.lang.String accountType;
	private java.lang.String accountNo;
	private java.lang.String accountName;
	private java.lang.String accountProperty;
	private java.lang.String province;
	private java.lang.String city;
	private java.lang.String openBankName;
	private java.lang.String payBankCode;
	private java.lang.Long amount;
	private java.lang.String currency;
	private java.lang.String certificateType;
	private java.lang.String certificateNumber;
	private java.lang.String processResult;
	private Integer sendType;
	private java.lang.String errorCode;
	private Integer statusId;
	private java.util.Date accountDate;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;
	private java.lang.String remark;

	public java.lang.String getRemark() {
		return remark;
	}

	public void setRemark(java.lang.String remark) {
		this.remark = remark;
	}

	/**
	 * 记录号
	 * @param geneId
	 */
	public void setGeneId(java.lang.Long geneId) {
		this.geneId = geneId;
	}
	
	/**
	 * 记录号
	 * @return
	 */
	public java.lang.Long getGeneId() {
		return this.geneId;
	}
	/**
	 * 交易批次号
	 * @param requestNo
	 */
	public void setRequestNo(java.lang.String requestNo) {
		this.requestNo = requestNo;
	}
	
	/**
	 * 交易批次号
	 * @return
	 */
	public java.lang.String getRequestNo() {
		return this.requestNo;
	}
	/**
	 * 业务代码
	 * @param bussinessCode
	 */
	public void setBussinessCode(java.lang.String bussinessCode) {
		this.bussinessCode = bussinessCode;
	}
	
	/**
	 * 业务代码
	 * @return
	 */
	public java.lang.String getBussinessCode() {
		return this.bussinessCode;
	}
	/**
	 * 订单号
	 * @param orderNo
	 */
	public void setOrderNo(java.lang.String orderNo) {
		this.orderNo = orderNo;
	}
	
	/**
	 * 订单号
	 * @return
	 */
	public java.lang.String getOrderNo() {
		return this.orderNo;
	}
	/**
	 * 订单类型,1债券包,2提现,3还款
	 * @param orderType
	 */
	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}
	
	/**
	 * 订单类型,1债券包,2提现,3还款
	 * @return
	 */
	public Integer getOrderType() {
		return this.orderType;
	}
	/**
	 * 记录序号
	 * @param geneSeq
	 */
	public void setGeneSeq(java.lang.String geneSeq) {
		this.geneSeq = geneSeq;
	}
	
	/**
	 * 记录序号
	 * @return
	 */
	public java.lang.String getGeneSeq() {
		return this.geneSeq;
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
	 * 银行代码
	 * @param bankCode
	 */
	public void setBankCode(java.lang.String bankCode) {
		this.bankCode = bankCode;
	}
	
	/**
	 * 银行代码
	 * @return
	 */
	public java.lang.String getBankCode() {
		return this.bankCode;
	}
	/**
	 * 账户类型
	 * @param accountType
	 */
	public void setAccountType(java.lang.String accountType) {
		this.accountType = accountType;
	}
	
	/**
	 * 账户类型
	 * @return
	 */
	public java.lang.String getAccountType() {
		return this.accountType;
	}
	/**
	 * 账号
	 * @param accountNo
	 */
	public void setAccountNo(java.lang.String accountNo) {
		this.accountNo = accountNo;
	}
	
	/**
	 * 账号
	 * @return
	 */
	public java.lang.String getAccountNo() {
		return this.accountNo;
	}
	/**
	 * 账号名
	 * @param accountName
	 */
	public void setAccountName(java.lang.String accountName) {
		this.accountName = accountName;
	}
	
	/**
	 * 账号名
	 * @return
	 */
	public java.lang.String getAccountName() {
		return this.accountName;
	}
	/**
	 * 账号属性
	 * @param accountProperty
	 */
	public void setAccountProperty(java.lang.String accountProperty) {
		this.accountProperty = accountProperty;
	}
	
	/**
	 * 账号属性
	 * @return
	 */
	public java.lang.String getAccountProperty() {
		return this.accountProperty;
	}
	/**
	 * 开户行所在省
	 * @param province
	 */
	public void setProvince(java.lang.String province) {
		this.province = province;
	}
	
	/**
	 * 开户行所在省
	 * @return
	 */
	public java.lang.String getProvince() {
		return this.province;
	}
	/**
	 * 开户行所在市
	 * @param city
	 */
	public void setCity(java.lang.String city) {
		this.city = city;
	}
	
	/**
	 * 开户行所在市
	 * @return
	 */
	public java.lang.String getCity() {
		return this.city;
	}
	/**
	 * 开户行名称
	 * @param openBankName
	 */
	public void setOpenBankName(java.lang.String openBankName) {
		this.openBankName = openBankName;
	}
	
	/**
	 * 开户行名称
	 * @return
	 */
	public java.lang.String getOpenBankName() {
		return this.openBankName;
	}
	/**
	 * 支付行号
	 * @param payBankCode
	 */
	public void setPayBankCode(java.lang.String payBankCode) {
		this.payBankCode = payBankCode;
	}
	
	/**
	 * 支付行号
	 * @return
	 */
	public java.lang.String getPayBankCode() {
		return this.payBankCode;
	}
	/**
	 * 金额
	 * @param amount
	 */
	public void setAmount(java.lang.Long amount) {
		this.amount = amount;
	}
	
	/**
	 * 金额
	 * @return
	 */
	public java.lang.Long getAmount() {
		return this.amount;
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
	 * 处理结果
	 * @param processResult
	 */
	public void setProcessResult(java.lang.String processResult) {
		this.processResult = processResult;
	}
	
	/**
	 * 处理结果
	 * @return
	 */
	public java.lang.String getProcessResult() {
		return this.processResult;
	}
	/**
	 * 发送类型,0正常,1代扣失败,2代扣延迟
	 * @param sendType
	 */
	public void setSendType(Integer sendType) {
		this.sendType = sendType;
	}
	
	/**
	 * 发送类型,0正常,1代扣失败,2代扣延迟
	 * @return
	 */
	public Integer getSendType() {
		return this.sendType;
	}
	/**
	 * 错误码
	 * @param errorCode
	 */
	public void setErrorCode(java.lang.String errorCode) {
		this.errorCode = errorCode;
	}
	
	/**
	 * 错误码
	 * @return
	 */
	public java.lang.String getErrorCode() {
		return this.errorCode;
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
	 * 账期
	 * @param accountDate
	 */
	public void setAccountDate(java.util.Date accountDate) {
		this.accountDate = accountDate;
	}
	
	/**
	 * 账期
	 * @return
	 */
	public java.util.Date getAccountDate() {
		return this.accountDate;
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

	public java.lang.String getRootInstcd() {
		return rootInstcd;
	}

	public void setRootInstcd(java.lang.String rootInstcd) {
		this.rootInstcd = rootInstcd;
	}
	
	
}