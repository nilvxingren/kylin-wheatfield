/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package test.wheatfield;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.rkylin.wheatfield.manager.ManageHistoryManager;
import com.rkylin.wheatfield.pojo.ManageHistory;
import com.rkylin.wheatfield.pojo.ManageHistoryQuery;

public class ManageHistoryManagerTest extends BaseJUnit4Test {
	
	@Autowired
	@Qualifier("manageHistoryManager")
	private ManageHistoryManager manageHistoryManager;


	public void testNewManageHistory() {
		ManageHistory ManageHistory = new ManageHistory();
		manageHistoryManager.saveManageHistory(ManageHistory);
	}


	public void testUpdateManageHistory(){
		ManageHistory ManageHistory = new ManageHistory();
		ManageHistory.setId(2l);
		manageHistoryManager.saveManageHistory(ManageHistory);
	}
	

	public void testDeleteManageHistory(){
		manageHistoryManager.deleteManageHistoryById(99L);
	}
	

	public void testDeleteManageHistoryByQuery(){
		ManageHistoryQuery query = new ManageHistoryQuery();
		manageHistoryManager.deleteManageHistory(query);
	}


	public void testFindManageHistoryById(){
		ManageHistoryQuery query = new ManageHistoryQuery();
		int size = manageHistoryManager.queryList(query).size();
		System.out.println(size);
	}
}
