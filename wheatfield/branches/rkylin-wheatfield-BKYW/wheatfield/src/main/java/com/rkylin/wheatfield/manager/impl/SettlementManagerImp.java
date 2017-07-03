package com.rkylin.wheatfield.manager.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.rkylin.wheatfield.dao.SettlementDao;
import com.rkylin.wheatfield.dao.TransOrderInfoDao;
import com.rkylin.wheatfield.manager.SettlementManager;
import com.rkylin.wheatfield.pojo.SettleBatchManage;
import com.rkylin.wheatfield.pojo.SettleBatchManageQuery;
import com.rkylin.wheatfield.pojo.TransOrderInfo;
import com.rkylin.wheatfield.pojo.TransOrderInfoQuery;

@Component("settlementManager")
public class SettlementManagerImp implements SettlementManager {

	@Autowired
	@Qualifier("settlementDao")
	private SettlementDao settlementDao;

	@Override
	public List<TransOrderInfo> queryList(Map paraMap) {
		return settlementDao.selectTransOrderInfoByDate(paraMap);
	}

	@Override
	public List<Map<String,Object>> queryP2PList(Map paraMap) {
		return settlementDao.selectDedtInfoByDate(paraMap);
	}
	
	@Override
	public List<Map<String,Object>> queryP2PDetailList(Map paraMap) {
		return settlementDao.selectDedtDetailByDate(paraMap);
	}
	
	@Override
	public List<Map<String,Object>> queryPaymentList(Map paraMap) {
		return settlementDao.selectPaymentByDate(paraMap);
	}

	@Override
	public List<Map<String,Object>> selectGenerationRes(Map paraMap) {
		return settlementDao.selectGenerationRes(paraMap);
	}

	@Override
	public List<Map<String,Object>> selectGenerationCnt(Map paraMap) {
		return settlementDao.selectGenerationCnt(paraMap);
	}

	@Override
	public List<TransOrderInfo> selectTransOrderInfoCh(Map paraMap) {
		return settlementDao.selectTransOrderInfoCh(paraMap);
	}

	@Override
	public List<TransOrderInfo> selectTransOrderInfoD(Map paraMap) {
		return settlementDao.selectTransOrderInfoD(paraMap);
	}
	
	@Override
	public int updatetransstatus(Map paraMap) {
		return settlementDao.updatetransstatus(paraMap);
	}

	@Override
	public List<Map<String,Object>> selectWithhold(Map paraMap) {
		return settlementDao.selectWithhold(paraMap);
	}

	@Override
	public List<SettleBatchManage> selectbatchno(SettleBatchManage paraMap) {
		return settlementDao.selectbatchno(paraMap);
	}
	
	@Override
	public List<Map<String,Object>> selectDeduct(Map paraMap) {
		return settlementDao.selectDeduct(paraMap);
	}
}
