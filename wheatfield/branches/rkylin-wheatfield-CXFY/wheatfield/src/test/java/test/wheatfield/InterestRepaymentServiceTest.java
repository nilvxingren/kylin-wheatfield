/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package test.wheatfield;

import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.rkylin.wheatfield.service.SettlementServiceFou;
import com.rkylin.wheatfield.service.SettlementServiceThr;

public class InterestRepaymentServiceTest extends BaseJUnit4Test {
	@Autowired
	@Qualifier("settlementServiceFou")
	private SettlementServiceFou settlementServiceFou;
	@Autowired
	@Qualifier("settlementServiceThr")
	private SettlementServiceThr settlementServiceThr;
	
	@Test
	public void test() {
		try {
			Map<String, String> map = settlementServiceFou.collateFN2Alipay("2015-07-10");
			System.out.println("errCode: " + map.get("errCode"));
			System.out.println("errMsg: " + map.get("errMsg"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*@Test
	public void test() {
		String genType;
		String interfaceName; 
		Integer dateType;
		
		String generationType = null;
		Integer orderType = null;
		String bussinessCoded = null;
		String mess = null;
		Integer type = null;
		String batchType = null;
		dateType = 1;
		
		mess = "代付";
		generationType = TransCodeConst.PAYMENT_WITHHOLD;
		orderType = SettleConstants.ORDER_WITHHOLD;
		bussinessCoded = SettleConstants.WITHHOLD_BATCH_CODE;
		batchType = SettleConstants.ROP_PAYMENT_BATCH_CODE;
		type = 7;
		
		settlementServiceThr.paymentGeneration(generationType, Constants.HT_ID, orderType, bussinessCoded, dateType);
	}*/
	
	/*@Autowired
	@Qualifier("settlementServiceSec")
	private SettlementServiceSec settlementServiceSec;//readP2PDebtFile
	
	@Autowired
	@Qualifier("interestRepaymentService")
	private InterestRepaymentServiceImpl interestRepaymentService;
	
	@Autowired
	@Qualifier("settlementService")
	private SettlementService settlementService;
	
	@Autowired
	@Qualifier("settlementServiceThr")
	private SettlementServiceThr settlementServiceThr;

	@Test
	public void test00() {
		Map<String, String> map = settlementServiceThr.withholdToP2P(Constants.KZ_ID, Constants.P2P_ID);
		System.out.println("errMsg ::: " + map.get("errMsg"));
		System.out.println("errCode ::: " + map.get("errCode"));
		InterestRepaymentResponse response = new InterestRepaymentResponse();
		Map<String, String> map = interestRepaymentService.canBeEarlyRepayment("1", "R00001", "KEZHAN", response);
		System.err.println(map.get("errMsg"));
		System.err.println(map.get("errCode"));
	}*/
}
