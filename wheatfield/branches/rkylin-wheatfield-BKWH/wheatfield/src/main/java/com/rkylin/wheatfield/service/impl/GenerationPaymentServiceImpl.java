package com.rkylin.wheatfield.service.impl;

import com.Rop.api.domain.FileUrl;
import com.rkylin.common.RedisIdGenerator;
import com.rkylin.crps.pojo.BaseResponse;
import com.rkylin.crps.pojo.OrderDetail;
import com.rkylin.crps.pojo.OrderDetails;
import com.rkylin.crps.service.CrpsApiService;
import com.rkylin.database.BaseDao;
import com.rkylin.file.txt.TxtReader;
import com.rkylin.file.txt.TxtWriter;
import com.rkylin.gaterouter.dto.bankaccount.cmbc.BatchAccountBalanceQueryDto;
import com.rkylin.gaterouter.dto.bankaccount.cmbc.BatchAccountBalanceQueryRespDto;
import com.rkylin.gaterouter.dto.bankpayment.BatchPaymentDto;
import com.rkylin.gaterouter.dto.bankpayment.BatchPaymentRespDto;
import com.rkylin.gaterouter.dto.bankpayment.PaymentDto;
import com.rkylin.gaterouter.service.BankPaymentService;
import com.rkylin.gaterouter.service.CmbcAccountService;
import com.rkylin.order.mixservice.CollectionWithholdService;
import com.rkylin.order.mixservice.SettlementToOrderService;
import com.rkylin.order.pojo.OrderPayment;
import com.rkylin.utils.RkylinMailUtil;
import com.rkylin.utils.SendSMS;
import com.rkylin.wheatfield.api.AccountService;
import com.rkylin.wheatfield.api.GenerationPayDubboService;
import com.rkylin.wheatfield.common.DateUtils;
import com.rkylin.wheatfield.common.PartyCodeUtil;
import com.rkylin.wheatfield.common.RedisBase;
import com.rkylin.wheatfield.common.UploadAndDownLoadUtils;
import com.rkylin.wheatfield.common.ValHasNoParam;
import com.rkylin.wheatfield.constant.BaseConstants;
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.constant.CrpsConstants;
import com.rkylin.wheatfield.constant.GatewayConstants;
import com.rkylin.wheatfield.constant.RedisConstants;
import com.rkylin.wheatfield.constant.SettleConstants;
import com.rkylin.wheatfield.constant.TransCodeConst;
import com.rkylin.wheatfield.dao.FinanaceAccountDao;
import com.rkylin.wheatfield.dao.GenerationPaymentDao;
import com.rkylin.wheatfield.dao.TransOrderInfoDao;
import com.rkylin.wheatfield.exception.AccountException;
import com.rkylin.wheatfield.manager.AccountInfoManager;
import com.rkylin.wheatfield.manager.CorporatAccountInfoManager;
import com.rkylin.wheatfield.manager.GenerationPaymentManager;
import com.rkylin.wheatfield.manager.ParameterInfoManager;
import com.rkylin.wheatfield.manager.SettleBatchResultManager;
import com.rkylin.wheatfield.manager.TransDaysSummaryManager;
import com.rkylin.wheatfield.manager.TransOrderInfoManager;
import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.model.TransOrderInfoResponse;
import com.rkylin.wheatfield.model.TransOrderInfosResponse;
import com.rkylin.wheatfield.pojo.AccountInfo;
import com.rkylin.wheatfield.pojo.AccountInfoQuery;
import com.rkylin.wheatfield.pojo.CorporatAccountInfo;
import com.rkylin.wheatfield.pojo.CorporatAccountInfoQuery;
import com.rkylin.wheatfield.pojo.FinanaceAccount;
import com.rkylin.wheatfield.pojo.GenerationPayment;
import com.rkylin.wheatfield.pojo.GenerationPaymentQuery;
import com.rkylin.wheatfield.pojo.OrderAuxiliary;
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
import com.rkylin.wheatfield.service.ParameterInfoService;
import com.rkylin.wheatfield.service.PaymentAccountService;
import com.rkylin.wheatfield.service.SettlementService;
import com.rkylin.wheatfield.service.SettlementServiceThr;
import com.rkylin.wheatfield.settlement.SettlementLogic;
import com.rkylin.wheatfield.utils.CodeEnum;
import com.rkylin.wheatfield.utils.CommUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Transactional

@Service("generationPaymentService")
public class GenerationPaymentServiceImpl extends BaseDao implements GenerationPaymentService,GenerationPayDubboService,
AccountService,
		IAPIService{
	private static Object lock=new Object();
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
	CollectionWithholdService collectionWithholdService;
	@Autowired
	RedisIdGenerator redisIdGenerator;
	
	@Autowired
	private CorporatAccountInfoManager corporatAccountInfoManager;
	public void setCrpsApiService(CrpsApiService crpsApiService) {
		this.crpsApiService = crpsApiService;
	}

	@Autowired
	private SettlementToOrderService settlementToOrderService;
	
	@Autowired
	RedisBase redisBase;
	
	@Autowired
	private TransOrderInfoDao transOrderInfoDao;
	
	@Autowired
	@Qualifier("finanaceAccountDao")
	private FinanaceAccountDao finanaceAccountDao;
	@Autowired
	private GenerationPaymentDao generationPaymentDao;
    @Autowired
    private BankPaymentService bankPaymentService;
    
    @Autowired
    private  ParameterInfoService parameterInfoService;
    
    @Autowired
    private CmbcAccountService cmbcAccountService;
	
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
	@Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
	public String refund(BaseResponse baseResponse){
		logger.info("refund====baseResponse=="+baseResponse);
		if (baseResponse==null) {
			return CodeEnum.FAILURE.getCode();
		}
		//退票或者单笔实时返回结果
		if (baseResponse instanceof OrderDetail) {
			OrderDetail orderDetail = (com.rkylin.crps.pojo.OrderDetail) baseResponse;
//			logger.info("-------代收付调用账户退票  开始<<<<<<<<<<userId="+orderDetail.getUserId()+" 代收付返回的信息=errMsg="+orderDetail.getErrMsg()+", statusId = "+orderDetail.getStatusId());
			CommonResponse res = null;
			if (orderDetail.getStatusId()==16) { //16 表示退票 
				try {
					res = refund(orderDetail.getOrderNo());
					//原退票流程结束后，调用此方法进行卡信息状态修改
					this.updateStatus(orderDetail);
				} catch (Exception e) { 
					e.printStackTrace();
					e.getMessage();
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
//				logger.info("-------代收付调用账户退票   结束<<<<<<<<<<<<<<<<<<代收付系统传入的参数异常！<<<<<<<<<<<<<<<<<userId="+orderDetail.getUserId()+",code="+CodeEnum.FAILURE.getCode());
//				return CodeEnum.FAILURE.getCode();
			}else{ //单笔实时代收
				logger.info("代收付推送实时代收的结果   开始  ==订单号="+orderDetail.getOrderNo()+" 机构号=="+orderDetail.getRootInstCd());
				CommonResponse response = recAndPaySysPushRealTimeRec(orderDetail);
				logger.info("代收付推送实时代收的结果   结束  ==订单号="+orderDetail.getOrderNo()+" 机构号=="+orderDetail.getRootInstCd()+" Code="+response.getCode()+"(1为成功,成功指代收付返回成功，并且处理成功，其他失败), errMsg="+response.getMsg());
			}
		}else{
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//解析代付结果，将结果入库
			OrderDetails orderDetails = (com.rkylin.crps.pojo.OrderDetails) baseResponse;
			List<OrderDetail> orderDetailList = orderDetails.getOrderDetails();
			if (orderDetailList==null || orderDetailList.size()==0) {
				logger.info("-------代收付调用账户解析代收付结果  结束<<<<<<<<<<<<<<参数为空<<<<<<<<<<<<<<<<");
				return CodeEnum.FAILURE.getCode();
			}
			logger.info("-------代收付调用账户解析代收付结果   userId="+orderDetailList.get(0).getUserId());
//			String oneKey = CommUtil.getGenerateId();
			String oneKey = orderDetailList.get(0).getRequestNo();
			logger.info("推送一次缓存数据的oneKey(批次号) ==="+oneKey);
			synchronized (lock) {
				logger.info("存储之前   所有缓存的  数据的   key ===" + redisBase.getSet(RedisConstants.GENERATIONPAYMENT_PUSH));
				redisBase.saveOrUpdateStrSet(RedisConstants.GENERATIONPAYMENT_PUSH, oneKey, 7, TimeUnit.HOURS, false);
				logger.info("存储之后  所有缓存的  数据的   key ===" + redisBase.getSet(RedisConstants.GENERATIONPAYMENT_PUSH));
				redisBase.saveOrUpdateList(oneKey, orderDetailList, 6, TimeUnit.HOURS);
			} 
		}
		return CodeEnum.SUCCESS.getCode();
	}
	
	public GenerationPaymentServiceImpl(){
	}
	
//	public GenerationPaymentServiceImpl(RedisBase redisBase,GenerationPaymentManager generationPaymentManager,
//			IErrorResponseService errorResponseService,HttpServletRequest request,
//			ParameterInfoManager parameterInfoManager,	SettleBatchResultManager settleBatchResultManager,
//			SettlementService settlementService,Properties userProperties,SettlementServiceThr settlementServiceThr,
//			TransDaysSummaryManager transDaysSummaryManager,TransOrderInfoManager transOrderInfoManager,
//			PaymentAccountService paymentAccountService,SettlementLogic settlementLogic,CrpsApiService crpsApiService,
//			AccountInfoManager accountInfoManager,CorporatAccountInfoManager corporatAccountInfoManager,
//			SettlementToOrderService settlementToOrderService,BaseDao baseDao){
//		this.redisBase = redisBase;
//		this.generationPaymentManager = generationPaymentManager;
//		this.errorResponseService = errorResponseService;
//		this.request = request;
//		this.parameterInfoManager = parameterInfoManager;
//		this.settleBatchResultManager = settleBatchResultManager;
//		this.settlementService = settlementService;
//		this.userProperties = userProperties;
//		this.settlementServiceThr = settlementServiceThr;
//		this.transDaysSummaryManager = transDaysSummaryManager;
//		this.transOrderInfoManager = transOrderInfoManager;
//		this.paymentAccountService = paymentAccountService;
//		this.settlementLogic = settlementLogic;
//		this.crpsApiService = crpsApiService;
//		this.accountInfoManager = accountInfoManager;
//		this.corporatAccountInfoManager = corporatAccountInfoManager;
//		this.settlementToOrderService = settlementToOrderService;
//		this.baseDao=baseDao;	}
	
	public void handleRecAndPayCacheData() {
		synchronized (lock) {
			Set<String> keySet = redisBase.getSet(RedisConstants.GENERATIONPAYMENT_PUSH);
			if (keySet==null || keySet.size()==0) {
				return;
			}
			for (String key : keySet) {
				List<OrderDetail> orderDetailList = redisBase.getList(key);
				
				
				if (orderDetailList == null || orderDetailList.size()==0) {
					logger.info("处理缓存中的代收付数据   根据  key=" + key + " 查出数据(continue)  orderDetailList的个数==0");
					continue;
				}
				logger.info("处理缓存中的代收付数据   根据  key=" + key + " 查出数据  orderDetailList的个数=="+orderDetailList.size());
				CommonResponse res = null;
				try {
					res = getGenerationPaymentList(orderDetailList,1);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(e.getMessage());
					logger.error("处理缓存中的代收付数据   处理key=" + key + " 时发生异常");
					continue;
				}
				if (!CodeEnum.SUCCESS.getCode().equals(res.getCode())) {
					logger.info("处理缓存中的代收付数据   处理key=" + key + " 时处理失败");
					continue;
				}
				logger.info("-------代收付调用账户解析代付结果   结束<<<<<<<<<<<<<<<<<成功<<<<<<<<<<<<userId="
						+ orderDetailList.get(0).getUserId() + ",code=" + CodeEnum.SUCCESS.getCode());
				redisBase.delSet(key);
				logger.info("清除缓存后   key=" + key + " 的缓存数据==" + redisBase.getSet(key));
				redisBase.saveOrUpdateStrSet(RedisConstants.GENERATIONPAYMENT_PUSH, key, 7L, TimeUnit.DAYS, true);
			}
		}
	}
	
	/**
	 * 代收付推送实时代收的结果（只有成功或失败）
	 * @param orderDetail 代收付封装的数据实体
	 * @return
	 */
	private CommonResponse recAndPaySysPushRealTimeRec(OrderDetail orderDetail){
		String orderNo = orderDetail.getOrderNo();
		String instCode = orderDetail.getRootInstCd();
		logger.info("代收付推送实时代收的结果==orderNo="+orderNo+" instCode=="+instCode+" statusId="+orderDetail.getStatusId());
		CommonResponse res = new CommonResponse();
		if (orderNo==null || "".equals(orderNo) || instCode==null ||"".equals(instCode)) {
			res.setCode(CodeEnum.ERR_PARAM_NULL.getCode());
			res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
			return res;
		}
		TransOrderInfoQuery query = new TransOrderInfoQuery();
		query.setOrderNo(orderNo);
		query.setMerchantCode(instCode);
		List<TransOrderInfo> transOrderInfoList = transOrderInfoManager.queryList(query);
		logger.info("代收付推送实时代收的结果,根据订单号=="+orderNo+",机构号 =="+instCode+" 查出的数据个数="+transOrderInfoList.size());
		if (transOrderInfoList.size()==0) {
			res.setCode(CodeEnum.ERR_TRADE_RECORD_NO_RESULT.getCode());
			res.setMsg(CodeEnum.ERR_TRADE_RECORD_NO_RESULT.getMessage());
			return res;
		} 
		TransOrderInfo transOrderInfo = transOrderInfoList.get(0);
		//如果不是处理中的状态，直接结束，不做任何处理
		if (transOrderInfo.getStatus()==TransCodeConst.TRANS_STATUS_INVALIDATION || transOrderInfo.getStatus()==TransCodeConst.TRANS_STATUS_PAY_SUCCEED) {
			logger.info("代收付推送实时代收的结果,订单号=="+orderNo+",机构号 =="+instCode+" 该数据在账户状态（4:成功，5:失败）已经是终态transOrderInfo.getStatus()="+transOrderInfo.getStatus());
			res.setCode(CodeEnum.ERR_TRADE_RECORD_NO_RESULT.getCode());
			res.setMsg(CodeEnum.ERR_TRADE_RECORD_NO_RESULT.getMessage());
			return res;
		}
		if(orderDetail.getStatusId()==15){//代收付系统返回成功
			if(!settlementServiceThr.isDayEndOk()){
				logger.info("代收付推送实时代收的结果,订单号=="+orderNo+",机构号 =="+instCode+"   日终没有正常结束！");
				res.setCode(CodeEnum.ERR_SYS_DAYEND_NOT_OK.getCode());
				res.setMsg(CodeEnum.ERR_SYS_DAYEND_NOT_OK.getMessage());
				return res;
			}
			//记账，生成流水及修改订单信息
			transOrderInfo.setErrorMsg("");
			ErrorResponse response=paymentAccountService.collectionRealTime(transOrderInfo);
			logger.info("代收付推送实时代收的结果,订单号=="+orderNo+",机构号 =="+instCode+" 生成流水及订单信息 response.isIs_success()="+response.isIs_success()+" msg ="+response.getMsg());
			//如果失败，修改订单信息状态
			if(!response.isIs_success()){
				transOrderInfo.setStatus(TransCodeConst.TRANS_STATUS_PAY_FAILED);
//				transOrderInfo.setStatus(TransCodeConst.TRANS_STATUS_INVALIDATION);
				transOrderInfo.setErrorMsg(response.getMsg());
				transOrderInfoManager.saveTransOrderInfo(transOrderInfo);
				res.setCode(CodeEnum.FAILURE.getCode());
				res.setMsg(response.getMsg());
				return res;
			}	
		}else{ //代收付系统返回失败
			logger.info("代收付推送实时代收的结果,订单号=="+orderNo+",机构号 =="+instCode+" 代收付返回失败 orderDetail.getRetCode()="+orderDetail.getRetCode()+" orderDetail.getErrMsg() ="+orderDetail.getErrMsg());
			transOrderInfo.setStatus(TransCodeConst.TRANS_STATUS_PAY_FAILED);
//			transOrderInfo.setStatus(TransCodeConst.TRANS_STATUS_INVALIDATION);
//			transOrderInfo.setErrorCode(orderDetail.getRetCode());
			transOrderInfo.setErrorMsg(orderDetail.getRetCode()+":"+orderDetail.getErrMsg());
			transOrderInfoManager.saveTransOrderInfo(transOrderInfo);
			res.setCode(CodeEnum.FAILURE.getCode());
			res.setMsg(orderDetail.getErrMsg());
		}
		List<TransOrderInfo> transOrInfoList = new ArrayList<TransOrderInfo>();
		transOrInfoList.add(transOrderInfo);
		// 代收付返回成功或失败的订单通知订单系统；
		Map<String,Object> rtnMapUp = null;
    	try {
    		rtnMapUp = updateOrderSysStatus(transOrInfoList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("代收付推送实时代收的结果,订单号=="+transOrderInfo.getOrderNo()+",机构号 =="+transOrderInfo.getMerchantCode()+" 调用订单系统异常");
			res.setCode(CodeEnum.FAILURE.getCode());
			res.setMsg("调用订单系统异常！");
			return res;
		}
    	logger.info("代收付推送实时代收的结果,订单号=="+transOrderInfo.getOrderNo()+",机构号 =="+transOrderInfo.getMerchantCode()+" 订单系统返回的数据rtnMapUp="+rtnMapUp);
     	if(!"true".equals(rtnMapUp.get("issuccess"))){
			List<OrderPayment> rtnOrderPayList =  (List<OrderPayment>)rtnMapUp.get("paymentList");
			res.setCode(CodeEnum.FAILURE.getCode());
			if("1".equals(rtnOrderPayList.get(0).getStatusId())){
				logger.info("代收付推送实时代收的结果,订单号=="+orderNo+",机构号 =="+instCode+"  在订单系统找不到数据");
				res.setMsg("在订单系统找不到数据！");
			}else{
				logger.info("代收付推送实时代收的结果,订单号=="+orderNo+",机构号 =="+instCode+"  订单系统数据状态已更新");
				res.setMsg("订单系统数据状态已更新！");
			}
     	}
    	return res;
	}
	
	/**
	 * // 代收付返回成功或失败的订单通知订单系统；
	 * @param transOrderInfo
	 * @param status
	 */
	private Map<String,Object> updateOrderSysStatus(List<TransOrderInfo> transOrderInfoList) {
		List<OrderPayment> orderPayments = new ArrayList<OrderPayment>();
		for (TransOrderInfo transOrderInfo : transOrderInfoList) {
			OrderPayment orderPayment = new OrderPayment();
			orderPayment.setPaymentId(transOrderInfo.getOrderNo());
			orderPayment.setPaymentTypeId(SettleConstants.COLLECTION_CODE);
			orderPayment.setStatusId("0");// 成功
			if (transOrderInfo.getStatus()==TransCodeConst.TRANS_STATUS_PAY_FAILED || transOrderInfo.getStatus()==TransCodeConst.TRANS_STATUS_INVALIDATION) {
				orderPayment.setStatusId("1");// 失败
			}
			orderPayment.setEndTime(DateUtils.getSysDate(Constants.DATE_FORMAT_YYYYMMDDHHMMSS));
			orderPayments.add(orderPayment);
		}
		Map<String,Object> rtnMapUp = new HashMap<String, Object>();
    	rtnMapUp.put("paymentList", orderPayments);
    	logger.info("代收付推送实时代收的结果==机构号 =="+transOrderInfoList.get(0).getMerchantCode()+" 发送到订单系统的参数rtnMapUp="+rtnMapUp);
    	rtnMapUp = settlementToOrderService.updateBatchPaymentStatus(rtnMapUp);
		return rtnMapUp;
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
	
	/**
	 * 手动退票  将原来的订单状态置为9（退票），并将涉及到的所有账户的记帐还原。
	 */
	@Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
	public CommonResponse getTransInforAndRefundRecords(String orderNo, String instCode) {
		logger.info("退票传入参数  orderNo="+orderNo+",instCode="+instCode);
		CommonResponse res = new CommonResponse();
		if (orderNo==null||"".equals(orderNo)||instCode==null||"".equals(instCode)) {
			res.setCode(CodeEnum.ERR_PARAM_NULL.getCode());
			res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
			return res;
		}
		TransOrderInfoQuery query = new TransOrderInfoQuery();
		query.setOrderNo(orderNo);
		query.setMerchantCode(instCode);
		List<TransOrderInfo> transOrderInfoList = transOrderInfoManager.selectTransOrderInfosRefund(query);
		logger.info("==orderNo="+orderNo+" instCode=="+instCode+"  transOrderInfoList.size()="+transOrderInfoList.size());
		if (transOrderInfoList.size()==0) {
			logger.info("==orderNo="+orderNo+" instCode=="+instCode+" 订单数据异常");
			res.setCode(CodeEnum.ERR_TRADE_RECORD_NO_RESULT.getCode());
			res.setMsg(CodeEnum.ERR_TRADE_RECORD_NO_RESULT.getMessage());
			return res;
		} 
		TransOrderInfo transOrderInfo = transOrderInfoList.get(0);
		if (TransCodeConst.TRANS_STATUS_PAY_SUCCEED!=transOrderInfo.getStatus()) {
			logger.info("==orderNo="+orderNo+" instCode=="+instCode+" 退票失败 status="+transOrderInfo.getStatus());
			res.setCode(CodeEnum.FAILURE.getCode());
			res.setMsg("此订单非成功状态！");
			return res;
		}
		transOrderInfo.setStatus(TransCodeConst.TRANS_STATUS_REFUND);
		transOrderInfo.setCreatedTime(null);
		transOrderInfo.setUpdatedTime(null);
		transOrderInfoManager.saveTransOrderInfo(transOrderInfo);
		// 生成新的转账，充值等记录流水
		String code = paymentAccountService.makeRefundRecords(transOrderInfo);
		logger.info("==orderNo="+orderNo+" instCode=="+instCode+"  code="+code);
		//账户状态非正常
		if(CodeEnum.ERR_ACCOUNT_NOT_NORMAL.getCode().equals(code)){
			res.setCode(CodeEnum.ERR_ACCOUNT_NOT_NORMAL.getCode());
			res.setMsg(CodeEnum.ERR_ACCOUNT_NOT_NORMAL.getMessage());
			return res;
		}
		//交易数据异常
		if(CodeEnum.ERR_TRADE_RECORD_NO_RESULT.getCode().equals(code) || CodeEnum.ERR_TRADE_RECORD_NOT_IN.getCode().equals(code)){
			res.setCode(CodeEnum.ERR_TRADE_RECORD_NO_RESULT.getCode());
			res.setMsg(CodeEnum.ERR_TRADE_RECORD_NO_RESULT.getMessage());
			return res;
		}
		return res;
	}
	
	/**
	 * 删除的缓存的某机构某天缓存的所有代收付推入的数据
	 * @param rootInstCd
	 * @param date
	 */
	public void delPushedGenPaySet(String rootInstCd,String date){
		logger.info("删除的缓存的机构号为"+rootInstCd+"在"+date+"日缓存的所有代收付推入的数据");
		String key = rootInstCd+"_ORDER_NOS_PUSH_"+date;
		Set<String> set = redisBase.getSet(key);
		logger.info("删除的缓存的机构号为"+rootInstCd+"在"+date+"日缓存的所有代收付推入的数据   查出的缓存="+set);
		redisBase.delSet(key);;
	}

	/**
	 * 代收付推入的数据订单号进行缓存，分机构缓存，防止数据过大，循环影响性能；如果在今天的缓存找不到就去昨天的缓存查找；如果需要清除缓存可以使用页面手动功能
	 * @param rootInstCd 机构号
	 * @param newOrderNoSet 新的订单号
	 * @return
	 */
	public boolean isPushedGenPay(String rootInstCd,Set<String> newOrderNoSet){
		logger.info("校验代收付是否已经推送过代收付数据传入参数    开始   机构号="+rootInstCd+"  传入订单号="+newOrderNoSet);
		//先查出本机构今天的缓存中是否有此批的订单号
		String today = DateUtils.getyyyyMMdd(Constants.DATE_YYYYMMDD);
		//今天的缓存的键 M000001_ORDER_NOS_PUSH_20151106
		String todayKey = rootInstCd+"_ORDER_NOS_PUSH_"+today;
		logger.info("校验代收付是否已经推送过代收付数据   机构号="+rootInstCd+"  today="+today+"缓存数据的key="+todayKey);
		Set<String> todaySet = redisBase.getSet(todayKey);
		logger.info("校验代收付是否已经推送过代收付数据   机构号="+rootInstCd+"  查出的这批推送前此机构today="+today+" 缓存的所有订单号="+todaySet);
		if (todaySet==null) { //如果为空，去昨天的的缓存查询
			String yestoday = DateUtils.getTheDayBefore(Constants.DATE_YYYYMMDD,-1);
			String yestodayKey = rootInstCd+"_ORDER_NOS_PUSH_"+yestoday;
			logger.info("校验代收付是否已经推送过代收付数据   机构号="+rootInstCd+"  yestoday="+yestoday+"缓存数据的key="+yestodayKey);
			Set<String> yestodaySet = redisBase.getSet(yestodayKey);
			logger.info("校验代收付是否已经推送过代收付数据   机构号="+rootInstCd+"  查出的这批推送前此机构yestoday="+yestoday+"缓存的所有订单号="+yestodaySet);
			if (yestodaySet==null) {
				//昨天和今天的缓存都没有查出，把新数据缓存
				redisBase.saveOrUpdateSet(todayKey, newOrderNoSet, 20, TimeUnit.MINUTES);
				return false;
			}
			for(String newOrderNo:newOrderNoSet){
				for(String yestodayOrderNo:yestodaySet){
					if (yestodayOrderNo.equals(newOrderNo)) {
						logger.info("校验代收付是否已经推送过代收付数据   机构号="+rootInstCd+"  查出的这批推送前此机构yestoday="+yestoday+" 新的订单号"+newOrderNo+"存在于缓存中");
						return true;
					}
				}
			}
		}else{
			for(String newOrderNo:newOrderNoSet){
				for(String todayOrderNo:todaySet){
					if (todayOrderNo.equals(newOrderNo)) {
						logger.info("校验代收付是否已经推送过代收付数据   机构号="+rootInstCd+"  查出的这批推送前此机构today="+today+" 新的订单号"+newOrderNo+"存在于缓存中");
						return true;
					}
				}
			}
			redisBase.saveOrUpdateSet(todayKey, newOrderNoSet, 20, TimeUnit.MINUTES);
		}
		logger.info("校验代收付是否已经推送过代收付数据传入参数    结束   传入的订单号没有在缓存中   机构号="+rootInstCd+"  传入订单号="+newOrderNoSet);
		return false;
	}

	/**
	 * 去代收付系统查询批量发送的代收付结果处理代收付结果
	 * @param orderNoArray 订单号数组
	 * @param batch	批次号
	 * @return
	 */
	public CommonResponse updateGenResultsByQuyRecAndPaySys(String batch,String[] orderNoArray){
		logger.info("去代收付系统查询批量发送的代收付结果处理代收付结果  batch="+batch+",orderNoArray="+orderNoArray+",Arrays.toString(orderNoArray)="+Arrays.toString(orderNoArray));
		CommonResponse res = new CommonResponse();
		if ((batch==null||"".equals(batch)) && (orderNoArray==null||orderNoArray.length==0)) {
			res.setCode(CodeEnum.ERR_PARAM_NULL.getCode());
			res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
			return res;
		}
		List<GenerationPayment> genPayList = generationPaymentDao.selectByOrderNoAndBatch(batch, orderNoArray);
		logger.info("去代收付系统查询批量发送的代收付结果处理代收付结果  根据条件在账户查出的数据的个数="+genPayList.size());
		if (genPayList.size()==0) {
			res.setCode(CodeEnum.ERR_DATA_NO_RESULT.getCode());
			res.setMsg(CodeEnum.ERR_DATA_NO_RESULT.getMessage());
			return res;
		}
		for (GenerationPayment genPay:genPayList) {
			if (genPay.getSendType()!=SettleConstants.GEN_SEND_TYPE_SENT) {
				res.setCode(CodeEnum.ERR_DATA_NOT_NORMAL.getCode());
				res.setMsg("去代收付系统查询批量发送的代收付结果处理代收付结果 订单号="+genPay.getOrderNo()+" SEND_TYPE="+genPay.getSendType()+" 不是已发送状态");
				return res;
			}
		}
		//调用代收付系统
		OrderDetails orderDetails = null;
		try {
			orderDetails = crpsApiService.findDetailsByBatchOrOrderNos(batch, orderNoArray, CrpsConstants.BUSSINESS_TYPE_NON_REAL_TIME);//(批次号，订单号数组，实时标志)
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("去代收付系统查询批量发送的代收付结果处理代收付结果  调用代收付系统异常 =====");
			res.setCode(CodeEnum.ERR_CRPS_CALL_ERROR.getCode());
			res.setMsg(CodeEnum.ERR_CRPS_CALL_ERROR.getMessage());
			return res;
		}
		logger.info("去代收付系统查询批量发送的代收付结果处理代收付结果   代收付返回的 ==OrderDetails==="+orderDetails);
		List<OrderDetail> orderDeList = null;
		if (orderDetails == null || orderDetails.getOrderDetails()==null ||orderDetails.getOrderDetails().size()==0) {
			res.setCode(CodeEnum.ERR_CRPS_NO_RESULT.getCode());
			res.setMsg(CodeEnum.ERR_CRPS_NO_RESULT.getMessage());
			orderDeList  = new ArrayList<OrderDetail>();
		}else{
			orderDeList = orderDetails.getOrderDetails();
		}
		logger.info("去代收付系统查询批量发送的代收付结果处理代收付结果    代收付返回的 ==OrderDetails的个数="+orderDeList.size());
		if (orderDeList.size()==0) {
			res.setCode(CodeEnum.ERR_CRPS_NO_RESULT.getCode());
			res.setMsg(CodeEnum.ERR_CRPS_NO_RESULT.getMessage());
			return res;
		}
		List<OrderDetail> orderDeSuccessList = new ArrayList<OrderDetail>();
		List<OrderDetail> orderDeFailList = new ArrayList<OrderDetail>();
		Map<String,Integer> orderNoToStatusMap = new HashMap<String,Integer>();
		for (OrderDetail orderDetail : orderDeList) {
			orderNoToStatusMap.put(orderDetail.getOrderNo(), orderDetail.getStatusId());
			for (GenerationPayment genPay:genPayList) {
				if (genPay.getOrderNo().equals(orderDetail.getOrderNo())) {
					if (orderDetail.getStatusId()==CrpsConstants.STATUS_SUCCESS) {
						orderDeSuccessList.add(orderDetail);
					}else if(orderDetail.getStatusId()==CrpsConstants.STATUS_FAILURE){
						orderDeFailList.add(orderDetail);
					}
					break;
				}
			}
		}
		logger.info("去代收付系统查询   代收付返回的 所有订单及状态值（15：成功，13：失败）="+orderNoToStatusMap);
		logger.info("去代收付系统查询   代收付返回的 成功的个数="+orderDeSuccessList.size()+",失败的个数="+orderDeFailList.size());
		orderDeSuccessList.addAll(orderDeFailList);
		if (orderDeSuccessList.size()==0) {
			res.setCode(CodeEnum.ERR_DATA_NOT_FINAL_STATE.getCode());
			res.setMsg(CodeEnum.ERR_DATA_NOT_FINAL_STATE.getMessage());
			return res;
		}
		return getGenerationPaymentList(orderDeSuccessList,1);
	}
	
	/**
	 * 手动处理代收付推入的数据缓存
	 * @param key
	 */
	public  CommonResponse handleRecAndPayCachePushed(String batch){
		logger.info("手动处理代收付推入的数据缓存   根据batch="+batch);
		CommonResponse res = new CommonResponse();
		if (batch==null || "".equals(batch)) {
			res.setCode(CodeEnum.ERR_PARAM_NULL.getCode());
			res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
			return res;
		}
		List<OrderDetail> orderDetailList  = redisBase.getList(batch);
		logger.info("手动处理代收付推入的数据缓存   根据key="+batch+" 查出的数据orderDetailList="+orderDetailList);
		if (orderDetailList==null || orderDetailList.size()==0) {
			res.setCode(CodeEnum.ERR_DATA_NO_RESULT.getCode());
			res.setMsg(CodeEnum.ERR_DATA_NO_RESULT.getMessage());
			return res;
		}
		try {
			res = getGenerationPaymentList(orderDetailList,0);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("手动处理处理缓存中的代收付数据   处理key=" + batch + " 时发生异常      "+e.getMessage());
			res.setCode(CodeEnum.FAILURE.getCode());
			res.setMsg("手动处理处理缓存中的代收付数据   处理key=" + batch + " 时发生异常      "+e.getMessage());
			return res;
		}
		if (!CodeEnum.SUCCESS.getCode().equals(res.getCode())) {
			logger.info("手动处理处理缓存中的代收付数据   处理key=" + batch + " 时处理失败");
			res.setCode(CodeEnum.FAILURE.getCode());
			res.setMsg("手动处理处理缓存中的代收付数据   处理key=" + batch + " 时处理失败      ");
			return res;
		}
		redisBase.delSet(batch);
		logger.info("清除缓存后   key=" + batch + " 的缓存数据==" + redisBase.getSet(batch));
		redisBase.saveOrUpdateStrSet(RedisConstants.GENERATIONPAYMENT_PUSH, batch, 7L, TimeUnit.DAYS, true);
		return res;
	}
	
//	@Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED) 
//	public CommonResponse getGenPaymentList(List<OrderDetail> orderDetailList,int judge){
//		return getGenerationPaymentList(orderDetailList,judge);
//	}
	
	/**
	 * 解析代收付结果，将结果入库
	 * judge 1:需要校验订单号是否重复    0：不需要
	 */
	private CommonResponse getGenerationPaymentList(List<OrderDetail> orderDetailList,int judge){
		CommonResponse res = new CommonResponse();
		if (orderDetailList == null || orderDetailList.isEmpty()) {
			res.setCode(CodeEnum.ERR_PARAM_NULL.getCode());
			return res;
		}
		logger.info("--------处理代收付返回结果数据开始------orderDetailList.size()==" + orderDetailList.size());
		//代收付处理成功的
		List<GenerationPayment> generationList = new ArrayList<GenerationPayment>();
		//代收付处理失败的
		List<GenerationPayment> generationFailList = new ArrayList<GenerationPayment>();
		//代收付传回的所有的订单编号（用于查询数据进行校验）
		String[] orderNoArray = new String[orderDetailList.size()];
		Set<String> newOrderNoSet = new HashSet<String>();
		for (int m = 0; m < orderDetailList.size(); m++) {
			OrderDetail orderDetail = orderDetailList.get(m);
			GenerationPayment generationPayment = new GenerationPayment();
			if (orderDetail.getStatusId() == 15) {
				generationPayment.setOrderNo(orderDetail.getOrderNo());
				generationPayment.setSendType(SettleConstants.SEND_NORMAL);
				generationList.add(generationPayment);
			} else if (orderDetail.getStatusId() == 13) {
				generationPayment.setOrderNo(orderDetail.getOrderNo());
				generationPayment.setSendType(SettleConstants.SEND_DEFEAT);
				generationPayment.setErrorCode(orderDetail.getRetCode() + ":" + orderDetail.getErrMsg());
				generationFailList.add(generationPayment);
			}
			orderNoArray[m] = orderDetail.getOrderNo();
			newOrderNoSet.add(orderDetail.getOrderNo());
		}
		logger.info("--judge="+judge+"----代收付发送的所有---orderNoArray.length=="+orderNoArray.length+"  传入的订单号newOrderNoSet="+newOrderNoSet);
		if (isPushedGenPay(orderDetailList.get(0).getRootInstCd(), newOrderNoSet) && judge==1) {
			res.setCode(CodeEnum.ERR_CRPS_PUSH_REPEAT.getCode());
			logger.info(CodeEnum.ERR_CRPS_PUSH_REPEAT.getMessage());
			return res;
		}
		logger.info("--------代收付返回正常结果个数---generationList.size()==" + generationList.size());
		logger.info("--------代收付返回失败结果个数---generationFailList.size()==" + generationFailList.size());
		generationList.addAll(generationFailList);
		try {
			generationPaymentManager.batchUpdateByOrderNoRootInstCd(generationList);
		} catch (Exception e2) {
			e2.printStackTrace();
			logger.error(e2.getMessage());
			logger.error("--根据代收付返回结果更新数据库异常");
		}
		List<GenerationPayment> genList = null;
		try {
			genList = generationPaymentManager.selectByOrderNo(orderNoArray);
		} catch (Exception e2) {
			e2.printStackTrace();
			logger.error(e2.getMessage());
			logger.error("--根据代收付返回结果查询数据库异常");
		}
		logger.info("--------根据代收付系统的参数从数据库查出的代收付数据---genList.size()==" + genList.size());
		if (genList.size() == 0) {
			res.setCode(CodeEnum.ERR_DATA_NO_RESULT.getCode());
			return res;
		}
		//把代收付结果更新
		Map<String, String> rtnMapUp = null;
		try {
			rtnMapUp = settlementServiceThr.updateRecAndPayResults(genList);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("代收付更新错误....");
			res.setCode(CodeEnum.ERR_DATABASE_CALL_ERROR.getCode());
			return res;
		}
		logger.info("把代收付结果更新   rtnMapUp=="+rtnMapUp);
		if (!"0000".equals(rtnMapUp.get("errCode"))) {
			RkylinMailUtil.sendMail("代收付通知接口",
					"代收付更新错误：" + rtnMapUp.get("errCode") + ":" + rtnMapUp.get("errMsg"), "21401233@qq.com");
			logger.error("代收付更新错误：" + rtnMapUp.get("errCode") + ":" + rtnMapUp.get("errMsg"));
			res.setCode(CodeEnum.ERR_DATABASE_CALL_ERROR.getCode());
			return res;
		}
		//转账到贷方,返回目的账户的信息
		TransOrderInfoResponse listRes =  transferToRepay(genList);
		if (CodeEnum.FAILURE.getCode().equals(res.getCode())) {
			return res;
		}
		//发起代付
		Map<String, TransOrderInfo> withHoldList = listRes.getStrToOrderMap();
		if (withHoldList!=null && withHoldList.size()!=0) {
			int failureNum = 0;
			for (TransOrderInfo withHoldTransOrderInfo:withHoldList.values()) {  
				Date date = DateUtils.getSysDate(Constants.DATE_FORMAT_YYYYMMDDHHMMSS);
				withHoldTransOrderInfo.setOrderDate(date);
				withHoldTransOrderInfo.setRequestTime(date);
				ErrorResponse errorResponse =paymentAccountService.withhold(withHoldTransOrderInfo, withHoldTransOrderInfo.getProductIdd());
				if (!errorResponse.isIs_success()) {
					failureNum++;
				}
			}
			if (failureNum!=0) {
				res.setCode(CodeEnum.FAILURE.getCode());
				res.setMsg("贷款还款   向贷款机构发起代付时，有失败的，失败的个数="+failureNum);
				return res;
			}	
		}
		// ********特殊处理：学生还款给课栈 成功后 ， 发起课栈代付交易给P2P 20150603 start
//		Map<String, String> rtnMapP2P = null;
//		try {
//			rtnMapP2P = settlementServiceThr.withholdToP2P(genList);
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("--------getGenerationPaymentList---settlementServiceThr.withholdToP2P()---系统错误！");
//			res.setCode(CodeEnum.ERR_DATABASE_CALL_ERROR.getCode());
//			return res;
//		}
//		if (rtnMapP2P.size()!=0 && !"P0".equals(rtnMapP2P.get("errCode"))) {
//			logger.error(
//					"学生还款给课栈 成功后，发起课栈代付交易给P2P错误：" + rtnMapP2P.get("errCode") + ":" + rtnMapP2P.get("errMsg"));
//			res.setCode(CodeEnum.ERR_DATABASE_CALL_ERROR.getCode());
//			return res;
//		}
		// 调用晶晶的存储过程把成功的代收付过渡至历史表
		logger.info("调用存储过程把成功的代收付数据过渡至历史表");
		Map<String, String> param = new HashMap<String, String>();
		super.getSqlSession().selectList("MyBatisMap.setgeneration", param);
		logger.info("=============MyBatisMap.setgeneration=======param===" + param);
		if (null == param || !String.valueOf(param.get("on_err_code")).equals("0")) {
			logger.error("维护代付表历史数据失败！");
			res.setCode(CodeEnum.ERR_DATABASE_CALL_ERROR.getCode());
			return res;
		}
		return res;
	}
	
	/**
	 * 转账给放款方（代收付返回结果后）
	 * @param genList
	 * @return
	 */
	private TransOrderInfoResponse transferToRepay(List<GenerationPayment> genList){
		TransOrderInfoResponse res = new TransOrderInfoResponse();
		List<GenerationPayment> genPaymentList = new ArrayList<GenerationPayment>();
		for (GenerationPayment generationPayment : genList) {
			if (generationPayment.getSendType()!=SettleConstants.SEND_NORMAL) {
				continue;
			}
			if (generationPayment.getOrderType()!=SettleConstants.ORDER_LOAN_REPAYMENT) {
				continue;
			}
			if (generationPayment.getStatusId()!=3) {
				continue;
			}
			genPaymentList.add(generationPayment);
		}
		logger.info("代收付结果返回的还款的个数="+genPaymentList.size());
		if (genPaymentList.size()==0) {
			return res;
		}
		List<TransOrderInfo> transOrderInfoList = transOrderInfoDao.selectByGenId(genPaymentList);
		logger.info("查出的计息系统发起的关于贷款还款代收的订单个数="+transOrderInfoList.size());
		if (transOrderInfoList.size()==0) {
			res.setCode(CodeEnum.FAILURE.getCode());
			res.setMsg("查出的计息系统发起的关于贷款还款代收的订单个数为0");
			return res;
		}
		Set<String> finAccIdSet = new HashSet<String>(); 
		for (TransOrderInfo transOrderInfo : transOrderInfoList) {
			finAccIdSet.add(transOrderInfo.getInterMerchantCode());
		}
		String[] finAccIdArray =  finAccIdSet.toArray(new String[0]);
		//查出转账时收款方的账户信息
		List<FinanaceAccount> finAccList = finanaceAccountDao.selectByFinAccountId(finAccIdArray);
		if (finAccList.size()==0) {
			res.setCode(CodeEnum.FAILURE.getCode());
			res.setMsg("没有查到转入方账户信息");
			return res;
		}
		OrderAuxiliary orderAuxiliary = new OrderAuxiliary();
		List<Integer> failList = new ArrayList<Integer>();
		//收款方userId和发起代付的交易对象
		Map<String,TransOrderInfo> intoIdToOrderMap = new HashMap<String,TransOrderInfo>();
		TransOrderInfo withholdTransOrderInfo = null; //发起代付交易的对象
		Set<String> genSet = new HashSet<String>(); //生成随机数临时放置，放置重复
		for (TransOrderInfo transOrderInfo : transOrderInfoList) {
			Integer requestId = transOrderInfo.getRequestId();
			transOrderInfo.setRequestId(null);
			transOrderInfo.setFuncCode(TransCodeConst.ADJUST_ACCOUNT_AMOUNT);
			Date date = DateUtils.getSysDate(Constants.DATE_FORMAT_YYYYMMDDHHMMSS);
			transOrderInfo.setOrderDate(date);
			transOrderInfo.setOrderNo(CommUtil.getGenerateNum(genSet,5)+TransCodeConst.ADJUST_ACCOUNT_AMOUNT);
			transOrderInfo.setRequestTime(date);
			transOrderInfo.setStatus(TransCodeConst.TRANS_STATUS_NORMAL);
			transOrderInfo.setUserFee(0L);
			orderAuxiliary.setProductQAA(transOrderInfo.getProductIdd());
			String productQAB = null;
			String recUserId = null;
			String rootInstCd = null;
			String finAccountId = transOrderInfo.getInterMerchantCode();
			for (FinanaceAccount finAccount:finAccList) {
				if (finAccount.getFinAccountId().equals(finAccountId)) {
					productQAB = finAccount.getGroupManage();
					recUserId = finAccount.getAccountRelateId();
					rootInstCd = finAccount.getRootInstCd();
					orderAuxiliary.setProductQAB(productQAB);
					break;
				}
			}
			transOrderInfo.setUserIpAddress("127.0.0.1");
			transOrderInfo.setBusiTypeId(null);
			transOrderInfo.setCreatedTime(null);
			transOrderInfo.setUpdatedTime(null);
			transOrderInfo.setRemark("贷款还款,代收付返回结果,内部转账");
			String result = paymentAccountService.transferInCommonUse(transOrderInfo, orderAuxiliary);
			if (!"ok".equals(result)) {
				failList.add(requestId);
				continue;
			}
			withholdTransOrderInfo=intoIdToOrderMap.get(finAccountId);
			if (withholdTransOrderInfo==null) {
				withholdTransOrderInfo =transOrderInfo;
				withholdTransOrderInfo.setUserId(recUserId);
				withholdTransOrderInfo.setFuncCode(TransCodeConst.PAYMENT_WITHHOLD);
				withholdTransOrderInfo.setMerchantCode(rootInstCd);
				withholdTransOrderInfo.setOrderNo(CommUtil.getGenerateNum(genSet,5)+TransCodeConst.PAYMENT_WITHHOLD);
				withholdTransOrderInfo.setRemark("代收付返回结果，发起代付还款");
				withholdTransOrderInfo.setProductIdd(productQAB);
			}else{
				Long amount = withholdTransOrderInfo.getAmount()+transOrderInfo.getAmount();
				withholdTransOrderInfo.setAmount(amount);
				withholdTransOrderInfo.setOrderAmount(amount);
			}
			withholdTransOrderInfo.setInterMerchantCode(recUserId);
			withholdTransOrderInfo.setRequestId(null);
			withholdTransOrderInfo.setErrorCode(null);
			withholdTransOrderInfo.setErrorMsg(null);
			intoIdToOrderMap.put(finAccountId, withholdTransOrderInfo);
		}
		if (failList.size()==transOrderInfoList.size()) {
			res.setCode(CodeEnum.FAILURE.getCode());
			res.setMsg("贷款还款返回结果转账时失败的requestId是:"+failList);
			return res;
		}
		if (failList.size()!=0) {
			res.setMsg("贷款还款返回结果转账时   全部失败    失败的requestId是:"+failList);
		}
		res.setStrToOrderMap(intoIdToOrderMap);
		return res;
	}
	
	/**
	 * 根据传入的订单数据，封装要传入代收付系统的数据OrderDetail
	 * @param transOrderInfo
	 * @return
	 */
	private OrderDetail getOrderDetailByTransOrderInfo(TransOrderInfo transOrderInfo){
		OrderDetail orderDetail = new OrderDetail(); //CommUtil
//		String batch=settlementLogic.getBatchNo(transOrderInfo.getAccountDate(),
//				SettleConstants.ROP_RECEIVE_BATCH_CODE,transOrderInfo.getMerchantCode());
		String batch = CommUtil.getGenerateNum(4);
		logger.info("====实时代收   机构号=="+transOrderInfo.getMerchantCode()+",订单号 == "+transOrderInfo.getOrderNo()+",批次号=="+batch);
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
		orderDetail.setAccountName(accountInfo.getAccountRealName());
		String accountProp = "10";
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
		//如果returnCode不是100000可能是代收付系统调用多渠道的额异常，具体看errorMsg
		if("4444".equals(returnCode)){
			logger.info("实时代收        外部系统异常      用户: "+transOrderInfo.getUserId()+" 机构码： "+transOrderInfo.getMerchantCode());
			transOrderInfo.setErrorMsg(returnCode+":"+errorMsg);
			return TransCodeConst.TRANS_STATUS_PROCESSING+"";  
		}
		if(!"100000".equals(returnCode)){
			logger.info("实时代收  (可能)校验性失败  用户: "+transOrderInfo.getUserId()+" 机构码： "+transOrderInfo.getMerchantCode());
			transOrderInfo.setErrorMsg(statusId+":"+errorMsg);
			return CodeEnum.ERR_PARAM_ABNORMAL.getCode(); 
		}
		//如果是处理中
		if (statusId==12) {
			logger.info("实时代收  处理中  用户: "+transOrderInfo.getUserId()+" 机构码： "+transOrderInfo.getMerchantCode());
			transOrderInfo.setErrorMsg(statusId+":"+errorMsg);
			return TransCodeConst.TRANS_STATUS_PROCESSING+""; 
		}
		//失败
		if (statusId==13) {
			logger.info("实时代收  失败  用户: "+transOrderInfo.getUserId()+" 机构码： "+transOrderInfo.getMerchantCode());
			transOrderInfo.setErrorMsg(statusId+":"+errorMsg);
			return CodeEnum.FAILURE.getCode(); 
		}
		return  CodeEnum.SUCCESS.getCode();
	}
	
	/**
	 * 手动提交发送失败(处于临时状态，sendType为3的)的代收交易到代收付系统
	 * @param batch 批次号
	 * @return
	 */
	public CommonResponse submitToRecAndPaySysManual(String batch){
		logger.info("--准备将提交失败(处于临时状态，sendType为3的)的代收交易提交到代收付系统---------手动----------批次号=="+batch);
		CommonResponse res = new CommonResponse();
		if(batch==null || "".equals(batch)){
			res.setCode(CodeEnum.ERR_PARAM_NULL.getCode());
			res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
			return res;
		}
		GenerationPaymentQuery query = new GenerationPaymentQuery();
		query.setProcessResult(batch);
		query.setSendType(3);
		List<GenerationPayment> genPayList=generationPaymentManager.queryList(query);
		logger.info("--将提交失败的代收交易提交到代收付系统---------手动----------查出的要发送到代收付系统的个数=="+genPayList.size());
		if(genPayList.size()==0){
			res.setCode(CodeEnum.ERR_DATA_NO_RESULT.getCode());
			res.setMsg(CodeEnum.ERR_DATA_NO_RESULT.getMessage());
			return res;
		}
		//只发送已经发送但发送失败的数据
//		for (GenerationPayment generationPayment : genPayList) {
//			if (generationPayment.getSendType()!=3) {
//				res.setCode(CodeEnum.ERR_DATA_NOT_NORMAL.getCode());
//				res.setMsg(CodeEnum.ERR_DATA_NOT_NORMAL.getMessage());
//				return res;
//			}
//		}
		//某一批发送到代收付的订单数据
		List<OrderDetail> orderDeBatchList =  new ArrayList<OrderDetail>();
		//所有发送到代收付成功的代收付交易
		List<GenerationPayment> genPaySuccessAllList = new ArrayList<GenerationPayment>();
		OrderDetail orderDeBatch = null;
		String retCode = null;
		String errorMsg = null;
		//每limitCount条生成一个批次号，并发到代收付系统（如果某一批发送失败，继续发送下一批）
		for (int i = 1; i <= genPayList.size(); i++) {
			GenerationPayment generationPayment = genPayList.get(i-1);
			//根据传入的汇总数据，封装成要传入代收付系统的数据OrderDetail
			OrderDetail orderDetail = getOrderDeByTransOrder(generationPayment);
			orderDetail.setAccountDate(generationPayment.getAccountDate());
			generationPayment.setSendType(2);//修改代收付表状态（已发送）
			generationPayment.setUpdatedTime(null);//(防止修改这个字段)
			genPaySuccessAllList.add(generationPayment);
			orderDetail.setRequestNo(batch);
			orderDeBatchList.add(orderDetail);
			//所有数据组装完成后再去调用代收付系统，否则继续循环组装；
			if (i!=genPayList.size())  {
				continue; 
			}
			//调用代收付系统接口
			try{
				orderDeBatch = crpsApiService.transDetailFromOrderList(orderDeBatchList);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				logger.error("上传到代收付系统---业务类型："+generationPayment.getOrderType()+",批次号："+batch+",机构号-"+generationPayment.getRootInstCd()+"--调用代收付系统异常！");
				res.setCode(CodeEnum.ERR_CRPS_CALL_ERROR.getCode());
				res.setMsg(CodeEnum.ERR_CRPS_CALL_ERROR.getMessage());
				return res;
			}
			retCode = orderDeBatch.getRetCode();
			errorMsg = orderDeBatch.getErrMsg();
			logger.info("上传到代收付系统--一批结束--业务类型="+generationPayment.getOrderType()+",机构号="+generationPayment.getRootInstCd()+",这一批某个订单order_no="+orderDeBatch.getOrderNo()+",retCode="+retCode+",errorMsg="+errorMsg);
			//处理失败，继续处理剩下的数据
			if (!"100000".equals(retCode)) {
				res.setCode(retCode);
				res.setMsg(errorMsg);
				genPaySuccessAllList.clear();
				break;
			}
		}
		if (!"100000".equals(retCode)) {
			for (GenerationPayment generationPayment:genPayList) {
				if (generationPayment.getOrderNo().equals(orderDeBatch.getOrderNo())) {
					generationPayment.setSendType(4);//发送失败的
					generationPayment.setErrorCode(retCode+":"+errorMsg);
					genPaySuccessAllList.add(generationPayment);
					break;
				}				
			}			
		}
		generationPaymentManager.batchUpdate(genPaySuccessAllList);
		logger.info("--手动提交代收付系统----结束---业务类型="+genPayList.get(0).getOrderType()+"([1,3,5,7]:6 ; [2,6]:7),机构号="+genPayList.get(0).getRootInstCd());
		return res;
	}	
		
		
	
	/**
	 * 代收付交易提交到代收付系统
	 * @param type 6:代收，7：代付
	 * @param rootInstCd 机构号
	 * @param  accDate  系统账期
	 * @param  accountDate  不同机构处理后的账期
	 */
	public void submitToRecAndPaySys(int type,String rootInstCd,String accDate,Date accountDate){
		logger.info("--准备批量提交代收付系统---业务类型="+type+"(6:代收，7：代付),机构号="+rootInstCd+",系统账期="+accDate+"---机构账期="+accountDate);
		GenerationPaymentQuery query=new GenerationPaymentQuery();
		query.setStatusId(Integer.parseInt(BaseConstants.ACCOUNT_STATUS_OK));
		query.setRootInstCd(rootInstCd);
		query.setOrderType(type);
		query.setAccountDate(accountDate);
		List<GenerationPayment> generationPaymentList=generationPaymentManager.queryListByOrderType(query);
		logger.info("---业务类型="+type+"(6:代收，7：代付),机构号="+rootInstCd+",查出的要发送到代收付系统的个数="+generationPaymentList.size());
		if(generationPaymentList.size()==0){
			return;
		}
		//先生成一个批次号，少于limitCount的时候直接使用;或者大于limitCount的时候第一批limitCount使用
		String batch = getBatchNo(type, rootInstCd, accDate, null);
		//所有发送到代收付成功的代收付交易
		List<GenerationPayment> genPaySuccessAllList = new ArrayList<GenerationPayment>();
		//查出的所有数据，按批发送，发送到代收付失败时，所有数据集
		List<GenerationPayment> genPayTempAllList = new ArrayList<GenerationPayment>();
		//某一批发送到代收付成功的代收付交易
		List<GenerationPayment> genPaySuccessList = new ArrayList<GenerationPayment>();
		//某一批发送到代收付的订单数据
		List<OrderDetail> orderDeBatchList =  new ArrayList<OrderDetail>();
		//发送到代收付所有失败的数据集
		List<OrderDetail> orderDeFailAllList =  new ArrayList<OrderDetail>();
		OrderDetail orderDeBatch = null;
		int limitCount = settlementServiceThr.getBatchLimit();
		logger.info("--批量提交代收付系统---业务类型="+type+"(6:代收，7：代付),机构号="+rootInstCd+",一个批次提交的上限数量="+limitCount);
		boolean lastGreaterLimit = false;//剩余的未发送的数据是否大于一个批次？
		//每limitCount条生成一个批次号，并发到代收付系统（如果某一批发送失败，继续发送下一批）
		for (int i = 1; i <= generationPaymentList.size(); i++) {
			GenerationPayment generationPayment = generationPaymentList.get(i-1);
			//根据传入的汇总数据，封装成要传入代收付系统的数据OrderDetail
			OrderDetail orderDetail = getOrderDeByTransOrder(generationPayment);
			orderDetail.setAccountDate(accountDate);
			generationPayment.setSendType(2);//修改代收付表状态（已发送）
			generationPayment.setUpdatedTime(null);//(防止修改这个字段)
			//总数据小于limitCount,或数据超过limitCount时将前limitCount条数据直接发送到代收付；
			if (i<=limitCount) {
				generationPayment.setProcessResult(batch);// 批次号
				genPaySuccessList.add(generationPayment);
				orderDetail.setRequestNo(batch);
				orderDeBatchList.add(orderDetail);
				//所有数据组装完成后再去调用代收付系统，否则继续循环组装；
				if ((generationPaymentList.size()<=limitCount && i!= generationPaymentList.size())|| 
					(generationPaymentList.size()>limitCount && i!=limitCount))  {
					continue; 
				}
				//调用代收付系统接口
				logger.info("上传到代收付系统--一批开始--业务类型："+type+",批次号："+batch+",机构号-"+rootInstCd+"--第"+i+"条数据");
				try{
					orderDeBatch = crpsApiService.transDetailFromOrderList(orderDeBatchList);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(e.getMessage());
					logger.error("上传到代收付系统---业务类型："+type+",批次号："+batch+",机构号-"+rootInstCd+"--调用代收付系统异常！");
					genPayTempAllList.addAll(genPaySuccessList);
					genPaySuccessList.clear();
					continue;
				}finally {
					orderDeBatchList.clear();
					batch = null;
				}
				String retCode = orderDeBatch.getRetCode();
				String errorMsg = orderDeBatch.getErrMsg();
				logger.info("上传到代收付系统--一批结束--业务类型="+type+",机构号="+rootInstCd+",这一批某个订单order_no="+orderDeBatch.getOrderNo()+",retCode="+retCode+",errorMsg="+errorMsg);
				//处理失败，继续处理剩下的数据
				if (!"100000".equals(retCode)) {
					genPayTempAllList.addAll(genPaySuccessList);
					orderDeFailAllList.add(orderDeBatch);
					genPaySuccessList.clear();
					continue;
				}
				genPaySuccessAllList.addAll(genPaySuccessList);
				genPaySuccessList.clear();
				continue;
			}
			//剩余的未发送的数据小于等于一个批次（例：一批200,共201,399,400）
			if(!lastGreaterLimit && generationPaymentList.size()-i<limitCount){
				batch = getBatchNo(type, rootInstCd, accDate, batch);
				generationPayment.setProcessResult(batch);// 批次号
				genPaySuccessList.add(generationPayment);
				orderDetail.setRequestNo(batch);
				orderDeBatchList.add(orderDetail);
				if (i != generationPaymentList.size()) {
					continue;
				}
				//调用代收付系统接口
				logger.info("上传到代收付系统--一批开始--业务类型："+type+",批次号："+batch+",机构号-"+rootInstCd+"--第"+i+"条数据");
				try{
					orderDeBatch = crpsApiService.transDetailFromOrderList(orderDeBatchList);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(e.getMessage());
					logger.error("上传到代收付系统---业务类型："+type+",批次号："+batch+",机构号-"+rootInstCd+"--调用代收付系统异常！");
					genPayTempAllList.addAll(genPaySuccessList);
					break;
				}
				String retCode = orderDeBatch.getRetCode();
				String errorMsg = orderDeBatch.getErrMsg();
				logger.info("上传到代收付系统--一批结束--业务类型="+type+",机构号="+rootInstCd+",这一批某个订单order_no="+orderDeBatch.getOrderNo()+",retCode="+retCode+",errorMsg="+errorMsg);
				//处理失败，直接返回
				if (!"100000".equals(retCode)) {
					genPayTempAllList.addAll(genPaySuccessList);
					orderDeFailAllList.add(orderDeBatch);
					break;
				}
				genPaySuccessAllList.addAll(genPaySuccessList);
				break;
			}else if(generationPaymentList.size()-i>=limitCount){ //剩余的未发送的数据大于一个批次（例：一批200,共401,599）
				lastGreaterLimit = true;
			}
			//剩余未发送的大于limitCount，发送limitCount条  
			if (!lastGreaterLimit) {
				continue;
			}
			batch = getBatchNo(type, rootInstCd, accDate, batch);
			generationPayment.setProcessResult(batch);// 批次号
			genPaySuccessList.add(generationPayment);
			orderDetail.setRequestNo(batch);
			orderDeBatchList.add(orderDetail);
			if (i%limitCount!=0) {
				continue;
			}
			//调用代收付系统接口
			logger.info("上传到代收付系统--一批开始--业务类型："+type+",批次号："+batch+",机构号-"+rootInstCd+"--第"+i+"条数据");
			try{
				orderDeBatch = crpsApiService.transDetailFromOrderList(orderDeBatchList);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				logger.error("上传到代收付系统---业务类型："+type+",批次号："+batch+",机构号-"+rootInstCd+"--调用代收付系统异常！");
				genPayTempAllList.addAll(genPaySuccessList);
				genPaySuccessList.clear();
				continue;
			}finally {
				orderDeBatchList.clear();
				batch = null;
				lastGreaterLimit = false;
			}
			String retCode = orderDeBatch.getRetCode();
			String errorMsg = orderDeBatch.getErrMsg();
			logger.info("上传到代收付系统--一批结束--业务类型="+type+",机构号="+rootInstCd+",这一批某个订单order_no="+orderDeBatch.getOrderNo()+",retCode="+retCode+",errorMsg="+errorMsg);
			//处理失败，直接返回
			if (!"100000".equals(retCode)) {
				genPayTempAllList.addAll(genPaySuccessList);
				orderDeFailAllList.add(orderDeBatch);
				genPaySuccessList.clear();
				continue;
			}
			genPaySuccessAllList.addAll(genPaySuccessList);
			genPaySuccessList.clear();
		}
		logger.info("---业务类型："+type+"---机构号-"+rootInstCd+"--成功发送到代收付系统的数据个数=="+genPaySuccessAllList.size());
		logger.info("---业务类型："+type+"---机构号-"+rootInstCd+"--发送到代收付所有失败的数据集个数(每次提交，代收付提示失败后返回的那条数据集)=="+orderDeFailAllList.size());
		logger.info("---业务类型："+type+"---机构号-"+rootInstCd+"--查出的所有数据，按批发送，发送到代收付失败时，所有数据集个数(整批的数据)=="+genPayTempAllList.size());
		for (GenerationPayment generationPayment : genPayTempAllList) {
			generationPayment.setSendType(3);//发送临时状态
			for(OrderDetail orderDetail:orderDeFailAllList){
				if (generationPayment.getOrderNo().equals(orderDetail.getOrderNo())) {
					generationPayment.setSendType(4);//发送失败的
					generationPayment.setErrorCode(orderDetail.getRetCode()+":"+orderDetail.getErrMsg());
				}				
			}
		}
		genPaySuccessAllList.addAll(genPayTempAllList);
		generationPaymentManager.batchUpdate(genPaySuccessAllList);
		logger.info("--批量提交代收付系统----结束---业务类型="+type+"(6:代收，7：代付),机构号="+rootInstCd);
	}

	/**
	 * 获取批次号
	 * @param type 6：代收，7:代付
	 * @param rootInstCd
	 * @param accDate  系统账期
	 * @param batch
	 * @return
	 */
	private String getBatchNo(int type, String rootInstCd, String accDate, String batch) {
		if (batch == null && type==6) {
			batch=settlementLogic.getBatchNo(DateUtils.getAccountDate(Constants.DATE_FORMAT_YYYYMMDD, accDate),SettleConstants.ROP_RECEIVE_BATCH_CODE,rootInstCd);
		}else if(batch == null && type==7){
			batch=settlementLogic.getBatchNo(DateUtils.getAccountDate(Constants.DATE_FORMAT_YYYYMMDD, accDate),SettleConstants.ROP_PAYMENT_BATCH_CODE,rootInstCd);
		}
		return batch;
	}
	
	/**
	 * //根据传入的汇总数据，封装成要传入代收付系统的数据OrderDetail
	 * @param generationPaymentList
	 * @return
	 */
	private OrderDetail getOrderDeByTransOrder(GenerationPayment generationPayment) {
//		for (GenerationPayment generationPayment:generationPaymentList) {
			OrderDetail orderDetail = new OrderDetail(); 
//			orderDetail.setRequestNo(batch);
			orderDetail.setBussinessCode(generationPayment.getBussinessCode());
			orderDetail.setBussinessType(1);//业务类型ID:0实时;1非实时
			orderDetail.setRootInstCd(generationPayment.getRootInstCd());
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
			int orderrType = 1; //定义此变量仅用于记录日志
			orderDetail.setOrderType(orderrType); //代收
			if (orderType==SettleConstants.ORDER_WITHDRAW || orderType==SettleConstants.ORDER_WITHHOLD) {
				orderrType = 2;
				orderDetail.setOrderType(orderrType); //代付
			}
			orderDetail.setAmount(generationPayment.getAmount());
			orderDetail.setCurrency(generationPayment.getCurrency());
			return orderDetail;
	}
	
	/**
	 * 根据代收付系统查出的实时代收结果修改账户系统
	 * @param orderNoArray
	 */
	public CommonResponse updateRealTimeTransInfoByRecAndPaySys(String[] orderNoArray){
		logger.info("根据代收付系统查出的实时代收结果修改账户系统    开始  ==============orderNoArray==="+orderNoArray);
		CommonResponse res = new CommonResponse();
		if(!settlementServiceThr.isDayEndOk()){
			logger.info("根据代收付系统查出的实时代收结果修改账户系统  日终没有正常结束！");
			res.setCode(CodeEnum.ERR_SYS_DAYEND_NOT_OK.getCode());
			res.setMsg(CodeEnum.ERR_SYS_DAYEND_NOT_OK.getMessage());
			return res;
		}
		//调用代收付系统
		OrderDetails orderDetails = null;
		try {
			orderDetails = crpsApiService.findDetailsByBatchOrOrderNos(null, orderNoArray, 0);//(批次号，订单号数组，实时标志)
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("根据代收付系统查出的实时代收结果修改账户系统  调用代收付系统异常 =====");
			res.setCode(CodeEnum.ERR_CRPS_CALL_ERROR.getCode());
			res.setMsg(CodeEnum.ERR_CRPS_CALL_ERROR.getMessage());
			return res;
		}
		logger.info("根据代收付系统查出的实时代收结果修改账户系统   代收付返回的 ==OrderDetails==="+orderDetails);
		List<OrderDetail> orderDeList = null;
		if (orderDetails == null || orderDetails.getOrderDetails()==null ||orderDetails.getOrderDetails().size()==0) {
			logger.info("根据代收付系统查出的实时代收结果修改账户系统   代收付返回的 ==OrderDetails为空");
			res.setCode(CodeEnum.ERR_CRPS_NO_RESULT.getCode());
			res.setMsg(CodeEnum.ERR_CRPS_NO_RESULT.getMessage());
			orderDeList  = new ArrayList<OrderDetail>();
		}else{
			orderDeList = orderDetails.getOrderDetails();
		}
		logger.info("根据代收付系统查出的实时代收结果修改账户系统   代收付返回的 ==OrderDetails的个数="+orderDeList.size());
		boolean exist = false;
		String merchantCode = null;
		List<TransOrderInfo> transOrInfoList = new ArrayList<TransOrderInfo>();
		for(String orderNo:orderNoArray){
			for (OrderDetail orderDe:orderDeList) {
				if (orderDe.getOrderNo().equals(orderNo)) {
					merchantCode = orderDe.getRootInstCd();
					exist = true;
					break;
				}
			}
			//如果订单在代收付系统不存在，修改账户系统的订单状态为失败,并通知订单
			if (!exist) {
				TransOrderInfoQuery query = new TransOrderInfoQuery();
				query.setOrderNo(orderNo);
				query.setMerchantCode(merchantCode);
				List<TransOrderInfo> transOrderInfoList =  transOrderInfoManager.queryList(query);
				if (transOrderInfoList.size()==0) {
					res.setCode(CodeEnum.ERR_TRADE_RECORD_NO_RESULT.getCode());
					res.setMsg("根据订单号="+orderNo+" 在账户系统没有查出实时代收数据");
					return res;
				}
				if (transOrderInfoList.get(0).getStatus()!=TransCodeConst.TRANS_STATUS_PROCESSING) {
					res.setCode(CodeEnum.FAILURE.getCode());
					res.setMsg("订单="+transOrderInfoList.get(0).getOrderNo()+"  不是处理中状态！");
					return res;
				}
				TransOrderInfo order = new TransOrderInfo();
				order.setStatus(TransCodeConst.TRANS_STATUS_INVALIDATION);
				order.setOrderNo(orderNo);
				order.setMerchantCode(merchantCode);
				transOrderInfoDao.updateStatusByOrderNoAndMerCode(order);
				transOrInfoList.add(order);
			}
		}
		List<Map<String,Object>> paramMapList = new ArrayList<Map<String,Object>>();
		Map<String,Object> paramMap = new HashMap<String,Object>();
		for (OrderDetail orderDetail : orderDeList) {
			paramMap.put("orderNo", orderDetail.getOrderNo());
			paramMap.put("merchantCode", orderDetail.getRootInstCd());
			paramMap.put("status", orderDetail.getStatusId());
			TransOrderInfoQuery query = new TransOrderInfoQuery();
			query.setOrderNo(orderDetail.getOrderNo());
			query.setMerchantCode(orderDetail.getRootInstCd());
			List<TransOrderInfo> transOrderInfoList =  transOrderInfoManager.queryList(query);
			logger.info("根据代收付系统查出的实时代收结果修改账户系统,订单号=="+orderDetail.getOrderNo()+",机构号 =="+orderDetail.getRootInstCd()+",status="+orderDetail.getStatusId()+"(代收付返回的，非查询条件) 查出的数据个数="+transOrderInfoList.size());
			if (transOrderInfoList.size()==0) {
				res.setCode(CodeEnum.ERR_TRADE_RECORD_NO_RESULT.getCode());
				res.setMsg("根据订单号="+orderDetail.getOrderNo()+" 在账户系统没有查出实时代收数据");
				return res;
			}
			if (transOrderInfoList.get(0).getStatus()!=TransCodeConst.TRANS_STATUS_PROCESSING) {
				res.setCode(CodeEnum.FAILURE.getCode());
				res.setMsg("订单="+orderDetail.getOrderNo()+"  不是处理中状态！");
				return res;
			}
			TransOrderInfo transOrderInfo = transOrderInfoList.get(0);
			if (orderDetail.getStatusId()==CrpsConstants.STATUS_SUCCESS) { //成功
				//记账，生成流水及修改订单信息
				transOrderInfo.setErrorMsg("");
				ErrorResponse response=paymentAccountService.collectionRealTime(transOrderInfo);
				logger.info("根据代收付系统查出的实时代收结果修改账户系统,订单号=="+orderDetail.getOrderNo()+",机构号 =="+orderDetail.getRootInstCd()+" 生成流水及订单信息 response.isIs_success()="+response.isIs_success()+" msg ="+response.getMsg());
				if(!response.isIs_success()){
					res.setCode(CodeEnum.FAILURE.getCode());
					res.setMsg(response.getMsg());
					return res;
				}
			}else if (orderDetail.getStatusId()==CrpsConstants.STATUS_FAILURE) { //失败
				logger.info("根据代收付系统查出的实时代收结果修改账户系统,订单号=="+orderDetail.getOrderNo()+",机构号 =="+orderDetail.getRootInstCd()+" 代收付返回失败 orderDetail.getRetCode()="+orderDetail.getRetCode()+" orderDetail.getErrMsg() ="+orderDetail.getErrMsg());
				transOrderInfo.setStatus(TransCodeConst.TRANS_STATUS_PAY_FAILED);
//				transOrderInfo.setStatus(TransCodeConst.TRANS_STATUS_INVALIDATION);
				transOrderInfo.setErrorMsg(orderDetail.getRetCode()+":"+orderDetail.getErrMsg());
				transOrderInfoManager.saveTransOrderInfo(transOrderInfo);
				res.setCode(CodeEnum.FAILURE.getCode());
				res.setMsg(orderDetail.getErrMsg());
			}else{
				res.setCode(CodeEnum.FAILURE.getCode());
				res.setMsg("从代收付查出的订单="+orderDetail.getOrderNo()+"  非终态！");
				return res;
			}
			paramMapList.add(paramMap);
			transOrInfoList.add(transOrderInfo);
		}
		if(transOrInfoList.size()==0){
			res.setCode(CodeEnum.FAILURE.getCode());
			res.setMsg("没有需要通知订单系统的！");
			return res;
		}
		logger.info("根据代收付系统查出的实时代收结果修改账户系统  代收付返回的数据=paramMapList=="+paramMapList);
		// 代收付返回成功或失败的订单通知订单系统；
		Map<String,Object> rtnMapUp = null;
    	try {
    		rtnMapUp = updateOrderSysStatus(transOrInfoList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("根据代收付系统查出的实时代收结果修改账户系统   机构号 =="+transOrInfoList.get(0).getMerchantCode()+" 调用订单系统异常");
			res.setCode(CodeEnum.ERR_ORDER_CALL_ERROR.getCode());
			res.setMsg(CodeEnum.ERR_ORDER_CALL_ERROR.getMessage());
			return res;
		}
    	logger.info("根据代收付系统查出的实时代收结果修改账户系统   机构号 =="+transOrInfoList.get(0).getMerchantCode()+" 订单系统返回的数据rtnMapUp="+rtnMapUp);
     	if(!"true".equals(rtnMapUp.get("issuccess"))){
			List<OrderPayment> rtnOrderPayList =  (List<OrderPayment>)rtnMapUp.get("paymentList");
			res.setCode(CodeEnum.FAILURE.getCode());
			for (OrderPayment orderPayment : rtnOrderPayList) {
				if("1".equals(orderPayment.getStatusId())){
					logger.info("根据代收付系统查出的实时代收结果修改账户系统,订单号=="+orderPayment.getPaymentId()+"  在订单系统找不到数据");
					res.setMsg("订单号=="+orderPayment.getPaymentId()+" 在订单系统找不到数据！");
				}else{
					logger.info("根据代收付系统查出的实时代收结果修改账户系统,订单号=="+orderPayment.getPaymentId()+"  订单系统数据状态已更新");
					res.setMsg("订单号=="+orderPayment.getPaymentId()+" 在订单系统数据状态已更新！");
				}	
				return res;
			}
     	}
    	return res;
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
	
	/**
	 * 计息系统查询订单状态（代收付系统是否返回结果）
	 * @param query
	 * @return
	 */
	public TransOrderInfosResponse getTransOrderInfos(TransOrderInfoQuery query){
//		List<TransOrderInfo>
		logger.info("计息系统查询订单状态（代收付系统是否返回结果)   query="+query);
		TransOrderInfosResponse res = new TransOrderInfosResponse();
		if (query==null) {
			res.setCode(CodeEnum.ERR_PARAM_NULL.getCode());
			res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
			return res;
		}
		logger.info("计息系统查询订单状态（代收付系统是否返回结果)   orderNo="+query.getOrderNo()+",merchantCode="+query.getMerchantCode());
		if (query.getOrderNo()==null||"".equals(query.getOrderNo())||query.getMerchantCode()==null||"".equals(query.getMerchantCode())){
			res.setCode(CodeEnum.ERR_PARAM_NULL.getCode());
			res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
			return res;
		}
		List<TransOrderInfo> transOrderInfoList = transOrderInfoDao.selectByExample(query);
		logger.info("计息系统查询订单状态（代收付系统是否返回结果)   擦出的订单个数="+transOrderInfoList.size());
		if (transOrderInfoList.size()==0) {
			res.setCode(CodeEnum.ERR_DATA_NO_RESULT.getCode());
			res.setMsg(CodeEnum.ERR_DATA_NO_RESULT.getMessage());
			return res;
		}
		res.setTransOrderInfoList(transOrderInfoList);
		return res;
	}
	/**
	 * 40143交易发送到多渠道
	 * Discription:
	 * @return CommonResponse
	 * @author Achilles
	 * @since 2016年7月27日
	 */
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public  CommonResponse submitToGateRouterTransferBatch(String accountDate,Integer sendType,String protocol){
        CommonResponse res = new CommonResponse();
        res.setCode(CodeEnum.FAILURE.getCode());
        if (CommUtil.isEmp(protocol)) {
            res.setMsg("协议为空!");
            return res;
        }
        GenerationPaymentQuery query=new GenerationPaymentQuery();
        query.setStatusId(Integer.parseInt(BaseConstants.ACCOUNT_STATUS_OK));
        query.setRootInstCd(protocol);
        if (accountDate==null||"".equals(accountDate)) {
            query.setAccountDate(DateUtils.getDate(getAccountDate(), Constants.DATE_FORMAT_YYYYMMDD));
        }else{
            query.setAccountDate(DateUtils.getDate(accountDate, Constants.DATE_FORMAT_YYYYMMDD));
        }
        List<GenerationPayment> generationPaymentList=null;
        if (sendType!=null) {
            query.setSendType(sendType);
            generationPaymentList=generationPaymentManager.queryList(query); 
        }else{
            generationPaymentList=generationPaymentManager.queryListByOrderType(query); 
        }
	    if (generationPaymentList.size()==0) {
	        res.setMsg("没有查出要发送到多渠道的数据!");
	        return res;
        }
	    BatchPaymentDto dto = new BatchPaymentDto();
	    dto.setSysNo(GatewayConstants.SYS_NO_ACCOUNT_003);
	    dto.setTransCode("16012");
	    dto.setOrgNo(protocol);
        dto.setBusiCode("16011");// 业务编码
        dto.setSignType(1);// 签名类型 固定值1 即MD5
        ParameterInfo parameterInfo = parameterInfoService.getParameterInfoByParaCode(GatewayConstants.GATEROUTER_MD5_KEY);
        String md5Key = parameterInfo.getParameterValue();
        dto.setSignMsg(dto.sign(md5Key));
        
        String batchNo = "U"+CommUtil.getGenerateNum(2);
        if (batchNo.length()>18) {
            batchNo=batchNo.substring(0, 18);
        }
        batchNo+="_GRTRB"; //GateRouterTransferBach
	    dto.setBatchNo(batchNo);
	    dto.setTotalAcount(generationPaymentList.size());
	    dto.setCurrency(BaseConstants.CURRENCY_CNY);
//	    dto.setAccountNo("");
	    dto.setAccountType(GatewayConstants.ACCOUNT_TYPE_PUB);
//	    dto.setAccountName("");
	    dto.setSettleFlag(1);//普通
	    List<PaymentDto> paymentDtoList = new ArrayList<PaymentDto>();
	    long sumAmount = 0l;
	    StringBuffer sb = new StringBuffer("批量转账入参明细字段:");
	    PaymentDto paymentDto = null;
	    for (GenerationPayment generationPayment : generationPaymentList) {
	        paymentDto = new PaymentDto();
	        generationPayment.setProcessResult(batchNo);
	        paymentDto.setSysNo(GatewayConstants.SYS_NO_ACCOUNT_003);
	        paymentDto.setTransCode("16012");
	        paymentDto.setOrgNo(protocol);
	        paymentDto.setBusiCode("16011");// 业务编码
	        paymentDto.setSignType(1);// 签名类型 固定值1 即MD5
	        paymentDto.setSignMsg(paymentDto.sign(md5Key));
	        
	        paymentDto.setTransNo(generationPayment.getOrderNo());
	        paymentDto.setPayAmount(generationPayment.getAmount());
	        paymentDto.setCurrency(BaseConstants.CURRENCY_CNY);
//	        paymentDto.setPayerAccountNo(payerAccountNo);
//	        paymentDto.setPayerAccountName(payerAccountName);
	        paymentDto.setReceiverAccountNo(generationPayment.getAccountNo());
	        paymentDto.setReceiverAccountName(generationPayment.getAccountName());
	        paymentDto.setReceiverAccountType(GatewayConstants.ACCOUNT_TYPE_PRI);
	        if (Constants.ACCOUNT_PROPERTY_PUBLIC.equals(generationPayment.getAccountProperty())) {
	            paymentDto.setReceiverAccountType(GatewayConstants.ACCOUNT_TYPE_PUB);
            }
	        paymentDto.setReceiverBankNo(generationPayment.getPayBankCode());
	        paymentDto.setReceiverBankName(generationPayment.getOpenBankName());
	        paymentDto.setBankFlag(2);//行外
	        if ("305".equals(generationPayment.getBankCode())) {
	            paymentDto.setBankFlag(1);
            }
	        paymentDto.setSettleFlag(1);
	        paymentDto.setPurpose("放款");
	        paymentDtoList.add(paymentDto);
	        sumAmount+=generationPayment.getAmount();
	        sb.append("TransNo="+paymentDto.getTransNo()+",").append("PayAmount="+paymentDto.getPayAmount()+",")
	        .append("Currency="+paymentDto.getCurrency()+",").append("ReceiverAccountNo="+paymentDto.getReceiverAccountNo()+",")
	        .append("ReceiverAccountName="+paymentDto.getReceiverAccountName()+",")
	        .append("ReceiverAccountType="+paymentDto.getReceiverAccountType()+",")
	        .append("ReceiverBankNo="+paymentDto.getReceiverBankNo()+",SettleFlag="+paymentDto.getSettleFlag()+",")
	        .append("ReceiverBankName="+paymentDto.getReceiverBankName()+",")
	        .append("BankFlag="+paymentDto.getBankFlag()+",").append("Purpose="+paymentDto.getPurpose()+"|");
        }
	    logger.info(sb.toString());
//	    BatchAccountBalanceQueryDto queryDto = new BatchAccountBalanceQueryDto();
//	    queryDto.setSysNo(GatewayConstants.SYS_NO_ACCOUNT_001);
//	    queryDto.setTransCode("16001");
//	    queryDto.setOrgNo(protocol);
//	    queryDto.setBusiCode("10001");// 业务编码
//	    queryDto.setChannelNo("160703");
//	    queryDto.setSignType(1);// 签名类型 固定值1 即MD5
//	    queryDto.setSignMsg(queryDto.sign(md5Key));
//	    List<String> accountList = new ArrayList<String>();
//	    parameterInfo = parameterInfoService.getParaInfoByProductId("MINSHENG_PUB_ACCOUNT");
//	    if (parameterInfo==null) {
//            logger.info("参数表没有预制民生测试账户!");
//            res.setMsg("系统异常");
//            return res; 
//        }
//	    accountList.add(parameterInfo.getParameterCode());
//	    queryDto.setAccountList(accountList);
//        logger.info("调用多渠道查余额传入参: SysNo=" + queryDto.getSysNo() + ",TransCode=" + queryDto.getTransCode() + ",OrgNo="
//                + queryDto.getOrgNo() + ",BusiCode=" + queryDto.getBusiCode() + ",SignType="+ queryDto.getSignType() + ",SignMsg=" 
//                + queryDto.getSignMsg() + ",AccountList=" + queryDto.getAccountList());
//	    BatchAccountBalanceQueryRespDto queryRespDto;
//        try {
//            queryRespDto = cmbcAccountService.batchAccountBalanceQuery(queryDto);
//        } catch (Exception e1) {
//            logger.error("调用多渠道余额查询接口异常!", e1);
//            res.setMsg("系统异常!");
//            return res;
//        }
//	    if (queryRespDto==null) {
//            logger.info("调用多渠道查询余额失败,返回对象queryRespDto=null");
//            res.setMsg("系统异常");
//            return res; 
//        }
//        logger.info("调用多渠道查询余额 返回值:  returnCode=" + queryRespDto.getReturnCode() + ",returnMsg=" + queryRespDto.getReturnMsg());
//        if (!"100000".equals(queryRespDto.getReturnCode())) {
//            updateGenPayStatus(generationPaymentList,3,batchNo);
//            res.setMsg(queryRespDto.getReturnMsg());
//            return res;
//        }
//        if (queryRespDto.getAccountBalanceDetailRespDtoList()==null||queryRespDto.getAccountBalanceDetailRespDtoList().size()==0) {
//            logger.info("调用多渠道查询余额集合失败,queryRespDto.getAccountBalanceDetailRespDtoList()="+queryRespDto.getAccountBalanceDetailRespDtoList());
//            res.setMsg("系统异常");
//            return res; 
//        }
//        String usableBalance = queryRespDto.getAccountBalanceDetailRespDtoList().get(0).getUsableBalance();
//        logger.info("调用多渠道查出余额="+usableBalance);
//        if (usableBalance==null||"".equals(usableBalance)||"0".equals(usableBalance)) {
//            logger.info("调用多渠道查询余额失败,金额异常,UsableBalance="+usableBalance);
//            res.setMsg("系统异常");
//            return res; 
//        }
//        Long usableBalancee = null;
//        if (usableBalance.contains(".")) {
//            if (usableBalance.split("\\.")[1].length()==1) {
//                usableBalancee = Long.parseLong((usableBalance.split("\\.")[0]+usableBalance.split("\\.")[1]))*10;
//            }else if(usableBalance.split("\\.")[1].length()==2){
//                usableBalancee = Long.parseLong((usableBalance.split("\\.")[0]+usableBalance.split("\\.")[1]));
//            }
//        }else{
//            usableBalancee = Long.parseLong(usableBalance)*100;
//        }
//        if (sumAmount>usableBalancee) {
//            parameterInfo = parameterInfoService.getParameterInfoByParaCode("sendMobiles");
//            if (parameterInfo==null) {
//                res.setMsg("参数表没有设置手机号,无法发送短信!");  
//                return res;  
//            }
//            SendSMS.sendSMS(parameterInfo.getParameterValue(), "账户系统"+parameterInfo.getRemark()
//            +"，编号YQMS_001存在保理主账户余额不足问题，请及时解决！");
//            res.setMsg("保理主账户余额不足!"); 
//            return res;
//        }
        dto.setSettleFlag(1);
	    dto.setTotalAmount(sumAmount);
        dto.setPaymentDtoList(paymentDtoList);
        logger.info("调用多渠道批量转账入参(批次字段): SysNo=" + dto.getSysNo() + ",TransCode=" + dto.getTransCode() + ",OrgNo="
                + dto.getOrgNo() + ",BusiCode=" + dto.getBusiCode() + ",SignType="+ dto.getSignType() + ",SignMsg=" 
                + dto.getSignMsg() + ",BatchNo=" + dto.getBatchNo()+",TotalAcount="+dto.getTotalAcount()+",Currency="
                +dto.getCurrency()+",AccountType="+dto.getAccountType()+",SettleFlag="+dto.getSettleFlag()+",TotalAmount="
                +dto.getTotalAmount());
        BatchPaymentRespDto resDto = null;
        try {
            resDto = bankPaymentService.batchPayment(dto);
        } catch (Exception e) {
            logger.error("调用多渠道批量转账接口异常!", e);
            res.setMsg("系统异常!");
            return res;
        } 
        logger.info("调用多渠道批量转账返回值:    returnCode=" + resDto.getReturnCode() + ",returnMsg=" + resDto.getReturnMsg());
        if (!"100000".equals(resDto.getReturnCode())) {
            updateGenPayStatus(generationPaymentList,3,batchNo);
            res.setMsg(resDto.getReturnMsg());
            return res;
        }
        logger.info("调用多渠道批量转账返回值:    StatusId=" + resDto.getStatusId());
	    if (resDto.getStatusId()!=10) {
	        updateGenPayStatus(generationPaymentList,3,batchNo);
	        res.setMsg("多渠道受理失败");
	        return res;
        }
	    updateGenPayStatus(generationPaymentList,2,batchNo);
        return new CommonResponse();
	}
    
    private void updateGenPayStatus(List<GenerationPayment> generationPaymentList,int status,String batchNo) {
        GenerationPayment genPayment = null;
        List<GenerationPayment> genPaymentList = new ArrayList<GenerationPayment>();
        for (GenerationPayment generationPayment : generationPaymentList) {
            genPayment = new GenerationPayment();
            genPayment.setGeneId(generationPayment.getGeneId());
            genPayment.setProcessResult(batchNo);
            genPayment.setSendType(status);
            genPaymentList.add(genPayment);
        }
        generationPaymentManager.batchUpdate(genPaymentList);
    }
}
