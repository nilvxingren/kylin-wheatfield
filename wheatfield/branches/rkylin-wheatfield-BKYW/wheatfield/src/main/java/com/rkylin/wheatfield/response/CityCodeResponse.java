package com.rkylin.wheatfield.response;

import java.util.List;

import com.rkylin.wheatfield.pojo.CityCode;
import com.rkylin.wheatfield.pojo.TlBankCode;


public class CityCodeResponse extends Response{
	private List<CityCode> citycodes;
	private List<TlBankCode> bankinfos;

	public List<CityCode> getCitycodes() {
		return citycodes;
	}

	public void setCitycodes(List<CityCode> citycodes) {
		this.citycodes = citycodes;
	}

	public List<TlBankCode> getBankinfos() {
		return bankinfos;
	}

	public void setBankinfos(List<TlBankCode> bankinfos) {
		this.bankinfos = bankinfos;
	}

}
