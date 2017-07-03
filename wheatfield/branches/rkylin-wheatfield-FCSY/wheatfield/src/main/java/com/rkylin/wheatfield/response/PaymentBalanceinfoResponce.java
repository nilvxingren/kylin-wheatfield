package com.rkylin.wheatfield.response;

import java.util.List;

import com.rkylin.wheatfield.pojo.AdvanceBalance;
import com.rkylin.wheatfield.pojo.Balance;
import com.thoughtworks.xstream.annotations.XStreamAlias;

public class PaymentBalanceinfoResponce extends Response {

	/**
	 * 账户余额信息
	 */
	@XStreamAlias("balanceinfo")
	private Balance balance;
	/**
	 * 预付金余额信息
	 */
	private List<AdvanceBalance> advancebalances;
	
	

	public List<AdvanceBalance> getAdvancebalances() {
		return advancebalances;
	}

	public void setAdvancebalances(List<AdvanceBalance> advancebalances) {
		this.advancebalances = advancebalances;
	}

	public Balance getBalance() {
		return balance;
	}

	public void setBalance(Balance balance) {
		this.balance = balance;
	}
}
