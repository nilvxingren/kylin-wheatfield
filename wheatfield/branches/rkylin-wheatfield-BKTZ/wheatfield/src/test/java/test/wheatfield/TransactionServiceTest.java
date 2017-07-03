package test.wheatfield;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.rkylin.wheatfield.service.TransactionService;

/**
 * 
 * @author  zhenpc@chanjet.com
 * @version 2015年3月31日 上午10:37:44
 */

public class TransactionServiceTest extends BaseJUnit4Test {
	
	@Autowired
	@Resource(name="transactionService")
	private TransactionService transactionService;
	
	@Test
	public void testInsert(){
		System.out.println("--------》1");
		transactionService.insertTest();
	}

}
