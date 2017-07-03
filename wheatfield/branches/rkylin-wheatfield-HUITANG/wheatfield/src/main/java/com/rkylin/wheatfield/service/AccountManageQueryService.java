package com.rkylin.wheatfield.service;

import java.util.List;

import com.rkylin.wheatfield.pojo.AccountInfo;
import com.rkylin.wheatfield.pojo.AccountInfoQuery;
import com.rkylin.wheatfield.pojo.FinanaceCompany;
import com.rkylin.wheatfield.pojo.FinanaceCompanyQuery;

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
}
