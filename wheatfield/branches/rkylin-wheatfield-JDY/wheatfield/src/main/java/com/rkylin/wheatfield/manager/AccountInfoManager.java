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
	
	void deleteAccountInfoById(Integer accountId);
	
	void deleteAccountInfo(AccountInfoQuery query);

	List<AccountInfo> queryListByNumAndConstId(AccountInfoQuery query);
	
	 int updateByPrimaryKey(AccountInfo record);
	 
	 
	 int updateByPrimaryKeySelective(AccountInfo record);
	 
	 int updateByOnecent(Map map);
	 
	 List<AccountInfo> queryoneCent(AccountInfoQuery query);
	 
	 List<AccountInfo> getAccountBankCardList(AccountInfoQuery query);
	 
	 int batchUpdate(List<AccountInfo> list);

	List<AccountInfo> queryListPlus(AccountInfoQuery accountInfoQuery);
	
	/** 
	* @Description:查询对私卡/对公户的详细信息
	*
	* @param rootInstId 机构号
	* @param userId 用户id
	* @param accountPurpose 账户目的（结算卡）
	* @param status 状态（正常）
	* @param response   
	*/
	List<AccountInfo> selectAccountListForJsp(AccountInfoQuery query);
	
	/** 
	* @Description:根据账户表id把相应数据状态改为：4验证失败
	*
	* @param accountId 账户表id
	* @param status 状态（验证失败4）
	* @return   
	*/
	int updateAccountInfoStatus(AccountInfoQuery query);
	
//	public List<AccountInfo> selectAccInfo(AccountInfoQuery example);
}
