/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.pojo;

import java.io.Serializable;

/**
 * FuncCode
 * @author code-generator
 *
 */
public class FuncCode implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.Integer funcId;
	private java.lang.String funcCode;
	private java.lang.String funcName;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;

	/**
	 * 功能编码ID
	 * @param funcId
	 */
	public void setFuncId(java.lang.Integer funcId) {
		this.funcId = funcId;
	}
	
	/**
	 * 功能编码ID
	 * @return
	 */
	public java.lang.Integer getFuncId() {
		return this.funcId;
	}
	/**
	 * 功能编码
	 * @param funcCode
	 */
	public void setFuncCode(java.lang.String funcCode) {
		this.funcCode = funcCode;
	}
	
	/**
	 * 功能编码
	 * @return
	 */
	public java.lang.String getFuncCode() {
		return this.funcCode;
	}
	/**
	 * 功能名称
	 * @param funcName
	 */
	public void setFuncName(java.lang.String funcName) {
		this.funcName = funcName;
	}
	
	/**
	 * 功能名称
	 * @return
	 */
	public java.lang.String getFuncName() {
		return this.funcName;
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