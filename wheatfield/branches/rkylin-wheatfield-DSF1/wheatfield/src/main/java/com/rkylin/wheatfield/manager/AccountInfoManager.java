/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager;

import java.util.List;
import java.util.Map;

import com.rkylin.wheatfield.pojo.AccountInfo;
import com.rkylin.wheatfield.pojo.AccountInfoQuery;

public interface AccountInfoManager {
	void saveAccountInfo(AccountInfo accountInfo);

	AccountInfo findAccountInfoById(Long id);
	
	List<AccountInfo> queryList(AccountInfoQuery query);
	
	List<AccountInfo> queryViewByUserIdAndPurpose(AccountInfoQuery query);
	
	void deleteAccountInfoById(Long id);
	
	void deleteAccountInfo(AccountInfoQuery query);

	List<AccountInfo> queryListByNumAndConstId(AccountInfoQuery query);
	
	 int updateByPrimaryKey(AccountInfo record);
	 
	 
	 int updateByPrimaryKeySelective(AccountInfo record);
	 
	 int updateByOnecent(Map map);
	 
	 List<AccountInfo> queryoneCent(AccountInfoQuery query);
	 
	 List<AccountInfo> getAccountBankCardList(AccountInfoQuery query);
	 
	 int batchUpdate(List<AccountInfo> list);

	List<AccountInfo> queryListPlus(AccountInfoQuery accountInfoQuery);
}
