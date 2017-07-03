package test.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.wheatfield.BaseJUnit4Test;

import com.rkylin.wheatfield.common.DateUtils;
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.constant.SettleConstants;
import com.rkylin.wheatfield.constant.TransCodeConst;
import com.rkylin.wheatfield.service.GenerationPaymentService;
import com.rkylin.wheatfield.service.SettlementServiceThr;

public class SettlementServiceThrImplTest   extends BaseJUnit4Test{

	@Autowired
	private SettlementServiceThr settlementServiceThr;
	@Autowired 
	private GenerationPaymentService generationPaymentService;
	
	@Test
	public void paymentGeneationTest(){
		//客栈
//		settlementServiceThr.paymentGeneration(TransCodeConst.PAYMENT_COLLECTION, Constants.KZ_ID,
//				SettleConstants.ORDER_COLLECTION,
//				SettleConstants.COLLECTION_CODE,1);
		
		String accountDate="2016-1-6";
		try {
			generationPaymentService.submitToRecAndPaySys
			(6,Constants.FN_ID,accountDate,DateUtils.getDate(accountDate, Constants.DATE_FORMAT_YYYYMMDD));
		} catch (Exception e) {
			e.printStackTrace();
		}
//		settlementServiceThr.paymentGeneration(
//				TransCodeConst.PAYMENT_WITHHOLD, Constants.HT_CLOUD_ID,
//				SettleConstants.ORDER_WITHHOLD,
////				SettleConstants.WITHHOLD_BATCH_CODE,1);
//				SettleConstants.WITHHOLD_BATCH_CODE_OLD,1);
		
		//会堂代收
//		settlementServiceThr.paymentGeneration(
//				TransCodeConst.PAYMENT_COLLECTION, Constants.HT_ID,
//				SettleConstants.ORDER_COLLECTION,
//				SettleConstants.COLLECTION_CODE_OLD,1);
		
		// 代付
//		settlementServiceThr.paymentGeneration(TransCodeConst.PAYMENT_WITHHOLD, Constants.HT_ID,
//				SettleConstants.ORDER_WITHHOLD,
//				SettleConstants.WITHHOLD_CODE,0);
		
//		settlementServiceThr.paymentGeneration(TransCodeConst.PAYMENT_COLLECTION, Constants.ZK_ID,
//				SettleConstants.ORDER_COLLECTION,
//				SettleConstants.COLLECTION_CODE,0);
		// 丰年代shou
//		settlementServiceThr.paymentGeneration(TransCodeConst.PAYMENT_COLLECTION, Constants.FN_ID,
//				SettleConstants.ORDER_COLLECTION,
//				SettleConstants.COLLECTION_CODE,1);
//		
//		settlementServiceThr.paymentGeneration(TransCodeConst.WITHDROW, Constants.FN_ID,
//				SettleConstants.ORDER_WITHDRAW,
//				SettleConstants.COLLECTION_CODE,1);
		
		// 君融贷提现
//			settlementServiceThr.paymentGeneration(TransCodeConst.WITHDROW, Constants.JRD_ID,SettleConstants.ORDER_WITHDRAW,
//					SettleConstants.WITHHOLD_CODE,1);
//		settlementServiceThr.paymentGeneration(TransCodeConst.WITHDROW, Constants.JRD_ID,SettleConstants.ORDER_WITHDRAW,
//				SettleConstants.WITHHOLD_CODE_OLD,1);
	}
}
