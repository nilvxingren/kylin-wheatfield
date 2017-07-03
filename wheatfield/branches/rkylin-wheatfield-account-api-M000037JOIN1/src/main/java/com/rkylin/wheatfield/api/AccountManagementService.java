package com.rkylin.wheatfield.api;

import java.util.List;
import java.util.Map;

import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.pojo.AccountInfo;
import com.rkylin.wheatfield.pojo.AccountInfoQuery;



/**
 * Created by sunguoxing on 15/12/1.
 */
public interface AccountManagementService {
	
	
	/** 
	* @Description:查询对私卡/对公户的详细信息
	*
	* @param rootInstId 机构号
	* @param userId 用户id
	* @param accountPurpose 账户目的（结算卡）
	* @param status 状态（正常）
	* @param response   
	*/
	public List<AccountInfo> selectAccountListForJsp(AccountInfoQuery query);
	
	/** 
	* @Description:根据账户表id把相应数据状态改为：4验证失败
	*
	* @param accountId 账户表id
	* @param status 状态（验证失败4）
	* @return   
	*/
	public int updateAccountInfoStatus(AccountInfoQuery query);
	
	/**
	 * 修改对公卡信息
	 * Discription:
	 * @param query
	 * @return int
	 * @author Achilles
	 * @since 2017年4月13日
	 */
	public CommonResponse updatePublicAccountInfo(Map<String, String[]> paramMap);
	
}