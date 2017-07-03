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

import com.rkylin.wheatfield.dao.TransCodeDao;
import com.rkylin.wheatfield.manager.TransCodeManager;
import com.rkylin.wheatfield.pojo.TransCode;
import com.rkylin.wheatfield.pojo.TransCodeQuery;

@Component("transCodeManager")
public class TransCodeManagerImpl implements TransCodeManager {
	
	@Autowired
	@Qualifier("transCodeDao")
	private TransCodeDao transCodeDao;
	
	@Override
	public void saveTransCode(TransCode transCode) {
		if (transCode.getTransId() == null) {
			transCodeDao.insertSelective(transCode);
		} else {
			transCodeDao.updateByPrimaryKeySelective(transCode);
		}
	}
	
	@Override
	public TransCode findTransCodeById(Long id) {
		return transCodeDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<TransCode> queryList(TransCodeQuery query) {
		return transCodeDao.selectByExample(query);
	}
	
	@Override
	public void deleteTransCodeById(Long id) {
		transCodeDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteTransCode(TransCodeQuery query) {
		transCodeDao.deleteByExample(query);
	}
}

