/**
 * @File name : SemiAutomatization.java
 * @Package : com.rkylin.wheatfield.service
 * @Description : TODO(人工补账接口)
 * @Creator : liuhuan
 * @CreateTime : 2015年11月11日 下午1:08:02
 * @Version : 1.0
 */
package com.rkylin.wheatfield.service;

import java.util.List;
import java.util.Map;

import com.rkylin.wheatfield.model.CommonResponse;

public interface SemiAutomatizationService {
	public String ForAccount(List<Map<String,String>> paramsValueList);
	
	/**
	 * 账户资金操作
	 * Discription:
	 * @param user
	 * @return CommonResponse
	 * @author Achilles
	 * @since 2016年7月7日
	 */
	public CommonResponse operateFinAccount(com.rkylin.wheatfield.bean.User user);
}
