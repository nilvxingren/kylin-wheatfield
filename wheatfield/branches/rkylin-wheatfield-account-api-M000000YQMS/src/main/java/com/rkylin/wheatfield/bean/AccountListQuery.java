package com.rkylin.wheatfield.bean;

import java.io.Serializable;

/**
 * 
 * Description: 用户信息查询请求实体
 * 
 * @author: sun
 * @CreateDate: 2017年2月16日
 * @version: V1.0
 */
public class AccountListQuery implements Serializable {

    private static final long serialVersionUID = -4705206857914564010L;

    // 机构号
    private String rootInstCd;
    // 用户id
    private String userId;
    // 用户中文姓名
    private String userChName;
    // 账户状态
    private Integer statusId;
    // 用户类型：1、个人用户；2、企业用户
    private Integer userType;

    // 分页大小
    private Integer pageSize;
    // 页码
    private Integer pageNum;
    // 起始行
    private int startRow;

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

    public String getRootInstCd() {
        return rootInstCd;
    }

    public void setRootInstCd(String rootInstCd) {
        this.rootInstCd = rootInstCd;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserChName() {
        return userChName;
    }

    public void setUserChName(String userChName) {
        this.userChName = userChName;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }
}
