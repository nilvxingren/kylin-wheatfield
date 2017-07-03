package com.rkylin.wheatfield.bean;

import java.io.Serializable;
import java.util.Date;

public class TransOrderQuy implements Serializable{

    /**
     * 机构码
     * */
    private String merchantCode;
    /**
     * 账户状态
     */
    private Integer status;   
    private java.lang.String funcCode;
    /**
     * 创建开始时间
     */
    private Date createdTimeFrom;
    /**
     * 创建结束时间
     */
    private Date createdTimeTo;
    
    /**
     * 每页显示条数
     */
    private Integer pageSize;
    /**
     * 页码
     */
    private Integer pageNum;

    public java.lang.String getFuncCode() {
        return funcCode;
    }
    public void setFuncCode(java.lang.String funcCode) {
        this.funcCode = funcCode;
    }
    public String getMerchantCode() {
        return merchantCode;
    }
    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public Date getCreatedTimeFrom() {
        return createdTimeFrom;
    }
    public void setCreatedTimeFrom(Date createdTimeFrom) {
        this.createdTimeFrom = createdTimeFrom;
    }
    public Date getCreatedTimeTo() {
        return createdTimeTo;
    }
    public void setCreatedTimeTo(Date createdTimeTo) {
        this.createdTimeTo = createdTimeTo;
    }
    public Integer getPageSize() {
        return pageSize;
    }
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
    public Integer getPageNum() {
        return pageNum;
    }
    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }
    
    
}
