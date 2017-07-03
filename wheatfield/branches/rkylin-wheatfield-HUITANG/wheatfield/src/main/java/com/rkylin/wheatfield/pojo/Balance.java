package com.rkylin.wheatfield.pojo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 用户余额
 * @author  zhenpc@chanjet.com
 * @version 2015年2月3日 下午4:01:55
 */
public class Balance implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/**
	 * 账户余额(分)
	 */
	@XStreamAlias("amount")
	private java.lang.Long amount;
	
	/**
	 * 可用余额
	 */
	@XStreamAlias("balanceusable")
	private java.lang.Long balanceUsable;
	
	/**
	 * 清算余额/可提现余额
	 */
	@XStreamAlias("balancesettle")
	private java.lang.Long balanceSettle;
	
	/**
	 * 冻结余额
	 */
	@XStreamAlias("balancefrozon")
	private java.lang.Long balanceFrozon;
	
	/**
	 * 透支额度
	 */
	@XStreamAlias("balanceoverlimit")
	private java.lang.Long balanceOverLimit;
	/**
	 * 贷记余额
	 */
	@XStreamAlias("balancecredit")
	private java.lang.Long balanceCredit;
	/**
	 * 账户心跳计次
	 */
	@XStreamAlias("pulsedegree")
	private java.lang.Integer pulseDegree;
	/**
	 * 账户心跳时间(毫秒)
	 */
	@XStreamAlias("pulsetime")
	private java.lang.String pulseTime; 
	/**
	 * 账户Id
	 */
	@XStreamAlias("finaccountid")
	private java.lang.String finAccountId;
	

	public java.lang.String getFinAccountId() {
		return finAccountId;
	}

	public void setFinAccountId(java.lang.String finAccountId) {
		this.finAccountId = finAccountId;
	}

	public java.lang.Integer getPulseDegree() {
		return pulseDegree;
	}

	public void setPulseDegree(java.lang.Integer pulseDegree) {
		this.pulseDegree = pulseDegree;
	}

	public java.lang.String getPulseTime() {
		return pulseTime;
	}

	public void setPulseTime(java.lang.String pulseTime) {
		this.pulseTime = pulseTime;
	}

	public java.lang.Long getAmount() {
		return amount;
	}

	public void setAmount(java.lang.Long amount) {
		this.amount = amount;
	}

	public java.lang.Long getBalanceUsable() {
		return balanceUsable;
	}

	public void setBalanceUsable(java.lang.Long balanceUsable) {
		this.balanceUsable = balanceUsable;
	}

	public java.lang.Long getBalanceSettle() {
		return balanceSettle;
	}

	public void setBalanceSettle(java.lang.Long balanceSettle) {
		this.balanceSettle = balanceSettle;
	}

	public java.lang.Long getBalanceFrozon() {
		return balanceFrozon;
	}

	public void setBalanceFrozon(java.lang.Long balanceFrozon) {
		this.balanceFrozon = balanceFrozon;
	}

	public java.lang.Long getBalanceOverLimit() {
		return balanceOverLimit;
	}

	public void setBalanceOverLimit(java.lang.Long balanceOverLimit) {
		this.balanceOverLimit = balanceOverLimit;
	}

	public java.lang.Long getBalanceCredit() {
		return balanceCredit;
	}

	public void setBalanceCredit(java.lang.Long balanceCredit) {
		this.balanceCredit = balanceCredit;
	}
	
	
}
