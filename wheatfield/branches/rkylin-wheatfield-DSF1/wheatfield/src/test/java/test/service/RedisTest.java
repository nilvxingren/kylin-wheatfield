package test.service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.rkylin.wheatfield.common.RedisBase;
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
		String key = "M000001_ORDER_NOS_PUSH_20151118";
		logger.info("11111===="+redisBase.getSet(key));;
		redisBase.delSet("GENERATIONPAYMENT_PUSH");;
		logger.info("3333===="+redisBase.getSet(key));;

	}
	
	@Test
	public void getSetTest() {
		logger.info("-----------------------------------------"+redisBase.getSet("GENERATIONPAYMENT_PUSH"));;
	}
	
}
