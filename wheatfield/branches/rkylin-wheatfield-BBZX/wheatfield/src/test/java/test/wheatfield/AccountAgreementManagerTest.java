/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package test.wheatfield;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.rkylin.wheatfield.manager.AccountAgreementManager;
import com.rkylin.wheatfield.pojo.AccountAgreement;
import com.rkylin.wheatfield.pojo.AccountAgreementQuery;

public class AccountAgreementManagerTest extends BaseJUnit4Test {
	
	@Autowired
	@Qualifier("accountAgreementManager")
	private AccountAgreementManager accountAgreementManager;


	public void testNewAccountAgreement() {
		AccountAgreement AccountAgreement = new AccountAgreement();
		accountAgreementManager.saveAccountAgreement(AccountAgreement);
	}


	public void testUpdateAccountAgreement(){
		AccountAgreement AccountAgreement = new AccountAgreement();
//		AccountAgreement.setId(2l);
		accountAgreementManager.saveAccountAgreement(AccountAgreement);
	}
	

	public void testDeleteAccountAgreement(){
		accountAgreementManager.deleteAccountAgreementById(99L);
	}
	

	public void testDeleteAccountAgreementByQuery(){
		AccountAgreementQuery query = new AccountAgreementQuery();
		accountAgreementManager.deleteAccountAgreement(query);
	}


	public void testFindAccountAgreementById(){
		AccountAgreementQuery query = new AccountAgreementQuery();
		int size = accountAgreementManager.queryList(query).size();
		System.out.println(size);
	}
}
