package com.rkylin.wheatfield.model;


import com.rkylin.wheatfield.pojo.Balance;


/**
 * 账户余额数据封装
 * @author Achilles
 *
 */
public class FinAccountBalanceResponse extends CommonResponse{

	private Balance balance;
	
	public Balance getBalance() {
		return balance;
	}
	public void setBalance(Balance balance) {
		this.balance = balance;
	}
}
