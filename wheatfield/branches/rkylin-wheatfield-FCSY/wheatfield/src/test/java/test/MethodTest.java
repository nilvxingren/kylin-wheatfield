package test;

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

import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.constant.SettleConstants;
import com.rkylin.wheatfield.dao.CorporatAccountInfoDao;
import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.pojo.CorporatAccountInfo;
import com.rkylin.wheatfield.pojo.GenerationPayment;
import com.rkylin.wheatfield.service.AccountManageService;
import com.rkylin.wheatfield.service.GenerationPaymentService;

import javax.annotation.Resource;

/**
 * Created by thonny on 2015-5-11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath*:spring/applicationContext.xml"})
public class MethodTest extends AbstractJUnit4SpringContextTests {

	// @Resource
	// AccountService getGenerationPaymentService;

	@Autowired
	private AccountManageService accountManageService;
	@Autowired
	private CorporatAccountInfoDao corporatAccountInfoDao;

	@Test
	public void getBalance() {
        Map<String, String> param = new HashMap<String, String>();  
        param.put("iv_fin_account_id", "141223100000001"); 
        param.put("iv_accrual_type", null);
        corporatAccountInfoDao.getBalance("MyBatisMap.getBalance", param);
		logger.info("param========1111111111111==========="+param);
        param.put("iv_fin_account_id", "141223100000003"); 
        param.put("iv_accrual_type", null);
        corporatAccountInfoDao.getBalance("MyBatisMap.getBalance", param);
//		AccountManageService accountService = (AccountManageService) applicationContext.getBean("accountService5");
		logger.info("param========22222222222222==========="+param);
//
	}
	
	
	@Test
	public void accountService() {
		accountManageService.paymentJudgePublicAccount();
//		AccountManageService accountService = (AccountManageService) applicationContext.getBean("accountService5");
		logger.info("accountManageService=="+accountManageService);
//
	}
	
	@Test
	public void updatePubAccountByPayResult() {
		accountManageService.updatePubAccountByPayResult();
//		AccountManageService accountService = (AccountManageService) applicationContext.getBean("accountService5");
		logger.info("accountManageService=="+accountManageService);
//
	}
	
	@Test
	public void update() {
		List<CorporatAccountInfo> corporatAccountInfoList = new ArrayList<CorporatAccountInfo>();
		CorporatAccountInfo corporatAccountInfo = new CorporatAccountInfo();
		corporatAccountInfo.setAccountNumber("565675756");
		corporatAccountInfo.setRootInstCd("M000001");
		corporatAccountInfo.setStatusId(Constants.ACCOUNT_NUM_STATRS_1);
		corporatAccountInfoList.add(corporatAccountInfo);
		CorporatAccountInfo corporatAccountInfo2 = new CorporatAccountInfo();
		corporatAccountInfo2.setAccountNumber("666");
		corporatAccountInfo2.setRootInstCd("M000001");
		corporatAccountInfo2.setStatusId(Constants.ACCOUNT_NUM_STATRS_4);
		corporatAccountInfoList.add(corporatAccountInfo2);
		corporatAccountInfoDao.batchUpdate(corporatAccountInfoList);
	}
	
}
