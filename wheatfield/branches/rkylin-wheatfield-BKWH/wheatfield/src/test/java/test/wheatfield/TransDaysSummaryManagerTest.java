/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package test.wheatfield;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.rkylin.wheatfield.manager.TransDaysSummaryManager;
import com.rkylin.wheatfield.pojo.TransDaysSummary;
import com.rkylin.wheatfield.pojo.TransDaysSummaryQuery;

public class TransDaysSummaryManagerTest extends BaseJUnit4Test {
	
	@Autowired
	@Qualifier("transDaysSummaryManager")
	private TransDaysSummaryManager transDaysSummaryManager;


	public void testNewTransDaysSummary() {
		TransDaysSummary TransDaysSummary = new TransDaysSummary();
		transDaysSummaryManager.saveTransDaysSummary(TransDaysSummary);
	}


	public void testUpdateTransDaysSummary(){
		TransDaysSummary TransDaysSummary = new TransDaysSummary();
		TransDaysSummary.getTransSumId();
		transDaysSummaryManager.saveTransDaysSummary(TransDaysSummary);
	}
	

	public void testDeleteTransDaysSummary(){
		transDaysSummaryManager.deleteTransDaysSummaryById(99L);
	}
	

	public void testDeleteTransDaysSummaryByQuery(){
		TransDaysSummaryQuery query = new TransDaysSummaryQuery();
		transDaysSummaryManager.deleteTransDaysSummary(query);
	}


	public void testFindTransDaysSummaryById(){
		TransDaysSummaryQuery query = new TransDaysSummaryQuery();
		int size = transDaysSummaryManager.queryList(query).size();
		System.out.println(size);
	}
}
