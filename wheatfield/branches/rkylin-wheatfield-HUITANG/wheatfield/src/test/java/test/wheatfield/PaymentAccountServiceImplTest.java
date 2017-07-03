package test.wheatfield;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.rkylin.wheatfield.common.DateUtils;
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.response.Response;
import com.rkylin.wheatfield.service.AccountManageService;
import com.rkylin.wheatfield.service.GenerationPaymentService;
import com.rkylin.wheatfield.service.PaymentAccountService;


/**
 * Created by thonny on 2015-5-11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath*:spring/applicationContext.xml"})
public class PaymentAccountServiceImplTest extends AbstractJUnit4SpringContextTests {

	@Autowired 
	private PaymentAccountService paymentAccountService;
	
	@Test
	public void colleBiz() {
		Map<String, String[]> paramMap = new HashMap();
		paramMap.put("amount", new String[]{"1"});
		paramMap.put("userid", new String[]{"18701514648"});
		paramMap.put("funccode", new String[]{"40131"});
		paramMap.put("intermerchantcode", new String[]{"100"});
		paramMap.put("merchantcode", new String[]{"M000001"});
//		paramMap.put("orderamount", new String[]{"100"}); 
		paramMap.put("ordercount", new String[]{"1"});
		paramMap.put("orderdate", new String[]{DateUtils.getyyyyMMdd(Constants.DATE_FORMAT_YYYYMMDDHHMMSS)});
		paramMap.put("orderno", new String[]{"10032"});
		paramMap.put("orderpackageno", new String[]{"100"});
		paramMap.put("paychannelid", new String[]{"100"});
		paramMap.put("remark", new String[]{"实时代收"});
		paramMap.put("productid", new String[]{"P000002"});
		
		paramMap.put("requestno", new String[]{"100"});
		paramMap.put("requesttime", new String[]{DateUtils.getyyyyMMdd(Constants.DATE_FORMAT_YYYYMMDDHHMMSS)});
		paramMap.put("status", new String[]{"1"});
		paramMap.put("tradeflowno", new String[]{"100"});
		paramMap.put("userfee", new String[]{"1"});
		paramMap.put("feeamount", new String[]{"0"});
		paramMap.put("profit", new String[]{"100"});
		
		paramMap.put("useripaddress", new String[]{"127.0.0.41"});
		Response res = paymentAccountService.collectionBiz(paramMap);
		
		logger.info("res======"+res);
//
	}

}
