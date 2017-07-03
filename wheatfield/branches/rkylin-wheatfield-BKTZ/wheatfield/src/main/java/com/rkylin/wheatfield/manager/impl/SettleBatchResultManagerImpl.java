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

import com.rkylin.wheatfield.dao.SettleBatchResultDao;
import com.rkylin.wheatfield.manager.SettleBatchResultManager;
import com.rkylin.wheatfield.pojo.SettleBatchResult;
import com.rkylin.wheatfield.pojo.SettleBatchResultQuery;

@Component("settleBatchResultManager")
public class SettleBatchResultManagerImpl implements SettleBatchResultManager {
	
	@Autowired
	@Qualifier("settleBatchResultDao")
	private SettleBatchResultDao settleBatchResultDao;
	
	@Override
	public void saveSettleBatchResult(SettleBatchResult settleBatchResult) {
		if (settleBatchResult.getResultId() == null) {
			settleBatchResultDao.insertSelective(settleBatchResult);
		} else {
			settleBatchResultDao.updateByPrimaryKeySelective(settleBatchResult);
		}
	}
	
	@Override
	public SettleBatchResult findSettleBatchResultById(Long id) {
		return settleBatchResultDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<SettleBatchResult> queryList(SettleBatchResultQuery query) {
		return settleBatchResultDao.selectByExample(query);
	}
	
	@Override
	public void deleteSettleBatchResultById(Long id) {
		settleBatchResultDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteSettleBatchResult(SettleBatchResultQuery query) {
		settleBatchResultDao.deleteByExample(query);
	}
}

