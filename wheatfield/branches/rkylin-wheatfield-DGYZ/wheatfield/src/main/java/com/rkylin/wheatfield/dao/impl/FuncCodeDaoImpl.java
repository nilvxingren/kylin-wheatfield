/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.wheatfield.dao.FuncCodeDao;
import com.rkylin.wheatfield.pojo.FuncCode;
import com.rkylin.wheatfield.pojo.FuncCodeQuery;
import com.rkylin.database.BaseDao;

@Repository("funcCodeDao")
public class FuncCodeDaoImpl extends BaseDao implements FuncCodeDao {
	
	@Override
	public int countByExample(FuncCodeQuery example) {
		return super.getSqlSession().selectOne("FuncCodeMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(FuncCodeQuery example) {
		return super.getSqlSession().delete("FuncCodeMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("FuncCodeMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(FuncCode record) {
		return super.getSqlSession().insert("FuncCodeMapper.insert", record);
	}
	
	@Override
	public int insertSelective(FuncCode record) {
		return super.getSqlSession().insert("FuncCodeMapper.insertSelective", record);
	}
	
	@Override
	public List<FuncCode> selectByExample(FuncCodeQuery example) {
		return super.getSqlSession().selectList("FuncCodeMapper.selectByExample", example);
	}
	
	@Override
	public FuncCode selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("FuncCodeMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(FuncCode record) {
		return super.getSqlSession().update("FuncCodeMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(FuncCode record) {
		return super.getSqlSession().update("FuncCodeMapper.updateByPrimaryKey", record);
	}
	
}
