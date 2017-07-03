/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.wheatfield.dao.SettleTransTabDao;
import com.rkylin.wheatfield.pojo.SettleTransTab;
import com.rkylin.wheatfield.pojo.SettleTransTabQuery;
import com.rkylin.database.BaseDao;

@Repository("settleTransTabDao")
public class SettleTransTabDaoImpl extends BaseDao implements SettleTransTabDao {
	
	@Override
	public int countByExample(SettleTransTabQuery example) {
		return super.getSqlSession().selectOne("SettleTransTabMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(SettleTransTabQuery example) {
		return super.getSqlSession().delete("SettleTransTabMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("SettleTransTabMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(SettleTransTab record) {
		return super.getSqlSession().insert("SettleTransTabMapper.insert", record);
	}
	
	@Override
	public int insertSelective(SettleTransTab record) {
		return super.getSqlSession().insert("SettleTransTabMapper.insertSelective", record);
	}
	
	@Override
	public List<SettleTransTab> selectByExample(SettleTransTabQuery example) {
		return super.getSqlSession().selectList("SettleTransTabMapper.selectByExample", example);
	}
	
	@Override
	public SettleTransTab selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("SettleTransTabMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(SettleTransTab record) {
		return super.getSqlSession().update("SettleTransTabMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(SettleTransTab record) {
		return super.getSqlSession().update("SettleTransTabMapper.updateByPrimaryKey", record);
	}

	@Override
	public void insertSelectiveBatch(List<SettleTransTab> list) {
		super.getSqlSession().insert("SettleTransTabMapper.insertSelectiveBatch", list);
	}
	
}
