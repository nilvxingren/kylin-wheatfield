/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package test.wheatfield;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.rkylin.wheatfield.manager.CreditRepaymentMonthSummaryManager;
import com.rkylin.wheatfield.pojo.CreditRepaymentMonthSummary;
import com.rkylin.wheatfield.pojo.CreditRepaymentMonthSummaryQuery;

public class CreditRepaymentMonthSummaryManagerTest extends BaseJUnit4Test {
	
	@Autowired
	@Qualifier("creditRepaymentMonthSummaryManager")
	private CreditRepaymentMonthSummaryManager creditRepaymentMonthSummaryManager;


	public void testNewCreditRepaymentMonthSummary() {
		CreditRepaymentMonthSummary CreditRepaymentMonthSummary = new CreditRepaymentMonthSummary();
		creditRepaymentMonthSummaryManager.saveCreditRepaymentMonthSummary(CreditRepaymentMonthSummary);
	}


	public void testUpdateCreditRepaymentMonthSummary(){
		CreditRepaymentMonthSummary CreditRepaymentMonthSummary = new CreditRepaymentMonthSummary();
//		CreditRepaymentMonthSummary.setId(2l);
		creditRepaymentMonthSummaryManager.saveCreditRepaymentMonthSummary(CreditRepaymentMonthSummary);
	}
	

	public void testDeleteCreditRepaymentMonthSummary(){
		creditRepaymentMonthSummaryManager.deleteCreditRepaymentMonthSummaryById(99L);
	}
	

	public void testDeleteCreditRepaymentMonthSummaryByQuery(){
		CreditRepaymentMonthSummaryQuery query = new CreditRepaymentMonthSummaryQuery();
		creditRepaymentMonthSummaryManager.deleteCreditRepaymentMonthSummary(query);
	}


	public void testFindCreditRepaymentMonthSummaryById(){
		CreditRepaymentMonthSummaryQuery query = new CreditRepaymentMonthSummaryQuery();
		int size = creditRepaymentMonthSummaryManager.queryList(query).size();
		System.out.println(size);
	}
}
