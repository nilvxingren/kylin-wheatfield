package com.rkylin.wheatfield.bean;

import java.io.Serializable;
import java.util.Date;

public class OpenAccountPerson implements Serializable {

    private String personChnName;//中文姓名
    private String personEngName;//英文姓名
    private Integer personType;//个人类别 默认1
    private String personSex;//性别（1：男，2：女）
    private String birthday;//出生日期,YYYYMMDD
    private String certificateType;//证件类型,0身份证;1护照;2军官证;3士兵证;4回乡证;5户口本;6外国护照;7其它(如果证件类型有值则证件号必须有值)
    private String certificateNumber;//证件号
    private String mobileTel;//手机号码
    private String fixTel;//固定电话号码
    private String email;//电子邮箱
    private String post;//邮编
    private String address;//地址
    private String remark;//备注
    private String userId;//用户ID
    private String rootInstCd;//机构码
    private String productId;//产品号
    private String roleCode;//RoleCode
    private String userName;//用户姓名
    private String accountCode;//户口号
    private String whetherRealName;//是否实名(0：非实名，1：已实名)默认0
    private String statusId;//状态
    private Date createdTime;
    private Date updatedTime;
    private Date startTime;
    private Date endTime;
    private Integer PageSize;
    private Integer PageNum;
    private String referUserId;
    
    public String getReferUserId() {
        return referUserId;
    }
    public void setReferUserId(String referUserId) {
        this.referUserId = referUserId;
    }   
    public Date getCreatedTime() {
        return createdTime;
    }
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
    public Date getUpdatedTime() {
        return updatedTime;
    }
    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
    public String getStatusId() {
        return statusId;
    }
    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }
    public Date getStartTime() {
        return startTime;
    }
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    public Date getEndTime() {
        return endTime;
    }
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    public Integer getPageSize() {
        return PageSize;
    }
    public void setPageSize(Integer pageSize) {
        PageSize = pageSize;
    }
    public Integer getPageNum() {
        return PageNum;
    }
    public void setPageNum(Integer pageNum) {
        PageNum = pageNum;
    }
    public String getPersonChnName() {
        return personChnName;
    }
    public void setPersonChnName(String personChnName) {
        this.personChnName = personChnName;
    }
    public String getPersonEngName() {
        return personEngName;
    }
    public void setPersonEngName(String personEngName) {
        this.personEngName = personEngName;
    }
    public Integer getPersonType() {
        return personType;
    }
    public void setPersonType(Integer personType) {
        this.personType = personType;
    }
    public String getPersonSex() {
        return personSex;
    }
    public void setPersonSex(String personSex) {
        this.personSex = personSex;
    }
    public String getBirthday() {
        return birthday;
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    public String getCertificateType() {
        return certificateType;
    }
    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }
    public String getCertificateNumber() {
        return certificateNumber;
    }
    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }
    public String getMobileTel() {
        return mobileTel;
    }
    public void setMobileTel(String mobileTel) {
        this.mobileTel = mobileTel;
    }
    public String getFixTel() {
        return fixTel;
    }
    public void setFixTel(String fixTel) {
        this.fixTel = fixTel;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPost() {
        return post;
    }
    public void setPost(String post) {
        this.post = post;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getRootInstCd() {
        return rootInstCd;
    }
    public void setRootInstCd(String rootInstCd) {
        this.rootInstCd = rootInstCd;
    }
    public String getProductId() {
        return productId;
    }
    public void setProductId(String productId) {
        this.productId = productId;
    }
    public String getRoleCode() {
        return roleCode;
    }
    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getAccountCode() {
        return accountCode;
    }
    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }
    public String getWhetherRealName() {
        return whetherRealName;
    }
    public void setWhetherRealName(String whetherRealName) {
        this.whetherRealName = whetherRealName;
    }
    
    
}
