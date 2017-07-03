/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.database.BaseDao;
import com.rkylin.wheatfield.dao.TransOrderInfoDao;
import com.rkylin.wheatfield.pojo.TransOrderInfo;
import com.rkylin.wheatfield.pojo.TransOrderInfoQuery;

@Repository("transOrderInfoDao")
public class TransOrderInfoDaoImpl extends BaseDao implements TransOrderInfoDao {
	
	@Override
	public int countByExample(TransOrderInfoQuery example) {
		return super.getSqlSession().selectOne("TransOrderInfoMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(TransOrderInfoQuery example) {
		return super.getSqlSession().delete("TransOrderInfoMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("TransOrderInfoMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(TransOrderInfo record) {
		return super.getSqlSession().insert("TransOrderInfoMapper.insert", record);
	}
	
	@Override
	public int insertSelective(TransOrderInfo record) {
		return super.getSqlSession().insert("TransOrderInfoMapper.insertSelective", record);
	}
	
	@Override
	public List<TransOrderInfo> selectByExample(TransOrderInfoQuery example) {
		return super.getSqlSession().selectList("TransOrderInfoMapper.selectByExample", example);
	}
	
	@Override
	public TransOrderInfo selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("TransOrderInfoMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(TransOrderInfo record) {
		return super.getSqlSession().update("TransOrderInfoMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(TransOrderInfo record) {
		return super.getSqlSession().update("TransOrderInfoMapper.updateByPrimaryKey", record);
	}

	@Override
	public List<TransOrderInfo> selectByExampleGroup(TransOrderInfoQuery example) {
		return super.getSqlSession().selectList("TransOrderInfoMapper.selectTransOrderGroup", example);
	}
	@Override
	public List<TransOrderInfo> selectByExampleGroupByInter(TransOrderInfoQuery example) {
		return super.getSqlSession().selectList("TransOrderInfoMapper.selectTransOrderGroupByInter", example);
	}
	@Override
	public List<TransOrderInfo> selectList(TransOrderInfoQuery example) {
		return super.getSqlSession().selectList("TransOrderInfoMapper.selectTransOrderList", example);
	}
	@Override
	public int batchUpdate( List<?> list){
		return super.batchUpdate("TransOrderInfoMapper.updateByOrderNo", list);
	}

	public List<TransOrderInfo> selectTransOrdersRefund(TransOrderInfoQuery query) {
		return super.getSqlSession().selectList("TransOrderInfoMapper.selectTransOrdersRefund", query);
	}
	
	@Override
	public List<TransOrderInfo> selectTransOrderInfos(TransOrderInfoQuery query) {
		return super.getSqlSession().selectList("TransOrderInfoMapper.selectTransOrders", query);
	}
	
	@Override
	public List<TransOrderInfo> selectTransOrdersAndSumId(TransOrderInfoQuery query) {
		return super.getSqlSession().selectList("TransOrderInfoMapper.selectTransOrdersAndSumId", query);
	}
}
