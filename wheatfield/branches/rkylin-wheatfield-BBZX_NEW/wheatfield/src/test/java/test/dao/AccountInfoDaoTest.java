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
import com.rkylin.wheatfield.dao.AccountInfoDao;
import com.rkylin.wheatfield.dao.GenerationPaymentDao;
import com.rkylin.wheatfield.dao.TransDaysSummaryDao;
import com.rkylin.wheatfield.pojo.AccountInfo;
import com.rkylin.wheatfield.pojo.AccountInfoQuery;
import com.rkylin.wheatfield.pojo.GenerationPayment;
import com.rkylin.wheatfield.pojo.TransDaysSummary;

/**
 * Created by thonny on 2015-5-11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath*:spring/applicationContext.xml"})
public class AccountInfoDaoTest extends AbstractJUnit4SpringContextTests {
	private static Logger logger = LoggerFactory.getLogger(AccountInfoDaoTest.class);
	

	@Autowired
	AccountInfoDao accountInfoDao;
	
	@Test
	public void queryByUserIdAndPurposeTest() {
		List<AccountInfoQuery> accountList = new ArrayList<AccountInfoQuery>();
		AccountInfoQuery accountInfoQuery =new AccountInfoQuery();
		accountInfoQuery.setAccountPurpose(Constants.ACCOUNT_PURPOSE_3);//提现卡类型
		accountInfoQuery.setAccountName("18701514648");//用户Id
		accountInfoQuery.setRootInstCd("M000001");//机构号
		accountList.add(accountInfoQuery);
		AccountInfoQuery accountInfoQuery1 =new AccountInfoQuery();
		accountInfoQuery1.setAccountPurpose(Constants.ACCOUNT_PURPOSE_1);//提现卡类型
		accountInfoQuery1.setAccountName("18701514648");//用户Id
		accountInfoQuery1.setRootInstCd("M000001");//机构号
		accountList.add(accountInfoQuery1);
		AccountInfoQuery accountInfoQuery2 =new AccountInfoQuery();
		accountInfoQuery2.setAccountPurpose(Constants.ACCOUNT_PURPOSE_4);//提现卡类型
		accountInfoQuery2.setAccountName("18701514648");//用户Id
		accountInfoQuery2.setRootInstCd("M000001");//机构号
		accountList.add(accountInfoQuery2);
		List<AccountInfo> accInforList = accountInfoDao.queryByUserIdAndPurpose(accountList);
		logger.info(""+accInforList);
	}

}
