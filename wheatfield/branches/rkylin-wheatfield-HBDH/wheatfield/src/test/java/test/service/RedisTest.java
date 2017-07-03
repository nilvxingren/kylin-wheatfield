package test.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.rkylin.crps.pojo.OrderDetail;
import com.rkylin.crps.pojo.OrderDetails;
import com.rkylin.wheatfield.common.RedisBase;
import com.rkylin.wheatfield.constant.RedisConstants;

import test.wheatfield.BaseJUnit4Test;

public class RedisTest extends BaseJUnit4Test {
	private static Logger logger = LoggerFactory.getLogger(RedisTest.class);
	
	@Autowired
	RedisBase redisBase;
	String key="M000001_ORDER_NOS_PUSH_20151109";
	
	@Test
	public void saveOrUpdateSetTest() {
		for (int i = 0; i < 1; i++) {
			Set<String> orderNoSet = new HashSet<String>();
			orderNoSet.add("wwwwwwwwwwwwww");
			redisBase.saveOrUpdateSet(key, orderNoSet, 2, TimeUnit.MINUTES);;
			logger.info("========i========"+i+"    "+redisBase.getSet(key));;
//			redisBase.delSet(key);	
		}
	}

	@Test
	public void getDelTest() {
		String key = "M000001_ORDER_NOS_PUSH_20151218";
		logger.info("11111===="+redisBase.getSet(key));;
		redisBase.delSet(key);;
		logger.info("3333===="+redisBase.getSet(key));;

	}
	
	@Test
	public void getSetTest() {
		String key = "HFYRHFYRHFRYF";
		redisBase.saveOrUpdateStrSet(RedisConstants.GENERATIONPAYMENT_PUSH, key, 4, TimeUnit.HOURS, false);
		List<OrderDetail> orderDetailList = new ArrayList();
		OrderDetail orderDe = new OrderDetail();
		orderDe.setOrderNo("1451359098219182554");
		orderDe.setStatusId(15);
		orderDetailList.add(orderDe);
		redisBase.saveOrUpdateList(key, orderDetailList, 350, TimeUnit.MINUTES);
		logger.info("-----------------------------------------"+redisBase.getList(key));;
	}
	
}
