package com.rkylin.wheatfield.response;

import com.Rop.api.domain.ApprovedCredit;
import com.rkylin.wheatfield.pojo.CreditApprovalInfo;

public class CreditApprovalInfoResponse extends Response {
	
	private boolean is_success;
	
	private String retmsg;
	
	private ApprovedCredit approvedcredit;
	
	public boolean isIs_success() {
		return is_success;
	}

	public void setIs_success(boolean is_success) {
		this.is_success = is_success;
	}

	public String getRetmsg() {
		return retmsg;
	}

	public void setRetmsg(String retmsg) {
		this.retmsg = retmsg;
	}

	public ApprovedCredit getApprovedcredit() {
		return approvedcredit;
	}

	public void setApprovedcredit(ApprovedCredit approvedcredit) {
		this.approvedcredit = approvedcredit;
	}

	

}
