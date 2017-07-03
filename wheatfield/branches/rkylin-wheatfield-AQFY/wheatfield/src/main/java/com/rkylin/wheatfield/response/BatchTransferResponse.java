package com.rkylin.wheatfield.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class BatchTransferResponse  extends Response{
    
    @XStreamAlias("return_code")
    private String returnCode="1";
    @XStreamAlias("return_msg")
    private String returnMsg;
    
    public String getReturnCode() {
        return returnCode;
    }
    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }
    public String getReturnMsg() {
        return returnMsg;
    }
    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }
    
    
}
