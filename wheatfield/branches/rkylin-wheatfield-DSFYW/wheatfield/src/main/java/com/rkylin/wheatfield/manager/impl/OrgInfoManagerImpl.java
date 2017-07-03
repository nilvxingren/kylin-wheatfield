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

import com.rkylin.wheatfield.dao.OrgInfoDao;
import com.rkylin.wheatfield.manager.OrgInfoManager;
import com.rkylin.wheatfield.pojo.OrgInfo;
import com.rkylin.wheatfield.pojo.OrgInfoQuery;

@Component("orgInfoManager")
public class OrgInfoManagerImpl implements OrgInfoManager {
	
	@Autowired
	@Qualifier("orgInfoDao")
	private OrgInfoDao orgInfoDao;
	
	@Override
	public void saveOrgInfo(OrgInfo orgInfo) {
		if (orgInfo.getOrgId() == null) {
			orgInfoDao.insertSelective(orgInfo);
		} else {
			orgInfoDao.updateByPrimaryKeySelective(orgInfo);
		}
	}
	
	@Override
	public OrgInfo findOrgInfoById(Long id) {
		return orgInfoDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<OrgInfo> queryList(OrgInfoQuery query) {
		return orgInfoDao.selectByExample(query);
	}
	
	@Override
	public void deleteOrgInfoById(Long id) {
		orgInfoDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteOrgInfo(OrgInfoQuery query) {
		orgInfoDao.deleteByExample(query);
	}
}

