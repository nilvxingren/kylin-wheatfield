package com.rkylin.wheatfield.model;

import java.util.List;

import com.rkylin.wheatfield.pojo.CityCode;
import com.rkylin.wheatfield.pojo.TlBankCode;


public class BankResponse  extends CommonResponse{
    
    private List<TlBankCode> bankInfos;

    public List<TlBankCode> getBankInfos() {
        return bankInfos;
    }

    public void setBankInfos(List<TlBankCode> bankInfos) {
        this.bankInfos = bankInfos;
    }


    

}
