/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package test.wheatfield;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.rkylin.wheatfield.manager.CreditRepaymentManager;
import com.rkylin.wheatfield.pojo.CreditRepayment;
import com.rkylin.wheatfield.pojo.CreditRepaymentQuery;

public class CreditRepaymentManagerTest extends BaseJUnit4Test {
	
	@Autowired
	@Qualifier("creditRepaymentManager")
	private CreditRepaymentManager creditRepaymentManager;


	public void testNewCreditRepayment() {
		CreditRepayment CreditRepayment = new CreditRepayment();
		creditRepaymentManager.saveCreditRepayment(CreditRepayment);
	}


	public void testUpdateCreditRepayment(){
		CreditRepayment CreditRepayment = new CreditRepayment();
		creditRepaymentManager.saveCreditRepayment(CreditRepayment);
	}
	

	public void testDeleteCreditRepayment(){
		creditRepaymentManager.deleteCreditRepaymentById(99L);
	}
	

	public void testDeleteCreditRepaymentByQuery(){
		CreditRepaymentQuery query = new CreditRepaymentQuery();
		creditRepaymentManager.deleteCreditRepayment(query);
	}


	public void testFindCreditRepaymentById(){
		CreditRepaymentQuery query = new CreditRepaymentQuery();
		int size = creditRepaymentManager.queryList(query).size();
		System.out.println(size);
	}
}
