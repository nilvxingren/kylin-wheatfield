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

import com.rkylin.wheatfield.dao.SettleBalanceEntryDao;
import com.rkylin.wheatfield.manager.SettleBalanceEntryManager;
import com.rkylin.wheatfield.pojo.SettleBalanceEntry;
import com.rkylin.wheatfield.pojo.SettleBalanceEntryQuery;

@Component("settleBalanceEntryManager")
public class SettleBalanceEntryManagerImpl implements SettleBalanceEntryManager {
	
	@Autowired
	@Qualifier("settleBalanceEntryDao")
	private SettleBalanceEntryDao settleBalanceEntryDao;
	
	@Override
	public void saveSettleBalanceEntry(SettleBalanceEntry settleBalanceEntry) {
		if (settleBalanceEntry.getSettleId() == null) {
			settleBalanceEntryDao.insertSelective(settleBalanceEntry);
		} else {
			settleBalanceEntryDao.updateByPrimaryKeySelective(settleBalanceEntry);
		}
	}
	
	@Override
	public SettleBalanceEntry findSettleBalanceEntryById(Long id) {
		return settleBalanceEntryDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<SettleBalanceEntry> queryList(SettleBalanceEntryQuery query) {
		return settleBalanceEntryDao.selectByExample(query);
	}
	
	@Override
	public void deleteSettleBalanceEntryById(Long id) {
		settleBalanceEntryDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteSettleBalanceEntry(SettleBalanceEntryQuery query) {
		settleBalanceEntryDao.deleteByExample(query);
	}
}

