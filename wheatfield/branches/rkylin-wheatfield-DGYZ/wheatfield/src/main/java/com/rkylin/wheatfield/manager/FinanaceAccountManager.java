/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager;

import java.util.List;

import com.rkylin.wheatfield.pojo.FinanaceAccount;
import com.rkylin.wheatfield.pojo.FinanaceAccountQuery;

public interface FinanaceAccountManager {
	void saveFinanaceAccount(FinanaceAccount finanaceAccount);

	FinanaceAccount findFinanaceAccountById(Long id);
	
	List<FinanaceAccount> queryList(FinanaceAccountQuery query);
	
	public List<FinanaceAccount> selectByFinAccountId(String[] finAccountIds);
	
	void deleteFinanaceAccountById(Long id);
	
	void deleteFinanaceAccount(FinanaceAccountQuery query);

	void updateFinanaceAccount(FinanaceAccount finanaceAccount);
}
