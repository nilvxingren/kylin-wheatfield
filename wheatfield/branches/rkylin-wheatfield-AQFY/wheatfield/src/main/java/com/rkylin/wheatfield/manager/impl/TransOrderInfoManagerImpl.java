/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.rkylin.wheatfield.dao.TransOrderInfoDao;
import com.rkylin.wheatfield.manager.TransOrderInfoManager;
import com.rkylin.wheatfield.pojo.TransOrderInfo;
import com.rkylin.wheatfield.pojo.TransOrderInfoQuery;

@Component("transOrderInfoManager")
public class TransOrderInfoManagerImpl implements TransOrderInfoManager {
	
	@Autowired
	@Qualifier("transOrderInfoDao")
	private TransOrderInfoDao transOrderInfoDao;
	
	@Override
	public int saveTransOrderInfo(TransOrderInfo transOrderInfo) {
		if (transOrderInfo.getRequestId() == null) {
			return transOrderInfoDao.insertSelective(transOrderInfo);
		} else {
			return transOrderInfoDao.updateByPrimaryKeySelective(transOrderInfo);
		}
	}
	
	@Override
	public TransOrderInfo findTransOrderInfoById(Integer id) {
		return transOrderInfoDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<TransOrderInfo> queryList(TransOrderInfoQuery query) {
		return transOrderInfoDao.selectByExample(query);
	}
	
	@Override
	public void deleteTransOrderInfoById(Integer id) {
		transOrderInfoDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteTransOrderInfo(TransOrderInfoQuery query) {
		transOrderInfoDao.deleteByExample(query);
	}

	@Override
	public List<TransOrderInfo> queryListGroup(TransOrderInfoQuery query) {
		
		return transOrderInfoDao.selectByExampleGroup(query);
	}
	@Override
	public List<TransOrderInfo> queryListGroupByInter(TransOrderInfoQuery query) {
		
		return transOrderInfoDao.selectByExampleGroupByInter(query);
	}
	@Override
	public List<TransOrderInfo> queryListByCreditDate(TransOrderInfoQuery query) {
		
		return transOrderInfoDao.selectList(query);
	}
	@Override
	public int batchUpdateByOrderNo(List<?> list) {
		return transOrderInfoDao.batchUpdate(list);
	}

	@Override
	public List<TransOrderInfo> selectTransOrderInfos(TransOrderInfoQuery query) {
		return transOrderInfoDao.selectTransOrderInfos(query);
	}
	
	@Override
	public List<TransOrderInfo> selectTransOrdersAndSumId(TransOrderInfoQuery query) {
		return transOrderInfoDao.selectTransOrdersAndSumId(query);
	}
	
	public List<TransOrderInfo> selectTransOrderInfosRefund(TransOrderInfoQuery query) {
		return transOrderInfoDao.selectTransOrdersRefund(query);
	}

	@Override
	public List<Map<String, Object>> selectSummaryInfo(Map summaryMap) {
		return transOrderInfoDao.selectSummaryInfo(summaryMap);
	}
	
	
}

