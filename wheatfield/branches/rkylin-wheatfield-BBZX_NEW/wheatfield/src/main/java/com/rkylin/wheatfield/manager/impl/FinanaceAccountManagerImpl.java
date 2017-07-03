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

import com.rkylin.wheatfield.dao.FinanaceAccountDao;
import com.rkylin.wheatfield.manager.FinanaceAccountManager;
import com.rkylin.wheatfield.pojo.FinanaceAccount;
import com.rkylin.wheatfield.pojo.FinanaceAccountQuery;

@Component("finanaceAccountManager")
public class FinanaceAccountManagerImpl implements FinanaceAccountManager {
	
	@Autowired
	@Qualifier("finanaceAccountDao")
	private FinanaceAccountDao finanaceAccountDao;
	
	@Override
	public void saveFinanaceAccount(FinanaceAccount finanaceAccount) {
//		if (finanaceAccount.getFinAccountId() == null) {
			finanaceAccountDao.insertSelective(finanaceAccount);
//		} else {
//			finanaceAccountDao.updateByPrimaryKeySelective(finanaceAccount);
//		}
	}
	
	@Override
	public FinanaceAccount findFinanaceAccountById(Long id) {
		return finanaceAccountDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<FinanaceAccount> queryList(FinanaceAccountQuery query) {
		return finanaceAccountDao.selectByExample(query);
	}
	
	@Override
	public List<FinanaceAccount> selectByFinAccountId(String[] finAccountIds) {
		return finanaceAccountDao.selectByFinAccountId(finAccountIds);
	}
	
	@Override
	public void deleteFinanaceAccountById(Long id) {
		finanaceAccountDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteFinanaceAccount(FinanaceAccountQuery query) {
		finanaceAccountDao.deleteByExample(query);
	}
	@Override
	public void updateFinanaceAccount(FinanaceAccount finanaceAccount) {
//		if (finanaceAccount.getFinAccountId() == null) {
//			finanaceAccountDao.insertSelective(finanaceAccount);
//		} else {
			finanaceAccountDao.updateByPrimaryKeySelective(finanaceAccount);
//		}
	}
}

