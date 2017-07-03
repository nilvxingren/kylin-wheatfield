package com.rkylin.wheatfield.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.rkylin.common.RedisIdGenerator;
import com.rkylin.database.BaseDao;
import com.rkylin.file.txt.TxtReader;
import com.rkylin.file.txt.TxtWriter;
import com.rkylin.gaterouter.dto.bankpayment.PaymentDto;
import com.rkylin.gaterouter.dto.bankpayment.PaymentRespDto;
import com.rkylin.gaterouter.service.BankPaymentService;
import com.rkylin.order.mixservice.CollectionWithholdService;
import com.rkylin.order.mixservice.SettlementToOrderService;
import com.rkylin.order.pojo.OrderAccountInfo;
import com.rkylin.order.pojo.OrderPayment;
import com.rkylin.order.service.OrderAccountInfoService;
import com.rkylin.util.bean.BeanMapper;
import com.rkylin.utils.RkylinMailUtil;
import com.rkylin.utils.SendSMS;
import com.rkylin.wheatfield.api.SemiAutomatizationServiceApi;
import com.rkylin.wheatfield.api.SettlementServiceThrDubboService;
import com.rkylin.wheatfield.common.DateUtils;
import com.rkylin.wheatfield.common.PartyCodeUtil;
import com.rkylin.wheatfield.common.UploadAndDownLoadUtils;
import com.rkylin.wheatfield.constant.AccountConstants;
import com.rkylin.wheatfield.constant.BaseConstants;
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.constant.SettleConstants;
import com.rkylin.wheatfield.constant.TransCodeConst;
import com.rkylin.wheatfield.dao.AccountInfoDao;
import com.rkylin.wheatfield.dao.CorporatAccountInfoDao;
import com.rkylin.wheatfield.dao.FinanaceAccountDao;
import com.rkylin.wheatfield.dao.FinanaceEntryDao;
import com.rkylin.wheatfield.dao.GenerationPaymentDao;
import com.rkylin.wheatfield.dao.ParameterInfoDao;
import com.rkylin.wheatfield.dao.TransDaysSummaryDao;
import com.rkylin.wheatfield.dao.TransOrderInfoDao;
import com.rkylin.wheatfield.exception.AccountException;
import com.rkylin.wheatfield.manager.AccountInfoManager;
import com.rkylin.wheatfield.manager.CorporatAccountInfoManager;
import com.rkylin.wheatfield.manager.CreditApprovalInfoManager;
import com.rkylin.wheatfield.manager.CreditRateTemplateManager;
import com.rkylin.wheatfield.manager.CreditRepaymentManager;
import com.rkylin.wheatfield.manager.FinanaceEntryManager;
import com.rkylin.wheatfield.manager.GenerationPaymentManager;
import com.rkylin.wheatfield.manager.InterestRepaymentManager;
import com.rkylin.wheatfield.manager.ParameterInfoManager;
import com.rkylin.wheatfield.manager.SettleSplittingEntryManager;
import com.rkylin.wheatfield.manager.SettlementManager;
import com.rkylin.wheatfield.manager.TransDaysSummaryManager;
import com.rkylin.wheatfield.manager.TransOrderInfoManager;
import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.model.PingAnParam;
import com.rkylin.wheatfield.pojo.AccountAgreement;
import com.rkylin.wheatfield.pojo.AccountInfo;
import com.rkylin.wheatfield.pojo.AccountInfoQuery;
import com.rkylin.wheatfield.pojo.Balance;
import com.rkylin.wheatfield.pojo.CorporatAccountInfo;
import com.rkylin.wheatfield.pojo.CorporatAccountInfoQuery;
import com.rkylin.wheatfield.pojo.CreditApprovalInfo;
import com.rkylin.wheatfield.pojo.CreditApprovalInfoQuery;
import com.rkylin.wheatfield.pojo.CreditRateTemplate;
import com.rkylin.wheatfield.pojo.CreditRateTemplateQuery;
import com.rkylin.wheatfield.pojo.CreditRepayment;
import com.rkylin.wheatfield.pojo.FinanaceAccount;
import com.rkylin.wheatfield.pojo.FinanaceAccountQuery;
import com.rkylin.wheatfield.pojo.FinanaceEntry;
import com.rkylin.wheatfield.pojo.GenerationPayment;
import com.rkylin.wheatfield.pojo.GenerationPaymentQuery;
import com.rkylin.wheatfield.pojo.InterestRepayment;
import com.rkylin.wheatfield.pojo.InterestRepaymentQuery;
import com.rkylin.wheatfield.pojo.ParameterInfo;
import com.rkylin.wheatfield.pojo.ParameterInfoQuery;
import com.rkylin.wheatfield.pojo.SettleSplittingEntry;
import com.rkylin.wheatfield.pojo.TransDaysSummary;
import com.rkylin.wheatfield.pojo.TransDaysSummaryQuery;
import com.rkylin.wheatfield.pojo.TransOrderInfo;
import com.rkylin.wheatfield.pojo.TransOrderInfoQuery;
import com.rkylin.wheatfield.pojo.User;
import com.rkylin.wheatfield.response.ErrorResponse;
import com.rkylin.wheatfield.response.Response;
import com.rkylin.wheatfield.response.SettlementSecResponse;
import com.rkylin.wheatfield.service.AccountManageService;
import com.rkylin.wheatfield.service.CheckInfoService;
import com.rkylin.wheatfield.service.GenerationPaymentService;
import com.rkylin.wheatfield.service.IAPIService;
import com.rkylin.wheatfield.service.OperationServive;
import com.rkylin.wheatfield.service.ParameterInfoService;
import com.rkylin.wheatfield.service.PaymentAccountService;
import com.rkylin.wheatfield.service.PaymentInternalService;
import com.rkylin.wheatfield.service.SemiAutomatizationService;
import com.rkylin.wheatfield.service.SettlementServiceThr;
import com.rkylin.wheatfield.settlement.ProfitLogic;
import com.rkylin.wheatfield.settlement.SettlementLogic;
import com.rkylin.wheatfield.utils.CodeEnum;
import com.rkylin.wheatfield.utils.CommUtil;
import com.rkylin.wheatfield.utils.DateUtil;


@Service("settlementServiceThr")
@SuppressWarnings({"rawtypes","unchecked"})
@Transactional
public class SettlementServiceThrImpl extends BaseDao implements
		SettlementServiceThr,SettlementServiceThrDubboService, IAPIService {
	private static Logger logger = LoggerFactory
			.getLogger(SettlementServiceThrImpl.class);

	@Autowired
	RedisIdGenerator redisIdGenerator;
	@Autowired
	Properties userProperties;
	@Autowired
	CreditApprovalInfoManager creditApprovalInfoManager;
	@Autowired
	AccountManageService accountManageService;
	@Autowired
	SettlementLogic settlementLogic;
	@Autowired
	GenerationPaymentService generationPaymentService;
	@Autowired
	GenerationPaymentDao generationPaymentDao;
	@Autowired
	CreditRateTemplateManager creditRateTemplateManager;
	@Autowired
	OperationServive operationServive;
	@Autowired
	TransOrderInfoManager transOrderInfoManager;
	@Autowired
	@Qualifier("transOrderInfoDao")
	private TransOrderInfoDao transOrderInfoDao;
	@Autowired
	TransDaysSummaryManager transDaysSummaryManager;
	@Autowired
	TransDaysSummaryDao transDaysSummaryDao;
	@Autowired
	AccountInfoManager accountInfoManager;
	@Autowired
	private AccountInfoDao accountInfoDao;
	@Autowired
	private CorporatAccountInfoDao corporatAccountInfoDao;
	@Autowired
	GenerationPaymentManager generationPaymentManager;
	@Autowired
	PaymentAccountService paymentAccountService;
	@Autowired
	private SettlementToOrderService settlementToOrderService;
	@Autowired
	SettleSplittingEntryManager settleSplittingEntryManager;
	@Autowired
	ParameterInfoManager parameterInfoManager;
	@Autowired
	PaymentInternalService paymentInternalService;
	@Autowired
	CreditRepaymentManager creditRepaymentManager;
	@Autowired
	InterestRepaymentManager interestRepaymentManager;
	@Autowired
	SettlementManager settlementManager;
	DateUtil dateUtil=new DateUtil();
	@Autowired
	CollectionWithholdService collectionWithholdService;
	@Autowired
	OrderAccountInfoService orderAccountInfoService;
	@Autowired
	ProfitLogic profitLogic;
//	@Autowired
//	private CorporatAccountInfoManager corporatAccountInfoManager;
	@Autowired
	private ParameterInfoDao parameterInfoDao;
    @Autowired
    private OperationServive operationService;
    @Autowired
    CheckInfoService checkInfoService;  
    @Autowired
    FinanaceEntryManager finanaceEntryManager;
    @Autowired
    private BankPaymentService bankPaymentService;
    @Autowired
    private FinanaceAccountDao finanaceAccountDao;
    @Autowired
    @Qualifier("finanaceEntryDao")
    private FinanaceEntryDao finanaceEntryDao;
    
    @Autowired
    private ParameterInfoService parameterInfoService;
    
    @Autowired
    private SemiAutomatizationService semiAutomatizationService;
    
    @Autowired
    @Qualifier("semiAutomatizationService")
    private SemiAutomatizationServiceApi semiAutomatizationServiceApi;
    
	/**
	 * 审核结果/授信结果读入/通知接口(dubbo)
	 * @param fileTpye ROP上文件类型
	 * @param accountDate	账期
	 * @param batch	批次号
	 * @param type 通知类型：1：审核/授信果  3:通知分润
	 * @return
	 */
	@Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
	public CommonResponse notifyCreditReslts(String fileTpye,String accountDate,String batch,String type){
		logger.info("审核结果/授信结果读入/通知接口   传入参数  fileTpye="+fileTpye+",accountDate="+accountDate+",batch="+batch+",type="+type);
		CommonResponse res = new CommonResponse();
		if (fileTpye==null || "".equals(fileTpye) || accountDate==null ||"".equals(accountDate)|| type==null ||"".equals(type)) {
			res.setCode(CodeEnum.ERR_PARAM_NULL.getCode());
			res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
			return res;
		}
		Map<String,String> rtnMap = null;
		if ("1".equals(type)) {// 审核授信接口
			rtnMap = getCreditInfo(fileTpye,accountDate,batch);
			logger.info("rtnMap=========="+rtnMap);
			if ("0000".equals(rtnMap.get("errCode"))) {
				res.setMsg(rtnMap.get("errMsg"));
			} else {
				res.setCode(CodeEnum.FAILURE.getCode());
				res.setMsg(rtnMap.get("errMsg"));
			}
			return res;
		}
		if("3".equals(type)){
			//通知分润
			Map<String, String> result = notifyProfitLogic(accountDate, fileTpye);
			if(!"0000".equals(result.get("errCode"))){
				res.setCode(CodeEnum.FAILURE.getCode());
			}
			res.setMsg(result.get("errMsg"));
		}else {
			res.setCode(CodeEnum.FAILURE.getCode());
			res.setMsg("type不合法！");
		}
		return res;
	}

	@Override
	public Response doJob(Map<String, String[]> paramMap, String methodName) {
		SettlementSecResponse response = new SettlementSecResponse();
		Map<String,String> rtnMap = new HashMap<String,String>();
		if ("ruixue.wheatfield.credit.notify".equals(methodName)) {
			String type = "";
			String accountDate = "";
			String batch = "";
			String filetype = "";
			for (Object keyObj : paramMap.keySet().toArray()) {
				String[] strs = paramMap.get(keyObj);
				for (String value : strs) {
					if (keyObj.equals("type")) {
						type = value;
					}else if(keyObj.equals("accountdate")) {
						accountDate = value;
					}else if (keyObj.equals("batch")) {
						batch = value;
					}else if (keyObj.equals("filetype")) {
						filetype = value;
					}
				}
			}
			if ("1".equals(type)) {// 审核授信接口
				rtnMap = getCreditInfo(filetype,accountDate,batch);
				if ("0000".equals(rtnMap.get("errCode"))) {
					response.setIs_success(true);
					response.setRtn_msg(rtnMap.get("errMsg"));
				} else {
					response.setIs_success(false);
					response.setRtn_msg(rtnMap.get("errMsg"));
				}
			} else if("2".equals(type)) {//代收付结果读入
				String msg=generationPaymentService.paymentFile(accountDate,batch,filetype,userProperties.getProperty("RKYLIN_PUBLIC_KEY"));
				if("ok".equals(msg)){
					rtnMap=this.updateCreditAccountSec();
					if("0000".equals(rtnMap.get("errCode"))){
						response.setIs_success(true);
						response.setRtn_msg(rtnMap.get("errMsg"));
						Map<String, String> rtnMapP2P = this.withholdToP2P();
						if (!"P0".equals(rtnMapP2P.get("errCode"))) {
							logger.error("学生还款给课栈 成功后，发起课栈代付交易给P2P错误："+rtnMap.get("errCode")+":"+rtnMap.get("errMsg"));
						}
						//RkylinMailUtil.sendMail("恭喜你", "代收付结果文件成功了！", "1663991989@qq.com");
					}else{
						response.setIs_success(false);
						response.setRtn_msg(rtnMap.get("errMsg"));
						//RkylinMailUtil.sendMail("错误信息", rtnMap.get("errCode")+":"+rtnMap.get("errMsg"), "1663991989@qq.com");
						//return errorResponseService.getErrorResponse(rtnMap.get("errCode"), rtnMap.get("errMsg"));
					}
			    	//调用晶晶的存储过程把interest的数据过渡至历史表20150615 end
					// 调用晶晶的存储过程把成功的代收付过渡至历史表
		            Map<String, String> param = new HashMap<String, String>();
			    	super.getSqlSession().selectList("MyBatisMap.setgeneration", param);
		            if(null==param||!String.valueOf(param.get("on_err_code")).equals("0")){
		    			logger.error("维护代付表历史数据失败！");
		            }
				}else{
					response.setIs_success(false);
					response.setRtn_msg(msg);
					//RkylinMailUtil.sendMail("错误信息", "债券包错误1111！", "1663991989@qq.com");
					//邮件发送
				}
			}else if("3".equals(type)){
				//通知分润
				Map<String, String> result = notifyProfitLogic(accountDate, filetype);
				if("0000".equals(result.get("errCode"))){
					response.setIs_success(true);
				}else{
					response.setIs_success(false);
				}
				response.setRtn_msg(result.get("errMsg"));
			}else {
				response.setIs_success(false);
				response.setRtn_msg("不支持的操作类型！");
			}
		}
		return response;
	}

	@Override
	@Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
	public Map<String,String> getCreditInfo(String type,String accountDate,String batch) {
		logger.info("读取授信结果文件 ————————————START————————————");
		logger.info("读取授信结果文件类型为："+type);
		logger.info("读取授信结果文件账期："+accountDate);

		Map<String,String> rtnMap = new HashMap<String,String>();
		rtnMap.put("errMsg", "授信成功");
		rtnMap.put("errCode", "0000");
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    	SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");

		accountDate = accountDate.replace("-", "");
		String filepath = SettleConstants.FILE_PATH +accountDate + File.separator+type + accountDate+"."+SettleConstants.FILE_CSV;
		File file = new File(SettleConstants.FILE_PATH +accountDate + File.separator);
		if (!file.exists()) {
			file.mkdirs();
		}
		String priOrPubKey = "";
		if ("2".equals(type)) {//丰年
			priOrPubKey = userProperties.getProperty("P2P_PRIVATE_KEY");
		} else if("20".equals(type)) {//会唐
			priOrPubKey = userProperties.getProperty("P2P_HT_PRIVATE_KEY");
		}else if ("23".equals(type) || "25".equals(type)) {//课栈
			priOrPubKey = userProperties.getProperty("P2P_KZH_PRIVATE_KEY");
		} else {
			rtnMap.put("errMsg", "输入的文件类型，不支持！");
			rtnMap.put("errCode", "0000");
			logger.error("输入的文件类型，不支持！");
		}

		Map<String,String> tmpMap = new HashMap<String,String>();
		try {
			tmpMap = settlementLogic.getFileFromROP(Integer.parseInt(type), batch, formatter.parse(accountDate), filepath, priOrPubKey, 1, userProperties.getProperty("FSAPP_KEY"),userProperties.getProperty("FSDAPP_SECRET"),userProperties.getProperty("FSROP_URL"));
			if (!"0000".equals(tmpMap.get("errCode"))) {
				rtnMap = tmpMap;
				logger.error("文件下载！"+rtnMap.get("errMsg"));
				return rtnMap;
			}
		} catch (Exception e) {
			logger.info("下载异常：" + e.getStackTrace(),e);
			rtnMap.put("errMsg", "文件下载异常！"+e.getMessage());
			rtnMap.put("errCode", "0001");
			logger.error("文件下载异常！"+e.getMessage());
			return rtnMap;
		}

		TxtReader txtReader = new TxtReader();
		List<Map> fileList = new ArrayList<Map>();
		logger.info("读取文件内容");
		try {
			txtReader.setEncode("UTF-8");
			fileList = txtReader.txtreader(new File(filepath) , SettleConstants.DEDT_SPLIT2);
			//fileList = txtReader.txtreader(new File("C:\\test\\download\\20150531\\20150531.csv") , SettleConstants.DEDT_SPLIT2);
		} catch(Exception e) {
			rtnMap.put("errMsg", "文件格式有误！"+e.getMessage());
			rtnMap.put("errCode", "0001");
			logger.error("授信文件操作异常！" + e.getMessage());
			return rtnMap;
		}

		logger.info("读取机构号对应关系");
		Map<String, String> parameterMap = settlementLogic.getParameterCodeByType("1",0);

		logger.info("编辑授信数据");
		BigDecimal amount = null;
		BigDecimal con_100 = new BigDecimal("100");
		CreditApprovalInfo creditApprovalInfo = new CreditApprovalInfo();
		AccountAgreement accountAgreement = new AccountAgreement();
    	Calendar c = Calendar.getInstance();
    	User user = new User();
    	String rtnStr = "";
    	String errStr = "";
		List<CreditApprovalInfo> rtnList = new ArrayList<CreditApprovalInfo>();
		List<CreditApprovalInfo> rtnerrorList = new ArrayList<CreditApprovalInfo>();

		try {
			for (Map<String,String> fileMap:fileList) {
				if (!"ok".equals(fileMap.get("L_10").toLowerCase())) {//只处理成功的交易
					continue;
				}
				creditApprovalInfo = new CreditApprovalInfo();
				accountAgreement = new AccountAgreement();
				user = new User();

				user.constId = parameterMap.get(fileMap.get("L_4"));

				creditApprovalInfo.setRootInstCd(parameterMap.get(fileMap.get("L_4")));//机构号
				creditApprovalInfo.setUserId(fileMap.get("L_7"));//商户用户
				amount = new BigDecimal(fileMap.get("L_12"));
				amount = amount.multiply(con_100);
				creditApprovalInfo.setAmount(amount.longValue());//授信金额
				if (fileMap.get("L_13") == null || "".equals(fileMap.get("L_13"))) {
					creditApprovalInfo.setAmountSingle(amount.longValue());//额度单笔最大授信金额
				} else {
					amount = new BigDecimal(fileMap.get("L_13"));
					amount = amount.multiply(con_100);
					creditApprovalInfo.setAmountSingle(amount.longValue());//额度单笔最大授信金额
				}
				creditApprovalInfo.setApplyAccountDate(formatter1.parse(fileMap.get("L_1")));//申请账期
				System.out.println(fileMap.get("L_0"));
				creditApprovalInfo.setApplyDate(formatter1.parse(fileMap.get("L_0")));//申请日期
				creditApprovalInfo.setAuditCompleteDate(formatter1.parse(fileMap.get("L_2")));//审核完成日期
//				creditApprovalInfo.setCreditAgreementId(fileMap.get("L_15"));//授信协议ID
				creditApprovalInfo.setCreditObjectType(fileMap.get("L_5"));//授信对象类型 0：终端用户；1：商户商家
				//学生的场合
				if ("0".equals(fileMap.get("L_5"))) {
					user.userId = fileMap.get("L_7");
					user.referUserId = fileMap.get("L_6");
					creditApprovalInfo.setDeadLine(fileMap.get("L_19"));//期限
				//学校的场合
				} else if ("1".equals(fileMap.get("L_5"))) {
					user.userId = fileMap.get("L_6");
					user.referUserId = parameterMap.get(fileMap.get("L_3"));
					creditApprovalInfo.setDeadLine(fileMap.get("L_16"));//期限
				}
				creditApprovalInfo.setCreditTypeId(new Integer(fileMap.get("L_11")));//授信种类ID额度:101/单笔:102
				user.creditType = fileMap.get("L_11");//授信种类ID额度:101/单笔:102
				creditApprovalInfo.setCurrency(fileMap.get("L_14"));//币种
				creditApprovalInfo.setStatusId("1");//生效
				if (fileMap.get("L_16")!=null && !"".equals(fileMap.get("L_16"))) {
					c.setTime(formatter.parse(accountDate));
					c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
					creditApprovalInfo.setStartTime(c.getTime());//生效时间
					c.setTime(formatter.parse(accountDate));
			    	c.add(Calendar.MONTH, Integer.parseInt((String)fileMap.get("L_16")));
					c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
					creditApprovalInfo.setEndTime(c.getTime());//失效时间
				}
				creditApprovalInfo.setExpansion1(fileMap.get("L_20"));//预留1
				creditApprovalInfo.setExpansion2(fileMap.get("L_21"));//预留2
				creditApprovalInfo.setExpansion3(fileMap.get("L_22"));//预留3
				creditApprovalInfo.setExpansion4(fileMap.get("L_23"));//预留4
				creditApprovalInfo.setExpansion5(fileMap.get("L_24"));//预留5
				creditApprovalInfo.setExpansion6(fileMap.get("L_25"));//预留6
				//creditApprovalInfo.setInterestDate(interestDate);//计息开始时间
				creditApprovalInfo.setMerchantId(fileMap.get("L_6"));//商户商家ID
				if (Constants.FN_ID.equals(parameterMap.get(fileMap.get("L_4")))) {//产品号
					creditApprovalInfo.setProductId(Constants.USER_SUB_ACCOUNT);
					user.productId = Constants.USER_SUB_ACCOUNT;
				} else if (Constants.HT_ID.equals(parameterMap.get(fileMap.get("L_4")))) {
					creditApprovalInfo.setProductId(Constants.HT_CREDIT_ACCOUNT);
					user.productId = Constants.HT_CREDIT_ACCOUNT;
				} else if (Constants.KZ_ID.equals(parameterMap.get(fileMap.get("L_4")))) {
					creditApprovalInfo.setProductId(Constants.KZ_CREDIT_ACCOUNT);
					user.productId = Constants.KZ_CREDIT_ACCOUNT;
				}
				creditApprovalInfo.setProviderId(parameterMap.get(fileMap.get("L_3")));//商户号
				creditApprovalInfo.setRateId(fileMap.get("L_18"));//费率协议号
				creditApprovalInfo.setVersion(fileMap.get("L_9"));//版本号
				//creditApprovalInfo.setVersionDate(versionDate);//版本时间
				accountAgreement.setAgmtCode(fileMap.get("L_15"));
				accountAgreement.setAgmtName(parameterMap.get(fileMap.get("L_3")));
				creditApprovalInfo.setCreditResultId(fileMap.get("L_8"));//授信申请订单号

				try {
					rtnStr = accountManageService.creditAccountNew(user, creditApprovalInfo, accountAgreement, parameterMap.get(fileMap.get("L_3")));
					if ("ok".equals(rtnStr)) {
						rtnList.add(creditApprovalInfo);
					} else {
						errStr = rtnMap.get("errMsg");
						if ("授信成功".equals(errStr)) {
							errStr = "用户["+user.userId+"]错误为："+rtnStr;
						} else {
							errStr = errStr + "," + "用户["+user.userId+"]错误为："+rtnStr;
						}
						logger.info("用户["+user.userId+"]，错误为："+rtnStr);
						rtnerrorList.add(creditApprovalInfo);
					}
				} catch(Exception x) {
					errStr = rtnMap.get("errMsg");
					if ("授信成功".equals(errStr)) {
						errStr = "用户["+user.userId+"]异常:" + x.getMessage();
					} else {
						errStr = errStr + "," + "用户["+user.userId+"]异常:" + x.getMessage();
					}
					logger.info("授信时异常!"+x.getMessage());
					rtnerrorList.add(creditApprovalInfo);
				}
			}
			if (rtnList.size() == 0) {
				logger.info("操作条目为空！错误条目为："+rtnerrorList.size());
			}
			if (rtnerrorList.size() != 0) {
				rtnMap.put("errMsg", "授信成功件数为："+rtnList.size()+"---错误件数为：" +rtnerrorList.size() +"---错误信息为："+errStr);
				logger.info("授信成功件数为："+rtnList.size()+"---错误件数为：" +rtnerrorList.size() +"---错误信息为："+errStr);
				rtnMap.put("errCode", "0002");
				return rtnMap;
			}
		} catch (Exception e) {
			rtnMap.put("errMsg", "编辑授信数据异常！"+e.getMessage());
			rtnMap.put("errCode", "0001");
			logger.error("编辑授信数据异常！"+e.getMessage());
			return rtnMap;
		}
		logger.info("读取授信结果文件 ————————————END————————————");
		return rtnMap;
	}

	@Override
	@Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
	public CommonResponse paymentGeneration(String generationType,String merchantId,int orderType,String bussinessCode,int dateType) {
        logger.info("----------------------------" + merchantId + "的 " + generationType + "汇总TASK启动---------------------");
        CommonResponse res = new CommonResponse();
        res.setCode(CodeEnum.FAILURE.getCode());
        //获取订单交易表中的用户代收数据
         /*获取账单日期      日期减一*/
        //获取当前账期
        Date accountDateLast=DateUtils.getDate(operationServive.getAccountDate(), "yyyy-MM-dd");//设置账期T-1
        //获取T-1日账期
        Date accountDate = accountDateLast;
        Calendar cal = Calendar.getInstance();
        cal.setTime(accountDate);
        cal.add(Calendar.DATE, -dateType);
        accountDate=cal.getTime();
        //记录代收账单汇总
        List<TransDaysSummary> transDaysSummaries=new ArrayList<TransDaysSummary>();
        TransOrderInfoQuery query=new TransOrderInfoQuery();
        //插入账期
        Date accountDateInsert;
        //特殊处理：会堂代收的场合,交易表的检索条件不同
        if(Constants.HT_ID.equals(merchantId) && TransCodeConst.PAYMENT_COLLECTION.equals(generationType)){
            Date date = DateUtils.getDate(dateUtil.getDate()+" 16:00:00", "yyyy-MM-dd hh:mm:ss");//获取当天的下午四点时间
            cal.setTime(date);
            cal.add(Calendar.DATE, -1);
            Date dateLast=cal.getTime();//获取前一天的时间
            query.setStartAccountDate(accountDate);
            query.setEndAccountDate(accountDateLast);
            query.setStartTime(dateLast);
            query.setEndTime(date);
            accountDateInsert = accountDateLast;
        //T+0汇总
        }else{
            query.setAccountDate(accountDate);
            accountDateInsert = accountDate;
        }
        if(dateType==0&&!TransCodeConst.PAYMENT_40143.equals(generationType)&&!TransCodeConst.PAYMENT_40144.equals(generationType)){
            if((Constants.KZ_ID.equals(merchantId)||Constants.BBZX_ID.equals(merchantId))&&!TransCodeConst.PAYMENT_COLLECTION.equals(generationType)){
                query.setBusiTypeId("1");
            }else if(!Constants.KZ_ID.equals(merchantId)&&!Constants.BBZX_ID.equals(merchantId)&&!Constants.YSJ_ID.equals(merchantId)){
                query.setErrorMsg(TransCodeConst.T0_FLAG);
            }
        }
        query.setMerchantCode(merchantId);
        query.setFuncCode(generationType);
        query.setStatus(TransCodeConst.TRANS_STATUS_NORMAL);
        List<TransOrderInfo> transOrderInfos;
        //特殊处理：代付的场合，检索sql文不同
        if(TransCodeConst.PAYMENT_WITHHOLD.equals(generationType)){
            //获取代收订单记录查询结果
            transOrderInfos=transOrderInfoManager.queryListGroupByInter(query);
        }else{
            transOrderInfos=transOrderInfoManager.queryListGroup(query);
        }
        List<TransOrderInfo> upTransList = new ArrayList();
        logger.info("获取"+ merchantId + "的 " + generationType +"订单共"+transOrderInfos.size()+"条");
        if (transOrderInfos.size()==0) {
            res.setMsg("没有查到订单");
            return res;
        }
        List<AccountInfoQuery> accountList = new ArrayList<AccountInfoQuery>();
        List<CorporatAccountInfoQuery> corAcclist = new ArrayList<CorporatAccountInfoQuery>();
        Set<String> userIdAndInstCdSet = new HashSet<String>();
        Set<String> accNoAndInstCdSet = new HashSet<String>();
        Set<String> sumIdSet = new HashSet<String>();
        Set<String> sumIdLoanSet = new HashSet<String>();//记录需要将代收付表的orderType改为7的交易
        Set<String> sumIdRdblSet = new HashSet<String>();//融德保理代收
        //统计用户提现数据
        for (TransOrderInfo transOrderInfo : transOrderInfos) {
//          Map<String,Object> upMap = Maps.newHashMap();
            TransOrderInfo  orderInfor = new TransOrderInfo();
            orderInfor.setOrderNo(transOrderInfo.getOrderNo());
            orderInfor.setMerchantCode( transOrderInfo.getMerchantCode());
//          upMap.put("status",TransCodeConst.TRANS_STATUS_GENARATED);
//          upMap.put("orderNo", transOrderInfo.getOrderNo());
//          upMap.put("merchantCode", transOrderInfo.getMerchantCode());
            upTransList.add(orderInfor);
            //代收汇总表中写数据
            TransDaysSummary transDaysSummary=new TransDaysSummary();
            transDaysSummary.setTransSumId(CommUtil.getGenerateId(sumIdSet));//汇总订单号
//          transDaysSummary.setTransSumId(redisIdGenerator.createRequestNo());//汇总订单号
            transDaysSummary.setRootInstCd(transOrderInfo.getMerchantCode());//机构号
            transDaysSummary.setOrderType(generationType);//订单类型--代收
            transDaysSummary.setAccountDate(accountDateInsert);//账单日期
            transDaysSummary.setSummaryAmount(transOrderInfo.getAmount());//汇总金额
            if(TransCodeConst.PAYMENT_WITHHOLD.equals(generationType)||TransCodeConst.PAYMENT_40143.equals(generationType)
                    ||TransCodeConst.PAYMENT_40144.equals(generationType)){//如果是代付汇总，获取InterMerchantCode值
                transDaysSummary.setUserId(transOrderInfo.getInterMerchantCode());
            }else{
                transDaysSummary.setUserId(transOrderInfo.getUserId());
            }
            transDaysSummary.setSummaryOrders(transOrderInfo.getOrderNo());//订单Id（汇总）
            transDaysSummaries.add(transDaysSummary);
            if (!userIdAndInstCdSet.contains(transDaysSummary.getUserId()+transOrderInfo.getMerchantCode())) {
                userIdAndInstCdSet.add(transDaysSummary.getUserId()+transOrderInfo.getMerchantCode());
                AccountInfoQuery accountInfoQuery =new AccountInfoQuery();
                accountInfoQuery.setAccountName(transDaysSummary.getUserId());//用户Id
                accountInfoQuery.setRootInstCd(transOrderInfo.getMerchantCode());//机构号
                accountList.add(accountInfoQuery);
            }
            if ( TransCodeConst.PAYMENT_WITHHOLD.equals(transOrderInfo.getFuncCode())&&"1".equals(transOrderInfo.getBusiTypeId()) && !accNoAndInstCdSet.contains(transOrderInfo.getInterMerchantCode()+transOrderInfo.getMerchantCode())) {//对公卡
                accNoAndInstCdSet.add(transOrderInfo.getInterMerchantCode()+transOrderInfo.getMerchantCode());
                CorporatAccountInfoQuery corAccInfoQuery =new CorporatAccountInfoQuery();
                corAccInfoQuery.setAccountNumber(transOrderInfo.getInterMerchantCode());
                corAccInfoQuery.setRootInstCd(transOrderInfo.getMerchantCode());//机构号
                corAcclist.add(corAccInfoQuery);
            }
            //如果 代收 && busytypeid ＝ 1 汇总的GENERATION_PAYMENT 的ordertype ＝ 7（贷款还款）
            if (TransCodeConst.PAYMENT_COLLECTION.equals(transOrderInfo.getFuncCode())&&"1".equals(transOrderInfo.getBusiTypeId())) {
                sumIdLoanSet.add(transDaysSummary.getTransSumId());
            }
	    //如果 代收 && busytypeid ＝ RS_CJZF0001 汇总的2.ROOT_INST_CD字段汇总成RS_CJZF0001  sumIdRdblSet
	    if (Constants.KZ_ID.contains(transOrderInfo.getMerchantCode())&&TransCodeConst.PAYMENT_COLLECTION.equals(transOrderInfo.getFuncCode())&&"RS_CJZF0001".equals(transOrderInfo.getBusiTypeId())) {
	       sumIdRdblSet.add(transDaysSummary.getTransSumId());
	    }
        }
        logger.info("--" + merchantId + "的 " + generationType + " 要查询的AccountInfo 的所有用户个数="+accountList.size()+",accountList="+accountList+"  要查出的CorporatAccountInfo 的所有对公个数="+corAcclist.size()+" corAcclist="+corAcclist);
        List<AccountInfo> accInforList = new ArrayList<AccountInfo>();
        if (accountList.size()!=0) {
            accInforList = accountInfoDao.queryByUserIdAndPurpose(accountList);
        }
        List<CorporatAccountInfo> corAccInforList= new ArrayList<CorporatAccountInfo>();
        if (corAcclist.size()!=0) {
            corAccInforList= corporatAccountInfoDao.queryByAccountNo(corAcclist);
        }
        logger.info("--" + merchantId + "的 " + generationType + "汇总TASK  查出的AccountInfo 的所有数据个数="+accInforList.size()+",查出的CorporatAccountInfo 的所有数据个数="+corAccInforList.size());
//      if (accInforList.size()==0 && corAccInforList.size()==0) {
//          return;
//      }
        logger.info(merchantId + "总共" + generationType + "笔数:"+transDaysSummaries.size());
        List<GenerationPayment> genList = new ArrayList<GenerationPayment>();
        int m = 0;
        //调用代收付接口写入代收付数据
        for (TransDaysSummary transDaysSummary : transDaysSummaries) {
            m++;
            if (m%200==0) {
                parameterInfoDao.testDBConnection(m);
            }
            //数据入汇总记录表
//              transDaysSummaryManager.saveTransDaysSummary(transDaysSummary);
            // 后面根据不同的table值，决定去ACCOUNT_INFO或CORPORAT_ACCOUNT_INFO查询数据
            TransOrderInfo transOrderInfo = null;
            for(TransOrderInfo toi:transOrderInfos){
                if (toi.getOrderNo().equals(transDaysSummary.getSummaryOrders())) {
                    transOrderInfo = toi;
                    break;
                }
            }
            String table = "ACCOUNT_INFO";
//            for (TransOrderInfo transOrderInfo:transOrderInfos) {
            if (TransCodeConst.PAYMENT_WITHHOLD.equals(transOrderInfo.getFuncCode())&&transOrderInfo.getOrderNo().equals(transDaysSummary.getSummaryOrders()) && "1".equals(transOrderInfo.getBusiTypeId())) {
                table = "CORPORAT_ACCOUNT_INFO";
            }
//            }
            //获取用户绑卡信息
//            AccountInfoQuery accountInfoQuery=new AccountInfoQuery();
            List<AccountInfo> accountInfos= new ArrayList<AccountInfo>();;
            //如果是提现，走以下流程
            if(TransCodeConst.WITHDROW.equals(generationType)){
                //先从订单系统获取卡信息
                OrderAccountInfo orderAccountInfo= null;
                try {
                    orderAccountInfo=orderAccountInfoService.findAccountInfoByOrderId(transDaysSummary.getSummaryOrders(), "1");
                } catch (Exception e) {
//                  e.printStackTrace();
                    logger.error("根据订单号："+transDaysSummary.getSummaryOrders()+"在订单系统中没有找到相应的提现绑卡信息！");
                }
                //数据封装：根据orderAccountInfo获取accountInfoOrder
                if (orderAccountInfo!=null) {
                    AccountInfo accountInfoOrder = getAccountInfoByOrder(orderAccountInfo);
                    accountInfos.add(accountInfoOrder);
                }
                if(accountInfos.size()==0){
                    for (AccountInfo accountInfo : accInforList) {
                        if (Constants.ACCOUNT_PURPOSE_3.equals(accountInfo.getAccountPurpose())&&
                                transDaysSummary.getUserId().equals(accountInfo.getAccountName())&&
                                transDaysSummary.getRootInstCd().equals(accountInfo.getRootInstCd())) {
                            accountInfos.add(accountInfo);
                            break;
                        }
                    }
                }
            }
            if (Constants.BIZ_TYPE_PENNY.equals(transOrderInfo.getBusiTypeId())) {
                logger.info("---一分钱代付-------订单中的卡号------TradeFlowNo="+transOrderInfo.getTradeFlowNo());
                for (AccountInfo accountInfo : accInforList) {
                    if (transOrderInfo.getTradeFlowNo().equals(accountInfo.getAccountNumber())&& transDaysSummary.getUserId().equals(accountInfo.getAccountName())&&
                            transDaysSummary.getRootInstCd().equals(accountInfo.getRootInstCd())) {
                        accountInfos.add(accountInfo);
                        break;
                    }
                }
            }
            if ("ACCOUNT_INFO".equals(table) &&  0 == accountInfos.size()) {
                for (AccountInfo accountInfo : accInforList) {
                    if ((Constants.ACCOUNT_PURPOSE_1.equals(accountInfo.getAccountPurpose())||Constants.ACCOUNT_PURPOSE_4.equals(accountInfo.getAccountPurpose()))&&
                            transDaysSummary.getUserId().equals(accountInfo.getAccountName())&&
                            transDaysSummary.getRootInstCd().equals(accountInfo.getRootInstCd())) {
                        accountInfos.add(accountInfo);
                        break;
                    }
                }
            }
            List<CorporatAccountInfo> corAccountInfos=null;
            if ("CORPORAT_ACCOUNT_INFO".equals(table)){
//                for(TransOrderInfo transOrderInfo:transOrderInfos){
                if (transOrderInfo.getOrderNo().equals(transDaysSummary.getSummaryOrders())) {
                    for (CorporatAccountInfo coAccountInfo : corAccInforList) {
                        if (transOrderInfo.getInterMerchantCode().equals(coAccountInfo.getAccountNumber())) {
                            corAccountInfos = new ArrayList<CorporatAccountInfo>();
                            corAccountInfos.add(coAccountInfo);
                            break;
                        }
                    }
                }
//                if(corAccountInfos!=null){
//                    break;
//                }
//                }
            }
            //准备数据调用接口写入代收付
            GenerationPayment generationPayment=new GenerationPayment();
            generationPayment.setUserId(transDaysSummary.getUserId());
            if("ACCOUNT_INFO".equals(table) && accountInfos.size()==0){
                generationPayment.setSendType(SettleConstants.SEND_DEFEAT);
                generationPayment.setErrorCode("账户未绑定结算卡");
            }else if("ACCOUNT_INFO".equals(table) && accountInfos.size()!=0){
                AccountInfo accountInfo=accountInfos.get(0);
                //数据封装
                getGenPayByAccountInfor(generationPayment, accountInfo);
            }else if("CORPORAT_ACCOUNT_INFO".equals(table)){
                if(corAccountInfos == null){
                    continue;
                }
                CorporatAccountInfo corAccountInfo=corAccountInfos.get(0);
                //数据封装
                getGenPayByCorAccountInfor(generationPayment,corAccountInfo);
            }
            generationPayment.setOrderNo(transDaysSummary.getTransSumId());//订单号
            //课栈特殊处理：机构号是课栈的场合  代收付表的机构号转换为丰年

            if(Constants.KZ_ID.equals(transDaysSummary.getRootInstCd())||Constants.TXYW_ID.equals(transDaysSummary.getRootInstCd())||Constants.BBZX_ID.equals(transDaysSummary.getRootInstCd())){
                generationPayment.setRootInstCd(Constants.FN_ID);//丰年机构号
                if (TransCodeConst.PAYMENT_40143.equals(transDaysSummary.getOrderType())||TransCodeConst.PAYMENT_40144.equals(transDaysSummary.getOrderType())) {
                    generationPayment.setRootInstCd(Constants.YQMS_KZ_ID);  
                }
            }else   if(Constants.MZ_ID.equals(transDaysSummary.getRootInstCd())||Constants.SQSM_ID.equals(transDaysSummary.getRootInstCd())
                    ||Constants.FACTORING_ID.equals(transDaysSummary.getRootInstCd())
                    ||Constants.FC_ID.equals(transDaysSummary.getRootInstCd())){                
                generationPayment.setRootInstCd(Constants.RS_ID);//融数机构号
            }else{
                generationPayment.setRootInstCd(transDaysSummary.getRootInstCd().substring(0, 7));//机构号
                if (Constants.YSJ_ID.equals(transDaysSummary.getRootInstCd())&&TransCodeConst.PAYMENT_40143.equals(transDaysSummary.getOrderType())) {
                    generationPayment.setRootInstCd(Constants.YQMS_YSJ_ID);
                }
            }
            generationPayment.setOrderType(orderType);//订单类型
            generationPayment.setUserId(transDaysSummary.getUserId());
            generationPayment.setAmount(transDaysSummary.getSummaryAmount());
            generationPayment.setStatusId(SettleConstants.TAKE_EFFECT);
            generationPayment.setBussinessCode(bussinessCode);//业务代码
            generationPayment.setAccountDate(accountDateInsert);//账单日期
            generationPayment.setRemark(transOrderInfo.getRemark());
            if (sumIdLoanSet.contains(transDaysSummary.getTransSumId())) {
                generationPayment.setOrderType(SettleConstants.ORDER_LOAN_REPAYMENT);//订单类型
            }
            if (sumIdRdblSet.contains(transDaysSummary.getTransSumId())) {
                generationPayment.setRootInstCd(Constants.RDBL_ID);
            }
            genList.add(generationPayment);
//              generationPaymentService.payMentResultTransform(generationPayment);
            logger.info(merchantId + "用户"+transDaysSummary.getUserId()+"写入成功,共" + generationType + "金额："+transDaysSummary.getSummaryAmount());
        }
        if (transDaysSummaries.size()!=0) {
            transDaysSummaryDao.insertBatch(transDaysSummaries);
            generationPaymentDao.insertBatch(genList);
            //批量更新交易表状态为7（已汇总）
            transOrderInfoDao.batchUpdateOrderInfor(upTransList);
//          transOrderInfoManager.batchUpdateByOrderNo(upTransList);
            res = new CommonResponse();
        }
        logger.info("----------------------------" + merchantId + "的 " + generationType + "汇总TASK结束---------------------");
        return res;
    }
	private void getGenPayByCorAccountInfor(GenerationPayment generationPayment,CorporatAccountInfo corAccountInfo) {
		generationPayment.setBankCode(corAccountInfo.getBankHead());
		generationPayment.setAccountType(AccountConstants.DEPOSIT_CARD);
		generationPayment.setAccountNo(corAccountInfo.getAccountNumber());
		generationPayment.setUserId(corAccountInfo.getAccountNumber());
		generationPayment.setAccountName(corAccountInfo.getAccountRealName());
		generationPayment.setAccountProperty(Constants.ACCOUNT_PROPERTY_PUBLIC);
		generationPayment.setProvince(corAccountInfo.getBankProvince());
		generationPayment.setCity(corAccountInfo.getBankCity());
		generationPayment.setOpenBankName(corAccountInfo.getBankBranchName());
		generationPayment.setPayBankCode(corAccountInfo.getBankBranch());
		generationPayment.setCurrency(corAccountInfo.getCurrency());
		generationPayment.setCertificateType(corAccountInfo.getCertificateType());
		generationPayment.setCertificateNumber(corAccountInfo.getCertificateNumber());
	}
	private void getGenPayByAccountInfor(GenerationPayment generationPayment,AccountInfo accountInfo) {
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
	private AccountInfo getAccountInfoByOrder(OrderAccountInfo orderAccountInfo) {
		AccountInfo accountInfoOrder=new AccountInfo();
		accountInfoOrder.setAccountName(orderAccountInfo.getAccountName());
		accountInfoOrder.setAccountTypeId(orderAccountInfo.getAccountTypeId());
		accountInfoOrder.setAccountPurpose(orderAccountInfo.getAccountPurpose());
		accountInfoOrder.setAccountNumber(orderAccountInfo.getAccountNumber());
		accountInfoOrder.setAccountProperty(orderAccountInfo.getAccountProperty());
		accountInfoOrder.setOpenAccountDate(orderAccountInfo.getOpenAccountDate());
		accountInfoOrder.setOpenAccountDescription(orderAccountInfo.getOpenAccountDescription());
		accountInfoOrder.setCurrency(orderAccountInfo.getCurrency());
		accountInfoOrder.setStatus(orderAccountInfo.getStatus());
		accountInfoOrder.setBankHead(orderAccountInfo.getBankHead());
		accountInfoOrder.setBankHeadName(orderAccountInfo.getBankHeadName());
		accountInfoOrder.setBankBranch(orderAccountInfo.getBankBranch());
		accountInfoOrder.setBankBranchName(orderAccountInfo.getBankBranchName());
		accountInfoOrder.setBankProvince(orderAccountInfo.getBankProvince());
		accountInfoOrder.setBankCity(orderAccountInfo.getBankCity());
		accountInfoOrder.setCertificateType(orderAccountInfo.getCertificateType());
		accountInfoOrder.setCertificateNumber(orderAccountInfo.getCertificateNumber());
		accountInfoOrder.setAccountRealName(orderAccountInfo.getAccountRealName());
		return accountInfoOrder;
	}


	//代收成功后 向P2P发起代付
	@Override
	public Map<String, String> withholdToP2P(List<GenerationPayment> generationPayments) {
		//给子健接口传递的参数
		Map<String, String[]> map = null;
		Map<String,String> rtnMap = new HashMap<String,String>();
		//查询还款成功数据
//		GenerationPaymentQuery query = new GenerationPaymentQuery();
//		query.setSendType(SettleConstants.SEND_NORMAL);//状态：代扣成功
//		query.setOrderType(SettleConstants.ORDER_LOAN_REPAYMENT);//操作类型：7 贷款还款
//		query.setStatusId(3);//状态：账户系统处理后
//		List<GenerationPayment> generationPayments= generationPaymentManager.queryList(query);
		int sum = generationPayments.size();
//		logger.info(">>> >>> 查询还款数据件数：" + sum);

	    if(generationPayments.size() > 0){//如果记录数 > 0
	    	//向交易表数据插入代付数据
	    	//String productId = Constants.USER_SUB_ACCOUNT;//被付方的产品id
	    	try{
	    		String mess = "";
	    		String[] resultArr = null;
	    		boolean hasErr = false;
	    		int errCount = 0;
	    		String interMerchantCode ="";
	    		String merchantCode ="";
	    		logger.info(">>> >>> 循环访问 generationPayment 代收付信息");
				for (GenerationPayment generationPayment : generationPayments) {
					logger.info("当前处理订单号(代收付表) orderNo=" + generationPayment.getOrderNo()+",SendType="+generationPayment.getSendType()+",statusId="+generationPayment.getStatusId()+",OrderType="+generationPayment.getOrderType());
					if (generationPayment.getSendType()!=SettleConstants.SEND_NORMAL) {
						continue;
					}
					if (generationPayment.getOrderType()!=SettleConstants.ORDER_LOAN_REPAYMENT) {
						continue;
					}
					if (generationPayment.getStatusId()!=3) {
						continue;
					}
					merchantCode =rootInstIdChangeR(generationPayment.getOrderNo(), generationPayment.getRootInstCd());
					if(Constants.KZ_ID.equals(merchantCode)){
						interMerchantCode = TransCodeConst.THIRDPARTYID_KZP2PZZH;
					}else{
						interMerchantCode="";
					}
					map = new HashMap<String, String[]>();
					map.put("userid", new String[]{generationPayment.getUserId()});
					map.put("amount", new String[]{String.valueOf(generationPayment.getAmount())});
					map.put("funccode", new String[]{TransCodeConst.PAYMENT_WITHHOLD});
					map.put("intermerchantcode", new String[]{interMerchantCode});
					map.put("merchantcode", new String[]{merchantCode});
					map.put("orderamount", new String[]{String.valueOf(generationPayment.getAmount())});
					map.put("ordercount", new String[]{"1"});
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					map.put("orderdate", new String[]{sdf.format(dateUtil.getNow())});
					map.put("requesttime", new String[]{sdf.format(dateUtil.getNow())});
					map.put("status", new String[]{"1"});
					map.put("userfee", new String[]{"0"});
					//后添加的 必填属性
					map.put("useripaddress", new String[]{"127.0.0.1"});
					map.put("productid", new String[]{Constants.KZ_CREDIT_ACCOUNT});
					map.put("requestno", new String[]{generationPayment.getRequestNo()});
					//业务类型ID busitypeid = account
					map.put("busitypeid", new String[]{"account"});
					//转出账户的产品id
					map.put("errormsg", new String[]{Constants.KZ_PRODUCT});
					logger.info(">>> >>> 调用 子健 代支付 接口");
					//调用子健的接口
					try {
						resultArr = collectionWithholdService.withholdService(map);
					} catch (Exception a) {
						// TODO: handle exception
						logger.error("还款代付失败，原因："+a.getMessage());
						RkylinMailUtil.sendMailThread("还款代付失败","具体原因："+a.getMessage(), TransCodeConst.FINANACE_ENTRY_ERROR_TOEMAIL);
					}

					/*测试
					 * */
					if(resultArr != null && resultArr.length > 0) {
						if(!Boolean.parseBoolean(resultArr[0])) {
							hasErr = true;
							errCount ++;
							String errOrderNo = generationPayment.getOrderNo();
							mess += errOrderNo + "; ";
						}
					}
				}
				if(hasErr) {
					String loggerMess = "课栈代付失败：总共代付"+ sum +"件, 成功"+ (sum-errCount) +"件, 失败"+ errCount +"件, " + resultArr[1] + "; 失败订单号[" + mess + "]";
					logger.info(loggerMess);
					rtnMap.put("errMsg", loggerMess);
					rtnMap.put("errCode", "P1");
				}
	    	}catch(Exception e){
	    		e.printStackTrace();
	    		logger.info("课栈代付失败：" + e.getMessage());
				rtnMap.put("errMsg", "课栈代付失败：" + e.getMessage());
				rtnMap.put("errCode", "P1");
				RkylinMailUtil.sendMailThread("还款代付失败","具体原因："+e.getMessage(), TransCodeConst.FINANACE_ENTRY_ERROR_TOEMAIL);
				return rtnMap;
	    	}
	    }else{
	    	logger.info("学生还款数据件数为0");
			rtnMap.put("errMsg", "学生还款数据件数为0");
			rtnMap.put("errCode", "P0");
			return rtnMap;
	    }

	    if(rtnMap.containsKey("errMsg")) {
	    	logger.info("课栈代付成功, 总共成功"+ sum +"件");
	    	rtnMap.put("errMsg", "课栈代付成功, 总共成功"+ sum +"件");
			rtnMap.put("errCode", "P0");
	    }

		return rtnMap;
	}

	//代收成功后 向P2P发起代付
	@Override
	public Map<String, String> withholdToP2P() {
		//给子健接口传递的参数
		Map<String, String[]> map = null;
		Map<String,String> rtnMap = new HashMap<String,String>();
		//查询还款成功数据
		GenerationPaymentQuery query = new GenerationPaymentQuery();
		query.setSendType(SettleConstants.SEND_NORMAL);//状态：代扣成功
		//query.setRootInstCd(Constants.FN_ID);
		query.setOrderType(SettleConstants.ORDER_LOAN_REPAYMENT);//操作类型：7 贷款还款
		query.setStatusId(3);//状态：账户系统处理后
		List<GenerationPayment> generationPayments= generationPaymentManager.queryList(query);
		int sum = generationPayments.size();
		logger.info(">>> >>> 查询还款数据件数：" + sum);

	    if(generationPayments.size() > 0){//如果记录数 > 0
	    	//向交易表数据插入代付数据
	    	//String productId = Constants.USER_SUB_ACCOUNT;//被付方的产品id
	    	try{
	    		String mess = "";
	    		String[] resultArr = null;
	    		boolean hasErr = false;
	    		int errCount = 0;
	    		String interMerchantCode ="";
	    		String merchantCode ="";
	    		logger.info(">>> >>> 循环访问 generationPayment 代收付信息");
				for (GenerationPayment generationPayment : generationPayments) {
					merchantCode =rootInstIdChangeR(generationPayment.getOrderNo(), generationPayment.getRootInstCd());
					if(Constants.KZ_ID.equals(merchantCode)){
						interMerchantCode = TransCodeConst.THIRDPARTYID_KZP2PZZH;
					}else{
						interMerchantCode="";
					}
					map = new HashMap<String, String[]>();
					map.put("userid", new String[]{generationPayment.getUserId()});
					map.put("amount", new String[]{String.valueOf(generationPayment.getAmount())});
					map.put("funccode", new String[]{TransCodeConst.PAYMENT_WITHHOLD});
					map.put("intermerchantcode", new String[]{interMerchantCode});
					map.put("merchantcode", new String[]{merchantCode});
					map.put("orderamount", new String[]{String.valueOf(generationPayment.getAmount())});
					map.put("ordercount", new String[]{"1"});
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					map.put("orderdate", new String[]{sdf.format(dateUtil.getNow())});
					map.put("requesttime", new String[]{sdf.format(dateUtil.getNow())});
					map.put("status", new String[]{"1"});
					map.put("userfee", new String[]{"0"});
					//后添加的 必填属性
					map.put("useripaddress", new String[]{"127.0.0.1"});
					map.put("productid", new String[]{Constants.KZ_CREDIT_ACCOUNT});
					map.put("requestno", new String[]{generationPayment.getRequestNo()});
					//业务类型ID busitypeid = account
					map.put("busitypeid", new String[]{"account"});
					//转出账户的产品id
					map.put("errormsg", new String[]{Constants.KZ_PRODUCT});
					logger.info(">>> >>> 调用 子健 代支付 接口");
					//调用子健的接口
					try {
						resultArr = collectionWithholdService.withholdService(map);
					} catch (Exception a) {
						// TODO: handle exception
						logger.error("还款代付失败，原因："+a.getMessage());
						RkylinMailUtil.sendMailThread("还款代付失败","具体原因："+a.getMessage(), TransCodeConst.FINANACE_ENTRY_ERROR_TOEMAIL);
					}

					/*测试
					 * */
					if(resultArr != null && resultArr.length > 0) {
						if(!Boolean.parseBoolean(resultArr[0])) {
							hasErr = true;
							errCount ++;
							String errOrderNo = generationPayment.getOrderNo();
							mess += errOrderNo + "; ";
						}
					}
				}
				if(hasErr) {
					String loggerMess = "课栈代付失败：总共代付"+ sum +"件, 成功"+ (sum-errCount) +"件, 失败"+ errCount +"件, " + resultArr[1] + "; 失败订单号[" + mess + "]";
					logger.info(loggerMess);
					rtnMap.put("errMsg", loggerMess);
					rtnMap.put("errCode", "P1");
				}
	    	}catch(Exception e){
	    		e.printStackTrace();
	    		logger.info("课栈代付失败：" + e.getMessage());
				rtnMap.put("errMsg", "课栈代付失败：" + e.getMessage());
				rtnMap.put("errCode", "P1");
				RkylinMailUtil.sendMailThread("还款代付失败","具体原因："+e.getMessage(), TransCodeConst.FINANACE_ENTRY_ERROR_TOEMAIL);
				return rtnMap;
	    	}
	    }else{
	    	logger.info("学生还款数据件数为0");
			rtnMap.put("errMsg", "学生还款数据件数为0");
			rtnMap.put("errCode", "P0");
			return rtnMap;
	    }

	    if(!rtnMap.containsKey("errMsg")) {
	    	logger.info("课栈代付成功, 总共成功"+ sum +"件");
	    	rtnMap.put("errMsg", "课栈代付成功, 总共成功"+ sum +"件");
			rtnMap.put("errCode", "P0");
	    }

		return rtnMap;
	}

	/**
	 * 获取发送一批数据的上限
	 * @return
	 */
	public int getBatchLimit(){
    	ParameterInfoQuery keyList =  new ParameterInfoQuery();
    	keyList.setParameterCode(SettleConstants.BATCH_LIMIT);
    	ParameterInfo parameterInfo = parameterInfoManager.queryList(keyList).get(0);
    	return Integer.parseInt(parameterInfo.getParameterValue());
	}

	/**
	 * 日终是否正常结束
	 * @return  true:正常       false:结束
	 */
	public boolean isDayEndOk(){
    	ParameterInfoQuery keyList =  new ParameterInfoQuery();
    	keyList.setParameterCode(SettleConstants.DAYEND);
    	List<ParameterInfo> parameterInfo = parameterInfoManager.queryList(keyList);
    	if (!"0".equals(parameterInfo.get(0).getParameterValue())) {
    		return false;
    	}
		return true;
	}


	@Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
	public Map<String,String> updateRecAndPayResults(List<GenerationPayment> generationList) {
		logger.info("通过代扣结果更新用户余额 ————————————START————————————");
		Map rtnMap = new HashMap();
		rtnMap.put("errCode", "0000");
		rtnMap.put("errMsg", "成功");

    	ParameterInfoQuery keyList =  new ParameterInfoQuery();
    	keyList.setParameterCode(SettleConstants.DAYEND);
    	List<ParameterInfo> parameterInfo = parameterInfoManager.queryList(keyList);
    	if (!"0".equals(parameterInfo.get(0).getParameterValue())) {
    		rtnMap.put("errCode", "0001");
    		rtnMap.put("errMsg", "日终没有正常结束！");
    		RkylinMailUtil.sendMailThread("清洁算开始异常","日终没有正常结束，不能开始清洁算操作", "21401233@qq.com");
    		return rtnMap;
    	}
		FinanaceEntry finanaceEntry = new FinanaceEntry();
		ErrorResponse errorResponse = null;
		SettleSplittingEntry settleSplittingEntry = null;
    	List<TransDaysSummary> tdslist = new ArrayList<TransDaysSummary>();
    	TransDaysSummaryQuery tdsq = new TransDaysSummaryQuery();
		TransDaysSummary transDaysSummary = new TransDaysSummary();
		CreditRepayment creditRepayment = new CreditRepayment();
//		InterestRepayment interestRepayment = new InterestRepayment();
		GenerationPayment upGenerationPayment = new GenerationPayment();
    	TransOrderInfo transOrderInfo = new TransOrderInfo();
    	String rootInstIdInfor = null;
    	Long collFailSumAmount = 0l;
		// 处理代收付交易TO账户系统
		for (GenerationPayment generationPayment:generationList) {
			logger.info("该条交易处理开始："+generationPayment.getOrderNo());
			settleSplittingEntry = new SettleSplittingEntry();
			transDaysSummary = new TransDaysSummary();
			creditRepayment = new CreditRepayment();
//			interestRepayment = new InterestRepayment();
			upGenerationPayment = new GenerationPayment();
			 try {
				if (generationPayment.getSendType() == null || generationPayment.getSendType() == 2 || generationPayment.getStatusId()==3 ||(generationPayment.getOrderType()!=SettleConstants.ORDER_COLLECTION && generationPayment.getOrderType()!=SettleConstants.ORDER_LOAN_REPAYMENT)) {
						continue;
				 } else if (generationPayment.getSendType() == SettleConstants.SETTLE_STU_0) {
					// 分为三种情况----1债券包,2提现,3还款
					if (SettleConstants.ORDER_BOND_PACKAGE == generationPayment.getOrderType()) {//债券包
						settleSplittingEntry.setSettleType(SettleConstants.SETTLE_TYPE_1);
						finanaceEntry.setPaymentAmount(generationPayment.getAmount());
						finanaceEntry.setReferId(generationPayment.getOrderNo());
						String rootInstId = rootInstIdChange(generationPayment.getOrderNo(),generationPayment.getRootInstCd());
						errorResponse = paymentInternalService.rightsPackageReturn(finanaceEntry, rootInstId);
						if (errorResponse.isIs_success() == true) {
							settleSplittingEntry.setStatusId(SettleConstants.SETTLE_STU_1);
							transDaysSummary.setStatusId(SettleConstants.SETTLE_STU_1);
							upGenerationPayment.setGeneId(generationPayment.getGeneId());
							upGenerationPayment.setStatusId(3);
							generationPayment.setStatusId(3);
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
							generationPayment.setStatusId(3);
						} else {
							settleSplittingEntry.setStatusId(SettleConstants.SETTLE_STU_0);
							transDaysSummary.setStatusId(SettleConstants.SETTLE_STU_0);
							settleSplittingEntry.setRemark("结果成功_更新账户失败");
							//throw new AccountException("ORDER_WITHDRAW--ERROR"+generationPayment.getOrderNo());
//					RkylinMailUtil.sendMailThread("账户记账流水失败通知","******************账户"+transOrderInfo.getUserId()+"做扣款-"+deductType+"记账流水数据入库连续失败三次", TransCodeConst.FINANACE_ENTRY_ERROR_TOEMAIL);
						}
				    	transDaysSummary.setTransSumId(generationPayment.getOrderNo());
				    	transDaysSummaryManager.updateTransDaysSummary(transDaysSummary);
					} else if (SettleConstants.ORDER_REPAYMENT == generationPayment.getOrderType() ) {//信用还款
						settleSplittingEntry.setSettleType(SettleConstants.SETTLE_TYPE_3);
						finanaceEntry.setPaymentAmount(generationPayment.getAmount());
						finanaceEntry.setReferId(generationPayment.getOrderNo());
						String rootInstId = rootInstIdChange(generationPayment.getOrderNo(),generationPayment.getRootInstCd());
						errorResponse = paymentInternalService.refundReturn(finanaceEntry, generationPayment.getUserId(), rootInstId);
						if (errorResponse.isIs_success() == true) {
							settleSplittingEntry.setStatusId(SettleConstants.SETTLE_STU_1);
							creditRepayment.setStatusId(SettleConstants.SETTLE_STU_1);
							upGenerationPayment.setGeneId(generationPayment.getGeneId());
							upGenerationPayment.setStatusId(3);
							generationPayment.setStatusId(3);
						} else {
							settleSplittingEntry.setStatusId(SettleConstants.SETTLE_STU_0);
							creditRepayment.setStatusId(SettleConstants.SETTLE_STU_0);
							settleSplittingEntry.setRemark("结果成功_更新账户失败");
							//throw new AccountException("ORDER_REPAYMENT--ERROR"+generationPayment.getOrderNo());
//				RkylinMailUtil.sendMailThread("账户记账流水失败通知","******************账户"+transOrderInfo.getUserId()+"做扣款-"+deductType+"记账流水数据入库连续失败三次", TransCodeConst.FINANACE_ENTRY_ERROR_TOEMAIL);
						}
						creditRepayment.setCredId(Integer.parseInt(generationPayment.getOrderNo()));
						creditRepayment.setRepaymentRepaidDate(new Date());
						creditRepaymentManager.saveCreditRepayment(creditRepayment);
					} else if (SettleConstants.ORDER_LOAN_REPAYMENT == generationPayment.getOrderType() ) {//贷款还款
						boolean flag = true;
						String productId = "";
//						String productId_kz = "";

//						String rootInstId = rootInstIdChangeR(generationPayment.getOrderNo(),generationPayment.getRootInstCd());
						rootInstIdInfor = rootInstIdChange(generationPayment.getOrderNo(),generationPayment.getRootInstCd());
						if (Constants.HT_ID.equals(rootInstIdInfor)) {
							productId = Constants.HT_CREDIT_ACCOUNT;
//							productId_kz = Constants.HT_PRODUCT;
						} else if (Constants.HT_CLOUD_ID.equals(rootInstIdInfor)) {
							productId = Constants.HT_CHANGDIFANG_ACCOUNT;
						} else if (Constants.FN_ID.equals(rootInstIdInfor)) {
							productId = Constants.FN_PRODUCT;
						} else if (Constants.KZ_ID.equals(rootInstIdInfor)) {
//							productId = Constants.KZ_CREDIT_ACCOUNT;
							productId = Constants.KZ_PRODUCT;
						}else if(Constants.LY_ID.equals(rootInstIdInfor)){
                            productId = Constants.LY_PRODUCT;
                        }else{
							flag = false;
						}
						if(flag){
							settleSplittingEntry.setSettleType(SettleConstants.ORDER_LOAN_REPAYMENT);
//							transOrderInfo = new TransOrderInfo();
//							transOrderInfo.setUserId(generationPayment.getUserId());
//							transOrderInfo.setAmount(generationPayment.getAmount());
//							transOrderInfo.setFuncCode(TransCodeConst.PAYMENT_COLLECTION);
//							transOrderInfo.setMerchantCode(rootInstId);
//							transOrderInfo.setOrderAmount(generationPayment.getAmount());
//							transOrderInfo.setOrderCount(1);
//							transOrderInfo.setOrderDate(generationPayment.getAccountDate());
//							transOrderInfo.setOrderNo(generationPayment.getOrderNo());
//							transOrderInfo.setRequestTime(generationPayment.getCreatedTime());
//							transOrderInfo.setStatus(4);
//							transOrderInfo.setUserFee(Long.valueOf("0"));
//							
//							errorResponse = paymentAccountService.collection(transOrderInfo, productId_kz);
//							if (errorResponse.isIs_success() == true) {
							finanaceEntry.setPaymentAmount(generationPayment.getAmount());
							finanaceEntry.setReferId(generationPayment.getOrderNo());
							errorResponse = paymentInternalService.collectionReturn(finanaceEntry, rootInstIdInfor,generationPayment.getUserId(),productId);
							if (errorResponse.isIs_success() == true) {
								settleSplittingEntry.setStatusId(SettleConstants.SETTLE_STU_1);
//								interestRepayment.setStatusId(SettleConstants.SETTLE_STU_1);
								upGenerationPayment.setGeneId(generationPayment.getGeneId());
								upGenerationPayment.setStatusId(3);
								generationPayment.setStatusId(3);
								transDaysSummary.setStatusId(SettleConstants.SETTLE_STU_1);
							} else {
								settleSplittingEntry.setStatusId(SettleConstants.SETTLE_STU_0);
//								interestRepayment.setStatusId(SettleConstants.SETTLE_STU_0);
								settleSplittingEntry.setRemark("结果成功_更新账户失败");
								transDaysSummary.setStatusId(SettleConstants.SETTLE_STU_0);
								
								//throw new AccountException("ORDER_WITHDRAW--ERROR"+generationPayment.getOrderNo());
			//					RkylinMailUtil.sendMailThread("账户记账流水失败通知","******************账户"+transOrderInfo.getUserId()+"做扣款-"+deductType+"记账流水数据入库连续失败三次", TransCodeConst.FINANACE_ENTRY_ERROR_TOEMAIL);
							}
//							} else {
//								settleSplittingEntry.setStatusId(SettleConstants.SETTLE_STU_0);
//								interestRepayment.setStatusId(SettleConstants.SETTLE_STU_0);
//								settleSplittingEntry.setRemark("结果成功_更新账户失败");
//							}
						}
//						interestRepayment.setInterId(Integer.parseInt(generationPayment.getOrderNo()));
//						interestRepayment.setRepaidRepaymentDate(new Date());
//						interestRepaymentManager.saveInterestRepayment(interestRepayment);
				    	transDaysSummary.setTransSumId(generationPayment.getOrderNo());
				    	transDaysSummaryManager.updateTransDaysSummary(transDaysSummary);
					} else if (SettleConstants.ORDER_COLLECTION == generationPayment.getOrderType()) {//代收
						boolean flag = true;
						String productId = "";
						String rootInstId = rootInstIdChange(generationPayment.getOrderNo(),generationPayment.getRootInstCd());
						if (Constants.HT_ID.equals(rootInstId)) {
							productId = Constants.HT_PRODUCT;
						} else if (Constants.HT_CLOUD_ID.equals(rootInstId)) {
							productId = Constants.HT_CHANGDIFANG_ACCOUNT;
						} else if (Constants.FN_ID.equals(rootInstId)) {
							productId = Constants.FN_PRODUCT;
						} else if (Constants.KZ_ID.equals(rootInstId)) {
							productId = Constants.KZ_PRODUCT;
						}else if(Constants.SQSM_ID.equals(rootInstId)){
							productId = Constants.SQSM_PRODUCT;
						}else if(Constants.YSJ_ID.equals(rootInstId)){
						    productId = Constants.YSJ_PRODUCT;
						}else if(Constants.BBZX_ID.equals(rootInstId)){
						    productId = Constants.BBZX_PRODUCT;
						}else if(Constants.LY_ID.equals(rootInstId)){
						    productId = Constants.LY_PRODUCT;
						}else{
							flag = false;
						}
						if(flag){
							settleSplittingEntry.setSettleType(SettleConstants.ORDER_COLLECTION);
							finanaceEntry.setPaymentAmount(generationPayment.getAmount());
							finanaceEntry.setReferId(generationPayment.getOrderNo());
							errorResponse = paymentInternalService.collectionReturn(finanaceEntry, rootInstId,generationPayment.getUserId(),productId);
							if (errorResponse.isIs_success() == true) {
								settleSplittingEntry.setStatusId(SettleConstants.SETTLE_STU_1);
								transDaysSummary.setStatusId(SettleConstants.SETTLE_STU_1);
								upGenerationPayment.setGeneId(generationPayment.getGeneId());
								upGenerationPayment.setStatusId(3);
								generationPayment.setStatusId(3);
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
						boolean flag = true;
						String productId = "";
						String rootInstId = rootInstIdChange(generationPayment.getOrderNo(),generationPayment.getRootInstCd());
						if (Constants.HT_ID.equals(rootInstId)) {
							productId = Constants.HT_PRODUCT;
						}else if (Constants.HT_CLOUD_ID.equals(rootInstId)) {
							productId = Constants.HT_CHANGDIFANG_ACCOUNT;
						}else if (Constants.KZ_ID.equals(rootInstId)) {
							productId = Constants.KZ_CREDIT_ACCOUNT;
						}else if(Constants.SQSM_ID.equals(rootInstId)){
							productId = Constants.SQSM_PRODUCT;
						}else if(Constants.MZ_ID.equals(rootInstId)){
							productId = Constants.MZ_PRODUCT;
						}else if(Constants.JRD_ID.equals(rootInstId)){
							productId = Constants.JRD_FINANCED_ACCOUNT_PRODUCT;
						}else{
							flag = false;
						}
						if(flag){
							settleSplittingEntry.setSettleType(SettleConstants.ORDER_WITHHOLD);
							finanaceEntry.setPaymentAmount(generationPayment.getAmount());
							finanaceEntry.setReferId(generationPayment.getOrderNo());
							errorResponse = paymentInternalService.withholdReturn(finanaceEntry, rootInstId,generationPayment.getUserId(),productId);
							if (errorResponse.isIs_success() == true) {
								settleSplittingEntry.setStatusId(SettleConstants.SETTLE_STU_1);
								transDaysSummary.setStatusId(SettleConstants.SETTLE_STU_1);
								upGenerationPayment.setGeneId(generationPayment.getGeneId());
								upGenerationPayment.setStatusId(3);
								generationPayment.setStatusId(3);
							} else {
								settleSplittingEntry.setStatusId(SettleConstants.SETTLE_STU_0);
								transDaysSummary.setStatusId(SettleConstants.SETTLE_STU_0);
								settleSplittingEntry.setRemark("结果成功_更新账户失败");
								//throw new AccountException("ORDER_WITHDRAW--ERROR"+generationPayment.getOrderNo());
//					RkylinMailUtil.sendMailThread("账户记账流水失败通知","******************账户"+transOrderInfo.getUserId()+"做扣款-"+deductType+"记账流水数据入库连续失败三次", TransCodeConst.FINANACE_ENTRY_ERROR_TOEMAIL);
							}
					    	transDaysSummary.setTransSumId(generationPayment.getOrderNo());
					    	transDaysSummaryManager.updateTransDaysSummary(transDaysSummary);
						}else{
							settleSplittingEntry.setRemark("未知交易！");
						}
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
						generationPayment.setStatusId(3);
					} else if (SettleConstants.ORDER_WITHDRAW == generationPayment.getOrderType()) {//提现
						settleSplittingEntry.setSettleType(SettleConstants.ORDER_WITHDRAW);
						transDaysSummary.setStatusId(SettleConstants.SETTLE_STU_0);
						transDaysSummary.setTransSumId(generationPayment.getOrderNo());
				    	transDaysSummaryManager.updateTransDaysSummary(transDaysSummary);
					} else if (SettleConstants.ORDER_REPAYMENT == generationPayment.getOrderType() ) {//信用还款
						settleSplittingEntry.setSettleType(SettleConstants.ORDER_REPAYMENT);
						creditRepayment.setStatusId(SettleConstants.SETTLE_STU_2);
						creditRepayment.setOverdueFlag(SettleConstants.SETTLE_STU_1);
						creditRepayment.setCredId(Integer.parseInt(generationPayment.getOrderNo()));
						creditRepaymentManager.saveCreditRepayment(creditRepayment);
						upGenerationPayment.setGeneId(generationPayment.getGeneId());
						upGenerationPayment.setStatusId(3);
						generationPayment.setStatusId(3);
					}else if (SettleConstants.ORDER_LOAN_REPAYMENT == generationPayment.getOrderType() ) {//贷款还款
					    collFailSumAmount+=generationPayment.getAmount();
//						String consid = this.rootInstIdChangeR(generationPayment.getOrderNo(), generationPayment.getRootInstCd());
//						String accDay=dateUtil.getDateTime(generationPayment.getAccountDate(), Constants.DATE_FORMAT_YYYYMMDD).substring(8,10);
//						String repayDay=repaymentDay(generationPayment,consid);
						settleSplittingEntry.setSettleType(SettleConstants.ORDER_REPAYMENT);
//						interestRepayment.setStatusId(SettleConstants.SETTLE_STU_2);
//						interestRepayment.setOverdueFlag2(SettleConstants.SETTLE_STU_1);
//						if (consid.equals(Constants.KZ_ID) && accDay.equals(repayDay) ){
//							interestRepayment.setOverdueFlag2(SettleConstants.SETTLE_STU_0);
//						}
//
//						interestRepayment.setInterId(Integer.parseInt(generationPayment.getOrderNo()));
//						interestRepaymentManager.saveInterestRepayment(interestRepayment);
						transDaysSummary.setStatusId(SettleConstants.SETTLE_STU_0);
						transDaysSummary.setTransSumId(generationPayment.getOrderNo());
				    	transDaysSummaryManager.updateTransDaysSummary(transDaysSummary);
						upGenerationPayment.setGeneId(generationPayment.getGeneId());
						upGenerationPayment.setStatusId(3);
						generationPayment.setStatusId(3);
					}else if (SettleConstants.ORDER_COLLECTION == generationPayment.getOrderType()) {//代收
					    collFailSumAmount+=generationPayment.getAmount();
						settleSplittingEntry.setSettleType(SettleConstants.ORDER_COLLECTION);
						transDaysSummary.setStatusId(SettleConstants.SETTLE_STU_0);
						transDaysSummary.setTransSumId(generationPayment.getOrderNo());
				    	transDaysSummaryManager.updateTransDaysSummary(transDaysSummary);
					} else if (SettleConstants.ORDER_WITHHOLD == generationPayment.getOrderType()) {//代付
						boolean flag = true;
						String rootInstId = rootInstIdChange(generationPayment.getOrderNo(),generationPayment.getRootInstCd());
						if (Constants.HT_ID.equals(rootInstId)) {
						}else if(Constants.HT_CLOUD_ID.equals(rootInstId)){
						} else if (Constants.KZ_ID.equals(rootInstId)) {
						} else if(Constants.MZ_ID.equals(rootInstId)){
						} else if(Constants.SQSM_ID.equals(rootInstId)){
						}else if(Constants.JRD_ID.equals(rootInstId)){
						}
						else {
							flag = false;
						}
						if (flag) {
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
				Map paraMap = new HashMap();
				String errOrder = "";
				// 更新交易表
				if (generationPayment.getSendType() == SettleConstants.SETTLE_STU_1 || generationPayment.getSendType() == SettleConstants.SETTLE_STU_0) {
					//代收代付交易
					if (SettleConstants.ORDER_COLLECTION == generationPayment.getOrderType()
							|| SettleConstants.ORDER_WITHHOLD == generationPayment.getOrderType()
							|| SettleConstants.ORDER_WITHDRAW == generationPayment.getOrderType()
							|| SettleConstants.ORDER_LOAN_REPAYMENT == generationPayment.getOrderType() ) {
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
										paraMap.put("MERCHANT_CODE", tds.getRootInstCd());
										if (generationPayment.getSendType() == SettleConstants.SETTLE_STU_0) {
											paraMap.put("STATUS", TransCodeConst.TRANS_STATUS_PAY_SUCCEED);
											settlementManager.updatetransstatus(paraMap);
										} else if (generationPayment.getSendType() == SettleConstants.SETTLE_STU_1) {
										    if (SettleConstants.ORDER_COLLECTION == generationPayment.getOrderType()||
										            SettleConstants.ORDER_LOAN_REPAYMENT == generationPayment.getOrderType()) {
										        upGenerationPayment.setGeneId(generationPayment.getGeneId());
                                                upGenerationPayment.setStatusId(3);
                                                generationPayment.setStatusId(3);
                                                paraMap.put("STATUS", TransCodeConst.TRANS_STATUS_REVERSAL);
                                                settlementManager.updatetransstatus(paraMap);
                                                continue;
                                            }
											// 抹账处理
											transOrderInfo = new TransOrderInfo();
											transOrderInfo.setOrderNo(redisIdGenerator.createRequestNo());
											transOrderInfo.setOrderPackageNo(orderno);
											transOrderInfo.setMerchantCode(tds.getRootInstCd());
											errorResponse = paymentInternalService.wipeAccount(transOrderInfo);
											if (errorResponse.isIs_success() == true) {
												upGenerationPayment.setGeneId(generationPayment.getGeneId());
												upGenerationPayment.setStatusId(3);
												generationPayment.setStatusId(3);
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
													generationPayment.setStatusId(3);
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
				if(SettleConstants.ORDER_LOAN_REPAYMENT == generationPayment.getOrderType() ){//贷款还款
					rootInstIdInfor = rootInstIdChange(generationPayment.getOrderNo(),generationPayment.getRootInstCd());
					settleSplittingEntry.setRootInstCd(rootInstIdInfor);
				}else if(SettleConstants.ORDER_COLLECTION == generationPayment.getOrderType() ||
						SettleConstants.ORDER_WITHHOLD == generationPayment.getOrderType()){//代收或代付
					settleSplittingEntry.setRootInstCd(rootInstIdChange(generationPayment.getOrderNo(),generationPayment.getRootInstCd()));
				}else{//上记以外
					settleSplittingEntry.setRootInstCd(generationPayment.getRootInstCd());
				}
				settleSplittingEntry.setAccountDate(generationPayment.getAccountDate());
				settleSplittingEntry.setUserId(generationPayment.getUserId());
				settleSplittingEntry.setAmount(generationPayment.getAmount());
				settleSplittingEntry.setOrderNo(generationPayment.getOrderNo());
				settleSplittingEntryManager.saveSettleSplittingEntry(settleSplittingEntry);
	 			logger.info("该条交易处理结束："+generationPayment.getOrderNo());
			} catch (AccountException e) {
				e.printStackTrace();
				logger.error("该条交易："+generationPayment.getOrderNo()+"  处理失败=======e.getMessage()==="+e.getMessage());
			}
		}
        //将代收失败的生成流水对冲其它应收款和备付金
        if (collFailSumAmount!=0l) { 
            List<com.rkylin.wheatfield.bean.User> userList = new ArrayList<com.rkylin.wheatfield.bean.User>();
            com.rkylin.wheatfield.bean.User user = new com.rkylin.wheatfield.bean.User();
            user.setInstCode(Constants.FN_ID);
            user.setUserId(TransCodeConst.THIRDPARTYID_FNZZH);
            user.setProductId(Constants.FN_PRODUCT);
            user.setAmount(collFailSumAmount);
            user.setStatus(0);
            user.setFinAccountId(generationList.get(0).getProcessResult().substring(0,20));
            userList.add(user);
            com.rkylin.wheatfield.bean.User user2 = new com.rkylin.wheatfield.bean.User();
            BeanMapper.copy(user, user2);
            user2.setUserId(TransCodeConst.THIRDPARTYID_QTYSKZH);
            userList.add(user2);
            CommonResponse res = semiAutomatizationServiceApi.operateFinAccounts(userList);
            if (!CodeEnum.SUCCESS.getCode().equals(res.getCode())) {
                logger.info("将代收失败的生成流水对冲备付金和其它应收款 msg="+res.getMsg()); 
            }
        }		
    	//代收付结果更新订单系统 20150603 start
    	Map<String,String> rtnMapUpOrder= this.updateOrderSysByGenPay(generationList);
    	if(!"P0".equals(rtnMapUpOrder.get("errCode"))){
    		rtnMap.put("errCode", rtnMapUpOrder.get("errCode"));
    		rtnMap.put("errMsg", "更新订单系统失败：" + rtnMapUpOrder.get("errMsg"));
			logger.error("更新订单系统失败：" + rtnMapUpOrder.get("errMsg"));
			return rtnMap;
   		}
		logger.info("通过代扣结果更新用户余额 ————————————END————————————");
		return rtnMap;
	}

	@Override
	@Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
	public Map<String,String> updateCreditAccountSec() {
		logger.info("通过代扣结果更新用户余额 ————————————START————————————");
		Map rtnMap = new HashMap();
		rtnMap.put("errCode", "0000");
		rtnMap.put("errMsg", "成功");

    	ParameterInfoQuery keyList =  new ParameterInfoQuery();
    	keyList.setParameterCode(SettleConstants.DAYEND);
    	List<ParameterInfo> parameterInfo = parameterInfoManager.queryList(keyList);
    	if (!"0".equals(parameterInfo.get(0).getParameterValue())) {
    		rtnMap.put("errCode", "0001");
    		rtnMap.put("errMsg", "日终没有正常结束！");
    		RkylinMailUtil.sendMailThread("清洁算开始异常","日终没有正常结束，不能开始清洁算操作", "21401233@qq.com");
    		return rtnMap;
    	}
    	try {
			// 取得当账期成功的代收付结果
			GenerationPaymentQuery generationPaymentQuery = new  GenerationPaymentQuery();
			generationPaymentQuery.setStatusId(SettleConstants.SETTLE_STU_1);
			List<GenerationPayment> rtnList=generationPaymentManager.queryList(generationPaymentQuery);
			FinanaceEntry finanaceEntry = new FinanaceEntry();
			ErrorResponse errorResponse = null;
			SettleSplittingEntry settleSplittingEntry = new SettleSplittingEntry();
	    	List<TransDaysSummary> tdslist = new ArrayList<TransDaysSummary>();
	    	TransDaysSummaryQuery tdsq = new TransDaysSummaryQuery();
			TransDaysSummary transDaysSummary = new TransDaysSummary();
			CreditRepayment creditRepayment = new CreditRepayment();
			InterestRepayment interestRepayment = new InterestRepayment();
			GenerationPayment upGenerationPayment = new GenerationPayment();
	    	TransOrderInfo transOrderInfo = new TransOrderInfo();
	    	try {
				// 处理代收付交易TO账户系统
				for (GenerationPayment generationPayment:rtnList) {
					logger.info("该条交易处理开始："+generationPayment.getOrderNo());
					if(generationPayment.getOrderType()!=SettleConstants.ORDER_COLLECTION && generationPayment.getOrderType()!=SettleConstants.ORDER_LOAN_REPAYMENT){
						logger.info("订单"+generationPayment.getOrderNo()+"非代收交易类型，人工补账");
						continue;
					}
					settleSplittingEntry = new SettleSplittingEntry();
					transDaysSummary = new TransDaysSummary();
					creditRepayment = new CreditRepayment();
					interestRepayment = new InterestRepayment();
					upGenerationPayment = new GenerationPayment();
					 if (generationPayment.getSendType() == null || generationPayment.getSendType() == 2) {
							continue;
					 } else if (generationPayment.getSendType() == SettleConstants.SETTLE_STU_0) {
						// 分为三种情况----1债券包,2提现,3还款
						if (SettleConstants.ORDER_BOND_PACKAGE == generationPayment.getOrderType()) {//债券包
							settleSplittingEntry.setSettleType(SettleConstants.SETTLE_TYPE_1);
							finanaceEntry.setPaymentAmount(generationPayment.getAmount());
							finanaceEntry.setReferId(generationPayment.getOrderNo());
							String rootInstId = rootInstIdChange(generationPayment.getOrderNo(),generationPayment.getRootInstCd());
							errorResponse = paymentInternalService.rightsPackageReturn(finanaceEntry, rootInstId);
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
						} else if (SettleConstants.ORDER_REPAYMENT == generationPayment.getOrderType() ) {//信用还款
							settleSplittingEntry.setSettleType(SettleConstants.SETTLE_TYPE_3);
							finanaceEntry.setPaymentAmount(generationPayment.getAmount());
							finanaceEntry.setReferId(generationPayment.getOrderNo());
							String rootInstId = rootInstIdChange(generationPayment.getOrderNo(),generationPayment.getRootInstCd());
							errorResponse = paymentInternalService.refundReturn(finanaceEntry, generationPayment.getUserId(), rootInstId);
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
				//				RkylinMailUtil.sendMailThread("账户记账流水失败通知","******************账户"+transOrderInfo.getUserId()+"做扣款-"+deductType+"记账流水数据入库连续失败三次", TransCodeConst.FINANACE_ENTRY_ERROR_TOEMAIL);
							}
							creditRepayment.setCredId(Integer.parseInt(generationPayment.getOrderNo()));
							creditRepayment.setRepaymentRepaidDate(new Date());
							creditRepaymentManager.saveCreditRepayment(creditRepayment);
						} else if (SettleConstants.ORDER_LOAN_REPAYMENT == generationPayment.getOrderType() ) {//贷款还款
							boolean flag = true;
							String productId = "";
							String productId_kz = "";
							String rootInstId = rootInstIdChangeR(generationPayment.getOrderNo(),generationPayment.getRootInstCd());
							if (Constants.HT_ID.equals(rootInstId)) {
								productId = Constants.HT_CREDIT_ACCOUNT;
								productId_kz = Constants.HT_PRODUCT;
							} else if (Constants.HT_CLOUD_ID.equals(rootInstId)) {
								productId = Constants.HT_CHANGDIFANG_ACCOUNT;
							} else if (Constants.FN_ID.equals(rootInstId)) {
								productId = Constants.FN_PRODUCT;
							} else if (Constants.KZ_ID.equals(rootInstId)) {
								productId = Constants.KZ_CREDIT_ACCOUNT;
								productId_kz = Constants.KZ_PRODUCT;
							}else{
								flag = false;
							}
							if(flag){
								settleSplittingEntry.setSettleType(SettleConstants.ORDER_LOAN_REPAYMENT);
								transOrderInfo = new TransOrderInfo();
								transOrderInfo.setUserId(generationPayment.getUserId());
								transOrderInfo.setAmount(generationPayment.getAmount());
								transOrderInfo.setFuncCode(TransCodeConst.PAYMENT_COLLECTION);
								transOrderInfo.setMerchantCode(rootInstId);
								transOrderInfo.setOrderAmount(generationPayment.getAmount());
								transOrderInfo.setOrderCount(1);
								transOrderInfo.setOrderDate(generationPayment.getAccountDate());
								transOrderInfo.setOrderNo(generationPayment.getOrderNo());
								transOrderInfo.setRequestTime(generationPayment.getCreatedTime());
								transOrderInfo.setStatus(4);
								transOrderInfo.setUserFee(Long.valueOf("0"));

								errorResponse = paymentAccountService.collection(transOrderInfo, productId_kz);
								if (errorResponse.isIs_success() == true) {
									finanaceEntry.setPaymentAmount(generationPayment.getAmount());
									finanaceEntry.setReferId(generationPayment.getOrderNo());
									errorResponse = paymentInternalService.collectionReturn(finanaceEntry, rootInstId,generationPayment.getUserId(),productId);
									if (errorResponse.isIs_success() == true) {
										settleSplittingEntry.setStatusId(SettleConstants.SETTLE_STU_1);
										interestRepayment.setStatusId(SettleConstants.SETTLE_STU_1);
										upGenerationPayment.setGeneId(generationPayment.getGeneId());
										upGenerationPayment.setStatusId(3);
									} else {
										settleSplittingEntry.setStatusId(SettleConstants.SETTLE_STU_0);
										interestRepayment.setStatusId(SettleConstants.SETTLE_STU_0);
										settleSplittingEntry.setRemark("结果成功_更新账户失败");
										//throw new AccountException("ORDER_WITHDRAW--ERROR"+generationPayment.getOrderNo());
					//					RkylinMailUtil.sendMailThread("账户记账流水失败通知","******************账户"+transOrderInfo.getUserId()+"做扣款-"+deductType+"记账流水数据入库连续失败三次", TransCodeConst.FINANACE_ENTRY_ERROR_TOEMAIL);
									}
								} else {
									settleSplittingEntry.setStatusId(SettleConstants.SETTLE_STU_0);
									interestRepayment.setStatusId(SettleConstants.SETTLE_STU_0);
									settleSplittingEntry.setRemark("结果成功_更新账户失败");
								}
							}
							interestRepayment.setInterId(Integer.parseInt(generationPayment.getOrderNo()));
							interestRepayment.setRepaidRepaymentDate(new Date());
							interestRepaymentManager.saveInterestRepayment(interestRepayment);
						} else if (SettleConstants.ORDER_COLLECTION == generationPayment.getOrderType()) {//代收
							boolean flag = true;
							String productId = "";
							String rootInstId = rootInstIdChange(generationPayment.getOrderNo(),generationPayment.getRootInstCd());
							if (Constants.HT_ID.equals(rootInstId)) {
								productId = Constants.HT_PRODUCT;
							} else if (Constants.HT_CLOUD_ID.equals(rootInstId)) {
								productId = Constants.HT_CHANGDIFANG_ACCOUNT;
							} else if (Constants.FN_ID.equals(rootInstId)) {
								productId = Constants.FN_PRODUCT;
							} else if (Constants.KZ_ID.equals(rootInstId)) {
								productId = Constants.KZ_PRODUCT;
							}else if(Constants.SQSM_ID.equals(rootInstId)){
								productId = Constants.SQSM_PRODUCT;
							}else if(Constants.YSJ_ID.equals(rootInstId)){
							    productId = Constants.YSJ_PRODUCT;
							}else{
								flag = false;
							}
							if(flag){
								settleSplittingEntry.setSettleType(SettleConstants.ORDER_COLLECTION);
								finanaceEntry.setPaymentAmount(generationPayment.getAmount());
								finanaceEntry.setReferId(generationPayment.getOrderNo());
								errorResponse = paymentInternalService.collectionReturn(finanaceEntry, rootInstId,generationPayment.getUserId(),productId);
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
							boolean flag = true;
							String productId = "";
							String rootInstId = rootInstIdChange(generationPayment.getOrderNo(),generationPayment.getRootInstCd());
							if (Constants.HT_ID.equals(rootInstId)) {
								productId = Constants.HT_PRODUCT;
							}else if (Constants.HT_CLOUD_ID.equals(rootInstId)) {
								productId = Constants.HT_CHANGDIFANG_ACCOUNT;
							}else if (Constants.KZ_ID.equals(rootInstId)) {
								productId = Constants.KZ_CREDIT_ACCOUNT;
							}else if(Constants.SQSM_ID.equals(rootInstId)){
								productId = Constants.SQSM_PRODUCT;
							}else if(Constants.MZ_ID.equals(rootInstId)){
								productId = Constants.MZ_PRODUCT;
							}else if(Constants.JRD_ID.equals(rootInstId)){
								productId = Constants.JRD_FINANCED_ACCOUNT_PRODUCT;
							}else{
								flag = false;
							}
							if(flag){
								settleSplittingEntry.setSettleType(SettleConstants.ORDER_WITHHOLD);
								finanaceEntry.setPaymentAmount(generationPayment.getAmount());
								finanaceEntry.setReferId(generationPayment.getOrderNo());
								errorResponse = paymentInternalService.withholdReturn(finanaceEntry, rootInstId,generationPayment.getUserId(),productId);
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
							}else{
								settleSplittingEntry.setRemark("未知交易！");
							}
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
						} else if (SettleConstants.ORDER_REPAYMENT == generationPayment.getOrderType() ) {//信用还款
							settleSplittingEntry.setSettleType(SettleConstants.ORDER_REPAYMENT);
							creditRepayment.setStatusId(SettleConstants.SETTLE_STU_2);
							creditRepayment.setOverdueFlag(SettleConstants.SETTLE_STU_1);
							creditRepayment.setCredId(Integer.parseInt(generationPayment.getOrderNo()));
							creditRepaymentManager.saveCreditRepayment(creditRepayment);
							upGenerationPayment.setGeneId(generationPayment.getGeneId());
							upGenerationPayment.setStatusId(3);
						}else if (SettleConstants.ORDER_LOAN_REPAYMENT == generationPayment.getOrderType() ) {//贷款还款
							String consid = this.rootInstIdChangeR(generationPayment.getOrderNo(), generationPayment.getRootInstCd());
							String accDay=dateUtil.getDateTime(generationPayment.getAccountDate(), Constants.DATE_FORMAT_YYYYMMDD).substring(8,10);
							String repayDay=repaymentDay(generationPayment,consid);
							settleSplittingEntry.setSettleType(SettleConstants.ORDER_REPAYMENT);
							interestRepayment.setStatusId(SettleConstants.SETTLE_STU_2);
							interestRepayment.setOverdueFlag2(SettleConstants.SETTLE_STU_1);
							if (consid.equals(Constants.KZ_ID) && accDay.equals(repayDay) ){
								interestRepayment.setOverdueFlag2(SettleConstants.SETTLE_STU_0);
							}

							interestRepayment.setInterId(Integer.parseInt(generationPayment.getOrderNo()));
							interestRepaymentManager.saveInterestRepayment(interestRepayment);
							upGenerationPayment.setGeneId(generationPayment.getGeneId());
							upGenerationPayment.setStatusId(3);
						}else if (SettleConstants.ORDER_COLLECTION == generationPayment.getOrderType()) {//代收
							settleSplittingEntry.setSettleType(SettleConstants.ORDER_COLLECTION);
							transDaysSummary.setStatusId(SettleConstants.SETTLE_STU_0);
							transDaysSummary.setTransSumId(generationPayment.getOrderNo());
					    	transDaysSummaryManager.updateTransDaysSummary(transDaysSummary);
						} else if (SettleConstants.ORDER_WITHHOLD == generationPayment.getOrderType()) {//代付
							boolean flag = true;
							String rootInstId = rootInstIdChange(generationPayment.getOrderNo(),generationPayment.getRootInstCd());
							if (Constants.HT_ID.equals(rootInstId)) {
							}else if(Constants.HT_CLOUD_ID.equals(rootInstId)){
							} else if (Constants.KZ_ID.equals(rootInstId)) {
							} else if(Constants.MZ_ID.equals(rootInstId)){
							} else if(Constants.SQSM_ID.equals(rootInstId)){
							}else if(Constants.JRD_ID.equals(rootInstId)){
							}
							else {
								flag = false;
							}
							if (flag) {
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
			    	Map paraMap = new HashMap();
			    	String errOrder = "";
			    	// 更新交易表
			    	if (generationPayment.getSendType() == SettleConstants.SETTLE_STU_1 || generationPayment.getSendType() == SettleConstants.SETTLE_STU_0) {
			    		//代收代付交易
			    		if (SettleConstants.ORDER_COLLECTION == generationPayment.getOrderType()
			    				|| SettleConstants.ORDER_WITHHOLD == generationPayment.getOrderType()
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
											paraMap.put("MERCHANT_CODE", tds.getRootInstCd());
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
					if(SettleConstants.ORDER_LOAN_REPAYMENT == generationPayment.getOrderType() ){//贷款还款
						settleSplittingEntry.setRootInstCd(rootInstIdChangeR(generationPayment.getOrderNo(),generationPayment.getRootInstCd()));
					}else if(SettleConstants.ORDER_COLLECTION == generationPayment.getOrderType() ||
							SettleConstants.ORDER_WITHHOLD == generationPayment.getOrderType()){//代收或代付
						settleSplittingEntry.setRootInstCd(rootInstIdChange(generationPayment.getOrderNo(),generationPayment.getRootInstCd()));
					}else{//上记以外
						settleSplittingEntry.setRootInstCd(generationPayment.getRootInstCd());
					}
					settleSplittingEntry.setAccountDate(generationPayment.getAccountDate());
					settleSplittingEntry.setUserId(generationPayment.getUserId());
					settleSplittingEntry.setAmount(generationPayment.getAmount());
					settleSplittingEntry.setOrderNo(generationPayment.getOrderNo());
			    	settleSplittingEntryManager.saveSettleSplittingEntry(settleSplittingEntry);
		 			logger.info("该条交易处理结束："+generationPayment.getOrderNo());
				}
	    	} catch (Exception z1) {
				rtnMap.put("errCode", "0002");
				rtnMap.put("errMsg", "处理代收付交易TO账户系统失败！");
    			logger.error("处理代收付交易TO账户系统失败！"+z1.getMessage());
				return rtnMap;
	    	}
	    	//代收付结果更新订单系统 20150603 start
	    	Map<String,String> rtnMapUpOrder= this.updateOrder();
	    	if(!"P0".equals(rtnMapUpOrder.get("errCode"))){
	    		rtnMap.put("errCode", rtnMapUpOrder.get("errCode"));
	    		rtnMap.put("errMsg", "更新订单系统失败：" + rtnMapUpOrder.get("errMsg"));
    			logger.error("更新订单系统失败：" + rtnMapUpOrder.get("errMsg"));
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

	//通过代收付结果更新订单系统
	@Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
	public Map<String, String> updateOrderSysByGenPay(List<GenerationPayment> rtnList) {
		logger.info("通过代收付结果更新订单系统  ————————————Start————————————传入  rtnList.size()="+rtnList.size());
		Map<String,String> rtnMap = new HashMap<String,String>();
		Map<String,String> mapNo = Maps.newHashMap();//key:交易订单号  value:代收付订单号
    	List<Map<String,String>> upList = new ArrayList();
		try{
	        if(rtnList.size()>0){
//	        	logger.info("更新订单系统的数据为:" + rtnList.size() + "件");
	        	List<OrderPayment> orderPayments = new ArrayList<OrderPayment>();
	        	for(GenerationPayment tempBean : rtnList){
	            	logger.info("当前处理订单号(代收付表) orderNo=" + tempBean.getOrderNo()+",SendType="+tempBean.getSendType()+",statusId="+tempBean.getStatusId()+",OrderType="+tempBean.getOrderType());
	        		if (tempBean.getSendType()!=SettleConstants.SEND_NORMAL && tempBean.getSendType()!=SettleConstants.SEND_DEFEAT) {
						continue;
					}
	        		if (tempBean.getStatusId()!=3) {
	        			continue;
	        		}
	        		if (tempBean.getOrderType()!=SettleConstants.ORDER_WITHDRAW && tempBean.getOrderType()!=SettleConstants.ORDER_COLLECTION && tempBean.getOrderType()!=SettleConstants.ORDER_WITHHOLD ) {
						continue;
					}
	        		TransDaysSummaryQuery tdsq = new TransDaysSummaryQuery();
	    			tdsq.setTransSumId(tempBean.getOrderNo());
	    			List<TransDaysSummary> tdslist = transDaysSummaryManager.queryList(tdsq);
//	            	logger.info("当前处理订单号(代收付表) orderNo=" + tempBean.getOrderNo()+"取得summary件数:" + tdslist.size());
	    			if(tdslist.size()>0){
	    				for(TransDaysSummary tds : tdslist){
//            				logger.info("当前处理订单号(代收付表) orderNo=" + tempBean.getOrderNo()+"取得summary  中  SummaryOrders=" + tds.getSummaryOrders());
	    					String[] orderNo = tds.getSummaryOrders().split(",");
	    					for(int i=0;i<orderNo.length;i++){
	    						OrderPayment orderPayment = new OrderPayment();
	            				orderPayment.setPaymentId(orderNo[i]);
	            				mapNo.put(orderNo[i],tempBean.getOrderNo());
	            				orderPayment.setPaymentTypeId(tempBean.getBussinessCode());
	            				orderPayment.setStatusId(tempBean.getSendType().toString());
	            				orderPayment.setEndTime(tempBean.getUpdatedTime());
	            				orderPayments.add(orderPayment);
	    					}
	    				}
	    			}else{
	    				logger.info("当前订单号:" + tempBean.getOrderNo() + "找不到汇总数据");
	    				Map<String,String> upMap = Maps.newHashMap();
		        		upMap.put("remark","找不到汇总数据");
		        		upMap.put("orderNo",tempBean.getOrderNo());
	    				upList.add(upMap);
	    			}
	        	}

	        	logger.info("准备更新订单系统的件数:" + orderPayments.size());
	        	Map<String,Object> rtnMapUp = Maps.newHashMap();
	        	rtnMapUp.put("paymentList", orderPayments);
	        	rtnMapUp = settlementToOrderService.updateBatchPaymentStatus(rtnMapUp);
	        	//更新有失败的场合，把数据更新到分润表
	        	if(!"true".equals(rtnMapUp.get("issuccess"))){
					List<OrderPayment> rtnOrderPayments = (List<OrderPayment>) rtnMapUp.get("paymentList");
					logger.info("更新失败订单系统的件数：" + rtnOrderPayments.size());
					int i=0;
					String errTradeNo = "";
					//代收付订单号交易相同的数据进行汇总
					for (OrderPayment temp : rtnOrderPayments) {
						i++;
						String ordNext = "";
						String ord =  mapNo.get(temp.getPaymentId());
						if(i !=rtnOrderPayments.size() ){
							ordNext = mapNo.get(rtnOrderPayments.get(i).getPaymentId());
						}
						errTradeNo =errTradeNo + temp.getPaymentId() + ",";
						if(!ordNext.equals(ord)){
							Map<String, String> upMap = Maps.newHashMap();
							if("1".equals(temp.getStatusId())){
								upMap.put("remark", "找不到订单系统数据:" + errTradeNo);
							}else{
								upMap.put("remark", "订单系统数据状态已更新:" + errTradeNo);
							}
							upMap.put("orderNo", ord);
							upList.add(upMap);
							errTradeNo = "";
						}
					}
				}
				// 更新分润结果表
				if (upList.size() > 0) {
		        	logger.info("准备更新分润表数据件数:" + upList.size());
					int updateCount = settleSplittingEntryManager.batchUpdateByOrderNo(upList);
					rtnMap.put("errMsg", "更新订单系统结束，更新失败件数为：" + updateCount);
					rtnMap.put("errCode", "P0");
					return rtnMap;
				}
	        }else{
	    		rtnMap.put("errMsg", "满足更新订单系统的数据为0件");
	    		rtnMap.put("errCode", "P0");
	    		return rtnMap;
	        }
		}catch (Exception ex){
			rtnMap.put("errMsg", "更新订单系统异常发生：" + ex.getMessage());
			rtnMap.put("errCode", "P1");
			return rtnMap;
		}
		logger.info("通过代收付结果更新订单系统  ————————————End————————————");
		rtnMap.put("errMsg", "更新订单系统成功");
		rtnMap.put("errCode", "P0");
		return rtnMap;
	}

	//通过代收付结果更新订单系统
	@Override
	@Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
	public Map<String, String> updateOrder() {
		// TODO Auto-generated method stub
		logger.info("通过代收付结果更新订单系统  ————————————Start————————————");
		Map<String,String> rtnMap = new HashMap<String,String>();
		GenerationPaymentQuery generationPaymentQuery = new  GenerationPaymentQuery();
		generationPaymentQuery.setSendType(9);//sqlmap中 in(0,1)(支付成功 ，支付失败)
		generationPaymentQuery.setOrderType(9);//sqlmap中 in(2,5,6)(代收，代付，提现)
		generationPaymentQuery.setStatusId(3);//处理账户系统后
		Map<String,String> mapNo = Maps.newHashMap();//key:交易订单号  value:代收付订单号
    	List<Map<String,String>> upList = new ArrayList();
		try{
			List<GenerationPayment> rtnList=generationPaymentManager.queryList(generationPaymentQuery);
	        if(rtnList.size()>0){
	        	logger.info("支付成功且更新账户的数据为:" + rtnList.size() + "件");
	        	List<OrderPayment> orderPayments = new ArrayList<OrderPayment>();
	        	for(GenerationPayment tempBean : rtnList){
	            	logger.info("当前处理订单号:" + tempBean.getOrderNo());
	        		TransDaysSummaryQuery tdsq = new TransDaysSummaryQuery();
	    			tdsq.setTransSumId(tempBean.getOrderNo());
	    			List<TransDaysSummary> tdslist = transDaysSummaryManager.queryList(tdsq);
	            	logger.info("取得summary件数:" + tdslist.size());
	    			if(tdslist.size()>0){
	    				for(TransDaysSummary tds : tdslist){
	    					String[] orderNo = tds.getSummaryOrders().split(",");
	    					for(int i=0;i<orderNo.length;i++){
	    						OrderPayment orderPayment = new OrderPayment();
	            				orderPayment.setPaymentId(orderNo[i]);
	            				mapNo.put(orderNo[i],tempBean.getOrderNo());
	            				orderPayment.setPaymentTypeId(tempBean.getBussinessCode());
	            				orderPayment.setStatusId(tempBean.getSendType().toString());
	            				orderPayment.setEndTime(tempBean.getUpdatedTime());
	            				orderPayments.add(orderPayment);
	    					}
	    				}
	    			}else{
	    				logger.info("当前订单号:" + tempBean.getOrderNo() + "找不到汇总数据");
	    				Map<String,String> upMap = Maps.newHashMap();
		        		upMap.put("remark","找不到汇总数据");
		        		upMap.put("orderNo",tempBean.getOrderNo());
	    				upList.add(upMap);
	    			}
	        	}

	        	logger.info("准备更新订单系统的件数:" + orderPayments.size());
	        	Map<String,Object> rtnMapUp = Maps.newHashMap();
	        	rtnMapUp.put("paymentList", orderPayments);
	        	rtnMapUp = settlementToOrderService.updateBatchPaymentStatus(rtnMapUp);
	        	//更新有失败的场合，把数据更新到分润表
	        	if(!"true".equals(rtnMapUp.get("issuccess"))){
					List<OrderPayment> rtnOrderPayments = (List<OrderPayment>) rtnMapUp.get("paymentList");
					logger.info("更新失败订单系统的件数：" + rtnOrderPayments.size());
					int i=0;
					String errTradeNo = "";
					//代收付订单号交易相同的数据进行汇总
					for (OrderPayment temp : rtnOrderPayments) {
						i++;
						String ordNext = "";
						String ord =  mapNo.get(temp.getPaymentId());
						if(i !=rtnOrderPayments.size() ){
							ordNext = mapNo.get(rtnOrderPayments.get(i).getPaymentId());
						}
						errTradeNo =errTradeNo + temp.getPaymentId() + ",";
						if(!ordNext.equals(ord)){
							Map<String, String> upMap = Maps.newHashMap();
							if("1".equals(temp.getStatusId())){
								upMap.put("remark", "找不到订单系统数据:" + errTradeNo);
							}else{
								upMap.put("remark", "订单系统数据状态已更新:" + errTradeNo);
							}
							upMap.put("orderNo", ord);
							upList.add(upMap);
							errTradeNo = "";
						}
					}
				}
				// 更新分润结果表
				if (upList.size() > 0) {
		        	logger.info("准备更新分润表数据件数:" + upList.size());
					int updateCount = settleSplittingEntryManager.batchUpdateByOrderNo(upList);
					rtnMap.put("errMsg", "更新订单系统结束，更新失败件数为：" + updateCount);
					rtnMap.put("errCode", "P0");
					return rtnMap;
				}
	        }else{
	    		rtnMap.put("errMsg", "满足更新订单系统的数据为0件");
	    		rtnMap.put("errCode", "P0");
	    		return rtnMap;
	        }
		}catch (Exception ex){
			rtnMap.put("errMsg", "更新订单系统异常发生：" + ex.getMessage());
			rtnMap.put("errCode", "P1");
			return rtnMap;
		}
		logger.info("通过代收付结果更新订单系统  ————————————End————————————");
		rtnMap.put("errMsg", "更新订单系统成功");
		rtnMap.put("errCode", "P0");
		return rtnMap;
	}
	//代收付商户号转换
	private String rootInstIdChange(String orderNo,String rootInstIdBe){
		TransDaysSummaryQuery tdsq = new TransDaysSummaryQuery();
		tdsq.setTransSumId(orderNo);
		List<TransDaysSummary> tdslist = transDaysSummaryManager.queryList(tdsq);
		if(null == tdslist || tdslist.size()==0){
			return rootInstIdBe;
		}else{
			return tdslist.get(0).getRootInstCd();
		}
	}

	//贷款还款时 商户号转换
	private String rootInstIdChangeR(String orderNo,String rootInstIdBe){
		InterestRepaymentQuery irq = new InterestRepaymentQuery();
		irq.setInterId(Integer.valueOf(orderNo));
		List<InterestRepayment> irlist = interestRepaymentManager.queryList(irq);
		if(null == irlist || irlist.size()==0){
			return rootInstIdBe;
		}else{
			return irlist.get(0).getRootInstCd();
		}
	}

	/**
	 * 通知分润,然后发送邮件
	 * @param invoicedate
	 * @param rootInstCd
	 * @return
	 */
	public Map<String, String> notifyProfitLogic(String invoicedate,String rootInstCd){
		logger.info("--------------通知分润：{invoicedate："+invoicedate +", rootInstCd:"+rootInstCd+"}----------------------");
		Map<String, String> result = this.profitLogic.profitLogic(invoicedate, rootInstCd);
		logger.info("--------------分润结果：{code："+result.get("errCode") +", msg:"+result.get("errMsg")+"}----------------------");
		logger.info("---------------开始发送邮件--------------------");
		RkylinMailUtil.sendMail("分润结果", "code："+result.get("errCode") +", msg:"+result.get("errMsg"), new String[]{"wangmingsheng@rkylin.com.cn","huzijian@rkylin.com.cn","sunruibin@rkylin.com.cn","wanghongliang@rkylin.com.cn"});
		logger.info("---------------发送邮件结束--------------------");
		return result;
	}

	//日批上传还款明细  棉庄
	@Override
	@Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
	public Map<String, String> uploadDeductFile(String rootInstId,String providerId) {
		// TODO Auto-generated method stub
		HashMap<String,String> rtnMap = new HashMap<String,String>();
		String accountDateS = "";
		//获取账期
		try {
			//获取T日账期
			accountDateS = operationServive.getAccountDate().replace("-","");
			//accountDateLast = dateUtil.parse(accountDateS , "eye-MM-dd");
		} catch (Exception e) {
			rtnMap.put("errMsg", "账期取得失败");
			rtnMap.put("errCode", "P1");
			return rtnMap;
		}
		if(null == accountDateS ){
			rtnMap.put("errMsg", "账期取得为0件");
			rtnMap.put("errCode", "P2");
			return rtnMap;
		}
		//上传明细取得
		Map<String,Object> paraMap = new HashMap<String,Object>();
		paraMap.put("accountDate", accountDateS);
		paraMap.put("merchantCode", rootInstId);
		paraMap.put("funcCode", TransCodeConst.CREDIT_CONSUME);
		paraMap.put("providerId", providerId);
//paraMap.put("status", "4");
		List<Map<String,Object>> rtnList = settlementManager.selectDeduct(paraMap);
		logger.debug("取得代扣明细件数:" + rtnList.size());
		if(rtnList.size() > 0){
			List txtList = new ArrayList();
			for (Map<String,Object> tmpbean:rtnList) {
				paraMap = new HashMap();
				//上传文件的列数为14，列数改变时须修改
				for(int i = 1; i < 15; i ++){
					paraMap.put("F_" + i, tmpbean.get("colum" + i));//账期
				}
				txtList.add(paraMap);
			}
			//上传文件名和路径的设定
			String path = SettleConstants.FILE_UP_PATH + accountDateS + File.separator;
			File filePath = new File(path);
			if (!filePath.exists()) {
				filePath.mkdirs();
			}
			//rtnList.get(0).get("colum6") ： 转换后的商户号 ，文件中的第6列
			path=path + rtnList.get(0).get("colum6") + "_DEDUCT_"+accountDateS+ "_" + PartyCodeUtil.getRandomCode()+".csv";
			//上传文件的配置设定
			Map configMap = new HashMap();
			configMap.put("FILE", path);
			String Key = userProperties.getProperty("P2P_PUBLIC_KEY");
			try {
				SimpleDateFormat f1= new SimpleDateFormat("yyyyMMdd");
				TxtWriter.WriteTxt(txtList, configMap, SettleConstants.DEDT_SPLIT2,"UTF-8");
				UploadAndDownLoadUtils.uploadFile(path, 3,
												  f1.parse(accountDateS),
						                          settlementLogic.getBatchNo(f1.parse(accountDateS), SettleConstants.ROP_COLLECTION_BATCH_CODE, rootInstId),
						                          SettleConstants.FILE_XML,Key,0,
						                          userProperties.getProperty("FSAPP_KEY"),
						                          userProperties.getProperty("FSDAPP_SECRET"),
						                          userProperties.getProperty("FSROP_URL"));
			} catch (Exception e) {
				rtnMap.put("errMsg", "上传代扣明细文件发生异常：" + e.getMessage());
				rtnMap.put("errCode", "P3");
				return rtnMap;
			}
		}else{
			rtnMap.put("errMsg", "当日代扣明细件数为0件");
			rtnMap.put("errCode", "P0");
			return rtnMap;
		}
		rtnMap.put("errMsg", "当日代扣明细文件上传成功");
		rtnMap.put("errCode", "P0");
		return rtnMap;
	}

	//贷款的还款日取得：需通过机构号和用户号去授信结果表＋费率模版表取得
	private String repaymentDay(GenerationPayment generationPayment,String consid) {
		String repayDay = "";
		CreditApprovalInfoQuery creditApprovalInfoQuery = new CreditApprovalInfoQuery();
		CreditRateTemplateQuery creditRateTemplateQuery = new CreditRateTemplateQuery();
		creditApprovalInfoQuery.setRootInstCd(consid);
		creditApprovalInfoQuery.setUserId(generationPayment.getUserId());
		List<CreditApprovalInfo> creditList = creditApprovalInfoManager.queryList(creditApprovalInfoQuery);
		if (creditList.size() > 0){
			creditRateTemplateQuery.setRateId(creditList.get(0).getRateId());
			List<CreditRateTemplate> creditRateTemplateList = creditRateTemplateManager.queryList(creditRateTemplateQuery);
			if (creditRateTemplateList.size() > 0){
				repayDay = creditRateTemplateList.get(0).getRepaymentDay();
			}
		}
		return repayDay;
	}
	
	/**
	 * 清结算汇总的代付结果返回后,调账,将代付的钱分到40142交易的每一个用户
	 * Discription:verify  是否校验40142交易的金额之和等于4014    1:校验,0:不校验
	 * @param transOrderInfo
	 * @return CommonResponse
	 * @author Achilles
	 * @since 2016年4月28日
	 */
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public CommonResponse adjustmentWithhold40142(String accountDate,String verify) {
	    logger.info("查询清结算汇总的代付结果,调账------传入账期="+accountDate+",verify="+verify);
        TransOrderInfoQuery query=new TransOrderInfoQuery();
        //因清结算汇总发起的是平安应收账款账户，他属于中间户，机构号是用的M000001
        query.setMerchantCode(Constants.FN_ID);
        query.setFuncCode(TransCodeConst.PAYMENT_WITHHOLD);
        query.setStatus(TransCodeConst.TRANS_STATUS_PAY_SUCCEED);//成功
        query.setBusiTypeId("10");//此类代付的标示
        if (accountDate==null||"".equals(accountDate)) {
            accountDate=generationPaymentService.getAccountDate(); 
        }
        query.setAccountDate(DateUtils.getDate(accountDate,Constants.DATE_FORMAT_YYYYMMDD));
        List<TransOrderInfo> orderList = transOrderInfoDao.selectByExample(query);
        CommonResponse res = new CommonResponse();
        res.setCode(CodeEnum.FAILURE.getCode());
        logger.info("代付结果返回后,调账  查出4014成功状态订单的个数="+orderList.size()); 
        if (orderList.size()==0) {
            logger.info("没有查到要处理的代付交易!");
            res.setMsg("没有查到要处理的代付交易!");
            return res;
        }
        Long amount = orderList.get(0).getAmount();
        query.setFuncCode("40142");
        query.setMerchantCode(Constants.HT_ID);
        query.setStatus(TransCodeConst.TRANS_STATUS_NORMAL);
        query.setBusiTypeId(null);
        query.setAccountDate(DateUtils.getAccountDate(Constants.DATE_FORMAT_YYYYMMDD, accountDate));
        List<TransOrderInfo> orderInfoList = transOrderInfoDao.selectByExample(query);
        logger.info("代付结果返回后,调账  查出40142订单的个数="+orderInfoList.size());  
        if (orderInfoList.size()==0) {
            logger.info("没有查到要处理的40142交易!");
            res.setMsg("没有查到未处理的40142交易!");
            return res;
        }
        Long totalAmount = 0l;
        for (TransOrderInfo transOrderInfo : orderInfoList) {
            totalAmount+=transOrderInfo.getAmount();
        }
        if (!"0".equals(verify)&&!totalAmount.equals(amount)) {
            logger.info("40142交易的金额之和不等于4014!  40142交易 totalAmount="+totalAmount+";4014Amount="+amount);
            res.setMsg("金额异常,请检查!");
            return res;
        }
        TransOrderInfo transOrderInfoUp = null;
        PingAnParam pingAnParam = parameterInfoService.getPingAnInteriorTransferParam();
        if (pingAnParam==null) {
            res.setMsg("参数表设置异常!");
            return res;
        }
        PaymentDto paymentDto = null;
        for (TransOrderInfo transOrderInfo : orderInfoList) {
            FinanaceEntry finanaceEntry = addToPayGetFinanaceEntry(transOrderInfo);
            if (finanaceEntry!=null) {
                finanaceEntryDao.insertSelective(finanaceEntry); 
            }
            paymentDto = new PaymentDto();
            paymentDto.setOrgNo(pingAnParam.getDealInst());
            //转出方为平安银行主账户
            paymentDto.setPayerAccountNo(pingAnParam.getMainAccountNo());
            paymentDto.setPayerAccountName(pingAnParam.getMainAccountName());
            paymentDto.setTransNo(transOrderInfo.getRequestId().toString());
            res =adjustmentToGaterouter(transOrderInfo,paymentDto,pingAnParam);
            logger.info("40142  代收付回结果后调用多渠道   code="+res.getCode()+",msg="+res.getMsg());
            transOrderInfoUp = new TransOrderInfo();
            transOrderInfoUp.setRequestId(transOrderInfo.getRequestId());
            transOrderInfoUp.setStatus(1);//银行已受理
            if (!CodeEnum.SUCCESS.getCode().equals(res.getCode())) {
                transOrderInfoUp.setErrorMsg(res.getMsg());
                transOrderInfoUp.setStatus(11);
            }
            transOrderInfoDao.updateByPrimaryKeySelective(transOrderInfoUp);
        }
        return res;
    } 
	
    /**
     * mice平安付款子账户加钱
     * Discription:
     * @param transOrderInfo
     * @return CommonResponse
     * @author Achilles
     * @since 2016年4月28日
     */
    private FinanaceEntry addToPayGetFinanaceEntry(TransOrderInfo transOrderInfo){
        User user=new User();
        user.constId=transOrderInfo.getMerchantCode();
        user.userId=transOrderInfo.getUserId();
        user.productId=Constants.HT_PINGAN_PAY_CHILD;
        boolean accountIsOK=operationService.checkAccount(user);
        if(!accountIsOK){
            logger.info("账户状态异常！ userId="+transOrderInfo.getUserId());
            return null;
        }
        Balance balance=checkInfoService.getBalance(user,"");
        if (balance==null) {
            logger.info("无法获取账户资金！userId="+transOrderInfo.getUserId());
            return null;
        }
        balance.setPulseDegree(balance.getPulseDegree()+1);
        String entryId=redisIdGenerator.createRequestNo();
        transOrderInfo.setFuncCode(TransCodeConst.CHARGE);
        List<FinanaceEntry> finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, balance,entryId, true);
        if(finanaceEntries.size()==0){
            logger.info("生成流水发生意外！userId="+transOrderInfo.getUserId());
            return null;
        }
        FinanaceEntry finanaceEntry = finanaceEntries.get(0);
        finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
        finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
        finanaceEntry.setRemark("补涨:mice平安付款子账户加钱");
        return finanaceEntry;
    }
	
    /**
     * 调多渠道调账
     * Discription:
     * @param transOrderInfo
     * @return CommonResponse
     * @author Achilles
     * @since 2016年4月28日
     */
	 private CommonResponse adjustmentToGaterouter(TransOrderInfo transOrderInfo,PaymentDto paymentDto,PingAnParam pingAnParam){
	        CommonResponse res = new CommonResponse();
	        res.setCode(CodeEnum.FAILURE.getCode());
	        paymentDto.setSysNo("zhxt001");//业务系统号
	        paymentDto.setTransCode("16011");//子账户操作(增删改)
	        
	        paymentDto.setBusiCode("16011");//业务编码 
	        paymentDto.setChannelNo("160601");//渠道号
	        paymentDto.setSignType(1);//固定值1 即MD5

	        paymentDto.setPayAmount(transOrderInfo.getOrderAmount());
	        paymentDto.setCurrency("CNY");
	        
	      //获取转入方用户卡信息
	        FinanaceAccountQuery finAccQuery = new FinanaceAccountQuery();
	        finAccQuery.setRootInstCd(transOrderInfo.getMerchantCode());
	        finAccQuery.setAccountRelateId(transOrderInfo.getUserId());
	        finAccQuery.setGroupManage(Constants.HT_PINGAN_PAY_CHILD);
	        finAccQuery.setStatusId("1");
	        List<FinanaceAccount> finList = finanaceAccountDao.selectByExample(finAccQuery);
	        if (finList.size()==0) {
	          res.setMsg("无法获取用户"+transOrderInfo.getUserId()+"的账户信息");
	          return res;
	        }
	        paymentDto.setReceiverAccountType(10);
	        paymentDto.setReceiverAccountNo(finList.get(0).getReferUserId());
//	        paymentDto.setReceiverAccountName(finList.get(0).getRecordMap());
	        paymentDto.setReceiverAccountName(paymentDto.getPayerAccountName());
	        paymentDto.setReceiverBankName("平安银行");
	        paymentDto.setBankFlag(1);//1行内，2行外
	        String remark = transOrderInfo.getRemark();
	        paymentDto.setPurpose(remark); 
	        if (remark==null||"".equals(remark)) {
	            paymentDto.setPurpose("付款");  
	        }
	        paymentDto.setSettleFlag(1);//结算标识   1普通，2实时，3 特急
	        paymentDto.setSignMsg(paymentDto.sign(pingAnParam.getMd5key()));
	        logger.info("调用多渠道转账(内部)传入参数: SysNo="+paymentDto.getSysNo()+",TransCode="+paymentDto.getTransCode()
	        +",OrgNo="+paymentDto.getOrgNo()+",BusiCode="+paymentDto.getBusiCode()+",ChannelNo="+paymentDto.getChannelNo()
	                + ",SignType="+paymentDto.getSignType()+",SignMsg="+paymentDto.getSignMsg()
	                +",TransNo="+paymentDto.getTransNo()+",PayAmount="+paymentDto.getPayAmount()
	                +",Currency="+paymentDto.getCurrency()+",PayerAccountNo="+paymentDto.getPayerAccountNo()
	                +",PayerAccountName="+paymentDto.getPayerAccountName()+",ReceiverAccountNo="+paymentDto.getReceiverAccountNo()
	                +",ReceiverAccountName="+paymentDto.getReceiverAccountName()+",ReceiverBankName="+paymentDto.getReceiverBankName()
	                +",BankFlag="+paymentDto.getBankFlag()+",SettleFlag="+paymentDto.getSettleFlag()+",Purpose="+paymentDto.getPurpose()
	                +",ReceiverAccountType="+paymentDto.getReceiverAccountType());
	        //调用多渠道
	        PaymentRespDto resDto = null;
	        try {
	            resDto = bankPaymentService.payment(paymentDto);
	        } catch (Exception e) {
	            logger.error("调用多渠道转账(内部)接口异常!",e);
	            res.setMsg("系统异常(多渠道)!");
	            return res;
	        }
	        logger.info("调用多渠道单笔转账(内部)  returnCode="+resDto.getReturnCode()+",returnMsg="+resDto.getReturnMsg());
	        if (!"100000".equals(resDto.getReturnCode())) {
	            res.setMsg("多渠道返回:"+resDto.getReturnMsg());
	            return res;
	        }
            logger.info("调用多渠道转账(内部)返回的状态值="+resDto.getStatusId());
	        if (resDto.getStatusId()!=10 && resDto.getStatusId()!=11 && resDto.getStatusId()!=12) {//非成功
	            res.setMsg("调用多渠道转账失败");
	            return res;
	        }
	        return new CommonResponse();
	    }
	
	 /**
	  * 汇总清结算发送的40142汇总的代付
	  * Discription:
	  * @return CommonResponse
	  * @author Achilles
	  * @since 2016年5月4日
	  */
	  @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	 public CommonResponse summarizing40142(){
	     CommonResponse res = new CommonResponse();
	     res.setCode(CodeEnum.FAILURE.getCode());
        TransOrderInfoQuery query=new TransOrderInfoQuery();
        query.setMerchantCode(Constants.FN_ID);
        query.setFuncCode(TransCodeConst.PAYMENT_WITHHOLD);
        query.setStatus(TransCodeConst.TRANS_STATUS_NORMAL);//成功
        query.setBusiTypeId("10");//此类代付的标示
        Date accountDate = DateUtils.getDate(generationPaymentService.getAccountDate(),Constants.DATE_FORMAT_YYYYMMDD);
        query.setAccountDate(accountDate);
        List<TransOrderInfo> tranOrderList = transOrderInfoDao.selectByExample(query);
        if (tranOrderList.size()==0) {
            res.setMsg("没有查出要汇总的代付交易(40142汇总的代付)");
            return res;
        }
        TransOrderInfo transOrderInfo = tranOrderList.get(0);
        TransDaysSummary transDaysSummary=new TransDaysSummary();
        transDaysSummary.setTransSumId(CommUtil.getGenerateNum(5));//汇总订单号
        transDaysSummary.setRootInstCd(transOrderInfo.getMerchantCode());//机构号
        transDaysSummary.setOrderType("7");
        transDaysSummary.setAccountDate(accountDate);//账单日期
        transDaysSummary.setSummaryAmount(transOrderInfo.getAmount());//汇总金额
        if(TransCodeConst.PAYMENT_WITHHOLD.equals(transOrderInfo.getFuncCode())){//如果是代付汇总，获取InterMerchantCode值
            transDaysSummary.setUserId(transOrderInfo.getInterMerchantCode());
        }else{
            transDaysSummary.setUserId(transOrderInfo.getUserId());
        }
        transDaysSummary.setSummaryOrders(transOrderInfo.getOrderNo());//订单Id（汇总）
        GenerationPayment generationPayment=new GenerationPayment();
        generationPayment.setUserId(transDaysSummary.getUserId());
        generationPayment.setOrderNo(transDaysSummary.getTransSumId());//订单号

        AccountInfo accountInfo=getUserAccInfoDebit(transOrderInfo);
        if (accountInfo==null) {
            res.setMsg("没有查出用户结算卡信息!");
            return res;
        }
        getGenPayByAccountInfor(generationPayment, accountInfo);
        generationPayment.setRootInstCd(transDaysSummary.getRootInstCd());
        generationPayment.setOrderType(6);//订单类型
        generationPayment.setUserId(transDaysSummary.getUserId());
        generationPayment.setAmount(transDaysSummary.getSummaryAmount());
        generationPayment.setStatusId(SettleConstants.TAKE_EFFECT);
        generationPayment.setBussinessCode(SettleConstants.WITHHOLD_BATCH_CODE);//业务代码
        generationPayment.setAccountDate(accountDate);//账单日期
        generationPayment.setRemark(transOrderInfo.getRemark());
        
        TransOrderInfo transOrderInfoNew = new TransOrderInfo();
        transOrderInfoNew.setRequestId(transOrderInfo.getRequestId());
        transOrderInfoNew.setStatus(7);
        transOrderInfoDao.updateByPrimaryKeySelective(transOrderInfoNew);
        transDaysSummaryDao.insertSelective(transDaysSummary);
        generationPaymentDao.insertSelective(generationPayment);
        return new CommonResponse();
	 }
	 
    private AccountInfo getUserAccInfoDebit(TransOrderInfo transOrderInfo) {
        com.rkylin.wheatfield.bean.User userr = new com.rkylin.wheatfield.bean.User();
        userr.setUserId(transOrderInfo.getUserId());
        userr.setInstCode(transOrderInfo.getMerchantCode());
        userr.setType(new String[]{"10001"});
        List<FinanaceAccount> finAccList = finanaceAccountDao.queryByInstCodeAndUser(userr);
        AccountInfoQuery accInfoQuery = new AccountInfoQuery();
        accInfoQuery.setFinAccountId(finAccList.get(0).getFinAccountId());
        accInfoQuery.setStatus(1);
        accInfoQuery.setAccountPurpose(Constants.ACCOUNT_PURPOSE_1);
        List<AccountInfo> accList = accountInfoDao.selectByExample(accInfoQuery);
        AccountInfo accInfo = null;
        if (accList.size()!=0) {
            accInfo=accList.get(0);
        }
        return accInfo;
    }
    
    /**
     * 处理订单状态为11的40142交易
     * Discription:
     * @param orderNo
     * @return CommonResponse
     * @author Achilles
     * @since 2016年5月9日
     */
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public CommonResponse manageBackFail40142(String orderNo){
        TransOrderInfoQuery query=new TransOrderInfoQuery();
        query.setMerchantCode(Constants.HT_ID);
        query.setOrderNo(orderNo);
        query.setFuncCode("40142");
//        query.setStatus(11);
        List<TransOrderInfo> orderList = transOrderInfoDao.selectByExample(query);
        CommonResponse res = new CommonResponse();
        res.setCode(CodeEnum.FAILURE.getCode());
        if (orderList.size()==0) {
            res.setMsg("没有查到要处理的交易!");
            return res;
        }
        TransOrderInfo transOrderInfo = orderList.get(0);
        if (transOrderInfo.getStatus()!=5&&transOrderInfo.getStatus()!=11) {
            res.setMsg("订单状态异常无法处理!");
            return res;  
        }
        PingAnParam pingAnParam = parameterInfoService.getPingAnInteriorTransferParam();
        if (pingAnParam==null) {
            res.setMsg("参数表设置异常!");
            return res;
        }
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setOrgNo(pingAnParam.getDealInst());
        //获取转出方用户卡信息
        paymentDto.setPayerAccountNo(pingAnParam.getMainAccountNo());
        paymentDto.setPayerAccountName(pingAnParam.getMainAccountName());
        String transNo = "40142"+CommUtil.getGenerateNum(1);
        paymentDto.setTransNo(transNo);
        res =adjustmentToGaterouter(transOrderInfo,paymentDto,pingAnParam);
        TransOrderInfo transOrderInfoUp = new TransOrderInfo();
        transOrderInfoUp.setRequestId(transOrderInfo.getRequestId());
        transOrderInfoUp.setOrderPackageNo(transNo);
        if (CodeEnum.SUCCESS.getCode().equals(res.getCode())) {
            transOrderInfoUp.setStatus(1);//银行已受理
            transOrderInfoUp.setErrorMsg("");
            transOrderInfoDao.updateByPrimaryKeySelective(transOrderInfoUp);
            return res;
        }
        transOrderInfoUp.setErrorMsg(res.getMsg());
        transOrderInfoDao.updateByPrimaryKeySelective(transOrderInfoUp);
        return res;
    }
    
    /**
     * 根据当日提现交易：保理主账户－；每日晚上11：30进行操作，根据当前账期(T-1)的账期进行汇总提现交易且提现状态=4金额，
     * 需要判断保理主账户金额是否足够，不足时，需要发送短信提醒
     * Discription:
     * @return CommonResponse
     * @author Achilles
     * @since 2016年7月7日
     */
    public CommonResponse summaryCreditWithdrawToUpdateFactoring(){
        TransOrderInfoQuery query=new TransOrderInfoQuery();
        query.setMerchantCode(Constants.FACTORING_ID);
        query.setFuncCode(TransCodeConst.WITHDROW);
        query.setStatus(4);
        String accountDate=generationPaymentService.getAccountDate();
        query.setAccountDate(DateUtils.getAccountDate(Constants.DATE_FORMAT_YYYYMMDD, accountDate));
        List<TransOrderInfo> orderList = transOrderInfoDao.selectByExample(query);
        logger.info("根据提现交易修改保理主账户   查出的提现订单个数="+orderList.size());
        CommonResponse res = new CommonResponse();
        res.setCode(CodeEnum.FAILURE.getCode());
        if (orderList.size()==0) {
            res.setMsg("没有查出符合条件的订单");  
            return res;
        }
        long sumAmount = 0l;
        for (TransOrderInfo transOrderInfo:orderList) {
            if ("14623543517562373".equals(transOrderInfo.getUserId())) {//保理_收款主账户
                continue;
            }
            sumAmount+=transOrderInfo.getAmount();
        }
        logger.info("根据提现交易修改保理主账户   查出的提现金额汇总="+sumAmount);
        //查询保理主账户账户余额
        Balance balance=checkInfoService.getBalance(new User(),"TIM14623543517451028");
        if (balance==null) {
            res.setMsg("没有查出保理主账户余额");  
            return res;
        }

        if (sumAmount>balance.getBalanceSettle()) {
            ParameterInfo parameterInfo = parameterInfoService.getParameterInfoByParaCode("sendMobiles");
            if (parameterInfo==null) {
                res.setMsg("保理主账户余额不足,参数表没有设置手机号,无法发送短信!");  
                return res;  
            }
            SendSMS.sendSMS(parameterInfo.getParameterValue(), "账户系统"+parameterInfo.getRemark()
            +"，编号QYBT001存在保理主账户余额不足问题，请及时解决！");
            res.setMsg("保理主账户余额不足!(根据当日提现修改保理主账户)可提现金额:"+balance.getBalanceSettle()+"分,需要提现金额:"+sumAmount); 
            return res;
        }
        com.rkylin.wheatfield.bean.User user = new com.rkylin.wheatfield.bean.User();
        user.setInstCode(Constants.FACTORING_ID);
        user.setUserId("14623543517451028");
        user.setProductId("P000165");
        user.setAmount(sumAmount);
        user.setStatus(0);
        return semiAutomatizationService.operateFinAccount(user);
    }
    
}
