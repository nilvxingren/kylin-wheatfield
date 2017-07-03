/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.wheatfield.dao.AccountAgreementDao;
import com.rkylin.wheatfield.pojo.AccountAgreement;
import com.rkylin.wheatfield.pojo.AccountAgreementQuery;
import com.rkylin.database.BaseDao;

@Repository("accountAgreementDao")
public class AccountAgreementDaoImpl extends BaseDao implements AccountAgreementDao {
	
	@Override
	public int countByExample(AccountAgreementQuery example) {
		return super.getSqlSession().selectOne("AccountAgreementMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(AccountAgreementQuery example) {
		return super.getSqlSession().delete("AccountAgreementMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("AccountAgreementMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(AccountAgreement record) {
		return super.getSqlSession().insert("AccountAgreementMapper.insert", record);
	}
	
	@Override
	public int insertSelective(AccountAgreement record) {
		return super.getSqlSession().insert("AccountAgreementMapper.insertSelective", record);
	}
	
	@Override
	public List<AccountAgreement> selectByExample(AccountAgreementQuery example) {
		return super.getSqlSession().selectList("AccountAgreementMapper.selectByExample", example);
	}
	
	@Override
	public AccountAgreement selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("AccountAgreementMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(AccountAgreement record) {
		return super.getSqlSession().update("AccountAgreementMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(AccountAgreement record) {
		return super.getSqlSession().update("AccountAgreementMapper.updateByPrimaryKey", record);
	}
	
}
