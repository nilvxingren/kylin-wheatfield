package com.rkylin.wheatfield.bean;

import java.io.Serializable;
import java.util.Date;

public class FinAccAndBalanceQuery implements Serializable{

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
     * 创建开始时间
     */
    private Date createdTimeFrom;
    /**
     * 创建结束时间
     */
    private Date createdTimeTo;
    /**
     * 账户状态
     */
    private Integer statusId;
    /**
     * 每页显示条数
     */
    private Integer pageSize;
    /**
     * 页码
     */
    private Integer pageNum;
    
    /**
     * 文件名
     */
    private String fileName;
    
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getInstCode() {
        return instCode;
    }
    public void setInstCode(String instCode) {
        this.instCode = instCode;
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
    public Integer getStatusId() {
        return statusId;
    }
    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
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
