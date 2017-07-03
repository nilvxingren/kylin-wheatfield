package com.rkylin.wheatfield.model;

import java.util.List;

import com.rkylin.wheatfield.pojo.CityCode;


public class CityCodeResponse  extends CommonResponse{
    
    private List<CityCode> cityCodeList;

    public List<CityCode> getCityCodeList() {
        return cityCodeList;
    }

    public void setCityCodeList(List<CityCode> cityCodeList) {
        this.cityCodeList = cityCodeList;
    }
    

}
