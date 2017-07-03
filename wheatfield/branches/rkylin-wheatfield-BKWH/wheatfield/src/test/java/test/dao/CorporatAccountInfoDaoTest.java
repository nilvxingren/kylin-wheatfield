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
import org.springframework.beans.factory.annotation.Qualifier;
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
import com.rkylin.wheatfield.dao.CorporatAccountInfoDao;
import com.rkylin.wheatfield.dao.GenerationPaymentDao;
import com.rkylin.wheatfield.dao.TransDaysSummaryDao;
import com.rkylin.wheatfield.pojo.AccountInfo;
import com.rkylin.wheatfield.pojo.AccountInfoQuery;
import com.rkylin.wheatfield.pojo.CorporatAccountInfo;
import com.rkylin.wheatfield.pojo.CorporatAccountInfoQuery;
import com.rkylin.wheatfield.pojo.GenerationPayment;
import com.rkylin.wheatfield.pojo.TransDaysSummary;

/**
 * Created by thonny on 2015-5-11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath*:spring/applicationContext.xml"})
public class CorporatAccountInfoDaoTest extends AbstractJUnit4SpringContextTests {
	private static Logger logger = LoggerFactory.getLogger(CorporatAccountInfoDaoTest.class);
	

	@Autowired
	@Qualifier("corporatAccountInfoDao")
	private CorporatAccountInfoDao corporatAccountInfoDao;
	
	@Test
	public void queryByAccountNoTest() {
		List<CorporatAccountInfoQuery> list = new ArrayList<CorporatAccountInfoQuery>();
		CorporatAccountInfoQuery accountInfoQuery =new CorporatAccountInfoQuery();
		accountInfoQuery.setAccountNumber("9876");
		accountInfoQuery.setRootInstCd("M000003");//机构号
		list.add(accountInfoQuery);
		CorporatAccountInfoQuery accountInfoQuery2 =new CorporatAccountInfoQuery();
		accountInfoQuery2.setAccountNumber("3242424");
		accountInfoQuery2.setRootInstCd("M000001");//机构号
		list.add(accountInfoQuery2);
		List<CorporatAccountInfo> accInforList= corporatAccountInfoDao.queryByAccountNo(list);
		logger.info(""+accInforList);
	}

}
