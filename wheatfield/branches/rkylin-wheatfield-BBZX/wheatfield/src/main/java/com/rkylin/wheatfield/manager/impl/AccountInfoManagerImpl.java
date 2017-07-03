/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.rkylin.wheatfield.dao.AccountInfoDao;
import com.rkylin.wheatfield.manager.AccountInfoManager;
import com.rkylin.wheatfield.pojo.AccountInfo;
import com.rkylin.wheatfield.pojo.AccountInfoQuery;

@Component("accountInfoManager")
public class AccountInfoManagerImpl implements AccountInfoManager {
	
	@Autowired
	@Qualifier("accountInfoDao")
	private AccountInfoDao accountInfoDao;
	
	@Override
	public void saveAccountInfo(AccountInfo accountInfo) {
		if (accountInfo.getAccountId() == null) {
			accountInfoDao.insertSelective(accountInfo);
		} else {
			accountInfoDao.updateByPrimaryKeySelective(accountInfo);
		}
	}
	
	@Override
	public AccountInfo findAccountInfoById(Long id) {
		return accountInfoDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<AccountInfo> queryList(AccountInfoQuery query) {
		return accountInfoDao.selectByExample(query);
	}
	@Override
	public List<AccountInfo> queryListByNumAndConstId(AccountInfoQuery query) {
		return accountInfoDao.selectByNumAndConstId(query);
	}
	
	@Override
	public void deleteAccountInfoById(Long id) {
		accountInfoDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteAccountInfo(AccountInfoQuery query) {
		accountInfoDao.deleteByExample(query);
	}

	@Override
	public List<AccountInfo> queryViewByUserIdAndPurpose(AccountInfoQuery query) {
		// TODO Auto-generated method stub
		return accountInfoDao.selectViewByUserIdAndPurpose(query);
	}

	@Override
	public int updateByPrimaryKey(AccountInfo record) {
		return accountInfoDao.updateByPrimaryKey(record);
	}
	public int updateByPrimaryKeySelective(AccountInfo  accountInfo){
		return accountInfoDao.updateByPrimaryKeySelective(accountInfo);
	}

	@Override
	public int updateByOnecent(Map map) {
		return accountInfoDao.updateByOnecent(map);
	}

	@Override
	public List<AccountInfo> queryoneCent(AccountInfoQuery query) {
		return accountInfoDao.queryoneCent(query);
	}
	@Override
	public int batchUpdate(List<AccountInfo> list) {
		return accountInfoDao.batchUpdate(list);
	}

	@Override
	public List<AccountInfo> getAccountBankCardList(AccountInfoQuery query) {
		return accountInfoDao.getAccountBankCardList(query);
	}

	@Override
	public List<AccountInfo> queryListPlus(AccountInfoQuery accountInfoQuery) {
		return accountInfoDao.selectByExamplePlus(accountInfoQuery);
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
	public List<AccountInfo> selectAccountListForJsp(AccountInfoQuery query) {
		return accountInfoDao.selectAccountListForJsp(query);
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
		return accountInfoDao.updateAccountInfoStatus(query);
	}
}

