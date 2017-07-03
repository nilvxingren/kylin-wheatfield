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

import com.rkylin.wheatfield.dao.SettleTransTabDao;
import com.rkylin.wheatfield.manager.SettleTransTabManager;
import com.rkylin.wheatfield.pojo.SettleTransTab;
import com.rkylin.wheatfield.pojo.SettleTransTabQuery;

@Component("settleTransTabManager")
public class SettleTransTabManagerImpl implements SettleTransTabManager {
	
	@Autowired
	@Qualifier("settleTransTabDao")
	private SettleTransTabDao settleTransTabDao;
	
	@Override
	public void saveSettleTransTab(SettleTransTab settleTransTab) {
		if (settleTransTab.getTabId() == null) {
			settleTransTabDao.insertSelective(settleTransTab);
		} else {
			settleTransTabDao.updateByPrimaryKeySelective(settleTransTab);
		}
	}
	
	@Override
	public SettleTransTab findSettleTransTabById(Long id) {
		return settleTransTabDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<SettleTransTab> queryList(SettleTransTabQuery query) {
		return settleTransTabDao.selectByExample(query);
	}
	
	@Override
	public void deleteSettleTransTabById(Long id) {
		settleTransTabDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteSettleTransTab(SettleTransTabQuery query) {
		settleTransTabDao.deleteByExample(query);
	}
	@Override
	public void saveSettleTransTabs(List<SettleTransTab> settleTransTabs) {
		settleTransTabDao.insertSelectiveBatch(settleTransTabs);		
	}
}

