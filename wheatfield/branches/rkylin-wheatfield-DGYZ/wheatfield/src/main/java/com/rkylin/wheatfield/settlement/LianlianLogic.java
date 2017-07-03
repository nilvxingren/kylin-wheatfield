package com.rkylin.wheatfield.settlement;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.ehcache.util.LongSequence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.Rop.api.domain.FileUrl;
import com.rkylin.file.txt.TxtReader;
import com.rkylin.utils.RkylinMailUtil;
import com.rkylin.wheatfield.common.DateUtils;
import com.rkylin.wheatfield.common.UploadAndDownLoadUtils;
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.constant.SettleConstants;
import com.rkylin.wheatfield.constant.TransCodeConst;
import com.rkylin.wheatfield.exception.AccountException;
import com.rkylin.wheatfield.manager.ParameterInfoManager;
import com.rkylin.wheatfield.manager.SettleBalanceEntryManager;
import com.rkylin.wheatfield.manager.SettleBatchManageManager;
import com.rkylin.wheatfield.manager.SettlementManager;
import com.rkylin.wheatfield.pojo.ParameterInfo;
import com.rkylin.wheatfield.pojo.ParameterInfoQuery;
import com.rkylin.wheatfield.pojo.SettleBalanceEntry;
import com.rkylin.wheatfield.pojo.SettleBalanceEntryQuery;
import com.rkylin.wheatfield.pojo.SettleBatchManage;
import com.rkylin.wheatfield.pojo.SettleBatchManageQuery;
import com.rkylin.wheatfield.pojo.TransOrderInfo;
import com.rkylin.wheatfield.response.ErrorResponse;
import com.rkylin.wheatfield.service.PaymentAccountService;
import com.rkylin.wheatfield.utils.ArithUtil;
import com.rkylin.wheatfield.utils.SFTPUtil;
import com.rkylin.wheatfield.utils.SettlementUtils;
@Component("lianlianLogic")
public class LianlianLogic {
	private static Logger logger = LoggerFactory.getLogger(LianlianLogic.class);

	@Autowired
	HttpServletRequest request;
	@Autowired
	ParameterInfoManager parameterInfoManager;
	@Autowired
	SettlementManager settlementManager;
	@Autowired
	SettlementUtils settlementUtils;
	@Autowired
	SettleBatchManageManager settleBatchManageManager;
	@Autowired
	SFTPUtil ftp;
	@Autowired
	PaymentAccountService paymentAccountService;
	@Autowired
	SettleBalanceEntryManager settleBalanceEntryManager;
	
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
	public Map<String,String> readCollateFileLL(String invoicedate,String rootInstCd,String wyFlg) {
		logger.info("读取连连对账文件 ————————————START————————————");
		Map rtnMap = new HashMap();
		rtnMap.put("errCode", "0000");
		rtnMap.put("errMsg", "成功");

    	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    	SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
    	SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss");
    	SimpleDateFormat formatter3 = new SimpleDateFormat("yyyyMMddHHmmss");
    	SimpleDateFormat formatter4 = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
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
			String filename = "LL_" + accountDate+"_M00000X_";
			
	    	String p2pfilePath = SettleConstants.FILE_PATH +accountDate + File.separator;
	    	File file = new File(p2pfilePath);
	    	if (!file.exists()) {
	    		file.mkdirs();
	    	}
			logger.info("获取授信文件，调用P2P接口1");
			String dfilename = "";
			String dfilepath = "";
			int nmax = 10;
			try {
				ftp.setHost("115.238.110.126");
				ftp.setPort(2122);
				ftp.setUsername("dlrsjf");
				ftp.setPassword("dlrsjf@123");
				
				ftp.connect();
				ftp.download("/dlrsjf/201508131000455502/", "JYMX_201508131000455502_"+accountDate+".txt", p2pfilePath+filename+"JY.txt");
				ftp.download("/dlrsjf/201508131000455502/", "FKMX_201508131000455502_"+accountDate+".txt", p2pfilePath+filename+"FK.txt");
				Thread.sleep(2000);
				ftp.disconnect();
			} catch (Exception e1) {
				ftp.disconnect();
				logger.error("获取连连对账文件异常3！" + e1.getMessage());
			}
			
			logger.info("读取连连对账文件");
			List<Map> fileList = new ArrayList<Map>();
			Map tempMap = new HashMap();
			for (int ii = 0 ; ii < 2 ; ii++) {
				String path=p2pfilePath + filename;
				if (ii == 0) {
					path = path + "JY.txt";
				} else {
					path = path + "FK.txt";
				}
				file = new File(path);
				if (!file.exists()) {
					logger.error("连连对账文件不存在！");
		    		rtnMap.put("errCode", "0999");
		    		rtnMap.put("errMsg", "连连对账文件不存在！");
					return rtnMap;
				}
				
				File collatefile = file;
				
				// 读取上游对账文件内容
				//第一行:标题行
				//明细行:
//				0:商户订单号(商户退款单号)
//				1:商户号
//				2:商户订单时间(yyyymmdd hh24:mm:ss)(商户退款时 间)
//				3:商户业务编号
//				4:银通支付单号(退款时返回商户原订单号)
//				5:银通账务日期
//				6:订单金额(元 保留2位小数)
//				7:商户收款标志(0收款 1付款)
//				8:交易状态(0-成功 5-已退款)
//				9:更新时间(yyyymmdd hh24:mm:ss)
//				10:手续费(元 保留2位小数)
//				11:支付产品(WEB支付网关|手机应用支 付网关|API渠道| WAP支付网关| IVR支付网关)
//				12:支付方式 (余额支付|储蓄卡网银支付|信用 卡网银支付|储蓄卡快捷支付|信用卡快捷支付|线下网点支付|充值卡支付|企业网银支付)
//				13:订单信息
				TxtReader txtReader = new TxtReader();
				List<Map> fileListSub = new ArrayList<Map>();
				try {
					txtReader.setEncode("UTF-8");
					fileListSub = txtReader.txtreader(collatefile , SettleConstants.DEDT_SPLIT2);
				} catch(Exception e) {
					logger.error("对账文件读取异常！" + e.getMessage());
					rtnMap.put("errCode", "0068");
					rtnMap.put("errMsg", "对账文件读取异常！");
					return rtnMap;
				}
				tempMap = new HashMap();
				if (fileListSub.size() > 1) {
					for (int jj = 1 ; jj < fileListSub.size() ; jj++) {
						tempMap = fileListSub.get(jj);
						fileList.add(tempMap);
					}
				}
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
			BigDecimal amount = null;
			BigDecimal con_100 = new BigDecimal("100");
			for (Map bean : fileList) {
				//编辑数据
				System.out.println(bean);
				if ("0".equals(bean.get("L_8").toString())) {
					bean.put("L_8","4015");
				} else if ("5".equals(bean.get("L_8").toString())) {
					bean.put("L_8","4017");
					bean.put("L_0",bean.get("L_4"));
					bean.put("L_6",bean.get("L_6").toString().replace("-", ""));
					bean.put("L_10",bean.get("L_10").toString().replace("-", ""));
				}
				//元  --〉 分
				amount = new BigDecimal(bean.get("L_6").toString());
				amount = amount.multiply(con_100);
				bean.put("L_6",amount.setScale(0).toString());
				amount = new BigDecimal(bean.get("L_10").toString());
				amount = amount.multiply(con_100);
				bean.put("L_10",amount.setScale(0).toString());
				tmpdate = bean.get("L_9").toString().replace("-", "").replace(":", "").replace(" ", "");
				if (maxtradetime.compareTo(tmpdate) < 0 ) {
					maxtradetime = tmpdate;
				}
				bean.put("STATUS_ID", SettleConstants.COLLATE_STU_2);// 先都制成长款标记
				fileMap.put((String) bean.get("L_0")+(String) bean.get("L_8"), bean);
			}
			
			// 取得我方交易数据
			Map paraMap = new HashMap();
			Map configMap = new HashMap();
			paraMap.put("ROOT", rootInstCd);
			paraMap.put("FUNC_CODE", "'"+TransCodeConst.CHARGE+"','"+TransCodeConst.FROZON+"'");
			paraMap.put("TRADE_DATE", accountDate2 + " 08:00:00");
			paraMap.put("PAY_CHANNEL_ID", "04");
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
				// TODO 修改必要
				if (TransCodeConst.CHARGE.equals(bean.getFuncCode())) {
					com_StrKey = bean.getRequestNo()+"4015";
					com_Str = bean.getRequestNo();
				} else if (TransCodeConst.FROZON.equals(bean.getFuncCode())){
					com_StrKey = bean.getRequestNo()+"4017";
					com_Str = bean.getRequestNo();
				}
				if (fileMap.containsKey(com_StrKey)) {
					subMap = fileMap.get(com_StrKey);
					if ("4015".equals(subMap.get("L_8")) && !TransCodeConst.CHARGE.equals(bean.getFuncCode())) {
						continue;
					}else if ("4017".equals(subMap.get("L_8")) && !TransCodeConst.FROZON.equals(bean.getFuncCode())) {
						continue;
					}
					settleBalanceEntry.setRootInstCd(bean.getMerchantCode());
					settleBalanceEntry.setBatchId((String)subMap.get("L_0"));
					settleBalanceEntry.setTransSeqId(subMap.get("L_8").toString());
					//settleBalanceEntry.setTransType(Integer.parseInt(subMap.get("L_0").toString()));
					settleBalanceEntry.setUserId(bean.getUserId());
					settleBalanceEntry.setOrderNo(com_Str);
					settleBalanceEntry.setTransTime(formatter4.parse(subMap.get("L_2").toString()));
					settleBalanceEntry.setAmount(Long.parseLong(subMap.get("L_6").toString()));
					settleBalanceEntry.setRetriRefNo(bean.getFuncCode());
					settleBalanceEntry.setMerchantCode(bean.getMerchantCode());
					settleBalanceEntry.setSettleTime(formatter4.parse(subMap.get("L_9").toString()));
					settleBalanceEntry.setFee(subMap.get("L_10").toString());

					if (bean.getStatus() == SettleConstants.TAKE_EFFECT) {//成功交易的场合
						if (subMap.get("L_6").toString().equals(bean.getAmount().toString())) {//平账
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
//								RkylinMailUtil.sendMailThread("HT上游对账长款警告","请查询SETTLE_BALANCE_ENTRY的订单号[ORDER_ID]\r\n订单号为["+bean.getOrderNo()+"]\r\n补账失败！", "21401233@qq.com");
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
					settleBalanceEntry.setRootInstCd("lianlian");
					settleBalanceEntry.setBatchId((String)filesubMap.get("L_0"));
					settleBalanceEntry.setTransSeqId(filesubMap.get("L_8").toString());
					//settleBalanceEntry.setTransType(Integer.parseInt(filesubMap.get("L_2").toString()));
					settleBalanceEntry.setUserId((String)filesubMap.get("L_1"));
					settleBalanceEntry.setOrderNo((String)filesubMap.get("L_4"));
					settleBalanceEntry.setTransTime(formatter4.parse(filesubMap.get("L_2").toString()));
					b_amount = new BigDecimal(settlementUtils.nvl(filesubMap.get("L_6"), "0"));
					settleBalanceEntry.setAmount(b_amount.longValue());
					settleBalanceEntry.setRetriRefNo(filesubMap.get("L_8").toString());
					settleBalanceEntry.setMerchantCode("lianlian");
					settleBalanceEntry.setSettleTime(formatter4.parse(filesubMap.get("L_9").toString()));
					b_amount = new BigDecimal(settlementUtils.nvl(filesubMap.get("L_10"), "0"));
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
		logger.info("读取连连对账文件 ————————————END————————————");
		return rtnMap;
	}
	
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
	public Map<String,String> readCollateFileM5LL(String invoicedate,String rootInstCd,String wyFlg) {
		logger.info("读取连连对账文件 ————————————START————————————");
		Map rtnMap = new HashMap();
		rtnMap.put("errCode", "0000");
		rtnMap.put("errMsg", "成功");

    	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    	SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
    	SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss");
    	SimpleDateFormat formatter3 = new SimpleDateFormat("yyyyMMddHHmmss");
    	SimpleDateFormat formatter4 = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
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
			String filename = "LL_" + accountDate+"_M000005_";
			
	    	String p2pfilePath = SettleConstants.FILE_PATH +accountDate + File.separator;
	    	File file = new File(p2pfilePath);
	    	if (!file.exists()) {
	    		file.mkdirs();
	    	}
			logger.info("获取授信文件，调用P2P接口1");
			String dfilename = "";
			String dfilepath = "";
			int nmax = 10;
			try {
				ftp.setHost("115.238.110.126");
				ftp.setPort(2122);
				ftp.setUsername("dljunr");
				ftp.setPassword("dljunr@123");
				
				ftp.connect();
				ftp.download("/dljunr/201512021000622601/", "JYMX_201512021000622601_"+accountDate+".txt", p2pfilePath+filename+"JY.txt");
				ftp.download("/dljunr/201512021000622601/", "FKMX_201512021000622601_"+accountDate+".txt", p2pfilePath+filename+"FK.txt");
				Thread.sleep(2000);
				ftp.disconnect();
			} catch (Exception e1) {
				ftp.disconnect();
				logger.error("获取连连对账文件异常3！" + e1.getMessage());
			}
			
			logger.info("读取连连对账文件");
			List<Map> fileList = new ArrayList<Map>();
			Map tempMap = new HashMap();
			for (int ii = 0 ; ii < 2 ; ii++) {
				String path=p2pfilePath + filename;
				if (ii == 0) {
					path = path + "JY.txt";
				} else {
					path = path + "FK.txt";
				}
				file = new File(path);
				if (!file.exists()) {
					logger.error("连连对账文件不存在！");
		    		rtnMap.put("errCode", "0999");
		    		rtnMap.put("errMsg", "连连对账文件不存在！");
					return rtnMap;
				}
				
				File collatefile = file;
				
				// 读取上游对账文件内容
				//第一行:标题行
				//明细行:
//				0:商户订单号(商户退款单号)
//				1:商户号
//				2:商户订单时间(yyyymmdd hh24:mm:ss)(商户退款时 间)
//				3:商户业务编号
//				4:银通支付单号(退款时返回商户原订单号)
//				5:银通账务日期
//				6:订单金额(元 保留2位小数)
//				7:商户收款标志(0收款 1付款)
//				8:交易状态(0-成功 5-已退款)
//				9:更新时间(yyyymmdd hh24:mm:ss)
//				10:手续费(元 保留2位小数)
//				11:支付产品(WEB支付网关|手机应用支 付网关|API渠道| WAP支付网关| IVR支付网关)
//				12:支付方式 (余额支付|储蓄卡网银支付|信用 卡网银支付|储蓄卡快捷支付|信用卡快捷支付|线下网点支付|充值卡支付|企业网银支付)
//				13:订单信息
				TxtReader txtReader = new TxtReader();
				List<Map> fileListSub = new ArrayList<Map>();
				try {
					txtReader.setEncode("UTF-8");
					fileListSub = txtReader.txtreader(collatefile , SettleConstants.DEDT_SPLIT2);
				} catch(Exception e) {
					logger.error("对账文件读取异常！" + e.getMessage());
					rtnMap.put("errCode", "0068");
					rtnMap.put("errMsg", "对账文件读取异常！");
					return rtnMap;
				}
				tempMap = new HashMap();
				if (fileListSub.size() > 1) {
					for (int jj = 1 ; jj < fileListSub.size() ; jj++) {
						tempMap = fileListSub.get(jj);
						fileList.add(tempMap);
					}
				}
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
			BigDecimal amount = null;
			BigDecimal con_100 = new BigDecimal("100");
			for (Map bean : fileList) {
				//编辑数据
				System.out.println(bean);
				if ("0".equals(bean.get("L_8").toString())) {
					bean.put("L_8","4015");
				} else if ("5".equals(bean.get("L_8").toString())) {
					bean.put("L_8","4017");
					bean.put("L_0",bean.get("L_4"));
					bean.put("L_6",bean.get("L_6").toString().replace("-", ""));
					bean.put("L_10",bean.get("L_10").toString().replace("-", ""));
				}
				//元  --〉 分
				amount = new BigDecimal(bean.get("L_6").toString());
				amount = amount.multiply(con_100);
				bean.put("L_6",amount.setScale(0).toString());
				amount = new BigDecimal(bean.get("L_10").toString());
				amount = amount.multiply(con_100);
				bean.put("L_10",amount.setScale(0).toString());
				tmpdate = bean.get("L_9").toString().replace("-", "").replace(":", "").replace(" ", "");
				if (maxtradetime.compareTo(tmpdate) < 0 ) {
					maxtradetime = tmpdate;
				}
				bean.put("STATUS_ID", SettleConstants.COLLATE_STU_2);// 先都制成长款标记
				fileMap.put((String) bean.get("L_0")+(String) bean.get("L_8"), bean);
			}
			
			// 取得我方交易数据
			Map paraMap = new HashMap();
			Map configMap = new HashMap();
			paraMap.put("ROOT", rootInstCd);
			paraMap.put("FUNC_CODE", "'"+TransCodeConst.CHARGE+"','"+TransCodeConst.FROZON+"'");
			paraMap.put("TRADE_DATE", accountDate2 + " 08:00:00");
			paraMap.put("PAY_CHANNEL_ID", "04");
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
				// TODO 修改必要
				if (TransCodeConst.CHARGE.equals(bean.getFuncCode())) {
					com_StrKey = bean.getRequestNo()+"4015";
					com_Str = bean.getRequestNo();
				} else if (TransCodeConst.FROZON.equals(bean.getFuncCode())){
					com_StrKey = bean.getRequestNo()+"4017";
					com_Str = bean.getRequestNo();
				}
				if (fileMap.containsKey(com_StrKey)) {
					subMap = fileMap.get(com_StrKey);
					if ("4015".equals(subMap.get("L_8")) && !TransCodeConst.CHARGE.equals(bean.getFuncCode())) {
						continue;
					}else if ("4017".equals(subMap.get("L_8")) && !TransCodeConst.FROZON.equals(bean.getFuncCode())) {
						continue;
					}
					settleBalanceEntry.setRootInstCd(bean.getMerchantCode());
					settleBalanceEntry.setBatchId((String)subMap.get("L_0"));
					settleBalanceEntry.setTransSeqId(subMap.get("L_8").toString());
					//settleBalanceEntry.setTransType(Integer.parseInt(subMap.get("L_0").toString()));
					settleBalanceEntry.setUserId(bean.getUserId());
					settleBalanceEntry.setOrderNo(com_Str);
					settleBalanceEntry.setTransTime(formatter4.parse(subMap.get("L_2").toString()));
					settleBalanceEntry.setAmount(Long.parseLong(subMap.get("L_6").toString()));
					settleBalanceEntry.setRetriRefNo(bean.getFuncCode());
					settleBalanceEntry.setMerchantCode(bean.getMerchantCode());
					settleBalanceEntry.setSettleTime(formatter4.parse(subMap.get("L_9").toString()));
					settleBalanceEntry.setFee(subMap.get("L_10").toString());

					if (bean.getStatus() == SettleConstants.TAKE_EFFECT) {//成功交易的场合
						if (subMap.get("L_6").toString().equals(bean.getAmount().toString())) {//平账
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
//								RkylinMailUtil.sendMailThread("HT上游对账长款警告","请查询SETTLE_BALANCE_ENTRY的订单号[ORDER_ID]\r\n订单号为["+bean.getOrderNo()+"]\r\n补账失败！", "21401233@qq.com");
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
					settleBalanceEntry.setRootInstCd("lianlian");
					settleBalanceEntry.setBatchId((String)filesubMap.get("L_0"));
					settleBalanceEntry.setTransSeqId(filesubMap.get("L_8").toString());
					//settleBalanceEntry.setTransType(Integer.parseInt(filesubMap.get("L_2").toString()));
					settleBalanceEntry.setUserId((String)filesubMap.get("L_1"));
					settleBalanceEntry.setOrderNo((String)filesubMap.get("L_4"));
					settleBalanceEntry.setTransTime(formatter4.parse(filesubMap.get("L_2").toString()));
					b_amount = new BigDecimal(settlementUtils.nvl(filesubMap.get("L_6"), "0"));
					settleBalanceEntry.setAmount(b_amount.longValue());
					settleBalanceEntry.setRetriRefNo(filesubMap.get("L_8").toString());
					settleBalanceEntry.setMerchantCode("lianlian");
					settleBalanceEntry.setSettleTime(formatter4.parse(filesubMap.get("L_9").toString()));
					b_amount = new BigDecimal(settlementUtils.nvl(filesubMap.get("L_10"), "0"));
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
		logger.info("读取连连对账文件 ————————————END————————————");
		return rtnMap;
	}
	
}
