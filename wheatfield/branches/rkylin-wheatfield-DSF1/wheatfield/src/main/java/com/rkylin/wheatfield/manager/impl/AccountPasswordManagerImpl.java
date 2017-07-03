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

import com.rkylin.wheatfield.dao.AccountPasswordDao;
import com.rkylin.wheatfield.manager.AccountPasswordManager;
import com.rkylin.wheatfield.pojo.AccountPassword;
import com.rkylin.wheatfield.pojo.AccountPasswordQuery;

@Component("accountPasswordManager")
public class AccountPasswordManagerImpl implements AccountPasswordManager {
	
	@Autowired
	@Qualifier("accountPasswordDao")
	private AccountPasswordDao accountPasswordDao;
	
	@Override
	public void saveAccountPassword(AccountPassword accountPassword) {
		if (accountPassword.getAcctPawdId() == null) {
			accountPasswordDao.insertSelective(accountPassword);
		} else {
			accountPasswordDao.updateByPrimaryKeySelective(accountPassword);
		}
	}
	
	@Override
	public AccountPassword findAccountPasswordById(Long id) {
		return accountPasswordDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<AccountPassword> queryList(AccountPasswordQuery query) {
		return accountPasswordDao.selectByExample(query);
	}
	
	@Override
	public void deleteAccountPasswordById(Long id) {
		accountPasswordDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteAccountPassword(AccountPasswordQuery query) {
		accountPasswordDao.deleteByExample(query);
	}
}

