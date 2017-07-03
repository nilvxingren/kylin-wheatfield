package com.rkylin.wheatfield.model;

import java.util.List;

import com.rkylin.wheatfield.pojo.AccountInfor;

public class AccountInfoQueryResponse  extends CommonResponse{
    
    private List<AccountInfor> accountInfos;
    private int totalCount;
    
    public List<AccountInfor> getAccountInfos() {
        return accountInfos;
    }
    public void setAccountInfos(List<AccountInfor> accountInfos) {
        this.accountInfos = accountInfos;
    }
    public int getTotalCount() {
        return totalCount;
    }
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
    

}
