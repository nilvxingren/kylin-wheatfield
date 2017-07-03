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

import com.rkylin.wheatfield.dao.CreditInfoDao;
import com.rkylin.wheatfield.manager.CreditInfoManager;
import com.rkylin.wheatfield.pojo.CreditInfo;
import com.rkylin.wheatfield.pojo.CreditInfoQuery;

@Component("creditInfoManager")
public class CreditInfoManagerImpl implements CreditInfoManager {
	
	@Autowired
	@Qualifier("creditInfoDao")
	private CreditInfoDao creditInfoDao;
	
	@Override
	public void saveCreditInfo(CreditInfo creditInfo) {
		if (creditInfo.getId() == null) {
			creditInfoDao.insertSelective(creditInfo);
		} else {
			creditInfoDao.updateByPrimaryKeySelective(creditInfo);
		}
	}
	
	@Override
	public CreditInfo findCreditInfoById(Long id) {
		return creditInfoDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<CreditInfo> queryList(CreditInfoQuery query) {
		return creditInfoDao.selectByExample(query);
	}
	
	@Override
	public void deleteCreditInfoById(Long id) {
		creditInfoDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteCreditInfo(CreditInfoQuery query) {
		creditInfoDao.deleteByExample(query);
	}
}

