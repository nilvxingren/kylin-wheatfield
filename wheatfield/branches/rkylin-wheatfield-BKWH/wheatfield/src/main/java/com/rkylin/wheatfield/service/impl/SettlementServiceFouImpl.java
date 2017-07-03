package com.rkylin.wheatfield.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.TradeRecord;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserTradeSearchRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserTradeSearchResponse;
import com.ibm.icu.text.SimpleDateFormat;
import com.rkylin.wheatfield.constant.BaseConstants;
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.constant.SettleConstants;
import com.rkylin.wheatfield.constant.TransCodeConst;
import com.rkylin.wheatfield.exception.AccountException;
import com.rkylin.wheatfield.manager.ParameterInfoManager;
import com.rkylin.wheatfield.manager.SettleBalanceEntryManager;
import com.rkylin.wheatfield.manager.SettlementManager;
import com.rkylin.wheatfield.pojo.ParameterInfo;
import com.rkylin.wheatfield.pojo.ParameterInfoQuery;
import com.rkylin.wheatfield.pojo.SettleBalanceEntry;
import com.rkylin.wheatfield.pojo.SettleBalanceEntryQuery;
import com.rkylin.wheatfield.pojo.TransOrderInfo;
import com.rkylin.wheatfield.response.ErrorResponse;
import com.rkylin.wheatfield.service.PaymentAccountService;
import com.rkylin.wheatfield.service.SettlementServiceFou;
import com.rkylin.wheatfield.utils.SettlementUtils;

@Service("settlementServiceFou")
@SuppressWarnings({"rawtypes","unchecked"})
@Transactional
public class SettlementServiceFouImpl implements SettlementServiceFou {
	private static Logger logger = LoggerFactory.getLogger(SettlementServiceFouImpl.class);
	@Autowired
	private ParameterInfoManager parameterInfoManager;
	@Autowired
	private Properties userProperties;
	@Autowired
	private SettlementManager settlementManager;
	@Autowired
	private SettlementUtils settlementUtils;
	@Autowired
	private PaymentAccountService paymentAccountService;
	@Autowired
	private SettleBalanceEntryManager settleBalanceEntryManager;
	
	@Override
	@Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
	public Map<String, String> collateFN2Alipay(String invoicedate) throws Exception {
		logger.info("读取'丰年To支付宝'对账 ————————————START————————————");
		//返回信息Map
		Map<String, String> rtnMap = new HashMap<String, String>();
		//查询账期query对象
		ParameterInfoQuery parameterInfoQuery =  new ParameterInfoQuery();
		//清算工具类
		SettlementUtils settlementUtils = new SettlementUtils();
		//开始日期字符串 yyyyMMdd
		String startDateStr = null;
		//结束日期字符串 yyyyMMdd
		String endDateStr = null;
		//对账记录时间点hh:mm:ss
		String timeStr = "13:00:00";
		//查询日终是否结束 0:结束; 1为:未结束
		parameterInfoQuery.setParameterCode(SettleConstants.DAYEND);
		List<ParameterInfo> parameterInfo = parameterInfoManager.queryList(parameterInfoQuery);
		//日终结果
		String dayend = parameterInfo.get(0).getParameterValue();
		if(!"0".equals(dayend)) {
			rtnMap.put("errCode", "P1");
			rtnMap.put("rtnMsg", "日终没有正常结束!");
			return rtnMap;
		}
		if(invoicedate == null) {//如果输入账期
			//从db中查询账期
			parameterInfoQuery.setParameterCode(SettleConstants.ACCOUNTDATE);
			parameterInfo = parameterInfoManager.queryList(parameterInfoQuery);
			invoicedate = parameterInfo.get(0).getParameterValue();
		}
		
		startDateStr = settlementUtils.getAccountDate(invoicedate, "yyyyMMdd", -1);
		endDateStr = invoicedate.replace("-", "");
		logger.info("获取到的账期是 {start_date:"+ startDateStr +", end_date:"+ endDateStr +"}");
		
		//支付宝交易信息
		List<TradeRecord> tradeRecordList = null;
		//读取'支付宝'交易信息返回 List<Map<String,String>>
		List<Map<String,String>> mapList = null;
		//编辑上游对账文件数据结构
		Map<String,Map<String,String>> fileMap = null;
		//我方交易信息
		List<TransOrderInfo> rtnList = null;
		
		//支付宝接口, "查询支付宝账户交易记录"
		try {
			tradeRecordList = this.getTradeSearchByAipay(startDateStr, endDateStr, timeStr);
			if(tradeRecordList == null || tradeRecordList.size() <= 0) {
				rtnMap.put("errCode", "P1");
				rtnMap.put("errMsg", "支付宝接口, '查询支付宝账户交易记录' 返回结果 为null或0条结果");
				return rtnMap;
			}
		} catch (Exception e) {
			rtnMap.put("errCode", "P2");
			rtnMap.put("errMsg", "支付宝接口, '查询支付宝账户交易记录' 异常");
			logger.info(e.getMessage());
			e.printStackTrace();
			return rtnMap;
		}
		//读取'支付宝'交易信息返回 List<Map<String,String>>
		try {
			mapList = this.getTradeRecord2Map(tradeRecordList, invoicedate);
			if(mapList == null || mapList.size() <= 0) {
				rtnMap.put("errCode", "P3");
				rtnMap.put("errMsg", "读取'支付宝'交易信息返回 List<Map<String,String> 返回结果 为null或0条结果");
				return rtnMap;
			}
		} catch (Exception e) {
			rtnMap.put("errCode", "P4");
			rtnMap.put("errMsg", "读取'支付宝'交易信息返回 List<Map<String,String> 异常");
			logger.info(e.getMessage());
			e.printStackTrace();
			return rtnMap;
		}
		//编辑上游对账文件数据结构
		try {
			fileMap = this.editCollateStructure(mapList);
			if(fileMap == null || fileMap.size() <= 0) {
				rtnMap.put("errCode", "P5");
				rtnMap.put("errMsg", "编辑上游对账文件数据结构 返回结果 为null或0条结果");
				return rtnMap;
			}
		} catch (Exception e) {
			rtnMap.put("errCode", "P6");
			rtnMap.put("errMsg", "编辑上游对账文件数据结构  异常");
			logger.info(e.getMessage());
			e.printStackTrace();
			return rtnMap;
		}
		
		//查询'我方'交易信息
		String rootInstCd = "'"+ Constants.FN_ID+"'";
		String funcCode = "'"+ TransCodeConst.CHARGE+"'";
		String invoicedateTime = endDateStr + " 13:40:00";
		String payChannelId = "02";
		try {
			rtnList = this.getTransOrderInfoFromDB(rootInstCd, funcCode, invoicedateTime, payChannelId);
			if(rtnList == null || rtnList.size() <=0) {
				rtnMap.put("errCode", "P7");
				rtnMap.put("errMsg", "读取'我方'交易信息 返回结果 为null或0条结果");
				return rtnMap;
			}
		} catch (Exception e) {
			rtnMap.put("errCode", "P8");
			rtnMap.put("errMsg", "读取'我方'交易信息  异常");
			logger.info(e.getMessage());
			e.printStackTrace();
			return rtnMap;
		}
		
		//开始对账
		String tmpdate= "";
		String maxtradetime = "";
		for (Map bean : mapList) {
			if (bean.size() < 10) {
				continue;
			}
			tmpdate = bean.get("L_3").toString().replace("-", "").replace(":", "").replace(" ", "");
			if (maxtradetime.compareTo(tmpdate) < 0 ) {
				maxtradetime = tmpdate;
			}
		}
		try {
			this.doCollate(fileMap, rtnList, maxtradetime);
		} catch (Exception e) {
			rtnMap.put("errCode", "P9");
			rtnMap.put("errMsg", "对账  异常");
			logger.info(e.getMessage());
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动回滚
			return rtnMap;
		}
		rtnMap.put("errCode", "P0");
		rtnMap.put("errMsg", "对账  成功");
		logger.info("读取'丰年To支付宝'对账  ————————————END————————————");
		return rtnMap;
	}
	
	/**
	 * 获取令牌
	 * @param client
	 * @return
	 * @throws Exception
	 */
	private String getMyAuthToken(AlipayClient client) throws Exception {
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
	private List<TradeRecord> getTradeSearchByAipay(String startDateStr,String endDateStr, String timeStr) throws Exception {
		//把yyyyMMdd 转换成 yyyy-MM-dd
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		startDateStr = sdf1.format(sdf.parse(startDateStr));
		endDateStr = sdf1.format(sdf.parse(endDateStr));
		
		logger.info(">>> >>> >>> >>> 调用 支付宝方法查询交易信息");
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
		AlipayClient client = new DefaultAlipayClient(serverUrl, appID, privateKey, format);
		//令牌
		String token = getMyAuthToken(client);
		String authToken = token;

		//调用支付宝接口, 查询支付宝账户交易记录
		do {
			res = client.execute(req, authToken);
			
			if(tradeRecordList == null) {
				tradeRecordList = res.getTradeRecords();
			} else {
				tradeRecordList.addAll(res.getTradeRecords());
			}
		} while(res.getTotalPages() != null && Integer.parseInt(res.getTotalPages()) > currentPage ++);
		
		//预留验证
		logger.info("[["+ currentPage +"]]" + "errcode:" + res.getErrorCode() + "; msg:" + res.getMsg() + "; totalPages:" + res.getTotalPages());
		return tradeRecordList;
	}
	
	/**
	 * 读取支付宝交易信息封装成List<Map>
	 * @param tradeRecordList
	 * @param invoicedate
	 * @param maxtradetime
	 * @return
	 */
	private List<Map<String, String>> getTradeRecord2Map(List<TradeRecord> tradeRecordList, String invoicedate) {
		logger.info(">>> >>> >>> >>> 读取支付表交易信息, 封装成List<Map>");
		List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
		Iterator<TradeRecord> iterator = tradeRecordList.iterator();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		//订单类型|null结算日期|商户id|订单最后修改时间|商户订单号|支付宝订单号|订单总金额|订单服务费|null币种|null商户原始订单金额(分)
		while(iterator.hasNext()) {
			Map<String, String> map = new HashMap<String, String>();
			TradeRecord tr = iterator.next();
			map.put("L_0", tr.getOrderType());
			map.put("L_1", invoicedate);
			map.put("L_2", tr.getPartnerId());
			map.put("L_3", sdf.format(tr.getModifiedTime()));
			map.put("L_4", tr.getMerchantOrderNo());
			map.put("L_5", tr.getAlipayOrderNo());
			map.put("L_6", tr.getTotalAmount());
			map.put("L_7", tr.getServiceCharge());
			map.put("L_8", BaseConstants.CURRENCY_CNY);
			map.put("L_9", "0");
			map.put("funCode", tr.getOrderTitle());
			mapList.add(map);
		}
	
		return mapList;
	}
	
	/**
	 * 编辑上游对账文件数据结构
	 * @param mapList
	 * @return
	 */
	private Map<String, Map<String, String>> editCollateStructure(List<Map<String, String>> mapList) {
		logger.info(">>> >>> >>> >>> 编辑上游对账文件数据结构");
		
		Map<String, Map<String, String>> fileMap = new HashMap<String, Map<String,String>>();
		
		for (Map bean : mapList) {
			if (bean.size() < 10) {
				continue;
			}
			bean.put("STATUS_ID", SettleConstants.COLLATE_STU_2);// 先都制成长款标记
			fileMap.put(String.valueOf(bean.get("L_4")), bean);
		}
		
		return fileMap;
	}
	
	/**
	 * 读取我方'丰年To支付宝'的交易信息
	 * @param rootInstCd
	 * @param funcCode
	 * @param invoicedate
	 * @return
	 */
	private List<TransOrderInfo> getTransOrderInfoFromDB(String rootInstCd, String funcCode, String invoicedateTime, String payChannelID) {
		logger.info(">>> >>> >>> >>> 读取我方'交易记录");
		Map<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("ROOT", rootInstCd);
		paraMap.put("FUNC_CODE", funcCode);
		paraMap.put("TRADE_DATE", invoicedateTime);
		paraMap.put("PAY_CHANNEL_ID", payChannelID);
		List<TransOrderInfo> transOrderInfoList = settlementManager.selectTransOrderInfoD(paraMap);
		
		return transOrderInfoList;
	}
	
	/**
	 * 开始对账
	 * @param fileMap
	 * @param rtnList
	 * @param maxtradetime
	 * @return
	 * @throws Exception
	 */
	private Map<String, String> doCollate(Map<String,Map<String,String>> fileMap, List<TransOrderInfo> rtnList, String maxtradetime) throws Exception {
		logger.info(">>> >>> >>> >>> 开始对账 ... ...");
		Map<String, String> rtnMap = new HashMap<String, String>();
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatter3 = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat formatter4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		SettleBalanceEntry settleBalanceEntry = new SettleBalanceEntry();
		List<SettleBalanceEntry> settleBalanceEntryList = new ArrayList<SettleBalanceEntry>();
		TransOrderInfo bean = new TransOrderInfo();
		TransOrderInfo transOrderInfo = new TransOrderInfo();
		ErrorResponse rtnErrorResponse = new ErrorResponse();
		Map subMap = new HashMap();
		BigDecimal b_amount = null;
		BigDecimal b_con100 = new BigDecimal("100");
		
		for (int i = 0; i < rtnList.size(); i ++) {
			settleBalanceEntry = new SettleBalanceEntry();
			bean = rtnList.get(i);
			b_amount = new BigDecimal("0");
			if (fileMap.containsKey(bean.getRequestNo())) {
				subMap = fileMap.get(bean.getRequestNo());
				settleBalanceEntry.setRootInstCd(bean.getMerchantCode());
				settleBalanceEntry.setBatchId((String)subMap.get("L_4"));
				settleBalanceEntry.setTransSeqId(subMap.get("L_0").toString());
				settleBalanceEntry.setUserId(bean.getUserId());
				settleBalanceEntry.setOrderNo(bean.getOrderNo());
				settleBalanceEntry.setTransTime(formatter4.parse(subMap.get("L_3").toString()));
				b_amount = new BigDecimal(settlementUtils.nvl(subMap.get("L_6"), "0"));
				b_amount = b_amount.multiply(b_con100);
				settleBalanceEntry.setAmount(b_amount.longValue());
				settleBalanceEntry.setRetriRefNo(bean.getFuncCode());
				settleBalanceEntry.setMerchantCode("alipay");
				settleBalanceEntry.setSettleTime(formatter1.parse(subMap.get("L_1").toString()));
				b_amount = new BigDecimal(settlementUtils.nvl(subMap.get("L_7"), "0"));
				b_amount = b_amount.multiply(b_con100);
				settleBalanceEntry.setFee(b_amount.setScale(0).toString());

				if (bean.getStatus() == SettleConstants.TAKE_EFFECT) {//成功交易的场合
					b_amount = new BigDecimal(settlementUtils.nvl(subMap.get("L_6"), "0"));
					b_amount = b_amount.multiply(b_con100);
					if (b_amount.setScale(0).toString().equals(bean.getAmount().toString())) {//平账
						settleBalanceEntry.setStatusId(SettleConstants.COLLATE_STU_1);
						subMap.put("STATUS_ID", SettleConstants.COLLATE_STU_1);
					} else {//错帐
						settleBalanceEntry.setStatusId(SettleConstants.COLLATE_STU_0);
						subMap.put("STATUS_ID", SettleConstants.COLLATE_STU_0);
					}
				} else {//失败交易的场合（传统意义上的长款）
					b_amount = new BigDecimal(settlementUtils.nvl(subMap.get("L_6"), "0"));
					b_amount = b_amount.multiply(b_con100);
					transOrderInfo.setAmount(b_amount.longValue());
					transOrderInfo.setFuncCode(bean.getFuncCode());
					transOrderInfo.setInterMerchantCode(bean.getInterMerchantCode());
					transOrderInfo.setMerchantCode(bean.getMerchantCode());
					transOrderInfo.setOrderAmount(b_amount.longValue());
					transOrderInfo.setOrderCount(bean.getOrderCount());
					transOrderInfo.setOrderDate(bean.getOrderDate());
					transOrderInfo.setOrderNo(bean.getOrderNo());
					transOrderInfo.setRemark("qjs_balance");
					transOrderInfo.setRequestTime(new Date());
					transOrderInfo.setStatus(SettleConstants.TAKE_EFFECT);

					if (Constants.FN_ID.equals(bean.getMerchantCode())) {
						rtnErrorResponse = paymentAccountService.recharge(transOrderInfo, Constants.FN_PRODUCT, bean.getUserId());
					} else {
					}
					
					if (rtnErrorResponse.isIs_success() == true) {
						settleBalanceEntry.setStatusId(SettleConstants.COLLATE_STU_1); //平账
						subMap.put("STATUS_ID", SettleConstants.COLLATE_STU_1);
					} else {
						settleBalanceEntry.setStatusId(SettleConstants.COLLATE_STU_2); //长款
						subMap.put("STATUS_ID", SettleConstants.COLLATE_STU_1);
					}
				}
				settleBalanceEntryList.add(settleBalanceEntry);
			} else {
				if (formatter3.format(bean.getOrderDate()).compareTo(maxtradetime) < 0) {//短款
					settleBalanceEntry.setRootInstCd(bean.getMerchantCode());
					settleBalanceEntry.setBatchId(bean.getRequestNo());
					settleBalanceEntry.setUserId(bean.getUserId());
					settleBalanceEntry.setOrderNo(bean.getOrderNo());
					settleBalanceEntry.setTransTime(bean.getOrderDate());
					settleBalanceEntry.setAmount(bean.getAmount());
					settleBalanceEntry.setRetriRefNo(bean.getFuncCode());
					settleBalanceEntry.setMerchantCode("alipay");
					settleBalanceEntry.setStatusId(SettleConstants.COLLATE_STU_3);
					settleBalanceEntryList.add(settleBalanceEntry);
					//RkylinMailUtil.sendMailThread("上游对账短款警告","请查询SETTLE_BALANCE_ENTRY的订单号[ORDER_ID]\r\n订单号为["+bean.getOrderNo()+"]", "21401233@qq.com");
					Thread.sleep(1000);
				}
			}
		}
		
		for (Map filesubMap : fileMap.values()) {
			settleBalanceEntry = new SettleBalanceEntry();
			if (SettleConstants.COLLATE_STU_2 == Integer.parseInt(filesubMap.get("STATUS_ID").toString())) {
				settleBalanceEntry.setRootInstCd("alipay");
				settleBalanceEntry.setBatchId((String)filesubMap.get("L_4"));
				settleBalanceEntry.setTransSeqId(filesubMap.get("L_0").toString());
				settleBalanceEntry.setUserId((String)filesubMap.get("L_2"));
				settleBalanceEntry.setOrderNo((String)filesubMap.get("L_4"));
				settleBalanceEntry.setTransTime(formatter4.parse(filesubMap.get("L_3").toString()));
				b_amount = new BigDecimal(settlementUtils.nvl(filesubMap.get("L_6"), "0"));
				b_amount = b_amount.multiply(b_con100);
				settleBalanceEntry.setAmount(b_amount.longValue());
				settleBalanceEntry.setRetriRefNo(filesubMap.get("funCode").toString());
				settleBalanceEntry.setMerchantCode("alipay");
				settleBalanceEntry.setSettleTime(formatter1.parse(filesubMap.get("L_1").toString()));
				b_amount = new BigDecimal(settlementUtils.nvl(filesubMap.get("L_7"), "0"));
				b_amount = b_amount.multiply(b_con100);
				settleBalanceEntry.setFee(b_amount.setScale(0).toString());
				settleBalanceEntry.setStatusId(SettleConstants.COLLATE_STU_2);
				settleBalanceEntryList.add(settleBalanceEntry);
				//RkylinMailUtil.sendMailThread("上游对账不知原因长款警告","请查询你方交易中订单号为["+filesubMap.get("L_4")+"]的交易的交易状态\r\n通联此交易为成功交易，请确认之后回复邮件", "21401233@qq.com");
				Thread.sleep(1000);
			}
		}
		
		// 对账结果插入对账结果表
		SettleBalanceEntryQuery settleBalanceEntryQuery = new SettleBalanceEntryQuery();
		List<SettleBalanceEntry> resList = new ArrayList<SettleBalanceEntry>();
		if (settleBalanceEntryList.size() > 0) {
			for (SettleBalanceEntry subbean:settleBalanceEntryList) {
				settleBalanceEntryQuery = new SettleBalanceEntryQuery();
				settleBalanceEntryQuery.setOrderNo(subbean.getOrderNo());
				resList = settleBalanceEntryManager.queryList(settleBalanceEntryQuery);
				if (resList.size() > 0) {
					subbean.setSettleId(resList.get(0).getSettleId());
				}
				settleBalanceEntryManager.saveSettleBalanceEntry(subbean);
			}
		}
		
		return rtnMap;
	}
}
