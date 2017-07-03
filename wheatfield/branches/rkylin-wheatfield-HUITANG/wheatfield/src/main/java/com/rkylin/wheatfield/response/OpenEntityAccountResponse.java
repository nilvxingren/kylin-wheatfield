package com.rkylin.wheatfield.response;

import java.util.List;

import com.rkylin.wheatfield.pojo.FinanaceCompany;
import com.rkylin.wheatfield.pojo.FinanacePerson;

public class OpenEntityAccountResponse extends Response{

	private boolean is_success=true;
	private String msg;
	private List<FinanacePerson> finanacepersons;
	private List<FinanaceCompany> finanacecompanys;
	
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
	
	public List<FinanacePerson> getFinanacepersons() {
		return finanacepersons;
	}

	public void setFinanacepersons(List<FinanacePerson> finanacepersons) {
		this.finanacepersons = finanacepersons;
	}
	
	public List<FinanaceCompany> getFinanacecompanys() {
		return finanacecompanys;
	}

	public void setFinanacecompanys(List<FinanaceCompany> finanacecompanys) {
		this.finanacecompanys = finanacecompanys;
	}
}
