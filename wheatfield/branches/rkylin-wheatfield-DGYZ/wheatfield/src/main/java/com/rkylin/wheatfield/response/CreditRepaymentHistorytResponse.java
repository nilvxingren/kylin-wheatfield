package com.rkylin.wheatfield.response;

import com.Rop.api.domain.CreditRepaymentInfo;


public class CreditRepaymentHistorytResponse extends Response{
	private CreditRepaymentInfo creditRepaymentInfo;

	public CreditRepaymentInfo getCreditRepaymentInfo() {
		return creditRepaymentInfo;
	}

	public void setCreditRepaymentInfo(CreditRepaymentInfo creditRepaymentInfo) {
		this.creditRepaymentInfo = creditRepaymentInfo;
	}

	
}
