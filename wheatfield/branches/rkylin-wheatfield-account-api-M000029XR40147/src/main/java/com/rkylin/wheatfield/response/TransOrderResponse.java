package com.rkylin.wheatfield.response;

import java.util.List;

import com.rkylin.wheatfield.pojo.TransOrderInfoNew;

/**
 * @author  likun
 * @version 创建时间：2015-4-23 下午4:42:54
 * @updator liuhuan 
 * @updatetime 2016-12-9 11:18:20
 * 类说明              处理订单信息
 */
public class TransOrderResponse extends Response {
    private static final long serialVersionUID = 1L;
	private List<TransOrderInfoNew> transOrderInfoNews;
	// 错误代码
    //@XStreamAlias("code")
    private String code;
    // 错误信息
    //@XStreamAlias("msg")
    private String msg;
    //返回结果
    //@XStreamAlias("is_success")
    private boolean isSuccess;
    private int totalCount;

	public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public List<TransOrderInfoNew> getTransOrderInfoNews() {
		return transOrderInfoNews;
	}

	public void setTransOrderInfoNews(List<TransOrderInfoNew> transOrderInfoNews) {
		this.transOrderInfoNews = transOrderInfoNews;
	}
}

