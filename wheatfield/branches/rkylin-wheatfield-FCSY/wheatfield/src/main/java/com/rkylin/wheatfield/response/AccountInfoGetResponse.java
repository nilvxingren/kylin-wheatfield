package com.rkylin.wheatfield.response;

import java.util.List;

import com.rkylin.wheatfield.pojo.AccountInfo;
import com.rkylin.wheatfield.pojo.AccountInfoPlus;
import com.rkylin.wheatfield.pojo.CreditApprovalInfo;
import com.rkylin.wheatfield.pojo.CreditInfo;
import com.rkylin.wheatfield.pojo.CreditRepaymentHistory;
import com.rkylin.wheatfield.pojo.FinanaceCompany;
import com.rkylin.wheatfield.pojo.FinanaceEntryDto;
import com.thoughtworks.xstream.annotations.XStreamAlias;

public class AccountInfoGetResponse extends Response{
	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;
	private String user_account;
	private boolean is_success=true;
	private List<CreditInfo> creditinfos;
	private List<CreditRepaymentHistory> creditrepaymenthistorys;
	private String msg;
	private List<AccountInfo> accountinfos;
	@XStreamAlias("accountinfopluss")
	private List<AccountInfoPlus> accountinfopluss;
	private List<CreditApprovalInfo> creditApprovalInfos;
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
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public List<CreditApprovalInfo> getCreditApprovalInfos() {
		return creditApprovalInfos;
	}
	public void setCreditApprovalInfos(List<CreditApprovalInfo> creditApprovalInfos) {
		this.creditApprovalInfos = creditApprovalInfos;
	}
	public List<AccountInfoPlus> getAccountinfopluss() {
		return accountinfopluss;
	}
	public void setAccountinfopluss(List<AccountInfoPlus> accountinfopluss) {
		this.accountinfopluss = accountinfopluss;
	}
	@XStreamAlias("openaccountcompanys")
	private List<FinanaceCompany> finanaceCompanies;
	
	public List<FinanaceCompany> getFinanaceCompanies() {
		return finanaceCompanies;
	}
	public void setFinanaceCompanies(List<FinanaceCompany> finanaceCompanies) {
		this.finanaceCompanies = finanaceCompanies;
	}
	public String getUser_account() {
		return user_account;
	}
	public void setUser_account(String user_account) {
		this.user_account = user_account;
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
	public List<CreditInfo> getCreditinfos() {
		return creditinfos;
	}
	public void setCreditinfos(List<CreditInfo> creditinfos) {
		this.creditinfos = creditinfos;
	}
	public List<AccountInfo> getAccountinfos() {
		return accountinfos;
	}
	public void setAccountinfos(List<AccountInfo> accountinfos) {
		this.accountinfos = accountinfos;
	}
	public List<CreditRepaymentHistory> getCreditrepaymenthistorys() {
		return creditrepaymenthistorys;
	}
	public void setCreditrepaymenthistorys(
			List<CreditRepaymentHistory> creditrepaymenthistorys) {
		this.creditrepaymenthistorys = creditrepaymenthistorys;
	}
	
}
