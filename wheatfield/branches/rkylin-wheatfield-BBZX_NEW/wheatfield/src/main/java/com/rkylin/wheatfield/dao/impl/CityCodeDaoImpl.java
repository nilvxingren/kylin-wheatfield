/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.wheatfield.dao.CityCodeDao;
import com.rkylin.wheatfield.pojo.CityCode;
import com.rkylin.wheatfield.pojo.CityCodeQuery;
import com.rkylin.database.BaseDao;

@Repository("cityCodeDao")
public class CityCodeDaoImpl extends BaseDao implements CityCodeDao {
	
	@Override
	public int countByExample(CityCodeQuery example) {
		return super.getSqlSession().selectOne("CityCodeMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(CityCodeQuery example) {
		return super.getSqlSession().delete("CityCodeMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("CityCodeMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(CityCode record) {
		return super.getSqlSession().insert("CityCodeMapper.insert", record);
	}
	
	@Override
	public int insertSelective(CityCode record) {
		return super.getSqlSession().insert("CityCodeMapper.insertSelective", record);
	}
	
	@Override
	public List<CityCode> selectByExample(CityCodeQuery example) {
		return super.getSqlSession().selectList("CityCodeMapper.selectByExample", example);
	}
	@Override
	public List<CityCode> selectByCode(CityCodeQuery example) {
		return super.getSqlSession().selectList("CityCodeMapper.selectByCode", example);
	}
	@Override
	public CityCode selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("CityCodeMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(CityCode record) {
		return super.getSqlSession().update("CityCodeMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(CityCode record) {
		return super.getSqlSession().update("CityCodeMapper.updateByPrimaryKey", record);
	}
	
}
