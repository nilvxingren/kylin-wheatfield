package com.rkylin.wheatfield.model;

import java.io.Serializable;
import java.util.Map;

import com.rkylin.wheatfield.pojo.TransOrderInfo;


public class TransOrderInfoResponse extends CommonResponse implements Serializable{

	private Map<String,TransOrderInfo> strToOrderMap;

	public Map<String, TransOrderInfo> getStrToOrderMap() {
		return strToOrderMap;
	}

	public void setStrToOrderMap(Map<String, TransOrderInfo> strToOrderMap) {
		this.strToOrderMap = strToOrderMap;
	}


}
