package test.wheatfield;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.rkylin.common.RedisIdGenerator;
import com.rkylin.wheatfield.common.DateUtils;
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.constant.SettleConstants;
import com.rkylin.wheatfield.manager.GenerationPaymentManager;
import com.rkylin.wheatfield.pojo.GenerationPayment;
import com.rkylin.wheatfield.pojo.GenerationPaymentQuery;
import com.rkylin.wheatfield.service.GenerationPaymentService;
import com.rkylin.wheatfield.service.SettlementService;
import com.rkylin.wheatfield.service.SettlementServiceThr;
import com.rkylin.wheatfield.settlement.SettlementLogic;

/**
 * 
 * @author  zhenpc@chanjet.com
 * @version 2015年3月30日 下午2:06:55
 */

public class RedisGeneratorTest extends BaseJUnit4Test{
	
	@Autowired
	RedisIdGenerator redisIdGenerator;
	@Autowired
	GenerationPaymentManager generationPaymentManager;
	@Autowired
	SettlementServiceThr settlementServiceThr;
	@Autowired
	GenerationPaymentService generationPaymentService;
	@Autowired
	SettlementLogic settlementLogic;
	
	private static Object lock = new Object();
	
	@Test
	public void withholdToP2P(){
		try {
			settlementServiceThr.withholdToP2P();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
	@Test
	public void rs(){
		int type=7;
		String batch="";
		String rootInstCd="M00000X";
		try {
			synchronized (lock) {
				try {
					//通联代收 type=6  ，代付7	
					String accountDate=generationPaymentService.getAccountDate();
					if(type==6){
						batch=(settlementLogic.getBatchNo(DateUtils.getAccountDate(Constants.DATE_FORMAT_YYYYMMDD, accountDate), SettleConstants.ROP_RECEIVE_BATCH_CODE, rootInstCd));
						logger.info("--------代收批次号-------"+batch);
					}else if(type==7){
						batch=(settlementLogic.getBatchNo(DateUtils.getAccountDate(Constants.DATE_FORMAT_YYYYMMDD, accountDate), SettleConstants.ROP_PAYMENT_BATCH_CODE, rootInstCd));
						logger.info("--------代付批次号-------"+batch);
					}
					generationPaymentService.submitToRecAndPaySys(type, batch,
							rootInstCd,DateUtils.getAccountDate(Constants.DATE_FORMAT_YYYYMMDD, accountDate));
					logger.info("--------------------------扫描数据库提交到代收付系统任务结束--------------------------------------");
				} catch (Exception e) {
					logger.error(e.getMessage());
					logger.info("--------------------------扫描数据库提交到代收付系统任务异常--------------------------------------");
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	public void gentTest(){
//		System.out.println(redisIdGenerator.createAuthCode());
//		System.out.println(redisIdGenerator.createAuthCode());
//		System.out.println(redisIdGenerator.createAuthCode());
//		System.out.println(redisIdGenerator.createAuthCode());
//		System.out.println(redisIdGenerator.createAuthCode());
//		System.out.println(redisIdGenerator.createAuthCode());
//		System.out.println(redisIdGenerator.createAuthCode());
//		System.out.println(redisIdGenerator.createAuthCode());
//		System.out.println(redisIdGenerator.createAuthCode());
//		System.out.println(redisIdGenerator.createAuthCode());
//		System.out.println(redisIdGenerator.createAuthCode());
//		System.out.println(redisIdGenerator.createAuthCode());
//		System.out.println(redisIdGenerator.createBatchRequestNo());
//		System.out.println(redisIdGenerator.createGwBatchPayNo());
//		System.out.println(redisIdGenerator.createGwPayFlowNo());
//		System.out.println(redisIdGenerator.createRequestNo());
	}
	
	public void test(){
		GenerationPaymentQuery query = new GenerationPaymentQuery();
		//query.setOrderNo(StringUtils.arrayToStr(orderNoArray));
		String[] orders=new String[2];
		orders[0]="20150830143301002";
		orders[1]="20150830143301004";
		
		//query.setOrderNo(sbStr.toString().substring(0,sbStr.toString().length()-1));
		List<GenerationPayment> genList =  generationPaymentManager.selectByOrderNo(orders);
		
		System.out.println(genList.size());
		
	}

}
