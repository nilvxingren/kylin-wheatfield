/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.pojo;

import java.io.Serializable;

/**
 * SettleBatchManage
 * @author code-generator
 *
 */
public class SettleBatchManage implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.Integer batchId;
	private java.lang.String rootInstCd;
	private java.lang.String batchType;
	private java.lang.String batchSubNo;
	private java.lang.String batchCons;
	private java.lang.String batchNum;
	private java.lang.String batchNumLen;
	private java.lang.String remark;
	private java.util.Date accountDate;
	private java.lang.String obligate1;
	private java.lang.String obligate2;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;

	/**
	 * 批次号ID
	 * @param batchId
	 */
	public void setBatchId(java.lang.Integer batchId) {
		this.batchId = batchId;
	}
	
	/**
	 * 批次号ID
	 * @return
	 */
	public java.lang.Integer getBatchId() {
		return this.batchId;
	}
	/**
	 * 管理机构代码
	 * @param rootInstCd
	 */
	public void setRootInstCd(java.lang.String rootInstCd) {
		this.rootInstCd = rootInstCd;
	}
	
	/**
	 * 管理机构代码
	 * @return
	 */
	public java.lang.String getRootInstCd() {
		return this.rootInstCd;
	}
	/**
	 * 批次号类型
	 * @param batchType
	 */
	public void setBatchType(java.lang.String batchType) {
		this.batchType = batchType;
	}
	
	/**
	 * 批次号类型
	 * @return
	 */
	public java.lang.String getBatchType() {
		return this.batchType;
	}
	/**
	 * 批次号子号
	 * @param batchSubNo
	 */
	public void setBatchSubNo(java.lang.String batchSubNo) {
		this.batchSubNo = batchSubNo;
	}
	
	/**
	 * 批次号子号
	 * @return
	 */
	public java.lang.String getBatchSubNo() {
		return this.batchSubNo;
	}
	/**
	 * 批次号固定内容
	 * @param batchCons
	 */
	public void setBatchCons(java.lang.String batchCons) {
		this.batchCons = batchCons;
	}
	
	/**
	 * 批次号固定内容
	 * @return
	 */
	public java.lang.String getBatchCons() {
		return this.batchCons;
	}
	/**
	 * 批次号发番数
	 * @param batchNum
	 */
	public void setBatchNum(java.lang.String batchNum) {
		this.batchNum = batchNum;
	}
	
	/**
	 * 批次号发番数
	 * @return
	 */
	public java.lang.String getBatchNum() {
		return this.batchNum;
	}
	/**
	 * 批次号发番数长度
	 * @param batchNumLen
	 */
	public void setBatchNumLen(java.lang.String batchNumLen) {
		this.batchNumLen = batchNumLen;
	}
	
	/**
	 * 批次号发番数长度
	 * @return
	 */
	public java.lang.String getBatchNumLen() {
		return this.batchNumLen;
	}
	/**
	 * 备注
	 * @param remark
	 */
	public void setRemark(java.lang.String remark) {
		this.remark = remark;
	}
	
	/**
	 * 备注
	 * @return
	 */
	public java.lang.String getRemark() {
		return this.remark;
	}
	/**
	 * 记账日期
	 * @param accountDate
	 */
	public void setAccountDate(java.util.Date accountDate) {
		this.accountDate = accountDate;
	}
	
	/**
	 * 记账日期
	 * @return
	 */
	public java.util.Date getAccountDate() {
		return this.accountDate;
	}
	/**
	 * 预留字段1
	 * @param obligate1
	 */
	public void setObligate1(java.lang.String obligate1) {
		this.obligate1 = obligate1;
	}
	
	/**
	 * 预留字段1
	 * @return
	 */
	public java.lang.String getObligate1() {
		return this.obligate1;
	}
	/**
	 * 预留字段2
	 * @param obligate2
	 */
	public void setObligate2(java.lang.String obligate2) {
		this.obligate2 = obligate2;
	}
	
	/**
	 * 预留字段2
	 * @return
	 */
	public java.lang.String getObligate2() {
		return this.obligate2;
	}
	/**
	 * 记录创建时间
	 * @param createdTime
	 */
	public void setCreatedTime(java.util.Date createdTime) {
		this.createdTime = createdTime;
	}
	
	/**
	 * 记录创建时间
	 * @return
	 */
	public java.util.Date getCreatedTime() {
		return this.createdTime;
	}
	/**
	 * 记录更新时间
	 * @param updatedTime
	 */
	public void setUpdatedTime(java.util.Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	
	/**
	 * 记录更新时间
	 * @return
	 */
	public java.util.Date getUpdatedTime() {
		return this.updatedTime;
	}
}