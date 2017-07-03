/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.wheatfield.dao.CreditRepaymentMonthSummaryDao;
import com.rkylin.wheatfield.pojo.CreditRepaymentMonthSummary;
import com.rkylin.wheatfield.pojo.CreditRepaymentMonthSummaryQuery;
import com.rkylin.database.BaseDao;

@Repository("creditRepaymentMonthSummaryDao")
public class CreditRepaymentMonthSummaryDaoImpl extends BaseDao implements CreditRepaymentMonthSummaryDao {
	
	@Override
	public int countByExample(CreditRepaymentMonthSummaryQuery example) {
		return super.getSqlSession().selectOne("CreditRepaymentMonthSummaryMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(CreditRepaymentMonthSummaryQuery example) {
		return super.getSqlSession().delete("CreditRepaymentMonthSummaryMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("CreditRepaymentMonthSummaryMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(CreditRepaymentMonthSummary record) {
		return super.getSqlSession().insert("CreditRepaymentMonthSummaryMapper.insert", record);
	}
	
	@Override
	public int insertSelective(CreditRepaymentMonthSummary record) {
		return super.getSqlSession().insert("CreditRepaymentMonthSummaryMapper.insertSelective", record);
	}
	
	@Override
	public List<CreditRepaymentMonthSummary> selectByExample(CreditRepaymentMonthSummaryQuery example) {
		return super.getSqlSession().selectList("CreditRepaymentMonthSummaryMapper.selectByExample", example);
	}
	
	@Override
	public CreditRepaymentMonthSummary selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("CreditRepaymentMonthSummaryMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(CreditRepaymentMonthSummary record) {
		return super.getSqlSession().update("CreditRepaymentMonthSummaryMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(CreditRepaymentMonthSummary record) {
		return super.getSqlSession().update("CreditRepaymentMonthSummaryMapper.updateByPrimaryKey", record);
	}
	
}
