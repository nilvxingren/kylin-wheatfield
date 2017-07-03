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

import com.rkylin.wheatfield.dao.FinanaceEntryDao;
import com.rkylin.wheatfield.manager.FinanaceEntryManager;
import com.rkylin.wheatfield.pojo.FinanaceEntry;
import com.rkylin.wheatfield.pojo.FinanaceEntryQuery;

@Component("finanaceEntryManager")
public class FinanaceEntryManagerImpl implements FinanaceEntryManager {
	
	@Autowired
	@Qualifier("finanaceEntryDao")
	private FinanaceEntryDao finanaceEntryDao;
	
	@Override
	public int saveFinanaceEntry(FinanaceEntry finanaceEntry) {
//		if (finanaceEntry.getFinanaceEntryId() == null) {
//			return finanaceEntryDao.insertSelective(finanaceEntry);
//		} else {
//			return finanaceEntryDao.updateByPrimaryKeySelective(finanaceEntry);
//		}
		return finanaceEntryDao.insertSelective(finanaceEntry);
	}
	
	@Override
	public FinanaceEntry findFinanaceEntryById(Long id) {
		return finanaceEntryDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<FinanaceEntry> queryList(FinanaceEntryQuery query) {
		return finanaceEntryDao.selectByExample(query);
	}
	
	@Override
	public void deleteFinanaceEntryById(Long id) {
		finanaceEntryDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteFinanaceEntry(FinanaceEntryQuery query) {
		finanaceEntryDao.deleteByExample(query);
	}

	@Override
	public void saveFinanaceEntryList(List<FinanaceEntry> finanaceEntries) {
		finanaceEntryDao.insertSelectiveBatch(finanaceEntries);		
	}

	@Override
	public List<FinanaceEntry> queryListNew(FinanaceEntryQuery query) {
		return finanaceEntryDao.selectByFinAccountId(query);
	}
}

