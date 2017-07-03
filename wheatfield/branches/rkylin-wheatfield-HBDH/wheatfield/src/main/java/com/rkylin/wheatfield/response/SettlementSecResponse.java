package com.rkylin.wheatfield.response;

import java.util.List;

import com.rkylin.wheatfield.pojo.CreditRateTemplateRes;
import com.rkylin.wheatfield.pojo.FinanaceEntryDto;
import com.rkylin.wheatfield.pojo.InterestRepayment;
import com.rkylin.wheatfield.pojo.WrongOrder;
import com.thoughtworks.xstream.annotations.XStreamAlias;


public class SettlementSecResponse extends Response {
	@XStreamAlias("is_success")
    private boolean is_success=true;
	@XStreamAlias("creditratetemplates")
	private List<CreditRateTemplateRes> creditRateTemplates;
	@XStreamAlias("rtn_msg")
	private String rtn_msg;
	@XStreamAlias("rateid")
	private String rateId;
	//查询还款计划用
	@XStreamAlias("calculatedate")
	String calculateDate;
	@XStreamAlias("interestdate")
	String interestDate;
	@XStreamAlias("creditratetemplate")
	private CreditRateTemplateRes creditRateTemplate;
	@XStreamAlias("repayments")
	private List<InterestRepayment> repayments;
	@XStreamAlias("wrongorderlist")
	private List<WrongOrder> wrongOrder;
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

	public List<WrongOrder> getWrongOrder() {
		return wrongOrder;
	}

	public void setWrongOrder(List<WrongOrder> wrongOrder) {
		this.wrongOrder = wrongOrder;
	}
	public String getRateId() {
		return rateId;
	}

	public void setRateId(String rateId) {
		this.rateId = rateId;
	}
	public String getCalculateDate() {
		return calculateDate;
	}

	public void setCalculateDate(String calculateDate) {
		this.calculateDate = calculateDate;
	}

	public String getInterestDate() {
		return interestDate;
	}

	public void setInterestDate(String interestDate) {
		this.interestDate = interestDate;
	}

	public CreditRateTemplateRes getCreditRateTemplate() {
		return creditRateTemplate;
	}

	public void setCreditRateTemplate(CreditRateTemplateRes creditRateTemplate) {
		this.creditRateTemplate = creditRateTemplate;
	}

	
	public String getRtn_msg() {
		return rtn_msg;
	}

	public void setRtn_msg(String rtn_msg) {
		this.rtn_msg = rtn_msg;
	}

	public List<CreditRateTemplateRes> getCreditRateTemplates() {
		return creditRateTemplates;
	}

	public void setCreditRateTemplates(List<CreditRateTemplateRes> creditRateTemplate) {
		this.creditRateTemplates = creditRateTemplate;
	}

	public boolean isIs_success() {
		return is_success;
	}

	public void setIs_success(boolean is_success) {
		this.is_success = is_success;
	}

	public List<InterestRepayment> getRepayments() {
		return repayments;
	}

	public void setRepayments(List<InterestRepayment> repayments) {
		this.repayments = repayments;
	}
	
}
