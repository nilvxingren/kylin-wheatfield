package com.rkylin.wheatfield.common;

import java.util.Map;

public class ValHasNoParam {
	public static boolean hasParam(Map<String, String[]> requestParams, String key){

		boolean checkFlag = false;

		if(requestParams.containsKey(key) && requestParams.get(key) != null && !"".equals(requestParams.get(key)[0])){
			checkFlag = true;
		}

		return checkFlag;
	}

	public static  boolean hasAllParam(Map<String,String[]> requestParams,String[] keys){
		boolean checkFlag = false;
		for(String key : keys){
			if(requestParams.containsKey(key) && requestParams.get(key) != null && !"".equals(requestParams.get(key))){
				checkFlag = true;
			}
		}
		return checkFlag;
	}
}
