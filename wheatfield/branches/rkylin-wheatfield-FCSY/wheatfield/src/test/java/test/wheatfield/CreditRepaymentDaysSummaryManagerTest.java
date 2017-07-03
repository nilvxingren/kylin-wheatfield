/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package test.wheatfield;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.rkylin.wheatfield.manager.CreditRepaymentDaysSummaryManager;
import com.rkylin.wheatfield.pojo.CreditRepaymentDaysSummary;
import com.rkylin.wheatfield.pojo.CreditRepaymentDaysSummaryQuery;

public class CreditRepaymentDaysSummaryManagerTest extends BaseJUnit4Test {
	
	@Autowired
	@Qualifier("creditRepaymentDaysSummaryManager")
	private CreditRepaymentDaysSummaryManager creditRepaymentDaysSummaryManager;


	public void testNewCreditRepaymentDaysSummary() {
		CreditRepaymentDaysSummary CreditRepaymentDaysSummary = new CreditRepaymentDaysSummary();
		creditRepaymentDaysSummaryManager.saveCreditRepaymentDaysSummary(CreditRepaymentDaysSummary);
	}


	public void testUpdateCreditRepaymentDaysSummary(){
		CreditRepaymentDaysSummary CreditRepaymentDaysSummary = new CreditRepaymentDaysSummary();
//		CreditRepaymentDaysSummary.setId(2l);
		creditRepaymentDaysSummaryManager.saveCreditRepaymentDaysSummary(CreditRepaymentDaysSummary);
	}
	

	public void testDeleteCreditRepaymentDaysSummary(){
		creditRepaymentDaysSummaryManager.deleteCreditRepaymentDaysSummaryById(99L);
	}
	

	public void testDeleteCreditRepaymentDaysSummaryByQuery(){
		CreditRepaymentDaysSummaryQuery query = new CreditRepaymentDaysSummaryQuery();
		creditRepaymentDaysSummaryManager.deleteCreditRepaymentDaysSummary(query);
	}


	public void testFindCreditRepaymentDaysSummaryById(){
		CreditRepaymentDaysSummaryQuery query = new CreditRepaymentDaysSummaryQuery();
		int size = creditRepaymentDaysSummaryManager.queryList(query).size();
		System.out.println(size);
	}
}
