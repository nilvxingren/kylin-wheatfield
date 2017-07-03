/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.wheatfield.dao.OpenBankCodeDao;
import com.rkylin.wheatfield.pojo.OpenBankCode;
import com.rkylin.wheatfield.pojo.OpenBankCodeQuery;
import com.rkylin.database.BaseDao;

@Repository("openBankCodeDao")
public class OpenBankCodeDaoImpl extends BaseDao implements OpenBankCodeDao {
	
	@Override
	public int countByExample(OpenBankCodeQuery example) {
		return super.getSqlSession().selectOne("OpenBankCodeMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(OpenBankCodeQuery example) {
		return super.getSqlSession().delete("OpenBankCodeMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("OpenBankCodeMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(OpenBankCode record) {
		return super.getSqlSession().insert("OpenBankCodeMapper.insert", record);
	}
	
	@Override
	public int insertSelective(OpenBankCode record) {
		return super.getSqlSession().insert("OpenBankCodeMapper.insertSelective", record);
	}
	
	@Override
	public List<OpenBankCode> selectByExample(OpenBankCodeQuery example) {
		return super.getSqlSession().selectList("OpenBankCodeMapper.selectByExample", example);
	}
	@Override
	public List<OpenBankCode> selectByCode(OpenBankCodeQuery example) {
		return super.getSqlSession().selectList("OpenBankCodeMapper.selectByCode", example);
	}
	@Override
	public OpenBankCode selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("OpenBankCodeMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(OpenBankCode record) {
		return super.getSqlSession().update("OpenBankCodeMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(OpenBankCode record) {
		return super.getSqlSession().update("OpenBankCodeMapper.updateByPrimaryKey", record);
	}
	
}
