/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package test.wheatfield;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.pojo.TransOrderInfoNew;
import com.rkylin.wheatfield.service.impl.OrderServiceImpl;

public class OrderServiceImplTest extends BaseJUnit4Test {
	
	@Autowired
//	@Qualifier("orderService")
	private OrderServiceImpl orderService;


	@Test
	public void testGetTransOrdersNew() {
		List<TransOrderInfoNew> resList = orderService.
				getTransOrdersNew(null, null, "M000001",null, "2015-11-12 22:11:33", null, null, null,null);
		System.out.println(resList);
//		
//		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++");
//		
//		System.out.println(resList.size());
//		
//		System.out.println(resList.get(0).getTranssumid());
//		
//		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++");
	}
	
}
