package com.rkylin.wheatfield.model;


import java.util.List;

import com.rkylin.wheatfield.bean.FinAccountAndBalance;


/**
 * 账户/余额数据封装
 * @author Achilles
 *
 */
public class FinAccountAndBalanceResponse extends CommonResponse{

	private List<FinAccountAndBalance> finAccAndBalanceList;
	private Integer totalUserAcount;
	private Long tocalAmount;
	private Integer sumAmount;
	
    public Integer getSumAmount() {
        return sumAmount;
    }
    public void setSumAmount(Integer sumAmount) {
        this.sumAmount = sumAmount;
    }
    public List<FinAccountAndBalance> getFinAccAndBalanceList() {
        return finAccAndBalanceList;
    }
    public void setFinAccAndBalanceList(List<FinAccountAndBalance> finAccAndBalanceList) {
        this.finAccAndBalanceList = finAccAndBalanceList;
    }
    public Integer getTotalUserAcount() {
        return totalUserAcount;
    }
    public void setTotalUserAcount(Integer totalUserAcount) {
        this.totalUserAcount = totalUserAcount;
    }
    public Long getTocalAmount() {
        return tocalAmount;
    }
    public void setTocalAmount(Long tocalAmount) {
        this.tocalAmount = tocalAmount;
    }
	
}
