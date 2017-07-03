/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package test.wheatfield;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.rkylin.wheatfield.manager.CurrencyInfoManager;
import com.rkylin.wheatfield.pojo.CurrencyInfo;
import com.rkylin.wheatfield.pojo.CurrencyInfoQuery;

public class CurrencyInfoManagerTest extends BaseJUnit4Test {
	
	@Autowired
	@Qualifier("currencyInfoManager")
	private CurrencyInfoManager currencyInfoManager;


	public void testNewCurrencyInfo() {
		CurrencyInfo CurrencyInfo = new CurrencyInfo();
		currencyInfoManager.saveCurrencyInfo(CurrencyInfo);
	}


	public void testUpdateCurrencyInfo(){
		CurrencyInfo CurrencyInfo = new CurrencyInfo();
//		CurrencyInfo.setId(2l);
		currencyInfoManager.saveCurrencyInfo(CurrencyInfo);
	}
	

	public void testDeleteCurrencyInfo(){
		currencyInfoManager.deleteCurrencyInfoById(99L);
	}
	

	public void testDeleteCurrencyInfoByQuery(){
		CurrencyInfoQuery query = new CurrencyInfoQuery();
		currencyInfoManager.deleteCurrencyInfo(query);
	}


	public void testFindCurrencyInfoById(){
		CurrencyInfoQuery query = new CurrencyInfoQuery();
		int size = currencyInfoManager.queryList(query).size();
		System.out.println(size);
	}
}
