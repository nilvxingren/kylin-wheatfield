/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.wheatfield.dao.FinanaceAccountDao;
import com.rkylin.wheatfield.pojo.FinanaceAccount;
import com.rkylin.wheatfield.pojo.FinanaceAccountQuery;
import com.rkylin.database.BaseDao;

@Repository("finanaceAccountDao")
public class FinanaceAccountDaoImpl extends BaseDao implements FinanaceAccountDao {
	
	@Override
	public int countByExample(FinanaceAccountQuery example) {
		return super.getSqlSession().selectOne("FinanaceAccountMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(FinanaceAccountQuery example) {
		return super.getSqlSession().delete("FinanaceAccountMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("FinanaceAccountMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(FinanaceAccount record) {
		return super.getSqlSession().insert("FinanaceAccountMapper.insert", record);
	}
	
	@Override
	public int insertSelective(FinanaceAccount record) {
		return super.getSqlSession().insert("FinanaceAccountMapper.insertSelective", record);
	}
	
	@Override
	public List<FinanaceAccount> selectByExample(FinanaceAccountQuery example) {
		return super.getSqlSession().selectList("FinanaceAccountMapper.selectByExample", example);
	}
	
	@Override
	public List<FinanaceAccount> selectByFinAccountId(String[] finAccountIds) {
		return super.getSqlSession().selectList("FinanaceAccountMapper.selectByFinAccountId", finAccountIds);
	}
	
	@Override
	public FinanaceAccount selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("FinanaceAccountMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(FinanaceAccount record) {
		return super.getSqlSession().update("FinanaceAccountMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(FinanaceAccount record) {
		return super.getSqlSession().update("FinanaceAccountMapper.updateByPrimaryKey", record);
	}
	
}
