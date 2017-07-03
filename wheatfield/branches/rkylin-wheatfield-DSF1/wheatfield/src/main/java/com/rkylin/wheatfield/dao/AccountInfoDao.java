/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao;

import java.util.List;
import java.util.Map;

import com.rkylin.wheatfield.pojo.AccountInfo;
import com.rkylin.wheatfield.pojo.AccountInfoQuery;

public interface AccountInfoDao {
	int countByExample(AccountInfoQuery example);
	
	int deleteByExample(AccountInfoQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(AccountInfo record);
	
	int insertSelective(AccountInfo record);
	
	List<AccountInfo> selectByExample(AccountInfoQuery example);
	
	List<AccountInfo> selectViewByUserIdAndPurpose(AccountInfoQuery example);
	
	List<AccountInfo> getAccountBankCardList(AccountInfoQuery query);
	
	AccountInfo selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(AccountInfo record);
	
	int updateByPrimaryKey(AccountInfo record);

	List<AccountInfo> selectByNumAndConstId(AccountInfoQuery example);

	int updateByOnecent(Map map);

	List<AccountInfo> queryoneCent(AccountInfoQuery query);

	int batchUpdate(List<AccountInfo> list);

	List<AccountInfo> selectByExamplePlus(AccountInfoQuery accountInfoQuery);
	
	public List<AccountInfo> queryByUserIdAndPurpose(List<AccountInfoQuery> list);
}
