package com.rkylin.wheatfield.response;

import java.util.List;

import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.pojo.FinanacePerson;

public class FinanacePersonResponse extends CommonResponse {

    private List<FinanacePerson> finanacePersonList;

    public List<FinanacePerson> getFinanacePersonList() {
        return finanacePersonList;
    }

    public void setFinanacePersonList(List<FinanacePerson> finanacePersonList) {
        this.finanacePersonList = finanacePersonList;
    }

    
    
}
