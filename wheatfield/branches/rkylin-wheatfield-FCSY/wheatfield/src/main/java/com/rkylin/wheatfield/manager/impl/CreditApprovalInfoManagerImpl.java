/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.rkylin.wheatfield.dao.CreditApprovalInfoDao;
import com.rkylin.wheatfield.manager.CreditApprovalInfoManager;
import com.rkylin.wheatfield.pojo.CreditApprovalInfo;
import com.rkylin.wheatfield.pojo.CreditApprovalInfoQuery;

@Component("creditApprovalInfoManager")
public class CreditApprovalInfoManagerImpl implements CreditApprovalInfoManager {
	
	@Autowired
	@Qualifier("creditApprovalInfoDao")
	private CreditApprovalInfoDao creditApprovalInfoDao;
	
	@Override
	public void saveCreditApprovalInfo(CreditApprovalInfo creditApprovalInfo) {
		if (creditApprovalInfo.getCreditId() == null) {
			creditApprovalInfoDao.insertSelective(creditApprovalInfo);
		} else {
			creditApprovalInfoDao.updateByPrimaryKeySelective(creditApprovalInfo);
		}
	}
	
	@Override
	public CreditApprovalInfo findCreditApprovalInfoById(Long id) {
		return creditApprovalInfoDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<CreditApprovalInfo> queryList(CreditApprovalInfoQuery query) {
		return creditApprovalInfoDao.selectByExample(query);
	}
	
	@Override
	public void deleteCreditApprovalInfoById(Long id) {
		creditApprovalInfoDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteCreditApprovalInfo(CreditApprovalInfoQuery query) {
		creditApprovalInfoDao.deleteByExample(query);
	}

	@Override
	public int updateInterestDate(Map<String, String> paraMap) {
		// TODO Auto-generated method stub
		return creditApprovalInfoDao.updateInterestDate(paraMap);
	}

	@Override
	public List<String> validateOrderList(Map<String, String> paraMap) {
		return creditApprovalInfoDao.validateOrderList(paraMap);
	}

	@Override
	public long sumAmountForSqsm() {
		return creditApprovalInfoDao.sumAmountForSqsm();
	}
	
}

