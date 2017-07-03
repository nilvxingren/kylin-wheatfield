/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.wheatfield.dao.CreditRepaymentDaysSummaryDao;
import com.rkylin.wheatfield.pojo.CreditRepaymentDaysSummary;
import com.rkylin.wheatfield.pojo.CreditRepaymentDaysSummaryQuery;
import com.rkylin.database.BaseDao;

@Repository("creditRepaymentDaysSummaryDao")
public class CreditRepaymentDaysSummaryDaoImpl extends BaseDao implements CreditRepaymentDaysSummaryDao {
	
	@Override
	public int countByExample(CreditRepaymentDaysSummaryQuery example) {
		return super.getSqlSession().selectOne("CreditRepaymentDaysSummaryMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(CreditRepaymentDaysSummaryQuery example) {
		return super.getSqlSession().delete("CreditRepaymentDaysSummaryMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("CreditRepaymentDaysSummaryMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(CreditRepaymentDaysSummary record) {
		return super.getSqlSession().insert("CreditRepaymentDaysSummaryMapper.insert", record);
	}
	
	@Override
	public int insertSelective(CreditRepaymentDaysSummary record) {
		return super.getSqlSession().insert("CreditRepaymentDaysSummaryMapper.insertSelective", record);
	}
	
	@Override
	public List<CreditRepaymentDaysSummary> selectByExample(CreditRepaymentDaysSummaryQuery example) {
		return super.getSqlSession().selectList("CreditRepaymentDaysSummaryMapper.selectByExample", example);
	}
	
	@Override
	public CreditRepaymentDaysSummary selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("CreditRepaymentDaysSummaryMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(CreditRepaymentDaysSummary record) {
		return super.getSqlSession().update("CreditRepaymentDaysSummaryMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(CreditRepaymentDaysSummary record) {
		return super.getSqlSession().update("CreditRepaymentDaysSummaryMapper.updateByPrimaryKey", record);
	}
	
}
