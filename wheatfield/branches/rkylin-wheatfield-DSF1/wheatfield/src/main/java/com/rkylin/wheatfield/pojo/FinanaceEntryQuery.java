/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.pojo;

import java.io.Serializable;

/**
 * FinanaceEntryQuery
 * @author code-generator
 *
 */
public class FinanaceEntryQuery implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.String finanaceEntryId;
	private java.util.Date accountDate;
	private java.lang.String finAccountId;
	private Integer accrualType;
	private java.lang.Long amount;
	private java.lang.String referEntryId;
	private java.lang.String pulseTime;
	private java.lang.Integer pulseDegree;
	private Integer direction;
	private java.lang.Long balanceUsable;
	private java.lang.Long balanceSettle;
	private java.lang.Long balanceFrozon;
	private java.lang.Long balanceOverLimit;
	private java.lang.Long balanceCredit;
	private Integer reverseFlag;
	private java.lang.String partyIdFrom;
	private java.lang.String referFrom;
	private java.lang.String referId;
	private java.lang.Long paymentAmount;
	private java.util.Date transDate;
	private java.lang.String thirdPartyId;
	private java.lang.String myNotes;
	private java.lang.String yourNotes;
	private java.lang.String hisNote;
	private java.lang.String remark;
	private java.lang.String recordMap;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;

	/**
	 * 账目条目ID
	 * @param finanaceEntryId
	 */
	public void setFinanaceEntryId(java.lang.String finanaceEntryId) {
		this.finanaceEntryId = finanaceEntryId;
	}
	
	/**
	 * 账目条目ID
	 * @return
	 */
	public java.lang.String getFinanaceEntryId() {
		return this.finanaceEntryId;
	}
	/**
	 * 记账日期
	 * @param accountDate
	 */
	public void setAccountDate(java.util.Date accountDate) {
		this.accountDate = accountDate;
	}
	
	/**
	 * 记账日期
	 * @return
	 */
	public java.util.Date getAccountDate() {
		return this.accountDate;
	}
	/**
	 * 账户ID
	 * @param finAccountId
	 */
	public void setFinAccountId(java.lang.String finAccountId) {
		this.finAccountId = finAccountId;
	}
	
	/**
	 * 账户ID
	 * @return
	 */
	public java.lang.String getFinAccountId() {
		return this.finAccountId;
	}
	/**
	 * 发生额类型
	 * @param accrualType
	 */
	public void setAccrualType(Integer accrualType) {
		this.accrualType = accrualType;
	}
	
	/**
	 * 发生额类型
	 * @return
	 */
	public Integer getAccrualType() {
		return this.accrualType;
	}
	/**
	 * 账户余额
	 * @param amount
	 */
	public void setAmount(java.lang.Long amount) {
		this.amount = amount;
	}
	
	/**
	 * 账户余额
	 * @return
	 */
	public java.lang.Long getAmount() {
		return this.amount;
	}
	/**
	 * 套录ID
	 * @param referEntryId
	 */
	public void setReferEntryId(java.lang.String referEntryId) {
		this.referEntryId = referEntryId;
	}
	
	/**
	 * 套录ID
	 * @return
	 */
	public java.lang.String getReferEntryId() {
		return this.referEntryId;
	}
	/**
	 * 账户心跳时间(毫秒)
	 * @param pulseTime
	 */
	public void setPulseTime(java.lang.String pulseTime) {
		this.pulseTime = pulseTime;
	}
	
	/**
	 * 账户心跳时间(毫秒)
	 * @return
	 */
	public java.lang.String getPulseTime() {
		return this.pulseTime;
	}
	/**
	 * 账户心跳计次
	 * @param pulseDegree
	 */
	public void setPulseDegree(java.lang.Integer pulseDegree) {
		this.pulseDegree = pulseDegree;
	}
	
	/**
	 * 账户心跳计次
	 * @return
	 */
	public java.lang.Integer getPulseDegree() {
		return this.pulseDegree;
	}
	/**
	 * 记账方向借/贷
	 * @param direction
	 */
	public void setDirection(Integer direction) {
		this.direction = direction;
	}
	
	/**
	 * 记账方向借/贷
	 * @return
	 */
	public Integer getDirection() {
		return this.direction;
	}
	/**
	 * 联机余额
	 * @param balanceUsable
	 */
	public void setBalanceUsable(java.lang.Long balanceUsable) {
		this.balanceUsable = balanceUsable;
	}
	
	/**
	 * 联机余额
	 * @return
	 */
	public java.lang.Long getBalanceUsable() {
		return this.balanceUsable;
	}
	/**
	 * 清算/提现余额
	 * @param balanceSettle
	 */
	public void setBalanceSettle(java.lang.Long balanceSettle) {
		this.balanceSettle = balanceSettle;
	}
	
	/**
	 * 清算/提现余额
	 * @return
	 */
	public java.lang.Long getBalanceSettle() {
		return this.balanceSettle;
	}
	/**
	 * 冻结余额
	 * @param balanceFrozon
	 */
	public void setBalanceFrozon(java.lang.Long balanceFrozon) {
		this.balanceFrozon = balanceFrozon;
	}
	
	/**
	 * 冻结余额
	 * @return
	 */
	public java.lang.Long getBalanceFrozon() {
		return this.balanceFrozon;
	}
	/**
	 * 透支额度
	 * @param balanceOverLimit
	 */
	public void setBalanceOverLimit(java.lang.Long balanceOverLimit) {
		this.balanceOverLimit = balanceOverLimit;
	}
	
	/**
	 * 透支额度
	 * @return
	 */
	public java.lang.Long getBalanceOverLimit() {
		return this.balanceOverLimit;
	}
	/**
	 * 贷记余额
	 * @param balanceCredit
	 */
	public void setBalanceCredit(java.lang.Long balanceCredit) {
		this.balanceCredit = balanceCredit;
	}
	
	/**
	 * 贷记余额
	 * @return
	 */
	public java.lang.Long getBalanceCredit() {
		return this.balanceCredit;
	}
	/**
	 * 冲正标记
	 * @param reverseFlag
	 */
	public void setReverseFlag(Integer reverseFlag) {
		this.reverseFlag = reverseFlag;
	}
	
	/**
	 * 冲正标记
	 * @return
	 */
	public Integer getReverseFlag() {
		return this.reverseFlag;
	}
	/**
	 * 交易发起方
	 * @param partyIdFrom
	 */
	public void setPartyIdFrom(java.lang.String partyIdFrom) {
		this.partyIdFrom = partyIdFrom;
	}
	
	/**
	 * 交易发起方
	 * @return
	 */
	public java.lang.String getPartyIdFrom() {
		return this.partyIdFrom;
	}
	/**
	 * 凭证来源(业务或类型)
	 * @param referFrom
	 */
	public void setReferFrom(java.lang.String referFrom) {
		this.referFrom = referFrom;
	}
	
	/**
	 * 凭证来源(业务或类型)
	 * @return
	 */
	public java.lang.String getReferFrom() {
		return this.referFrom;
	}
	/**
	 * 记账凭证号
	 * @param referId
	 */
	public void setReferId(java.lang.String referId) {
		this.referId = referId;
	}
	
	/**
	 * 记账凭证号
	 * @return
	 */
	public java.lang.String getReferId() {
		return this.referId;
	}
	/**
	 * 交易发生额
	 * @param paymentAmount
	 */
	public void setPaymentAmount(java.lang.Long paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	
	/**
	 * 交易发生额
	 * @return
	 */
	public java.lang.Long getPaymentAmount() {
		return this.paymentAmount;
	}
	/**
	 * 交易日期
	 * @param transDate
	 */
	public void setTransDate(java.util.Date transDate) {
		this.transDate = transDate;
	}
	
	/**
	 * 交易日期
	 * @return
	 */
	public java.util.Date getTransDate() {
		return this.transDate;
	}
	/**
	 * 交易相关方
	 * @param thirdPartyId
	 */
	public void setThirdPartyId(java.lang.String thirdPartyId) {
		this.thirdPartyId = thirdPartyId;
	}
	
	/**
	 * 交易相关方
	 * @return
	 */
	public java.lang.String getThirdPartyId() {
		return this.thirdPartyId;
	}
	/**
	 * 我方摘要
	 * @param myNotes
	 */
	public void setMyNotes(java.lang.String myNotes) {
		this.myNotes = myNotes;
	}
	
	/**
	 * 我方摘要
	 * @return
	 */
	public java.lang.String getMyNotes() {
		return this.myNotes;
	}
	/**
	 * 你方摘要
	 * @param yourNotes
	 */
	public void setYourNotes(java.lang.String yourNotes) {
		this.yourNotes = yourNotes;
	}
	
	/**
	 * 你方摘要
	 * @return
	 */
	public java.lang.String getYourNotes() {
		return this.yourNotes;
	}
	/**
	 * 他方摘要
	 * @param hisNote
	 */
	public void setHisNote(java.lang.String hisNote) {
		this.hisNote = hisNote;
	}
	
	/**
	 * 他方摘要
	 * @return
	 */
	public java.lang.String getHisNote() {
		return this.hisNote;
	}
	/**
	 * 备注
	 * @param remark
	 */
	public void setRemark(java.lang.String remark) {
		this.remark = remark;
	}
	
	/**
	 * 备注
	 * @return
	 */
	public java.lang.String getRemark() {
		return this.remark;
	}
	/**
	 * 记录安全码
	 * @param recordMap
	 */
	public void setRecordMap(java.lang.String recordMap) {
		this.recordMap = recordMap;
	}
	
	/**
	 * 记录安全码
	 * @return
	 */
	public java.lang.String getRecordMap() {
		return this.recordMap;
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