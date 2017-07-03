package com.rkylin.wheatfield.service;

import com.rkylin.wheatfield.pojo.Balance;
import com.rkylin.wheatfield.pojo.FinanaceEntry;
import com.rkylin.wheatfield.pojo.TransOrderInfo;

public interface FinanaceEntryService {
	
	public FinanaceEntry getAccountFlow(TransOrderInfo transOrderInfo, Balance balance, String entryId,boolean flag, String quotaFlag);
	
	public String getAccountDate();

}
