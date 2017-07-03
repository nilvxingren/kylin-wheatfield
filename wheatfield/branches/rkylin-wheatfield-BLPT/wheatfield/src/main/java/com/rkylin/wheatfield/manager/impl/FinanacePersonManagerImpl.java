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

import com.rkylin.wheatfield.dao.FinanacePersonDao;
import com.rkylin.wheatfield.manager.FinanacePersonManager;
import com.rkylin.wheatfield.pojo.FinanacePerson;
import com.rkylin.wheatfield.pojo.FinanacePersonQuery;
import com.rkylin.wheatfield.utils.CommUtil;

@Component("finanacePersonManager")
public class FinanacePersonManagerImpl implements FinanacePersonManager {
	
	@Autowired
	@Qualifier("finanacePersonDao")
	private FinanacePersonDao finanacePersonDao;
	
	@Override
	public void saveFinanacePerson(FinanacePerson finanacePerson) {
			finanacePersonDao.insertSelective(finanacePerson);
	}
	
	@Override
	public FinanacePerson findFinanacePersonById(Long id) {
		return finanacePersonDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<FinanacePerson> queryList(FinanacePersonQuery query) {
		return finanacePersonDao.selectByExample(query);
	}
	
	@Override
	public void deleteFinanacePersonById(Long id) {
		finanacePersonDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteFinanacePerson(FinanacePersonQuery query) {
		finanacePersonDao.deleteByExample(query);
	}

	@Override
	public List<FinanacePerson> queryListT(FinanacePersonQuery query) {
		return finanacePersonDao.selectByRootInstCdOrNumOrStatusId(query);
	}

	@Override
	public void updateFinanacePersonByFinanaceAccountId(
			FinanacePerson finanacePerson) {
		finanacePersonDao.updateByFinanaceAccountId(finanacePerson);
	}

	@Override
	public List<FinanacePerson> queryListBatch(FinanacePersonQuery query) {
		return finanacePersonDao.selectByExampleBatch(query);
	}

//	@Override
//    public List<FinanacePerson> selectPersonAccInfo(FinanacePersonQuery query) {
//        if (query.getPageSize()==null) {
//            query.setPageSize(50);
//        }
//        if (query.getPageNum()==null) {
//            query.setPageNum(0);
//        }            
//        if (query.getAccountCode()==null && query.getPageSize()==null && query.getPageNum()==null) {
//            query.setPageNum(0);
//            query.setPageSize(50);
//        }        
//        return finanacePersonDao.selectPersonAccInfo(query);
//    }
}

