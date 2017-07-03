/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager;

import java.util.List;

import com.rkylin.wheatfield.pojo.ParameterInfo;
import com.rkylin.wheatfield.pojo.ParameterInfoQuery;

public interface ParameterInfoManager {
	void saveParameterInfo(ParameterInfo parameterInfo);

	ParameterInfo findParameterInfoById(Long id);
	
	List<ParameterInfo> queryList(ParameterInfoQuery query);
	
	void deleteParameterInfoById(Long id);
	
	void deleteParameterInfo(ParameterInfoQuery query);

	List<ParameterInfo> queryAllowErrorCountList(ParameterInfoQuery query);
	
	void updateParameterInfoByCode(ParameterInfo parameterInfo);
	
	public ParameterInfo getParameterInfo(ParameterInfoQuery query);
	
	public String getParameterValueByParameterCode(String parameterCode);
	
	public List<ParameterInfo> getParameterInfoByParamCode(String parameterCode);
}
