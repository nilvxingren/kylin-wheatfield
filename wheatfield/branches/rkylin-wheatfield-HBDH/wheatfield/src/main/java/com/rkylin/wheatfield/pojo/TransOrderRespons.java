package com.rkylin.wheatfield.pojo;

import java.util.List;

import com.rkylin.wheatfield.response.Response;

/**
 * @author  likun
 * @version 创建时间：2015-4-23 下午3:35:17
 * 类说明
 */
public class TransOrderRespons extends Response {

	private List<TransOrder> transorder;

	public List<TransOrder> getTransorder() {
		return transorder;
	}

	public void setTransorder(List<TransOrder> transorder) {
		this.transorder = transorder;
	}
}
