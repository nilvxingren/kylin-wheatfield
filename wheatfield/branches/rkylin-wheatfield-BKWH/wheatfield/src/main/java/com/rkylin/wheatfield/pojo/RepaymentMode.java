package com.rkylin.wheatfield.pojo;

import java.io.Serializable;


public class RepaymentMode implements Serializable{
	private static final long serialVersionUID = 1L;
	public String getRateId() {
		return rateId;
	}
	public void setRateId(String rateId) {
		this.rateId = rateId;
	}
	public String getRateType() {
		return rateType;
	}
	public void setRateType(String rateType) {
		this.rateType = rateType;
	}
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
	public String getRateProperty() {
		return rateProperty;
	}
	public void setRateProperty(String rateProperty) {
		this.rateProperty = rateProperty;
	}
	public String getRateInterestForm() {
		return rateInterestForm;
	}
	public void setRateInterestForm(String rateInterestForm) {
		this.rateInterestForm = rateInterestForm;
	}
	public String getRateInterestType() {
		return rateInterestType;
	}
	public void setRateInterestType(String rateInterestType) {
		this.rateInterestType = rateInterestType;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	private String rateId;
	private String rateType;
	private String billDay;
	private String repaymentDay;
	private String rateProperty;
	private String rateInterestForm;
	private String rateInterestType;
	private String rate;	

}
