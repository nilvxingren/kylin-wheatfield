package com.rkylin.wheatfield.model;

import java.util.List;

import com.rkylin.wheatfield.bean.OpenAccountPerson;

public class FinPersonResponse extends CommonResponse {

    private List<OpenAccountPerson> accountPersons;
    private int totalCount;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<OpenAccountPerson> getAccountPersons() {
        return accountPersons;
    }

    public void setAccountPersons(List<OpenAccountPerson> accountPersons) {
        this.accountPersons = accountPersons;
    }

    
}
