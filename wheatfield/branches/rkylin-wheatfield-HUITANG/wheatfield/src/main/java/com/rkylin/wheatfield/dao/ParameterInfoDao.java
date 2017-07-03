/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao;

import java.util.List;

import com.rkylin.wheatfield.pojo.ParameterInfo;
import com.rkylin.wheatfield.pojo.ParameterInfoQuery;

public interface ParameterInfoDao {
	int countByExample(ParameterInfoQuery example);
	
	int deleteByExample(ParameterInfoQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(ParameterInfo record);
	
	int insertSelective(ParameterInfo record);
	
	List<ParameterInfo> selectByExample(ParameterInfoQuery example);
	
	ParameterInfo selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(ParameterInfo record);
	
	int updateByPrimaryKey(ParameterInfo record);
	
	List<ParameterInfo> selectAllowErrorCount(ParameterInfoQuery example);
}
