/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.rkylin.wheatfield.dao.InterestRepaymentDao;
import com.rkylin.wheatfield.pojo.InterestRepayment;
import com.rkylin.wheatfield.pojo.InterestRepaymentQuery;
import com.rkylin.database.BaseDao;

@Repository("interestRepaymentDao")
public class InterestRepaymentDaoImpl extends BaseDao implements InterestRepaymentDao {
	
	@Override
	public int countByExample(InterestRepaymentQuery example) {
		return super.getSqlSession().selectOne("InterestRepaymentMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(InterestRepaymentQuery example) {
		return super.getSqlSession().delete("InterestRepaymentMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("InterestRepaymentMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(InterestRepayment record) {
		return super.getSqlSession().insert("InterestRepaymentMapper.insert", record);
	}
	
	@Override
	public int insertSelective(InterestRepayment record) {
		return super.getSqlSession().insert("InterestRepaymentMapper.insertSelective", record);
	}
	
	@Override
	public List<InterestRepayment> selectByExample(InterestRepaymentQuery example) {
		return super.getSqlSession().selectList("InterestRepaymentMapper.selectByExample", example);
	}
	
	@Override
	public InterestRepayment selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("InterestRepaymentMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(InterestRepayment record) {
		return super.getSqlSession().update("InterestRepaymentMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(InterestRepayment record) {
		return super.getSqlSession().update("InterestRepaymentMapper.updateByPrimaryKey", record);
	}
	
	@Override
	public List<InterestRepayment> selectInterAndParamByExample(
			InterestRepaymentQuery example) {
		return super.getSqlSession().selectList("InterestRepaymentMapper.selectInterAndParamByExample", example);
	}
	
	@Override
	public List<InterestRepayment> selectInterestRepaymentExample(
			InterestRepaymentQuery example) {		
		return super.getSqlSession().selectList("InterestRepaymentMapper.selectInterestRepaymentExample", example);
	}

	@Override
	public List<InterestRepayment> selectForRepayment(InterestRepaymentQuery example) {
		return super.getSqlSession().selectList("InterestRepaymentMapper.selectForRepayment", example);
	}

	@Override
	public List<InterestRepayment> selectForUpload(Map<String, String> parameter) {
		return super.getSqlSession().selectList("InterestRepaymentMapper.selectForUpload", parameter);
	}

	@Override
	public List<Map<String, ?>> selectForRecoverAmount() {
		return super.getSqlSession().selectList("InterestRepaymentMapper.selectForRecoverAmount");
	}
	
	@Override
	public List<InterestRepayment> selectRepaymentForUpload(InterestRepaymentQuery example) {
		return super.getSqlSession().selectList("InterestRepaymentMapper.selectRepaymentForUpload", example);
	}
	
	@Override
	public List<InterestRepayment> selectForCapital(String rootInstCd) {
		return super.getSqlSession().selectList("InterestRepaymentMapper.selectForCapital", rootInstCd);
	}
	
	@Override
	public List<InterestRepayment> selectForInterest(String rootInstCd) {
		return super.getSqlSession().selectList("InterestRepaymentMapper.selectForInterest", rootInstCd);
	}
	
}
