package com.rkylin.wheatfield.bean;

import java.io.Serializable;
import java.util.List;

public class FinAccountInfoVo  implements Serializable{

    /**
     * Description:
     */
    private static final long serialVersionUID = 1L;
    /**
     * 机构号
     */
    private String rootInstCd;
    /**
     * userId用户ID
     */
    private List<String> userIdList;
    /**
     * 管理分组/产品码
     */
    private String groupManage;
    
    public String getRootInstCd() {
        return rootInstCd;
    }
    public void setRootInstCd(String rootInstCd) {
        this.rootInstCd = rootInstCd;
    }
    public List<String> getUserIdList() {
        return userIdList;
    }
    public void setUserIdList(List<String> userIdList) {
        this.userIdList = userIdList;
    }
    public String getGroupManage() {
        return groupManage;
    }
    public void setGroupManage(String groupManage) {
        this.groupManage = groupManage;
    }
    
    
    
}
