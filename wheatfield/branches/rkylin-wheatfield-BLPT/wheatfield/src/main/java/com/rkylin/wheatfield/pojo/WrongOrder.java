package com.rkylin.wheatfield.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("wrongorder")
public class WrongOrder {
	private String orderid;
	private String wrong_msg;
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getWrong_msg() {
		return wrong_msg;
	}
	public void setWrong_msg(String wrong_msg) {
		this.wrong_msg = wrong_msg;
	}
}
