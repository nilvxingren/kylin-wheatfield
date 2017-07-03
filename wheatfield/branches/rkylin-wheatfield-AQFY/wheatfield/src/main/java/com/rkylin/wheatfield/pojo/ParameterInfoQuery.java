/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.pojo;

import java.io.Serializable;

/**
 * ParameterInfoQuery
 * @author code-generator
 *
 */
public class ParameterInfoQuery implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.Integer parameterId;
	private java.lang.String parameterType;
	private java.lang.String productId;
	private java.lang.String parameterCode;
	private java.lang.String parameterValue;
	private Integer status;
	private java.util.Date startTime;
	private java.util.Date endTime;
	private java.lang.String remark;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;

	/**
	 * 参数编码ID
	 * @param parameterId
	 */
	public void setParameterId(java.lang.Integer parameterId) {
		this.parameterId = parameterId;
	}
	
	/**
	 * 参数编码ID
	 * @return
	 */
	public java.lang.Integer getParameterId() {
		return this.parameterId;
	}
	/**
	 * 参数类型
	 * @param parameterType
	 */
	public void setParameterType(java.lang.String parameterType) {
		this.parameterType = parameterType;
	}
	
	/**
	 * 参数类型
	 * @return
	 */
	public java.lang.String getParameterType() {
		return this.parameterType;
	}
	/**
	 * 参数适用产品ID
	 * @param productId
	 */
	public void setProductId(java.lang.String productId) {
		this.productId = productId;
	}
	
	/**
	 * 参数适用产品ID
	 * @return
	 */
	public java.lang.String getProductId() {
		return this.productId;
	}
	/**
	 * 参数编码
	 * @param parameterCode
	 */
	public void setParameterCode(java.lang.String parameterCode) {
		this.parameterCode = parameterCode;
	}
	
	/**
	 * 参数编码
	 * @return
	 */
	public java.lang.String getParameterCode() {
		return this.parameterCode;
	}
	/**
	 * 参数值
	 * @param parameterValue
	 */
	public void setParameterValue(java.lang.String parameterValue) {
		this.parameterValue = parameterValue;
	}
	
	/**
	 * 参数值
	 * @return
	 */
	public java.lang.String getParameterValue() {
		return this.parameterValue;
	}
	/**
	 * 状态
	 * @param status
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	/**
	 * 状态
	 * @return
	 */
	public Integer getStatus() {
		return this.status;
	}
	/**
	 * 生效时间
	 * @param startTime
	 */
	public void setStartTime(java.util.Date startTime) {
		this.startTime = startTime;
	}
	
	/**
	 * 生效时间
	 * @return
	 */
	public java.util.Date getStartTime() {
		return this.startTime;
	}
	/**
	 * 失效时间
	 * @param endTime
	 */
	public void setEndTime(java.util.Date endTime) {
		this.endTime = endTime;
	}
	
	/**
	 * 失效时间
	 * @return
	 */
	public java.util.Date getEndTime() {
		return this.endTime;
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