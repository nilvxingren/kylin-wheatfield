/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package test.wheatfield;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.rkylin.wheatfield.manager.FinanaceEntryManager;
import com.rkylin.wheatfield.pojo.FinanaceEntry;
import com.rkylin.wheatfield.pojo.FinanaceEntryQuery;

public class FinanaceEntryManagerTest extends BaseJUnit4Test {
	
	@Autowired
	@Qualifier("finanaceEntryManager")
	private FinanaceEntryManager finanaceEntryManager;


	public void testNewFinanaceEntry() {
		FinanaceEntry FinanaceEntry = new FinanaceEntry();
		finanaceEntryManager.saveFinanaceEntry(FinanaceEntry);
	}


	public void testUpdateFinanaceEntry(){
		FinanaceEntry FinanaceEntry = new FinanaceEntry();
//		FinanaceEntry.setId(2l);
		finanaceEntryManager.saveFinanaceEntry(FinanaceEntry);
	}
	

	public void testDeleteFinanaceEntry(){
		finanaceEntryManager.deleteFinanaceEntryById(99L);
	}
	

	public void testDeleteFinanaceEntryByQuery(){
		FinanaceEntryQuery query = new FinanaceEntryQuery();
		finanaceEntryManager.deleteFinanaceEntry(query);
	}


	public void testFindFinanaceEntryById(){
		FinanaceEntryQuery query = new FinanaceEntryQuery();
		int size = finanaceEntryManager.queryList(query).size();
		System.out.println(size);
	}
}
