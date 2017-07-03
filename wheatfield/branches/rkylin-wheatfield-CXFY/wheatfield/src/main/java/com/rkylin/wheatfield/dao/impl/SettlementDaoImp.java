package com.rkylin.wheatfield.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.rkylin.database.BaseDao;
import com.rkylin.wheatfield.dao.SettlementDao;
import com.rkylin.wheatfield.pojo.SettleBatchManage;
import com.rkylin.wheatfield.pojo.TransOrderInfo;

@Repository("settlementDao")
public class SettlementDaoImp  extends BaseDao implements SettlementDao {
	
	@Override
	public List<TransOrderInfo> selectTransOrderInfoByDate(Map paraMap) {
		return super.getSqlSession().selectList("SettlementMapper.selectTransOrderInfoByDate", paraMap);
	}

	@Override
	public List<Map<String,Object>> selectDedtInfoByDate(Map paraMap) {
		return super.getSqlSession().selectList("SettlementMapper.selectDedtInfoByDate", paraMap);
	}
	
	@Override
	public List<Map<String,Object>> selectDedtDetailByDate(Map paraMap) {
		return super.getSqlSession().selectList("SettlementMapper.selectDedtDetailByDate", paraMap);
	}

	@Override
	public List<Map<String,Object>> selectPaymentByDate(Map paraMap) {
		return super.getSqlSession().selectList("SettlementMapper.selectPaymentByDate", paraMap);
	}
	
	@Override
	public List<Map<String,Object>> selectGenerationRes(Map paraMap) {
		return super.getSqlSession().selectList("SettlementMapper.selectgenerationres", paraMap);
	}
	
	@Override
	public List<Map<String,Object>> selectGenerationCnt(Map paraMap) {
		return super.getSqlSession().selectList("SettlementMapper.selectgenerationcnt", paraMap);
	}
	
	@Override
	public List<TransOrderInfo> selectTransOrderInfoCh(Map paraMap) {
		return super.getSqlSession().selectList("SettlementMapper.selectTransOrderInfoCh", paraMap);
	}
	
	@Override
	public List<TransOrderInfo> selectTransOrderInfoD(Map paraMap) {
		return super.getSqlSession().selectList("SettlementMapper.selectTransOrderInfoD", paraMap);
	}

	@Override
	public int updatetransstatus(Map paraMap) {
		return super.getSqlSession().update("SettlementMapper.updatetransstatus", paraMap);
	}
	
	@Override
	public List<Map<String,Object>> selectWithhold(Map paraMap) {
		return super.getSqlSession().selectList("SettlementMapper.selectWithhold", paraMap);
	}
	
	@Override
	public List<SettleBatchManage> selectbatchno(SettleBatchManage paraMap) {
		return super.getSqlSession().selectList("SettlementMapper.selectbatchno", paraMap);
	}
	
	@Override
	public List<Map<String,Object>> selectDeduct(Map paraMap) {
		return super.getSqlSession().selectList("SettlementMapper.selectDeduct", paraMap);
	}
}
