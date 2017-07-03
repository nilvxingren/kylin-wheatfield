/*
 * Powered By code-generator
 * Web Site: http://www.rkylin.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.rkylin.wheatfield.dao.InterestRepaymentHisDao;
import com.rkylin.wheatfield.manager.InterestRepaymentHisManager;
import com.rkylin.wheatfield.pojo.InterestRepaymentHis;
import com.rkylin.wheatfield.pojo.InterestRepaymentHisQuery;

@Component("interestRepaymentHisManager")
public class InterestRepaymentHisManagerImpl implements InterestRepaymentHisManager {
	
	@Autowired
	@Qualifier("interestRepaymentHisDao")
	private InterestRepaymentHisDao interestRepaymentHisDao;
	
	@Override
	public void saveInterestRepaymentHis(InterestRepaymentHis interestRepaymentHis) {
			interestRepaymentHisDao.insertSelective(interestRepaymentHis);
	}

	@Override
	public int updateInterestRepaymentHis(InterestRepaymentHis interestRepaymentHis) {
		return interestRepaymentHisDao.updateByPrimaryKeySelective(interestRepaymentHis);
	}

	@Override
	public InterestRepaymentHis findInterestRepaymentHisById(String id) {
		return interestRepaymentHisDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<InterestRepaymentHis> queryList(InterestRepaymentHisQuery query) {
		return interestRepaymentHisDao.selectByExample(query);
	}
	
	@Override
	public void deleteInterestRepaymentHisById(String id) {
		interestRepaymentHisDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteInterestRepaymentHis(InterestRepaymentHisQuery query) {
		interestRepaymentHisDao.deleteByExample(query);
	}

	@Override
	public long sumRepaidAmountByExample(InterestRepaymentHisQuery example) {
		Long amount =  interestRepaymentHisDao.sumRepaidAmountByExample(example);
		if(amount == null){
			return 0;
		}
		return amount;
	}
}

