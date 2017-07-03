/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager;

import java.util.List;

import com.rkylin.wheatfield.pojo.AccountAgreement;
import com.rkylin.wheatfield.pojo.AccountAgreementQuery;

public interface AccountAgreementManager {
	void saveAccountAgreement(AccountAgreement accountAgreement);

	AccountAgreement findAccountAgreementById(Long id);
	
	List<AccountAgreement> queryList(AccountAgreementQuery query);
	
	void deleteAccountAgreementById(Long id);
	
	void deleteAccountAgreement(AccountAgreementQuery query);

	void updateAccountAgreement(AccountAgreement accountAgreement);
}
