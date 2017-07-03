package com.rkylin.wheatfield.pojo;

import java.io.Serializable;

/**
 * OrgInfo
 * @author code-generator
 *
 */
public class OrgInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.Integer orgId;
	private java.lang.String rootInstCd;
	private java.lang.String rootInstName;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;

	/**
	 * 机构编码ID
	 * @param orgId
	 */
	public void setOrgId(java.lang.Integer orgId) {
		this.orgId = orgId;
	}
	
	/**
	 * 机构编码ID
	 * @return
	 */
	public java.lang.Integer getOrgId() {
		return this.orgId;
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
	 * 机构名称
	 * @param rootInstName
	 */
	public void setRootInstName(java.lang.String rootInstName) {
		this.rootInstName = rootInstName;
	}
	
	/**
	 * 机构名称
	 * @return
	 */
	public java.lang.String getRootInstName() {
		return this.rootInstName;
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