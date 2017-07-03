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

import com.rkylin.wheatfield.dao.CreditRepaymentDao;
import com.rkylin.wheatfield.manager.CreditRepaymentManager;
import com.rkylin.wheatfield.pojo.CreditRepayment;
import com.rkylin.wheatfield.pojo.CreditRepaymentQuery;

@Component("creditRepaymentManager")
public class CreditRepaymentManagerImpl implements CreditRepaymentManager {
	
	@Autowired
	@Qualifier("creditRepaymentDao")
	private CreditRepaymentDao creditRepaymentDao;
	
	@Override
	public void saveCreditRepayment(CreditRepayment creditRepayment) {
		if (creditRepayment.getCredId() == null) {
			creditRepaymentDao.insertSelective(creditRepayment);
		} else {
			creditRepaymentDao.updateByPrimaryKeySelective(creditRepayment);
		}
	}
	
	@Override
	public CreditRepayment findCreditRepaymentById(Long id) {
		return creditRepaymentDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<CreditRepayment> queryList(CreditRepaymentQuery query) {
		return creditRepaymentDao.selectByExample(query);
	}
	
	@Override
	public void deleteCreditRepaymentById(Long id) {
		creditRepaymentDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteCreditRepayment(CreditRepaymentQuery query) {
		creditRepaymentDao.deleteByExample(query);
	}
}

