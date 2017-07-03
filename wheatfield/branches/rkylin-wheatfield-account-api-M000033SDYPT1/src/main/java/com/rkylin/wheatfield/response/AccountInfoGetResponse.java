package com.rkylin.wheatfield.response;

import java.util.List;

import com.rkylin.wheatfield.pojo.FinanaceEntryDto;
import com.thoughtworks.xstream.annotations.XStreamAlias;

public class AccountInfoGetResponse extends Response{
	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;
	private boolean is_success=true;
	private String msg;
	@XStreamAlias("finanaceEntrys")
	private List<FinanaceEntryDto> finanaceEntryList;
	
	/**
	 * @return the finanaceEntryList
	 */
	public List<FinanaceEntryDto> getFinanaceEntryList() {
		return finanaceEntryList;
	}
	/**
	 * @param finanaceEntryList the finanaceEntryList to set
	 */
	public void setFinanaceEntryList(List<FinanaceEntryDto> finanaceEntryList) {
		this.finanaceEntryList = finanaceEntryList;
	}
	public boolean isIs_success() {
		return is_success;
	}
	public void setIs_success(boolean is_success) {
		this.is_success = is_success;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
