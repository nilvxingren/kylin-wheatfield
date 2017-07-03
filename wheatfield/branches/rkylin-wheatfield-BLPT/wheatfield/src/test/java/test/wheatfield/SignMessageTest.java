package test.wheatfield;

import java.util.Iterator;

import javax.annotation.Resource;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.rkylin.wheatfield.pojo.AccountXqsDetial;
import com.rkylin.wheatfield.service.UtilsResponseService;

public class SignMessageTest extends BaseJUnit4Test {
	@Autowired
	@Qualifier("utilsResponseService")
	UtilsResponseService utilsResponseService;
	@Test
	public  void signImport() throws Exception {
		String req_sn = "sr_001";
		String contractno = "sr_002";
		String merchant_id = "M000001";
		String signinfodetailarray = "<signinfodetails>"+
				"<signinfodetail>"+
				"<agreementno>M00000X6217710702946088</agreementno>"+
				"<mobile>13810253710</mobile>"+
				"<acct>6217710702946088</acct>"+
				"<acname>毛伟宁</acname>"+
				"<bankcode>302</bankcode>"+
				"<bankprov>北京</bankprov>"+
				"<bankcity>北京</bankcity>"+
				"<bankname>中信银行</bankname>"+
				"<cnapsno>302100011219</cnapsno>"+
				"<accttype></accttype>"+
				"<acctprop></acctprop>"+
				"<idtype></idtype>"+
				"<idno></idno>"+
				"<maxsignleamt>1000</maxsignleamt>"+
				"<daymaxsucccnt>1000</daymaxsucccnt>"+
				"<daymaxsuccamt>1000</daymaxsuccamt>"+
				"<monmaxsucccnt>1000</monmaxsucccnt>"+
				"<monmaxsuccamt>1000</monmaxsuccamt>"+
				"<agrdeadline>201508110211</agrdeadline>"+
				"</signinfodetail>"+
				"<signinfodetail>"+
				"<agreementno>M00000X6226220124444703</agreementno>"+
				"<mobile>13810253710</mobile>"+
				"<acct>6226220124444703</acct>"+
				"<acname>毛伟宁</acname>"+
				"<bankcode>305</bankcode>"+
				"<bankprov>北京</bankprov>"+
				"<bankcity>北京</bankcity>"+
				"<bankname>中国民生银行</bankname>"+
				"<cnapsno>305100001442</cnapsno>"+
				"<accttype></accttype>"+
				"<acctprop></acctprop>"+
				"<idtype></idtype>"+
				"<idno></idno>"+
				"<maxsignleamt>1000</maxsignleamt>"+
				"<daymaxsucccnt>1000</daymaxsucccnt>"+
				"<daymaxsuccamt>1000</daymaxsuccamt>"+
				"<monmaxsucccnt>1000</monmaxsucccnt>"+
				"<monmaxsuccamt>1000</monmaxsuccamt>"+
				"<agrdeadline>201508110211</agrdeadline>"+
				"</signinfodetail>"+
				"</signinfodetails>";

		AccountXqsDetial accountXqsDetial=utilsResponseService.impSignmessage(req_sn, contractno,merchant_id, signinfodetailarray);
		System.out.println("Code:" + accountXqsDetial.getRet_code());
		System.out.println("Message:" + accountXqsDetial.getRet_message());
	}
	
}
