package com.rkylin.wheatfield.model;

import java.util.List;
import com.rkylin.wheatfield.pojo.CorporatAccountInfo;


/**
 * 对公银行卡数据封装
 * @author Achilles
 *
 */
public class CorAccountInfoResponse extends CommonResponse{

	private List<CorporatAccountInfo> corAccountInfoList;

	public List<CorporatAccountInfo> getCorAccountInfoList() {
		return corAccountInfoList;
	}

	public void setCorAccountInfoList(List<CorporatAccountInfo> corAccountInfoList) {
		this.corAccountInfoList = corAccountInfoList;
	}

	
	
	
	

	
}
