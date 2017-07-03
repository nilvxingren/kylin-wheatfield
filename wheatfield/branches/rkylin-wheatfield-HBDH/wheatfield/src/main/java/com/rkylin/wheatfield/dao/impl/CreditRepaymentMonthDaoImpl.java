/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.wheatfield.dao.CreditRepaymentMonthDao;
import com.rkylin.wheatfield.pojo.CreditRepaymentMonth;
import com.rkylin.wheatfield.pojo.CreditRepaymentMonthQuery;
import com.rkylin.database.BaseDao;

@Repository("creditRepaymentMonthDao")
public class CreditRepaymentMonthDaoImpl extends BaseDao implements CreditRepaymentMonthDao {
	
	@Override
	public int countByExample(CreditRepaymentMonthQuery example) {
		return super.getSqlSession().selectOne("CreditRepaymentMonthMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(CreditRepaymentMonthQuery example) {
		return super.getSqlSession().delete("CreditRepaymentMonthMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("CreditRepaymentMonthMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(CreditRepaymentMonth record) {
		return super.getSqlSession().insert("CreditRepaymentMonthMapper.insert", record);
	}
	
	@Override
	public int insertSelective(CreditRepaymentMonth record) {
		return super.getSqlSession().insert("CreditRepaymentMonthMapper.insertSelective", record);
	}
	
	@Override
	public List<CreditRepaymentMonth> selectByExample(CreditRepaymentMonthQuery example) {
		return super.getSqlSession().selectList("CreditRepaymentMonthMapper.selectByExample", example);
	}
	
	@Override
	public CreditRepaymentMonth selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("CreditRepaymentMonthMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(CreditRepaymentMonth record) {
		return super.getSqlSession().update("CreditRepaymentMonthMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(CreditRepaymentMonth record) {
		return super.getSqlSession().update("CreditRepaymentMonthMapper.updateByPrimaryKey", record);
	}
	
}
