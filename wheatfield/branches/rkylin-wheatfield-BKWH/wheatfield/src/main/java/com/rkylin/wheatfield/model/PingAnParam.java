package com.rkylin.wheatfield.model;

public class PingAnParam {
    
    /**
     * 开户使用的机构号
     */
    private String openInst;
    /**
     * 开户使用的机构号
     */
    private String dealInst;
    /**
     * MD5加密key
     */
    private String md5key;
    /**
     * 主账户号
     */
    private String mainAccountNo;
    /**
     * 主账户名
     */
    private String mainAccountName;
    
    public String getDealInst() {
        return dealInst;
    }
    public void setDealInst(String dealInst) {
        this.dealInst = dealInst;
    }
    public String getOpenInst() {
        return openInst;
    }
    public void setOpenInst(String openInst) {
        this.openInst = openInst;
    }
    public String getMd5key() {
        return md5key;
    }
    public void setMd5key(String md5key) {
        this.md5key = md5key;
    }
    public String getMainAccountNo() {
        return mainAccountNo;
    }
    public void setMainAccountNo(String mainAccountNo) {
        this.mainAccountNo = mainAccountNo;
    }
    public String getMainAccountName() {
        return mainAccountName;
    }
    public void setMainAccountName(String mainAccountName) {
        this.mainAccountName = mainAccountName;
    }
    
    
}
