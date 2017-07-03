package com.rkylin.wheatfield.response;

import java.util.List;

import com.rkylin.wheatfield.pojo.CreditRepaymentMonth;


public class CreditRepaymentMonthResponse extends Response{
	private List<CreditRepaymentMonth> creditrepaymentmonths;
	private CreditRepaymentMonth creditrepaymentmonth;
	public List<CreditRepaymentMonth> getCreditrepaymentmonths() {
		return creditrepaymentmonths;
	}
	public void setCreditrepaymentmonths(
			List<CreditRepaymentMonth> creditrepaymentmonths) {
		this.creditrepaymentmonths = creditrepaymentmonths;
	}
	public CreditRepaymentMonth getCreditrepaymentmonth() {
		return creditrepaymentmonth;
	}
	public void setCreditrepaymentmonth(CreditRepaymentMonth creditrepaymentmonth) {
		this.creditrepaymentmonth = creditrepaymentmonth;
	}

}
