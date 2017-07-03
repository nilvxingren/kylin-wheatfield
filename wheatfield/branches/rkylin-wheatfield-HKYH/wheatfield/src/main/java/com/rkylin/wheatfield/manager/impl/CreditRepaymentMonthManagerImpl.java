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

import com.rkylin.wheatfield.dao.CreditRepaymentMonthDao;
import com.rkylin.wheatfield.manager.CreditRepaymentMonthManager;
import com.rkylin.wheatfield.pojo.CreditRepaymentMonth;
import com.rkylin.wheatfield.pojo.CreditRepaymentMonthQuery;

@Component("creditRepaymentMonthManager")
public class CreditRepaymentMonthManagerImpl implements CreditRepaymentMonthManager {
	
	@Autowired
	@Qualifier("creditRepaymentMonthDao")
	private CreditRepaymentMonthDao creditRepaymentMonthDao;
	
	@Override
	public void saveCreditRepaymentMonth(CreditRepaymentMonth creditRepaymentMonth) {
		if (creditRepaymentMonth.getUserId() == null) {
			creditRepaymentMonthDao.insertSelective(creditRepaymentMonth);
		} else {
			creditRepaymentMonthDao.updateByPrimaryKeySelective(creditRepaymentMonth);
		}
	}
	
	@Override
	public CreditRepaymentMonth findCreditRepaymentMonthById(Long id) {
		return creditRepaymentMonthDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<CreditRepaymentMonth> queryList(CreditRepaymentMonthQuery query) {
		return creditRepaymentMonthDao.selectByExample(query);
	}
	
	@Override
	public void deleteCreditRepaymentMonthById(Long id) {
		creditRepaymentMonthDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteCreditRepaymentMonth(CreditRepaymentMonthQuery query) {
		creditRepaymentMonthDao.deleteByExample(query);
	}
}

