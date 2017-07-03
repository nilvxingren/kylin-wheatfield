/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.wheatfield.dao.FinanaceEntryLastDao;
import com.rkylin.wheatfield.pojo.FinanaceEntryLast;
import com.rkylin.wheatfield.pojo.FinanaceEntryLastQuery;
import com.rkylin.database.BaseDao;

@Repository("finanaceEntryLastDao")
public class FinanaceEntryLastDaoImpl extends BaseDao implements FinanaceEntryLastDao {
	
	@Override
	public int countByExample(FinanaceEntryLastQuery example) {
		return super.getSqlSession().selectOne("FinanaceEntryLastMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(FinanaceEntryLastQuery example) {
		return super.getSqlSession().delete("FinanaceEntryLastMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("FinanaceEntryLastMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(FinanaceEntryLast record) {
		return super.getSqlSession().insert("FinanaceEntryLastMapper.insert", record);
	}
	
	@Override
	public int insertSelective(FinanaceEntryLast record) {
		return super.getSqlSession().insert("FinanaceEntryLastMapper.insertSelective", record);
	}
	
	@Override
	public List<FinanaceEntryLast> selectByExample(FinanaceEntryLastQuery example) {
		return super.getSqlSession().selectList("FinanaceEntryLastMapper.selectByExample", example);
	}
	
	@Override
	public FinanaceEntryLast selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("FinanaceEntryLastMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(FinanaceEntryLast record) {
		return super.getSqlSession().update("FinanaceEntryLastMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(FinanaceEntryLast record) {
		return super.getSqlSession().update("FinanaceEntryLastMapper.updateByPrimaryKey", record);
	}
	@Override
	public void insertSelectiveBatch(List<FinanaceEntryLast> list) {
		super.getSqlSession().insert("FinanaceEntryMapper.insertSelectiveBatch", list);
	}
}
