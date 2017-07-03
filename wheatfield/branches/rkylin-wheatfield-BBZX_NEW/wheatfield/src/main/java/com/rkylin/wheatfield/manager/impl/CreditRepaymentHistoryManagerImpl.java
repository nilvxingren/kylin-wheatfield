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

import com.rkylin.wheatfield.dao.CreditRepaymentHistoryDao;
import com.rkylin.wheatfield.manager.CreditRepaymentHistoryManager;
import com.rkylin.wheatfield.pojo.CreditRepaymentHistory;
import com.rkylin.wheatfield.pojo.CreditRepaymentHistoryQuery;

@Component("creditRepaymentHistoryManager")
public class CreditRepaymentHistoryManagerImpl implements CreditRepaymentHistoryManager {
	
	@Autowired
	@Qualifier("creditRepaymentHistoryDao")
	private CreditRepaymentHistoryDao creditRepaymentHistoryDao;
	
	@Override
	public void saveCreditRepaymentHistory(CreditRepaymentHistory creditRepaymentHistory) {
		if (creditRepaymentHistory.getRepayId() == null) {
			creditRepaymentHistoryDao.insertSelective(creditRepaymentHistory);
		} else {
			creditRepaymentHistoryDao.updateByPrimaryKeySelective(creditRepaymentHistory);
		}
	}
	
	@Override
	public CreditRepaymentHistory findCreditRepaymentHistoryById(Long id) {
		return creditRepaymentHistoryDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<CreditRepaymentHistory> queryList(CreditRepaymentHistoryQuery query) {
		return creditRepaymentHistoryDao.selectByExample(query);
	}
	
	@Override
	public void deleteCreditRepaymentHistoryById(Long id) {
		creditRepaymentHistoryDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteCreditRepaymentHistory(CreditRepaymentHistoryQuery query) {
		creditRepaymentHistoryDao.deleteByExample(query);
	}
}

