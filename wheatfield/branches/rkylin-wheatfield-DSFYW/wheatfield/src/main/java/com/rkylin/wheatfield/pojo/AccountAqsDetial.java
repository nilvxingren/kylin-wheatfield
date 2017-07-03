package com.rkylin.wheatfield.pojo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("accountaqsdetial")
public class AccountAqsDetial implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String errmsg;

	private String daymaxsucccnt;

	private String agreementno;

	private String monmaxsuccamt;

	private String acct;

	private String status;

	private String daymaxsuccamt;

	private String maxsingleamt;

	private String memid;

	private String acctname;

	private String monmaxsucccnt;

	private String contractno;

	private String agrdeadline;

	public String getErrmsg() {
		return this.errmsg;
	}

	public void setErrmsg(String paramString) {
		this.errmsg = paramString;
	}

	public String getDaymaxsucccnt() {
		return this.daymaxsucccnt;
	}

	public void setDaymaxsucccnt(String paramString) {
		this.daymaxsucccnt = paramString;
	}

	public String getAgreementno() {
		return this.agreementno;
	}

	public void setAgreementno(String paramString) {
		this.agreementno = paramString;
	}

	public String getMonmaxsuccamt() {
		return this.monmaxsuccamt;
	}

	public void setMonmaxsuccamt(String paramString) {
		this.monmaxsuccamt = paramString;
	}

	public String getAcct() {
		return this.acct;
	}

	public void setAcct(String paramString) {
		this.acct = paramString;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String paramString) {
		this.status = paramString;
	}

	public String getDaymaxsuccamt() {
		return this.daymaxsuccamt;
	}

	public void setDaymaxsuccamt(String paramString) {
		this.daymaxsuccamt = paramString;
	}

	public String getMaxsingleamt() {
		return this.maxsingleamt;
	}

	public void setMaxsingleamt(String paramString) {
		this.maxsingleamt = paramString;
	}

	public String getMemid() {
		return this.memid;
	}

	public void setMemid(String paramString) {
		this.memid = paramString;
	}

	public String getAcctname() {
		return this.acctname;
	}

	public void setAcctname(String paramString) {
		this.acctname = paramString;
	}

	public String getMonmaxsucccnt() {
		return this.monmaxsucccnt;
	}

	public void setMonmaxsucccnt(String paramString) {
		this.monmaxsucccnt = paramString;
	}

	public String getContractno() {
		return this.contractno;
	}

	public void setContractno(String paramString) {
		this.contractno = paramString;
	}

	public String getAgrdeadline() {
		return this.agrdeadline;
	}

	public void setAgrdeadline(String paramString) {
		this.agrdeadline = paramString;
	}
}
