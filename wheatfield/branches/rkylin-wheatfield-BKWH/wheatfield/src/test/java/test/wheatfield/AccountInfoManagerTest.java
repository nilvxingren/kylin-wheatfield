/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package test.wheatfield;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.rkylin.wheatfield.manager.AccountInfoManager;
import com.rkylin.wheatfield.pojo.AccountInfo;
import com.rkylin.wheatfield.pojo.AccountInfoQuery;

public class AccountInfoManagerTest extends BaseJUnit4Test {
	
	@Autowired
	@Qualifier("accountInfoManager")
	private AccountInfoManager accountInfoManager;


	public void testNewAccountInfo() {
		AccountInfo AccountInfo = new AccountInfo();
		accountInfoManager.saveAccountInfo(AccountInfo);
	}


	public void testUpdateAccountInfo(){
		AccountInfo AccountInfo = new AccountInfo();
//		AccountInfo.setId(2l);
		accountInfoManager.saveAccountInfo(AccountInfo);
	}
	

	public void testDeleteAccountInfo(){
		accountInfoManager.deleteAccountInfoById(99);
	}
	

	public void testDeleteAccountInfoByQuery(){
		AccountInfoQuery query = new AccountInfoQuery();
		accountInfoManager.deleteAccountInfo(query);
	}


	public void testFindAccountInfoById(){
		AccountInfoQuery query = new AccountInfoQuery();
		int size = accountInfoManager.queryList(query).size();
		System.out.println(size);
	}
}
