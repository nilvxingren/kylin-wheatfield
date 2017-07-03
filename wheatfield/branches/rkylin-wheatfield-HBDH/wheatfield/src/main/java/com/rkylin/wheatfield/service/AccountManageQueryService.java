package com.rkylin.wheatfield.service;

import java.util.List;

import com.rkylin.wheatfield.pojo.AccountInfo;
import com.rkylin.wheatfield.pojo.AccountInfoQuery;
import com.rkylin.wheatfield.pojo.FinanaceCompany;
import com.rkylin.wheatfield.pojo.FinanaceCompanyQuery;
import com.rkylin.wheatfield.response.AccountInfoGetResponse;

/**
 * 账户管理查询相关接口
 * @author rzl
 *
 */
public interface AccountManageQueryService {

	/**
	 * 获取银行卡信息
	 * @param user
	 * @param objOrList
	 * @return
	 */
	List<AccountInfo> getAccountBankCardList(AccountInfoQuery accountInfoQuery);
	/**
	 * 获取企业信息
	 * @param user
	 * @param objOrList
	 * @return
	 */
	List<FinanaceCompany> getAccountCompanyList(FinanaceCompanyQuery finanaceCompanyQuery);
	
	/**
	 * 查询流水信息
	*
	* @param rootinstcd 管理机构代码（查询类型为1时，必须入参）
	* @param userid 用户id（查询类型为1时，必须入参）
	* @param productid 产品号
	* @param createdtimefrom 记录创建开始时间
	* @param createdtimeto 记录创建结束时间
	* @param requestid 交易记录编码（查询类型为2时，必须入参）
	* @param querytype 查询类型（1:根据账户查询 2:根据交易记录查询）
	* @return
	 */
	AccountInfoGetResponse queryFinanaceentryList(String rootinstcd, String userid,
			String productid, String createdtimefrom, String createdtimeto,String requestid,String querytype);
}
