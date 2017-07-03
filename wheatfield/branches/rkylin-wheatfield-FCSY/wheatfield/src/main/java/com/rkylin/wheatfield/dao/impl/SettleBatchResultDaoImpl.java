/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.wheatfield.dao.SettleBatchResultDao;
import com.rkylin.wheatfield.pojo.SettleBatchResult;
import com.rkylin.wheatfield.pojo.SettleBatchResultQuery;
import com.rkylin.database.BaseDao;

@Repository("settleBatchResultDao")
public class SettleBatchResultDaoImpl extends BaseDao implements SettleBatchResultDao {
	
	@Override
	public int countByExample(SettleBatchResultQuery example) {
		return super.getSqlSession().selectOne("SettleBatchResultMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(SettleBatchResultQuery example) {
		return super.getSqlSession().delete("SettleBatchResultMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("SettleBatchResultMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(SettleBatchResult record) {
		return super.getSqlSession().insert("SettleBatchResultMapper.insert", record);
	}
	
	@Override
	public int insertSelective(SettleBatchResult record) {
		return super.getSqlSession().insert("SettleBatchResultMapper.insertSelective", record);
	}
	
	@Override
	public List<SettleBatchResult> selectByExample(SettleBatchResultQuery example) {
		return super.getSqlSession().selectList("SettleBatchResultMapper.selectByExample", example);
	}
	
	@Override
	public SettleBatchResult selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("SettleBatchResultMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(SettleBatchResult record) {
		return super.getSqlSession().update("SettleBatchResultMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(SettleBatchResult record) {
		return super.getSqlSession().update("SettleBatchResultMapper.updateByPrimaryKey", record);
	}
	
}
