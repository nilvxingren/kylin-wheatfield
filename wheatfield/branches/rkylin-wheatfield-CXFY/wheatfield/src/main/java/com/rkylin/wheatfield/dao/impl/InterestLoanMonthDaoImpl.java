/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.database.BaseDao;
import com.rkylin.wheatfield.dao.InterestLoanMonthDao;
import com.rkylin.wheatfield.pojo.InterestLoanMonth;
import com.rkylin.wheatfield.pojo.InterestLoanMonthQuery;



@Repository("interestLoanMonthDao")
public class InterestLoanMonthDaoImpl extends BaseDao implements InterestLoanMonthDao {
	
	@Override
	public int countByExample(InterestLoanMonthQuery example) {
		return super.getSqlSession().selectOne("InterestLoanMonthMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(InterestLoanMonthQuery example) {
		return super.getSqlSession().delete("InterestLoanMonthMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("InterestLoanMonthMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(InterestLoanMonth record) {
		return super.getSqlSession().insert("InterestLoanMonthMapper.insert", record);
	}
	
	@Override
	public int insertSelective(InterestLoanMonth record) {
		return super.getSqlSession().insert("InterestLoanMonthMapper.insertSelective", record);
	}
	
	@Override
	public List<InterestLoanMonth> selectByExample(InterestLoanMonthQuery example) {
		return super.getSqlSession().selectList("InterestLoanMonthMapper.selectByExample", example);
	}
	
	@Override
	public InterestLoanMonth selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("InterestLoanMonthMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(InterestLoanMonth record) {
		return super.getSqlSession().update("InterestLoanMonthMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(InterestLoanMonth record) {
		return super.getSqlSession().update("InterestLoanMonthMapper.updateByPrimaryKey", record);
	}
	
}
