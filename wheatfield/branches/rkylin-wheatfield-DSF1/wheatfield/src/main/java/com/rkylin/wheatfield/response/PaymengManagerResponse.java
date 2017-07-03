package com.rkylin.wheatfield.response;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class PaymengManagerResponse extends Response{
	// 错误代码
	@XStreamAlias("code")
	private String code;
	// 错误信息
	@XStreamAlias("msg")
	private String msg;
	// 子错误代码
	@XStreamAlias("sub_code")
	private String subCode;
	// 子错误信息
	@XStreamAlias("sub_msg")
	private String subMsg;
	// 参数列表
	@XStreamAlias("args")
	private List<String> args;
	//返回结果
	@XStreamAlias("is_success")
	private boolean is_success;
	//授权码
	@XStreamAlias("authcode")
	private String authCode;
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
	public boolean isIs_success() {
		return is_success;
	}
	public void setIs_success(boolean is_success) {
		this.is_success = is_success;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
}
