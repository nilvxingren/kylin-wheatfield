package com.rkylin.wheatfield.bean;

public class BatchTransfer  implements java.io.Serializable{

    private java.lang.String merchantCode;
    private java.lang.String orderPackageNo;
    private java.lang.String userIpAddress;
    private java.util.List<com.rkylin.wheatfield.bean.TransferInfo> outTransferInfoList;
    private java.util.List<com.rkylin.wheatfield.bean.TransferInfo> intoTransferInfoList;
    
    public java.lang.String getMerchantCode() {
        return merchantCode;
    }
    public void setMerchantCode(java.lang.String merchantCode) {
        this.merchantCode = merchantCode;
    }
    public java.lang.String getOrderPackageNo() {
        return orderPackageNo;
    }
    public void setOrderPackageNo(java.lang.String orderPackageNo) {
        this.orderPackageNo = orderPackageNo;
    }
    public java.lang.String getUserIpAddress() {
        return userIpAddress;
    }
    public void setUserIpAddress(java.lang.String userIpAddress) {
        this.userIpAddress = userIpAddress;
    }
    public java.util.List<com.rkylin.wheatfield.bean.TransferInfo> getOutTransferInfoList() {
        return outTransferInfoList;
    }
    public void setOutTransferInfoList(java.util.List<com.rkylin.wheatfield.bean.TransferInfo> outTransferInfoList) {
        this.outTransferInfoList = outTransferInfoList;
    }
    public java.util.List<com.rkylin.wheatfield.bean.TransferInfo> getIntoTransferInfoList() {
        return intoTransferInfoList;
    }
    public void setIntoTransferInfoList(java.util.List<com.rkylin.wheatfield.bean.TransferInfo> intoTransferInfoList) {
        this.intoTransferInfoList = intoTransferInfoList;
    }
    
    
}
