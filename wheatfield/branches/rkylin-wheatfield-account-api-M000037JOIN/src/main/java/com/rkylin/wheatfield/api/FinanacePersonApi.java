package com.rkylin.wheatfield.api;

import com.rkylin.wheatfield.bean.OpenAccountPerson;
import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.model.FinPersonResponse;

public interface FinanacePersonApi {

    /**
     * 个人户开户
     * Discription:
     * @param openAccountPerson
     * @return CommonResponse
     * @author Achilles
     * @since 2016年12月26日
     */
    public CommonResponse accountoprPerRealNameOpenByDubbo(OpenAccountPerson openAccountPerson);
    
    /**
     * 个人户更新
     * Discription:
     * @param openAccountPerson
     * @return CommonResponse
     * @author Achilles
     * @since 2016年12月27日
     */
    public CommonResponse accountoprPerRealNameUpByDubbo(OpenAccountPerson openAccountPerson);
    
    /**
     * 个人户基本信息查询
     * Discription:
     * @param openAccountPerson
     * @return CommonResponse
     * @author Achilles
     * @since 2016年12月27日
     */
    public FinPersonResponse accountoprPerRealNameQueryBasicByDubbo(OpenAccountPerson openAccountPerson);
}
