package com.rkylin.wheatfield.model;

import com.rkylin.wheatfield.pojo.FinanaceAccount;

public class FinanaceAccountResponse extends CommonResponse {

    private FinanaceAccount finanaceAccount;

    public FinanaceAccount getFinanaceAccount() {
        return finanaceAccount;
    }

    public void setFinanaceAccount(FinanaceAccount finanaceAccount) {
        this.finanaceAccount = finanaceAccount;
    }
    
}
