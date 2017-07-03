package com.rkylin.wheatfield.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("advancebalance")
public class AdvanceBalance {

	/**
	 * 预付金金额
	 */
	@XStreamAlias("amount")
	private long amount;
	/**
	 * 账户userId
	 */
	@XStreamAlias("userid")
	private String userId;
	/**
	 * 类型  0=商户，1=台长，3=当天消费金额
	 */
	@XStreamAlias("type")
	private int type;
	/**
	 * 台长Id
	 */
	@XStreamAlias("referuserid")
	private String referUserId;
	/**
	 * 台长发生额
	 */
	@XStreamAlias("accrual")
	private long accrual;
	/**
	 * 台长发生额
	 */
	public long getAccrual() {
		return accrual;
	}
	/**
	 * 台长发生额
	 */
	public void setAccrual(long accrual) {
		this.accrual = accrual;
	}
	public String getReferUserId() {
		return referUserId;
	}
	public void setReferUserId(String referUserId) {
		this.referUserId = referUserId;
	}
	/**
	 * 预付金金额
	 */
	public long getAmount() {
		return amount;
	}
	/**
	 * 预付金金额
	 */
	public void setAmount(long amount) {
		this.amount = amount;
	}
	/**
	 * 账户userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * 账户userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * 类型  0=商户，1=台长，3=当天消费金额
	 */
	public int getType() {
		return type;
	}
	/**
	 * 类型  0=商户，1=台长，3=当天消费金额
	 */
	public void setType(int type) {
		this.type = type;
	}
	
}
