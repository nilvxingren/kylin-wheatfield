package com.rkylin.wheatfield.common;

import com.Rop.api.DefaultRopClient;
import com.Rop.api.request.ExternalSessionGetRequest;
import com.Rop.api.response.ExternalSessionGetResponse;

public class SessionUtils {
	public static String sessionGet(String ropUrl,String appKey,String appSecret) {
		String sessionKey = null;
//		String ropUrl=PropertiesUtils.get("ROP_URL");
//		String appKey=PropertiesUtils.get("APP_KEY");
//		String appSecret=PropertiesUtils.get("APP_SECRET");
//		  String ropUrl = "https://115.159.25.44:30005/ropapi";
//		  String appKey = "7E59D764-6484-4F77-9DB1-0EB953E55DBC";
//		  String appSecret = "2845A40B-1D90-44CD-92B1-69FB56009EC1";
		DefaultRopClient ropClient = new DefaultRopClient(ropUrl, appKey,
				appSecret);
		try {
			ExternalSessionGetRequest sessionGetReq = new ExternalSessionGetRequest();

			ExternalSessionGetResponse sessionGetRsp = ropClient
					.execute(sessionGetReq);
			sessionKey = sessionGetRsp.getSession();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		} finally {

		}
		return sessionKey;
	}
}
