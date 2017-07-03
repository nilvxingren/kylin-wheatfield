/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.pojo;

import java.io.Serializable;

/**
 * FinanaceAccountQuery
 * @author code-generator
 *
 */
public class FinanaceAccountQuery implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.String rootInstCd;
	private java.lang.String finAccountId;
	private java.lang.String finAccountTypeId;
	private java.lang.String finAccountName;
	private java.lang.String accountCode;
	private java.lang.String accountRelateId;
	private java.lang.String groupManage;
	private java.lang.String groupSettle;
	private java.lang.String currency;
	private java.lang.Long amount;
	private java.lang.Long balanceUsable;
	private java.lang.Long balanceSettle;
	private java.lang.Long balanceFrozon;
	private java.lang.Long balanceOverLimit;
	private java.lang.Long balanceCredit;
//	private java.lang.Long balanceBonus;
//	private java.lang.Long balancePoints;
	private java.lang.String referUserId;
	private java.util.Date startTime;
	private java.util.Date endTime;
	private java.lang.String bussControl;
	private java.lang.String remark;
	private java.lang.String statusId;
	private java.lang.String recordMap;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;

	public java.lang.String getReferUserId() {
		return referUserId;
	}

	public void setReferUserId(java.lang.String referUserId) {
		this.referUserId = referUserId;
	}

	/**
	 * 管理机构代码--机构
	 * @param rootInstCd
	 */
	public void setRootInstCd(java.lang.String rootInstCd) {
		this.rootInstCd = rootInstCd;
	}
	
	/**
	 * 管理机构代码--机构
	 * @return
	 */
	public java.lang.String getRootInstCd() {
		return this.rootInstCd;
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
	 * 账户类型ID
	 * @param finAccountTypeId
	 */
	public void setFinAccountTypeId(java.lang.String finAccountTypeId) {
		this.finAccountTypeId = finAccountTypeId;
	}
	
	/**
	 * 账户类型ID
	 * @return
	 */
	public java.lang.String getFinAccountTypeId() {
		return this.finAccountTypeId;
	}
	/**
	 * 账户名称--username
	 * @param finAccountName
	 */
	public void setFinAccountName(java.lang.String finAccountName) {
		this.finAccountName = finAccountName;
	}
	
	/**
	 * 账户名称--username
	 * @return
	 */
	public java.lang.String getFinAccountName() {
		return this.finAccountName;
	}
	/**
	 * 户口编码
	 * @param accountCode
	 */
	public void setAccountCode(java.lang.String accountCode) {
		this.accountCode = accountCode;
	}
	
	/**
	 * 户口编码
	 * @return
	 */
	public java.lang.String getAccountCode() {
		return this.accountCode;
	}
	/**
	 * 账户关联ID--userid
	 * @param accountRelateId
	 */
	public void setAccountRelateId(java.lang.String accountRelateId) {
		this.accountRelateId = accountRelateId;
	}
	
	/**
	 * 账户关联ID-userId
	 * @return
	 */
	public java.lang.String getAccountRelateId() {
		return this.accountRelateId;
	}
	/**
	 * 管理分组--产品
	 * @param groupManage
	 */
	public void setGroupManage(java.lang.String groupManage) {
		this.groupManage = groupManage;
	}
	
	/**
	 * 管理分组--产品id
	 * @return
	 */
	public java.lang.String getGroupManage() {
		return this.groupManage;
	}
	/**
	 * 核算分组
	 * @param groupSettle
	 */
	public void setGroupSettle(java.lang.String groupSettle) {
		this.groupSettle = groupSettle;
	}
	
	/**
	 * 核算分组
	 * @return
	 */
	public java.lang.String getGroupSettle() {
		return this.groupSettle;
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
	 * 账户余额(分)
	 * @param amount
	 */
	public void setAmount(java.lang.Long amount) {
		this.amount = amount;
	}
	
	/**
	 * 账户余额(分)
	 * @return
	 */
	public java.lang.Long getAmount() {
		return this.amount;
	}
	/**
	 * 可用余额
	 * @param balanceUsable
	 */
	public void setBalanceUsable(java.lang.Long balanceUsable) {
		this.balanceUsable = balanceUsable;
	}
	
	/**
	 * 可用余额
	 * @return
	 */
	public java.lang.Long getBalanceUsable() {
		return this.balanceUsable;
	}
	/**
	 * 清算余额/可提现余额
	 * @param balanceSettle
	 */
	public void setBalanceSettle(java.lang.Long balanceSettle) {
		this.balanceSettle = balanceSettle;
	}
	
	/**
	 * 清算余额/可提现余额
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
//	/**
//	 * 预留余额1,红包
//	 * @param balanceBonus
//	 */
//	public void setBalanceBonus(java.lang.Long balanceBonus) {
//		this.balanceBonus = balanceBonus;
//	}
//	
//	/**
//	 * 预留余额1,红包
//	 * @return
//	 */
//	public java.lang.Long getBalanceBonus() {
//		return this.balanceBonus;
//	}
//	/**
//	 * 预留余额2,积分
//	 * @param balancePoints
//	 */
//	public void setBalancePoints(java.lang.Long balancePoints) {
//		this.balancePoints = balancePoints;
//	}
//	
//	/**
//	 * 预留余额2,积分
//	 * @return
//	 */
//	public java.lang.Long getBalancePoints() {
//		return this.balancePoints;
//	}
//	/**
//	 * 预留余额3
//	 * @param balanceReserve1
//	 */
//	public void setBalanceReserve1(java.lang.Long balanceReserve1) {
//		this.balanceReserve1 = balanceReserve1;
//	}
//	
//	/**
//	 * 预留余额3
//	 * @return
//	 */
//	public java.lang.Long getBalanceReserve1() {
//		return this.balanceReserve1;
//	}
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
	 * 账户业务控制
	 * @param bussControl
	 */
	public void setBussControl(java.lang.String bussControl) {
		this.bussControl = bussControl;
	}
	
	/**
	 * 账户业务控制
	 * @return
	 */
	public java.lang.String getBussControl() {
		return this.bussControl;
	}
	/**
	 * 开户备注
	 * @param remark
	 */
	public void setRemark(java.lang.String remark) {
		this.remark = remark;
	}
	
	/**
	 * 开户备注
	 * @return
	 */
	public java.lang.String getRemark() {
		return this.remark;
	}
	/**
	 * 账户状态
	 * @param statusId
	 */
	public void setStatusId(java.lang.String statusId) {
		this.statusId = statusId;
	}
	
	/**
	 * 账户状态
	 * @return
	 */
	public java.lang.String getStatusId() {
		return this.statusId;
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