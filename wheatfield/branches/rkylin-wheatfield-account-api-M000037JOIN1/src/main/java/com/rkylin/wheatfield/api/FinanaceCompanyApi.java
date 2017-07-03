package com.rkylin.wheatfield.api;

import com.rkylin.wheatfield.bean.OpenAccountCompany;
import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.model.FinCompanyResponse;

public interface FinanaceCompanyApi {

    /**
     * 企业开户
     * Discription:
     * @param openAccountPersonCompany
     * @return CommonResponse
     * @author Achilles
     * @since 2016年12月29日
     */
    public CommonResponse accountoprComRealNameOpenByDubbo(OpenAccountCompany openAccountCompany);
    
    /**
     * 企业户修改
     * Discription:
     * @param openAccountPersonCompany
     * @return CommonResponse
     * @author Achilles
     * @since 2016年12月29日
     */
    public CommonResponse accountoprComRealNameUpByDubbo(OpenAccountCompany openAccountCompany);
    
    /**
     * 企业户基本信息查询
     * Discription:
     * @param openAccountPersonCompany
     * @return CommonResponse
     * @author Achilles
     * @since 2016年12月29日
     */
    public FinCompanyResponse accountoprComRealNameQueryBasicByDubbo(OpenAccountCompany openAccountCompany);
}
