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

import com.rkylin.wheatfield.dao.FinanaceAccountAuthDao;
import com.rkylin.wheatfield.manager.FinanaceAccountAuthManager;
import com.rkylin.wheatfield.pojo.FinanaceAccountAuth;
import com.rkylin.wheatfield.pojo.FinanaceAccountAuthQuery;

@Component("finanaceAccountAuthManager")
public class FinanaceAccountAuthManagerImpl implements FinanaceAccountAuthManager {
	
	@Autowired
	@Qualifier("finanaceAccountAuthDao")
	private FinanaceAccountAuthDao finanaceAccountAuthDao;
	
	@Override
	public void saveFinanaceAccountAuth(FinanaceAccountAuth finanaceAccountAuth) {
		if (finanaceAccountAuth.getFinAntAuthId() == null) {
			finanaceAccountAuthDao.insertSelective(finanaceAccountAuth);
		} else {
			finanaceAccountAuthDao.updateByPrimaryKeySelective(finanaceAccountAuth);
		}
	}
	
	@Override
	public FinanaceAccountAuth findFinanaceAccountAuthById(Long finAntAuthId) {
		return finanaceAccountAuthDao.selectByPrimaryKey(finAntAuthId);
	}
	
	@Override
	public List<FinanaceAccountAuth> queryList(FinanaceAccountAuthQuery query) {
		return finanaceAccountAuthDao.selectByExample(query);
	}
	
	@Override
	public void deleteFinanaceAccountAuthById(Long finAntAuthId) {
		finanaceAccountAuthDao.deleteByPrimaryKey(finAntAuthId);
	}
	
	@Override
	public void deleteFinanaceAccountAuth(FinanaceAccountAuthQuery query) {
		finanaceAccountAuthDao.deleteByExample(query);
	}
}

