/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.rkylin.wheatfield.dao.CorporatAccountInfoDao;
import com.rkylin.wheatfield.manager.CorporatAccountInfoManager;
import com.rkylin.wheatfield.pojo.CorporatAccountInfo;
import com.rkylin.wheatfield.pojo.CorporatAccountInfoQuery;
import com.rkylin.wheatfield.pojo.CorporatAccountInfoScopeQuery;

@Component("corporatAccountInfoManager")
public class CorporatAccountInfoManagerImpl implements CorporatAccountInfoManager {
	
	@Autowired
	@Qualifier("corporatAccountInfoDao")
	private CorporatAccountInfoDao corporatAccountInfoDao;
	
	@Override
	public void saveCorporatAccountInfo(CorporatAccountInfo corporatAccountInfo) {
		if (corporatAccountInfo.getCorporateAccountId() == null) {
			corporatAccountInfoDao.insertSelective(corporatAccountInfo);
		} else {
			corporatAccountInfoDao.updateByPrimaryKeySelective(corporatAccountInfo);
		}
	}
	
	@Override
	public CorporatAccountInfo findCorporatAccountInfoById(Long id) {
		return corporatAccountInfoDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<CorporatAccountInfo> queryList(CorporatAccountInfoQuery query) {
		return corporatAccountInfoDao.selectByExample(query);
	}
	
	@Override
	public void deleteCorporatAccountInfoById(Long id) {
		corporatAccountInfoDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteCorporatAccountInfo(CorporatAccountInfoQuery query) {
		corporatAccountInfoDao.deleteByExample(query);
	}

	@Override
	public int insertByList(List<CorporatAccountInfo> corporatAccountInfoList) {
		return corporatAccountInfoDao.insertByList(corporatAccountInfoList);
	}

	@Override
	public List<CorporatAccountInfo> queryListScope(CorporatAccountInfoScopeQuery query) {
		return corporatAccountInfoDao.selectByScope(query);
	}

	@Override
	public int updateCorporatAccountInfo(CorporatAccountInfo corporatAccountInfo) {
		return corporatAccountInfoDao.updateById(corporatAccountInfo);
	}
}

