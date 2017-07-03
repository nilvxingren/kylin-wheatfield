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

import com.rkylin.wheatfield.dao.CreditRateTemplateDao;
import com.rkylin.wheatfield.manager.CreditRateTemplateManager;
import com.rkylin.wheatfield.pojo.CreditRateTemplate;
import com.rkylin.wheatfield.pojo.CreditRateTemplateQuery;
import com.rkylin.wheatfield.pojo.CreditRateTemplateRes;

@Component("creditRateTemplateManager")
public class CreditRateTemplateManagerImpl implements CreditRateTemplateManager {
	
	@Autowired
	@Qualifier("creditRateTemplateDao")
	private CreditRateTemplateDao creditRateTemplateDao;
	
	@Override
	public void saveCreditRateTemplate(CreditRateTemplate creditRateTemplate) {
		if (creditRateTemplate.getRateId() == null) {
			creditRateTemplateDao.insertSelective(creditRateTemplate);
		} else {
			creditRateTemplateDao.updateByPrimaryKeySelective(creditRateTemplate);
		}
	}
	@Override
	public void insertCreditRateTemplate(CreditRateTemplate creditRateTemplate) {
		creditRateTemplateDao.insertSelective(creditRateTemplate);
	}
	@Override
	public CreditRateTemplate findCreditRateTemplateById(Long id) {
		return creditRateTemplateDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<CreditRateTemplate> queryList(CreditRateTemplateQuery query) {
		return creditRateTemplateDao.selectByExample(query);
	}
	
	@Override
	public List<CreditRateTemplateRes> queryListWithJoin(CreditRateTemplateQuery query) {
		return creditRateTemplateDao.selectWithJoin(query);
	}	
	
	@Override
	public void deleteCreditRateTemplateById(Long id) {
		creditRateTemplateDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteCreditRateTemplate(CreditRateTemplateQuery query) {
		creditRateTemplateDao.deleteByExample(query);
	}
	
	@Override
	public int countByExample(CreditRateTemplateQuery query){
		return creditRateTemplateDao.countByExample(query);
	}
	
}

