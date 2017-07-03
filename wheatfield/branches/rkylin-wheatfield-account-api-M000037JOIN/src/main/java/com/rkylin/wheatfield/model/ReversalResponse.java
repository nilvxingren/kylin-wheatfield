package com.rkylin.wheatfield.model;

import java.util.List;

import com.rkylin.wheatfield.pojo.Balance;


/**
 * 抹账数据封装
 * @author Achilles
 *
 */
public class ReversalResponse extends CommonResponse{

	private List<Balance> balanceBeforeList;
	private List<Balance> balanceAfterList;
	public List<Balance> getBalanceBeforeList() {
		return balanceBeforeList;
	}
	public void setBalanceBeforeList(List<Balance> balanceBeforeList) {
		this.balanceBeforeList = balanceBeforeList;
	}
	public List<Balance> getBalanceAfterList() {
		return balanceAfterList;
	}
	public void setBalanceAfterList(List<Balance> balanceAfterList) {
		this.balanceAfterList = balanceAfterList;
	}
	

	
	

	
}
