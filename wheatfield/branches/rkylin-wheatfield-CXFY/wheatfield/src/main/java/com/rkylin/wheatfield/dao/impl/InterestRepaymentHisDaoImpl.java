/*
 * Powered By code-generator
 * Web Site: http://www.rkylin.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.database.BaseDao;
import com.rkylin.wheatfield.dao.InterestRepaymentHisDao;
import com.rkylin.wheatfield.pojo.InterestRepaymentHis;
import com.rkylin.wheatfield.pojo.InterestRepaymentHisQuery;

@Repository("interestRepaymentHisDao")
public class InterestRepaymentHisDaoImpl extends BaseDao implements InterestRepaymentHisDao {
	
	@Override
	public int countByExample(InterestRepaymentHisQuery example) {
		return super.getSqlSession().selectOne("InterestRepaymentHisMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(InterestRepaymentHisQuery example) {
		return super.getSqlSession().delete("InterestRepaymentHisMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(String id) {
		return super.getSqlSession().delete("InterestRepaymentHisMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(InterestRepaymentHis record) {
		return super.getSqlSession().insert("InterestRepaymentHisMapper.insert", record);
	}
	
	@Override
	public int insertSelective(InterestRepaymentHis record) {
		return super.getSqlSession().insert("InterestRepaymentHisMapper.insertSelective", record);
	}
	
	@Override
	public List<InterestRepaymentHis> selectByExample(InterestRepaymentHisQuery example) {
		return super.getSqlSession().selectList("InterestRepaymentHisMapper.selectByExample", example);
	}
	
	@Override
	public InterestRepaymentHis selectByPrimaryKey(String id) {
		return super.getSqlSession().selectOne("InterestRepaymentHisMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(InterestRepaymentHis record) {
		return super.getSqlSession().update("InterestRepaymentHisMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(InterestRepaymentHis record) {
		return super.getSqlSession().update("InterestRepaymentHisMapper.updateByPrimaryKey", record);
	}

	@Override
	public Long sumRepaidAmountByExample(InterestRepaymentHisQuery example) {
		return super.getSqlSession().selectOne("InterestRepaymentHisMapper.sumRepaidAmountByExample", example);
	}
	
}
