/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.database.BaseDao;
import com.rkylin.wheatfield.dao.FinanaceEntryDao;
import com.rkylin.wheatfield.pojo.FinanaceEntry;
import com.rkylin.wheatfield.pojo.FinanaceEntryDto;
import com.rkylin.wheatfield.pojo.FinanaceEntryQuery;

@Repository("finanaceEntryDao")
public class FinanaceEntryDaoImpl extends BaseDao implements FinanaceEntryDao {
	
	@Override
	public int countByExample(FinanaceEntryQuery example) {
		return super.getSqlSession().selectOne("FinanaceEntryMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(FinanaceEntryQuery example) {
		return super.getSqlSession().delete("FinanaceEntryMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("FinanaceEntryMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(FinanaceEntry record) {
		return super.getSqlSession().insert("FinanaceEntryMapper.insert", record);
	}
	
	@Override
	public int insertSelective(FinanaceEntry record) {
		return super.getSqlSession().insert("FinanaceEntryMapper.insertSelective", record);
	}
	
	@Override
	public List<FinanaceEntry> selectByExample(FinanaceEntryQuery example) {
		return super.getSqlSession().selectList("FinanaceEntryMapper.selectByExample", example);
	}
	
	@Override
	public FinanaceEntry selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("FinanaceEntryMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(FinanaceEntry record) {
		return super.getSqlSession().update("FinanaceEntryMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(FinanaceEntry record) {
		return super.getSqlSession().update("FinanaceEntryMapper.updateByPrimaryKey", record);
	}

	@Override
	public void insertSelectiveBatch(List<FinanaceEntry> list) {
		super.getSqlSession().insert("FinanaceEntryMapper.insertSelectiveBatch", list);
	}

	@Override
	public List<FinanaceEntry> selectByFinAccountId(FinanaceEntryQuery example) {
		return super.getSqlSession().selectList("FinanaceEntryMapper.selectByFinAccountId", example);
	}
	
	@Override
	public List<FinanaceEntryDto> queryFinDtoList(FinanaceEntryQuery example) {
		return super.getSqlSession().selectList("FinanaceEntryMapper.queryFinDtoList", example);
	}
	
	@Override
	public List<FinanaceEntryDto> selectByReferid (String referid){
		return super.getSqlSession().selectList("FinanaceEntryMapper.selectListByReferid", referid);
	}
	
}
