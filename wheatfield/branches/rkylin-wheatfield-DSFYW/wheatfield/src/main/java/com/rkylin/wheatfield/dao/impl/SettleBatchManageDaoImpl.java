/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.wheatfield.dao.SettleBatchManageDao;
import com.rkylin.wheatfield.pojo.SettleBatchManage;
import com.rkylin.wheatfield.pojo.SettleBatchManageQuery;
import com.rkylin.database.BaseDao;

@Repository("settleBatchManageDao")
public class SettleBatchManageDaoImpl extends BaseDao implements SettleBatchManageDao {
	
	@Override
	public int countByExample(SettleBatchManageQuery example) {
		return super.getSqlSession().selectOne("SettleBatchManageMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(SettleBatchManageQuery example) {
		return super.getSqlSession().delete("SettleBatchManageMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("SettleBatchManageMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(SettleBatchManage record) {
		return super.getSqlSession().insert("SettleBatchManageMapper.insert", record);
	}
	
	@Override
	public int insertSelective(SettleBatchManage record) {
		return super.getSqlSession().insert("SettleBatchManageMapper.insertSelective", record);
	}
	
	@Override
	public List<SettleBatchManage> selectByExample(SettleBatchManageQuery example) {
		return super.getSqlSession().selectList("SettleBatchManageMapper.selectByExample", example);
	}
	
	@Override
	public SettleBatchManage selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("SettleBatchManageMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(SettleBatchManage record) {
		return super.getSqlSession().update("SettleBatchManageMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(SettleBatchManage record) {
		return super.getSqlSession().update("SettleBatchManageMapper.updateByPrimaryKey", record);
	}
	
}
