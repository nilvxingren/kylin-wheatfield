package test.wheatfield;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;

import com.rkylin.common.RedisIdGenerator;
import com.rkylin.wheatfield.api.PaymentAccountServiceApi;
import com.rkylin.wheatfield.api.PaymentAccountServiceApi;
import com.rkylin.wheatfield.bean.BatchTransferOrderInfo;
import com.rkylin.wheatfield.bean.OrderInfo;
import com.rkylin.wheatfield.common.DateUtils;
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.constant.TransCodeConst;
import com.rkylin.wheatfield.manager.GenerationPaymentManager;
import com.rkylin.wheatfield.manager.TransDaysSummaryManager;
import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.pojo.GenerationPayment;
import com.rkylin.wheatfield.pojo.TransOrderInfo;
import com.rkylin.wheatfield.response.Response;
import com.rkylin.wheatfield.service.GenerationPaymentService;
import com.rkylin.wheatfield.service.PaymentAccountService;
import com.rkylin.wheatfield.utils.CommUtil;

public class PaymentAccountServiceImplTest extends BaseJUnit4Test{

	@Autowired 
	private PaymentAccountService paymentAccountService;
//    @Autowired 
//    @Qualifier("paymentAccountService")
//    private PaymentAccountServiceApi paymentAccountServiceApi;
	@Autowired 
	private GenerationPaymentService generationPaymentService;
	@Autowired
	private GenerationPaymentManager generationPaymentManager;
	@Autowired
	RedisIdGenerator redisIdGenerator;
	@Autowired
	TransDaysSummaryManager transDaysSummaryManager;
	@Autowired
	@Qualifier("paymentAccountService")
	PaymentAccountServiceApi paymentAccountServiceApi;
	
	String accountDate="2015-12-15";
	
	@Test
	public void isPushedGenPayTest() {
		String accountDate=generationPaymentService.getAccountDate();
		System.out.println(accountDate);
//		Set newOrderNoSet = new HashSet();
//		newOrderNoSet.add("4");
//		generationPaymentService.isPushedGenPay("M000002", newOrderNoSet);
	}
	
	@Test
	public void withdrawTest() {
//		String accountDate=generationPaymentService.getAccountDate();//账期
		for(int i=1;i<=1;i++){
			TransOrderInfo transOrderInfo = new TransOrderInfo();
			transOrderInfo.setUserId("18701514648");
			transOrderInfo.setProductIdd("P000002");;
			transOrderInfo.setAmount(2L);
			transOrderInfo.setBankCode("403");
			transOrderInfo.setFuncCode("4016");
			transOrderInfo.setUserFee(0L);
			transOrderInfo.setOrderAmount(2L);	
			transOrderInfo.setMerchantCode("M000001");
			transOrderInfo.setOrderCount(1);
			transOrderInfo.setErrorMsg("T0");
			transOrderInfo.setOrderDate(DateUtils.getSysDate(Constants.DATE_FORMAT_YYYYMMDDHHMMSS));
			transOrderInfo.setOrderNo("ORDER_NO_WITHDRAW_"+i);
			transOrderInfo.setAccountDate(DateUtils.getDate("2015-12-29",Constants.DATE_FORMAT_YYYYMMDD));
			transOrderInfo.setRemark("体现运营平台"+i);
			transOrderInfo.setRequestNo(DateUtils.getyyyyMMdd(Constants.DATE_FORMAT_YYYYMMDDHHMMSS));
			transOrderInfo.setRequestTime(DateUtils.getSysDate(Constants.DATE_FORMAT_YYYYMMDDHHMMSS));
			transOrderInfo.setStatus(1);
			transOrderInfo.setUserIpAddress("11.11.11.11");
			paymentAccountService.withdrow(transOrderInfo, "P000002","18701514648",null);
		}
	}
	
	@Test
	public void submitToRecAndPaySys() {
		String date = "2015-12-29";
		try {
			generationPaymentService.submitToRecAndPaySys
			(6,Constants.FN_ID,date,DateUtils.getDate(date, Constants.DATE_FORMAT_YYYYMMDD));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void collectionTest() {
//		String accountDate=generationPaymentService.getAccountDate();//账期
		int m = 52;
		for(int i=m;i<=m;i++){
			TransOrderInfo transOrderInfo = new TransOrderInfo();
			transOrderInfo.setUserId("18701514648");
			transOrderInfo.setProductIdd("P000002");;
			transOrderInfo.setAmount(2L);
			transOrderInfo.setBankCode("403");
			transOrderInfo.setFuncCode("4013");
			transOrderInfo.setUserFee(0L);
			transOrderInfo.setOrderAmount(2L);	
			transOrderInfo.setMerchantCode("M000001");
			transOrderInfo.setOrderCount(1);
			transOrderInfo.setErrorMsg("T0");
			transOrderInfo.setOrderDate(DateUtils.getSysDate(Constants.DATE_FORMAT_YYYYMMDDHHMMSS));
			transOrderInfo.setOrderNo("_ORDER_"+i);
//			transOrderInfo.setAccountDate(DateUtils.getDate("2015-11-13",Constants.DATE_FORMAT_YYYYMMDD));
			transOrderInfo.setRemark("运营平台测试"+i);
			transOrderInfo.setRequestNo(DateUtils.getyyyyMMdd(Constants.DATE_FORMAT_YYYYMMDDHHMMSS));
			transOrderInfo.setRequestTime(DateUtils.getSysDate(Constants.DATE_FORMAT_YYYYMMDDHHMMSS));
			transOrderInfo.setStatus(1);
			transOrderInfo.setUserIpAddress("11.11.11.11");
			paymentAccountService.collection(transOrderInfo, "P000002");
		}
	}
	
	@Test
	public void colleBiz() {
		Map<String, String[]> paramMap = new HashMap();
		paramMap.put("amount", new String[]{"1"});
		paramMap.put("userid", new String[]{"18701514648"});
		String funCode ="4013";
		paramMap.put("funccode", new String[]{funCode});
		paramMap.put("merchantcode", new String[]{"M000001"});
		paramMap.put("orderamount", new String[]{"1"}); 
		paramMap.put("ordercount", new String[]{"1"});
		paramMap.put("orderdate", new String[]{DateUtils.getyyyyMMdd(Constants.DATE_FORMAT_YYYYMMDDHHMMSS)});
		paramMap.put("orderno", new String[]{"10096"});
		paramMap.put("remark", new String[]{"代收"+funCode});
		paramMap.put("productid", new String[]{"P000002"});
		paramMap.put("requesttime", new String[]{DateUtils.getyyyyMMdd(Constants.DATE_FORMAT_YYYYMMDDHHMMSS)});
		paramMap.put("status", new String[]{"1"});
		paramMap.put("userfee", new String[]{"0"});
		paramMap.put("useripaddress", new String[]{"127.0.0.41"});
		Response res = paymentAccountService.collectionBiz(paramMap);
		logger.info("res======"+res);
//
	}

	@Test
	public void collection() {
		TransOrderInfo transOrderInfo = new TransOrderInfo();
		Long amount = 1L;
		transOrderInfo.setAmount(amount);
		transOrderInfo.setUserId("15051204040");
		String funCode ="4013";
		transOrderInfo.setFuncCode(funCode);
		transOrderInfo.setMerchantCode("M000004");
		transOrderInfo.setInterMerchantCode(TransCodeConst.THIRDPARTYID_KZP2PZZH);
		transOrderInfo.setBusiTypeId("1");
		transOrderInfo.setOrderAmount(amount);
		transOrderInfo.setOrderCount(1);
		transOrderInfo.setOrderDate(DateUtils.getSysDate(Constants.DATE_FORMAT_YYYYMMDDHHMMSS));
		transOrderInfo.setRemark("代收:"+funCode);
		String orderNo = "K00062";
		transOrderInfo.setOrderNo(orderNo);
		transOrderInfo.setProductIdd("P000008");
		transOrderInfo.setRequestTime(DateUtils.getSysDate(Constants.DATE_FORMAT_YYYYMMDDHHMMSS));
		transOrderInfo.setStatus(1);
		transOrderInfo.setUserFee(0L);
		transOrderInfo.setUserIpAddress("127.0.0.44");
		transOrderInfo.setRemark("计息发起代收贷款还款  订单号="+orderNo);
		CommonResponse res = paymentAccountService.collection(transOrderInfo);
		logger.info("res======"+res);
	}
	
	@Test
    public void transferInCommonTest() {
	    Map map = new HashMap<String, String[]>();
	    map.put("userid", new String[]{"7878"});
	    CommonResponse res = paymentAccountServiceApi.transferInCommon(map);
	    logger.info("res======"+res);
	}
    @Test
    public void collectionBatchTest() {
//    String accountDate=generationPaymentService.getAccountDate();//账期
        int m = 5;
        List<com.rkylin.wheatfield.bean.TransOrderInfo> orderList = new ArrayList<com.rkylin.wheatfield.bean.TransOrderInfo>();
//      for(int i=1;i<=m;i++){=
        String packageNo =new Date().toString().substring(0, 20);
            com.rkylin.wheatfield.bean.TransOrderInfo transOrderInfo = new com.rkylin.wheatfield.bean.TransOrderInfo();
            transOrderInfo.setOrderPackageNo(packageNo);
            transOrderInfo.setUserId("18701514648");
            transOrderInfo.setProductId("P000002");;
            transOrderInfo.setAmount(2L);
            transOrderInfo.setBankCode("403");
            transOrderInfo.setFuncCode("4013");
            transOrderInfo.setUserFee(0L);
            transOrderInfo.setOrderAmount(2L);  
            transOrderInfo.setMerchantCode("M000001");
            transOrderInfo.setOrderCount(1);
            transOrderInfo.setOrderDate(DateUtils.getSysDate(Constants.DATE_FORMAT_YYYYMMDDHHMMSS));
            transOrderInfo.setOrderNo("BATCH_ORDER_"+"A2");
//        transOrderInfo.setAccountDate(DateUtils.getDate("2015-11-13",Constants.DATE_FORMAT_YYYYMMDD));
            transOrderInfo.setRemark("批量代收"+323);
            transOrderInfo.setRequestNo(DateUtils.getyyyyMMdd(Constants.DATE_FORMAT_YYYYMMDDHHMMSS));
            transOrderInfo.setRequestTime(DateUtils.getSysDate(Constants.DATE_FORMAT_YYYYMMDDHHMMSS));
            transOrderInfo.setStatus(1);
            transOrderInfo.setUserIpAddress("13.13.11.11");
            orderList.add(transOrderInfo);
            
            com.rkylin.wheatfield.bean.TransOrderInfo transOrderInfo2 = new com.rkylin.wheatfield.bean.TransOrderInfo();
            transOrderInfo2.setOrderPackageNo(packageNo);
            transOrderInfo2.setUserId("1870151464823");
            transOrderInfo2.setProductId("P000002");;
            transOrderInfo2.setAmount(2L);
            transOrderInfo2.setBankCode("403");
            transOrderInfo2.setFuncCode("4013");
            transOrderInfo2.setUserFee(0L);
            transOrderInfo2.setOrderAmount(2L);  
            transOrderInfo2.setMerchantCode("M000001");
            transOrderInfo2.setOrderCount(1);
            transOrderInfo2.setOrderDate(DateUtils.getSysDate(Constants.DATE_FORMAT_YYYYMMDDHHMMSS));
            transOrderInfo2.setOrderNo("BATCH_ORDER_"+"b2");
//        transOrderInfo.setAccountDate(DateUtils.getDate("2015-11-13",Constants.DATE_FORMAT_YYYYMMDD));
            transOrderInfo2.setRemark("批量代收"+567575);
            transOrderInfo2.setRequestNo(DateUtils.getyyyyMMdd(Constants.DATE_FORMAT_YYYYMMDDHHMMSS));
            transOrderInfo2.setRequestTime(DateUtils.getSysDate(Constants.DATE_FORMAT_YYYYMMDDHHMMSS));
            transOrderInfo2.setStatus(1);
            transOrderInfo2.setUserIpAddress("13.13.11.11");
            orderList.add(transOrderInfo2);             
//      }
        CommonResponse res = paymentAccountServiceApi.collectionBatch(orderList);
        System.out.println(res);
    }	
    
    @Test
    public void transferBatchTest() {
        com.rkylin.wheatfield.bean.BatchTransferOrderInfo batchTransfer=new BatchTransferOrderInfo();
        batchTransfer.setMerchantCode("M000001");
        batchTransfer.setIntoUserId("18801438893");
        batchTransfer.setIntoProductId("P000002");
        batchTransfer.setFuncCode("3001");
        batchTransfer.setOrderPackageNo(CommUtil.getGenerateNum(2));
        
        List<com.rkylin.wheatfield.bean.OrderInfo> orderInfoList = new ArrayList<com.rkylin.wheatfield.bean.OrderInfo>();
        com.rkylin.wheatfield.bean.OrderInfo orderInfo = new OrderInfo();
        orderInfo.setAmount(1l);
        orderInfo.setOutUserId("18701514648");
        orderInfo.setOutProductId("P000002");
        orderInfo.setOrderNo("ORDER_NO_"+CommUtil.getGenerateNum(2));
        orderInfo.setRequestNo("RequestNo");
        orderInfo.setRemark("BATCH");
        orderInfoList.add(orderInfo);
        
        com.rkylin.wheatfield.bean.OrderInfo orderInfo2 = new OrderInfo();
        orderInfo2.setAmount(20l);
        orderInfo2.setOutUserId("ios4");
        orderInfo2.setOutProductId("P000002");
        orderInfo2.setOrderNo("ORDER_NO_"+CommUtil.getGenerateNum(2));
        orderInfo2.setRequestNo("RequestNo");
        orderInfo2.setRemark("BATCH");
        orderInfoList.add(orderInfo2);
        batchTransfer.setOrderInfoList(orderInfoList);
        CommonResponse res = paymentAccountServiceApi.transferBatch(batchTransfer);
        System.out.println(res);
    }   
}