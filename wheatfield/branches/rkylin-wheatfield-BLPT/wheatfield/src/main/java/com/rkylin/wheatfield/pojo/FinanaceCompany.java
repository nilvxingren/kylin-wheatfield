/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * FinanaceCompany
 * @author code-generator
 *
 */
@XStreamAlias("openaccountcompany")
public class FinanaceCompany implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer companyId;
	private String companyCode;
	private String finAccountId;
	@XStreamAlias("companyname")
	private String companyName;
	@XStreamAlias("companyshortname")
	private String companyShortName;
	private Integer companyType;
	@XStreamAlias("mcc")
	private String mcc;
	@XStreamAlias("post")
	private String post;
	@XStreamAlias("connect")
	private String connect;
	@XStreamAlias("address")
	private String address;
	@XStreamAlias("buslince")
	private String buslince;
	@XStreamAlias("acuntopnlince")
	private String acuntOpnLince;
	@XStreamAlias("texregcardf")
	private String taxregCard1;
	@XStreamAlias("taxregcards")
	private String taxregCard2;
	@XStreamAlias("organcertificate")
	private String organCertificate;
	@XStreamAlias("corporatename")
	private String corporateName;
	@XStreamAlias("corporateidentity")
	private String corporateIdentity;
	@XStreamAlias("busplacectf")
	private String busPlaceCtf;
	@XStreamAlias("loancard")
	private String loanCard;
	@XStreamAlias("statusid")
	private String statusId;
	@XStreamAlias("remark")
	private String remark;
	@XStreamAlias("createdtime")
	private java.util.Date createdTime;
	@XStreamAlias("updatedtime")
	private java.util.Date updatedTime;
	@XStreamAlias("userid")
	private String userId;
	
    private String accountCode;//户口号
    private String whetherRealName;//是否实名(0：非实名，1：已实名)默认0
    private String rootInstCd;
    private String productId;
    private String finAccountName;
    private String referUserId;
    
    public String getReferUserId() {
        return referUserId;
    }

    public void setReferUserId(String referUserId) {
        this.referUserId = referUserId;
    }

	public String getFinAccountName() {
        return finAccountName;
    }

    public void setFinAccountName(String finAccountName) {
        this.finAccountName = finAccountName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public String getRootInstCd() {
        return rootInstCd;
    }

    public void setRootInstCd(String rootInstCd) {
        this.rootInstCd = rootInstCd;
    }

    /**
	 * 企业ID
	 * @param companyId
	 */
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	
	/**
	 * 企业ID
	 * @return
	 */
	public Integer getCompanyId() {
		return this.companyId;
	}
	/**
	 * 
	 * @param companyCode
	 */
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getCompanyCode() {
		return this.companyCode;
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
	 * 企业名称
	 * @param companyName
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	/**
	 * 企业名称
	 * @return
	 */
	public String getCompanyName() {
		return this.companyName;
	}
	/**
	 * �?�?
	 * @param companyShortName
	 */
	public void setCompanyShortName(String companyShortName) {
		this.companyShortName = companyShortName;
	}
	
	/**
	 * �?�?
	 * @return
	 */
	public String getCompanyShortName() {
		return this.companyShortName;
	}
	/**
	 * 企业类别
	 * @param companyType
	 */
	public void setCompanyType(Integer companyType) {
		this.companyType = companyType;
	}
	
	/**
	 * 企业类别
	 * @return
	 */
	public Integer getCompanyType() {
		return this.companyType;
	}
	/**
	 * 行业类别
	 * @param mcc
	 */
	public void setMcc(String mcc) {
		this.mcc = mcc;
	}
	
	/**
	 * 行业类别
	 * @return
	 */
	public String getMcc() {
		return this.mcc;
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
	 * 联系方式
	 * @param connect
	 */
	public void setConnect(String connect) {
		this.connect = connect;
	}
	
	/**
	 * 联系方式
	 * @return
	 */
	public String getConnect() {
		return this.connect;
	}
	/**
	 * 企业地址
	 * @param address
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	
	/**
	 * 企业地址
	 * @return
	 */
	public String getAddress() {
		return this.address;
	}
	/**
	 * 营业执照
	 * @param buslince
	 */
	public void setBuslince(String buslince) {
		this.buslince = buslince;
	}
	
	/**
	 * 营业执照
	 * @return
	 */
	public String getBuslince() {
		return this.buslince;
	}
	/**
	 * �?户许可证
	 * @param acuntOpnLince
	 */
	public void setAcuntOpnLince(String acuntOpnLince) {
		this.acuntOpnLince = acuntOpnLince;
	}
	
	/**
	 * �?户许可证
	 * @return
	 */
	public String getAcuntOpnLince() {
		return this.acuntOpnLince;
	}
	/**
	 * 税务登记�?1
	 * @param taxregCard1
	 */
	public void setTaxregCard1(String taxregCard1) {
		this.taxregCard1 = taxregCard1;
	}
	
	/**
	 * 税务登记�?1
	 * @return
	 */
	public String getTaxregCard1() {
		return this.taxregCard1;
	}
	/**
	 * 税务登记�?2
	 * @param taxregCard2
	 */
	public void setTaxregCard2(String taxregCard2) {
		this.taxregCard2 = taxregCard2;
	}
	
	/**
	 * 税务登记�?2
	 * @return
	 */
	public String getTaxregCard2() {
		return this.taxregCard2;
	}
	/**
	 * 组织机构代码�?
	 * @param organCertificate
	 */
	public void setOrganCertificate(String organCertificate) {
		this.organCertificate = organCertificate;
	}
	
	/**
	 * 组织机构代码�?
	 * @return
	 */
	public String getOrganCertificate() {
		return this.organCertificate;
	}
	/**
	 * 法人姓名
	 * @param corporateName
	 */
	public void setCorporateName(String corporateName) {
		this.corporateName = corporateName;
	}
	
	/**
	 * 法人姓名
	 * @return
	 */
	public String getCorporateName() {
		return this.corporateName;
	}
	/**
	 * 法人身份�?
	 * @param corporateIdentity
	 */
	public void setCorporateIdentity(String corporateIdentity) {
		this.corporateIdentity = corporateIdentity;
	}
	
	/**
	 * 法人身份�?
	 * @return
	 */
	public String getCorporateIdentity() {
		return this.corporateIdentity;
	}
	/**
	 * 经营场所实地认证
	 * @param busPlaceCtf
	 */
	public void setBusPlaceCtf(String busPlaceCtf) {
		this.busPlaceCtf = busPlaceCtf;
	}
	
	/**
	 * 经营场所实地认证
	 * @return
	 */
	public String getBusPlaceCtf() {
		return this.busPlaceCtf;
	}
	/**
	 * 贷款�?
	 * @param loanCard
	 */
	public void setLoanCard(String loanCard) {
		this.loanCard = loanCard;
	}
	
	/**
	 * 贷款�?
	 * @return
	 */
	public String getLoanCard() {
		return this.loanCard;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}