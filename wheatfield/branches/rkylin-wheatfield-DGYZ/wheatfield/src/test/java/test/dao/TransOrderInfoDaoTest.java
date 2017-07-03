package test.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.constant.TransCodeConst;
import com.rkylin.wheatfield.dao.TransDaysSummaryDao;
import com.rkylin.wheatfield.dao.TransOrderInfoDao;
import com.rkylin.wheatfield.pojo.GenerationPayment;
import com.rkylin.wheatfield.pojo.TransDaysSummary;
import com.rkylin.wheatfield.pojo.TransOrderInfo;

/**
 * Created by thonny on 2015-5-11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath*:spring/applicationContext.xml"})
public class TransOrderInfoDaoTest extends AbstractJUnit4SpringContextTests {
	private static Logger logger = LoggerFactory.getLogger(TransOrderInfoDaoTest.class);
	
	@Autowired
	@Qualifier("transOrderInfoDao")
	private TransOrderInfoDao transOrderInfoDao;
	
	@Test
	public void selectByGenIdTest() {
		List<GenerationPayment> list = new ArrayList<GenerationPayment>();
		GenerationPayment g = new GenerationPayment();
		g.setGeneId(2016010300000004L);
		list.add(g);
		List<TransOrderInfo> l = transOrderInfoDao.selectByGenId(list);
		logger.info(l+"");
	}
	
	@Test
	public void updateBatchTest() {
//		List<Map<String,Object>> l = new ArrayList<Map<String,Object>>();
//		Map map = new HashMap();
//		map.put("status", 4);
//		map.put("orderNo", "S4000000000000003");
//		map.put("merchantCode", "M000001");
//		l.add(map);
//		Map map2 = new HashMap();
//		map2.put("status", 4);
//		map2.put("orderNo", "S5000000000000000");
//		map2.put("merchantCode", "M000001");
//		l.add(map2);
		
		List<TransOrderInfo> l = new ArrayList<TransOrderInfo>();
		TransOrderInfo order = new TransOrderInfo();
		order.setStatus(100);
		order.setOrderNo("1448338313719442196AA");
		order.setMerchantCode("M000001");
		l.add(order);
		TransOrderInfo order1 = new TransOrderInfo();
		order1.setStatus(101);
		order1.setOrderNo("14483383137184038983AA");
		order1.setMerchantCode("M000001");
		l.add(order1);
		transOrderInfoDao.batchUpdateStatusByOrderNoAndMerCode(l);
	}

}
