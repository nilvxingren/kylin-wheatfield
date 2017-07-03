/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.wheatfield.dao.FinanaceAccountAuthDao;
import com.rkylin.wheatfield.pojo.FinanaceAccountAuth;
import com.rkylin.wheatfield.pojo.FinanaceAccountAuthQuery;
import com.rkylin.database.BaseDao;

@Repository("finanaceAccountAuthDao")
public class FinanaceAccountAuthDaoImpl extends BaseDao implements FinanaceAccountAuthDao {
	
	@Override
	public int countByExample(FinanaceAccountAuthQuery example) {
		return super.getSqlSession().selectOne("FinanaceAccountAuthMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(FinanaceAccountAuthQuery example) {
		return super.getSqlSession().delete("FinanaceAccountAuthMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("FinanaceAccountAuthMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(FinanaceAccountAuth record) {
		return super.getSqlSession().insert("FinanaceAccountAuthMapper.insert", record);
	}
	
	@Override
	public int insertSelective(FinanaceAccountAuth record) {
		return super.getSqlSession().insert("FinanaceAccountAuthMapper.insertSelective", record);
	}
	
	@Override
	public List<FinanaceAccountAuth> selectByExample(FinanaceAccountAuthQuery example) {
		return super.getSqlSession().selectList("FinanaceAccountAuthMapper.selectByExample", example);
	}
	
	@Override
	public FinanaceAccountAuth selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("FinanaceAccountAuthMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(FinanaceAccountAuth record) {
		return super.getSqlSession().update("FinanaceAccountAuthMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(FinanaceAccountAuth record) {
		return super.getSqlSession().update("FinanaceAccountAuthMapper.updateByPrimaryKey", record);
	}
	
}
