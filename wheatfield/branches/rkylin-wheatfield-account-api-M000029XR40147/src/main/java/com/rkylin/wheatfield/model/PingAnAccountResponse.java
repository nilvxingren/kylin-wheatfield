package com.rkylin.wheatfield.model;

import java.util.List;

public class PingAnAccountResponse  extends CommonResponse{

    private List<com.rkylin.wheatfield.bean.User> userList;

    public List<com.rkylin.wheatfield.bean.User> getUserList() {
        return userList;
    }

    public void setUserList(List<com.rkylin.wheatfield.bean.User> userList) {
        this.userList = userList;
    }
    
    
}
