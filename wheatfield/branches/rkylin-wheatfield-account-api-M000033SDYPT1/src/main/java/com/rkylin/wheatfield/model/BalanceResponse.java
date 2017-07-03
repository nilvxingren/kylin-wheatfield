package com.rkylin.wheatfield.model;

import java.util.List;

import com.rkylin.wheatfield.pojo.Balance;
import com.rkylin.wheatfield.pojo.SHBalanceInfo;


/**
 * 账户余额数据封装
 * @author Achilles
 *
 */
public class BalanceResponse extends CommonResponse{

	private Balance balance;
	private List<SHBalanceInfo> shBalanceInfoList;
	
	public Balance getBalance() {
		return balance;
	}
	public void setBalance(Balance balance) {
		this.balance = balance;
	}
	public List<SHBalanceInfo> getShBalanceInfoList() {
		return shBalanceInfoList;
	}
	public void setShBalanceInfoList(List<SHBalanceInfo> shBalanceInfoList) {
		this.shBalanceInfoList = shBalanceInfoList;
	}

	
	

	
}
