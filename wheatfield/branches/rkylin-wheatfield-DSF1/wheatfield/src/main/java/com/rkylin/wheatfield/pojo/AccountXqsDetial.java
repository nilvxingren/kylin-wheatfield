package com.rkylin.wheatfield.pojo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("accountxqsdetial")
public class AccountXqsDetial implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ret_code;
	private String ret_message;
	@XStreamAlias("accountaqsdetials")
	private List<AccountAqsDetial> accountaqsdetials;
	public String getRet_code() {
		return ret_code;
	}
	public void setRet_code(String ret_code) {
		this.ret_code = ret_code;
	}
	public String getRet_message() {
		return ret_message;
	}
	public void setRet_message(String ret_message) {
		this.ret_message = ret_message;
	}
	public List<AccountAqsDetial> getAccountaqsdetials() {
		return accountaqsdetials;
	}
	public void setAccountaqsdetials(List<AccountAqsDetial> accountaqsdetials) {
		this.accountaqsdetials = accountaqsdetials;
	}
	
}
