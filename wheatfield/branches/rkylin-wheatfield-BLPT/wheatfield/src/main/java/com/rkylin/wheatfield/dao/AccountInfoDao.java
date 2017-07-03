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
	
	int deleteByPrimaryKey(Integer accountId);
	
	int insert(AccountInfo record);
	
	int insertSelective(AccountInfo record);
	
	List<AccountInfo> selectByExample(AccountInfoQuery example);
	
	public List<AccountInfo> selectByConLike(AccountInfoQuery example);
	
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
	
	public List<AccountInfo> selectAccInfo(AccountInfoQuery example);
}
