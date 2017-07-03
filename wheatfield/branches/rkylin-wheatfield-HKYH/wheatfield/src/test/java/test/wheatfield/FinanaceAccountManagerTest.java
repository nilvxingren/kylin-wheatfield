/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package test.wheatfield;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.rkylin.wheatfield.manager.FinanaceAccountManager;
import com.rkylin.wheatfield.pojo.FinanaceAccount;
import com.rkylin.wheatfield.pojo.FinanaceAccountQuery;

public class FinanaceAccountManagerTest extends BaseJUnit4Test {
	
	@Autowired
	@Qualifier("finanaceAccountManager")
	private FinanaceAccountManager finanaceAccountManager;


	public void testNewFinanaceAccount() {
		FinanaceAccount FinanaceAccount = new FinanaceAccount();
		finanaceAccountManager.saveFinanaceAccount(FinanaceAccount);
	}


	public void testUpdateFinanaceAccount(){
		FinanaceAccount FinanaceAccount = new FinanaceAccount();
//		FinanaceAccount.setId(2l);
		finanaceAccountManager.saveFinanaceAccount(FinanaceAccount);
	}
	

	public void testDeleteFinanaceAccount(){
		finanaceAccountManager.deleteFinanaceAccountById(99L);
	}
	

	public void testDeleteFinanaceAccountByQuery(){
		FinanaceAccountQuery query = new FinanaceAccountQuery();
		finanaceAccountManager.deleteFinanaceAccount(query);
	}


	public void testFindFinanaceAccountById(){
		FinanaceAccountQuery query = new FinanaceAccountQuery();
		int size = finanaceAccountManager.queryList(query).size();
		System.out.println(size);
	}
}
