/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.pojo;

import java.io.Serializable;

/**
 * ManageHistoryQuery
 * @author code-generator
 *
 */
public class ManageHistoryQuery implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.String finAccountId;
	private java.lang.String operateUserId;
	private Integer operateType;
	private java.lang.String operateDesc;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;

	/**
	 * 账号ID
	 * @param finAccountId
	 */
	public void setFinAccountId(java.lang.String finAccountId) {
		this.finAccountId = finAccountId;
	}
	
	/**
	 * 账号ID
	 * @return
	 */
	public java.lang.String getFinAccountId() {
		return this.finAccountId;
	}
	/**
	 * 操作人员ID
	 * @param operateUserId
	 */
	public void setOperateUserId(java.lang.String operateUserId) {
		this.operateUserId = operateUserId;
	}
	
	/**
	 * 操作人员ID
	 * @return
	 */
	public java.lang.String getOperateUserId() {
		return this.operateUserId;
	}
	/**
	 * 操作类型
	 * @param operateType
	 */
	public void setOperateType(Integer operateType) {
		this.operateType = operateType;
	}
	
	/**
	 * 操作类型
	 * @return
	 */
	public Integer getOperateType() {
		return this.operateType;
	}
	/**
	 * 操作说明
	 * @param operateDesc
	 */
	public void setOperateDesc(java.lang.String operateDesc) {
		this.operateDesc = operateDesc;
	}
	
	/**
	 * 操作说明
	 * @return
	 */
	public java.lang.String getOperateDesc() {
		return this.operateDesc;
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