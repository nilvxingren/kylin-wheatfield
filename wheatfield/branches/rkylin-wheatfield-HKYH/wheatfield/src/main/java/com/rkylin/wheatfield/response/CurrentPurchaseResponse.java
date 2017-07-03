package com.rkylin.wheatfield.response;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 公共转账返回信息
 * @author mawanxia
 *
 */
public class CurrentPurchaseResponse extends Response {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4070405853946081198L;
	// 错误代码
	@XStreamAlias("code")
	private String code;
	// 错误信息
	@XStreamAlias("msg")
	private String msg;
	// 参数列表
	@XStreamAlias("args")
	private List<String> args;
	// 返回结果
	@XStreamAlias("is_success")
	private boolean is_success;
	// 授权码
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
