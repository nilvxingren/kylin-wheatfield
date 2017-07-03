package com.rkylin.wheatfield.model;

import java.util.List;

import com.rkylin.wheatfield.bean.OpenAccountCompany;

public class FinCompanyResponse extends CommonResponse {

    private List<OpenAccountCompany> accountCompanys;
    private int totalCount;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<OpenAccountCompany> getAccountCompanys() {
        return accountCompanys;
    }

    public void setAccountCompanys(List<OpenAccountCompany> accountCompanys) {
        this.accountCompanys = accountCompanys;
    }

    
}
