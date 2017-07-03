/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.wheatfield.dao.CreditRepaymentDao;
import com.rkylin.wheatfield.pojo.CreditRepayment;
import com.rkylin.wheatfield.pojo.CreditRepaymentQuery;
import com.rkylin.database.BaseDao;

@Repository("creditRepaymentDao")
public class CreditRepaymentDaoImpl extends BaseDao implements CreditRepaymentDao {
	
	@Override
	public int countByExample(CreditRepaymentQuery example) {
		return super.getSqlSession().selectOne("CreditRepaymentMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(CreditRepaymentQuery example) {
		return super.getSqlSession().delete("CreditRepaymentMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("CreditRepaymentMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(CreditRepayment record) {
		return super.getSqlSession().insert("CreditRepaymentMapper.insert", record);
	}
	
	@Override
	public int insertSelective(CreditRepayment record) {
		return super.getSqlSession().insert("CreditRepaymentMapper.insertSelective", record);
	}
	
	@Override
	public List<CreditRepayment> selectByExample(CreditRepaymentQuery example) {
		return super.getSqlSession().selectList("CreditRepaymentMapper.selectByExample", example);
	}
	
	@Override
	public CreditRepayment selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("CreditRepaymentMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(CreditRepayment record) {
		return super.getSqlSession().update("CreditRepaymentMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(CreditRepayment record) {
		return super.getSqlSession().update("CreditRepaymentMapper.updateByPrimaryKey", record);
	}
	
}
