package com.rkylin.wheatfield.response;

import com.rkylin.wheatfield.bean.AcNode;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("tl_accountinfo_response")
public class TLAccounInfoResponse extends Response{
	private static final long serialVersionUID = 1L;

	// 错误信息
	@XStreamAlias("msg")
	private String msg;
	//返回结果
	@XStreamAlias("is_success")
	private boolean is_success;
	
//	private List<AcNode> acNodeList;
	
	private AcNode acNode;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isIs_success() {
		return is_success;
	}

	public void setIs_success(boolean is_success) {
		this.is_success = is_success;
	}

	public AcNode getAcNode() {
		return acNode;
	}

	public void setAcNode(AcNode acNode) {
		this.acNode = acNode;
	}

}
