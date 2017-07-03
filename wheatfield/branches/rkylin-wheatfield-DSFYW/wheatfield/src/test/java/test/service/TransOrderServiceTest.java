package test.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import test.wheatfield.BaseJUnit4Test;

import com.rkylin.wheatfield.api.TransOrderDubboService;
import com.rkylin.wheatfield.bean.OrderQuery;
import com.rkylin.wheatfield.bean.TransOrderInfo;
import com.rkylin.wheatfield.bean.TransOrderStatusUpdate;
import com.rkylin.wheatfield.constant.TransCodeConst;
import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.model.TransOrderInfosResponse;
import com.rkylin.wheatfield.pojo.TransOrderInfoQuery;
import com.rkylin.wheatfield.service.OrderService;
import com.rkylin.wheatfield.service.TransOrderService;

public class TransOrderServiceTest  extends BaseJUnit4Test{

	@Autowired
	@Qualifier("transOrderService")
	private TransOrderService transOrderService;
	
    @Autowired
    @Qualifier("transOrderService")
    private TransOrderDubboService transOrderDubboService;
    
    @Autowired
    private OrderService orderService;
    
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
    
    @Test
    public void notifyTransOrderResultsTest(){
        List<com.rkylin.wheatfield.bean.TransOrderInfo> transOrderInfoList = new ArrayList<com.rkylin.wheatfield.bean.TransOrderInfo>();
        com.rkylin.wheatfield.bean.TransOrderInfo transOrderInfo = new TransOrderInfo();
        transOrderInfo.setMerchantCode("M000001");
        transOrderInfo.setOrderNo("49_ORDER_1");
        transOrderInfo.setAmount(4L);
//        transOrderInfo.setStatus(TransCodeConst.TRANS_STATUS_PAY_SUCCEED);
//        transOrderInfo.setStatus(TransCodeConst.TRANS_STATUS_PAY_FAILED);
        transOrderInfo.setStatus(TransCodeConst.TRANS_STATUS_REFUND);
        transOrderInfo.setErrorMsg("49_ORDER_1  fail");
        transOrderInfoList.add(transOrderInfo);
        com.rkylin.wheatfield.bean.TransOrderInfo transOrderInfo2 = new TransOrderInfo();
        transOrderInfo2.setMerchantCode("M000001");
        transOrderInfo2.setOrderNo("50_ORDER_1");
        transOrderInfo2.setAmount(5L);
//        transOrderInfo2.setStatus(TransCodeConst.TRANS_STATUS_PAY_SUCCEED);
        transOrderInfo2.setStatus(TransCodeConst.TRANS_STATUS_PAY_FAILED);
        transOrderInfo2.setErrorMsg("50_ORDER_1  fail");
//        transOrderInfoList.add(transOrderInfo2);     //141223100010002   
        CommonResponse res = transOrderDubboService.notifyTransOrderResults(transOrderInfoList);
        System.out.println(res);
    } 
  
    @Test
    public void manageRecAndPayCacheResultsTest(){
        transOrderService.manageRecAndPayCacheResults(null);
    }  
 
    @Test
    public void updateAccountInfoByPayResultTest(){
        CommonResponse res = transOrderService.updateAccountInfoByPayResult();
        System.out.println(res);
    }     
    
    @Test
    public void updateOrderSysOrderStatusTest(){
        List<com.rkylin.wheatfield.pojo.TransOrderInfo> transOrderInfoAllList = new ArrayList<com.rkylin.wheatfield.pojo.TransOrderInfo>();
        com.rkylin.wheatfield.pojo.TransOrderInfo order = new com.rkylin.wheatfield.pojo.TransOrderInfo();
        order.setOrderNo("OP20150811133049002");
        order.setFuncCode("4014");
        order.setStatus(TransCodeConst.TRANS_STATUS_PAY_FAILED);
        transOrderInfoAllList.add(order);
        CommonResponse res = orderService.updateOrderSysOrderStatus(transOrderInfoAllList);
        System.out.println(res);
    } 
}
