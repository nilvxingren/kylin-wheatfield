package com.rkylin.wheatfield.response;

import java.util.List;

import com.rkylin.wheatfield.pojo.InterestRepayment;

public class InterestRepaymentResponse extends Response {
	private String urlKey;
	
	private boolean is_success = false;
	
	private String out_principal;
	
	private String out_fees; 
	
	private String rtn_msg;
	
	private List<InterestRepayment> repaymentes;

	public String getUrlKey() {
		return urlKey;
	}

	public void setUrlKey(String urlKey) {
		this.urlKey = urlKey;
	}

	public boolean isIs_success() {
		return is_success;
	}

	public void setIs_success(boolean is_success) {
		this.is_success = is_success;
	}

	

	public List<InterestRepayment> getRepaymentes() {
		return repaymentes;
	}

	public void setRepaymentes(List<InterestRepayment> repaymentes) {
		this.repaymentes = repaymentes;
	}

	public String getOut_principal() {
		return out_principal;
	}

	public void setOut_principal(String out_principal) {
		this.out_principal = out_principal;
	}

	public String getOut_fees() {
		return out_fees;
	}

	public void setOut_fees(String out_fees) {
		this.out_fees = out_fees;
	}

	public String getRtn_msg() {
		return rtn_msg;
	}

	public void setRtn_msg(String rtn_msg) {
		this.rtn_msg = rtn_msg;
	}
}
