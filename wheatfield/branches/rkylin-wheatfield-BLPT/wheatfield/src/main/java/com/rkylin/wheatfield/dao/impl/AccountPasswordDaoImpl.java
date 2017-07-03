/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.wheatfield.dao.AccountPasswordDao;
import com.rkylin.wheatfield.pojo.AccountPassword;
import com.rkylin.wheatfield.pojo.AccountPasswordQuery;
import com.rkylin.database.BaseDao;

@Repository("accountPasswordDao")
public class AccountPasswordDaoImpl extends BaseDao implements AccountPasswordDao {
	
	@Override
	public int countByExample(AccountPasswordQuery example) {
		return super.getSqlSession().selectOne("AccountPasswordMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(AccountPasswordQuery example) {
		return super.getSqlSession().delete("AccountPasswordMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("AccountPasswordMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(AccountPassword record) {
		return super.getSqlSession().insert("AccountPasswordMapper.insert", record);
	}
	
	@Override
	public int insertSelective(AccountPassword record) {
		return super.getSqlSession().insert("AccountPasswordMapper.insertSelective", record);
	}
	
	@Override
	public List<AccountPassword> selectByExample(AccountPasswordQuery example) {
		return super.getSqlSession().selectList("AccountPasswordMapper.selectByExample", example);
	}
	
	@Override
	public AccountPassword selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("AccountPasswordMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(AccountPassword record) {
		return super.getSqlSession().update("AccountPasswordMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(AccountPassword record) {
		return super.getSqlSession().update("AccountPasswordMapper.updateByPrimaryKey", record);
	}
	
}
