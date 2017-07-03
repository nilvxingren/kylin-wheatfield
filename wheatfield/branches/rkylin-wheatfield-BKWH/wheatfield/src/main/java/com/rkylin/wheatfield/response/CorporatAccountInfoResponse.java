/**
 * @File name : CorporatAccountInfoResponse.java
 * @Package : com.rkylin.wheatfield.response
 * @Creator : liuhuan
 * @CreateTime : 2015年9月8日 上午11:17:09
 * @Version : 1.0
 */
package com.rkylin.wheatfield.response;

import java.util.List;

import com.rkylin.wheatfield.pojo.CorporatAccountInfo;
import com.thoughtworks.xstream.annotations.XStreamAlias;

public class CorporatAccountInfoResponse extends Response {
	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;
	private boolean is_success=true;
	private String msg;
	@XStreamAlias("corporateaccounts")
	private List<CorporatAccountInfo> corporateaccounts;

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
	public List<CorporatAccountInfo> getCorporateaccounts() {
		return corporateaccounts;
	}
	public void setCorporateaccounts(List<CorporatAccountInfo> corporateaccounts) {
		this.corporateaccounts = corporateaccounts;
	}
}
