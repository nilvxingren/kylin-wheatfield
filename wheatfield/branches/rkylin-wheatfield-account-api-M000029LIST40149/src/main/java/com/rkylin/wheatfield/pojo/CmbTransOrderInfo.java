package com.rkylin.wheatfield.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * TransOrderInfo
 * @author code-generator
 *
 */
public class CmbTransOrderInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.String cmbDealCode;
	private java.lang.String orderPackageNo;
	private java.lang.Integer orderCount;
	private java.util.Date orderDate;
	
	private List<SubTransOrderInfo> subTransOrderInfoList;
	
	public java.lang.String getCmbDealCode() {
		return cmbDealCode;
	}

	public void setCmbDealCode(java.lang.String cmbDealCode) {
		this.cmbDealCode = cmbDealCode;
	}

	/**
	 * 订单包号
	 * @param orderPackageNo
	 */
	public void setOrderPackageNo(java.lang.String orderPackageNo) {
		this.orderPackageNo = orderPackageNo;
	}
	
	/**
	 * 订单包号
	 * @return
	 */
	public java.lang.String getOrderPackageNo() {
		return this.orderPackageNo;
	}
	
	public java.lang.Integer getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(java.lang.Integer orderCount) {
		this.orderCount = orderCount;
	}

	/**
	 * 订单日期
	 * @param orderDate
	 */
	public void setOrderDate(java.util.Date orderDate) {
		this.orderDate = orderDate;
	}
	
	/**
	 * 订单日期
	 * @return
	 */
	public java.util.Date getOrderDate() {
		return this.orderDate;
	}

	public List<SubTransOrderInfo> getSubTransOrderInfoList() {
		return subTransOrderInfoList;
	}

	public void setSubTransOrderInfoList(List<SubTransOrderInfo> subTransOrderInfoList) {
		this.subTransOrderInfoList = subTransOrderInfoList;
	}

}