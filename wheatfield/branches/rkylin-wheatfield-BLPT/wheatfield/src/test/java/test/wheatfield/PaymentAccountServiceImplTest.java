package test.wheatfield;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import com.rkylin.common.RedisIdGenerator;
import com.rkylin.wheatfield.api.PaymentAccountServiceApi;
import com.rkylin.wheatfield.bean.BatchTransfer;
import com.rkylin.wheatfield.bean.BatchTransferOrderInfo;
import com.rkylin.wheatfield.bean.OrderInfo;
import com.rkylin.wheatfield.bean.TransferInfo;
import com.rkylin.wheatfield.common.DateUtils;
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.constant.TransCodeConst;
import com.rkylin.wheatfield.manager.GenerationPaymentManager;
import com.rkylin.wheatfield.manager.TransDaysSummaryManager;
import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.pojo.TransOrderInfo;
import com.rkylin.wheatfield.response.ErrorResponse;
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
		int m = 10000;
		String userIdPrefix = "kz_lh_201609181415";
		String j = null;
		for(int i=1;i<=m;i++){
			TransOrderInfo transOrderInfo = new TransOrderInfo();
			if (i<10) {
                j="0000"+i;
            }else if (i>=10&&i<100) {
                j="000"+i;
            }else if (i>=100&&i<1000) {
                j="00"+i;
            }else if (i>=1000&&i<10000) {
                j="0"+i;
            }
			transOrderInfo.setUserId(userIdPrefix+j);
			transOrderInfo.setProductIdd("P000008");;
			transOrderInfo.setAmount(1L);
			transOrderInfo.setBankCode("403");
			transOrderInfo.setFuncCode("4013");
			transOrderInfo.setUserFee(0L);
			transOrderInfo.setOrderAmount(1L);	
			transOrderInfo.setMerchantCode("M000004");
			transOrderInfo.setOrderCount(1);
//			transOrderInfo.setErrorMsg("T0");
			Date now = DateUtils.getSysDate(Constants.DATE_FORMAT_YYYYMMDDHHMMSS);
			transOrderInfo.setOrderDate(now);
			transOrderInfo.setOrderNo("BBC_ORDER_"+i);
//			transOrderInfo.setAccountDate(DateUtils.getDate("2015-11-13",Constants.DATE_FORMAT_YYYYMMDD));
			transOrderInfo.setRemark("账户自测"+i);
			transOrderInfo.setRequestNo(DateUtils.getyyyyMMdd(Constants.DATE_FORMAT_YYYYMMDDHHMMSS));
			transOrderInfo.setRequestTime(now);
			transOrderInfo.setStatus(1);
			transOrderInfo.setUserIpAddress("17.17.11.11");
			ErrorResponse res =paymentAccountService.collection(transOrderInfo, "P000008");
			logger.info(res);
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
        List<com.rkylin.wheatfield.bean.TransOrderInfo> orderList = new ArrayList<com.rkylin.wheatfield.bean.TransOrderInfo>();
//      for(int i=1;i<=m;i++){=
        String packageNo =DateUtils.getSysDateStr(Constants.DATE_FORMAT_YYYYMMDDHHMMSS);
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
            transOrderInfo.setOrderNo("BATCH_ORDER_"+"A4");
//        transOrderInfo.setAccountDate(DateUtils.getDate("2015-11-13",Constants.DATE_FORMAT_YYYYMMDD));
            transOrderInfo.setRemark("NOACCOUNT");
            transOrderInfo.setRequestNo(DateUtils.getyyyyMMdd(Constants.DATE_FORMAT_YYYYMMDDHHMMSS));
            transOrderInfo.setRequestTime(DateUtils.getSysDate(Constants.DATE_FORMAT_YYYYMMDDHHMMSS));
            transOrderInfo.setStatus(1);
            transOrderInfo.setUserIpAddress("13.13.11.11");
            orderList.add(transOrderInfo);
            
            com.rkylin.wheatfield.bean.TransOrderInfo transOrderInfo2 = new com.rkylin.wheatfield.bean.TransOrderInfo();
            transOrderInfo2.setOrderPackageNo(packageNo);
            transOrderInfo2.setUserId("18701514648");
            transOrderInfo2.setProductId("P000002");;
            transOrderInfo2.setAmount(2L);
            transOrderInfo2.setBankCode("403");
            transOrderInfo2.setFuncCode("4013");
            transOrderInfo2.setUserFee(0L);
            transOrderInfo2.setOrderAmount(2L);  
            transOrderInfo2.setMerchantCode("M000001");
            transOrderInfo2.setOrderCount(1);
            transOrderInfo2.setOrderDate(DateUtils.getSysDate(Constants.DATE_FORMAT_YYYYMMDDHHMMSS));
            transOrderInfo2.setOrderNo("BATCH_ORDER_"+"b4");
//        transOrderInfo.setAccountDate(DateUtils.getDate("2015-11-13",Constants.DATE_FORMAT_YYYYMMDD));
            transOrderInfo2.setRemark("NOACCOUNT");
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
    
    @Test
    public void doJobTest() {
        Map<String, String[]> paramMap = new HashMap();
      paramMap.put("merchantcode", new String[]{"M00000145"});
      paramMap.put("orderpackageno", new String[]{"egfregregergewrw"});
        paramMap.put("useripaddress", new String[]{"127.0.0.4221"});
        
        paramMap.put("outuserinfo", new String[]{"["
                + "{\"userid\":\"h5005\",\"productid\":\"P000002\",\"orderno\":\"orderno232\","
                + "\"requestno\":\"requestno232\",\"remark\":\"remark_h5005\",\"amount\":\"3\"},"
                + "{\"userid\":\"h5009\",\"productid\":\"P000002\",\"orderno\":\"orderno23\","
                + "\"requestno\":\"requestno232\",\"remark\":\"remark_h5009\",\"amount\":\"2\"}"
                                            + "]"});
        
        paramMap.put("intouserinfo", new String[]{"["
                + "{\"userid\":\"ios6\",\"productid\":\"P000002\"}"
                                            + "]"});
        
//        Response res = paymentAccountService.doJob(paramMap,"ruixue.wheatfield.user.transfer.batch");
//        logger.info("res======"+res);
//
    }
    
    @Test
    public void transBatchTest() {
        BatchTransfer batchTransfer=new BatchTransfer();
        batchTransfer.setMerchantCode("M000001");
        batchTransfer.setOrderPackageNo(CommUtil.getGenerateNum(1)+"A");
        
        List<TransferInfo> outTransferInfoList =  new ArrayList<TransferInfo>();
        TransferInfo outTransferInfo= new TransferInfo();
        outTransferInfo.setAmount(1l);
        outTransferInfo.setProductId("P000002");
        outTransferInfo.setUserId("h5005");
        outTransferInfo.setOrderNo(CommUtil.getGenerateNum(1)+"B");
//        outTransferInfoList.add(outTransferInfo);
        TransferInfo outTransferInfo2= new TransferInfo();
        outTransferInfo2.setAmount(1l);
        outTransferInfo2.setProductId("P000002");
        outTransferInfo2.setUserId("18701514648");
        outTransferInfo2.setOrderNo(CommUtil.getGenerateNum(1)+"C");
        outTransferInfoList.add(outTransferInfo2);
        batchTransfer.setOutTransferInfoList(outTransferInfoList);
        
        List<TransferInfo> intoTransferInfoList =   new ArrayList<TransferInfo>();
        TransferInfo inTransferInfo= new TransferInfo();
        inTransferInfo.setAmount(1l);
        inTransferInfo.setProductId("P000002");
        inTransferInfo.setUserId("h5009");
        inTransferInfo.setOrderNo(CommUtil.getGenerateNum(1)+"E");
        intoTransferInfoList.add(inTransferInfo);
        TransferInfo inTransferInfo2= new TransferInfo();
        inTransferInfo2.setAmount(1l);
        inTransferInfo2.setProductId("P000002");
        inTransferInfo2.setUserId("ios2");
        inTransferInfo2.setOrderNo(CommUtil.getGenerateNum(1)+"F");
        intoTransferInfoList.add(inTransferInfo2);
        batchTransfer.setIntoTransferInfoList(intoTransferInfoList);
        CommonResponse res = paymentAccountServiceApi.transferBatch(batchTransfer);
        logger.info("res======"+res);
    }
    @Test
    public void rechargeTest() {
        com.rkylin.wheatfield.bean.TransOrderInfo transOrderInfo = new com.rkylin.wheatfield.bean.TransOrderInfo();
        transOrderInfo.setAmount(1l);
//        transOrderInfo.setUserId("141223100000050");
        transOrderInfo.setUserId("18701514648");
        transOrderInfo.setMerchantCode("M000001");
        transOrderInfo.setOrderAmount(1l);
        transOrderInfo.setOrderNo("K0006293");
        transOrderInfo.setProductId("P000002");
        transOrderInfo.setUserFee(0L);
        transOrderInfo.setUserIpAddress("127.0.0.44");
        transOrderInfo.setRemark("4015");
        CommonResponse res = paymentAccountServiceApi.recharge(transOrderInfo);
        logger.info("res======"+res);
    }    
}