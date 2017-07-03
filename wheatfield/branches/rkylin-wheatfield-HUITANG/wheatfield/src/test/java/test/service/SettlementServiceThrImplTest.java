package test.service;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.wheatfield.BaseJUnit4Test;

import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.constant.SettleConstants;
import com.rkylin.wheatfield.constant.TransCodeConst;
import com.rkylin.wheatfield.service.SettlementServiceThr;

public class SettlementServiceThrImplTest   extends BaseJUnit4Test{

	@Autowired
	private SettlementServiceThr settlementServiceThr;
	
	@Test
	public void paymentGeneationTest(){
		// 代付
		settlementServiceThr.paymentGeneration(TransCodeConst.PAYMENT_WITHHOLD, Constants.HT_ID,
				SettleConstants.ORDER_WITHHOLD,
				SettleConstants.WITHHOLD_CODE,0);
		
		
	}
	
	
	
	@Test
	public void testTT(){
		Map<String,String> resultMap = settlementServiceThr.getCreditInfo("23", "2015-11-09", "");
		if(resultMap != null && !resultMap.isEmpty()){
			Set<String> set = resultMap.keySet();
			Iterator<String> it = set.iterator();
			while(it.hasNext()){
				System.out.println(resultMap.get(it.next()));
			}
		}
	}
}
