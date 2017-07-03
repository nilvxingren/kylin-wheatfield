package test.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.rkylin.wheatfield.api.AccountService;
import com.rkylin.wheatfield.common.DateUtils;
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.manager.GenerationPaymentManager;
import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.pojo.GenerationPayment;
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
