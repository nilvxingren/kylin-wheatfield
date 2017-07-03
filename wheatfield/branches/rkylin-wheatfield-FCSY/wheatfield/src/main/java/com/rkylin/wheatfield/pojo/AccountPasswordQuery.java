/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.pojo;

import java.io.Serializable;

/**
 * AccountPasswordQuery
 * @author code-generator
 *
 */
public class AccountPasswordQuery implements Serializable{
	private static final long serialVersionUID = 1L;
	private java.lang.String statusId;
	private java.lang.Integer acctPawdId;
	private java.lang.String rootInstCd;
	private java.lang.String userId;
	private java.lang.String passwordType;
	private Integer prosetFlag;
	private java.lang.String password;
	private java.lang.Integer allowErrorCount;
	private java.lang.Integer errorCount;
	private java.lang.String remark;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;

	/**
	 * 账户密码ID
	 * @param acctPawdId
	 */
	public void setAcctPawdId(java.lang.Integer acctPawdId) {
		this.acctPawdId = acctPawdId;
	}
	
	/**
	 * 账户密码ID
	 * @return
	 */
	public java.lang.Integer getAcctPawdId() {
		return this.acctPawdId;
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
	 * 用户号
	 * @param userId
	 */
	public void setUserId(java.lang.String userId) {
		this.userId = userId;
	}
	
	/**
	 * 用户号
	 * @return
	 */
	public java.lang.String getUserId() {
		return this.userId;
	}
	/**
	 * 密码类型.P支付密码,Q查询密码
	 * @param passwordType
	 */
	public void setPasswordType(java.lang.String passwordType) {
		this.passwordType = passwordType;
	}
	
	/**
	 * 密码类型.P支付密码,Q查询密码
	 * @return
	 */
	public java.lang.String getPasswordType() {
		return this.passwordType;
	}
	/**
	 * 是否预设密码
	 * @param prosetFlag
	 */
	public void setProsetFlag(Integer prosetFlag) {
		this.prosetFlag = prosetFlag;
	}
	
	/**
	 * 是否预设密码
	 * @return
	 */
	public Integer getProsetFlag() {
		return this.prosetFlag;
	}
	/**
	 * 密码
	 * @param password
	 */
	public void setPassword(java.lang.String password) {
		this.password = password;
	}
	
	/**
	 * 密码
	 * @return
	 */
	public java.lang.String getPassword() {
		return this.password;
	}
	/**
	 * 允许密码连续错误次数
	 * @param allowErrorCount
	 */
	public void setAllowErrorCount(java.lang.Integer allowErrorCount) {
		this.allowErrorCount = allowErrorCount;
	}
	
	/**
	 * 允许密码连续错误次数
	 * @return
	 */
	public java.lang.Integer getAllowErrorCount() {
		return this.allowErrorCount;
	}
	/**
	 * 密码连续错误次数
	 * @param errorCount
	 */
	public void setErrorCount(java.lang.Integer errorCount) {
		this.errorCount = errorCount;
	}
	
	/**
	 * 密码连续错误次数
	 * @return
	 */
	public java.lang.Integer getErrorCount() {
		return this.errorCount;
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

	public java.lang.String getStatusId() {
		return statusId;
	}

	public void setStatusId(java.lang.String statusId) {
		this.statusId = statusId;
	}
}