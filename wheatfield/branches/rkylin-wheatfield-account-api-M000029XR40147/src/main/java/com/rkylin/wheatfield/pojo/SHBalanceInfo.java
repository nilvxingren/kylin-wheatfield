package com.rkylin.wheatfield.pojo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 用户余额
 * @author  liuhuan
 * @version 2015年11月10日 下午2:06:55
 */
@XStreamAlias("shbalanceinfo")
public class SHBalanceInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/**
	 * 账户余额(分)
	 */
	private java.lang.Long amount;
	
	/**
	 * 可用余额
	 */
	private java.lang.Long balanceUsable;
	
	/**
	 * 清算余额/可提现余额
	 */
	private java.lang.Long balanceSettle;
	
	/**
	 * 冻结余额
	 */
	private java.lang.Long balanceFrozon;
	
	/**
	 * 透支额度
	 */
	private java.lang.Long balanceOverLimit;
	/**
	 * 贷记余额
	 */
	private java.lang.Long balanceCredit;
	/**
	 * 用户id
	 */
	private java.lang.String userId;
	/**
	 * 产品号
	 */
	private java.lang.String productId;
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
	public java.lang.String getUserId() {
		return userId;
	}
	public void setUserId(java.lang.String userId) {
		this.userId = userId;
	}
	public java.lang.String getProductId() {
		return productId;
	}
	public void setProductId(java.lang.String productId) {
		this.productId = productId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	} 
}
