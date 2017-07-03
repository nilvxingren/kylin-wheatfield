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

import com.rkylin.wheatfield.dao.OpenBankCodeDao;
import com.rkylin.wheatfield.manager.OpenBankCodeManager;
import com.rkylin.wheatfield.pojo.OpenBankCode;
import com.rkylin.wheatfield.pojo.OpenBankCodeQuery;

@Component("openBankCodeManager")
public class OpenBankCodeManagerImpl implements OpenBankCodeManager {
	
	@Autowired
	@Qualifier("openBankCodeDao")
	private OpenBankCodeDao openBankCodeDao;
	
	@Override
	public void saveOpenBankCode(OpenBankCode openBankCode) {
//		if (openBankCode.() == null) {
//			openBankCodeDao.insertSelective(openBankCode);
//		} else {
//			openBankCodeDao.updateByPrimaryKeySelective(openBankCode);
//		}
	}
	
	@Override
	public OpenBankCode findOpenBankCodeById(Long id) {
		return openBankCodeDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<OpenBankCode> queryList(OpenBankCodeQuery query) {
		return openBankCodeDao.selectByExample(query);
	}
	@Override
	public List<OpenBankCode> queryListByCode(OpenBankCodeQuery query) {
		return openBankCodeDao.selectByCode(query);
	}
	@Override
	public void deleteOpenBankCodeById(Long id) {
		openBankCodeDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteOpenBankCode(OpenBankCodeQuery query) {
		openBankCodeDao.deleteByExample(query);
	}
}

