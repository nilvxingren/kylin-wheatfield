package com.rkylin.wheatfield.bean;

import java.io.Serializable;
import java.util.Date;

import com.rkylin.wheatfield.pojo.Balance;

public class FinAccountAndBalance implements Serializable{

    /**
     * 机构码
     * */
    private String instCode;
    /**
     * 用户ID
     * */
    private String userId;
    /**
     * 产品号
     * */
    private String productId;
    /**
     * 账户Id
     */
    private java.lang.String finAccountId;    
    /**
     * 账户状态
     */
    private Integer statusId;
    /**
     * 账户属性
     */
    private Integer accountProperty;
    /**
     * 创建时间
     */
    private Date createdTime;
    private java.lang.String finAccountTypeId;
    private java.lang.String finAccountName;
    /**
     * 对应产品分组名
     */
    private java.lang.String productName;
    private java.lang.String accountCode;
    private java.lang.String referUserId;    
    
    private Balance balance;
    
    public Balance getBalance() {
        return balance;
    }
    public void setBalance(Balance balance) {
        this.balance = balance;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getProductId() {
        return productId;
    }
    public void setProductId(String productId) {
        this.productId = productId;
    }
    public java.lang.String getFinAccountId() {
        return finAccountId;
    }
    public void setFinAccountId(java.lang.String finAccountId) {
        this.finAccountId = finAccountId;
    }
    public Integer getStatusId() {
        return statusId;
    }
    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }
    public Integer getAccountProperty() {
        return accountProperty;
    }
    public void setAccountProperty(Integer accountProperty) {
        this.accountProperty = accountProperty;
    }
    public Date getCreatedTime() {
        return createdTime;
    }
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
    public java.lang.String getFinAccountTypeId() {
        return finAccountTypeId;
    }
    public void setFinAccountTypeId(java.lang.String finAccountTypeId) {
        this.finAccountTypeId = finAccountTypeId;
    }
    public java.lang.String getFinAccountName() {
        return finAccountName;
    }
    public void setFinAccountName(java.lang.String finAccountName) {
        this.finAccountName = finAccountName;
    }
    public java.lang.String getAccountCode() {
        return accountCode;
    }
    public void setAccountCode(java.lang.String accountCode) {
        this.accountCode = accountCode;
    }
    public java.lang.String getReferUserId() {
        return referUserId;
    }
    public void setReferUserId(java.lang.String referUserId) {
        this.referUserId = referUserId;
    }
    public java.lang.String getProductName() {
        return productName;
    }
    public void setProductName(java.lang.String productName) {
        this.productName = productName;
    }
    public String getInstCode() {
        return instCode;
    }
    public void setInstCode(String instCode) {
        this.instCode = instCode;
    }
    
}
