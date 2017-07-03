package com.rkylin.wheatfield.bean;

import java.io.Serializable;

/**
 * 输入参数,订单
 * @author Achilles
 *
 */
public class OrderQuery  implements Serializable{

	private String[] requestNoArray;
	private Integer[] statusArray;
	public String[] getRequestNoArray() {
		return requestNoArray;
	}
	public void setRequestNoArray(String[] requestNoArray) {
		this.requestNoArray = requestNoArray;
	}
	public Integer[] getStatusArray() {
		return statusArray;
	}
	public void setStatusArray(Integer[] statusArray) {
		this.statusArray = statusArray;
	}
	
	
	
	
}
