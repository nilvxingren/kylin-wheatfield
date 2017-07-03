/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.wheatfield.dao.SettleSplittingEntryDao;
import com.rkylin.wheatfield.pojo.SettleSplittingEntry;
import com.rkylin.wheatfield.pojo.SettleSplittingEntryQuery;
import com.rkylin.database.BaseDao;

@Repository("settleSplittingEntryDao")
public class SettleSplittingEntryDaoImpl extends BaseDao implements SettleSplittingEntryDao {
	
	@Override
	public int countByExample(SettleSplittingEntryQuery example) {
		return super.getSqlSession().selectOne("SettleSplittingEntryMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(SettleSplittingEntryQuery example) {
		return super.getSqlSession().delete("SettleSplittingEntryMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("SettleSplittingEntryMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(SettleSplittingEntry record) {
		return super.getSqlSession().insert("SettleSplittingEntryMapper.insert", record);
	}
	
	@Override
	public int insertSelective(SettleSplittingEntry record) {
		return super.getSqlSession().insert("SettleSplittingEntryMapper.insertSelective", record);
	}
	
	@Override
	public List<SettleSplittingEntry> selectByExample(SettleSplittingEntryQuery example) {
		return super.getSqlSession().selectList("SettleSplittingEntryMapper.selectByExample", example);
	}
	
	@Override
	public SettleSplittingEntry selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("SettleSplittingEntryMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(SettleSplittingEntry record) {
		return super.getSqlSession().update("SettleSplittingEntryMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(SettleSplittingEntry record) {
		return super.getSqlSession().update("SettleSplittingEntryMapper.updateByPrimaryKey", record);
	}
	
	@Override
	public int batchUpdate( List<?> list){
		return super.batchUpdate("SettleSplittingEntryMapper.updateByOrderNo", list);
	}	
}
