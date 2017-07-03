/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package test.wheatfield;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.rkylin.wheatfield.manager.TransOrderInfoManager;
import com.rkylin.wheatfield.pojo.TransOrderInfo;
import com.rkylin.wheatfield.pojo.TransOrderInfoQuery;

public class TransOrderInfoManagerTest extends BaseJUnit4Test {
	
	@Autowired
	@Qualifier("transOrderInfoManager")
	private TransOrderInfoManager transOrderInfoManager;


	public void testNewTransOrderInfo() {
		TransOrderInfo TransOrderInfo = new TransOrderInfo();
		transOrderInfoManager.saveTransOrderInfo(TransOrderInfo);
	}


	public void testUpdateTransOrderInfo(){
		TransOrderInfo TransOrderInfo = new TransOrderInfo();
//		TransOrderInfo.setId(2l);
		transOrderInfoManager.saveTransOrderInfo(TransOrderInfo);
	}
	

	public void testDeleteTransOrderInfo(){
		transOrderInfoManager.deleteTransOrderInfoById(99L);
	}
	

	public void testDeleteTransOrderInfoByQuery(){
		TransOrderInfoQuery query = new TransOrderInfoQuery();
		transOrderInfoManager.deleteTransOrderInfo(query);
	}


	public void testFindTransOrderInfoById(){
		TransOrderInfoQuery query = new TransOrderInfoQuery();
		int size = transOrderInfoManager.queryList(query).size();
		System.out.println(size);
	}
}
