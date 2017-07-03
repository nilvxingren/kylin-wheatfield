package test.wheatfield;

import java.math.BigDecimal;
import java.util.List;
import java.util.Properties;

import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.TradeRecord;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserTradeSearchRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserTradeSearchResponse;
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.pojo.TransOrderInfo;
import com.rkylin.wheatfield.utils.DateUtil;

public class AlipayUserTradeSearchDemo extends BaseJUnit4Test {
	@Autowired
	private Properties userProperties;
	
	/***
	 * 获取令牌
	 * @param client
	 * @return
	 * @throws Exception
	 */
	String getMyAuthToken(AlipayClient client) throws Exception {
		AlipaySystemOauthTokenRequest req = new AlipaySystemOauthTokenRequest();
		// 根据授权码取授权令牌
		req.setGrantType("authorization_code");
		// 授权码，用户对应用授权后得到。
		req.setCode(userProperties.getProperty("ALIPAY_APP_CODE"));
		
		AlipaySystemOauthTokenResponse response = client.execute(req);
		
		String token = response.getAccessToken();
		
		return token;
	}
	/**
	 * 支付宝接口, "查询支付宝账户交易记录"
	 * @param alipayConfigMap
	 * @param req
	 * @return
	 */
	List<TradeRecord> getTradeSearchByAipay(String startDateStr,String endDateStr, String timeStr) throws Exception {
		//当前页
		Integer currentPage = 1;
		//支付宝响应对象
		AlipayUserTradeSearchResponse res = null;
		//返回信息结构体
		List<TradeRecord> tradeRecordList = null;
		//支付宝查询交易信息请求对象
		AlipayUserTradeSearchRequest req = new AlipayUserTradeSearchRequest();
		//交易开始日期
		req.setStartTime(startDateStr + " " + timeStr);
		//交易结束日期
		req.setEndTime(endDateStr + " " + timeStr);
		
		req.setMerchantOrderNo("W2015071010332500006");
		//查询页数
		req.setPageNo(currentPage + "");
		//每页记录条数
		req.setPageSize("500");
		//密钥 ... ...
		String serverUrl = userProperties.getProperty("ALIPAY_SERVER_URL");
		String appID = userProperties.getProperty("ALIPAY_APP_ID");
 		String privateKey = userProperties.getProperty("ALIPAY_PRIVATE_KEY");
		String format = "json";
		// 初始化应用
		AlipayClient client = new DefaultAlipayClient(serverUrl,appID, privateKey, format);
		//获取令牌
		String token = getMyAuthToken(client);
		String authToken = token;
System.out.println("authTokenauthTokenauthTokenauthToken:::::  " + token);

		//调用支付宝接口, 查询支付宝账户交易记录
		do {
			res = client.execute(req, authToken);
			
			if(tradeRecordList == null) {
				tradeRecordList = res.getTradeRecords();
			} else {
				tradeRecordList.addAll(res.getTradeRecords());
			}
		} while(res.getTotalPages() != null && Integer.parseInt(res.getTotalPages()) > currentPage ++);
		
		return tradeRecordList;
	}
	
	/***
	 * 测试方法
	 * 参数详见:
	 * https://openhome.alipay.com/doc/viewApiDoc.htm?name=alipay.user.trade.search&version=1.0&subVersion=1.2&packageCode=FUNDS_RECON
	 */
	//@Test
	public void test() {
		try {
			List<TradeRecord> list = this.getTradeSearchByAipay("2015-07-10", "2015-07-11", "00:00:00");
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + list);
			
			for(TradeRecord t : list) {
				System.out.println("***********************************************************************************");
				System.out.println("AlipayOrderNo:" + t.getAlipayOrderNo());
				System.out.println("InOutType:" + t.getInOutType());
				System.out.println("MerchantOrderNo:" + t.getMerchantOrderNo());
				System.out.println("OppositeLogonId:" + t.getOppositeLogonId());
				System.out.println("OppositeName:" + t.getOppositeName());
				System.out.println("OppositeUserId:" + t.getOppositeUserId());
				System.out.println("OrderFrom:" + t.getOrderFrom());
				System.out.println("OrderStatus:" + t.getOrderStatus());
				System.out.println("OrderTitle:" + t.getOrderTitle());
				System.out.println("OrderType:" + t.getOrderType());
				System.out.println("OwnerLogonId:" + t.getOwnerLogonId());
				System.out.println("OwnerName:" + t.getOwnerName());
				System.out.println("OwnerUserId:" + t.getOwnerUserId());
				System.out.println("PartnerId:" + t.getPartnerId());
				System.out.println("ServiceCharge:" + t.getServiceCharge());
				System.out.println("TotalAmount:" + t.getTotalAmount());
				System.out.println("CreateTime:" + t.getCreateTime());
				System.out.println("ModifiedTime:" + t.getModifiedTime());
				System.out.println("ModifiedTime:" + t.getModifiedTime());
				System.out.println("// *********************************************************************************");
				System.out.println();
				System.out.println();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void validataZFB() throws Exception{
		String merchantId="W2015071010332500006";
		boolean isOK=false;
		DateUtil dateUtil=new DateUtil();
		logger.info("*****************调用 支付宝方法查询交易信息**************");
		//支付宝响应对象
		AlipayUserTradeSearchResponse res = null;
		//当前页
		int currentPage=1;
		//返回信息结构体
		List<TradeRecord> tradeRecordList = null;
		//支付宝查询交易信息请求对象
		AlipayUserTradeSearchRequest req = new AlipayUserTradeSearchRequest();
		//交易开始日期
		req.setStartTime("2015-07-10"+" 00:00:00");
		//交易结束日期
		req.setEndTime("2015-07-11"+" 00:00:00");
		//查询页数
		req.setPageNo("1");
		//每页记录条数
		req.setPageSize("10");
		//商户订单号
		req.setMerchantOrderNo(merchantId);
		//密钥 ... ...
			String serverUrl = userProperties.getProperty("ALIPAY_SERVER_URL");
			String appID = userProperties.getProperty("ALIPAY_APP_ID");
	 		String privateKey = userProperties.getProperty("ALIPAY_PRIVATE_KEY");
			String format = "json";
			// 初始化应用
			AlipayClient client = new DefaultAlipayClient(serverUrl,appID, privateKey, format);
			//获取令牌
			String token = getMyAuthToken(client);
			String authToken = token;
			System.out.println("authTokenauthTokenauthTokenauthToken:::::  " + token);

			//调用支付宝接口, 查询支付宝账户交易记录
			do {
				res = client.execute(req, authToken);
				
				if(tradeRecordList == null) {
					tradeRecordList = res.getTradeRecords();
				} else {
					tradeRecordList.addAll(res.getTradeRecords());
				}
			} while(res.getTotalPages() != null && Integer.parseInt(res.getTotalPages()) > currentPage ++);
				
		//判断返回数据的有效性
		if(null!=tradeRecordList&&tradeRecordList.size()==1){
			//计算金额
			Long payAmount=0l;
			try {
				payAmount=Long.parseLong(changeY2F(tradeRecordList.get(0).getTotalAmount()));
			} catch (Exception e) {
				logger.error("支付宝充值校验信息失败:"+e.getMessage());
			}
			if(payAmount==500){
				logger.info("支付宝充值校验信息成功");
				isOK=true;
			}else{
				logger.info("支付宝充值校验信息成功，请确认支付宝返回的订单金额为:" + tradeRecordList.get(0).getTotalAmount() + "; "
						+ "提交的交易订单金额为：" );
			}
		}else{
			logger.info("支付宝充值校验信息失败，请确认code:" + res.getSubCode() + "; msg:" + res.getSubMsg());
		}
		System.out.println(isOK);
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
}
