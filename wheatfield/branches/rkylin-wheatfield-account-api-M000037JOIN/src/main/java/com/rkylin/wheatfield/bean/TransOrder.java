package com.rkylin.wheatfield.bean;

import java.io.Serializable;

/**
 * 输入参数,订单
 * @author Achilles
 *
 */
public class TransOrder  implements Serializable{

	private Integer requestId;
	private String instCode;
	private String userId;
	private String orderNo;
	private String orderNoG;
	private Long amountG;
	private Integer status;
	
	public Integer getRequestId() {
		return requestId;
	}
	public void setRequestId(Integer requestId) {
		this.requestId = requestId;
	}
	public String getInstCode() {
		return instCode;
	}
	public void setInstCode(String instCode) {
		this.instCode = instCode;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getOrderNoG() {
		return orderNoG;
	}
	public void setOrderNoG(String orderNoG) {
		this.orderNoG = orderNoG;
	}
	public Long getAmountG() {
		return amountG;
	}
	public void setAmountG(Long amountG) {
		this.amountG = amountG;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
}
