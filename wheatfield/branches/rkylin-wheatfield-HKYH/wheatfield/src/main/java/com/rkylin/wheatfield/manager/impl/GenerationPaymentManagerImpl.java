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

import com.rkylin.wheatfield.dao.GenerationPaymentDao;
import com.rkylin.wheatfield.manager.GenerationPaymentManager;
import com.rkylin.wheatfield.pojo.GenerationPayment;
import com.rkylin.wheatfield.pojo.GenerationPaymentQuery;

@Component("generationPaymentManager")
public class GenerationPaymentManagerImpl implements GenerationPaymentManager {
	
	@Autowired
	@Qualifier("generationPaymentDao")
	private GenerationPaymentDao generationPaymentDao;
	
	@Override
	public void saveGenerationPayment(GenerationPayment generationPayment) {
		if (generationPayment.getGeneId() == null) {
			generationPaymentDao.insertSelective(generationPayment);
		} else {
			generationPaymentDao.updateByPrimaryKeySelective(generationPayment);
		}
	}
	
	@Override
	public GenerationPayment findGenerationPaymentById(Long id) {
		return generationPaymentDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<GenerationPayment> queryList(GenerationPaymentQuery query) {
		return generationPaymentDao.selectByExample(query);
	}
	@Override
	public List<GenerationPayment> queryListByOrderType(GenerationPaymentQuery query) {
		return generationPaymentDao.selectByOrderType(query);
	}
	@Override
	public void deleteGenerationPaymentById(Long id) {
		generationPaymentDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteGenerationPayment(GenerationPaymentQuery query) {
		generationPaymentDao.deleteByExample(query);
	}
	@Override
	public void batchUpdate(List<?> list){
		generationPaymentDao.batchUpdate(list);
	}

	@Override
	public void batchUpdateByOrderNoRootInstCd(List<?> list) {
		generationPaymentDao.batchUpdateByOrderNoRootInstCd(list);
		
	}
	
	public List<GenerationPayment> selectByOrderNo(String[]  orderNo) {
		return generationPaymentDao.selectByOrderNo(orderNo);
	}
}

