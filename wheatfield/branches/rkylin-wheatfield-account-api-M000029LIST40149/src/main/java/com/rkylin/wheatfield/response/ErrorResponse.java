package com.rkylin.wheatfield.response;

import java.util.List;

public class ErrorResponse extends Response{
	private static final long serialVersionUID = 1L;
	// 错误代码
	private String code;
	// 错误信息
	private String msg;
	// 子错误代码
	private String subCode;
	// 子错误信息
	private String subMsg;
	// 参数列表
	private List<String> args;
	//返回结果
	private boolean is_success;
	//授权码
	private String authCode;
	
public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public boolean isIs_success() {
		return is_success;
	}
	public void setIs_success(boolean is_success) {
		this.is_success = is_success;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getSubCode() {
		return subCode;
	}
	public void setSubCode(String subCode) {
		this.subCode = subCode;
	}
	public String getSubMsg() {
		return subMsg;
	}
	public void setSubMsg(String subMsg) {
		this.subMsg = subMsg;
	}
	public List<String> getArgs() {
		return args;
	}
	public void setArgs(List<String> args) {
		this.args = args;
	}
}
