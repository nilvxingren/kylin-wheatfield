package test.wheatfield;

import com.Rop.api.ApiException;
import com.Rop.api.DefaultRopClient;
import com.Rop.api.RopResponse;
import com.Rop.api.request.ExternalSessionGetRequest;
import com.Rop.api.request.WheatfieldPersonAccountoprRequest;
import com.Rop.api.response.ExternalSessionGetResponse;

public class ThreadTest extends BaseJUnit4Test {

	
	public  static void creatPerson(){
		for (int i = 0; i < 10; i++) {
			Thread t=new Thread(new Runnable() {				
				@Override
				public void run() {
					openPerson();
				}				
			});
			t.start();
			System.out.println(t.getName());
			
		}		
	}
	
	public static void main(String[] args) {
		creatPerson();
	}
	
	public static void openPerson(){
		String jsonOrXml = "xml";
        String appKey = "857F5C39-884C-470F-9FA7-DCABFD558ABE";
        String appSecret = "C2F29B5A-947D-4783-9988-348DACF9F0F0";
        String ropUrl = "https://testapi.open.ruixuesoft.com:30005/ropapi";
        DefaultRopClient ropClient = new DefaultRopClient(ropUrl, appKey,appSecret, jsonOrXml);
        WheatfieldPersonAccountoprRequest openPersonRequest=new WheatfieldPersonAccountoprRequest();
        openPersonRequest.setPersonchnname("TDTEST05");
        openPersonRequest.setCertificatetype("0");
        openPersonRequest.setCertificatenumber("1234567892");
        openPersonRequest.setUserid("RZL_TD_TEST05");
        openPersonRequest.setConstid("M000003");
        openPersonRequest.setProductid("P000005");
        openPersonRequest.setOpertype("1");        
        try {
            RopResponse rsp = ropClient.execute(openPersonRequest,sessionGet(ropUrl,appKey,appSecret));
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
