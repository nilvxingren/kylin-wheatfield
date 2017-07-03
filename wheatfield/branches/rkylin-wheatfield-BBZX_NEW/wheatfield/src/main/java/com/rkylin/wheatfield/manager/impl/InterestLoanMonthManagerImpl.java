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

import com.rkylin.wheatfield.dao.InterestLoanMonthDao;
import com.rkylin.wheatfield.manager.InterestLoanMonthManager;
import com.rkylin.wheatfield.pojo.InterestLoanMonth;
import com.rkylin.wheatfield.pojo.InterestLoanMonthQuery;


@Component("interestLoanMonthManager")
public class InterestLoanMonthManagerImpl implements InterestLoanMonthManager {
	
	@Autowired
	@Qualifier("interestLoanMonthDao")
	private InterestLoanMonthDao interestLoanMonthDao;
	
	@Override
	public void saveInterestLoanMonth(InterestLoanMonth interestLoanMonth) {
		if (interestLoanMonth.getLoanMonthId() == null) {
			interestLoanMonthDao.insertSelective(interestLoanMonth);
		} else {
			interestLoanMonthDao.updateByPrimaryKeySelective(interestLoanMonth);
		}
	}
	
	@Override
	public InterestLoanMonth findInterestLoanMonthById(Long id) {
		return interestLoanMonthDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<InterestLoanMonth> queryList(InterestLoanMonthQuery query) {
		return interestLoanMonthDao.selectByExample(query);
	}
	
	@Override
	public void deleteInterestLoanMonthById(Long id) {
		interestLoanMonthDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteInterestLoanMonth(InterestLoanMonthQuery query) {
		interestLoanMonthDao.deleteByExample(query);
	}
}

