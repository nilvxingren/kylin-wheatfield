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

import com.rkylin.wheatfield.dao.ManageHistoryDao;
import com.rkylin.wheatfield.manager.ManageHistoryManager;
import com.rkylin.wheatfield.pojo.ManageHistory;
import com.rkylin.wheatfield.pojo.ManageHistoryQuery;

@Component("manageHistoryManager")
public class ManageHistoryManagerImpl implements ManageHistoryManager {
	
	@Autowired
	@Qualifier("manageHistoryDao")
	private ManageHistoryDao manageHistoryDao;
	
	@Override
	public void saveManageHistory(ManageHistory manageHistory) {
		if (manageHistory.getId() == null) {
			manageHistoryDao.insertSelective(manageHistory);
		} else {
			manageHistoryDao.updateByPrimaryKeySelective(manageHistory);
		}
	}
	
	@Override
	public ManageHistory findManageHistoryById(Long id) {
		return manageHistoryDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<ManageHistory> queryList(ManageHistoryQuery query) {
		return manageHistoryDao.selectByExample(query);
	}
	
	@Override
	public void deleteManageHistoryById(Long id) {
		manageHistoryDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteManageHistory(ManageHistoryQuery query) {
		manageHistoryDao.deleteByExample(query);
	}
}

