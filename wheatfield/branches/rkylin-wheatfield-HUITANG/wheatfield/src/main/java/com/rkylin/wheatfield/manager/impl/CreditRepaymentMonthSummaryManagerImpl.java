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

import com.rkylin.wheatfield.dao.CreditRepaymentMonthSummaryDao;
import com.rkylin.wheatfield.manager.CreditRepaymentMonthSummaryManager;
import com.rkylin.wheatfield.pojo.CreditRepaymentMonthSummary;
import com.rkylin.wheatfield.pojo.CreditRepaymentMonthSummaryQuery;

@Component("creditRepaymentMonthSummaryManager")
public class CreditRepaymentMonthSummaryManagerImpl implements CreditRepaymentMonthSummaryManager {
	
	@Autowired
	@Qualifier("creditRepaymentMonthSummaryDao")
	private CreditRepaymentMonthSummaryDao creditRepaymentMonthSummaryDao;
	
	@Override
	public void saveCreditRepaymentMonthSummary(CreditRepaymentMonthSummary creditRepaymentMonthSummary) {
		if (creditRepaymentMonthSummary.getUserId() == null) {
			creditRepaymentMonthSummaryDao.insertSelective(creditRepaymentMonthSummary);
		} else {
			creditRepaymentMonthSummaryDao.updateByPrimaryKeySelective(creditRepaymentMonthSummary);
		}
	}
	
	@Override
	public CreditRepaymentMonthSummary findCreditRepaymentMonthSummaryById(Long id) {
		return creditRepaymentMonthSummaryDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<CreditRepaymentMonthSummary> queryList(CreditRepaymentMonthSummaryQuery query) {
		return creditRepaymentMonthSummaryDao.selectByExample(query);
	}
	
	@Override
	public void deleteCreditRepaymentMonthSummaryById(Long id) {
		creditRepaymentMonthSummaryDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteCreditRepaymentMonthSummary(CreditRepaymentMonthSummaryQuery query) {
		creditRepaymentMonthSummaryDao.deleteByExample(query);
	}
}

