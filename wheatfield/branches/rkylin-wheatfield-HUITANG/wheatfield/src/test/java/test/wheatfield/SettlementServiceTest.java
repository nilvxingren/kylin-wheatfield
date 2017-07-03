/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package test.wheatfield;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.rkylin.wheatfield.pojo.CreditInfo;
import com.rkylin.wheatfield.response.ErrorResponse;
import com.rkylin.wheatfield.service.SettlementService;
import com.rkylin.wheatfield.settlement.SettlementLogic;

import test.wheatfield.BaseJUnit4Test;

public class SettlementServiceTest extends BaseJUnit4Test {

	@Autowired
	@Qualifier("settlementService")
	private SettlementService settlementService;//readP2PDebtFile
	

	@Autowired
	private SettlementLogic settlementLogic;
	
	@Autowired
	Properties userProperties;

	@Test
	public void testRopupload() {
    	SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Map<String, String> rtnMap = new HashMap<String, String>();
		try {
			rtnMap = settlementLogic.setFileFromROP(13
					, "1234"
					, formatter1.parse("2015-07-01 00:00:00")
					, "Z:\\下载\\fr1234.csv"
					, userProperties.getProperty("FN_PUBLIC_KEY")
					, 2
					, userProperties.getProperty("FSAPP_KEY")
					, userProperties.getProperty("FSDAPP_SECRET")
					, userProperties.getProperty("FSROP_URL"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(rtnMap.get("errCode")+"+++++"+rtnMap.get("errMsg"));
	}
	

	public void testgetFileFromROP() {
    	SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Map<String, String> rtnMap = new HashMap<String, String>();
		try {
			rtnMap = settlementLogic.getFileFromROP(11
					, ""
					, formatter1.parse("2015-07-01 00:00:00")
					, "C:\\test\\testsun1234.csv"
					, userProperties.getProperty("FN_PUBLIC_KEY")
					, 2
					, userProperties.getProperty("FSAPP_KEY")
					, userProperties.getProperty("FSDAPP_SECRET")
					, userProperties.getProperty("FSROP_URL"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(rtnMap.get("errCode")+"+++++"+rtnMap.get("errMsg"));
	}
	
	public void testReadP2PDebtFile() {
		ErrorResponse file = settlementService.readP2PDebtFile(null);
		System.out.println("返回结果："+file.getCode() + "||" + file.getMsg());
	}
	
	public void testCreateP2PDebtFile() {
		String file = settlementService.createP2PDebtFile(null);
		System.out.println("返回结果："+file);
	}
	

	public void testCreateDebtFile() {
		String file = settlementService.createDebtAccountFile(null);
		System.out.println("返回结果："+file);
	}
	

	public void testGetCreditInfo() {
		List<CreditInfo> rtnList = new ArrayList<CreditInfo>();
		rtnList = settlementService.getCreditInfo(null);
		System.out.println("返回结果件数："+rtnList.size());
		System.out.println("内容如下：");
		for (CreditInfo abc:rtnList) {
			System.out.println("**************************");
			System.out.println("授信号："+abc.getId());
			//System.out.println("商户ID："+abc.getMerchantId());
			System.out.println("用户ID："+abc.getUserId());
			System.out.println("授信协议ID："+abc.getCreditAgreementId());
			System.out.println("授信种类ID："+abc.getCreditTypeId());
			System.out.println("授信金额："+abc.getAmount());
			System.out.println("授信利率："+abc.getRate());
			System.out.println("币种："+abc.getCurrency());
			System.out.println("生效时间："+abc.getStartTime());
			System.out.println("失效时间："+abc.getEndTime());
		}
	}
}
