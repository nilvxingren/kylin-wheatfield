/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package test.wheatfield;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.rkylin.wheatfield.manager.CreditRepaymentHistoryManager;
import com.rkylin.wheatfield.pojo.CreditRepaymentHistory;
import com.rkylin.wheatfield.pojo.CreditRepaymentHistoryQuery;

public class CreditRepaymentHistoryManagerTest extends BaseJUnit4Test {
	
	@Autowired
	@Qualifier("creditRepaymentHistoryManager")
	private CreditRepaymentHistoryManager creditRepaymentHistoryManager;


	public void testNewCreditRepaymentHistory() {
		CreditRepaymentHistory CreditRepaymentHistory = new CreditRepaymentHistory();
		creditRepaymentHistoryManager.saveCreditRepaymentHistory(CreditRepaymentHistory);
	}


	public void testUpdateCreditRepaymentHistory(){
		CreditRepaymentHistory CreditRepaymentHistory = new CreditRepaymentHistory();
//		CreditRepaymentHistory.setId(2l);
		creditRepaymentHistoryManager.saveCreditRepaymentHistory(CreditRepaymentHistory);
	}
	

	public void testDeleteCreditRepaymentHistory(){
		creditRepaymentHistoryManager.deleteCreditRepaymentHistoryById(99L);
	}
	

	public void testDeleteCreditRepaymentHistoryByQuery(){
		CreditRepaymentHistoryQuery query = new CreditRepaymentHistoryQuery();
		creditRepaymentHistoryManager.deleteCreditRepaymentHistory(query);
	}


	public void testFindCreditRepaymentHistoryById(){
		CreditRepaymentHistoryQuery query = new CreditRepaymentHistoryQuery();
		int size = creditRepaymentHistoryManager.queryList(query).size();
		System.out.println(size);
	}
}
