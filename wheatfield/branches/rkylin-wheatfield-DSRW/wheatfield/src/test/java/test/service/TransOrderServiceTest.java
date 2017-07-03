package test.service;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.wheatfield.BaseJUnit4Test;

import com.rkylin.wheatfield.api.TransOrderDubboService;
import com.rkylin.wheatfield.bean.OrderQuery;
import com.rkylin.wheatfield.bean.TransOrderStatusUpdate;
import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.model.TransOrderInfosResponse;
import com.rkylin.wheatfield.pojo.TransOrderInfoQuery;
import com.rkylin.wheatfield.service.TransOrderService;

public class TransOrderServiceTest  extends BaseJUnit4Test{

	@Autowired
	private TransOrderService transOrderService;
	
    @Autowired
    private TransOrderDubboService transOrderDubboService;
    
    @Test
    public void getOrdersByPackageNoTest(){
        TransOrderInfoQuery query = new TransOrderInfoQuery();
        query.setOrderPackageNo("a744a2e6473e4fba8bed3e8945e6f0b8");
        TransOrderInfosResponse res = transOrderDubboService.getOrdersByPackageNo(query);
        System.out.println(res);
    }
	
	@Test
	public void getOrdersByReqNoTest(){
		OrderQuery query = new OrderQuery();
		query.setRequestNoArray(new String[]{"OP20160510174130002"});
		query.setStatusArray(new Integer[]{1});
		TransOrderInfosResponse res = transOrderService.getOrdersByReqNo(query);
		System.out.println(res);
	}

    @Test
    public void updateTransOrderStatusTest(){
        TransOrderStatusUpdate transOrder = new TransOrderStatusUpdate(); 
//        Set<Integer> requestIdSet = new HashSet<Integer>();
//        requestIdSet.add(12);
//        transOrder.setRequestIdSet(requestIdSet);
        Set<String> requestNoSet = new HashSet<String>();
        requestNoSet.add("12M");
        transOrder.setRequestNoSet(requestNoSet);
//        transOrder.setOrderPackageNo("89");
        Set<String> orderNoSet = new HashSet<String>();
        orderNoSet.add("12M");
        transOrder.setOrderNoSet(orderNoSet);
        transOrder.setMerchantCode("M01");
        transOrder.setStatus(77);
        CommonResponse res = transOrderDubboService.updateTransOrderStatus(transOrder);
        System.out.println(res);
    }
}
