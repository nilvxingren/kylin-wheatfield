package com.rkylin.wheatfield.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Rop.api.ApiException;
import com.Rop.api.DefaultRopClient;
import com.Rop.api.domain.FileUrl;
import com.Rop.api.request.FengnianAhpFinanceCompareRequest;
import com.Rop.api.response.FengnianAhpFinanceCompareResponse;
import com.rkylin.common.RedisIdGenerator;
import com.rkylin.database.BaseDao;
import com.rkylin.file.txt.TxtReader;
import com.rkylin.file.txt.TxtWriter;
import com.rkylin.utils.RkylinMailUtil;
import com.rkylin.wheatfield.common.SessionUtils;
import com.rkylin.wheatfield.common.UploadAndDownLoadUtils;
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.constant.SettleConstants;
import com.rkylin.wheatfield.constant.TransCodeConst;
import com.rkylin.wheatfield.exception.AccountException;
import com.rkylin.wheatfield.manager.AccountAgreementManager;
import com.rkylin.wheatfield.manager.AccountInfoManager;
import com.rkylin.wheatfield.manager.CreditInfoManager;
import com.rkylin.wheatfield.manager.CreditRepaymentManager;
import com.rkylin.wheatfield.manager.GenerationPaymentManager;
import com.rkylin.wheatfield.manager.ParameterInfoManager;
import com.rkylin.wheatfield.manager.SettleBalanceEntryManager;
import com.rkylin.wheatfield.manager.SettleSplittingEntryManager;
import com.rkylin.wheatfield.manager.SettleTransTabManager;
import com.rkylin.wheatfield.manager.SettlementManager;
import com.rkylin.wheatfield.manager.TransDaysSummaryManager;
import com.rkylin.wheatfield.manager.TransOrderInfoManager;
import com.rkylin.wheatfield.pojo.AccountAgreement;
import com.rkylin.wheatfield.pojo.AccountInfo;
import com.rkylin.wheatfield.pojo.AccountInfoQuery;
import com.rkylin.wheatfield.pojo.CreditInfo;
import com.rkylin.wheatfield.pojo.CreditRepayment;
import com.rkylin.wheatfield.pojo.FinanaceEntry;
import com.rkylin.wheatfield.pojo.GenerationPayment;
import com.rkylin.wheatfield.pojo.GenerationPaymentQuery;
import com.rkylin.wheatfield.pojo.ParameterInfo;
import com.rkylin.wheatfield.pojo.ParameterInfoQuery;
import com.rkylin.wheatfield.pojo.SettleBalanceEntry;
import com.rkylin.wheatfield.pojo.SettleBalanceEntryQuery;
import com.rkylin.wheatfield.pojo.SettleSplittingEntry;
import com.rkylin.wheatfield.pojo.SettleTransTab;
import com.rkylin.wheatfield.pojo.SettleTransTabQuery;
import com.rkylin.wheatfield.pojo.TransDaysSummary;
import com.rkylin.wheatfield.pojo.TransDaysSummaryQuery;
import com.rkylin.wheatfield.pojo.TransOrderInfo;
import com.rkylin.wheatfield.pojo.User;
import com.rkylin.wheatfield.response.ErrorResponse;
import com.rkylin.wheatfield.response.Response;
import com.rkylin.wheatfield.response.SettlementResponse;
import com.rkylin.wheatfield.service.AccountManageService;
import com.rkylin.wheatfield.service.GenerationPaymentService;
import com.rkylin.wheatfield.service.IAPIService;
import com.rkylin.wheatfield.service.IErrorResponseService;
import com.rkylin.wheatfield.service.PaymentAccountService;
import com.rkylin.wheatfield.service.PaymentInternalService;
import com.rkylin.wheatfield.service.SettlementService;
import com.rkylin.wheatfield.settlement.SettlementLogic;
import com.rkylin.wheatfield.utils.ArithUtil;
import com.rkylin.wheatfield.utils.SettlementUtils;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("settlementService")
@SuppressWarnings({"rawtypes","unchecked"})
@Transactional
public class SettlementServiceImpl extends BaseDao implements SettlementService,IAPIService {
	private static Logger logger = LoggerFactory.getLogger(SettlementServiceImpl.class);

	@Autowired
	private CreditInfoManager creditInfoManager;
	@Autowired
	private TransOrderInfoManager transOrderInfoManager;
	@Autowired
	private SettlementManager settlementManager;
	@Autowired
	HttpServletRequest request;
	@Autowired
	IErrorResponseService errorResponseService;
	@Autowired
	ParameterInfoManager parameterInfoManager;
	@Autowired
	PaymentInternalService paymentInternalService;
	@Autowired
	RedisIdGenerator redisIdGenerator;
	@Autowired
	TransDaysSummaryManager transDaysSummaryManager;
	@Autowired
	AccountInfoManager accountInfoManager;
	@Autowired
	GenerationPaymentService generationPaymentService;
	@Autowired
	AccountAgreementManager accountAgreementManager;
	@Autowired
	GenerationPaymentManager generationPaymentManager;
	@Autowired
	SettleSplittingEntryManager settleSplittingEntryManager;
	@Autowired
	SettleBalanceEntryManager settleBalanceEntryManager;
	@Autowired
	PaymentAccountService paymentAccountService;
	@Autowired
	AccountManageService accountManageService;
	@Autowired
	CreditRepaymentManager creditRepaymentManager;
	@Autowired
	Properties userProperties;
	@Autowired
	SettleTransTabManager settleTransTabManager;
	@Autowired
	SettlementLogic settlementLogic;
	
	private Response responseR = null;
	//private SettlementResponse response = new SettlementResponse();
	
	@Override
	@Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
 	public List<CreditInfo> getCreditInfo(String invoicedate) {
		logger.info("读取授信结果文件 ————————————START————————————");
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    	SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
    	Calendar c = Calendar.getInstance();
    	
    	ParameterInfoQuery keyList =  new ParameterInfoQuery();
    	keyList.setParameterCode(SettleConstants.ACCOUNTDATE);
		List<CreditInfo> rtnList = new ArrayList<CreditInfo>();
		List<CreditInfo> rtnerrorList = new ArrayList<CreditInfo>();
		Map subMap = null;

    	try {
	    	List<ParameterInfo> parameterInfo = parameterInfoManager.queryList(keyList);
	    	SettlementUtils settlementUtils = new SettlementUtils();
	    	String accountDate = "";
	    	if (invoicedate == null || "".equals(invoicedate)) {
				try {
					accountDate = settlementUtils.getAccountDate(parameterInfo.get(0).getParameterValue(), "yyyyMMdd",0);
				} catch (Exception e2) {
					logger.error("计算账期异常！" + e2.getMessage());
					responseR = errorResponseService.getErrorResponse("S6", "账期无效");
					return null;
				}
	    	} else {
	    		accountDate = invoicedate.replace("-", "");
	    	}
			logger.info("取得的账期为"+ accountDate);
			
	    	String filename = "P2P_" + accountDate+"."+SettleConstants.FILE_CSV;
			
	    	String p2pfilePath = SettleConstants.FILE_PATH +accountDate + File.separator;
	    	File file = new File(p2pfilePath);
	    	if (!file.exists()) {
	    		file.mkdirs();
	    	}
			logger.info("获取授信文件，调用P2P接口1");
			String dfilename = "";
			String dfilepath = "";

			String Key = userProperties.getProperty("P2P_PRIVATE_KEY");
			try {
				String urlKeyStr = "";
				List<FileUrl> urlKey = UploadAndDownLoadUtils.getUrlKeys(2, formatter.parse(accountDate), "", SettleConstants.FILE_XML,userProperties.getProperty("FSAPP_KEY"),userProperties.getProperty("FSDAPP_SECRET"),userProperties.getProperty("FSROP_URL"));
				if (urlKey == null || urlKey.size() == 0) {
					logger.error("授信结果文件不存在2！");
					responseR = errorResponseService.getErrorResponse("S1", "授信结果文件不存在！");
					return null;
				}
//				List<FileUrl> urlKey = new ArrayList<FileUrl>();
//				FileUrl fileUrl = new FileUrl();
//				fileUrl.setBatch("001");
//				fileUrl.setInvoice_date(accountDate);
//				fileUrl.setUrl_key("79d973f8-8c1c-4346-abe9-23f3417c0003");
//				urlKey.add(fileUrl);
				
				for (FileUrl fileurl:urlKey) {
					urlKeyStr = fileurl.getUrl_key();
					dfilename = "P2P_"+fileurl.getInvoice_date()+"_"+fileurl.getBatch()+"."+SettleConstants.FILE_CSV;
					dfilepath = UploadAndDownLoadUtils.downloadFile(accountDate,SettleConstants.FILE_XML, urlKeyStr,dfilename,request,2,Key,1,userProperties.getProperty("FSAPP_KEY"),userProperties.getProperty("FSDAPP_SECRET"),userProperties.getProperty("FSROP_URL"));
					settlementUtils.copyFile(dfilepath+File.separator+dfilename, p2pfilePath+filename);
					break;
				}
			} catch (Exception e1) {
				logger.error("获取授信文件，调用P2P接口异常3！" + e1.getMessage());
				responseR = errorResponseService.getErrorResponse("S2", "授信结果文件取得失败！");
				return null;
			}
			
			
			logger.info("读取授信文件4");
			String path=p2pfilePath + filename;
			file = new File(path);
			if (!file.exists()) {
				logger.error("授信文件不存在！5");
				responseR = errorResponseService.getErrorResponse("S1", "授信结果文件不存在！");
				return null;
			}
			//file = new File("C:\\test\\download\\20150409\\P2P_20150409.csv");
			TxtReader txtReader = new TxtReader();
			List<Map> fileList = new ArrayList<Map>();
			try {
				txtReader.setEncode("UTF-8");
				fileList = txtReader.txtreader(file , SettleConstants.DEDT_SPLIT2);
			} catch(Exception e) {
				logger.error("授信文件操作异常6！" + e.getMessage());
				responseR = errorResponseService.getErrorResponse("S3", "读取授信结果文件失败！");
				return null;
			}
			// 编辑文件读出的数据
			// 授信文件的格式
			//日期
			//机构码(P2P)
			//商户号(丰年)
			//用户号(丰年用户)
			//授信种类
			//审核结果
			//币种
			//金额
			//利率
			//期限
			//授信协议号
			//扩张字段1
			//扩张字段2
			//扩张字段3
			List<CreditInfo> sxfileList = new ArrayList<CreditInfo>();
			BigDecimal amount = null;
			BigDecimal con_100 = new BigDecimal("100");
			AccountAgreement accountAgreement = new AccountAgreement();
			String rtnMag = null;
			User user = new User();
			CreditInfo sxfileMap = new CreditInfo();
			for (Map fileMap:fileList) {
				if (SettleConstants.PASS.equals(fileMap.get("L_5"))) {
					sxfileMap = new CreditInfo();
					user = new User();
					user.userId = (String)fileMap.get("L_3");
					user.constId = Constants.FN_ID;
					user.productId = Constants.USER_SUB_ACCOUNT;
					sxfileMap.setUserId((String)fileMap.get("L_3"));
					sxfileMap.setRootInstCd(Constants.FN_ID);

					accountAgreement.setAgmtCode((String)fileMap.get("L_10"));
					accountAgreement.setAgmtName((String)fileMap.get("L_3"));
					
					if (SettleConstants.LIMIT.equals(fileMap.get("L_4"))) {
						sxfileMap.setCreditTypeId(SettleConstants.CREDIT_TYPE_ID_LIMIT);
					} else if (SettleConstants.SINGLE.equals(fileMap.get("L_4"))) {
						sxfileMap.setCreditTypeId(SettleConstants.CREDIT_TYPE_ID_SINGLE);
					}
					amount = new BigDecimal((String)fileMap.get("L_7"));
					amount = amount.multiply(con_100);
					sxfileMap.setAmount(amount.longValue());
					sxfileMap.setRate((String)fileMap.get("L_8"));
					sxfileMap.setCurrency((String)fileMap.get("L_6"));
					c.setTime(formatter.parse(accountDate));
					c.add(Calendar.DAY_OF_MONTH, 1); // 第二天生效
					c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
					sxfileMap.setStartTime(c.getTime());
					c.setTime(formatter.parse(accountDate));
			    	c.add(Calendar.MONTH, Integer.parseInt((String)fileMap.get("L_9")));
					c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
					sxfileMap.setEndTime(c.getTime());
					sxfileList.add(sxfileMap);
					try {
						rtnMag = accountManageService.creditAccount(user, sxfileMap, accountAgreement, Constants.P2P_ID);
						if ("ok".equals(rtnMag)) {
							rtnList.add(sxfileMap);
						} else {
							logger.error("授信时异常,用户["+sxfileMap.getUserId()+"]，错误为："+rtnMag);
							rtnerrorList.add(sxfileMap);
						}
					} catch(Exception x) {
						logger.error("操作条目为空！错误条目为："+x.getMessage());
						rtnerrorList.add(sxfileMap);
					}
				}
			}
			
			if (sxfileList.size() == 0) {
				logger.error("授信结果文件内容为空！7");
				responseR = errorResponseService.getErrorResponse("S4", "授信结果文件内容为空！");
				return null;
			}
			
			if (rtnList == null || rtnList.size() == 0) {
				logger.error("操作条目为空！错误条目为："+rtnerrorList.size());
				responseR = errorResponseService.getErrorResponse("S6", "操作条目为空！");
			}
    	} catch (Exception z) {
			logger.error("授信结果文件格式错误，请联系相关负责人");
    		responseR = errorResponseService.getErrorResponse("S5", "授信结果文件格式错误，请联系相关负责人");
    	}
		logger.info("读取授信结果文件 ————————————END————————————");
		return rtnList;
	}

	@Override
	@Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
	public String createDebtAccountFile(String invoicedate) {
		logger.info("生成丰年（非充值）对账文件 ————————————START————————————");
		logger.debug("取得昨日债权消费");
		//测试环境
		String appKey_DZ = userProperties.getProperty("APP_KEY_DZ");
		String appSecret_DZ =userProperties.getProperty("APP_SECRET_DZ");
		String url_DZ=userProperties.getProperty("ROP_URL_DZ");
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    	SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
    	SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss");
    	ParameterInfoQuery keyList =  new ParameterInfoQuery();
    	keyList.setParameterCode(SettleConstants.DAYEND);
    	List<ParameterInfo> parameterInfo = parameterInfoManager.queryList(keyList);
    	if (!"0".equals(parameterInfo.get(0).getParameterValue())) {
    		RkylinMailUtil.sendMailThread("清洁算开始异常","日终没有正常结束，不能开始清洁算操作", "21401233@qq.com");
    		return null;
    	}
    	
    	keyList.setParameterCode(SettleConstants.ACCOUNTDATE);
		parameterInfo = parameterInfoManager.queryList(keyList);
    	SettlementUtils settlementUtils = new SettlementUtils();
		String rtnurlKey = null;
    	String accountDate = "";
    	try {
	    	if (invoicedate == null || "".equals(invoicedate)) {
				try {
					accountDate = settlementUtils.getAccountDate(parameterInfo.get(0).getParameterValue(), "yyyyMMdd",-1);
				} catch (Exception e2) {
					logger.error("计算账期异常！" + e2.getMessage());
					responseR = errorResponseService.getErrorResponse("S1", "账期无效");
					return null;
				}
	    	} else {
	    		accountDate = invoicedate.replace("-", "");
	    	}
			logger.info("取得的账期为"+ accountDate);
			Calendar c = Calendar.getInstance();
	    	c.setTime(new Date());
	    	c.add(Calendar.DAY_OF_MONTH, -2);
			String start_date = SettleConstants.DEDT_ACCOUNT_DATE.replace("{YMD}", formatter.format(c.getTime()));
	    	c.add(Calendar.DAY_OF_MONTH, 1);
			String end_date = SettleConstants.DEDT_ACCOUNT_DATE.replace("{YMD}", formatter.format(c.getTime()));
			
			logger.info("将要取得["+start_date+"]至["+end_date+"]的数据");
			
			Map paraMap = new HashMap();
			Map configMap = new HashMap();
			paraMap.put("ACCOUNT_DATE", accountDate + " 00:00:00");
			paraMap.put("FUNC_CODE", "'"+TransCodeConst.CHARGE+"'");
			paraMap.put("STATUS", SettleConstants.SETTLE_STU_0);
			paraMap.put("ROOT", Constants.FN_ID);
			List<TransOrderInfo> rtnList = settlementManager.queryList(paraMap);
			//List<TransOrderInfo> rtnList = new ArrayList();
			logger.debug("编辑取得的"+rtnList.size()+"条数据");
			List excelList = new ArrayList();
			
			int countN = 0;//总条数
			BigDecimal amountS = new BigDecimal("0");//总金额
			BigDecimal amount = new BigDecimal("0");//金额
			if (rtnList.size() == 0) {
	//			paraMap.put("F_1", accountDate);//账期
	//			paraMap.put("F_2", " ");
	//			paraMap.put("F_3", " ");
	//			paraMap.put("F_4", " ");
	//			paraMap.put("F_5", "0");
	//			paraMap.put("F_6", " ");
	//			paraMap.put("F_7", " ");
	//			paraMap.put("F_8", " ");
	//			paraMap.put("F_9", " ");
	//			paraMap.put("F_10", " ");
	//			paraMap.put("F_11", "0");
	//			paraMap.put("F_12", "0");
	//			paraMap.put("F_13", " ");
	//			//paraMap.put("F_14", "无数据");
	//			excelList.add(paraMap);
			} else {
				for (TransOrderInfo tmpbean:rtnList) {
					paraMap = new HashMap();
					if (!TransCodeConst.CHARGE.equals(tmpbean.getFuncCode())) {
						paraMap.put("F_1", accountDate);//账期
						paraMap.put("F_2", tmpbean.getUserId());//用户号
						paraMap.put("F_3", settlementUtils.nvl(tmpbean.getInterMerchantCode(),""));//商户号
						paraMap.put("F_4", tmpbean.getTradeFlowNo());//交易流水号
						paraMap.put("F_5", Long.toString(tmpbean.getAmount()));//交易金额
						paraMap.put("F_6", formatter1.format(tmpbean.getOrderDate()));//交易日期
						paraMap.put("F_7", formatter2.format(tmpbean.getOrderDate()));//交易时间
						paraMap.put("F_8", tmpbean.getFuncCode());//功能码
						paraMap.put("F_9", tmpbean.getOrderNo());//订单号
						paraMap.put("F_10", tmpbean.getOrderCount().toString());//订单数量
						paraMap.put("F_11", tmpbean.getStatus().toString());//交易状态
						paraMap.put("F_12", settlementUtils.nvl(tmpbean.getFeeAmount(),"0"));//手续费1
						paraMap.put("F_13", settlementUtils.nvl(tmpbean.getUserFee(),"0"));//手续费2
						paraMap.put("F_14", settlementUtils.nvl(tmpbean.getRemark(),""));//备注
						excelList.add(paraMap);
						countN++;
						amount = new BigDecimal(Long.toString(tmpbean.getAmount()));
						amountS = amountS.add(amount);
					}
				}
			}
	
			String path = SettleConstants.FILE_UP_PATH + accountDate + File.separator;
			File filePath = new File(path);
			if (!filePath.exists()) {
				filePath.mkdirs();
			}
			path=path + "FN_CCHARGE_"+accountDate+".csv";
	//		configMap.put("MODEL_FILE", "src/main/java/com/rkylin/wheatfield/model/dedtaccount.xlsx");
	//		configMap.put("4", "D");
	//		configMap.put("10", "D");
	//		configMap.put("11", "D");
	
			configMap.put("FILE", path);
	//		configMap.put("SHEET", "债权交易");
	//		configMap.put("firstStyleRow", "4");
	//		configMap.put("firstDetailRow", "4");
			
			List reportHead = new LinkedList();
			List reportTail = new LinkedList();
			
			Map infoMap = new HashMap();
			reportHead.clear();
			infoMap.put("F_1", accountDate);
			infoMap.put("F_2", countN+"");
			infoMap.put("F_3", amountS.toString());
			reportHead.add(infoMap);
			
	//		infoMap =  new HashMap();
	//		infoMap.put("ROW", "1");
	//		infoMap.put("COL", "0");
	//		infoMap.put("VALUE", accountDate);
	//		reportHead.add(infoMap);
	//		infoMap =  new HashMap();
	//		infoMap.put("ROW", "1");
	//		infoMap.put("COL", "1");
	//		infoMap.put("VALUE", countN+"");
	//		reportHead.add(infoMap);
	//		infoMap =  new HashMap();
	//		infoMap.put("ROW", "1");
	//		infoMap.put("COL", "2");
	//		infoMap.put("VALUE", amountS.setScale(2).toString());
	//		reportHead.add(infoMap);
	
			configMap.put("REPORT-HEAD", reportHead);
			
	//		reportTail.clear();
	//		infoMap =  new HashMap();
	//		infoMap.put("ROW", "6");
	//		infoMap.put("COL", "3");
	//		infoMap.put("VALUE", "制表人：");
	//		reportTail.add(infoMap);
	//		infoMap =  new HashMap();
	//		infoMap.put("ROW", "7");
	//		infoMap.put("COL", "3");
	//		infoMap.put("VALUE", "制表时间：");
	//		reportTail.add(infoMap);
	//
	//		configMap.put("REPORT-TAIL", reportTail);

			String Key = userProperties.getProperty("FN_PRIVATE_KEY");
			try {
				TxtWriter.WriteTxt(excelList, configMap, SettleConstants.DEDT_SPLIT2,"UTF-8");
				rtnurlKey = UploadAndDownLoadUtils.uploadFile(path, 11, formatter.parse(accountDate), "", SettleConstants.FILE_XML,Key,1,userProperties.getProperty("FSAPP_KEY"),userProperties.getProperty("FSDAPP_SECRET"),userProperties.getProperty("FSROP_URL"));
			} catch (Exception e) {
				logger.error("生成债权对账文件操作异常！" + e.getMessage());
				responseR = errorResponseService.getErrorResponse("S2", "对账文件生成或文件服务器连接失败");
				return null;
			}
			DefaultRopClient ropClient = new DefaultRopClient(url_DZ, appKey_DZ,
					appSecret_DZ, SettleConstants.FILE_XML);
			FengnianAhpFinanceCompareRequest fileurlRequest=new FengnianAhpFinanceCompareRequest();
			fileurlRequest.setType(11);
			fileurlRequest.setInvoice_date(formatter.parse(accountDate));
			try {
				FengnianAhpFinanceCompareResponse filrUrlResponse=ropClient.execute(fileurlRequest, SessionUtils.sessionGet(url_DZ, appKey_DZ,appSecret_DZ));
				if(!filrUrlResponse.getBody().contains("对账完成")) {
					logger.error("丰年对账接（非充值）口失败"+filrUrlResponse.getBody());
					RkylinMailUtil.sendMailThread("丰年对账接（非充值）口失败",filrUrlResponse.getBody(), "21401233@qq.com");
				}
			} catch (ApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	} catch (Exception z) {
			logger.error("对账文件生成失败，请联系相关负责人");
    		responseR = errorResponseService.getErrorResponse("S3", "对账文件生成失败，请联系相关负责人");
    	}
		logger.info("生成丰年（非充值）对账文件 ————————————END————————————");
		return rtnurlKey;
	}
	
	@Override
	@Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
	public String createDebtAccountFile2(String invoicedate) {
		logger.info("生成丰年（充值）对账文件 ————————————START————————————");
		logger.debug("取得昨日债权消费");
		//测试环境
		String appKey_DZ = userProperties.getProperty("APP_KEY_DZ");
		String appSecret_DZ =userProperties.getProperty("APP_SECRET_DZ");
		String url_DZ=userProperties.getProperty("ROP_URL_DZ");
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    	SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
    	SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss");
    	ParameterInfoQuery keyList =  new ParameterInfoQuery();
    	keyList.setParameterCode(SettleConstants.DAYEND);
    	List<ParameterInfo> parameterInfo = parameterInfoManager.queryList(keyList);
    	if (!"0".equals(parameterInfo.get(0).getParameterValue())) {
    		RkylinMailUtil.sendMailThread("清洁算开始异常","日终没有正常结束，不能开始清洁算操作", "21401233@qq.com");
    		return null;
    	}
    	
    	keyList.setParameterCode(SettleConstants.ACCOUNTDATE);
		parameterInfo = parameterInfoManager.queryList(keyList);
    	SettlementUtils settlementUtils = new SettlementUtils();
		String rtnurlKey = null;
    	String accountDate = "";;
    	try {
	    	if (invoicedate == null || "".equals(invoicedate)) {
				try {
					accountDate = settlementUtils.getAccountDate(parameterInfo.get(0).getParameterValue(), "yyyyMMdd",0);
				} catch (Exception e2) {
					logger.error("计算账期异常！" + e2.getMessage());
					responseR = errorResponseService.getErrorResponse("S1", "账期无效");
					return null;
				}
	    	} else {
	    		accountDate = invoicedate.replace("-", "");
	    	}
			logger.info("取得的账期为"+ accountDate);
			Calendar c = Calendar.getInstance();
	    	c.setTime(new Date());
	    	c.add(Calendar.DAY_OF_MONTH, -2);
			String start_date = SettleConstants.DEDT_ACCOUNT_DATE.replace("{YMD}", formatter.format(c.getTime()));
	    	c.add(Calendar.DAY_OF_MONTH, 1);
			String end_date = SettleConstants.DEDT_ACCOUNT_DATE.replace("{YMD}", formatter.format(c.getTime()));
			
			logger.info("将要取得["+start_date+"]至["+end_date+"]的数据");
			
			Map paraMap = new HashMap();
			Map configMap = new HashMap();
			paraMap.put("ACCOUNT_DATE", accountDate + " 00:00:00");
			paraMap.put("ROOT", Constants.FN_ID);
			List<TransOrderInfo> rtnList = settlementManager.selectTransOrderInfoCh(paraMap);
			//List<TransOrderInfo> rtnList = new ArrayList();
			logger.debug("编辑取得的"+rtnList.size()+"条数据");
			List excelList = new ArrayList();
			
			int countN = 0;//总条数
			BigDecimal amountS = new BigDecimal("0");//总金额
			BigDecimal amount = new BigDecimal("0");//金额
			if (rtnList.size() == 0) {
	//			paraMap.put("F_1", accountDate);//账期
	//			paraMap.put("F_2", " ");
	//			paraMap.put("F_3", " ");
	//			paraMap.put("F_4", " ");
	//			paraMap.put("F_5", "0");
	//			paraMap.put("F_6", " ");
	//			paraMap.put("F_7", " ");
	//			paraMap.put("F_8", " ");
	//			paraMap.put("F_9", " ");
	//			paraMap.put("F_10", " ");
	//			paraMap.put("F_11", "0");
	//			paraMap.put("F_12", "0");
	//			paraMap.put("F_13", " ");
	//			//paraMap.put("F_14", "无数据");
	//			excelList.add(paraMap);
			} else {
				for (TransOrderInfo tmpbean:rtnList) {
					paraMap = new HashMap();
					if (TransCodeConst.CHARGE.equals(tmpbean.getFuncCode())) {
						paraMap.put("F_1", accountDate);//账期
						paraMap.put("F_2", tmpbean.getUserId());//用户号
						paraMap.put("F_3", settlementUtils.nvl(tmpbean.getMerchantCode(),""));//商户号
						paraMap.put("F_4", tmpbean.getTradeFlowNo());//交易流水号
						paraMap.put("F_5", Long.toString(tmpbean.getAmount()));//交易金额
						paraMap.put("F_6", formatter1.format(tmpbean.getOrderDate()));//交易日期
						paraMap.put("F_7", formatter2.format(tmpbean.getOrderDate()));//交易时间
						paraMap.put("F_8", tmpbean.getFuncCode());//功能码
						paraMap.put("F_9", tmpbean.getOrderNo());//订单号
						paraMap.put("F_10", tmpbean.getOrderCount().toString());//订单数量
						paraMap.put("F_11", tmpbean.getStatus().toString());//交易状态
						paraMap.put("F_12", settlementUtils.nvl(tmpbean.getFeeAmount(),"0"));//手续费1
						paraMap.put("F_13", settlementUtils.nvl(tmpbean.getUserFee(),"0"));//手续费2
						paraMap.put("F_14", settlementUtils.nvl(tmpbean.getRemark(),""));//备注
						excelList.add(paraMap);
						countN++;
						amount = new BigDecimal(Long.toString(tmpbean.getAmount()));
						amountS = amountS.add(amount);
					}
				}
			}
	
			String path = SettleConstants.FILE_UP_PATH + accountDate + File.separator;
			File filePath = new File(path);
			if (!filePath.exists()) {
				filePath.mkdirs();
			}
			path=path + "FN_CHARGE_"+accountDate+".csv";
	//		configMap.put("MODEL_FILE", "src/main/java/com/rkylin/wheatfield/model/dedtaccount.xlsx");
	//		configMap.put("4", "D");
	//		configMap.put("10", "D");
	//		configMap.put("11", "D");
	
			configMap.put("FILE", path);
	//		configMap.put("SHEET", "债权交易");
	//		configMap.put("firstStyleRow", "4");
	//		configMap.put("firstDetailRow", "4");
			
			List reportHead = new LinkedList();
			List reportTail = new LinkedList();
			
			Map infoMap = new HashMap();
			reportHead.clear();
			infoMap.put("F_1", accountDate);
			infoMap.put("F_2", countN+"");
			infoMap.put("F_3", amountS.toString());
			reportHead.add(infoMap);
			
	//		infoMap =  new HashMap();
	//		infoMap.put("ROW", "1");
	//		infoMap.put("COL", "0");
	//		infoMap.put("VALUE", accountDate);
	//		reportHead.add(infoMap);
	//		infoMap =  new HashMap();
	//		infoMap.put("ROW", "1");
	//		infoMap.put("COL", "1");
	//		infoMap.put("VALUE", countN+"");
	//		reportHead.add(infoMap);
	//		infoMap =  new HashMap();
	//		infoMap.put("ROW", "1");
	//		infoMap.put("COL", "2");
	//		infoMap.put("VALUE", amountS.setScale(2).toString());
	//		reportHead.add(infoMap);
	
			configMap.put("REPORT-HEAD", reportHead);
			
	//		reportTail.clear();
	//		infoMap =  new HashMap();
	//		infoMap.put("ROW", "6");
	//		infoMap.put("COL", "3");
	//		infoMap.put("VALUE", "制表人：");
	//		reportTail.add(infoMap);
	//		infoMap =  new HashMap();
	//		infoMap.put("ROW", "7");
	//		infoMap.put("COL", "3");
	//		infoMap.put("VALUE", "制表时间：");
	//		reportTail.add(infoMap);
	//
	//		configMap.put("REPORT-TAIL", reportTail);

			String Key = userProperties.getProperty("FN_PRIVATE_KEY");
			try {
				TxtWriter.WriteTxt(excelList, configMap, SettleConstants.DEDT_SPLIT2,"UTF-8");
				rtnurlKey = UploadAndDownLoadUtils.uploadFile(path, 12, formatter.parse(accountDate), "", SettleConstants.FILE_XML,Key,1,userProperties.getProperty("FSAPP_KEY"),userProperties.getProperty("FSDAPP_SECRET"),userProperties.getProperty("FSROP_URL"));
			} catch (Exception e) {
				logger.error("生成债权对账文件操作异常！" + e.getMessage());
				responseR = errorResponseService.getErrorResponse("S2", "对账文件生成或文件服务器连接失败");
				return null;
			}
			DefaultRopClient ropClient = new DefaultRopClient(url_DZ, appKey_DZ,
					appSecret_DZ, SettleConstants.FILE_XML);
			FengnianAhpFinanceCompareRequest fileurlRequest=new FengnianAhpFinanceCompareRequest();
			fileurlRequest.setType(12);
			fileurlRequest.setInvoice_date(formatter.parse(accountDate));
			try {
				FengnianAhpFinanceCompareResponse filrUrlResponse=ropClient.execute(fileurlRequest, SessionUtils.sessionGet(url_DZ, appKey_DZ,appSecret_DZ));
			} catch (ApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	} catch (Exception z) {
			logger.error("对账文件生成失败，请联系相关负责人");
    		responseR = errorResponseService.getErrorResponse("S3", "对账文件生成失败，请联系相关负责人");
    	}
		logger.info("生成丰年（充值）对账文件 ————————————END————————————");
		return rtnurlKey;
	}
	
	@Override
	@Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
	public String createP2PDebtFile(String invoicedate) {
		logger.info("生成P2P债权包文件 ————————————START————————————");
		logger.debug("取得昨日债权消费");

    	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    	SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
    	SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss");
    	ParameterInfoQuery keyList =  new ParameterInfoQuery();
    	keyList.setParameterCode(SettleConstants.ACCOUNTDATE);
    	SettlementUtils settlementUtils = new SettlementUtils();
		String rtnurlKey = null;
    	String accountDate = "";
    	try {
    		List<ParameterInfo> parameterInfo = parameterInfoManager.queryList(keyList);
	    	if (invoicedate == null || "".equals(invoicedate)) {
				try {
					accountDate = settlementUtils.getAccountDate(parameterInfo.get(0).getParameterValue(), "yyyyMMdd",-1);
				} catch (Exception e2) {
					logger.error("计算账期异常！" + e2.getMessage());
					responseR = errorResponseService.getErrorResponse("S1", "账期无效");
					return null;
				}
	    	} else {
	    		accountDate = invoicedate.replace("-", "");
	    	}
			logger.info("取得的账期为"+ accountDate);
			
//			Calendar c = Calendar.getInstance();
//	    	c.setTime(new Date());
//	    	c.add(Calendar.DAY_OF_MONTH, -2);
//			String start_date = SettleConstants.DEDT_DATE.replace("{YMD}", formatter.format(c.getTime()));
//	    	c.add(Calendar.DAY_OF_MONTH, 1);
//			String end_date = SettleConstants.DEDT_DATE.replace("{YMD}", formatter.format(c.getTime()));
//			
//			logger.info("将要取得["+start_date+"]至["+end_date+"]的数据");
			
			Map paraMap = new HashMap();
			Map configMap = new HashMap();
			paraMap.put("ACCOUNT_DATE", accountDate+ " 00:00:00");
			paraMap.put("STATUS", SettleConstants.SETTLE_STU_1);
			paraMap.put("FUNC_CODE", TransCodeConst.CREDIT_CONSUME);
			List<Map<String,Object>> rtnList = settlementManager.queryP2PList(paraMap);
			//List<TransOrderInfo> rtnList = new ArrayList();
			logger.debug("编辑取得的"+rtnList.size()+"条数据");
			List txtList = new ArrayList();
	    	
			int countN = 0;//总条数
			BigDecimal amountS = new BigDecimal("0");//总金额
			BigDecimal amount = new BigDecimal("0");//金额
			BigDecimal con_100 = new BigDecimal("100");//金额
			if (rtnList.size() == 0) {
	//			paraMap.put("F_1", accountDate);//账期
	//			paraMap.put("F_2", " ");
	//			paraMap.put("F_3", " ");
	//			paraMap.put("F_4", " ");
	//			paraMap.put("F_5", "0");
	//			paraMap.put("F_6", " ");
	//			paraMap.put("F_7", " ");
	//			paraMap.put("F_8", " ");
	//			paraMap.put("F_9", " ");
	//			paraMap.put("F_10", " ");
	//			paraMap.put("F_11", "0");
	//			paraMap.put("F_12", "0");
	//			paraMap.put("F_13", " ");
	//			//paraMap.put("F_14", "无数据");
	//			excelList.add(paraMap);
			} else {
		    	keyList.setParameterCode(SettleConstants.BILLDAY);
	    		parameterInfo = parameterInfoManager.queryList(keyList);
	    		String billDay = parameterInfo.get(0).getParameterValue();
		    	keyList.setParameterCode(SettleConstants.REPAYMENTDAY);
	    		parameterInfo = parameterInfoManager.queryList(keyList);
	    		String repaymentDay = parameterInfo.get(0).getParameterValue();
		    	
				for (Map<String,Object> tmpbean:rtnList) {
					paraMap = new HashMap();
	
					paraMap.put("F_1", accountDate);//账期
					paraMap.put("F_2", tmpbean.get("proid"));//机构号
					paraMap.put("F_3", tmpbean.get("merid"));//商户号
					paraMap.put("F_4", tmpbean.get("userid"));//用户号
					paraMap.put("F_5", tmpbean.get("orderno"));//交易流水号
					paraMap.put("F_6", tmpbean.get("imercd"));//商户编码
					amount = new BigDecimal(tmpbean.get("amount").toString()).divide(con_100);
					paraMap.put("F_7", amount.setScale(2).toString());//交易金额
					paraMap.put("F_8", tmpbean.get("orderdate"));//交易时间
					paraMap.put("F_9", "1");//还款期数
					paraMap.put("F_10", billDay);//账单日
					paraMap.put("F_11", repaymentDay);//还款日
					txtList.add(paraMap);
					countN++;
					amountS = amountS.add(amount);
				}
			}
	
			String path = SettleConstants.FILE_UP_PATH + accountDate;
			File filePath = new File(path);
			if (!filePath.exists()) {
				filePath.mkdirs();
			}
			path=path+File.separator + "FN_P2P_"+accountDate+"_1.csv";
			
			configMap.put("FILE", path);
			
			List reportHead = new LinkedList();
			List reportTail = new LinkedList();
			
			Map infoMap = null;
			reportHead.clear();
			
			infoMap =  new HashMap();
			infoMap.put("F_1", accountDate);
			infoMap.put("F_2", countN+"");
			infoMap.put("F_3", amountS.setScale(2).toString());
			reportHead.add(infoMap);
	
			//configMap.put("REPORT-HEAD", reportHead);
			
	//		reportTail.clear();
	//		infoMap =  new HashMap();
	//		infoMap.put("ROW", "6");
	//		infoMap.put("COL", "3");
	//		infoMap.put("VALUE", "制表人：");
	//		reportTail.add(infoMap);
	//		infoMap =  new HashMap();
	//		infoMap.put("ROW", "7");
	//		infoMap.put("COL", "3");
	//		infoMap.put("VALUE", "制表时间：");
	//		reportTail.add(infoMap);
	//
	//		configMap.put("REPORT-TAIL", reportTail);

			String Key = userProperties.getProperty("P2P_PUBLIC_KEY");
			try {
				TxtWriter.WriteTxt(txtList, configMap, SettleConstants.DEDT_SPLIT2,"UTF-8");
				rtnurlKey = UploadAndDownLoadUtils.uploadFile(path, 3, formatter.parse(accountDate), "FNWALLET001", SettleConstants.FILE_XML,Key,0,userProperties.getProperty("FSAPP_KEY"),userProperties.getProperty("FSDAPP_SECRET"),userProperties.getProperty("FSROP_URL"));
			} catch (Exception e) {
				logger.error("生成债权包文件操作异常！" + e.getMessage());
				errorResponseService.getErrorResponse("S2", "债权包文件生成或文件服务器连接失败");
				return null;
			}

	} catch (Exception z) {
		logger.error("债权包文件生成失败，请联系相关负责人");
		errorResponseService.getErrorResponse("S3", "债权包文件生成失败，请联系相关负责人");
	}
		logger.info("生成P2P债权包文件 ————————————END————————————");
		return rtnurlKey;
	}

	@Override
	@Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
	public ErrorResponse readP2PDebtFile(String invoicedate) {
		logger.info("读取债权包文件 ————————————START————————————");

    	ParameterInfoQuery keyList =  new ParameterInfoQuery();
    	keyList.setParameterCode(SettleConstants.ACCOUNTDATE);
    	SettlementUtils settlementUtils = new SettlementUtils();
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    	String accountDate = "";
    	String accountDate1 = "";
    	ArithUtil arithUtil = new ArithUtil();
    	try {
			List<TransDaysSummary> transDaysSummaries=new ArrayList<TransDaysSummary>();
	    	List<ParameterInfo> parameterInfo = parameterInfoManager.queryList(keyList);
	    	if (invoicedate == null || "".equals(invoicedate)) {
				try {
					accountDate = settlementUtils.getAccountDate(parameterInfo.get(0).getParameterValue(), "yyyyMMdd",-1);
					accountDate1 = settlementUtils.getAccountDate(parameterInfo.get(0).getParameterValue(), "yyyyMMdd",0);
				} catch (Exception e2) {
					logger.error("计算账期异常！" + e2.getMessage());
					responseR = errorResponseService.getErrorResponse("S1", "账期无效");
					return null;
				}
	    	} else {
	    		accountDate = invoicedate.replace("-", "");
	    	}
			logger.info("取得的账期为"+ accountDate);

			Map paraMap = new HashMap();
			paraMap.put("ACCOUNT_DATE", accountDate+ " 00:00:00");
			paraMap.put("STATUS", SettleConstants.SETTLE_STU_1);
			paraMap.put("FUNC_CODE", TransCodeConst.CREDIT_CONSUME);
			List<Map<String,Object>> rtnList = settlementManager.queryP2PList(paraMap);
			logger.debug("编辑取得的"+rtnList.size()+"条数据");
			
			if(null!=rtnList&&0<rtnList.size()){
				String userId="";//用户Id
				String amount="0";//用户金额
				StringBuilder sbStr=new StringBuilder();//记录订单号，以逗号分隔
				int i=0;
				//统计代扣金额
				for (Map subMap : rtnList) {
					amount=arithUtil.doubleAdd(amount, subMap.get("amount").toString());//金额累加
					sbStr.append(subMap.get("orderno")+",");
				}
				if (!"0".equals(amount)) {
					TransDaysSummary transDaysSummary=new TransDaysSummary();
					transDaysSummary.setTransSumId(redisIdGenerator.createRequestNo());//汇总订单号
					transDaysSummary.setRootInstCd(Constants.FN_ID);//机构号
					transDaysSummary.setOrderType(TransCodeConst.PAYMENT_COLLECTION);//订单类型--代收
					transDaysSummary.setAccountDate(formatter.parse(accountDate));//账单日期
					transDaysSummary.setSummaryAmount(Long.parseLong(amount));//汇总金额
					transDaysSummary.setUserId(TransCodeConst.THIRDPARTYID_P2PZZH);
					//transDaysSummary.setSummaryOrders(sbStr.substring(0,sbStr.length()-1));//订单Id（汇总）
					logger.info("用户"+Constants.P2P_ID+"共代扣"+amount);
					transDaysSummaries.add(transDaysSummary);
				}
				
			}
			//调用代收付接口写入代收付数据
			if(null!=transDaysSummaries&&0<transDaysSummaries.size()){
				for (TransDaysSummary transDaysSummary : transDaysSummaries) {
					//数据入汇总记录表
					transDaysSummaryManager.saveTransDaysSummary(transDaysSummary);
					//获取用户绑卡信息
					AccountInfoQuery accountInfoQuery=new AccountInfoQuery();
					accountInfoQuery.setAccountPurpose(Constants.ACCOUNT_PURPOSE_1);//清算卡类型
					accountInfoQuery.setAccountName(transDaysSummary.getUserId());//用户Id
					accountInfoQuery.setRootInstCd(transDaysSummary.getRootInstCd());//机构号
					List<AccountInfo> accountInfos=accountInfoManager.queryViewByUserIdAndPurpose(accountInfoQuery);
					//准备数据调用接口写入代收付
					GenerationPayment generationPayment=new GenerationPayment();
					if( null == accountInfos || 0 == accountInfos.size()){
						generationPayment.setSendType(SettleConstants.SEND_DEFEAT);
						generationPayment.setErrorCode("卡不存在");
					}else{
						AccountInfo accountInfo=accountInfos.get(0);
						generationPayment.setBankCode(accountInfo.getBankHead());
						generationPayment.setAccountType(accountInfo.getAccountTypeId());
						generationPayment.setAccountNo(accountInfo.getAccountNumber());
						generationPayment.setAccountName(accountInfo.getAccountRealName());
						generationPayment.setAccountProperty(accountInfo.getAccountProperty());
						generationPayment.setProvince(accountInfo.getBankProvince());
						generationPayment.setCity(accountInfo.getBankCity());
						generationPayment.setOpenBankName(accountInfo.getBankBranchName());
						generationPayment.setPayBankCode(accountInfo.getBankBranch());
						generationPayment.setCurrency(accountInfo.getCurrency());
						generationPayment.setCertificateType(accountInfo.getCertificateType());
						generationPayment.setCertificateNumber(accountInfo.getCertificateNumber());	
					}
					generationPayment.setAmount(transDaysSummary.getSummaryAmount());
					generationPayment.setOrderNo(transDaysSummary.getTransSumId());//订单号
					generationPayment.setRootInstCd(transDaysSummary.getRootInstCd());//机构号
					generationPayment.setOrderType(SettleConstants.ORDER_BOND_PACKAGE);//订单类型
					generationPayment.setUserId(transDaysSummary.getUserId());
					//generationPayment.setSendType(SettleConstants.SEND_NORMAL);
					generationPayment.setStatusId(SettleConstants.TAKE_EFFECT);
					generationPayment.setBussinessCode(SettleConstants.COLLECTION_CODE);//业务代码
					generationPayment.setAccountDate(formatter.parse(accountDate));	
						
					String result= generationPaymentService.payMentResultTransform(generationPayment);
					//判断返回结果，ok 结束  ，其他状态需要调用冲正接口
					if(!"ok".equals(result)){
						String[] orderNos=transDaysSummary.getSummaryOrders().split(",");
//							for (String orderNo : orderNos) {
//								if(null!=orderNo&&!"".equals(orderNo)){
//									paymentAccountService.antiDeduct(transOrderInfo, transDaysSummary.getUserId());
//								}
//							}
					}else{
						logger.info("用户"+transDaysSummary.getUserId()+"代扣数据写入成功,共代扣金额"+transDaysSummary.getSummaryAmount());
					}
				}
			}

    	} catch (Exception z) {
			logger.error("代扣操作错误，请联系相关负责人" + z.getMessage());
			return errorResponseService.getErrorResponse("S5", "代扣操作错误，请联系相关负责人");
    	}
		
		
		logger.info("读取债权包文件 ————————————END————————————");
		return null;
	}
	
	@Override
	@Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
	public String createPaymentFile(String rePaydate) {
		logger.info("生成还款明细文件 ————————————START————————————");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    	ParameterInfoQuery keyList =  new ParameterInfoQuery();
    	keyList.setParameterCode(SettleConstants.ACCOUNTDATE);
    	SettlementUtils settlementUtils = new SettlementUtils();
		String rtnurlKey = null;
    	String accountDate = "";
    	try {
    		List<ParameterInfo> parameterInfo = parameterInfoManager.queryList(keyList);
	    	if (rePaydate == null || "".equals(rePaydate)) {
				try {
					accountDate = settlementUtils.getAccountDate(parameterInfo.get(0).getParameterValue(), "yyyyMMdd",-1);
				} catch (Exception e2) {
					logger.error("计算账期异常！" + e2.getMessage());
					responseR = errorResponseService.getErrorResponse("S1", "账期无效");
					return null;
				}
	    	} else {
	    		accountDate = rePaydate.replace("-", "");
	    	}
			logger.info("取得的账期为"+ accountDate);
			
//			Calendar c = Calendar.getInstance();
//	    	c.setTime(new Date());
//	    	c.add(Calendar.DAY_OF_MONTH, -2);
//			String start_date = SettleConstants.DEDT_DATE.replace("{YMD}", formatter.format(c.getTime()));
//	    	c.add(Calendar.DAY_OF_MONTH, 1);
//			String end_date = SettleConstants.DEDT_DATE.replace("{YMD}", formatter.format(c.getTime()));
//			
//			logger.info("将要取得["+start_date+"]至["+end_date+"]的数据");
			
			Map paraMap = new HashMap();
			Map configMap = new HashMap();
//			paraMap.put("ACCOUNT_DATE", accountDate+ " 00:00:00");
//			paraMap.put("START_DATE", start_date);
//			paraMap.put("END_DATE", end_date);
			paraMap.put("STATUS", "99");
			//paraMap.put("FUNC_CODE", TransCodeConst.CREDIT_CONSUME);
			List<Map<String,Object>> rtnList = settlementManager.queryPaymentList(paraMap);
			//List<TransOrderInfo> rtnList = new ArrayList();
			logger.debug("编辑取得的"+rtnList.size()+"条数据");
			List txtList = new ArrayList();
	    	
			int countN = 0;//总条数
			BigDecimal amountS = new BigDecimal("0");//总金额
			BigDecimal amount = new BigDecimal("0");//金额
			BigDecimal con_100 = new BigDecimal("100");//金额
			if (rtnList.size() == 0) {
	//			paraMap.put("F_1", accountDate);//账期
	//			paraMap.put("F_2", " ");
	//			paraMap.put("F_3", " ");
	//			paraMap.put("F_4", " ");
	//			paraMap.put("F_5", "0");
	//			paraMap.put("F_6", " ");
	//			paraMap.put("F_7", " ");
	//			paraMap.put("F_8", " ");
	//			paraMap.put("F_9", " ");
	//			paraMap.put("F_10", " ");
	//			paraMap.put("F_11", "0");
	//			paraMap.put("F_12", "0");
	//			paraMap.put("F_13", " ");
	//			//paraMap.put("F_14", "无数据");
	//			excelList.add(paraMap);
			} else {
				for (Map<String,Object> tmpbean:rtnList) {
					paraMap = new HashMap();

					paraMap.put("F_1", accountDate);//账期
					paraMap.put("F_2", tmpbean.get("proid"));//机构号
					paraMap.put("F_3", tmpbean.get("merid"));//商户号
					paraMap.put("F_4", tmpbean.get("userid"));//用户号
					paraMap.put("F_5", settlementUtils.nvl(tmpbean.get("repaiddate"), ""));//实际还款日
					paraMap.put("F_6", "1");//还款期数
					paraMap.put("F_7", "1");//账期
					amount = new BigDecimal(tmpbean.get("ypay").toString()).divide(con_100);
					paraMap.put("F_8", amount.setScale(2).toString());//应还金额
					if ("1".equals(tmpbean.get("status").toString())) {
						paraMap.put("F_9", amount.setScale(2).toString());//还款金额
					} else {
						paraMap.put("F_9", new BigDecimal("0").setScale(2).toString());//还款金额
					}
					paraMap.put("F_10", tmpbean.get("status"));//状态
					txtList.add(paraMap);
					countN++;
					amountS = amountS.add(amount);
				}
			}
	
			String path = SettleConstants.FILE_UP_PATH + accountDate;
			File filePath = new File(path);
			if (!filePath.exists()) {
				filePath.mkdirs();
			}
			path=path+File.separator + "FN_P2P_H_"+accountDate+"_1.csv";
			
			configMap.put("FILE", path);
			
			List reportHead = new LinkedList();
			List reportTail = new LinkedList();
			
			Map infoMap = null;
			reportHead.clear();
			
			infoMap =  new HashMap();
			infoMap.put("F_1", accountDate);
			infoMap.put("F_2", countN+"");
			infoMap.put("F_3", amountS.setScale(2).toString());
			reportHead.add(infoMap);
	
			//configMap.put("REPORT-HEAD", reportHead);
			
	//		reportTail.clear();
	//		infoMap =  new HashMap();
	//		infoMap.put("ROW", "6");
	//		infoMap.put("COL", "3");
	//		infoMap.put("VALUE", "制表人：");
	//		reportTail.add(infoMap);
	//		infoMap =  new HashMap();
	//		infoMap.put("ROW", "7");
	//		infoMap.put("COL", "3");
	//		infoMap.put("VALUE", "制表时间：");
	//		reportTail.add(infoMap);
	//
	//		configMap.put("REPORT-TAIL", reportTail);

			String Key = userProperties.getProperty("P2P_PUBLIC_KEY");
			try {
				TxtWriter.WriteTxt(txtList, configMap, SettleConstants.DEDT_SPLIT2,"UTF-8");
				rtnurlKey = UploadAndDownLoadUtils.uploadFile(path, 4, formatter.parse(accountDate), "FNWALLET001", SettleConstants.FILE_XML,Key,0,userProperties.getProperty("FSAPP_KEY"),userProperties.getProperty("FSDAPP_SECRET"),userProperties.getProperty("FSROP_URL"));
			} catch (Exception e) {
				logger.error("生成还款明细文件操作异常！" + e.getMessage());
				errorResponseService.getErrorResponse("S2", "还款明细文件生成或文件服务器连接失败");
				return null;
			}

			// 调用晶晶的存储过程把成功的还款过渡至历史表
	    	Map<String, String> param = new HashMap<String, String>();
	    	super.getSqlSession().selectList("MyBatisMap.setrepayment", param);

            if(null==param || !String.valueOf(param.get("on_err_code")).equals("0")){
        		logger.error("还款过渡存储过程失败");
        		errorResponseService.getErrorResponse("S3", "还款过渡存储过程失败");
    			throw new AccountException("还款过渡存储过程失败");
            }
			
			
	} catch (Exception z) {
		logger.error("还款明细文件生成失败，请联系相关负责人");
		errorResponseService.getErrorResponse("S3", "还款明细文件生成失败，请联系相关负责人");
	}
		logger.info("生成还款明细文件 ————————————END————————————");
		return rtnurlKey;
	}

	@Override
	@Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
	public Map readCollateFile(String invoicedate) {
		logger.info("读取通联对账文件 ————————————START————————————");
		Map rtnMap = new HashMap();
		rtnMap.put("errCode", "0000");
		rtnMap.put("errMsg", "成功");

    	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    	SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
    	SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss");
    	SimpleDateFormat formatter3 = new SimpleDateFormat("yyyyMMddHHmmss");
    	SimpleDateFormat formatter4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	ParameterInfoQuery keyList =  new ParameterInfoQuery();
    	keyList.setParameterCode(SettleConstants.DAYEND);
    	List<ParameterInfo> parameterInfo = parameterInfoManager.queryList(keyList);
    	if (!"0".equals(parameterInfo.get(0).getParameterValue())) {
    		rtnMap.put("errCode", "0001");
    		rtnMap.put("errMsg", "日终没有正常结束！");
    		RkylinMailUtil.sendMailThread("清洁算开始异常","日终没有正常结束，不能开始清洁算操作", "21401233@qq.com");
    		return rtnMap;
    	}
    	
    	keyList.setParameterCode(SettleConstants.ACCOUNTDATE);
		parameterInfo = parameterInfoManager.queryList(keyList);
    	SettlementUtils settlementUtils = new SettlementUtils();
		String rtnurlKey = null;
    	String accountDate = "";
    	String accountDate2 = "";
    	try {
	    	if (invoicedate == null || "".equals(invoicedate)) {
				try {
					accountDate = settlementUtils.getAccountDate(parameterInfo.get(0).getParameterValue(), "yyyyMMdd",-1);
					accountDate2 = settlementUtils.getAccountDate(parameterInfo.get(0).getParameterValue(), "yyyyMMdd",0);
				} catch (Exception e2) {
					logger.error("计算账期异常！" + e2.getMessage());
					responseR = errorResponseService.getErrorResponse("S1", "账期无效");
					return null;
				}
	    	} else {
	    		accountDate = settlementUtils.getAccountDate(invoicedate,"yyyyMMdd",-1);
	    		accountDate2 = invoicedate.replace("-", "");
	    	}
			logger.info("取得的账期为"+ accountDate);
			// TODO 上游渠道对账文件取得方式待定
			String filename = "TL_" + accountDate+"."+SettleConstants.FILE_CSV;
			
	    	String p2pfilePath = SettleConstants.FILE_PATH +accountDate + File.separator;
	    	File file = new File(p2pfilePath);
	    	if (!file.exists()) {
	    		file.mkdirs();
	    	}
			logger.info("获取授信文件，调用P2P接口1");
			String dfilename = "";
			String dfilepath = "";
			int nmax = 10;
			String Key = userProperties.getProperty("RKYLIN_PUBLIC_KEY");
			try {
				String urlKeyStr = "";
				List<FileUrl> urlKey = new ArrayList<FileUrl>();
				
				for (int ntry=0;ntry<nmax;ntry++) {
					urlKey = UploadAndDownLoadUtils.getUrlKeys(16, formatter.parse(accountDate), "", SettleConstants.FILE_XML,userProperties.getProperty("FSAPP_KEY"),userProperties.getProperty("FSDAPP_SECRET"),userProperties.getProperty("FSROP_URL"));
					if (urlKey == null || urlKey.size() == 0) {
						Thread.sleep(180000);//三分钟
						continue;
					} else {
						break;
					}
				}
				
				if (urlKey == null || urlKey.size() == 0) {
					logger.error("通联对账文件不存在2！");
					responseR = errorResponseService.getErrorResponse("S1", "通联对账文件不存在！");
					return null;
				}
//				List<FileUrl> urlKey = new ArrayList<FileUrl>();
//				FileUrl fileUrl = new FileUrl();
//				fileUrl.setBatch("001");
//				fileUrl.setInvoice_date(accountDate);
//				fileUrl.setUrl_key("79d973f8-8c1c-4346-abe9-23f3417c0003");
//				urlKey.add(fileUrl);
				
				for (FileUrl fileurl:urlKey) {
					urlKeyStr = fileurl.getUrl_key();
					dfilename = "TL_"+fileurl.getInvoice_date()+"_"+fileurl.getBatch()+"."+SettleConstants.FILE_CSV;
					dfilepath = UploadAndDownLoadUtils.downloadFile(accountDate,SettleConstants.FILE_XML, urlKeyStr,dfilename,request,16,Key,0,userProperties.getProperty("FSAPP_KEY"),userProperties.getProperty("FSDAPP_SECRET"),userProperties.getProperty("FSROP_URL"));
					settlementUtils.copyFile(dfilepath+File.separator+dfilename, p2pfilePath+filename);
					break;
				}
			} catch (Exception e1) {
				logger.error("获取通联对账文件异常3！" + e1.getMessage());
				
			}
			
			
			logger.info("读取通联对账文件4");
			String path=p2pfilePath + filename;
			file = new File(path);
			if (!file.exists()) {
				logger.error("通联对账文件不存在！");
				responseR = errorResponseService.getErrorResponse("S1", "通联对账文件不存在！");
				return null;
			}
			
			File collatefile = file;
			
			// 读取上游对账文件内容
			//第一行:结算日期|批次号|交易笔数|成功笔数|交易金额|退款笔数|退款金额|手续费|清算金额  
			//明细行:交易类型|结算日期|商户号|交易时间|商户订单号|通联流水号|交易金额|手续费|清算金额|币种|商户原始订单金额(分)
			TxtReader txtReader = new TxtReader();
			List<Map> fileList = new ArrayList<Map>();
			try {
				txtReader.setEncode("UTF-8");
				fileList = txtReader.txtreader(collatefile , SettleConstants.DEDT_SPLIT);
			} catch(Exception e) {
				logger.error("对账文件读取异常！" + e.getMessage());
				rtnMap.put("errCode", "0068");
				rtnMap.put("errMsg", "对账文件读取异常！");
				return rtnMap;
			}
			
			if (fileList.size() == 0) {
				rtnMap.put("errCode", "0001");
				rtnMap.put("errMsg", "对账文件里面的内容为空！");
				return rtnMap;
			}
			
			// 编辑上游对账文件数据结构
			Map<String,Map<String,String>> fileMap = new HashMap<String,Map<String,String>>();
			String maxtradetime = "";
			String tmpdate= "";
			for (Map bean : fileList) {
				if (bean.size() <10) {
					continue;
				}
				tmpdate = bean.get("L_3").toString().replace("-", "").replace(":", "").replace(" ", "");
				if (maxtradetime.compareTo(tmpdate) < 0 ) {
					maxtradetime = tmpdate;
				}
				bean.put("STATUS_ID", SettleConstants.COLLATE_STU_2);// 先都制成长款标记
				fileMap.put((String) bean.get("L_4"), bean);
			}
			
			// 取得我方交易数据
			Map paraMap = new HashMap();
			Map configMap = new HashMap();
			paraMap.put("ROOT","'"+ Constants.FN_ID+"'");
			paraMap.put("FUNC_CODE", "'"+ TransCodeConst.CHARGE+"'");
			paraMap.put("TRADE_DATE", accountDate2 + " 13:40:00");
			paraMap.put("PAY_CHANNEL_ID", "01");
			List<TransOrderInfo> rtnList = settlementManager.selectTransOrderInfoD(paraMap);
			//List<TransOrderInfo> rtnList = new ArrayList();
			logger.debug("编辑取得的"+rtnList.size()+"条数据");
			
			// 进行对账
			SettleBalanceEntry settleBalanceEntry = new SettleBalanceEntry();
			List<SettleBalanceEntry> settleBalanceEntryList = new ArrayList<SettleBalanceEntry>();
			TransOrderInfo bean = new TransOrderInfo();
			TransOrderInfo transOrderInfo = new TransOrderInfo();
			ErrorResponse rtnErrorResponse = new ErrorResponse();
			Map subMap = new HashMap();
			BigDecimal b_amount = null;
			BigDecimal b_con100 = new BigDecimal("100");
			
			for (int i=0;i<rtnList.size();i++) {
				settleBalanceEntry = new SettleBalanceEntry();
				bean = rtnList.get(i);
				b_amount = new BigDecimal("0");
				if (fileMap.containsKey(bean.getRequestNo())) {
					subMap = fileMap.get(bean.getRequestNo());
					settleBalanceEntry.setRootInstCd(bean.getMerchantCode());
					settleBalanceEntry.setBatchId((String)subMap.get("L_4"));
					settleBalanceEntry.setTransSeqId(subMap.get("L_0").toString());
					//settleBalanceEntry.setTransType(Integer.parseInt(subMap.get("L_0").toString()));
					settleBalanceEntry.setUserId(bean.getUserId());
					settleBalanceEntry.setOrderNo(bean.getOrderNo());
					settleBalanceEntry.setTransTime(formatter4.parse(subMap.get("L_3").toString()));
					b_amount = new BigDecimal(settlementUtils.nvl(subMap.get("L_6"), "0"));
					b_amount = b_amount.multiply(b_con100);
					settleBalanceEntry.setAmount(b_amount.longValue());
					settleBalanceEntry.setRetriRefNo(TransCodeConst.CHARGE);
					settleBalanceEntry.setMerchantCode(bean.getMerchantCode());
					settleBalanceEntry.setSettleTime(formatter1.parse(subMap.get("L_1").toString()));
					b_amount = new BigDecimal(settlementUtils.nvl(subMap.get("L_7"), "0"));
					b_amount = b_amount.multiply(b_con100);
					settleBalanceEntry.setFee(b_amount.setScale(0).toString());

					if (bean.getStatus() == SettleConstants.TAKE_EFFECT) {//成功交易的场合
						b_amount = new BigDecimal(settlementUtils.nvl(subMap.get("L_6"), "0"));
						b_amount = b_amount.multiply(b_con100);
						if (b_amount.setScale(0).toString().equals(bean.getAmount().toString())) {//平账
							//bean.setStatus(TransCodeConst.TRANS_STATUS_RECONCILIATION_SUCCEED);
							settleBalanceEntry.setStatusId(SettleConstants.COLLATE_STU_1);
							subMap.put("STATUS_ID", SettleConstants.COLLATE_STU_1);
						} else {//错帐
							//bean.setStatus(TransCodeConst.TRANS_STATUS_RECONCILIATION_FAILED);
							settleBalanceEntry.setStatusId(SettleConstants.COLLATE_STU_0);
							subMap.put("STATUS_ID", SettleConstants.COLLATE_STU_0);
							RkylinMailUtil.sendMailThread("上游对账错帐警告","请查询SETTLE_BALANCE_ENTRY的订单号[ORDER_ID]\r\n订单号为["+bean.getOrderNo()+"]", "21401233@qq.com");
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
//						transOrderInfo.setOrderPackageNo("");
//						transOrderInfo.paychannelid
						transOrderInfo.setRemark("qjs_balance");
//						transOrderInfo.requestno
						transOrderInfo.setRequestTime(new Date());
						transOrderInfo.setStatus(SettleConstants.TAKE_EFFECT);
//						transOrderInfo.tradeflowno
						//transOrderInfo.setUserFee(Long.parseLong(settlementUtils.nvl(subMap.get("L_9").toString(), "0")));
						if (Constants.FN_ID.equals(bean.getMerchantCode())) {
							rtnErrorResponse = paymentAccountService.recharge(transOrderInfo, Constants.FN_PRODUCT, bean.getUserId());
						} else {
							RkylinMailUtil.sendMailThread("上游对账长款警告","交易中的机构号不存在：["+bean.getMerchantCode()+"]", "21401233@qq.com");
						}
						
						if (rtnErrorResponse.isIs_success() == true) {
							//bean.setStatus(TransCodeConst.TRANS_STATUS_RECONCILIATION_SUCCEED);
							settleBalanceEntry.setStatusId(SettleConstants.COLLATE_STU_1); //平账
							subMap.put("STATUS_ID", SettleConstants.COLLATE_STU_1);
						} else {
							//bean.setStatus(TransCodeConst.TRANS_STATUS_RECONCILIATION_FAILED);
							settleBalanceEntry.setStatusId(SettleConstants.COLLATE_STU_2); //长款
							subMap.put("STATUS_ID", SettleConstants.COLLATE_STU_1);
							RkylinMailUtil.sendMailThread("上游对账长款警告","请查询SETTLE_BALANCE_ENTRY的订单号[ORDER_ID]\r\n订单号为["+bean.getOrderNo()+"]\r\n补账失败！", "21401233@qq.com");
							Thread.sleep(1000);
						}
					}
					settleBalanceEntryList.add(settleBalanceEntry);
					//settleBalanceEntryManager.saveSettleBalanceEntry(settleBalanceEntry);
				} else {
					if (formatter3.format(bean.getOrderDate()).compareTo(maxtradetime) < 0) {//短款
						settleBalanceEntry.setRootInstCd(bean.getMerchantCode());
						settleBalanceEntry.setBatchId(bean.getRequestNo());
//						settleBalanceEntry.setTransSeqId(bean.getRequestNo());
						settleBalanceEntry.setUserId(bean.getUserId());
						settleBalanceEntry.setOrderNo(bean.getOrderNo());
						settleBalanceEntry.setTransTime(bean.getOrderDate());
						settleBalanceEntry.setAmount(bean.getAmount());;
						settleBalanceEntry.setRetriRefNo(TransCodeConst.CHARGE);
						settleBalanceEntry.setMerchantCode(bean.getMerchantCode());
						//bean.setStatus(TransCodeConst.TRANS_STATUS_RECONCILIATION_FAILED);
						settleBalanceEntry.setStatusId(SettleConstants.COLLATE_STU_3);
						settleBalanceEntryList.add(settleBalanceEntry);
						RkylinMailUtil.sendMailThread("上游对账短款警告","请查询SETTLE_BALANCE_ENTRY的订单号[ORDER_ID]\r\n订单号为["+bean.getOrderNo()+"]", "21401233@qq.com");
						Thread.sleep(1000);
					}
				}
			}
			for (Map filesubMap : fileMap.values()) {
				settleBalanceEntry = new SettleBalanceEntry();
				if (SettleConstants.COLLATE_STU_2 == Integer.parseInt(filesubMap.get("STATUS_ID").toString())) {
					settleBalanceEntry.setRootInstCd("tonglian");
					settleBalanceEntry.setBatchId((String)filesubMap.get("L_4"));
					settleBalanceEntry.setTransSeqId(filesubMap.get("L_0").toString());
					//settleBalanceEntry.setTransType(Integer.parseInt(filesubMap.get("L_2").toString()));
					settleBalanceEntry.setUserId((String)filesubMap.get("L_2"));
					settleBalanceEntry.setOrderNo((String)filesubMap.get("L_4"));
					settleBalanceEntry.setTransTime(formatter4.parse(filesubMap.get("L_3").toString()));
					b_amount = new BigDecimal(settlementUtils.nvl(filesubMap.get("L_6"), "0"));
					b_amount = b_amount.multiply(b_con100);
					settleBalanceEntry.setAmount(b_amount.longValue());
					settleBalanceEntry.setRetriRefNo(TransCodeConst.CHARGE);
					settleBalanceEntry.setMerchantCode("tonglian");
					settleBalanceEntry.setSettleTime(formatter1.parse(filesubMap.get("L_1").toString()));
					b_amount = new BigDecimal(settlementUtils.nvl(filesubMap.get("L_7"), "0"));
					b_amount = b_amount.multiply(b_con100);
					settleBalanceEntry.setFee(b_amount.setScale(0).toString());
					settleBalanceEntry.setStatusId(SettleConstants.COLLATE_STU_2);
					settleBalanceEntryList.add(settleBalanceEntry);
					RkylinMailUtil.sendMailThread("上游对账不知原因长款警告","请查询你方交易中订单号为["+filesubMap.get("L_4")+"]的交易的交易状态\r\n通联此交易为成功交易，请确认之后回复邮件", "21401233@qq.com");
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
			
			//更新交易订单表
//			if (rtnList.size() >0) {
//				for (TransOrderInfo transOrderInfobean:rtnList) {
//					transOrderInfoManager.saveTransOrderInfo(transOrderInfobean);
//				}
//			}
    	} catch (Exception z) {
			logger.error("对账文件生成失败，请联系相关负责人"+z.getMessage());
			rtnMap.put("errCode", "0068");
			rtnMap.put("errMsg", "对账文件生成失败，请联系相关负责人");
			throw new AccountException("对账文件生成失败，请联系相关负责人");
			//return rtnMap;
    	}
		logger.info("读取通联对账文件 ————————————END————————————");
		return rtnMap;
	}

	@Override
	@Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
	public Map readCollateResFile(String invoicedate,String batch) {
		logger.info("读取丰年对账结果文件 ————————————START————————————");
		Map rtnMap = new HashMap();
		rtnMap.put("errCode", "0000");
		rtnMap.put("errMsg", "成功");

    	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    	SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
    	SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss");
    	SimpleDateFormat formatter3 = new SimpleDateFormat("yyyyMMddHHmmss");
    	SimpleDateFormat formatter4 = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
    	ParameterInfoQuery keyList =  new ParameterInfoQuery();
    	keyList.setParameterCode(SettleConstants.DAYEND);
    	List<ParameterInfo> parameterInfo = parameterInfoManager.queryList(keyList);
    	if (!"0".equals(parameterInfo.get(0).getParameterValue())) {
    		rtnMap.put("errCode", "0001");
    		rtnMap.put("errMsg", "日终没有正常结束！");
    		RkylinMailUtil.sendMailThread("清洁算开始异常","日终没有正常结束，不能开始清洁算操作", "21401233@qq.com");
    		return rtnMap;
    	}
		
    	keyList.setParameterCode(SettleConstants.ACCOUNTDATE);
		parameterInfo = parameterInfoManager.queryList(keyList);
    	SettlementUtils settlementUtils = new SettlementUtils();
		String rtnurlKey = null;
    	String accountDate = "";
    	String accountDate2 = "";
    	try {
	    	if (invoicedate == null || "".equals(invoicedate)) {
				try {
					accountDate = settlementUtils.getAccountDate(parameterInfo.get(0).getParameterValue(), "yyyyMMdd",-1);
					accountDate2 = settlementUtils.getAccountDate(parameterInfo.get(0).getParameterValue(), "yyyyMMdd",0);
				} catch (Exception e2) {
					logger.error("计算账期异常！" + e2.getMessage());
					responseR = errorResponseService.getErrorResponse("S1", "账期无效");
					return null;
				}
	    	} else {
	    		accountDate = invoicedate.replace("-", "");
	    		accountDate2 = invoicedate.replace("-", "");
	    	}
			logger.info("取得的账期为"+ accountDate + "||传入批次号为" + batch);

			List<SettleBalanceEntry> resList = new ArrayList<SettleBalanceEntry>();
			SettleBalanceEntryQuery settleBalanceEntryQuery = new SettleBalanceEntryQuery();
			settleBalanceEntryQuery.setRootInstCd(Constants.FN_ID);
			settleBalanceEntryQuery.setSettleTime(formatter3.parse(accountDate+"000000"));
			settleBalanceEntryQuery.setStatusId(99);
			resList = settleBalanceEntryManager.queryList(settleBalanceEntryQuery);
			if (resList.size() > 0) {
				rtnMap.put("errCode", "0001");
				rtnMap.put("errMsg", "相同账期的对账结果文件已经被分润，无法重新上传！");
				return rtnMap;
			}
			
			// 取得分润结果文件
			logger.info("获取丰年对账结果文件");
			String dfilename = "";
			String dfilepath = "";
	    	String filename = "FN_DZ_" + accountDate+"_"+ batch +"."+SettleConstants.FILE_CSV;
			
	    	String p2pfilePath = SettleConstants.FILE_PATH +accountDate + File.separator;
	    	File file = new File(p2pfilePath);
	    	if (!file.exists()) {
	    		file.mkdirs();
	    	}
			String Key = userProperties.getProperty("FN_PRIVATE_KEY");
			try {
				String urlKeyStr = "";
				List<FileUrl> urlKey = UploadAndDownLoadUtils.getUrlKeys(14, formatter.parse(accountDate), batch, SettleConstants.FILE_XML,userProperties.getProperty("FSAPP_KEY"),userProperties.getProperty("FSDAPP_SECRET"),userProperties.getProperty("FSROP_URL"));
				if (urlKey == null || urlKey.size() == 0) {
					logger.error("丰年对账结果文件不存在！");
					rtnMap.put("errCode", "0001");
					rtnMap.put("errMsg", "丰年对账结果文件不存在！");
				}
//				List<FileUrl> urlKey = new ArrayList<FileUrl>();
//				FileUrl fileUrl = new FileUrl();
//				fileUrl.setBatch("001");
//				fileUrl.setInvoice_date(accountDate);
//				fileUrl.setUrl_key("79d973f8-8c1c-4346-abe9-23f3417c0003");
//				urlKey.add(fileUrl);
				
				for (FileUrl fileurl:urlKey) {
					urlKeyStr = fileurl.getUrl_key();
					dfilename = "FN_DZ_"+fileurl.getInvoice_date()+"_"+fileurl.getBatch()+"."+SettleConstants.FILE_CSV;
					dfilepath = UploadAndDownLoadUtils.downloadFile(accountDate,SettleConstants.FILE_XML, urlKeyStr,dfilename,request,14,Key,1,userProperties.getProperty("FSAPP_KEY"),userProperties.getProperty("FSDAPP_SECRET"),userProperties.getProperty("FSROP_URL"));
					settlementUtils.copyFile(dfilepath+File.separator+dfilename, p2pfilePath+filename);
					break;
				}
			} catch (Exception e1) {
				logger.error("获取丰年对账结果文件失败！" + e1.getMessage());
				rtnMap.put("errCode", "0001");
				rtnMap.put("errMsg", "获取丰年对账结果文件失败！");
				return rtnMap;
			}
			
			logger.info("读取丰年对账结果文件");
			String path=p2pfilePath + filename;
			file = new File(path);
//	    	file = new File("C:\\test\\download\\20150507\\FN_DZ_20150507.csv");
			if (!file.exists()) {
				logger.error("丰年对账结果文件不存在!");
				rtnMap.put("errCode", "0001");
				rtnMap.put("errMsg", "丰年对账结果文件不存在!");
				return rtnMap;
			}
			TxtReader txtReader = new TxtReader();
			List<Map> fileList = new ArrayList<Map>();
			try {
				txtReader.setEncode("UTF-8");
				fileList = txtReader.txtreader(file , SettleConstants.DEDT_SPLIT2);
			} catch(Exception e) {
				logger.error("丰年对账结果文件操作异常！" + e.getMessage());
				rtnMap.put("errCode", "0001");
				rtnMap.put("errMsg", "丰年对账结果文件操作异常！");
				return rtnMap;
			}
			
			//File collatefile = new File(SettleConstants.FILE_UP_PATH + accountDate + File.separator + "FN_CCHARGE_"+accountDate+".csv");
			
			//用户号|商户号|交易流水号|交易金额|交易日期|交易时间|功能码|订单号|订单数量|交易状态|手续费1|手续费2|备注|结果
			SettleBalanceEntry settleBalanceEntry = new SettleBalanceEntry();
			List<SettleBalanceEntry> settleBalanceEntryList = new ArrayList<SettleBalanceEntry>();
			TransOrderInfo transOrderInfo = new TransOrderInfo();
			ErrorResponse rtnErrorResponse = new ErrorResponse();
			if (fileList.size() == 0) {
				logger.info("丰年对账结果文件内容为空！");
			}
			for (Map<String,String> subMap:fileList) {
				settleBalanceEntry = new SettleBalanceEntry();
				settleBalanceEntry.setRootInstCd(Constants.FN_ID);
				settleBalanceEntry.setTransSeqId(subMap.get("L_1"));
				//settleBalanceEntry.setTransType(Integer.parseInt(subMap.get("L_6").toString()));
				//settleBalanceEntry.setBatchId(subMap.get("L_6"));
				settleBalanceEntry.setUserId(subMap.get("L_0"));
				settleBalanceEntry.setOrderNo(subMap.get("L_7"));
				if ("".equals(subMap.get("L_4")) && "".equals(subMap.get("L_5"))) {
					
				} else if (!"".equals(subMap.get("L_4")) && "".equals(subMap.get("L_5"))) {
					settleBalanceEntry.setTransTime(formatter1.parse(subMap.get("L_4") + subMap.get("L_5")));
				} else if (!"".equals(subMap.get("L_4")) && !"".equals(subMap.get("L_5"))) {
					settleBalanceEntry.setTransTime(formatter4.parse(subMap.get("L_4") + subMap.get("L_5")));
				}
				settleBalanceEntry.setAmount(Long.parseLong(subMap.get("L_3")));
				settleBalanceEntry.setRetriRefNo(subMap.get("L_6"));
				settleBalanceEntry.setMerchantCode(subMap.get("L_1"));
				settleBalanceEntry.setSettleTime(formatter.parse(accountDate));
				settleBalanceEntry.setFee(subMap.get("L_10"));
				if ("1".equals(subMap.get("L_13"))) {//长款
					settleBalanceEntry.setStatusId(SettleConstants.COLLATE_STU_2);//长款
					RkylinMailUtil.sendMailThread("丰年对账警告","请查询SETTLE_BALANCE_ENTRY的订单号[ORDER_ID]\r\n订单号为["+subMap.get("L_7")+"]", "21401233@qq.com");
					Thread.sleep(1000);
					
					// 信用消费或储值消费的场合，才能补账
//					if (TransCodeConst.CREDIT_CONSUME.equals(subMap.get("L_6")) || TransCodeConst.DEBIT_CONSUME.equals(subMap.get("L_6"))) {
//						transOrderInfo.setAmount(Long.parseLong(subMap.get("L_3"))+Long.parseLong(subMap.get("L_10")));
//						transOrderInfo.setFuncCode(subMap.get("L_6"));
//						transOrderInfo.setInterMerchantCode(subMap.get("L_1"));
//						transOrderInfo.setMerchantCode(Constants.FN_ID);
//						transOrderInfo.setOrderAmount(Long.parseLong(subMap.get("L_3")));
//						transOrderInfo.setOrderCount(Integer.parseInt(subMap.get("L_8")));
//						transOrderInfo.setOrderDate(formatter4.parse(subMap.get("L_4") + subMap.get("L_5")));
//						transOrderInfo.setOrderNo(subMap.get("L_7"));
////						transOrderInfo.setOrderPackageNo("qjs_balance");
////						transOrderInfo.paychannelid
//						transOrderInfo.setRemark("qjs_balance");
////						transOrderInfo.requestno
//						transOrderInfo.setRequestTime(new Date());
//						transOrderInfo.setStatus(SettleConstants.TAKE_EFFECT);
////						transOrderInfo.tradeflowno
//						transOrderInfo.setUserFee(Long.parseLong(subMap.get("L_10")));
//						rtnErrorResponse = paymentAccountService.deduct(transOrderInfo, Constants.FN_PRODUCT, subMap.get("L_0"));
//						
//						if (rtnErrorResponse.isIs_success() == true) {
//							settleBalanceEntry.setRetriRefNo("补账");
//							settleBalanceEntry.setStatusId(SettleConstants.COLLATE_STU_1); //平账
//						}
//					} else {
//					}
				} else if ("2".equals(subMap.get("L_13"))) {//短款
					settleBalanceEntry.setStatusId(SettleConstants.COLLATE_STU_3);//短款
					RkylinMailUtil.sendMailThread("丰年对账短款警告","请查询SETTLE_BALANCE_ENTRY的订单号[ORDER_ID]\r\n订单号为["+subMap.get("L_7")+"]", "21401233@qq.com");
					Thread.sleep(1000);
				}else if ("3".equals(subMap.get("L_13"))) {//错帐
					settleBalanceEntry.setStatusId(SettleConstants.COLLATE_STU_0);//错款
					RkylinMailUtil.sendMailThread("丰年对账错帐警告","请查询SETTLE_BALANCE_ENTRY的订单号[ORDER_ID]\r\n订单号为["+subMap.get("L_7")+"]", "21401233@qq.com");
					Thread.sleep(1000);
				}else if ("0".equals(subMap.get("L_13"))) {//平帐
					settleBalanceEntry.setStatusId(SettleConstants.COLLATE_STU_1);//平款
				}
				settleBalanceEntryList.add(settleBalanceEntry);
			}
			
			// 对账结果插入对账结果表
			resList = new ArrayList<SettleBalanceEntry>();
			if (settleBalanceEntryList.size() > 0) {
				for (SettleBalanceEntry subbean:settleBalanceEntryList) {
					settleBalanceEntryQuery = new SettleBalanceEntryQuery();
					settleBalanceEntryQuery.setOrderNo(subbean.getOrderNo());
					settleBalanceEntryQuery.setRootInstCd(Constants.FN_ID);
					settleBalanceEntryQuery.setSettleTime(formatter3.parse(accountDate+"000000"));
					resList = settleBalanceEntryManager.queryList(settleBalanceEntryQuery);
					if (resList.size() > 0) {
						subbean.setSettleId(resList.get(0).getSettleId());
					}
					settleBalanceEntryManager.saveSettleBalanceEntry(subbean);
				}
			}
    	} catch (Exception z) {
			logger.error("对账文件生成失败，请联系相关负责人"+z.getMessage());
			rtnMap.put("errCode", "0068");
			rtnMap.put("errMsg", "对账文件生成失败，请联系相关负责人");
    	}
		logger.info("读取丰年对账结果文件 ————————————END————————————");
		return rtnMap;
	}
	
	@Override
	@Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
	public Map updateCreditAccount() {
		logger.info("通过代扣结果更新用户余额 ————————————START————————————");
		Map rtnMap = new HashMap();
		rtnMap.put("errCode", "0000");
		rtnMap.put("errMsg", "成功");

    	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    	SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
    	SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss");
    	ParameterInfoQuery keyList =  new ParameterInfoQuery();
    	keyList.setParameterCode(SettleConstants.DAYEND);
    	List<ParameterInfo> parameterInfo = parameterInfoManager.queryList(keyList);
    	if (!"0".equals(parameterInfo.get(0).getParameterValue())) {
    		rtnMap.put("errCode", "0001");
    		rtnMap.put("errMsg", "日终没有正常结束！");
    		RkylinMailUtil.sendMailThread("清洁算开始异常","日终没有正常结束，不能开始清洁算操作", "21401233@qq.com");
    		return rtnMap;
    	}
    	
//    	keyList.setParameterCode(SettleConstants.ACCOUNTDATE);
//		parameterInfo = parameterInfoManager.queryList(keyList);
    	SettlementUtils settlementUtils = new SettlementUtils();
		String rtnurlKey = null;
    	String accountDate = "";
    	try {
//	    	if (invoicedate == null || "".equals(invoicedate)) {
//				try {
//					accountDate = settlementUtils.getAccountDate(parameterInfo.get(0).getParameterValue(), "yyyyMMdd",0);
//				} catch (Exception e2) {
//					logger.error("计算账期异常！" + e2.getMessage());
//					responseR = errorResponseService.getErrorResponse("S1", "账期无效");
//					return null;
//				}
//	    	} else {
//	    		accountDate = invoicedate.replace("-", "");
//	    	}
//			logger.info("取得的账期为"+ accountDate);

			// 检查重复付款
//			Map paraMap = new HashMap();
//			paraMap.put("SEND_TYPE", SettleConstants.SEND_NORMAL);
//			paraMap.put("STATUS_ID", Integer.parseInt(BaseConstants.ACCOUNT_STATUS_OK));
//			List<Map<String,Object>> cntList=settlementManager.selectGenerationCnt(paraMap);
//			String cnt = cntList.get(0).get("cnt").toString();
//			if (!"0".equals(cnt)) {
//				rtnMap.put("errCode", "0001");
//				rtnMap.put("errMsg", "有重复付款！");
//				return rtnMap;
//			}
			
			// 取得当账期成功的代收付结果
			GenerationPaymentQuery generationPaymentQuery = new  GenerationPaymentQuery();
//			generationPaymentQuery.setSendType(SettleConstants.SEND_NORMAL);
			generationPaymentQuery.setStatusId(SettleConstants.SETTLE_STU_1);
			List<GenerationPayment> rtnList=generationPaymentManager.queryList(generationPaymentQuery);
			
			FinanaceEntry finanaceEntry = new FinanaceEntry();
			ErrorResponse errorResponse = null;
			SettleSplittingEntry settleSplittingEntry = new SettleSplittingEntry();
	    	List<TransDaysSummary> tdslist = new ArrayList<TransDaysSummary>();
	    	TransDaysSummaryQuery tdsq = new TransDaysSummaryQuery();
			String rtnMsg = "";
			User user = new User();
			TransDaysSummary transDaysSummary = new TransDaysSummary();
			CreditRepayment creditRepayment = new CreditRepayment();
			GenerationPayment upGenerationPayment = new GenerationPayment();
	    	try {
				// 处理代收付交易TO账户系统
				for (GenerationPayment generationPayment:rtnList) {
					settleSplittingEntry = new SettleSplittingEntry();
					transDaysSummary = new TransDaysSummary();
					creditRepayment = new CreditRepayment();
					upGenerationPayment = new GenerationPayment();
					 if (generationPayment.getSendType() == null || generationPayment.getSendType() == 2) {
							continue;
					 } else if (generationPayment.getSendType() == SettleConstants.SETTLE_STU_0) {
						// 分为三种情况----1债券包,2提现,3还款
						if (SettleConstants.ORDER_BOND_PACKAGE == generationPayment.getOrderType()) {//债券包
							settleSplittingEntry.setSettleType(SettleConstants.SETTLE_TYPE_1);
							finanaceEntry.setPaymentAmount(generationPayment.getAmount());
							finanaceEntry.setReferId(generationPayment.getOrderNo());
							errorResponse = paymentInternalService.rightsPackageReturn(finanaceEntry, generationPayment.getRootInstCd());
							if (errorResponse.isIs_success() == true) {
								settleSplittingEntry.setStatusId(SettleConstants.SETTLE_STU_1);
								transDaysSummary.setStatusId(SettleConstants.SETTLE_STU_1);
								upGenerationPayment.setGeneId(generationPayment.getGeneId());
								upGenerationPayment.setStatusId(3);
							} else {
								settleSplittingEntry.setStatusId(SettleConstants.SETTLE_STU_0);
								transDaysSummary.setStatusId(SettleConstants.SETTLE_STU_0);
								settleSplittingEntry.setRemark("结果成功_更新账户失败");
								//throw new AccountException("ORDER_BOND_PACKAGE--ERROR"+generationPayment.getOrderNo());
			//					RkylinMailUtil.sendMailThread("账户记账流水失败通知","******************账户"+transOrderInfo.getUserId()+"做扣款-"+deductType+"记账流水数据入库连续失败三次", TransCodeConst.FINANACE_ENTRY_ERROR_TOEMAIL);
							}
					    	transDaysSummary.setTransSumId(generationPayment.getOrderNo());
					    	transDaysSummaryManager.updateTransDaysSummary(transDaysSummary);
						} else if (SettleConstants.ORDER_WITHDRAW == generationPayment.getOrderType()) {//提现
							settleSplittingEntry.setSettleType(SettleConstants.SETTLE_TYPE_2);
							finanaceEntry.setPaymentAmount(generationPayment.getAmount());
							finanaceEntry.setReferId(generationPayment.getOrderNo());
							errorResponse = paymentInternalService.withdrawReturn(finanaceEntry, generationPayment.getRootInstCd());
							if (errorResponse.isIs_success() == true) {
								settleSplittingEntry.setStatusId(SettleConstants.SETTLE_STU_1);
								transDaysSummary.setStatusId(SettleConstants.SETTLE_STU_1);
								upGenerationPayment.setGeneId(generationPayment.getGeneId());
								upGenerationPayment.setStatusId(3);
							} else {
								settleSplittingEntry.setStatusId(SettleConstants.SETTLE_STU_0);
								transDaysSummary.setStatusId(SettleConstants.SETTLE_STU_0);
								settleSplittingEntry.setRemark("结果成功_更新账户失败");
								//throw new AccountException("ORDER_WITHDRAW--ERROR"+generationPayment.getOrderNo());
			//					RkylinMailUtil.sendMailThread("账户记账流水失败通知","******************账户"+transOrderInfo.getUserId()+"做扣款-"+deductType+"记账流水数据入库连续失败三次", TransCodeConst.FINANACE_ENTRY_ERROR_TOEMAIL);
							}
					    	transDaysSummary.setTransSumId(generationPayment.getOrderNo());
					    	transDaysSummaryManager.updateTransDaysSummary(transDaysSummary);
						} else if (SettleConstants.ORDER_REPAYMENT == generationPayment.getOrderType()) {//还款
							settleSplittingEntry.setSettleType(SettleConstants.SETTLE_TYPE_3);
							finanaceEntry.setPaymentAmount(generationPayment.getAmount());
							finanaceEntry.setReferId(generationPayment.getOrderNo());
							errorResponse = paymentInternalService.refundReturn(finanaceEntry, generationPayment.getUserId(), generationPayment.getRootInstCd());
							if (errorResponse.isIs_success() == true) {
								settleSplittingEntry.setStatusId(SettleConstants.SETTLE_STU_1);
								creditRepayment.setStatusId(SettleConstants.SETTLE_STU_1);
								upGenerationPayment.setGeneId(generationPayment.getGeneId());
								upGenerationPayment.setStatusId(3);
							} else {
								settleSplittingEntry.setStatusId(SettleConstants.SETTLE_STU_0);
								creditRepayment.setStatusId(SettleConstants.SETTLE_STU_0);
								settleSplittingEntry.setRemark("结果成功_更新账户失败");
								//throw new AccountException("ORDER_REPAYMENT--ERROR"+generationPayment.getOrderNo());
			//					RkylinMailUtil.sendMailThread("账户记账流水失败通知","******************账户"+transOrderInfo.getUserId()+"做扣款-"+deductType+"记账流水数据入库连续失败三次", TransCodeConst.FINANACE_ENTRY_ERROR_TOEMAIL);
							}
							creditRepayment.setCredId(Integer.parseInt(generationPayment.getOrderNo()));
							creditRepayment.setRepaymentRepaidDate(new Date());
							creditRepaymentManager.saveCreditRepayment(creditRepayment);
						} else if (SettleConstants.ORDER_COLLECTION == generationPayment.getOrderType()) {//代收
							if (Constants.HT_ID.equals(generationPayment.getRootInstCd())) {
								settleSplittingEntry.setSettleType(SettleConstants.ORDER_COLLECTION);
								finanaceEntry.setPaymentAmount(generationPayment.getAmount());
								finanaceEntry.setReferId(generationPayment.getOrderNo());
								errorResponse = paymentInternalService.collectionReturn(finanaceEntry, generationPayment.getRootInstCd(),generationPayment.getUserId(),Constants.HT_PRODUCT);
								if (errorResponse.isIs_success() == true) {
									settleSplittingEntry.setStatusId(SettleConstants.SETTLE_STU_1);
									transDaysSummary.setStatusId(SettleConstants.SETTLE_STU_1);
									upGenerationPayment.setGeneId(generationPayment.getGeneId());
									upGenerationPayment.setStatusId(3);
								} else {
									settleSplittingEntry.setStatusId(SettleConstants.SETTLE_STU_0);
									transDaysSummary.setStatusId(SettleConstants.SETTLE_STU_0);
									settleSplittingEntry.setRemark("结果成功_更新账户失败");
									//throw new AccountException("ORDER_WITHDRAW--ERROR"+generationPayment.getOrderNo());
				//					RkylinMailUtil.sendMailThread("账户记账流水失败通知","******************账户"+transOrderInfo.getUserId()+"做扣款-"+deductType+"记账流水数据入库连续失败三次", TransCodeConst.FINANACE_ENTRY_ERROR_TOEMAIL);
								}
							} else if (Constants.FN_ID.equals(generationPayment.getRootInstCd())) {
								settleSplittingEntry.setSettleType(SettleConstants.ORDER_COLLECTION);
								finanaceEntry.setPaymentAmount(generationPayment.getAmount());
								finanaceEntry.setReferId(generationPayment.getOrderNo());
								errorResponse = paymentInternalService.collectionReturn(finanaceEntry, generationPayment.getRootInstCd(),generationPayment.getUserId(),Constants.FN_PRODUCT);
								if (errorResponse.isIs_success() == true) {
									settleSplittingEntry.setStatusId(SettleConstants.SETTLE_STU_1);
									transDaysSummary.setStatusId(SettleConstants.SETTLE_STU_1);
									upGenerationPayment.setGeneId(generationPayment.getGeneId());
									upGenerationPayment.setStatusId(3);
								} else {
									settleSplittingEntry.setStatusId(SettleConstants.SETTLE_STU_0);
									transDaysSummary.setStatusId(SettleConstants.SETTLE_STU_0);
									settleSplittingEntry.setRemark("结果成功_更新账户失败");
									//throw new AccountException("ORDER_WITHDRAW--ERROR"+generationPayment.getOrderNo());
				//					RkylinMailUtil.sendMailThread("账户记账流水失败通知","******************账户"+transOrderInfo.getUserId()+"做扣款-"+deductType+"记账流水数据入库连续失败三次", TransCodeConst.FINANACE_ENTRY_ERROR_TOEMAIL);
								}
								
							}
					    	transDaysSummary.setTransSumId(generationPayment.getOrderNo());
					    	transDaysSummaryManager.updateTransDaysSummary(transDaysSummary);
						} else if (SettleConstants.ORDER_WITHHOLD == generationPayment.getOrderType()) {//代付
							if (Constants.HT_ID.equals(generationPayment.getRootInstCd())) {
								settleSplittingEntry.setSettleType(SettleConstants.ORDER_WITHHOLD);
								finanaceEntry.setPaymentAmount(generationPayment.getAmount());
								finanaceEntry.setReferId(generationPayment.getOrderNo());
								errorResponse = paymentInternalService.withholdReturn(finanaceEntry, generationPayment.getRootInstCd(),generationPayment.getUserId(),Constants.HT_PRODUCT);
								if (errorResponse.isIs_success() == true) {
									settleSplittingEntry.setStatusId(SettleConstants.SETTLE_STU_1);
									transDaysSummary.setStatusId(SettleConstants.SETTLE_STU_1);
									upGenerationPayment.setGeneId(generationPayment.getGeneId());
									upGenerationPayment.setStatusId(3);
								} else {
									settleSplittingEntry.setStatusId(SettleConstants.SETTLE_STU_0);
									transDaysSummary.setStatusId(SettleConstants.SETTLE_STU_0);
									settleSplittingEntry.setRemark("结果成功_更新账户失败");
									//throw new AccountException("ORDER_WITHDRAW--ERROR"+generationPayment.getOrderNo());
				//					RkylinMailUtil.sendMailThread("账户记账流水失败通知","******************账户"+transOrderInfo.getUserId()+"做扣款-"+deductType+"记账流水数据入库连续失败三次", TransCodeConst.FINANACE_ENTRY_ERROR_TOEMAIL);
								}
						    	transDaysSummary.setTransSumId(generationPayment.getOrderNo());
						    	transDaysSummaryManager.updateTransDaysSummary(transDaysSummary);
							}
							settleSplittingEntry.setRemark("未知交易！");
						}
					} else if (generationPayment.getSendType() == SettleConstants.SETTLE_STU_1) {
						settleSplittingEntry.setStatusId(SettleConstants.SETTLE_STU_0);
						if (SettleConstants.ORDER_BOND_PACKAGE == generationPayment.getOrderType()) {//债权包
							settleSplittingEntry.setSettleType(SettleConstants.ORDER_BOND_PACKAGE);
							transDaysSummary.setStatusId(SettleConstants.SETTLE_STU_0);
							transDaysSummary.setTransSumId(generationPayment.getOrderNo());
					    	transDaysSummaryManager.updateTransDaysSummary(transDaysSummary);
							upGenerationPayment.setGeneId(generationPayment.getGeneId());
							upGenerationPayment.setStatusId(3);
						} else if (SettleConstants.ORDER_WITHDRAW == generationPayment.getOrderType()) {//提现
							settleSplittingEntry.setSettleType(SettleConstants.ORDER_WITHDRAW);
							transDaysSummary.setStatusId(SettleConstants.SETTLE_STU_0);
							transDaysSummary.setTransSumId(generationPayment.getOrderNo());
					    	transDaysSummaryManager.updateTransDaysSummary(transDaysSummary);
						} else if (SettleConstants.ORDER_REPAYMENT == generationPayment.getOrderType()) {//还款
							settleSplittingEntry.setSettleType(SettleConstants.ORDER_REPAYMENT);
							creditRepayment.setStatusId(SettleConstants.SETTLE_STU_2);
							creditRepayment.setOverdueFlag(SettleConstants.SETTLE_STU_1);
							creditRepayment.setCredId(Integer.parseInt(generationPayment.getOrderNo()));
							creditRepaymentManager.saveCreditRepayment(creditRepayment);
							upGenerationPayment.setGeneId(generationPayment.getGeneId());
							upGenerationPayment.setStatusId(3);
						} else if (SettleConstants.ORDER_COLLECTION == generationPayment.getOrderType()) {//代收
							settleSplittingEntry.setSettleType(SettleConstants.ORDER_COLLECTION);
							transDaysSummary.setStatusId(SettleConstants.SETTLE_STU_0);
							transDaysSummary.setTransSumId(generationPayment.getOrderNo());
					    	transDaysSummaryManager.updateTransDaysSummary(transDaysSummary);
						} else if (SettleConstants.ORDER_WITHHOLD == generationPayment.getOrderType()) {//代付
							if (Constants.HT_ID.equals(generationPayment.getRootInstCd())) {
								settleSplittingEntry.setSettleType(SettleConstants.ORDER_WITHHOLD);
								transDaysSummary.setStatusId(SettleConstants.SETTLE_STU_0);
								transDaysSummary.setTransSumId(generationPayment.getOrderNo());
						    	transDaysSummaryManager.updateTransDaysSummary(transDaysSummary);
							}
						}
					}
					
					String ordernos = "";
			    	String[] ordernosarr=null;
			    	tdslist = new ArrayList<TransDaysSummary>();
			    	TransOrderInfo transOrderInfo = new TransOrderInfo();
			    	Map paraMap = new HashMap();
			    	String errOrder = "";
			    	// 更新交易表
			    	if (generationPayment.getSendType() == SettleConstants.SETTLE_STU_1 || generationPayment.getSendType() == SettleConstants.SETTLE_STU_0) {
			    		//代收代付交易
			    		if (SettleConstants.ORDER_COLLECTION == generationPayment.getOrderType() 
			    				|| (SettleConstants.ORDER_WITHHOLD == generationPayment.getOrderType() && Constants.HT_ID.equals(generationPayment.getRootInstCd()))
			    				|| SettleConstants.ORDER_WITHDRAW == generationPayment.getOrderType()) {
			    			tdsq = new TransDaysSummaryQuery();
			    			paraMap = new HashMap();
			    			tdsq.setTransSumId(generationPayment.getOrderNo());
			    			tdslist = transDaysSummaryManager.queryList(tdsq);
			    			if (tdslist.size() > 0) {
			    				for (TransDaysSummary tds:tdslist) {
			    					ordernos = tds.getSummaryOrders();
			    					ordernosarr = ordernos.split(SettleConstants.DEDT_SPLIT2);
									for(String orderno:ordernosarr)	{
										if (orderno !=null&&!"".equals(orderno)) {
											paraMap.put("ORDER_NO", orderno);
											if (generationPayment.getSendType() == SettleConstants.SETTLE_STU_0) {
												paraMap.put("STATUS", TransCodeConst.TRANS_STATUS_PAY_SUCCEED);
											    settlementManager.updatetransstatus(paraMap);
											} else if (generationPayment.getSendType() == SettleConstants.SETTLE_STU_1) {
												// 抹账处理
												transOrderInfo = new TransOrderInfo();
												transOrderInfo.setOrderNo(redisIdGenerator.createRequestNo());
												transOrderInfo.setOrderPackageNo(orderno);
												transOrderInfo.setMerchantCode(tds.getRootInstCd());
												errorResponse = paymentInternalService.wipeAccount(transOrderInfo);
												if (errorResponse.isIs_success() == true) {
													upGenerationPayment.setGeneId(generationPayment.getGeneId());
													upGenerationPayment.setStatusId(3);
													paraMap.put("STATUS", TransCodeConst.TRANS_STATUS_PAY_FAILED);
													settlementManager.updatetransstatus(paraMap);										
												} else {
													transOrderInfo = new TransOrderInfo();
													transOrderInfo.setOrderNo(redisIdGenerator.createRequestNo());
													transOrderInfo.setOrderPackageNo(orderno);
													transOrderInfo.setMerchantCode(tds.getRootInstCd());
													transOrderInfo.setFuncCode(TransCodeConst.SETTLEMENT_RT);
													errorResponse = paymentAccountService.antiDeduct(transOrderInfo);
													if (errorResponse.isIs_success() == true) {
														upGenerationPayment.setGeneId(generationPayment.getGeneId());
														upGenerationPayment.setStatusId(3);
														paraMap.put("STATUS", TransCodeConst.TRANS_STATUS_PAY_FAILED);
														settlementManager.updatetransstatus(paraMap);						
													} else {
														errOrder = errOrder + "," + orderno;
														//throw new AccountException("ORDER_WITHDRAW--ERROR"+generationPayment.getOrderNo());
									//					RkylinMailUtil.sendMailThread("账户记账流水失败通知","******************账户"+transOrderInfo.getUserId()+"做扣款-"+deductType+"记账流水数据入库连续失败三次", TransCodeConst.FINANACE_ENTRY_ERROR_TOEMAIL);
													}
												}
												if (!"".equals(errOrder)) {
													settleSplittingEntry.setRemark("结果失败_更新账户失败"+errOrder);
												}
											}
										}
									}
			    				}
			    			}
			    		}
			    	}
					 
			    	if (upGenerationPayment.getGeneId() != null && !"".equals(upGenerationPayment.getGeneId())) {
			    		generationPaymentManager.saveGenerationPayment(upGenerationPayment);
			    	}
			    	
					settleSplittingEntry.setSettleId(redisIdGenerator.settleEntryNo());
					settleSplittingEntry.setRootInstCd(generationPayment.getRootInstCd());
					settleSplittingEntry.setAccountDate(generationPayment.getAccountDate());
//					settleSplittingEntry.setBatchId(generationPayment.getRequestNo());
					settleSplittingEntry.setUserId(generationPayment.getUserId());
					settleSplittingEntry.setAmount(generationPayment.getAmount());
					settleSplittingEntry.setOrderNo(generationPayment.getOrderNo());
			    	settleSplittingEntryManager.saveSettleSplittingEntry(settleSplittingEntry);
				}
	    	} catch (Exception z1) {
				rtnMap.put("errCode", "0002");
				rtnMap.put("errMsg", "处理代收付交易TO账户系统失败！");
    			logger.error("处理代收付交易TO账户系统失败！"+z1.getMessage());
				return rtnMap;
	    	}
		
			// 取得账户操作失败的记录重新TO账户系统
//	    	SettleSplittingEntryQuery settleSplittingEntryQuery = new SettleSplittingEntryQuery();
//	    	settleSplittingEntryQuery.setStatusId(SettleConstants.SETTLE_STU_0);
//	    	List<SettleSplittingEntry> settleSplittingList = settleSplittingEntryManager.queryList(settleSplittingEntryQuery);
//	    	
//	    	for (SettleSplittingEntry bean : settleSplittingList) {
//	    		settleSplittingEntry = new SettleSplittingEntry();
//				transDaysSummary = new TransDaysSummary();
//				if (SettleConstants.SETTLE_TYPE_1 == bean.getSettleType()) {//债券包
//					finanaceEntry.setPaymentAmount(bean.getAmount());
//					finanaceEntry.setReferId(bean.getOrderNo());
//					errorResponse = paymentInternalService.rightsPackageReturn(finanaceEntry, bean.getRootInstCd());
//				} else if (SettleConstants.SETTLE_TYPE_2 == bean.getSettleType()) {//提现
//					finanaceEntry.setPaymentAmount(bean.getAmount());
//					finanaceEntry.setReferId(bean.getOrderNo());
//					errorResponse = paymentInternalService.withdrawReturn(finanaceEntry, bean.getRootInstCd());
//				} else if (SettleConstants.SETTLE_TYPE_3 == bean.getSettleType()) {//还款
//					finanaceEntry.setPaymentAmount(bean.getAmount());
//					finanaceEntry.setReferId(bean.getOrderNo());
//					errorResponse = paymentInternalService.refundReturn(finanaceEntry, bean.getUserId(), bean.getRootInstCd());
//				}
//				settleSplittingEntry = bean;
//				if (errorResponse.isIs_success() == true) {
//					if (SettleConstants.SETTLE_TYPE_1 == bean.getSettleType() ||SettleConstants.SETTLE_TYPE_2 == bean.getSettleType()) {
//						transDaysSummary.setStatusId(SettleConstants.SETTLE_STU_1);
//						transDaysSummary.setTransSumId(bean.getOrderNo());
//				    	transDaysSummaryManager.saveTransDaysSummary(transDaysSummary);
//					} else if (SettleConstants.SETTLE_TYPE_3 == bean.getSettleType()) {
//						creditRepayment.setStatusId(SettleConstants.SETTLE_STU_1);
//						creditRepayment.setCredId(Integer.parseInt(bean.getOrderNo()));
//						creditRepaymentManager.saveCreditRepayment(creditRepayment);
//					}
//					settleSplittingEntry.setStatusId(SettleConstants.SETTLE_STU_1);
//			    	settleSplittingEntryManager.saveSettleSplittingEntry(settleSplittingEntry);
//				} else {
//					continue;
//				}
//	    	}
			
			// 调用晶晶的存储过程把成功的代收付过渡至历史表
            Map<String, String> param = new HashMap<String, String>();
	    	super.getSqlSession().selectList("MyBatisMap.setgeneration", param);

            if(null==param||!String.valueOf(param.get("on_err_code")).equals("0")){
    			rtnMap.put("errCode", "0004");
    			rtnMap.put("errMsg", "维护代付表历史数据失败！");
    			logger.error("维护代付表历史数据失败！");
    			return rtnMap;
            }
			
    	} catch (Exception z) {
			logger.error("对账文件生成失败，请联系相关负责人" + z.getMessage());
			rtnMap.put("errCode", "0003");
			rtnMap.put("errMsg", "通过代扣结果更新用户余额！");
			return rtnMap;
    		//throw new RuntimeException(z.getMessage());
    	}
		logger.info("通过代扣结果更新用户余额 ————————————END————————————");
		return rtnMap;
	}

	@Override
	@Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
	public Map updateSettleFile(String invoicedate,String batch) {
		logger.info("通过分润结果更新用户余额 ————————————START————————————");
		Map rtnMap = new HashMap();
		rtnMap.put("errCode", "0000");
		rtnMap.put("errMsg", "成功");

    	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    	SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
    	SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss");
    	SimpleDateFormat formatter3 = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
    	SimpleDateFormat formatter4 = new SimpleDateFormat("yyyyMMddHHmmss");
    	ParameterInfoQuery keyList =  new ParameterInfoQuery();
    	keyList.setParameterCode(SettleConstants.DAYEND);
    	List<ParameterInfo> parameterInfo = parameterInfoManager.queryList(keyList);
    	if (!"0".equals(parameterInfo.get(0).getParameterValue())) {
    		rtnMap.put("errCode", "0001");
    		rtnMap.put("errMsg", "日终没有正常结束！");
    		RkylinMailUtil.sendMailThread("清洁算开始异常","日终没有正常结束，不能开始清洁算操作", "21401233@qq.com");
    		return rtnMap;
    	}
    	
    	keyList.setParameterCode(SettleConstants.ACCOUNTDATE);
		parameterInfo = parameterInfoManager.queryList(keyList);
    	SettlementUtils settlementUtils = new SettlementUtils();
		String rtnurlKey = null;
    	String accountDate = "";;
    	try {
	    	if (invoicedate == null || "".equals(invoicedate)) {
				try {
					accountDate = settlementUtils.getAccountDate(parameterInfo.get(0).getParameterValue(), "yyyyMMdd",0);
				} catch (Exception e2) {
					logger.error("计算账期异常！" + e2.getMessage());
					responseR = errorResponseService.getErrorResponse("S1", "账期无效");
					return null;
				}
	    	} else {
	    		accountDate = invoicedate.replace("-", "");
	    	}
			logger.info("取得的账期为"+ accountDate + "||传入批次号为" + batch);

			List<SettleBalanceEntry> resList = new ArrayList<SettleBalanceEntry>();
			SettleBalanceEntryQuery settleBalanceEntryQuery = new SettleBalanceEntryQuery();
			settleBalanceEntryQuery.setRootInstCd(Constants.FN_ID);
			settleBalanceEntryQuery.setSettleTime(formatter4.parse(accountDate+"000000"));
			settleBalanceEntryQuery.setStatusId(99);
			resList = settleBalanceEntryManager.queryList(settleBalanceEntryQuery);
			if (resList.size() > 0) {
				rtnMap.put("errCode", "0001");
				rtnMap.put("errMsg", "相同账期的对账结果文件已经被分润，无法重新上传！");
				return rtnMap;
			}
			
			// 取得分润结果文件
			logger.info("获取授信文件，调用P2P接口1");
			String dfilename = "";
			String dfilepath = "";
	    	String filename = "FN_FR_" + accountDate+"_" + batch + "."+SettleConstants.FILE_CSV;
			
	    	String p2pfilePath = SettleConstants.FILE_PATH +accountDate + File.separator;
	    	File file = new File(p2pfilePath);
	    	if (!file.exists()) {
	    		file.mkdirs();
	    	}
			String Key = userProperties.getProperty("FN_PRIVATE_KEY");
			try {
				String urlKeyStr = "";
				List<FileUrl> urlKey = UploadAndDownLoadUtils.getUrlKeys(13, formatter.parse(accountDate), batch, SettleConstants.FILE_XML,userProperties.getProperty("FSAPP_KEY"),userProperties.getProperty("FSDAPP_SECRET"),userProperties.getProperty("FSROP_URL"));
				if (urlKey == null || urlKey.size() == 0) {
					logger.error("分润结果文件不存在！");
					rtnMap.put("errCode", "0001");
					rtnMap.put("errMsg", "分润结果文件不存在！");
				}
//				List<FileUrl> urlKey = new ArrayList<FileUrl>();
//				FileUrl fileUrl = new FileUrl();
//				fileUrl.setBatch("001");
//				fileUrl.setInvoice_date(accountDate);
//				fileUrl.setUrl_key("79d973f8-8c1c-4346-abe9-23f3417c0003");
//				urlKey.add(fileUrl);
				
				for (FileUrl fileurl:urlKey) {
					urlKeyStr = fileurl.getUrl_key();
					dfilename = "FN_FR_"+fileurl.getInvoice_date()+"_"+fileurl.getBatch()+"."+SettleConstants.FILE_CSV;
					dfilepath = UploadAndDownLoadUtils.downloadFile(accountDate,SettleConstants.FILE_XML, urlKeyStr,dfilename,request,13,Key,1,userProperties.getProperty("FSAPP_KEY"),userProperties.getProperty("FSDAPP_SECRET"),userProperties.getProperty("FSROP_URL"));
					settlementUtils.copyFile(dfilepath+File.separator+dfilename, p2pfilePath+filename);
					break;
				}
			} catch (Exception e1) {
				logger.error("获取分润结果文件文件失败！" + e1.getMessage());
				rtnMap.put("errCode", "0002");
				rtnMap.put("errMsg", "获取分润结果文件文件失败！");
				return rtnMap;
			}
			
			logger.info("读取丰年分润结果文件");
			String path=p2pfilePath + filename;
			file = new File(path);
			if (!file.exists()) {
				logger.error("丰年分润结果文件不存在!");
				rtnMap.put("errCode", "0003");
				rtnMap.put("errMsg", "丰年分润结果文件不存在!");
				return rtnMap;
			}
//			file = new File("C:\\test\\FN_FR_20150527.csv");
			TxtReader txtReader = new TxtReader();
			List<Map> fileList = new ArrayList<Map>();
			try {
				txtReader.setEncode("UTF-8");
				fileList = txtReader.txtreader(file , SettleConstants.DEDT_SPLIT2);
			} catch(Exception e) {
				logger.error("丰年分润结果文件操作异常！" + e.getMessage());
				rtnMap.put("errCode", "0004");
				rtnMap.put("errMsg", "丰年分润结果文件操作异常！");
				return rtnMap;
			}

			if (fileList.size() == 0) {
				rtnMap.put("errCode", "0005");
				rtnMap.put("errMsg", "丰年分润结果文件内容异常！");
				return rtnMap;
			}
			
			//第一行	帐期}总笔数|总金额
			String amountmax = (String)fileList.get(0).get("L_2");
			
			// 检查总金额
			Map paraMap = new HashMap();
//			paraMap.put("ACCOUNT_DATE", accountDate + " 00:00:00");
//			paraMap.put("FUNC_CODE", TransCodeConst.CHARGE);
//			paraMap.put("STATUS", SettleConstants.SETTLE_STU_1);
			settleBalanceEntryQuery =new  SettleBalanceEntryQuery();
			settleBalanceEntryQuery.setSettleTime(formatter3.parse(accountDate + " 00:00:00"));
			settleBalanceEntryQuery.setStatusId(SettleConstants.SETTLE_STU_1);
			List<SettleBalanceEntry> rtnList = settleBalanceEntryManager.queryList(settleBalanceEntryQuery);
			List<SettleBalanceEntry> upList = new ArrayList<SettleBalanceEntry>();
			SettleBalanceEntry uosbe = new SettleBalanceEntry();
			Long amountmaxto = new Long(0);
			for (SettleBalanceEntry bean:rtnList) {
				uosbe = new SettleBalanceEntry();
				if (TransCodeConst.CREDIT_CONSUME.equals(bean.getRetriRefNo()) || TransCodeConst.DEBIT_CONSUME.equals(bean.getRetriRefNo())) {
					amountmaxto += bean.getAmount();
					uosbe.setSettleId(bean.getSettleId());
					uosbe.setStatusId(99);//分润完了转台
					upList.add(uosbe);
				}
			}
			
			if (amountmax.compareTo(amountmaxto.toString()) == 0) {
				
			} else {
				rtnMap.put("errCode", "0006");
				rtnMap.put("errMsg", "分润文件金额与实际交易不符！文件内金额["+amountmax+"],DB中["+amountmaxto+"]");
				RkylinMailUtil.sendMailThread("分润文件金额与实际交易不符","分润文件金额与实际交易不符,文件内金额["+amountmax+"],DB中["+amountmaxto+"]", "21401233@qq.com");
				return rtnMap;
			}
			
			//第一行	帐期}总笔数|总金额
			//明细行	帐期|机构号|台长号|用户号|金额|备注
			//明细行	帐期,机构号,用户号,台长号,金额,备注
			ErrorResponse errorResponse = null;
			FinanaceEntry finanaceEntry = new FinanaceEntry();
			SettleSplittingEntry settleSplittingEntry = new SettleSplittingEntry();
			for (Map<String,String> subMap : fileList) {
				if (subMap.size() <4) {
					continue;
				}
				settleSplittingEntry = new SettleSplittingEntry();
				settleSplittingEntry.setSettleId(redisIdGenerator.settleEntryNo());
				settleSplittingEntry.setRootInstCd(subMap.get("L_1"));
				settleSplittingEntry.setAccountDate(formatter1.parse(subMap.get("L_0")));
				settleSplittingEntry.setAccountRelateId(subMap.get("L_3"));
				settleSplittingEntry.setUserId(subMap.get("L_2"));
				settleSplittingEntry.setSettleType(SettleConstants.SETTLE_TYPE_5);
				settleSplittingEntry.setAmount(Long.parseLong(subMap.get("L_4")));
				finanaceEntry = new FinanaceEntry();
				finanaceEntry.setPaymentAmount(Long.parseLong(subMap.get("L_4")));
				finanaceEntry.setReferId(settleSplittingEntry.getSettleId());
				if (subMap.get("L_3") != null && !"".equals(subMap.get("L_3"))) {
					errorResponse = paymentInternalService.fenrun(finanaceEntry, settleSplittingEntry.getUserId(),
							settleSplittingEntry.getRootInstCd(),Constants.ADVANCE_ACCOUNT,settleSplittingEntry.getAccountRelateId());
				} else {
					errorResponse = paymentInternalService.fenrun(finanaceEntry, settleSplittingEntry.getUserId(),
							settleSplittingEntry.getRootInstCd(),Constants.FN_PRODUCT,settleSplittingEntry.getAccountRelateId());
				}
				
				if (errorResponse.isIs_success() == true) {
					settleSplittingEntry.setStatusId(SettleConstants.SETTLE_STU_1);
				} else {
					settleSplittingEntry.setStatusId(SettleConstants.SETTLE_STU_0);
					settleSplittingEntry.setRemark("账户操作失败！"+errorResponse.getMsg());
				}
				settleSplittingEntryManager.saveSettleSplittingEntry(settleSplittingEntry);
			}
			
			if (upList.size() > 0) {
				for (SettleBalanceEntry bean:upList) {
					settleBalanceEntryManager.saveSettleBalanceEntry(bean);
				}
			}
			
			//分润结束，退款挂账开始
			SettleTransTabQuery settleTransTabQuery = new SettleTransTabQuery();
			settleTransTabQuery.setStatusId(0);
			List<SettleTransTab> settleTransTabList = settleTransTabManager.queryList(settleTransTabQuery);
			TransOrderInfo transOrderInfo = new TransOrderInfo();
			SettleTransTab settleTransTabbean = new SettleTransTab();
			errorResponse = new ErrorResponse();
			if (settleTransTabList.size() > 0) {
				Long balanceAmount = Long.parseLong("0");
				Long acAmount = Long.parseLong("0");
				for (SettleTransTab settleTransTab:settleTransTabList) {
					transOrderInfo = new TransOrderInfo();
					settleTransTabbean = new SettleTransTab();
					balanceAmount = paymentInternalService.getInternalBalance(settleTransTab.getUserId(), settleTransTab.getRootInstCd(), settleTransTab.getGroupManage(), settleTransTab.getReferUserId());
					if (balanceAmount == 0) {
						
					} else{
						transOrderInfo.setAmount(settleTransTab.getAmount());
						transOrderInfo.setFuncCode(TransCodeConst.ADJUST_ACCOUNT_AMOUNT);
						if (Constants.FN_ID.equals(settleTransTab.getRootInstCd())) {
							transOrderInfo.setInterMerchantCode(TransCodeConst.THIRDPARTYID_FNZZHQY);
						}
						transOrderInfo.setMerchantCode(settleTransTab.getRootInstCd());
						transOrderInfo.setOrderCount(1);
						transOrderInfo.setOrderDate(new Date());
						transOrderInfo.setOrderNo(redisIdGenerator.createRequestNo());
						transOrderInfo.setOrderPackageNo(settleTransTab.getOrderNo());
						transOrderInfo.setRemark("qjs_tuikuan");
						transOrderInfo.setRequestNo(settleTransTab.getBatchNo());
						transOrderInfo.setRequestTime(new Date());
						transOrderInfo.setStatus(1);
						transOrderInfo.setUserFee(Long.parseLong("0"));
						if ( balanceAmount >= settleTransTab.getAmount()) {
							transOrderInfo.setOrderAmount(settleTransTab.getAmount());
							settleTransTabbean.setTabId(settleTransTab.getTabId());
							settleTransTabbean.setStatusId(1);
						} else {
							transOrderInfo.setOrderAmount(balanceAmount);
							acAmount = settleTransTab.getAmount();
							settleTransTabbean = settleTransTab;
							settleTransTabbean.setAmount(acAmount - balanceAmount);
						}
						errorResponse = paymentAccountService.transfer(transOrderInfo, settleTransTab.getGroupManage(), settleTransTab.getUserId());
						if (errorResponse.isIs_success() == true) {
							settleTransTabManager.saveSettleTransTab(settleTransTabbean);
						} else {
							logger.info("分润结束，退款挂账:账户余额：["+balanceAmount+"]需要退款：["+settleTransTab.getAmount()+"],账户操作失败！");
				    		RkylinMailUtil.sendMailThread("分润挂账退款","分润结束，退款挂账:账户余额：["+balanceAmount+"]需要退款：["+settleTransTab.getAmount()+"],账户操作失败！", "21401233@qq.com");
						}
					}
				}
			}
    	} catch (Exception z) {
			logger.error("分润结果更新用户余额失败，请联系相关负责人");
			rtnMap.put("errCode", "0007");
			rtnMap.put("errMsg", "通过分润结果更新用户余额！");
			return rtnMap;
    	}
		logger.info("通过分润结果更新用户余额 ————————————END————————————");
		return rtnMap;
	}

	@Override
	public Map<String,String> proRepayment() {
		logger.info("计息计划任务 ————————————START————————————");
    	Map<String, String> param = new HashMap<String, String>();
    	super.getSqlSession().selectList("MyBatisMap.prorepayment", param);

        if(null==param||!String.valueOf(param.get("on_err_code")).equals("0")){
    		//RkylinMailUtil.sendMailThread("计息(丰年)计划任务","计息计划任务:["+param.get("on_err_code")+"]-["+param.get("ov_err_text")+"]", "21401233@qq.com");
			//logger.error("计息(丰年)计划任务:["+param.get("on_err_code")+"]-["+param.get("ov_err_text")+"]");
        	logger.error("prorepayment 出错了");
        }
		logger.info("计息计划任务 ————————————END————————————");
        return param;
	}
	
	@Override
	@Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
	public Map readCollateFileHT(String invoicedate,String rootInstCd,String fileType,String batch,String wyFlg) {
		logger.info("读取HT通联对账文件 ————————————START————————————");
		Map rtnMap = new HashMap();
		rtnMap.put("errCode", "0000");
		rtnMap.put("errMsg", "成功");

    	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    	SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
    	SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss");
    	SimpleDateFormat formatter3 = new SimpleDateFormat("yyyyMMddHHmmss");
    	SimpleDateFormat formatter4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	ParameterInfoQuery keyList =  new ParameterInfoQuery();
    	keyList.setParameterCode(SettleConstants.DAYEND);
    	List<ParameterInfo> parameterInfo = parameterInfoManager.queryList(keyList);
    	if (!"0".equals(parameterInfo.get(0).getParameterValue())) {
    		rtnMap.put("errCode", "0001");
    		rtnMap.put("errMsg", "日终没有正常结束！");
    		RkylinMailUtil.sendMailThread("清洁算开始异常","日终没有正常结束，不能开始清洁算操作", "21401233@qq.com");
    		return rtnMap;
    	}
    	
    	keyList.setParameterCode(SettleConstants.ACCOUNTDATE);
		parameterInfo = parameterInfoManager.queryList(keyList);
    	SettlementUtils settlementUtils = new SettlementUtils();
		String rtnurlKey = null;
    	String accountDate = "";
    	String accountDate2 = "";
    	try {
	    	if (invoicedate == null || "".equals(invoicedate)) {
				try {
					accountDate = settlementUtils.getAccountDate(parameterInfo.get(0).getParameterValue(), "yyyyMMdd",-1);
					accountDate2 = settlementUtils.getAccountDate(parameterInfo.get(0).getParameterValue(), "yyyyMMdd",0);
				} catch (Exception e2) {
					logger.error("计算账期异常！" + e2.getMessage());
		    		rtnMap.put("errCode", "0001");
		    		rtnMap.put("errMsg", "计算账期异常！");
					return rtnMap;
				}
	    	} else {
	    		accountDate = invoicedate.replace("-", "");
	    		accountDate2 = settlementUtils.getAccountDate(invoicedate,"yyyyMMdd",1);
	    	}
			logger.info("取得的账期为"+ accountDate);
			// TODO 上游渠道对账文件取得方式待定
			String filename = "TLHT_" + accountDate+"_"+fileType+"_"+batch+"."+SettleConstants.FILE_CSV;
			
	    	String p2pfilePath = SettleConstants.FILE_PATH +accountDate + File.separator;
	    	File file = new File(p2pfilePath);
	    	if (!file.exists()) {
	    		file.mkdirs();
	    	}
			logger.info("获取授信文件，调用P2P接口1");
			String dfilename = "";
			String dfilepath = "";
			int nmax = 10;
			String Key = userProperties.getProperty("RKYLIN_PUBLIC_KEY");
			try {
				String urlKeyStr = "";
				List<FileUrl> urlKey = new ArrayList<FileUrl>();
				
				for (int ntry=0;ntry<nmax;ntry++) {
					urlKey = UploadAndDownLoadUtils.getUrlKeys(Integer.parseInt(fileType), formatter.parse(accountDate), batch, SettleConstants.FILE_XML,userProperties.getProperty("FSAPP_KEY"),userProperties.getProperty("FSDAPP_SECRET"),userProperties.getProperty("FSROP_URL"));
					if (urlKey == null || urlKey.size() == 0) {
						Thread.sleep(180000);//三分钟
						continue;
					} else {
						break;
					}
				}
				
				if (urlKey == null || urlKey.size() == 0) {
					logger.error("通联对账文件不存在！");
		    		rtnMap.put("errCode", "0999");
		    		rtnMap.put("errMsg", "通联对账文件不存在！");
					return rtnMap;
				}
//				List<FileUrl> urlKey = new ArrayList<FileUrl>();
//				FileUrl fileUrl = new FileUrl();
//				fileUrl.setBatch("001");
//				fileUrl.setInvoice_date(accountDate);
//				fileUrl.setUrl_key("79d973f8-8c1c-4346-abe9-23f3417c0003");
//				urlKey.add(fileUrl);
				
				for (FileUrl fileurl:urlKey) {
					urlKeyStr = fileurl.getUrl_key();
					dfilename = "TLHT_"+fileurl.getInvoice_date()+"_"+fileurl.getBatch()+"_"+fileType+"."+SettleConstants.FILE_CSV;
					dfilepath = UploadAndDownLoadUtils.downloadFile(accountDate,SettleConstants.FILE_XML, urlKeyStr,dfilename,request,Integer.parseInt(fileType),Key,0,userProperties.getProperty("FSAPP_KEY"),userProperties.getProperty("FSDAPP_SECRET"),userProperties.getProperty("FSROP_URL"));
					settlementUtils.copyFile(dfilepath+File.separator+dfilename, p2pfilePath+filename);
					break;
				}
			} catch (Exception e1) {
				logger.error("获取通联对账文件异常3！" + e1.getMessage());
				
			}
			
			
			logger.info("读取通联对账文件4");
			String path=p2pfilePath + filename;
			file = new File(path);
			if (!file.exists()) {
				logger.error("通联对账文件不存在！");
	    		rtnMap.put("errCode", "0999");
	    		rtnMap.put("errMsg", "通联对账文件不存在！");
				return rtnMap;
			}
			
			File collatefile = file;
			
			// 读取上游对账文件内容
			//第一行:结算日期|批次号|交易笔数|成功笔数|交易金额|退款笔数|退款金额|手续费|清算金额  
			//明细行:交易类型|结算日期|商户号|交易时间|商户订单号|通联流水号|交易金额|手续费|清算金额|币种|商户原始订单金额(分)
			TxtReader txtReader = new TxtReader();
			List<Map> fileList = new ArrayList<Map>();
			try {
				txtReader.setEncode("UTF-8");
				fileList = txtReader.txtreader(collatefile , SettleConstants.DEDT_SPLIT);
			} catch(Exception e) {
				logger.error("对账文件读取异常！" + e.getMessage());
				rtnMap.put("errCode", "0068");
				rtnMap.put("errMsg", "对账文件读取异常！");
				return rtnMap;
			}
			
			if (fileList.size() == 0) {
				rtnMap.put("errCode", "0001");
				rtnMap.put("errMsg", "对账文件里面的内容为空！");
				return rtnMap;
			}
			
			// 编辑上游对账文件数据结构
			Map<String,Map<String,String>> fileMap = new HashMap<String,Map<String,String>>();
			String maxtradetime = "";
			String tmpdate= "";
			for (Map bean : fileList) {
				if (bean.size() <10) {
					continue;
				}
				tmpdate = bean.get("L_3").toString().replace("-", "").replace(":", "").replace(" ", "");
				if (maxtradetime.compareTo(tmpdate) < 0 ) {
					maxtradetime = tmpdate;
				}
				bean.put("STATUS_ID", SettleConstants.COLLATE_STU_2);// 先都制成长款标记
				fileMap.put((String) bean.get("L_4")+(String) bean.get("L_0"), bean);
			}
			
			// 取得我方交易数据
			Map paraMap = new HashMap();
			Map configMap = new HashMap();
//			paraMap.put("ROOT", "'"+Constants.HT_ID+"','"+Constants.KZ_ID+"'");
			paraMap.put("ROOT", rootInstCd);
			paraMap.put("FUNC_CODE", "'"+TransCodeConst.CHARGE+"','"+TransCodeConst.FROZON+"'");
			paraMap.put("TRADE_DATE", accountDate2 + " 13:40:00");
			paraMap.put("PAY_CHANNEL_ID", "01");
			paraMap.put("BUSI_TYPE_ID", wyFlg);
			List<TransOrderInfo> rtnList = settlementManager.selectTransOrderInfoD(paraMap);
			//List<TransOrderInfo> rtnList = new ArrayList();
			logger.debug("编辑取得的"+rtnList.size()+"条数据");
			
			// 进行对账
			SettleBalanceEntry settleBalanceEntry = new SettleBalanceEntry();
			List<SettleBalanceEntry> settleBalanceEntryList = new ArrayList<SettleBalanceEntry>();
			TransOrderInfo bean = new TransOrderInfo();
			TransOrderInfo transOrderInfo = new TransOrderInfo();
			ErrorResponse rtnErrorResponse = new ErrorResponse();
			Map subMap = new HashMap();
			BigDecimal b_amount = null;
			BigDecimal b_con100 = new BigDecimal("100");
			String com_Str="";
			String com_StrKey="";
			
			for (int i=0;i<rtnList.size();i++) {
				settleBalanceEntry = new SettleBalanceEntry();
				bean = rtnList.get(i);
				b_amount = new BigDecimal("0");
				if (TransCodeConst.CHARGE.equals(bean.getFuncCode())) {
					com_StrKey = bean.getRequestNo()+"ZF";
					com_Str = bean.getRequestNo();
				} else if (TransCodeConst.FROZON.equals(bean.getFuncCode())){
					com_StrKey = bean.getRequestNo()+"TH";
					com_Str = bean.getRequestNo();
				}
				if (fileMap.containsKey(com_StrKey)) {
					subMap = fileMap.get(com_StrKey);
					if ("ZF".equals(subMap.get("L_0")) && !TransCodeConst.CHARGE.equals(bean.getFuncCode())) {
						continue;
					}else if ("TH".equals(subMap.get("L_0")) && !TransCodeConst.FROZON.equals(bean.getFuncCode())) {
						continue;
					}
					settleBalanceEntry.setRootInstCd(bean.getMerchantCode());
					settleBalanceEntry.setBatchId((String)subMap.get("L_4"));
					settleBalanceEntry.setTransSeqId(subMap.get("L_0").toString());
					//settleBalanceEntry.setTransType(Integer.parseInt(subMap.get("L_0").toString()));
					settleBalanceEntry.setUserId(bean.getUserId());
					settleBalanceEntry.setOrderNo(com_Str);
					settleBalanceEntry.setTransTime(formatter4.parse(subMap.get("L_3").toString()));
					b_amount = new BigDecimal(settlementUtils.nvl(subMap.get("L_6"), "0"));
					b_amount = b_amount.multiply(b_con100);
					settleBalanceEntry.setAmount(b_amount.longValue());
					settleBalanceEntry.setRetriRefNo(bean.getFuncCode());
					settleBalanceEntry.setMerchantCode(bean.getMerchantCode());
					settleBalanceEntry.setSettleTime(formatter1.parse(subMap.get("L_1").toString()));
					b_amount = new BigDecimal(settlementUtils.nvl(subMap.get("L_7"), "0"));
					b_amount = b_amount.multiply(b_con100);
					settleBalanceEntry.setFee(b_amount.setScale(0).toString());

					if (bean.getStatus() == SettleConstants.TAKE_EFFECT) {//成功交易的场合
						b_amount = new BigDecimal(settlementUtils.nvl(subMap.get("L_6"), "0"));
						b_amount = b_amount.multiply(b_con100);
						if (b_amount.setScale(0).toString().equals(bean.getAmount().toString())) {//平账
							//bean.setStatus(TransCodeConst.TRANS_STATUS_RECONCILIATION_SUCCEED);
							settleBalanceEntry.setStatusId(SettleConstants.COLLATE_STU_1);
							subMap.put("STATUS_ID", SettleConstants.COLLATE_STU_1);
						} else {//错帐
							//bean.setStatus(TransCodeConst.TRANS_STATUS_RECONCILIATION_FAILED);
							settleBalanceEntry.setStatusId(SettleConstants.COLLATE_STU_0);
							subMap.put("STATUS_ID", SettleConstants.COLLATE_STU_0);
//							RkylinMailUtil.sendMailThread("HT上游对账错帐警告","请查询SETTLE_BALANCE_ENTRY的订单号[ORDER_ID]\r\n订单号为["+bean.getOrderNo()+"]", "21401233@qq.com");
						}
					} else {//失败交易的场合（传统意义上的长款）
						if (TransCodeConst.CHARGE.equals(bean.getFuncCode())) {//只有充值场合可以补账
							b_amount = new BigDecimal(settlementUtils.nvl(subMap.get("L_6"), "0"));
							b_amount = b_amount.multiply(b_con100);
							transOrderInfo.setAmount(b_amount.longValue());
							transOrderInfo.setFuncCode(bean.getFuncCode());
							transOrderInfo.setInterMerchantCode(bean.getInterMerchantCode());
							transOrderInfo.setMerchantCode(bean.getMerchantCode());
							transOrderInfo.setOrderAmount(b_amount.longValue());
							transOrderInfo.setOrderCount(bean.getOrderCount());
							transOrderInfo.setOrderDate(bean.getOrderDate());
							transOrderInfo.setOrderNo(bean.getRequestNo());
							transOrderInfo.setRequestNo(bean.getRequestNo());
	//						transOrderInfo.setOrderPackageNo("");
	//						transOrderInfo.paychannelid
							transOrderInfo.setRemark("qjs_balance");
	//						transOrderInfo.requestno
							transOrderInfo.setRequestTime(new Date());
							transOrderInfo.setStatus(SettleConstants.TAKE_EFFECT);
	//						transOrderInfo.tradeflowno
							//transOrderInfo.setUserFee(Long.parseLong(settlementUtils.nvl(subMap.get("L_9").toString(), "0")));
							if (Constants.HT_ID.equals(bean.getMerchantCode())) {
								rtnErrorResponse = paymentAccountService.recharge(transOrderInfo, Constants.HT_PRODUCT, bean.getUserId());
							} else {
//								RkylinMailUtil.sendMailThread("HT上游对账长款警告","交易中的机构号不存在：["+bean.getMerchantCode()+"]", "21401233@qq.com");
							}
							
							if (rtnErrorResponse.isIs_success() == true) {
								//bean.setStatus(TransCodeConst.TRANS_STATUS_RECONCILIATION_SUCCEED);
								settleBalanceEntry.setStatusId(SettleConstants.COLLATE_STU_1); //平账
								subMap.put("STATUS_ID", SettleConstants.COLLATE_STU_1);
							} else {
								//bean.setStatus(TransCodeConst.TRANS_STATUS_RECONCILIATION_FAILED);
								settleBalanceEntry.setStatusId(SettleConstants.COLLATE_STU_2); //长款
								subMap.put("STATUS_ID", SettleConstants.COLLATE_STU_1);
								RkylinMailUtil.sendMailThread("HT上游对账长款警告","请查询SETTLE_BALANCE_ENTRY的订单号[ORDER_ID]\r\n订单号为["+bean.getOrderNo()+"]\r\n补账失败！", "21401233@qq.com");
//								Thread.sleep(1000);
							}
						} else {
							settleBalanceEntry.setStatusId(SettleConstants.COLLATE_STU_3); //短款
							subMap.put("STATUS_ID", SettleConstants.COLLATE_STU_1);
//							RkylinMailUtil.sendMailThread("HT上游对账长款警告","请查询SETTLE_BALANCE_ENTRY的订单号[ORDER_ID]\r\n订单号为["+bean.getOrderNo()+"]\r\n补账失败！", "21401233@qq.com");
//							Thread.sleep(1000);
						}
					}
					settleBalanceEntryList.add(settleBalanceEntry);
					//settleBalanceEntryManager.saveSettleBalanceEntry(settleBalanceEntry);
				} else {
					if (formatter3.format(bean.getRequestTime()).compareTo(maxtradetime) < 0) {//短款
						settleBalanceEntry.setRootInstCd(bean.getMerchantCode());
						settleBalanceEntry.setBatchId(bean.getRequestNo());
//						settleBalanceEntry.setTransSeqId(bean.getRequestNo());
						settleBalanceEntry.setUserId(bean.getUserId());
						settleBalanceEntry.setOrderNo(bean.getRequestNo());
						settleBalanceEntry.setTransTime(bean.getOrderDate());
						settleBalanceEntry.setAmount(bean.getAmount());;
						settleBalanceEntry.setRetriRefNo(bean.getFuncCode());
						settleBalanceEntry.setMerchantCode(bean.getMerchantCode());
						//bean.setStatus(TransCodeConst.TRANS_STATUS_RECONCILIATION_FAILED);
						settleBalanceEntry.setStatusId(SettleConstants.COLLATE_STU_3);
						settleBalanceEntryList.add(settleBalanceEntry);
//						RkylinMailUtil.sendMailThread("HT上游对账短款警告","请查询SETTLE_BALANCE_ENTRY的订单号[ORDER_ID]\r\n订单号为["+bean.getOrderNo()+"]", "21401233@qq.com");
//						Thread.sleep(1000);
					}
				}
			}
			for (Map filesubMap : fileMap.values()) {
				settleBalanceEntry = new SettleBalanceEntry();
				if (SettleConstants.COLLATE_STU_2 == Integer.parseInt(filesubMap.get("STATUS_ID").toString())) {
					settleBalanceEntry.setRootInstCd("tonglian");
					settleBalanceEntry.setBatchId((String)filesubMap.get("L_4"));
					settleBalanceEntry.setTransSeqId(filesubMap.get("L_0").toString());
					//settleBalanceEntry.setTransType(Integer.parseInt(filesubMap.get("L_2").toString()));
					settleBalanceEntry.setUserId((String)filesubMap.get("L_2"));
					settleBalanceEntry.setOrderNo((String)filesubMap.get("L_4"));
					settleBalanceEntry.setTransTime(formatter4.parse(filesubMap.get("L_3").toString()));
					b_amount = new BigDecimal(settlementUtils.nvl(filesubMap.get("L_6"), "0"));
					b_amount = b_amount.multiply(b_con100);
					settleBalanceEntry.setAmount(b_amount.longValue());
					settleBalanceEntry.setRetriRefNo(filesubMap.get("L_0").toString());
					settleBalanceEntry.setMerchantCode("tonglian");
					settleBalanceEntry.setSettleTime(formatter1.parse(filesubMap.get("L_1").toString()));
					b_amount = new BigDecimal(settlementUtils.nvl(filesubMap.get("L_7"), "0"));
					b_amount = b_amount.multiply(b_con100);
					settleBalanceEntry.setFee(b_amount.setScale(0).toString());
					settleBalanceEntry.setStatusId(SettleConstants.COLLATE_STU_2);
					settleBalanceEntryList.add(settleBalanceEntry);
//					RkylinMailUtil.sendMailThread("HT上游对账不知原因长款警告","请查询你方交易中订单号为["+filesubMap.get("L_4")+"]的交易的交易状态\r\n通联此交易为成功交易，请确认之后回复邮件", "21401233@qq.com");
//					Thread.sleep(1000);
				}
			}
			// 对账结果插入对账结果表
			SettleBalanceEntryQuery settleBalanceEntryQuery = new SettleBalanceEntryQuery();
			List<SettleBalanceEntry> resList = new ArrayList<SettleBalanceEntry>();
			if (settleBalanceEntryList.size() > 0) {
				for (SettleBalanceEntry subbean:settleBalanceEntryList) {
					settleBalanceEntryQuery = new SettleBalanceEntryQuery();
					settleBalanceEntryQuery.setOrderNo(subbean.getOrderNo());
					settleBalanceEntryQuery.setRetriRefNo(subbean.getRetriRefNo());
					settleBalanceEntryQuery.setRootInstCd(subbean.getRootInstCd());
					resList = settleBalanceEntryManager.queryList(settleBalanceEntryQuery);
					if (resList.size() > 0) {
						if (resList.get(0).getStatusId() != 1) {
							subbean.setSettleId(resList.get(0).getSettleId());
						}
					}
					settleBalanceEntryManager.saveSettleBalanceEntry(subbean);
				}
			}
			
			//更新交易订单表
//			if (rtnList.size() >0) {
//				for (TransOrderInfo transOrderInfobean:rtnList) {
//					transOrderInfoManager.saveTransOrderInfo(transOrderInfobean);
//				}
//			}
    	} catch (Exception z) {
			logger.error("对账文件生成失败，请联系相关负责人"+z.getMessage());
			rtnMap.put("errCode", "0068");
			rtnMap.put("errMsg", "对账文件生成失败，请联系相关负责人");
			throw new AccountException("对账文件生成失败，请联系相关负责人");
			//return rtnMap;
    	}
		logger.info("读取HT通联对账文件 ————————————END————————————");
		return rtnMap;
	}
	
	@Override
	@Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
	public String createDebtAccountFileHT(String invoicedate,String rootInstCd,String fileType) {
		logger.info("生成会唐（充值）对账文件 ————————————START————————————");
		logger.debug("取得昨日债权消费");
		//测试环境
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    	SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
    	SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss");
    	SimpleDateFormat formatter3 = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
    	ParameterInfoQuery keyList =  new ParameterInfoQuery();
    	keyList.setParameterCode(SettleConstants.DAYEND);
    	List<ParameterInfo> parameterInfo = parameterInfoManager.queryList(keyList);
    	if (!"0".equals(parameterInfo.get(0).getParameterValue())) {
    		RkylinMailUtil.sendMailThread("清洁算开始异常","日终没有正常结束，不能开始清洁算操作", "21401233@qq.com");
    		return null;
    	}
    	
    	keyList.setParameterCode(SettleConstants.ACCOUNTDATE);
		parameterInfo = parameterInfoManager.queryList(keyList);
    	SettlementUtils settlementUtils = new SettlementUtils();
		String rtnurlKey = null;
    	String accountDate = "";
    	String accountDate2 = "";
    	try {
	    	if (invoicedate == null || "".equals(invoicedate)) {
				try {
					accountDate = settlementUtils.getAccountDate(parameterInfo.get(0).getParameterValue(), "yyyyMMdd",0);
					accountDate2 = settlementUtils.getAccountDate(parameterInfo.get(0).getParameterValue(), "yyyyMMdd",-1);
				} catch (Exception e2) {
					logger.error("计算账期异常！" + e2.getMessage());
					responseR = errorResponseService.getErrorResponse("S1", "账期无效");
					return null;
				}
	    	} else {
	    		accountDate = invoicedate.replace("-", "");
	    		accountDate2 = invoicedate.replace("-", "");
	    	}
			logger.info("取得的账期为"+ accountDate2);
			Calendar c = Calendar.getInstance();
	    	c.setTime(new Date());
	    	c.add(Calendar.DAY_OF_MONTH, -2);
			String start_date = SettleConstants.DEDT_ACCOUNT_DATE.replace("{YMD}", formatter.format(c.getTime()));
	    	c.add(Calendar.DAY_OF_MONTH, 1);
			String end_date = SettleConstants.DEDT_ACCOUNT_DATE.replace("{YMD}", formatter.format(c.getTime()));
			
			logger.info("将要取得["+start_date+"]至["+end_date+"]的数据");
			
			Map paraMap = new HashMap();
			Map configMap = new HashMap();
			List<TransOrderInfo> rtnList = new ArrayList<TransOrderInfo>();
//			TransOrderInfoQuery transOrderInfoQuery = new TransOrderInfoQuery();
//			if (Constants.JRD_ID.equals(rootInstCd)) {
//				transOrderInfoQuery.setAccountDate(formatter3.parse(accountDate2 + " 00:00:00"));
//				transOrderInfoQuery.setMerchantCode(rootInstCd);
//				rtnList = transOrderInfoManager.queryList(transOrderInfoQuery);
//			} else {
				paraMap.put("ACCOUNT_DATE", accountDate + " 00:00:00");
				paraMap.put("ROOT", rootInstCd);
				rtnList = settlementManager.selectTransOrderInfoCh(paraMap);
//			}
			//List<TransOrderInfo> rtnList = new ArrayList();
			logger.debug("编辑取得的"+rtnList.size()+"条数据");
			List excelList = new ArrayList();
			
			int countN = 0;//总条数
			BigDecimal amountS = new BigDecimal("0");//总金额
			BigDecimal amount = new BigDecimal("0");//金额
			if (rtnList.size() == 0) {
	//			paraMap.put("F_1", accountDate);//账期
	//			paraMap.put("F_2", " ");
	//			paraMap.put("F_3", " ");
	//			paraMap.put("F_4", " ");
	//			paraMap.put("F_5", "0");
	//			paraMap.put("F_6", " ");
	//			paraMap.put("F_7", " ");
	//			paraMap.put("F_8", " ");
	//			paraMap.put("F_9", " ");
	//			paraMap.put("F_10", " ");
	//			paraMap.put("F_11", "0");
	//			paraMap.put("F_12", "0");
	//			paraMap.put("F_13", " ");
	//			//paraMap.put("F_14", "无数据");
	//			excelList.add(paraMap);
				logger.info("无交易");
				return "无交易";
			} else {
				logger.info("编辑对账文件交易");
				for (TransOrderInfo tmpbean:rtnList) {
					paraMap = new HashMap();
					//充值和退款交易生成对账文件
					if (TransCodeConst.CHARGE.equals(tmpbean.getFuncCode()) || TransCodeConst.FROZON.equals(tmpbean.getFuncCode())) {
						paraMap.put("F_1", accountDate2);//账期
						paraMap.put("F_2", tmpbean.getUserId());//用户号
						paraMap.put("F_3", settlementUtils.nvl(tmpbean.getMerchantCode(),""));//商户号
						paraMap.put("F_5", Long.toString(tmpbean.getAmount()));//交易金额
						paraMap.put("F_6", formatter1.format(tmpbean.getRequestTime()));//交易日期
						paraMap.put("F_7", formatter2.format(tmpbean.getRequestTime()));//交易时间
						paraMap.put("F_8", tmpbean.getFuncCode());//功能码
						paraMap.put("F_4", settlementUtils.nvl(tmpbean.getOrderPackageNo(),""));//交易流水号
						paraMap.put("F_9", tmpbean.getRequestNo());//订单号
						paraMap.put("F_10", tmpbean.getOrderCount().toString());//订单数量
						paraMap.put("F_11", tmpbean.getStatus().toString());//交易状态
						paraMap.put("F_12", settlementUtils.nvl(tmpbean.getFeeAmount(),"0"));//手续费1
						paraMap.put("F_13", settlementUtils.nvl(tmpbean.getUserFee(),"0"));//手续费2
						paraMap.put("F_14", settlementUtils.nvl(tmpbean.getRemark(),""));//备注
						excelList.add(paraMap);
						countN++;
						amount = new BigDecimal(Long.toString(tmpbean.getAmount()));
						amountS = amountS.add(amount);
					}
				}
	
				String path = SettleConstants.FILE_UP_PATH + accountDate2 + File.separator;
				File filePath = new File(path);
				if (!filePath.exists()) {
					filePath.mkdirs();
				}
				path=path + rootInstCd + "_CHARGE_"+accountDate2+".csv";
		//		configMap.put("MODEL_FILE", "src/main/java/com/rkylin/wheatfield/model/dedtaccount.xlsx");
		//		configMap.put("4", "D");
		//		configMap.put("10", "D");
		//		configMap.put("11", "D");
		
				configMap.put("FILE", path);
		//		configMap.put("SHEET", "债权交易");
		//		configMap.put("firstStyleRow", "4");
		//		configMap.put("firstDetailRow", "4");
				
				List reportHead = new LinkedList();
				List reportTail = new LinkedList();
				
				Map infoMap = new HashMap();
				reportHead.clear();
				infoMap.put("F_1", accountDate2);
				infoMap.put("F_2", countN+"");
				infoMap.put("F_3", amountS.toString());
				reportHead.add(infoMap);
				
		//		infoMap =  new HashMap();
		//		infoMap.put("ROW", "1");
		//		infoMap.put("COL", "0");
		//		infoMap.put("VALUE", accountDate);
		//		reportHead.add(infoMap);
		//		infoMap =  new HashMap();
		//		infoMap.put("ROW", "1");
		//		infoMap.put("COL", "1");
		//		infoMap.put("VALUE", countN+"");
		//		reportHead.add(infoMap);
		//		infoMap =  new HashMap();
		//		infoMap.put("ROW", "1");
		//		infoMap.put("COL", "2");
		//		infoMap.put("VALUE", amountS.setScale(2).toString());
		//		reportHead.add(infoMap);
		
				configMap.put("REPORT-HEAD", reportHead);
				
		//		reportTail.clear();
		//		infoMap =  new HashMap();
		//		infoMap.put("ROW", "6");
		//		infoMap.put("COL", "3");
		//		infoMap.put("VALUE", "制表人：");
		//		reportTail.add(infoMap);
		//		infoMap =  new HashMap();
		//		infoMap.put("ROW", "7");
		//		infoMap.put("COL", "3");
		//		infoMap.put("VALUE", "制表时间：");
		//		reportTail.add(infoMap);
		//
		//		configMap.put("REPORT-TAIL", reportTail);
	
				String Key = "";
				int flg = 0;
				if ("27".equals(fileType)) {
					Key = userProperties.getProperty("HT_PRIVATE_KEY");
					flg = 1;
				} else if ("32".equals(fileType)) {
					Key = userProperties.getProperty("P2P_PUBLIC_KEY");
					flg = 0;
				} else {
					return "不明确的文件类型";
				}
				try {
					TxtWriter.WriteTxt(excelList, configMap, SettleConstants.DEDT_SPLIT2,"UTF-8");
					} catch (Exception e) {
					logger.error("文件生成失败！" + e.getMessage());
					responseR = errorResponseService.getErrorResponse("S2", "文件生成失败");
					return null;
				}
				try {
					rtnurlKey = UploadAndDownLoadUtils.uploadFile(path, Integer.parseInt(fileType), formatter.parse(accountDate2), "", SettleConstants.FILE_XML,Key,flg,userProperties.getProperty("FSAPP_KEY"),userProperties.getProperty("FSDAPP_SECRET"),userProperties.getProperty("FSROP_URL"));
				} catch (Exception e) {
					logger.error("第二次上传也失败了！" + e.getMessage());
					responseR = errorResponseService.getErrorResponse("S2", "对账文件生成或文件服务器连接失败");
					return null;
				}
			}
    	} catch (Exception z) {
			logger.error("对账文件生成失败，请联系相关负责人");
    		responseR = errorResponseService.getErrorResponse("S3", "对账文件生成失败，请联系相关负责人");
    	}
		logger.info("生成会唐（充值）对账文件 ————————————END————————————");
		return rtnurlKey;
	}
	
	@Override
	@Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
	public String createDebtAccountFileForAll(String invoicedate,String rootInstCd,String fileType) {
		logger.info("生成（非充值）对账文件 ————————————START————————————");
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    	SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
    	SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss");
    	SimpleDateFormat formatter3 = new SimpleDateFormat("yyyyMMddHHmmss");
    	ParameterInfoQuery keyList =  new ParameterInfoQuery();
    	keyList.setParameterCode(SettleConstants.DAYEND);
    	List<ParameterInfo> parameterInfo = parameterInfoManager.queryList(keyList);
    	if (!"0".equals(parameterInfo.get(0).getParameterValue())) {
    		RkylinMailUtil.sendMailThread("清洁算开始异常","日终没有正常结束，不能开始清洁算操作", "21401233@qq.com");
    		return null;
    	}
    	
    	keyList.setParameterCode(SettleConstants.ACCOUNTDATE);
		parameterInfo = parameterInfoManager.queryList(keyList);
    	SettlementUtils settlementUtils = new SettlementUtils();
		String rtnurlKey = null;
    	String accountDate = "";
    	String accountDate2 = "";
    	try {
	    	if (invoicedate == null || "".equals(invoicedate)) {
				try {
					accountDate = settlementUtils.getAccountDate(parameterInfo.get(0).getParameterValue(), "yyyyMMdd",-1);
					accountDate2 = settlementUtils.getAccountDate(parameterInfo.get(0).getParameterValue(), "yyyyMMdd",-2);
				} catch (Exception e2) {
					logger.error("计算账期异常！" + e2.getMessage());
					responseR = errorResponseService.getErrorResponse("S1", "账期无效");
					return null;
				}
	    	} else {
	    		accountDate = invoicedate.replace("-", "");
				Calendar c = Calendar.getInstance();
		    	c.setTime(formatter1.parse(invoicedate));
		    	c.add(Calendar.DAY_OF_MONTH, -1);
		    	accountDate2 = formatter.format(c.getTime());
	    	}
			logger.info("取得的账期为"+ accountDate);
			
			Map paraMap = new HashMap();
			Map configMap = new HashMap();
			paraMap.put("ACCOUNT_DATE", accountDate + " 00:00:00");
			paraMap.put("FUNC_CODE", "'"+TransCodeConst.CHARGE+"','"+TransCodeConst.FROZON+"'");
			paraMap.put("STATUS", SettleConstants.SETTLE_STU_0);
			paraMap.put("ROOT", rootInstCd);
			List<TransOrderInfo> rtnList = settlementManager.queryList(paraMap);
			//List<TransOrderInfo> rtnList = new ArrayList();
			logger.debug("编辑取得的"+rtnList.size()+"条数据");
			List excelList = new ArrayList();
			
			int countN = 0;//总条数
			BigDecimal amountS = new BigDecimal("0");//总金额
			BigDecimal amount = new BigDecimal("0");//金额
			if (rtnList.size() == 0) {
	//			paraMap.put("F_1", accountDate);//账期
	//			paraMap.put("F_2", " ");
	//			paraMap.put("F_3", " ");
	//			paraMap.put("F_4", " ");
	//			paraMap.put("F_5", "0");
	//			paraMap.put("F_6", " ");
	//			paraMap.put("F_7", " ");
	//			paraMap.put("F_8", " ");
	//			paraMap.put("F_9", " ");
	//			paraMap.put("F_10", " ");
	//			paraMap.put("F_11", "0");
	//			paraMap.put("F_12", "0");
	//			paraMap.put("F_13", " ");
	//			//paraMap.put("F_14", "无数据");
	//			excelList.add(paraMap);
				logger.info("无交易");
				return "无交易";
			} else {
				String createTime = "";
				String minTime = "";
				for (TransOrderInfo tmpbean:rtnList) {
					paraMap = new HashMap();
					if (!TransCodeConst.CHARGE.equals(tmpbean.getFuncCode()) && !TransCodeConst.FROZON.equals(tmpbean.getFuncCode())) {
						createTime = formatter3.format(tmpbean.getCreatedTime());
						minTime = accountDate2 + "230000";
						if (createTime.compareTo(minTime) >= 0) {
							paraMap.put("F_1", accountDate);//账期
							paraMap.put("F_2", tmpbean.getUserId());//用户号
							paraMap.put("F_3", settlementUtils.nvl(tmpbean.getInterMerchantCode(),""));//商户号
							paraMap.put("F_5", Long.toString(tmpbean.getAmount()));//交易金额
							paraMap.put("F_6", formatter1.format(tmpbean.getRequestTime()));//交易日期
							paraMap.put("F_7", formatter2.format(tmpbean.getRequestTime()));//交易时间
							paraMap.put("F_8", tmpbean.getFuncCode());//功能码
							if ("".equals(settlementUtils.nvl(tmpbean.getRequestNo(),""))) {
								paraMap.put("F_4", tmpbean.getOrderNo());//交易流水号
							} else {
								paraMap.put("F_4", settlementUtils.nvl(tmpbean.getRequestNo(),""));//交易流水号
							}
							paraMap.put("F_9", tmpbean.getOrderNo());//订单号
							paraMap.put("F_10", tmpbean.getOrderCount().toString());//订单数量
							paraMap.put("F_11", tmpbean.getStatus().toString());//交易状态
							paraMap.put("F_12", settlementUtils.nvl(tmpbean.getFeeAmount(),"0"));//手续费1
							paraMap.put("F_13", settlementUtils.nvl(tmpbean.getUserFee(),"0"));//手续费2
							paraMap.put("F_14", settlementUtils.nvl(tmpbean.getRemark(),""));//备注
							excelList.add(paraMap);
							countN++;
							amount = new BigDecimal(Long.toString(tmpbean.getAmount()));
							amountS = amountS.add(amount);
						}
					}
				}
			}
	
			String path = SettleConstants.FILE_UP_PATH + accountDate + File.separator;
			File filePath = new File(path);
			if (!filePath.exists()) {
				filePath.mkdirs();
			}
			path=path + rootInstCd + "_CCHARGE_"+accountDate+".csv";;
	//		configMap.put("MODEL_FILE", "src/main/java/com/rkylin/wheatfield/model/dedtaccount.xlsx");
	//		configMap.put("4", "D");
	//		configMap.put("10", "D");
	//		configMap.put("11", "D");
	
			configMap.put("FILE", path);
	//		configMap.put("SHEET", "债权交易");
	//		configMap.put("firstStyleRow", "4");
	//		configMap.put("firstDetailRow", "4");
			
			List reportHead = new LinkedList();
			List reportTail = new LinkedList();
			
			Map infoMap = new HashMap();
			reportHead.clear();
			infoMap.put("F_1", accountDate);
			infoMap.put("F_2", countN+"");
			infoMap.put("F_3", amountS.toString());
			reportHead.add(infoMap);
			
	//		infoMap =  new HashMap();
	//		infoMap.put("ROW", "1");
	//		infoMap.put("COL", "0");
	//		infoMap.put("VALUE", accountDate);
	//		reportHead.add(infoMap);
	//		infoMap =  new HashMap();
	//		infoMap.put("ROW", "1");
	//		infoMap.put("COL", "1");
	//		infoMap.put("VALUE", countN+"");
	//		reportHead.add(infoMap);
	//		infoMap =  new HashMap();
	//		infoMap.put("ROW", "1");
	//		infoMap.put("COL", "2");
	//		infoMap.put("VALUE", amountS.setScale(2).toString());
	//		reportHead.add(infoMap);
	
			configMap.put("REPORT-HEAD", reportHead);
			
	//		reportTail.clear();
	//		infoMap =  new HashMap();
	//		infoMap.put("ROW", "6");
	//		infoMap.put("COL", "3");
	//		infoMap.put("VALUE", "制表人：");
	//		reportTail.add(infoMap);
	//		infoMap =  new HashMap();
	//		infoMap.put("ROW", "7");
	//		infoMap.put("COL", "3");
	//		infoMap.put("VALUE", "制表时间：");
	//		reportTail.add(infoMap);
	//
	//		configMap.put("REPORT-TAIL", reportTail);

			String Key = "";
			int flg = 0;
			if ("11".equals(fileType)) {
				Key = userProperties.getProperty("FN_PRIVATE_KEY");
				flg = 1;
			} else if ("33".equals(fileType)) {
				Key = userProperties.getProperty("P2P_PUBLIC_KEY");
				flg = 0;
			} else {
				return "不明确的文件类型";
			}
			try {
				TxtWriter.WriteTxt(excelList, configMap, SettleConstants.DEDT_SPLIT2,"UTF-8");
			} catch (Exception e) {
				logger.error("文件生成异常！" + e.getMessage());
				responseR = errorResponseService.getErrorResponse("S2", "文件生成异常");
				return null;
			}
			try {
				rtnurlKey = UploadAndDownLoadUtils.uploadFile(path, Integer.parseInt(fileType), formatter.parse(accountDate), "", SettleConstants.FILE_XML,Key,flg,userProperties.getProperty("FSAPP_KEY"),userProperties.getProperty("FSDAPP_SECRET"),userProperties.getProperty("FSROP_URL"));
			} catch (Exception e) {
				logger.error("第二次上传文件也失败了！" + e.getMessage());
				responseR = errorResponseService.getErrorResponse("S2", "对账文件生成或文件服务器连接失败");
				return null;
			}
    	} catch (Exception z) {
			logger.error("对账文件生成失败，请联系相关负责人");
    		responseR = errorResponseService.getErrorResponse("S3", "对账文件生成失败，请联系相关负责人");
    	}
		logger.info("生成丰年（非充值）对账文件 ————————————END————————————");
		return rtnurlKey;
	}
	
	@Override
	public Response doJob(Map<String, String[]> paramMap, String methodName) {
		String reCode = "P0";
		String reMsg = "成功";
		
		String invoicedate=null;
		String rePaydate=null;
		for(Object keyObj : paramMap.keySet().toArray()){
			String[] strs = paramMap.get(keyObj);
			for(String value : strs){
				if(keyObj.equals("invoicedate")){
					invoicedate=value;
				}
				if(keyObj.equals("rePaydate")){
					rePaydate=value;
				}
			}
		}
		
		SettlementResponse response = new SettlementResponse();
		if("ruixue.wheatfield.settlement.creditload".equals(methodName)){
//			List<CreditInfo> creditInfoList = this.getCreditInfo(invoicedate);
//			if (creditInfoList ==null || creditInfoList.size() == 0 ) {
//				return responseR;
//			} else {
//				 response.setCreditInfoList(creditInfoList);
//			}
	//		Map rtnMap = this.readCollateFile(invoicedate);
			Map rtnMap = this.readCollateResFile(invoicedate,null);
//			Map rtnMap = this.updateSettleFile(invoicedate);
			System.out.println("code:"+rtnMap.get("errCode"));
			System.out.println("msg:"+rtnMap.get("errMsg"));

//	    	SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//			try {
//				settlementLogic.getBatchNo(formatter1.parse("2015-06-01 00:00:00"), "10000001", "M000003");
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}else if("ruixue.wheatfield.settlement.collate".equals(methodName)){
	    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	    	//createDebtAccountFileHT("2015-06-17","M000003");
//			readP2PDebtFile(null);
//			String urlkey = null;
//			urlkey = this.createP2PDebtFile(invoicedate); //生成债权包
//			//urlkey = this.createDebtAccountFile(invoicedate);
////			urlkey = this.createDebtAccountFile2(invoicedate);
//			if (urlkey==null || urlkey == "") {
//				return responseR;
//			} else {
//				response.setUrlkey(urlkey);
//			}
			//Map rtnMap = this.readCollateFile(invoicedate);
			//Map rtnMap = this.updateCreditAccount();
//			Map rtnMap = this.updateCreditAccount(invoicedate);
//			System.out.println("code:"+rtnMap.get("errCode"));
//			System.out.println("msg:"+rtnMap.get("errMsg"));
//			String Key = userProperties.getProperty("FN_PRIVATE_KEY");
//			String path = "C:\\test\\upload\\20150427\\FN_CCHARGE_20150427.csv";
//			try {
//				UploadAndDownLoadUtils.uploadFile(path, 11, formatter.parse(invoicedate), "2", SettleConstants.FILE_XML,Key,1);
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

//			String Key = userProperties.getProperty("RKYLIN_PUBLIC_KEY");
////			String Key = userProperties.getProperty("FN_PRIVATE_KEY");
////			Key = userProperties.getProperty("FN_PUBLIC_KEY");
//			try {
//				invoicedate = "2015-05-11";
//				SettlementUtils settlementUtils = new SettlementUtils();
//				String urlKeyStr = "";
//		    	String filename = "DZ_FN_" + invoicedate.replace("-", "")+"."+SettleConstants.FILE_CSV;
//				
//		    	String p2pfilePath = SettleConstants.FILE_PATH +invoicedate.replace("-", "") + File.separator;
//		    	File file = new File(p2pfilePath);
//		    	if (!file.exists()) {
//		    		file.mkdirs();
//		    	}
//				List<FileUrl> urlKey = UploadAndDownLoadUtils.getUrlKeys(16, formatter.parse(invoicedate), "", SettleConstants.FILE_XML,userProperties.getProperty("FSAPP_KEY"),userProperties.getProperty("FSDAPP_SECRET"),userProperties.getProperty("FSROP_URL"));
//				if (urlKey == null || urlKey.size() == 0) {
//					logger.error("授信结果文件不存在！");
//					responseR = errorResponseService.getErrorResponse("S1", "授信结果文件不存在！");
//					return null;
//				}
//				int i = 0;
//				for (FileUrl fileurl:urlKey) {
//					urlKeyStr = fileurl.getUrl_key();
//					String dfilename = "DZ_"+fileurl.getInvoice_date()+"_"+fileurl.getBatch()+"."+SettleConstants.FILE_CSV;
//					String dfilepath = UploadAndDownLoadUtils.downloadFile(invoicedate.replace("-", ""),SettleConstants.FILE_XML, urlKeyStr,dfilename,request,16,Key,0,userProperties.getProperty("FSAPP_KEY"),userProperties.getProperty("FSDAPP_SECRET"),userProperties.getProperty("FSROP_URL"));
//					settlementUtils.copyFile(dfilepath+File.separator+dfilename, p2pfilePath+i+filename);
//					i++;
//					//break;
//				}
//			} catch (Exception e1) {
//				logger.error("获取授信文件，调用P2P接口异常！" + e1.getMessage());
//				responseR = errorResponseService.getErrorResponse("S2", "授信结果文件取得失败！");
//				return null;
//			}
//			String appKey_DZ = userProperties.getProperty("APP_EKY_K");
//			String appSecret_DZ =userProperties.getProperty("APP_SECRET_k");
//			String url_DZ=userProperties.getProperty("ROP_URL_K");
//			DefaultRopClient ropClient = new DefaultRopClient(url_DZ, appKey_DZ,
//					appSecret_DZ, SettleConstants.FILE_XML);
//			FengnianAhpFinanceCompareaccountRequest fileurlRequest=new FengnianAhpFinanceCompareaccountRequest();
//			fileurlRequest.setType(11);
//			try {
//				fileurlRequest.setInvoice_date(formatter.parse(invoicedate));
//				FengnianAhpFinanceCompareaccountResponse filrUrlResponse=ropClient.execute(fileurlRequest, SessionUtils.sessionGet(url_DZ, appKey_DZ,appSecret_DZ));
//				if(filrUrlResponse.getBody().contains("对账完成")){
//
//				}
//				System.out.println(filrUrlResponse);
//				logger.info(filrUrlResponse.getMsg());
//				logger.info(filrUrlResponse.getBody());
//				System.out.println(filrUrlResponse);
//			} catch (Exception e1) {
//				logger.error("获取授信文件，调用P2P接口异常3！" + e1.getMessage());
//				responseR = errorResponseService.getErrorResponse("S2", "授信结果文件取得失败！");
//				return null;
//			}
		}else if("ruixue.wheatfield.settlement.dedtcollate".equals(methodName)){
			String urlkey = null;
			//urlkey = this.createP2PDebtFile(invoicedate);
			urlkey = this.createPaymentFile(invoicedate);
			if (urlkey==null || urlkey == "") {
				return responseR;
			} else {
				response.setUrlkey(urlkey);
			}
		}else if("ruixue.wheatfield.settlement.repayfile".equals(methodName)){
			String urlkey = null;
			urlkey = this.createPaymentFile(rePaydate);
			if (urlkey==null || urlkey == "") {
				return responseR;
			} else {
				response.setUrlkey(urlkey);
			}
		} else if ("ruixue.wheatfield.account.notify".equals(methodName)) {
			 response.setCreditInfoList(this.getCreditInfo(invoicedate));
		}else{
			return errorResponseService.getErrorResponse("S0");
		}
		return response;
	}
	
}
