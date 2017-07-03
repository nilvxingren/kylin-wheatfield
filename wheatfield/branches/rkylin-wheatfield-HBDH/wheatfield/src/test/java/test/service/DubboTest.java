package test.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.rkylin.crps.pojo.BaseResponse;
import com.rkylin.crps.pojo.OrderDetail;
import com.rkylin.crps.pojo.OrderDetails;
import com.rkylin.crps.pojo.ResultCode;
import com.rkylin.crps.service.CrpsApiService;
import com.rkylin.wheatfield.api.AccountDubboService;
import com.rkylin.wheatfield.api.AccountManagementService;
import com.rkylin.wheatfield.api.AccountService;
import com.rkylin.wheatfield.api.BankAccountServiceApi;
import com.rkylin.wheatfield.api.FinanaceAccountService;
import com.rkylin.wheatfield.api.FinanaceEntryService;
import com.rkylin.wheatfield.api.GenerationPayDubboService;
import com.rkylin.wheatfield.api.OrderDubboService;
import com.rkylin.wheatfield.api.PaymentAccountServiceApi;
import com.rkylin.wheatfield.api.PaymentInternalOutService;
import com.rkylin.wheatfield.api.SemiAutomatizationServiceApi;
import com.rkylin.wheatfield.common.DateUtils;
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.domain.M000003OpenEntityAccountBean;
import com.rkylin.wheatfield.enumtype.UserTypeEnum;
import com.rkylin.wheatfield.manager.GenerationPaymentManager;
import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.model.WipeAccountResponse;
import com.rkylin.wheatfield.pojo.AccountInfo;
import com.rkylin.wheatfield.pojo.AccountInfoQuery;
import com.rkylin.wheatfield.pojo.Balance;
import com.rkylin.wheatfield.pojo.GenerationPayment;
import com.rkylin.wheatfield.pojo.User;
import com.rkylin.wheatfield.response.AccountInfoGetResponse;
import com.rkylin.wheatfield.response.ErrorResponse;
import com.rkylin.wheatfield.service.GenerationPaymentService;
import com.rkylin.wheatfield.utils.DateUtil;

import javax.annotation.Resource;

/**
 * Created by thonny on 2015-5-11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath*:spring/applicationContext.xml"})
public class DubboTest extends AbstractJUnit4SpringContextTests {

	// @Resource
	// AccountService getGenerationPaymentService;

	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	GenerationPaymentManager generationPaymentManager;
	@Autowired
	GenerationPaymentService generationPaymentService;

	/**
	 * 把流水表的流水数据复制到流水历史表；    如果原来有测试数据，直接删除订单表中新生成的数据便可以再次调用；
	 * 页面输入==  订单号:自定义一个订单号	订单包号：原订单号      机构码：相关机构号      账户状态：选择正常  
	 */
	@Test
	public void wipeAccountForDubboTest() {
		PaymentInternalOutService genService = (PaymentInternalOutService) applicationContext.getBean("paymentInternalOutService23");
		WipeAccountResponse res =  genService.wipeAccountPlat("_ORDER_11_NEW", "_ORDER_11",  "M000001");
		System.out.println(res);
	}
	
	@Test
	public void getBalanceTest() {
		PaymentAccountServiceApi genService = (PaymentAccountServiceApi) applicationContext.getBean("paymentAccountServiceApi23");
		User user = new User();
		user.userId="18701514648";
		user.constId ="M000001";
		user.productId ="P000002";
		user.statusID="1";
		Balance res =  genService.getBalance(user, null);
		System.out.println(res);
	}
	
	// 先生成一个交易,加一个新的订单号，然后直接调用
	@Test
	public void reversalPlatTest() {
		PaymentAccountServiceApi genService = (PaymentAccountServiceApi) applicationContext.getBean("paymentAccountServiceApi23");
		CommonResponse res =  genService.reversalPlat("10011", "_ORDER_29_NEW", "127.0.0.1", "_ORDER_29", "M000001");
		System.out.println(res);
	}
	
	@Test  // 直接调用
	public void forAccountTest() {
		SemiAutomatizationServiceApi genService = (SemiAutomatizationServiceApi) applicationContext.getBean("semiAutomatizationServiceApi23");
		List<Map<String,String>> paramsValueList  = new ArrayList<Map<String,String>>();
		Map<String,String> map = new HashMap<String,String>();
		map.put("type", "1001");
		map.put("amount", "1");
		map.put("userid", "18701514648");
		map.put("consid", "M000001");
		map.put("productid", "P000002");
		map.put("status", "1");
		paramsValueList.add(map);
		String res =  genService.ForAccount(paramsValueList);
		System.out.println(res);
	}
	
	/**
	 * 同一个机构下的两个用户
	 */
	@Test
	public void shareBankCardInfoTest() {
		BankAccountServiceApi genService = (BankAccountServiceApi) applicationContext.getBean("bankAccountServiceApi23");
		String res =  genService.shareBankCardInfo("18701514648", "18801438893", "M000001", "P000002", "6210981000003996263");
		System.out.println(res);
	}
	
	@Test
	public void updateAccountInfoStatusTest() {
		AccountManagementService genService = (AccountManagementService) applicationContext.getBean("accountManagementService23");
		AccountInfoQuery query = new AccountInfoQuery(); 
		query.setAccountId(3907);;
		query.setStatus(1);
		int res =  genService.updateAccountInfoStatus(query);
		System.out.println(res);
	}
	
	@Test
	public void selectAccountListForJspTest() {
		AccountManagementService genService = (AccountManagementService) applicationContext.getBean("accountManagementService23");
		AccountInfoQuery query = new AccountInfoQuery(); 
		query.setRootInstCd("M000001");
		query.setAccountName("18801438893");
		query.setProductId("P000002");
		List<AccountInfo> res =  genService.selectAccountListForJsp(query);
		System.out.println(res);
	}
	
	/**
	 * 从订单系统发起一笔实时代收，如果返回终态，手动将账户系统和订单系统的订单状态改为处理中的状态，用订单表的订单号输入测试
	 */
	@Test
	public void updateRealTimeTransInfoByRecAndPaySysTest() {
		GenerationPayDubboService genService = (GenerationPayDubboService) applicationContext.getBean("getGenerationPaymentService23");
		CommonResponse res =  genService.updateRealTimeTransInfoByRecAndPaySys(new String[]{"OP20151221172119001"});
		System.out.println(res);
	}
	
	@Test
	public void saveMerchantAccountTest() {
		AccountDubboService genService = (AccountDubboService) applicationContext.getBean("accountDubboService23");
		String res = genService.saveMerchantAccount(new M000003OpenEntityAccountBean());
		System.out.println(res);
	}
	
	@Test
	public void queryFinanaceentryListTest() {
		FinanaceEntryService genService = (FinanaceEntryService) applicationContext.getBean("finanaceEntryService23");
		AccountInfoGetResponse res = genService.queryFinanaceentryList("M000001","18701514648","P000002",null, null,null, "1");
		System.out.println(res);
	}
	
	/**
	 * 在账户系统找一个从订单系统发起的交易，并且此订单在订单系统是非成功状态；用此订单号进行操作
	 */
	@Test
	public void updateOrderSysStatusByAccountSysTest() {
		OrderDubboService genService = (OrderDubboService) applicationContext.getBean("orderDubboService23");
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		Map<String, String> map = new HashMap<String, String>();
		map.put("orderNo", "OP20151223165843001");
		map.put("instCode", "M000001");
		list.add(map);
		CommonResponse res = genService.updateOrderSysStatusByAccountSys(list);
		System.out.println(res);
	}
	/**
	 *  手动生成一笔代收付交易，汇总,发送到代收付系统，将代收付系统的交易状态改为成功或失败状态，用账户系统的批次号进行测试
	 */
	@Test
	public void updateGenResultsByQuyRecAndPaySysTest() {
		GenerationPayDubboService genService = (GenerationPayDubboService) applicationContext.getBean("getGenerationPaymentService23");
		CommonResponse res = genService.updateGenResultsByQuyRecAndPaySys("FNRECEIVE_ST20151222001",null);
		System.out.println(res);
	}
	
	/**
	 * 手动生成一笔代收付交易，汇总，在代收付表PROCESS_RESULT 添加一个随意的批次号（不要和已有的重复，），SEND_TYPE改为2，将此交易存于缓存，
	 *方法参照test.service.RedisTest.getSetTest;将上述批次号输入页面输入框进行操作
	 */
	@Test
	public void handleRecAndPayCachePushedTest() {
		GenerationPayDubboService genService = (GenerationPayDubboService) applicationContext.getBean("getGenerationPaymentService23");
		CommonResponse res = genService.handleRecAndPayCachePushed("batch_20151223");
		System.out.println(res);
	}
	
	/**
	 * 手动生成一笔代收付交易，进行汇总，在代收付表PROCESS_RESULT 添加一个随意的批次号（不要和已有的重复，），在页面输入这个批次号进行测试
	 */
	@Test
	public void submitToRecAndPaySysManualTest() {
		GenerationPayDubboService genService = (GenerationPayDubboService) applicationContext.getBean("getGenerationPaymentService23");
		CommonResponse res = genService.submitToRecAndPaySysManual("batch_20151216");
		System.out.println(res);
//		logger.info("accountService=="+accountService.refund(orderDetail));
	}
	
	/**
	 * 页面：     输入相应的账户id，选择状态值直接进行修改，
	 */
	@Test
	public void updateStatusByIdTest() {
		FinanaceAccountService genService = (FinanaceAccountService) applicationContext.getBean("finanaceAccountService23");
		int res = genService.updateStatusById("TCA20151217180900001", "1");
		System.out.println(res);
//		logger.info("accountService=="+accountService.refund(orderDetail));

	}
	
	/**
	 * 找一个订单状态为4的代付或体现交易，直接调用
	 * 页面：  输入订单表中相应的订单号和机构号
	 */
	@Test
	public void refundTest() {
//		GenerationPayDubboService genService = (GenerationPayDubboService) applicationContext.getBean("getGenerationPaymentService23");
//		CommonResponse res = genService.getTransInforAndRefundRecords("20151215165656", "M000003");
		CommonResponse res = generationPaymentService.getTransInforAndRefundRecords("20151215165656", "M000003");
		System.out.println(res);
//		logger.info("accountService=="+accountService.refund(orderDetail));
	}
	
	@Test
	public void accountService() {
		AccountService accountService = (AccountService) applicationContext.getBean("getGenerationPaymentService");
//		baseResponse.
		OrderDetail orderDetail = new OrderDetail();
//		orderDetail.setOrderNo("OP20151029181654001");
		orderDetail.setOrderNo("OP20151028142815001");
//		orderDetail.setOrderNo("OP20151028141442001");
//		orderDetail.setOrderNo("20150826171201001");
		orderDetail.setRootInstCd("M000001");
		orderDetail.setStatusId(15);
		logger.info("accountService=="+accountService.refund(orderDetail));

	}
	
	@Test
	public void GenerationPaymentManager() {
		try {
			generationPaymentManager.batchUpdate(new ArrayList());;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void refund() {
		AccountService accountService = (AccountService) applicationContext.getBean("getGenerationPaymentService");
//		baseResponse.
		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setOrderNo("20150814070000001");
//		orderDetail.setOrderNo("20150826171201001");
		orderDetail.setStatusId(16);
//		accountService.refund(orderDetail);
		logger.info("accountService=="+accountService.refund(orderDetail));

	}
	
	@Test
	public void getGenerationPaymentList() {
		AccountService accountService = (AccountService) applicationContext.getBean("getGenerationPaymentService");
		List list = new ArrayList();
		OrderDetails orderDetails = new OrderDetails();
		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setOrderNo("1447606336597183824");
//		orderDetail.setOrderNo("20150826171201001");
		orderDetail.setStatusId(15);
		list.add(orderDetail);
//		accountService.refund(orderDetail);
		orderDetails.setOrderDetails(list);
		logger.info("accountService=="+accountService.refund(orderDetails));
		CommonResponse res = new CommonResponse();
		System.out.println(res.getCode());

	}
	
	@Test
	public void submitToCollection() {
		GenerationPaymentService generationPaymentService = (GenerationPaymentService) applicationContext.getBean("generationPaymentService");
		ResultCode resultCode = null;
		try {
			String accountDate=generationPaymentService.getAccountDate();
			generationPaymentService.submitToRecAndPaySys
			(6,Constants.FN_ID,accountDate,DateUtils.getDate(accountDate, Constants.DATE_FORMAT_YYYYMMDD));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("----------------------------------");;
		}
//		System.out.println("======="+resultCode.getRetMsg().getRetCode());;
		// getGenerationPaymentService.getGenerationPaymentList(new
		// ArrayList<GenerationPayment>());
	}
	
	@Test
	public void aaupdateTest() {
		CrpsApiService crpsApiService = (CrpsApiService) applicationContext.getBean("crpsApiService");
		ResultCode resultCode = null;
		try {
			resultCode = crpsApiService.transDetailFromOrder(new OrderDetail());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("----------------------------------");;
		}
		System.out.println("======="+resultCode.getRetMsg().getRetCode());;
		// getGenerationPaymentService.getGenerationPaymentList(new
		// ArrayList<GenerationPayment>());
	}
}
