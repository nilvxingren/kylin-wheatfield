/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.wheatfield.dao.FinanaceEntryHistoryDao;
import com.rkylin.wheatfield.pojo.FinanaceEntryHistory;
import com.rkylin.wheatfield.pojo.FinanaceEntryHistoryQuery;
import com.rkylin.database.BaseDao;

@Repository("finanaceEntryHistoryDao")
public class FinanaceEntryHistoryDaoImpl extends BaseDao implements FinanaceEntryHistoryDao {
	
	@Override
	public int countByExample(FinanaceEntryHistoryQuery example) {
		return super.getSqlSession().selectOne("FinanaceEntryHistoryMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(FinanaceEntryHistoryQuery example) {
		return super.getSqlSession().delete("FinanaceEntryHistoryMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("FinanaceEntryHistoryMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(FinanaceEntryHistory record) {
		return super.getSqlSession().insert("FinanaceEntryHistoryMapper.insert", record);
	}
	
	@Override
	public int insertSelective(FinanaceEntryHistory record) {
		return super.getSqlSession().insert("FinanaceEntryHistoryMapper.insertSelective", record);
	}
	
	@Override
	public List<FinanaceEntryHistory> selectByExample(FinanaceEntryHistoryQuery example) {
		return super.getSqlSession().selectList("FinanaceEntryHistoryMapper.selectByExample", example);
	}
	
	@Override
	public FinanaceEntryHistory selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("FinanaceEntryHistoryMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(FinanaceEntryHistory record) {
		return super.getSqlSession().update("FinanaceEntryHistoryMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(FinanaceEntryHistory record) {
		return super.getSqlSession().update("FinanaceEntryHistoryMapper.updateByPrimaryKey", record);
	}
	
}
