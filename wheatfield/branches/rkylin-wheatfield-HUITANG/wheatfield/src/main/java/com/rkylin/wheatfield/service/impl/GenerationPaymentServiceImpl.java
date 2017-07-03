package com.rkylin.wheatfield.service.impl;

import com.Rop.api.domain.FileUrl;
import com.rkylin.crps.pojo.BaseResponse;
import com.rkylin.crps.pojo.OrderDetail;
import com.rkylin.crps.pojo.OrderDetails;
import com.rkylin.crps.pojo.ResultCode;
import com.rkylin.crps.service.CrpsApiService;
import com.rkylin.database.BaseDao;
import com.rkylin.file.txt.TxtReader;
import com.rkylin.file.txt.TxtWriter;
import com.rkylin.utils.RkylinMailUtil;
import com.rkylin.wheatfield.api.AccountService;
import com.rkylin.wheatfield.common.DateUtils;
import com.rkylin.wheatfield.common.PartyCodeUtil;
import com.rkylin.wheatfield.common.UploadAndDownLoadUtils;
import com.rkylin.wheatfield.common.ValHasNoParam;
import com.rkylin.wheatfield.constant.BaseConstants;
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.constant.SettleConstants;
import com.rkylin.wheatfield.constant.TransCodeConst;
import com.rkylin.wheatfield.exception.AccountException;
import com.rkylin.wheatfield.manager.AccountInfoManager;
import com.rkylin.wheatfield.manager.CorporatAccountInfoManager;
import com.rkylin.wheatfield.manager.GenerationPaymentManager;
import com.rkylin.wheatfield.manager.ParameterInfoManager;
import com.rkylin.wheatfield.manager.SettleBatchResultManager;
import com.rkylin.wheatfield.manager.TransDaysSummaryManager;
import com.rkylin.wheatfield.manager.TransOrderInfoManager;
import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.pojo.AccountInfo;
import com.rkylin.wheatfield.pojo.AccountInfoQuery;
import com.rkylin.wheatfield.pojo.CorporatAccountInfo;
import com.rkylin.wheatfield.pojo.CorporatAccountInfoQuery;
import com.rkylin.wheatfield.pojo.GenerationPayment;
import com.rkylin.wheatfield.pojo.GenerationPaymentQuery;
import com.rkylin.wheatfield.pojo.ParameterInfo;
import com.rkylin.wheatfield.pojo.ParameterInfoQuery;
import com.rkylin.wheatfield.pojo.SettleBatchResult;
import com.rkylin.wheatfield.pojo.TransDaysSummary;
import com.rkylin.wheatfield.pojo.TransDaysSummaryQuery;
import com.rkylin.wheatfield.pojo.TransOrderInfo;
import com.rkylin.wheatfield.pojo.TransOrderInfoQuery;
import com.rkylin.wheatfield.response.ErrorResponse;
import com.rkylin.wheatfield.response.PaymengManagerResponse;
import com.rkylin.wheatfield.response.Response;
import com.rkylin.wheatfield.service.GenerationPaymentService;
import com.rkylin.wheatfield.service.IAPIService;
import com.rkylin.wheatfield.service.IErrorResponseService;
import com.rkylin.wheatfield.service.PaymentAccountService;
import com.rkylin.wheatfield.service.SettlementService;
import com.rkylin.wheatfield.service.SettlementServiceThr;
import com.rkylin.wheatfield.settlement.SettlementLogic;
import com.rkylin.wheatfield.utils.CodeEnum;
import com.rkylin.wheatfield.utils.DateUtil;
import com.rkylin.wheatfield.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Transactional

@Service("generationPaymentService")
public class GenerationPaymentServiceImpl extends BaseDao implements GenerationPaymentService,
AccountService,
		IAPIService{
	private static Logger logger = LoggerFactory.getLogger(GenerationPaymentServiceImpl.class);
	@Autowired
	GenerationPaymentManager generationPaymentManager;
	@Autowired
	IErrorResponseService errorResponseService;
	@Autowired
	HttpServletRequest request;
	@Autowired
	ParameterInfoManager parameterInfoManager;
	@Autowired
	SettleBatchResultManager settleBatchResultManager;
	@Autowired
	SettlementService settlementService;
	@Autowired
	Properties userProperties;
	@Autowired
	SettlementServiceThr settlementServiceThr;
	@Autowired
	private TransDaysSummaryManager transDaysSummaryManager;
	@Autowired
	private TransOrderInfoManager transOrderInfoManager;
	@Autowired
	private PaymentAccountService paymentAccountService;
	@Autowired
	SettlementLogic settlementLogic;
	private CrpsApiService crpsApiService;
	@Autowired
	private AccountInfoManager accountInfoManager;
	
	@Autowired
	private CorporatAccountInfoManager corporatAccountInfoManager;
	public void setCrpsApiService(CrpsApiService crpsApiService) {
		this.crpsApiService = crpsApiService;
	}

	@Override
	public Response doJob(Map<String, String[]> paramMap, String methodName) {
		PaymengManagerResponse response=new PaymengManagerResponse();
//		new Thread(){
//			public void run() {
//				thiszRun();
//			}
//		}.start();
		if(!ValHasNoParam.hasParam(paramMap, "type")){
			return errorResponseService.getErrorResponse("P1","类型不能为空！");
		}
		if(!ValHasNoParam.hasParam(paramMap, "invoicedate")){
			return errorResponseService.getErrorResponse("P2","账期不能为空！");
		}
		//1、代收付，2、分润，3、授信。4、债券包 5,对账结果
		String type="";
		String invoicedate="";
		String batch="";
		String filetype="";
		for(Object keyObj : paramMap.keySet().toArray()){
			String[] strs = paramMap.get(keyObj);
			for(String value : strs){
				if("type".equals(keyObj)){
					type=value;
				}else if("invoicedate".equals(keyObj)){
					invoicedate=value;
				}else if("batch".equals(keyObj)){
					batch=value;
				}else if("filetype".equals(keyObj)){
					filetype=value;
				}
			}
		}
		if("1".equals(type)){
			if(!ValHasNoParam.hasParam(paramMap, "batch")){
				return errorResponseService.getErrorResponse("P3","批次号不能为空！");
			}
			if(!ValHasNoParam.hasParam(paramMap, "filetype")){
				return errorResponseService.getErrorResponse("P4","文件类型不能为空！");
			}
			Map<String,String> rtnMap = this.readPaymentFile(filetype,invoicedate,batch);
			if("0000".equals(rtnMap.get("errCode"))){
				response.setIs_success(true);
			}else{
				return errorResponseService.getErrorResponse(rtnMap.get("errCode"), rtnMap.get("errMsg"));
			}
		}else if("2".equals(type)){
			Map<String,String> rtnMap=settlementService.updateSettleFile(invoicedate,batch);
			if("0000".equals(rtnMap.get("errCode"))){
				response.setIs_success(true);
			}else{
				return errorResponseService.getErrorResponse(rtnMap.get("errCode"), rtnMap.get("errMsg"));
//				RkylinMailUtil.sendMail("错误信息", "债券包错误！", "1663991989@qq.com");
			}
		}else if("3".equals(type)){
			List list=settlementService.getCreditInfo(invoicedate);
			if(list!=null && list.size()>0){
				response.setIs_success(true);
			}else{
				//邮件发送
			}
		}else if("4".equals(type)){
//			String urlKey=settlementService.createP2PDebtFile();
//			if(urlKey!=null && !"".equals(urlKey)){
//			}
			ErrorResponse errorResponse=settlementService.readP2PDebtFile(invoicedate);
			if(errorResponse==null){
				response.setIs_success(true);
			}else{
//				RkylinMailUtil.sendMail("错误信息", "债券包错误！", "1663991989@qq.com");
			}
		}else if("5".equals(type)){
//			String urlKey=settlementService.createP2PDebtFile();
//			if(urlKey!=null && !"".equals(urlKey)){
//			}
			Map<String,String> rtnMap=settlementService.readCollateResFile(invoicedate,batch);
			if("0000".equals(rtnMap.get("errCode"))){
				response.setIs_success(true);
			}else{
				return errorResponseService.getErrorResponse(rtnMap.get("errCode"), rtnMap.get("errMsg"));
//				RkylinMailUtil.sendMail("错误信息", "债券包错误！", "1663991989@qq.com");
			}
		}
//		MD5Utils.string2MD5(operatetime+"|"+type+"|"+"");
	
		return response;
	}
	/**
	 * 代收付表数据入库
	 * 成功返回ok ,失败返回失败原因
	 */
	@Override
	public String payMentResultTransform(GenerationPayment generationPayment){
		if(generationPayment.getAccountDate()==null || "".equals(DateUtils.getDateFormat(Constants.DATE_FORMAT_YYYYMMDD, generationPayment.getAccountDate()))){
			return "账期不能为空！";
		}
		try{
			generationPaymentManager.saveGenerationPayment(generationPayment);
			return "ok";
		}catch(Exception e){
			logger.error(e.getMessage());
			return "入库失败:"+e.getMessage();
		}
	}
	
	/**
	 * 文件下载
	 */
	@Override
	public String accountNotify(String urlKey,String suffix,int type,String priOrPubKey) {
		String resultUrl="";
		try{
			resultUrl=UploadAndDownLoadUtils.downloadFile("download", "json", urlKey, suffix,request,type,priOrPubKey,2,userProperties.getProperty("FSAPP_KEY"),userProperties.getProperty("FSDAPP_SECRET"),userProperties.getProperty("FSROP_URL"));
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		return resultUrl;
	}

	/**
	 * 解析代收付结果文件，将结果入库
	 * @param filePath 本地文件路径
	 */
	public String getGenerationPaymentList(String filePath){
		logger.info("--------解析代收付结果文件，将结果入库---------");
//		List<GenerationPayment> generationPaymentList=new ArrayList<GenerationPayment>();
		TxtReader txtReader = new TxtReader();
		List<Map> fileList = new ArrayList<Map>();
		String msg="ok";
		try {
			txtReader.setEncode("UTF-8");
			File file=new File(filePath);
			fileList = txtReader.txtreader(file , SettleConstants.DEDT_SPLIT2);
		}catch(Exception e){
			logger.error(e.getMessage());
			msg="读取文件失败！";
		}
		try{
			if(fileList!=null && fileList.size()>1){
				List<GenerationPayment> generationList=new ArrayList<GenerationPayment>();
				for (int i = 1; i < fileList.size(); i++) {
					GenerationPayment generationPayment=new GenerationPayment();
					Map<String,String> map=fileList.get(i);
	//				 for (Object key : map.keySet()) {
	//					   System.out.println("key= "+ key + " and value= " + map.get(key));
					    generationPayment.setRequestNo(map.get("L_0"));
						generationPayment.setBussinessCode(map.get("L_1"));
						generationPayment.setGeneId(Long.parseLong(map.get("L_2")));
						generationPayment.setGeneSeq(map.get("L_3"));
						generationPayment.setUserId(map.get("L_4"));
						generationPayment.setRootInstCd(map.get("L_5"));
						generationPayment.setBankCode(map.get("L_6"));
						generationPayment.setAccountType(map.get("L_7"));
						generationPayment.setAccountNo(map.get("L_8"));
						generationPayment.setAccountName(map.get("L_9"));
						generationPayment.setProvince(map.get("L_10"));
						generationPayment.setCity(map.get("L_11"));
						generationPayment.setOpenBankName(map.get("L_12"));
						generationPayment.setAccountProperty(map.get("L_13"));
						generationPayment.setAmount(Long.parseLong(map.get("L_14")));
						generationPayment.setCurrency(map.get("L_15"));
						generationPayment.setCertificateType(map.get("L_16"));
						generationPayment.setCertificateNumber(map.get("L_17"));
						generationPayment.setPayBankCode(map.get("L_18"));
						generationPayment.setProcessResult(map.get("L_19"));
						generationPayment.setErrorCode(map.get("L_20"));
						if(map.get("L_20")==null || "".equals(map.get("L_20"))){
							//成功
							generationPayment.setSendType(SettleConstants.SEND_NORMAL);
							//generationPayment.setStatusId(Constants.GENERATION_PAYMENT_STATUS_1);
						}else{
							generationPayment.setSendType(SettleConstants.SEND_DEFEAT);
						}
	//					generationPayment.setAccountDate(DateUtils.getSysDate(Constants.DATE_FORMAT_YYYYMMDD));
	//				  }
						generationList.add(generationPayment);
	//					generationPaymentManager.saveGenerationPayment(generationPayment);
				}
				generationPaymentManager.batchUpdate(generationList);
			}
		} catch(Exception e) {
			msg="插入数据库失败！";
			logger.error(msg+e.getMessage());
		}
		return msg;
	}
	
	/**
	 * 退票|解析代收付结果，将结果入库（代收付系统调用，此方法两个业务流程，外部两次调用这同一个方法）
	 * @param baseResponse
	 * @return
	 */
	public String refund(BaseResponse baseResponse){
		logger.info("refund====baseResponse=="+baseResponse);
		if (baseResponse==null) {
			return CodeEnum.FAILURE.getCode();
		}
		//退票
		if (baseResponse instanceof OrderDetail) {
			OrderDetail orderDetail = (com.rkylin.crps.pojo.OrderDetail) baseResponse;
			logger.info("-------代收付调用账户退票  开始<<<<<<<<<<userId="+orderDetail.getUserId()+" 代收付返回的信息=orderDetail.getErrMsg()="+orderDetail.getErrMsg());
			CommonResponse res = null;
			if (orderDetail.getStatusId()==null || orderDetail.getStatusId()!=16) {
				logger.info("-------代收付调用账户退票   结束<<<<<<<<<<<<<<<<<<代收付系统传入的参数异常！<<<<<<<<<<<<<<<<<userId="+orderDetail.getUserId()+",code="+CodeEnum.FAILURE.getCode());
				return CodeEnum.FAILURE.getCode();
			}
			try {
				res = refund(orderDetail.getOrderNo());
				//原退票流程结束后，调用此方法进行卡信息状态修改
				this.updateStatus(orderDetail);
			} catch (Exception e) { 
				logger.info("-------代收付调用账户退票   结束<<<<<<<<<<<<<<<<<<调用异常！<<<<<<<<<<<<<<<<<<<<<<<<<userId="+orderDetail.getUserId()+",code="+CodeEnum.FAILURE.getCode());
				RkylinMailUtil.sendMailThread("退票处理失败","**代收付系统传入的 orderNo=="+orderDetail.getOrderNo()+",orderDetail="+orderDetail.getRootInstCd()+",userID="+orderDetail.getUserId()+"  "+"退票处理", TransCodeConst.FINANACE_ENTRY_ERROR_TOEMAIL);
				return CodeEnum.FAILURE.getCode();
			}
			if (!CodeEnum.SUCCESS.getCode().equals(res.getCode())) {
				logger.info("-------代收付调用账户退票   结束<<<<<<<<<<<<<<<<<<退票处理失败！<<<<<<<<<<<<<<<<<<<<<<<<<<<userId="+orderDetail.getUserId()+",code="+CodeEnum.FAILURE.getCode());
				RkylinMailUtil.sendMailThread("退票处理失败","**代收付系统传入的 orderNo=="+orderDetail.getOrderNo()+",orderDetail="+orderDetail.getRootInstCd()+",userID="+orderDetail.getUserId()+"  "+"退票处理", TransCodeConst.FINANACE_ENTRY_ERROR_TOEMAIL);
				return CodeEnum.FAILURE.getCode();
			}
			logger.info("-------代收付调用账户退票   结束<<<<<<<<<<<<<<<<成功<<<<<<<<<<<<<<<<<<<userId="+orderDetail.getUserId()+",code="+CodeEnum.SUCCESS.getCode());
		}else{
			//解析代付结果，将结果入库
			OrderDetails orderDetails = (com.rkylin.crps.pojo.OrderDetails) baseResponse;
			List<OrderDetail> orderDetailList = orderDetails.getOrderDetails();
			logger.info("-------代收付调用账户解析代付结果   开始<<<<<<<<<<userId="+orderDetailList.get(0).getUserId());
			if (orderDetailList==null || orderDetailList.size()==0) {
				logger.info("-------代收付调用账户解析代付结果  结束<<<<<<<<<<<<<<参数异常<<<<<<<<<<<<<<<<<<userId="+orderDetailList.get(0).getUserId()+",code="+CodeEnum.FAILURE.getCode());
				return CodeEnum.FAILURE.getCode();
			}
			CommonResponse res = null;
			try {
				res = getGenerationPaymentList(orderDetailList);
			} catch (Exception e) {
				logger.info("-------代收付调用账户解析代付结果  结束<<<<<<<<<<<<<<调用异常<<<<<<<<<<<<<<userId="+orderDetailList.get(0).getUserId()+",code="+CodeEnum.FAILURE.getCode());
				return CodeEnum.FAILURE.getCode();
			}
			if (!CodeEnum.SUCCESS.getCode().equals(res.getCode())) {
				logger.info("-------代收付调用账户解析代付结果  结束<<<<<<<<<<<<<<处理失败<<<<<<<<<<<<<<<userId="+orderDetailList.get(0).getUserId()+",code="+CodeEnum.FAILURE.getCode());
				return CodeEnum.FAILURE.getCode();
			}
			logger.info("-------代收付调用账户解析代付结果   结束<<<<<<<<<<<<<<<<<成功<<<<<<<<<<<<userId="+orderDetailList.get(0).getUserId()+",code="+CodeEnum.SUCCESS.getCode());
		}
		return CodeEnum.SUCCESS.getCode();
	}
		/**
	 * @Description : TODO(退票业务执行后，增加修改 卡信息表的状态)
	 * @CreateTime : 2015年10月9日 下午3:05:08
	 * @Creator : liuhuan
	 */
	public void updateStatus(OrderDetail orderDetail){
		//当金额等于1，remark等于qjs_tuikuan时表示的是一分钱代付
		if(orderDetail != null && orderDetail.getAmount() != null && 1 == orderDetail.getAmount() && "qjs_tuikuan".equals(orderDetail.getRemark())){
			logger.info("--------退票业务执行后更新状态方法开始-------");
			
			//机构码转换
			String rootInstCd = orderDetail.getRootInstCd();
			logger.info("--------转换前机构码：-------" + rootInstCd);
			TransDaysSummaryQuery tdsq = new TransDaysSummaryQuery();
			tdsq.setTransSumId(orderDetail.getOrderNo());
			List<TransDaysSummary> tdslist = transDaysSummaryManager.queryList(tdsq);
			if(null != tdslist && !tdslist.isEmpty()){
				rootInstCd = tdslist.get(0).getRootInstCd();
				logger.info("--------查找结果不为空，转化后机构码：-------" + rootInstCd);
			}
			
			//新表查找值
			CorporatAccountInfoQuery corporatAccountInfoQuery = new CorporatAccountInfoQuery();
			corporatAccountInfoQuery.setRootInstCd(rootInstCd);
			corporatAccountInfoQuery.setAccountNumber(orderDetail.getAccountNo());
			corporatAccountInfoQuery.setStatusId(1);
			List<CorporatAccountInfo> corporatAccountInfoList = corporatAccountInfoManager.queryList(corporatAccountInfoQuery);
            //accountinfo表查找值
			AccountInfoQuery accountInfoQuery = new AccountInfoQuery();
			accountInfoQuery.setRootInstCd(rootInstCd);
			accountInfoQuery.setAccountNumber(orderDetail.getAccountNo());
			accountInfoQuery.setStatus(1);
			List<AccountInfo> accountInfoList = accountInfoManager.queryListByNumAndConstId(accountInfoQuery);
			if(corporatAccountInfoList == null || corporatAccountInfoList.isEmpty()){
				//如果新表为空，则判断accountinfo是否为空，不为空则更新accountinfo状态值为4
				if(accountInfoList != null && !accountInfoList.isEmpty()){
					logger.info("-------新表为空，accountinfo不为空,更新accountinfo状态值为4--------");
					AccountInfo accountInfo = accountInfoList.get(0);
					accountInfo.setStatus(4);
					int updateResult = accountInfoManager.updateByPrimaryKey(accountInfo);
					logger.info("-------修改accountInfo表记录条数：" + updateResult);
				}
			}else{
				//如果新表不为空，accountinfo为空，则将新表更改状态值为4
				if(accountInfoList == null || accountInfoList.isEmpty()){
					logger.info("-------新表不为空，accountinfo为空,将新表更改状态值为4--------");
					CorporatAccountInfo corporatAccountInfo = corporatAccountInfoList.get(0);
					corporatAccountInfo.setStatusId(4);
					int updateCResult = corporatAccountInfoManager.updateCorporatAccountInfo(corporatAccountInfo);
					logger.info("-------修改CorporatAccountInfo表记录条数：" + updateCResult);
				}else{
					//新表不为空，accountinfo也不为空，则修改accountinfo表状态值为4，新表状态值为0失效
					logger.info("-------新表不为空，accountinfo也不为空，则修改accountinfo表状态值为4，新表状态值为0失效--------");
					AccountInfo accountInfo = accountInfoList.get(0);
					accountInfo.setStatus(4);
					int updateResult = accountInfoManager.updateByPrimaryKey(accountInfo);
					logger.info("-------修改AccountInfo表记录条数：" + updateResult);
					
					CorporatAccountInfo corporatAccountInfo = corporatAccountInfoList.get(0);
					corporatAccountInfo.setStatusId(0);
					int updateCResult = corporatAccountInfoManager.updateCorporatAccountInfo(corporatAccountInfo);
					logger.info("-------修改CorporatAccountInfo表记录条数：" + updateCResult);
				}
			}
			logger.info("--------退票业务执行后更新状态方法结束-------");
		}
	}
	
	/**
	 * 退票
	 * @param orderNo  订单号
	 * @return
	 */
	public CommonResponse refund(String orderNo){
		logger.info("退票   refund 代收付系统传入的 orderNo=="+orderNo);
		CommonResponse res = new CommonResponse();
		if (orderNo==null || "".equals(orderNo)) {
			res.setCode(CodeEnum.ERR_PARAM_NULL.getCode());
			return res;
		}
		TransDaysSummary transDaysSummary = transDaysSummaryManager.findTransDaysSummaryById(orderNo);
		logger.info("退票   refund --transDaysSummary=="+transDaysSummary);
		if (transDaysSummary==null) {
			res.setCode(CodeEnum.ERR_TRADE_RECORD_NO_RESULT.getCode());
			return res;
		}
		//只有代付，提现可以走退票
		if(!TransCodeConst.PAYMENT_WITHHOLD.equals(transDaysSummary.getOrderType()) && !TransCodeConst.WITHDROW.equals(transDaysSummary.getOrderType())){
			res.setCode(CodeEnum.ERR_TRADE_RECORD_NO_RESULT.getCode());
			return res;
		}
		String summaryOrders = transDaysSummary.getSummaryOrders();
		logger.info("退票 代收付系统传入的 orderNo=="+orderNo+"--summaryOrders=="+summaryOrders);
		String instCode = transDaysSummary.getRootInstCd();
		//查询交易记录并生成退票记录
		res = getTransInforAndRefundRecords(summaryOrders, instCode);
		return res;
	}
	
	//修改原订单状态，
	@Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
	public CommonResponse getTransInforAndRefundRecords(String orderNo, String instCode) {
		TransOrderInfoQuery query = new TransOrderInfoQuery();
		query.setOrderNo(orderNo);
		query.setMerchantCode(instCode);
		logger.info("getTransInforAndRefundRecords==orderNo="+orderNo+" instCode=="+instCode);
		List<TransOrderInfo> transOrderInfoList = transOrderInfoManager.selectTransOrderInfosRefund(query);
		logger.info("getTransInforAndRefundRecords==orderNo="+orderNo+" instCode=="+instCode+"  transOrderInfoList="+transOrderInfoList);
		CommonResponse res = new CommonResponse();
		if (transOrderInfoList.size()==0) {
			logger.info("getTransInforAndRefundRecords==orderNo="+orderNo+" instCode=="+instCode+" 订单数据异常");
			res.setCode(CodeEnum.ERR_TRADE_RECORD_NO_RESULT.getCode());
			return res;
		} 
		TransOrderInfo transOrderInfo = transOrderInfoList.get(0);
		if (TransCodeConst.TRANS_STATUS_PAY_SUCCEED!=transOrderInfo.getStatus()) {
			logger.info("getTransInforAndRefundRecords==orderNo="+orderNo+" instCode=="+instCode+" 退票失败 status="+transOrderInfo.getStatus());
			res.setCode(CodeEnum.FAILURE.getCode());
			return res;
		}
		transOrderInfo.setStatus(TransCodeConst.TRANS_STATUS_REFUND);
		transOrderInfo.setCreatedTime(null);
		transOrderInfo.setUpdatedTime(null);
		transOrderInfoManager.saveTransOrderInfo(transOrderInfo);
//		Long.parseLong("shdh");
		// 生成新的转账，充值等记录流水
		String code = paymentAccountService.makeRefundRecords(transOrderInfo);
		logger.info("getTransInforAndRefundRecords==orderNo="+orderNo+" instCode=="+instCode+"  code="+code);
		//账户状态非正常
		if(CodeEnum.ERR_ACCOUNT_NOT_NORMAL.getCode().equals(code)){
			res.setCode(CodeEnum.ERR_ACCOUNT_NOT_NORMAL.getCode());
			return res;
		}
		//交易数据异常
		if(CodeEnum.ERR_TRADE_RECORD_NO_RESULT.getCode().equals(code) || CodeEnum.ERR_TRADE_RECORD_NOT_IN.getCode().equals(code)){
			res.setCode(CodeEnum.ERR_TRADE_RECORD_NO_RESULT.getCode());
			return res;
		}
		return res;
	}
	
	/**
	 * 解析代收付结果，将结果入库
	 *
	 */
	private CommonResponse getGenerationPaymentList(List<OrderDetail> orderDetailList){
		CommonResponse res = new CommonResponse();
		if(orderDetailList == null || orderDetailList.isEmpty()){
			res.setCode(CodeEnum.ERR_PARAM_NULL.getCode());
			return res;
		}
		logger.info("--------处理代收付返回结果(或账户自己调用)数据开始------orderDetailList.size()=="+orderDetailList.size());
		//代收付处理成功的
		List<GenerationPayment> generationList = new ArrayList<GenerationPayment>();
		//代收付处理失败的
		List<GenerationPayment> generationFailList = new ArrayList<GenerationPayment>();
		//代收付传回的所有的订单编号（用于查询数据进行校验）
		String[]  orderNoArray = new String[orderDetailList.size()];
		for (int m=0;m<orderDetailList.size();m++) {
			OrderDetail orderDetail = orderDetailList.get(m);
			GenerationPayment generationPayment = new GenerationPayment();
			if(orderDetail.getStatusId()==15){
				generationPayment.setOrderNo(orderDetail.getOrderNo());
				generationPayment.setSendType(SettleConstants.SEND_NORMAL);
				generationList.add(generationPayment);
			}else if(orderDetail.getStatusId()==13){
				generationPayment.setOrderNo(orderDetail.getOrderNo());
				generationPayment.setSendType(SettleConstants.SEND_DEFEAT);
				generationPayment.setErrorCode(orderDetail.getRetCode()+":"+orderDetail.getErrMsg());
				generationFailList.add(generationPayment);
			}
			orderNoArray[m]=orderDetail.getOrderNo();
		}
		logger.info("--------getGenerationPaymentList---代收付(或账户自己调用)发送的所有---orderNoArray.length=="+orderNoArray.length);
//		GenerationPaymentQuery query = new GenerationPaymentQuery();
//		query.setOrderNo(StringUtils.arrayToStr(orderNoArray));
		List<GenerationPayment> genList =  generationPaymentManager.selectByOrderNo(orderNoArray);
		logger.info("--------getGenerationPaymentList---根据代收付系统(或账户自己调用)的参数从数据库查出的代付数据---genList.size()=="+genList.size());
		//如果数据库查出的数据少于代收付系统传来的数据，不做处理直接返回
		if (genList.size()<orderDetailList.size()) {
			logger.info("--------getGenerationPaymentList---根据代收付系统(或账户自己调用)的参数从数据库查出的代付数据数量不一致---");
			res.setCode(CodeEnum.ERR_TRADE_RECORD_NO_RESULT.getCode());
			return res;
		}
		logger.info("--------getGenerationPaymentList---代收付(或账户自己调用)返回正常结果个数---generationList.size()=="+generationList.size());
		if (generationList.size()!=0) {
			generationPaymentManager.batchUpdateByOrderNoRootInstCd(generationList);
		}
		logger.info("--------getGenerationPaymentList---代收付(或账户自己调用)返回失败结果个数---generationFailList.size()=="+generationFailList.size());
		if(generationFailList.size()!=0){
			generationPaymentManager.batchUpdateByOrderNoRootInstCd(generationFailList);
		}
		//把代收付结果更新
		Map<String, String> rtnMapUp = null;
		try {
			rtnMapUp = settlementServiceThr.updateCreditAccountSec();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("代收付更新错误....");
			res.setCode(CodeEnum.ERR_DATABASE_CALL_ERROR.getCode());
		return res;
		}
		if (!"0000".equals(rtnMapUp.get("errCode"))) {
			RkylinMailUtil.sendMail("代收付通知接口","代收付更新错误：" + rtnMapUp.get("errCode") + ":"+ rtnMapUp.get("errMsg"), "21401233@qq.com");
			logger.error("代收付更新错误：" + rtnMapUp.get("errCode") + ":"+ rtnMapUp.get("errMsg"));			res.setCode(CodeEnum.ERR_DATABASE_CALL_ERROR.getCode());
			res.setCode(CodeEnum.ERR_DATABASE_CALL_ERROR.getCode());
			return res;
		
		}
		// ********特殊处理：学生还款给课栈 成功后 ， 发起课栈代付交易给P2P 20150603 start
		Map<String, String> rtnMapP2P = null;
		try {
			rtnMapP2P = settlementServiceThr.withholdToP2P();
		} catch (Exception e) {
			logger.error("--------getGenerationPaymentList---settlementServiceThr.withholdToP2P()---系统错误！");
			res.setCode(CodeEnum.ERR_DATABASE_CALL_ERROR.getCode());
			return res;
		}
		if (!"P0".equals(rtnMapP2P.get("errCode"))) {
			logger.error("学生还款给课栈 成功后，发起课栈代付交易给P2P错误："+ rtnMapP2P.get("errCode") + ":"+ rtnMapP2P.get("errMsg"));
			res.setCode(CodeEnum.ERR_DATABASE_CALL_ERROR.getCode());
			return res;
		}
		// 调用晶晶的存储过程把成功的代收付过渡至历史表
		logger.info("调用存储过程把成功的代收付数据过渡至历史表");
		Map<String, String> param = new HashMap<String, String>();
		super.getSqlSession().selectList("MyBatisMap.setgeneration", param);
		logger.info("=============MyBatisMap.setgeneration=======param==="+param);
		if (null == param || !String.valueOf(param.get("on_err_code")).equals("0")) {
			logger.error("维护代付表历史数据失败！");
			res.setCode(CodeEnum.ERR_DATABASE_CALL_ERROR.getCode());
			return res;
		}
		return res;
	}
	
	/**
	 * 根据传入的订单数据，封装要传入代收付系统的数据OrderDetail
	 * @param transOrderInfo
	 * @return
	 */
	private OrderDetail getOrderDetailByTransOrderInfo(TransOrderInfo transOrderInfo){
		OrderDetail orderDetail = new OrderDetail();
		String batch=settlementLogic.getBatchNo(transOrderInfo.getAccountDate(),
					SettleConstants.ROP_RECEIVE_BATCH_CODE,transOrderInfo.getMerchantCode());
		orderDetail.setRequestNo(batch);//批号
		orderDetail.setBussinessCode(SettleConstants.COLLECTION_CODE);
		orderDetail.setBussinessType(0);//业务类型ID:0实时;1非实时
		orderDetail.setRootInstCd(transOrderInfo.getMerchantCode());
		orderDetail.setUserId(transOrderInfo.getUserId());
		//查询用户的绑卡信息
		AccountInfoQuery accountInfoQuery=new AccountInfoQuery();
		accountInfoQuery.setAccountPurpose(Constants.ACCOUNT_PURPOSE_1);
		accountInfoQuery.setAccountName(transOrderInfo.getUserId());
		accountInfoQuery.setRootInstCd(transOrderInfo.getMerchantCode());
		List<AccountInfo> accountInfos = accountInfoManager.queryViewByUserIdAndPurpose(accountInfoQuery);
		if (accountInfos.size()==0) {
			accountInfoQuery.setAccountPurpose(Constants.ACCOUNT_PURPOSE_4);
			accountInfos = accountInfoManager.queryViewByUserIdAndPurpose(accountInfoQuery);
		}
		if (accountInfos.size()==0) {
			logger.info("实时代收   无法获取用户 ："+transOrderInfo.getUserId()+"  结算卡信息-------------------");
			return null;
		}
		AccountInfo accountInfo = accountInfos.get(0);
		String certificateType = accountInfo.getCertificateType();
		if(certificateType!=null && !"".equals(certificateType)){
			orderDetail.setCertificateType(Integer.parseInt(certificateType));
		}
		
		orderDetail.setCertificateNumber(accountInfo.getCertificateNumber());
		orderDetail.setAccountNo(accountInfo.getAccountNumber());
		orderDetail.setAccountName(accountInfo.getAccountName());
		String accountProp = "10"; //定义此变量仅用于记录日志
		orderDetail.setAccountProp(accountProp); //对私
		if("1".equals(accountInfo.getAccountProperty())){
			accountProp="20";
			orderDetail.setAccountProp(accountProp);// 对公
		}
		orderDetail.setAccountType(Integer.parseInt(accountInfo.getAccountTypeId()));
		orderDetail.setBankCode(accountInfo.getBankHead());
		orderDetail.setBankName(accountInfo.getBankHeadName());
		orderDetail.setPayBankCode(accountInfo.getBankBranch());
		orderDetail.setProvince(accountInfo.getBankProvince());
		orderDetail.setCity(accountInfo.getBankCity());
		orderDetail.setOrderNo(transOrderInfo.getOrderNo());
		orderDetail.setRemark(transOrderInfo.getRemark());
		orderDetail.setOrderType(1); //代收
		orderDetail.setAmount(transOrderInfo.getAmount());
		orderDetail.setCurrency(accountInfo.getCurrency());
		orderDetail.setAccountDate(transOrderInfo.getAccountDate());
		return orderDetail; 
	}
	
	/**
	 * 实时代收(提交到代收付系统)
	 * @param transOrderInfo
	 */
	public String submitToCollRealTime(TransOrderInfo transOrderInfo){
		logger.info("实时代收----入参---transOrderInfo=="+transOrderInfo);
		if (transOrderInfo==null) {
			return CodeEnum.ERR_PARAM_NULL.getCode();
		}
		OrderDetail orderDetail = getOrderDetailByTransOrderInfo(transOrderInfo);
		if (orderDetail==null) {
			logger.info("实时代收  没有查到用户: "+transOrderInfo.getUserId()+" 机构码： "+transOrderInfo.getMerchantCode() +" 结算卡！");
			transOrderInfo.setErrorMsg(CodeEnum.ERR_ACCOUNT_NO_DEBIT_CARD.getMessage());
			return CodeEnum.ERR_ACCOUNT_NO_DEBIT_CARD.getCode();
		}
		logger.info("--实时代收--发送到代收付的参数:	RequestNo="+orderDetail.getRequestNo()+",BussinessCode="+orderDetail.getBussinessCode()
				+",BussinessType=0,RootInstCd="+orderDetail.getRootInstCd()+",UserId="+orderDetail.getUserId()
				+",CertificateType="+orderDetail.getCertificateType()+",AccountNo="+orderDetail.getAccountNo()+",AccountName="
				+orderDetail.getAccountName()+",accountProp="+orderDetail.getAccountProp()+",AccountType="+orderDetail.getAccountType()
				+",BankCode="+orderDetail.getBankCode()+",BankName="+orderDetail.getBankName()+",PayBankCode="
				+orderDetail.getPayBankCode()+",OrderNo="+orderDetail.getOrderNo()+",OrderType="+orderDetail.getOrderType()
				+",Amount="+orderDetail.getAmount()+",Currency="+orderDetail.getCurrency()+",AccountDate="+orderDetail.getAccountDate());
		//提交到代收付系统
		OrderDetail orderDe = null;
		try {
			orderDe = crpsApiService.transDetailFromSingle(orderDetail);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			logger.error("实时代收  用户: "+transOrderInfo.getUserId()+" 机构码： "+transOrderInfo.getMerchantCode() +" 调用代收付系统异常！");
			transOrderInfo.setErrorMsg(CodeEnum.ERR_CRPS_CALL_ERROR.getMessage());
			return CodeEnum.ERR_CRPS_CALL_ERROR.getCode(); 
		}
		int statusId = orderDe.getStatusId() == null?0:orderDe.getStatusId();
		String returnCode = orderDe.getRetCode();
		String errorMsg = orderDe.getErrMsg();
		logger.info("实时代收 用户: "+transOrderInfo.getUserId()+" 机构码： "+transOrderInfo.getMerchantCode() +" 调用代收付系统返回statusId="+orderDe.getStatusId()+",returnCode="+returnCode+",errorMsg="+errorMsg);
		//如果returnCode是100000可能是代收付系统调用多渠道的额异常，具体看errorMsg
		if(!"100000".equals(returnCode)){
			logger.info("实时代收        失败      用户: "+transOrderInfo.getUserId()+" 机构码： "+transOrderInfo.getMerchantCode());
			transOrderInfo.setErrorCode(returnCode);
			transOrderInfo.setErrorMsg(errorMsg);
			return CodeEnum.FAILURE.getCode(); 
		}
		//如果是处理中
		if (statusId==12) {
			logger.info("实时代收  处理中  用户: "+transOrderInfo.getUserId()+" 机构码： "+transOrderInfo.getMerchantCode());
			transOrderInfo.setErrorCode(statusId+"");
			transOrderInfo.setErrorMsg(errorMsg);
			return "2"; 
		}
		//如果不成功
		if (statusId!=15) {
			logger.info("实时代收  失败  用户: "+transOrderInfo.getUserId()+" 机构码： "+transOrderInfo.getMerchantCode());
			transOrderInfo.setErrorCode(statusId+"");
			transOrderInfo.setErrorMsg(errorMsg);
			return CodeEnum.FAILURE.getCode(); 
		}
		return  CodeEnum.SUCCESS.getCode();
	}
	
	/**
	 * 交易信息提交到代收付系统
	 * @param type 参考平台注释6:代收，7：代付
	 * @param batch 批次，可为空
	 * @param  accountDate  yyyy-MM-dd
	 */
	public void submitToCollection(int type,String batch,String rootInstCd,Date accountDate){
		logger.info("-------业务类型："+type+"-------批次号："+batch+"--------机构号-"+rootInstCd+"---accountDate:"+accountDate);
		GenerationPaymentQuery query=new GenerationPaymentQuery();
		query.setStatusId(Integer.parseInt(BaseConstants.ACCOUNT_STATUS_OK));
		query.setSendType(null);
		query.setRootInstCd(rootInstCd);
		query.setOrderType(type);
		query.setAccountDate(accountDate);
		List<GenerationPayment> generationPaymentList=generationPaymentManager.queryListByOrderType(query);
		logger.info("---业务类型："+type+"----批次号："+batch+"----机构号-"+rootInstCd+"----generationPaymentList="+generationPaymentList);
		if(generationPaymentList.size()==0){
			return;
		}
		List<GenerationPayment> generationPayList = new ArrayList<GenerationPayment>();
		//如果调用代收付出现错误，将数据
		List<OrderDetail> orderDetailList = new ArrayList<OrderDetail>();
		for (GenerationPayment generationPayment:generationPaymentList) {
			logger.info("-------业务类型："+type+"-------批次号："+batch+"--------机构号-"+rootInstCd+"----generationPaymentList.size:"+generationPaymentList.size());
			OrderDetail orderDetail = new OrderDetail(); 
			orderDetail.setRequestNo(batch);
			orderDetail.setBussinessCode(generationPayment.getBussinessCode());
			orderDetail.setBussinessType(1);//业务类型ID:0实时;1非实时
			orderDetail.setRootInstCd(generationPayment.getRootInstCd());
//			orderDetail.setProductId("1");
			orderDetail.setUserId(generationPayment.getUserId());
			String certificateType = generationPayment.getCertificateType();
			if(certificateType!=null && !"".equals(certificateType)){
				orderDetail.setCertificateType(Integer.parseInt(certificateType));
			}
			orderDetail.setCertificateNumber(generationPayment.getCertificateNumber());
			orderDetail.setAccountNo(generationPayment.getAccountNo());
			orderDetail.setAccountName(generationPayment.getAccountName());
			String accountProp = "10"; //定义此变量仅用于记录日志
			orderDetail.setAccountProp(accountProp); //对私
			if("1".equals(generationPayment.getAccountProperty())){
				accountProp="20";
				orderDetail.setAccountProp(accountProp);// 对公
			}
			orderDetail.setAccountType(Integer.parseInt(generationPayment.getAccountType()));
			orderDetail.setBankCode(generationPayment.getBankCode());
			orderDetail.setBankName(generationPayment.getOpenBankName());
			orderDetail.setPayBankCode(generationPayment.getPayBankCode());
//			orderDetail.setPayBankName(generationPayment.getp);
			orderDetail.setProvince(generationPayment.getProvince());
			orderDetail.setCity(generationPayment.getCity());
			orderDetail.setOrderNo(generationPayment.getOrderNo());
			orderDetail.setRemark(generationPayment.getRemark());
			Integer orderType = generationPayment.getOrderType();
			logger.info("-------业务类型："+type+"-------批次号："+batch+"--------机构号-"+rootInstCd+"--自己系统--orderType="+orderType);
			int orderrType = 1; //定义此变量仅用于记录日志
			orderDetail.setOrderType(orderrType); //代收
			if (orderType==SettleConstants.ORDER_WITHDRAW || orderType==SettleConstants.ORDER_WITHHOLD) {
				orderrType = 2;
				orderDetail.setOrderType(orderrType); //代付
			}
			orderDetail.setAmount(generationPayment.getAmount());
			orderDetail.setCurrency(generationPayment.getCurrency());
			orderDetail.setAccountDate(accountDate);
			logger.info("---业务类型："+type+"---批次号："+batch+"---机构号-"+rootInstCd
					+"--发送到代收付的参数:	RequestNo="+batch+",BussinessCode="+generationPayment.getBussinessCode()
					+",BussinessType=0,RootInstCd="+generationPayment.getRootInstCd()+",UserId="+generationPayment.getUserId()
					+",CertificateType="+certificateType+",AccountNo="+generationPayment.getAccountNo()+",AccountName="
					+generationPayment.getAccountName()+",accountProp="+accountProp+",AccountType="+generationPayment.getAccountType()
					+",BankCode="+generationPayment.getBankCode()+"BankName="+generationPayment.getOpenBankName()+",PayBankCode="
					+generationPayment.getPayBankCode()+",OrderNo="+generationPayment.getOrderNo()+",OrderType="+orderrType
					+",Amount="+generationPayment.getAmount()+",Currency="+generationPayment.getCurrency()+",AccountDate="+accountDate);
			//发送到代付系统
			ResultCode resultCode = null;
			try {
				resultCode = crpsApiService.transDetailFromOrder(orderDetail);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				logger.error("---业务类型："+type+"---批次号："+batch+"---机构号-"+rootInstCd+"--order_no:"+generationPayment.getOrderNo()+"--调用代收付系统异常！");
				continue;
			}
			logger.info("---业务类型："+type+"---批次号："+batch+"---机构号-"+rootInstCd+"--order_no:"+generationPayment.getOrderNo()+"--resultCode:"+resultCode);
			int retCode = resultCode.getRetMsg().getRetCode();
			String errorMsg=resultCode.getRetMsg().getErrMsg();
			logger.info("---业务类型："+type+"---批次号："+batch+"---机构号-"+rootInstCd+"--order_no:"+generationPayment.getOrderNo()+"--retCode:"+retCode+"--errorMsg:"+errorMsg);
			//如果不成功
			if (retCode!=1023) {
				OrderDetail orderDe = new OrderDetail();
				orderDe.setStatusId(13);
				orderDe.setOrderNo(generationPayment.getOrderNo());
				orderDe.setRetCode(retCode+"");
				orderDe.setErrMsg(errorMsg);
				orderDetailList.add(orderDe);
				continue;
			}
			generationPayment.setSendType(2);//修改状态（已发送）
			generationPayment.setUpdatedTime(null);
			generationPayList.add(generationPayment);
		} 
		logger.info("---业务类型："+type+"---批次号："+batch+"---机构号-"+rootInstCd+"--成功发送到代收付系统的数据个数=="+generationPayList.size());
		logger.info("---业务类型："+type+"---批次号："+batch+"---机构号-"+rootInstCd+"--发送到代收付系统返回失败的数据个数=="+orderDetailList.size());
		//修改状态为已发送
		if (generationPayList.size()!=0) {
			try {
				generationPaymentManager.batchUpdate(generationPayList);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("---业务类型："+type+"---批次号："+batch+"---机构号-"+rootInstCd+"--修改发送类型异常！");
				return;
			}
			logger.info("---业务类型："+type+"---批次号："+batch+"---机构号-"+rootInstCd+"--成功发送到代收付系统并修改发送状态成功！-----------");
		}
		//发送到代收付系统返回失败的数据处理
		if (orderDetailList.size()!=0) {
			String code = this.getGenerationPaymentList(orderDetailList).getCode();
			logger.info("---业务类型："+type+"---批次号："+batch+"---机构号-"+rootInstCd+"--处理发送到代收付系统失败的数据---code=="+code);
		}
	}
	
	/**
	 * 生成代收付明细文件并上传
	 * 返回'ok' ，失败返回失败原因,SettleConstants.FILE_UP_PATH配置下载路径
	 * @param type 参考平台注释6:代收，7：代付
	 * @param batch 批次，可为空
	 * @param  accountDate  yyyy-MM-dd
	 */
	@Override
	public void uploadPaymentFile(int type,String batch,String rootInstCd,Date accountDate){
		//是否添加代收代付标记int type,String batch
		//代付通知之后立即入库，还是定时下载入库
		logger.info("-------文件类型："+type+"-------批次号："+batch+"--------机构号-"+rootInstCd);
		GenerationPaymentQuery query=new GenerationPaymentQuery();
		query.setStatusId(Integer.parseInt(BaseConstants.ACCOUNT_STATUS_OK));
		query.setSendType(null);
		query.setRootInstCd(rootInstCd);
		query.setOrderType(type);
//		if(Constants.HT_FILE_TYPE_6.equals(type+"") && Constants.HT_FILE_BATCH_02.equals(batch) && Constants.HT_ID.equals(rootInstCd)){
//			query.setAccountDate(DateUtils.getDate(this.getAccountDate(), Constants.DATE_FORMAT_YYYYMMDD));
//		}else{
			query.setAccountDate(accountDate);
//		}
		List<GenerationPayment> generationPaymentList=generationPaymentManager.queryListByOrderType(query);
		String msg="";
		SettleBatchResult settleBatchResult = new SettleBatchResult();
//			int orderType=SettleConstants.ORDER_WITHDRAW;
//			int orderType1=SettleConstants.ORDER_WITHDRAW;
//			int orderType2=SettleConstants.ORDER_WITHHOLD;
////				String batch=PartyCodeUtil.getRandomCode();
//				if(BaseConstants.FILE_TYPE_6==type){
//					orderType=SettleConstants.ORDER_BOND_PACKAGE;
//					orderType1=SettleConstants.ORDER_REPAYMENT;
//					orderType2=SettleConstants.ORDER_COLLECTION;
//				}else if(BaseConstants.FILE_TYPE_7==type){
//					orderType=SettleConstants.ORDER_WITHDRAW;
//					orderType1=SettleConstants.ORDER_WITHDRAW;
//					orderType2=SettleConstants.ORDER_WITHHOLD;
//				}
			List txtList= new ArrayList();
			if(generationPaymentList!=null && generationPaymentList.size()>0){
				for (int i = 0; i < generationPaymentList.size(); i++) {
					GenerationPayment generationPayment=generationPaymentList.get(i);
					//还款--代收文件
//					if(orderType==generationPayment.getOrderType() ||orderType1==generationPayment.getOrderType()||orderType2==generationPayment.getOrderType()){
						Map paraMap = new HashMap();
						paraMap.put("F_1", generationPayment.getBussinessCode()==null ? "" : generationPayment.getBussinessCode());
						paraMap.put("F_2", generationPayment.getGeneId().toString()==null ? "" : generationPayment.getGeneId().toString());
						paraMap.put("F_3", generationPayment.getUserId()==null ? "" : generationPayment.getUserId());
						paraMap.put("F_4", generationPayment.getRootInstCd()==null ? "" : generationPayment.getRootInstCd());
						paraMap.put("F_5", generationPayment.getBankCode()==null ? "" : generationPayment.getBankCode());
						paraMap.put("F_6", generationPayment.getAccountType()==null ? "" : generationPayment.getAccountType());
						paraMap.put("F_7", generationPayment.getAccountNo()==null ? "" : generationPayment.getAccountNo());
						paraMap.put("F_8", generationPayment.getAccountName()==null ? "" : generationPayment.getAccountName());
						paraMap.put("F_9", generationPayment.getProvince()==null ? "" : generationPayment.getProvince());
						paraMap.put("F_10", generationPayment.getCity()==null ? "" : generationPayment.getCity());
						paraMap.put("F_11", generationPayment.getOpenBankName()==null ? "" : generationPayment.getOpenBankName());
						paraMap.put("F_12", generationPayment.getAccountProperty().equals("2") ? "0" : generationPayment.getAccountProperty());
						paraMap.put("F_13", generationPayment.getAmount()==null ? "" : generationPayment.getAmount());
						paraMap.put("F_14", generationPayment.getCurrency()==null ? "" : generationPayment.getCurrency());
						paraMap.put("F_15", generationPayment.getCertificateType()==null ? "" : generationPayment.getCertificateType());
						paraMap.put("F_16", generationPayment.getCertificateNumber()==null ? "" : generationPayment.getCertificateNumber());
						paraMap.put("F_17", generationPayment.getPayBankCode()==null ? "" : generationPayment.getPayBankCode());
						paraMap.put("F_18", generationPayment.getRemark()==null ? "" : generationPayment.getRemark());
						txtList.add(paraMap);
					}
				}
//			}
				String path = SettleConstants.FILE_UP_PATH + DateUtils.getyyyyMMdd(Constants.DATE_FORMAT_YYYYMMDD)+PartyCodeUtil.getRandomCode();
				File filePath = new File(path);
				if (!filePath.exists()) {
					filePath.mkdirs();
				}
				String randomStr = "PANYMENT_"+DateUtils.getyyyyMMdd(Constants.DATE_FORMAT_YYYYMMDD)+PartyCodeUtil.getRandomCode()+".csv";
				path=path+File.separator + randomStr;
				Map configMap=new HashMap();
				configMap.put("FILE", path);
				List reportHead = new LinkedList();
				Map headMap=new HashMap();
				headMap.put("F_1", "业务代码");
				headMap.put("F_2", "订单号");
				headMap.put("F_3", "用户编号");
				headMap.put("F_4", "机构编号");
				headMap.put("F_5", "银行代码");
				headMap.put("F_6","账户类型");
				headMap.put("F_7", "账号");
				headMap.put("F_8", "账号名");
				headMap.put("F_9","开户行所在省");
				headMap.put("F_10", "开户行所在市");
				headMap.put("F_11", "开户行名称");
				headMap.put("F_12","账号属性");
				headMap.put("F_13", "金额");
				headMap.put("F_14", "币种");
				headMap.put("F_15", "开户证件类型");
				headMap.put("F_16", "证件号");
				headMap.put("F_17", "支付行号");
				headMap.put("F_18", "交易附言");
				reportHead.add(headMap);
				configMap.put("REPORT-HEAD", reportHead);
				try {
					TxtWriter.WriteTxt(txtList,configMap, SettleConstants.DEDT_SPLIT2,"UTF-8");
//					for (int i = 0; i < 3; i++) {
//						msg=UploadAndDownLoadUtils.uploadFile(path,type, accountDate, batch, SettleConstants.FILE_XML,userProperties.getProperty("RKYLIN_PUBLIC_KEY"),2,userProperties.getProperty("FSAPP_KEY"),userProperties.getProperty("FSDAPP_SECRET"),userProperties.getProperty("FSROP_URL"));
//						String str=msg.split("-")[0];
//						if(!"no".equals(str)){
//							break;
//						}
//						Thread.sleep(10000);
//					}
					msg=UploadAndDownLoadUtils.uploadFile(path,type, accountDate, batch, SettleConstants.FILE_XML,userProperties.getProperty("RKYLIN_PUBLIC_KEY"),2,userProperties.getProperty("FSAPP_KEY"),userProperties.getProperty("FSDAPP_SECRET"),userProperties.getProperty("FSROP_URL"));
					String str=msg.split("-")[0];
					if(!"no".equals(str)){
						settleBatchResult.setAccountDate(accountDate);
						settleBatchResult.setRootInstCd(rootInstCd);
						if (type==6) {
							settleBatchResult.setBatchType(SettleConstants.ROP_RECEIVE_BATCH_CODE);
						} else if (type==7) {
							settleBatchResult.setBatchType(SettleConstants.ROP_PAYMENT_BATCH_CODE);
						}
						settleBatchResult.setBatchNo(batch);
						settleBatchResult.setFileName(randomStr);
						if (type==6 || type==7) {
							settleBatchResultManager.saveSettleBatchResult(settleBatchResult);
						}
						
						for (int i = 0; i < generationPaymentList.size(); i++) {
							generationPaymentList.get(i).setSendType(SettleConstants.SEND_DELAY);
						}
						generationPaymentManager.batchUpdate(generationPaymentList);
					}
				} catch (Exception e) {
					logger.error("生成文件操作异常！" + e.getMessage());
	//				errorResponseService.getErrorResponse("S2", "对账文件生成或文件服务器连接失败");
					msg="文件处理异常！:"+e.getMessage();
				}
				RkylinMailUtil.sendMail("上传信息", "机构号："+rootInstCd+"批次号："+batch+"类型："+type+"信息："+msg, "1663991989@qq.com");
			logger.debug(msg);
			}
//		}else{
//			msg="解析文件暂无数据！";
//		}
	public String getAccountDate(){
		try{
			ParameterInfoQuery parameterInfoQuery=new ParameterInfoQuery();
			parameterInfoQuery.setParameterCode(TransCodeConst.ACCOUNTDATE_CODE);
			List parameterList=parameterInfoManager.queryList(parameterInfoQuery);
			if(parameterList!=null && parameterList.size()>0){
				ParameterInfo parameterInfo=(ParameterInfo) parameterList.get(0);
				return parameterInfo.getParameterValue();
			}else{
				return null;
			}
		}catch(Exception e){
			logger.error(e.getMessage());
			return null;
		}
	}
	public String paymentFile(String invoicedate,String batch,String opertype,String priOrPubKey){
		String msg="ok";
		try{
			String nameAndSuffix=this.getAccountDate()+"FILE_TYPE_"+opertype+".csv";
				Date ad=new Date();
				if(invoicedate!=null && !"".equals(invoicedate)){
					ad=DateUtils.getDate(invoicedate, Constants.DATE_FORMAT_YYYYMMDD);
				}else{
					ad=DateUtils.getDate( this.getAccountDate(), Constants.DATE_FORMAT_YYYYMMDD);
				}
				List list=UploadAndDownLoadUtils.getUrlKeys(Integer.parseInt(opertype), ad, batch, "json",userProperties.getProperty("FSAPP_KEY"),userProperties.getProperty("FSDAPP_SECRET"),userProperties.getProperty("FSROP_URL"));
				if(list!=null && list.size()>0){
					for (int j = 0; j < list.size(); j++) {
						FileUrl fileUrl=(FileUrl) list.get(j);
						String urlKey=fileUrl.getUrl_key();
						String url=this.accountNotify(urlKey,nameAndSuffix,Integer.parseInt(opertype),priOrPubKey);
						if(url!=null && !"".equals(url)){
							msg=this.getGenerationPaymentList(url+File.separator+nameAndSuffix);
						}else{
							logger.error("下载文件失败！");
							msg= "下载文件失败！";
						}
					}
				}else{
					logger.error("暂无数据！");
					msg= "暂无数据！";
				}
			
		}catch(Exception e){
			logger.error(e.getMessage());
			msg= "数据异常！"+e.getMessage();
		}
		logger.info(msg);
		return msg;
		
	}
	//代收付结果读入
	public Map<String,String> readPaymentFile(String filetype,String invoicedate,String batch) {
		logger.info("代收付结果读入-----------START----------------" + new Date());
		logger.info("输入参数：" + filetype);
		logger.info("输入参数：" + batch);
		logger.info("输入参数：" + invoicedate);
		String msg = "";
		Map<String, String> rtnMap = new HashMap<String, String>();
		msg = this.paymentFile(invoicedate, batch, filetype,userProperties.getProperty("RKYLIN_PUBLIC_KEY"));
		if ("ok".equals(msg)) {
			Map<String, String> rtnMapUp = settlementServiceThr.updateCreditAccountSec();
			if ("0000".equals(rtnMapUp.get("errCode"))) {
				// ********特殊处理：学生还款给课栈 成功后 ， 发起课栈代付交易给P2P 20150603 start
				Map<String, String> rtnMapP2P = settlementServiceThr.withholdToP2P();
				if (!"P0".equals(rtnMapP2P.get("errCode"))) {
					logger.error("学生还款给课栈 成功后，发起课栈代付交易给P2P错误："+ rtnMapUp.get("errCode") + ":"+ rtnMapUp.get("errMsg"));
				}
			} else {
				RkylinMailUtil.sendMail("代收付通知接口","代收付更新错误：" + rtnMapUp.get("errCode") + ":"+ rtnMapUp.get("errMsg"), "21401233@qq.com");
				logger.error("代收付更新错误：" + rtnMapUp.get("errCode") + ":"+ rtnMapUp.get("errMsg"));
			}
			// 调用晶晶的存储过程把成功的代收付过渡至历史表
			logger.info("调用存储过程把成功的代收付数据过渡至历史表");
			Map<String, String> param = new HashMap<String, String>();
			super.getSqlSession().selectList("MyBatisMap.setgeneration", param);
			if (null == param || !String.valueOf(param.get("on_err_code")).equals("0")) {
				logger.error("维护代付表历史数据失败！");
			}
			logger.info("代收付数据过渡至历史表成功");
		} else {
			rtnMap.put("errCode", "0001");
			rtnMap.put("errMsg", "代收付结果读入错误！");
			RkylinMailUtil.sendMail("代收付通知接口", "代收付结果读入错误！", "21401233@qq.com");
			logger.error("代收付结果通知接口读入错误！");
			return rtnMap;
		}
		rtnMap.put("errCode", "0000");
		logger.info("代收付结果读入-----------END----------------" + new Date());
		return rtnMap;
	}
}
