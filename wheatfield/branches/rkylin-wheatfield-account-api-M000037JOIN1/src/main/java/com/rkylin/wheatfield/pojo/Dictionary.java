/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.wheatfield.pojo;

import java.io.Serializable;

/**
 * Dictionary
 * @author code-generator
 *
 */
public class Dictionary implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.Integer dicId;
	private java.lang.String tableName;
	private java.lang.String dictionaryName;
	private java.lang.String dictionaryValue;
	private java.lang.String dictionaryRemark;
	private java.lang.String dictionaryNameDescribe;
	private java.lang.Integer status;
	private java.lang.String remark;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;

	/**
	 * 字典流水号
	 * @param dicId
	 */
	public void setDicId(java.lang.Integer dicId) {
		this.dicId = dicId;
	}
	
	/**
	 * 字典流水号
	 * @return
	 */
	public java.lang.Integer getDicId() {
		return this.dicId;
	}
	/**
	 * 表名
	 * @param tableName
	 */
	public void setTableName(java.lang.String tableName) {
		this.tableName = tableName;
	}
	
	/**
	 * 表名
	 * @return
	 */
	public java.lang.String getTableName() {
		return this.tableName;
	}
	/**
	 * 字典名称，如 类型为type,状态为status，以实际表中的相关字段为准
	 * @param dictionaryName
	 */
	public void setDictionaryName(java.lang.String dictionaryName) {
		this.dictionaryName = dictionaryName;
	}
	
	/**
	 * 字典名称，如 类型为type,状态为status，以实际表中的相关字段为准
	 * @return
	 */
	public java.lang.String getDictionaryName() {
		return this.dictionaryName;
	}
	/**
	 * 字典对应的值
	 * @param dictionaryValue
	 */
	public void setDictionaryValue(java.lang.String dictionaryValue) {
		this.dictionaryValue = dictionaryValue;
	}
	
	/**
	 * 字典对应的值
	 * @return
	 */
	public java.lang.String getDictionaryValue() {
		return this.dictionaryValue;
	}
	/**
	 * 字典中值的注释
	 * @param dictionaryRemark
	 */
	public void setDictionaryRemark(java.lang.String dictionaryRemark) {
		this.dictionaryRemark = dictionaryRemark;
	}
	
	/**
	 * 字典中值的注释
	 * @return
	 */
	public java.lang.String getDictionaryRemark() {
		return this.dictionaryRemark;
	}
	/**
	 * 字典中名称的描述
	 * @param dictionaryNameDescribe
	 */
	public void setDictionaryNameDescribe(java.lang.String dictionaryNameDescribe) {
		this.dictionaryNameDescribe = dictionaryNameDescribe;
	}
	
	/**
	 * 字典中名称的描述
	 * @return
	 */
	public java.lang.String getDictionaryNameDescribe() {
		return this.dictionaryNameDescribe;
	}
	/**
	 * 状态  1为正常   0为失效
	 * @param status
	 */
	public void setStatus(java.lang.Integer status) {
		this.status = status;
	}
	
	/**
	 * 状态  1为正常   0为失效
	 * @return
	 */
	public java.lang.Integer getStatus() {
		return this.status;
	}
	/**
	 * 信息备注
	 * @param remark
	 */
	public void setRemark(java.lang.String remark) {
		this.remark = remark;
	}
	
	/**
	 * 信息备注
	 * @return
	 */
	public java.lang.String getRemark() {
		return this.remark;
	}
	/**
	 * 创建时间
	 * @param createdTime
	 */
	public void setCreatedTime(java.util.Date createdTime) {
		this.createdTime = createdTime;
	}
	
	/**
	 * 创建时间
	 * @return
	 */
	public java.util.Date getCreatedTime() {
		return this.createdTime;
	}
	/**
	 * 更新时间
	 * @param updatedTime
	 */
	public void setUpdatedTime(java.util.Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	
	/**
	 * 更新时间
	 * @return
	 */
	public java.util.Date getUpdatedTime() {
		return this.updatedTime;
	}
}