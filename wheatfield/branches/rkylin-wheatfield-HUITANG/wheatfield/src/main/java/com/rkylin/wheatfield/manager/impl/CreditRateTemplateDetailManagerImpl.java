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

import com.rkylin.wheatfield.dao.CreditRateTemplateDetailDao;
import com.rkylin.wheatfield.manager.CreditRateTemplateDetailManager;
import com.rkylin.wheatfield.pojo.CreditRateTemplateDetail;
import com.rkylin.wheatfield.pojo.CreditRateTemplateDetailQuery;

@Component("creditRateTemplateDetailManager")
public class CreditRateTemplateDetailManagerImpl implements CreditRateTemplateDetailManager {
	
	@Autowired
	@Qualifier("creditRateTemplateDetailDao")
	private CreditRateTemplateDetailDao creditRateTemplateDetailDao;
	
	@Override
	public void saveCreditRateTemplateDetail(CreditRateTemplateDetail creditRateTemplateDetail) {
		if (creditRateTemplateDetail.getRateDetailId() == null) {
			creditRateTemplateDetailDao.insertSelective(creditRateTemplateDetail);
		} else {
			creditRateTemplateDetailDao.updateByPrimaryKeySelective(creditRateTemplateDetail);
		}
	}
	
	@Override
	public CreditRateTemplateDetail findCreditRateTemplateDetailById(Long id) {
		return creditRateTemplateDetailDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<CreditRateTemplateDetail> queryList(CreditRateTemplateDetailQuery query) {
		return creditRateTemplateDetailDao.selectByExample(query);
	}
	
	@Override
	public void deleteCreditRateTemplateDetailById(Long id) {
		creditRateTemplateDetailDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteCreditRateTemplateDetail(CreditRateTemplateDetailQuery query) {
		creditRateTemplateDetailDao.deleteByExample(query);
	}
	
	@Override
	public void updateCreditRateTemplateDetail(CreditRateTemplateDetail creditRateTemplateDetail){
		creditRateTemplateDetailDao.updateByRateId(creditRateTemplateDetail);
	}
	
	@Override
	public void updateStatusByRateId(CreditRateTemplateDetail creditRateTemplateDetail){
		creditRateTemplateDetailDao.updateStatusByRateId(creditRateTemplateDetail);
	}	
	
}

