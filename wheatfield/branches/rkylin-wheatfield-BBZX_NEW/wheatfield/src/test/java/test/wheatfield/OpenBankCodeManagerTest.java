/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package test.wheatfield;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.rkylin.wheatfield.manager.OpenBankCodeManager;
import com.rkylin.wheatfield.pojo.OpenBankCode;
import com.rkylin.wheatfield.pojo.OpenBankCodeQuery;

public class OpenBankCodeManagerTest extends BaseJUnit4Test {
	
	@Autowired
	@Qualifier("openBankCodeManager")
	private OpenBankCodeManager openBankCodeManager;


	public void testNewOpenBankCode() {
		OpenBankCode OpenBankCode = new OpenBankCode();
		openBankCodeManager.saveOpenBankCode(OpenBankCode);
	}


	public void testUpdateOpenBankCode(){
		OpenBankCode OpenBankCode = new OpenBankCode();
//		OpenBankCode.setId(2l);
		openBankCodeManager.saveOpenBankCode(OpenBankCode);
	}
	

	public void testDeleteOpenBankCode(){
		openBankCodeManager.deleteOpenBankCodeById(99L);
	}
	

	public void testDeleteOpenBankCodeByQuery(){
		OpenBankCodeQuery query = new OpenBankCodeQuery();
		openBankCodeManager.deleteOpenBankCode(query);
	}


	public void testFindOpenBankCodeById(){
		OpenBankCodeQuery query = new OpenBankCodeQuery();
		int size = openBankCodeManager.queryList(query).size();
		System.out.println(size);
	}
}
