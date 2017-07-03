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

import com.rkylin.wheatfield.dao.CreditRepaymentDaysSummaryDao;
import com.rkylin.wheatfield.manager.CreditRepaymentDaysSummaryManager;
import com.rkylin.wheatfield.pojo.CreditRepaymentDaysSummary;
import com.rkylin.wheatfield.pojo.CreditRepaymentDaysSummaryQuery;

@Component("creditRepaymentDaysSummaryManager")
public class CreditRepaymentDaysSummaryManagerImpl implements CreditRepaymentDaysSummaryManager {
	
	@Autowired
	@Qualifier("creditRepaymentDaysSummaryDao")
	private CreditRepaymentDaysSummaryDao creditRepaymentDaysSummaryDao;
	
	@Override
	public void saveCreditRepaymentDaysSummary(CreditRepaymentDaysSummary creditRepaymentDaysSummary) {
		if (creditRepaymentDaysSummary.getUserId() == null) {
			creditRepaymentDaysSummaryDao.insertSelective(creditRepaymentDaysSummary);
		} else {
			creditRepaymentDaysSummaryDao.updateByPrimaryKeySelective(creditRepaymentDaysSummary);
		}
	}
	
	@Override
	public CreditRepaymentDaysSummary findCreditRepaymentDaysSummaryById(Long id) {
		return creditRepaymentDaysSummaryDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<CreditRepaymentDaysSummary> queryList(CreditRepaymentDaysSummaryQuery query) {
		return creditRepaymentDaysSummaryDao.selectByExample(query);
	}
	
	@Override
	public void deleteCreditRepaymentDaysSummaryById(Long id) {
		creditRepaymentDaysSummaryDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteCreditRepaymentDaysSummary(CreditRepaymentDaysSummaryQuery query) {
		creditRepaymentDaysSummaryDao.deleteByExample(query);
	}
}

