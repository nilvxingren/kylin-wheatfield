package com.rkylin.wheatfield.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单查询入参
 * 
 * @author sun
 *
 */
public class TransOrderQuery implements Serializable {

    private static final long serialVersionUID = 8339808523194138175L;
    // 机构号
    private String merchantCode;
    // 订单状态
    private Integer status;
    // 交易金额上限
    private Long amountUpper;
    // 交易金额下限
    private Long amountLower;
    // 交易渠道
    private String payChannelId;
    // 交易类型
    private String busiTypeId;
    // 创建时间结束时间
    private Date createTimeUpper;
    // 创建时间开始时间
    private Date createTimeLower;
    // 用户id
    private String userId;

    // 分页大小
    private Integer pageSize;
    // 页码
    private Integer pageNum;
    // 起始行
    private int startRow;

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

    public Long getAmountUpper() {
        return amountUpper;
    }

    public void setAmountUpper(Long amountUpper) {
        this.amountUpper = amountUpper;
    }

    public Long getAmountLower() {
        return amountLower;
    }

    public void setAmountLower(Long amountLower) {
        this.amountLower = amountLower;
    }

    public String getPayChannelId() {
        return payChannelId;
    }

    public void setPayChannelId(String payChannelId) {
        this.payChannelId = payChannelId;
    }

    public Date getCreateTimeUpper() {
        return createTimeUpper;
    }

    public void setCreateTimeUpper(Date createTimeUpper) {
        this.createTimeUpper = createTimeUpper;
    }

    public Date getCreateTimeLower() {
        return createTimeLower;
    }

    public void setCreateTimeLower(Date createTimeLower) {
        this.createTimeLower = createTimeLower;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBusiTypeId() {
        return busiTypeId;
    }

    public void setBusiTypeId(String busiTypeId) {
        this.busiTypeId = busiTypeId;
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

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }
}
