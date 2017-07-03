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

import com.rkylin.wheatfield.dao.CurrencyInfoDao;
import com.rkylin.wheatfield.manager.CurrencyInfoManager;
import com.rkylin.wheatfield.pojo.CurrencyInfo;
import com.rkylin.wheatfield.pojo.CurrencyInfoQuery;

@Component("currencyInfoManager")
public class CurrencyInfoManagerImpl implements CurrencyInfoManager {
	
	@Autowired
	@Qualifier("currencyInfoDao")
	private CurrencyInfoDao currencyInfoDao;
	
	@Override
	public void saveCurrencyInfo(CurrencyInfo currencyInfo) {
		if (currencyInfo.getCurrencyId() == null) {
			currencyInfoDao.insertSelective(currencyInfo);
		} else {
			currencyInfoDao.updateByPrimaryKeySelective(currencyInfo);
		}
	}
	
	@Override
	public CurrencyInfo findCurrencyInfoById(Long id) {
		return currencyInfoDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<CurrencyInfo> queryList(CurrencyInfoQuery query) {
		return currencyInfoDao.selectByExample(query);
	}
	
	@Override
	public void deleteCurrencyInfoById(Long id) {
		currencyInfoDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteCurrencyInfo(CurrencyInfoQuery query) {
		currencyInfoDao.deleteByExample(query);
	}
}

