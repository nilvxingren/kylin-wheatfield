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
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.constant.TransCodeConst;
import com.rkylin.wheatfield.dao.TransDaysSummaryDao;
import com.rkylin.wheatfield.pojo.TransDaysSummary;

/**
 * Created by thonny on 2015-5-11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath*:spring/applicationContext.xml"})
public class TransDaysSummaryDaoTest extends AbstractJUnit4SpringContextTests {
	private static Logger logger = LoggerFactory.getLogger(TransDaysSummaryDaoTest.class);
	
	@Autowired
	TransDaysSummaryDao transDaysSummaryDao;

	@Autowired
	RedisIdGenerator redisIdGenerator;
	
	@Test
	public void insertBatchTest() {
		List l = new ArrayList();
		for (int i = 2; i < 4; i++) {
			TransDaysSummary transDaysSummary=new TransDaysSummary();
			transDaysSummary.setTransSumId(redisIdGenerator.createRequestNo());//汇总订单号
			transDaysSummary.setRootInstCd("M000002");//机构号
			transDaysSummary.setOrderType("6");//订单类型--代收
			transDaysSummary.setAccountDate(DateUtils.getDate("2015-11-10", Constants.DATE_FORMAT_YYYYMMDD));//账单日期
			transDaysSummary.setSummaryAmount(1l);//汇总金额
			transDaysSummary.setUserId("8989");
			transDaysSummary.setSummaryOrders(i+"AABBCC");//订单Id（汇总）
			l.add(transDaysSummary);
		}
		transDaysSummaryDao.insertBatch(l);
		
	}

}
