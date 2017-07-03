package com.rkylin.wheatfield.model;

import java.util.List;

import com.rkylin.wheatfield.pojo.FinanaceAccount;

public class UserResponse extends CommonResponse {

    private List<FinanaceAccount>  finAccountList;
    
    public List<FinanaceAccount> getFinAccountList() {
        return finAccountList;
    }

    public void setFinAccountList(List<FinanaceAccount> finAccountList) {
        this.finAccountList = finAccountList;
    }
}
