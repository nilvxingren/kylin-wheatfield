/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.wheatfield.dao.TlBankCodeDao;
import com.rkylin.wheatfield.pojo.TlBankCode;
import com.rkylin.wheatfield.pojo.TlBankCodeQuery;
import com.rkylin.database.BaseDao;

@Repository("tlBankCodeDao")
public class TlBankCodeDaoImpl extends BaseDao implements TlBankCodeDao {
	
	@Override
	public int countByExample(TlBankCodeQuery example) {
		return super.getSqlSession().selectOne("TlBankCodeMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(TlBankCodeQuery example) {
		return super.getSqlSession().delete("TlBankCodeMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("TlBankCodeMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(TlBankCode record) {
		return super.getSqlSession().insert("TlBankCodeMapper.insert", record);
	}
	
	@Override
	public int insertSelective(TlBankCode record) {
		return super.getSqlSession().insert("TlBankCodeMapper.insertSelective", record);
	}
	
	@Override
	public List<TlBankCode> selectByExample(TlBankCodeQuery example) {
		return super.getSqlSession().selectList("TlBankCodeMapper.selectByExample", example);
	}
	
	@Override
	public TlBankCode selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("TlBankCodeMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(TlBankCode record) {
		return super.getSqlSession().update("TlBankCodeMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(TlBankCode record) {
		return super.getSqlSession().update("TlBankCodeMapper.updateByPrimaryKey", record);
	}
	
}
