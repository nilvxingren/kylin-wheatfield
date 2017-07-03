package com.rkylin.wheatfield.response;

import java.util.List;

import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.pojo.FinanaceCompany;

public class FinanaceCompanyResponse extends CommonResponse {

    private List<FinanaceCompany> finanaceCompanyList;

    public List<FinanaceCompany> getFinanaceCompanyList() {
        return finanaceCompanyList;
    }

    public void setFinanaceCompanyList(List<FinanaceCompany> finanaceCompanyList) {
        this.finanaceCompanyList = finanaceCompanyList;
    }
    
    
}
