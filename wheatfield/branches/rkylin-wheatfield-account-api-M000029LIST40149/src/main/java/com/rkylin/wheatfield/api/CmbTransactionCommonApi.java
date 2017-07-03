package com.rkylin.wheatfield.api;

import com.rkylin.wheatfield.pojo.CmbTransOrderInfo;
import com.rkylin.wheatfield.response.CmbTransactionCommonResponse;

public interface CmbTransactionCommonApi {
	
	public CmbTransactionCommonResponse execute(CmbTransOrderInfo multTransOrderInfo );


}
