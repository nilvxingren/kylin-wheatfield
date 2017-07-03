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
import com.rkylin.wheatfield.dao.FinanaceCompanyDao;
import com.rkylin.wheatfield.dao.GenerationPaymentDao;
import com.rkylin.wheatfield.dao.TransDaysSummaryDao;
import com.rkylin.wheatfield.pojo.AccountInfo;
import com.rkylin.wheatfield.pojo.AccountInfoQuery;
import com.rkylin.wheatfield.pojo.FinanaceCompany;
import com.rkylin.wheatfield.pojo.FinanaceCompanyQuery;
import com.rkylin.wheatfield.pojo.GenerationPayment;
import com.rkylin.wheatfield.pojo.TransDaysSummary;

import test.wheatfield.BaseJUnit4Test;

/**
 * Created by thonny on 2015-5-11.
 */
public class FinanaceCompanyDaoTest  extends BaseJUnit4Test {
	private static Logger logger = LoggerFactory.getLogger(FinanaceCompanyDaoTest.class);
	

	@Autowired
	FinanaceCompanyDao finanaceCompanyDao;
	
	@Test
	public void getFinanaceCompaniesTest() {
		FinanaceCompanyQuery query = new FinanaceCompanyQuery();
		query.setRootInstCd("M000001");
		List<FinanaceCompany> list = finanaceCompanyDao.getFinanaceCompanies(query);
		logger.info(""+list);
	}

}
