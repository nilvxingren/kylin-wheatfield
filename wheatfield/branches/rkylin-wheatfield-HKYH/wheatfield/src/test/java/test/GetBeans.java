package test;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.rkylin.crps.service.CrpsApiService;
import com.rkylin.wheatfield.api.AccountService;

public class GetBeans {
	private static Logger logger = LoggerFactory.getLogger(GetBeans.class);

	// public static ApplicationContext applicationContext = new
	// ClassPathXmlApplicationContext("resource/applicationContext.xml");
	public static ApplicationContext app = new ClassPathXmlApplicationContext(
			"spring-web.xml");

	@Test
	public void refund() {
		AccountService accountService = (AccountService) app.getBean("getGenerationPaymentService");
		logger.info("accountService=="+accountService.toString());

	}
	
	@Test
	public void test() {
		CrpsApiService crpsApiService = (CrpsApiService) app.getBean("crpsApiService");
		logger.info("crpsApiService=="+crpsApiService.toString());

	}

	// public static SqlMapClientTemplate getSqlMapClientTemplate(){
	// return
	// (SqlMapClientTemplate)applicationContext.getBean("sqlMapClientTemplate");
	// }

	// public static SqlSession getSqlSession(){
	// return (SqlSession)applicationContext.getBean("sqlSession");
	// }
	//
	// public static JdbcTemplate getJdbcTemplate(){
	// return (JdbcTemplate)applicationContext.getBean("jdbcTemplate");
	// }
	//
	// public static JdbcTemplate getOutJdbcTemplate(){
	// return (JdbcTemplate)applicationContext.getBean("outJdbcTemplate");
	// }
	//
	// @SuppressWarnings("rawtypes")
	// public static BaseDaoJdbcTemplate getBaseDaoJdbcTemplate(){
	// return
	// (BaseDaoJdbcTemplate)applicationContext.getBean(BaseDaoJdbcTemplate.DAO_NAME);
	// }
	//
	// public static DatasService getDatasService(){
	// return
	// (DatasService)applicationContext.getBean(DatasService.SERVICE_NAME);
	// }
	//
	// public static DependencyInjection getDependencyInjection(){
	// return
	// (DependencyInjection)applicationContext.getBean("com.server.config.DependencyInjection");
	// }
}
