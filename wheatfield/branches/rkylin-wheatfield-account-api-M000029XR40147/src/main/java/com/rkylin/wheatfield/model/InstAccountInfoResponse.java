package com.rkylin.wheatfield.model;

import java.util.List;

import com.rkylin.wheatfield.pojo.FinanaceCompany;
import com.rkylin.wheatfield.pojo.FinanacePerson;

/**
 * 
 * Description: 机构用户信息返回实体
 * 
 * @author: sun
 * @CreateDate: 2017年2月16日
 * @version: V1.0
 */
public class InstAccountInfoResponse extends CommonResponse {

    private static final long serialVersionUID = -4661718956734099605L;

    // 机构个人用户列表
    private List<FinanacePerson> finanacePersonList;
    // 机构企业用户列表
    private List<FinanaceCompany> financeCompanyList;
    // 机构个人用户总数
    private Long personNo;
    // 机构企业用户总数
    private Long companyNo;
    // 用户类型：1、个人用户；2、企业用户
    private Integer userType;
    // 查询数据总量
    private Integer sumAmount;

    public Integer getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(Integer sumAmount) {
        this.sumAmount = sumAmount;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public List<FinanacePerson> getFinanacePersonList() {
        return finanacePersonList;
    }

    public void setFinanacePersonList(List<FinanacePerson> finanacePersonList) {
        this.finanacePersonList = finanacePersonList;
    }

    public List<FinanaceCompany> getFinanceCompanyList() {
        return financeCompanyList;
    }

    public void setFinanceCompanyList(List<FinanaceCompany> financeCompanyList) {
        this.financeCompanyList = financeCompanyList;
    }

    public Long getPersonNo() {
        return personNo;
    }

    public void setPersonNo(Long personNo) {
        this.personNo = personNo;
    }

    public Long getCompanyNo() {
        return companyNo;
    }

    public void setCompanyNo(Long companyNo) {
        this.companyNo = companyNo;
    }
}
