package com.rkylin.wheatfield.model;

import java.util.List;

import com.rkylin.wheatfield.bean.FinAccountInfo;

public class FinAccountInfoResponse extends CommonResponse {

    private List<FinAccountInfo> finAccountInfoList;

    public List<FinAccountInfo> getFinAccountInfoList() {
        return finAccountInfoList;
    }

    public void setFinAccountInfoList(List<FinAccountInfo> finAccountInfoList) {
        this.finAccountInfoList = finAccountInfoList;
    }

    
}
