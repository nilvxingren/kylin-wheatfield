/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.wheatfield.dao.CreditRepaymentHistoryDao;
import com.rkylin.wheatfield.pojo.CreditRepaymentHistory;
import com.rkylin.wheatfield.pojo.CreditRepaymentHistoryQuery;
import com.rkylin.database.BaseDao;

@Repository("creditRepaymentHistoryDao")
public class CreditRepaymentHistoryDaoImpl extends BaseDao implements CreditRepaymentHistoryDao {
	
	@Override
	public int countByExample(CreditRepaymentHistoryQuery example) {
		return super.getSqlSession().selectOne("CreditRepaymentHistoryMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(CreditRepaymentHistoryQuery example) {
		return super.getSqlSession().delete("CreditRepaymentHistoryMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("CreditRepaymentHistoryMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(CreditRepaymentHistory record) {
		return super.getSqlSession().insert("CreditRepaymentHistoryMapper.insert", record);
	}
	
	@Override
	public int insertSelective(CreditRepaymentHistory record) {
		return super.getSqlSession().insert("CreditRepaymentHistoryMapper.insertSelective", record);
	}
	
	@Override
	public List<CreditRepaymentHistory> selectByExample(CreditRepaymentHistoryQuery example) {
		return super.getSqlSession().selectList("CreditRepaymentHistoryMapper.selectByExample", example);
	}
	
	@Override
	public CreditRepaymentHistory selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("CreditRepaymentHistoryMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(CreditRepaymentHistory record) {
		return super.getSqlSession().update("CreditRepaymentHistoryMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(CreditRepaymentHistory record) {
		return super.getSqlSession().update("CreditRepaymentHistoryMapper.updateByPrimaryKey", record);
	}
	
}
