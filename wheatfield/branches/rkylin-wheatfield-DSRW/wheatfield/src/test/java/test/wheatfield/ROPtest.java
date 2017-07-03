package test.wheatfield;

import com.Rop.api.ApiException;
import com.Rop.api.DefaultRopClient;
import com.Rop.api.RopResponse;
import com.Rop.api.request.ExternalSessionGetRequest;
import com.Rop.api.request.WheatfieldCityQueryRequest;
import com.Rop.api.request.WheatfieldRepaymentplanQueryRequest;
import com.Rop.api.request.WheatfieldUserDeductRequest;
import com.Rop.api.response.ExternalSessionGetResponse;

public class ROPtest extends BaseJUnit4Test   {

	 public static void main(String[] args) {
	        String jsonOrXml = "xml";
	        String appKey = "857F5C39-884C-470F-9FA7-DCABFD558ABE";
	        String appSecret = "C2F29B5A-947D-4783-9988-348DACF9F0F0";
	        String ropUrl = "https://testapi.open.ruixuesoft.com:30005/ropapi";
	        DefaultRopClient ropClient = new DefaultRopClient(ropUrl, appKey,appSecret, jsonOrXml);
//	        JrdLoanAvailableRequest jrdLoanAvailableRequest = new JrdLoanAvailableRequest();
//	        jrdLoanAvailableRequest.setMerid("KEZHAN");
//	        jrdLoanAvailableRequest.setMercaporderid("ord00001");
//	        jrdLoanAvailableRequest.setMeruser("s00001");
//	        jrdLoanAvailableRequest.setMerloanorderid("u0001");
//	        jrdLoanAvailableRequest.setMerprovider("0001");
//	        WheatfieldRepaymentplanQueryRequest aaa = new WheatfieldRepaymentplanQueryRequest();
//	        aaa.setOrderid("2015061110064500001");
//	        aaa.setUserid("1");
//	        aaa.setRootinstid("KEZHAN");
//	        aaa.setType("2");
//	        aaa.setUserorderid("5");
//	        jrdLoanAvailableRequest.setCpamount("432");//单位是分
	//
//	        jrdLoanAvailableRequest.setCplaunchtime("2015-06-08");
//	        jrdLoanAvailableRequest.setCrmodeid("x0001");
//	        jrdLoanAvailableRequest.setAccountloanorderid("ac000001");
	        //WheatfieldUserDeductRequest aaa=new WheatfieldUserDeductRequest();
	        //WheatfieldCityQueryRequest aaa=new WheatfieldCityQueryRequest();
	        WheatfieldCityQueryRequest city=new WheatfieldCityQueryRequest();
	        try {
	            RopResponse rsp = ropClient.execute(city,sessionGet(ropUrl,appKey,appSecret));
	            System.out.println(rsp.isSuccess());
	            
	            System.out.println(rsp.getErrorCode());
	            System.out.println(rsp.getMsg());
	        } catch (ApiException e1) {
	            e1.printStackTrace();
	        }
	    }


	    public static  String sessionGet(String ropUrl,String appKey,String appSecret) {
	        String sessionKey = null;
	        DefaultRopClient ropClient = new DefaultRopClient(ropUrl, appKey,appSecret);
	        try {
	            ExternalSessionGetRequest sessionGetReq = new ExternalSessionGetRequest();

	            ExternalSessionGetResponse sessionGetRsp = ropClient.execute(sessionGetReq);
	            sessionKey = sessionGetRsp.getSession();
	        } catch (Exception ex) {
	            System.out.println(ex.getMessage());
	        } finally {

	        }
	        return sessionKey;
	    }
	
}
