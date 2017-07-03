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

import com.rkylin.wheatfield.dao.FinanaceEntryHistoryDao;
import com.rkylin.wheatfield.manager.FinanaceEntryHistoryManager;
import com.rkylin.wheatfield.pojo.FinanaceEntryHistory;
import com.rkylin.wheatfield.pojo.FinanaceEntryHistoryQuery;

@Component("finanaceEntryHistoryManager")
public class FinanaceEntryHistoryManagerImpl implements FinanaceEntryHistoryManager {
	
	@Autowired
	@Qualifier("finanaceEntryHistoryDao")
	private FinanaceEntryHistoryDao finanaceEntryHistoryDao;
	
	@Override
	public void saveFinanaceEntryHistory(FinanaceEntryHistory finanaceEntryHistory) {
//		if (finanaceEntryHistory.getId() == null) {
//			finanaceEntryHistoryDao.insertSelective(finanaceEntryHistory);
//		} else {
//			finanaceEntryHistoryDao.updateByPrimaryKeySelective(finanaceEntryHistory);
//		}
		finanaceEntryHistoryDao.insertSelective(finanaceEntryHistory);
	}
	
	@Override
	public FinanaceEntryHistory findFinanaceEntryHistoryById(Long id) {
		return finanaceEntryHistoryDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<FinanaceEntryHistory> queryList(FinanaceEntryHistoryQuery query) {
		return finanaceEntryHistoryDao.selectByExample(query);
	}
	
	@Override
	public void deleteFinanaceEntryHistoryById(Long id) {
		finanaceEntryHistoryDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteFinanaceEntryHistory(FinanaceEntryHistoryQuery query) {
		finanaceEntryHistoryDao.deleteByExample(query);
	}
}

