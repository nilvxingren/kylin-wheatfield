/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package test.wheatfield;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.rkylin.wheatfield.manager.TransCodeManager;
import com.rkylin.wheatfield.pojo.TransCode;
import com.rkylin.wheatfield.pojo.TransCodeQuery;

public class TransCodeManagerTest extends BaseJUnit4Test {
	
	@Autowired
	@Qualifier("transCodeManager")
	private TransCodeManager transCodeManager;


	public void testNewTransCode() {
		TransCode TransCode = new TransCode();
		transCodeManager.saveTransCode(TransCode);
	}


	public void testUpdateTransCode(){
		TransCode TransCode = new TransCode();
//		TransCode.setId(2l);
		transCodeManager.saveTransCode(TransCode);
	}
	

	public void testDeleteTransCode(){
		transCodeManager.deleteTransCodeById(99L);
	}
	

	public void testDeleteTransCodeByQuery(){
		TransCodeQuery query = new TransCodeQuery();
		transCodeManager.deleteTransCode(query);
	}


	public void testFindTransCodeById(){
		TransCodeQuery query = new TransCodeQuery();
		int size = transCodeManager.queryList(query).size();
		System.out.println(size);
	}
}
