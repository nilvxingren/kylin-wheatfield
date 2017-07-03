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

import com.rkylin.wheatfield.dao.FuncCodeDao;
import com.rkylin.wheatfield.manager.FuncCodeManager;
import com.rkylin.wheatfield.pojo.FuncCode;
import com.rkylin.wheatfield.pojo.FuncCodeQuery;

@Component("funcCodeManager")
public class FuncCodeManagerImpl implements FuncCodeManager {
	
	@Autowired
	@Qualifier("funcCodeDao")
	private FuncCodeDao funcCodeDao;
	
	@Override
	public void saveFuncCode(FuncCode funcCode) {
		if (funcCode.getFuncId() == null) {
			funcCodeDao.insertSelective(funcCode);
		} else {
			funcCodeDao.updateByPrimaryKeySelective(funcCode);
		}
	}
	
	@Override
	public FuncCode findFuncCodeById(Long id) {
		return funcCodeDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<FuncCode> queryList(FuncCodeQuery query) {
		return funcCodeDao.selectByExample(query);
	}
	
	@Override
	public void deleteFuncCodeById(Long id) {
		funcCodeDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteFuncCode(FuncCodeQuery query) {
		funcCodeDao.deleteByExample(query);
	}
}

