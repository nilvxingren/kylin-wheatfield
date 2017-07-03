/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.pojo;

import java.io.Serializable;

/**
 * FinanacePersonQuery
 * @author code-generator
 *
 */
public class FinanacePersonQuery implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private Integer personId;
    private String personCode;
    private String finAccountId;
    private String personChnName;
    private String personEngName;
    private Integer personType;
    private String personSex;
    private String birthday;
    private String certificateType;
    private String certificateNumber;
    private String mobileTel;
    private String fixTel;
    private String email;
    private String post;
    private String address;
    private String statusId;
    private String remark;
    private java.util.Date createdTime;
    private java.util.Date updatedTime;
    private String rootInstCd;
    private String userId;
    private String birthdayFrom;
    private String birthdayTo;
    private java.util.Date createdTimeFrom;
    private java.util.Date createdTimeTo;
    
    private Integer dataIndex;
    private Integer dataSize;

    public Integer getDataIndex() {
        return dataIndex;
    }

    public void setDataIndex(Integer dataIndex) {
        this.dataIndex = dataIndex;
    }

    public Integer getDataSize() {
        return dataSize;
    }

    public void setDataSize(Integer dataSize) {
        this.dataSize = dataSize;
    }

    public java.util.Date getCreatedTimeFrom() {
        return createdTimeFrom;
    }

    public java.util.Date getCreatedTimeTo() {
        return createdTimeTo;
    }

    /**
     * 个人ID
     * @param personId
     */
    public void setPersonId(Integer personId) {
        this.personId = personId;
    }
    
    /**
     * 个人ID
     * @return
     */
    public Integer getPersonId() {
        return this.personId;
    }
    
    /**
     * 个人编号
     * @param personCode
     */
    public void setPersonCode(String personCode) {
        this.personCode = personCode;
    }
    
    /**
     * 个人编号
     * @return
     */
    public String getPersonCode() {
        return this.personCode;
    }
    
    /**
     * 账户ID
     * @param finAccountId
     */
    public void setFinAccountId(String finAccountId) {
        this.finAccountId = finAccountId;
    }
    
    /**
     * 账户ID
     * @return
     */
    public String getFinAccountId() {
        return this.finAccountId;
    }
    
    /**
     * 中文姓名
     * @param personChnName
     */
    public void setPersonChnName(String personChnName) {
        this.personChnName = personChnName;
    }
    
    /**
     * 中文姓名
     * @return
     */
    public String getPersonChnName() {
        return this.personChnName;
    }
    
    /**
     * 英文姓名
     * @param personEngName
     */
    public void setPersonEngName(String personEngName) {
        this.personEngName = personEngName;
    }
    
    /**
     * 英文姓名
     * @return
     */
    public String getPersonEngName() {
        return this.personEngName;
    }
    
    /**
     * 个人类别
     * @param personType
     */
    public void setPersonType(Integer personType) {
        this.personType = personType;
    }
    
    /**
     * 个人类别
     * @return
     */
    public Integer getPersonType() {
        return this.personType;
    }
    
    /**
     * 性别
     * @param personSex
     */
    public void setPersonSex(String personSex) {
        this.personSex = personSex;
    }
    
    /**
     * 性别
     * @return
     */
    public String getPersonSex() {
        return this.personSex;
    }
    
    /**
     * 出生日期,YYYYMMDD
     * @param birthday
     */
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    
    /**
     * 出生日期,YYYYMMDD
     * @return
     */
    public String getBirthday() {
        return this.birthday;
    }
    
    /**
     * 证件类型,0身份�?;1护照;2军官�?;3士兵�?;4回乡�?;5户口�?;6外国护照;7其它
     * @param certificateType
     */
    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }
    
    /**
     * 证件类型,0身份�?;1护照;2军官�?;3士兵�?;4回乡�?;5户口�?;6外国护照;7其它
     * @return
     */
    public String getCertificateType() {
        return this.certificateType;
    }
    
    /**
     * 证件�?
     * @param certificateNumber
     */
    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }
    
    /**
     * 证件�?
     * @return
     */
    public String getCertificateNumber() {
        return this.certificateNumber;
    }
    
    /**
     * 手机号码
     * @param mobileTel
     */
    public void setMobileTel(String mobileTel) {
        this.mobileTel = mobileTel;
    }
    
    /**
     * 手机号码
     * @return
     */
    public String getMobileTel() {
        return this.mobileTel;
    }
    
    /**
     * 固定电话号码
     * @param fixTel
     */
    public void setFixTel(String fixTel) {
        this.fixTel = fixTel;
    }
    
    /**
     * 固定电话号码
     * @return
     */
    public String getFixTel() {
        return this.fixTel;
    }
    
    /**
     * 电子邮箱
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * 电子邮箱
     * @return
     */
    public String getEmail() {
        return this.email;
    }
    
    /**
     * 邮编
     * @param post
     */
    public void setPost(String post) {
        this.post = post;
    }
    
    /**
     * 邮编
     * @return
     */
    public String getPost() {
        return this.post;
    }
    
    /**
     * 地址
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }
    
    /**
     * 地址
     * @return
     */
    public String getAddress() {
        return this.address;
    }
    
    /**
     * 状�??,0失效,1生效
     * @param statusId
     */
    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }
    
    /**
     * 状�??,0失效,1生效
     * @return
     */
    public String getStatusId() {
        return this.statusId;
    }
    
    /**
     * 备注
     * @param remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    /**
     * 备注
     * @return
     */
    public String getRemark() {
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
    
    /**
     * 机构码
     * @param rootInstCd
     */
    public String getRootInstCd() {
        return rootInstCd;
    }
    
    /**
     * 机构码
     * @return
     */
    public void setRootInstCd(String rootInstCd) {
        this.rootInstCd = rootInstCd;
    }
    
    /**
     * 接入机构用户ID
     * @param userId
     */
    public String getUserId() {
        return userId;
    }
    
    /**
     * 接入机构用户ID
     * @return
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    /**
     * 出生日期From
     * @param birthdayFrom
     */
    public String getBirthdayFrom() {
        return birthdayFrom;
    }
    
    /**
     * 出生日期From
     * @return
     */
    public void setBirthdayFrom(String birthdayFrom) {
        this.birthdayFrom = birthdayFrom;
    }
    
    /**
     * 出生日期To
     * @param birthdayTo
     */
    public String getBirthdayTo() {
        return birthdayTo;
    }
    
    /**
     * 出生日期To
     * @return
     */
    public void setBirthdayTo(String birthdayTo) {
        this.birthdayTo = birthdayTo;
    }
    
    /**
     * 记录创建时间From
     * @param createdTimeFrom
     */
    public void setCreatedTimeFrom(java.util.Date createdTimeFrom) {
        this.createdTimeFrom = createdTimeFrom;
    }
    
    /**
     * 记录创建时间From
     * @return
     */
    public java.util.Date geCreatedTimeFrom() {
        return this.createdTimeFrom;
    }
    
    /**
     * 记录创建时间To
     * @param createdTimeTo
     */
    public void setCreatedTimeTo(java.util.Date createdTimeTo) {
        this.createdTimeTo = createdTimeTo;
    }
    
    /**
     * 记录创建时间To
     * @return
     */
    public java.util.Date geCreatedTimeTo() {
        return this.createdTimeTo;
    }
}