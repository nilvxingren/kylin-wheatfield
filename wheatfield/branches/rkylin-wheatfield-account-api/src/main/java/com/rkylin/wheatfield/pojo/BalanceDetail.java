package com.rkylin.wheatfield.pojo;

import java.io.Serializable;

public class BalanceDetail  implements Serializable{

	private Balance balance;
	private String name;

	public Balance getBalance() {
		return balance;
	}

	public void setBalance(Balance balance) {
		this.balance = balance;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	 
	 
}
