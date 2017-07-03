/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package test.wheatfield;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.rkylin.wheatfield.manager.AccountPasswordManager;
import com.rkylin.wheatfield.pojo.AccountPassword;
import com.rkylin.wheatfield.pojo.AccountPasswordQuery;

public class AccountPasswordManagerTest extends BaseJUnit4Test {
	
	@Autowired
	@Qualifier("accountPasswordManager")
	private AccountPasswordManager accountPasswordManager;


	public void testNewAccountPassword() {
		AccountPassword AccountPassword = new AccountPassword();
		accountPasswordManager.saveAccountPassword(AccountPassword);
	}


	public void testUpdateAccountPassword(){
		AccountPassword AccountPassword = new AccountPassword();
		AccountPassword.setAcctPawdId(1);
		accountPasswordManager.saveAccountPassword(AccountPassword);
	}
	

	public void testDeleteAccountPassword(){
		accountPasswordManager.deleteAccountPasswordById(99L);
	}
	

	public void testDeleteAccountPasswordByQuery(){
		AccountPasswordQuery query = new AccountPasswordQuery();
		accountPasswordManager.deleteAccountPassword(query);
	}


	public void testFindAccountPasswordById(){
		AccountPasswordQuery query = new AccountPasswordQuery();
		int size = accountPasswordManager.queryList(query).size();
		System.out.println(size);
	}
}
