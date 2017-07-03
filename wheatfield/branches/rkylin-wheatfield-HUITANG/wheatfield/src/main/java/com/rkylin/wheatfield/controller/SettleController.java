package com.rkylin.wheatfield.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.Rop.api.internal.stream.connect.HttpResponse;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.rkylin.common.RedisIdGenerator;
import com.rkylin.database.BaseDao;
import com.rkylin.file.excel.toExcel;
import com.rkylin.file.excel.toExcel2007;
import com.rkylin.utils.RkylinMailUtil;
import com.rkylin.wheatfield.common.DateUtils;
import com.rkylin.wheatfield.common.PartyCodeUtil;
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.constant.SettleConstants;
import com.rkylin.wheatfield.constant.TransCodeConst;
import com.rkylin.wheatfield.exception.AccountException;
import com.rkylin.wheatfield.manager.ParameterInfoManager;
import com.rkylin.wheatfield.manager.SettleBalanceEntryManager;
import com.rkylin.wheatfield.manager.SettleBatchResultManager;
import com.rkylin.wheatfield.manager.SettleSplittingEntryManager;
import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.pojo.ParameterInfo;
import com.rkylin.wheatfield.pojo.ParameterInfoQuery;
import com.rkylin.wheatfield.pojo.SettleBalanceEntry;
import com.rkylin.wheatfield.pojo.SettleBalanceEntryQuery;
import com.rkylin.wheatfield.pojo.SettleBatchResult;
import com.rkylin.wheatfield.pojo.SettleBatchResultQuery;
import com.rkylin.wheatfield.pojo.SettleSplittingEntry;
import com.rkylin.wheatfield.pojo.SettleSplittingEntryQuery;
import com.rkylin.wheatfield.service.AccountManageService;
import com.rkylin.wheatfield.service.GenerationPaymentService;
import com.rkylin.wheatfield.service.SettlementService;
import com.rkylin.wheatfield.service.SettlementServiceThr;
import com.rkylin.wheatfield.service.SettlementWebService;
import com.rkylin.wheatfield.settlement.SettlementLogic;
import com.rkylin.wheatfield.utils.CodeEnum;
import com.rkylin.wheatfield.utils.SettlementUtils;

@Controller
@Scope("prototype")
public class SettleController extends BaseDao {

	private static Logger logger = LoggerFactory
			.getLogger(SettleController.class);
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private SettleBalanceEntryManager settleBalanceEntryManager;
	@Autowired
	private SettleSplittingEntryManager settleSplittingEntryManager;
	@Autowired
	private SettlementService settlementService;
	@Autowired
	private SettlementWebService settlementWebService;
	@Autowired
	private SettlementWebService settlementwebService;
	@Autowired
	RedisIdGenerator redisIdGenerator;
	@Autowired
	GenerationPaymentService generationPaymentService;
	@Autowired
	SettlementLogic settlementLogic;
	@Autowired
	SettlementServiceThr settlementServiceThr;
	@Autowired
	SettleBatchResultManager settleBatchResultManager;
	@Autowired
	ParameterInfoManager parameterInfoManager;
	@Autowired
	Properties userProperties;	@Autowired
	private AccountManageService accountManageService;	/**
	 * 手动退票   20150817  PPP
	 * @param orderNo
	 * @param insCode
	 * @return
	 */
	@RequestMapping("/handleRefund")
	public void handleRefund(String orderNo, String insCode,HttpServletResponse response){
		logger.info("refund==orderNo="+orderNo+" insCode=="+insCode);
		if(orderNo==null||"".equals(orderNo.trim())||insCode==null||"".equals(insCode.trim())){
			logger.info("refund==orderNo="+orderNo+" insCode=="+insCode+"  handleRefund  参数为空 ！！！");
			try {
				response.getWriter().write("{\"success\":\"false\",\"errMsg\":\"参数不能为空\"}");
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//查询交易记录并生成退票记录
		CommonResponse res = generationPaymentService.getTransInforAndRefundRecords(orderNo, insCode);
		if (!CodeEnum.SUCCESS.getCode().equals(res.getCode())) {
			logger.info("refund==orderNo="+orderNo+" insCode=="+insCode+"  handleRefund  执行失败 ！！！");
			try {
				response.getWriter().write("{\"success\":\"false\",\"errMsg\":\"处理失败\"}");
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			response.getWriter().write("{\"success\":\"true\"}");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 对账结果查询
	 */
	@RequestMapping("/getSettle")
	public String getSettle(HttpServletResponse response) {

		try {
			String startDate = (String) request.getParameter("startDate");
			String yinlianId = (String) request.getParameter("yinlianId");
			SettleBalanceEntryQuery settleBalanceEntry = new SettleBalanceEntryQuery();
			if (!StringUtils.isEmpty(startDate)) {
				settleBalanceEntry.setSettleTime(DateUtils.getDate(startDate,
						"yyyyMMdd"));
			}
			if (!StringUtils.isEmpty(yinlianId)) {
				settleBalanceEntry.setRootInstCd(yinlianId);
			}

			List<SettleBalanceEntry> settleList = settleBalanceEntryManager
					.queryList(settleBalanceEntry);
			request.setAttribute("getSettleList", settleList);
			request.setAttribute("startDate", startDate);
			request.setAttribute("yinlianId", yinlianId);
			request.getSession().setAttribute("getSettleListSession",
					settleList);

		} catch (Exception e) {
			// TODO: handle exception
		}

		return "collate/collate_download";
	}
	
	/**
	 * 重新查询代收付表，更新账户系统
	 */
	@RequestMapping("/updateCreditAccount")
	public String updateCreditAccount(HttpServletResponse response) {
		try {
			Map rtnMap = settlementService.updateCreditAccount();
			request.setAttribute("errCode", rtnMap.get("errCode"));
			request.setAttribute("errMsg", rtnMap.get("errMsg"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "account/update_account";
	}
	
	/**
	 * 重新根据分润文件，更新账户系统
	 */
	@RequestMapping("/updateSettleFile")
	    public String updateSettleFile(HttpServletRequest request,
	            @RequestParam("fileUpload") MultipartFile fileUpload)
	            throws Exception {
		try {
			String startDate = request.getParameter("startDate");
			String flg = (String) request.getParameter("flg_OK");
			if ("fenrun".equals(flg)) {
				String fileName = "FN_FR_"+DateUtils.getyyyyMMdd("yyyyMMdd")+"_" + PartyCodeUtil.getRandomCode()+".csv";
		        if (!fileUpload.isEmpty()) {
					if(settlementWebService.fileUpload(fileUpload,fileName)){
						Map rtnMap = settlementWebService.updateSettleFile(startDate,fileName);
						request.setAttribute("errCode", rtnMap.get("errCode").toString());
						request.setAttribute("errMsg", rtnMap.get("errMsg").toString());
						request.setAttribute("startDate",startDate);
					}else{
						request.setAttribute("errCode","9999");
						request.setAttribute("errMsg", "文件上传失败");
						request.setAttribute("startDate",startDate);
					}
		        }else{
					request.setAttribute("errCode","9999");
					request.setAttribute("errMsg", "文件为空");
					request.setAttribute("startDate",startDate);
		        }
			} else if ("duizhang".equals(flg)) {
				String urlkey = settlementService.createDebtAccountFile(startDate);
				if (urlkey==null || urlkey == "") {
					request.setAttribute("errCode","9999");
					request.setAttribute("errMsg", "文件生成失败");
					request.setAttribute("startDate",startDate);
				} else {
					request.setAttribute("errCode","0000");
					request.setAttribute("errMsg", "操作成功");
					request.setAttribute("startDate",startDate);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "account/update_settle";
	}
	
	/**
	 * 代收付结果查询
	 */
	@RequestMapping("/getColAndPay")
	public String getColAndPay(HttpServletResponse response) {
		String startDate = (String) request.getParameter("startDate");
		String statusId = (String) request.getParameter("statusId");
		SettleSplittingEntryQuery settleSplittingEntryQuery = new SettleSplittingEntryQuery();
		if (!StringUtils.isEmpty(startDate)) {
			settleSplittingEntryQuery.setAccountDate(DateUtils.getDate(
					startDate, "yyyyMMdd"));
		}
		if (!StringUtils.isEmpty(statusId)) {
			settleSplittingEntryQuery.setStatusId(Integer.parseInt(statusId));
		}

		List<SettleSplittingEntry> settleList = settleSplittingEntryManager
				.queryList(settleSplittingEntryQuery);
		request.setAttribute("getColAndPayList", settleList);
		request.setAttribute("startDate", startDate);
		request.setAttribute("statusId", statusId);
		request.getSession().setAttribute("getColAndPayListSession",
				settleList);
		return "collate/colAndPay_download";
	}

	/**
	 * 对账结果下载
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/downSettle")
	public String downSettleExcel(HttpServletResponse response) {
		List excelList = new ArrayList();
		List<SettleBalanceEntry> settleList = (List<SettleBalanceEntry>) request
				.getSession().getAttribute("getSettleListSession");
		Map paraMap = new HashMap();
		Map configMap = new HashMap();
		if (settleList != null && settleList.size() > 0) {
			for (SettleBalanceEntry settleBalanceEntry : settleList) {
				paraMap = new HashMap();
				paraMap.put("F_1", settleBalanceEntry.getRootInstCd()==null?null:settleBalanceEntry.getRootInstCd().toString());// 管理机构代码
				paraMap.put("F_2", settleBalanceEntry.getBatchId()==null?null:settleBalanceEntry.getBatchId().toString());// 交易批次号
				paraMap.put("F_3", settleBalanceEntry.getTransSeqId()==null?null:settleBalanceEntry.getTransSeqId().toString());// 交易序号
				paraMap.put("F_4", settleBalanceEntry.getRetriRefNo()==null?null:settleBalanceEntry.getRetriRefNo().toString());// 交易类型
				String statusName = "";
				if(settleBalanceEntry.getStatusId().toString().equals("0")){
					statusName = "错账";
				}else if(settleBalanceEntry.getStatusId().toString().equals("1")){
					statusName = "平账";
				}else if(settleBalanceEntry.getStatusId().toString().equals("2")){
					statusName = "长款";
				}else if(settleBalanceEntry.getStatusId().toString().equals("3")){
					statusName = "短款";
				}else if(settleBalanceEntry.getStatusId().toString().equals("99")){
					statusName = "已分润";
				}else{
					statusName = settleBalanceEntry.getStatusId().toString();
				}
				paraMap.put("F_5", statusName);// 交易状态
				paraMap.put("F_6", settleBalanceEntry.getOrderNo()==null?null:settleBalanceEntry.getOrderNo().toString());// 订单号
				paraMap.put("F_7", settleBalanceEntry.getTransTime()==null?null:DateUtils.getDateFormat("yyyy-MM-dd HH:mm:ss", settleBalanceEntry.getTransTime()).toString());// 交易日期
				paraMap.put("F_8", settleBalanceEntry.getAmount()==null?null:settleBalanceEntry.getAmount().toString());// 交易金额
				paraMap.put("F_9", settleBalanceEntry.getRetriRefNo()==null?null:settleBalanceEntry.getRetriRefNo().toString());// 关联号
				paraMap.put("F_10", settleBalanceEntry.getSettleTime()==null?null:DateUtils.getDateFormat("yyyy-MM-dd", settleBalanceEntry.getSettleTime()).toString());// 清算日期
				paraMap.put("F_11", settleBalanceEntry.getMerchantCode()==null?null:settleBalanceEntry.getMerchantCode().toString());// 商户编码
				paraMap.put("F_12", settleBalanceEntry.getFee()==null?null:settleBalanceEntry.getFee().toString());// 手续费1
				paraMap.put("F_13", settleBalanceEntry.getFee2()==null?null:settleBalanceEntry.getFee2().toString());// 手续费2
				excelList.add(paraMap);
			}

		} else {
			logger.debug("账户过期或生成对账文件数据为空");
			return null;
		}
		String realPath = request.getSession().getServletContext().getRealPath("/");
		String pathStr = "upload/settle/";
		String dateStr = DateUtils.getyyyyMMdd("yyyyMMdd");
//		String path = SettleConstants.FILE_UP_PATH + "20150427"
//				+ File.separator;
		String path = realPath+pathStr+dateStr+ File.separator;
		File filePath = new File(path);
		if (!filePath.exists()) {
			filePath.mkdirs();
		}
		String fileName = "DZ_" + dateStr + ".xlsx";
//		 configMap.put("MODEL_FILE",System.getProperty("user.dir")+
//		 "/src/main/java/com/rkylin/wheatfield/model/settleModel.xlsx");
		 configMap.put("MODEL_FILE",realPath+"upload/model/settleModel.xlsx");
//		 configMap.put("5", "D");
		 configMap.put("7", "D");
		 configMap.put("11", "D");
		 configMap.put("12", "D");

		 configMap.put("FILE", path+fileName);
		 configMap.put("SHEET", "对账结果");
		 configMap.put("firstStyleRow", "1");
		 configMap.put("firstDetailRow", "1");

		// List reportHead = new LinkedList();
		// List reportTail = new LinkedList();
		//
		// Map infoMap = new HashMap();
		// reportHead.clear();
		// infoMap.put("F_1", accountDate);
		// infoMap.put("F_2", countN+"");
		// infoMap.put("F_3", amountS.toString());
		// reportHead.add(infoMap);

		// infoMap = new HashMap();
		// infoMap.put("ROW", "1");
		// infoMap.put("COL", "0");
		// infoMap.put("VALUE", accountDate);
		// reportHead.add(infoMap);
		// infoMap = new HashMap();
		// infoMap.put("ROW", "1");
		// infoMap.put("COL", "1");
		// infoMap.put("VALUE", countN+"");
		// reportHead.add(infoMap);
		// infoMap = new HashMap();
		// infoMap.put("ROW", "1");
		// infoMap.put("COL", "2");
		// infoMap.put("VALUE", amountS.setScale(2).toString());
		// reportHead.add(infoMap);

		// configMap.put("REPORT-HEAD", reportHead);

		// reportTail.clear();
		// infoMap = new HashMap();
		// infoMap.put("ROW", "6");
		// infoMap.put("COL", "3");
		// infoMap.put("VALUE", "制表人：");
		// reportTail.add(infoMap);
		// infoMap = new HashMap();
		// infoMap.put("ROW", "7");
		// infoMap.put("COL", "3");
		// infoMap.put("VALUE", "制表时间：");
		// reportTail.add(infoMap);
		//
		// configMap.put("REPORT-TAIL", reportTail);

		try {
			toExcel2007.WriteDetailSheet(excelList, configMap, null);
		} catch (Exception e) {
			logger.error("生成对账文件操作异常！" + e.getMessage());
		}
		//下载报表
		downExcel(path,fileName,response);
		return null;
	}
	/**
	 * 代收付结果下载
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/downColAndPay")
	public String downDsfExcel(HttpServletResponse response) {
		List excelList = new ArrayList();
		List<SettleSplittingEntry> settleList = (List<SettleSplittingEntry>) request
				.getSession().getAttribute("getColAndPayListSession");
		Map paraMap = new HashMap();
		Map configMap = new HashMap();
		if (settleList != null && settleList.size() > 0) {
			for (SettleSplittingEntry settleSplittingEntry : settleList) {
				paraMap = new HashMap();
				paraMap.put("F_1", settleSplittingEntry.getSettleId()==null?null:settleSplittingEntry.getSettleId().toString());// 管理机构代码
				paraMap.put("F_2", settleSplittingEntry.getRootInstCd()==null?null:settleSplittingEntry.getRootInstCd().toString());// 交易批次号
				paraMap.put("F_3", settleSplittingEntry.getAccountDate()==null?null:DateUtils.getDateFormat("yyyy-MM-dd HH:mm:ss", settleSplittingEntry.getAccountDate()).toString());// 交易序号
				paraMap.put("F_4", settleSplittingEntry.getBatchId()==null?null:settleSplittingEntry.getBatchId().toString());// 交易类型
				paraMap.put("F_5", settleSplittingEntry.getUserId()==null?null:settleSplittingEntry.getUserId().toString());// 交易状态
				paraMap.put("F_6", settleSplittingEntry.getAmount()==null?null:settleSplittingEntry.getAmount().toString());// 订单号
				paraMap.put("F_7", settleSplittingEntry.getSettleType()==null?null:settleSplittingEntry.getSettleType().toString());// 交易日期
				String statusName="";
				if(settleSplittingEntry.getStatusId().toString().equals("1")){
					statusName = "完成";
				}else if(settleSplittingEntry.getStatusId().toString().equals("0")){
					statusName = "失败";
				}else{
					statusName = settleSplittingEntry.getStatusId().toString();
				}
				paraMap.put("F_8", statusName);// 交易金额
				paraMap.put("F_9", settleSplittingEntry.getOrderNo()==null?null:settleSplittingEntry.getOrderNo().toString());// 关联号
				paraMap.put("F_10", settleSplittingEntry.getAccountRelateId()==null?null:settleSplittingEntry.getAccountRelateId().toString());// 清算日期
				paraMap.put("F_11", settleSplittingEntry.getRemark()==null?null:settleSplittingEntry.getRemark().toString());// 商户编码
				excelList.add(paraMap);
			}

		} else {
			logger.debug("session过期或生成对账文件数据为空");
			return null;
		}
		String realPath = request.getSession().getServletContext().getRealPath("/");
		String pathStr = "upload/colAndPay/";
		String dateStr = DateUtils.getyyyyMMdd("yyyyMMdd");
//		String path = SettleConstants.FILE_UP_PATH + "20150427"
//				+ File.separator;
		String path = realPath+pathStr+dateStr+ File.separator;
		File filePath = new File(path);
		if (!filePath.exists()) {
			filePath.mkdirs();
		}
		String fileName = "DSF_" + dateStr + ".xlsx";
//		 configMap.put("MODEL_FILE",System.getProperty("user.dir")+
//		 "/src/main/java/com/rkylin/wheatfield/model/settleModel.xlsx");
		 configMap.put("MODEL_FILE",realPath+"upload/model/colAndPay.xlsx");
		 configMap.put("5", "D");

		 configMap.put("FILE", path+fileName);
		 configMap.put("SHEET", "代收付结果");
		 configMap.put("firstStyleRow", "1");
		 configMap.put("firstDetailRow", "1");

		// List reportHead = new LinkedList();
		// List reportTail = new LinkedList();
		//
		// Map infoMap = new HashMap();
		// reportHead.clear();
		// infoMap.put("F_1", accountDate);
		// infoMap.put("F_2", countN+"");
		// infoMap.put("F_3", amountS.toString());
		// reportHead.add(infoMap);

		// infoMap = new HashMap();
		// infoMap.put("ROW", "1");
		// infoMap.put("COL", "0");
		// infoMap.put("VALUE", accountDate);
		// reportHead.add(infoMap);
		// infoMap = new HashMap();
		// infoMap.put("ROW", "1");
		// infoMap.put("COL", "1");
		// infoMap.put("VALUE", countN+"");
		// reportHead.add(infoMap);
		// infoMap = new HashMap();
		// infoMap.put("ROW", "1");
		// infoMap.put("COL", "2");
		// infoMap.put("VALUE", amountS.setScale(2).toString());
		// reportHead.add(infoMap);

		// configMap.put("REPORT-HEAD", reportHead);

		// reportTail.clear();
		// infoMap = new HashMap();
		// infoMap.put("ROW", "6");
		// infoMap.put("COL", "3");
		// infoMap.put("VALUE", "制表人：");
		// reportTail.add(infoMap);
		// infoMap = new HashMap();
		// infoMap.put("ROW", "7");
		// infoMap.put("COL", "3");
		// infoMap.put("VALUE", "制表时间：");
		// reportTail.add(infoMap);
		//
		// configMap.put("REPORT-TAIL", reportTail);

		try {
			toExcel2007.WriteDetailSheet(excelList, configMap, null);
		} catch (Exception e) {
			logger.error("生成代收付文件操作异常！" + e.getMessage());
		}
		//下载报表
		downExcel(path,fileName,response);
		return null;
	}

	public void downExcel(String path,String fileName, HttpServletResponse response) {

		try {
			File f = new File(path);
			if (f.exists()) {
				OutputStream outputStream = response.getOutputStream();
				FileInputStream inputStream = null;

				// response.setContentType("application/x-download");
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-Disposition",
						"attachment;filename=\""
								+ new String(fileName.toString()
										.getBytes("GBK"), "ISO8859-1"));

				try {
					// 下载文件
					inputStream = new FileInputStream(path+fileName);

					byte[] buffer = new byte[1024];
					int i = -1;

					while ((i = inputStream.read(buffer)) != -1) {
						outputStream.write(buffer, 0, i);

					}

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						outputStream.flush();
						if (outputStream != null) {
							outputStream.close();
						}
						if (inputStream != null) {
							inputStream.close();
						}
						outputStream = null;
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}else{
				logger.info(path+"，文件不存在");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 冲正&抹账
	 */
	@RequestMapping("/correct")
	public String correct(HttpServletResponse response) {
		Map<String,String> rtnMap = new HashMap<String,String>();
		try {
			String orderno = (String) request.getParameter("orderno");
			String rootinstcd = (String) request.getParameter("rootinstcd");
			String flg = (String) request.getParameter("flg_OK");
			if ("chongzheng".equals(flg)) {
				rtnMap = settlementwebService.correct(redisIdGenerator.createRequestNo(), orderno, rootinstcd, TransCodeConst.SETTLEMENT_RT);
			}
			if ("mozhang".equals(flg)) {
				rtnMap = settlementwebService.correct(redisIdGenerator.createRequestNo(), orderno, rootinstcd, null);
			}

			request.setAttribute("orderno", orderno);
			request.setAttribute("rootinstcd", rootinstcd);
			request.setAttribute("errCode", rtnMap.get("errCode"));
			request.setAttribute("errMsg", rtnMap.get("errMsg"));

		} catch (Exception e) {
			// TODO: handle exception
		}

		return "account/correct_settle";
	}
	
	/**
	 * 会唐 代收付结果更新
	 * 代收付文件操作
	 * 1.交易信息汇总 + 代收付文件上传
	 * 2.文件读取 + 代收付结果读入
	 */
	@RequestMapping("/uploadAndDownloadFileHT")
	public String uploadAndDownloadFileHT(String genType, String interfaceName, Integer dateType, HttpServletResponse response) {
		/*   generationType:代收付类型  代收，提现等
		 *   merchantId:商户ID 会堂，丰年等
		 *   orderType:订单类型  代收，代付等
		 *   bussinessCoded：To通联业务代码
		 *   dateType:汇总日期类型  例：0：当日数据汇总 1：t+1日数据汇总 2：t+2日数据汇总 ...
		 */
		Map<String, String> rstMap = null;
		dateType = dateType == null ? 1 : 0;
		String generationType = null;
		Integer orderType = null;
		String bussinessCoded = null;
		String mess = null;
		Integer type = null;
		String batchType = null;
		
		if("uploadFileButt".equals(interfaceName)) {
			if("r".equals(genType)) {
				dateType = 1;
				mess = "代收";
				generationType = TransCodeConst.PAYMENT_COLLECTION;
				orderType = SettleConstants.ORDER_COLLECTION;
				bussinessCoded = SettleConstants.COLLECTION_CODE_OLD;
				batchType = SettleConstants.ROP_RECEIVE_BATCH_CODE;
				type = 6;
				settlementServiceThr.paymentGeneration(generationType, Constants.HT_ID, orderType, bussinessCoded, dateType);
			} else if("p".equals(genType)) {
				mess = "代付";
				generationType = TransCodeConst.PAYMENT_WITHHOLD;
				orderType = SettleConstants.ORDER_WITHHOLD;
				bussinessCoded = SettleConstants.WITHHOLD_BATCH_CODE;
				batchType = SettleConstants.ROP_PAYMENT_BATCH_CODE;
				type = 7;
				settlementServiceThr.paymentGeneration(generationType, Constants.HT_ID, orderType, bussinessCoded, dateType);
				settlementServiceThr.paymentGeneration(generationType, Constants.HT_CLOUD_ID, orderType, bussinessCoded, dateType);
			} else {
				logger.error("您选择的操作不存在 ... ...");
			}
			
			//生成会唐代收文件并上传
			try {

				String accountDate=generationPaymentService.getAccountDate();
				String batch = settlementLogic.getBatchNo(DateUtils.getAccountDate(Constants.DATE_FORMAT_YYYYMMDD, accountDate), batchType, Constants.HT_ID);
				logger.info("-------会堂 "+ mess +" 批次号------" + batch);
				generationPaymentService.uploadPaymentFile(type, batch, Constants.HT_ID, DateUtils.getDate(accountDate, Constants.DATE_FORMAT_YYYYMMDD));

			} catch (AccountException e) {
				logger.error("生成会唐  "+ mess +" 文件失败"+e.getMessage());
				//RkylinMailUtil.sendMailThread("会唐 "+ mess +" 文件", "生成会唐代收文件失败"+e.getMessage(), TransCodeConst.GENERATION_PAY_ERROR_TOEMAIL);
			}
		} else if("downloadFileButt".equals(interfaceName)) {
			if("r".equals(genType)) {
				type = 8;
				mess = "代收";
			} else if("p".equals(genType)) {
				type = 9;
				mess = "代付";
			} else {
				logger.error("您选择的操作不存在 ... ...");
			}
			
			String filetype = type+"";
			String invoicedate = null;
			String rootinstcd = "M000003";
			rstMap = this.readPaymentFileForTask(filetype, invoicedate, rootinstcd, mess,dateType);		} else {
			logger.error("调用接口不存在... ...");
		}
		
		request.setAttribute("genType", genType);
		request.setAttribute("interfaceName", interfaceName);
		request.setAttribute("dateType", dateType);
		
		if(rstMap != null && rstMap.size() > 0) {
			for(String key : rstMap.keySet()) {
				request.setAttribute(key, rstMap.get(key));
			}
		}
		
		return "account/update_account";
	}
	
	/**
	 * 会唐读取对账结果文件
	 * @param filetype
	 * @param invoicedate
	 * @param rootinstcd
	 * @param mess
	 * @return
	 */
	private synchronized Map<String, String> readPaymentFileForTask(String filetype, String invoicedate, String rootinstcd, String mess,int dateType) {
		Map<String, String> rstMap = new HashMap<String, String>();
		String batchType = null;
		logger.info("接受 "+ mess +" 结果计划任务-----------START----------------" + new Date());
		logger.info("输入参数：" + filetype);
		logger.info("输入参数：" + invoicedate);
		logger.info("输入参数：" + rootinstcd);
		if ("8".equals(filetype)) {
			batchType = SettleConstants.ROP_RECEIVE_BATCH_CODE;
		} else if ("9".equals(filetype)) {
			batchType = SettleConstants.ROP_PAYMENT_BATCH_CODE;
		}
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    	ParameterInfoQuery keyList =  new ParameterInfoQuery();
    	keyList.setParameterCode(SettleConstants.ACCOUNTDATE);
    	List<ParameterInfo> parameterInfo = parameterInfoManager.queryList(keyList);
    	SettlementUtils settlementUtils = new SettlementUtils();
    	String accountDate = "";
    	if (invoicedate == null || "".equals(invoicedate)) {
			try {
				if (0 == dateType) {
					accountDate = settlementUtils.getAccountDate(parameterInfo.get(0).getParameterValue(), "yyyy-MM-dd", 0);
				} else {
					accountDate = settlementUtils.getAccountDate(parameterInfo.get(0).getParameterValue(), "yyyy-MM-dd", -1);
				}
			} catch (Exception e2) {
				logger.error("计算账期异常！" + e2.getMessage());
				rstMap.put("errCode", "P0");
				rstMap.put("errMsg", "计算账期异常！" + e2.getMessage());
				return rstMap;
			}
    	} else {
    		accountDate = invoicedate;
    	}
		logger.info("取得的账期为"+ accountDate);
		
		SettleBatchResultQuery settleBatchResultQuery = new SettleBatchResultQuery();
		SettleBatchResult SettleBatch = new SettleBatchResult();
		try {
			settleBatchResultQuery.setAccountDate(formatter.parse(accountDate + " 00:00:00"));
			settleBatchResultQuery.setBatchType(batchType);
			settleBatchResultQuery.setRootInstCd(rootinstcd);
		} catch (Exception e) {
			logger.error("时期转换异常！"+ e.getMessage());
			rstMap.put("errCode", "P1");
			rstMap.put("errMsg", "时期转换异常！"+ e.getMessage());
			e.printStackTrace();
		}
		
		List<SettleBatchResult> settleBatchResultList = settleBatchResultManager.queryList(settleBatchResultQuery);
		
		if (settleBatchResultList.size() == 0) {
			logger.error("账期内["+accountDate+"]没有对应的批次号");
			rstMap.put("errCode", "P2");
			rstMap.put("errMsg", "账期内["+accountDate+"]没有对应的批次号");
			return rstMap;
		}
		
		String msg = "";
		Map<String,String> rtnMap = new HashMap<String,String>();
		for (SettleBatchResult settleBatchResult:settleBatchResultList) {
			SettleBatch = new SettleBatchResult();
			msg=generationPaymentService.paymentFile(accountDate, settleBatchResult.getBatchNo(), filetype, userProperties.getProperty("RKYLIN_PUBLIC_KEY"));
			if("ok".equals(msg)){
				rtnMap = new HashMap<String,String>();
				rtnMap=settlementServiceThr.updateCreditAccountSec();		
				if("0000".equals(rtnMap.get("errCode"))) {
					SettleBatch.setBatchResult("成功");
					//********特殊处理：学生还款给课栈 成功后 ， 发起课栈代付交易给P2P 20150603 start
					Map<String, String> rtnMapP2P = settlementServiceThr.withholdToP2P();
					if (!"P0".equals(rtnMapP2P.get("errCode"))) {
						logger.error("学生还款给课栈 成功后，发起课栈代付交易给P2P错误："+rtnMap.get("errCode")+":"+rtnMap.get("errMsg"));
						rstMap.put("errCode", "P6");
						rstMap.put("errMsg", "学生还款给课栈 成功后，发起课栈代付交易给P2P错误："+rtnMap.get("errCode")+":"+rtnMap.get("errMsg"));
					}	
				} else {
					SettleBatch.setBatchResult("结果读入失败");
					logger.error("代收付更新错误："+rtnMap.get("errCode")+":"+rtnMap.get("errMsg"));
					rstMap.put("errCode", "P3");
					rstMap.put("errMsg", " "+ mess +" 更新错误："+rtnMap.get("errCode")+":"+rtnMap.get("errMsg"));
				}
				// 调用晶晶的存储过程把成功的代收付过渡至历史表
	            Map<String, String> param = new HashMap<String, String>();
		    	super.getSqlSession().selectList("MyBatisMap.setgeneration", param);
	            if(null==param||!String.valueOf(param.get("on_err_code")).equals("0")){
	    			logger.error("维护代付表历史数据失败！");
	    			rstMap.put("errCode", "P5");
					rstMap.put("errMsg", "维护代付表历史数据失败！");
	            }
			} else {
				SettleBatch.setBatchResult("文件下载失败");
				logger.error(" "+ mess +" 结果读入错误！");
				rstMap.put("errCode", "P4");
				rstMap.put("errMsg", ""+ mess +"结果读入错误！");
			}
			SettleBatch.setResultId(settleBatchResult.getResultId());
			settleBatchResultManager.saveSettleBatchResult(SettleBatch);
		}
		
		logger.info("接受代收付结果计划任务-----------END----------------"+new Date());
		return rstMap;
	}
	
	/**
	 * 会唐 上游对账
	 * @param invoicedate
	 * @return
	 */
	@RequestMapping("/readCollateFileHT")
	public String readCollateFileHT(String invoicedate) {
		Map<String,String> rtnMap = null;
		request.setAttribute("invoicedate", invoicedate);
		
		if(invoicedate != null && !invoicedate.trim().equals("")) {
			try {
				rtnMap = settlementService.readCollateFileHT(invoicedate,"'"+Constants.HT_ID+"','"+Constants.KZ_ID+"'","26","","B1");
			} catch (Exception e) {
				rtnMap = new HashMap<String, String>();
				rtnMap.put("errCode", "P1");
				rtnMap.put("errMsg", "对账文件生成失败，请联系相关负责人");
			}
		} else {
			logger.error("账期不能为空, 请输入账期!");
			request.setAttribute("errCode", "P0");
			request.setAttribute("errMsg", "账期不能为空, 请输入账期!");
			return "account/collate_tlht_c";
		}
		
		if(rtnMap != null && rtnMap.size() > 0) {
			request.setAttribute("errCode", rtnMap.get("errCode"));
			request.setAttribute("errMsg", rtnMap.get("errMsg"));
		} else {
			request.setAttribute("result", "success");
		}
		
		return "account/collate_tlht_c";
	}
	
	/**
	 * 会唐 发送下游对账文件
	 * @param invoicedate
	 * @return
	 */
	@RequestMapping("/createDebtAccountFileHT")
	public String createDebtAccountFileHT(String invoicedate) {
		request.setAttribute("invoicedate", invoicedate);
		String filename = null;
		
		try {
			filename = settlementService.createDebtAccountFileHT(invoicedate,Constants.HT_ID,"27");
			request.setAttribute("errCode", "上传文件: ");
			request.setAttribute("errMsg", filename);
		} catch (Exception e) {
			request.setAttribute("errCode", "P3");
			request.setAttribute("errMsg", e.getMessage());
		}
		
		return "account/collate_tlht_c";
	}
	
	/**
	 * 发起1分钱代付发起&读入
	 */
	@RequestMapping("/settementall")
	public String settementall(HttpServletResponse response) {
		Map<String,String> rtnMap = new HashMap<String,String>();
		try {
			String orderno = (String) request.getParameter("orderno");
			String rootinstcd = (String) request.getParameter("rootinstcd");
			String flg = (String) request.getParameter("flg_OK");
			if ("faqi1fen".equals(flg)) {
				 accountManageService.onecentService();
			}
			if ("duru1fen".equals(flg)) {
				accountManageService.updateAccountoneCent();
			}

			request.setAttribute("errCode", "0000");
			request.setAttribute("errMsg", "由于方法没有返回值，只能说反正已经调用了，看看数据库去吧。。");

		} catch (Exception e) {
			// TODO: handle exception
		}

		return "account/correct_settle";
	}
}
