package com.rkylin.wheatfield.model;


import java.util.List;

import com.rkylin.wheatfield.bean.FinAccountAndBalance;


/**
 * 账户/余额数据封装
 * @author Achilles
 *
 */
public class FinAccAndBalanceResponse extends CommonResponse{

	private List<FinAccountAndBalance> finAccAndBalanceList;
	private Long provisionsAmount;
	
	
    public List<FinAccountAndBalance> getFinAccAndBalanceList() {
        return finAccAndBalanceList;
    }
    public void setFinAccAndBalanceList(List<FinAccountAndBalance> finAccAndBalanceList) {
        this.finAccAndBalanceList = finAccAndBalanceList;
    }
    public Long getProvisionsAmount() {
        return provisionsAmount;
    }
    public void setProvisionsAmount(Long provisionsAmount) {
        this.provisionsAmount = provisionsAmount;
    }
	
}
