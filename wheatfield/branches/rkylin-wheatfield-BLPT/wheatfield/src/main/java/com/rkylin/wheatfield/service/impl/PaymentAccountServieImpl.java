package com.rkylin.wheatfield.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.dubbo.common.json.JSONArray;
import com.alibaba.dubbo.common.json.JSONObject;
import com.rkylin.common.RedisIdGenerator;
import com.rkylin.gaterouter.dto.bankpayment.PaymentDto;
import com.rkylin.gaterouter.dto.bankpayment.PaymentRespDto;
import com.rkylin.gaterouter.service.BankPaymentService;
import com.rkylin.util.bean.BeanMapper;
import com.rkylin.utils.RkylinMailUtil;
import com.rkylin.wheatfield.api.PaymentAccountServiceApi;
import com.rkylin.wheatfield.api.SemiAutomatizationServiceApi;
import com.rkylin.wheatfield.bean.BatchTransfer;
import com.rkylin.wheatfield.bean.BatchTransferOrderInfo;
import com.rkylin.wheatfield.bean.OrderInfo;
import com.rkylin.wheatfield.bean.TransferInfo;
import com.rkylin.wheatfield.common.DateUtils;
import com.rkylin.wheatfield.common.ValHasNoParam;
import com.rkylin.wheatfield.constant.AccountConstants;
import com.rkylin.wheatfield.constant.BaseConstants;
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.constant.TransCodeConst;
import com.rkylin.wheatfield.dao.AccountInfoDao;
import com.rkylin.wheatfield.dao.FinanaceAccountDao;
import com.rkylin.wheatfield.dao.FinanaceEntryDao;
import com.rkylin.wheatfield.dao.TransOrderInfoDao;
import com.rkylin.wheatfield.exception.AccountException;
import com.rkylin.wheatfield.manager.FinanaceAccountAuthManager;
import com.rkylin.wheatfield.manager.FinanaceAccountManager;
import com.rkylin.wheatfield.manager.FinanaceEntryManager;
import com.rkylin.wheatfield.manager.SettleTransTabManager;
import com.rkylin.wheatfield.manager.TransOrderInfoManager;
import com.rkylin.wheatfield.model.BalanceResponse;
import com.rkylin.wheatfield.model.BatchResponse;
import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.model.FinanaceEntryResponse;
import com.rkylin.wheatfield.model.PingAnParam;
import com.rkylin.wheatfield.model.ReversalResponse;
import com.rkylin.wheatfield.pojo.AccountInfo;
import com.rkylin.wheatfield.pojo.AccountInfoQuery;
import com.rkylin.wheatfield.pojo.AdvanceBalance;
import com.rkylin.wheatfield.pojo.Balance;
import com.rkylin.wheatfield.pojo.FinanaceAccount;
import com.rkylin.wheatfield.pojo.FinanaceAccountAuth;
import com.rkylin.wheatfield.pojo.FinanaceAccountAuthQuery;
import com.rkylin.wheatfield.pojo.FinanaceAccountQuery;
import com.rkylin.wheatfield.pojo.FinanaceEntry;
import com.rkylin.wheatfield.pojo.FinanaceEntryQuery;
import com.rkylin.wheatfield.pojo.OrderAuxiliary;
import com.rkylin.wheatfield.pojo.ParameterInfo;
import com.rkylin.wheatfield.pojo.SHBalanceInfo;
import com.rkylin.wheatfield.pojo.SettleTransTab;
import com.rkylin.wheatfield.pojo.TransOrderInfo;
import com.rkylin.wheatfield.pojo.TransOrderInfoQuery;
import com.rkylin.wheatfield.pojo.User;
import com.rkylin.wheatfield.response.BatchTransferResponse;
import com.rkylin.wheatfield.response.ErrorResponse;
import com.rkylin.wheatfield.response.PaymengManagerResponse;
import com.rkylin.wheatfield.response.PaymentBalanceinfoResponce;
import com.rkylin.wheatfield.response.Response;
import com.rkylin.wheatfield.response.SHBalanceInfoResponse;
import com.rkylin.wheatfield.service.AccountManageService;
import com.rkylin.wheatfield.service.CheckInfoService;
import com.rkylin.wheatfield.service.GenerationPaymentService;
import com.rkylin.wheatfield.service.IAPIService;
import com.rkylin.wheatfield.service.IErrorResponseService;
import com.rkylin.wheatfield.service.OperationServive;
import com.rkylin.wheatfield.service.ParameterInfoService;
import com.rkylin.wheatfield.service.PaymentAccountService;
import com.rkylin.wheatfield.service.PaymentInternalService;
import com.rkylin.wheatfield.service.SHBalanceInfoService;
import com.rkylin.wheatfield.service.SettlementServiceThr;
import com.rkylin.wheatfield.utils.ArithUtil;
import com.rkylin.wheatfield.utils.BeanUtil;
import com.rkylin.wheatfield.utils.CodeEnum;
import com.rkylin.wheatfield.utils.CommUtil;
import com.rkylin.wheatfield.utils.DateUtil;
import com.rkylin.wheatfield.utils.StringUtils;
import com.rkylin.wheatfield.utils.VerifyUtil;

@Transactional
@Service("paymentAccountService")

public class PaymentAccountServieImpl implements PaymentAccountService,IAPIService,PaymentAccountServiceApi {
	private static Logger logger = LoggerFactory.getLogger(PaymentAccountServieImpl.class);	
	private static Object lock=new Object();
	@Autowired
	OperationServive operationService;
	@Autowired
	FinanaceEntryManager finanaceEntryManager;
	@Autowired
	IErrorResponseService errorResponseService;
	@Autowired
	CheckInfoService checkInfoService;	
	@Autowired
	OperationServiceImpl operationServiceImpl;
	@Autowired
	TransOrderInfoManager transOrderInfoManager;
	@Autowired
	AccountManageService accountManageService;
	@Autowired
	PaymentInternalService paymentInternalService;
	@Autowired
	RedisIdGenerator redisIdGenerator;
	@Autowired
	FinanaceAccountAuthManager finanaceAccountAuthManager;
	@Autowired
	SettleTransTabManager settleTransTabManager;
	@Autowired
	FinanaceAccountManager finanaceAccountManager;
	@Autowired
	GenerationPaymentService generationPaymentService;
	@Autowired
	SettlementServiceThr settlementServiceThr;
	@Autowired
	private FinanaceEntryDao finanaceEntryDao;
	@Autowired
	private FinanaceAccountDao finanaceAccountDao;
	@Autowired
	private SHBalanceInfoService shBalanceInfoService; 
	@Autowired
	private BankPaymentService bankPaymentService;
    @Autowired
    @Qualifier("accountInfoDao")
    private AccountInfoDao accountInfoDao;
    
    @Autowired
    @Qualifier("transOrderInfoDao")
    private TransOrderInfoDao transOrderInfoDao;
    
    @Autowired
    private ParameterInfoService parameterInfoService;
    
    @Autowired
    @Qualifier("semiAutomatizationService")
    private SemiAutomatizationServiceApi semiAutomatizationServiceApi;
	
	ArithUtil arithUtil=new ArithUtil();
	DateUtil dateUtil=new DateUtil();
	
	/**
	 * 查询余额（dubbo）
	 * @param user
	 * @param finAccountId
	 * @param type  1:精确查找（有平衡检查）    2：查询用户所有账户（没有平衡检查） 
	 * @return
	 */
	public BalanceResponse getUserBalance(User user,String finAccountId,String type) {
		logger.info("查询余额传入参数  finAccountId="+finAccountId+",type="+type+"   user的所有值："+BeanUtil.getBeanVal(user, null));
		BalanceResponse res = new BalanceResponse();
		if (type==null || "".equals(type)) {
			res.setCode(CodeEnum.FAILURE.getCode());
			res.setMsg("type非法");
			return res;
		}
		if ("1".equals(type)) {
			if (user.userId==null||"".equals(user.userId)||user.constId==null||"".equals(user.constId)
					||user.productId==null||"".equals(user.productId)||user.statusID==null||"".equals(user.statusID)) {
				res.setCode(CodeEnum.ERR_PARAM_NULL.getCode());
				res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
				return res;
			}
			Balance balance = checkInfoService.getBalance(user,finAccountId);
			logger.info("type="+type+"  查出的余额   balance值  "+BeanUtil.getBeanVal(balance, null));
			if (balance==null) {
				res.setCode(CodeEnum.ERR_DATA_NO_RESULT.getCode());
				res.setMsg(CodeEnum.ERR_DATA_NO_RESULT.getMessage());
				return res;
			}
			res.setBalance(balance);
		}else if ("2".equals(type)) {
			if (user.userId==null||"".equals(user.userId)||user.constId==null||"".equals(user.constId)) {
				res.setCode(CodeEnum.ERR_PARAM_NULL.getCode());
				res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
				return res;
			}
			SHBalanceInfoResponse response = shBalanceInfoService.getBalanceInfoList(user.userId,user.constId);
			if (!response.isIs_success()) {
				res.setCode(CodeEnum.ERR_DATA_NO_RESULT.getCode());
				res.setMsg(CodeEnum.ERR_DATA_NO_RESULT.getMessage());
				return res;
			}
			List<SHBalanceInfo> shBalanceInfoList = response.getShbalanceinfolist();
			logger.info("type="+type+"  查出的余额   balanceList值  "+BeanUtil.getBeanListVal(shBalanceInfoList, null));
			res.setShBalanceInfoList(shBalanceInfoList);
		}
		return res;
	}
	
	@Override
	public Balance getBalance(User user,String finAccountId) {
		// TODO 获取账户余额
		return checkInfoService.getBalance(user,finAccountId);
	}

	public String makeRefundRecords(TransOrderInfo transOrderInfo){
		synchronized (lock) {
			String[] productIdStrings={""};
			User user=new User();
			String userId = transOrderInfo.getUserId();
			user.userId=userId;
			user.constId = transOrderInfo.getMerchantCode();
			//if(Constants.HT_CLOUD_ID.equals(transOrderInfo.getMerchantCode()) && !userId.equals(transOrderInfo.getInterMerchantCode())){
			if(Constants.HT_CLOUD_ID.equals(transOrderInfo.getMerchantCode()) && Constants.HT_PRODUCT.equals(transOrderInfo.getErrorCode())){
				user.constId=Constants.HT_ID;
			}
			if("141223100000022".equals(userId)){
				user.constId = Constants.FN_ID;
			}
			user.productId=transOrderInfo.getErrorCode();
			logger.info("makeRefundRecords userId="+userId+",constId="+transOrderInfo.getMerchantCode()+",productId="+transOrderInfo.getErrorCode());
			if (transOrderInfo.getErrorCode()==null) {
				user.uEType=AccountConstants.ACCOUNT_TYPE_BASE;
			}
			//判断账户状态是否正常
			boolean accountIsOK=operationService.checkAccount(user);
			if(!accountIsOK){
				return CodeEnum.ERR_ACCOUNT_NOT_NORMAL.getCode();
			}
			//获取套录号
			String entryId=redisIdGenerator.createRequestNo();
			logger.info("makeRefundRecords userId="+userId+",constId="+transOrderInfo.getMerchantCode()+",entryId="+entryId);
			//获取每个账户记账流水
			List<FinanaceEntry> finanaceEntries=new ArrayList<FinanaceEntry>();
			List<FinanaceEntry> finanaceEntriesAll=new ArrayList<FinanaceEntry>();
			//记录该订单交易流水原始amount金额
			long amount=transOrderInfo.getAmount();
			Balance balanceA = new Balance();
			for (int i = 0; i <= 1; i++) {
				boolean flag=true;
				if(0==i){
					userId=transOrderInfo.getUserId();	
				}else {
					userId=TransCodeConst.THIRDPARTYID_FNZZH;								
					flag=false;
				}
				user.userId=userId;
				Balance balance=null;
				if(flag){
					balance=checkInfoService.getBalance(user,"");
				}else{
					balance=checkInfoService.getBalance(user,userId);
				}
				if(balance==null){ //无法获取用户余额信息！
					return CodeEnum.ERR_ACCOUNT_NOT_EXIST.getCode();
				}
				
				balance.setPulseDegree(balance.getPulseDegree()+1);
				transOrderInfo.setFuncCode(TransCodeConst.CHARGE);
				//判断机构号是丰年
				if (i == 0 && Constants.FN_ID.equals(transOrderInfo.getMerchantCode()) && transOrderInfo.getUserFee() > 0){
					logger.info("--------退票是丰年用户并且有手续费,用户的退票金额为订单金额+手续费");
					transOrderInfo.setAmount(amount + transOrderInfo.getUserFee());
				} else {
					transOrderInfo.setAmount(amount);
				}
				finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, balance, entryId, flag);
				logger.info("makeRefundRecords userId="+userId+",constId="+transOrderInfo.getMerchantCode()+",finanaceEntries.size()="+finanaceEntries.size());
				if(finanaceEntries.size()==0){ //账户流水数据入库失败！
					return CodeEnum.ERR_TRADE_RECORD_NOT_IN.getCode();
				}
				for(FinanaceEntry finanaceEntry:finanaceEntries) {
					finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
					finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
					if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
						finanaceEntry.setRemark("退票重划");
					}
				}
				finanaceEntriesAll.addAll(finanaceEntries);
				if (i==0){
					balanceA = balance;
				}
			}
			//恢复原始amount金额
			transOrderInfo.setAmount(amount);
			//判断如果是君融贷，并且有手续费，做一笔转账
			if (Constants.JRD_ID.equals(transOrderInfo.getMerchantCode()) && transOrderInfo.getUserFee() > 0){
				String earningsEntryId=redisIdGenerator.createRequestNo();
				logger.info("--------退票用户是君融贷用户并且有手续费,做收益户向用户的转账，金额为手续费");
				transOrderInfo.setAmount(transOrderInfo.getUserFee());
				transOrderInfo.setFuncCode(TransCodeConst.ADJUST_ACCOUNT_AMOUNT);
				userId=TransCodeConst.THIRDPARTYID_JRDQYSYZZH;
				Balance earningsBalance=null;
				earningsBalance=checkInfoService.getBalance(user,userId);
				if (earningsBalance==null){ //无法获取用户余额信息！
					logger.error("获取君融贷收益账户余额信息失败！");
					return CodeEnum.ERR_ACCOUNT_NOT_EXIST.getCode();
				}
				if (earningsBalance.getBalanceSettle() < transOrderInfo.getUserFee()){
					logger.error("君融贷收益账户余额不足！");
					return CodeEnum.ERR_ACCOUNT_NOT_EXIST.getCode();
				}
				earningsBalance.setPulseDegree(earningsBalance.getPulseDegree()+1);
				finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, earningsBalance,balanceA, earningsEntryId, false);
				if(null!=finanaceEntries&&finanaceEntries.size()>0){
					for(FinanaceEntry finanaceEntry:finanaceEntries) {
						finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);	
						finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
						if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
							finanaceEntry.setRemark("转账");
						}
						if(finanaceEntry.getPaymentAmount()>0){
							finanaceEntriesAll.add(finanaceEntry);
						}
					}
				}
			}
			//恢复原始amount金额
			transOrderInfo.setAmount(amount);
			logger.info("makeRefundRecords userId="+userId+",constId="+transOrderInfo.getMerchantCode()+",finanaceEntriesAll.size()="+finanaceEntriesAll.size());
			//批量插入记录流水表
			try {
				transOrderInfo.setRequestId(null);
				transOrderInfo.setStatus(TransCodeConst.TRANS_STATUS_NORMAL);
				transOrderInfo.setFuncCode(TransCodeConst.REFUND);
				insertFinanaceEntry(finanaceEntriesAll, transOrderInfo, productIdStrings);
			} catch (AccountException e) {
				e.printStackTrace();
				logger.error("******************账户  "+transOrderInfo.getUserId()+" 记账流水数据入库失败");
				return CodeEnum.ERR_TRADE_RECORD_NOT_IN.getCode();
			}
			return CodeEnum.SUCCESS.getCode();
		}
	}
	
	@Override
	public ErrorResponse recharge(TransOrderInfo transOrderInfo, String productId, String userId){
		synchronized (lock) {
			logger.info("---------------------------账户UserId"+userId+"充值操作开始------------------------");
			logger.info("充值操作参数信息：amount="+transOrderInfo.getAmount()+",UserId="+transOrderInfo.getUserId()+",funccode="+transOrderInfo.getFuncCode()
					+",intermerchantcode="+transOrderInfo.getInterMerchantCode()+",merchantcode="+transOrderInfo.getMerchantCode()+",orderamount="+transOrderInfo.getOrderAmount()
					+",ordercount="+transOrderInfo.getOrderCount()+",orderdate="+transOrderInfo.getOrderDate()+",orderno="+transOrderInfo.getOrderNo()
					+",orderpackageno="+transOrderInfo.getOrderPackageNo()+",paychannelid="+transOrderInfo.getPayChannelId()+",remark="+transOrderInfo.getRemark()
					+",productid="+productId+",requestno="+transOrderInfo.getRequestNo()+",requesttime="+transOrderInfo.getRequestTime()+",status="+transOrderInfo.getStatus()
					+",tradeflowno="+transOrderInfo.getTradeFlowNo()+",userfee="+transOrderInfo.getUserFee()+",feeamount="+transOrderInfo.getFeeAmount()
					+",profit="+transOrderInfo.getProfit()+",busitypeid="+transOrderInfo.getBusiTypeId()+",bankcode="+transOrderInfo.getBankCode()+",errorcode="+transOrderInfo.getErrorCode()
					+",errormsg="+transOrderInfo.getErrorMsg()+",useripaddress="+transOrderInfo.getUserIpAddress());
			// TODO 充值
			ErrorResponse response=new ErrorResponse();
			String reCode = "C0";
			String reMsg = "成功";
			String[] productIdStrings={productId};
			//校验交易码
			if (!TransCodeConst.CHARGE.equals(transOrderInfo.getFuncCode())){
				logger.info("充值交易码错误！订单中的交易码为"+transOrderInfo.getFuncCode());
				response.setCode("C1");
				response.setMsg("充值交易码错误！");
				return response;
			}
			//校验订单号是否存在
			if(!this.orderNoChk(transOrderInfo.getOrderNo(),transOrderInfo.getMerchantCode())){
				logger.info("订单号："+transOrderInfo.getOrderNo()+"在机构号："+transOrderInfo.getMerchantCode()+"中已存在");
				response.setCode("C9");
				response.setMsg("该交易订单号已存在，请确认！");
				return response;
			}
			//获取每个账户记账流水
			List<FinanaceEntry> finanaceEntries=new ArrayList<FinanaceEntry>();
			//获取所有账户记账流水
			List<FinanaceEntry> finanaceEntrielist=new ArrayList<FinanaceEntry>();
			User user=new User();
			user.userId=userId;
			user.constId=transOrderInfo.getMerchantCode();
			user.productId=productId;
			//判断账户状态是否正常
			boolean accountIsOK=operationService.checkAccount(user);
			if(!accountIsOK){
				logger.error("用户"+userId+"状态为非正常状态");
				reCode = "C1";
				reMsg = "账户状态非正常状态";
			}else{
				//判断订单信息是否有误
				transOrderInfo.setProductIdd(productId);
				String msg=checkInfoService.checkTradeInfo(transOrderInfo);
				if(!"ok".equals(msg)){
					logger.error(msg);
					reCode = "C2";
					reMsg = msg;
				}else{
					try {
						//获取套录号
						String entryId=redisIdGenerator.createRequestNo();
						for (int i = 0; i <= 0; i++) {
							boolean flag=true;
							if(0==i){
								userId=transOrderInfo.getUserId();	
								flag=true;
							}else {
								userId=TransCodeConst.THIRDPARTYID_FNZZH;								
								flag=false;
							}
							user.userId=userId;
							Balance balance=null;
							if(flag){
								balance=checkInfoService.getBalance(user,"");
							}else{
								balance=checkInfoService.getBalance(user,userId);
							}
							if(null!=balance){
								balance.setPulseDegree(balance.getPulseDegree()+1);
								finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, balance, entryId, flag);
								if(null!=finanaceEntries&&0<finanaceEntries.size()){
									for(FinanaceEntry finanaceEntry:finanaceEntries) {
										finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
										finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
										if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
											finanaceEntry.setRemark("充值");
										}
										finanaceEntrielist.add(finanaceEntry);
									}
							}else{
								logger.error("获取用户["+userId+"]账户流水信息失败");
								reCode = "C1";
								reMsg = "账户流水数据入库失败";
								break;
							}
							}else{
								logger.error("获取用户["+userId+"]余额信息失败");
								reCode = "C1";
								reMsg = "用户余额信息查询失败";
								break;
							}							
						}
						//批量插入记录流水表   写入数据失败尝试三次
						if(reCode.equals("C0")){
							try {
								insertFinanaceEntry(finanaceEntrielist, transOrderInfo, productIdStrings);
							} catch (AccountException e) {
								logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败一次");
								try {
									insertFinanaceEntry(finanaceEntrielist, transOrderInfo, productIdStrings);
								} catch (AccountException e1) {
									logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败两次");
									try {
										insertFinanaceEntry(finanaceEntrielist, transOrderInfo, productIdStrings);
									} catch (AccountException e2) {
										//发送邮件或短信通知管理员
										logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败三次");
										//启用线程 发送邮件
										RkylinMailUtil.sendMailThread("账户记账流水失败通知","******************账户"+transOrderInfo.getUserId()+"做充值记账流水数据入库连续失败三次", TransCodeConst.FINANACE_ENTRY_ERROR_TOEMAIL);
										logger.error(e2.getMessage());
										reCode = "C3";
										reMsg = "数据库操作异常";
									}
								}
							}
						}
						
					} catch (AccountException e) {
						logger.error(e.getMessage());
						reCode = "C3";
						reMsg = "数据库操作异常";
					}	
				}
			}
			if(reCode.equals("C0")){
				response.setIs_success(true);
			}else{
				response.setCode(reCode);
				response.setMsg(reMsg);
			}
			logger.info("---------------------------账户UserId"+transOrderInfo.getUserId()+"充值操作结束------------------------");
			return response;
		}
	}

	@Override
	public ErrorResponse advanceorders(TransOrderInfo transOrderInfo,String productId,String referUserId) {
		// TODO 预付金代付--订单    订单表生成数据
		synchronized (lock){
			logger.info("---------------------------商户="+transOrderInfo.getInterMerchantCode()+",台长="+referUserId+"预付金操作开始------------------------");
			logger.info("预付金代付操作参数信息：amount="+transOrderInfo.getAmount()+",UserId="+transOrderInfo.getUserId()+",funccode="+transOrderInfo.getFuncCode()
					+",intermerchantcode="+transOrderInfo.getInterMerchantCode()+",merchantcode="+transOrderInfo.getMerchantCode()+",orderamount="+transOrderInfo.getOrderAmount()
					+",ordercount="+transOrderInfo.getOrderCount()+",orderdate="+transOrderInfo.getOrderDate()+",orderno="+transOrderInfo.getOrderNo()
					+",orderpackageno="+transOrderInfo.getOrderPackageNo()+",paychannelid="+transOrderInfo.getPayChannelId()+",remark="+transOrderInfo.getRemark()
					+",productid="+productId+",requestno="+transOrderInfo.getRequestNo()+",requesttime="+transOrderInfo.getRequestTime()+",status="+transOrderInfo.getStatus()
					+",tradeflowno="+transOrderInfo.getTradeFlowNo()+",userfee="+transOrderInfo.getUserFee()+",feeamount="+transOrderInfo.getFeeAmount()
					+",profit="+transOrderInfo.getProfit()+",busitypeid="+transOrderInfo.getBusiTypeId()+",bankcode="+transOrderInfo.getBankCode()+",errorcode="+transOrderInfo.getErrorCode()
					+",errormsg="+transOrderInfo.getErrorMsg()+",useripaddress="+transOrderInfo.getUserIpAddress()+",referUserId="+referUserId);
			ErrorResponse response=new ErrorResponse();
			String reCode = "C0";
			String reMsg = "成功";
			String[] productIdStrings = {productId};
			//校验交易码
			if (!TransCodeConst.ADVANCE.equals(transOrderInfo.getFuncCode())){
				logger.info("预付金代付交易码错误！订单中的交易码为"+transOrderInfo.getFuncCode());
				response.setCode("C1");
				response.setMsg("预付金代付交易码错误！");
				return response;
			}
			//校验订单号是否存在
			if(!this.orderNoChk(transOrderInfo.getOrderNo(),transOrderInfo.getMerchantCode())){
				logger.info("订单号："+transOrderInfo.getOrderNo()+"在机构号："+transOrderInfo.getMerchantCode()+"中已存在");
				response.setCode("C1");
				response.setMsg("该交易订单号已存在，请确认！");
				return response;
			}
			//获取每个账户记账流水
			List<FinanaceEntry> finanaceEntries=new ArrayList<FinanaceEntry>();
			//获取所有账户记账流水
			List<FinanaceEntry> finanaceEntrielist=new ArrayList<FinanaceEntry>();
			User user=new User();
			user.constId=transOrderInfo.getMerchantCode();
			user.productId=Constants.FN_PRODUCT;
			user.userId=transOrderInfo.getUserId();
			//判断账户状态是否正常
			boolean accountIsOK=operationService.checkAccount(user);
			if(!accountIsOK){
				logger.error("用户"+transOrderInfo.getUserId()+"状态为非正常状态");
				reCode = "C1";
				reMsg = "账户状态非正常状态";
			}else{
				//判断订单信息是否有误
				String msg=checkInfoService.checkTradeInfo(transOrderInfo);
				if(!"ok".equals(msg)){
					logger.error(msg);
					reCode = "C2";
					reMsg = msg;
				}else{
					try {
						//获取套录号
						String entryId=redisIdGenerator.createRequestNo();
						//判断商户是否有预付金子账户 ，没有则创建
						user.userId=transOrderInfo.getInterMerchantCode();
						user.productId=Constants.ADVANCE_ACCOUNT;
						user.referUserId=referUserId;
						String rs=accountManageService.openAccount(user, new FinanaceEntry());
						if("ok".equals(rs)){//创建/已存在预付金子账户
							//内部转账操作  丰年主账户(企业)可提现余额减     账户/商户主账户余额增加
							boolean flag=false;
							//获取丰年企业账户余额
							Balance balance=checkInfoService.getBalance(user,transOrderInfo.getUserId());
							if(null!=balance){
								balance.setPulseDegree(balance.getPulseDegree()+1);
								finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, balance, entryId, flag);
								if(null!=finanaceEntries&&finanaceEntries.size()>0){
									for(FinanaceEntry finanaceEntry:finanaceEntries) {
										finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);	
										finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
										if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
											finanaceEntry.setRemark("预付金");
										}
										finanaceEntrielist.add(finanaceEntry);
									}
									//####################预付金账户和商户预付金子账户####################
									for (int i = 0; i <= 1; i++) {
										String uId=null;
										flag=true;
										if(0==i){
											uId=transOrderInfo.getInterMerchantCode();	
										}else {
											uId=TransCodeConst.THIRDPARTYID_FNYFJZH;							
										}
										user.userId=uId;
										user.constId=transOrderInfo.getMerchantCode();
										user.productId=productId;
										user.referUserId=referUserId;
										balance=new Balance();
										if(0==i){
											balance=checkInfoService.getBalance(user,"");
										}else{
											balance=checkInfoService.getBalance(user,uId);
										}
										if(null!=balance){
											balance.setPulseDegree(balance.getPulseDegree()+1);
											FinanaceEntry finanaceEntry=new FinanaceEntry();
											finanaceEntry.setPaymentAmount(transOrderInfo.getAmount());
											finanaceEntries=checkInfoService.getInternalFinanaceEntries(finanaceEntry, balance, flag, "yufujin", transOrderInfo.getUserId());
											if(finanaceEntries!=null){						
												if(null!=finanaceEntries&&finanaceEntries.size()>0){
													for(FinanaceEntry finanaceEntryInfo:finanaceEntries) {
														finanaceEntryInfo.setReferEntryId(entryId);
														finanaceEntryInfo.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);	
														if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
															finanaceEntry.setRemark("预付金");
														}
														finanaceEntrielist.add(finanaceEntryInfo);
													}
											}else{
												logger.error("获取用户["+transOrderInfo.getUserId()+"]账户流水信息失败");
												reCode = "C3";
												reMsg = "账户流水数据入库失败";
												break;
											}
											}else{
												logger.error("获取用户["+transOrderInfo.getUserId()+"]账户流水信息失败");
												reCode = "C3";
												reMsg = "账户流水数据入库失败";
												break;
											}							
										}else{
											logger.error("获取用户["+transOrderInfo.getUserId()+"]余额信息失败");
											reCode = "C4";
											reMsg = "用户余额信息查询失败";
										}
									}
								}else{
									logger.error("账户["+transOrderInfo.getUserId()+"]余额不足");
									reCode = "C3";
									reMsg = "账户余额不足";
								}
							}else{
								logger.error("获取用户["+transOrderInfo.getUserId()+"]余额信息失败");
								reCode = "C3";
								reMsg = "账户余额查询失败";
							}
						}else{
							logger.error("创建账户"+transOrderInfo.getInterMerchantCode()+"的预付金账户失败，请确认主账户");
							reCode = "C2";
							reMsg = "创建账户"+transOrderInfo.getInterMerchantCode()+"的预付金账户失败，请确认主账户";
						}
						if(reCode.equals("C0")){
							try {
								insertFinanaceEntry(finanaceEntrielist, transOrderInfo,productIdStrings);
							} catch (AccountException e) {
								logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败一次");
								try {
									insertFinanaceEntry(finanaceEntrielist, transOrderInfo,productIdStrings);
								} catch (AccountException e1) {
									logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败两次");
									try {
										insertFinanaceEntry(finanaceEntrielist, transOrderInfo,productIdStrings);
									} catch (AccountException e2) {
										//发送邮件或短信通知管理员
										logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败三次");
										//启用线程 发送邮件
										RkylinMailUtil.sendMailThread("账户记账流水失败通知","******************账户"+transOrderInfo.getUserId()+"做充值记账流水数据入库连续失败三次", TransCodeConst.FINANACE_ENTRY_ERROR_TOEMAIL);
										logger.error(e2.getMessage());
										reCode = "C5";
										reMsg = "数据库操作异常";
									}
								}
							}
						}
						
					} catch (AccountException e) {
						logger.error(e.getMessage());
						reCode = "C5";
						reMsg = "数据库操作异常";
					}
				}
			}
			if(reCode.equals("C0")){
				response.setIs_success(true);
			}else{
				response.setCode(reCode);
				response.setMsg(reMsg);
			}
			logger.info("---------------------------商户="+transOrderInfo.getInterMerchantCode()+",台长="+referUserId+"预付金操作结束------------------------");
			return response;
		}
	}
	
	@Override
	public ErrorResponse deduct(TransOrderInfo transOrderInfo, String productId, String userId) {
		synchronized (lock){
			logger.info("---------------------------账户UserId"+userId+"，消费类型="+transOrderInfo.getFuncCode()+"消费操作开始------------------------");
			logger.info("消费操作参数信息：amount="+transOrderInfo.getAmount()+",UserId="+transOrderInfo.getUserId()+",funccode="+transOrderInfo.getFuncCode()
					+",intermerchantcode="+transOrderInfo.getInterMerchantCode()+",merchantcode="+transOrderInfo.getMerchantCode()+",orderamount="+transOrderInfo.getOrderAmount()
					+",ordercount="+transOrderInfo.getOrderCount()+",orderdate="+transOrderInfo.getOrderDate()+",orderno="+transOrderInfo.getOrderNo()
					+",orderpackageno="+transOrderInfo.getOrderPackageNo()+",paychannelid="+transOrderInfo.getPayChannelId()+",remark="+transOrderInfo.getRemark()
					+",productid="+productId+",requestno="+transOrderInfo.getRequestNo()+",requesttime="+transOrderInfo.getRequestTime()+",status="+transOrderInfo.getStatus()
					+",tradeflowno="+transOrderInfo.getTradeFlowNo()+",userfee="+transOrderInfo.getUserFee()+",feeamount="+transOrderInfo.getFeeAmount()
					+",profit="+transOrderInfo.getProfit()+",busitypeid="+transOrderInfo.getBusiTypeId()+",bankcode="+transOrderInfo.getBankCode()+",errorcode="+transOrderInfo.getErrorCode()
					+",errormsg="+transOrderInfo.getErrorMsg()+",useripaddress="+transOrderInfo.getUserIpAddress());
			// TODO 扣款
			ErrorResponse response=new ErrorResponse();
			String reCode = "C0";
			String reMsg = "成功";
			String deductType="信用消费";//扣款类型
			String[] productIdStrings = {productId};
			//校验交易码
			if (!TransCodeConst.CREDIT_CONSUME.equals(transOrderInfo.getFuncCode())	&& !TransCodeConst.DEBIT_CONSUME.equals(transOrderInfo.getFuncCode())){
				logger.info("消费交易码错误！订单中的交易码为"+transOrderInfo.getFuncCode());
				response.setCode("C1");
				response.setMsg("消费交易码错误！");
				return response;
			}
			//校验订单号是否存在
			if(!this.orderNoChk(transOrderInfo.getOrderNo(),transOrderInfo.getMerchantCode())){
				logger.info("订单号："+transOrderInfo.getOrderNo()+"在机构号："+transOrderInfo.getMerchantCode()+"中已存在");
				response.setCode("C1");
				response.setMsg("该交易订单号已存在，请确认！");
				return response;
			}
			//获取每个账户记账流水
			List<FinanaceEntry> finanaceEntries=new ArrayList<FinanaceEntry>();
			//获取所有账户记账流水
			List<FinanaceEntry> finanaceEntrieAlls=new ArrayList<FinanaceEntry>();
			User user=new User();
			user.userId=userId;
			user.constId=transOrderInfo.getMerchantCode();
			user.productId=productId;
			//判断账户状态是否正常
			boolean accountIsOK=operationService.checkAccount(user);
			if(!accountIsOK){
				logger.error("用户"+userId+"状态为非正常状态");
				reCode = "C1";
				reMsg = "账户状态非正常状态";
			}else{
				//判断订单信息是否有误
				transOrderInfo.setErrorCode(productId);//暂存消费产品号
				String msg=checkInfoService.checkTradeInfo(transOrderInfo);
				if(!"ok".equals(msg)){
					logger.error(msg);
					reCode = "C2";
					reMsg = msg;
				}else{
					try {
						long amount=transOrderInfo.getAmount();//获取改单交易金额
						//获取套录号
						String entryId=redisIdGenerator.createRequestNo();
						int num=1;
						if(productId.equals(Constants.FN_PRODUCT)&&transOrderInfo.getUserFee()>0){
							num=3;
							transOrderInfo.setAmount(amount-transOrderInfo.getUserFee());
						}
						Balance balanceT=null;
						for (int i = 0; i <= num; i++) {
							boolean flag=true;
							if(0==i){
								userId=transOrderInfo.getUserId();	
								flag=true;
							}else if(1==i) {
								flag=false;
							}else if(2==i) {
								userId=transOrderInfo.getUserId();	
								user.productId=Constants.FN_RED_PACKET;
								transOrderInfo.setAmount(transOrderInfo.getUserFee());
								flag=true;
							}else if(3==i) {
								flag=false;
							}
							user.userId=userId;
							if(transOrderInfo.getFuncCode().equals(TransCodeConst.DEBIT_CONSUME)){//储蓄消费
								deductType="储蓄消费";
							}
							//信用消费记账中间账户为其它应收款，储蓄消费记账中间账户为其它应付款
							if(transOrderInfo.getFuncCode().equals(TransCodeConst.CREDIT_CONSUME)){//信用消费
								userId=TransCodeConst.THIRDPARTYID_QTYSKZH;
							}else{
								userId=TransCodeConst.THIRDPARTYID_QTYFKZH;
							}
							Balance balance=null;
							if(3==i) {
								balance=balanceT;
							}else{
								if(flag){
									balance=checkInfoService.getBalance(user,"");
								}else{
									balance=checkInfoService.getBalance(user,userId);
								}
							}
							if(null!=balance){
								balance.setPulseDegree(balance.getPulseDegree()+1);
								finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, balance, entryId, flag);
								if(null!=finanaceEntries&&0<finanaceEntries.size()){
									for(FinanaceEntry finanaceEntry:finanaceEntries) {	
										finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
										if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
											finanaceEntry.setRemark(deductType);
										}
										finanaceEntrieAlls.add(finanaceEntry);
									}
									if(1==i) {
										balanceT=balance;
									}
								}else{
									logger.error("获取用户["+userId+"]账户流水信息失败");
									reCode = "C3";
									reMsg = "账户流水数据入库失败";
									break;
								}							
							}else{
								logger.error("获取用户["+userId+"]余额信息失败");
								reCode = "C4";
								reMsg = "用户余额查询失败";
							}
						}
						transOrderInfo.setAmount(amount);
						//批量插入记录流水表   写入数据失败尝试三次
						if(reCode.equals("C0")){
							try {
								insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo, productIdStrings);
							} catch (AccountException e) {
								logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败一次");
								try {
									insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo, productIdStrings);
								} catch (AccountException e1) {
									logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败两次");
									try {
										insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo, productIdStrings);
									} catch (AccountException e2) {
										//发送邮件或短信通知管理员
										logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败三次");
										//启用线程 发送邮件
										RkylinMailUtil.sendMailThread("账户记账流水失败通知","******************账户"+transOrderInfo.getUserId()+"做扣款-"+deductType+"记账流水数据入库连续失败三次", TransCodeConst.FINANACE_ENTRY_ERROR_TOEMAIL);
										logger.error(e2.getMessage());
										reCode = "C5";
										reMsg = "数据库操作异常";
									}
								}
							}
						}
						
						
					} catch (AccountException e) {
						logger.error(e.getMessage());
						reCode = "C5";
						reMsg = "数据库操作异常";
					}
				}
			}
			if(reCode.equals("C0")){
				response.setIs_success(true);
			}else{
				response.setCode(reCode);
				response.setMsg(reMsg);
			}
			logger.info("---------------------------账户UserId"+userId+"，消费类型="+transOrderInfo.getFuncCode()+"消费操作结束------------------------");
			return response;
		}
	}
	@Override
	public ErrorResponse transfer(TransOrderInfo transOrderInfo,String productId,String userId) {
		// TODO 转账
		synchronized (lock) {
			logger.info("---------------------------账户UserId"+userId+"向账户"+transOrderInfo.getInterMerchantCode()+"转入"+transOrderInfo.getAmount()+"分，转账操作开始------------------------");
			logger.info("转账操作参数信息：amount="+transOrderInfo.getAmount()+",UserId="+transOrderInfo.getUserId()+",funccode="+transOrderInfo.getFuncCode()
					+",intermerchantcode="+transOrderInfo.getInterMerchantCode()+",merchantcode="+transOrderInfo.getMerchantCode()+",orderamount="+transOrderInfo.getOrderAmount()
					+",ordercount="+transOrderInfo.getOrderCount()+",orderdate="+transOrderInfo.getOrderDate()+",orderno="+transOrderInfo.getOrderNo()
					+",orderpackageno="+transOrderInfo.getOrderPackageNo()+",paychannelid="+transOrderInfo.getPayChannelId()+",remark="+transOrderInfo.getRemark()
					+",productid="+productId+",requestno="+transOrderInfo.getRequestNo()+",requesttime="+transOrderInfo.getRequestTime()+",status="+transOrderInfo.getStatus()
					+",tradeflowno="+transOrderInfo.getTradeFlowNo()+",userfee="+transOrderInfo.getUserFee()+",feeamount="+transOrderInfo.getFeeAmount()
					+",profit="+transOrderInfo.getProfit()+",busitypeid="+transOrderInfo.getBusiTypeId()+",bankcode="+transOrderInfo.getBankCode()+",errorcode="+transOrderInfo.getErrorCode()
					+",errormsg="+transOrderInfo.getErrorMsg()+",useripaddress="+transOrderInfo.getUserIpAddress());
			ErrorResponse response=new ErrorResponse();
			String reCode = "C0";
			String reMsg = "成功";
			String[] productIdStrings = {productId};
			//校验交易码
			if (!TransCodeConst.ADJUST_ACCOUNT_AMOUNT.equals(transOrderInfo.getFuncCode())){
				logger.info("转账交易码错误！订单中的交易码为"+transOrderInfo.getFuncCode());
				response.setCode("C1");
				response.setMsg("转账交易码错误！");
				return response;
			}
			//校验转出方与转入方是否相同
			if(!productId.equals(Constants.HT_PRODUCT)){
				if(transOrderInfo.getUserId().equals(transOrderInfo.getInterMerchantCode())){
					logger.info("同一个账户之间不能进行转账");
					response.setCode("C1");
					response.setMsg("同一个账户之间不能进行转账！");
					return response;
				}				
			}
			//校验订单号是否存在
			if(!this.orderNoChk(transOrderInfo.getOrderNo(),transOrderInfo.getMerchantCode())){
				logger.info("订单号："+transOrderInfo.getOrderNo()+"在机构号："+transOrderInfo.getMerchantCode()+"中已存在");
				response.setCode("C1");
				response.setMsg("该交易订单号已存在，请确认！");
				return response;
			}
			//获取每个账户记账流水
			List<FinanaceEntry> finanaceEntries=new ArrayList<FinanaceEntry>();
			//获取所有账户记账流水
			List<FinanaceEntry> finanaceEntrieAlls=new ArrayList<FinanaceEntry>();
			User user=new User();
			user.userId=userId;
			user.constId=transOrderInfo.getMerchantCode();
			//记录原有产品号信息
			String oldProduct=productId;
			//user.uEType=AccountConstants.ACCOUNT_TYPE_BASE;
			//判断是否为会唐与会唐之间转账的产品号
			if(transOrderInfo.getMerchantCode().equals(Constants.HT_ID)&& productId.equals(Constants.HT_TRANSFER_PRODUCT_STRING)){
				user.productId=Constants.HT_PRODUCT;
				productId=Constants.HT_PRODUCT;
			}else{
				user.productId=productId;
			}	
			
			//判断账户状态是否正常
			boolean accountIsOK=operationService.checkAccount(user);
			if(!accountIsOK){
				logger.error("用户"+userId+"状态为非正常状态");
				reCode = "C1";
				reMsg = "账户状态非正常状态";
			}else{
				//转出方的产品号暂存到errorCode,二期需要考虑通用性
				transOrderInfo.setErrorCode(productId);
				//判断订单信息是否有误
				String msg=checkInfoService.checkTradeInfo(transOrderInfo);
				if(!"ok".equals(msg)){
					logger.error(msg);
					reCode = "C2";
					reMsg = msg;
				}else{
					transOrderInfo.setErrorCode(oldProduct);
					try {
						//获取套录号
						String entryId=redisIdGenerator.createRequestNo();
						boolean flag=false;
						Balance balance=checkInfoService.getBalance(user,"");
						if(null!=balance){
							balance.setPulseDegree(balance.getPulseDegree()+1);
							finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, balance, entryId, flag);
							if(null!=finanaceEntries&&finanaceEntries.size()>0){
								for(FinanaceEntry finanaceEntry:finanaceEntries) {
									finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);	
									finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
									if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
										finanaceEntry.setRemark("转账");
									}
									finanaceEntrieAlls.add(finanaceEntry);
								}
							}else{
								logger.error("转入方账户信息有误");
								reCode = "C3";
								reMsg = "转入方账户信息有误";
							}							
						}else{
							logger.error("获取用户["+userId+"]余额信息失败");
							reCode = "C4";
							reMsg = "用户余额查询失败";
						}
						//批量插入记录流水表   写入数据失败尝试三次
						if(reCode.equals("C0")){
							try {
								insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo, productIdStrings);
							} catch (AccountException e) {
								logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败一次");
								try {
									insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo, productIdStrings);
								} catch (AccountException e1) {
									logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败两次");
									try {
										insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo, productIdStrings);
									} catch (AccountException e2) {
										//发送邮件或短信通知管理员
										logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败三次");
										//启用线程 发送邮件
										RkylinMailUtil.sendMailThread("账户记账流水失败通知","******************账户"+transOrderInfo.getUserId()+"做转账记账流水数据入库连续失败三次，转入方"+transOrderInfo.getInterMerchantCode(), TransCodeConst.FINANACE_ENTRY_ERROR_TOEMAIL);
										logger.error(e2.getMessage());
										reCode = "C5";
										reMsg = "数据库操作异常";
									}
								}
							}
						}
						
					} catch (AccountException e) {
						logger.error(e.getMessage());
						reCode = "C5";
						reMsg = "数据库操作异常";
					}
				}
			}
			if(reCode.equals("C0")){
				response.setIs_success(true);
			}else{
				response.setCode(reCode);
				response.setMsg(reMsg);
			}
			logger.info("---------------------------账户UserId"+userId+"向账户"+transOrderInfo.getInterMerchantCode()+"转入"+transOrderInfo.getAmount()+"分，转账操作结束------------------------");
			return response;
		}
	}

	@Override
	public ErrorResponse afterspendingrefund(TransOrderInfo transOrderInfo,String productId,String userId,String referUserId) {
		// TODO 消费后退款----转账交易
		synchronized (lock) {
			logger.info("---------------------------账户UserId"+userId+"消费后退款操作开始------------------------");
			logger.info("消费后退款操作参数信息：amount="+transOrderInfo.getAmount()+",UserId="+transOrderInfo.getUserId()+",funccode="+transOrderInfo.getFuncCode()
					+",intermerchantcode="+transOrderInfo.getInterMerchantCode()+",merchantcode="+transOrderInfo.getMerchantCode()+",orderamount="+transOrderInfo.getOrderAmount()
					+",ordercount="+transOrderInfo.getOrderCount()+",orderdate="+transOrderInfo.getOrderDate()+",orderno="+transOrderInfo.getOrderNo()
					+",orderpackageno="+transOrderInfo.getOrderPackageNo()+",paychannelid="+transOrderInfo.getPayChannelId()+",remark="+transOrderInfo.getRemark()
					+",productid="+productId+",requestno="+transOrderInfo.getRequestNo()+",requesttime="+transOrderInfo.getRequestTime()+",status="+transOrderInfo.getStatus()
					+",tradeflowno="+transOrderInfo.getTradeFlowNo()+",userfee="+transOrderInfo.getUserFee()+",feeamount="+transOrderInfo.getFeeAmount()
					+",profit="+transOrderInfo.getProfit()+",busitypeid="+transOrderInfo.getBusiTypeId()+",bankcode="+transOrderInfo.getBankCode()+",errorcode="+transOrderInfo.getErrorCode()
					+",errormsg="+transOrderInfo.getErrorMsg()+",useripaddress="+transOrderInfo.getUserIpAddress()+",referUserId="+referUserId);
			ErrorResponse response=new ErrorResponse();
			String reCode = "C0";
			String reMsg = "成功";
			String[] productIdStrings = {productId};
			//校验交易码
			if (!TransCodeConst.AFTER_SPENDING_REFUND.equals(transOrderInfo.getFuncCode())){
				logger.info("消费后退款交易码错误！订单中的交易码为"+transOrderInfo.getFuncCode());
				response.setCode("C1");
				response.setMsg("消费后退款交易码错误！");
				return response;
			}
			//校验订单号是否存在
			if(!this.orderNoChk(transOrderInfo.getOrderNo(),transOrderInfo.getMerchantCode())){
				logger.info("订单号："+transOrderInfo.getOrderNo()+"在机构号："+transOrderInfo.getMerchantCode()+"中已存在");
				response.setCode("C1");
				response.setMsg("该交易订单号已存在，请确认！");
				return response;
			}
			//获取每个账户记账流水
			List<FinanaceEntry> finanaceEntries=new ArrayList<FinanaceEntry>();
			//获取所有账户记账流水
			List<FinanaceEntry> finanaceEntrieAlls=new ArrayList<FinanaceEntry>();
			//根据订单号获取订单信息
			TransOrderInfoQuery query=new TransOrderInfoQuery();
			query.setOrderPackageNo(transOrderInfo.getOrderPackageNo());
			query.setFuncCode(transOrderInfo.getFuncCode());
			//---------------------添加机构号-------------------
			query.setMerchantCode(transOrderInfo.getMerchantCode());
			List<TransOrderInfo> transOrderInfos=transOrderInfoManager.queryList(query);
			if(transOrderInfos!=null&&transOrderInfos.size()>0){
				logger.error("该订单"+transOrderInfo.getOrderPackageNo()+"已经退款，不能重复退款");
				response.setCode("C1");
				response.setMsg("该订单"+transOrderInfo.getOrderPackageNo()+"已经退款，不能重复退");
				return response;
			}
			User user=new User();
			user.userId=userId;
			user.constId=transOrderInfo.getMerchantCode();
			user.uEType=AccountConstants.ACCOUNT_TYPE_BASE;
			user.productId=productId;
			//判断账户状态是否正常
			boolean accountIsOK=operationService.checkAccount(user);
			if(!accountIsOK){
				logger.error("用户"+userId+"状态为非正常状态");
				reCode = "C1";
				reMsg = "账户状态非正常状态";
			}else{
				//判断订单信息是否有误
				String msg=checkInfoService.checkTradeInfo(transOrderInfo);
				if(!"ok".equals(msg)){
					logger.error(msg);
					reCode = "C2";
					reMsg = msg;
				}else{
					try {
						//获取套录号
						String entryId=redisIdGenerator.createRequestNo();
						boolean flag=false;
						//默认获取丰年企业主账户余额
						String merchantCode = transOrderInfo.getMerchantCode();
						String finanaceAccountId = getBusiMasterAccountId(merchantCode);
						Balance balance=checkInfoService.getBalance(user,finanaceAccountId);
						if(null!=balance){
							balance.setPulseDegree(balance.getPulseDegree()+1);
							//用交易订单表中的错误编码存储管理分组
							transOrderInfo.setErrorCode(productId);
							finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, balance, entryId, flag);
							if(null!=finanaceEntries&&finanaceEntries.size()>0){
								for(FinanaceEntry finanaceEntry:finanaceEntries) {
									finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);	
									if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
										finanaceEntry.setRemark("消费后退款");
									}
									finanaceEntrieAlls.add(finanaceEntry);
								}
								//用交易订单表中的错误信息存储referUserId
								transOrderInfo.setErrorMsg(referUserId);
							}else{
								logger.error("获取用户["+userId+"]账户流水信息失败");
								reCode = "C3";
								reMsg = "账户流水数据入库失败";
							}							
						}else{
							logger.error("获取用户["+userId+"]余额信息失败");
							reCode = "C4";
							reMsg = "用户余额查询失败";
						}
						//批量插入记录流水表   写入数据失败尝试三次
						if(reCode.equals("C0")){
							try {
								insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo, productIdStrings);
							} catch (AccountException e) {
								logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败一次");
								try {
									insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo, productIdStrings);
								} catch (AccountException e1) {
									logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败两次");
									try {
										insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo, productIdStrings);
									} catch (AccountException e2) {
										//发送邮件或短信通知管理员
										logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败三次");
										//启用线程 发送邮件
										RkylinMailUtil.sendMailThread("账户记账流水失败通知","******************账户"+transOrderInfo.getUserId()+"做转账记账流水数据入库连续失败三次，转入方"+transOrderInfo.getInterMerchantCode(), TransCodeConst.FINANACE_ENTRY_ERROR_TOEMAIL);
										logger.error(e2.getMessage());
										reCode = "C5";
										reMsg = "数据库操作异常";
									}
									
								}
							}
						}
						
					} catch (AccountException e) {
						logger.error(e.getMessage());
						reCode = "C5";
						reMsg = "数据库操作异常";
					}
				}
			}
			if(reCode.equals("C0")){
				response.setIs_success(true);
			}else{
				response.setCode(reCode);
				response.setMsg(reMsg);
			}
			logger.info("---------------------------账户UserId"+userId+"消费后退款操作结束------------------------");
			return response;
		}
	}
	
	//根据传入的机构码获取该机构的主账户finanaceAccountId
	private String getBusiMasterAccountId(String constId){
		String finanaceAccountId = "";
		if("M000001".equals(constId)){
			finanaceAccountId = TransCodeConst.THIRDPARTYID_FNZZHQY;
		}else if(Constants.HT_ID.equals(constId)){
			finanaceAccountId = TransCodeConst.THIRDPARTYID_HTZZH;
		}else if(Constants.KZ_ID.equals(constId)){
			finanaceAccountId = TransCodeConst.THIRDPARTYID_KZP2PZZH;
		}else if(Constants.JRD_ID.equals(constId)){
			finanaceAccountId = TransCodeConst.THIRDPARTYID_JRDQYZZH;
		}else if(Constants.SQSM_ID.equals(constId)){
			finanaceAccountId = TransCodeConst.THIRDPARTYID_SQSMQYZZH;
		}else if(Constants.MZ_ID.equals(constId)){
			finanaceAccountId = TransCodeConst.THIRDPARTYID_MZQYZZH;
		}else if(Constants.YNC_ID.equals(constId)){
			finanaceAccountId = TransCodeConst.THIRDPARTYID_YNCZZH;
		}else if(Constants.ZJWK_ID.equals(constId)){
			finanaceAccountId = TransCodeConst.THIRDPARTYID_ZJWKZZH;
		}else if(Constants.MYHR_ID.equals(constId)){
			finanaceAccountId = TransCodeConst.THIRDPARTYID_MYHRZZH;
		}else if("M000011".equals(constId)){
			finanaceAccountId = TransCodeConst.THIRDPARTYID_ZKZZH;
		}else if("M000012".equals(constId)){
			finanaceAccountId = TransCodeConst.THIRDPARTYID_ZJDYZZH;
		}else{
			finanaceAccountId = TransCodeConst.THIRDPARTYID_FNZZHQY;
		}
		logger.info("根据机构码取得相应企业主账户finanaceAccountId : " + finanaceAccountId);
		return finanaceAccountId;
	}
	
	@Override	
	public ErrorResponse withdrow(TransOrderInfo transOrderInfo,String productId, String userId,String referUserId) {
		// TODO 提现
		synchronized (lock) {
			logger.info("---------------------------账户UserId"+userId+"提现操作开始------------------------");
			logger.info("提现操作参数信息：amount="+transOrderInfo.getAmount()+",UserId="+transOrderInfo.getUserId()+",funccode="+transOrderInfo.getFuncCode()
					+",intermerchantcode="+transOrderInfo.getInterMerchantCode()+",merchantcode="+transOrderInfo.getMerchantCode()+",orderamount="+transOrderInfo.getOrderAmount()
					+",ordercount="+transOrderInfo.getOrderCount()+",orderdate="+transOrderInfo.getOrderDate()+",orderno="+transOrderInfo.getOrderNo()
					+",orderpackageno="+transOrderInfo.getOrderPackageNo()+",paychannelid="+transOrderInfo.getPayChannelId()+",remark="+transOrderInfo.getRemark()
					+",productid="+productId+",requestno="+transOrderInfo.getRequestNo()+",requesttime="+transOrderInfo.getRequestTime()+",status="+transOrderInfo.getStatus()
					+",tradeflowno="+transOrderInfo.getTradeFlowNo()+",userfee="+transOrderInfo.getUserFee()+",feeamount="+transOrderInfo.getFeeAmount()
					+",profit="+transOrderInfo.getProfit()+",busitypeid="+transOrderInfo.getBusiTypeId()+",bankcode="+transOrderInfo.getBankCode()+",errorcode="+transOrderInfo.getErrorCode()
					+",errormsg="+transOrderInfo.getErrorMsg()+",useripaddress="+transOrderInfo.getUserIpAddress()+",referUserId="+referUserId);
			ErrorResponse response=new ErrorResponse();
			String reCode = "C0";
			String reMsg = "成功";
			String[] productStrings = {productId};
			//校验交易码
			if (!TransCodeConst.WITHDROW.equals(transOrderInfo.getFuncCode())){
				logger.info("提现交易码错误！订单中的交易码为"+transOrderInfo.getFuncCode());
				response.setCode("C1");
				response.setMsg("提现交易码错误！");
				return response;
			}
			//校验订单号是否存在
			if(!this.orderNoChk(transOrderInfo.getOrderNo(),transOrderInfo.getMerchantCode())){
				logger.info("订单号："+transOrderInfo.getOrderNo()+"在机构号："+transOrderInfo.getMerchantCode()+"中已存在");
				response.setCode("C1");
				response.setMsg("该交易订单号已存在，请确认！");
				return response;
			}
			//获取每个账户记账流水
			List<FinanaceEntry> finanaceEntries=new ArrayList<FinanaceEntry>();
			//获取所有账户记账流水
			List<FinanaceEntry> finanaceEntrieAlls=new ArrayList<FinanaceEntry>();
			User user=new User();
			user.userId=userId;
			user.constId=transOrderInfo.getMerchantCode();
			user.productId=productId;
			user.referUserId=referUserId;
			//判断账户状态是否正常
			boolean accountIsOK=operationService.checkAccount(user);
			if(!accountIsOK){
				logger.error("用户"+userId+"状态为非正常状态");
				reCode = "C1";
				reMsg = "账户状态非正常状态";
			}else{
				//判断订单信息是否有误
				String msg=checkInfoService.checkTradeInfo(transOrderInfo);
				if(!"ok".equals(msg)){
					logger.error(msg);
					reCode = "C2";
					reMsg = msg;
				}else{
					try {
						//获取套录号
						String entryId=redisIdGenerator.createRequestNo();
						for (int i = 0; i <= 0; i++) {
							boolean flag=true;
							
							if(0==i){
								userId=transOrderInfo.getUserId();	
								flag=true;
							}else{
								userId=TransCodeConst.THIRDPARTYID_TXDQSZH;
								flag=false;
								
							}
							user.userId=userId;
							Balance balance=null;
							if(flag){
								balance=checkInfoService.getBalance(user,"");
							}else{
								balance=checkInfoService.getBalance(user,userId);
							}
							if(null!=balance){
								balance.setPulseDegree(balance.getPulseDegree()+1);
								finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, balance, entryId, flag);
								if(null!=finanaceEntries&&finanaceEntries.size()>0){
									for(FinanaceEntry finanaceEntry:finanaceEntries) {
										finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
										finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
										if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
											finanaceEntry.setRemark("提现");
										}
										finanaceEntrieAlls.add(finanaceEntry);
									}
								}else{
									logger.error("获取用户["+userId+"]账户流水信息失败");
									reCode = "C3";
									reMsg = "账户流水数据入库失败";
									break;
								}							
							}else{
								logger.error("获取用户["+userId+"]余额信息失败");
								reCode = "C4";
								reMsg = "用户余额查询失败";
							}
						}
						//批量插入记录流水表   写入数据失败尝试三次
						if(reCode.equals("C0")){
							try {
								insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo, productStrings);
							} catch (AccountException e) {
								logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败一次");
								try {
									insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo, productStrings);
								} catch (AccountException e1) {
									logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败两次");
									try {
										insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo, productStrings);
									} catch (AccountException e2) {
										//发送邮件或短信通知管理员
										logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败三次");
										//启用线程 发送邮件
										RkylinMailUtil.sendMailThread("账户记账流水失败通知","******************账户"+transOrderInfo.getUserId()+"做提现记账流水数据入库连续失败三次", TransCodeConst.FINANACE_ENTRY_ERROR_TOEMAIL);
										logger.error(e2.getMessage());
										reCode = "C5";
										reMsg = "数据库操作异常";
									}
									
								}
							}
						}
						
						//***************通知代收付现改为日结线程定时汇总批量处理用户提现数据*************
					} catch (AccountException e) {
						// TODO: handle exception
						logger.error(e.getMessage());
						reCode = "C5";
						reMsg = "数据库操作异常";
					}
				}
			}
			if(reCode.equals("C0")){
				response.setIs_success(true);
			}else{
				response.setCode(reCode);
				response.setMsg(reMsg);
			}
			logger.info("---------------------------账户UserId"+userId+"提现操作结束------------------------");
			return response;
		}		
	}

	@Override
	public ErrorResponse antiDeduct(TransOrderInfo transOrderInfo) {
		// TODO 扣款冲正
		synchronized (lock) {
			logger.info("---------------------------账户UserId"+transOrderInfo.getUserId()+"冲正操作开始------------------------");
			logger.info("商户扣款冲正参数：funccode="+transOrderInfo.getFuncCode()+",orderno="+transOrderInfo.getOrderNo()+",useripaddress="+transOrderInfo.getUserIpAddress()
					+",orderpackageno="+transOrderInfo.getOrderPackageNo());
			ErrorResponse response=new ErrorResponse();
			String reCode = "C0";
			String reMsg = "成功";
			String[] productIdStrings={""};
			//校验交易码
			if (!TransCodeConst.MERCHANT_RT.equals(transOrderInfo.getFuncCode())
					&& !TransCodeConst.SETTLEMENT_RT.equals(transOrderInfo.getFuncCode())
					&& !TransCodeConst.MANUAL_RT.equals(transOrderInfo.getFuncCode())){
				logger.info("扣款冲正交易码错误！订单中的交易码为"+transOrderInfo.getFuncCode());
				response.setCode("C1");
				response.setMsg("扣款冲正交易码错误！");
				return response;
			}
			//校验订单号是否存在
			if(!this.orderNoChk(transOrderInfo.getOrderNo(),transOrderInfo.getMerchantCode())){
				logger.info("订单号："+transOrderInfo.getOrderNo()+"在机构号："+transOrderInfo.getMerchantCode()+"中已存在");
				response.setCode("C1");
				response.setMsg("该交易订单号已存在，请确认！");
				return response;
			}
			//获取每个账户记账流水
			List<FinanaceEntry> finanaceEntries=new ArrayList<FinanaceEntry>();
			//根据订单号获取订单信息
			TransOrderInfoQuery query=new TransOrderInfoQuery();
			query.setOrderPackageNo(transOrderInfo.getOrderPackageNo());
			query.setFuncCode(transOrderInfo.getFuncCode());
			//---------------------添加机构号-------------------
			query.setMerchantCode(transOrderInfo.getMerchantCode());
			List<TransOrderInfo> transOrderInfos=transOrderInfoManager.queryList(query);
			if(transOrderInfos==null||transOrderInfos.size()==0){
				query.setOrderNo(transOrderInfo.getOrderPackageNo());
				query.setFuncCode(null);
				query.setOrderPackageNo(null);
				transOrderInfos=transOrderInfoManager.queryList(query);
			}else{
				logger.error("该订单已经冲正");
				response.setCode("C1");
				response.setMsg("该订单已经冲正,不能重复冲正");
				return response;
			}
			if(transOrderInfos==null||transOrderInfos.size()!=1){
				logger.error("获取订单流水信息为空");
				reCode = "C1";
				reMsg = "获取订单流水信息为空";
			}else{
				
				//获取冲正交易号
				String funcCode=transOrderInfo.getFuncCode();
				String orderNo=transOrderInfo.getOrderNo();
				String oldOrderNo=transOrderInfo.getOrderPackageNo();
				transOrderInfo=transOrderInfos.get(0);
				transOrderInfo.setFuncCode(funcCode);
				transOrderInfo.setOrderNo(orderNo);
				transOrderInfo.setOrderPackageNo(oldOrderNo);
				//----------------添加机构------------------
				transOrderInfo.setMerchantCode(transOrderInfo.getMerchantCode());
				//根据订单号获取记账流水信息
				List<FinanaceEntry> finanaceEntrieList=checkInfoService.getFinanaceEntrieByOrderId(String.valueOf(transOrderInfo.getRequestId()));
				if(finanaceEntrieList==null||finanaceEntrieList.size()<=0){
					logger.error("获取流水信息为空");
					reCode = "C1";
					reMsg = "获取流水信息为空";
				}else{
					try {
						//获取套录号
						String newEntryId=redisIdGenerator.createBatchRequestNo();
						//获取冲正账期
						String reverseTime=operationService.getAccountDate();
						//int resultNum=operationService.getAccountcount(finanaceEntrieList);
						int resultNum=1;
						if(newEntryId != null && reverseTime!=null){
							String accountID = "";
							FinanaceEntry finanaceEntry = null;
							Balance balance=new Balance();
							User user=new User();
							
							/* 为所有的账目流水全部生成一笔反向交易 */
							for(FinanaceEntry finanaceEn : finanaceEntrieList){								
								if(!accountID.equals(finanaceEn.getFinAccountId())){
									accountID=finanaceEn.getFinAccountId();
									balance=checkInfoService.getBalance(user, finanaceEn.getFinAccountId());
									balance.setPulseDegree(balance.getPulseDegree()+1);
								}else{
									balance.setPulseDegree(balance.getPulseDegree()+1);
								}
								/* 发生额类型:清算余额.贷借方向:C贷 */
								if(BaseConstants.TYPE_BALANCE_SETTLE==finanaceEn.getAccrualType()&&BaseConstants.CREDIT_TYPE==finanaceEn.getDirection()){
									balance.setBalanceUsable(balance.getBalanceUsable()-finanaceEn.getPaymentAmount());//账户可用余额
									balance.setBalanceSettle(balance.getBalanceSettle()-finanaceEn.getPaymentAmount());//账户清算余额
									if(resultNum==1){
										balance.setAmount(balance.getAmount()-finanaceEn.getPaymentAmount());//账户余额
									}
								}
								/* 发生额类型:清算余额.贷借方向:D借 */
								if(BaseConstants.TYPE_BALANCE_SETTLE==finanaceEn.getAccrualType()&&BaseConstants.DEBIT_TYPE==finanaceEn.getDirection()){
									balance.setBalanceUsable(balance.getBalanceUsable()+finanaceEn.getPaymentAmount());//账户可用余额
									balance.setBalanceSettle(balance.getBalanceSettle()+finanaceEn.getPaymentAmount());//账户清算余额
									if(resultNum==1){
										balance.setAmount(balance.getAmount()+finanaceEn.getPaymentAmount());//账户余额
									}
								}
								/* 发生额类型:冻结余额.贷借方向:C贷 */
								if(BaseConstants.TYPE_BALANCE_FROZON==finanaceEn.getAccrualType()&&BaseConstants.CREDIT_TYPE==finanaceEn.getDirection()){
									balance.setBalanceFrozon(balance.getBalanceFrozon()-finanaceEn.getPaymentAmount());//账户冻结余额
									if(resultNum==1){
										balance.setAmount(balance.getAmount()-finanaceEn.getPaymentAmount());//账户余额
									}
								}
								/* 发生额类型:冻结余额.贷借方向:D借 */
								if(BaseConstants.TYPE_BALANCE_FROZON==finanaceEn.getAccrualType()&&BaseConstants.DEBIT_TYPE==finanaceEn.getDirection()){
									balance.setBalanceFrozon(balance.getBalanceFrozon()+finanaceEn.getPaymentAmount());//账户冻结余额
									if(resultNum==1){
										balance.setAmount(balance.getAmount()+finanaceEn.getPaymentAmount());//账户余额
									}
								}
								/* 发生额类型:贷记余额.贷借方向:C贷 */
								if(BaseConstants.TYPE_BALANCE_CREDIT==finanaceEn.getAccrualType()&&BaseConstants.CREDIT_TYPE==finanaceEn.getDirection()){
									balance.setBalanceUsable(balance.getBalanceUsable()-finanaceEn.getPaymentAmount());//账户可用余额
									balance.setBalanceCredit(balance.getBalanceCredit()-finanaceEn.getPaymentAmount());//账户贷记余额
									if(resultNum==1){
										balance.setAmount(balance.getAmount()-finanaceEn.getPaymentAmount());//账户余额
									}
								}
								/* 发生额类型:贷记余额.贷借方向:D借 */
								if(BaseConstants.TYPE_BALANCE_CREDIT==finanaceEn.getAccrualType()&&BaseConstants.DEBIT_TYPE==finanaceEn.getDirection()){
									balance.setBalanceUsable(balance.getBalanceUsable()+finanaceEn.getPaymentAmount());//账户可用余额
									balance.setBalanceCredit(balance.getBalanceCredit()+finanaceEn.getPaymentAmount());//账户贷记余额
									if(resultNum==1){
										balance.setAmount(balance.getAmount()+finanaceEn.getPaymentAmount());//账户余额
									}
								}
								/* 发生交易流水 */
								finanaceEntry = operationService.addReverseTrade(finanaceEn, balance, newEntryId, reverseTime);
								if(finanaceEntry == null){
									logger.error("生成对应交易流水的反流水失败");
									reCode = "C3";
									reMsg = "生成对应交易流水的反流水失败";
									break;
								}
								finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
								if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
									finanaceEntry.setRemark("冲正");
								}
								finanaceEntries.add(finanaceEntry);
							}
							//将订单流水Id制空
							//transOrderInfo.setRequestId(null);
							//设置新订单创建时间和更新时间为null;
							transOrderInfo.setCreatedTime(null);
							transOrderInfo.setUpdatedTime(null);
							//批量插入记录流水表   写入数据失败尝试三次
							if(reCode.equals("C0")){
								try {
									insertFinanaceEntry(finanaceEntries, transOrderInfo, productIdStrings);
								} catch (AccountException e) {
									logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败一次");
									try {
										insertFinanaceEntry(finanaceEntries, transOrderInfo, productIdStrings);
									} catch (AccountException e1) {
										logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败两次");
										try {
											insertFinanaceEntry(finanaceEntries, transOrderInfo, productIdStrings);
										} catch (AccountException e2) {
											//发送邮件或短信通知管理员
											logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败三次");
											//启用线程 发送邮件
											RkylinMailUtil.sendMailThread("账户记账流水失败通知","******************账户"+transOrderInfo.getUserId()+"做商户扣款冲正记账流水数据入库连续失败三次", TransCodeConst.FINANACE_ENTRY_ERROR_TOEMAIL);
											logger.error(e2.getMessage());
											reCode = "C5";
											reMsg = "数据库操作异常";
										}
									}
								}
							}
						}
							
					} catch (AccountException e) {
						logger.error(e.getMessage());
						reCode = "C4";
						reMsg = "数据库操作异常";
					}
				}
			}		
			if(reCode.equals("C0")){
				response.setIs_success(true);
			}else{
				response.setCode(reCode);
				response.setMsg(reMsg);
			}
			logger.info("---------------------------账户UserId"+transOrderInfo.getUserId()+"冲正操作结束------------------------");
			return response;
		}
	}
	
	@Override
	public ErrorResponse consumptionbeforerefund(TransOrderInfo transOrderInfo) {
		// TODO 消费前退款 ---相当于冲正操作
		synchronized (lock) {
			logger.info("---------------------------账户UserId"+transOrderInfo.getUserId()+"消费前退款操作开始------------------------");
			logger.info("消费前退款参数：funccode="+transOrderInfo.getFuncCode()+",orderno="+transOrderInfo.getOrderNo()+",useripaddress="+transOrderInfo.getUserIpAddress()
					+",orderpackageno="+transOrderInfo.getOrderPackageNo());
			ErrorResponse response=new ErrorResponse();
			String reCode = "C0";
			String reMsg = "成功";
			String[] productIdStrings={""};
			//校验交易码
			if (!TransCodeConst.CONSUMPTION_BEFER_REFUND.equals(transOrderInfo.getFuncCode())){
				logger.info("消费前退款交易码错误！订单中的交易码为"+transOrderInfo.getFuncCode());
				response.setCode("C1");
				response.setMsg("消费前退款交易码错误！");
				return response;
			}
			//校验订单号是否存在
			if(!this.orderNoChk(transOrderInfo.getOrderNo(),transOrderInfo.getMerchantCode())){
				logger.info("订单号："+transOrderInfo.getOrderNo()+"在机构号："+transOrderInfo.getMerchantCode()+"中已存在");
				response.setCode("C1");
				response.setMsg("该交易订单号已存在，请确认！");
				return response;
			}
			//获取每个账户记账流水
			List<FinanaceEntry> finanaceEntries=new ArrayList<FinanaceEntry>();
			//根据订单号获取订单信息
			TransOrderInfoQuery query=new TransOrderInfoQuery();
			query.setOrderPackageNo(transOrderInfo.getOrderPackageNo());
			query.setFuncCode(transOrderInfo.getFuncCode());
			//----------------添加机构------------------
			query.setMerchantCode(transOrderInfo.getMerchantCode());
			List<TransOrderInfo> transOrderInfos=transOrderInfoManager.queryList(query);
			if(transOrderInfos==null||transOrderInfos.size()==0){
				query.setOrderNo(transOrderInfo.getOrderPackageNo());
				query.setFuncCode(null);
				query.setOrderPackageNo(null);
				transOrderInfos=transOrderInfoManager.queryList(query);
			}else{
				logger.error("该订单已经退款");
				response.setCode("C1");
				response.setMsg("该订单已经退款,不能重复退款");
				return response;
			}
			if(transOrderInfos==null||transOrderInfos.size()!=1){
				logger.error("获取订单流水信息为空");
				reCode = "C1";
				reMsg = "获取订单流水信息为空";
			}else{
				
				//获取冲正交易号
				String funcCode=transOrderInfo.getFuncCode();
				String orderNo=transOrderInfo.getOrderNo();
				String oldOrderNo=transOrderInfo.getOrderPackageNo();
				transOrderInfo=transOrderInfos.get(0);
				transOrderInfo.setFuncCode(funcCode);
				transOrderInfo.setOrderNo(orderNo);
				transOrderInfo.setOrderPackageNo(oldOrderNo);
				//----------------添加机构------------------
				transOrderInfo.setMerchantCode(transOrderInfo.getMerchantCode());
				//根据订单号获取记账流水信息
				List<FinanaceEntry> finanaceEntrieList=checkInfoService.getFinanaceEntrieByOrderId(String.valueOf(transOrderInfo.getRequestId()));
				if(finanaceEntrieList==null||finanaceEntrieList.size()<=0){
					logger.error("获取流水信息为空");
					reCode = "C1";
					reMsg = "获取流水信息为空";
				}else{
					try {
						//获取套录号
						String newEntryId=redisIdGenerator.createBatchRequestNo();
						//获取冲正账期
						String reverseTime=operationService.getAccountDate();
						int resultNum=operationService.getAccountcount(finanaceEntrieList);
						if(newEntryId != null && reverseTime!=null){
							String accountID = "";
							FinanaceEntry finanaceEntry = null;
							Balance balance=new Balance();
							User user=new User();
							/* 为所有的账目流水全部生成一笔反向交易 */
							for(FinanaceEntry finanaceEn : finanaceEntrieList){								
								if(!accountID.equals(finanaceEn.getFinAccountId())){
									accountID=finanaceEn.getFinAccountId();
									balance=checkInfoService.getBalance(user, finanaceEn.getFinAccountId());
									balance.setPulseDegree(balance.getPulseDegree()+1);
								}else{
									balance.setPulseDegree(balance.getPulseDegree()+1);
								}
								/* 发生额类型:清算余额.贷借方向:C贷 */
								if(BaseConstants.TYPE_BALANCE_SETTLE==finanaceEn.getAccrualType()&&BaseConstants.CREDIT_TYPE==finanaceEn.getDirection()){
									balance.setBalanceUsable(balance.getBalanceUsable()-finanaceEn.getPaymentAmount());//账户可用余额
									balance.setBalanceSettle(balance.getBalanceSettle()-finanaceEn.getPaymentAmount());//账户清算余额
									if(resultNum==1){
										balance.setAmount(balance.getAmount()-finanaceEn.getPaymentAmount());//账户余额
									}
								}
								/* 发生额类型:清算余额.贷借方向:D借 */
								if(BaseConstants.TYPE_BALANCE_SETTLE==finanaceEn.getAccrualType()&&BaseConstants.DEBIT_TYPE==finanaceEn.getDirection()){
									balance.setBalanceUsable(balance.getBalanceUsable()+finanaceEn.getPaymentAmount());//账户可用余额
									balance.setBalanceSettle(balance.getBalanceSettle()+finanaceEn.getPaymentAmount());//账户清算余额
									if(resultNum==1){
										balance.setAmount(balance.getAmount()+finanaceEn.getPaymentAmount());//账户余额
									}
								}
								/* 发生额类型:冻结余额.贷借方向:C贷 */
								if(BaseConstants.TYPE_BALANCE_FROZON==finanaceEn.getAccrualType()&&BaseConstants.CREDIT_TYPE==finanaceEn.getDirection()){
									balance.setBalanceFrozon(balance.getBalanceFrozon()-finanaceEn.getPaymentAmount());//账户冻结余额
									if(resultNum==1){
										balance.setAmount(balance.getAmount()-finanaceEn.getPaymentAmount());//账户余额
									}
								}
								/* 发生额类型:冻结余额.贷借方向:D借 */
								if(BaseConstants.TYPE_BALANCE_FROZON==finanaceEn.getAccrualType()&&BaseConstants.DEBIT_TYPE==finanaceEn.getDirection()){
									balance.setBalanceFrozon(balance.getBalanceFrozon()+finanaceEn.getPaymentAmount());//账户冻结余额
									if(resultNum==1){
										balance.setAmount(balance.getAmount()+finanaceEn.getPaymentAmount());//账户余额
									}
								}
								/* 发生额类型:贷记余额.贷借方向:C贷 */
								if(BaseConstants.TYPE_BALANCE_CREDIT==finanaceEn.getAccrualType()&&BaseConstants.CREDIT_TYPE==finanaceEn.getDirection()){
									balance.setBalanceUsable(balance.getBalanceUsable()-finanaceEn.getPaymentAmount());//账户可用余额
									balance.setBalanceCredit(balance.getBalanceCredit()-finanaceEn.getPaymentAmount());//账户贷记余额
									if(resultNum==1){
										balance.setAmount(balance.getAmount()-finanaceEn.getPaymentAmount());//账户余额
									}
								}
								/* 发生额类型:贷记余额.贷借方向:D借 */
								if(BaseConstants.TYPE_BALANCE_CREDIT==finanaceEn.getAccrualType()&&BaseConstants.DEBIT_TYPE==finanaceEn.getDirection()){
									balance.setBalanceUsable(balance.getBalanceUsable()+finanaceEn.getPaymentAmount());//账户可用余额
									balance.setBalanceCredit(balance.getBalanceCredit()+finanaceEn.getPaymentAmount());//账户贷记余额
									if(resultNum==1){
										balance.setAmount(balance.getAmount()+finanaceEn.getPaymentAmount());//账户余额
									}
								}
								/* 发生交易流水 */
								finanaceEntry = operationService.addReverseTrade(finanaceEn, balance, newEntryId, reverseTime);
								if(finanaceEntry == null){
									logger.error("生成对应交易流水的反流水失败");
									reCode = "C3";
									reMsg = "生成对应交易流水的反流水失败";
									break;
								}
								finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
								if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
									finanaceEntry.setRemark("消费前退款");
								}
								finanaceEntries.add(finanaceEntry);
							}
							//将订单流水Id制空
							//transOrderInfo.setRequestId(null);
							//设置新订单创建时间和更新时间为null;
							transOrderInfo.setCreatedTime(null);
							transOrderInfo.setUpdatedTime(null);
							//批量插入记录流水表   写入数据失败尝试三次
							if(reCode.equals("C0")){
								try {
									insertFinanaceEntry(finanaceEntries, transOrderInfo, productIdStrings);
								} catch (AccountException e) {
									logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败一次");
									try {
										insertFinanaceEntry(finanaceEntries, transOrderInfo, productIdStrings);
									} catch (AccountException e1) {
										logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败两次");
										try {
											insertFinanaceEntry(finanaceEntries, transOrderInfo, productIdStrings);
										} catch (AccountException e2) {
											//发送邮件或短信通知管理员
											logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败三次");
											//启用线程 发送邮件
											RkylinMailUtil.sendMailThread("账户记账流水失败通知","******************账户"+transOrderInfo.getUserId()+"做商户扣款冲正记账流水数据入库连续失败三次", TransCodeConst.FINANACE_ENTRY_ERROR_TOEMAIL);
											logger.error(e2.getMessage());
											reCode = "C5";
											reMsg = "数据库操作异常";
										}
									}
								}
							}
						}
					} catch (AccountException e) {
						logger.error(e.getMessage());
						reCode = "C4";
						reMsg = "数据库操作异常";
					}
				}
			}		
			if(reCode.equals("C0")){
				response.setIs_success(true);
			}else{
				response.setCode(reCode);
				response.setMsg(reMsg);
			}
			logger.info("---------------------------账户UserId"+transOrderInfo.getUserId()+"消费前退款操作结束------------------------");
			return response;
		}
	}

	@Override
	public ErrorResponse preauthorization(TransOrderInfo transOrderInfo,String productId, String referUserId) {
		// TODO 预授权  操作账户的可提现余额与冻结余额
		synchronized (lock) {
			logger.info("---------------------------账户UserId"+transOrderInfo.getUserId()+"预授权操作开始------------------------");
			logger.info("预授权操作参数信息：amount="+transOrderInfo.getAmount()+",UserId="+transOrderInfo.getUserId()+",funccode="+transOrderInfo.getFuncCode()
					+",intermerchantcode="+transOrderInfo.getInterMerchantCode()+",merchantcode="+transOrderInfo.getMerchantCode()+",orderamount="+transOrderInfo.getOrderAmount()
					+",ordercount="+transOrderInfo.getOrderCount()+",orderdate="+transOrderInfo.getOrderDate()+",orderno="+transOrderInfo.getOrderNo()
					+",orderpackageno="+transOrderInfo.getOrderPackageNo()+",paychannelid="+transOrderInfo.getPayChannelId()+",remark="+transOrderInfo.getRemark()
					+",productid="+productId+",requestno="+transOrderInfo.getRequestNo()+",requesttime="+transOrderInfo.getRequestTime()+",status="+transOrderInfo.getStatus()
					+",tradeflowno="+transOrderInfo.getTradeFlowNo()+",userfee="+transOrderInfo.getUserFee()+",feeamount="+transOrderInfo.getFeeAmount()
					+",profit="+transOrderInfo.getProfit()+",busitypeid="+transOrderInfo.getBusiTypeId()+",bankcode="+transOrderInfo.getBankCode()+",errorcode="+transOrderInfo.getErrorCode()
					+",errormsg="+transOrderInfo.getErrorMsg()+",useripaddress="+transOrderInfo.getUserIpAddress()+",referUserId="+referUserId);
			ErrorResponse response=new ErrorResponse();
			String reCode = "C0";
			String reMsg = "成功";
			String[] productIdStrings={"productId"};
			//校验交易码
			if (!TransCodeConst.PRE_AUTHORIZATION.equals(transOrderInfo.getFuncCode())){
				logger.info("预授权交易码错误！订单中的交易码为"+transOrderInfo.getFuncCode());
				response.setCode("C1");
				response.setMsg("预授权交易码错误！");
				return response;
			}
			//校验订单号是否存在
			if(!this.orderNoChk(transOrderInfo.getOrderNo(),transOrderInfo.getMerchantCode())){
				logger.info("订单号："+transOrderInfo.getOrderNo()+"在机构号："+transOrderInfo.getMerchantCode()+"中已存在");
				response.setCode("C1");
				response.setMsg("该交易订单号已存在，请确认！");
				return response;
			}
			String authCode=null;
			//获取UserId
			String userId=transOrderInfo.getUserId();
			//获取每个账户记账流水
			List<FinanaceEntry> finanaceEntries=new ArrayList<FinanaceEntry>();
			//获取所有账户记账流水
			List<FinanaceEntry> finanaceEntrieAlls=new ArrayList<FinanaceEntry>();
			//授权表对象
			FinanaceAccountAuth finanaceAccountAuthInfo=new FinanaceAccountAuth();
			User user=new User();
			user.userId=userId;
			user.constId=transOrderInfo.getMerchantCode();
			user.uEType=AccountConstants.ACCOUNT_TYPE_BASE;
			user.productId=productId;
			//判断账户状态是否正常
			boolean accountIsOK=operationService.checkAccount(user);
			if(!accountIsOK){
				logger.error("用户"+userId+"状态为非正常状态");
				reCode = "C1";
				reMsg = "账户状态非正常状态";
			}else{
				//判断订单信息是否有误
				String msg=checkInfoService.checkTradeInfo(transOrderInfo);
				if(!"ok".equals(msg)){
					logger.error(msg);
					reCode = "C2";
					reMsg = msg;
				}else{
					try {
						//获取套录号
						String entryId=redisIdGenerator.createRequestNo();
						boolean flag=false;
						//##############生成一笔预授权记账流水
						Balance balance=checkInfoService.getBalance(user,"");
						if(null!=balance){
							balance.setPulseDegree(balance.getPulseDegree()+1);
							finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, balance, entryId, flag);
							if(null!=finanaceEntries&&finanaceEntries.size()>0){
								for(FinanaceEntry finanaceEntry:finanaceEntries) {
									finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);	
									finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
									if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
										finanaceEntry.setRemark("预授权");
									}
									finanaceEntrieAlls.add(finanaceEntry);
								}
								//######################入预授权表数据设置
								//获取授权码
								authCode=redisIdGenerator.createAuthCode(10);								
								finanaceAccountAuthInfo.setAuthTypeId(transOrderInfo.getFuncCode());
								finanaceAccountAuthInfo.setFinAccountId(balance.getFinAccountId());
								finanaceAccountAuthInfo.setAuthCode(authCode);
								finanaceAccountAuthInfo.setReferEntryId(entryId);
								finanaceAccountAuthInfo.setAmount(transOrderInfo.getAmount());
								finanaceAccountAuthInfo.setStatusId(String.valueOf(Constants.AGMT_STATUS_1));							
							}else{
								if(finanaceEntries==null){
									logger.error("订单冻结金额大于用户"+userId+"可冻结金额");
									reCode = "C2";
									reMsg = "订单冻结金额大于用户"+userId+"可冻结金额";
								}else{
									logger.error("获取用户["+userId+"]账户流水信息失败");
									reCode = "C2";
									reMsg = "账户流水数据入库失败";
								}
							}							
						}else{
							logger.error("获取用户["+userId+"]余额信息失败");
							reCode = "C2";
							reMsg = "用户余额查询失败";
						}
						//批量插入记录流水表   写入数据失败尝试三次
						if(reCode.equals("C0")){
							try {
								insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo,finanaceAccountAuthInfo, productIdStrings);
							} catch (AccountException e) {
								logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败一次");
								try {
									insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo,finanaceAccountAuthInfo, productIdStrings);
								} catch (AccountException e1) {
									logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败两次");
									try {
										insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo,finanaceAccountAuthInfo, productIdStrings);
									} catch (AccountException e2) {
										//发送邮件或短信通知管理员
										logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败三次");
										//启用线程 发送邮件
										RkylinMailUtil.sendMailThread("账户记账流水失败通知","******************账户"+transOrderInfo.getUserId()+"做转账记账流水数据入库连续失败三次，转入方"+transOrderInfo.getInterMerchantCode(), TransCodeConst.FINANACE_ENTRY_ERROR_TOEMAIL);
										logger.error(e2.getMessage());
										reCode = "C5";
										reMsg = "数据库操作异常";
									}
									
								}
							}
						}
						
					} catch (AccountException e) {
						logger.error(e.getMessage());
						reCode = "C5";
						reMsg = "数据库操作异常";
					}
				}
			}
			if(reCode.equals("C0")){
				response.setAuthCode(authCode);
				response.setIs_success(true);
			}else{
				response.setCode(reCode);
				response.setMsg(reMsg);
			}
			logger.info("---------------------------账户UserId"+transOrderInfo.getUserId()+"预授权操作结束------------------------");
			return response;
		}
	}

	@Override
	public ErrorResponse preauthorizationcomplete(
			TransOrderInfo transOrderInfo, String productId, String referUserId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 实时代收：记录订单信息，生成记账流水
	 * @param transOrderInfo
	 * @return
	 */
	public ErrorResponse collectionRealTime(TransOrderInfo transOrderInfo) {
		logger.info("实时代收操作参数信息：amount="+transOrderInfo.getAmount()+",UserId="+transOrderInfo.getUserId()+",funccode="+transOrderInfo.getFuncCode()
				+",intermerchantcode="+transOrderInfo.getInterMerchantCode()+",merchantcode="+transOrderInfo.getMerchantCode()+",orderamount="+transOrderInfo.getOrderAmount()
				+",ordercount="+transOrderInfo.getOrderCount()+",orderdate="+transOrderInfo.getOrderDate()+",orderno="+transOrderInfo.getOrderNo()
				+",orderpackageno="+transOrderInfo.getOrderPackageNo()+",paychannelid="+transOrderInfo.getPayChannelId()+",remark="+transOrderInfo.getRemark()
				+",productid="+transOrderInfo.getProductIdd()+",requestno="+transOrderInfo.getRequestNo()+",requesttime="+transOrderInfo.getRequestTime()+",status="+transOrderInfo.getStatus()
				+",tradeflowno="+transOrderInfo.getTradeFlowNo()+",userfee="+transOrderInfo.getUserFee()+",feeamount="+transOrderInfo.getFeeAmount()
				+",profit="+transOrderInfo.getProfit()+",busitypeid="+transOrderInfo.getBusiTypeId()+",bankcode="+transOrderInfo.getBankCode()+",errorcode="+transOrderInfo.getErrorCode()
				+",errormsg="+transOrderInfo.getErrorMsg()+",useripaddress="+transOrderInfo.getUserIpAddress());
		synchronized (lock) {
			ErrorResponse response = new ErrorResponse();
			response.setCode("C1");
			//校验订单号是否存在
			//			if(!this.orderNoChk(transOrderInfo.getOrderNo(),transOrderInfo.getMerchantCode()) && transOrderInfo.getStatus()!=TransCodeConst.TRANS_STATUS_PROCESSING ){
			//				logger.info("账户UserId："+transOrderInfo.getUserId()+" 订单号："+transOrderInfo.getOrderNo()+"在机构号："+transOrderInfo.getMerchantCode()+"中已存在");
			//				response.setMsg("该交易订单号已存在，请确认！");
			//				return response;
			//			}
			//判断订单信息是否有误
			String msg = checkInfoService.checkTradeInfo(transOrderInfo);
			if (!"ok".equals(msg)) {
				logger.info("账户UserId：" + transOrderInfo.getUserId() + " 订单号：" + transOrderInfo.getOrderNo()
						+ " 校验订单信息：msg==" + msg);
				response.setMsg(msg);
				return response;
			}
			String userId = transOrderInfo.getUserId();
			User user = new User();
			user.userId = userId;
			user.constId = transOrderInfo.getMerchantCode();
			user.productId = transOrderInfo.getProductIdd() == null ? transOrderInfo.getErrorCode()
					: transOrderInfo.getProductIdd();
			//判断账户状态是否正常
			//			boolean accountIsOK=operationService.checkAccount(user);
			//			if(!accountIsOK){
			//				logger.error("用户: "+userId+"  状态为非正常状态");
			//				logger.info("账户UserId："+transOrderInfo.getUserId()+" 订单号："+transOrderInfo.getOrderNo()+"  账户状态非正常状态");
			//				response.setMsg("账户状态非正常状态");
			//				return response;
			//			}
			//获取套录号
			String entryId = redisIdGenerator.createRequestNo();
			//获取所有账户记账流水
			List<FinanaceEntry> finanaceEntrieAlls = new ArrayList<FinanaceEntry>();
			//瑞金麟备付金账户加，用户账户加(循环两次，分别操作两个)
			for (int i = 0; i <= 1; i++) {
				boolean flag = true;
				Balance balance = null;
				if (0 == i) {
					userId = TransCodeConst.THIRDPARTYID_FNZZH;//瑞金麟备付金账户		
					balance = checkInfoService.getBalance(user, null);
				} else {
					userId = transOrderInfo.getUserId();//用户
					balance = checkInfoService.getBalance(user, null);
				}
				user.userId = userId;
				if (balance == null) {
					logger.error("获取用户[" + userId + "]余额信息失败");
					response.setMsg("获取用户[" + userId + "]余额信息失败");
					return response;
				}
				balance.setPulseDegree(balance.getPulseDegree() + 1);
				List<FinanaceEntry> finanaceEntries = checkInfoService.getFinanaceEntries(transOrderInfo, balance,
						entryId, flag);
				if (finanaceEntries == null || finanaceEntries.size() == 0) {
					logger.error("获取用户[" + userId + "]账户流水信息失败");
					response.setMsg("获取用户[" + userId + "]账户流水信息失败");
					return response;
				}
				for (FinanaceEntry finanaceEntry : finanaceEntries) {
					finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
					finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
					if (null == finanaceEntry.getRemark() || "".equals(finanaceEntry.getRemark())) {
						finanaceEntry.setRemark("实时代收");
					}
					finanaceEntrieAlls.add(finanaceEntry);
				}
			}
			transOrderInfo.setStatus(TransCodeConst.TRANS_STATUS_PAY_SUCCEED);
			transOrderInfo.setErrorCode(transOrderInfo.getProductIdd());
			insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo, new String[] { "productId" });
			response.setIs_success(true);
			response.setCode(null);
			return response;
		}
	}
	
	@Override
	public ErrorResponse collection(TransOrderInfo transOrderInfo,String productId) {
		// TODO 代收     备付金加  其他应收款加
		synchronized (lock) {
			logger.info("---------------------------账户UserId"+transOrderInfo.getUserId()+"代收操作开始------------------------");
			logger.info("代收操作参数信息：amount="+transOrderInfo.getAmount()+",UserId="+transOrderInfo.getUserId()+",funccode="+transOrderInfo.getFuncCode()
					+",intermerchantcode="+transOrderInfo.getInterMerchantCode()+",merchantcode="+transOrderInfo.getMerchantCode()+",orderamount="+transOrderInfo.getOrderAmount()
					+",ordercount="+transOrderInfo.getOrderCount()+",orderdate="+transOrderInfo.getOrderDate()+",orderno="+transOrderInfo.getOrderNo()
					+",orderpackageno="+transOrderInfo.getOrderPackageNo()+",paychannelid="+transOrderInfo.getPayChannelId()+",remark="+transOrderInfo.getRemark()
					+",productid="+productId+",requestno="+transOrderInfo.getRequestNo()+",requesttime="+transOrderInfo.getRequestTime()+",status="+transOrderInfo.getStatus()
					+",tradeflowno="+transOrderInfo.getTradeFlowNo()+",userfee="+transOrderInfo.getUserFee()+",feeamount="+transOrderInfo.getFeeAmount()
					+",profit="+transOrderInfo.getProfit()+",busitypeid="+transOrderInfo.getBusiTypeId()+",bankcode="+transOrderInfo.getBankCode()+",errorcode="+transOrderInfo.getErrorCode()
					+",errormsg="+transOrderInfo.getErrorMsg()+",useripaddress="+transOrderInfo.getUserIpAddress());
			ErrorResponse response=new ErrorResponse();
			String reCode = "C0";
			String reMsg = "成功";
			String[] productIdStrings={"productId"};
			//校验交易码
			if (!TransCodeConst.PAYMENT_COLLECTION.equals(transOrderInfo.getFuncCode())){
				logger.info("代收交易码错误！订单中的交易码为"+transOrderInfo.getFuncCode());
				response.setCode("C1");
				response.setMsg("代收交易码错误！");
				return response;
			}
			//校验订单号是否存在
			if(!this.orderNoChk(transOrderInfo.getOrderNo(),transOrderInfo.getMerchantCode())){
				logger.info("订单号："+transOrderInfo.getOrderNo()+"在机构号："+transOrderInfo.getMerchantCode()+"中已存在");
				response.setCode("C1");
				response.setMsg("该交易订单号已存在，请确认！");
				return response;
			}
			//判断订单信息是否有误
			String msg=checkInfoService.checkTradeInfo(transOrderInfo);
			if(!"ok".equals(msg)){
				logger.error(msg);
				response.setCode("C1");
				response.setMsg(msg);
				return response;
			}
			//获取每个账户记账流水
			List<FinanaceEntry> finanaceEntries=new ArrayList<FinanaceEntry>();
			//获取所有账户记账流水
			List<FinanaceEntry> finanaceEntrieAlls=new ArrayList<FinanaceEntry>();
			String userId=transOrderInfo.getUserId();
			User user=new User();
			user.userId=userId;
			user.constId=transOrderInfo.getMerchantCode();
			user.productId=productId;
			//判断账户状态是否正常
			boolean accountIsOK=operationService.checkAccount(user);
			if(!accountIsOK){
				logger.error("用户"+userId+"状态为非正常状态");
				reCode = "C1";
				reMsg = "账户状态非正常状态";
			}else{
				try {
					//获取套录号
					String entryId=redisIdGenerator.createRequestNo();
					for (int i = 0; i <= 1; i++) {
						boolean flag=true;
						if(0==i){
							userId=TransCodeConst.THIRDPARTYID_FNZZH;//丰年备付金账户			
						}else{
							userId=TransCodeConst.THIRDPARTYID_QTYSKZH;//其它应收款账户
						}
						user.userId=userId;
						Balance balance=null;
						balance=checkInfoService.getBalance(user,userId);
						if(null!=balance){
							balance.setPulseDegree(balance.getPulseDegree()+1);
							finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, balance, entryId, flag);
							if(null!=finanaceEntries&&finanaceEntries.size()>0){
								for(FinanaceEntry finanaceEntry:finanaceEntries) {
									finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
									finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
									if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
										finanaceEntry.setRemark("代收");
									}
									finanaceEntrieAlls.add(finanaceEntry);
								}
							}else{
								logger.error("获取用户["+userId+"]账户流水信息失败");
								reCode = "C3";
								reMsg = "账户流水数据入库失败";
								break;
							}							
						}else{
							logger.error("获取用户["+userId+"]余额信息失败");
							reCode = "C4";
							reMsg = "用户余额查询失败";
						}
					}
					//批量插入记录流水表   写入数据失败尝试三次
					if(reCode.equals("C0")){
						try {
							insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo, productIdStrings);
						} catch (AccountException e) {
							logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败一次");
							try {
								insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo, productIdStrings);
							} catch (AccountException e1) {
								logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败两次");
								try {
									insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo, productIdStrings);
								} catch (AccountException e2) {
									//发送邮件或短信通知管理员
									logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败三次");
									//启用线程 发送邮件
									RkylinMailUtil.sendMailThread("账户记账流水失败通知","******************账户"+transOrderInfo.getUserId()+"做提现记账流水数据入库连续失败三次", TransCodeConst.FINANACE_ENTRY_ERROR_TOEMAIL);
									logger.error(e2.getMessage());
									reCode = "C5";
									reMsg = "数据库操作异常";
								}
							}
						}
					}
					
				} catch (AccountException e) {
					logger.error(e.getMessage());
					reCode = "C5";
					reMsg = "数据库操作异常";
				}
			}
			if(reCode.equals("C0")){
				response.setIs_success(true);
			}else{
				response.setCode(reCode);
				response.setMsg(reMsg);
			}
			logger.info("---------------------------账户UserId"+transOrderInfo.getUserId()+"代收操作结束------------------------");
			return response;
		}
	}

	@Override
	public ErrorResponse withhold(TransOrderInfo transOrderInfo,String productId) {
		// TODO 代付
		synchronized (lock) {
			logger.info("---------------------------账户UserId"+transOrderInfo.getUserId()+"代付操作开始------------------------");
			logger.info("代付操作参数信息：amount="+transOrderInfo.getAmount()+",UserId="+transOrderInfo.getUserId()+",funccode="+transOrderInfo.getFuncCode()
					+",intermerchantcode="+transOrderInfo.getInterMerchantCode()+",merchantcode="+transOrderInfo.getMerchantCode()+",orderamount="+transOrderInfo.getOrderAmount()
					+",ordercount="+transOrderInfo.getOrderCount()+",orderdate="+transOrderInfo.getOrderDate()+",orderno="+transOrderInfo.getOrderNo()
					+",orderpackageno="+transOrderInfo.getOrderPackageNo()+",paychannelid="+transOrderInfo.getPayChannelId()+",remark="+transOrderInfo.getRemark()
					+",productid="+productId+",requestno="+transOrderInfo.getRequestNo()+",requesttime="+transOrderInfo.getRequestTime()+",status="+transOrderInfo.getStatus()
					+",tradeflowno="+transOrderInfo.getTradeFlowNo()+",userfee="+transOrderInfo.getUserFee()+",feeamount="+transOrderInfo.getFeeAmount()
					+",profit="+transOrderInfo.getProfit()+",busitypeid="+transOrderInfo.getBusiTypeId()+",bankcode="+transOrderInfo.getBankCode()+",errorcode="+transOrderInfo.getErrorCode()
					+",errormsg="+transOrderInfo.getErrorMsg()+",useripaddress="+transOrderInfo.getUserIpAddress());
			ErrorResponse response=new ErrorResponse();
			String reCode = "C0";
			String reMsg = "成功";
			String[] productIdStrings={"productId"};
			//校验交易码
			if (!TransCodeConst.PAYMENT_WITHHOLD.equals(transOrderInfo.getFuncCode())){
				logger.info("代付交易码错误！订单中的交易码为"+transOrderInfo.getFuncCode());
				response.setCode("C1");
				response.setMsg("代付交易码错误！");
				return response;
			}
			//校验订单号是否存在
			if(!this.orderNoChk(transOrderInfo.getOrderNo(),transOrderInfo.getMerchantCode())){
				logger.info("订单号："+transOrderInfo.getOrderNo()+"在机构号："+transOrderInfo.getMerchantCode()+"中已存在");
				response.setCode("C1");
				response.setMsg("该交易订单号已存在，请确认！");
				return response;
			}
			//获取每个账户记账流水
			List<FinanaceEntry> finanaceEntries=new ArrayList<FinanaceEntry>();
			//获取所有账户记账流水
			List<FinanaceEntry> finanaceEntrieAlls=new ArrayList<FinanaceEntry>();
			String userId=transOrderInfo.getUserId();
			User user=new User();
			user.userId=userId;
			//user.constId=transOrderInfo.getMerchantCode();
			//user.productId=productId;
			//2016-11-18 16:37:02 RZL修改
//			if(transOrderInfo.getMerchantCode().equals(Constants.KZ_ID)||
//			        transOrderInfo.getMerchantCode().equals(Constants.FN_ID)||
//			        productId.equals(Constants.HT_CHANGDIFANG_ACCOUNT) || 
//			        transOrderInfo.getMerchantCode().equals(Constants.JRD_ID) || 
//			        transOrderInfo.getMerchantCode().equals(Constants.MZ_ID) || 
//			        transOrderInfo.getMerchantCode().equals(Constants.SQSM_ID)||
//			        transOrderInfo.getMerchantCode().equals(Constants.ZK_ID)||
//			        transOrderInfo.getMerchantCode().equals(Constants.TLZX_ID)){
//				user.constId=transOrderInfo.getMerchantCode();
//				user.productId=productId;
//			}else{
//				user.constId=Constants.HT_ID;
//				user.productId=Constants.HT_PRODUCT;
//			}
			//以上判断优化  2016-11-18 16:37:02 RZL修改
			if((transOrderInfo.getMerchantCode().equals(Constants.HT_ID)|| 
					transOrderInfo.getMerchantCode().equals(Constants.HT_CLOUD_ID))&&
					!productId.equals(Constants.HT_CHANGDIFANG_ACCOUNT)){
				user.constId=Constants.HT_ID;
				user.productId=Constants.HT_PRODUCT;
			}else{
				user.constId=transOrderInfo.getMerchantCode();
				user.productId=productId;
			}
			
			//判断账户状态是否正常
			boolean accountIsOK=false;
			if(productId.equals(Constants.FN_PRODUCT)){
				accountIsOK=true;
			}else{
				accountIsOK=operationService.checkAccount(user);
			}
			
			if(!accountIsOK){
				logger.error("用户"+userId+"状态为非正常状态");
				reCode = "C1";
				reMsg = "账户状态非正常状态";
			}else{
				//判断订单信息是否有误
				//----------临时修改merchantCode
				String orderMerchantCode=transOrderInfo.getMerchantCode();
				if(user.constId.equals(Constants.HT_ID)){
					transOrderInfo.setMerchantCode(Constants.HT_ID);
				}
				transOrderInfo.setErrorCode(productId);//暂存产品号
				String msg=checkInfoService.checkTradeInfo(transOrderInfo);
				if(!"ok".equals(msg)){
					logger.error(msg);
					reCode = "C2";
					reMsg = msg;
				}else{
					try {
						//获取套录号
						String entryId=redisIdGenerator.createRequestNo();
						for (int i = 0; i <= 0; i++) {
							boolean flag=false;
							if(1==i){
								userId=TransCodeConst.THIRDPARTYID_QTYFKZH;//其它应付款账户
								flag=true;
							}
							user.userId=userId;
							Balance balance=null;
							if(flag){
								balance=checkInfoService.getBalance(user,userId);
							}else{
								if(productId.equals(Constants.FN_PRODUCT)){
									balance=checkInfoService.getBalance(user,userId);
								}else{
									balance=checkInfoService.getBalance(user,"");
								}
								
							}
							if(null!=balance){
								balance.setPulseDegree(balance.getPulseDegree()+1);
								finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, balance, entryId, flag);
								if(null!=finanaceEntries&&finanaceEntries.size()>0){
									for(FinanaceEntry finanaceEntry:finanaceEntries) {
										finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
										finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
										if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
											finanaceEntry.setRemark("代付");
										}
										finanaceEntrieAlls.add(finanaceEntry);
									}
									//-----------------------临时修改
									transOrderInfo.setMerchantCode(orderMerchantCode);
								}else{
									logger.error("获取用户["+userId+"]账户流水信息失败");
									reCode = "C3";
									reMsg = "账户流水数据入库失败";
									break;
								}							
							}else{
								logger.error("获取用户["+userId+"]余额信息失败");
								reCode = "C4";
								reMsg = "用户余额查询失败";
							}
						}
						//批量插入记录流水表   写入数据失败尝试三次
						if(reCode.equals("C0")){
							try {
								insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo, productIdStrings);
							} catch (AccountException e) {
								logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败一次");
								try {
									insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo, productIdStrings);
								} catch (AccountException e1) {
									logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败两次");
									try {
										insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo, productIdStrings);
									} catch (AccountException e2) {
										//发送邮件或短信通知管理员
										logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败三次");
										//启用线程 发送邮件
										RkylinMailUtil.sendMailThread("账户记账流水失败通知","******************账户"+transOrderInfo.getUserId()+"做提现记账流水数据入库连续失败三次", TransCodeConst.FINANACE_ENTRY_ERROR_TOEMAIL);
										logger.error(e2.getMessage());
										reCode = "C5";
										reMsg = "数据库操作异常";
									}
									
								}
							}
						}
						
					} catch (AccountException e) {
						logger.error(e.getMessage());
						reCode = "C5";
						reMsg = "数据库操作异常";
					}
				}
			}
			if(reCode.equals("C0")){
				response.setIs_success(true);
			}else{
				response.setCode(reCode);
				response.setMsg(reMsg);
			}
			logger.info("---------------------------账户UserId"+transOrderInfo.getUserId()+"代付操作结束------------------------");
			return response;
		}
	}
	
	@Override
	public List<AdvanceBalance> getAdvanceBalance(User user,String queryType) {
		logger.info("-------------------获取商户"+user.userId+"与台长"+user.referUserId+"之间的预付金信息----------------------");
		logger.info("查询商户与台长预付金信息参数：userid="+user.userId+",constid="+user.constId+",productid="+user.productId+",referuserid="+user.referUserId
				+",querytype="+queryType);
		//判断QueryType类型
		FinanaceAccountQuery query=new FinanaceAccountQuery();
		query.setRootInstCd(user.constId);
		query.setAccountRelateId(user.userId);
		query.setGroupManage(user.productId);
		query.setStatusId(BaseConstants.ACCOUNT_STATUS_OK);
		List<AdvanceBalance> advanceBalances=new ArrayList<AdvanceBalance>();
		if(null==user.referUserId||"".equals(user.referUserId)){
			if(Constants.QUERY_BALANCE_TYPE_1.equals(queryType)){//以商户Id为主 查询相关联的台长余额信息
				//根据商户Id,管理分组,机构号获取相关联的台长预付金信息
				List<FinanaceAccount> finanaceAccounts=finanaceAccountManager.queryList(query);
				if(null!=finanaceAccounts&&finanaceAccounts.size()>0){
					for (FinanaceAccount finanaceAccount : finanaceAccounts) {
						AdvanceBalance advanceBalance=new AdvanceBalance();
						//获取台长预付金 金额
						Balance balance=checkInfoService.getBalance(user, finanaceAccount.getFinAccountId());
						//获取当天扣款金额信息
						FinanaceEntryQuery queEntryQuery=new FinanaceEntryQuery();
						queEntryQuery.setFinAccountId(finanaceAccount.getFinAccountId());
						queEntryQuery.setDirection(0);//获取当天账户扣款流水
						List<FinanaceEntry> finanaceEntries=finanaceEntryManager.queryListNew(queEntryQuery);
						long lsAmount=0;//默认当天账户扣款金额为0
						if(null!=finanaceEntries&&finanaceEntries.size()>0){
							lsAmount=finanaceEntries.get(0).getPaymentAmount();
						}
						
						if(null!=balance){
							//台长的预付金额
							advanceBalance.setAmount(balance.getBalanceSettle());
							advanceBalance.setUserId(finanaceAccount.getAccountRelateId());
							advanceBalance.setReferUserId(finanaceAccount.getReferUserId());
							advanceBalance.setType(Constants.ADVANCE_BALANCE_TYPE_0);
//							if(balance.getBalanceSettle()>finanaceAccount.getBalanceSettle()){
//								advanceBalance.setAccrual(balance.getBalanceSettle()-finanaceAccount.getBalanceSettle());
//							}else{
//								advanceBalance.setAccrual(finanaceAccount.getBalanceSettle()-balance.getBalanceSettle());
//							}	
							advanceBalance.setAccrual(lsAmount);
							advanceBalances.add(advanceBalance);
						}
					}
				}
			}else if(Constants.QUERY_BALANCE_TYPE_2.equals(queryType)){//以台长Id为主  查询相关联的商户预付金余额信息
				//获取台长所关联的商户信息
				query.setGroupManage(user.productId);
				query.setReferUserId(user.userId);
				query.setAccountRelateId(null);
				List<FinanaceAccount> finanaceAccounts=finanaceAccountManager.queryList(query);
				for (FinanaceAccount finanaceAccount : finanaceAccounts) {
					//台长所对应商户的预付金信息
					AdvanceBalance advanceBalance=new AdvanceBalance();
					//获取台长预付金 金额
					Balance balance=checkInfoService.getBalance(user, finanaceAccount.getFinAccountId());
					if(null!=balance){
						//台长的预付金额
						advanceBalance.setAmount(balance.getBalanceSettle());
						advanceBalance.setUserId(finanaceAccount.getAccountRelateId());
						advanceBalance.setReferUserId(finanaceAccount.getReferUserId());
						advanceBalance.setType(Constants.ADVANCE_BALANCE_TYPE_1);
						advanceBalances.add(advanceBalance);
					}
				}
			}
		}else{//查询商户下的ReferUserId预付金余额
			Balance balance=checkInfoService.getBalance(user, "");
			if(null!=balance){
				AdvanceBalance advanceBalance=new AdvanceBalance();	
				advanceBalance.setAmount(balance.getBalanceSettle());
				advanceBalance.setUserId(user.userId);
				advanceBalance.setReferUserId(user.referUserId);
				advanceBalance.setType(Constants.ADVANCE_BALANCE_TYPE_1);
				advanceBalances.add(advanceBalance);
			}
		}
		
		return advanceBalances;
	}
	
	@Override
	public ErrorResponse freezefund(TransOrderInfo transOrderInfo,OrderAuxiliary orderAuxiliary) {
		// TODO 冻结账户金额   账户提现余额减  账户的冻结余额加
		synchronized (lock) {
			logger.info("---------------------------账户UserId"+transOrderInfo.getUserId()+"冻结账户余额操作开始------------------------");
			logger.info("冻结账户余额操作参数信息：amount="+transOrderInfo.getAmount()+",UserId="+transOrderInfo.getUserId()+",funccode="+transOrderInfo.getFuncCode()
					+",intermerchantcode="+transOrderInfo.getInterMerchantCode()+",merchantcode="+transOrderInfo.getMerchantCode()+",orderamount="+transOrderInfo.getOrderAmount()
					+",ordercount="+transOrderInfo.getOrderCount()+",orderdate="+transOrderInfo.getOrderDate()+",orderno="+transOrderInfo.getOrderNo()
					+",orderpackageno="+transOrderInfo.getOrderPackageNo()+",paychannelid="+transOrderInfo.getPayChannelId()+",remark="+transOrderInfo.getRemark()
					+",productid="+orderAuxiliary.getProductQAA()+",requestno="+transOrderInfo.getRequestNo()+",requesttime="+transOrderInfo.getRequestTime()+",status="+transOrderInfo.getStatus()
					+",tradeflowno="+transOrderInfo.getTradeFlowNo()+",userfee="+transOrderInfo.getUserFee()+",feeamount="+transOrderInfo.getFeeAmount()
					+",profit="+transOrderInfo.getProfit()+",busitypeid="+transOrderInfo.getBusiTypeId()+",bankcode="+transOrderInfo.getBankCode()+",errorcode="+transOrderInfo.getErrorCode()
					+",errormsg="+transOrderInfo.getErrorMsg()+",useripaddress="+transOrderInfo.getUserIpAddress());
			ErrorResponse response=new ErrorResponse();
			String reCode = "C0";
			String reMsg = "成功";
			String[] productIdStrings={orderAuxiliary.getProductQAA()};
			//校验交易码
			if (!TransCodeConst.FROZON.equals(transOrderInfo.getFuncCode())){
				logger.info("冻结账户金额交易码错误！订单中的交易码为"+transOrderInfo.getFuncCode());
				response.setCode("C1");
				response.setMsg("冻结账户金额交易码错误！");
				return response;
			}
			//校验订单号是否存在
			if(!this.orderNoChk(transOrderInfo.getOrderNo(),transOrderInfo.getMerchantCode())){
				logger.info("订单号："+transOrderInfo.getOrderNo()+"在机构号："+transOrderInfo.getMerchantCode()+"中已存在");
				response.setCode("C1");
				response.setMsg("该交易订单号已存在，请确认！");
				return response;
			}
			//获取每个账户记账流水
			List<FinanaceEntry> finanaceEntries=new ArrayList<FinanaceEntry>();
			//获取所有账户记账流水
			List<FinanaceEntry> finanaceEntrieAlls=new ArrayList<FinanaceEntry>();
			String userId=transOrderInfo.getUserId();
			User user=new User();
			user.userId=userId;
			user.constId=transOrderInfo.getMerchantCode();
			user.productId=orderAuxiliary.getProductQAA();
			//判断账户状态是否正常
			boolean accountIsOK=operationService.checkAccount(user);
			if(!accountIsOK){
				logger.error("用户"+userId+"状态为非正常状态");
				reCode = "C1";
				reMsg = "账户状态非正常状态";
			}else{
				String msg=checkInfoService.checkTradeInfo(transOrderInfo,orderAuxiliary);
				if(!"ok".equals(msg)){
					logger.error(msg);
					reCode = "C2";
					reMsg = msg;
				}else{
					try {
						//获取套录号
						String entryId=redisIdGenerator.createRequestNo();
						boolean flag=true;
						Balance balance=checkInfoService.getBalance(user,"");
						if(null!=balance){
							finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, balance, entryId, flag);
							if(null!=finanaceEntries&&finanaceEntries.size()>0){
								int i=0;
								for(FinanaceEntry finanaceEntry:finanaceEntries) {
									if(i==0){
										finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
									}else{
										finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_FROZON);
									}
									finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
									if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
										finanaceEntry.setRemark("冻结账户余额");
									}
									finanaceEntrieAlls.add(finanaceEntry);
									i++;
								}
							}else{
								logger.error("获取用户["+userId+"]账户流水信息失败");
								reCode = "C3";
								reMsg = "账户流水数据入库失败";
							}							
						}else{
							logger.error("获取用户["+userId+"]余额信息失败");
							reCode = "C4";
							reMsg = "用户余额查询失败";
						}
						//批量插入记录流水表   写入数据失败尝试三次
						if(reCode.equals("C0")){
							try {
								insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo, productIdStrings);
							} catch (AccountException e) {
								logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败一次");
								try {
									insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo, productIdStrings);
								} catch (AccountException e1) {
									logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败两次");
									try {
										insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo, productIdStrings);
									} catch (AccountException e2) {
										//发送邮件或短信通知管理员
										logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败三次");
										//启用线程 发送邮件
										RkylinMailUtil.sendMailThread("账户记账流水失败通知","******************账户"+transOrderInfo.getUserId()+"做提现记账流水数据入库连续失败三次", TransCodeConst.FINANACE_ENTRY_ERROR_TOEMAIL);
										logger.error(e2.getMessage());
										reCode = "C5";
										reMsg = "数据库操作异常";
									}
								}
							}
						}
					} catch (AccountException e) {
						logger.error(e.getMessage());
						reCode = "C5";
						reMsg = "数据库操作异常";
					}
				}
			}
			if(reCode.equals("C0")){
				response.setIs_success(true);
			}else{
				response.setCode(reCode);
				response.setMsg(reMsg);
			}
			logger.info("---------------------------账户UserId"+transOrderInfo.getUserId()+"冻结账户余额操作结束------------------------");
			return response;
		}
	}

	@Override
	public ErrorResponse thawfund(TransOrderInfo transOrderInfo,OrderAuxiliary orderAuxiliary) {
		// TODO 解冻账户金额  账户提现余额加  账户的冻结余额减
		synchronized (lock) {
			logger.info("---------------------------账户UserId"+transOrderInfo.getUserId()+"解冻账户余额操作开始------------------------");
			logger.info("解冻账户余额操作参数信息：amount="+transOrderInfo.getAmount()+",UserId="+transOrderInfo.getUserId()+",funccode="+transOrderInfo.getFuncCode()
					+",intermerchantcode="+transOrderInfo.getInterMerchantCode()+",merchantcode="+transOrderInfo.getMerchantCode()+",orderamount="+transOrderInfo.getOrderAmount()
					+",ordercount="+transOrderInfo.getOrderCount()+",orderdate="+transOrderInfo.getOrderDate()+",orderno="+transOrderInfo.getOrderNo()
					+",orderpackageno="+transOrderInfo.getOrderPackageNo()+",paychannelid="+transOrderInfo.getPayChannelId()+",remark="+transOrderInfo.getRemark()
					+",productid="+orderAuxiliary.getProductQAA()+",requestno="+transOrderInfo.getRequestNo()+",requesttime="+transOrderInfo.getRequestTime()+",status="+transOrderInfo.getStatus()
					+",tradeflowno="+transOrderInfo.getTradeFlowNo()+",userfee="+transOrderInfo.getUserFee()+",feeamount="+transOrderInfo.getFeeAmount()
					+",profit="+transOrderInfo.getProfit()+",busitypeid="+transOrderInfo.getBusiTypeId()+",bankcode="+transOrderInfo.getBankCode()+",errorcode="+transOrderInfo.getErrorCode()
					+",errormsg="+transOrderInfo.getErrorMsg()+",useripaddress="+transOrderInfo.getUserIpAddress());
			ErrorResponse response=new ErrorResponse();
			String reCode = "C0";
			String reMsg = "成功";
			String[] productIdStrings={orderAuxiliary.getProductQAA()};
			//校验交易码
			if (!TransCodeConst.FROZEN.equals(transOrderInfo.getFuncCode())){
				logger.info("解冻账户金额交易码错误！订单中的交易码为"+transOrderInfo.getFuncCode());
				response.setCode("C1");
				response.setMsg("解冻账户金额交易码错误！");
				return response;
			}
			//校验订单号是否存在
			if(!this.orderNoChk(transOrderInfo.getOrderNo(),transOrderInfo.getMerchantCode())){
				logger.info("订单号："+transOrderInfo.getOrderNo()+"在机构号："+transOrderInfo.getMerchantCode()+"中已存在");
				response.setCode("C1");
				response.setMsg("该交易订单号已存在，请确认！");
				return response;
			}
			//获取每个账户记账流水
			List<FinanaceEntry> finanaceEntries=new ArrayList<FinanaceEntry>();
			//获取所有账户记账流水
			List<FinanaceEntry> finanaceEntrieAlls=new ArrayList<FinanaceEntry>();
			String userId=transOrderInfo.getUserId();
			User user=new User();
			user.userId=userId;
			user.constId=transOrderInfo.getMerchantCode();
			user.productId=orderAuxiliary.getProductQAA();
			//判断账户状态是否正常
			boolean accountIsOK=operationService.checkAccount(user);
			if(!accountIsOK){
				logger.error("用户"+userId+"状态为非正常状态");
				reCode = "C1";
				reMsg = "账户状态非正常状态";
			}else{
				String msg=checkInfoService.checkTradeInfo(transOrderInfo,orderAuxiliary);
				if(!"ok".equals(msg)){
					logger.error(msg);
					reCode = "C2";
					reMsg = msg;
				}else{
					try {
						//获取套录号
						String entryId=redisIdGenerator.createRequestNo();
						boolean flag=true;
						Balance balance=checkInfoService.getBalance(user,"");
						if(null!=balance){
							finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, balance, entryId, flag);
							if(null!=finanaceEntries&&finanaceEntries.size()>0){
								int i=0;
								for(FinanaceEntry finanaceEntry:finanaceEntries) {
									if(i==0){
										finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_FROZON);
									}else{
										finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
									}
									finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
									if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
										finanaceEntry.setRemark("解冻账户余额");
									}
									finanaceEntrieAlls.add(finanaceEntry);
									i++;
								}
							}else{
								logger.error("获取用户["+userId+"]账户流水信息失败");
								reCode = "C3";
								reMsg = "账户流水数据入库失败";
							}							
						}else{
							logger.error("获取用户["+userId+"]余额信息失败");
							reCode = "C4";
							reMsg = "用户余额查询失败";
						}
						//批量插入记录流水表   写入数据失败尝试三次
						if(reCode.equals("C0")){
							try {
								insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo, productIdStrings);
							} catch (AccountException e) {
								logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败一次");
								try {
									insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo, productIdStrings);
								} catch (AccountException e1) {
									logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败两次");
									try {
										insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo, productIdStrings);
									} catch (AccountException e2) {
										//发送邮件或短信通知管理员
										logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败三次");
										//启用线程 发送邮件
										RkylinMailUtil.sendMailThread("账户记账流水失败通知","******************账户"+transOrderInfo.getUserId()+"做提现记账流水数据入库连续失败三次", TransCodeConst.FINANACE_ENTRY_ERROR_TOEMAIL);
										logger.error(e2.getMessage());
										reCode = "C5";
										reMsg = "数据库操作异常";
									}
								}
							}
						}
						
					} catch (AccountException e) {
						logger.error(e.getMessage());
						reCode = "C5";
						reMsg = "数据库操作异常";
					}
				}
			}
			if(reCode.equals("C0")){
				response.setIs_success(true);
			}else{
				response.setCode(reCode);
				response.setMsg(reMsg);
			}
			logger.info("---------------------------账户UserId"+transOrderInfo.getUserId()+"解冻账户余额操作结束------------------------");
			return response;
		}
	}
	@Override
	public ErrorResponse orderrefund(TransOrderInfo transOrderInfo,OrderAuxiliary orderAuxiliary) {
		// TODO 订单退款  账户冻结资金解冻  模拟账户提现流水记账 因是银行已退款成功 所以备付金账户余额减
		synchronized (lock) {
			logger.info("---------------------------账户UserId"+transOrderInfo.getUserId()+"订单退款操作开始------------------------");
			logger.info("订单退款操作参数信息：amount="+transOrderInfo.getAmount()+",UserId="+transOrderInfo.getUserId()+",funccode="+transOrderInfo.getFuncCode()
					+",intermerchantcode="+transOrderInfo.getInterMerchantCode()+",merchantcode="+transOrderInfo.getMerchantCode()+",orderamount="+transOrderInfo.getOrderAmount()
					+",ordercount="+transOrderInfo.getOrderCount()+",orderdate="+transOrderInfo.getOrderDate()+",orderno="+transOrderInfo.getOrderNo()
					+",orderpackageno="+transOrderInfo.getOrderPackageNo()+",paychannelid="+transOrderInfo.getPayChannelId()+",remark="+transOrderInfo.getRemark()
					+",productid="+orderAuxiliary.getProductQAA()+",requestno="+transOrderInfo.getRequestNo()+",requesttime="+transOrderInfo.getRequestTime()+",status="+transOrderInfo.getStatus()
					+",tradeflowno="+transOrderInfo.getTradeFlowNo()+",userfee="+transOrderInfo.getUserFee()+",feeamount="+transOrderInfo.getFeeAmount()
					+",profit="+transOrderInfo.getProfit()+",busitypeid="+transOrderInfo.getBusiTypeId()+",bankcode="+transOrderInfo.getBankCode()+",errorcode="+transOrderInfo.getErrorCode()
					+",errormsg="+transOrderInfo.getErrorMsg()+",useripaddress="+transOrderInfo.getUserIpAddress());
			ErrorResponse response=new ErrorResponse();
			String reCode = "C0";
			String reMsg = "成功";
			String[] productIdStrings={orderAuxiliary.getProductQAA()};
			//校验交易码
			if (!TransCodeConst.ORDER_REFUND.equals(transOrderInfo.getFuncCode())){
				logger.info("订单退款交易码错误！订单中的交易码为"+transOrderInfo.getFuncCode());
				response.setCode("C1");
				response.setMsg("订单退款交易码错误！");
				return response;
			}
			//校验订单号是否存在
			if(!this.orderNoChk(transOrderInfo.getOrderNo(),transOrderInfo.getMerchantCode())){
				logger.info("订单号："+transOrderInfo.getOrderNo()+"在机构号："+transOrderInfo.getMerchantCode()+"中已存在");
				response.setCode("C1");
				response.setMsg("该交易订单号已存在，请确认！");
				return response;
			}
			//获取每个账户记账流水
			List<FinanaceEntry> finanaceEntries=new ArrayList<FinanaceEntry>();
			//获取所有账户记账流水
			List<FinanaceEntry> finanaceEntrieAlls=new ArrayList<FinanaceEntry>();
			String userId=transOrderInfo.getUserId();
			String funcCode=transOrderInfo.getFuncCode();
			User user=new User();
			user.userId=userId;
			user.constId=transOrderInfo.getMerchantCode();
			user.productId=orderAuxiliary.getProductQAA();
			//判断账户状态是否正常
			boolean accountIsOK=operationService.checkAccount(user);
			if(!accountIsOK){
				logger.error("用户"+userId+"状态为非正常状态");
				reCode = "C1";
				reMsg = "账户状态非正常状态";
			}else{
				String msg=checkInfoService.checkTradeInfo(transOrderInfo,orderAuxiliary);
				if(!"ok".equals(msg)){
					logger.error(msg);
					reCode = "C2";
					reMsg = msg;
				}else{
					try {
						//获取套录号
						String entryId=redisIdGenerator.createRequestNo();
						boolean flag=true;
						Balance balance=checkInfoService.getBalance(user,"");
						if(null!=balance){
							transOrderInfo.setFuncCode(TransCodeConst.FROZEN);
							transOrderInfo.setErrorCode(funcCode);//为了区别真实体现，该值暂做一个订单退款标识
							finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, balance, entryId, flag);
							if(null!=finanaceEntries&&finanaceEntries.size()>0){
								int j=0;
								for(FinanaceEntry finanaceEntry:finanaceEntries) {
									if(j==0){
										finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_FROZON);
									}else{
										finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
									}
									finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
									if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
										finanaceEntry.setRemark("订单退款");
									}
									finanaceEntrieAlls.add(finanaceEntry);
									j++;
								}
								//******************账户提现流水*************************
								for (int i = 0; i <= 1; i++) {
									if(0==i){
										flag=true;
									}else{
										userId=TransCodeConst.THIRDPARTYID_FNZZH;
										flag=true;
									}
									user.userId=userId;
									if(1==i){
										balance=checkInfoService.getBalance(user,userId);
									}
									if(null!=balance){
										balance.setPulseDegree(balance.getPulseDegree()+1);
										transOrderInfo.setFuncCode(TransCodeConst.WITHDROW);
										finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, balance, entryId, flag);
										if(null!=finanaceEntries&&finanaceEntries.size()>0){
											for(FinanaceEntry finanaceEntry:finanaceEntries) {
												finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
												finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
												if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
													finanaceEntry.setRemark("订单退款");
												}
												finanaceEntrieAlls.add(finanaceEntry);
											}
										}else{
											logger.error("获取用户["+userId+"]账户流水信息失败");
											reCode = "C3";
											reMsg = "账户流水数据入库失败";
											break;
										}							
									}else{
										logger.error("获取用户["+userId+"]余额信息失败");
										reCode = "C4";
										reMsg = "用户余额查询失败";
									}
								}
								
							}else{
								logger.error("获取用户["+userId+"]账户流水信息失败");
								reCode = "C3";
								reMsg = "账户流水数据入库失败";
							}							
						}else{
							logger.error("获取用户["+userId+"]余额信息失败");
							reCode = "C4";
							reMsg = "用户余额查询失败";
						}
						//批量插入记录流水表   写入数据失败尝试三次
						if(reCode.equals("C0")){
							try {
								transOrderInfo.setFuncCode(funcCode);
								insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo, productIdStrings);
							} catch (AccountException e) {
								logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败一次");
								try {
									insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo, productIdStrings);
								} catch (AccountException e1) {
									logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败两次");
									try {
										insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo, productIdStrings);
									} catch (AccountException e2) {
										//发送邮件或短信通知管理员
										logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败三次");
										//启用线程 发送邮件
										RkylinMailUtil.sendMailThread("账户记账流水失败通知","******************账户"+transOrderInfo.getUserId()+"做提现记账流水数据入库连续失败三次", TransCodeConst.FINANACE_ENTRY_ERROR_TOEMAIL);
										logger.error(e2.getMessage());
										reCode = "C5";
										reMsg = "数据库操作异常";
									}
								}
							}
						}
						
					} catch (AccountException e) {
						logger.error(e.getMessage());
						reCode = "C5";
						reMsg = "数据库操作异常";
					}
				}
			}
			if(reCode.equals("C0")){
				response.setIs_success(true);
			}else{
				response.setCode(reCode);
				response.setMsg(reMsg);
			}
			logger.info("---------------------------账户UserId"+transOrderInfo.getUserId()+"订单退款操作结束------------------------");
			return response;
		}
	}
	@Override
	public ErrorResponse freezefundauthcode(TransOrderInfo transOrderInfo,OrderAuxiliary orderAuxiliary) {
		// TODO 冻结账户金额返回授权码   账户提现余额减  账户的冻结余额加
		synchronized (lock) {
			logger.info("---------------------------账户UserId"+transOrderInfo.getUserId()+"冻结账户余额操作开始------------------------");
			logger.info("冻结账户余额操作参数信息：amount="+transOrderInfo.getAmount()+",UserId="+transOrderInfo.getUserId()+",funccode="+transOrderInfo.getFuncCode()
					+",intermerchantcode="+transOrderInfo.getInterMerchantCode()+",merchantcode="+transOrderInfo.getMerchantCode()+",orderamount="+transOrderInfo.getOrderAmount()
					+",ordercount="+transOrderInfo.getOrderCount()+",orderdate="+transOrderInfo.getOrderDate()+",orderno="+transOrderInfo.getOrderNo()
					+",orderpackageno="+transOrderInfo.getOrderPackageNo()+",paychannelid="+transOrderInfo.getPayChannelId()+",remark="+transOrderInfo.getRemark()
					+",productid="+orderAuxiliary.getProductQAA()+",requestno="+transOrderInfo.getRequestNo()+",requesttime="+transOrderInfo.getRequestTime()+",status="+transOrderInfo.getStatus()
					+",tradeflowno="+transOrderInfo.getTradeFlowNo()+",userfee="+transOrderInfo.getUserFee()+",feeamount="+transOrderInfo.getFeeAmount()
					+",profit="+transOrderInfo.getProfit()+",busitypeid="+transOrderInfo.getBusiTypeId()+",bankcode="+transOrderInfo.getBankCode()+",errorcode="+transOrderInfo.getErrorCode()
					+",errormsg="+transOrderInfo.getErrorMsg()+",useripaddress="+transOrderInfo.getUserIpAddress());
			ErrorResponse response=new ErrorResponse();
			String reCode = "C0";
			String reMsg = "成功";
			String[] productIdStrings={orderAuxiliary.getProductQAA()};
			//校验交易码
			if (!TransCodeConst.FROZON_AUTHCODE.equals(transOrderInfo.getFuncCode())){
				logger.info("冻结账户金额返回授权码交易码错误！订单中的交易码为"+transOrderInfo.getFuncCode());
				response.setCode("C1");
				response.setMsg("冻结账户金额交易码错误！");
				return response;
			}
			//校验订单号是否存在
			if(!this.orderNoChk(transOrderInfo.getOrderNo(),transOrderInfo.getMerchantCode())){
				logger.info("订单号："+transOrderInfo.getOrderNo()+"在机构号："+transOrderInfo.getMerchantCode()+"中已存在");
				response.setCode("C1");
				response.setMsg("该交易订单号已存在，请确认！");
				return response;
			}
			//获取每个账户记账流水
			List<FinanaceEntry> finanaceEntries=new ArrayList<FinanaceEntry>();
			//获取所有账户记账流水
			List<FinanaceEntry> finanaceEntrieAlls=new ArrayList<FinanaceEntry>();
			//授权表对象
			FinanaceAccountAuth finanaceAccountAuthInfo=new FinanaceAccountAuth();
			//设置授权码
			String authCode=null;
			String userId=transOrderInfo.getUserId();
			User user=new User();
			user.userId=userId;
			user.constId=transOrderInfo.getMerchantCode();
			user.productId=orderAuxiliary.getProductQAA();
			//判断账户状态是否正常
			boolean accountIsOK=operationService.checkAccount(user);
			if(!accountIsOK){
				logger.error("用户"+userId+"状态为非正常状态");
				reCode = "C1";
				reMsg = "账户状态非正常状态";
			}else{
				String msg=checkInfoService.checkTradeInfo(transOrderInfo,orderAuxiliary);
				if(!"ok".equals(msg)){
					logger.error(msg);
					reCode = "C2";
					reMsg = msg;
				}else{
					try {
						//获取套录号
						String entryId=redisIdGenerator.createRequestNo();
						boolean flag=true;
						Balance balance=checkInfoService.getBalance(user,"");
						if(null!=balance){
							finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, balance, entryId, flag);
							if(null!=finanaceEntries&&finanaceEntries.size()>0){
								int i=0;
								for(FinanaceEntry finanaceEntry:finanaceEntries) {
									if(i==0){
										finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
									}else{
										finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_FROZON);
									}
									finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
									if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
										finanaceEntry.setRemark("冻结账户余额");
									}
									finanaceEntrieAlls.add(finanaceEntry);
									i++;
								}
								
								//######################入预授权表数据设置
								//获取授权码
								authCode=redisIdGenerator.createAuthCode(10);	
								logger.info("订单号："+transOrderInfo.getOrderNo()+"的授权码为："+authCode);
								finanaceAccountAuthInfo.setAuthTypeId(transOrderInfo.getFuncCode());
								finanaceAccountAuthInfo.setFinAccountId(balance.getFinAccountId());
								finanaceAccountAuthInfo.setAuthCode(authCode);
								finanaceAccountAuthInfo.setReferEntryId(entryId);
								finanaceAccountAuthInfo.setAmount(transOrderInfo.getAmount());
								finanaceAccountAuthInfo.setStatusId(String.valueOf(Constants.AGMT_STATUS_1));	
							}else{
								logger.error("获取用户["+userId+"]账户流水信息失败");
								reCode = "C3";
								reMsg = "账户流水数据入库失败";
							}							
						}else{
							logger.error("获取用户["+userId+"]余额信息失败");
							reCode = "C4";
							reMsg = "用户余额查询失败";
						}
						//批量插入记录流水表   写入数据失败尝试三次
						if(reCode.equals("C0")){
							try {
								insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo,finanaceAccountAuthInfo, productIdStrings);
							} catch (AccountException e) {
								logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败一次");
								try {
									insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo,finanaceAccountAuthInfo, productIdStrings);
								} catch (AccountException e1) {
									logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败两次");
									try {
										insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo,finanaceAccountAuthInfo, productIdStrings);
									} catch (AccountException e2) {
										//发送邮件或短信通知管理员
										logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败三次");
										//启用线程 发送邮件
										RkylinMailUtil.sendMailThread("账户记账流水失败通知","******************账户"+transOrderInfo.getUserId()+"做提现记账流水数据入库连续失败三次", TransCodeConst.FINANACE_ENTRY_ERROR_TOEMAIL);
										logger.error(e2.getMessage());
										reCode = "C5";
										reMsg = "数据库操作异常";
									}
								}
							}
						}
					} catch (AccountException e) {
						logger.error(e.getMessage());
						reCode = "C5";
						reMsg = "数据库操作异常";
					}
				}
			}
			if(reCode.equals("C0")){
				response.setAuthCode(authCode);
				response.setIs_success(true);
			}else{
				response.setCode(reCode);
				response.setMsg(reMsg);
			}
			logger.info("---------------------------账户UserId"+transOrderInfo.getUserId()+"冻结账户余额操作结束------------------------");
			return response;
		}
	}

	@Override
	public ErrorResponse thawfundauthcode(TransOrderInfo transOrderInfo,OrderAuxiliary orderAuxiliary) {
		// TODO 通过授权码解冻账户金额  账户提现余额加  账户的冻结余额减
		synchronized (lock) {
			logger.info("---------------------------账户UserId"+transOrderInfo.getUserId()+"解冻账户余额操作开始------------------------");
			logger.info("解冻账户余额操作参数信息：amount="+transOrderInfo.getAmount()+",UserId="+transOrderInfo.getUserId()+",funccode="+transOrderInfo.getFuncCode()
					+",intermerchantcode="+transOrderInfo.getInterMerchantCode()+",merchantcode="+transOrderInfo.getMerchantCode()+",orderamount="+transOrderInfo.getOrderAmount()
					+",ordercount="+transOrderInfo.getOrderCount()+",orderdate="+transOrderInfo.getOrderDate()+",orderno="+transOrderInfo.getOrderNo()
					+",orderpackageno="+transOrderInfo.getOrderPackageNo()+",paychannelid="+transOrderInfo.getPayChannelId()+",remark="+transOrderInfo.getRemark()
					+",productid="+orderAuxiliary.getProductQAA()+",requestno="+transOrderInfo.getRequestNo()+",requesttime="+transOrderInfo.getRequestTime()+",status="+transOrderInfo.getStatus()
					+",tradeflowno="+transOrderInfo.getTradeFlowNo()+",userfee="+transOrderInfo.getUserFee()+",feeamount="+transOrderInfo.getFeeAmount()
					+",profit="+transOrderInfo.getProfit()+",busitypeid="+transOrderInfo.getBusiTypeId()+",bankcode="+transOrderInfo.getBankCode()+",errorcode="+transOrderInfo.getErrorCode()
					+",errormsg="+transOrderInfo.getErrorMsg()+",useripaddress="+transOrderInfo.getUserIpAddress());
			ErrorResponse response=new ErrorResponse();
			String reCode = "C0";
			String reMsg = "成功";
			String[] productIdStrings={orderAuxiliary.getProductQAA()};
			//校验交易码
			if (!TransCodeConst.FROZEN_AUTHCODE.equals(transOrderInfo.getFuncCode())){
				logger.info("解冻账户金额交易码错误！订单中的交易码为"+transOrderInfo.getFuncCode());
				response.setCode("C1");
				response.setMsg("解冻账户金额交易码错误！");
				return response;
			}
			//校验订单号是否存在
			if(!this.orderNoChk(transOrderInfo.getOrderNo(),transOrderInfo.getMerchantCode())){
				logger.info("订单号："+transOrderInfo.getOrderNo()+"在机构号："+transOrderInfo.getMerchantCode()+"中已存在");
				response.setCode("C1");
				response.setMsg("该交易订单号已存在，请确认！");
				return response;
			}
			//根据授权码获取授权信息
			FinanaceAccountAuthQuery query =new FinanaceAccountAuthQuery();
			query.setAuthCode(orderAuxiliary.getAuthCode());
			query.setStatusId(AccountConstants.AUTHCODE_STATUS1);
			List<FinanaceAccountAuth> finanaceAccountAuths= finanaceAccountAuthManager.queryList(query);
			if(null==finanaceAccountAuths||finanaceAccountAuths.size()==0){
				logger.info("该授权码不存在或已失效，code:"+orderAuxiliary.getAuthCode());
				response.setCode("C1");
				response.setMsg("该授权码不存在或已失效，请确认！");
				return response;
			}
			orderAuxiliary.setAuthAmount(finanaceAccountAuths.get(0).getAmount());
			//获取每个账户记账流水
			List<FinanaceEntry> finanaceEntries=new ArrayList<FinanaceEntry>();
			//获取所有账户记账流水
			List<FinanaceEntry> finanaceEntrieAlls=new ArrayList<FinanaceEntry>();
			//授权码信息
			FinanaceAccountAuth finanaceAccountAuth=new FinanaceAccountAuth();
			String userId=transOrderInfo.getUserId();
			User user=new User();
			user.userId=userId;
			user.constId=transOrderInfo.getMerchantCode();
			user.productId=orderAuxiliary.getProductQAA();
			//判断账户状态是否正常
			boolean accountIsOK=operationService.checkAccount(user);
			if(!accountIsOK){
				logger.error("用户"+userId+"状态为非正常状态");
				reCode = "C1";
				reMsg = "账户状态非正常状态";
			}else{
				String msg=checkInfoService.checkTradeInfo(transOrderInfo,orderAuxiliary);
				if(!"ok".equals(msg)){
					logger.error(msg);
					reCode = "C2";
					reMsg = msg;
				}else{
					try {
						//获取套录号
						String entryId=redisIdGenerator.createRequestNo();
						boolean flag=true;
						Balance balance=checkInfoService.getBalance(user,"");
						if(null!=balance){
							finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, balance, entryId, flag);
							if(null!=finanaceEntries&&finanaceEntries.size()>0){
								int i=0;
								for(FinanaceEntry finanaceEntry:finanaceEntries) {
									if(i==0){
										finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_FROZON);
									}else{
										finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
									}
									finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
									if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
										finanaceEntry.setRemark("解冻账户余额");
									}
									finanaceEntrieAlls.add(finanaceEntry);
									i++;
								}
								
								//#######转账(红包发放)
								if(null!=balance &&transOrderInfo.getInterMerchantCode()!=null&&!transOrderInfo.getInterMerchantCode().equals("")){
									logger.info("红包解冻并向"+transOrderInfo.getInterMerchantCode()+"账户中转入红包"+transOrderInfo.getAmount()+"分");
									transOrderInfo.setErrorCode(TransCodeConst.ADJUST_ACCOUNT_AMOUNT);//暂存转账交易码
									balance.setPulseDegree(balance.getPulseDegree()+1);
									finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, balance, entryId, flag);
									if(null!=finanaceEntries&&finanaceEntries.size()>0){
										for(FinanaceEntry finanaceEntry:finanaceEntries) {
											finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);	
											finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
											if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
												finanaceEntry.setRemark("解冻账户余额-转账");
											}
											finanaceEntrieAlls.add(finanaceEntry);
										}
										//#######更新授权表中的金额信息
										finanaceAccountAuth.setFinAntAuthId(finanaceAccountAuths.get(0).getFinAntAuthId());
										finanaceAccountAuth.setAmount(finanaceAccountAuths.get(0).getAmount()-transOrderInfo.getAmount());
									}else{
										logger.error("获取用户["+userId+"]账户流水信息失败");
										reCode = "C3";
										reMsg = "账户流水数据入库失败";
									}							
								}else{
									logger.info("账户金额解冻，转入方为空，不包含转账");
									//#######更新授权表中的金额信息
									finanaceAccountAuth.setFinAntAuthId(finanaceAccountAuths.get(0).getFinAntAuthId());
									finanaceAccountAuth.setAmount(finanaceAccountAuths.get(0).getAmount()-transOrderInfo.getAmount());
								}
							}else{
								logger.error("获取用户["+userId+"]账户流水信息失败");
								reCode = "C3";
								reMsg = "账户流水数据入库失败";
							}							
						}else{
							logger.error("获取用户["+userId+"]余额信息失败");
							reCode = "C4";
							reMsg = "用户余额查询失败";
						}
						//把相应的授权码存放在errorMsg
						transOrderInfo.setErrorMsg(orderAuxiliary.getAuthCode());
						//批量插入记录流水表   写入数据失败尝试三次
						if(reCode.equals("C0")){
							try {
								insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo,finanaceAccountAuth, productIdStrings);
							} catch (AccountException e) {
								logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败一次");
								try {
									insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo,finanaceAccountAuth, productIdStrings);
								} catch (AccountException e1) {
									logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败两次");
									try {
										insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo,finanaceAccountAuth, productIdStrings);
									} catch (AccountException e2) {
										//发送邮件或短信通知管理员
										logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败三次");
										//启用线程 发送邮件
										RkylinMailUtil.sendMailThread("账户记账流水失败通知","******************账户"+transOrderInfo.getUserId()+"做提现记账流水数据入库连续失败三次", TransCodeConst.FINANACE_ENTRY_ERROR_TOEMAIL);
										logger.error(e2.getMessage());
										reCode = "C5";
										reMsg = "数据库操作异常";
									}
								}
							}
						}
						
					} catch (AccountException e) {
						logger.error(e.getMessage());
						reCode = "C5";
						reMsg = "数据库操作异常";
					}
				}
			}
			if(reCode.equals("C0")){
				response.setIs_success(true);
			}else{
				response.setCode(reCode);
				response.setMsg(reMsg);
			}
			logger.info("---------------------------账户UserId"+transOrderInfo.getUserId()+"解冻账户余额操作结束------------------------");
			return response;
		}
	}
	
	@Override
	public String withdrowJRD(TransOrderInfo transOrderInfo,OrderAuxiliary orderAuxiliary) {
		// TODO 君融贷提现-----优先主账户里边的金额，后充值子账户中的金额（注意：该部分金额会涉及手续费）
		synchronized (lock) {
			logger.info("---------------------------账户UserId"+transOrderInfo.getUserId()+"提现操作开始------------------------");
			logger.info("提现操作参数信息：amount="+transOrderInfo.getAmount()+",UserId="+transOrderInfo.getUserId()+",funccode="+transOrderInfo.getFuncCode()
					+",intermerchantcode="+transOrderInfo.getInterMerchantCode()+",merchantcode="+transOrderInfo.getMerchantCode()+",orderamount="+transOrderInfo.getOrderAmount()
					+",ordercount="+transOrderInfo.getOrderCount()+",orderdate="+transOrderInfo.getOrderDate()+",orderno="+transOrderInfo.getOrderNo()
					+",orderpackageno="+transOrderInfo.getOrderPackageNo()+",paychannelid="+transOrderInfo.getPayChannelId()+",remark="+transOrderInfo.getRemark()
					+",productid="+orderAuxiliary.getProductQAA()+",requestno="+transOrderInfo.getRequestNo()+",requesttime="+transOrderInfo.getRequestTime()+",status="+transOrderInfo.getStatus()
					+",tradeflowno="+transOrderInfo.getTradeFlowNo()+",userfee="+transOrderInfo.getUserFee()+",feeamount="+transOrderInfo.getFeeAmount()
					+",profit="+transOrderInfo.getProfit()+",busitypeid="+transOrderInfo.getBusiTypeId()+",bankcode="+transOrderInfo.getBankCode()+",errorcode="+transOrderInfo.getErrorCode()
					+",errormsg="+transOrderInfo.getErrorMsg()+",useripaddress="+transOrderInfo.getUserIpAddress());
			String isOk ="ok";
			String[] productIdStrings={orderAuxiliary.getProductQAA()};
			//校验交易码
			if (!TransCodeConst.WITHDROW.equals(transOrderInfo.getFuncCode())){
				logger.info("提现交易码错误！订单中的交易码为"+transOrderInfo.getFuncCode());
				return "提现交易码错误";
			}
			//校验订单号是否存在
			if(!this.orderNoChk(transOrderInfo.getOrderNo(),transOrderInfo.getMerchantCode())){
				logger.info("订单号："+transOrderInfo.getOrderNo()+"在机构号："+transOrderInfo.getMerchantCode()+"中已存在");
				return "该交易订单号已存在，请确认！";
			}
			//获取每个账户记账流水
			List<FinanaceEntry> finanaceEntries=new ArrayList<FinanaceEntry>();
			//获取所有账户记账流水
			List<FinanaceEntry> finanaceEntrieAlls=new ArrayList<FinanaceEntry>();
			User user=new User();
			user.userId=transOrderInfo.getUserId();
			user.constId=transOrderInfo.getMerchantCode();
			user.productId=orderAuxiliary.getProductQAA();
			//判断账户状态是否正常
			boolean accountIsOK=operationService.checkAccount(user);
			if(!accountIsOK){
				logger.error("用户"+transOrderInfo.getUserId()+"状态为非正常状态");
				return "账户"+transOrderInfo.getUserId()+"状态非正常状态,请确认入参是否正确！";
			}
		//判断订单信息是否有误
		String msg=checkInfoService.checkTradeInfo(transOrderInfo);
		if(!"ok".equals(msg)){
			logger.error(msg);
			return msg;
		}
		try {
			//获取套录号
			String entryId=redisIdGenerator.createRequestNo();
			
			//获取主账户余额
			Balance balance=checkInfoService.getBalance(user,"");
			if(null==balance){
				logger.error("获取账户余额信息失败！userId:"+user.userId+";产品号："+user.productId);
				return "获取账户余额信息失败！";
			}
			long amount=transOrderInfo.getAmount();
			//主账户余额减   待提现账户加   主账户余额减  收益账户加
			if(balance.getBalanceSettle()>=transOrderInfo.getOrderAmount()){
				finanaceEntrieAlls  = JRDwithdrow(transOrderInfo, user, entryId,balance);
			}else{
				
				//从子账户向主账户转账  子账户提现余额减   主账户提现余额加
				 TransOrderInfo transOrderInfo2= new TransOrderInfo();
				 transOrderInfo2= transOrderInfo;
				 transOrderInfo2.setAmount(transOrderInfo.getOrderAmount()-balance.getBalanceSettle());
				 transOrderInfo2.setFuncCode(TransCodeConst.ADJUST_ACCOUNT_AMOUNT);
				
				 logger.info("转账部分开始=========================================>");
				 
				//常规转账交易
				//balance为转出方的账户余额信息，balanceB为转入方的账户余额信息
				Balance balanceB=new Balance();				 
			    User userB=new User();
			    userB.userId=transOrderInfo.getUserId();
			    userB.constId=transOrderInfo.getMerchantCode();
			    userB.productId=Constants.JRD_RECHARGE_ACCOUNT_PRODUCT;
				balanceB=checkInfoService.getBalance(userB,"");				
				if(null!=balanceB){
					balanceB.setPulseDegree(balanceB.getPulseDegree()+1);
					balance.setPulseDegree(balance.getPulseDegree()+1);
					finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo2, balanceB,balance, entryId, false);
					if(null!=finanaceEntries&&finanaceEntries.size()>0){
						for(FinanaceEntry finanaceEntry:finanaceEntries) {
							finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);	
							finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
							if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
								finanaceEntry.setRemark("转账");
							}
							if(finanaceEntry.getPaymentAmount()>0){
								finanaceEntrieAlls.add(finanaceEntry);
							}
						}
					}
				}else{
					logger.error("获取账户余额信息失败！userId:"+user.userId+";产品号："+user.productId);
					return "获取账户余额信息失败！";
				}
				 logger.info("转账部分开始=========================================>");
				 //String result= transferNew(transOrderInfo2,orderAuxiliary);
				 //if("ok".equals(result)){
					//主账户余额减   待提现账户加   主账户余额减  收益账户加
					 transOrderInfo.setAmount(amount);
					 transOrderInfo.setFuncCode(TransCodeConst.WITHDROW);
					 finanaceEntries  = JRDwithdrow(transOrderInfo, user, entryId,balance);
					 for(FinanaceEntry finanaceEntry:finanaceEntries) {
						if(finanaceEntry.getPaymentAmount()>0){
							finanaceEntrieAlls.add(finanaceEntry);
						}
					}
//				 }else{
//					 logger.error("转账失败");
//					 isOk = "转账失败";
//				 }
			}			
			if(isOk.equals("ok")){
				transOrderInfo.setAmount(amount);
				try {
					insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo, productIdStrings);
				} catch (AccountException e) {
					logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败一次");
					try {
						insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo, productIdStrings);
					} catch (AccountException e1) {
						logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败两次");
						try {
							insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo, productIdStrings);
						} catch (AccountException e2) {
							//发送邮件或短信通知管理员
							logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败三次");
							//启用线程 发送邮件
							RkylinMailUtil.sendMailThread("账户记账流水失败通知","******************账户"+transOrderInfo.getUserId()+"做提现记账流水数据入库连续失败三次", TransCodeConst.FINANACE_ENTRY_ERROR_TOEMAIL);
							logger.error(e2.getMessage());
							isOk = "数据库操作异常";
						}
						
					}
				}
			}
			//***************通知代收付现改为日结线程定时汇总批量处理用户提现数据*************
			} catch (AccountException e) {
				logger.error(e.getMessage());
				isOk = "数据库操作异常";
			}
			logger.info("---------------------------账户UserId"+transOrderInfo.getUserId()+"提现操作结束------------------------");
			return isOk;
		}		
	}

	@Override
	public String withholdJRD(TransOrderInfo transOrderInfo,OrderAuxiliary orderAuxiliary) {
		// TODO 君融贷代付
		synchronized (lock) {
			logger.info("---------------------------账户UserId："+transOrderInfo.getUserId()+"代付操作开始------------------------");
			logger.info("代付操作参数信息：amount="+transOrderInfo.getAmount()+",UserId="+transOrderInfo.getUserId()+",funccode="+transOrderInfo.getFuncCode()
					+",intermerchantcode="+transOrderInfo.getInterMerchantCode()+",merchantcode="+transOrderInfo.getMerchantCode()+",orderamount="+transOrderInfo.getOrderAmount()
					+",ordercount="+transOrderInfo.getOrderCount()+",orderdate="+transOrderInfo.getOrderDate()+",orderno="+transOrderInfo.getOrderNo()
					+",orderpackageno="+transOrderInfo.getOrderPackageNo()+",paychannelid="+transOrderInfo.getPayChannelId()+",remark="+transOrderInfo.getRemark()
					+",productid="+orderAuxiliary.getProductQAA()+",requestno="+transOrderInfo.getRequestNo()+",requesttime="+transOrderInfo.getRequestTime()+",status="+transOrderInfo.getStatus()
					+",tradeflowno="+transOrderInfo.getTradeFlowNo()+",userfee="+transOrderInfo.getUserFee()+",feeamount="+transOrderInfo.getFeeAmount()
					+",profit="+transOrderInfo.getProfit()+",busitypeid="+transOrderInfo.getBusiTypeId()+",bankcode="+transOrderInfo.getBankCode()+",errorcode="+transOrderInfo.getErrorCode()
					+",errormsg="+transOrderInfo.getErrorMsg()+",useripaddress="+transOrderInfo.getUserIpAddress());
			String isOk="ok";
			String[] productIdStrings={orderAuxiliary.getProductQAA()};
			//校验交易码
			if (!TransCodeConst.PAYMENT_WITHHOLD.equals(transOrderInfo.getFuncCode())){
				logger.info("代付交易码错误！订单中的交易码为"+transOrderInfo.getFuncCode());
				return "代付交易码错误！";
			}
			//校验订单号是否存在
			if(!this.orderNoChk(transOrderInfo.getOrderNo(),transOrderInfo.getMerchantCode())){
				logger.info("订单号："+transOrderInfo.getOrderNo()+"在机构号："+transOrderInfo.getMerchantCode()+"中已存在");
				return "该交易订单号已存在，请确认！";
			}
			//获取每个账户记账流水
			List<FinanaceEntry> finanaceEntries=new ArrayList<FinanaceEntry>();
			//获取所有账户记账流水
			List<FinanaceEntry> finanaceEntrieAlls=new ArrayList<FinanaceEntry>();
			String userId=transOrderInfo.getUserId();
			User user=new User();
			user.userId=userId;
			user.constId=transOrderInfo.getMerchantCode();
			user.productId=orderAuxiliary.getProductQAA();
			if(!operationService.checkAccount(user)){
				logger.error("用户"+userId+"状态为非正常状态");
				return "账户状态非正常状态";
			}
			//判断订单信息是否有误
			transOrderInfo.setErrorCode(orderAuxiliary.getProductQAA());//暂存产品号
			//临时添加，后续删除
			transOrderInfo.setInterMerchantCode(transOrderInfo.getUserId());
			
			String msg=checkInfoService.checkTradeInfo(transOrderInfo);
			if(!"ok".equals(msg)){
				logger.error(msg);
				return msg;
			}
			try {
				//原始代付金额
				long amount=transOrderInfo.getAmount();
				//记录代付发起方的余额信息
				Balance balanceB=null;
				//获取套录号
				String entryId=redisIdGenerator.createRequestNo();
				int j=1;
				for (int i = 0; i <= j; i++) {
					boolean flag=false;
					if(1==i){
						continue;
//						userId=TransCodeConst.THIRDPARTYID_QTYFKZH;//其它应付款账户
//						flag=true;
					}else if (2==i) {//手续费部分
						userId=transOrderInfo.getUserId();
						flag=false;
						transOrderInfo.setAmount(transOrderInfo.getUserFee());
					}else if (3==i) {//手续费部分
						userId=TransCodeConst.THIRDPARTYID_JRDQYSYZZH;
						flag=true;
					} 
					user.userId=userId;
					Balance balance=null;
					//判断userfee是否有金额
					if(i==0){
						if(transOrderInfo.getUserId()!=TransCodeConst.THIRDPARTYID_JRDQYSYZZH&&transOrderInfo.getUserId()!=TransCodeConst.THIRDPARTYID_JRDQYZZH){
							if(null!=transOrderInfo.getUserFee()&&transOrderInfo.getUserFee()>0){//有手续费，需要在充值子账户中做提现
								j=3;
							}
						}
					}
					if(flag){
						balance=checkInfoService.getBalance(user,userId);
					}else{
						if(2==i){
							balance=balanceB;
						}else{
							balance=checkInfoService.getBalance(user,"");
						}
					}
					if(null!=balance){
						balance.setPulseDegree(balance.getPulseDegree()+1);
						finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, balance, entryId, flag);
						if(null!=finanaceEntries&&finanaceEntries.size()>0){
							for(FinanaceEntry finanaceEntry:finanaceEntries) {
								finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
								finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
								if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
									finanaceEntry.setRemark("代付");
								}
								finanaceEntrieAlls.add(finanaceEntry);
							}
							if(0==i){
								balanceB=balance;
							}
						}else{
							logger.error("获取用户["+userId+"]账户流水信息失败");
							isOk = "账户流水数据入库失败";
							break;
						}							
					}else{
						logger.error("获取用户["+userId+"]余额信息失败");
						isOk = "用户余额查询失败";
						break;
					}
				}
				//批量插入记录流水表   写入数据失败尝试三次
				if(isOk.equals("ok")){
					try {
						transOrderInfo.setAmount(amount);						insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo, productIdStrings);					} catch (AccountException e) {
						logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败一次");
						try {
							insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo,productIdStrings);
						} catch (AccountException e1) {
							logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败两次");
							try {
								insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo, productIdStrings);
							} catch (AccountException e2) {
								//发送邮件或短信通知管理员
								logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败三次");
								//启用线程 发送邮件
								RkylinMailUtil.sendMailThread("账户记账流水失败通知","******************账户"+transOrderInfo.getUserId()+"做提现记账流水数据入库连续失败三次", TransCodeConst.FINANACE_ENTRY_ERROR_TOEMAIL);
								logger.error(e2.getMessage());
								isOk = "数据库操作异常";
							}
							
						}
					}
				}
			} catch (AccountException e) {
				logger.error(e.getMessage());
				isOk = "数据库操作异常";
			}
			logger.info("---------------------------账户UserId"+transOrderInfo.getUserId()+"代付操作结束------------------------");
			return isOk;
		}
	}
	@Override
	public String transferNew(TransOrderInfo transOrderInfo,OrderAuxiliary orderAuxiliary) {
		// TODO 转账
		synchronized (lock) {
			String isOk="ok";
			String[] productIdStrings={orderAuxiliary.getProductQAA(),orderAuxiliary.getProductQAB()};
			logger.info("---------------------------账户UserId："+transOrderInfo.getUserId()+"向账户"+transOrderInfo.getInterMerchantCode()+"转入"+transOrderInfo.getAmount()+"分，转账操作开始------------------------");
			logger.info("转账操作参数信息：amount="+transOrderInfo.getAmount()+",UserId="+transOrderInfo.getUserId()+",funccode="+transOrderInfo.getFuncCode()
					+",intermerchantcode="+transOrderInfo.getInterMerchantCode()+",merchantcode="+transOrderInfo.getMerchantCode()+",orderamount="+transOrderInfo.getOrderAmount()
					+",ordercount="+transOrderInfo.getOrderCount()+",orderdate="+transOrderInfo.getOrderDate()+",orderno="+transOrderInfo.getOrderNo()
					+",orderpackageno="+transOrderInfo.getOrderPackageNo()+",paychannelid="+transOrderInfo.getPayChannelId()+",remark="+transOrderInfo.getRemark()
					+",productid="+orderAuxiliary.getProductQAA()+",requestno="+transOrderInfo.getRequestNo()+",requesttime="+transOrderInfo.getRequestTime()+",status="+transOrderInfo.getStatus()
					+",tradeflowno="+transOrderInfo.getTradeFlowNo()+",userfee="+transOrderInfo.getUserFee()+",feeamount="+transOrderInfo.getFeeAmount()
					+",profit="+transOrderInfo.getProfit()+",busitypeid="+transOrderInfo.getBusiTypeId()+",bankcode="+transOrderInfo.getBankCode()+",errorcode="+transOrderInfo.getErrorCode()
					+",errormsg="+transOrderInfo.getErrorMsg()+",useripaddress="+transOrderInfo.getUserIpAddress()+",intoproductid="+orderAuxiliary.getProductQAB());
			//校验交易码
			if (!TransCodeConst.ADJUST_ACCOUNT_AMOUNT.equals(transOrderInfo.getFuncCode())&&
			        !TransCodeConst.FUNCTION_TRANSFER.equals(transOrderInfo.getFuncCode())){
				logger.info("转账交易码错误！订单中的交易码为"+transOrderInfo.getFuncCode());
				return "转账交易码错误！";
			}
			//校验订单号是否存在
			if(!this.orderNoChk(transOrderInfo.getOrderNo(),transOrderInfo.getMerchantCode())){
				logger.info("订单号："+transOrderInfo.getOrderNo()+"在机构号："+transOrderInfo.getMerchantCode()+"中已存在");
				return "该交易订单号已存在，请确认！";
			}
			//获取每个账户记账流水
			List<FinanaceEntry> finanaceEntries=new ArrayList<FinanaceEntry>();
			//获取所有账户记账流水
			List<FinanaceEntry> finanaceEntrieAlls=new ArrayList<FinanaceEntry>();
			User user=new User();
			user.userId=transOrderInfo.getUserId();
			user.constId=transOrderInfo.getMerchantCode();
			user.productId=orderAuxiliary.getProductQAA();
			//判断账户状态是否正常
			boolean accountIsOK=operationService.checkAccount(user);
			if(!accountIsOK){
				logger.error("用户"+transOrderInfo.getUserId()+"状态为非正常状态");
				return "账户状态非正常状态";
			}
			//判断订单信息是否有误
			String msg=checkInfoService.checkTradeInfo(transOrderInfo,orderAuxiliary);
			if(!"ok".equals(msg)){
				logger.error(msg);
				return msg;
			}
			try {
				//获取套录号
				String entryId=redisIdGenerator.createRequestNo();
				boolean flag=false;
				Balance balance=null;
				//记录订单剩余金额
				Long nAmount=0l;
				//订单原始金额
				Long oAmount=transOrderInfo.getAmount();
				//获取转入方的余额信息
				User userB=new User();
				userB.userId=transOrderInfo.getInterMerchantCode();
				userB.constId = transOrderInfo.getMerchantCode();
				
				if(null==orderAuxiliary.getProductQAB()||"".equals(orderAuxiliary.getProductQAB())){
					userB.productId=orderAuxiliary.getProductQAA();
				}else{
					userB.productId=orderAuxiliary.getProductQAB();
				}
				
				Balance balanceB=checkInfoService.getBalance(userB,"");
				if(null==balanceB){
					logger.error("获取转入方余额信息失败！");
					return "获取转入方余额信息失败！";
				}
				//根据业务类型进行转账操作
				if(AccountConstants.JRD_BUSITYPE_INVESTMENT.equals(transOrderInfo.getBusiTypeId())||AccountConstants.JRD_BUSITYPE_RIGHTS_TRANS.equals(transOrderInfo.getBusiTypeId())){//君融贷--投资/债权转让
					//主账户金额不满足时，充值子账户中的金额需要部分转账
					balance=checkInfoService.getBalance(user,"");
					if(null!=balance){
						if(balance.getBalanceSettle()<oAmount){
							nAmount=oAmount-balance.getBalanceSettle();
							transOrderInfo.setAmount(balance.getBalanceSettle());
						}
						balance.setPulseDegree(balance.getPulseDegree()+1);
						balanceB.setPulseDegree(balanceB.getPulseDegree()+1);
						finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, balance,balanceB, entryId, flag);
						if(null!=finanaceEntries&&finanaceEntries.size()>0){
							for(FinanaceEntry finanaceEntry:finanaceEntries) {
								finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);	
								finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
								if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
									finanaceEntry.setRemark("转账-主账户");
								}
								if(finanaceEntry.getPaymentAmount()>0){
									finanaceEntrieAlls.add(finanaceEntry);
								}
							}
						}
						if(nAmount>0){//金额不满足，从充值子账户中扣除
							flag=false;
							user.productId=Constants.JRD_RECHARGE_ACCOUNT_PRODUCT;
							balance=checkInfoService.getBalance(user,"");
							transOrderInfo.setAmount(nAmount);
							if(null!=balance){
								balance.setPulseDegree(balance.getPulseDegree()+1);
								balanceB.setPulseDegree(balanceB.getPulseDegree()+1);
								finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, balance,balanceB, entryId, flag);
								if(null!=finanaceEntries&&finanaceEntries.size()>0){
									for(FinanaceEntry finanaceEntry:finanaceEntries) {
										finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);	
										finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
										if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
											finanaceEntry.setRemark("转账-子账户");
										}
										if(finanaceEntry.getPaymentAmount()>0){
											finanaceEntrieAlls.add(finanaceEntry);
										}
									}
								}
							}else{
								logger.error("获取账户余额信息失败！userId:"+user.userId+";产品号："+user.productId);
								return "获取账户余额信息失败！";
							}
						}
						if(transOrderInfo.getUserFee()!=null&&transOrderInfo.getUserFee()>0){//判断是否有手续费,有手续费扣除手续费
							flag=false;
							//获取君融贷收益户余额信息
							transOrderInfo.setAmount(transOrderInfo.getUserFee());
							balance=checkInfoService.getBalance(user,TransCodeConst.THIRDPARTYID_JRDQYSYZZH);
							if(null!=balance){
								balance.setPulseDegree(balance.getPulseDegree()+1);
								balanceB.setPulseDegree(balanceB.getPulseDegree()+1);
								finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, balanceB,balance, entryId, flag);
								if(null!=finanaceEntries&&finanaceEntries.size()>0){
									for(FinanaceEntry finanaceEntry:finanaceEntries) {
										finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);	
										finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
										if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
											finanaceEntry.setRemark("转账-手续费");
										}
										if(finanaceEntry.getPaymentAmount()>0){
											finanaceEntrieAlls.add(finanaceEntry);
										}
									}
								}
							}else{
								logger.error("获取账户余额信息失败！userId:"+user.userId+";产品号："+user.productId);
								return "获取账户余额信息失败！";
							}
						}
					}else{
						logger.error("获取账户余额信息失败！userId:"+user.userId+";产品号："+user.productId);
						return "获取账户余额信息失败！";
					}
				}else if(AccountConstants.JRD_BUSITYPE_RECEIVABLE.equals(transOrderInfo.getBusiTypeId())){//君融贷--回款
					//回款---充值子账户金额优先，后主账户，此后融资子账户 。无需手续费
					user.productId=Constants.JRD_RECHARGE_ACCOUNT_PRODUCT;
					balance=checkInfoService.getBalance(user,"");
					if(null!=balance){
						if(balance.getBalanceSettle()<oAmount){
							nAmount=oAmount-balance.getBalanceSettle()-transOrderInfo.getUserFee();
							transOrderInfo.setAmount(balance.getBalanceSettle());
						}
						balance.setPulseDegree(balance.getPulseDegree()+1);
						balanceB.setPulseDegree(balanceB.getPulseDegree()+1);
						finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, balance,balanceB, entryId, flag);
						if(null!=finanaceEntries&&finanaceEntries.size()>0){
							for(FinanaceEntry finanaceEntry:finanaceEntries) {
								finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);	
								finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
								if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
									finanaceEntry.setRemark("转账-子账户");
								}
								if(finanaceEntry.getPaymentAmount()>0){
									finanaceEntrieAlls.add(finanaceEntry);
								}
							}
							if(nAmount>0){//主账户扣款部分金额
								flag=false;
								user.productId=orderAuxiliary.getProductQAA();
								balance=checkInfoService.getBalance(user,"");
								transOrderInfo.setAmount(nAmount);
								if(null!=balance){
									if(nAmount>balance.getBalanceSettle()){
										nAmount=nAmount-balance.getBalanceSettle()-transOrderInfo.getUserFee();
										transOrderInfo.setAmount(balance.getBalanceSettle());
									}else{
										nAmount=0l;
									}
									
									balance.setPulseDegree(balance.getPulseDegree()+1);
									balanceB.setPulseDegree(balanceB.getPulseDegree()+1);
									finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, balance,balanceB, entryId, flag);
									if(null!=finanaceEntries&&finanaceEntries.size()>0){
										for(FinanaceEntry finanaceEntry:finanaceEntries) {
											finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);	
											finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
											if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
												finanaceEntry.setRemark("转账-主账户");
											}
											if(finanaceEntry.getPaymentAmount()>0){
												finanaceEntrieAlls.add(finanaceEntry);
											}
										}
										if(nAmount>0){//融资子账户扣款部分金额
											flag=false;
											user.productId=Constants.JRD_FINANCED_ACCOUNT_PRODUCT;
											balance=checkInfoService.getBalance(user,"");
											transOrderInfo.setAmount(nAmount);
											if(null!=balance){
												balance.setPulseDegree(balance.getPulseDegree()+1);
												balanceB.setPulseDegree(balanceB.getPulseDegree()+1);
												finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, balance,balanceB, entryId, flag);
												if(null!=finanaceEntries&&finanaceEntries.size()>0){
													for(FinanaceEntry finanaceEntry:finanaceEntries) {
														finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);	
														finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
														if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
															finanaceEntry.setRemark("转账-融资子账户");
														}
														if(finanaceEntry.getPaymentAmount()>0){
															finanaceEntrieAlls.add(finanaceEntry);
														}
													}
												}
											}else{
												logger.error("获取账户余额信息失败！userId:"+user.userId+";产品号："+user.productId);
												return "获取账户余额信息失败！";
											}
										}
									}
								}else{
									logger.error("获取账户余额信息失败！userId:"+user.userId+";产品号："+user.productId);
									return "获取账户余额信息失败！";
								}
							}
						}
					}else{
						logger.error("获取账户余额信息失败！userId:"+user.userId+";产品号："+user.productId);
						return "获取账户余额信息失败！";
					}
//								}
//								else if(AccountConstants.JRD_BUSITYPE_RIGHTS_TRANS.equals(transOrderInfo.getBusiTypeId())){//君融贷--债权转让
//									//债权转让 主账户金额优先，后充值子账户   转入B主账户
//									
				
				}else if(AccountConstants.JRD_BUSITYPE_INVEST.equals(transOrderInfo.getBusiTypeId())){
					logger.info("---------君融贷投资----------");
					//转出方可用余额查询
					balance = checkInfoService.getBalance(user,"");
					if(null == balance){
						logger.error("获取转出方余额信息失败！");
						return "获取转出方余额信息失败！";
					}
					if(balance.getBalanceSettle() < oAmount){
						logger.error("清算余额/可提现余额小于投资金额！");
						return "清算余额/可提现余额小于投资金额！";
					}
					
					balance.setPulseDegree(balance.getPulseDegree() + 1);
					//获取转出方流水
					FinanaceEntry finanaceEntry = checkInfoService.getFinanaceEntry(transOrderInfo, balance, entryId, false);
					if(finanaceEntry != null){
						if(null == finanaceEntry.getRemark() || "".equals(finanaceEntry.getRemark())){
							finanaceEntry.setRemark("君融贷投资");
						}
						if(finanaceEntry.getPaymentAmount() > 0){
							finanaceEntrieAlls.add(finanaceEntry);
						}
					}else{
						finanaceEntrieAlls.clear();
						logger.info("-----------获取转出方流水为空-------------");
					}
					
					//君融贷已用活期存款子账户	M000005	141223100000028	10002	141223100000013	P000032
					Balance balanceYYHQCK = checkInfoService.getBalance(user,"141223100000028");
					if(null == balanceYYHQCK){
						logger.error("获取君融贷已用活期存款子账户余额信息失败！");
						return "获取君融贷已用活期存款子账户余额信息失败！";
					}
					balanceYYHQCK.setPulseDegree(balanceYYHQCK.getPulseDegree() + 1);
					finanaceEntry = checkInfoService.getFinanaceEntry(transOrderInfo, balanceYYHQCK, entryId, true);
					if(finanaceEntry != null){
						if(null == finanaceEntry.getRemark() || "".equals(finanaceEntry.getRemark())){
							finanaceEntry.setRemark("君融贷投资");
						}
						if(finanaceEntry.getPaymentAmount() > 0){
							finanaceEntrieAlls.add(finanaceEntry);
						}
					}else{
						finanaceEntrieAlls.clear();
						logger.info("---------获取君融贷已用活期存款子账户流水为空-------------");
					}
					
					balanceB.setPulseDegree(balanceB.getPulseDegree() + 1);
					Long userFee = transOrderInfo.getUserFee() != null ? transOrderInfo.getUserFee() : 0;
					transOrderInfo.setAmount(transOrderInfo.getAmount() - userFee);
					//获取转入方流水
					finanaceEntry = checkInfoService.getFinanaceEntry(transOrderInfo, balanceB, entryId, true);
					if(finanaceEntry != null){
						if(null == finanaceEntry.getRemark() || "".equals(finanaceEntry.getRemark())){
							finanaceEntry.setRemark("君融贷投资");
						}
						if(finanaceEntry.getPaymentAmount() > 0){
							finanaceEntrieAlls.add(finanaceEntry);
						}
					}else{
						finanaceEntrieAlls.clear();
						logger.info("------------获取转入方流水为空-------------");
					}
					//如果手续费大于0则可获取君融贷企业收益账户流水信息
					if(userFee > 0){
					    logger.info("------------手续费大于零---------------");
						//君融贷企业收益账户 M000005 141223100000014 P000011
						Balance balanceQYSYZH = checkInfoService.getBalance(user,"141223100000014");
						if(null == balanceQYSYZH){
							logger.error("获取君融贷已用活期存款子账户余额信息失败！");
							return "获取君融贷已用活期存款子账户余额信息失败！";
						}
						transOrderInfo.setAmount(userFee);
						balanceQYSYZH.setPulseDegree(balanceQYSYZH.getPulseDegree() + 1);
						finanaceEntry = checkInfoService.getFinanaceEntry(transOrderInfo, balanceQYSYZH, entryId, true);
						if(finanaceEntry != null){
							if(null == finanaceEntry.getRemark() || "".equals(finanaceEntry.getRemark())){
								finanaceEntry.setRemark("君融贷投资");
							}
							if(finanaceEntry.getPaymentAmount() > 0){
								finanaceEntrieAlls.add(finanaceEntry);
							}
						}else{
							finanaceEntrieAlls.clear();
							logger.info("-----------获取君融贷企业收益账户为空-----------");
						}
					}
				}else{
					//常规转账交易
					user.productId=orderAuxiliary.getProductQAA();
					balance=checkInfoService.getBalance(user,"");
					if(null!=balance){
						balance.setPulseDegree(balance.getPulseDegree()+1);
						balanceB.setPulseDegree(balanceB.getPulseDegree()+1);
						finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, balance,balanceB, entryId, flag);
						if(null!=finanaceEntries&&finanaceEntries.size()>0){
							for(FinanaceEntry finanaceEntry:finanaceEntries) {
								finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);	
								finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
								if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
									finanaceEntry.setRemark("转账");
								}
								if(finanaceEntry.getPaymentAmount()>0){
									finanaceEntrieAlls.add(finanaceEntry);
								}
							}
						}
					}else{
						logger.error("获取账户余额信息失败！userId:"+user.userId+";产品号："+user.productId);
						return "获取账户余额信息失败！";
					}
				}
				//批量插入记录流水表   写入数据失败尝试三次
				if(isOk.equals("ok")){
					transOrderInfo.setAmount(oAmount);
					transOrderInfo.setRemark(transOrderInfo.getBusiTypeId());
					try {
						insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo, productIdStrings);
					} catch (AccountException e) {
						logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败一次");
						try {
							insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo, productIdStrings);
						} catch (AccountException e1) {
							logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败两次");
							try {
								insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo, productIdStrings);
							} catch (AccountException e2) {
								//发送邮件或短信通知管理员
								logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败三次");
								//启用线程 发送邮件
								RkylinMailUtil.sendMailThread("账户记账流水失败通知","******************账户"+transOrderInfo.getUserId()+"做转账记账流水数据入库连续失败三次，转入方"+transOrderInfo.getInterMerchantCode(), TransCodeConst.FINANACE_ENTRY_ERROR_TOEMAIL);
								logger.error(e2.getMessage());
								isOk = "数据库操作异常";
							}
						}
					}
				}
				
			} catch (AccountException e) {
				logger.error(e.getMessage());
				isOk = "数据库操作异常";
			}
			logger.info("---------------------------账户UserId"+transOrderInfo.getUserId()+"向账户"+transOrderInfo.getInterMerchantCode()+"转入"+transOrderInfo.getAmount()+"分，转账操作结束------------------------");
			return isOk;
		}
	}
	
	/**
	 * 校验订单号是否存在
	 * @param orderno
	 * @param merchantcode
	 * @return
	 */
	public boolean orderNoChk(String orderno, String merchantcode) {
		TransOrderInfoQuery tquery = new TransOrderInfoQuery();
		tquery.setOrderNo(orderno);
		tquery.setMerchantCode(merchantcode);
		List<TransOrderInfo> orderList=transOrderInfoManager.queryList(tquery);
		if (orderList != null && orderList.size() > 0) {
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 实时代收数据合法性校验
	 * @param paramMap
	 * @return
	 */
	private ErrorResponse verifyDataCollection(Map<String, String[]> paramMap){
		ErrorResponse response=new ErrorResponse();
		response.setCode("P1");
		if (paramMap.get("funccode")==null || paramMap.get("funccode").length==0) {
			response.setMsg("funccode不能为空！");
			return response;
		}
		if(!TransCodeConst.PAYMENT_COLLECTION.equals(paramMap.get("funccode")[0]) && !TransCodeConst.PAYMENT_REAL_TIME_COLLECTION.equals(paramMap.get("funccode")[0])){
			response.setMsg("funccode输入错误!");
			return response;
		}
		String[] paramArray = null;
		if(TransCodeConst.PAYMENT_COLLECTION.equals(paramMap.get("funccode")[0])){
			paramArray = new String[]{"amount","userid","merchantcode","orderamount","ordercount",
					"orderdate","orderno","productid","requesttime","status","userfee","useripaddress"};
		}else{
			paramArray = new String[]{"amount","userid","merchantcode","ordercount",
					"orderdate","orderno","productid","requesttime","status","userfee","useripaddress"};
		}
		for (String param:paramArray) {
			if (!ValHasNoParam.hasParam(paramMap,param)) {
				response.setMsg(param+"不能为空！");
				return response;
			}
		}
		if(!DateUtil.isYYYYMMDDHHMMSS(paramMap.get("orderdate")[0])){
			response.setMsg("orderdate格式不合法！");
			return response;
		}
		if(!DateUtil.isYYYYMMDDHHMMSS(paramMap.get("requesttime")[0])){
			response.setMsg("requesttime格式不合法！");
			return response;
		}
		if(!"1".equals(paramMap.get("status")[0]) && !"0".equals(paramMap.get("status")[0])){
			response.setMsg("status格式不合法！");
			return response;
		}
		if(!VerifyUtil.isIP(paramMap.get("useripaddress")[0])){
			response.setMsg("useripaddress格式不合法！");
			return response;
		}
		response.setCode(null);
		response.setIs_success(true);
		return response;
	}
	
	/**
	 * 把数据封装成TransOrderInfo 对象；
	 * @param paramMap
	 * @return
	 */
	private TransOrderInfo getTransOrderInfoByParams(Map<String, String[]> paramMap) {
		TransOrderInfo transOrderInfo=new TransOrderInfo();
		Long amount = 0L;
		Long userFee = 0L;
		String funCode = null;
		for(Object keyObj : paramMap.keySet().toArray()){
			String[] strs = paramMap.get(keyObj);
			for(String value : strs){
				if(keyObj.equals("userid")){
					transOrderInfo.setUserId(value);
				}else if(keyObj.equals("productid")){
					transOrderInfo.setProductIdd(value);;
				}else if (keyObj.equals("amount")) {
					amount = Long.parseLong(value);
					transOrderInfo.setAmount(amount);
				}else if(keyObj.equals("bankcode")){
					transOrderInfo.setBankCode(value);
				}else if(keyObj.equals("busitypeid")){
					transOrderInfo.setBusiTypeId(value);
				}else if(keyObj.equals("errorcode")){
					transOrderInfo.setErrorCode(value);
				}else if(keyObj.equals("errormsg")){
					transOrderInfo.setErrorMsg(value);
				}else if(keyObj.equals("funccode")){
					transOrderInfo.setFuncCode(value);
					funCode = value;
				}else if(keyObj.equals("userfee")){
					userFee = Long.parseLong(value);
					transOrderInfo.setUserFee(userFee);
				}else if(keyObj.equals("orderamount")){
					if (value!=null && !"".equals(value)) {
						transOrderInfo.setOrderAmount(Long.parseLong(value));	
					}
				}else if(keyObj.equals("intermerchantcode")){
					transOrderInfo.setInterMerchantCode(value);
				}else if(keyObj.equals("merchantcode")|| keyObj.equals("root_inst_cd")){
					transOrderInfo.setMerchantCode(value);
				}else if(keyObj.equals("ordercount")){
					transOrderInfo.setOrderCount(Integer.parseInt(value));
				}else if(keyObj.equals("orderdate")){
					transOrderInfo.setOrderDate(DateUtil.parseDate(value, Constants.DATE_FORMAT_YYYYMMDDHHMMSS));
				}else if(keyObj.equals("orderno")){
					transOrderInfo.setOrderNo(value);
				}else if(keyObj.equals("orderpackageno")){
					transOrderInfo.setOrderPackageNo(value);
				}else if(keyObj.equals("paychannelid")){
					transOrderInfo.setPayChannelId(value);
				}else if(keyObj.equals("profit")){
					transOrderInfo.setProfit(Long.parseLong(value));
				}else if(keyObj.equals("remark")){
					transOrderInfo.setRemark(StringUtils.subStr(value,120));
				}else if(keyObj.equals("requestid")){
					transOrderInfo.setRequestId(Integer.parseInt(value));
				}else if(keyObj.equals("requestno")){
					transOrderInfo.setRequestNo(value);
				}else if(keyObj.equals("requesttime")){
						transOrderInfo.setRequestTime(DateUtil.parseDate(value, Constants.DATE_FORMAT_YYYYMMDDHHMMSS));
				}else if(keyObj.equals("status")){
					transOrderInfo.setStatus(Integer.parseInt(value));
				}else if(keyObj.equals("transtype")){
					transOrderInfo.setTransType(Integer.parseInt(value));
				}else if(keyObj.equals("useripaddress")){
					transOrderInfo.setUserIpAddress(value);
				}
			}
		}
		//如果是实时代收，计算订单金额
		if(TransCodeConst.PAYMENT_REAL_TIME_COLLECTION.equals(funCode)){
			transOrderInfo.setOrderAmount(amount+userFee);
		}
		String accountDate=generationPaymentService.getAccountDate();//账期
		transOrderInfo.setAccountDate(DateUtils.getDate(accountDate,Constants.DATE_FORMAT_YYYYMMDD));
		return transOrderInfo;
	}
	
	/**
	 * 代收(dubbo)
	 * @param transOrderInfo
	 * @return
	 */
	public CommonResponse collection(TransOrderInfo transOrderInfo){
		logger.info("代收所有入参: "+BeanUtil.getBeanVal(transOrderInfo, null));
		CommonResponse res = new CommonResponse();
		if (transOrderInfo==null) {
			res.setCode(CodeEnum.ERR_PARAM_NULL.getCode());
			res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
			return res;
		}
		res = verifyDataCollection(transOrderInfo);
		if (CodeEnum.FAILURE.getCode().equals(res.getCode())) {
			return res;
		}
		//如果是实时代收，计算订单金额
		if(TransCodeConst.PAYMENT_REAL_TIME_COLLECTION.equals(transOrderInfo.getFuncCode())){
			transOrderInfo.setOrderAmount(transOrderInfo.getAmount()+transOrderInfo.getUserFee());
		}
		String accountDate=generationPaymentService.getAccountDate();//账期
		transOrderInfo.setAccountDate(DateUtils.getDate(accountDate,Constants.DATE_FORMAT_YYYYMMDD));
		ErrorResponse response = collectionRecord(transOrderInfo);
		if (!response.isIs_success()) {
			res.setCode(CodeEnum.FAILURE.getCode());
			if ("P2".equals(response.getCode())) {
				res.setCode(CodeEnum.PROCESSING.getCode());
			}
			res.setMsg(response.getMsg());
			return res;
		}
		return res;
	}
	
	/**
	 * 代收校验字段值（dubbo）
	 * @param transOrderInfo
	 * @return
	 */
	private CommonResponse verifyDataCollection(TransOrderInfo transOrderInfo) {
		CommonResponse res = new CommonResponse();
		res.setCode(CodeEnum.FAILURE.getCode());
		if (transOrderInfo.getFuncCode()==null || "".equals(transOrderInfo.getFuncCode())) {
			res.setMsg("funccode不能为空！");
			return res;
		}
		if(!TransCodeConst.PAYMENT_COLLECTION.equals(transOrderInfo.getFuncCode()) && !TransCodeConst.PAYMENT_REAL_TIME_COLLECTION.equals(transOrderInfo.getFuncCode())){
			res.setMsg("funccode输入错误!");
			return res;
		}
		String[] paramArray = null;
		if(TransCodeConst.PAYMENT_COLLECTION.equals(transOrderInfo.getFuncCode())){
			paramArray = new String[]{"amount","userId","merchantCode","orderAmount","orderCount",
					"orderDate","orderNo","productIdd","requestTime","status","userFee","userIpAddress"};
		}else{
			paramArray = new String[]{"amount","userId","merchantCode","orderCount",
					"orderDate","orderNo","productIdd","requestTime","status","userFee","userIpAddress"};
		}
		String field = BeanUtil.validateBeanProEmpty(transOrderInfo, paramArray);
		if (field!=null) {
			res.setMsg(field+"不能为空！");
			return res;
		}
		if(TransCodeConst.TRANS_STATUS_NORMAL!=transOrderInfo.getStatus() && TransCodeConst.TRANS_STATUS_INVALIDATION!=transOrderInfo.getStatus()){
			res.setMsg("status格式不合法！");
			return res;
		}
		if(!VerifyUtil.isIP(transOrderInfo.getUserIpAddress())){
			res.setMsg("useripaddress格式不合法！");
			return res;
		}
		res.setCode(CodeEnum.SUCCESS.getCode());
		return res;
	}

	/**
	 * 代收|实时代收
	 * @param paramMap
	 * @return
	 */
	@Transactional
	public Response collectionBiz(Map<String, String[]> paramMap){
		StringBuffer paramsBuffer = new StringBuffer("代收 入参: ");
		//仅仅用作打印日志
		for (Object keyObj : paramMap.keySet().toArray()) {
			String[] strs = paramMap.get(keyObj);
			if (strs == null) {
				continue;
			}
			for (String value : strs) {
				paramsBuffer.append(keyObj + "=" + value + ", ");
			}
		}
		logger.info("代收 入参==" + paramsBuffer.toString());
		//校验数据合法性
		ErrorResponse response = verifyDataCollection(paramMap);
		if (!response.isIs_success()) {
			return response;
		}
		//把数据封装成TransOrderInfo 对象；
		TransOrderInfo transOrderInfo = getTransOrderInfoByParams(paramMap);
		response = collectionRecord(transOrderInfo);
		if (!response.isIs_success()) {
			return response;
		}
		return response;
	}

	/**
	 * （实时）代收，落单，记账（提交到代收付系统）
	 * @param transOrderInfo
	 * @return
	 */
	private ErrorResponse collectionRecord(TransOrderInfo transOrderInfo) {
		//代收（非实时）
		ErrorResponse response = new ErrorResponse();
		if (TransCodeConst.PAYMENT_COLLECTION.equals(transOrderInfo.getFuncCode())) {
			//判断订单状态是否为失效状态 ，如为失效 则不记录分录信息
			if (TransCodeConst.TRANS_STATUS_INVALIDATION == transOrderInfo.getStatus()) {
				response = this.saveTransorder(transOrderInfo, "代收");
			} else {
				response = this.collection(transOrderInfo, transOrderInfo.getProductIdd());
			}
		} else { //实时代收
			//判断订单状态是否为失效状态 ，如为失效,只记录订单信息，不记录分录信息
			if (TransCodeConst.TRANS_STATUS_INVALIDATION == transOrderInfo.getStatus()) {
				response = this.saveTransorder(transOrderInfo, "实时代收");
			} else {
				if (!this.orderNoChk(transOrderInfo.getOrderNo(), transOrderInfo.getMerchantCode())
						&& transOrderInfo.getStatus() != TransCodeConst.TRANS_STATUS_PROCESSING) {
					logger.info("账户UserId：" + transOrderInfo.getUserId() + " 订单号：" + transOrderInfo.getOrderNo()
							+ "在机构号：" + transOrderInfo.getMerchantCode() + "中已存在");
					response.setIs_success(false);
					response.setCode("P1");
					response.setMsg("该交易订单号已存在，请确认！");
					return response;
				}
				User user = new User();
				user.userId = transOrderInfo.getUserId();
				user.constId = transOrderInfo.getMerchantCode();
				user.productId = transOrderInfo.getProductIdd() == null ? transOrderInfo.getErrorCode()
						: transOrderInfo.getProductIdd();
				//判断账户状态是否正常
				boolean accountIsOK = operationService.checkAccount(user);
				if (!accountIsOK) {
					logger.info("账户UserId：" + transOrderInfo.getUserId() + " 订单号：" + transOrderInfo.getOrderNo()
							+ "  账户状态非正常状态");
					response.setMsg("账户状态非正常状态");
					transOrderInfo.setStatus(TransCodeConst.TRANS_STATUS_INVALIDATION);
					transOrderInfo.setErrorCode(transOrderInfo.getProductIdd());
					transOrderInfo.setErrorMsg("账户状态非正常状态");
					transOrderInfoManager.saveTransOrderInfo(transOrderInfo);
					response.setIs_success(false);
					response.setCode("P1");
					return response;
				}
				if (!settlementServiceThr.isDayEndOk()) {
					logger.info("实时代收,订单号==" + transOrderInfo.getOrderNo() + ",机构号 =="
							+ transOrderInfo.getMerchantCode() + "   日终没有正常结束！");
					response.setCode("P1");
					response.setMsg(CodeEnum.ERR_SYS_DAYEND_NOT_OK.getMessage());
					transOrderInfo.setStatus(TransCodeConst.TRANS_STATUS_INVALIDATION);
					transOrderInfo.setErrorCode(transOrderInfo.getProductIdd());
					transOrderInfo.setErrorMsg(CodeEnum.ERR_SYS_DAYEND_NOT_OK.getMessage());
					transOrderInfoManager.saveTransOrderInfo(transOrderInfo);
					response.setIs_success(false);
					response.setCode("P1");
					return response;
				}
				// 提交到代收付系统
				String code = generationPaymentService.submitToCollRealTime(transOrderInfo);
				if (CodeEnum.SUCCESS.getCode().equals(code)) { //如果成功记账，生成流水及订单信息
						//如果成功记账，生成流水及订单信息
					response = this.collectionRealTime(transOrderInfo);
					//如果失败，只记录订单信息
					if (!response.isIs_success()) {
						transOrderInfo.setStatus(TransCodeConst.TRANS_STATUS_PAY_FAILED);
						transOrderInfo.setErrorMsg(response.getMsg());
						transOrderInfo.setErrorCode(transOrderInfo.getProductIdd());
						transOrderInfoManager.saveTransOrderInfo(transOrderInfo);
						response.setIs_success(false);
					}
				} else if ("10".equals(code) || CodeEnum.ERR_CRPS_CALL_ERROR.getCode().equals(code)) { //处理中
					transOrderInfo.setStatus(TransCodeConst.TRANS_STATUS_PROCESSING);
					transOrderInfo.setErrorCode(transOrderInfo.getProductIdd());
					transOrderInfoManager.saveTransOrderInfo(transOrderInfo);
					response.setCode("P2");
					response.setMsg("处理中！");
					response.setIs_success(false);
					return response;
				} else if (CodeEnum.FAILURE.getCode().equals(code)) { // 失败
					transOrderInfo.setStatus(TransCodeConst.TRANS_STATUS_PAY_FAILED);
					transOrderInfo.setErrorCode(transOrderInfo.getProductIdd());
					transOrderInfoManager.saveTransOrderInfo(transOrderInfo);
					response.setMsg(transOrderInfo.getErrorMsg());
					response.setIs_success(false);
				}else{	//校验性失败
					transOrderInfo.setStatus(TransCodeConst.TRANS_STATUS_INVALIDATION);
					transOrderInfo.setErrorCode(transOrderInfo.getProductIdd());
					transOrderInfoManager.saveTransOrderInfo(transOrderInfo);
					response.setMsg(transOrderInfo.getErrorMsg());
					response.setIs_success(false);	
				}
			}
		}
		return response;
	}
	
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	@Override
	public Response doJob(Map<String, String[]> paramMap, String methodName) {
	    if ("ruixue.wheatfield.user.transfer.batch".equals(methodName)) {
	        return transBatch(paramMap);
        }
		String reCode = "P0";
		String reMsg = "成功";
		ErrorResponse response=new ErrorResponse();
		PaymengManagerResponse okResponse=new PaymengManagerResponse();
		if("ruixue.wheatfield.user.collection".equals(methodName)) {//  代收|实时代收
			return collectionBiz(paramMap);
		}else if("ruixue.wheatfield.balance.get".equals(methodName)||"ruixue.wheatfield.user.getadvancebalance".equals(methodName)){
			if(!ValHasNoParam.hasParam(paramMap, "userid")){			
				reCode = "P1";
				reMsg = "userid不能为空";			
			}else if(!ValHasNoParam.hasParam(paramMap, "usertype")){
				reCode = "P1";
				reMsg = "usertype不能为空";
			}else if(!ValHasNoParam.hasParam(paramMap, "constid")){
				reCode = "P1";
				reMsg = "constid不能为空";
			}else if(!ValHasNoParam.hasParam(paramMap, "productid")){
				reCode = "P1";
				reMsg = "productid不能为空";
			}
			if("ruixue.wheatfield.user.getadvancebalance".equals(methodName)){
				if(!ValHasNoParam.hasParam(paramMap, "querytype")){
					reCode = "P1";
					reMsg = "querytype不能为空";
				}
			}
		}else{
			if("ruixue.wheatfield.user.antideduct".equals(methodName)||"ruixue.wheatfield.user.consumptionbeforerefund".equals(methodName)||"ruixue.wheatfield.user.wipeaccount".equals(methodName)){
				if(!ValHasNoParam.hasParam(paramMap, "funccode")){
					reCode = "P1";
					reMsg = "funccode不能为空";
				}else if(!ValHasNoParam.hasParam(paramMap, "orderno")){
					reCode = "P1";
					reMsg = "orderno不能为空";
				}else if(!ValHasNoParam.hasParam(paramMap, "useripaddress")){
					reCode = "P1";
					reMsg = "useripaddress不能为空";
				}else if(!ValHasNoParam.hasParam(paramMap, "root_inst_cd")){
					reCode = "P1";
					reMsg = "root_inst_cd不能为空";
				}else if(!ValHasNoParam.hasParam(paramMap, "orderpackageno")){
					reCode = "P1";
					reMsg = "orderpackageno不能为空";
				}
			}else{
				if("ruixue.wheatfield.user.transfer".equals(methodName)
						|| "ruixue.wheatfield.user.transfer.new".equals(methodName)
						|| "ruixue.wheatfield.user.transfer.common".equals(methodName)
						|| "ruixue.wheatfield.redenvelope.exchange".equals(methodName)
						|| "ruixue.wheatfield.demand.redemption".equals(methodName)){
					if(!ValHasNoParam.hasParam(paramMap, "intermerchantcode")){
						reCode = "P1";
						reMsg = "intermerchantcode不能为空";
						response.setCode(reCode);
						response.setMsg(reMsg);
						return response;
					}
				}
				if(!"ruixue.wheatfield.demand.redemption".equals(methodName)){
					if(!ValHasNoParam.hasParam(paramMap, "amount")){
						reCode = "P1";
						reMsg = "amount不能为空";
					}
				}
//					else if(!ValHasNoParam.hasParam(paramMap, "feeamount")){
//					reCode = "P1";
//					reMsg = "feeamount不能为空"; 
//				}
				if(!ValHasNoParam.hasParam(paramMap, "userid")){
					reCode = "P1";
					reMsg = "userid不能为空";
				}else if(!ValHasNoParam.hasParam(paramMap, "funccode")){
					reCode = "P1";
					reMsg = "funccode不能为空";
				}else if(!ValHasNoParam.hasParam(paramMap, "merchantcode")){
					reCode = "P1";
					reMsg = "merchantcode不能为空";
				}
//				else if(!ValHasNoParam.hasParam(paramMap, "orderamount")){
//					reCode = "P1";
//					reMsg = "orderamount不能为空";
//				}
				else if(!ValHasNoParam.hasParam(paramMap, "ordercount")){
					reCode = "P1";
					reMsg = "ordercount不能为空";
				}else if(!ValHasNoParam.hasParam(paramMap, "orderdate")){
					reCode = "P1";
					reMsg = "orderdate不能为空";
				}else if(!ValHasNoParam.hasParam(paramMap, "orderno")){
					reCode = "P1";
					reMsg = "orderno不能为空";
				}else if(!ValHasNoParam.hasParam(paramMap, "requesttime")){
					reCode = "P1";
					reMsg = "requesttime不能为空";
				}else if(!ValHasNoParam.hasParam(paramMap, "status")){
					reCode = "P1";
					reMsg = "status不能为空";
				}else if(!ValHasNoParam.hasParam(paramMap, "userfee")){
					reCode = "P1";
					reMsg = "userfee不能为空";
				}else if(!ValHasNoParam.hasParam(paramMap, "productid")){
					reCode = "P1";
					reMsg = "productid不能为空";
				}else if(!ValHasNoParam.hasParam(paramMap, "useripaddress")){
					reCode = "P1";
					reMsg = "useripaddress不能为空";
				}
				
				if("ruixue.wheatfield.user.thaw.authcode".equals(methodName)){
					if(!ValHasNoParam.hasParam(paramMap, "authcode")){
						reCode = "P1";
						reMsg = "authcode不能为空";
					}
				}
			}
		}
		User user=new User();
		DateUtil dateUtil=new DateUtil();
		TransOrderInfo transOrderInfo=new TransOrderInfo();
		OrderAuxiliary orderAuxiliary=new OrderAuxiliary();
		String productId=null;//产品号
		String referUserId=null;//第三方用户号
		String queryType=null;
		for(Object keyObj : paramMap.keySet().toArray()){
			System.out.println("key:" + keyObj);
			String[] strs = paramMap.get(keyObj);
			for(String value : strs){
				logger.info("参数KEY="+keyObj+" Value="+value);
				if(keyObj.equals("userid")){//*****User Start*******
					user.userId=value;
					transOrderInfo.setUserId(value);
				}else if(keyObj.equals("usertype")){
					//user.userType=UserTypeEnum.toEnum(value);
					//user.uEType=value;
				}else if(keyObj.equals("constid")){
					user.constId=value;					
				}else if(keyObj.equals("productid")){
					user.productId=value;
					productId=value;
					orderAuxiliary.setProductQAA(value);
				}else if(keyObj.equals("role")){//*****User End*********
					user.role=value;
				}else if (keyObj.equals("amount")) {//*******TransOrderInfo Start********
					transOrderInfo.setAmount(Long.parseLong(value));
				}else if(keyObj.equals("bankcode")){
					transOrderInfo.setBankCode(value);
				}else if(keyObj.equals("busitypeid")){
					transOrderInfo.setBusiTypeId(value);
				}else if(keyObj.equals("errorcode")){
					transOrderInfo.setErrorCode(value);
				}else if(keyObj.equals("errormsg")){
					transOrderInfo.setErrorMsg(value);
				}else if(keyObj.equals("feeamount")){
					transOrderInfo.setFeeAmount(Long.parseLong(value));
				}else if(keyObj.equals("funccode")){
					transOrderInfo.setFuncCode(value);
				}else if(keyObj.equals("intermerchantcode")){
					transOrderInfo.setInterMerchantCode(value);
				}else if(keyObj.equals("merchantcode")|| keyObj.equals("root_inst_cd")){
					transOrderInfo.setMerchantCode(value);
				}else if(keyObj.equals("orderamount")){
					transOrderInfo.setOrderAmount(Long.parseLong(value));
				}else if(keyObj.equals("ordercount")){
					transOrderInfo.setOrderCount(Integer.parseInt(value));
				}else if(keyObj.equals("orderdate")){
					try {
						transOrderInfo.setOrderDate(dateUtil.parse(value, Constants.DATE_FORMAT_YYYYMMDDHHMMSS));
					} catch (ParseException e) {
						reCode = "P2";
						reMsg = "orderdate日期格式错误";
						e.printStackTrace();
					}
				}else if(keyObj.equals("orderno")){
					transOrderInfo.setOrderNo(value);
				}else if(keyObj.equals("orderpackageno")){
					transOrderInfo.setOrderPackageNo(value);
				}else if(keyObj.equals("paychannelid")){
					transOrderInfo.setPayChannelId(value);
				}else if(keyObj.equals("profit")){
					transOrderInfo.setProfit(Long.parseLong(value));
				}else if(keyObj.equals("remark")){
					transOrderInfo.setRemark(StringUtils.subStr(value,120));
				}else if(keyObj.equals("requestid")){
					transOrderInfo.setRequestId(Integer.parseInt(value));
				}else if(keyObj.equals("requestno")){
					transOrderInfo.setRequestNo(value);
				}else if(keyObj.equals("requesttime")){
					try {
						transOrderInfo.setRequestTime(dateUtil.parse(value, Constants.DATE_FORMAT_YYYYMMDDHHMMSS));
					} catch (ParseException e) {
						reCode = "P2";
						reMsg = "requesttime日期格式错误";
						e.printStackTrace();
					}
				}else if(keyObj.equals("status")){
					transOrderInfo.setStatus(Integer.parseInt(value));
				}else if(keyObj.equals("tradeflowno")){
					transOrderInfo.setTradeFlowNo(value);
					orderAuxiliary.setProductQAB(value);
				}else if(keyObj.equals("transtype")){
					transOrderInfo.setTransType(Integer.parseInt(value));
				}else if(keyObj.equals("userfee")){
					transOrderInfo.setUserFee(Long.parseLong(value));
				}else if(keyObj.equals("referuserid")){
					if(!"".equals(value)){
						referUserId=value;
						user.referUserId=value;
					}					
				}else if(keyObj.equals("useripaddress")){//*******TransOrderInfo End******
					transOrderInfo.setUserIpAddress(value);
				}else if(keyObj.equals("querytype")){
					queryType=value;
				}else if(keyObj.equals("authcode")){
					orderAuxiliary.setAuthCode(value);
				}else if(keyObj.equals("intoproductid")){
					orderAuxiliary.setProductQAB(value);
				} else if ( keyObj.equals("capitalamount") ) {
					orderAuxiliary.setCapitalAmount(Long.parseLong(value));
				} else if ( keyObj.equals("interestamount") ) {
					orderAuxiliary.setInterestamount(Long.parseLong(value));
				} else if ( keyObj.equals("intermerchantcode") ) {
					transOrderInfo.setInterMerchantCode(value);
				}
			}
		}	
		if(!reCode.equals("P0")){
			response.setCode(reCode);
			response.setMsg(reMsg);
			return response;
		}
		if("ruixue.wheatfield.balance.get".equals(methodName)){//获取账户余额
//			if(null!=orderAuxiliary.getAuthCode()&&!"".equals(orderAuxiliary.getAuthCode())){
//				//根据授权码查询余额信息
//				
//			}else{
				FinanaceAccountQuery query=new FinanaceAccountQuery();
				query.setRootInstCd(user.constId);
				query.setAccountRelateId(user.userId);
				query.setGroupManage(user.productId);
				query.setFinAccountTypeId(user.uEType);
				query.setReferUserId(user.referUserId);
//				query.setStatusId(BaseConstants.ACCOUNT_STATUS_OK);
				List<FinanaceAccount> faList=finanaceAccountManager.queryList(query);
				if(faList!=null && faList.size()==1){
					FinanaceAccount fa=faList.get(0);
					String statusId=fa.getStatusId();
					if(BaseConstants.ACCOUNT_STATUS_OK.equals(statusId)
							|| BaseConstants.ACCOUNT_STATUS_FREEZE.equals(statusId)){
						PaymentBalanceinfoResponce payMentBalanceResponce=new PaymentBalanceinfoResponce();			
						payMentBalanceResponce.setBalance(this.getBalance(user,""));
						if(payMentBalanceResponce.getBalance()==null){
							response.setCode("C1");
							response.setMsg("获取账户余额失败,请确认账户信息是否填写正确");
						}else{
							return payMentBalanceResponce;
						}
					}else {
						response.setCode("C1");
						response.setMsg("账户失效，请确认");
					}
				}else{
					response.setCode("C1");
					response.setMsg("账户信息有误，请确认");
				}
//			}			
		}else if("ruixue.wheatfield.user.recharge".equals(methodName)){//充值
			//判断订单状态是否为失效状态 ，如为失效 则不记录分录信息
			if(TransCodeConst.TRANS_STATUS_INVALIDATION==transOrderInfo.getStatus()){
				response=this.saveTransorder(transOrderInfo, "充值");
			}else{
				if(transOrderInfo.getMerchantCode().equals(Constants.TXYW_ID)){
					String result=this.rechargeTXYW(transOrderInfo, productId, transOrderInfo.getUserId());
					if(result.equals("ok")){
						okResponse.setIs_success(true);
						return okResponse;
					}else{
						response.setCode("C1");
						response.setMsg(result);
						response.setIs_success(false);
						return response;
					}
				}else if (Constants.FC_ID.equals(transOrderInfo.getMerchantCode())) {
				    transOrderInfo.setProductIdd(productId);
				    CommonResponse res = rechargeUserFee(transOrderInfo);
                    if(CodeEnum.SUCCESS.getCode().equals(res.getCode())){
                        okResponse.setIs_success(true);
                        return okResponse;
                    }else{
                        response.setCode("C1");
                        response.setMsg(res.getMsg());
                        response.setIs_success(false);
                        return response;
                    }
                }else{
					response=this.recharge(transOrderInfo, productId, transOrderInfo.getUserId());
				}
				
				
			}			
		}else if("ruixue.wheatfield.user.deduct".equals(methodName)){//扣款
			//判断订单状态是否为失效状态 ，如为失效 则不记录分录信息
			if(TransCodeConst.TRANS_STATUS_INVALIDATION==transOrderInfo.getStatus()){
				response=this.saveTransorder(transOrderInfo, "扣款(消费)");
			}else{
				response=this.deduct(transOrderInfo, productId, transOrderInfo.getUserId());
			}			
		}else if("ruixue.wheatfield.user.transfer".equals(methodName)){//转账
			//判断订单状态是否为失效状态 ，如为失效 则不记录分录信息
			if(TransCodeConst.TRANS_STATUS_INVALIDATION==transOrderInfo.getStatus()){
				response=this.saveTransorder(transOrderInfo, "转账");
			}else{
				//第三方UserId暂存errormsg,二期需要考虑
				transOrderInfo.setErrorMsg(referUserId);
				response=this.transfer(transOrderInfo,productId, transOrderInfo.getUserId());
			}
		}else if("ruixue.wheatfield.user.withdrow".equals(methodName)){//提现
			//判断账户提现金额逻辑是否正确
			if(transOrderInfo.getOrderAmount()!=(transOrderInfo.getAmount()+transOrderInfo.getUserFee())){
				response.setCode("C2");
				response.setMsg("请确认订单金额与入账金额和用户手续费是否输入正确！");
				return response;
			}
			//判断订单状态是否为失效状态 ，如为失效 则不记录分录信息
			if(TransCodeConst.TRANS_STATUS_INVALIDATION==transOrderInfo.getStatus()){
				response=this.saveTransorder(transOrderInfo, "提现");
			}else{
				if(transOrderInfo.getMerchantCode().equals(Constants.JRD_ID)){
					String result=this.withdrowJRD(transOrderInfo,orderAuxiliary);
					if(result.equals("ok")){
						okResponse.setIs_success(true);
						return okResponse;
					}else{
						response.setCode("C1");
						response.setMsg(result);
						response.setIs_success(false);
						return response;
					}
				}else if(transOrderInfo.getMerchantCode().equals(Constants.TXYW_ID)){
					String result=this.withdrowTXYW(transOrderInfo,orderAuxiliary);
					if(result.equals("ok")){
						okResponse.setIs_success(true);
						return okResponse;
					}else{
						response.setCode("C1");
						response.setMsg(result);
						response.setIs_success(false);
						return response;
					}
				}else if(transOrderInfo.getMerchantCode().equals(Constants.ZJDY_ID)){
                    String result=this.withdrowZJDY(transOrderInfo,orderAuxiliary);
                    if(result.equals("ok")){
                        okResponse.setIs_success(true);
                        return okResponse;
                    }else{
                        response.setCode("C1");
                        response.setMsg(result);
                        response.setIs_success(false);
                        return response;
                    }
				}else if (transOrderInfo.getMerchantCode().equals(Constants.FC_ID)) {
				    CommonResponse res=withdrowUserFee(transOrderInfo, orderAuxiliary);
                    if(CodeEnum.SUCCESS.getCode().equals(res.getCode())){
                        okResponse.setIs_success(true);
                        return okResponse;
                    }else{
                        response.setCode("C1");
                        response.setMsg(res.getMsg());
                        response.setIs_success(false);
                        return response;
                    }
                }else if (transOrderInfo.getMerchantCode().equals(Constants.LINGKE_ID)) {
				    CommonResponse res=withdrowLingKe(transOrderInfo, orderAuxiliary);
                    if(CodeEnum.SUCCESS.getCode().equals(res.getCode())){
                        okResponse.setIs_success(true);
                        return okResponse;
                    }else{
                        response.setCode("C1");
                        response.setMsg(res.getMsg());
                        response.setIs_success(false);
                        return response;
                    }
                }else{
					response=this.withdrow(transOrderInfo,productId, transOrderInfo.getUserId(),referUserId);
				}
			}
		}else if("ruixue.wheatfield.user.antideduct".equals(methodName)){//商户扣款冲正
			response=this.antiDeduct(transOrderInfo);
		}else if("ruixue.wheatfield.user.advanceorders".equals(methodName)) {//预付金待付
			//判断订单状态是否为失效状态 ，如为失效 则不记录分录信息
			if(TransCodeConst.TRANS_STATUS_INVALIDATION==transOrderInfo.getStatus()){
				response=this.saveTransorder(transOrderInfo, "预付金待付");
			}else{
				response=this.advanceorders(transOrderInfo, productId, referUserId);
			}
		}else if("ruixue.wheatfield.user.consumptionbeforerefund".equals(methodName)) {//消费前退款
			response=this.consumptionbeforerefund(transOrderInfo);
		}else if("ruixue.wheatfield.user.afterspendingrefund".equals(methodName)) {//消费后退款
			//判断订单状态是否为失效状态 ，如为失效 则不记录分录信息
			if(TransCodeConst.TRANS_STATUS_INVALIDATION==transOrderInfo.getStatus()){
				response=this.saveTransorder(transOrderInfo, "消费后退款");
			}else{
				response=this.afterspendingrefund(transOrderInfo,productId, transOrderInfo.getUserId(),referUserId);
			}
		}else if("ruixue.wheatfield.user.preauthorization".equals(methodName)) {//预授权
			//判断订单状态是否为失效状态 ，如为失效 则不记录分录信息
			if(TransCodeConst.TRANS_STATUS_INVALIDATION==transOrderInfo.getStatus()){
				response=this.saveTransorder(transOrderInfo, "预授权");
			}else{
				response=this.preauthorization(transOrderInfo, productId, referUserId);
			}
		}else if("ruixue.wheatfield.user.preauthorizationcomplete".equals(methodName)) {//预授权完成
			//判断订单状态是否为失效状态 ，如为失效 则不记录分录信息
			if(TransCodeConst.TRANS_STATUS_INVALIDATION==transOrderInfo.getStatus()){
				response=this.saveTransorder(transOrderInfo, "预授权完成");
			}else{
				response=this.preauthorizationcomplete(transOrderInfo, productId, referUserId);
			}
		}else if("ruixue.wheatfield.user.withhold".equals(methodName)) {//代付
			//判断订单状态是否为失效状态 ，如为失效 则不记录分录信息
			if(TransCodeConst.TRANS_STATUS_INVALIDATION==transOrderInfo.getStatus()){
				response=this.saveTransorder(transOrderInfo, "代付");
			}else{
			    if ("40141".equals(transOrderInfo.getFuncCode())) {
			        transOrderInfo.setProductIdd(paramMap.get("productid")[0]);
			        CommonResponse res = null;
                    try {
                        res = withhold40141(transOrderInfo);
                    } catch (AccountException e) {
                        logger.info("40141  交易    code="+res.getCode()+",msg="+res.getMsg());
                        response.setCode("C1");
                        response.setMsg(res.getMsg());
                        response.setIs_success(false);
                        return response;
                    }
			        if (!CodeEnum.SUCCESS.getCode().equals(res.getCode())) {
			            logger.info("40141  交易    code="+res.getCode()+",msg="+res.getMsg());
                        response.setCode("C1");
                        response.setMsg(res.getMsg());
                        response.setIs_success(false);
                    }else{
                        response.setIs_success(true);
                    }
			        return response;
                }
	             if ("40142".equals(transOrderInfo.getFuncCode())) {
	                    transOrderInfo.setProductIdd(paramMap.get("productid")[0]);
	                    CommonResponse res = null;
	                    try {
	                        res = withhold40142(transOrderInfo);
	                    } catch (AccountException e) {
	                        logger.info("40142  交易    code="+res.getCode()+",msg="+res.getMsg());
	                        response.setCode("C1");
	                        response.setMsg(res.getMsg());
	                        response.setIs_success(false);
	                        return response;
	                    }
	                    if (!CodeEnum.SUCCESS.getCode().equals(res.getCode())) {
	                        logger.info("40142  交易    code="+res.getCode()+",msg="+res.getMsg());
	                        response.setCode("C1");
	                        response.setMsg(res.getMsg());
	                        response.setIs_success(false);
	                    }else{
	                        response.setIs_success(true);
	                    }
	                    return response;
	             }
	             if ("40143".equals(transOrderInfo.getFuncCode())) {
                     transOrderInfo.setProductIdd(paramMap.get("productid")[0]);
                     CommonResponse res = null;
                     try {
                         res = withhold40143(transOrderInfo);
                     } catch (AccountException e) {
                         logger.info("40143  交易    code="+res.getCode()+",msg="+res.getMsg());
                         response.setCode("C1");
                         response.setMsg("系统异常!");
                         response.setIs_success(false);
                         return response;
                     }
                     if (!CodeEnum.SUCCESS.getCode().equals(res.getCode())) {
                         logger.info("40143  交易    code="+res.getCode()+",msg="+res.getMsg());
                         response.setCode("C1");
                         response.setMsg(res.getMsg());
                         response.setIs_success(false);
                     }else{
                         response.setIs_success(true);
                     }
                     return response;
	             }	 
                 if (TransCodeConst.PAYMENT_40144.equals(transOrderInfo.getFuncCode())) {
                     transOrderInfo.setProductIdd(paramMap.get("productid")[0]);
                     CommonResponse res = null;
                     try {
                         res = withhold40144(transOrderInfo);
                     } catch (AccountException e) {
                         logger.info(TransCodeConst.PAYMENT_40144+"  交易    code="+res.getCode()+",msg="+res.getMsg());
                         response.setCode("C1");
                         response.setMsg("系统异常!");
                         response.setIs_success(false);
                         return response;
                     }
                     if (!CodeEnum.SUCCESS.getCode().equals(res.getCode())) {
                         logger.info(TransCodeConst.PAYMENT_40144+" 交易    code="+res.getCode()+",msg="+res.getMsg());
                         response.setCode("C1");
                         response.setMsg(res.getMsg());
                         response.setIs_success(false);
                     }else{
                         response.setIs_success(true);
                     }
                     return response;
              } 	             
				if(transOrderInfo.getMerchantCode().equals(Constants.JRD_ID)){
					//判断账户提现金额逻辑是否正确
					if(transOrderInfo.getOrderAmount()!=(transOrderInfo.getAmount()+transOrderInfo.getUserFee())){
						response.setCode("C2");
						response.setMsg("请确认订单金额与入账金额和用户手续费是否输入正确！");
						return response;
					}
					String result=this.withholdJRD(transOrderInfo,orderAuxiliary);
					if(result.equals("ok")){
						okResponse.setIs_success(true);
						return okResponse;
					}else{
						response.setCode("C1");
						response.setMsg(result);
						response.setIs_success(false);
						return response;
					}
				}else{
					response=this.withhold(transOrderInfo, productId);
				}
			}
		}else if("ruixue.wheatfield.user.getadvancebalance".equals(methodName)) {//获取预付金余额
			PaymentBalanceinfoResponce payMentBalanceResponce=new PaymentBalanceinfoResponce();			
			payMentBalanceResponce.setAdvancebalances(this.getAdvanceBalance(user, queryType));
			if(payMentBalanceResponce.getAdvancebalances()==null){
				response.setCode("C2");
				response.setMsg("获取账户预付金失败,请确认账户信息是否填写正确");
			}else{
				return payMentBalanceResponce;
			}
		}else if("ruixue.wheatfield.user.frozon".equals(methodName)){//冻结账户余额
			//判断订单状态是否为失效状态 ，如为失效 则不记录分录信息
			if(TransCodeConst.TRANS_STATUS_INVALIDATION==transOrderInfo.getStatus()){
				response=this.saveTransorder(transOrderInfo, "冻结账户余额");
			}else{
				response=this.freezefund(transOrderInfo, orderAuxiliary);
			}
		}else if("ruixue.wheatfield.user.thaw".equals(methodName)){//解冻账户余额
			//判断订单状态是否为失效状态 ，如为失效 则不记录分录信息
			if(TransCodeConst.TRANS_STATUS_INVALIDATION==transOrderInfo.getStatus()){
				response=this.saveTransorder(transOrderInfo, "解冻账户余额");
			}else{
				response=this.thawfund(transOrderInfo, orderAuxiliary);
			}
		}else if("ruixue.wheatfield.user.orderrefund".equals(methodName)){//订单退款
			//判断订单状态是否为失效状态 ，如为失效 则不记录分录信息
			if(TransCodeConst.TRANS_STATUS_INVALIDATION==transOrderInfo.getStatus()){
				response=this.saveTransorder(transOrderInfo, "订单退款");
			}else{
				response=this.orderrefund(transOrderInfo, orderAuxiliary);
			}
		}else if("ruixue.wheatfield.user.frozon.authcode".equals(methodName)){//冻结账户余额返回授权码
			//判断订单状态是否为失效状态 ，如为失效 则不记录分录信息
			if(TransCodeConst.TRANS_STATUS_INVALIDATION==transOrderInfo.getStatus()){
				response=this.saveTransorder(transOrderInfo, "冻结账户余额");
			}else{
				response=this.freezefundauthcode(transOrderInfo, orderAuxiliary);
			}
		}else if("ruixue.wheatfield.user.thaw.authcode".equals(methodName)){//使用授权码解冻账户余额
			//判断订单状态是否为失效状态 ，如为失效 则不记录分录信息
			if(TransCodeConst.TRANS_STATUS_INVALIDATION==transOrderInfo.getStatus()){
				response=this.saveTransorder(transOrderInfo, "解冻账户余额");
			}else{
				response=this.thawfundauthcode(transOrderInfo, orderAuxiliary);
			}
		}else if("ruixue.wheatfield.user.transfer.new".equals(methodName)){
			//判断订单状态是否为失效状态 ，如为失效 则不记录分录信息
			if(TransCodeConst.TRANS_STATUS_INVALIDATION==transOrderInfo.getStatus()){
				response=this.saveTransorder(transOrderInfo, "转账新接口");
			}else{
				String result=this.transferNew(transOrderInfo, orderAuxiliary);
				if(result.equals("ok")){
					okResponse.setIs_success(true);
					return okResponse;
				}else{
					response.setCode("C1");
					response.setMsg(result);
					response.setIs_success(false);
					return response;
				}
			}
		}else if("ruixue.wheatfield.user.transfer.common".equals(methodName)){
			//转账通用
			if(!ValHasNoParam.hasParam(paramMap, "intoproductid")){
			    response.setCode("P1");
				response.setMsg("intoproductid不能为空");
				return response;
			}
			//判断订单状态是否为失效状态 ，如为失效 则不记录分录信息
			if(TransCodeConst.TRANS_STATUS_INVALIDATION==transOrderInfo.getStatus()){
				response = this.saveTransorder(transOrderInfo, "转账通用");
			}else{
				String result = this.transferInCommonUse(transOrderInfo, orderAuxiliary);
				if("ok".equals(result)){
					okResponse.setIs_success(true);
					return okResponse;
				}else{
					response.setCode("C1");
					response.setMsg(result);
					response.setIs_success(false);
					return response;
				}
			}
		}else if("ruixue.wheatfield.redenvelope.exchange".equals(methodName)){
			//红包兑换
			if(!ValHasNoParam.hasParam(paramMap, "intoproductid")){
			    response.setCode("P1");
				response.setMsg("intoproductid不能为空");
				return response;
			}
			//判断订单状态是否为失效状态 ，如为失效 则不记录分录信息
			if(TransCodeConst.TRANS_STATUS_INVALIDATION==transOrderInfo.getStatus()){
				response = this.saveTransorder(transOrderInfo, "红包兑换");
			}else{
				String result = this.redPackageExchange(transOrderInfo, orderAuxiliary);
				if("ok".equals(result)){
					okResponse.setIs_success(true);
					return okResponse;
				}else{
					response.setCode("C1");
					response.setMsg(result);
					response.setIs_success(false);
					return response;
				}
			}
		} else if ( "ruixue.wheatfield.demand.redemption".equals(methodName) ) {//君融贷活期产品赎回
			//判断订单状态是否为失效状态 ，如为失效 则不记录分录信息
			if ( TransCodeConst.TRANS_STATUS_INVALIDATION == transOrderInfo.getStatus() ) {
				response = this.saveTransorder(transOrderInfo, "君融贷活期产品赎回");
			} else {
				String result = this.redemption(transOrderInfo, orderAuxiliary);
				if ( result.equals("ok") ) {
					okResponse.setIs_success(true);
					return okResponse;
				} else {
					response.setCode("C1");
					response.setMsg(result);
					response.setIs_success(false);
					return response;
				}
			}
		}
//		else if("ruixue.wheatfield.user.wipeaccount".equals(methodName)) {//抹帐
//			response=paymentInternalService.wipeAccount(transOrderInfo);
//		}
		else{
			return errorResponseService.getErrorResponse("S0");
		}
		return response;
	}
	
	/**
	 * 40142 交易   用户交易   MICE主账户减
	 * Discription:
	 * @param transOrderInfo
	 * @return CommonResponse
	 * @author Achilles
	 * @since 2016年4月28日
	 */
     @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
     public CommonResponse withhold40142(TransOrderInfo transOrderInfo){
         logger.info("40142 交易  传入参数:"+BeanUtil.getBeanVal(transOrderInfo, null)); 
         synchronized (lock) {
            CommonResponse res = new CommonResponse();
            res.setCode(CodeEnum.FAILURE.getCode());
            //校验订单号是否存在
            if (!this.orderNoChk(transOrderInfo.getOrderNo(), transOrderInfo.getMerchantCode())) {
                res.setMsg("订单号：" + transOrderInfo.getOrderNo() + "该交易订单号已存在，请确认！");
                return res;
            }
            User user = new User();
            user.userId = transOrderInfo.getUserId();
            user.constId = transOrderInfo.getMerchantCode();
            user.productId = Constants.HT_PRODUCT;
            //判断账户状态是否正常
            boolean accountIsOK = operationService.checkAccount(user);
            if (!accountIsOK) {
                res.setMsg("用户" + transOrderInfo.getUserId() + "状态为非正常状态");
                return res;
            }
            OrderAuxiliary orderAuxiliary = new OrderAuxiliary();
            orderAuxiliary.setProductQAA(transOrderInfo.getProductIdd());
            //判断订单信息是否有误
            transOrderInfo.setFuncCode("4014");//只为用代付生成流水,下面会恢复原值
            transOrderInfo.setErrorCode(Constants.HT_PRODUCT);
            String msg = checkInfoService.checkTradeInfo(transOrderInfo);
            if (!"ok".equals(msg)) {
                res.setMsg(msg);
                return res;
            }
            Balance balance = checkInfoService.getBalance(user, "");
            if (balance == null) {
                res.setMsg("获取账户余额失败  userid=" + transOrderInfo.getUserId());
                return res;
            }
            //MICE主账户－
            balance.setPulseDegree(balance.getPulseDegree() + 1);
            String entryId = redisIdGenerator.createRequestNo();
            List<FinanaceEntry> finanaceEntries =
                    checkInfoService.getFinanaceEntries(transOrderInfo, balance, entryId, false);
            if (finanaceEntries == null || finanaceEntries.size() == 0) {
                res.setMsg("生成流水失败");
                return res;
            }
            FinanaceEntry finanaceEntry = finanaceEntries.get(0);
            finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
            finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
            finanaceEntry.setRemark("40142");
            List<FinanaceEntry> finanaceEntriesAll = new ArrayList<FinanaceEntry>();
            finanaceEntriesAll.add(finanaceEntry);
            //平安应收账款＋
            //         balance=checkInfoService.getBalance(user,"141223100000059");
            //         if (balance==null) {
            //             res.setMsg("获取账户余额失败  userid=141223100000059");
            //             return res;
            //         }
            //         balance.setPulseDegree(balance.getPulseDegree()+1);
            //         transOrderInfo.setFuncCode("4015");//只为用充值生成流水,下面会恢复原值
            //         finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, balance, entryId, true);
            //         if (finanaceEntries==null||finanaceEntries.size()==0) {
            //             res.setMsg("生成流水失败");
            //             return res;
            //         }
            //         finanaceEntry = finanaceEntries.get(0);
            //         finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
            //         finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
            //         finanaceEntry.setRemark("40142");
            //         finanaceEntriesAll.add(finanaceEntry);
            transOrderInfo.setFuncCode("40142");//回复原值
            insertFinanaceEntry(finanaceEntriesAll, transOrderInfo, null);
            return new CommonResponse();
        }
     }
	
     /**
      * 40143交易
      * Discription:
      * @param transOrderInfo
      * @return CommonResponse
      * @author Achilles
      * @since 2016年7月27日
      */
     @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
     public CommonResponse withhold40143(TransOrderInfo transOrderInfo){
         logger.info("40143 交易  传入参数:"+BeanUtil.getBeanVal(transOrderInfo, null)); 
         synchronized (lock) {
            CommonResponse res = new CommonResponse();
            res.setCode(CodeEnum.FAILURE.getCode());
            //校验订单号是否存在
            if (!this.orderNoChk(transOrderInfo.getOrderNo(), transOrderInfo.getMerchantCode())) {
                res.setMsg("订单号：" + transOrderInfo.getOrderNo() + "该交易订单号已存在，请确认！");
                return res;
            }
            User user = new User();
            user.userId = transOrderInfo.getUserId();
            user.constId = transOrderInfo.getMerchantCode();
            user.productId = transOrderInfo.getProductIdd();
            //判断账户状态是否正常
            boolean accountIsOK = operationService.checkAccount(user);
            if (!accountIsOK) {
                res.setMsg("用户" + transOrderInfo.getUserId() + "状态为非正常状态");
                return res;
            }
            transOrderInfo.setAccountDate(DateUtils.getDate(generationPaymentService.getAccountDate(), Constants.DATE_FORMAT_YYYYMMDD));
            transOrderInfoDao.insertSelective(transOrderInfo);
            return new CommonResponse();
        }
     }
     
	/**
	 * 付款(转账)MICE平安付款子账户-
	 * Discription:
	 * @param transOrderInfo
	 * @return CommonResponse
	 * @author Achilles
	 * @since 2016年4月26日
	 */
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public CommonResponse withhold40141(TransOrderInfo transOrderInfo){
	    logger.info("40141交易 传入参数:"+BeanUtil.getBeanVal(transOrderInfo, null));
	    synchronized (lock) {
            CommonResponse res = new CommonResponse();
            res.setCode(CodeEnum.FAILURE.getCode());
            //校验订单号是否存在
            if (!this.orderNoChk(transOrderInfo.getOrderNo(), transOrderInfo.getMerchantCode())) {
                res.setMsg("订单号：" + transOrderInfo.getOrderNo() + "该交易订单号已存在，请确认！");
                return res;
            }
            User user = new User();
            user.userId = transOrderInfo.getUserId();
            user.constId = transOrderInfo.getMerchantCode();
            user.productId = Constants.HT_PINGAN_PAY_CHILD;
            //判断账户状态是否正常
            boolean accountIsOK = operationService.checkAccount(user);
            if (!accountIsOK) {
                res.setMsg("用户" + transOrderInfo.getUserId() + "状态为非正常状态");
                return res;
            }
            OrderAuxiliary orderAuxiliary = new OrderAuxiliary();
            orderAuxiliary.setProductQAA(transOrderInfo.getProductIdd());
            //判断订单信息是否有误
            transOrderInfo.setErrorCode(Constants.HT_PINGAN_PAY_CHILD);
            transOrderInfo.setFuncCode("4014");//只为用代付生成流水,下面会恢复原值
            String msg = checkInfoService.checkTradeInfo(transOrderInfo);
            if (!"ok".equals(msg)) {
                res.setMsg(msg);
                return res;
            }
            //获取账户余额
            user.userId = transOrderInfo.getUserId();
            Balance balance = checkInfoService.getBalance(user, "");
            if (balance == null) {
                res.setMsg("获取账户余额失败!");
                return res;
            }
            balance.setPulseDegree(balance.getPulseDegree() + 1);
            String entryId = redisIdGenerator.createRequestNo();
            List<FinanaceEntry> finanaceEntries =
                    checkInfoService.getFinanaceEntries(transOrderInfo, balance, entryId, false);
            if (finanaceEntries.size() == 0) {
                res.setMsg("生成流水失败!");
                return res;
            }
            transOrderInfo.setFuncCode("40141");//回复原值
            transOrderInfo.setAccountDate(
                    DateUtils.getDate(operationService.getAccountDate(), Constants.DATE_FORMAT_YYYYMMDD));
            transOrderInfoManager.saveTransOrderInfo(transOrderInfo);
            FinanaceEntry finanaceEntry = finanaceEntries.get(0);
            finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
            finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
            finanaceEntry.setRemark("40141");
            //生成流水
            finanaceEntry.setAccountDate(transOrderInfo.getAccountDate());
            finanaceEntryDao.insertSelective(finanaceEntry);
            //调用多渠道发送转账交易 ,如果成功记录流水,如果失败,修改订单状态为失效
            res = toGaterouterTransfer(transOrderInfo);
            if (!CodeEnum.SUCCESS.getCode().equals(res.getCode())) {//如果失败,修改订单状态
                TransOrderInfo transOrderInfoNew = new TransOrderInfo();
                transOrderInfoNew.setRequestId(transOrderInfo.getRequestId());
                transOrderInfoNew.setRemark(res.getMsg());
                transOrderInfoNew.setStatus(5);
                transOrderInfoDao.updateByPrimaryKeySelective(transOrderInfoNew);
                return res;
            }
            return res;
        }
	}

	/**
	 * 调用多渠道发送转账交易 ,如果成功记录流水
	 * Discription:
	 * @param transOrderInfo
	 * @param finanaceEntries
	 * @return CommonResponse
	 * @author Achilles
	 * @since 2016年4月29日
	 */
    private CommonResponse toGaterouterTransfer(TransOrderInfo transOrderInfo) {
        CommonResponse res = new CommonResponse();
        res.setCode(CodeEnum.FAILURE.getCode());
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setSysNo("zhxt001");//业务系统号
        paymentDto.setTransCode("16011");//子账户操作(增删改)
        PingAnParam pingAnParam = parameterInfoService.getPingAnOutTransferParam();
        if (pingAnParam==null) {
            res.setMsg("参数表设置异常!");
            return res;
        }
        paymentDto.setOrgNo(pingAnParam.getDealInst());
//        paymentDto.setOrgNo(transOrderInfo.getMerchantCode());
        paymentDto.setBusiCode("16011");//业务编码 
        paymentDto.setChannelNo("160601");//渠道号
        paymentDto.setSignType(1);//固定值1 即MD5
        paymentDto.setSignMsg(paymentDto.sign(pingAnParam.getMd5key()));
        
        paymentDto.setTransNo(transOrderInfo.getRequestId().toString());//流水号
        paymentDto.setPayAmount(transOrderInfo.getOrderAmount());//付款金额
        paymentDto.setCurrency("CNY");
        //获取转出方用户卡信息
        List<FinanaceAccount> finList = getOutFinAccList(transOrderInfo);
        if (finList.size()==0) {
            res.setMsg("无法获取用户"+transOrderInfo.getUserId()+"的账户信息");
            return res;
        }
        paymentDto.setPayerAccountNo(finList.get(0).getReferUserId());//付款人账号
        paymentDto.setPayerAccountName(finList.get(0).getRecordMap());//付款人姓名
        
      //获取转入方账户信息
        List<FinanaceAccount> finAccList = getIntoUserFinAcc(transOrderInfo);
        if (finAccList.size()==0) {
            res.setMsg("无法获取用户"+transOrderInfo.getInterMerchantCode()+"的账户信息");
            return res;
        }
      //获取转入方用户卡信息
        AccountInfo accountInfo = getIntoUserAccList(finAccList.get(0).getFinAccountId());
        if (accountInfo==null) {
          res.setMsg("无法获取用户"+transOrderInfo.getInterMerchantCode()+"的卡信息");
          return res;
        }
        paymentDto.setReceiverAccountNo(accountInfo.getAccountNumber());//收款账号
        paymentDto.setReceiverAccountName(accountInfo.getAccountRealName());//收款户名
        paymentDto.setReceiverAccountType(10);
        if ("1".equals(accountInfo.getAccountProperty())) {
            paymentDto.setReceiverAccountType(20);
        }
        ParameterInfo parameterInfo = parameterInfoService.getParaInfoByProductId("PINGAN_BANK_INFO");
        if (parameterInfo==null) {
            res.setMsg("系统异常,无法获取平安银行信息!");
            return res; 
        }
        paymentDto.setBankFlag(1);//行内标识 1行内，2行外
        if (!parameterInfo.getParameterCode().equals(accountInfo.getBankHead())) {
            paymentDto.setBankFlag(2);//行内标识 1行内，2行外
//            if (accountInfo.getBankBranch()==null||"".equals(accountInfo.getBankBranch())) {
//                res.setMsg("用户的绑卡信息中没有支行号!");
//                return res; 
//            }
            paymentDto.setReceiverBankNo(accountInfo.getBankBranch());
//            if (accountInfo.getBankBranch()==null||"".equals(accountInfo.getBankBranch())) {
//                paymentDto.setReceiverBankNo(accountInfo.getBankHead());
//            }
            paymentDto.setReceiverProvince(accountInfo.getBankProvince());//收款省份
            paymentDto.setReceiverCity(accountInfo.getBankCity());//收款城市
        }
        paymentDto.setReceiverBankName(accountInfo.getBankHeadName());//收款行名

        String remark = transOrderInfo.getRemark();
        paymentDto.setPurpose(remark); //资金用途
        if (remark==null||"".equals(remark)) {
            paymentDto.setPurpose("付款");  
        }
        paymentDto.setPurpose("ZZZZZ"+paymentDto.getPurpose());
        paymentDto.setSettleFlag(1);//结算标识   1普通，2实时，3 特急
        PaymentRespDto resDto = null;
        logger.info("调用多渠道转账传入参数: SysNo="+paymentDto.getSysNo()+",TransCode="+paymentDto.getTransCode()
        +",OrgNo="+paymentDto.getOrgNo()+",BusiCode="+paymentDto.getBusiCode()+",ChannelNo="+paymentDto.getChannelNo()
                + ",SignType="+paymentDto.getSignType()+",SignMsg="+paymentDto.getSignMsg()
                +",TransNo="+paymentDto.getTransNo()+",PayAmount="+paymentDto.getPayAmount()
                +",Currency="+paymentDto.getCurrency()+",PayerAccountNo="+paymentDto.getPayerAccountNo()
                +",PayerAccountName="+paymentDto.getPayerAccountName()+",ReceiverBankNo="+paymentDto.getReceiverBankNo()
                +",ReceiverProvince="+paymentDto.getReceiverProvince()+",ReceiverCity="+paymentDto.getReceiverCity()
                +",ReceiverAccountNo="+paymentDto.getReceiverAccountNo()+",ReceiverAccountName="+paymentDto.getReceiverAccountName()
                +",ReceiverBankName="+paymentDto.getReceiverBankName()+",Purpose="+paymentDto.getPurpose()
                +",BankFlag="+paymentDto.getBankFlag()+",SettleFlag="+paymentDto.getSettleFlag()+",ReceiverAccountType="+paymentDto.getReceiverAccountType());
      //调用多渠道
        try {
            resDto = bankPaymentService.payment(paymentDto);
        } catch (Exception e) {
            logger.error("调用多渠道转账接口异常!",e);
            res.setMsg("系统异常(多渠道)!");
            return res;
        }
        logger.info("调用多渠道单笔转账   returnCode="+resDto.getReturnCode()+",returnMsg="+resDto.getReturnMsg());
        if (!"100000".equals(resDto.getReturnCode())) {
            res.setMsg("多渠道返回:"+resDto.getReturnMsg());
            return res;
        }
        logger.info("调用多渠道转账返回的状态值="+resDto.getStatusId());
        if (resDto.getStatusId()!=10 && resDto.getStatusId()!=11 && resDto.getStatusId()!=12) {//非成功
            res.setMsg("调用多渠道转账失败");
            return res;
        }
        return new CommonResponse();
    }

    private List<FinanaceAccount> getIntoUserFinAcc(TransOrderInfo transOrderInfo) {
        com.rkylin.wheatfield.bean.User userr = new com.rkylin.wheatfield.bean.User();
        userr.setUserId(transOrderInfo.getInterMerchantCode());
        userr.setInstCode(transOrderInfo.getMerchantCode());
        userr.setType(new String[]{"10001"});
        List<FinanaceAccount> finAccList = finanaceAccountDao.queryByInstCodeAndUser(userr);
        return finAccList;
    }

    private AccountInfo getIntoUserAccList(String finAccountId) {
        AccountInfoQuery accInfoQuery = new AccountInfoQuery();
        accInfoQuery.setFinAccountId(finAccountId);
        accInfoQuery.setStatus(1);
        accInfoQuery.setAccountPurpose(Constants.ACCOUNT_PURPOSE_1);//查出的是1,3,4的卡
        List<AccountInfo> accList = accountInfoDao.selectByExample(accInfoQuery);
        AccountInfo accInfo = null;
        if (accList.size()!=0) {
            accInfo=accList.get(0);
        }
//        AccountInfo accInfo = null;
//        for (AccountInfo accountInfo : accList) {
//            if (Constants.ACCOUNT_PURPOSE_3.equals(accountInfo.getAccountPurpose())) {
//                accInfo = accountInfo;
//                return accountInfo;
//            }
//        }
//        if (accInfo==null) {
//            for (AccountInfo accountInfo : accList) {
//                if (Constants.ACCOUNT_PURPOSE_1.equals(accountInfo.getAccountPurpose())||
//                        Constants.ACCOUNT_PURPOSE_4.equals(accountInfo.getAccountPurpose())) {
//                    accInfo = accountInfo;
//                }
//            }   
//        }
        return accInfo;
    }

    private List<FinanaceAccount> getOutFinAccList(TransOrderInfo transOrderInfo) {
        FinanaceAccountQuery finAccQuery = new FinanaceAccountQuery();
        finAccQuery.setRootInstCd(transOrderInfo.getMerchantCode());
        finAccQuery.setAccountRelateId(transOrderInfo.getUserId());
        finAccQuery.setGroupManage(Constants.HT_PINGAN_PAY_CHILD);
        finAccQuery.setStatusId("1");
        List<FinanaceAccount> finList = finanaceAccountDao.selectByExample(finAccQuery);
        return finList;
    }
	 
    /**
     * 删除订单及流水
     * Discription:
     * @param requestId void
     * @author Achilles
     * @since 2016年4月27日
     */
//    private void deleteOrderAndEntryByRequestId(java.lang.Integer requestId){
//        transOrderInfoManager.deleteTransOrderInfoById(requestId);
//        FinanaceEntryQuery query = new FinanaceEntryQuery();
//        query.setReferId(requestId+"");
//        finanaceEntryDao.deleteByExample(query);
//    }
    
	/**
	 * @Description : TODO(转账通用)
	 * @CreateTime : 2015年10月27日 上午9:30:20
	 * @Creator : liuhuan
	 */
	//@Override
	public String transferInCommonUse(TransOrderInfo transOrderInfo,OrderAuxiliary orderAuxiliary) {
		synchronized (lock) {
			String isOk = "ok";
			String[] productIdStrings = {orderAuxiliary.getProductQAA(),orderAuxiliary.getProductQAB()};
			logger.info("-------转账通用接口--------账户UserId：" + transOrderInfo.getUserId() + "向账户" + transOrderInfo.getInterMerchantCode() + "转入" + transOrderInfo.getAmount() + "分，转账操作开始------------------------");
			logger.info("转账操作参数信息：amount = " + transOrderInfo.getAmount() + ",UserId = " + transOrderInfo.getUserId() + ",funccode = " + transOrderInfo.getFuncCode()
					+ ",intermerchantcode = " + transOrderInfo.getInterMerchantCode() + ",merchantcode = " + transOrderInfo.getMerchantCode() + ",orderamount = " + transOrderInfo.getOrderAmount()
					+ ",ordercount = " + transOrderInfo.getOrderCount() + ",orderdate = " + transOrderInfo.getOrderDate() + ",orderno = " + transOrderInfo.getOrderNo()
					+ ",orderpackageno="+transOrderInfo.getOrderPackageNo()+",paychannelid="+transOrderInfo.getPayChannelId()+",remark="+transOrderInfo.getRemark()
					+ ",productid = " + orderAuxiliary.getProductQAA() + ",requestno = " + transOrderInfo.getRequestNo() + ",requesttime = " + transOrderInfo.getRequestTime() + ",status = " + transOrderInfo.getStatus()
					+ ",tradeflowno = " + transOrderInfo.getTradeFlowNo() + ",userfee = " + transOrderInfo.getUserFee() + ",feeamount = " + transOrderInfo.getFeeAmount()
					+ ",profit = " + transOrderInfo.getProfit() + ",busitypeid = " + transOrderInfo.getBusiTypeId() + ",bankcode = " + transOrderInfo.getBankCode() + ",errorcode = " + transOrderInfo.getErrorCode()
					+ ",errormsg = " + transOrderInfo.getErrorMsg() + ",useripaddress = " + transOrderInfo.getUserIpAddress() + ",intoproductid = " + orderAuxiliary.getProductQAB());
			//校验交易码
			if (!TransCodeConst.ADJUST_ACCOUNT_AMOUNT.equals(transOrderInfo.getFuncCode())&&
			        !TransCodeConst.FUNCTION_TRANSFER.equals(transOrderInfo.getFuncCode())){
				logger.info("转账交易码错误！订单中的交易码为" + transOrderInfo.getFuncCode());
				return "转账交易码错误！";
			}
			//校验订单号是否存在
			if(!this.orderNoChk(transOrderInfo.getOrderNo(),transOrderInfo.getMerchantCode())){
				logger.info("订单号：" + transOrderInfo.getOrderNo() + "在机构号：" + transOrderInfo.getMerchantCode() + "中已存在");
				return "订单号：" + transOrderInfo.getOrderNo() + "该交易订单号已存在，请确认！";
			}

			User user = new User();
			user.userId = transOrderInfo.getUserId();
			user.constId = transOrderInfo.getMerchantCode();
			user.productId = orderAuxiliary.getProductQAA();
			//判断账户状态是否正常
			boolean accountIsOK = operationService.checkAccount(user);
			if(!accountIsOK){
				logger.error("用户" + transOrderInfo.getUserId() + "状态为非正常状态");
				return "用户" + transOrderInfo.getUserId() + "状态为非正常状态";
			}
			//判断订单信息是否有误
			String msg = checkInfoService.checkTradeInfo(transOrderInfo,orderAuxiliary);
			if(!"ok".equals(msg)){
				logger.error(msg);
				return msg;
			}

			//获取每个账户记账流水
			List<FinanaceEntry> finanaceEntries = new ArrayList<FinanaceEntry>();
			//获取所有账户记账流水
			List<FinanaceEntry> finanaceEntrieAlls = new ArrayList<FinanaceEntry>();
			try {
				//获取套录号
				String entryId = redisIdGenerator.createRequestNo();
				boolean flag = false;
				//订单原始金额
				Long oAmount = transOrderInfo.getAmount();
				//获取转入方的余额信息
				User userB = new User();
				userB.userId = transOrderInfo.getInterMerchantCode();
				userB.constId = transOrderInfo.getMerchantCode();
				userB.productId = orderAuxiliary.getProductQAB();
				
				//判断转入方账户是否正常，且与转出方是否属于同一机构
				boolean accountBIsOk = operationService.checkAccount(userB);
				if(!accountBIsOk){
					logger.error("转入方用户 " + transOrderInfo.getInterMerchantCode() + " 状态为非正常状态或者与转出方不在同一机构，请查证!");
					return "转入方账户状态非正常状态或者与转出方不在同一机构，请查证!";
				}
				
				Balance balanceB = checkInfoService.getBalance(userB,"");
				if(null == balanceB){
					logger.error("获取转入方余额信息失败！");
					return "获取转入方余额信息失败！";
				}
				
				//常规转账交易
				user.productId = orderAuxiliary.getProductQAA();
				Balance balance = checkInfoService.getBalance(user,"");
				if(null != balance){
					balance.setPulseDegree(balance.getPulseDegree() + 1);
					balanceB.setPulseDegree(balanceB.getPulseDegree() + 1);
					finanaceEntries = checkInfoService.getFinanaceEntries(transOrderInfo, balance,balanceB, entryId, flag);
					if(null != finanaceEntries && !finanaceEntries.isEmpty()){
						for(FinanaceEntry finanaceEntry : finanaceEntries) {
							finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);	
							finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
							if(null == finanaceEntry.getRemark() || "".equals(finanaceEntry.getRemark())){
								finanaceEntry.setRemark("转账通用");
							}
							if(finanaceEntry.getPaymentAmount() > 0){
								finanaceEntrieAlls.add(finanaceEntry);
							}
						}
					}
				}else{
					logger.error("获取账户余额信息失败！userId : " + user.userId + ";产品号 ：" + user.productId);
					return "获取账户余额信息失败！";
				}
				transOrderInfo.setErrorCode(orderAuxiliary.getProductQAA());
				transOrderInfo.setErrorMsg(orderAuxiliary.getProductQAB());
				//批量插入记录流水表   写入数据失败尝试三次
				if("ok".equals(isOk)){
					transOrderInfo.setAmount(oAmount);
					try {
						insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo, productIdStrings);
					} catch (AccountException e) {
						logger.error("******************账户"+transOrderInfo.getUserId() + "记账流水数据入库失败一次");
						try {
							insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo, productIdStrings);
						} catch (AccountException e1) {
							logger.error("******************账户" + transOrderInfo.getUserId() + "记账流水数据入库失败两次");
							try {
								insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo, productIdStrings);
							} catch (AccountException e2) {
								//发送邮件或短信通知管理员
								logger.error("******************账户" + transOrderInfo.getUserId() + "记账流水数据入库失败三次");
								//启用线程 发送邮件
								RkylinMailUtil.sendMailThread("账户记账流水失败通知","******************账户" + transOrderInfo.getUserId() + "做转账记账流水数据入库连续失败三次，转入方" + transOrderInfo.getInterMerchantCode(), TransCodeConst.FINANACE_ENTRY_ERROR_TOEMAIL);
								logger.error(e2.getMessage());
								isOk = "数据库操作异常";
							}
						}
					}
				}
				
			} catch (AccountException e) {
				logger.error(e.getMessage());
				isOk = "数据库操作异常";
			}
			logger.info("-------转账通用接口-------账户UserId" + transOrderInfo.getUserId() + "向账户" + transOrderInfo.getInterMerchantCode() + "转入" + transOrderInfo.getAmount() + "分，转账操作结束------------------------");
			return isOk;
		}
	}
	/**
	 * @Description : TODO(红包兑换)
	 * @CreateTime : 2015年10月27日 下午4:23:41
	 * @Creator : liuhuan
	 */
	public String redPackageExchange(TransOrderInfo transOrderInfo,OrderAuxiliary orderAuxiliary){
		logger.info("-------红包兑换接口开始--------");
		
		User user = new User();
		user.userId = transOrderInfo.getInterMerchantCode();
		user.constId = transOrderInfo.getMerchantCode();
		user.productId = orderAuxiliary.getProductQAB();
		user.referUserId = "0";
		
		//判断转入方账户是否存在
		FinanaceAccountQuery query=new FinanaceAccountQuery();
		query.setRootInstCd(transOrderInfo.getMerchantCode());
		query.setAccountRelateId(transOrderInfo.getInterMerchantCode());
		query.setGroupManage(orderAuxiliary.getProductQAB());
		
		List<FinanaceAccount> faChildList = finanaceAccountManager.queryList(query);
		if(faChildList == null || faChildList.isEmpty()){
			//转入方账户不存在则查找主账户是否存在
			query=new FinanaceAccountQuery();
			query.setRootInstCd(transOrderInfo.getMerchantCode());
			query.setAccountRelateId(transOrderInfo.getInterMerchantCode());
			query.setFinAccountTypeId(AccountConstants.ACCOUNT_TYPE_BASE);
			List<FinanaceAccount> faList = finanaceAccountManager.queryList(query);
			if(faList == null || faList.isEmpty()){
				logger.error("转入方用户" + transOrderInfo.getInterMerchantCode() + "主账户不存在，请先创建主账户");
				return "转入方用户" + transOrderInfo.getInterMerchantCode() + "主账户不存在，请先创建主账户";	
			}else{
				FinanaceAccount finanaceAccount = faList.get(0);
				if(!BaseConstants.ACCOUNT_STATUS_OK.equals(finanaceAccount.getStatusId())){
					logger.error("转入方用户" + transOrderInfo.getInterMerchantCode() + "主账户为失效状态");
					return "转入方用户" + transOrderInfo.getInterMerchantCode() + "主账户为失效状态";
				}
				user.userName = finanaceAccount.getFinAccountName();
				accountManageService.openAccount(user, new FinanaceEntry());
			}
		}else{
			FinanaceAccount financeAccount = faChildList.get(0);
			if(!BaseConstants.ACCOUNT_STATUS_OK.equals(financeAccount.getStatusId())){
				logger.error("转入方用户" + transOrderInfo.getInterMerchantCode() + "账户为失效状态");
				return "转入方用户" + transOrderInfo.getInterMerchantCode() + "账户为失效状态";
			}
		}
		
		String result = this.transferInCommonUse(transOrderInfo, orderAuxiliary);
		
		logger.info("-------红包兑换接口结束--------调用转账接口返回结果：result : " + result);
		return result;
	}

	@Override	
//	@Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
	public void insertFinanaceEntry(List<FinanaceEntry> finanaceEntries,TransOrderInfo transOrderInfo,String[] productIdStrings){
		try {
			if(null!=finanaceEntries&&0<finanaceEntries.size()){
				transOrderInfo.setAccountDate(finanaceEntries.get(0).getAccountDate());
				//判断是否是冲正操作，是 就更新原订单的订单状态
				if(TransCodeConst.MERCHANT_RT.equals(transOrderInfo.getFuncCode())||TransCodeConst.SETTLEMENT_RT.equals(transOrderInfo.getFuncCode())||TransCodeConst.MANUAL_RT.equals(transOrderInfo.getFuncCode())||TransCodeConst.CONSUMPTION_BEFER_REFUND.equals(transOrderInfo.getFuncCode())){
					//如果是冲正交易，需要将原有交易订单状态置为冲正

			        TransOrderInfo orderInfo2 = new TransOrderInfo();
			        orderInfo2.setStatus(Integer.valueOf(6));
			        orderInfo2.setRequestId(transOrderInfo.getRequestId());
	
			        this.transOrderInfoManager.saveTransOrderInfo(orderInfo2);
			        transOrderInfo.setRequestId(null);
			}else if(transOrderInfo.getFuncCode().equals(TransCodeConst.AFTER_SPENDING_REFUND)){
					//消费后退款  将订单所涉及到的账户退款挂起
					List<SettleTransTab> settleTransTabs=operationService.getSettleTransTabs(transOrderInfo);
					if(settleTransTabs.size()>0){
						settleTransTabManager.saveSettleTransTabs(settleTransTabs);
					}
			}
			//判断交易订单是否为代付，根据机构号判断及时间区间
			if(this.isWithholdInterval(new Date())&&TransCodeConst.PAYMENT_WITHHOLD.equals(transOrderInfo.getFuncCode())&&Constants.HT_ID.equals(transOrderInfo.getMerchantCode().substring(0, 7))){
				//获取君融贷TO会唐预付金账户的金额
				FinanaceAccountQuery query =new FinanaceAccountQuery();
				query.setFinAccountId(TransCodeConst.THIRDPARTYID_HTYFJZH);
				List<FinanaceAccount> finanaceAccounts=finanaceAccountManager.queryList(query);
				if(null!=finanaceAccounts&&finanaceAccounts.size()>0){
					long amount=finanaceAccounts.get(0).getAmount();//账户金额信息
					if(amount>=transOrderInfo.getAmount()){//预付金账户金额大于等于代付 金额
						logger.info("更新君融贷与会唐预付金账户金额：");
						logger.info("原有金额："+amount);
						FinanaceAccount finanaceAccount=finanaceAccounts.get(0);
						finanaceAccount.setAmount(amount-transOrderInfo.getAmount());
						finanaceAccount.setBalanceUsable(amount-transOrderInfo.getAmount());
						finanaceAccount.setBalanceSettle(amount-transOrderInfo.getAmount());
						finanaceAccountManager.updateFinanaceAccount(finanaceAccount);
						transOrderInfo.setErrorMsg(TransCodeConst.T0_FLAG);
					}else{
						logger.info("获取君融贷与会唐预付金账户中的金额为："+amount+";小于订单代付的"+transOrderInfo.getAmount()+"金额,不能做T+0");
					}
				}
			}else if(this.isWithholdInterval(new Date())&&TransCodeConst.WITHDROW.equals(transOrderInfo.getFuncCode())&&Constants.FN_ID.equals(transOrderInfo.getMerchantCode())){
				transOrderInfo.setErrorMsg(TransCodeConst.T0_FLAG);
			}else if(isCreditWithdrawT0() &&TransCodeConst.WITHDROW.equals(transOrderInfo.getFuncCode())&&Constants.FACTORING_ID.equals(transOrderInfo.getMerchantCode())){
			    transOrderInfo.setErrorMsg(TransCodeConst.T0_FLAG);
			}
			transOrderInfoManager.saveTransOrderInfo(transOrderInfo);
			//transSettlementService.transToSettlement(transOrderInfo,productIdStrings);
			List<FinanaceEntry> finaceEntryList=new ArrayList<FinanaceEntry>();
			for (FinanaceEntry finanaceEntry : finanaceEntries) {
				finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
				finaceEntryList.add(finanaceEntry);
			}
			finanaceEntryManager.saveFinanaceEntryList(finaceEntryList);
			}
		} catch (AccountException e) {
			throw new AccountException("ERROR");
		}
	}	
	@Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
	public void insertFinanaceEntry(List<FinanaceEntry> finanaceEntries,TransOrderInfo transOrderInfo,FinanaceAccountAuth finanaceAccountAuth,String[] productIdStrings){
		try {
			if(null!=finanaceEntries&&0<finanaceEntries.size()){
				transOrderInfo.setAccountDate(finanaceEntries.get(0).getAccountDate());
				transOrderInfoManager.saveTransOrderInfo(transOrderInfo);
				//transSettlementService.transToSettlement(transOrderInfo,productIdStrings);
				finanaceAccountAuthManager.saveFinanaceAccountAuth(finanaceAccountAuth);
				List<FinanaceEntry> finaceEntryList=new ArrayList<FinanaceEntry>();
				for (FinanaceEntry finanaceEntry : finanaceEntries) {
					finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
					finaceEntryList.add(finanaceEntry);
				}
				finanaceEntryManager.saveFinanaceEntryList(finaceEntryList);
			}
		} catch (AccountException e) {
			throw new AccountException("ERROR");
		}
	}
	/**
	 * 判断订单状态是否为失效状态 ，如为失效 则只保存交易订单信息
	 * @param transOrderInfo
	 * @param logMsg
	 * @return
	 */
	private ErrorResponse saveTransorder(TransOrderInfo transOrderInfo,String logMsg){
		ErrorResponse response=new ErrorResponse();
		logger.info("------账户 userId="+transOrderInfo.getUserId()+"调用"+logMsg+"接口，订单号:"+transOrderInfo.getOrderNo()+"，状态为失效状态，不记录分录信息");
		try {
			transOrderInfoManager.saveTransOrderInfo(transOrderInfo);
			response.setIs_success(true);
		} catch (AccountException e) {
			logger.error(e.getMessage());
			response.setCode("C1");
			response.setMsg("订单："+transOrderInfo.getOrderNo()+"保存失败！");
		}
		return response;
	}
	
	/**
	 * 白条提现是否是T0
	 * Discription:
	 * @return boolean
	 * @author Achilles
	 * @since 2016年7月18日
	 */
    private boolean isCreditWithdrawT0(){
        boolean isOK=false;
        try {
            //获取当前账期
            Date tDate=dateUtil.parse(operationService.getAccountDate(), Constants.DATE_FORMAT_YYYYMMDD);
            //获取T-1日账期
            Calendar cal = Calendar.getInstance();
            cal.setTime(tDate);
            cal.add(Calendar.DATE, -1);
            Date tDateLast=cal.getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_YYYYMMDD);
            
            //设定T-1日的时分秒
            tDateLast=dateUtil.parse(dateFormat.format(tDateLast)+" 23:00:00", Constants.DATE_FORMAT_YYYYMMDDHHMMSS);
            //设定T日的时分秒
            tDate=dateUtil.parse(dateFormat.format(tDate)+" 16:00:00", Constants.DATE_FORMAT_YYYYMMDDHHMMSS);
            Date date = new Date();
            if(date.getTime()>tDateLast.getTime()&&date.getTime()<tDate.getTime()){
                isOK=true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isOK;
    }
	
	/**
	 * 判断代付T+0的时间条件
	 * @param date
	 * @return
	 */
	private boolean isWithholdInterval(Date date){
		boolean isOK=false;
		try {
			//获取当前账期
			Date tDate=dateUtil.parse(operationService.getAccountDate(), Constants.DATE_FORMAT_YYYYMMDD);
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
			e.printStackTrace();
		}
		return isOK;
	}
	
	/** 
	 * 君融贷提现记账共通方法
	 * @param transOrderInfo 交易流水信息
	 * @param entryId 套录号
	 *  */
	List<FinanaceEntry> JRDwithdrow(TransOrderInfo transOrderInfo ,User user, String entryId ,Balance balance){
		logger.info("JRDwithdrow start==================>>");
		//获取每个账户记账流水
		List<FinanaceEntry> finanaceEntries=new ArrayList<FinanaceEntry>();
		//获取所有账户记账流水
		List<FinanaceEntry> finanaceEntrieAlls=new ArrayList<FinanaceEntry>();
		
		long amount = transOrderInfo.getAmount();
		long userfee = transOrderInfo.getUserFee();
		logger.info("提现金额:"+amount+"||手续费:"+userfee);
		boolean flag=true;
		//判断用户手续费是否为0
		if(0==userfee){
			//主提现-；待提现+
			for(int i = 0; i <= 0 ; i++){
				if(0==i){
					if(balance==null){
						balance = checkInfoService.getBalance(user,"");
					}
				}else{
					balance = checkInfoService.getBalance(user,TransCodeConst.THIRDPARTYID_TXDQSZH);
					flag=false;
				}
				balance.setPulseDegree(balance.getPulseDegree()+1);
				finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, balance, entryId, flag);
				if(null!=finanaceEntries&&finanaceEntries.size()>0){
					for(FinanaceEntry finanaceEntry:finanaceEntries) {
						finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
						finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
						if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
							finanaceEntry.setRemark("提现");
						}
						if(finanaceEntry.getPaymentAmount()>0){
							finanaceEntrieAlls.add(finanaceEntry);
						}
					}
				}
			}

		}else{
			//主提现-；待提现+；收益户+
			for(int i = 0; i <= 2 ; i++){
				if(0==i){
					if(balance==null){
						balance = checkInfoService.getBalance(user,"");
					}
					transOrderInfo.setAmount(amount+userfee);
				}else if(1 == i){					
					continue;
//					balance = checkInfoService.getBalance(user,TransCodeConst.THIRDPARTYID_TXDQSZH);
//					transOrderInfo.setAmount(amount);
//					flag=false;
				}else{
					balance = checkInfoService.getBalance(user,TransCodeConst.THIRDPARTYID_JRDQYSYZZH);
					transOrderInfo.setAmount(userfee);
					flag=false;
				}
				balance.setPulseDegree(balance.getPulseDegree()+1);
				finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, balance, entryId, flag);
				if(null!=finanaceEntries&&finanaceEntries.size()>0){
					for(FinanaceEntry finanaceEntry:finanaceEntries) {
						finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
						finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
						if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
							finanaceEntry.setRemark("提现");
						}
						if(finanaceEntry.getPaymentAmount()>0){
							finanaceEntrieAlls.add(finanaceEntry);
						}
					}
				}
			}
		}
		logger.info("JRDwithdrow end <<==================");
		return finanaceEntrieAlls;
	}

	/**
	 * by liuhuan
	 */
	public ErrorResponse transferForDubbo(TransOrderInfo transOrderInfo,String productId){
		ErrorResponse response = this.checkParam(transOrderInfo, productId);
		if(!"ok".equals(response.getCode())){
			return response;
		}
		return this.transfer(transOrderInfo, productId, transOrderInfo.getUserId());
	}
	/**
	 * by liuhuan
	 */
	public ErrorResponse deductForDubbo(TransOrderInfo transOrderInfo,String productId){
		ErrorResponse response = this.checkParam(transOrderInfo, productId);
		if(!"ok".equals(response.getCode())){
			return response;
		}
		if(transOrderInfo.getUserFee() == null || "".equals(transOrderInfo.getUserFee().toString())){
			response.setCode("C1");
			response.setMsg("用户手续费不可以为空！");
			return response;			
		}
		return this.deduct(transOrderInfo, productId, transOrderInfo.getUserId());
	}
	/**
	 * by liuhuan
	 */	
	private ErrorResponse checkParam(TransOrderInfo transOrderInfo,String productId){
		ErrorResponse response=new ErrorResponse();
		response.setCode("ok");
		
		if(transOrderInfo == null){
			response.setCode("C1");
			response.setMsg("订单信息不可以为空！");
			return response;
		}
		if(transOrderInfo.getAmount() == null || "".equals(transOrderInfo.getAmount().toString())){
			response.setCode("C1");
			response.setMsg("入账金额不可以为空！");
			return response;			
		}
		if(transOrderInfo.getUserId() == null || "".equals(transOrderInfo.getUserId())){
			response.setCode("C1");
			response.setMsg("用户ID不可以为空！");
			return response;			
		}
		if(transOrderInfo.getFuncCode() == null || "".equals(transOrderInfo.getFuncCode())){
			response.setCode("C1");
			response.setMsg("功能编码不可以为空！");
			return response;			
		}
		if(transOrderInfo.getInterMerchantCode() == null || "".equals(transOrderInfo.getInterMerchantCode())){
			response.setCode("C1");
			response.setMsg("中间商户编码不可以为空！");
			return response;			
		}
		if(transOrderInfo.getMerchantCode() == null || "".equals(transOrderInfo.getMerchantCode())){
			response.setCode("C1");
			response.setMsg("商户编码/机构号不可以为空！");
			return response;			
		}
		if(transOrderInfo.getOrderAmount() == null || "".equals(transOrderInfo.getOrderAmount().toString())){
			response.setCode("C1");
			response.setMsg("订单金额不可以为空！");
			return response;			
		}
		if(transOrderInfo.getOrderCount() == null || "".equals(transOrderInfo.getOrderCount().toString())){
			response.setCode("C1");
			response.setMsg("订单数量不可以为空！");
			return response;			
		}
		if(transOrderInfo.getOrderDate() == null || "".equals(transOrderInfo.getOrderDate().toString())){
			response.setCode("C1");
			response.setMsg("订单日期不可以为空！");
			return response;			
		}
		if(transOrderInfo.getOrderNo() == null || "".equals(transOrderInfo.getOrderNo())){
			response.setCode("C1");
			response.setMsg("订单号不可以为空！");
			return response;			
		}
		if(transOrderInfo.getRequestTime() == null || "".equals(transOrderInfo.getRequestTime().toString())){
			response.setCode("C1");
			response.setMsg("交易请求时间不可以为空,格式 yyyy-MM-dd hh:mm:ss！");
			return response;			
		}
		if(transOrderInfo.getStatus() == null || "".equals(transOrderInfo.getStatus().toString())){
			response.setCode("C1");
			response.setMsg("状态不可以为空,1正常！");
			return response;			
		}
		if(productId == null || "".equals(productId)){
			response.setCode("C1");
			response.setMsg("产品号不可以为空！");
			return response;			
		}
		if(transOrderInfo.getUserIpAddress() == null || "".equals(transOrderInfo.getUserIpAddress())){
			response.setCode("C1");
			response.setMsg("用户IP地址不可以为空！");
			return response;			
		}
		
		return response;
	}

	/**
	 * 冲正（运营平台）
	 * @param funcCode 机构号
	 * @param newOrderNo 新订单号
	 * @param userIpAddress 用户ip
	 * @param oldOrderNo 旧订单号
	 * @param rootInstCd 机构号
	 * @return
	 */
    public ReversalResponse reversalPlat(String funcCode,String newOrderNo,String userIpAddress,String oldOrderNo,String rootInstCd){
    	logger.info("冲正（运营平台）  传入参数funcCode="+funcCode+",newOrderNo="+newOrderNo+
    			",userIpAddress="+userIpAddress+",oldOrderNo="+oldOrderNo+",rootInstCd="+rootInstCd);
    	ReversalResponse res = new ReversalResponse();
		if (funcCode==null||"".equals(funcCode.trim())||newOrderNo==null||"".equals(newOrderNo.trim())||oldOrderNo==null||"".equals(oldOrderNo.trim())||rootInstCd==null||"".equals(rootInstCd.trim())) {
			res.setCode(CodeEnum.ERR_PARAM_NULL.getCode());
			res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
			return res;
		}
		TransOrderInfoQuery query=new TransOrderInfoQuery();
		query.setOrderNo(oldOrderNo);
		query.setMerchantCode(rootInstCd);
		List<TransOrderInfo> transOrderInfos=transOrderInfoManager.queryList(query);
		logger.info("冲正（运营平台）  查出的订单个数="+transOrderInfos.size());
		if (transOrderInfos.size()==0) {
			res.setCode(CodeEnum.ERR_DATA_NO_RESULT.getCode());
			res.setMsg(CodeEnum.ERR_DATA_NO_RESULT.getMessage());
			return res;
		}
		FinanaceEntryQuery finQuery = new FinanaceEntryQuery();
		finQuery.setReferId(String.valueOf(transOrderInfos.get(0).getRequestId()));
		List<FinanaceEntry> finEntryList = finanaceEntryDao.selectByExample(finQuery);
		logger.info("冲正（运营平台）  查出的流水个数="+finEntryList.size());
		if (finEntryList.size()==0) {
			res.setCode(CodeEnum.FAILURE.getCode());
			res.setMsg("没有查出流水信息");
			return res;
		}
		String[] finAccIdArray = new String[finEntryList.size()];
		for (int i = 0; i < finEntryList.size(); i++) {
			finAccIdArray[i]=finEntryList.get(i).getFinAccountId();
		}
		List<FinanaceAccount> finAccList = finanaceAccountDao.selectByFinAccountId(finAccIdArray);
		logger.info(" 冲正前查出的资金账户信息个数="+finAccList.size());
		if (finAccList.size()==0) {
			res.setCode(CodeEnum.FAILURE.getCode());
			res.setMsg("冲正前没有查出资金账户信息");
			return res;
		}
		List<Balance> balanceBeforeList = new ArrayList<Balance>();
		for (int i = 0; i < finAccList.size(); i++) {
			FinanaceAccount finAccount = finAccList.get(i);
			User user = new User();
			user.userId=finAccount.getAccountRelateId();
			user.constId=rootInstCd;
			user.statusID=BaseConstants.ACCOUNT_STATUS_OK;
			Balance balance = (Balance) checkInfoService.getBalance(user, finAccount.getFinAccountId());
			if (balance==null) {
				res.setCode(CodeEnum.FAILURE.getCode());
				res.setMsg("冲正前无法获取用户账户信息");
				return res;
			}
			balance.setUserId(finAccount.getAccountRelateId());
			balanceBeforeList.add(balance);
		}
		res.setBalanceBeforeList(balanceBeforeList);
		ErrorResponse errorResponse = antideductForDubbo(funcCode,newOrderNo,userIpAddress,oldOrderNo,rootInstCd);
		if (!errorResponse.isIs_success()) {
			res.setCode(CodeEnum.FAILURE.getCode());
			res.setMsg(errorResponse.getMsg());
			return res;
		}
		List<Balance> balanceAfterList = new ArrayList<Balance>();
		for (int i = 0; i < finAccList.size(); i++) {
			FinanaceAccount finAccount = finAccList.get(i);
			User user = new User();
			user.userId=finAccount.getAccountRelateId();
			user.constId=rootInstCd;
			user.statusID=BaseConstants.ACCOUNT_STATUS_OK;
			Balance balance = (Balance) checkInfoService.getBalance(user, finAccount.getFinAccountId());
			if (balance==null) {
				res.setCode(CodeEnum.FAILURE.getCode());
				res.setMsg("冲正后无法获取用户账户信息");
				return res;
			}
			balance.setUserId(finAccount.getAccountRelateId());
			balanceAfterList.add(balance);
		}
		res.setBalanceAfterList(balanceAfterList);;
    	return res;
    }
	
	@Override
	public ErrorResponse antideductForDubbo(String funcCode, String orderNo, String userIpAddress, String orderPackageNo,String rootInstCd) {
		logger.info("---------------------------dubbo接口调用冲正操作开始------------------------");
		ErrorResponse response=new ErrorResponse();
		
		logger.info("-------入参：funcCode : " + funcCode + ";orderNo : " + orderNo + ";userIpAddress : " + userIpAddress + ";orderPackageNo : " + orderPackageNo + ";rootInstCd : " + rootInstCd + "------------------------");
		
		if(funcCode == null || "".equals(funcCode)){
			response.setCode("P1");
			response.setMsg("funccode不能为空");
			logger.info("---------------------------funccode不能为空------------------------");
			return response;
		}
		if(orderNo == null || "".equals(orderNo)){
			response.setCode("P1");
			response.setMsg("orderno不能为空");
			logger.info("---------------------------orderno不能为空------------------------");
			return response;
		}
		if(userIpAddress == null || "".equals(userIpAddress)){
			response.setCode("P1");
			response.setMsg("useripaddress不能为空");
			logger.info("---------------------------useripaddress不能为空------------------------");
			return response;
		}
		if(orderPackageNo == null || "".equals(orderPackageNo)){
			response.setCode("P1");
			response.setMsg("orderPackageNo不能为空");
			logger.info("---------------------------orderPackageNo不能为空------------------------");
			return response;
		}
		if(rootInstCd == null || "".equals(rootInstCd)){
			response.setCode("P1");
			response.setMsg("rootInstCd不能为空");
			logger.info("---------------------------rootInstCd不能为空------------------------");
			return response;
		}
		
		TransOrderInfo transOrderInfo = new TransOrderInfo();
		transOrderInfo.setFuncCode(funcCode);
		transOrderInfo.setOrderNo(orderNo);
		transOrderInfo.setOrderPackageNo(orderPackageNo);
		transOrderInfo.setUserIpAddress(userIpAddress);
		transOrderInfo.setMerchantCode(rootInstCd);
		
		response = this.antiDeduct(transOrderInfo);
		logger.info("---------------------------dubbo接口调用冲正操作结束------------------------");
		return response;
	}

	@Override
	public ErrorResponse afterSpendingRefundForDubbo(TransOrderInfo transOrderInfo,String productId,String referUserId) {
		logger.info("------------通过dubbo调用消费后退款流程开始------------");
		
		logger.info("----入参：amount : " + transOrderInfo.getAmount() + ";feeamount : " + transOrderInfo.getFeeAmount());
		logger.info("----入参：userid : " + transOrderInfo.getUserId() + ";funccode : " + transOrderInfo.getFuncCode());
		logger.info("----入参：intermerchantcode : " + transOrderInfo.getInterMerchantCode() + ";merchantcode : " + transOrderInfo.getMerchantCode());
		logger.info("----入参：orderamount : " + transOrderInfo.getOrderAmount() + ";ordercount : " + transOrderInfo.getOrderCount());
		logger.info("----入参：orderDate : " + transOrderInfo.getOrderDate() + ";orderno : " + transOrderInfo.getOrderNo());
		logger.info("----入参：orderpackageno : " + transOrderInfo.getOrderPackageNo() + ";requesttime : " + transOrderInfo.getRequestTime());
		logger.info("----入参：status : " + transOrderInfo.getStatus() + ";userfee : " + transOrderInfo.getUserFee());
		logger.info("----入参：orderpackageno : " + transOrderInfo.getOrderPackageNo() + ";useripaddress : " + transOrderInfo.getUserIpAddress());
		logger.info("----入参：productId : " + productId);
		
		ErrorResponse response=new ErrorResponse();
		if(transOrderInfo.getAmount() == null || transOrderInfo.getAmount() <= 0){
			response.setCode("P1");
			response.setMsg("请输入正确的退款金额amount");
			logger.info("------------请输入正确的退款金额amount------------");
			return response;
		}
//		if(transOrderInfo.getFeeAmount() == null || transOrderInfo.getFeeAmount() <= 0){
//			response.setCode("P1");
//			response.setMsg("feeamount不能为空");
//			logger.info("------------feeamount不能为空------------");
//			return response;
//		}
		if(transOrderInfo.getUserId() == null || "".equals(transOrderInfo.getUserId())){
			response.setCode("P1");
			response.setMsg("userid不能为空");
			logger.info("------------userid不能为空------------");
			return response;
		}
		if(transOrderInfo.getFuncCode() == null || "".equals(transOrderInfo.getFuncCode())){
			response.setCode("P1");
			response.setMsg("funccode不能为空");
			logger.info("------------funccode不能为空------------");
			return response;
		}
		if(transOrderInfo.getInterMerchantCode() == null || "".equals(transOrderInfo.getInterMerchantCode())){
			response.setCode("P1");
			response.setMsg("intermerchantcode不能为空");
			logger.info("------------intermerchantcode不能为空------------");
			return response;
		}
		if(transOrderInfo.getMerchantCode() == null || "".equals(transOrderInfo.getMerchantCode())){
			response.setCode("P1");
			response.setMsg("merchantcode不能为空");
			logger.info("------------merchantcode不能为空------------");
			return response;
		}
		if(transOrderInfo.getOrderAmount() == null || transOrderInfo.getOrderAmount() <= 0){
			response.setCode("P1");
			response.setMsg("请输入正确的订单金额orderamount");
			logger.info("------------请输入正确的订单金额orderamount------------");
			return response;
		}
		if(transOrderInfo.getOrderCount() == null || transOrderInfo.getOrderCount() <= 0){
			response.setCode("P1");
			response.setMsg("请输入正确的订单数量ordercount");
			logger.info("------------请输入正确的订单金额ordercount------------");
			return response;
		}
		if(transOrderInfo.getOrderDate() == null){
			response.setCode("P1");
			response.setMsg("orderDate不能为空");
			logger.info("------------orderDate不能为空------------");
			return response;
		}
		if(transOrderInfo.getOrderNo() == null || "".equals(transOrderInfo.getOrderNo())){
			response.setCode("P1");
			response.setMsg("orderno不能为空");
			logger.info("------------orderno不能为空------------");
			return response;
		}
		if(transOrderInfo.getOrderPackageNo() == null || "".equals(transOrderInfo.getOrderPackageNo())){
			response.setCode("P1");
			response.setMsg("orderpackageno不能为空");
			logger.info("------------orderpackageno不能为空------------");
			return response;
		}
		if(transOrderInfo.getRequestTime() == null){
			response.setCode("P1");
			response.setMsg("requesttime不能为空");
			logger.info("------------requesttime不能为空------------");
			return response;
		}
		if(transOrderInfo.getStatus() == null){
			response.setCode("P1");
			response.setMsg("status不能为空");
			logger.info("------------status不能为空------------");
			return response;
		}
		if(transOrderInfo.getUserFee() == null){
			response.setCode("P1");
			response.setMsg("userfee不能为空");
			logger.info("------------userfee不能为空------------");
			return response;
		}
		if(transOrderInfo.getUserIpAddress() == null || "".equals(transOrderInfo.getUserIpAddress())){
			response.setCode("P1");
			response.setMsg("useripaddress不能为空");
			logger.info("------------useripaddress不能为空------------");
			return response;
		}
		if(productId == null || "".equals(productId)){
			response.setCode("P1");
			response.setMsg("productId不能为空");
			logger.info("------------productId不能为空------------");
			return response;
		}
		//判断订单状态是否为失效状态 ，如为失效 则不记录分录信息
		if(TransCodeConst.TRANS_STATUS_INVALIDATION == transOrderInfo.getStatus()){
			response = this.saveTransorder(transOrderInfo, "消费后退款");
		}else{
			response = this.afterspendingrefund(transOrderInfo,productId, transOrderInfo.getUserId(),referUserId);
		}
		logger.info("------------通过dubbo调用消费后退款流程结束------------");
		return response;
	}


	/**
	 * 君融贷活期理财赎回
	 * @param transOrderInfo
	 * @param orderAuxiliary
	 * @return
	 */
	public String redemption(TransOrderInfo transOrderInfo, OrderAuxiliary orderAuxiliary ){
		synchronized ( lock ) {
			String isOk = "ok";
			logger.info("---------------------------账户UserId："
					+ transOrderInfo.getUserId() + "从君融贷赎回本金：" + orderAuxiliary.getCapitalAmount()
					+ "分，利息：" + orderAuxiliary.getInterestamount() + "分，赎回操作开始------------------------");
			logger.info("赎回操作参数信息：capitalAmount ="
					+ orderAuxiliary.getCapitalAmount() + "interestAmount = " + orderAuxiliary.getInterestamount()
					+ ",UserId =" + transOrderInfo.getUserId() + ",feeamount =" + transOrderInfo.getFeeAmount()
					+ ",funccode =" + transOrderInfo.getFuncCode() + ",intermerchantcode ="
					+ transOrderInfo.getInterMerchantCode() + ",merchantcode =" + transOrderInfo.getMerchantCode()
					+ "orderamount = " + transOrderInfo.getOrderAmount() + ",ordercount="
					+ transOrderInfo.getOrderCount() + ",orderdate=" + transOrderInfo.getOrderDate() + ",orderno="
					+ transOrderInfo.getOrderNo() + ",orderpackageno=" + ",remark =" + transOrderInfo.getRemark()
					+ ",requestno=" + transOrderInfo.getRequestNo() + ",requesttime=" + transOrderInfo.getRequestTime()
					+ ",status=" + transOrderInfo.getStatus() + ",tradeflowno=" + transOrderInfo.getTradeFlowNo()
					+ ",userfee=" + transOrderInfo.getUserFee() + ",feeamount=" + transOrderInfo.getFeeAmount()
					+ ",profit=" + transOrderInfo.getProfit() + ",busitypeid=" + transOrderInfo.getBusiTypeId()
					+ ",bankcode=" + transOrderInfo.getBankCode() + ",errorcode=" + transOrderInfo.getErrorCode()
					+ ",errormsg=" + transOrderInfo.getErrorMsg() + ",productid=" + orderAuxiliary.getProductQAA()
					+ ",useripaddress=" + transOrderInfo.getUserIpAddress() + ",intoproductid="
					+ orderAuxiliary.getProductQAB());

			//入参校验
			if (null == orderAuxiliary.getCapitalAmount() || "".equals(orderAuxiliary.getCapitalAmount()) ) {
				logger.info("请输入赎回本金金额！");
				return "请输入赎回本金金额！";
			}
			if (null == orderAuxiliary.getInterestamount() || "".equals(orderAuxiliary.getInterestamount()) ) {
				logger.info("请输入赎回利息金额！");
				return "请输入赎回利息金额！";
			}
			if ( null == transOrderInfo.getMerchantCode() || "".equals(transOrderInfo.getMerchantCode()) ) {
				logger.info("机构号为空！");
				return "机构号为空！";
			}
			if ( null == transOrderInfo.getMerchantCode() || "".equals(transOrderInfo.getMerchantCode()) ) {
				logger.info("机构号为空！");
				return "机构号为空！";
			}
			if ( null == transOrderInfo.getOrderNo() || "".equals(transOrderInfo.getOrderNo()) ) {
				logger.info("订单号为空！");
				return "订单号为空！";
			}
			if (  !Constants.JRD_PURCHASE_INTOPRODUCTID.equals(orderAuxiliary.getProductQAA()) ) {
				logger.info("转出产品号不为用户本金子账户！");
				return "转出产品号不为用户本金子账户！";
			}
			if ( null == transOrderInfo.getMerchantCode() || "".equals(transOrderInfo.getMerchantCode()) ) {
				logger.info("机构号为空！");
				return "机构号为空！";
			}
			if ( null == orderAuxiliary.getProductQAB() || "".equals(orderAuxiliary.getProductQAB()) ) {
				logger.info("转入产品号为空！");
				return "转入产品为空！";
			}//对于一些不影响操作的字段未校验
			if ( null == transOrderInfo.getBusiTypeId()
					|| "".equals(transOrderInfo.getBusiTypeId()) || !AccountConstants.JRD_BUSITYPE_REDEMPTION.equals(transOrderInfo.getBusiTypeId()) ) {
				logger.info("业务类型为空或不是赎回类型！");
				return "业务类型为空或不是赎回类型！";
			}

			if ( null == transOrderInfo.getInterMerchantCode() || "".equals(transOrderInfo.getInterMerchantCode()) ) {
				logger.info("转入用户id（intermerchantcode）为空！");
				return "转入用户id（intermerchantcode）为空！";
			}

			//业务校验
			
			if(! transOrderInfo.getUserId().equals(transOrderInfo.getInterMerchantCode())){
				logger.info("userId和InterMerchantCode不一样！必须赎回到自己账户!");
				return "userId和InterMerchantCode不一样！必须赎回到自己账户！";
			}
			//校验交易码
			if ( !TransCodeConst.ADJUST_ACCOUNT_AMOUNT.equals(transOrderInfo.getFuncCode()) ) {
				logger.info("赎回交易码错误！订单中的交易码为" + transOrderInfo.getFuncCode());
				return "赎回交易码错误！";
			}
			//校验订单号是否存在
			if ( !this.orderNoChk(transOrderInfo.getOrderNo(), transOrderInfo.getMerchantCode()) ) {
				logger.info("订单号：" + transOrderInfo.getOrderNo() + "在机构号：" + transOrderInfo.getMerchantCode() + "中已存在");
				return "该交易订单号已存在，请确认！";
			}

			//校验各君融贷账户状态是否正常
			long accountCapitalAmount = 0L;//用户活期本金子账户活期本金金额
			long accountInterestAmount = 0L;//用户活期利息子账户活期利息金额
			long JRDCapitalAmount = 0L;//君融贷可用活期存款子账户金额
			long JRDInterestAmount = 0L;//君融贷活期应付利息子账户金额
			long capitalAmount = orderAuxiliary.getCapitalAmount();//赎回本金金额
			long interestAmount = orderAuxiliary.getInterestamount();//赎回利息金额
			boolean accountIsOK = true;//校验各账户状态

			User user = new User();
			user.userId = transOrderInfo.getUserId();
			user.constId = transOrderInfo.getMerchantCode();
			user.productId = Constants.JRD_PURCHASE_INTOPRODUCTID;//用户活期本金子账户

			accountIsOK = operationService.checkAccount(user);
			if ( !accountIsOK ) {
				logger.error(transOrderInfo.getUserId() + "用户活期本金子账户状态非正常状态");
				return "用户活期本金子账户状态非正常状态";
			}
			Balance capitalBalance = this.getBalance(user, "");
			if ( null == capitalBalance ) {
				logger.error(transOrderInfo.getUserId() + "获取用户活期本金子账户金额失败");
				return "获取用户活期本金子账户金额失败";
			}
			accountCapitalAmount = capitalBalance.getBalanceSettle();

			user.productId = Constants.JRD_INTEREST_ACCOUNT;//用户活期活期利息子账户
			accountIsOK = operationService.checkAccount(user);
			if ( !accountIsOK ) {
				logger.error(transOrderInfo.getUserId() + "用户活期活期利息子账户状态为非正常状态");
				return "用户活期活期利息子账户状态非正常状态";
			}
			Balance interestBalance = this.getBalance(user, "");
			if ( null == interestBalance ) {
				logger.error(transOrderInfo.getUserId() + "获取用户活期利息子账户金额失败");
				return "获取用户活期利息子账户金额失败";
			}
			accountInterestAmount = interestBalance.getBalanceSettle();

			user.userId = transOrderInfo.getInterMerchantCode();//转入用户id
			user.productId = orderAuxiliary.getProductQAB();//用户转入账户（主账户）;
			accountIsOK = operationService.checkAccount(user);
			if ( !accountIsOK ) {
				logger.error(transOrderInfo.getUserId() + "用户主账户状态为非正常状态");
				return "用户主账户状态非正常状态";
			}
			Balance mainBalance = this.getBalance(user, "");

			user.userId = Constants.JRD_USERID;
			user.constId = Constants.JRD_ID;
			user.productId = Constants.JRD_CURRENT_ACCOUNT;//君融贷活期存款子账户
			accountIsOK = operationService.checkAccount(user);
			if ( !accountIsOK ) {
				logger.error(transOrderInfo.getUserId() + "君融贷活期存款子账户状态为非正常状态");
				return "君融贷活期存款子账户状态为非正常状态";
			}
			Balance JRDCurrentBalance = this.getBalance(user, "");
			if ( null == JRDCurrentBalance ) {
				logger.error(transOrderInfo.getUserId() + "获取君融贷活期存款子账户金额失败");
				return "获取君融贷活期存款子账户子账户金额失败";
			}
			JRDCapitalAmount = JRDCurrentBalance.getBalanceSettle();

			user.productId = Constants.JRD_QUOTA_ACCOUNT;//君融贷可用活期存款子账户
			accountIsOK = operationService.checkAccount(user);
			if ( !accountIsOK ) {
				logger.error(transOrderInfo.getUserId() + "君融贷可用活期存款子账户状态为非正常状态");
				return "君融贷可用活期存款子账户状态为非正常状态";
			}
			Balance JRDCurrentUsableBalance = this.getBalance(user, "");
			user.productId = Constants.JRD_PAYABLE_INTEREST_ACCOUNT;//君融贷活期应付利息子账户
			accountIsOK = operationService.checkAccount(user);
			if ( !accountIsOK ) {
				logger.error(transOrderInfo.getUserId() + "君融贷活期应付利息子账户状态为非正常状态");
				return "君融贷活期应付利息子账户状态为非正常状态";
			}
			Balance JRDInterestBalance = this.getBalance(user, "");
			if ( null == JRDInterestBalance ) {
				logger.error(transOrderInfo.getUserId() + "获取君融贷活期应付利息子账户金额失败");
				return "获取君融贷活期应付利息子账户金额失败";
			}
			JRDInterestAmount = JRDInterestBalance.getBalanceSettle();

			if ( capitalAmount >= 0 && interestAmount >= 0 ) {
				if ( capitalAmount == 0 && interestAmount == 0 ) {
					logger.info("赎回的总金额等于0！参数中的赎回本金金额为：" + capitalAmount + "赎回利息金额为：" + interestAmount);
					return "赎回的总金额等于0！";
				}
				if ( capitalAmount > accountCapitalAmount ) {
					logger.info("赎回的本金金额大于用户的本金金额！参数中的赎回本金金额为：" + capitalAmount);
					return "赎回的本金金额大于用户的本金金额！";
				}
				if ( interestAmount > accountInterestAmount ) {
					logger.info("赎回的利息金额大于用户的利息金额！参数中的赎回利息金额为：" + interestAmount);
					return "赎回的利息金额大于用户的利息金额！";
				}
			} else {
				logger.info("赎回金额不能为负数！参数中的赎回本金金额为：" + capitalAmount + "赎回利息金额为：" + interestAmount);
				return "赎回金额不能为负数！";
			}
			if ( capitalAmount > JRDCapitalAmount ) {
				logger.info("赎回的本金金额大于君融贷活期子账户金额！参数中的赎回本金金额为：" + capitalAmount);
				return "赎回的本金金额大于君融贷活期子账户金额！";
			}
			if ( interestAmount > JRDInterestAmount ) {
				logger.info("赎回的利息金额大于活期应付利息子账户金额！参数中的赎回利息金额为：" + interestAmount);
				return "赎回的利息金额大于君融贷活期应付利息子账户金额！";
			}


			//保存订单信息
			transOrderInfo.setAmount(capitalAmount);//赎回本金金额
			//赎回利息金额，此处少字段，存入了表的利润字段
			transOrderInfo.setProfit(interestAmount);
			//转出产品号，此字段存入了表的错误编码字段
			transOrderInfo.setErrorCode(orderAuxiliary.getProductQAA());
			//转入产品号，此字段存入了表的错误信息字段
			transOrderInfo.setErrorMsg(orderAuxiliary.getProductQAB());
			transOrderInfo.setRemark("君融贷活期理财赎回");

			String accountDateString = operationServiceImpl.getAccountDate();
			if ( accountDateString == null || accountDateString.equals("") ) {
				return null;
			}
			try {
				transOrderInfo.setAccountDate(dateUtil.parse(accountDateString, "yyyy-MM-dd"));
			} catch ( ParseException e ) {
				e.printStackTrace();
			}//设置记账日期
			ErrorResponse response = this.saveTransorder(transOrderInfo, "君融贷活期理财赎回");
			if ( !response.isIs_success() ) {
				logger.error("保存赎回订单信息失败！");
				return "保存赎回订单信息失败！";
			}

			//生成交易流水

			//获取单个账户记账流水
			FinanaceEntry finanaceEntry = new FinanaceEntry();
			//获取所有账户记账流水
			List<FinanaceEntry> finanaceEntrieAlls = new ArrayList<FinanaceEntry>();

			try {
				//获取套录号
				String entryId = redisIdGenerator.createRequestNo();
				boolean flag = false;
				if ( capitalAmount == 0 ) {//只赎回利息

					transOrderInfo.setAmount(interestAmount);

					//用户利息子账户减相应金额
					interestBalance.setPulseDegree(interestBalance.getPulseDegree() + 1);//心跳计次加一
					interestBalance.setBalanceSettle(interestBalance.getBalanceSettle() - interestAmount);//清算余额
					interestBalance.setAmount(interestBalance.getAmount() - interestAmount);//账户余额
					interestBalance.setBalanceUsable(interestBalance.getBalanceUsable() - interestAmount);//账户可用余额
					flag = false;
					finanaceEntry = operationServiceImpl.getAccountFlow(transOrderInfo, interestBalance, entryId, flag);
					finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
					finanaceEntrieAlls.add(finanaceEntry);

					//用户主账户加相应金额
					mainBalance.setPulseDegree(mainBalance.getPulseDegree() + 1);//心跳计次加一
					mainBalance.setAmount(mainBalance.getAmount() + interestAmount);//账户余额
					mainBalance.setBalanceSettle(mainBalance.getBalanceSettle() + interestAmount);//账户清算余额/可提现余额
					mainBalance.setBalanceUsable(mainBalance.getBalanceUsable() + interestAmount);//账户可用余额
					flag = true;
					finanaceEntry = operationServiceImpl.getAccountFlow(transOrderInfo, mainBalance, entryId, flag);
					finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
					finanaceEntrieAlls.add(finanaceEntry);

					//君融贷应付利息子账户减相应金额
					JRDInterestBalance.setPulseDegree(JRDInterestBalance.getPulseDegree() + 1);//心跳计次加一
					JRDInterestBalance.setAmount(JRDInterestBalance.getAmount() - interestAmount);//账户余额
					JRDInterestBalance.setBalanceSettle(JRDInterestBalance.getBalanceSettle() - interestAmount);//账户清算余额/可提现余额
					JRDInterestBalance.setBalanceUsable(JRDInterestBalance.getBalanceUsable() - interestAmount);//账户可用余额
					flag = false;
					finanaceEntry = operationServiceImpl.getAccountFlow(transOrderInfo, JRDInterestBalance, entryId,
						flag);
					finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
					finanaceEntrieAlls.add(finanaceEntry);

				} else if ( interestAmount == 0 ) {//只赎回本金

					transOrderInfo.setAmount(capitalAmount);

					//用户本金子账户减相应金额
					capitalBalance.setPulseDegree(capitalBalance.getPulseDegree() + 1);//心跳计次加一
					capitalBalance.setBalanceSettle(capitalBalance.getBalanceSettle() - capitalAmount);//清算余额
					capitalBalance.setAmount(capitalBalance.getAmount() - capitalAmount);//账户余额
					capitalBalance.setBalanceUsable(capitalBalance.getBalanceUsable() - capitalAmount);//账户可用余额
					flag = false;
					finanaceEntry = operationServiceImpl.getAccountFlow(transOrderInfo, capitalBalance, entryId, flag);
					finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
					finanaceEntrieAlls.add(finanaceEntry);

					//用户主账户加相应金额
					mainBalance.setPulseDegree(mainBalance.getPulseDegree() + 1);//心跳计次加一
					mainBalance.setAmount(mainBalance.getAmount() + capitalAmount);//账户余额
					mainBalance.setBalanceSettle(mainBalance.getBalanceSettle() + capitalAmount);//账户清算余额/可提现余额
					mainBalance.setBalanceUsable(mainBalance.getBalanceUsable() + capitalAmount);//账户可用余额
					flag = true;
					finanaceEntry = operationServiceImpl.getAccountFlow(transOrderInfo, mainBalance, entryId, flag);
					finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
					finanaceEntrieAlls.add(finanaceEntry);

					//君融贷活期存款子账户减相应金额
					JRDCurrentBalance.setPulseDegree(JRDCurrentBalance.getPulseDegree() + 1);//心跳计次加一
					JRDCurrentBalance.setAmount(JRDCurrentBalance.getAmount() - capitalAmount);//账户余额
					JRDCurrentBalance.setBalanceSettle(JRDCurrentBalance.getBalanceSettle() - capitalAmount);//账户清算余额/可提现余额
					JRDCurrentBalance.setBalanceUsable(JRDCurrentBalance.getBalanceUsable() - capitalAmount);//账户可用余额
					flag = false;
					finanaceEntry = operationServiceImpl.getAccountFlow(transOrderInfo, JRDCurrentBalance, entryId,
						flag);
					finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
					finanaceEntrieAlls.add(finanaceEntry);

					//君融贷可用活期存款子账户减相应金额，若不够减，则置为0
					if (JRDCurrentUsableBalance.getBalanceSettle() > 0){//如果君融贷可用活期余额为0，则此账户不计流水
						JRDCurrentUsableBalance.setPulseDegree(JRDCurrentUsableBalance.getPulseDegree() + 1);//心跳计次加一
						if ( JRDCurrentUsableBalance.getBalanceSettle() - capitalAmount < 0 ) {
							//流水变动余额应为实际变动余额
							transOrderInfo.setAmount(JRDCurrentUsableBalance.getBalanceSettle());
							
							JRDCurrentUsableBalance.setAmount(0L);//账户余额
							JRDCurrentUsableBalance.setBalanceSettle(0L);//账户清算余额/可提现余额
							JRDCurrentUsableBalance.setBalanceUsable(0L);//账户可用余额
						} else {
							JRDCurrentUsableBalance.setAmount(JRDCurrentUsableBalance.getAmount() - capitalAmount);//账户余额
							JRDCurrentUsableBalance.setBalanceSettle(JRDCurrentUsableBalance.getBalanceSettle()
									- capitalAmount);//账户清算余额/可提现余额
							JRDCurrentUsableBalance.setBalanceUsable(JRDCurrentUsableBalance.getBalanceUsable()
									- capitalAmount);//账户可用余额
						}
						
						flag = false;
						finanaceEntry = operationServiceImpl.getAccountFlow(transOrderInfo, JRDCurrentUsableBalance,
							entryId, flag);
						finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
						finanaceEntrieAlls.add(finanaceEntry);
					}

				} else {//本金利息都有

					transOrderInfo.setAmount(interestAmount);

					//用户利息子账户减相应金额
					interestBalance.setPulseDegree(interestBalance.getPulseDegree() + 1);//心跳计次加一
					interestBalance.setBalanceSettle(interestBalance.getBalanceSettle() - interestAmount);//清算余额
					interestBalance.setAmount(interestBalance.getAmount() - interestAmount);//账户余额
					interestBalance.setBalanceUsable(interestBalance.getBalanceUsable() - interestAmount);//账户可用余额
					flag = false;
					finanaceEntry = operationServiceImpl.getAccountFlow(transOrderInfo, interestBalance, entryId, flag);
					finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
					finanaceEntrieAlls.add(finanaceEntry);

					//用户主账户加相应金额
					mainBalance.setPulseDegree(mainBalance.getPulseDegree() + 1);//心跳计次加一
					mainBalance.setAmount(mainBalance.getAmount() + interestAmount);//账户余额
					mainBalance.setBalanceSettle(mainBalance.getBalanceSettle() + interestAmount);//账户清算余额/可提现余额
					mainBalance.setBalanceUsable(mainBalance.getBalanceUsable() + interestAmount);//账户可用余额
					flag = true;
					finanaceEntry = operationServiceImpl.getAccountFlow(transOrderInfo, mainBalance, entryId, flag);
					finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
					finanaceEntrieAlls.add(finanaceEntry);

					//君融贷应付利息子账户减相应金额
					JRDInterestBalance.setPulseDegree(JRDInterestBalance.getPulseDegree() + 1);//心跳计次加一
					JRDInterestBalance.setAmount(JRDInterestBalance.getAmount() - interestAmount);//账户余额
					JRDInterestBalance.setBalanceSettle(JRDInterestBalance.getBalanceSettle() - interestAmount);//账户清算余额/可提现余额
					JRDInterestBalance.setBalanceUsable(JRDInterestBalance.getBalanceUsable() - interestAmount);//账户可用余额
					flag = false;
					finanaceEntry = operationServiceImpl.getAccountFlow(transOrderInfo, JRDInterestBalance, entryId,
						flag);
					finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
					finanaceEntrieAlls.add(finanaceEntry);

					transOrderInfo.setAmount(capitalAmount);

					//用户本金子账户减相应金额
					capitalBalance.setPulseDegree(capitalBalance.getPulseDegree() + 1);//心跳计次加一
					capitalBalance.setBalanceSettle(capitalBalance.getBalanceSettle() - capitalAmount);//清算余额
					capitalBalance.setAmount(capitalBalance.getAmount() - capitalAmount);//账户余额
					capitalBalance.setBalanceUsable(capitalBalance.getBalanceUsable() - capitalAmount);//账户可用余额
					flag = false;
					finanaceEntry = operationServiceImpl.getAccountFlow(transOrderInfo, capitalBalance, entryId, flag);
					finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
					finanaceEntrieAlls.add(finanaceEntry);

					//用户主账户加相应金额
					mainBalance.setPulseDegree(mainBalance.getPulseDegree() + 1);//心跳计次加一
					mainBalance.setAmount(mainBalance.getAmount() + capitalAmount);//账户余额
					mainBalance.setBalanceSettle(mainBalance.getBalanceSettle() + capitalAmount);//账户清算余额/可提现余额
					mainBalance.setBalanceUsable(mainBalance.getBalanceUsable() + capitalAmount);//账户可用余额
					flag = true;
					finanaceEntry = operationServiceImpl.getAccountFlow(transOrderInfo, mainBalance, entryId, flag);
					finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
					finanaceEntrieAlls.add(finanaceEntry);

					//君融贷活期存款子账户减相应金额
					JRDCurrentBalance.setPulseDegree(JRDCurrentBalance.getPulseDegree() + 1);//心跳计次加一
					JRDCurrentBalance.setAmount(JRDCurrentBalance.getAmount() - capitalAmount);//账户余额
					JRDCurrentBalance.setBalanceSettle(JRDCurrentBalance.getBalanceSettle() - capitalAmount);//账户清算余额/可提现余额
					JRDCurrentBalance.setBalanceUsable(JRDCurrentBalance.getBalanceUsable() - capitalAmount);//账户可用余额
					flag = false;
					finanaceEntry = operationServiceImpl.getAccountFlow(transOrderInfo, JRDCurrentBalance, entryId,
						flag);
					finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
					finanaceEntrieAlls.add(finanaceEntry);

					//君融贷可用活期存款子账户减相应金额，若不够减，则置为0
					if (JRDCurrentUsableBalance.getBalanceSettle() > 0){//如果君融贷可用活期余额为0，则此账户不计流水
						JRDCurrentUsableBalance.setPulseDegree(JRDCurrentUsableBalance.getPulseDegree() + 1);//心跳计次加一
						if ( JRDCurrentUsableBalance.getBalanceSettle() - capitalAmount < 0 ) {
							//流水变动余额应为实际变动余额
							transOrderInfo.setAmount(JRDCurrentUsableBalance.getBalanceSettle());
							
							JRDCurrentUsableBalance.setAmount(0L);//账户余额
							JRDCurrentUsableBalance.setBalanceSettle(0L);//账户清算余额/可提现余额
							JRDCurrentUsableBalance.setBalanceUsable(0L);//账户可用余额
						} else {
							JRDCurrentUsableBalance.setAmount(JRDCurrentUsableBalance.getAmount() - capitalAmount);//账户余额
							JRDCurrentUsableBalance.setBalanceSettle(JRDCurrentUsableBalance.getBalanceSettle()
									- capitalAmount);//账户清算余额/可提现余额
							JRDCurrentUsableBalance.setBalanceUsable(JRDCurrentUsableBalance.getBalanceUsable()
									- capitalAmount);//账户可用余额
						}
						
						flag = false;
						finanaceEntry = operationServiceImpl.getAccountFlow(transOrderInfo, JRDCurrentUsableBalance,
							entryId, flag);
						finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
						finanaceEntrieAlls.add(finanaceEntry);
					}
				}

				List<FinanaceEntry> finaceEntryList = new ArrayList<FinanaceEntry>();
				for ( FinanaceEntry finan : finanaceEntrieAlls ) {
					finan.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
					finan.setReferId(String.valueOf(transOrderInfo.getRequestId()));
					if ( null == finan.getRemark() || "".equals(finan.getRemark()) ) {
						finan.setRemark("君融贷活期理财赎回");
					}
					finaceEntryList.add(finan);
				}

				//批量插入记录流水表   写入数据失败尝试三次
				if ( isOk.equals("ok") ) {
					try {
						finanaceEntryManager.saveFinanaceEntryList(finaceEntryList);
					} catch ( AccountException e ) {
						logger.error("******************账户" + transOrderInfo.getUserId() + "记账流水数据入库失败一次");
						try {
							finanaceEntryManager.saveFinanaceEntryList(finaceEntryList);
						} catch ( AccountException e1 ) {
							logger.error("******************账户" + transOrderInfo.getUserId() + "记账流水数据入库失败两次");
							try {
								finanaceEntryManager.saveFinanaceEntryList(finaceEntryList);
							} catch ( AccountException e2 ) {
								//发送邮件或短信通知管理员
								logger.error("******************账户" + transOrderInfo.getUserId() + "记账流水数据入库失败三次");
								//启用线程 发送邮件
								RkylinMailUtil.sendMailThread(
									"账户记账流水失败通知",
									"******************账户"
											+ transOrderInfo.getUserId() + "做赎回记账流水数据入库连续失败三次，转入方"
											+ transOrderInfo.getInterMerchantCode(),
									TransCodeConst.FINANACE_ENTRY_ERROR_TOEMAIL);
								logger.error(e2.getMessage());
								isOk = "数据库操作异常";
							}
						}
					}
				}

			} catch ( AccountException e ) {
				logger.error(e.getMessage());
				isOk = "数据库操作异常";
			}
			logger.info("---------------------------账户UserId"
					+ transOrderInfo.getUserId() + "赎回操作结束------------------------");
			return isOk;
		}
	}

	@Override
	public String rechargeTXYW(TransOrderInfo transOrderInfo,String productId, String userId) {
		synchronized (lock) {
			logger.info("---------------------------通讯运维账户UserId"+userId+"充值操作开始------------------------");
			logger.info("充值操作参数信息：amount="+transOrderInfo.getAmount()+",UserId="+transOrderInfo.getUserId()+",funccode="+transOrderInfo.getFuncCode()
					+",intermerchantcode="+transOrderInfo.getInterMerchantCode()+",merchantcode="+transOrderInfo.getMerchantCode()+",orderamount="+transOrderInfo.getOrderAmount()
					+",ordercount="+transOrderInfo.getOrderCount()+",orderdate="+transOrderInfo.getOrderDate()+",orderno="+transOrderInfo.getOrderNo()
					+",orderpackageno="+transOrderInfo.getOrderPackageNo()+",paychannelid="+transOrderInfo.getPayChannelId()+",remark="+transOrderInfo.getRemark()
					+",productid="+productId+",requestno="+transOrderInfo.getRequestNo()+",requesttime="+transOrderInfo.getRequestTime()+",status="+transOrderInfo.getStatus()
					+",tradeflowno="+transOrderInfo.getTradeFlowNo()+",userfee="+transOrderInfo.getUserFee()+",feeamount="+transOrderInfo.getFeeAmount()
					+",profit="+transOrderInfo.getProfit()+",busitypeid="+transOrderInfo.getBusiTypeId()+",bankcode="+transOrderInfo.getBankCode()+",errorcode="+transOrderInfo.getErrorCode()
					+",errormsg="+transOrderInfo.getErrorMsg()+",useripaddress="+transOrderInfo.getUserIpAddress());
			// TODO 充值
			ErrorResponse response=new ErrorResponse();
			String reCode = "C0";
			String reMsg = "成功";
			String[] productIdStrings={productId};
			//校验交易码
			if (!TransCodeConst.CHARGE.equals(transOrderInfo.getFuncCode())){
				logger.info("充值交易码错误！订单中的交易码为"+transOrderInfo.getFuncCode());
				return "充值交易码错误！";
			}
			//校验订单号是否存在
			if(!this.orderNoChk(transOrderInfo.getOrderNo(),transOrderInfo.getMerchantCode())){
				logger.info("订单号："+transOrderInfo.getOrderNo()+"在机构号："+transOrderInfo.getMerchantCode()+"中已存在");
				return "该交易订单号已存在，请确认！";
			}
			//获取每个账户记账流水
			List<FinanaceEntry> finanaceEntries=new ArrayList<FinanaceEntry>();
			//获取所有账户记账流水
			List<FinanaceEntry> finanaceEntrielist=new ArrayList<FinanaceEntry>();
			User user=new User();
			user.userId=userId;
			user.constId=transOrderInfo.getMerchantCode();
			user.productId=productId;
			//判断账户状态是否正常
			boolean accountIsOK=operationService.checkAccount(user);
			if(!accountIsOK){
				logger.error("用户"+userId+"状态为非正常状态");
				reCode = "C1";
				reMsg = "账户状态非正常状态";
			}else{
				//判断订单信息是否有误
				transOrderInfo.setProductIdd(productId);
				String msg=checkInfoService.checkTradeInfo(transOrderInfo);
				if(!"ok".equals(msg)){
					logger.error(msg);
					reCode = "C2";
					reMsg = msg;
				}else{
					try {
						//获取套录号
						String entryId=redisIdGenerator.createRequestNo();
						long amount=transOrderInfo.getAmount();
						for (int i = 0; i <= 0; i++) {
							boolean flag=true;
							if(0==i){
								userId=transOrderInfo.getUserId();	
								flag=true;
							}else {
								userId=TransCodeConst.THIRDPARTYID_FNZZH;								
								flag=false;
							}
							user.userId=userId;
							Balance balance=null;
							if(flag){
								balance=checkInfoService.getBalance(user,"");
							}else{
								balance=checkInfoService.getBalance(user,userId);
							}
							//**********************账户充值************************
							if(null!=balance){
								balance.setPulseDegree(balance.getPulseDegree()+1);
								finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, balance, entryId, flag);
								if(null!=finanaceEntries&&0<finanaceEntries.size()){
									for(FinanaceEntry finanaceEntry:finanaceEntries) {
										finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
										finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
										if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
											finanaceEntry.setRemark("充值");
										}
										finanaceEntrielist.add(finanaceEntry);
									}								
								}else{
									logger.error("获取用户["+userId+"]账户流水信息失败");
									reCode = "C1";
									reMsg = "账户流水数据入库失败";
									break;
								}
								//***********************收益户添加手续费********************************
								balance=checkInfoService.getBalance(new User(), TransCodeConst.THIRDPARTYID_TXYWSYH);
								if(null!=balance){
									balance.setPulseDegree(balance.getPulseDegree()+1);
									transOrderInfo.setAmount(transOrderInfo.getUserFee());
									finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, balance, entryId, flag);
									if(null!=finanaceEntries&&0<finanaceEntries.size()){
										for(FinanaceEntry finanaceEntry:finanaceEntries) {
											finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
											finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
											if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
												finanaceEntry.setRemark("充值-手续费");
											}
											finanaceEntrielist.add(finanaceEntry);
										}								
									}else{
										logger.error("获取用户["+userId+"]账户流水信息失败");
										reCode = "C1";
										reMsg = "账户流水数据入库失败";
										break;
									}
								}else{
									logger.error("获取用户["+userId+"]余额信息失败");
									reCode = "C1";
									reMsg = "用户余额信息查询失败";
									break;
								}		
							}else{
								logger.error("获取用户["+userId+"]余额信息失败");
								reCode = "C1";
								reMsg = "用户余额信息查询失败";
								break;
							}							
						}
						transOrderInfo.setAmount(amount);
						//批量插入记录流水表   写入数据失败尝试三次
						if(reCode.equals("C0")){
							try {
								insertFinanaceEntry(finanaceEntrielist, transOrderInfo, productIdStrings);
							} catch (AccountException e) {
								logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败一次");
								try {
									insertFinanaceEntry(finanaceEntrielist, transOrderInfo, productIdStrings);
								} catch (AccountException e1) {
									logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败两次");
									try {
										insertFinanaceEntry(finanaceEntrielist, transOrderInfo, productIdStrings);
									} catch (AccountException e2) {
										//发送邮件或短信通知管理员
										logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败三次");
										//启用线程 发送邮件
										RkylinMailUtil.sendMailThread("账户记账流水失败通知","******************账户"+transOrderInfo.getUserId()+"做充值记账流水数据入库连续失败三次", TransCodeConst.FINANACE_ENTRY_ERROR_TOEMAIL);
										logger.error(e2.getMessage());
										reCode = "C3";
										reMsg = "数据库操作异常";
									}
								}
							}
						}						
					} catch (AccountException e) {
						logger.error(e.getMessage());
						reCode = "C3";
						reMsg = "数据库操作异常";
					}	
				}
			}
			logger.info("---------------------------通讯运维账户UserId"+transOrderInfo.getUserId()+"充值操作结束------------------------");
			if(reCode.equals("C0")){
				return "ok";
			}else{
				response.setCode(reCode);
				response.setMsg(reMsg);
				return reMsg;
			}
		}
	}

	/**
	 * 指尖代言提现
	 * Discription:
	 * @param transOrderInfo
	 * @param orderAuxiliary
	 * @return String
	 * @author Achilles
	 * @since 2016年7月13日
	 */
    private String withdrowZJDY(TransOrderInfo transOrderInfo,OrderAuxiliary orderAuxiliary) {
        // TODO 指尖提现-----涉及手续费
        synchronized (lock) {
            logger.info("---------------------------账户UserId"+transOrderInfo.getUserId()+"指尖提现操作开始------------------------");
            logger.info("提现操作参数信息：amount="+transOrderInfo.getAmount()+",UserId="+transOrderInfo.getUserId()+",funccode="+transOrderInfo.getFuncCode()
                    +",intermerchantcode="+transOrderInfo.getInterMerchantCode()+",merchantcode="+transOrderInfo.getMerchantCode()+",orderamount="+transOrderInfo.getOrderAmount()
                    +",ordercount="+transOrderInfo.getOrderCount()+",orderdate="+transOrderInfo.getOrderDate()+",orderno="+transOrderInfo.getOrderNo()
                    +",orderpackageno="+transOrderInfo.getOrderPackageNo()+",paychannelid="+transOrderInfo.getPayChannelId()+",remark="+transOrderInfo.getRemark()
                    +",productid="+orderAuxiliary.getProductQAA()+",requestno="+transOrderInfo.getRequestNo()+",requesttime="+transOrderInfo.getRequestTime()+",status="+transOrderInfo.getStatus()
                    +",tradeflowno="+transOrderInfo.getTradeFlowNo()+",userfee="+transOrderInfo.getUserFee()+",feeamount="+transOrderInfo.getFeeAmount()
                    +",profit="+transOrderInfo.getProfit()+",busitypeid="+transOrderInfo.getBusiTypeId()+",bankcode="+transOrderInfo.getBankCode()+",errorcode="+transOrderInfo.getErrorCode()
                    +",errormsg="+transOrderInfo.getErrorMsg()+",useripaddress="+transOrderInfo.getUserIpAddress());
            String isOk ="ok";
            String[] productIdStrings={orderAuxiliary.getProductQAA()};
            //校验交易码
            if (!TransCodeConst.WITHDROW.equals(transOrderInfo.getFuncCode())){
                logger.info("提现交易码错误！订单中的交易码为"+transOrderInfo.getFuncCode());
                return "提现交易码错误";
            }
            //校验订单号是否存在
            if(!this.orderNoChk(transOrderInfo.getOrderNo(),transOrderInfo.getMerchantCode())){
                logger.info("订单号："+transOrderInfo.getOrderNo()+"在机构号："+transOrderInfo.getMerchantCode()+"中已存在");
                return "该交易订单号已存在，请确认！";
            }
            //获取每个账户记账流水
            List<FinanaceEntry> finanaceEntries=new ArrayList<FinanaceEntry>();
            //获取所有账户记账流水
            List<FinanaceEntry> finanaceEntrieAlls=new ArrayList<FinanaceEntry>();
            User user=new User();
            user.userId=transOrderInfo.getUserId();
            user.constId=transOrderInfo.getMerchantCode();
            user.productId=orderAuxiliary.getProductQAA();
            //判断账户状态是否正常
            boolean accountIsOK=operationService.checkAccount(user);
            if(!accountIsOK){
                logger.error("用户"+transOrderInfo.getUserId()+"状态为非正常状态");
                return "账户"+transOrderInfo.getUserId()+"状态非正常状态,请确认入参是否正确！";
            }
            //判断订单信息是否有误
            String msg=checkInfoService.checkTradeInfo(transOrderInfo);
            if(!"ok".equals(msg)){
                logger.error(msg);
                return msg;
            }
            try {
                //获取套录号
                String entryId=redisIdGenerator.createRequestNo();
                Balance balance=null;
                //账户提现到账金额
                long amount=transOrderInfo.getAmount();
                //记录原交易码
                String funcCode_old=transOrderInfo.getFuncCode();
                //主账户余额减            
                //#########################主账户减############################
                String userId=transOrderInfo.getUserId();
                user.userId=userId;
                balance=checkInfoService.getBalance(user,"");
                if(null==balance){
                    logger.error("获取用户["+userId+"]余额信息失败");
                    return "用户余额查询失败"; 
                }
                balance.setPulseDegree(balance.getPulseDegree()+1);
                finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, balance, entryId, true);
                if(null!=finanaceEntries&&finanaceEntries.size()>0){
                    for(FinanaceEntry finanaceEntry:finanaceEntries) {
                        finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
                        finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
                        if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
                            finanaceEntry.setRemark("提现");
                        }
                        finanaceEntrieAlls.add(finanaceEntry);
                    }
                }else{
                    logger.error("获取用户["+userId+"]账户流水信息失败");
                    isOk = "账户流水数据入库失败";
                }  
                //#########################主账户余额减      收益账户加############################
                if (transOrderInfo.getUserFee()!=0l) {
                    Balance balanceB=new Balance(); 
                    balanceB=checkInfoService.getBalance(new User(),"TIM14623543516773642");   
                    transOrderInfo.setAmount(transOrderInfo.getUserFee());
                    transOrderInfo.setFuncCode(TransCodeConst.ADJUST_ACCOUNT_AMOUNT);
                    if(null==balanceB){
                        logger.error("获取账户余额信息失败！userId:"+user.userId+";产品号："+user.productId);
                        return "获取账户余额信息失败！";  
                    }
                    balance.setPulseDegree(balance.getPulseDegree()+1);
                    finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, balance,balanceB, entryId, false);
                    if(null!=finanaceEntries&&finanaceEntries.size()>0){
                        for(FinanaceEntry finanaceEntry:finanaceEntries) {
                            finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);    
                            finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
                            if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
                                finanaceEntry.setRemark("提现手续费");
                            }
                            if(finanaceEntry.getPaymentAmount()>0){
                                finanaceEntrieAlls.add(finanaceEntry);
                            }
                        }
                    }                   
                }
                if(isOk.equals("ok")){
                    transOrderInfo.setAmount(amount);
                    transOrderInfo.setFuncCode(funcCode_old);
                    try {
                        insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo, productIdStrings);
                    } catch (AccountException e) {
                        e.printStackTrace();
                        logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败");
                        isOk = "数据库操作异常";
                    }
                }
            } catch (AccountException e) {
                logger.error(e.getMessage());
                isOk = "数据库操作异常";
            }
        logger.info("---------------------------账户UserId"+transOrderInfo.getUserId()+"指尖提现操作结束------------------------");
        return isOk;
        }       
    }	
	
	@Override
	public String withdrowTXYW(TransOrderInfo transOrderInfo,OrderAuxiliary orderAuxiliary) {
		// TODO 通讯运维提现-----涉及手续费
		synchronized (lock) {
			logger.info("---------------------------账户UserId"+transOrderInfo.getUserId()+"通讯运维提现操作开始------------------------");
			logger.info("提现操作参数信息：amount="+transOrderInfo.getAmount()+",UserId="+transOrderInfo.getUserId()+",funccode="+transOrderInfo.getFuncCode()
					+",intermerchantcode="+transOrderInfo.getInterMerchantCode()+",merchantcode="+transOrderInfo.getMerchantCode()+",orderamount="+transOrderInfo.getOrderAmount()
					+",ordercount="+transOrderInfo.getOrderCount()+",orderdate="+transOrderInfo.getOrderDate()+",orderno="+transOrderInfo.getOrderNo()
					+",orderpackageno="+transOrderInfo.getOrderPackageNo()+",paychannelid="+transOrderInfo.getPayChannelId()+",remark="+transOrderInfo.getRemark()
					+",productid="+orderAuxiliary.getProductQAA()+",requestno="+transOrderInfo.getRequestNo()+",requesttime="+transOrderInfo.getRequestTime()+",status="+transOrderInfo.getStatus()
					+",tradeflowno="+transOrderInfo.getTradeFlowNo()+",userfee="+transOrderInfo.getUserFee()+",feeamount="+transOrderInfo.getFeeAmount()
					+",profit="+transOrderInfo.getProfit()+",busitypeid="+transOrderInfo.getBusiTypeId()+",bankcode="+transOrderInfo.getBankCode()+",errorcode="+transOrderInfo.getErrorCode()
					+",errormsg="+transOrderInfo.getErrorMsg()+",useripaddress="+transOrderInfo.getUserIpAddress());
			String isOk ="ok";
			String[] productIdStrings={orderAuxiliary.getProductQAA()};
			//校验交易码
			if (!TransCodeConst.WITHDROW.equals(transOrderInfo.getFuncCode())){
				logger.info("提现交易码错误！订单中的交易码为"+transOrderInfo.getFuncCode());
				return "提现交易码错误";
			}
			//校验订单号是否存在
			if(!this.orderNoChk(transOrderInfo.getOrderNo(),transOrderInfo.getMerchantCode())){
				logger.info("订单号："+transOrderInfo.getOrderNo()+"在机构号："+transOrderInfo.getMerchantCode()+"中已存在");
				return "该交易订单号已存在，请确认！";
			}
			//获取每个账户记账流水
			List<FinanaceEntry> finanaceEntries=new ArrayList<FinanaceEntry>();
			//获取所有账户记账流水
			List<FinanaceEntry> finanaceEntrieAlls=new ArrayList<FinanaceEntry>();
			User user=new User();
			user.userId=transOrderInfo.getUserId();
			user.constId=transOrderInfo.getMerchantCode();
			user.productId=orderAuxiliary.getProductQAA();
			//判断账户状态是否正常
			boolean accountIsOK=operationService.checkAccount(user);
			if(!accountIsOK){
				logger.error("用户"+transOrderInfo.getUserId()+"状态为非正常状态");
				return "账户"+transOrderInfo.getUserId()+"状态非正常状态,请确认入参是否正确！";
			}
		//判断订单信息是否有误
		String msg=checkInfoService.checkTradeInfo(transOrderInfo);
		if(!"ok".equals(msg)){
			logger.error(msg);
			return msg;
		}
		try {
			//获取套录号
			String entryId=redisIdGenerator.createRequestNo();
			Balance balance=null;
			//账户提现到账金额
			long amount=transOrderInfo.getAmount();
			//记录原交易码
			String funcCode_old=transOrderInfo.getFuncCode();
			//主账户余额减			
			//#########################主账户减############################
			for (int i = 0; i <= 0; i++) {
				boolean flag=true;
				String userId=null;
				if(0==i){
					userId=transOrderInfo.getUserId();	
					flag=true;
				}else{
					userId=TransCodeConst.THIRDPARTYID_TXDQSZH;
					flag=false;
					
				}
				user.userId=userId;
				
				if(flag){
					balance=checkInfoService.getBalance(user,"");
				}else{
					balance=checkInfoService.getBalance(user,userId);
				}				
				
				if(null!=balance){
					balance.setPulseDegree(balance.getPulseDegree()+1);
					finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, balance, entryId, flag);
					if(null!=finanaceEntries&&finanaceEntries.size()>0){
						for(FinanaceEntry finanaceEntry:finanaceEntries) {
							finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
							finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
							if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
								finanaceEntry.setRemark("提现");
							}
							finanaceEntrieAlls.add(finanaceEntry);
						}
					}else{
						logger.error("获取用户["+userId+"]账户流水信息失败");
						isOk = "账户流水数据入库失败";
						break;
					}							
				}else{
					logger.error("获取用户["+userId+"]余额信息失败");
					isOk = "用户余额查询失败";
				}
			}			
			//#########################主账户余额减      收益账户加############################
			Balance balanceB=new Balance();	
			balanceB=checkInfoService.getBalance(new User(),TransCodeConst.THIRDPARTYID_TXYWSYH);	
			transOrderInfo.setAmount(transOrderInfo.getUserFee());
			transOrderInfo.setFuncCode(TransCodeConst.ADJUST_ACCOUNT_AMOUNT);
			if(null!=balanceB){
				balance.setPulseDegree(balance.getPulseDegree()+1);
				finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, balance,balanceB, entryId, false);
				if(null!=finanaceEntries&&finanaceEntries.size()>0){
					for(FinanaceEntry finanaceEntry:finanaceEntries) {
						finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);	
						finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
						if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
							finanaceEntry.setRemark("提现手续费");
						}
						if(finanaceEntry.getPaymentAmount()>0){
							finanaceEntrieAlls.add(finanaceEntry);
						}
					}
				}
			}else{
				logger.error("获取账户余额信息失败！userId:"+user.userId+";产品号："+user.productId);
				return "获取账户余额信息失败！";
			}
			
			
			if(isOk.equals("ok")){
				transOrderInfo.setAmount(amount);
				transOrderInfo.setFuncCode(funcCode_old);
				try {
					insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo, productIdStrings);
				} catch (AccountException e) {
					logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败一次");
					try {
						insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo, productIdStrings);
					} catch (AccountException e1) {
						logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败两次");
						try {
							insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo, productIdStrings);
						} catch (AccountException e2) {
							//发送邮件或短信通知管理员
							logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败三次");
							//启用线程 发送邮件
							RkylinMailUtil.sendMailThread("账户记账流水失败通知","******************账户"+transOrderInfo.getUserId()+"做提现记账流水数据入库连续失败三次", TransCodeConst.FINANACE_ENTRY_ERROR_TOEMAIL);
							logger.error(e2.getMessage());
							isOk = "数据库操作异常";
						}
						
					}
				}
			}
			//***************通知代收付现改为日结线程定时汇总批量处理用户提现数据*************
			} catch (AccountException e) {
				logger.error(e.getMessage());
				isOk = "数据库操作异常";
			}
			logger.info("---------------------------账户UserId"+transOrderInfo.getUserId()+"通讯运维提现操作结束------------------------");
			return isOk;
		}		
	}

    @Override
    public ErrorResponse redPackageExchangeForDubbo(TransOrderInfo transOrderInfo, String productId,
            String intoProductId) {
        ErrorResponse response = new ErrorResponse();
        String reCode = "P0";
        String reMsg = "";
        
        if(null == transOrderInfo.getInterMerchantCode() || "".equals(transOrderInfo.getInterMerchantCode())){
            reCode = "P1";
            reMsg = "intermerchantcode不能为空";
        }else if(transOrderInfo.getAmount() == null || transOrderInfo.getAmount() <= 0){
            reCode = "P1";
            reMsg = "amount不能为空且不能小于0";
        }else if(null == transOrderInfo.getUserId() || "".equals(transOrderInfo.getUserId())){
            reCode = "P1";
            reMsg = "userid不能为空";
        }else if(null == transOrderInfo.getFuncCode() || "".equals(transOrderInfo.getFuncCode())){
            reCode = "P1";
            reMsg = "funccode不能为空";
        }else if(null == transOrderInfo.getMerchantCode() || "".equals(transOrderInfo.getMerchantCode())){
            reCode = "P1";
            reMsg = "merchantcode不能为空";
        }else if(null == transOrderInfo.getOrderCount() || transOrderInfo.getOrderCount() <= 0){
            reCode = "P1";
            reMsg = "ordercount不能为空且不能小于0";
        }else if(null == transOrderInfo.getOrderDate()){
            reCode = "P1";
            reMsg = "orderdate不能为空";
        }else if(null == transOrderInfo.getOrderNo() || "".equals(transOrderInfo.getOrderNo())){
            reCode = "P1";
            reMsg = "orderno不能为空";
        }else if(null == transOrderInfo.getRequestTime()){
            reCode = "P1";
            reMsg = "requesttime不能为空";
        }else if(null == transOrderInfo.getStatus()){
            reCode = "P1";
            reMsg = "status不能为空";
        }else if(null == transOrderInfo.getUserFee() || transOrderInfo.getUserFee() < 0){
            reCode = "P1";
            reMsg = "userfee不能为空且不能小于0";
        }else if(null == productId || "".equals(productId)){
            reCode = "P1";
            reMsg = "productid不能为空";
        }else if(null == transOrderInfo.getUserIpAddress() || "".equals(transOrderInfo.getUserIpAddress())){
            reCode = "P1";
            reMsg = "useripaddress不能为空";
        }else if(null == intoProductId || "".equals(intoProductId)){
            reCode = "P1";
            reMsg = "intoproductid不能为空";
        }
        if(!"P0".equals(reCode)){
            response.setCode(reCode);
            response.setMsg(reMsg);
            return response;
        }
        OrderAuxiliary orderAuxiliary = new OrderAuxiliary();
        orderAuxiliary.setProductQAA(productId);
        orderAuxiliary.setProductQAB(intoProductId);
        //红包兑换
        //判断订单状态是否为失效状态 ，如为失效 则不记录分录信息
        if(TransCodeConst.TRANS_STATUS_INVALIDATION == transOrderInfo.getStatus()){
            response = this.saveTransorder(transOrderInfo, "红包兑换");
        }else{
            String result = this.redPackageExchange(transOrderInfo, orderAuxiliary);
            if("ok".equals(result)){
                response.setIs_success(true);
            }else{
                response.setCode("C1");
                response.setMsg(result);
                response.setIs_success(false);
            }
        }
        return response;
    }
    
    
    /**
     * 转账
     * Discription:
     * @param transOrderInfo
     * @return CommonResponse
     * @author Achilles
     * @since 2016年6月29日
     */
    public CommonResponse transferInCommon(Map<String, String[]> paramMap) {
        logger.info("转账传入参数:"+CommUtil.getMapKeyVal(paramMap));
        CommonResponse res = new CommonResponse();
        res.setCode(CodeEnum.FAILURE.getCode());
        if (paramMap==null||paramMap.size()==0) {
            res.setMsg("参数为空!");
            return res;
        }
        Response response = doJob(paramMap, "ruixue.wheatfield.user.transfer.common");
        if (response instanceof ErrorResponse) {
            ErrorResponse errorResponse = (ErrorResponse) response;
            res.setMsg(errorResponse.getMsg());
            return res;
        }
        return new CommonResponse();
    }
     
    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public BatchResponse collectionBatch(List<com.rkylin.wheatfield.bean.TransOrderInfo> transOrderList) {
      logger.info("代收传入参数:"+BeanUtil.getBeanListVal(transOrderList, null));
      BatchResponse res = new BatchResponse();
      res.setCode(CodeEnum.FAILURE.getCode());
      if (transOrderList==null||transOrderList.size()==0) {
          res.setMsg("参数为空!");
          return res;
      }
      String countLimit = parameterInfoService.getParameterValByParameterCode("BATCH_LIMIT");
      if (countLimit==null) {
          logger.info("参数表没有设置上限条数!");
          res.setMsg("系统异常,请重试!");
          return res;
      }
      if (transOrderList.size()>Integer.parseInt(countLimit)) {
          logger.info("代收超过上限条数!");
          res.setMsg("代收超过上限条数!");
          return res;
      }
      com.rkylin.wheatfield.pojo.TransOrderInfo transOrderInfoo = null;
      List<com.rkylin.wheatfield.pojo.TransOrderInfo> transOrderInfoList = new ArrayList<com.rkylin.wheatfield.pojo.TransOrderInfo>();
      Set<String> orderNoUniqSet = new HashSet<String>();
      Set<String> orderPackageNoSet = new HashSet<String>();
      CommonResponse commonResponse = null;
      int i = 0;
      for(com.rkylin.wheatfield.bean.TransOrderInfo transOrder:transOrderList){
          if (TransCodeConst.NO_TALLY.equals(transOrder.getRemark())) {
              i++;
          }
          transOrderInfoo = new com.rkylin.wheatfield.pojo.TransOrderInfo();
          BeanMapper.copy(transOrder, transOrderInfoo);
          transOrderInfoo.setProductIdd(transOrder.getProductId());
          orderPackageNoSet.add(transOrder.getOrderPackageNo());
          commonResponse = verifyDataCollection(transOrderInfoo);
          if (transOrder.getOrderPackageNo()==null||"".equals(transOrder.getOrderPackageNo())) {
              logger.info("代收  参数异常  订单包号不能为空  orderNo="+transOrder.getOrderNo()+",merchantCode="+transOrder.getMerchantCode());
              res.setMsg("订单包号不能为空(orderNo="+transOrder.getOrderNo()+")");
              return res;
          }
          if (transOrder.getOrderPackageNo().length()>20) {
              logger.info("代收  参数异常  订单包号不能超过20位  getOrderPackageNo="+transOrder.getOrderPackageNo()+",merchantCode="+transOrder.getMerchantCode());
              res.setMsg("订单包号不能超过20位(getOrderPackageNo="+transOrder.getOrderPackageNo()+")");
              return res;
          }          
          if (transOrder.getAmount()<=0l) {
              logger.info("代收  参数异常  金额必须大于0   orderNo="+transOrder.getOrderNo()+",merchantCode="+transOrder.getMerchantCode());
              res.setMsg("金额必须大于0(orderNo="+transOrder.getOrderNo()+")");
              return res;
          }
          if (!CodeEnum.SUCCESS.getCode().equals(commonResponse.getCode())) {
              logger.info("代收  参数异常   orderNo="+transOrder.getOrderNo()+",merchantCode="+transOrder.getMerchantCode()+",msg="+commonResponse.getMsg());
              res.setMsg(commonResponse.getMsg()+"(orderNo="+transOrder.getOrderNo()+")");
              return res;
          }
          if (orderNoUniqSet.contains(transOrder.getMerchantCode()+transOrder.getOrderNo())) {
              logger.info("代收  参数集存在订单号重复   orderNo="+transOrder.getOrderNo()+",merchantCode="+transOrder.getMerchantCode());
              res.setMsg("参数集存在订单号重复"+"(orderNo="+transOrder.getOrderNo()+")");
              return res;
          }
          orderNoUniqSet.add(transOrder.getMerchantCode()+transOrder.getOrderNo());
          transOrderInfoList.add(transOrderInfoo);
      }
      if (orderPackageNoSet.size()!=1) {
          logger.info("批量代收   同一批只能有一个订单包号");
          res.setMsg("同一批只能有一个订单包号");
          return res;
      }
      boolean noAccount = false;
      if (i==transOrderList.size()) {
          noAccount = true;
      }else if (i>0 && i<transOrderList.size()) {
          logger.info("是否记账remark标示异常!");
          res.setMsg("是否记账remark标示异常!");
          return res;
      }
      Long sumAmount = 0l;
      Date accountDate = DateUtils.getDate(generationPaymentService.getAccountDate(),Constants.DATE_FORMAT_YYYYMMDD);
      List<com.rkylin.wheatfield.pojo.TransOrderInfo> transOrderInfooList = new ArrayList<com.rkylin.wheatfield.pojo.TransOrderInfo>();
      for(com.rkylin.wheatfield.pojo.TransOrderInfo transOrderInfo:transOrderInfoList){
          transOrderInfo.setStatus(1);
          transOrderInfo.setAccountDate(accountDate);
          //校验订单号是否存在
          if(!this.orderNoChk(transOrderInfo.getOrderNo(),transOrderInfo.getMerchantCode())){
              logger.info("批量代收  订单号(与数据库)重复,OrderNo="+transOrderInfo.getOrderNo()
                      +",MerchantCode="+transOrderInfo.getMerchantCode());
              res.setMsg("订单号重复,OrderNo="+transOrderInfo.getOrderNo());
              return res;
          }         
          sumAmount += transOrderInfo.getOrderAmount();
          transOrderInfooList.add(transOrderInfo);
      }
      transOrderInfoo = transOrderInfoList.get(0);
      Long amount = transOrderInfoo.getAmount();
      transOrderInfoo.setAmount(sumAmount);
      FinanaceEntryResponse finanaceEntryResponse = getFinanaceEntry(transOrderInfoo);
      if (!CodeEnum.SUCCESS.getCode().equals(finanaceEntryResponse.getCode())) {
          logger.info("批量代收生成流水异常:"+finanaceEntryResponse.getMsg());
          res.setMsg("生成流水失败");
          return res;
      }
      transOrderInfoo.setAmount(amount);
      transOrderInfoDao.insertSelectiveBatch(transOrderInfooList); 
      if (!noAccount) {
          finanaceEntryManager.saveFinanaceEntryList(finanaceEntryResponse.getEntryList());  
      }
      res.setCode(CodeEnum.SUCCESS.getCode());
      return res;
    }

    private FinanaceEntryResponse getFinanaceEntry(com.rkylin.wheatfield.pojo.TransOrderInfo transOrderInfo){
        FinanaceEntryResponse res = new FinanaceEntryResponse();
        res.setCode(CodeEnum.FAILURE.getCode());
        //获取备付金流水
        User user=new User();
        String userId=TransCodeConst.THIRDPARTYID_FNZZH;//丰年备付金账户
        user.userId=userId;
        Balance balance=checkInfoService.getBalance(user,userId);
        if(balance==null){
            res.setMsg("获取用户["+userId+"]余额信息失败");
            return res;  
        }
        balance.setPulseDegree(balance.getPulseDegree()+1);
        String requestNo = redisIdGenerator.createRequestNo();
        List<FinanaceEntry> finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, balance, requestNo,true);
        if(finanaceEntries.size()==0){
            res.setMsg("获取用户["+userId+"]账户流水信息失败");
            return res;   
        }
        FinanaceEntry finanaceEntryFN = finanaceEntries.get(0); 
        finanaceEntryFN.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
        finanaceEntryFN.setReferId(transOrderInfo.getOrderPackageNo());
        if(null==finanaceEntryFN.getRemark()||"".equals(finanaceEntryFN.getRemark())){
            finanaceEntryFN.setRemark("批量代收");
        }
        //获取其他应收流水
        userId=TransCodeConst.THIRDPARTYID_QTYSKZH;
        user.userId=userId;
        balance=checkInfoService.getBalance(user,userId);
        if(balance==null){
            res.setMsg("获取用户["+userId+"]余额信息失败");
            return res;  
        }
        balance.setPulseDegree(balance.getPulseDegree()+1);
        finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, balance,requestNo,true);
        if(finanaceEntries.size()==0){
            res.setMsg("获取用户["+userId+"]账户流水信息失败");
            return res;   
        }
        FinanaceEntry finanaceEntryQT = finanaceEntries.get(0); 
        finanaceEntryQT.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
        finanaceEntryQT.setReferId(transOrderInfo.getOrderPackageNo());
        if(finanaceEntryQT.getRemark()==null||"".equals(finanaceEntryQT.getRemark())){
            finanaceEntryQT.setRemark("批量代收");
        }
        finanaceEntries = new ArrayList<FinanaceEntry>();
        finanaceEntries.add(finanaceEntryFN);
        finanaceEntries.add(finanaceEntryQT);
        res.setEntryList(finanaceEntries);
        res.setCode(CodeEnum.SUCCESS.getCode());
        return res;
    } 

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public BatchResponse transferBatch(BatchTransferOrderInfo batchOrderInfo) {
        synchronized (lock) {
        logger.info("批量转账   转入方   参数:"+BeanUtil.getBeanVal(batchOrderInfo, null));
        BatchResponse res = new BatchResponse();
        res.setCode(CodeEnum.FAILURE.getCode());
        if (batchOrderInfo==null) {
            res.setMsg("参数为空!");
            return res;
        }
        String field = BeanUtil.validateBeanProEmpty(batchOrderInfo, new String[] { "merchantCode", "intoProductId",
                "intoUserId","funcCode","orderPackageNo"});
        logger.info("转入方  传入的参数字段校验 是否有空值(field的值为是空值的字段,为null表示都不为空)  field=" + field);
        if (field != null) {
            res.setMsg(field + "不能为空");
            return res;
        }

        if (batchOrderInfo.getOrderPackageNo().length()>20) {
            logger.info("代收  参数异常  订单包号不能超过20位  OrderPackageNo="+batchOrderInfo.getOrderPackageNo()+",merchantCode="+batchOrderInfo.getMerchantCode());
            res.setMsg("订单包号不能超过20位(OrderPackageNo="+batchOrderInfo.getOrderPackageNo()+")");
            return res;
        } 
        if (!TransCodeConst.ADJUST_ACCOUNT_AMOUNT.equals(batchOrderInfo.getFuncCode()) && 
                !TransCodeConst.FUNCTION_TRANSFER.equals(batchOrderInfo.getFuncCode())) {
            res.setMsg("功能码错误!");
            return res;
        }
        List<com.rkylin.wheatfield.bean.OrderInfo> orderInfoList = batchOrderInfo.getOrderInfoList();
        if (orderInfoList==null||orderInfoList.size()==0) {
            res.setMsg("转出方参数为空!");
            return res;
        }
        String countLimit = parameterInfoService.getParameterValByParameterCode("BATCH_LIMIT");
        if (countLimit==null) {
            logger.info("参数表没有设置上限条数!");
            res.setMsg("系统异常,请重试!");
            return res;
        }
        if (orderInfoList.size()>Integer.parseInt(countLimit)) {
            logger.info("超过上限条数!");
            res.setMsg("超过上限条数!");
            return res;
        }
        logger.info("批量转账   转出方   参数:"+BeanUtil.getBeanListVal(orderInfoList, null));
        Set<String> orderNoUniqSet = new HashSet<String>();
        for (OrderInfo orderInfo : orderInfoList) {
            field = BeanUtil.validateBeanProEmpty(orderInfo,new String[]{ "amount","outProductId","outUserId","orderNo"});
            logger.info("转出方  传入的参数字段校验 是否有空值(field的值为是空值的字段,为null表示都不为空)  field=" + field);
            if (field != null) {
                logger.info("批量转账  非空字段为空  orderNo="+orderInfo.getOrderNo()+",merchantCode="+batchOrderInfo.getMerchantCode());
                res.setMsg(field + "不能为空");
                return res;
            }
            if (orderInfo.getAmount()<=0l) {
                logger.info("批量转账  金额必须大于0   orderNo="+orderInfo.getOrderNo()+",merchantCode="+batchOrderInfo.getMerchantCode());
                res.setMsg("金额必须大于0");
                return res;
            }
            if (orderNoUniqSet.contains(batchOrderInfo.getMerchantCode()+orderInfo.getOrderNo())) {
                logger.info("批量转账  参数集存在订单号重复   orderNo="+orderInfo.getOrderNo()+",merchantCode="+batchOrderInfo.getMerchantCode());
                res.setMsg("参数集存在订单号重复");
                return res;
            }
            orderNoUniqSet.add(batchOrderInfo.getMerchantCode()+orderInfo.getOrderNo());
        }
        Long sumAmount = 0l;
        Date accountDate = DateUtils.getDate(generationPaymentService.getAccountDate(),Constants.DATE_FORMAT_YYYYMMDD);
        List<com.rkylin.wheatfield.pojo.TransOrderInfo> transOrderInfooList = new ArrayList<com.rkylin.wheatfield.pojo.TransOrderInfo>();
        TransOrderInfo transOrderInfo = null;
        Date now =new Date();
        com.rkylin.wheatfield.bean.User user = null;
        List<com.rkylin.wheatfield.bean.User> userList = new ArrayList<com.rkylin.wheatfield.bean.User>();
        for (OrderInfo orderInfo : orderInfoList) {
            transOrderInfo = new TransOrderInfo();
            transOrderInfo.setRequestTime(now);
            transOrderInfo.setRequestNo(orderInfo.getRequestNo());
            transOrderInfo.setOrderPackageNo(batchOrderInfo.getOrderPackageNo());
            transOrderInfo.setOrderNo(orderInfo.getOrderNo());
            transOrderInfo.setOrderDate(now);
            transOrderInfo.setOrderAmount(orderInfo.getAmount());
            transOrderInfo.setOrderCount(1);
            transOrderInfo.setFuncCode(batchOrderInfo.getFuncCode());
            transOrderInfo.setInterMerchantCode(batchOrderInfo.getIntoUserId());
            transOrderInfo.setMerchantCode(batchOrderInfo.getMerchantCode());
            transOrderInfo.setUserId(orderInfo.getOutUserId());
            transOrderInfo.setAmount(orderInfo.getAmount());
            transOrderInfo.setUserFee(0l);
            transOrderInfo.setUserIpAddress(batchOrderInfo.getUserIpAddress());
            transOrderInfo.setErrorCode(orderInfo.getOutProductId());
            transOrderInfo.setErrorMsg(batchOrderInfo.getIntoProductId());
            transOrderInfo.setStatus(1);
            transOrderInfo.setRemark(orderInfo.getRemark());
            transOrderInfo.setAccountDate(accountDate);
            //校验订单号是否存在
            if(!this.orderNoChk(orderInfo.getOrderNo(),batchOrderInfo.getMerchantCode())){
                logger.info("批量转账  订单号(与数据库)重复,OrderNo="+transOrderInfo.getOrderNo()
                    +",MerchantCode="+transOrderInfo.getMerchantCode());
                    res.setMsg("订单号重复,OrderNo="+transOrderInfo.getOrderNo());
                return res;
            }
            //判断订单信息是否有误
            String msg=checkInfoService.checkTradeInfo(transOrderInfo);
            if(!"ok".equals(msg)){
                logger.info("批量转账  msg="+msg+",OrderNo="+transOrderInfo.getOrderNo()
                +",MerchantCode="+transOrderInfo.getMerchantCode());
                res.setMsg(msg);
            return res;
            }          
            sumAmount += transOrderInfo.getOrderAmount();
            transOrderInfooList.add(transOrderInfo);
            user = new com.rkylin.wheatfield.bean.User();
            user.setUserId(orderInfo.getOutUserId());
            user.setInstCode(batchOrderInfo.getMerchantCode());
            user.setProductId(orderInfo.getOutProductId());
            user.setAmount(orderInfo.getAmount());
            user.setStatus(0);
            user.setFinAccountId(batchOrderInfo.getOrderPackageNo());
            userList.add(user);
        }
        user = new com.rkylin.wheatfield.bean.User();
        user.setUserId(batchOrderInfo.getIntoUserId());
        user.setInstCode(batchOrderInfo.getMerchantCode());
        user.setProductId(batchOrderInfo.getIntoProductId());
        user.setAmount(sumAmount);
        user.setStatus(1);
        user.setFinAccountId(batchOrderInfo.getOrderPackageNo());
        userList.add(user);
        CommonResponse commonResponse = semiAutomatizationServiceApi.operateFinAccounts(userList);
        if (!CodeEnum.SUCCESS.getCode().equals(commonResponse.getCode())) {
            logger.info("批量转账生成流水异常:"+commonResponse.getMsg());
            res.setMsg("生成流水失败!");
            return res;
        }
        transOrderInfoDao.insertSelectiveBatch(transOrderInfooList); 
        res.setCode(CodeEnum.SUCCESS.getCode());
        return res;
        }
    }
 
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public CommonResponse withdrowUserFee(TransOrderInfo transOrderInfo,OrderAuxiliary orderAuxiliary) {
        synchronized (lock) {
            logger.info("提现transOrderInfo参数："+BeanUtil.getBeanVal(transOrderInfo, null));
            logger.info("提现orderAuxiliary参数："+BeanUtil.getBeanVal(orderAuxiliary, null));
            String[] productIdStrings={orderAuxiliary.getProductQAA()};
            CommonResponse res = new CommonResponse();
            res.setCode(CodeEnum.FAILURE.getCode());
            //校验交易码
            if (!TransCodeConst.WITHDROW.equals(transOrderInfo.getFuncCode())){
                logger.info("提现交易码错误！订单中的交易码为"+transOrderInfo.getFuncCode());
                res.setMsg("提现交易码错误");
                return res;
            }
            //校验订单号是否存在
            if(!this.orderNoChk(transOrderInfo.getOrderNo(),transOrderInfo.getMerchantCode())){
                logger.info("订单号："+transOrderInfo.getOrderNo()+"在机构号："+transOrderInfo.getMerchantCode()+"中已存在");
                res.setMsg("该交易订单号已存在，请确认！");
                return res;
            }
            //获取每个账户记账流水
            List<FinanaceEntry> finanaceEntries=new ArrayList<FinanaceEntry>();
            //获取所有账户记账流水
            List<FinanaceEntry> finanaceEntrieAlls=new ArrayList<FinanaceEntry>();
            User user=new User();
            user.userId=transOrderInfo.getUserId();
            user.constId=transOrderInfo.getMerchantCode();
            user.productId=orderAuxiliary.getProductQAA();
            //判断账户状态是否正常
            boolean accountIsOK=operationService.checkAccount(user);
            if(!accountIsOK){
                logger.info("用户"+transOrderInfo.getUserId()+"状态为非正常状态");
                res.setMsg("账户状态非正常状态");
                return res;
            }
            //判断订单信息是否有误
            String msg=checkInfoService.checkTradeInfo(transOrderInfo);
            if(!"ok".equals(msg)){
                logger.info(msg);
                res.setMsg(msg);
                return res;
            }
            //获取套录号
            String entryId=redisIdGenerator.createRequestNo();
            Balance balance=null;
            //账户提现到账金额
            long amount=transOrderInfo.getAmount();
            //记录原交易码
            String funcCode_old=transOrderInfo.getFuncCode();
            //主账户余额减            
            //#########################主账户减############################
            boolean flag=true;
            String userId=null;
            userId=transOrderInfo.getUserId();  
            user.userId=userId;
            balance=checkInfoService.getBalance(user,"");
            if(balance==null){
                logger.info("获取用户["+userId+"]余额信息失败");
                res.setMsg("用户余额查询失败");
                return res;
            }
            balance.setPulseDegree(balance.getPulseDegree()+1);
            finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, balance, entryId, flag);
            if(finanaceEntries.size()==0){
                logger.info("获取用户["+userId+"]账户流水信息失败");
                res.setMsg("获取用户账户流水信息失败");
                return res;
            }
            finanaceEntries.get(0).setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
            finanaceEntries.get(0).setReferId(String.valueOf(transOrderInfo.getRequestId()));
            if(null==finanaceEntries.get(0).getRemark()||"".equals(finanaceEntries.get(0).getRemark())){
                finanaceEntries.get(0).setRemark("提现");
            }
            finanaceEntrieAlls.addAll(finanaceEntries);
            //#########################主账户余额减      收益账户加############################
            if (transOrderInfo.getUserFee()!=0l) {
                Balance balanceB=new Balance(); 
                balanceB=checkInfoService.getBalance(new User(),TransCodeConst.THIRDPARTYID_FCSYH);   
                transOrderInfo.setAmount(transOrderInfo.getUserFee());
                transOrderInfo.setFuncCode(TransCodeConst.ADJUST_ACCOUNT_AMOUNT);
                if(balanceB==null){
                    logger.error("获取账户余额信息失败！userId:"+user.userId+";产品号："+user.productId);
                    res.setMsg("获取账户余额信息失败！");
                    return res;
                }
                balance.setPulseDegree(balance.getPulseDegree()+1);
                finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, balance,balanceB, entryId, false);
                if(finanaceEntries.size()==0){
                    logger.error("获取账户余额信息失败！userId:"+user.userId+";产品号："+user.productId);
                    res.setMsg("生成流水失败！");
                    return res;
                }
                for(FinanaceEntry finanaceEntry:finanaceEntries) {
                    finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);    
                    finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
                    if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
                        finanaceEntry.setRemark("提现手续费");
                    }
                    if(finanaceEntry.getPaymentAmount()>0){
                        finanaceEntrieAlls.add(finanaceEntry);
                    }
                }                
            }
            transOrderInfo.setAmount(amount);
            transOrderInfo.setFuncCode(funcCode_old);
            insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo, productIdStrings);
            return new CommonResponse();
         }
      }

    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public CommonResponse rechargeUserFee(TransOrderInfo transOrderInfo) {
        synchronized (lock) {
            String productId = transOrderInfo.getProductIdd();
            String userId = transOrderInfo.getUserId();
            logger.info("充值参数："+BeanUtil.getBeanVal(transOrderInfo, null));
            CommonResponse res = new CommonResponse();
            res.setCode(CodeEnum.FAILURE.getCode());
            //校验交易码
            if (!TransCodeConst.CHARGE.equals(transOrderInfo.getFuncCode())){
                logger.info("充值交易码错误！订单中的交易码为"+transOrderInfo.getFuncCode());
                res.setMsg("充值交易码错误！");
                return res;
            }
            //校验订单号是否存在
            if(!this.orderNoChk(transOrderInfo.getOrderNo(),transOrderInfo.getMerchantCode())){
                logger.info("订单号："+transOrderInfo.getOrderNo()+"在机构号："+transOrderInfo.getMerchantCode()+"中已存在");
                res.setMsg("该交易订单号已存在，请确认！");
                return res;
            }
            //获取每个账户记账流水
            List<FinanaceEntry> finanaceEntries=new ArrayList<FinanaceEntry>();
            //获取所有账户记账流水
            List<FinanaceEntry> finanaceEntrielist=new ArrayList<FinanaceEntry>();
            User user=new User();
            user.userId=userId;
            user.constId=transOrderInfo.getMerchantCode();
            user.productId=productId;
            //判断账户状态是否正常
            boolean accountIsOK=operationService.checkAccount(user);
            if(!accountIsOK){
                logger.info("用户"+userId+"状态为非正常状态");
                res.setMsg("状态为非正常状态！");
                return res;
            }
            //判断订单信息是否有误
            String msg=checkInfoService.checkTradeInfo(transOrderInfo);
            if(!"ok".equals(msg)){
                logger.info(msg);
                res.setMsg(msg);
                return res;
            }
            String entryId=redisIdGenerator.createRequestNo();
            long amount=transOrderInfo.getAmount();
            boolean flag=true;
            userId=transOrderInfo.getUserId();  
            user.userId=userId;
            Balance balance=null;
            balance=checkInfoService.getBalance(user,"");
            //**********************账户充值************************
            if(balance==null){
                logger.info("获取用户["+userId+"]余额信息失败");
                res.setMsg("用户余额信息查询失败");
                return res;
            }
            balance.setPulseDegree(balance.getPulseDegree()+1);
            finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, balance, entryId, flag);
            if(finanaceEntries.size()==0){
                logger.info("获取用户["+userId+"]账户流水信息失败");
                res.setMsg("生成流水失败");
                return res;
            }
            for(FinanaceEntry finanaceEntry:finanaceEntries) {
                finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
                finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
                if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
                    finanaceEntry.setRemark("充值");
                }
                finanaceEntrielist.add(finanaceEntry);
            }   
            if (transOrderInfo.getUserFee()!=0l) {
                //***********************收益户添加手续费********************************
                balance=checkInfoService.getBalance(new User(), TransCodeConst.THIRDPARTYID_FCSYH);
                if(balance==null){
                    logger.info("获取用户["+userId+"]余额信息失败");
                    res.setMsg("用户余额信息查询失败");
                    return res;
                }
                balance.setPulseDegree(balance.getPulseDegree()+1);
                transOrderInfo.setAmount(transOrderInfo.getUserFee());
                finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, balance, entryId, flag);
                if(finanaceEntries.size()==0){
                    logger.info("获取用户["+userId+"]账户流水信息失败");
                    res.setMsg("获取账户流失败");
                    return res;
                }
                for(FinanaceEntry finanaceEntry:finanaceEntries) {
                    finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
                    finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
                    if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
                        finanaceEntry.setRemark("充值-手续费");
                    }
                    finanaceEntrielist.add(finanaceEntry);
                }                 
            }
            transOrderInfo.setAmount(amount);
            insertFinanaceEntry(finanaceEntrielist, transOrderInfo, new String[]{productId});
            }
            return new CommonResponse();
        }
    
        /**
         * 批量转账:支持一对多,多对一
         * Discription:
         * @param paramMap
         * @return Response
         * @author Achilles
         * @since 2016年8月29日
         */
        private Response transBatch(Map<String, String[]> paramMap){
          logger.info("批量转账  入参:"+CommUtil.getMapKeyVal(paramMap));  
          BatchTransferResponse response=new BatchTransferResponse();
          response.setReturnCode(CodeEnum.FAILURE.getCode());
          if (paramMap==null||paramMap.size()==0) {
              response.setReturnMsg("参数为空");
              return response;
          }
          String outUserInfo = CommUtil.getArrayOneVal(paramMap.get("outuserinfo"));
          String intoUserInfo = CommUtil.getArrayOneVal(paramMap.get("intouserinfo"));
          if (outUserInfo==null||intoUserInfo==null) {
              response.setReturnMsg("必输参数为空");
              return response;
          }
          JSONArray outJsonArray = null;
          try {
              outJsonArray = (JSONArray) JSON.parse(outUserInfo);
          } catch (com.alibaba.dubbo.common.json.ParseException e) {
            response.setReturnMsg("转出方参数格式异常");
            return response;
          }
          JSONArray inJsonArray = null;
          try {
              inJsonArray = (JSONArray) JSON.parse(intoUserInfo);
          } catch (com.alibaba.dubbo.common.json.ParseException e) {
            response.setReturnMsg("转入方参数格式异常");
            return response;
          } 
          if (outJsonArray.length()==0 || inJsonArray.length()==0) {
              response.setReturnMsg("转出转入方信息异常,请检查!");
              return response;
          }
          BatchTransfer batchTransfer = new BatchTransfer();
          batchTransfer.setMerchantCode(CommUtil.getArrayOneVal(paramMap.get("merchantcode")));
          batchTransfer.setOrderPackageNo(CommUtil.getArrayOneVal(paramMap.get("orderpackageno")));
          batchTransfer.setUserIpAddress(CommUtil.getArrayOneVal(paramMap.get("useripaddress")));
          List<com.rkylin.wheatfield.bean.TransferInfo> outTransferInfoList = new ArrayList<com.rkylin.wheatfield.bean.TransferInfo>();
          com.rkylin.wheatfield.bean.TransferInfo  transferInfo = null;
          for (int i = 0; i < outJsonArray.length(); i++) {
              transferInfo = new com.rkylin.wheatfield.bean.TransferInfo();
              JSONObject outJsonObject  = (JSONObject) outJsonArray.get(i);
              if (outJsonArray.length()>=1 && inJsonArray.length()==1 && StringUtils.valueOf(outJsonObject.get("amount"))!=null) {
                  try {
                      transferInfo.setAmount(Long.parseLong(StringUtils.valueOf(outJsonObject.get("amount"))));
                  } catch (Exception e) {
                      logger.error("付款方金额异常", e);
                      response.setReturnMsg("付款方金额异常,请检查");
                      return response;
                  }                 
              }
              transferInfo.setUserId(StringUtils.valueOf(outJsonObject.get("userid")));
              transferInfo.setProductId(StringUtils.valueOf(outJsonObject.get("productid")));
              transferInfo.setOrderNo(StringUtils.valueOf(outJsonObject.get("orderno")));
              transferInfo.setRequestNo(StringUtils.valueOf(outJsonObject.get("requestno")));
              transferInfo.setBusiTypeId(StringUtils.valueOf(outJsonObject.get("busitypeid")));
              transferInfo.setRemark(StringUtils.valueOf(outJsonObject.get("remark")));
              outTransferInfoList.add(transferInfo);
          } 
          batchTransfer.setOutTransferInfoList(outTransferInfoList);
          List<com.rkylin.wheatfield.bean.TransferInfo> intoTransferInfoList = new ArrayList<com.rkylin.wheatfield.bean.TransferInfo>();
          for (int i = 0; i < inJsonArray.length(); i++) {
              transferInfo = new com.rkylin.wheatfield.bean.TransferInfo();
              JSONObject inJsonObject  = (JSONObject) inJsonArray.get(i);
              if (inJsonArray.length()!=1 && StringUtils.valueOf(inJsonObject.get("amount"))!=null) {
                  try {
                      transferInfo.setAmount(Long.parseLong(StringUtils.valueOf(inJsonObject.get("amount"))));
                  } catch (Exception e) {
                      logger.error("收款方金额异常", e);
                      response.setReturnMsg("收款方金额异常,请检查");
                      return response;
                  } 
              }
              transferInfo.setUserId(StringUtils.valueOf(inJsonObject.get("userid")));
              transferInfo.setProductId(StringUtils.valueOf(inJsonObject.get("productid")));
              transferInfo.setOrderNo(StringUtils.valueOf(inJsonObject.get("orderno")));
              transferInfo.setRequestNo(StringUtils.valueOf(inJsonObject.get("requestno")));
              transferInfo.setBusiTypeId(StringUtils.valueOf(inJsonObject.get("busitypeid")));
              transferInfo.setRemark(StringUtils.valueOf(inJsonObject.get("remark")));
              intoTransferInfoList.add(transferInfo);
            }           
            batchTransfer.setIntoTransferInfoList(intoTransferInfoList);
            CommonResponse res = batchTransfer(batchTransfer);
            if (!CodeEnum.SUCCESS.getCode().equals(res.getCode())) {
                logger.info("批量转账  msg="+res.getMsg());
                response.setReturnMsg(res.getMsg());
                return response;
            }
            return new BatchTransferResponse();
        } 
        
        private CommonResponse batchTransfer(BatchTransfer batchTransfer){
            synchronized (lock) {
            CommonResponse res = new CommonResponse();
            res.setCode(CodeEnum.FAILURE.getCode());
            List<TransferInfo> outTransferInfoList = batchTransfer.getOutTransferInfoList();
            List<TransferInfo> intoTransferInfoList = batchTransfer.getIntoTransferInfoList();
            if(outTransferInfoList.size()>1 && intoTransferInfoList.size()>1){
                res.setMsg("批量转账仅支持一对一,多对一,一对多!");
                return res;
            }
            String field = BeanUtil.validateBeanProEmpty(batchTransfer, new String[] { "merchantCode", "orderPackageNo" });
            if (field != null) {
                res.setMsg(field + "不能为空");
                return res;
            }
            String outMulti = "0";
            if (outTransferInfoList.size()>1 || (outTransferInfoList.size()==1 && intoTransferInfoList.size()==1)) {
                outMulti = "1";
            }
            return multiTransfer(batchTransfer,outMulti);
            }
        }
        
        /**
         * 批量转账
         * Discription:
         * @param batchTransfer
         * @param outMulti
         * @return CommonResponse
         * @author Achilles
         * @since 2016年9月1日
         */
        private CommonResponse multiTransfer(BatchTransfer batchTransfer,String outMulti){
            CommonResponse res = new CommonResponse();
            res.setCode(CodeEnum.FAILURE.getCode());
            String countLimit = parameterInfoService.getParameterValByParameterCode("BATCH_LIMIT");
            if (countLimit==null) {
                logger.info("参数表没有设置上限条数!");
                res.setMsg("系统异常,请重试!");
                return res;
            }
            List<TransferInfo> transferInfoList = batchTransfer.getOutTransferInfoList();
            TransferInfo singleTransferInfo = batchTransfer.getIntoTransferInfoList().get(0);
            if (!"1".equals(outMulti)) {
                transferInfoList = batchTransfer.getIntoTransferInfoList();
                singleTransferInfo = batchTransfer.getOutTransferInfoList().get(0);
            }
            if (transferInfoList.size()>Integer.parseInt(countLimit)) {
                logger.info("超过上限条数!");
                res.setMsg("超过上限条数!");
                return res;
            }
            Set<String> orderNoUniqSet = new HashSet<String>();
            Set<String> finAccountUniqSet = new HashSet<String>();
            String field = null;
            for (TransferInfo orderInfo : transferInfoList) {
                field = BeanUtil.validateBeanProEmpty(orderInfo,new String[]{ "amount","productId","userId","orderNo"});
                if (field != null) {
                    logger.info("非空字段为空  orderNo="+orderInfo.getOrderNo()+",merchantCode="+batchTransfer.getMerchantCode());
                    res.setMsg(field + "不能为空");
                    return res;
                }
                if (orderInfo.getAmount()<=0l) {
                    logger.info("批量转账  金额必须大于0   orderNo="+orderInfo.getOrderNo()+",merchantCode="+batchTransfer.getMerchantCode());
                    res.setMsg("金额必须大于0");
                    return res;
                }
                if (orderNoUniqSet.contains(batchTransfer.getMerchantCode()+orderInfo.getOrderNo())) {
                    logger.info("批量转账  参数集存在订单号重复   orderNo="+orderInfo.getOrderNo()+",merchantCode="+batchTransfer.getMerchantCode());
                    res.setMsg("参数集存在订单号重复");
                    return res;
                }
                orderNoUniqSet.add(batchTransfer.getMerchantCode()+orderInfo.getOrderNo());
                if (finAccountUniqSet.contains(orderInfo.getUserId()+orderInfo.getProductId())) {
                    logger.info("批量转账  存在重复账户   orderNo="+orderInfo.getOrderNo()+
                            ",merchantCode="+batchTransfer.getMerchantCode()+",userId="+orderInfo.getUserId());
                    res.setMsg("相同账户不能有多条转账,请合并!");
                    return res;
                }
                finAccountUniqSet.add(orderInfo.getUserId()+orderInfo.getProductId());   
            }
            field = BeanUtil.validateBeanProEmpty(singleTransferInfo,new String[]{"productId","userId"});
            if (field != null) {
                logger.info("批量转账  非空字段为空  productId="+singleTransferInfo.getProductId()+",userId="+singleTransferInfo.getUserId());
                res.setMsg(field + "不能为空");
                return res;
            }
            Long sumAmount = 0l;
            Date accountDate = DateUtils.getDate(generationPaymentService.getAccountDate(),Constants.DATE_FORMAT_YYYYMMDD);
            List<com.rkylin.wheatfield.pojo.TransOrderInfo> transOrderInfooList = new ArrayList<com.rkylin.wheatfield.pojo.TransOrderInfo>();
            TransOrderInfo transOrderInfo = null;
            Date now =new Date();
            com.rkylin.wheatfield.bean.User user = null;
            List<com.rkylin.wheatfield.bean.User> userList = new ArrayList<com.rkylin.wheatfield.bean.User>();
            for (TransferInfo multiOrderInfo : transferInfoList) {
                transOrderInfo = new TransOrderInfo();
                transOrderInfo.setRequestTime(now);
                transOrderInfo.setRequestNo(multiOrderInfo.getRequestNo());
                transOrderInfo.setOrderPackageNo(batchTransfer.getOrderPackageNo());
                transOrderInfo.setOrderNo(multiOrderInfo.getOrderNo());
                transOrderInfo.setOrderDate(now);
                transOrderInfo.setOrderAmount(multiOrderInfo.getAmount());
                transOrderInfo.setOrderCount(1);
                transOrderInfo.setFuncCode(TransCodeConst.ADJUST_ACCOUNT_AMOUNT);
                transOrderInfo.setMerchantCode(batchTransfer.getMerchantCode());
                transOrderInfo.setAmount(multiOrderInfo.getAmount());
                transOrderInfo.setUserFee(0l);
                transOrderInfo.setUserIpAddress(batchTransfer.getUserIpAddress());
                transOrderInfo.setUserId(multiOrderInfo.getUserId());
                transOrderInfo.setInterMerchantCode(singleTransferInfo.getUserId());
                transOrderInfo.setErrorCode(multiOrderInfo.getProductId());
                transOrderInfo.setErrorMsg(singleTransferInfo.getProductId());
                transOrderInfo.setRemark(multiOrderInfo.getRemark());
                transOrderInfo.setBusiTypeId(multiOrderInfo.getBusiTypeId());
                if (!"1".equals(outMulti)) {
                    transOrderInfo.setUserId(singleTransferInfo.getUserId());
                    transOrderInfo.setInterMerchantCode(multiOrderInfo.getUserId());
                    transOrderInfo.setErrorCode(singleTransferInfo.getProductId()); 
                    transOrderInfo.setErrorMsg(multiOrderInfo.getProductId());
                }
                transOrderInfo.setStatus(1);
                transOrderInfo.setAccountDate(accountDate);
                //校验订单号是否存在
                if(!this.orderNoChk(transOrderInfo.getOrderNo(),batchTransfer.getMerchantCode())){
                    logger.info("批量转账  订单号(与数据库)重复,OrderNo="+transOrderInfo.getOrderNo()
                        +",MerchantCode="+transOrderInfo.getMerchantCode());
                        res.setMsg("订单号重复,OrderNo="+transOrderInfo.getOrderNo());
                    return res;
                }
                //判断订单信息是否有误
                String msg = null;
                try {
                    msg = checkInfoService.checkTradeInfo(transOrderInfo);
                } catch (Exception e) {
                    logger.error("账户不存在",e);
                    res.setMsg("账户不存在!");
                    return res;
                }
                if(!"ok".equals(msg)){
                    logger.info("批量转账  msg="+msg+",OrderNo="+transOrderInfo.getOrderNo()
                    +",MerchantCode="+transOrderInfo.getMerchantCode());
                    res.setMsg(msg);
                return res;
                }          
                sumAmount += transOrderInfo.getOrderAmount();
                transOrderInfooList.add(transOrderInfo);
                user = new com.rkylin.wheatfield.bean.User();
                user.setUserId(transOrderInfo.getUserId());
                user.setInstCode(batchTransfer.getMerchantCode());
                user.setProductId(transOrderInfo.getErrorCode());
                user.setAmount(transOrderInfo.getAmount());
                user.setStatus(0);
                if (!"1".equals(outMulti)) {
                    user.setUserId(transOrderInfo.getInterMerchantCode());
                    user.setProductId(transOrderInfo.getErrorMsg());
                    user.setStatus(1);
                }
                user.setFinAccountId(batchTransfer.getOrderPackageNo());
                userList.add(user);
            }
            user = new com.rkylin.wheatfield.bean.User();
            user.setUserId(singleTransferInfo.getUserId());
            user.setInstCode(batchTransfer.getMerchantCode());
            user.setProductId(singleTransferInfo.getProductId());
            user.setAmount(sumAmount);
            user.setStatus(1);
            if (!"1".equals(outMulti)) {
                user.setStatus(0);
            }
            user.setFinAccountId(batchTransfer.getOrderPackageNo());
            userList.add(user);
            CommonResponse commonResponse = semiAutomatizationServiceApi.operateFinAccounts(userList);
            if (!CodeEnum.SUCCESS.getCode().equals(commonResponse.getCode())) {
                logger.info("批量转账生成流水异常:"+commonResponse.getMsg());
                res.setMsg("生成流水失败!");
                return res;
            }
            transOrderInfoDao.insertSelectiveBatch(transOrderInfooList); 
            res.setCode(CodeEnum.SUCCESS.getCode());
            return res;
        }
        
        /**
         * 领客科技
         * @param transOrderInfo
         * @param orderAuxiliary
         * @return
         */
        @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
        public CommonResponse withdrowLingKe(TransOrderInfo transOrderInfo,OrderAuxiliary orderAuxiliary) {
            synchronized (lock) {
                logger.info("提现transOrderInfo参数："+BeanUtil.getBeanVal(transOrderInfo, null));
                logger.info("提现orderAuxiliary参数："+BeanUtil.getBeanVal(orderAuxiliary, null));
                String[] productIdStrings={orderAuxiliary.getProductQAA()};
                CommonResponse res = new CommonResponse();
                res.setCode(CodeEnum.FAILURE.getCode());
                //校验交易码
                if (!TransCodeConst.WITHDROW.equals(transOrderInfo.getFuncCode())){
                    logger.info("提现交易码错误！订单中的交易码为"+transOrderInfo.getFuncCode());
                    res.setMsg("提现交易码错误");
                    return res;
                }
                //校验订单号是否存在
                if(!this.orderNoChk(transOrderInfo.getOrderNo(),transOrderInfo.getMerchantCode())){
                    logger.info("订单号："+transOrderInfo.getOrderNo()+"在机构号："+transOrderInfo.getMerchantCode()+"中已存在");
                    res.setMsg("该交易订单号已存在，请确认！");
                    return res;
                }
                //获取每个账户记账流水
                List<FinanaceEntry> finanaceEntries=new ArrayList<FinanaceEntry>();
                //获取所有账户记账流水
                List<FinanaceEntry> finanaceEntrieAlls=new ArrayList<FinanaceEntry>();
                User user=new User();
                user.userId=transOrderInfo.getUserId();
                user.constId=transOrderInfo.getMerchantCode();
                user.productId=orderAuxiliary.getProductQAA();
                //判断账户状态是否正常
                boolean accountIsOK=operationService.checkAccount(user);
                if(!accountIsOK){
                    logger.info("用户"+transOrderInfo.getUserId()+"状态为非正常状态");
                    res.setMsg("账户状态非正常状态");
                    return res;
                }
                //判断订单信息是否有误
                String msg=checkInfoService.checkTradeInfo(transOrderInfo);
                if(!"ok".equals(msg)){
                    logger.info(msg);
                    res.setMsg(msg);
                    return res;
                }
                //获取套录号
                String entryId=redisIdGenerator.createRequestNo();
                Balance balance=null;
                //账户提现到账金额
                long amount=transOrderInfo.getAmount();
                //记录原交易码
                String funcCode_old=transOrderInfo.getFuncCode();
                //主账户余额减            
                //#########################主账户减############################
                boolean flag=true;
                String userId=null;
                userId=transOrderInfo.getUserId();  
                user.userId=userId;
                balance=checkInfoService.getBalance(user,"");
                if(balance==null){
                    logger.info("获取用户["+userId+"]余额信息失败");
                    res.setMsg("用户余额查询失败");
                    return res;
                }
                balance.setPulseDegree(balance.getPulseDegree()+1);
                finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, balance, entryId, flag);
                if(finanaceEntries.size()==0){
                    logger.info("获取用户["+userId+"]账户流水信息失败");
                    res.setMsg("获取用户账户流水信息失败");
                    return res;
                }
                finanaceEntries.get(0).setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
                finanaceEntries.get(0).setReferId(String.valueOf(transOrderInfo.getRequestId()));
                if(null==finanaceEntries.get(0).getRemark()||"".equals(finanaceEntries.get(0).getRemark())){
                    finanaceEntries.get(0).setRemark("提现");
                }
                finanaceEntrieAlls.addAll(finanaceEntries);
                //#########################主账户余额减      收益账户加############################
                if (transOrderInfo.getUserFee()!=0l) {
                    Balance balanceB=new Balance(); 
                    balanceB=checkInfoService.getBalance(new User(),TransCodeConst.THIRDPARTYID_LKSYH);   
                    transOrderInfo.setAmount(transOrderInfo.getUserFee());
                    transOrderInfo.setFuncCode(TransCodeConst.ADJUST_ACCOUNT_AMOUNT);
                    if(balanceB==null){
                        logger.error("获取账户余额信息失败！userId:"+user.userId+";产品号："+user.productId);
                        res.setMsg("获取账户余额信息失败！");
                        return res;
                    }
                    balance.setPulseDegree(balance.getPulseDegree()+1);
                    finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, balance,balanceB, entryId, false);
                    if(finanaceEntries.size()==0){
                        logger.error("获取账户余额信息失败！userId:"+user.userId+";产品号："+user.productId);
                        res.setMsg("生成流水失败！");
                        return res;
                    }
                    for(FinanaceEntry finanaceEntry:finanaceEntries) {
                        finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);    
                        finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
                        if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
                            finanaceEntry.setRemark("提现手续费");
                        }
                        if(finanaceEntry.getPaymentAmount()>0){
                            finanaceEntrieAlls.add(finanaceEntry);
                        }
                    }                
                }
                transOrderInfo.setAmount(amount);
                transOrderInfo.setFuncCode(funcCode_old);
                insertFinanaceEntry(finanaceEntrieAlls, transOrderInfo, productIdStrings);
                return new CommonResponse();
             }
          }

        /**
         *加钱40151交易,对外dubbo
         */
        @Override
        @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
        public CommonResponse recharge(com.rkylin.wheatfield.bean.TransOrderInfo transOrderInfoo) {
            synchronized (lock) {
                logger.info(TransCodeConst.FUNCTION_CHARGE+"交易参数："+BeanUtil.getBeanVal(transOrderInfoo, null));
                String field = BeanUtil.validateBeanProEmpty(transOrderInfoo, new String[]{"amount","userId","merchantCode","orderAmount","orderNo","productId","userFee","userIpAddress"});
                CommonResponse res = new CommonResponse();
                res.setCode(CodeEnum.FAILURE.getCode());
                if (field!=null) {
                    logger.info(field+"不能为空!");
                    res.setMsg(field+"不能为空!");
                    return res;
                }
                if (transOrderInfoo.getAmount()==0l||transOrderInfoo.getAmount()+transOrderInfoo.getUserFee()!=transOrderInfoo.getOrderAmount()) {
                    logger.info("金额异常!");
                    res.setMsg("金额异常!");
                    return res;
                }
                com.rkylin.wheatfield.pojo.TransOrderInfo transOrderInfo = new com.rkylin.wheatfield.pojo.TransOrderInfo();
                BeanMapper.copy(transOrderInfoo, transOrderInfo);
                transOrderInfo.setRequestId(null);
                transOrderInfo.setCreatedTime(null);
                transOrderInfo.setUpdatedTime(null);
                transOrderInfo.setOrderCount(1);
                Date date = DateUtils.getSysDate(Constants.DATE_FORMAT_YYYYMMDDHHMMSS);
                transOrderInfo.setRequestTime(date);
                transOrderInfo.setOrderDate(date);
                transOrderInfo.setStatus(TransCodeConst.TRANS_STATUS_NORMAL);
                transOrderInfo.setFuncCode(TransCodeConst.FUNCTION_CHARGE);
                String userId = transOrderInfo.getUserId();
                if(!this.orderNoChk(transOrderInfo.getOrderNo(),transOrderInfo.getMerchantCode())){
                    logger.info("订单号："+transOrderInfo.getOrderNo()+"在机构号："+transOrderInfo.getMerchantCode()+"中已存在");
                    res.setMsg("该交易订单号"+transOrderInfo.getOrderNo()+"已存在，请确认！");
                    return res;
                }
                User user=new User();
                user.userId=userId;
                user.constId=transOrderInfo.getMerchantCode();
                user.productId=transOrderInfoo.getProductId();
                //判断账户状态是否正常
                boolean accountIsOK=operationService.checkAccount(user);
                if(!accountIsOK){
                    logger.info("用户"+userId+"状态为非正常状态");
                    res.setMsg("状态为非正常状态！");
                    return res;
                }
                userId=transOrderInfo.getUserId();  
                user.userId=userId;
                Balance balance=checkInfoService.getBalance(user,"");
                if(balance==null){
                    logger.info("获取用户["+userId+"]余额信息失败");
                    res.setMsg("用户余额信息查询失败");
                    return res;
                }
                balance.setPulseDegree(balance.getPulseDegree()+1);
                List<FinanaceEntry> finanaceEntries = checkInfoService.getFinanaceEntries(transOrderInfo, balance, redisIdGenerator.createRequestNo(), true);
                if(finanaceEntries.size()==0){
                    logger.info("获取用户["+userId+"]账户流水信息失败");
                    res.setMsg("生成流水失败");
                    return res;
                }
                for(FinanaceEntry finanaceEntry:finanaceEntries) {
                    finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
                    finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
                    if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
                        finanaceEntry.setRemark("加钱,类似充值交易");
                    }
                }   
                insertFinanaceEntry(finanaceEntries, transOrderInfo,null);
               }
              return new CommonResponse();
            }       
        
        public CommonResponse withhold40144(TransOrderInfo transOrderInfo){
            logger.info(transOrderInfo.getFuncCode()+" 交易  传入参数:"+BeanUtil.getBeanVal(transOrderInfo, null)); 
            synchronized (lock) {
               CommonResponse res = new CommonResponse();
               res.setCode(CodeEnum.FAILURE.getCode());
               //校验订单号是否存在
               if (!this.orderNoChk(transOrderInfo.getOrderNo(), transOrderInfo.getMerchantCode())) {
                   res.setMsg("订单号：" + transOrderInfo.getOrderNo() + "该交易订单号已存在，请确认！");
                   return res;
               }
               User user = new User();
               user.userId = transOrderInfo.getUserId();
               user.constId = transOrderInfo.getMerchantCode();
               user.productId = transOrderInfo.getProductIdd();
               //判断账户状态是否正常
               boolean accountIsOK = operationService.checkAccount(user);
               if (!accountIsOK) {
                   res.setMsg("用户" + transOrderInfo.getUserId() + "状态为非正常状态");
                   return res;
               }
               transOrderInfo.setAccountDate(DateUtils.getDate(generationPaymentService.getAccountDate(), Constants.DATE_FORMAT_YYYYMMDD));
               transOrderInfoDao.insertSelective(transOrderInfo);
               com.rkylin.wheatfield.bean.User beanUser = new com.rkylin.wheatfield.bean.User();
               beanUser.setUserId(transOrderInfo.getUserId());
               beanUser.setInstCode(transOrderInfo.getMerchantCode());
               beanUser.setProductId(transOrderInfo.getProductIdd());
               beanUser.setAmount(transOrderInfo.getAmount());
               beanUser.setStatus(BaseConstants.DEBIT_TYPE);
               Integer requestId = transOrderInfo.getRequestId();
               beanUser.setFinAccountId(String.valueOf(requestId));
               CommonResponse commonResponse = semiAutomatizationServiceApi.operateFinAccount(beanUser);
               if (!CodeEnum.SUCCESS.getCode().equals(commonResponse.getCode())) {
                   transOrderInfo = new TransOrderInfo();
                   transOrderInfo.setRequestId(requestId);
                   transOrderInfo.setStatus(TransCodeConst.TRANS_STATUS_INVALIDATION);
                   transOrderInfoDao.updateByPrimaryKeySelective(transOrderInfo);
                   logger.info("生成流水异常:"+commonResponse.getMsg());
                   res.setMsg("生成流水失败!");
                   return res;
               }
               res.setCode(CodeEnum.SUCCESS.getCode());
               return res;
           }
        }
        
        /**
         * 批量转账
         * Discription:
         * @param batchTransfer
         * @return CommonResponse
         * @author Achilles
         * @since 2016年9月22日
         */
        @Override
        @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
        public CommonResponse transferBatch(BatchTransfer batchTransfer){
            CommonResponse res = new CommonResponse();
            res.setCode(CodeEnum.FAILURE.getCode());
            if (batchTransfer==null) {
                res.setMsg("参数为空!");
                return res;
            }
            if (CommUtil.isEmp(batchTransfer.getOutTransferInfoList()) || CommUtil.isEmp(batchTransfer.getIntoTransferInfoList())) {
                res.setMsg("转出转入方信息异常,请检查!");
                return res;
            }
            res = batchTransfer(batchTransfer);
            if (!CodeEnum.SUCCESS.getCode().equals(res.getCode())) {
                logger.info("批量转账  msg="+res.getMsg());
                return res;
            }
            res.setCode(CodeEnum.SUCCESS.getCode());
            return res;
        }

        /**
         * Discription:充值
         * @param transOrderInfo
         * @author liuhuan
         * @since 2017年2月6日
         */
        @Override
        public CommonResponse rechargeByDubbo(TransOrderInfo transOrderInfo) {
            logger.info("--------调用dubbo充值接口开始--------"); 
            CommonResponse response = new CommonResponse();
            String reCode = "P0";
            String reMsg = "成功";
            response.setMsg(reMsg);
            if(transOrderInfo.getAmount() == null || transOrderInfo.getAmount() <= 0){
                reCode = "P1";
                reMsg = "amount需大于0";
            }else if(transOrderInfo.getUserId() == null || "".equals(transOrderInfo.getUserId())){
                reCode = "P1";
                reMsg = "userid不能为空";
            }else if(transOrderInfo.getFuncCode() == null || "".equals(transOrderInfo.getFuncCode())){
                reCode = "P1";
                reMsg = "funccode不能为空";
            }else if(transOrderInfo.getMerchantCode() == null || "".equals(transOrderInfo.getMerchantCode())){
                reCode = "P1";
                reMsg = "merchantcode不能为空";
            }else if(transOrderInfo.getOrderAmount() != null && transOrderInfo.getOrderAmount() <= 0){
                reCode = "P1";
                reMsg = "orderamount需大于0";
            }else if(transOrderInfo.getOrderCount() == null || transOrderInfo.getOrderCount() <= 0){
                reCode = "P1";
                reMsg = "ordercount需大于0";
            }else if(transOrderInfo.getOrderDate() == null || "".equals(transOrderInfo.getOrderDate())){
                reCode = "P1";
                reMsg = "orderdate不能为空";
            }else if(transOrderInfo.getOrderNo() == null || "".equals(transOrderInfo.getOrderNo())){
                reCode = "P1";
                reMsg = "orderno不能为空";
            }else if(transOrderInfo.getRequestTime() == null || "".equals(transOrderInfo.getRequestTime())){
                reCode = "P1";
                reMsg = "requesttime不能为空";
            }else if(transOrderInfo.getStatus() == null || "".equals(transOrderInfo.getStatus())){
                reCode = "P1";
                reMsg = "status不能为空";
            }else if(transOrderInfo.getUserFee() == null || "".equals(transOrderInfo.getUserFee())){
                reCode = "P1";
                reMsg = "userfee不能小于0";
            }else if(transOrderInfo.getProductId() == null || "".equals(transOrderInfo.getProductId())){
                reCode = "P1";
                reMsg = "productid不能为空";
            }else if(transOrderInfo.getUserIpAddress() == null || "".equals(transOrderInfo.getUserIpAddress())){
                reCode = "P1";
                reMsg = "useripaddress不能为空";
            }else if(transOrderInfo.getRemark() == null || "".equals(transOrderInfo.getRemark())){
                reCode = "P1";
                reMsg = "remark不能为空";
            }
            if(!reCode.equals("P0")){
                response.setCode(reCode);
                response.setMsg(reMsg);
                logger.info("--------调用dubbo充值接口结束-----返回结果信息code: " + response.getCode() + "-msg: " + response.getMsg() + "--");
                return response;
            }
            if(TransCodeConst.TRANS_STATUS_INVALIDATION==transOrderInfo.getStatus()){
                ErrorResponse errorResponse = this.saveTransorder(transOrderInfo, "充值");
                if(errorResponse.getCode().equals("C0")){
                    logger.info("--------调用dubbo充值接口结束-----返回结果信息code: " + response.getCode() + "-msg: " + response.getMsg() + "--");
                    return response;
                }else{
                    response.setCode(errorResponse.getCode());
                    response.setMsg(errorResponse.getMsg());
                }
            }else if(transOrderInfo.getMerchantCode().equals(Constants.TXYW_ID)){
                String result = this.rechargeTXYW(transOrderInfo, transOrderInfo.getProductId(), transOrderInfo.getUserId());
                if(result.equals("ok")){
                    logger.info("--------调用dubbo充值接口结束-----返回结果信息code: " + response.getCode() + "-msg: " + response.getMsg() + "--");
                    return response;
                }else{
                    response.setCode("C1");
                    response.setMsg(result);
                }
            }else if(Constants.FC_ID.equals(transOrderInfo.getMerchantCode())) {
                transOrderInfo.setProductIdd(transOrderInfo.getProductId());
                CommonResponse res = rechargeUserFee(transOrderInfo);
                if(CodeEnum.SUCCESS.getCode().equals(res.getCode())){
                    logger.info("--------调用dubbo充值接口结束-----返回结果信息code: " + response.getCode() + "-msg: " + response.getMsg() + "--");
                    return response;
                }else{
                    response.setCode("C1");
                    response.setMsg(res.getMsg());
                }
            }else{
                ErrorResponse errorResponse = this.recharge(transOrderInfo, transOrderInfo.getProductId(), transOrderInfo.getUserId());
                if(errorResponse.isIs_success()){
                    logger.info("--------调用dubbo充值接口结束-----返回结果信息code: " + response.getCode() + "-msg: " + response.getMsg() + "--");
                    return response;
                }else{
                    response.setCode(errorResponse.getCode());
                    response.setMsg(errorResponse.getMsg());
                }
            }
            logger.info("--------调用dubbo充值接口结束-----返回结果信息code: " + response.getCode() + "-msg: " + response.getMsg() + "--");
            return response;
        }
        
}
