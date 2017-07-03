/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package test.wheatfield;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.rkylin.wheatfield.manager.SettleBalanceEntryManager;
import com.rkylin.wheatfield.pojo.SettleBalanceEntry;
import com.rkylin.wheatfield.pojo.SettleBalanceEntryQuery;

public class SettleBalanceEntryManagerTest extends BaseJUnit4Test {
	
	@Autowired
	@Qualifier("settleBalanceEntryManager")
	private SettleBalanceEntryManager settleBalanceEntryManager;


	public void testNewSettleBalanceEntry() {
		SettleBalanceEntry SettleBalanceEntry = new SettleBalanceEntry();
		settleBalanceEntryManager.saveSettleBalanceEntry(SettleBalanceEntry);
	}


	public void testUpdateSettleBalanceEntry(){
		SettleBalanceEntry SettleBalanceEntry = new SettleBalanceEntry();
//		SettleBalanceEntry.setId(2l);
		settleBalanceEntryManager.saveSettleBalanceEntry(SettleBalanceEntry);
	}
	

	public void testDeleteSettleBalanceEntry(){
		settleBalanceEntryManager.deleteSettleBalanceEntryById(99L);
	}
	

	public void testDeleteSettleBalanceEntryByQuery(){
		SettleBalanceEntryQuery query = new SettleBalanceEntryQuery();
		settleBalanceEntryManager.deleteSettleBalanceEntry(query);
	}


	public void testFindSettleBalanceEntryById(){
		SettleBalanceEntryQuery query = new SettleBalanceEntryQuery();
		int size = settleBalanceEntryManager.queryList(query).size();
		System.out.println(size);
	}
}
