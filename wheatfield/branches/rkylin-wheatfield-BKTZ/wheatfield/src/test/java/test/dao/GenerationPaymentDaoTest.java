package test.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.rkylin.common.RedisIdGenerator;
import com.rkylin.wheatfield.common.DateUtils;
import com.rkylin.wheatfield.common.RedisBase;
import com.rkylin.wheatfield.constant.AccountConstants;
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.constant.SettleConstants;
import com.rkylin.wheatfield.constant.TransCodeConst;
import com.rkylin.wheatfield.dao.GenerationPaymentDao;
import com.rkylin.wheatfield.dao.TransDaysSummaryDao;
import com.rkylin.wheatfield.pojo.GenerationPayment;
import com.rkylin.wheatfield.pojo.TransDaysSummary;

/**
 * Created by thonny on 2015-5-11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath*:spring/applicationContext.xml"})
public class GenerationPaymentDaoTest extends AbstractJUnit4SpringContextTests {
	private static Logger logger = LoggerFactory.getLogger(GenerationPaymentDaoTest.class);
	
	@Autowired
	GenerationPaymentDao generationPaymentDao;

	@Autowired
	RedisIdGenerator redisIdGenerator;
	
	@Test
	public void selectByOrderNoAndBatchTest() {
		String batch = "FNRECEIVE_ST20151214004";
		String[]  orderNoArray = {"1450248982594152430"};
		List<GenerationPayment>  list = generationPaymentDao.selectByOrderNoAndBatch(batch,orderNoArray);
		logger.info(list+"");
	}
	
	@Test
	public void insertBatchTest() {
		List l = new ArrayList();
		for (int i = 2; i < 4; i++) {
			GenerationPayment generationPayment=new GenerationPayment();
			generationPayment.setUserId("18701514648");
			generationPayment.setSendType(SettleConstants.SEND_DEFEAT);
			generationPayment.setErrorCode("账户未绑定结算卡");
			generationPayment.setBankCode("gongshang");
			generationPayment.setAccountType("2");
			generationPayment.setAccountNo("8989");
			generationPayment.setAccountName("PPP");
			generationPayment.setAccountProperty("2");
			generationPayment.setProvince("BEIJING");
			generationPayment.setCity("BEIJING");
			generationPayment.setOrderNo("iuiuiui"+i);
			generationPayment.setOpenBankName("");
			generationPayment.setPayBankCode("");
			generationPayment.setCurrency("CNY");
			generationPayment.setCertificateType("0");
			generationPayment.setCertificateNumber("610321");
			generationPayment.setRootInstCd(Constants.FN_ID);//丰年机构号
			generationPayment.setOrderType(2);//订单类型
			generationPayment.setAmount(1l);
			generationPayment.setStatusId(1);
			generationPayment.setBussinessCode("98989");//业务代码
			generationPayment.setAccountDate(DateUtils.getDate("2015-11-10", Constants.DATE_FORMAT_YYYYMMDD));//账单日期
			generationPayment.setRemark("eouroe");
			l.add(generationPayment);
		}
		generationPaymentDao.insertBatch(l);
		
	}

}
