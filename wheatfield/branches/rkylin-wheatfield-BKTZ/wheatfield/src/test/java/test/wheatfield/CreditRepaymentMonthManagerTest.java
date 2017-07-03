/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package test.wheatfield;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.rkylin.wheatfield.manager.CreditRepaymentMonthManager;
import com.rkylin.wheatfield.pojo.CreditRepaymentMonth;
import com.rkylin.wheatfield.pojo.CreditRepaymentMonthQuery;

public class CreditRepaymentMonthManagerTest extends BaseJUnit4Test {
	
	@Autowired
	@Qualifier("creditRepaymentMonthManager")
	private CreditRepaymentMonthManager creditRepaymentMonthManager;


	public void testNewCreditRepaymentMonth() {
		CreditRepaymentMonth CreditRepaymentMonth = new CreditRepaymentMonth();
		creditRepaymentMonthManager.saveCreditRepaymentMonth(CreditRepaymentMonth);
	}


	public void testUpdateCreditRepaymentMonth(){
		CreditRepaymentMonth CreditRepaymentMonth = new CreditRepaymentMonth();
//		CreditRepaymentMonth.setId(2l);
		creditRepaymentMonthManager.saveCreditRepaymentMonth(CreditRepaymentMonth);
	}
	

	public void testDeleteCreditRepaymentMonth(){
		creditRepaymentMonthManager.deleteCreditRepaymentMonthById(99L);
	}
	

	public void testDeleteCreditRepaymentMonthByQuery(){
		CreditRepaymentMonthQuery query = new CreditRepaymentMonthQuery();
		creditRepaymentMonthManager.deleteCreditRepaymentMonth(query);
	}


	public void testFindCreditRepaymentMonthById(){
		CreditRepaymentMonthQuery query = new CreditRepaymentMonthQuery();
		int size = creditRepaymentMonthManager.queryList(query).size();
		System.out.println(size);
	}
}
