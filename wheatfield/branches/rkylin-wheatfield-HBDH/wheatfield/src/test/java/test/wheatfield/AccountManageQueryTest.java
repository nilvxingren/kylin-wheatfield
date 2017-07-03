/**
*
*/
package test.wheatfield;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.rkylin.wheatfield.response.AccountInfoGetResponse;
import com.rkylin.wheatfield.service.AccountManageQueryService;

/**
 * Description:	   
 * Date:          2015年12月3日 下午4:00:27 
 * @author        sun guoxing
 * @version       1.0 
 */
public class AccountManageQueryTest extends BaseJUnit4Test {
	@Autowired
	@Qualifier("accountManageQueryService")
	private AccountManageQueryService accountManageQueryService;//readP2PDebtFile
	
	@Test
	public void testFinanaceEntryList() {
//		List<FinanaceEntryDto> rtnList = new ArrayList<FinanaceEntryDto>();
		AccountInfoGetResponse reponse = accountManageQueryService.queryFinanaceentryList("M000005", "141223100000013", 
				"P0002 ", "2015-10-01", "2015-12-01", "8992874", "1");
		System.out.println("+++++++++++++++++++++++++++++++++++++  is_success:" + reponse.isIs_success() + ", error_msg:" + reponse.getMsg() );
		if(reponse.isIs_success()){
			System.out.println("----------------------------listSize:" + reponse.getFinanaceEntryList().size());
		}
//		System.out.println("内容如下：");
//		for (CreditInfo abc:rtnList) {
//			System.out.println("**************************");
//			System.out.println("授信号："+abc.getId());
//			//System.out.println("商户ID："+abc.getMerchantId());
//			System.out.println("用户ID："+abc.getUserId());
//			System.out.println("授信协议ID："+abc.getCreditAgreementId());
//			System.out.println("授信种类ID："+abc.getCreditTypeId());
//			System.out.println("授信金额："+abc.getAmount());
//			System.out.println("授信利率："+abc.getRate());
//			System.out.println("币种："+abc.getCurrency());
//			System.out.println("生效时间："+abc.getStartTime());
//			System.out.println("失效时间："+abc.getEndTime());
//		}
	}
	
	public static void t() {
		String url ="http://localhost:8080/wheatfield/ropapi?"
				+ "rootinstcd=M000005&"
				+ "userid=141223100000013&"
				+ "productid=P000022&"
				+ "createdtimefrom=2015-10-01&"
				+ "createdtimeto=2015-12-01&"
				+ "requestid=1&"
				+ "querytype=1&"
				//+ "useripaddress=127.0.0.1&"
				+ "method=ruixue.wheatfield.finanace.entrylist.query&"
				+ "format=json&"
				+ "timestamp=2015-12-03%2013%3A55%3A01";
		System.out.println("url:----------------------------" + url);
		HttpClient client = new HttpClient();
		HttpMethod method = new GetMethod(url);
		try {
			client.executeMethod(method);
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		t();
	}
}
