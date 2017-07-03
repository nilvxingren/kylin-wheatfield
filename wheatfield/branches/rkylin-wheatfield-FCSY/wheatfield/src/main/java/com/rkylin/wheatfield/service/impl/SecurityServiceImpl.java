package com.rkylin.wheatfield.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.constant.ErrorCodeConstants;
import com.rkylin.wheatfield.response.Response;
import com.rkylin.wheatfield.service.IErrorResponseService;
import com.rkylin.wheatfield.service.ISecurityService;
import com.rkylin.wheatfield.utils.SignUtils;





public class SecurityServiceImpl implements ISecurityService {

	private static final String APP_KEY = "APP_KEY";
	
	private static final String APP_SECRET = "APP_SECRET";
	
	private Properties userProperties;

	private IErrorResponseService errorResponseService;
	
	public Properties getUserProperties() {
		return userProperties;
	}

	public void setUserProperties(Properties userProperties) {
		this.userProperties = userProperties;
	}

	public IErrorResponseService getErrorResponseService() {
		return errorResponseService;
	}

	public void setErrorResponseService(IErrorResponseService errorResponseService) {
		this.errorResponseService = errorResponseService;
	}

	@Override
	public Response verifyRequest(Map<String, String[]> requestParams){

		Response response = null;

		try {
			// 如果请求中不包含任何参数
			if(this.hasNoParam(requestParams)){
				return errorResponseService.getErrorResponse(ErrorCodeConstants.SYS_ERROR_CODE_S1);
			}

			// 如果没有传递app_key
			if(!this.hasParam(requestParams, Constants.SYS_PARAM_APP_KEY)){
				return errorResponseService.getErrorResponse(ErrorCodeConstants.SYS_ERROR_CODE_S2);
			}

			// 如果app_key不存在
			if(!this.verifyAppKey(requestParams.get(Constants.SYS_PARAM_APP_KEY)[0])){
				return errorResponseService.getErrorResponse(ErrorCodeConstants.SYS_ERROR_CODE_S3);
			}

			// 如果没有传递method
			if(!this.hasParam(requestParams, Constants.SYS_PARAM_METHOD)){
				return errorResponseService.getErrorResponse(ErrorCodeConstants.SYS_ERROR_CODE_S4);
			}

//			// 如果没有传递format
//			if(!this.hasParam(requestParams, Constants.SYS_PARAM_FORMAT)){
//				return errorResponseService.getErrorResponse(ErrorCodeConstants.SYS_ERROR_CODE_S5);
//			}
//
//			// 如果没有传递sign
//			if(!this.hasParam(requestParams, Constants.SYS_PARAM_SIGN)){
//				return errorResponseService.getErrorResponse(ErrorCodeConstants.SYS_ERROR_CODE_S7);
//			}
//			
//			// 如果签名验证无效
//			String secret = userProperties.getProperty(APP_SECRET);
//			if(!this.checkSign(requestParams, secret)){
//				return errorResponseService.getErrorResponse(ErrorCodeConstants.SYS_ERROR_CODE_S11);
//			}
		} catch (Exception e) {
			e.printStackTrace();
			response = errorResponseService.getErrorResponse(ErrorCodeConstants.SYS_ERROR_CODE_S0);
		}

		return response;
	}
	
	/**
	 * 判断请求中是否包含参数
	 * 
	 * @param requestParams
	 * @return
	 */
	private boolean hasNoParam(Map<String, String[]> requestParams){

		boolean checkFlag = false;

		// 请求中没有包含任何参数
		if(requestParams == null || requestParams.isEmpty()){
			checkFlag = true;
		}

		return checkFlag;
	}

	/**
	 * 验证签名是否有效
	 * 
	 * @param requestParams
	 * @param secret
	 * @return
	 * @throws IOException
	 */
	private boolean checkSign(Map<String, String[]> requestParams, String secret) throws IOException{
		
		boolean checkFlag = false;
		
		// 取得请求中的签名
		String requestSign = requestParams.get(Constants.SYS_PARAM_SIGN)[0];

		// 将去除sign的请求参数签名
		Map<String, String[]> signParams = new HashMap<String, String[]>();
		signParams.putAll(requestParams);
		signParams.remove(Constants.SYS_PARAM_SIGN);
		
		String checkSign = SignUtils.signRopRequest(signParams, secret);
		
		// 如果签名结果与请求中的签名相等，则验证通过
		if(requestSign.equals(checkSign)){
			checkFlag = true;
		}

		return checkFlag;
	}
	
	/**
	 * 根据key检查请求中是否包含某特定参数
	 * 
	 * @param requestParams
	 * @param key
	 * @return
	 */
	private boolean hasParam(Map<String, String[]> requestParams, String key){

		boolean checkFlag = false;

		if(requestParams.containsKey(key) && requestParams.get(key) != null){
			checkFlag = true;
		}

		return checkFlag;
	}
	
	/**
	 * 验证APP_KEY是否有效
	 * 
	 * @param appKey
	 * @return
	 */
	private boolean verifyAppKey(String appKey){

		boolean checkFlag = false;
		
		String sysAppKey = userProperties.getProperty(APP_KEY);

		if(sysAppKey != null && sysAppKey.equals(appKey)){
			checkFlag = true;
		}

		return checkFlag;
	}
}
