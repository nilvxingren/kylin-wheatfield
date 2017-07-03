/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.rkylin.database.BaseDao;
import com.rkylin.wheatfield.dao.AccountInfoDao;
import com.rkylin.wheatfield.pojo.AccountInfo;
import com.rkylin.wheatfield.pojo.AccountInfoQuery;

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
	public List<AccountInfo> selectByConLike(AccountInfoQuery example) {
		return super.getSqlSession().selectList("AccountInfoMapper.selectByConLike", example);
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

	public List<AccountInfo> queryByUserIdAndPurpose(List<AccountInfoQuery> list) {
		return super.getSqlSession().selectList("AccountInfoMapper.queryByUserIdAndPurpose", list);
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
	
	/** 
	* @Description:查询对私卡/对公户的详细信息
	*
	* @param rootInstId 机构号
	* @param userId 用户id
	* @param accountPurpose 账户目的（结算卡）
	* @param status 状态（正常）
	* @param response   
	*/
	@Override
	public List<AccountInfo> selectAccountListForJsp(AccountInfoQuery example) {
		return super.getSqlSession().selectList("AccountInfoMapper.selectAccountListForJsp", example);
	}
	
	/** 
	* @Description:根据账户表id把相应数据状态改为：4验证失败
	*
	* @param accountId 账户表id
	* @param status 状态（验证失败4）
	* @return   
	*/
	@Override
	public int updateAccountInfoStatus(AccountInfoQuery query){
		return super.getSqlSession().update("AccountInfoMapper.updateAccountInfoStatus", query);
	}
	
}
