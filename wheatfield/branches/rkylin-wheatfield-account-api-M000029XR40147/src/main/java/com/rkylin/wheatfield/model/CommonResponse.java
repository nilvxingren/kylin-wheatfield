package com.rkylin.wheatfield.model;

import java.io.Serializable;


public class CommonResponse implements Serializable{

	/**
	 * 封装返回数据基类
	 */
	private static final long serialVersionUID = 1L;
	
	public String msg;
	public String code = "1";
	
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


}
