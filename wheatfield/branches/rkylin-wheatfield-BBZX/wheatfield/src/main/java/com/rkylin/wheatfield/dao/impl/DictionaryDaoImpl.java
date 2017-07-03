/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.wheatfield.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.wheatfield.dao.DictionaryDao;
import com.rkylin.wheatfield.pojo.Dictionary;
import com.rkylin.wheatfield.pojo.DictionaryQuery;
import com.rkylin.database.BaseDao;

@Repository("dictionaryDao")
public class DictionaryDaoImpl extends BaseDao implements DictionaryDao {
	
	@Override
	public int countByExample(DictionaryQuery example) {
		return super.getSqlSession().selectOne("DictionaryMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(DictionaryQuery example) {
		return super.getSqlSession().delete("DictionaryMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("DictionaryMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(Dictionary record) {
		return super.getSqlSession().insert("DictionaryMapper.insert", record);
	}
	
	@Override
	public int insertSelective(Dictionary record) {
		return super.getSqlSession().insert("DictionaryMapper.insertSelective", record);
	}
	
	@Override
	public List<Dictionary> selectByExample(DictionaryQuery example) {
		return super.getSqlSession().selectList("DictionaryMapper.selectByExample", example);
	}
	
	@Override
	public Dictionary selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("DictionaryMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(Dictionary record) {
		return super.getSqlSession().update("DictionaryMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(Dictionary record) {
		return super.getSqlSession().update("DictionaryMapper.updateByPrimaryKey", record);
	}
	
}
