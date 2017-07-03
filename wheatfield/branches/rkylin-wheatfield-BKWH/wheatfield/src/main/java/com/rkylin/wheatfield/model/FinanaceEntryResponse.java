package com.rkylin.wheatfield.model;

import java.util.List;

import com.rkylin.wheatfield.pojo.FinanaceEntry;

public class FinanaceEntryResponse extends CommonResponse{

    private List<FinanaceEntry> entryList;

    public List<FinanaceEntry> getEntryList() {
        return entryList;
    }

    public void setEntryList(List<FinanaceEntry> entryList) {
        this.entryList = entryList;
    }
    
    
}
