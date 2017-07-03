/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.rkylin.wheatfield.dao.AccountInfoDao;
import com.rkylin.wheatfield.pojo.AccountInfo;
import com.rkylin.wheatfield.pojo.AccountInfoQuery;
import com.rkylin.database.BaseDao;

@Repository("accountInfoDao")
public class AccountInfoDaoImpl extends BaseDao implements AccountInfoDao {
	
	@Override
	public int countByExample(AccountInfoQuery example) {
		return super.getSqlSession().selectOne("AccountInfoMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(AccountInfoQuery example) {
		return super.getSqlSession().delete("AccountInfoMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("AccountInfoMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(AccountInfo record) {
		return super.getSqlSession().insert("AccountInfoMapper.insert", record);
	}
	
	@Override
	public int insertSelective(AccountInfo record) {
		return super.getSqlSession().insert("AccountInfoMapper.insertSelective", record);
	}
	
	@Override
	public List<AccountInfo> selectByExample(AccountInfoQuery example) {
		return super.getSqlSession().selectList("AccountInfoMapper.selectByExample", example);
	}
	@Override
	public List<AccountInfo> selectByNumAndConstId(AccountInfoQuery example) {
		return super.getSqlSession().selectList("AccountInfoMapper.selectByNumAndConstId", example);
	}
	@Override
	public AccountInfo selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("AccountInfoMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(AccountInfo record) {
		return super.getSqlSession().update("AccountInfoMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(AccountInfo record) {
		return super.getSqlSession().update("AccountInfoMapper.updateByPrimaryKey", record);
	}

	@Override
	public List<AccountInfo> selectViewByUserIdAndPurpose(
			AccountInfoQuery example) {
		return super.getSqlSession().selectList("AccountInfoMapper.selectViewByUserIdAndPurpose", example);
	}

	@Override
	public int updateByOnecent(Map map) {
		return super.getSqlSession().update("AccountInfoMapper.updateByOnecent",map);
	}

	@Override
	public List<AccountInfo> queryoneCent(AccountInfoQuery query) {
		return super.getSqlSession().selectList("AccountInfoMapper.queryoneCent", query);
	}
	@Override
	public int batchUpdate(List<AccountInfo> list) {
		return super.batchUpdate("AccountInfoMapper.updateByPrimaryKeySelective", list);
	}

	@Override
	public List<AccountInfo> getAccountBankCardList(AccountInfoQuery query) {
		return super.getSqlSession().selectList("AccountInfoMapper.selectBankCard", query);
	}

	@Override
	public List<AccountInfo> selectByExamplePlus(AccountInfoQuery accountInfoQuery) {
		return super.getSqlSession().selectList("AccountInfoMapper.selectByExamplePlus", accountInfoQuery);
	}
	
}
