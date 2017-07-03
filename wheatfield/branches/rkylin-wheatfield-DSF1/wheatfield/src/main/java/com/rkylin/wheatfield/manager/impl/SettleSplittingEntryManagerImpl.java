/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.rkylin.wheatfield.dao.SettleSplittingEntryDao;
import com.rkylin.wheatfield.manager.SettleSplittingEntryManager;
import com.rkylin.wheatfield.pojo.SettleSplittingEntry;
import com.rkylin.wheatfield.pojo.SettleSplittingEntryQuery;

@Component("settleSplittingEntryManager")
public class SettleSplittingEntryManagerImpl implements SettleSplittingEntryManager {
	
	@Autowired
	@Qualifier("settleSplittingEntryDao")
	private SettleSplittingEntryDao settleSplittingEntryDao;
	
	@Override
	public void saveSettleSplittingEntry(SettleSplittingEntry settleSplittingEntry) {
		settleSplittingEntryDao.insertSelective(settleSplittingEntry);
	}

	@Override
	public void updateSettleSplittingEntry(SettleSplittingEntry settleSplittingEntry) {
		settleSplittingEntryDao.updateByPrimaryKeySelective(settleSplittingEntry);
	}
	
	@Override
	public SettleSplittingEntry findSettleSplittingEntryById(Long id) {
		return settleSplittingEntryDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<SettleSplittingEntry> queryList(SettleSplittingEntryQuery query) {
		return settleSplittingEntryDao.selectByExample(query);
	}
	
	@Override
	public void deleteSettleSplittingEntryById(Long id) {
		settleSplittingEntryDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteSettleSplittingEntry(SettleSplittingEntryQuery query) {
		settleSplittingEntryDao.deleteByExample(query);
	}
	
	@Override
	public int batchUpdateByOrderNo(List<?> list) {
		return settleSplittingEntryDao.batchUpdate(list);
	}
}

