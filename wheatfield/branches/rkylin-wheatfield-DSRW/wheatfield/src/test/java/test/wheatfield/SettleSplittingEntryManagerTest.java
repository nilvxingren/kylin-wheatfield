/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package test.wheatfield;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.rkylin.wheatfield.manager.SettleSplittingEntryManager;
import com.rkylin.wheatfield.pojo.SettleSplittingEntry;
import com.rkylin.wheatfield.pojo.SettleSplittingEntryQuery;

public class SettleSplittingEntryManagerTest extends BaseJUnit4Test {
	
	@Autowired
	@Qualifier("settleSplittingEntryManager")
	private SettleSplittingEntryManager settleSplittingEntryManager;


	public void testNewSettleSplittingEntry() {
		SettleSplittingEntry SettleSplittingEntry = new SettleSplittingEntry();
		settleSplittingEntryManager.saveSettleSplittingEntry(SettleSplittingEntry);
	}


	public void testUpdateSettleSplittingEntry(){
		SettleSplittingEntry SettleSplittingEntry = new SettleSplittingEntry();
//		SettleSplittingEntry.setId(2l);
		settleSplittingEntryManager.saveSettleSplittingEntry(SettleSplittingEntry);
	}
	

	public void testDeleteSettleSplittingEntry(){
		settleSplittingEntryManager.deleteSettleSplittingEntryById(99L);
	}
	

	public void testDeleteSettleSplittingEntryByQuery(){
		SettleSplittingEntryQuery query = new SettleSplittingEntryQuery();
		settleSplittingEntryManager.deleteSettleSplittingEntry(query);
	}


	public void testFindSettleSplittingEntryById(){
		SettleSplittingEntryQuery query = new SettleSplittingEntryQuery();
		int size = settleSplittingEntryManager.queryList(query).size();
		System.out.println(size);
	}
}
