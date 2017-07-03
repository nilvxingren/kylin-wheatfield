/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.rkylin.wheatfield.dao.ParameterInfoDao;
import com.rkylin.wheatfield.manager.ParameterInfoManager;
import com.rkylin.wheatfield.pojo.ParameterInfo;
import com.rkylin.wheatfield.pojo.ParameterInfoQuery;

@Component("parameterInfoManager")
public class ParameterInfoManagerImpl implements ParameterInfoManager {
	
	@Autowired
	@Qualifier("parameterInfoDao")
	private ParameterInfoDao parameterInfoDao;
	
	@Override
	public void saveParameterInfo(ParameterInfo parameterInfo) {
		if (parameterInfo.getParameterId() == null) {
			parameterInfoDao.insertSelective(parameterInfo);
		} else {
			parameterInfoDao.updateByPrimaryKeySelective(parameterInfo);
		}
	}
	
	@Override
	public ParameterInfo findParameterInfoById(Long id) {
		return parameterInfoDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<ParameterInfo> queryList(ParameterInfoQuery query) {
		return parameterInfoDao.selectByExample(query);
	}
	
	@Override
	public void deleteParameterInfoById(Long id) {
		parameterInfoDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteParameterInfo(ParameterInfoQuery query) {
		parameterInfoDao.deleteByExample(query);
	}

	@Override
	public List<ParameterInfo> queryAllowErrorCountList(ParameterInfoQuery query) {
		return parameterInfoDao.selectAllowErrorCount(query);
	}
}

