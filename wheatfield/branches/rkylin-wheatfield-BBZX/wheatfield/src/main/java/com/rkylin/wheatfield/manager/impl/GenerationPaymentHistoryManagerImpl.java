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

import com.rkylin.wheatfield.dao.GenerationPaymentHistoryDao;
import com.rkylin.wheatfield.manager.GenerationPaymentHistoryManager;
import com.rkylin.wheatfield.pojo.GenerationPaymentHistory;
import com.rkylin.wheatfield.pojo.GenerationPaymentHistoryQuery;

@Component("generationPaymentHistoryManager")
public class GenerationPaymentHistoryManagerImpl implements GenerationPaymentHistoryManager {
	
	@Autowired
	@Qualifier("generationPaymentHistoryDao")
	private GenerationPaymentHistoryDao generationPaymentHistoryDao;
	
	@Override
	public void saveGenerationPaymentHistory(GenerationPaymentHistory generationPaymentHistory) {
		if (generationPaymentHistory.getGeneId() == null) {
			generationPaymentHistoryDao.insertSelective(generationPaymentHistory);
		} else {
			generationPaymentHistoryDao.updateByPrimaryKeySelective(generationPaymentHistory);
		}
	}
	
	@Override
	public GenerationPaymentHistory findGenerationPaymentHistoryById(Long id) {
		return generationPaymentHistoryDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<GenerationPaymentHistory> queryList(GenerationPaymentHistoryQuery query) {
		return generationPaymentHistoryDao.selectByExample(query);
	}
	
	@Override
	public void deleteGenerationPaymentHistoryById(Long id) {
		generationPaymentHistoryDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteGenerationPaymentHistory(GenerationPaymentHistoryQuery query) {
		generationPaymentHistoryDao.deleteByExample(query);
	}
	
	public List<GenerationPaymentHistory> selectByOnecent(){
		return generationPaymentHistoryDao.selectByOnecent();
	};
}

