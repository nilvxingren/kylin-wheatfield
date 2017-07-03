package com.rkylin.wheatfield.model;

import java.util.List;

import com.rkylin.wheatfield.pojo.Balance;
import com.rkylin.wheatfield.pojo.BalanceDetail;


/**
 * 账户余额数据封装
 * @author Achilles
 *
 */
public class BalanceDeResponse extends CommonResponse{

	private List<BalanceDetail> balanceDeList;

	public List<BalanceDetail> getBalanceDeList() {
		return balanceDeList;
	}

	public void setBalanceDeList(List<BalanceDetail> balanceDeList) {
		this.balanceDeList = balanceDeList;
	}


	
	
}
