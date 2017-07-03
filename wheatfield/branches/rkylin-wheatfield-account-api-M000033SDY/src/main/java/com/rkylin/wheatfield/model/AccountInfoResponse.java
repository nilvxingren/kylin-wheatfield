package com.rkylin.wheatfield.model;

import java.util.List;
import java.util.Map;
import com.rkylin.wheatfield.pojo.AccountInfo;


/**
 * 对私银行卡数据封装
 * @author Achilles
 *
 */
public class AccountInfoResponse extends CommonResponse{

	private Map<String,List<AccountInfo>> accountInfoListMap;

	public Map<String, List<AccountInfo>> getAccountInfoListMap() {
		return accountInfoListMap;
	}

	public void setAccountInfoListMap(Map<String, List<AccountInfo>> accountInfoListMap) {
		this.accountInfoListMap = accountInfoListMap;
	}
	
	
}
