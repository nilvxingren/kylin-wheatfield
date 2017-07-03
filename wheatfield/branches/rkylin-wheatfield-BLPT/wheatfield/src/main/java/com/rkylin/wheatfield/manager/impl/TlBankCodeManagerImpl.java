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

import com.rkylin.wheatfield.dao.TlBankCodeDao;
import com.rkylin.wheatfield.manager.TlBankCodeManager;
import com.rkylin.wheatfield.pojo.TlBankCode;
import com.rkylin.wheatfield.pojo.TlBankCodeQuery;

@Component("tlBankCodeManager")
public class TlBankCodeManagerImpl implements TlBankCodeManager {
	
	@Autowired
	@Qualifier("tlBankCodeDao")
	private TlBankCodeDao tlBankCodeDao;
	
	@Override
	public void saveTlBankCode(TlBankCode tlBankCode) {
		if (tlBankCode.getBankId() == null) {
			tlBankCodeDao.insertSelective(tlBankCode);
		} else {
			tlBankCodeDao.updateByPrimaryKeySelective(tlBankCode);
		}
	}
	
	@Override
	public TlBankCode findTlBankCodeById(Long id) {
		return tlBankCodeDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<TlBankCode> queryList(TlBankCodeQuery query) {
		return tlBankCodeDao.selectByExample(query);
	}
	
	@Override
	public void deleteTlBankCodeById(Long id) {
		tlBankCodeDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteTlBankCode(TlBankCodeQuery query) {
		tlBankCodeDao.deleteByExample(query);
	}
}

