package com.rkylin.wheatfield.api;

import com.rkylin.wheatfield.model.AccountInfoQueryResponse;
import com.rkylin.wheatfield.pojo.AccountInfor;

public interface AccountInfoApi {

    public AccountInfoQueryResponse accountInfoQueryByDubbo(AccountInfor accountInfor);
}
