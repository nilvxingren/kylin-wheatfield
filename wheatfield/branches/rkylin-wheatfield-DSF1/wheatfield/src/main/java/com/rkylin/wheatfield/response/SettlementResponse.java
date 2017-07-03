package com.rkylin.wheatfield.response;

import java.util.List;

import com.rkylin.wheatfield.pojo.CreditInfo;

public class SettlementResponse extends Response {
	private String urlkey;
	private List<CreditInfo> CreditInfoList;
	public String getUrlkey() {
		return urlkey;
	}
	public void setUrlkey(String urlkey) {
		this.urlkey = urlkey;
	}
	public List<CreditInfo> getCreditInfoList() {
		return CreditInfoList;
	}
	public void setCreditInfoList(List<CreditInfo> creditInfoList) {
		CreditInfoList = creditInfoList;
	}
}
