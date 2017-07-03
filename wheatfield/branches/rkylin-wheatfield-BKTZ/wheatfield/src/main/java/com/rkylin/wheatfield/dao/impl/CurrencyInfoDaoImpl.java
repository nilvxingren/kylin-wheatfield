/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.wheatfield.dao.CurrencyInfoDao;
import com.rkylin.wheatfield.pojo.CurrencyInfo;
import com.rkylin.wheatfield.pojo.CurrencyInfoQuery;
import com.rkylin.database.BaseDao;

@Repository("currencyInfoDao")
public class CurrencyInfoDaoImpl extends BaseDao implements CurrencyInfoDao {
	
	@Override
	public int countByExample(CurrencyInfoQuery example) {
		return super.getSqlSession().selectOne("CurrencyInfoMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(CurrencyInfoQuery example) {
		return super.getSqlSession().delete("CurrencyInfoMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("CurrencyInfoMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(CurrencyInfo record) {
		return super.getSqlSession().insert("CurrencyInfoMapper.insert", record);
	}
	
	@Override
	public int insertSelective(CurrencyInfo record) {
		return super.getSqlSession().insert("CurrencyInfoMapper.insertSelective", record);
	}
	
	@Override
	public List<CurrencyInfo> selectByExample(CurrencyInfoQuery example) {
		return super.getSqlSession().selectList("CurrencyInfoMapper.selectByExample", example);
	}
	
	@Override
	public CurrencyInfo selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("CurrencyInfoMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(CurrencyInfo record) {
		return super.getSqlSession().update("CurrencyInfoMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(CurrencyInfo record) {
		return super.getSqlSession().update("CurrencyInfoMapper.updateByPrimaryKey", record);
	}
	
}
