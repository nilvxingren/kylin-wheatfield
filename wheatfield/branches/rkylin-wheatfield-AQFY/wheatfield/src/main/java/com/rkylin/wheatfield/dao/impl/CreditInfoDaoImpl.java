/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.wheatfield.dao.CreditInfoDao;
import com.rkylin.wheatfield.pojo.CreditInfo;
import com.rkylin.wheatfield.pojo.CreditInfoQuery;
import com.rkylin.database.BaseDao;

@Repository("creditInfoDao")
public class CreditInfoDaoImpl extends BaseDao implements CreditInfoDao {
	
	@Override
	public int countByExample(CreditInfoQuery example) {
		return super.getSqlSession().selectOne("CreditInfoMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(CreditInfoQuery example) {
		return super.getSqlSession().delete("CreditInfoMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("CreditInfoMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(CreditInfo record) {
		return super.getSqlSession().insert("CreditInfoMapper.insert", record);
	}
	
	@Override
	public int insertSelective(CreditInfo record) {
		return super.getSqlSession().insert("CreditInfoMapper.insertSelective", record);
	}
	
	@Override
	public List<CreditInfo> selectByExample(CreditInfoQuery example) {
		return super.getSqlSession().selectList("CreditInfoMapper.selectByExample", example);
	}
	
	@Override
	public CreditInfo selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("CreditInfoMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(CreditInfo record) {
		return super.getSqlSession().update("CreditInfoMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(CreditInfo record) {
		return super.getSqlSession().update("CreditInfoMapper.updateByPrimaryKey", record);
	}
	
}
