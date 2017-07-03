package com.rkylin.wheatfield.bean;

import java.io.Serializable;
import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class OpenAccountCompany implements Serializable{

    private String companyName;//企业名称
    private String companyShortName;//企业简称
    private String mcc;//MCC码
    private String post;//邮编
    private String connect;//联系方式
    private String address;//地址
    private String buslince;//营业执照
    private String acuntOpnLince;//开户许可证
    private String companyCode;//企业编号
    private String companyType;//企业类型
    private String taxregCard1;//税务登记证1
    private String taxregCard2;//税务登记证2
    private String organCertificate;//组织结构代码证
    private String corporateName;//法人姓名
    private String corporateIdentity;//法人身份证
    private String busPlaceCtf;//经营场所实地认证
    private String loanCard;//贷款卡
    private String remark;//备注
    private String userId;//用户ID
    private String rootInstCd;//机构码
    private String productId;//产品号
    private String roleCode;
    private String userName;//用户名称，及接入机构的用户名
    private String accountCode;//户口号
    private String whetherRealName;//是否实名(0：非实名，1：已实名)默认0
    private Date startTime;
    private Date endTime;
    private Integer PageSize;
    private Integer PageNum;
    private java.util.Date createdTime;
    private java.util.Date updatedTime;
    private String referUserId;
    
    public String getReferUserId() {
        return referUserId;
    }
    public void setReferUserId(String referUserId) {
        this.referUserId = referUserId;
    }
    public java.util.Date getCreatedTime() {
        return createdTime;
    }
    public void setCreatedTime(java.util.Date createdTime) {
        this.createdTime = createdTime;
    }
    public java.util.Date getUpdatedTime() {
        return updatedTime;
    }
    public void setUpdatedTime(java.util.Date updatedTime) {
        this.updatedTime = updatedTime;
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
    public String getCompanyType() {
        return companyType;
    }
    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }
    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    public String getCompanyShortName() {
        return companyShortName;
    }
    public void setCompanyShortName(String companyShortName) {
        this.companyShortName = companyShortName;
    }
    public String getMcc() {
        return mcc;
    }
    public void setMcc(String mcc) {
        this.mcc = mcc;
    }
    public String getPost() {
        return post;
    }
    public void setPost(String post) {
        this.post = post;
    }
    public String getConnect() {
        return connect;
    }
    public void setConnect(String connect) {
        this.connect = connect;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getBuslince() {
        return buslince;
    }
    public void setBuslince(String buslince) {
        this.buslince = buslince;
    }
    public String getAcuntOpnLince() {
        return acuntOpnLince;
    }
    public void setAcuntOpnLince(String acuntOpnLince) {
        this.acuntOpnLince = acuntOpnLince;
    }
    public String getCompanyCode() {
        return companyCode;
    }
    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }
    public String getTaxregCard1() {
        return taxregCard1;
    }
    public void setTaxregCard1(String taxregCard1) {
        this.taxregCard1 = taxregCard1;
    }
    public String getTaxregCard2() {
        return taxregCard2;
    }
    public void setTaxregCard2(String taxregCard2) {
        this.taxregCard2 = taxregCard2;
    }
    public String getOrganCertificate() {
        return organCertificate;
    }
    public void setOrganCertificate(String organCertificate) {
        this.organCertificate = organCertificate;
    }
    public String getCorporateName() {
        return corporateName;
    }
    public void setCorporateName(String corporateName) {
        this.corporateName = corporateName;
    }
    public String getCorporateIdentity() {
        return corporateIdentity;
    }
    public void setCorporateIdentity(String corporateIdentity) {
        this.corporateIdentity = corporateIdentity;
    }
    public String getBusPlaceCtf() {
        return busPlaceCtf;
    }
    public void setBusPlaceCtf(String busPlaceCtf) {
        this.busPlaceCtf = busPlaceCtf;
    }
    public String getLoanCard() {
        return loanCard;
    }
    public void setLoanCard(String loanCard) {
        this.loanCard = loanCard;
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
