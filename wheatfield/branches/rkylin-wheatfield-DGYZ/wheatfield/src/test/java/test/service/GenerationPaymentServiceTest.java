package test.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.wheatfield.BaseJUnit4Test;

import com.rkylin.crps.pojo.OrderDetail;
import com.rkylin.crps.pojo.OrderDetails;
import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.pojo.TransOrderInfoQuery;
import com.rkylin.wheatfield.service.GenerationPaymentService;

public class GenerationPaymentServiceTest   extends BaseJUnit4Test{

	@Autowired
	private GenerationPaymentService generationPaymentService;
	
//	@Test
//	public void getGenPaymentListTest(){
//		List<OrderDetail> list = new ArrayList<OrderDetail>();
//		OrderDetail orderDetail1 = new OrderDetail();
//		orderDetail1.setOrderNo("1452146971605171621");
//		orderDetail1.setStatusId(13);
//		list.add(orderDetail1);
//		OrderDetail orderDetail2 = new OrderDetail();
//		orderDetail2.setOrderNo("1452146971605171256");
//		orderDetail2.setStatusId(13);
//		list.add(orderDetail2);
//		CommonResponse res = generationPaymentService.getGenPaymentList(list, 0);
//		System.out.println(res);
//	}
	
	@Test
	public void getAccountDateTest(){
		String res = generationPaymentService.getAccountDate();
		System.out.println(res);
	}
	
	@Test
	public void paymentGeneationTest(){
		TransOrderInfoQuery query = new TransOrderInfoQuery();
		query.setOrderNo("2015123021570005");
		query.setMerchantCode("M00001323");
		CommonResponse res = generationPaymentService.getTransOrderInfos(query);
		System.out.println(res);
	}
}
