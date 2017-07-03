package test.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.wheatfield.BaseJUnit4Test;

import com.rkylin.wheatfield.bean.OrderQuery;
import com.rkylin.wheatfield.model.TransOrderInfosResponse;
import com.rkylin.wheatfield.service.TransOrderService;

public class TransOrderServiceTest  extends BaseJUnit4Test{

	@Autowired
	private TransOrderService transOrderService;
	
	@Test
	public void getOrdersByReqNoTest(){
		OrderQuery query = new OrderQuery();
		query.setRequestNoArray(new String[]{"1456369630512711465","1456369603630364707","1456369195091864125","1456369193609579156"});
		query.setStatusArray(new Integer[]{1,8});
		TransOrderInfosResponse res = transOrderService.getOrdersByReqNo(query);
		System.out.println(res);
	}

}
