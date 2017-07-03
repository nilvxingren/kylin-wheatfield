package com.rkylin.wheatfield.response;

import java.util.List;

import com.rkylin.wheatfield.pojo.AccountAqsDetial;
import com.thoughtworks.xstream.annotations.XStreamAlias;

public class UtilsResponse extends Response {
	//账单日
	private String billDay;
	//还款日
	private String repaymentDay;
	@XStreamAlias("ret_code")
	private String ret_code;
	@XStreamAlias("ret_message")
	private String ret_message;
	@XStreamAlias("accountaqsdetials")
	private List<AccountAqsDetial> accountaqsdetials;
	
	public String getBillDay() {
		return billDay;
	}
	public void setBillDay(String billDay) {
		this.billDay = billDay;
	}
	public String getRepaymentDay() {
		return repaymentDay;
	}
	public void setRepaymentDay(String repaymentDay) {
		this.repaymentDay = repaymentDay;
	}
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
