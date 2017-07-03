/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package test.wheatfield;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.rkylin.wheatfield.manager.CreditInfoManager;
import com.rkylin.wheatfield.pojo.CreditInfo;
import com.rkylin.wheatfield.pojo.CreditInfoQuery;

public class CreditInfoManagerTest extends BaseJUnit4Test {
	
	@Autowired
	@Qualifier("creditInfoManager")
	private CreditInfoManager creditInfoManager;


	public void testNewCreditInfo() {
		CreditInfo CreditInfo = new CreditInfo();
		creditInfoManager.saveCreditInfo(CreditInfo);
	}


	public void testUpdateCreditInfo(){
		CreditInfo CreditInfo = new CreditInfo();
		CreditInfo.setId(2l);
		creditInfoManager.saveCreditInfo(CreditInfo);
	}
	

	public void testDeleteCreditInfo(){
		creditInfoManager.deleteCreditInfoById(99L);
	}
	

	public void testDeleteCreditInfoByQuery(){
		CreditInfoQuery query = new CreditInfoQuery();
		creditInfoManager.deleteCreditInfo(query);
	}


	public void testFindCreditInfoById(){
		CreditInfoQuery query = new CreditInfoQuery();
		int size = creditInfoManager.queryList(query).size();
		System.out.println(size);
	}
}
