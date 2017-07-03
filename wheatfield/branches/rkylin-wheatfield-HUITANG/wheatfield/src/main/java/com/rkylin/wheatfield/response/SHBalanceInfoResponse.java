/**
 * @Package : com.rkylin.wheatfield.response
 * @Creator : liuhuan
 * @CreateTime : 2015年11月10日 上午11:17:09
 * @Version : 1.0
 */
package com.rkylin.wheatfield.response;

import java.util.List;

import com.rkylin.wheatfield.pojo.SHBalanceInfo;
import com.thoughtworks.xstream.annotations.XStreamAlias;

public class SHBalanceInfoResponse extends Response {
	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;
	private boolean is_success=true;
	private String msg;
	private String code;
	@XStreamAlias("shbalanceinfolist")
	private List<SHBalanceInfo> shbalanceinfolist;

	public boolean isIs_success() {
		return is_success;
	}
	public void setIs_success(boolean is_success) {
		this.is_success = is_success;
	}
	public List<SHBalanceInfo> getShbalanceinfolist() {
		return shbalanceinfolist;
	}
	public void setShbalanceinfolist(List<SHBalanceInfo> shbalanceinfolist) {
		this.shbalanceinfolist = shbalanceinfolist;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
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
