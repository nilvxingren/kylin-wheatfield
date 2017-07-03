package com.rkylin.wheatfield.response;

import java.io.Serializable;
import java.util.List;

import com.rkylin.wheatfield.pojo.SubTransOrderInfo;

public class CmbTransactionCommonResponse implements Serializable{

	/**
	 * 封装返回数据基类
	 */
	private static final long serialVersionUID = 1L;
	
	private String msg;
	private String code = "1";
	
	private List< SubTransOrderInfo > errList;

	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public List<SubTransOrderInfo> getErrList() {
		return errList;
	}
	public void setErrList(List<SubTransOrderInfo> errList) {
		this.errList = errList;
	}

}
