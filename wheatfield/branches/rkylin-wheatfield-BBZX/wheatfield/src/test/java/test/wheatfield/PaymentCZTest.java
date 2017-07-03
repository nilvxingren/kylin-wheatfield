package test.wheatfield;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.util.HttpURLConnection;
import org.junit.Test;

import com.allinpay.ets.client.PaymentResult;
import com.allinpay.ets.client.SecurityUtil;
import com.allinpay.ets.client.StringUtil;
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.utils.DateUtil;

public class PaymentCZTest {
	public static void main(String[] args) {
		DateUtil dateUtil=new DateUtil();
//		for (int i = 0; i < 10000; i++) {
//			System.out.println(getRandomString(6));
//		}
		//System.out.println(new Date());
//		try {
//			System.out.println(isWithholdInterval(dateUtil.parse("2015-07-15 18:14:51",Constants.DATE_FORMAT_YYYYMMDDHHMMSS )));
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		String aa[]={"M000001","M000005","M000002"};
//		//Arrays.sort(aa);  
//		int a = Arrays.binarySearch(aa, "M000002");
//		System.out.println(a);
		
		String aAmount="5.00";
		Long bAmount=500l;
		
		Long cAmount=Long.parseLong(changeY2F(aAmount)+"");
		System.out.println(cAmount);
		StringBuilder sbStr=new StringBuilder();
		sbStr.append("'1',");
		sbStr.append("'2',");
		System.out.println(sbStr.toString().substring(0,sbStr.toString().length()-1));
		
		
	}
	
	   public static String changeY2F(String amount){    
	        String currency =  amount.replaceAll("\\$|\\￥|\\,", "");  //处理包含, ￥ 或者$的金额    
	        int index = currency.indexOf(".");    
	        int length = currency.length();    
	        Long amLong = 0l;    
	        if(index == -1){    
	            amLong = Long.valueOf(currency+"00");    
	        }else if(length - index >= 3){    
	            amLong = Long.valueOf((currency.substring(0, index+3)).replace(".", ""));    
	        }else if(length - index == 2){    
	            amLong = Long.valueOf((currency.substring(0, index+2)).replace(".", "")+0);    
	        }else{    
	            amLong = Long.valueOf((currency.substring(0, index+1)).replace(".", "")+"00");    
	        }    
	        return amLong.toString();    
	    } 
	
	private static boolean isWithholdInterval(Date date){
		DateUtil dateUtil=new DateUtil();
		boolean isOK=false;
		try {
			//获取当前账期
			Date tDate=dateUtil.parse("2015-07-16", "yyyy-MM-dd");
			System.out.println(tDate);
			//获取T-1日账期
			Calendar cal = Calendar.getInstance();
			cal.setTime(tDate);
			cal.add(Calendar.DATE, -1);			
			Date tDateLast=cal.getTime();
			
			SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_YYYYMMDD);
			
			//设定T-1日的时分秒
			tDateLast=dateUtil.parse(dateFormat.format(tDateLast)+" 23:00:00", Constants.DATE_FORMAT_YYYYMMDDHHMMSS);
			//设定T日的时分秒
			tDate=dateUtil.parse(dateFormat.format(tDate)+" 15:30:00", Constants.DATE_FORMAT_YYYYMMDDHHMMSS);
			if(date.getTime()>tDateLast.getTime()&&date.getTime()<tDate.getTime()){
				isOK=true;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isOK;
	}
	
	private static String getRandomString(int length) { //length表示生成字符串的长度
	    String base = "QWERTYUIOPASDFGHJKLZXCVBNMabcdefghijklmnopqrstuvwxyz0123456789";   
	    Random random = new Random();   
	    StringBuffer sb = new StringBuffer();   
	    for (int i = 0; i < length; i++) {   
	        int number = random.nextInt(base.length());   
	        sb.append(base.charAt(number));   
	    }   
	    return sb.toString();   
	 }  
	
	@Test
	public void validataOrder(){
		DateUtil dateUtil=new DateUtil();
		String serverUrl="http://ceshi.allinpay.com/gateway/index.do?";
		String key="1234567890";
		String merchantId="100020091218001";
		String version="v1.5";
		String signType="0";
		String orderNo="OP20150820162556002";
		String orderDatetime="20150820162556";
		String queryDatetime=dateUtil.getDateTime("yyyyMMddhhmmss");
		StringBuffer bufSignSrc=new StringBuffer();
		StringUtil.appendSignPara(bufSignSrc, "merchantId",merchantId);
		StringUtil.appendSignPara(bufSignSrc, "version", version);
		StringUtil.appendSignPara(bufSignSrc, "signType", signType);
		StringUtil.appendSignPara(bufSignSrc, "orderNo", orderNo);
		StringUtil.appendSignPara(bufSignSrc, "orderDatetime",orderDatetime);
		StringUtil.appendSignPara(bufSignSrc, "queryDatetime",queryDatetime);
		StringUtil.appendLastSignPara(bufSignSrc, "key", key);
		System.out.println("签名字符串："+bufSignSrc.toString());
		String signMsg=SecurityUtil.MD5Encode(bufSignSrc.toString());
	
		// 提交查询请求
		boolean isSuccess = false;
		String resultMsg = "";
		Map<String, String> result = new HashMap<String, String>();
		try{
			String listenUrl=serverUrl;
			HttpClient httpclient=new HttpClient();
			PostMethod postmethod=new PostMethod(listenUrl);
			NameValuePair[] date = { new NameValuePair("merchantId",merchantId),
					new NameValuePair("version",version),
					new NameValuePair("signType",signType),
					new NameValuePair("orderNo",orderNo),
					new NameValuePair("orderDatetime",orderDatetime),
					new NameValuePair("queryDatetime",queryDatetime),
					new NameValuePair("signMsg",signMsg)};
			postmethod.setRequestBody(date);
			int responseCode=httpclient.executeMethod(postmethod);
			System.out.println("responseCode="+responseCode);
			
			if(responseCode == HttpURLConnection.HTTP_OK){
				String strResponse = postmethod.getResponseBodyAsString();
				
				// 解析查询返回结果
				strResponse = URLDecoder.decode(strResponse);
			//	Map<String, String> result = new HashMap<String, String>();
				String[] parameters = strResponse.split("&");
				for (int i = 0; i < parameters.length; i++) {
					String msg = parameters[i];
					int index = msg.indexOf('=');
					if (index > 0) {
						String name = msg.substring(0, index);
						String value = msg.substring(index + 1);
						result.put(name, value);
					}
				}
		
				// 查询结果会以Server方式通知商户(同支付返回)；
				// 若无法取得Server通知结果，可以通过解析查询返回结果，更新订单状态(参考如下).
				if (null != result.get("ERRORCODE")) {
					// 未查询到订单
					System.out.println("ERRORCODE=" + result.get("ERRORCODE"));
					System.out.println("ERRORMSG=" + result.get("ERRORMSG"));
					resultMsg = result.get("ERRORMSG");
		
				} else {
					// 查询到订单
					String payResult = result.get("payResult");
					if (payResult.equals("1")) {
						System.out.println("订单付款成功！");
		
						// 支付成功，验证签名
						PaymentResult paymentResult = new PaymentResult();
						paymentResult.setMerchantId(result.get("merchantId"));
						paymentResult.setVersion(result.get("version"));
						paymentResult.setLanguage(result.get("language"));
						paymentResult.setSignType(result.get("signType"));
						paymentResult.setPayType(result.get("payType"));
						paymentResult.setIssuerId(result.get("issuerId"));
						paymentResult.setPaymentOrderId(result
								.get("paymentOrderId"));
						paymentResult.setOrderNo(result.get("orderNo"));
						paymentResult.setOrderDatetime(result
								.get("orderDatetime"));
						paymentResult.setOrderAmount(result.get("orderAmount"));
						paymentResult.setPayAmount(result.get("payAmount"));
						paymentResult.setPayDatetime(result.get("payDatetime"));
						paymentResult.setExt1(result.get("ext1"));
						paymentResult.setExt2(result.get("ext2"));
						paymentResult.setPayResult(result.get("payResult"));
						paymentResult.setErrorCode(result.get("errorCode"));
						paymentResult.setReturnDatetime(result
								.get("returnDatetime"));
						paymentResult.setKey(key);
						paymentResult.setSignMsg(result.get("signMsg"));
						paymentResult.setCertPath("d:\\TLCert-JRDtest.cer");
		
						boolean verifyResult = paymentResult.verify();
		
						if (verifyResult) {
							System.out.println("验签成功！商户更新订单状态。");
							resultMsg = "订单支付成功，验签成功！商户更新订单状态。";
							isSuccess = true;
						} else {
							System.out.println("验签失败！");
							resultMsg = "订单支付成功，验签失败！";
						}
		
					} else {
							System.out.println("订单尚未付款！");
							resultMsg = "订单尚未付款！";
					}
				}
		
			}
		}catch(Exception e){
		 	e.printStackTrace();
		}
	}
}
