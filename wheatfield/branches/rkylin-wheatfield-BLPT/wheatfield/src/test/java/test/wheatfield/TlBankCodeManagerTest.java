/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package test.wheatfield;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.rkylin.wheatfield.manager.TlBankCodeManager;
import com.rkylin.wheatfield.pojo.TlBankCode;
import com.rkylin.wheatfield.pojo.TlBankCodeQuery;

public class TlBankCodeManagerTest extends BaseJUnit4Test {
	
	@Autowired
	@Qualifier("tlBankCodeManager")
	private TlBankCodeManager tlBankCodeManager;


	public void testNewTlBankCode() {
		TlBankCode TlBankCode = new TlBankCode();
		tlBankCodeManager.saveTlBankCode(TlBankCode);
	}


	public void testUpdateTlBankCode(){
		TlBankCode TlBankCode = new TlBankCode();
//		TlBankCode.setId(2l);
		tlBankCodeManager.saveTlBankCode(TlBankCode);
	}
	

	public void testDeleteTlBankCode(){
		tlBankCodeManager.deleteTlBankCodeById(99L);
	}
	

	public void testDeleteTlBankCodeByQuery(){
		TlBankCodeQuery query = new TlBankCodeQuery();
		tlBankCodeManager.deleteTlBankCode(query);
	}


	public void testFindTlBankCodeById(){
		TlBankCodeQuery query = new TlBankCodeQuery();
		int size = tlBankCodeManager.queryList(query).size();
		System.out.println(size);
	}
}
