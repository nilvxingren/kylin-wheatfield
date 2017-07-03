package com.rkylin.wheatfield.response;

import com.rkylin.wheatfield.pojo.FinanaceCompany;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.List;

/**
 * Created by thonny on 2015-5-13.
 */
public class AccountCompanyInfoRespon extends Response{


    private boolean is_success=true;
    private String msg;
    @XStreamAlias("finanacecompanies")
    private List<FinanaceCompany> accountCompanyInfos;

    public boolean is_success() {
        return is_success;
    }

    public List<FinanaceCompany> getAccountCompanyInfos() {
        return accountCompanyInfos;
    }

    public void setAccountCompanyInfos(List<FinanaceCompany> accountCompanyInfos) {
        this.accountCompanyInfos = accountCompanyInfos;
    }

    public boolean isIs_success() {
        return is_success;
    }
    public void setIs_success(boolean is_success) {
        this.is_success = is_success;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
}
