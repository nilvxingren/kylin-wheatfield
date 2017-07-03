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

import com.rkylin.wheatfield.dao.AccountAgreementDao;
import com.rkylin.wheatfield.manager.AccountAgreementManager;
import com.rkylin.wheatfield.pojo.AccountAgreement;
import com.rkylin.wheatfield.pojo.AccountAgreementQuery;

@Component("accountAgreementManager")
public class AccountAgreementManagerImpl implements AccountAgreementManager {
	
	@Autowired
	@Qualifier("accountAgreementDao")
	private AccountAgreementDao accountAgreementDao;
	
	@Override
	public void saveAccountAgreement(AccountAgreement accountAgreement) {
			accountAgreementDao.insertSelective(accountAgreement);
	}
	
	@Override
	public AccountAgreement findAccountAgreementById(Long id) {
		return accountAgreementDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<AccountAgreement> queryList(AccountAgreementQuery query) {
		return accountAgreementDao.selectByExample(query);
	}
	
	@Override
	public void deleteAccountAgreementById(Long id) {
		accountAgreementDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteAccountAgreement(AccountAgreementQuery query) {
		accountAgreementDao.deleteByExample(query);
	}
	@Override
	public void updateAccountAgreement(AccountAgreement accountAgreement) {
			accountAgreementDao.updateByPrimaryKeySelective(accountAgreement);
	}
}

