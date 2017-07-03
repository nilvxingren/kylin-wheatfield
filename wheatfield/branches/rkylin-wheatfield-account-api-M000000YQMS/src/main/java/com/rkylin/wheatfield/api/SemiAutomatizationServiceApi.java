/**
 * @File name : SemiAutomatizationServiceApi.java
 * @Package : com.rkylin.wheatfield.api
 * @Description : TODO(补账Dubbo接口)
 * @Creator : liuhuan
 * @CreateTime : 2015年12月3日 上午11:01:41
 * @Version : 1.0
 */
package com.rkylin.wheatfield.api;

import java.util.List;
import java.util.Map;

import com.rkylin.wheatfield.model.CommonResponse;

public interface SemiAutomatizationServiceApi {
    public String ForAccount(List<Map<String, String>> paramsValueList);

    /**
     * 账户资金操作
     * Discription:
     * @param paramsValueList
     * @return String
     * @author Achilles
     * @since 2016年6月23日
     */
    public CommonResponse operateFinAccount(com.rkylin.wheatfield.bean.User user);
    
    /**
     * 批量账户资金操作
     */
    public CommonResponse operateFinAccounts(List<com.rkylin.wheatfield.bean.User> userList);
    
    /**
     * 调账
     * Discription:
     * @param user
     * @return CommonResponse
     * @author Achilles
     * @since 2017年2月16日
     */
    public CommonResponse operateFinanaceAccount(com.rkylin.wheatfield.bean.TransOrderInf transOrderInf);
}
