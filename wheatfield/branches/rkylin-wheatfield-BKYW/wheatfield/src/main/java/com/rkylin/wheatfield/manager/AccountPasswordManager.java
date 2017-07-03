/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager;

import java.util.List;

import com.rkylin.wheatfield.pojo.AccountPassword;
import com.rkylin.wheatfield.pojo.AccountPasswordQuery;

public interface AccountPasswordManager {
	void saveAccountPassword(AccountPassword accountPassword);

	AccountPassword findAccountPasswordById(Long id);
	
	List<AccountPassword> queryList(AccountPasswordQuery query);
	
	void deleteAccountPasswordById(Long id);
	
	void deleteAccountPassword(AccountPasswordQuery query);
}
