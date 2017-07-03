/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package test.wheatfield;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.rkylin.wheatfield.pojo.TransOrderInfoNew;
import com.rkylin.wheatfield.service.impl.OrderServiceImpl;

public class OrderServiceImplTest extends BaseJUnit4Test {
	
	@Autowired
//	@Qualifier("orderService")
	private OrderServiceImpl orderService;


	@Test
	public void testGetTransOrdersNew() {
		List<TransOrderInfoNew> resList = orderService.getTransOrdersNew("jrd_pc_20150814132653891781", "1439535185096091600", "M000005", "2015-08-12 14:53:06", "2015-08-18 14:53:06", "4014", "7");
		
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++");
		
		System.out.println(resList.size());
		
		System.out.println(resList.get(0).getTranssumid());
		
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++");
	}
	
}
