/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager.impl;

import com.rkylin.wheatfield.dao.FinanaceCompanyDao;
import com.rkylin.wheatfield.manager.FinanaceCompanyManager;
import com.rkylin.wheatfield.pojo.FinanaceCompany;
import com.rkylin.wheatfield.pojo.FinanaceCompanyQuery;
import com.rkylin.wheatfield.utils.CommUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("finanaceCompanyManager")
public class FinanaceCompanyManagerImpl implements FinanaceCompanyManager {
	
	@Autowired
	@Qualifier("finanaceCompanyDao")
	private FinanaceCompanyDao finanaceCompanyDao;
	
	@Override
	public void saveFinanaceCompany(FinanaceCompany finanaceCompany) {
			finanaceCompanyDao.insertSelective(finanaceCompany);
	}
	
	@Override
	public FinanaceCompany findFinanaceCompanyById(Long id) {
		return finanaceCompanyDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<FinanaceCompany> queryList(FinanaceCompanyQuery query) {
		return finanaceCompanyDao.selectByExample(query);
	}
	
	@Override
	public void deleteFinanaceCompanyById(Long id) {
		finanaceCompanyDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteFinanaceCompany(FinanaceCompanyQuery query) {
		finanaceCompanyDao.deleteByExample(query);
	}

	@Override
	public void updateFinanaceCompanyByFinanaceAccountId(FinanaceCompany finanaceCompany) {
		finanaceCompanyDao.updateByFinanaceAccountId(finanaceCompany);
	}

	@Override
	public List<FinanaceCompany> queryListT(FinanaceCompanyQuery query) {
		return finanaceCompanyDao.selectByRootInstCdOrBUSLINCEOrStatusId(query);
	}

	@Override
	public List<FinanaceCompany> getFinanaceCompanies(FinanaceCompanyQuery query) {
		return finanaceCompanyDao.getFinanaceCompanies(query);
	}

//    @Override
//    public List<FinanaceCompany> selectCompanyAccInfo(FinanaceCompanyQuery query) {         
//        if (query.getAccountCode()==null && query.getDataCount()==null && query.getDataIndex()==null) {
//            query.setDataIndex(0);
//            query.setDataCount(50);
//        }
//        return finanaceCompanyDao.selectCompanyAccInfo(query);
//    }
}

