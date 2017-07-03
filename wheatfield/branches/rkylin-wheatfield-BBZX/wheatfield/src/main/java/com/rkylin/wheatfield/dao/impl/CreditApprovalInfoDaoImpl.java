/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.rkylin.wheatfield.dao.CreditApprovalInfoDao;
import com.rkylin.wheatfield.pojo.CreditApprovalInfo;
import com.rkylin.wheatfield.pojo.CreditApprovalInfoQuery;
import com.rkylin.database.BaseDao;

@Repository("creditApprovalInfoDao")
public class CreditApprovalInfoDaoImpl extends BaseDao implements CreditApprovalInfoDao {
	
	@Override
	public int countByExample(CreditApprovalInfoQuery example) {
		return super.getSqlSession().selectOne("CreditApprovalInfoMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(CreditApprovalInfoQuery example) {
		return super.getSqlSession().delete("CreditApprovalInfoMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("CreditApprovalInfoMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(CreditApprovalInfo record) {
		return super.getSqlSession().insert("CreditApprovalInfoMapper.insert", record);
	}
	
	@Override
	public int insertSelective(CreditApprovalInfo record) {
		return super.getSqlSession().insert("CreditApprovalInfoMapper.insertSelective", record);
	}
	
	@Override
	public List<CreditApprovalInfo> selectByExample(CreditApprovalInfoQuery example) {
		return super.getSqlSession().selectList("CreditApprovalInfoMapper.selectByExample", example);
	}
	
	@Override
	public CreditApprovalInfo selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("CreditApprovalInfoMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(CreditApprovalInfo record) {
		return super.getSqlSession().update("CreditApprovalInfoMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(CreditApprovalInfo record) {
		return super.getSqlSession().update("CreditApprovalInfoMapper.updateByPrimaryKey", record);
	}

	@Override
	public int updateInterestDate(Map<String, String> paraMap) {
		// TODO Auto-generated method stub
		return super.getSqlSession().update("CreditApprovalInfoMapper.updateInterestDate", paraMap);
	}

	@Override
	public List<String> validateOrderList(Map<String,String> paraMap) {
		String statement = "CreditApprovalInfoMapper.validateOrderList";

		List<String> list = super.getSqlSession().selectList(statement, paraMap);

		return list;
	}

	@Override
	public long sumAmountForSqsm() {
		return super.getSqlSession().selectOne("CreditApprovalInfoMapper.sumAmountForSQSM");
	}
}
