/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.wheatfield.dao.ManageHistoryDao;
import com.rkylin.wheatfield.pojo.ManageHistory;
import com.rkylin.wheatfield.pojo.ManageHistoryQuery;
import com.rkylin.database.BaseDao;

@Repository("manageHistoryDao")
public class ManageHistoryDaoImpl extends BaseDao implements ManageHistoryDao {
	
	@Override
	public int countByExample(ManageHistoryQuery example) {
		return super.getSqlSession().selectOne("ManageHistoryMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(ManageHistoryQuery example) {
		return super.getSqlSession().delete("ManageHistoryMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("ManageHistoryMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(ManageHistory record) {
		return super.getSqlSession().insert("ManageHistoryMapper.insert", record);
	}
	
	@Override
	public int insertSelective(ManageHistory record) {
		return super.getSqlSession().insert("ManageHistoryMapper.insertSelective", record);
	}
	
	@Override
	public List<ManageHistory> selectByExample(ManageHistoryQuery example) {
		return super.getSqlSession().selectList("ManageHistoryMapper.selectByExample", example);
	}
	
	@Override
	public ManageHistory selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("ManageHistoryMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(ManageHistory record) {
		return super.getSqlSession().update("ManageHistoryMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(ManageHistory record) {
		return super.getSqlSession().update("ManageHistoryMapper.updateByPrimaryKey", record);
	}
	
}
