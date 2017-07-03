package com.rkylin.wheatfield.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("wheatfield_pingan_account_response")
public class PingAnOpenAccountResponse  extends Response{

    @XStreamAlias("is_success")
    private boolean isSuccess=true;
    
    private String code;
    // 错误信息
    private String msg;
    @XStreamAlias("subaccountno")
    private String subAccountNo;
    
    public String getSubAccountNo() {
        return subAccountNo;
    }
    public void setSubAccountNo(String subAccountNo) {
        this.subAccountNo = subAccountNo;
    }
    public boolean isSuccess() {
        return isSuccess;
    }
    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
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
    
}
