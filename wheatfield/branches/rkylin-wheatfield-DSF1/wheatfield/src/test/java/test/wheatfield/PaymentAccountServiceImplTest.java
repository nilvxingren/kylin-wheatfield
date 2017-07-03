package test.wheatfield;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.rkylin.common.RedisIdGenerator;
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

public class PaymentAccountServiceImplTest extends BaseJUnit4Test{

	@Autowired 
	private PaymentAccountService paymentAccountService;
	@Autowired 
	private GenerationPaymentService generationPaymentService;
	@Autowired
	private GenerationPaymentManager generationPaymentManager;
	@Autowired
	RedisIdGenerator redisIdGenerator;
	@Autowired
	TransDaysSummaryManager transDaysSummaryManager;
	
	String accountDate="2015-11-12";
	
	@Test
	public void isPushedGenPayTest() {
		Set newOrderNoSet = new HashSet();
		newOrderNoSet.add("4");
		generationPaymentService.isPushedGenPay("M000002", newOrderNoSet);
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
			transOrderInfo.setOrderNo("41_ORDER_"+i);
			transOrderInfo.setAccountDate(DateUtils.getDate(accountDate,Constants.DATE_FORMAT_YYYYMMDD));
			transOrderInfo.setRemark("41_ORDER_FN_WITHDRAW");
			transOrderInfo.setRequestNo(DateUtils.getyyyyMMdd(Constants.DATE_FORMAT_YYYYMMDDHHMMSS));
			transOrderInfo.setRequestTime(DateUtils.getSysDate(Constants.DATE_FORMAT_YYYYMMDDHHMMSS));
			transOrderInfo.setStatus(1);
			transOrderInfo.setUserIpAddress("11.11.11.11");
			paymentAccountService.withdrow(transOrderInfo, "P000002","18701514648",null);
		}
	}
	
	@Test
	public void collectionTest() {
//		String accountDate=generationPaymentService.getAccountDate();//账期
		for(int i=70;i<=200;i++){
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
			transOrderInfo.setOrderNo("52_ORDER_"+i);
//			transOrderInfo.setAccountDate(DateUtils.getDate("2015-11-13",Constants.DATE_FORMAT_YYYYMMDD));
			transOrderInfo.setRemark("52_ORDER_FN_COLL");
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
	public void submitToRecAndPaySys() {
		String accountDate="2016-1-5";
		try {
			generationPaymentService.submitToRecAndPaySys
			(6,Constants.FN_ID,accountDate,DateUtils.getDate(accountDate, Constants.DATE_FORMAT_YYYYMMDD));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
