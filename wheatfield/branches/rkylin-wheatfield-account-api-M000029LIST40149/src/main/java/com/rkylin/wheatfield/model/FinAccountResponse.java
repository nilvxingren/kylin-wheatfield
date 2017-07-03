package com.rkylin.wheatfield.model;

import java.util.List;
import com.rkylin.wheatfield.pojo.FinanaceAccount;

/**
 * 用户账户数据封装
 * @author Achilles
 *
 */
public class FinAccountResponse extends CommonResponse{

	private List<FinanaceAccount> finAccList;

	public List<FinanaceAccount> getFinAccList() {
		return finAccList;
	}

	public void setFinAccList(List<FinanaceAccount> finAccList) {
		this.finAccList = finAccList;
	}

	
}
