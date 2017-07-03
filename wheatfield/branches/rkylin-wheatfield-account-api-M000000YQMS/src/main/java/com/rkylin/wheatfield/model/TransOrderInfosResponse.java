package com.rkylin.wheatfield.model;

import java.math.BigDecimal;
import java.util.List;

import com.rkylin.wheatfield.pojo.TransOrderInfo;

/**
 * 订单数据封装
 * 
 * @author Achilles
 *
 */
public class TransOrderInfosResponse extends CommonResponse {

    private static final long serialVersionUID = -2154362966251058678L;

    private List<TransOrderInfo> transOrderInfoList;

    // 数据总条数
    private Integer sumAmount;

    // 总金额
    private BigDecimal totalAmount;

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(Integer sumAmount) {
        this.sumAmount = sumAmount;
    }

    public List<TransOrderInfo> getTransOrderInfoList() {
        return transOrderInfoList;
    }

    public void setTransOrderInfoList(List<TransOrderInfo> transOrderInfoList) {
        this.transOrderInfoList = transOrderInfoList;
    }

}
