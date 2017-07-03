package com.rkylin.wheatfield.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.Rop.api.ApiException;
import com.Rop.api.DefaultRopClient;
import com.Rop.api.request.TlpaySingleRealtimeValidateRequest;
import com.Rop.api.response.TlpaySingleRealtimeValidateResponse;
import com.rkylin.common.RedisIdGenerator;
import com.rkylin.gaterouter.dto.authentication.BankAccountCheckDto;
import com.rkylin.gaterouter.dto.authentication.BankAccountCheckRespDto;
import com.rkylin.gaterouter.service.AuthenticationService;
import com.rkylin.gateway.dto.personBankCheck.PersonBankCheckDto;
import com.rkylin.gateway.dto.personBankCheck.PersonBankCheckRespDto;
import com.rkylin.gateway.service.CreditService;
import com.rkylin.utils.RkylinMailUtil;
import com.rkylin.wheatfield.api.AccountManagementService;
import com.rkylin.wheatfield.common.DateUtils;
import com.rkylin.wheatfield.common.PartyCodeUtil;
import com.rkylin.wheatfield.common.SessionUtils;
import com.rkylin.wheatfield.common.ValHasNoParam;
import com.rkylin.wheatfield.constant.AccountConstants;
import com.rkylin.wheatfield.constant.BaseConstants;
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.constant.ErrorCodeConstants;
import com.rkylin.wheatfield.constant.SettleConstants;
import com.rkylin.wheatfield.constant.TransCodeConst;
import com.rkylin.wheatfield.dao.AccountInfoDao;
import com.rkylin.wheatfield.dao.CorporatAccountInfoDao;
import com.rkylin.wheatfield.dao.FinanacePersonDao;
import com.rkylin.wheatfield.dao.GenerationPaymentHistoryDao;
import com.rkylin.wheatfield.dao.ParameterInfoDao;
import com.rkylin.wheatfield.enumtype.UserTypeEnum;
import com.rkylin.wheatfield.exception.AccountException;
import com.rkylin.wheatfield.manager.AccountAgreementManager;
import com.rkylin.wheatfield.manager.AccountInfoManager;
import com.rkylin.wheatfield.manager.AccountPasswordManager;
import com.rkylin.wheatfield.manager.CorporatAccountInfoManager;
import com.rkylin.wheatfield.manager.CreditApprovalInfoManager;
import com.rkylin.wheatfield.manager.CreditInfoManager;
import com.rkylin.wheatfield.manager.CreditRepaymentHistoryManager;
import com.rkylin.wheatfield.manager.FinanaceAccountManager;
import com.rkylin.wheatfield.manager.GenerationPaymentHistoryManager;
import com.rkylin.wheatfield.manager.OpenBankCodeManager;
import com.rkylin.wheatfield.manager.ParameterInfoManager;
import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.model.OpenSubAccountResponse;
import com.rkylin.wheatfield.pojo.AccountAgreement;
import com.rkylin.wheatfield.pojo.AccountAgreementQuery;
import com.rkylin.wheatfield.pojo.AccountInfo;
import com.rkylin.wheatfield.pojo.AccountInfoPlus;
import com.rkylin.wheatfield.pojo.AccountInfoQuery;
import com.rkylin.wheatfield.pojo.CorporatAccountInfo;
import com.rkylin.wheatfield.pojo.CorporatAccountInfoQuery;
import com.rkylin.wheatfield.pojo.CreditApprovalInfo;
import com.rkylin.wheatfield.pojo.CreditApprovalInfoQuery;
import com.rkylin.wheatfield.pojo.CreditInfo;
import com.rkylin.wheatfield.pojo.CreditInfoQuery;
import com.rkylin.wheatfield.pojo.CreditRepaymentHistory;
import com.rkylin.wheatfield.pojo.CreditRepaymentHistoryQuery;
import com.rkylin.wheatfield.pojo.FinanaceAccount;
import com.rkylin.wheatfield.pojo.FinanaceAccountQuery;
import com.rkylin.wheatfield.pojo.FinanaceEntry;
import com.rkylin.wheatfield.pojo.FinanacePerson;
import com.rkylin.wheatfield.pojo.FinanacePersonQuery;
import com.rkylin.wheatfield.pojo.GenerationPaymentHistory;
import com.rkylin.wheatfield.pojo.ParameterInfo;
import com.rkylin.wheatfield.pojo.ParameterInfoQuery;
import com.rkylin.wheatfield.pojo.TransOrderInfo;
import com.rkylin.wheatfield.pojo.User;
import com.rkylin.wheatfield.pojo.ValidateData;
import com.rkylin.wheatfield.response.AccountInfoGetResponse;
import com.rkylin.wheatfield.response.ErrorResponse;
import com.rkylin.wheatfield.response.PingAnOpenAccountResponse;
import com.rkylin.wheatfield.response.Response;
import com.rkylin.wheatfield.service.AccountInfoService;
import com.rkylin.wheatfield.service.AccountManageService;
import com.rkylin.wheatfield.service.CheckInfoService;
import com.rkylin.wheatfield.service.CorporatAccountInfoService;
import com.rkylin.wheatfield.service.CreditApprovalInfoService;
import com.rkylin.wheatfield.service.GenerationPaymentService;
import com.rkylin.wheatfield.service.IAPIService;
import com.rkylin.wheatfield.service.IErrorResponseService;
import com.rkylin.wheatfield.service.ParameterInfoService;
import com.rkylin.wheatfield.service.PaymentAccountService;
import com.rkylin.wheatfield.service.PaymentInternalService;
import com.rkylin.wheatfield.utils.BeanUtil;
import com.rkylin.wheatfield.utils.CodeEnum;
import com.rkylin.wheatfield.utils.CommUtil;
import com.rkylin.wheatfield.utils.DateUtil;
@Transactional
@Service("accountManageService")
public class AccountManageServiceImpl implements AccountManageService,AccountManagementService,
		IAPIService {
	private static Logger logger = LoggerFactory
			.getLogger(AccountManageServiceImpl.class);
	
	private static Object lock=new Object();
	
	@Autowired
	FinanaceAccountManager finanaceAccountManager;
	@Autowired
	AccountInfoManager accountInfoManager;
	@Autowired
	CreditInfoManager creditInfoManager;
	@Autowired
	CreditApprovalInfoManager creditApprovalInfoManager;
	@Autowired
	IErrorResponseService errorResponseService;
	@Autowired
	PaymentInternalService paymentInternalService;
	@Autowired
	CreditRepaymentHistoryManager creditRepaymentHistoryManager;
	@Autowired
	GenerationPaymentService generationPaymentService;
	@Autowired
	AccountAgreementManager accountAgreementManager;
	@Autowired
	OpenBankCodeManager openBankCodeManager;
	@Autowired
	RedisIdGenerator redisIdGenerator;
	@Autowired
	Properties userProperties;
	@Autowired
	AccountPasswordManager accountPasswordManager;
	@Autowired
	ParameterInfoManager parameterInfoManager;
    @Autowired
    private  ParameterInfoService parameterInfoService;
	@Autowired
	PaymentAccountService   paymentAccountService;
	@Autowired
	GenerationPaymentHistoryManager generationPaymentHistoryManager;
	@Autowired
	private CorporatAccountInfoManager corporatAccountInfoManager;
	@Autowired
	private GenerationPaymentHistoryDao genPayHistoryDao;
	@Autowired
	private CorporatAccountInfoDao corporatAccountInfoDao;
	
	@Autowired
	private CorporatAccountInfoService corporatAccountInfoService;
	@Autowired
	CreditApprovalInfoService creditApprovalInfoService;
	@Autowired
	CheckInfoService checkInfoService;
	@Autowired
	@Qualifier("accountInfoDao")
	private AccountInfoDao accountInfoDao;
	@Autowired
	private CreditService creditService;
	@Autowired
	@Qualifier("parameterInfoDao")
	private ParameterInfoDao parameterInfoDao;
	@Autowired
	private AccountInfoService accountInfoService;
	@Autowired
	private FinanacePersonDao finanacePersonDao;
	
	@Autowired
	private AuthenticationService authenticationService;
	
	/**
	 * 修改对公卡号信息（代付失败的对公卡）
	 * @param user
	 * @param accountInfo
	 * @return
	 */
	@Override
	public String updateCreatesAccount(User user,Map<String,String> map) {
		logger.info("--------修改对公银行卡信息（系统校验失败）！--------");
		List<FinanaceAccount> faList =this.getAllAccount(user, "oper");
		if(faList!=null && faList.size()>0){
			logger.info("---用户信息校验成功---");
			for (int j = 0; j < faList.size(); j++) {
				FinanaceAccount finanaceAccount = faList.get(j);
				if (BaseConstants.ACCOUNT_STATUS_OK.equals(finanaceAccount.getStatusId())) {
					if(!map.isEmpty()){
					AccountInfo accountInfo=new AccountInfo();
					if(map.containsKey("accountid")){
						accountInfo.setAccountId(Integer.parseInt(map.get("accountid")));
						logger.info("----主键Id:"+map.get("accountid"));
					}
					if(map.containsKey("accountnumber")){
						accountInfo.setAccountNumber(map.get("accountnumber"));
						logger.info("----账号："+map.get("accountnumber"));
					}
					accountInfo.setStatus(Constants.ACCOUNT_NUM_STATRS_2);
					logger.info("----状态："+accountInfo.getStatus());
					if(map.containsKey("bankhead")){
						accountInfo.setBankHead(map.get("bankhead"));
						logger.info("----总行号："+map.get("bankhead"));
					}
					
					if(map.containsKey("hankheadname")){
						accountInfo.setBankHeadName(map.get("hankheadname"));
						logger.info("----总行名称："+map.get("hankheadname"));
					}
					if(map.containsKey("hankbranch")){
						accountInfo.setBankBranch(map.get("hankbranch"));
						logger.info("----支行号："+map.get("hankbranch"));
					}
					if(map.containsKey("hankbranchname")){
						accountInfo.setBankBranchName(map.get("hankbranchname"));
						logger.info("----支行名称："+map.get("hankbranchname"));
					}
					if(map.containsKey("bankprovinec")){
						accountInfo.setBankProvince(map.get("bankprovinec"));
						logger.info("----所在省："+map.get("bankprovinec"));
					}
					if(map.containsKey("bankcity")){
						accountInfo.setBankCity(map.get("bankcity"));
						logger.info("----所在市："+map.get("bankcity"));
					}
					if(map.containsKey("certificatetype")){
						accountInfo.setCertificateType(map.get("certificatetype"));
						logger.info("----证件类型："+map.get("certificatetype"));
					}
					if(map.containsKey("certificatenumber")){
						accountInfo.setCertificateNumber(map.get("certificatenumber"));
						logger.info("----证件号："+map.get("certificatenumber"));
					}
					if(map.containsKey("accountrealname")){
						accountInfo.setAccountRealName(map.get("accountrealname"));
						logger.info("----持卡人姓名："+map.get("accountrealname"));
					}
					if(accountInfo.getAccountId()==null || "".equals(accountInfo.getAccountId())){
						return "主键不能为空！";
					}
					AccountInfoQuery query=new AccountInfoQuery();
					query.setAccountId(accountInfo.getAccountId());
					List<AccountInfo> list=accountInfoManager.queryList(query);
					//校验账号信息是否存在
					if(list!=null && list.size()>0){
						if(!list.get(0).getFinAccountId().equals(finanaceAccount.getFinAccountId())){
							return "卡信息与用户信息不匹配！";
						}
						//修改的卡必须是对公，审核失败的
						if(!Constants.ACCOUNT_PROPERTY_PUBLIC.equals(list.get(0).getAccountProperty())){
							return "修改账号属性必须为对公！";
						}
						AccountInfoQuery aQuery = new AccountInfoQuery();
						aQuery.setAccountNumber(accountInfo.getAccountNumber());
//						aQuery.setFinAccountId(finanaceAccount
//								.getFinAccountId());
						aQuery.setStatus(Constants.ACCOUNT_NUM_STATRS_OK_ALL);
						aQuery.setRootInstCd(user.constId);
						List<AccountInfo> accountList = accountInfoManager
								.queryListByNumAndConstId(aQuery);
						if (accountList != null && accountList.size() > 0) {
							if(!accountList.get(0).getAccountId().equals(accountInfo.getAccountId())){
								logger.info("该卡号已经绑定过，请勿重复绑定！");
								return "该卡号已经绑定过，请勿重复绑定！";
							}
						}
						AccountInfoQuery acQuery = new AccountInfoQuery();
						acQuery.setAccountName(finanaceAccount
								.getAccountRelateId());
						acQuery.setFinAccountId(finanaceAccount.getFinAccountId());
						acQuery.setStatus(Constants.ACCOUNT_NUM_STATRS_1);
						List<AccountInfo> acList = accountInfoManager
								.queryList(acQuery);
						if (acList != null
								&& acList.size() > 0
								&&accountInfo.getCertificateNumber()!=null
								&& !accountInfo.getCertificateNumber().equals(
										acList.get(0).getCertificateNumber())) {
							logger.info("持卡人证件号必须相同！");
							return "持卡人证件号必须相同！";
						}
						//校验账号状态是否为审核失败
						if(Constants.ACCOUNT_NUM_STATRS_4==list.get(0).getStatus()){
							accountInfoManager.saveAccountInfo(accountInfo);
						}else{
							return "修改账号前请确保账号为审核失败状态！";
						}
					}else{
						return "该账号信息不存在！";
					}
					return "ok";
				}else{
					return "无修改信息！";
				}
				}
			}
		}else{
			return "用户信息不存在！";
		}
		return "";
	}
	/**
	 * 查看银行卡状态
	 * @param accountNumber  卡号
	 */
	@Override
	public String getAccountByNo(User user, String accountNumber) {
		logger.info("--------查看银行卡状态！--------");
		logger.info("--用户ID--:"+user.userId+"--机构号--："+user.constId+"--产品号--："+user.productId+"--用户名--"+user.userName+"--用户卡号：--"+accountNumber);
		try {
			List<FinanaceAccount> faList =this.getAllAccount(user, "oper");
			//校验用户是否存在
			if (faList != null && faList.size() > 0) {
				for (int j = 0; j < faList.size(); j++) {
					FinanaceAccount finanaceAccount = faList.get(j);
					//用户有效才能进一步操作
					if (BaseConstants.ACCOUNT_STATUS_OK.equals(finanaceAccount
							.getStatusId())) {
						AccountInfoQuery accountInfoQuery = new AccountInfoQuery();
						accountInfoQuery.setFinAccountId(finanaceAccount
								.getFinAccountId());
						accountInfoQuery.setAccountName(user.userId);
						accountInfoQuery.setAccountNumber(accountNumber);
						List<AccountInfo> accountList=accountInfoManager.queryList(accountInfoQuery);
						//正常来说accountList最多只有一条状态为非失效的数据
						if(accountList!=null && accountList.size()>0){
							for (int i = 0; i < accountList.size(); i++) {
								AccountInfo accountInfo=accountList.get(i);
								//一个卡号：生效，待审核，审核中，审核失败，四种状态只能存在一种
								if(Constants.ACCOUNT_NUM_STATRS_1==accountInfo.getStatus()){
									return "ok";
								}else if(accountInfo.getStatus()==Constants.ACCOUNT_NUM_STATRS_2){
									return "待审核状态";
								}else if(Constants.ACCOUNT_NUM_STATRS_3==accountInfo.getStatus()){
									return "审核中状态";
								}else if(Constants.ACCOUNT_NUM_STATRS_4==accountInfo.getStatus()){
									return "审核失败状态";
								}
							}
						}else{
							return "暂无卡信息！";
						}
					}
				}
			} else {
				return "无此用户信息！";
			}
			return null;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return "系统错误：";
		}
	}
	/**
	 * 校验账号是否绑定
	 * user
	 * accountNumber 银行卡号
	 * @return 未绑定：null  ,绑定过：错误信息
	 */
	@Override
	public String accountCkeck(User user ,String accountNumber){
		logger.info("--校验卡号是否绑定过!--");
		logger.info("--用户ID--:"+user.userId+"--机构号--："+user.constId+"--产品号--："+user.productId+"--用户名--"+user.userName+"第三方用户ID"+user.referUserId);
		logger.info("--卡号--："+accountNumber);
		List<FinanaceAccount> faList = this.getAllAccount(user, "oper");
		if (faList != null && faList.size() > 0) {
			for (int j = 0; j < faList.size(); j++) {
				FinanaceAccount finanaceAccount = faList.get(j);
				if (BaseConstants.ACCOUNT_STATUS_OK.equals(finanaceAccount
						.getStatusId())) {
					AccountInfoQuery aQuery = new AccountInfoQuery();
					aQuery.setAccountNumber(accountNumber);
					//状态为5时查询除失效状态的所有状态卡
					aQuery.setStatus(Constants.ACCOUNT_NUM_STATRS_OK_ALL);
					aQuery.setRootInstCd(user.constId);
					List<AccountInfo> accountList = accountInfoManager
							.queryListByNumAndConstId(aQuery);
					if (accountList != null && accountList.size() > 0) {
						logger.info("该卡号已经绑定过，请勿重复绑定！");
						return "该卡号已经绑定过，请勿重复绑定！";
					}
				}
				else {
					logger.info("账户已经关闭或冻结！");
					return "账户已经关闭或冻结！";
				}
			}
		}else{
			logger.info("暂无用户！");
			return "暂无用户！";
		}
		return null;
	}
	/**
	 * 账户解冻
	 * 1。解冻主账户
	 * 2.解冻子账户，首先检查主账户的有效性，如果主账户状态正常，可以解冻，非正常，不如解冻
	 * @return 成功：ok   失败：错误信息
	 */
	@Override
	@Transactional
	public String rmFreeze(User user){
		logger.info("----解除账户冻结！----");
		logger.info("--用户ID--:"+user.userId+"--机构号--："+user.constId+"--产品号--："+user.productId+"--用户名--"+user.userName+"第三方用户ID"+user.referUserId);
		if (user.referUserId == null || "".equals(user.referUserId)) {
			user.productId = null;
		}	
		List<FinanaceAccount> faList =this.getAllAccount(user, "");
			if(faList!=null && faList.size()>0){
				if(user.referUserId!=null && !"".equals(user.referUserId)){
					List<FinanaceAccount> list=new ArrayList<FinanaceAccount>();
					FinanaceAccountQuery query = new FinanaceAccountQuery();
					query.setRootInstCd(user.constId);
					query.setAccountRelateId(user.userId);
					query.setFinAccountTypeId(AccountConstants.ACCOUNT_TYPE_BASE);
					list = finanaceAccountManager.queryList(query);
					if(!BaseConstants.ACCOUNT_STATUS_OK.equals(list.get(0).getStatusId())){
						logger.info("解冻子账户操作，主账户状态应为有效！");
						return "解冻子账户操作，主账户状态应为有效！";
					}
					if (!BaseConstants.ACCOUNT_STATUS_FREEZE.equals(faList.get(0).getStatusId())){
						return "账户状态为非冻结状态！";
					}
				} 
				List<FinanaceAccount> paList =this.getAllAccount(user, "oper");
				if(paList!=null && paList.size()>0){
					if(!BaseConstants.ACCOUNT_STATUS_FREEZE.equals(paList.get(0).getStatusId())){
						logger.info("账户状态为非冻结状态！");
						return "账户状态为非冻结状态！";
					}
				}
				
				for (int i = 0; i < faList.size(); i++) {
					FinanaceAccount fa = faList.get(i);
					if (fa.getStatusId() != null
							&& BaseConstants.ACCOUNT_STATUS_FREEZE.equals(fa.getStatusId())) {
						String finAccountId = fa.getFinAccountId();
						FinanaceAccount finanaceAccount = new FinanaceAccount();
						finanaceAccount.setFinAccountId(finAccountId);
						finanaceAccount
								.setStatusId(BaseConstants.ACCOUNT_STATUS_OK);
						finanaceAccountManager
								.updateFinanaceAccount(finanaceAccount);
//						AccountPasswordQuery query=new AccountPasswordQuery();
//						query.setUserId(user.userId);
//						query.setRootInstCd(user.constId);
//						List<AccountPassword> aList=accountPasswordManager.queryList(query);
//						if(aList!=null && aList.size()>0){
//							AccountPassword accountPassword =new AccountPassword();
//							accountPassword.setAcctPawdId(aList.get(0).getAcctPawdId());
//							accountPassword.setErrorCount(0);
//							accountPassword.setStatusId(BaseConstants.PWD_STATUS_0);
//							accountPasswordManager.saveAccountPassword(accountPassword);
//						}
					}
				}
			} else {
				logger.info("暂无账户信息！");
				return "暂无账户信息！";
			}
		return "ok";
	}
	/**
	 * 提交通联，校验银行卡信息有效性
	 * @param data
	 * @return 有效：True   无效：错误码：错误信息
	 */
	public String validateData(ValidateData data) {
		logger.info("--提交通联校验银行卡信息有效性开始--");
		DefaultRopClient ropClient = new DefaultRopClient(
				userProperties.getProperty("TROP_URL"),
				userProperties.getProperty("TAPP_KEY"),
				userProperties.getProperty("TAPP_SECRET"), "xml");
		TlpaySingleRealtimeValidateRequest validateRequest = new TlpaySingleRealtimeValidateRequest();
		validateRequest.setAccount_name(data.getAccountName());
		validateRequest.setAccount_no(data.getAccountNo());
		validateRequest.setAccount_prop(data.getAccountPorp());
		validateRequest.setAccount_type(data.getAccountType());
		validateRequest.setBank_code(data.getBankCode());
		validateRequest.setBindid(data.getBindId());
		validateRequest.setId(data.getId());
		validateRequest.setId_type(data.getIdType());
//		validateRequest.setMerchant_id(data.getMerchantId());
		validateRequest.setMerrem(data.getMerrem());
		validateRequest.setRelatedcard(data.getRelatedCard());
		validateRequest.setRelatid(data.getRelatId());
//		validateRequest.setUser_pass(data.getUserPass());
//		validateRequest.setUser_name(data.getUserName());
		validateRequest.setTel(data.getTel());
		validateRequest.setSubmit_time(data.getSubmitTime());
		validateRequest.setReq_sn(data.getReqSn());
		validateRequest.setRemark(data.getRemark());
//		validateRequest.setMerchant_id(data.getMerchantId());
		validateRequest.setMerchant_id("M000001");

		try {
			TlpaySingleRealtimeValidateResponse validateResponse = ropClient
					.execute(validateRequest, SessionUtils.sessionGet(
							userProperties.getProperty("TROP_URL"),
							userProperties.getProperty("TAPP_KEY"),
							userProperties.getProperty("TAPP_SECRET")));
			if (validateResponse != null) {
				if (validateResponse.isSuccess()) {
					return validateResponse.getIs_success();
				} else {
					logger.info("--错误号--："+validateResponse.getSubCode()+"--错误信息--："+validateResponse.getSubMsg());
					return validateResponse.getSubCode()+":"+validateResponse.getSubMsg();
				}
			}
		} catch (ApiException e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	/**
	 * 修改提现卡
	 * 检查传入卡号的有效性，
	 * 传入卡为结算卡时：ACCOUNT_PURPOSE=4
	 * 提现卡：提示信息
	 * 其它卡：ACCOUNT_PURPOSE=3
	 * accountNumber  银行卡号
	 * 
	 */
	@Override
	@Transactional
	public String updateAccountInfo(User user, String accountNumber) {
		logger.info("-----修改提现卡-----");
		logger.info("--用户ID--:"+user.userId+"--机构号--："+user.constId+"--产品号--："+user.productId+"--用户名--"+user.userName+"第三方用户ID"+user.referUserId);
		logger.info("--卡号--:"+accountNumber);
		try {
			List<FinanaceAccount> faList = this.getAllAccount(user, "oper");
			if (faList != null && faList.size() > 0) {
				for (int i = 0; i < faList.size(); i++) {
					if (BaseConstants.ACCOUNT_STATUS_OK.equals(faList.get(i)
							.getStatusId())) {
						AccountInfoQuery accQuery = new AccountInfoQuery();
						accQuery.setAccountNumber(accountNumber);
						accQuery.setAccountName(user.userId);
						accQuery.setFinAccountId(faList.get(i)
								.getFinAccountId());
						accQuery.setStatus(Constants.ACCOUNT_NUM_STATRS_1);
						List<AccountInfo> aList = accountInfoManager
								.queryList(accQuery);
						if (aList != null && aList.size() > 0) {
							
//							if(Constants.ACCOUNT_PURPOSE_1.equals(aList.get(0).getAccountPurpose())){
//								return "结算卡不能修改！";
//							}
							if(Constants.ACCOUNT_PURPOSE_3.equals(aList.get(0).getAccountPurpose()) || Constants.ACCOUNT_PURPOSE_4.equals(aList.get(0).getAccountPurpose())){
								logger.info("此卡已经是提现卡！");
								return "此卡已经是提现卡！";
							}
							AccountInfoQuery aQuery = new AccountInfoQuery();
							aQuery.setFinAccountId(faList.get(i).getFinAccountId());
							aQuery.setAccountName(user.userId);
							aQuery.setStatus(Constants.ACCOUNT_NUM_STATRS_1);
							aQuery.setAccountPurpose(Constants.ACCOUNT_PURPOSE_3);
							List<AccountInfo> accountList = accountInfoManager
									.queryList(aQuery);
							if (accountList != null && accountList.size() > 0) {
								for (int j = 0; j < accountList.size(); j++) {
										AccountInfo account1 = new AccountInfo();
										account1.setAccountId(accountList.get(j)
												.getAccountId());
										account1.setFinAccountId(faList.get(i)
												.getFinAccountId());
										account1.setAccountPurpose(Constants.ACCOUNT_PURPOSE_2);
										accountInfoManager.saveAccountInfo(account1);
								}
							}else{
								aQuery.setAccountPurpose(Constants.ACCOUNT_PURPOSE_4);
								List<AccountInfo> accountList4 = accountInfoManager
										.queryList(aQuery);
								if(accountList4!=null && accountList4.size()>0){
									for (int j = 0; j < accountList4.size(); j++) {
										if (accountList4.get(j).getStatus()
												.equals(Constants.ACCOUNT_NUM_STATRS_1)) {
											AccountInfo account1 = new AccountInfo();
											account1.setAccountId(accountList4.get(j)
													.getAccountId());
											account1.setFinAccountId(faList.get(i)
													.getFinAccountId());
											account1.setAccountPurpose(Constants.ACCOUNT_PURPOSE_1);
											accountInfoManager.saveAccountInfo(account1);
										}
									}
								}
							}
							AccountInfo account = new AccountInfo();
								if(Constants.ACCOUNT_PURPOSE_2.equals(aList.get(0).getAccountPurpose())){
									account.setAccountPurpose(Constants.ACCOUNT_PURPOSE_3);
								}else if (Constants.ACCOUNT_PURPOSE_1.equals(aList.get(0).getAccountPurpose())){
									account.setAccountPurpose(Constants.ACCOUNT_PURPOSE_4);
								}
								account.setAccountId(aList.get(0).getAccountId());
								accountInfoManager.saveAccountInfo(account);
							
						} else {
							logger.info("银行卡不存在！");
							return "银行卡不存在！";
						}
					}
					else {
						logger.info("账户已经关闭或冻结！");
						return "账户已经关闭或冻结！";
					}
				}
			} else {
				logger.info("暂无此账户！");
				return "暂无此账户！";
			}
		} catch (Exception e) {
			logger.error("更改用户目的失败!"+e.getMessage());
			return "更改用户目的失败!";
		}
		return "ok";
	}
	/**
	 * 删除指定银行卡信息
	 * 结算卡不能被删除，
	 * 如果结算卡和体现卡是一张，改ACCOUNT_PURPOSE=1
	 * @return  成功：ok   失败：失败信息
	 */
	@Override
	public String delAccountInfo(User user, String accountNumber) {
		logger.info("-----删除指定银行卡！------");
		logger.info("--用户ID--:"+user.userId+"--机构号--："+user.constId+"--产品号--："+user.productId+"--用户名--"+user.userName+"第三方用户ID"+user.referUserId);
		logger.info("--卡号--:"+accountNumber);
		try {
			List<FinanaceAccount> faList = this.getAllAccount(user, "");
			if (faList != null && faList.size() > 0) {
				for (int i = 0; i < faList.size(); i++) {
					FinanaceAccount finanaceAccount = faList.get(i);
					if (BaseConstants.ACCOUNT_STATUS_OK.equals(finanaceAccount
							.getStatusId())) {
						AccountInfoQuery aQuery = new AccountInfoQuery();
//						aQuery.setFinAccountId(finanaceAccount
//								.getFinAccountId());
						aQuery.setRootInstCd(user.constId);
						aQuery.setAccountName(user.userId);
						aQuery.setStatus(Constants.ACCOUNT_NUM_STATRS_1);
						aQuery.setAccountNumber(accountNumber);
						List<AccountInfo> accountList = accountInfoManager
								.queryList(aQuery);
						AccountInfo accountInfo = new AccountInfo();
						if(accountList!=null && accountList.size()>0){
//							if (Constants.ACCOUNT_PURPOSE_1.equals(accountList.get(
//									0).getAccountPurpose())) {
//								logger.info("结算卡不能删除！");
//								return "结算卡不能删除！";
//							}
							accountInfo.setAccountId(accountList.get(0)
									.getAccountId());
							accountInfo.setFinAccountId(finanaceAccount
									.getFinAccountId());
							accountInfo.setAccountNumber(accountNumber);
							
							if (Constants.ACCOUNT_PURPOSE_4.equals(accountList.get(
									0).getAccountPurpose())) {
								accountInfo.setAccountPurpose(Constants.ACCOUNT_PURPOSE_1);
								accountInfo.setStatus(Constants.ACCOUNT_NUM_STATRS_1);
							}else if(Constants.ACCOUNT_PURPOSE_2.equals(accountList.get(
									0).getAccountPurpose()) || Constants.ACCOUNT_PURPOSE_3.equals(accountList.get(
											0).getAccountPurpose())||Constants.ACCOUNT_PURPOSE_1.equals(accountList.get(
				                                    0).getAccountPurpose())){
								accountInfo.setStatus(Constants.ACCOUNT_NUM_STATRS_0);
							}
							accountInfoManager.saveAccountInfo(accountInfo);
						}else{
							logger.info("该用户暂时未绑定"+accountNumber+"这张卡！");
							return "该用户暂时未绑定"+accountNumber+"这张卡！";
						}
					}
					else {
						logger.info("账户已经关闭或冻结！");
						return "账户已经关闭或冻结！";
					}
				}
			} else {
				logger.info("暂无账户！");
				return "暂无账户！";
			}
		} catch (Exception e) {
			logger.error("银行卡删除失败！"+e.getMessage());
			return "银行卡删除失败！";
		}
		return "ok";

	}
	/**
	 * 获取银行卡信息列表
	 * objOrList  1：结算卡信息  2：所有卡信息
	 */
	@Override
	public List<AccountInfo> getAccountList(User user, String objOrList) {
		logger.info("--------获取银行卡信息列表！--------");
		logger.info("--用户ID--:"+user.userId+"--机构号--："+user.constId+"--产品号--："+user.productId+"--用户名--"+user.userName+"第三方用户ID"+user.referUserId);
		try {
			List<FinanaceAccount> faList =this.getAllAccount(user, "oper");
			if (faList != null && faList.size() >= 0) {
				for (int j = 0; j < faList.size(); j++) {
					FinanaceAccount finanaceAccount = faList.get(j);
					if (BaseConstants.ACCOUNT_STATUS_OK.equals(finanaceAccount
							.getStatusId())) {
						AccountInfoQuery accountInfoQuery = new AccountInfoQuery();
						accountInfoQuery.setFinAccountId(finanaceAccount
								.getFinAccountId());
						accountInfoQuery.setAccountName(user.userId);
						accountInfoQuery.setStatus(Constants.ACCOUNT_NUM_STATRS_OK_ALL);
						if (Constants.ACCOUNT_PURPOSE_1.equals(objOrList)) {
							accountInfoQuery
									.setAccountPurpose(Constants.ACCOUNT_PURPOSE_1);
						}
						return accountInfoManager.queryList(accountInfoQuery);
					}
				}
			} else {
				return null;
			}
			return null;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	/**
	 * 开通账户 开通子账户时flag!=null && !"".equals(flag) 
	 * 开通主账户时flag=="" || flag==null
	 * 预付金子账户referUserId=台长userId 
	 * 用户子账户referUserId="M000002"--p2p UserId
	 * FinanaceEntry.paymentamount 支付金额/授信金额
	 *  FinanaceEntry.referId 记账凭证号/授信协议号
	 */
	@Override
	@Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
	public String openAccount(User user,
			FinanaceEntry finanaceEntry) {
		logger.info("------开通账户！---------");
		logger.info("--用户ID--:"+user.userId+"--机构号--："+user.constId+"--产品号--："+user.productId+"--用户名--"+user.userName+"第三方用户ID"+user.referUserId);
		List<FinanaceAccount> list=new ArrayList<FinanaceAccount>();
		FinanaceAccountQuery query = new FinanaceAccountQuery();
		query.setRootInstCd(user.constId);
		query.setAccountRelateId(user.userId);
//		query.setStatusId(BaseConstants.ACCOUNT_STATUS_OK);
//		query.setGroupManage(user.productId);
		query.setFinAccountTypeId(AccountConstants.ACCOUNT_TYPE_BASE);
		list = finanaceAccountManager.queryList(query);
		if(user.referUserId!=null && !"".equals(user.referUserId)){
			if (list == null || list.size() <= 0) {
				logger.info("主账户暂不存在，请先创建主账户！");
				return "主账户暂不存在，请先创建主账户！";
			}
			if(!BaseConstants.ACCOUNT_STATUS_OK.equals(list.get(0).getStatusId())){
				logger.info("主账户状态非正常，请先将主账户置为有效状态！");
				return "主账户状态非正常，请先将主账户置为有效状态！";
			}
		}
		String accountCode = "";
		if(list!=null && list.size()>0){
			for (int i = 0; i < list.size(); i++) {
//				if (BaseConstants.ACCOUNT_STATUS_OK.equals(list.get(i)
//						.getStatusId())) {
					if (user.referUserId != null && !"".equals(user.referUserId)) {
						accountCode = list.get(i).getAccountCode();
						query.setRootInstCd(user.constId);
						query.setAccountRelateId(user.userId);
						query.setGroupManage(user.productId);
						query.setReferUserId(user.referUserId);
						query.setFinAccountTypeId(AccountConstants.ACCOUNT_TYPE_CHILD);
						List<FinanaceAccount> flist = finanaceAccountManager.queryList(query);
						if(flist!=null && flist.size()>0){
							return "ok";
						}
						break;
					} else {
						logger.info("该用户已经开通主账户！");
						return "该用户已经开通主账户！";
					}
//				}
			}
		}
		FinanaceAccount finanaceAccount = new FinanaceAccount();
		finanaceAccount.setFinAccountId(redisIdGenerator.accountEntryNo());
		finanaceAccount.setRootInstCd(user.constId);
		finanaceAccount.setGroupManage(user.productId);
		finanaceAccount.setFinAccountName(user.userName);
		finanaceAccount.setAccountRelateId(user.userId);
		finanaceAccount.setStatusId(BaseConstants.ACCOUNT_STATUS_OK);
		finanaceAccount.setAmount(Long.parseLong(BaseConstants.INITIAL_AMOUNT));
		finanaceAccount.setBalanceSettle(Long
				.parseLong(BaseConstants.INITIAL_AMOUNT));
		finanaceAccount.setBalanceFrozon(Long
				.parseLong(BaseConstants.INITIAL_AMOUNT));
		finanaceAccount.setBalanceOverLimit(Long
				.parseLong(BaseConstants.INITIAL_AMOUNT));
		if (user.referUserId != null && !"".equals(user.referUserId)) {
			finanaceAccount.setReferUserId(user.referUserId);
			finanaceAccount
			.setFinAccountTypeId(AccountConstants.ACCOUNT_TYPE_CHILD);
			finanaceAccount.setAccountCode(accountCode);
		}else{
			finanaceAccount
			.setFinAccountTypeId(AccountConstants.ACCOUNT_TYPE_BASE);
			finanaceAccount
			.setAccountCode(PartyCodeUtil.getPartyCodeByRule());//生成方式
		}
		finanaceAccount.setGroupSettle(AccountConstants.GROUP_SETTLE_BASE);
		finanaceAccount.setCurrency(BaseConstants.CURRENCY_CNY);
		finanaceAccount.setStartTime(DateUtils
				.getSysDate(Constants.DATE_FORMAT_YYYYMMDD));
		finanaceAccount.setBussControl(BaseConstants.BUSS_CONTRAL_TOTAL);
		finanaceAccount.setRemark("");
		finanaceAccount.setRecordMap("");
		finanaceAccountManager.saveFinanaceAccount(finanaceAccount);
		String ArraysAll[][] = AccountConstants.ORGANIZATION_CONSTIDS_PRODEUCTS;
		String ArraysConstId[] = ArraysAll[0];
		if (Arrays.asList(ArraysConstId).contains(user.constId)
				&& AccountConstants.ACCOUNT_TYPE_BASE.equals(finanaceAccount.getFinAccountTypeId())){
			logger.info("校验机构结果：此机构需要开通子账户");
	        int productNum = Arrays.binarySearch(ArraysConstId, user.constId);
			String redCode = "";
			List<FinanaceAccount> redList = new ArrayList<FinanaceAccount>();
			FinanaceAccountQuery redQuery = new FinanaceAccountQuery();
			redQuery.setRootInstCd(user.constId);
			redQuery.setAccountRelateId(user.userId);
			redQuery.setStatusId(BaseConstants.ACCOUNT_STATUS_OK);
			redQuery.setFinAccountTypeId(AccountConstants.ACCOUNT_TYPE_BASE);
			redList = finanaceAccountManager.queryList(redQuery);
			int loopNumber = ArraysAll[productNum + 1].length;
			if (redList != null && redList.size() > 0) {
				for  (int j = 0; j < loopNumber; j++){
					redCode = redList.get(0).getAccountCode();
					FinanaceAccount finanaceAccountChild = new FinanaceAccount();
					finanaceAccountChild.setFinAccountId(redisIdGenerator
							.accountEntryNo());
					finanaceAccountChild.setRootInstCd(user.constId);
					finanaceAccountChild.setGroupManage(ArraysAll[productNum+1][j]);
					finanaceAccountChild.setFinAccountName(user.userName);
					finanaceAccountChild.setAccountRelateId(user.userId);
					finanaceAccountChild
							.setStatusId(BaseConstants.ACCOUNT_STATUS_OK);
					finanaceAccountChild.setAmount(Long
							.parseLong(BaseConstants.INITIAL_AMOUNT));
					finanaceAccountChild.setBalanceSettle(Long
							.parseLong(BaseConstants.INITIAL_AMOUNT));
					finanaceAccountChild.setBalanceFrozon(Long
							.parseLong(BaseConstants.INITIAL_AMOUNT));
					finanaceAccountChild.setBalanceOverLimit(Long
							.parseLong(BaseConstants.INITIAL_AMOUNT));
					// finanaceAccountChild.setReferUserId("");
					finanaceAccountChild
							.setFinAccountTypeId(AccountConstants.ACCOUNT_TYPE_CHILD);
					finanaceAccountChild.setAccountCode(redCode);
					finanaceAccountChild
							.setGroupSettle(AccountConstants.GROUP_SETTLE_BASE);
					finanaceAccountChild.setCurrency(BaseConstants.CURRENCY_CNY);
					finanaceAccountChild.setStartTime(DateUtils
							.getSysDate(Constants.DATE_FORMAT_YYYYMMDD));
					finanaceAccountChild
							.setBussControl(BaseConstants.BUSS_CONTRAL_TOTAL);
					finanaceAccountChild.setRemark("");
					finanaceAccountChild.setRecordMap("");
					finanaceAccountManager
							.saveFinanaceAccount(finanaceAccountChild);
				}
			}
		}
//			if (user.referUserId!=null && Constants.P2P_ID.equals(user.referUserId)) {
//				if(user.creditType.equals(SettleConstants.CREDIT_TYPE_ID_LIMIT+"")){
//					ErrorResponse errorResponse = paymentInternalService.credit(finanaceEntry,user.userId, user.constId, user.productId, user.referUserId);
//					if (errorResponse.isIs_success()) {
//						return "ok";
//					} else {
//						logger.info("操作金额时失败！" + errorResponse.getMsg());
//						throw new AccountException("操作金额时失败！" + errorResponse.getMsg());
//					}
//				}				
//			}
		return "ok";
	}

	/**
	 * 冻结账户
	 */
	@Override
	public String freezeAccount(User user) {
		logger.info("---------冻结账户----------");
		logger.info("--用户ID--:"+user.userId+"--机构号--："+user.constId+"--产品号--："+user.productId+"--用户名--"+user.userName+"第三方用户ID"+user.referUserId);
		String msg = "ok";
		try {
			if (user.referUserId == null || "".equals(user.referUserId)) {
				user.productId = null;
			}
			List<FinanaceAccount> faList = this.getAllAccount(user, "");
			if (faList != null && faList.size() >= 1) {
				List<FinanaceAccount> paList = this.getAllAccount(user, "oper");
				FinanaceAccount pa = paList.get(0);
				if (pa.getStatusId() != null
						&& !BaseConstants.ACCOUNT_STATUS_OK.equals(pa
								.getStatusId())) {
					logger.info("账户已经冻结或关闭！");
					msg = "账户已经冻结或关闭！";
				} else {
					for (int i = 0; i < faList.size(); i++) {
						FinanaceAccount fa = faList.get(i);
						if (fa.getStatusId() != null
								&& BaseConstants.ACCOUNT_STATUS_OK.equals(fa
										.getStatusId())) {
							String finAccountId = fa.getFinAccountId();
							FinanaceAccount finanaceAccount = new FinanaceAccount();
							finanaceAccount.setFinAccountId(finAccountId);
							finanaceAccount
									.setStatusId(BaseConstants.ACCOUNT_STATUS_FREEZE);
							finanaceAccountManager
									.updateFinanaceAccount(finanaceAccount);
						}
					}
				}
			} else {
				logger.info("暂无该账户！");
				msg = "暂无该账户！";
			}
		} catch (Exception e) {
			logger.error("冻结账户失败！" + e.getMessage());
			msg = "冻结账户失败！" ;
		}
		return msg;
	}

	/**
	 * 关闭账户
	 */
	@Override
	public String closeAccount(User user) {
		logger.info("--------关闭账户--------");
		logger.info("--用户ID--:"+user.userId+"--机构号--："+user.constId+"--产品号--："+user.productId+"--用户名--"+user.userName+"第三方用户ID"+user.referUserId);
		String msg = "ok";
		try {
			if (user.referUserId == null || "".equals(user.referUserId)) {
				user.productId = null;
			}
			List<FinanaceAccount> faList = this.getAllAccount(user, "");
			if (faList != null && faList.size() >= 1) {
				List<FinanaceAccount> paList = this.getAllAccount(user, "oper");
				FinanaceAccount pa = paList.get(0);
				if (pa.getStatusId() != null
						&& !BaseConstants.ACCOUNT_STATUS_OK.equals(pa
								.getStatusId())) {
					logger.info("账户已经关闭或冻结！");
					msg = "账户已经关闭或冻结！";
				} else {
					for (int i = 0; i < faList.size(); i++) {
						FinanaceAccount fa = faList.get(i);
						if (BaseConstants.ACCOUNT_STATUS_OK.equals(fa
								.getStatusId()) ) {
							String finAccountId = fa.getFinAccountId();
							FinanaceAccount finanaceAccount = new FinanaceAccount();
							finanaceAccount.setFinAccountId(finAccountId);
							finanaceAccount
									.setStatusId(BaseConstants.ACCOUNT_STATUS_OFF);
							finanaceAccount
									.setEndTime(DateUtils
											.getSysDate(Constants.DATE_FORMAT_YYYYMMDDHHMMSS));
							finanaceAccountManager
									.updateFinanaceAccount(finanaceAccount);
						}
					}
				}

			} else {
				logger.info("暂无该账户！");
				msg = "暂无该账户！";
			}
		} catch (Exception e) {
			logger.error("关闭账户失败！" + e.getMessage());
			msg = "关闭账户失败！";
		}
		return msg;
	}

	/**
	 * 检查账户有效性（dubbo）
	 * @param user
	 * @return 1:表示有效
	 */
	public CommonResponse verifyAccount(User user) {
		logger.info("检查账户有效性   user="+user);
		CommonResponse res = new CommonResponse();
		if (user==null) {
			res.setCode(CodeEnum.ERR_PARAM_NULL.getCode());
			res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
			return res;
		}
		logger.info("检查账户有效性   传入字段的所有值="+BeanUtil.getBeanVal(user, null));
		//校验数据合法性
		res = verifyDataVerifyAccount(user);
		if (CodeEnum.FAILURE.getCode().equals(res.getCode())) {
			logger.info("检查账户有效性   参数校验   未通过  msg="+res.getMsg());
			return res;
		}
		String result =   checkAccount(user);
		if (!"ok".equals(result)) {
			res.setCode(CodeEnum.FAILURE.getCode());
			res.setMsg(result);
			return res;
		}
		return res;
	}
	
	/**
	 * 检查账户有效性 校验字段值
	 * @param user
	 * @return
	 */
	private CommonResponse verifyDataVerifyAccount(User user) {
		CommonResponse res = new CommonResponse();
		res.setCode(CodeEnum.FAILURE.getCode());
		if (user.userId==null||"".equals(user.userId)) {
			res.setMsg("userId不能为空！");
			return res;
		}
		if (user.constId==null||"".equals(user.constId)) {
			res.setMsg("constId不能为空！");
			return res;
		}
		if (user.productId==null||"".equals(user.productId)) {
			res.setMsg("productId不能为空！");
			return res;
		}
		res.setCode(CodeEnum.SUCCESS.getCode());
		return res;
	}
	/**
	 * 检查账户有效性 目前只能检查主账户
	 * user.referUserId != null && !"".equals(user.referUserId)时为子账户
	 */
	@Override
	public String checkAccount(User user) {
		logger.info("-------------检查账户有效性---------------");
		logger.info("--用户ID--:"+user.userId+"--机构号--："+user.constId+"--产品号--："+user.productId+"--用户名--"+user.userName+"第三方用户ID"+user.referUserId);
		String msg = "ok";
		try {
			List<FinanaceAccount> faList =this.getAllAccount(user, "");
			if (faList != null && faList.size() >= 1) {
				if (faList.get(0).getStatusId().equals(user.statusID)){
					msg = "ok";
				} else {
					logger.info("账户状态不为" + user.statusID + "！");
					msg = "账户状态不为" + user.statusID + "！";
				}
			} else {
				logger.info("暂无账户！");
				msg = "暂无账户！";
			}
		} catch (Exception e) {
			logger.error("查询账户有效性失败！" +e.getMessage());
			msg = "查询账户有效性失败！";
		}
		return msg;
	}

	/**
	 * p2p子账户授信
	 * 协议号必须唯一
	 * 只能有一条授信信息
	 * @return  成功：ok
	 */
	@Override
	@Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
	public String creditAccount(User user, CreditInfo creditInfo,
			AccountAgreement accountAgreement, String provideruserid)
			throws AccountException {
		logger.info("------账户手动授信--------");
		logger.info("--用户ID--:"+user.userId+"--机构号--："+user.constId+"--产品号--："+user.productId+"--用户名--"+user.userName+"第三方用户ID"+user.referUserId);
		String msg = "ok";
		FinanaceAccountQuery qquery = new FinanaceAccountQuery();
		qquery.setRootInstCd(user.constId);
		qquery.setAccountRelateId(user.userId);
		qquery.setFinAccountTypeId(AccountConstants.ACCOUNT_TYPE_BASE);
		qquery.setStatusId(BaseConstants.ACCOUNT_STATUS_OK);
		// qquery.setGroupManage(user.productId);
		// fquery.setFinAccountTypeId(AccountConstants.ACCOUNT_TYPE_BASE);
		List<FinanaceAccount> qList = finanaceAccountManager.queryList(qquery);
		boolean bool = false;
		if (qList != null && qList.size() > 0 ) {
			bool = true;
		}
		if (!bool) {
			logger.info("主账户不存在，请先创建主账户！");
			throw new AccountException("主账户不存在，请先创建主账户！");
		}
		AccountAgreementQuery query = new AccountAgreementQuery();
		query.setSecParty(user.userId);
		query.setProductId(user.productId);//协议级别是P级，在定义P时不可重
		List<AccountAgreement> list = accountAgreementManager.queryList(query);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				if (Constants.AGMT_STATUS_1 == list.get(i).getStatus()) {
					logger.info("用户已有授信信息！");
					msg = "用户已有授信信息！";
					break;
				}
			}
		}
		if ("ok".equals(msg)) {
//			if(user.referUserId==null || "".equals(user.referUserId)){
//				throw new AccountException("第三方用户ID不能为空！");
//			}
			List<AccountAgreement> alist = accountAgreementManager.queryList(new AccountAgreementQuery());
			if(alist!=null && alist.size()>0){
				for (int i = 0; i < alist.size(); i++) {
					if(alist.get(i).getAgmtCode().equals(accountAgreement.getAgmtCode())){
						logger.info("已存在该协议号，请检查重新填写！");
						return "已存在该协议号，请检查重新填写！";
					}
				}
			}
				
//				FinanaceAccountQuery fquery = new FinanaceAccountQuery();
//				fquery.setAccountRelateId(provideruserid);
//				List<FinanaceAccount> fList = finanaceAccountManager
//						.queryList(fquery);
//				if (fList != null && fList.size() > 0) {
//					creditInfo.setProviderId(fList.get(0).getFinAccountId());
//				}
				creditInfo.setProviderId(provideruserid);
				user.referUserId=provideruserid;
				accountAgreement
						.setAgmtId(redisIdGenerator.createAgreementNo());// 主键生成机制，需要待定
				accountAgreement.setAgmtCode(accountAgreement.getAgmtCode());
				accountAgreement.setAgmtName(accountAgreement.getAgmtName());
				accountAgreement.setFirParty(user.constId);
				accountAgreement.setSecParty(user.userId);
				accountAgreement.setThdParty(user.referUserId);
				accountAgreement.setStatus(Constants.AGMT_STATUS_1);
				accountAgreement.setAgmtContent("");
				accountAgreement.setProductId(user.productId);
				accountAgreementManager.saveAccountAgreement(accountAgreement);
				creditInfo.setRootInstCd(user.constId);
				creditInfo.setUserId(user.userId);
				creditInfo.setCreditAgreementId(accountAgreement.getAgmtId());
				creditInfoManager.saveCreditInfo(creditInfo);
				FinanaceEntry finanaceEntry=new FinanaceEntry();
				finanaceEntry.setPaymentAmount(creditInfo.getAmount());
				finanaceEntry.setReferId(accountAgreement.getAgmtId());
				try{
					String msg1 = this.openAccount(user,finanaceEntry);
					if (!"ok".equals(msg1)) {
						logger.info(msg1);
						throw new AccountException(msg1);
					}
				}catch(Exception e){
					logger.error(e.getMessage());
					throw new AccountException(e.getMessage());
				}
				
		}
		return msg;
	}
	
	/**
	 * @Updator : liuhuan
	 * @Updatetime : 20151013
	 * @Updatetime : 20151126
	 */
	@Override
	@Transactional
	public String creditAccountNew(User user, CreditApprovalInfo creditApprovalInfo,
			AccountAgreement accountAgreement, String provideruserid) throws Exception {
		logger.info("------账户手动授信--------");
		logger.info("--用户ID--:"+user.userId+"--机构号--："+user.constId+"--产品号--："+user.productId+"--用户名--"+user.userName+"第三方用户ID"+user.referUserId);
		String msg = "ok";
		FinanaceAccountQuery qquery = new FinanaceAccountQuery();
		qquery.setRootInstCd(user.constId);
		qquery.setAccountRelateId(user.userId);
		qquery.setStatusId(BaseConstants.ACCOUNT_STATUS_OK);
		qquery.setFinAccountTypeId(AccountConstants.ACCOUNT_TYPE_BASE);
		List<FinanaceAccount> qList = finanaceAccountManager.queryList(qquery);

		if (qList == null || qList.isEmpty()) {
			logger.info("主账户不存在，请先创建主账户！");
			return "主账户不存在，请先创建主账户！";
		}
		//判断用户在当前机构是否存在有效的授信信息
		CreditApprovalInfoQuery caiq = new CreditApprovalInfoQuery();
		caiq.setRootInstCd(creditApprovalInfo.getRootInstCd());
		caiq.setUserId(creditApprovalInfo.getUserId());
		caiq.setStatusId("1");
		List<CreditApprovalInfo> queryList = creditApprovalInfoManager.queryList(caiq);
		if(queryList != null && !queryList.isEmpty()){
			logger.info("用户 ： " + creditApprovalInfo.getUserId() + " 在 " + creditApprovalInfo.getRootInstCd() + "机构内已存在有效协议信息!");
			return "用户 ： " + creditApprovalInfo.getUserId() + " 在 " + creditApprovalInfo.getRootInstCd() + "机构内已存在有效协议信息!";	
		}
		//判断用户传入的协议号是否存在且生效
		AccountAgreementQuery accountAgreementQuery = new AccountAgreementQuery();
		accountAgreementQuery.setAgmtCode(accountAgreement.getAgmtCode());
		accountAgreementQuery.setStatus(1);
		List<AccountAgreement> alist = accountAgreementManager.queryList(accountAgreementQuery);
		if(alist != null && !alist.isEmpty()){
			logger.info("已存在该协议号且生效，请检查重新填写！");
			return "已存在该协议号且生效，请检查重新填写！";
		}
		
		try{
			//保存协议信息
			accountAgreement.setAgmtId(redisIdGenerator.createAgreementNo());// 主键生成机制，需要待定
			accountAgreement.setAgmtCode(accountAgreement.getAgmtCode());
			accountAgreement.setAgmtName(accountAgreement.getAgmtName());
			accountAgreement.setFirParty(user.constId);
			accountAgreement.setSecParty(user.userId);
			accountAgreement.setThdParty(user.referUserId);
			accountAgreement.setStatus(Constants.AGMT_STATUS_1);
			accountAgreement.setAgmtContent("");
			accountAgreement.setProductId(user.productId);
			accountAgreementManager.saveAccountAgreement(accountAgreement);
			//保存授信信息
			creditApprovalInfo.setCreditAgreementId(accountAgreement.getAgmtId());
			creditApprovalInfoManager.saveCreditApprovalInfo(creditApprovalInfo);
			
			//判断是否开户的字段   false表示不开户，true表示开户
			boolean openFlag = false;
			
			qquery = new FinanaceAccountQuery();
			qquery.setRootInstCd(user.constId);
			qquery.setAccountRelateId(user.userId);
			qquery.setGroupManage(user.productId);
			qquery.setReferUserId(user.referUserId);
			qquery.setFinAccountTypeId(AccountConstants.ACCOUNT_TYPE_CHILD);
			List<FinanaceAccount> flist = finanaceAccountManager.queryList(qquery);
			//当查找结果为空则为最新授信需要开户，如果不为空，查找到账户失效则更新为有效
			if(flist == null || flist.isEmpty()){
				openFlag = true;
			}else{
				FinanaceAccount finanaceAccount = flist.get(0);
				if(!BaseConstants.ACCOUNT_STATUS_OK.equals(finanaceAccount.getStatusId())){
					logger.info("----------查找到授信子账户为失效状态，置为有效状态开始---------");
					finanaceAccount.setStatusId(BaseConstants.ACCOUNT_STATUS_OK);
					finanaceAccount.setAmount(Long.parseLong(BaseConstants.INITIAL_AMOUNT));
					finanaceAccount.setBalanceSettle(Long.parseLong(BaseConstants.INITIAL_AMOUNT));
					finanaceAccount.setBalanceFrozon(Long.parseLong(BaseConstants.INITIAL_AMOUNT));
					finanaceAccount.setBalanceOverLimit(Long.parseLong(BaseConstants.INITIAL_AMOUNT));
					finanaceAccount.setGroupSettle(AccountConstants.GROUP_SETTLE_BASE);
					finanaceAccount.setCurrency(BaseConstants.CURRENCY_CNY);
					finanaceAccount.setStartTime(DateUtils.getSysDate(Constants.DATE_FORMAT_YYYYMMDD));
					finanaceAccount.setBussControl(BaseConstants.BUSS_CONTRAL_TOTAL);
					finanaceAccount.setRemark("");
					finanaceAccount.setRecordMap("");
					
					finanaceAccountManager.updateFinanaceAccount(finanaceAccount);
					logger.info("----------查找到授信子账户为失效状态，置为有效状态结束---------");
				}
			}
			if(openFlag){
				//开户（授信子账户）
				String msg1 = this.openAccount(user,new FinanaceEntry());
				if (!"ok".equals(msg1)) {
					logger.info(msg1);
					throw new AccountException(msg1);
				}	
			}
		}catch(AccountException ae){
			logger.error(ae.getMessage());
			msg = "数据库操作异常!";
		}
		return msg;
	}
	
	/**
	 * 绑定账户
	 * 一个机构，一张卡，状态为有效，的卡只能有一张
	 * 结算卡只能有一张
	 * 提现卡只能有一张
	 * @return 成功：ok
	 */
	@Override
	public String bindingBankAccount(User user, AccountInfo accountInfo,ValidateData data) {
		synchronized (lock) {
		logger.info("绑定银行卡  入参AccountInfo:"+BeanUtil.getBeanVal(accountInfo, null));
		logger.info("绑定银行卡  入参ValidateData:"+BeanUtil.getBeanVal(data, null));
		logger.info("--用户ID--:"+user.userId+"--机构号--："+user.constId+"--产品号--："+user.productId+"--用户名--"+user.userName+"第三方用户ID"+user.referUserId);
		String msg = "ok";
		try {
			List<FinanaceAccount> faList = this.getAllAccount(user, "oper");
			if (faList != null && faList.size() > 0) {
				for (int j = 0; j < faList.size(); j++) {
					FinanaceAccount finanaceAccount = faList.get(j);
					if (BaseConstants.ACCOUNT_STATUS_OK.equals(finanaceAccount
							.getStatusId())) {
						AccountInfoQuery aQuery = new AccountInfoQuery();
						aQuery.setAccountNumber(accountInfo.getAccountNumber());
						aQuery.setStatus(Constants.ACCOUNT_NUM_STATRS_OK_ALL);
						aQuery.setRootInstCd(user.constId);
						List<AccountInfo> accountList = accountInfoManager
								.queryListByNumAndConstId(aQuery);
						if (accountList != null && accountList.size() > 0) {
							logger.info("该卡号已经绑定过，请勿重复绑定！");
							return "该卡号已经绑定过，请勿重复绑定！";
						}
						AccountInfoQuery acQuery = new AccountInfoQuery();
						acQuery.setAccountName(finanaceAccount
								.getAccountRelateId());
						acQuery.setFinAccountId(finanaceAccount.getFinAccountId());
						acQuery.setStatus(Constants.ACCOUNT_NUM_STATRS_1);
						List<AccountInfo> acList = accountInfoManager
								.queryList(acQuery);
						if (Constants.ACCOUNT_PROPERTY_PRIVATE.equals(accountInfo.getAccountProperty())) {
	                       for (AccountInfo accountInfoo : acList) {
	                           if (Constants.ACCOUNT_PROPERTY_PRIVATE.equals(accountInfoo.getAccountProperty())
	                               &&!accountInfo.getCertificateNumber().equals(accountInfoo.getCertificateNumber())) {
	                               logger.info("绑定对私卡时证件号必须相同！");
	                               return "绑定对私卡时证件号必须相同！";
	                           } 
	                        }                           
                        }
						if (Constants.ACCOUNT_PURPOSE_1.equals(accountInfo.getAccountPurpose())
								|| Constants.ACCOUNT_PURPOSE_3.equals(accountInfo.getAccountPurpose())
								|| Constants.ACCOUNT_PURPOSE_4.equals(accountInfo.getAccountPurpose())) {
							aQuery.setAccountNumber(null);
							aQuery.setAccountPurpose(accountInfo
									.getAccountPurpose());
							aQuery.setFinAccountId(finanaceAccount
									.getFinAccountId());
							aQuery.setStatus(Constants.ACCOUNT_NUM_STATRS_OK_ALL);
							List aList = accountInfoManager.queryList(aQuery);
							if (aList.size() != 0) {
								if (Constants.ACCOUNT_PURPOSE_1
										.equals(accountInfo.getAccountPurpose())) {
									logger.info("结算卡已经存在，请绑定其它卡！");
									return "结算卡已经存在，请绑定其它卡！";
								} else if (Constants.ACCOUNT_PURPOSE_3
										.equals(accountInfo.getAccountPurpose())) {
									logger.info("提现卡已经存在，请绑定其它卡！");
									return "提现卡已经存在，请绑定其它卡！";
								} else if (Constants.ACCOUNT_PURPOSE_4
										.equals(accountInfo.getAccountPurpose())) {
									logger.info("结算卡或者提现卡或者结算提现一体卡已经存在，请绑定其它卡！");
									return "结算卡或者提现卡或者结算提现一体卡已经存在，请绑定其它卡！";
								}
							}
						}
						accountInfo.setFinAccountId(finanaceAccount.getFinAccountId());
						//获取账户属性,如果是对私,状态为1
						if((Constants.ACCOUNT_PROPERTY_PRIVATE).equals(accountInfo.getAccountProperty())){
							accountInfo.setStatus(Constants.ACCOUNT_NUM_STATRS_1);
						}else if((Constants.ACCOUNT_PROPERTY_PUBLIC).equals(accountInfo.getAccountProperty())){
							String resultMsg = corporatAccountInfoService.accountInfoCheck(accountInfo,user.constId);
                            if("ok".equals(resultMsg)){
                            	accountInfo.setStatus(Constants.ACCOUNT_NUM_STATRS_1);
                            }else if("none".equals(resultMsg)){
    							//对公账户未审核,通过一分钱审核
    							accountInfo.setStatus(Constants.ACCOUNT_NUM_STATRS_2);
                            }else{
                            	logger.info(resultMsg);
                            	return resultMsg;
                            } 							
						}
						String validate="True";
	                    if (data!=null) {
	                    	//判断开户时的证件号是否相同          
				            if(data.getMerchantId().equals("M000004")&& accountInfo.getAccountProperty().equals("2")){
				            	FinanacePersonQuery finanacePersonQuery=new FinanacePersonQuery();
				    			finanacePersonQuery.setFinAccountId(accountInfo.getFinAccountId());
				    			List<FinanacePerson> finPersons= finanacePersonDao.selectByExample(finanacePersonQuery);
				    			if(finPersons!=null&&finPersons.size()>0){				    				
				    				if(null==finPersons.get(0).getCertificateNumber() || !finPersons.get(0).getCertificateNumber().equals(accountInfo.getCertificateNumber())){
				    					msg ="证件号与开户时的证件号码不符，请确认!";
	                                    logger.info("证件号与开户时的证件号码不符，请确认!");
	                                    return msg;
				    				}				    				
				    				if(null==finPersons.get(0).getPersonChnName() || !finPersons.get(0).getPersonChnName().equals(accountInfo.getAccountRealName())){
				    					msg ="姓名与开户时的姓名不符，请确认!";
	                                    logger.info("姓名与开户时的姓名不符，请确认!");
	                                    return msg;
				    				}
				    			}
				            }
	                    	
	                        String ifTo= ifToPengYuanVerify(user.constId);
	                        if (CodeEnum.SUCCESS.getCode().equals(ifTo)) {//去鹏元校验
	                            CommonResponse res= toPengYuanVerify(accountInfo,user.constId);
	                            if (CodeEnum.FAILURE.getCode().equals(res.getCode())) {
	                                msg ="上传信息与银行不一致!";
	                                logger.info("去鹏元校验失败:"+res.getMsg());
	                                return msg;
	                            }else if("2".equals(res.getCode())){
                                    msg ="系统忙，请稍后再试！";
                                    logger.info("去鹏元校验失败:"+res.getMsg());
                                    return msg; 
	                            } 
                            }else{//去通联校验
                                CommonResponse res = toTongLianVerify(accountInfo,user.constId);
                                if (!CodeEnum.SUCCESS.getCode().equals(res.getCode())) {
                                    logger.info("绑卡,去多渠道/通联校验失败后   msg="+res.getMsg());
                                   return res.getMsg();
                                }
                            }
	                     }
	                    if ("True".equals(validate)) {
	                        accountInfoManager.saveAccountInfo(accountInfo);
	                    }
					} else {
						logger.info("账户状态非正常！");
						msg = "账户状态非正常！";
						break;
					}
				}
			} else {
				logger.info("暂无账户信息！");
				msg = "暂无账户信息！";
			}
		} catch (Exception e) {
			logger.error("绑定账户信息失败！"+e.getMessage());
			msg = "绑定账户信息失败！";
			e.printStackTrace();
		}
		return msg;
		}
	}
	/**
	 * 如果是对公账户,发送一分钱
	 * @param accountInfo
	 * @param finanaceAccount
	 */
    public void accountOnecent(AccountInfo accountInfo,FinanaceAccount finanaceAccount){
    	if((Constants.ACCOUNT_PROPERTY_PUBLIC).equals(accountInfo.getAccountProperty())){
			
			TransOrderInfo transOrderInfo=new TransOrderInfo();
			transOrderInfo.setUserId(TransCodeConst.THIRDPARTYID_DGZHJYZCZH);//?
			transOrderInfo.setAmount(Long.valueOf(1));
			transOrderInfo.setFuncCode(TransCodeConst.PAYMENT_WITHHOLD);
			transOrderInfo.setInterMerchantCode(accountInfo.getAccountName()); //?
			transOrderInfo.setMerchantCode(finanaceAccount.getRootInstCd());  //?
			transOrderInfo.setOrderAmount(Long.valueOf(1));
			DateUtil dateUtil=new DateUtil();
			transOrderInfo.setOrderDate(dateUtil.getNow());
			transOrderInfo.setOrderNo(redisIdGenerator.createRequestNo());
			transOrderInfo.setRequestTime(dateUtil.getNow());
			transOrderInfo.setStatus(TransCodeConst.TRANS_STATUS_NORMAL);
			transOrderInfo.setUserFee(0L);
			transOrderInfo.setRemark(Constants.TRANS_ORDER_REMARK);
			transOrderInfo.setTradeFlowNo(accountInfo.getAccountNumber());
			transOrderInfo.setBusiTypeId(Constants.BIZ_TYPE_PENNY);
			String productId=Constants.FN_PRODUCT;
			ErrorResponse oneCent = paymentAccountService.withhold(transOrderInfo, productId);
			if(oneCent.isIs_success()){
		     logger.info("代付成功"+transOrderInfo.getUserId()+"对公账户代付发起成功,更新一行卡状态为审核中,卡号为"+accountInfo.getAccountNumber());
			//将银行卡状态改为审核中
			accountInfo.setStatus(Constants.ACCOUNT_NUM_STATRS_3);
			accountInfoManager.updateByPrimaryKeySelective(accountInfo);
			}else{
				logger.info("对公账户发起代付一分钱失败发送邮件,账户卡号为"+accountInfo.getAccountNumber());
				RkylinMailUtil.sendMailThread("对公账户发起代付一分钱失败",transOrderInfo.getUserId()+"对公账户代付发起失败", TransCodeConst.FINANACE_ENTRY_ERROR_TOEMAIL);
			}
    	}
    }
	/**
	 * 授信信息查询
	 * provideruserid  授信方用户ID 目前为P2p  userId
	 * @return 
	 */
	@Override
	public List<CreditInfo> creditInfoQuery(User user, String provideruserid) {
		logger.info("------授信信息查询------");
		logger.info("--用户ID--:"+user.userId+"--机构号--："+user.constId+"--产品号--："+user.productId+"--用户名--"+user.userName+"第三方用户ID"+provideruserid);
		List<CreditInfo> creditList = new ArrayList<CreditInfo>();
		try {
//			FinanaceAccountQuery fquery = new FinanaceAccountQuery();
//			fquery.setAccountRelateId(provideruserid);
//			List<FinanaceAccount> fList = finanaceAccountManager
//					.queryList(fquery);
//			if (fList != null && fList.size() > 0) {
				CreditInfoQuery query = new CreditInfoQuery();
				query.setRootInstCd(user.constId);
				query.setUserId(user.userId);
				query.setProviderId(provideruserid);
				query.setEndTime(null);
				creditList = creditInfoManager.queryList(query);
//			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return creditList;
	}

	/**
	 * 还款账单信息查询
	 */
	@Override
	public List<CreditRepaymentHistory> getRepaymentInfo(User user,
			String repayDate) {
		logger.info("--------还款账单信息查询----------还款日期："+repayDate);
		logger.info("--用户ID--:"+user.userId+"--机构号--："+user.constId+"--产品号--："+user.productId+"--用户名--"+user.userName+"第三方用户ID"+user.referUserId);
		List<CreditRepaymentHistory> list = new ArrayList<CreditRepaymentHistory>();
		try {
			CreditRepaymentHistoryQuery creditRepaymentHistoryQuery = new CreditRepaymentHistoryQuery();
			if(repayDate!=null && !"".equals(repayDate)){
				creditRepaymentHistoryQuery.setRepaidDate(DateUtils.getDate(
						repayDate, Constants.DATE_FORMAT_YYYYMMDD));
			}
			creditRepaymentHistoryQuery.setUserId(user.userId);
			list = creditRepaymentHistoryManager
					.queryList(creditRepaymentHistoryQuery);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return list;
	}

	@Override
	public Response doJob(Map<String, String[]> paramMap, String methodName) {
	    if("ruixue.wheatfield.pingan.account.open".equals(methodName)){
            logger.info("平安开子账户  输入参数="+getMapKeyVal(paramMap));
            PingAnOpenAccountResponse accResponse= new PingAnOpenAccountResponse(); 
            String[] paramArray = new String[]{"userid","instcode","accproductcode","rolecode","subaccountname"};
            for (String param:paramArray) {
                if (!ValHasNoParam.hasParam(paramMap,param)) {
                    accResponse.setSuccess(false);
                    accResponse.setCode(CodeEnum.FAILURE.getCode());
                    accResponse.setMsg(param+"不能为空！");
                    return accResponse;
                }
            }
            com.rkylin.wheatfield.bean.User userr = new com.rkylin.wheatfield.bean.User();
            userr.setUserId(getArrayOneVal(paramMap.get("userid")));
            userr.setInstCode(getArrayOneVal(paramMap.get("instcode")));
            userr.setProductId(getArrayOneVal(paramMap.get("accproductcode")));
            userr.setName(getArrayOneVal(paramMap.get("subaccountname")));//子账户姓名
            userr.setRoleCode(getArrayOneVal(paramMap.get("rolecode")));
            OpenSubAccountResponse res = accountInfoService.openSubAccountMice(userr);
            if (!CodeEnum.SUCCESS.getCode().equals(res.getCode())) {
                accResponse.setSuccess(false);
                accResponse.setCode(CodeEnum.FAILURE.getCode());
                accResponse.setMsg(res.getMsg());
                return accResponse;
            }
            accResponse.setSubAccountNo(res.getSubAccountNo()); 
            return accResponse;
        }
		if (!"ruixue.wheatfield.creditapprovalinfo.query".equals(methodName)){
			if (!ValHasNoParam.hasParam(paramMap, "userid")) {
				return errorResponseService.getErrorResponse("P1", "userid不能为空");
			}
			if (!ValHasNoParam.hasParam(paramMap, "usertype")) {
				return errorResponseService.getErrorResponse("P2", "usertype不能为空");
			}
			if (!ValHasNoParam.hasParam(paramMap, "productid")) {
				return errorResponseService.getErrorResponse("P4", "productid不能为空");
			}
		}

		if (!ValHasNoParam.hasParam(paramMap, "constid")) {
			return errorResponseService.getErrorResponse("P3", "constid不能为空");
		}
		User user = new User();
		for (Object keyObj : paramMap.keySet().toArray()) {
			String[] strs = paramMap.get(keyObj);
			for (String value : strs) {
				if (keyObj.equals("userid")) {
					user.userId = value;
				} else if (keyObj.equals("usertype")) {
					user.userType = UserTypeEnum.toEnum(value);
				} else if (keyObj.equals("constid")) {
					user.constId = value;
				} else if (keyObj.equals("productid")) {
					user.productId = value;
				} else if (keyObj.equals("role")) {
					user.role = value;
				} else if (keyObj.equals("referuserid")) {
					user.referUserId = value;
				} else if (keyObj.equals("statusid")){
					user.statusID = value;
				}
			}
		}
		AccountInfoGetResponse response = new AccountInfoGetResponse();
		if ("ruixue.wheatfield.account.open".equals(methodName)) {
			if (!ValHasNoParam.hasParam(paramMap, "username")) {
				return errorResponseService.getErrorResponse("P5", "用户名称不能为空");
			}
			for (Object keyObj : paramMap.keySet().toArray()) {
				String[] strs = paramMap.get(keyObj);
				for (String value : strs) {
					if (keyObj.equals("username")) {
						user.userName = value;
					}
				}
			}
			String msg = this.openAccount(user, new FinanaceEntry());
			if ("ok".equals(msg)) {
				response.setIs_success(true);
			} else {
				return errorResponseService.getErrorResponse("P6", msg);
			}
		
		}else if ("ruixue.wheatfield.bankaccount.binding".equals(methodName)) {
			// if(!ValHasNoParam.hasParam(paramMap, "accountname")){
			// return errorResponseService.getErrorResponse("P19","账户名称不能为空");
			// }
			if (!ValHasNoParam.hasParam(paramMap, "accountnumber")) {
				return errorResponseService.getErrorResponse("P6", "账号不能为空");
			}
			if (!CommUtil.isNum(CommUtil.getArrayOneVal(paramMap.get("accountnumber")))) {
			    return errorResponseService.getErrorResponse("P6", "账号必须为数字!");
            }
			
			if (!ValHasNoParam.hasParam(paramMap, "accounttypeid")) {
				return errorResponseService
						.getErrorResponse("P7", "账户类型ID不能为空");
			}
		     if (!CommUtil.isNum(CommUtil.getArrayOneVal(paramMap.get("accounttypeid")))) {
	                return errorResponseService.getErrorResponse("P7", "账户类型ID必须为数字!");
	            }
			// if(!ValHasNoParam.hasParam(paramMap, "bankbranchname")){
			// return errorResponseService.getErrorResponse("P9","开户行支行名称不能为空");
			// }
			if (!ValHasNoParam.hasParam(paramMap, "bankheadname")) {
				return errorResponseService.getErrorResponse("P8",
						"开户行总行名称不能为空");
			}
			if (!ValHasNoParam.hasParam(paramMap, "currency")) {
				return errorResponseService.getErrorResponse("P10", "币种不能为空");
			}
			// if(!ValHasNoParam.hasParam(paramMap, "openaccountdate")){
			// return errorResponseService.getErrorResponse("P11","开户日期不能为空");
			// }
			// if(!ValHasNoParam.hasParam(paramMap, "openaccountdescription")){
			// return errorResponseService.getErrorResponse("P12","账号用途不能为空");
			// }
			if (!ValHasNoParam.hasParam(paramMap, "accountpurpose")) {
				return errorResponseService.getErrorResponse("P13", "账户目的不能为空");
			}
		    if (!CommUtil.isNum(CommUtil.getArrayOneVal(paramMap.get("accountpurpose")))) {
	            return errorResponseService.getErrorResponse("P13", "账户目的必须为数字!");
	        }
			if (!ValHasNoParam.hasParam(paramMap, "accountproperty")) {
				return errorResponseService.getErrorResponse("P14", "账户属性不能为空");
			}
	        if (!CommUtil.isNum(CommUtil.getArrayOneVal(paramMap.get("accountproperty")))) {
	            return errorResponseService.getErrorResponse("P14", "账户属性必须为数字!");
	        }
//			 if(!ValHasNoParam.hasParam(paramMap, "certificatetype")){
//			 return errorResponseService.getErrorResponse("P15","开户证件类型不能为空");
//			 }
			if(!ValHasNoParam.hasParam(paramMap, "certificatenumnumber")){
				 return errorResponseService.getErrorResponse("P16","证件号不能为空");
			}
//			if (!ValHasNoParam.hasParam(paramMap, "user_pass")) {
//				return errorResponseService.getErrorResponse("P17", "密码不能为空");
//			}
			if (!ValHasNoParam.hasParam(paramMap, "req_sn")) {
				return errorResponseService
						.getErrorResponse("P18", "交易批次号不能为空");
			}
			if (!ValHasNoParam.hasParam(paramMap, "submit_time")) {
				return errorResponseService.getErrorResponse("P19", "提交时间不能为空");
			}
		    if (!CommUtil.isNum(CommUtil.getArrayOneVal(paramMap.get("submit_time")))) {
	            return errorResponseService.getErrorResponse("P19", "提交时间必须为数字!");
	        }
			if (!ValHasNoParam.hasParam(paramMap, "account_name")) {
				return errorResponseService.getErrorResponse("P20", "账号名不能为空");
			}
			if (!ValHasNoParam.hasParam(paramMap, "bank_code")) {
				return errorResponseService.getErrorResponse("P21", "银行编码不能为空");
			}
		    if (!CommUtil.isNum(CommUtil.getArrayOneVal(paramMap.get("bank_code")))) {
	            return errorResponseService.getErrorResponse("P21", "银行编码必须为数字!");
	        }
		    if (CommUtil.getArrayOneVal(paramMap.get("tel"))!=null&&!CommUtil.isNum(CommUtil.getArrayOneVal(paramMap.get("tel")))) {
                return errorResponseService.getErrorResponse("P22", "手机号必须为数字!");
            }
            if (CommUtil.getArrayOneVal(paramMap.get("relatedcard"))!=null&&!CommUtil.isNum(CommUtil.getArrayOneVal(paramMap.get("relatedcard")))) {
                return errorResponseService.getErrorResponse("P23", "关联卡号必须为数字!");
            }
            String openDate = CommUtil.getArrayOneVal(paramMap.get("openaccountdate"));
            if (openDate!=null) {
                if (!DateUtil.isYYYYMMDD(openDate)&&!DateUtil.isYYYYMMDDHHMMSS(openDate)){
                    return errorResponseService.getErrorResponse("P24", "开户日期非法!");
                }
            }            
			AccountInfo accountInfo = new AccountInfo();
			ValidateData data = new ValidateData();
			for (Object keyObj : paramMap.keySet().toArray()) {
				String[] strs = paramMap.get(keyObj);
				for (String value : strs) {
					// if("accountname".equals(keyObj)){
					// accountInfo.setAccountName(value);
					// }else
					if ("accountnumber".equals(keyObj)) {
						data.setAccountNo(value);
						accountInfo.setAccountNumber(value);
					} else if ("accounttypeid".equals(keyObj)) {
						accountInfo.setAccountTypeId(value);
						data.setAccountType(value);
					} else if ("bankbranchname".equals(keyObj)) {
						accountInfo.setBankBranchName(value);
					} else if ("bankheadname".equals(keyObj)) {
						accountInfo.setBankHeadName(value);
					} else if ("currency".equals(keyObj)) {
						accountInfo.setCurrency(value);
					} else if ("openaccountdate".equals(keyObj)) {
						try {
							Date date = DateUtils.getDate(value,
									Constants.DATE_FORMAT_YYYYMMDD);
							accountInfo.setOpenAccountDate(date);
						} catch (Exception e) {
							e.printStackTrace();
							return errorResponseService
									.getErrorResponse(ErrorCodeConstants.SYS_ERROR_CODE_M1);
						}
					} else if ("openaccountdescription".equals(keyObj)
							&& value != null && !"".equals(value)) {
						accountInfo.setOpenAccountDescription(value);
					} else if ("accountpurpose".equals(keyObj)) {
						accountInfo.setAccountPurpose(value);

					} else if ("accountproperty".equals(keyObj)) {

						accountInfo.setAccountProperty(value);
						// 对公
						if ("1".equals(value)) {
							data.setAccountPorp(value);
						} else {
							data.setAccountPorp("0");
						}
					} else if ("certificatetype".equals(keyObj)) {
						accountInfo.setCertificateType(value);
						data.setIdType(value);
					} else if ("certificatenumnumber".equals(keyObj)) {
						accountInfo.setCertificateNumber(value);
						data.setId(value);
					}  else if ("req_sn".equals(keyObj)) {
						data.setReqSn(value);
					} else if ("constid".equals(keyObj)) {
						data.setMerchantId(value);
					} else if ("submit_time".equals(keyObj)) {	
						data.setSubmitTime(value);
					} else if ("bindid".equals(keyObj)) {
						data.setBindId(value);
					} else if ("relatid".equals(keyObj)) {
						data.setRelatId(value);
					} else if ("account_type".equals(keyObj)) {
						data.setAccountType(value);
					} else if ("bank_code".equals(keyObj)) {
						data.setBankCode(value);
						accountInfo.setBankHead(value);
					} else if ("account_name".equals(keyObj)) {
						data.setAccountName(value);
						accountInfo.setAccountRealName(value);
					} else if ("relatedcard".equals(keyObj)) {
						data.setRelatedCard(value);
					} else if ("tel".equals(keyObj)) {
						data.setTel(value);
						accountInfo.setOpenAccountDescription(value);
					} else if ("merrem".equals(keyObj)) {
						data.setMerrem(value);
					} else if ("remark".equals(keyObj)) {
						data.setRemark(value);
					} else if ("userid".equals(keyObj)) {
//						data.setUserName(value);
						accountInfo.setAccountName(value);
					}else if("bank_branch".equals(keyObj)){
						accountInfo.setBankBranch(value);
					}else if("bank_province".equals(keyObj)){
						accountInfo.setBankProvince(value);
					}else if("bank_city".equals(keyObj)){
						accountInfo.setBankCity(value);
					}
				}
			}
			if(!"0".equals(data.getAccountPorp())){
			    data=null;
			}
			String msg = this.bindingBankAccount(user, accountInfo,data);
			if ("ok".equals(msg)) {
				response.setIs_success(true);
			} else {
				logger.info("----C1--bind-"+msg);
				return errorResponseService.getErrorResponse("C1", msg);
			}
		} else if ("ruixue.wheatfield.account.check".equals(methodName)) {
			if (!ValHasNoParam.hasParam(paramMap, "statusid")) {
				user.statusID = BaseConstants.ACCOUNT_STATUS_OK;
			}
			String msg = this.checkAccount(user);
			if ("ok".equals(msg)) {
				response.setIs_success(true);
			} else {
				return errorResponseService.getErrorResponse("C1", msg);
			}
		} else if ("ruixue.wheatfield.account.close".equals(methodName)) {
			String msg = this.closeAccount(user);
			if ("ok".equals(msg)) {
				response.setIs_success(true);
			} else {
				return errorResponseService.getErrorResponse("C1", msg);
			}
		} else if ("ruixue.wheatfield.account.credit".equals(methodName)) {
			if (!ValHasNoParam.hasParam(paramMap, "amount")) {
				return errorResponseService.getErrorResponse("P6", "授信金额不能为空");
			}
			if (!ValHasNoParam.hasParam(paramMap, "agmtcode")) {
				return errorResponseService
						.getErrorResponse("P7", "授信协议ID不能为空");
			}
			if (!ValHasNoParam.hasParam(paramMap, "credittypeid")) {
				return errorResponseService
						.getErrorResponse("P8", "授信种类ID不能为空");
			}
			if (!ValHasNoParam.hasParam(paramMap, "rate")) {
				return errorResponseService.getErrorResponse("P9", "授信利率不能为空");
			}
			if (!ValHasNoParam.hasParam(paramMap, "currency")) {
				return errorResponseService.getErrorResponse("P10", "币种不能为空");
			}
			if (!ValHasNoParam.hasParam(paramMap, "provideruserid")) {
				return errorResponseService.getErrorResponse("P13",
						"授信提供方ID不能为空");
			}
			if (!ValHasNoParam.hasParam(paramMap, "agmtname")) {
				return errorResponseService.getErrorResponse("P14",
						"授信协议名称不能为空");
			}
			CreditInfo creditInfo = new CreditInfo();
			AccountAgreement accountAgreement = new AccountAgreement();
			creditInfo.setStartTime(DateUtils
					.getSysDate(Constants.DATE_FORMAT_YYYYMMDD));
			String provideruserid = "";
			for (Object keyObj : paramMap.keySet().toArray()) {
				String[] strs = paramMap.get(keyObj);
				for (String value : strs) {
					if ("amount".equals(keyObj)) {
						creditInfo.setAmount(Long.parseLong(value));
					} else if ("provideruserid".equals(keyObj)) {
						provideruserid = value;
					} else if ("credittypeid".equals(keyObj)) {
						creditInfo.setCreditTypeId(Integer.parseInt(value));
					} else if ("rate".equals(keyObj)) {
						creditInfo.setRate(value);
					} else if ("currency".equals(keyObj)) {
						creditInfo.setCurrency(value);
					} else if ("agmtcode".equals(keyObj)) {
						accountAgreement.setAgmtCode(value);
					} else if ("agmtname".equals(keyObj)) {
						accountAgreement.setAgmtName(value);
					}

				}
			}
			String msg = "";
			try {
				msg = this.creditAccount(user, creditInfo, accountAgreement,
						provideruserid);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error(e.getMessage());
				msg=e.getMessage();
			}
			if ("ok".equals(msg)) {
				response.setIs_success(true);
			} else {
				return errorResponseService.getErrorResponse("C1", msg);
			}
		} else if ("ruixue.wheatfield.creditinfo.query".equals(methodName)) {
			if (!ValHasNoParam.hasParam(paramMap, "provideruserid")) {
				return errorResponseService.getErrorResponse("P6",
						"授信提供方用户ID不能为空");
			}
			String provideruserid = "";
			for (Object keyObj : paramMap.keySet().toArray()) {
				String[] strs = paramMap.get(keyObj);
				for (String value : strs) {
					if ("provideruserid".equals(keyObj)) {
						provideruserid = value;
					}
				}
			}
			List<CreditInfo> creditInfoList = this.creditInfoQuery(user,
					provideruserid);
			if (creditInfoList != null && creditInfoList.size() > 0) {
				String billday=getBillorPaymentDay("BillDay");
				String RepaymentDay=getBillorPaymentDay("RepaymentDay");
				for (int i = 0; i < creditInfoList.size(); i++) {
					creditInfoList.get(i).setBillday(billday);
					creditInfoList.get(i).setRepaymentday(RepaymentDay);
				}
				response.setCreditinfos(creditInfoList);
//				List<Creditinfo> cList = new ArrayList<Creditinfo>();
//				for (int i = 0; i < creditInfoList.size(); i++) {
//					CreditInfo creditInfo = creditInfoList.get(i);
//					Creditinfo cre = new Creditinfo();
//					if (creditInfo.getId() != null
//							&& !"".equals(creditInfo.getId())) {
//						cre.setBillday(billday);
//						cre.setRepaymentday(RepaymentDay);
//						cre.setAmount(creditInfo.getAmount().toString());
//						cre.setCreditagreementid(creditInfo
//								.getCreditAgreementId());
//						cre.setCredittypeid(creditInfo.getCreditTypeId()
//								.toString());
//						cre.setCurrency(creditInfo.getCurrency());
//						cre.setEndtime(DateUtils.getDateFormat(
//								Constants.DATE_FORMAT_YYYYMMDD,
//								creditInfo.getEndTime()));
//						cre.setMerchantid(creditInfo.getRootInstCd());
//						cre.setProviderid(creditInfo.getProviderId());
//						cre.setRate(creditInfo.getRate());
//						cre.setStarttime(DateUtils.getDateFormat(
//								Constants.DATE_FORMAT_YYYYMMDD,
//								creditInfo.getStartTime()));
//						cre.setUserid(creditInfo.getUserId());
//						cList.add(cre);
//					}
//				}
//				if (cList != null && cList.size() > 0) {
//					response.setCreditinfos(cList);
//				} else {
//					return errorResponseService.getErrorResponse("C1",
//							"获取授信明细失败！");
//				}
			} else {
				return errorResponseService.getErrorResponse("C2", "获取授信明细失败！");
			}

		} else if ("ruixue.wheatfield.account.freeze".equals(methodName)) {
			String msg = this.freezeAccount(user);
			if ("ok".equals(msg)) {
				response.setIs_success(true);
			} else {
				return errorResponseService.getErrorResponse("C1", msg);
			}
		} else if ("ruixue.wheatfield.creditrepaymenthistory.query"
				.equals(methodName)) {
			String repaydate = "";
			for (Object keyObj : paramMap.keySet().toArray()) {
				String[] strs = paramMap.get(keyObj);
				for (String value : strs) {
					if ("repaydate".equals(keyObj)) {
						repaydate = value;
					}
				}
			}
			FinanaceAccountQuery query = new FinanaceAccountQuery();
			query.setRootInstCd(user.constId);
			query.setAccountRelateId(user.userId);
			query.setGroupManage(user.productId);
			// query.setFinAccountTypeId(AccountConstants.ACCOUNT_TYPE_BASE);
			List<FinanaceAccount> faList = finanaceAccountManager
					.queryList(query);
			if (faList != null && faList.size() > 0) {

				List<CreditRepaymentHistory> creditRepaymentHistoryList = this
						.getRepaymentInfo(user, repaydate);
				if (creditRepaymentHistoryList != null
						&& creditRepaymentHistoryList.size() > 0) {
					response.setCreditrepaymenthistorys(creditRepaymentHistoryList);
				} else {
					return errorResponseService.getErrorResponse("C1",
							"暂无还款明细！");
				}
			} else {
				return errorResponseService.getErrorResponse("C2",
						"该账户无效请检查用户信息！");
			}
		} else if ("ruixue.wheatfield.accountinfo.query".equals(methodName)) {
			if (!ValHasNoParam.hasParam(paramMap, "objorlist")) {
				return errorResponseService.getErrorResponse("P1", "类型不能为空");
			}
			String objOrList = "";
			for (Object keyObj : paramMap.keySet().toArray()) {
				String[] strs = paramMap.get(keyObj);
				for (String value : strs) {
					if ("objorlist".equals(keyObj)) {
						objOrList = value;
					}
				}
			}
			List<AccountInfo> accountList = this
					.getAccountList(user, objOrList);
			
			if (accountList != null && accountList.size() > 0) {
				response.setAccountinfos(accountList);
			} else {
				return errorResponseService.getErrorResponse("C1", "暂无数据！");
			}		
		} else if ("ruixue.wheatfield.accountinfoplus.query".equals(methodName)) {
			if (!ValHasNoParam.hasParam(paramMap, "objorlist")) {
				return errorResponseService.getErrorResponse("P1", "类型不能为空");
			}
			String objOrList = "";
			for (Object keyObj : paramMap.keySet().toArray()) {
				String[] strs = paramMap.get(keyObj);
				for (String value : strs) {
					if ("objorlist".equals(keyObj)) {
						objOrList = value;
					}
				}
			}
			List<AccountInfoPlus> accountList = this.getAccountListPlus(user, objOrList);
			
			if (accountList != null && accountList.size() > 0) {
				response.setAccountinfopluss(accountList);
			} else {
				return errorResponseService.getErrorResponse("C1", "暂无数据！");
			}
		}else if ("ruixue.wheatfield.accountinfo.delete".equals(methodName)) {
			if (!ValHasNoParam.hasParam(paramMap, "accountnumber")) {
				return errorResponseService.getErrorResponse("P1", "银行号不能为空");
			}
			String accountNumber = "";
			for (Object keyObj : paramMap.keySet().toArray()) {
				String[] strs = paramMap.get(keyObj);
				for (String value : strs) {
					if ("accountnumber".equals(keyObj)) {
						accountNumber = value;
					}
				}
			}
			String msg = this.delAccountInfo(user, accountNumber);
			if ("ok".equals(msg)) {
				response.setIs_success(true);
			} else {
				return errorResponseService.getErrorResponse("C1", msg);
			}
		} else if ("ruixue.wheatfield.accountinfo.update".equals(methodName)) {
			if (!ValHasNoParam.hasParam(paramMap, "accountnumber")) {
				return errorResponseService.getErrorResponse("P5", "银行号不能为空");
			}
			String accountNumber = "";
			for (Object keyObj : paramMap.keySet().toArray()) {
				String[] strs = paramMap.get(keyObj);
				for (String value : strs) {
					if ("accountnumber".equals(keyObj)) {
						accountNumber = value;
					}
				}
			}
			String msg = this.updateAccountInfo(user, accountNumber);
			if ("ok".equals(msg)) {
				response.setIs_success(true);
			} else {
				return errorResponseService.getErrorResponse("C1", msg);
			}
		}else if("ruixue.wheatfield.account.rmfreeze".equals(methodName)){
			
			String msg=this.rmFreeze(user);
			if("ok".equals(msg)){
				response.setIs_success(true);
			}else{
				return errorResponseService.getErrorResponse("C1", msg);
			}
		}else if("ruixue.wheatfield.accountnum.ckeck".equals(methodName)){
			if (!ValHasNoParam.hasParam(paramMap, "accountnumber")) {
				return errorResponseService.getErrorResponse("P5", "银行号不能为空");
			}
			String accountNumber = "";
			for (Object keyObj : paramMap.keySet().toArray()) {
				String[] strs = paramMap.get(keyObj);
				for (String value : strs) {
					if ("accountnumber".equals(keyObj)) {
						accountNumber = value;
					}
				}
			}
			String msg=this.accountCkeck(user, accountNumber);
			if(msg !=null){
				return errorResponseService.getErrorResponse("C1", msg);
			}else{
				response.setIs_success(true);
			}
		}else if("ruixue.wheatfield.account.status.get".equals(methodName)){
			if (!ValHasNoParam.hasParam(paramMap, "accountnumber")) {
				return errorResponseService.getErrorResponse("P5", "银行号不能为空");
			}
			String accountNumber = "";
			for (Object keyObj : paramMap.keySet().toArray()) {
				String[] strs = paramMap.get(keyObj);
				for (String value : strs) {
					if ("accountnumber".equals(keyObj)) {
						accountNumber = value;
					}
				}
			}
			String msg=this.getAccountByNo(user, accountNumber);
			if(!"ok".equals(msg)){
				return errorResponseService.getErrorResponse("C1", msg);
			}else{
				response.setIs_success(true);
			}
		}else if("ruixue.wheatfield.account.update".equals(methodName)){
			if (!ValHasNoParam.hasParam(paramMap, "accountid")) {
				return errorResponseService.getErrorResponse("P5", "账号主键不能为空！");
			}
			//待修改
			Map<String ,String> map=new HashMap<String, String>();
			for (Object keyObj : paramMap.keySet().toArray()) {
				String[] strs = paramMap.get(keyObj);
				map.put(String.valueOf(keyObj), strs[0]);
			}
			String msg=this.updateCreatesAccount(user,map);
			if(!"ok".equals(msg)){
				return errorResponseService.getErrorResponse("C1", msg);
			}else{
				response.setIs_success(true);
			}
		}else if ("ruixue.wheatfield.creditapprovalinfo.query".equals(methodName)) {//授信信息结果查询，使用新表CreditApprovalInfo
			String provideruserid = "";
			if (ValHasNoParam.hasParam(paramMap, "provideruserid")) {
				for (Object keyObj : paramMap.keySet().toArray()) {
					String[] strs = paramMap.get(keyObj);
					for (String value : strs) {
						if ("provideruserid".equals(keyObj)) {
							provideruserid = value;
						}
					}
				}				
			}

			List<CreditApprovalInfo> creditInfoList = creditApprovalInfoService.selectcreditInfo(user, provideruserid);
			if (creditInfoList != null && creditInfoList.size() > 0) {
				String billday=getBillorPaymentDay("BillDay");
				String RepaymentDay=getBillorPaymentDay("RepaymentDay");
				for (int i = 0; i < creditInfoList.size(); i++) {
					creditInfoList.get(i).setBillday(billday);
					creditInfoList.get(i).setRepaymentday(RepaymentDay);
				}
				response.setCreditApprovalInfos(creditInfoList);
			} else {
				return errorResponseService.getErrorResponse("C2", "获取授信明细失败！");
			}
		}
		return response;
	}
	
    public static <T> T getArrayOneVal(T[] array){
        if (array==null||array.length==0) {
            return null; 
        }
        return array[0];
    }
	
    /**
     * 获取map的键值(rop输入参数的打印)
     * @param paramMap
     * @return
     */
    private String getMapKeyVal(Map<String, String[]> paramMap){
        if (paramMap==null||paramMap.size()==0) {
            return "";
        }
        StringBuffer paramsBuffer = new StringBuffer();
        for (Object keyObj : paramMap.keySet().toArray()) {
            String[] strs = paramMap.get(keyObj);
            if (strs == null) {
                continue;
            }
            for (String value : strs) {
                paramsBuffer.append(keyObj + "=" + value + ", ");
            }
        }
        return paramsBuffer.toString();
    }
    
	public List<FinanaceAccount> getAllAccount(User user,String oper){
		FinanaceAccountQuery query = new FinanaceAccountQuery();
		query.setRootInstCd(user.constId);
		query.setAccountRelateId(user.userId);
		if (user.productId != null && !"".equals(user.productId)) {
			query.setGroupManage(user.productId);
		}
		if(user.referUserId!=null && !"".equals(user.referUserId)){
			query.setFinAccountTypeId(AccountConstants.ACCOUNT_TYPE_CHILD);
			query.setReferUserId(user.referUserId);
		}else{
			if("oper".equals(oper)){
				query.setFinAccountTypeId(AccountConstants.ACCOUNT_TYPE_BASE);
			}
		}
		List<FinanaceAccount> faList = finanaceAccountManager
				.queryList(query);
		return faList;
	}
	public String getBillorPaymentDay(String parameterCode){
		ParameterInfoQuery query=new ParameterInfoQuery();
		query.setParameterCode(parameterCode);
		List<ParameterInfo> parameterList=parameterInfoManager.queryList(query);
		if(parameterList!=null && parameterList.size()>0){
			return parameterList.get(0).getParameterValue();
		}
		return null;
	}
	
	
	//定时任务每天查询accountInfo中待审核对公账户,调用待支付接口 start 
	public void onecentService(){
		AccountInfoQuery acQuery = new AccountInfoQuery();
		//对公账户
		acQuery.setAccountProperty("1");
		acQuery.setStatus(Constants.ACCOUNT_NUM_STATRS_2);
		List<AccountInfo> accountInfoList = accountInfoManager
				.queryoneCent(acQuery);
		if(null!=accountInfoList&&accountInfoList.size()>0){
		for (int i = 0; i <accountInfoList.size(); i++) {
			AccountInfo accountInfo=accountInfoList.get(i);
			logger.info("银行卡待审核条数"+accountInfoList.size()+"银行卡卡号"+accountInfo.getAccountNumber()+accountInfo);
			FinanaceAccountQuery  finanaceAccountQuery=new FinanaceAccountQuery();
			finanaceAccountQuery.setFinAccountId(accountInfo.getFinAccountId());
			List<FinanaceAccount>  finanaceAccountList= finanaceAccountManager.queryList(finanaceAccountQuery);
			FinanaceAccount finanaceAccount=null;
			if(null!=finanaceAccountList&&finanaceAccountList.size()>0){
				//使用主键查询只有一条记录
				finanaceAccount=finanaceAccountList.get(0);
			}
			this.accountOnecent(accountInfo, finanaceAccount);
		}
		}
	}

	
	/**
	 * 查询对公账户一分钱代付结果，修改对公账户状态 2015-09-06
	 */
	public void updatePubAccountByPayResult(){
		// 查询已经处理的一分钱代付结果（7天内）
		List<GenerationPaymentHistory> genPayHisList=genPayHistoryDao.selectPayResOfJudgePubAccount();
		logger.info("代收付系统已经返回的一分钱代付结果个数是="+genPayHisList.size());
		if (genPayHisList.size()==0) {
			return;
		}
		List<CorporatAccountInfo> corporatAccountInfoList = new ArrayList<CorporatAccountInfo>();
		for (GenerationPaymentHistory genPayHis:genPayHisList) {
			CorporatAccountInfo corporatAccountInfo = new CorporatAccountInfo();
			corporatAccountInfo.setAccountNumber(genPayHis.getAccountNo());
			corporatAccountInfo.setRootInstCd(genPayHis.getRootInstcd());
			corporatAccountInfo.setStatusId(Constants.ACCOUNT_NUM_STATRS_1);
			logger.info("genPayHis.getAccountNo()=="+genPayHis.getAccountNo()+"  genPayHis.getSendType()="+genPayHis.getSendType());
			
			if(genPayHis.getSendType() == null){
				continue;
			}
			if (SettleConstants.SEND_DEFEAT==genPayHis.getSendType()) { //失败
				corporatAccountInfo.setStatusId(Constants.ACCOUNT_NUM_STATRS_4);
			}
			corporatAccountInfoList.add(corporatAccountInfo);
		}
		//批量修改对公账户状态；
		corporatAccountInfoDao.batchUpdate(corporatAccountInfoList);
	}
	
	/**
	 * 代付一分钱校验对公账户（定时调度）  2015-09-06
	 */
	public void paymentJudgePublicAccount(){
		CorporatAccountInfoQuery corQuy = new CorporatAccountInfoQuery();
		corQuy.setStatusId(Constants.ACCOUNT_NUM_STATRS_2);
		List<CorporatAccountInfo> accountInfoList = corporatAccountInfoManager.queryList(corQuy);
		logger.info("对公银行卡待审核条数="+accountInfoList.size());
		if (accountInfoList.size()==0) {
			return;
		}
		List<CorporatAccountInfo> corporatAccountInfoList = new ArrayList<CorporatAccountInfo>();
		for (CorporatAccountInfo accountInfo:accountInfoList) {
			logger.info("对公银行卡待审核条数="+accountInfoList.size()+"银行卡卡号="+accountInfo.getAccountNumber());
			//创建订单实体
			TransOrderInfo transOrderInfo = getTransOrderInfoByAccount(accountInfo);
			String productId=Constants.FN_PRODUCT;
			ErrorResponse oneCent = paymentAccountService.withhold(transOrderInfo, productId);
			if(oneCent.isIs_success()){
			     logger.info("代付成功"+transOrderInfo.getUserId()+"对公账户代付发起成功,更新一行卡状态为审核中,卡号为"+accountInfo.getAccountNumber());
				//将银行卡状态改为审核中
				accountInfo.setStatusId(Constants.ACCOUNT_NUM_STATRS_3);
				corporatAccountInfoList.add(accountInfo);
			}else{
				logger.info("对公账户发起代付一分钱失败发送邮件,账户卡号为"+accountInfo.getAccountNumber());
				RkylinMailUtil.sendMailThread("对公账户发起代付一分钱失败",transOrderInfo.getUserId()+"对公账户代付发起失败", TransCodeConst.FINANACE_ENTRY_ERROR_TOEMAIL);
			}
		}
		if (corporatAccountInfoList.size()!=0) {
			corporatAccountInfoDao.batchUpdate(corporatAccountInfoList);
		}
	}
	
	/**
	 * 封装订单实体
	 * @param accountInfo
	 * @param finanaceAccount
	 * @return
	 */
	private TransOrderInfo getTransOrderInfoByAccount(CorporatAccountInfo accountInfo){
		TransOrderInfo transOrderInfo=new TransOrderInfo();
		transOrderInfo.setUserId(TransCodeConst.THIRDPARTYID_DGZHJYZCZH);//?
		transOrderInfo.setAmount(Long.valueOf(1));
		transOrderInfo.setFuncCode(TransCodeConst.PAYMENT_WITHHOLD);
		transOrderInfo.setBusiTypeId("1");// 标示对公账户
		transOrderInfo.setInterMerchantCode(accountInfo.getAccountNumber()); //?
		transOrderInfo.setMerchantCode(accountInfo.getRootInstCd());  //?
		transOrderInfo.setOrderAmount(Long.valueOf(1));
		DateUtil dateUtil=new DateUtil();
		transOrderInfo.setOrderDate(dateUtil.getNow());
		transOrderInfo.setOrderNo(redisIdGenerator.createRequestNo());
		transOrderInfo.setRequestTime(dateUtil.getNow());
		transOrderInfo.setStatus(TransCodeConst.TRANS_STATUS_NORMAL);
		transOrderInfo.setUserFee(0L);
		transOrderInfo.setRemark(Constants.TRANS_ORDER_REMARK);
		String productId=Constants.FN_PRODUCT;
		transOrderInfo.setErrorCode(productId);
		return transOrderInfo;
	}
	
	//定时任务结束 end
	
	 //根据代收付更新后的订单更新银行卡状态 start
	public void updateAccountoneCent(){
	List<GenerationPaymentHistory> generationPaymentHistoryList = generationPaymentHistoryManager.selectByOnecent();
	    if(null!=generationPaymentHistoryList&&generationPaymentHistoryList.size()>0){
	    	
	    	for (int i = 0; i < generationPaymentHistoryList.size(); i++) {
	    		GenerationPaymentHistory generationPaymentHistory=generationPaymentHistoryList.get(i);
	    		logger.info("查询状态为审核中的往期代收付明细返回结果----"+"卡号"+generationPaymentHistory.getAccountNo()
	    				+"-----返回结果0表示成功,其他为失败"+generationPaymentHistory.getSendType()
	    				+"-----机构号"+generationPaymentHistory.getRootInstcd());
	    		
	    		//如果往期代收付明细表,返回结果中sendType为0,表示成功,否则表示为失败
	    		if(generationPaymentHistory.getSendType().equals(Integer.valueOf(0))){
	    	    //更新状态 银行卡生效
	    			Map<String,Object> map=new HashMap<String,Object>();
	    			map.put("accountnumber", generationPaymentHistory.getAccountNo());
	    			map.put("status", Constants.ACCOUNT_NUM_STATRS_1);
	    			map.put("rootinstcd", generationPaymentHistory.getRootInstcd());
	    			 accountInfoManager.updateByOnecent(map);
	    	    }else{
	    	    	//更新状态审核失败
	    	    	Map<String,Object> map=new HashMap<String,Object>();
	    	    	map.put("accountnumber", generationPaymentHistory.getAccountNo());
	    	    	map.put("status", Constants.ACCOUNT_NUM_STATRS_4);
	    	    	map.put("rootinstcd", generationPaymentHistory.getRootInstcd());
	    			accountInfoManager.updateByOnecent(map);
	    	    }
	    	
	        }
	    }
	}
	//根据代收付更新后的订单更新银行卡状态 end
	/**
	 * 获取银行卡信息列表(增加总行logo的url地址)
	 * objOrList  1：结算卡信息  2：所有卡信息
	 */
	@Override
	public List<AccountInfoPlus> getAccountListPlus(User user, String objOrList) {
		logger.info("--------获取银行卡信息列表(增加总行logo的url地址)！--------");
		logger.info("--用户ID--:"+user.userId+"--机构号--："+user.constId+"--产品号--："+user.productId+"--用户名--"+user.userName+"第三方用户ID"+user.referUserId);
		try {
			List<FinanaceAccount> faList =this.getAllAccount(user, "oper");
			if (faList != null && faList.size() >= 0) {
				for (int j = 0; j < faList.size(); j++) {
					FinanaceAccount finanaceAccount = faList.get(j);
					if (BaseConstants.ACCOUNT_STATUS_OK.equals(finanaceAccount
							.getStatusId())) {
						AccountInfoQuery accountInfoQuery = new AccountInfoQuery();
						accountInfoQuery.setFinAccountId(finanaceAccount
								.getFinAccountId());
						accountInfoQuery.setAccountName(user.userId);
						accountInfoQuery.setStatus(Constants.ACCOUNT_NUM_STATRS_OK_ALL);
						if (Constants.ACCOUNT_PURPOSE_1.equals(objOrList)) {
							accountInfoQuery
									.setAccountPurpose(Constants.ACCOUNT_PURPOSE_1);
						}
						List<AccountInfo> accountInfoList = accountInfoManager.queryListPlus(accountInfoQuery);
						return this.changeResultType(accountInfoList);
					}
				}
			} else {
				return null;
			}
			return null;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}
	
	private List<AccountInfoPlus> changeResultType(List<AccountInfo> accountInfoList){
		List<AccountInfoPlus> accountInfoPluss = new ArrayList<AccountInfoPlus>();
		for(AccountInfo accountInfo : accountInfoList){
			AccountInfoPlus accountInfoPlus = new AccountInfoPlus();
			accountInfoPlus.setAccountId(accountInfo.getAccountId());
			accountInfoPlus.setAccountName(accountInfo.getAccountName());
			accountInfoPlus.setAccountNumber(accountInfo.getAccountNumber());
			accountInfoPlus.setAccountProperty(accountInfo.getAccountProperty());
			accountInfoPlus.setAccountPurpose(accountInfo.getAccountPurpose());
			accountInfoPlus.setAccountRealName(accountInfo.getAccountRealName());
			accountInfoPlus.setAccountTypeId(accountInfo.getAccountTypeId());
			accountInfoPlus.setBankBranch(accountInfo.getBankBranch());
			accountInfoPlus.setBankBranchName(accountInfo.getBankBranchName());
			accountInfoPlus.setBankCity(accountInfo.getBankCity());
			accountInfoPlus.setBankHead(accountInfo.getBankHead());
			accountInfoPlus.setBankHeadName(accountInfo.getBankHeadName());
			accountInfoPlus.setBankProvince(accountInfo.getBankProvince());
			accountInfoPlus.setCertificateNumber(accountInfo.getCertificateNumber());
			accountInfoPlus.setCertificateType(accountInfo.getCertificateType());
			accountInfoPlus.setCreatedTime(accountInfo.getCreatedTime());
			accountInfoPlus.setCurrency(accountInfo.getCurrency());
			accountInfoPlus.setErrorCode(accountInfo.getErrorCode());
			accountInfoPlus.setFinAccountId(accountInfo.getFinAccountId());
			accountInfoPlus.setOpenAccountDate(accountInfo.getOpenAccountDate());
			accountInfoPlus.setOpenAccountDescription(accountInfo.getOpenAccountDescription());
			accountInfoPlus.setRootInstCd(accountInfo.getRootInstCd());
			accountInfoPlus.setStatus(accountInfo.getStatus());
			accountInfoPlus.setUpdatedTime(accountInfo.getUpdatedTime());
			accountInfoPlus.setUrllogo(accountInfo.getUrllogo());
			
			accountInfoPluss.add(accountInfoPlus);
		}
		return accountInfoPluss;
	}
	
	/** 
	* @Description:查询对私卡/对公户的详细信息
	*
	* @param rootInstId 机构号
	* @param userId 用户id
	* @param accountPurpose 账户目的（结算卡）
	* @param status 状态（正常）
	* @param response   
	*/
	@Override
	public List<AccountInfo> selectAccountListForJsp(AccountInfoQuery query) {
		
		logger.info("-------------------查询对私卡/对公户的详细信息,开始，入参为：rootInstId=" + query.getRootInstCd()  + ",userId=" + query.getAccountName() +
				",productId=" + query.getProductId());
		
		List<AccountInfo> accList = new ArrayList<AccountInfo>();
		
		if(null == query.getRootInstCd() || "".equals(query.getRootInstCd().trim())){
			logger.info("机构号为空！");
			return accList;
		}
//		if(null == query.getProductId() || "".equals(query.getProductId().trim())){
//			logger.info("产品号为空！");
//			return accList;
//		}
		if(null == query.getAccountName() || "".equals(query.getAccountName().trim())){
			logger.info("用户id为空！");
			return accList;
		}
		
		accList = accountInfoDao.selectAccountListForJsp(query);
		logger.info("-------------------查询对私卡/对公户的详细信息,结束" );
		
		return accList;
	}
	
	/** 
	* @Description:根据账户表id把相应数据状态改为：4验证失败
	*
	* @param accountId 账户表id
	* @param status 状态（验证失败4）
	* @return   
	*/
	@Override
	public int updateAccountInfoStatus(AccountInfoQuery query){
		logger.info("-------------------根据账户表id把相应数据状态改为：4验证失败,开始，入参为：accountId=" + query.getAccountId() + ",status=" + query.getStatus() );
		if(null == query.getAccountId()){
			logger.info("账户表id不能为空");
			return -1; 
		}
		if(null == query.getStatus()){
			logger.info("Status不能为空");
			return -1; 
		}
		
		logger.info("-------------------根据账户表id把相应数据状态改为：4验证失败,结束" );
		
		return accountInfoDao.updateAccountInfoStatus(query);
	}
	
	/**
	 * 查询某机构是否需要去鹏元校验
	 * Discription:
	 * @param instCode
	 * @return String
	 * @author Achilles
	 * @since 2016年4月20日
	 */
	private String ifToPengYuanVerify(String instCode){
	    ParameterInfoQuery query = new ParameterInfoQuery();
	    query.setParameterCode(Constants.PENGYUAN_VERIFY_INST);
	    query.setStatus(1);
	    List<ParameterInfo> list = parameterInfoDao.selectByExample(query);
	    if (list.size()==0) {
            return CodeEnum.FAILURE.getCode();
        }
	    String[] instCodes = list.get(0).getParameterValue().split(",");
	    for (String instCodee : instCodes) {
            if (instCodee.equals(instCode)) {
                return CodeEnum.SUCCESS.getCode();
            }
        }
	    return CodeEnum.FAILURE.getCode();
	}
	
	/**
	 * 去通联校验(多渠道)
	 * Discription:
	 * @param accountInfo
	 * @return CommonResponse
	 * @author Achilles
	 * @since 2016年7月11日
	 */
	private CommonResponse toTongLianVerify(AccountInfo accountInfo,String instCode){
	    BankAccountCheckDto dto =new BankAccountCheckDto();
	    dto.setSysNo("zhxt001");// 业务系统号
	    dto.setTransCode("19120");
	    dto.setOrgNo(instCode);
	    dto.setBusiCode("10001");
	    dto.setChannelNo("110109");
	    dto.setSignType(1);
        CommonResponse res = new CommonResponse();
        res.setCode(CodeEnum.FAILURE.getCode());
        ParameterInfo parameterInfo = parameterInfoService.getParameterInfoByParaCode("GATEROUTER_MD5_KEY");
        if (parameterInfo==null) {
            res.setMsg("绑卡失败,系统错误!");
            logger.info("绑卡  参数表没有设置加密key!");
            return res; 
        }
	    dto.setSignMsg(dto.sign(parameterInfo.getParameterValue()));
	    dto.setBankCode(accountInfo.getBankHead());
	    dto.setAccountType("10");//对私
	    dto.setAccountNo(accountInfo.getAccountNumber());
	    dto.setAccountName(accountInfo.getAccountRealName());
        String certificateType = accountInfo.getCertificateType();
        if ("X".equals(accountInfo.getCertificateType())) {
            certificateType = "99";
        }
        dto.setIdType(certificateType);
        dto.setId(accountInfo.getCertificateNumber());
        logger.info("绑卡(通联) 发给多渠道的参数:SysNo="+dto.getSysNo()+",TransCode="+dto.getTransCode()+",OrgNo="+dto.getOrgNo()
        +",BusiCode="+dto.getBusiCode()+",ChannelNo="+dto.getChannelNo()+",SignType="+dto.getSignType()+",SignMsg="+dto.getSignMsg()+",BankCode="+dto.getBankCode()+
        ",AccountType="+dto.getAccountType()+",AccountNo="+dto.getAccountNo()+",AccountName="+dto.getAccountName()+
        ",IdType="+dto.getIdType()+",Id="+dto.getId());
        BankAccountCheckRespDto bankAccountCheckRespDto = authenticationService.bankAccountCheck(dto);
        logger.info("绑卡(通联) ReturnCode="+bankAccountCheckRespDto.getReturnCode()+", ReturnMsg="+bankAccountCheckRespDto.getReturnMsg()
        +",ChannelCode="+bankAccountCheckRespDto.getChannelCode()+",ChannelMsg="+bankAccountCheckRespDto.getChannelMsg()+",StatusId="+bankAccountCheckRespDto.getStatusId());
        if (!"100000".equals(bankAccountCheckRespDto.getReturnCode())) {
            res.setMsg("系统异常,请稍后再试!");
            return res;
        } 
	    if (!"16".equals(String.valueOf(bankAccountCheckRespDto.getStatusId()))) {
            res.setMsg(bankAccountCheckRespDto.getChannelMsg());
            return res;
        }
        return new CommonResponse();
	}
	
	   /**
     * 去鹏元校验
     * Discription:
     * @return CommonResponse
     * @author Achilles
     * @since 2016年4月20日
     */
    private CommonResponse toPengYuanVerify(AccountInfo accountInfo,String instCode){
        PersonBankCheckDto person =new PersonBankCheckDto();
        person.setTransCode("19112");
        person.setSysNo("zhxt001");// 业务系统号
        person.setSignType(1);
        person.setQueryName(accountInfo.getAccountRealName());
        String certificateType = accountInfo.getCertificateType();
        if ("X".equals(accountInfo.getCertificateType())) {
            certificateType = "99";
        }
        person.setIdType(Integer.parseInt(certificateType));
        person.setIdCode(accountInfo.getCertificateNumber());
        person.setOrgNo(instCode);
        person.setAccountNo(accountInfo.getAccountNumber());
        logger.info("去鹏元校验------------------------------卡号="+accountInfo.getAccountNumber());
        CommonResponse res = new CommonResponse();
        res.setCode(CodeEnum.FAILURE.getCode());
        ParameterInfo parameterInfo = parameterInfoService.getParameterInfoByParaCode("GATEROUTER_MD5_KEY");
        if (parameterInfo==null) {
            res.setMsg("绑卡失败,系统错误!");
            logger.info("绑卡  参数表没有设置加密key!");
            return res; 
        }
        person.setSignMsg(person.sign(parameterInfo.getParameterValue()));
        logger.info("绑卡(鹏元) 发给多渠道的参数:TransCode="+person.getTransCode()+",OrgNo="+person.getOrgNo()+",SysNo="+person.getSysNo()
        +",SignType="+person.getSignType()+",QueryName="+person.getQueryName()+",IdType="+person.getIdType()+",IdCode="+person.getIdCode()
        +",AccountNo="+person.getAccountNo()+",SignMsg="+person.getSignMsg());
        PersonBankCheckRespDto personBankCheckRespDto = creditService.personBankCheck(person);
        logger.info("多渠道返回码=="+personBankCheckRespDto.getReturnCode()+", 信息="+personBankCheckRespDto.getReturnMsg()+",ChannelCode="+personBankCheckRespDto.getChannelCode());
        if (!"100000".equals(personBankCheckRespDto.getReturnCode())||!"1".equals(personBankCheckRespDto.getChannelCode())) {
            res.setMsg(personBankCheckRespDto.getReturnMsg());
            return res;
        }
        logger.info("去鹏元校验personBankCheckRespDto.getCisReport().getTreatResult()="+personBankCheckRespDto.getCisReport().getTreatResult());
        if (!"1".equals(personBankCheckRespDto.getCisReport().getTreatResult())) {
            if ("0".equals(personBankCheckRespDto.getCisReport().getTreatResult())&&"3".equals(personBankCheckRespDto.getCisReport().getPersonBankCheckInfo().getTreatResult())) {
                res.setCode("2");
            }
            res.setMsg("查询失败原因id:"+personBankCheckRespDto.getCisReport().getQueryReasonID()); 
            return res;
        }
        logger.info("去鹏元校验personBankCheckRespDto.getCisReport().getPersonBankCheckInfo().getTreatResult()="+personBankCheckRespDto.getCisReport().getPersonBankCheckInfo().getTreatResult());
//        if ("3".equals(personBankCheckRespDto.getCisReport().getPersonBankCheckInfo().getTreatResult())) {
//            res.setMsg(personBankCheckRespDto.getCisReport().getPersonBankCheckInfo().getErrorMessage());
//            res.setCode("2");
//            return res;
//        }
        if (!"1".equals(personBankCheckRespDto.getCisReport().getPersonBankCheckInfo().getTreatResult())) {
            res.setMsg(personBankCheckRespDto.getCisReport().getPersonBankCheckInfo().getErrorMessage()); 
            return res;
        }
       logger.info("去鹏元校验personBankCheckRespDto.getCisReport().getPersonBankCheckInfo().getItem().getStatus()="+personBankCheckRespDto.getCisReport().getPersonBankCheckInfo().getItem().getStatus());
       if (!"1".equals(personBankCheckRespDto.getCisReport().getPersonBankCheckInfo().getItem().getStatus())) {
           res.setMsg(personBankCheckRespDto.getCisReport().getPersonBankCheckInfo().getItem().getMessage());
           return res;
       }
        return new CommonResponse();
    }
    
    /**
     * 开户
     * Discription:
     * @param user
     * @return String
     * @author Achilles
     * @since 2016年5月2日
     */
    @Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
    public String openSubAccount(User user) {
        logger.info("------开通账户！---------");
        logger.info("--用户ID--:"+user.userId+"--机构号--："+user.constId+"--产品号--："+user.productId+"--用户名--"+user.userName+"第三方用户ID"+user.referUserId);
        List<FinanaceAccount> list=new ArrayList<FinanaceAccount>();
        FinanaceAccountQuery query = new FinanaceAccountQuery();
        query.setRootInstCd(user.constId);
        query.setAccountRelateId(user.userId);
        query.setFinAccountTypeId(AccountConstants.ACCOUNT_TYPE_BASE);
        list = finanaceAccountManager.queryList(query);
        if(user.referUserId!=null && !"".equals(user.referUserId)){
            if (list == null || list.size() <= 0) {
                logger.info("主账户暂不存在，请先创建主账户！");
                return "主账户暂不存在，请先创建主账户！";
            }
            if(!BaseConstants.ACCOUNT_STATUS_OK.equals(list.get(0).getStatusId())){
                logger.info("主账户状态非正常，请先将主账户置为有效状态！");
                return "主账户状态非正常，请先将主账户置为有效状态！";
            }
        }
        String accountCode = "";
        if(list!=null && list.size()>0){
            for (int i = 0; i < list.size(); i++) {
                    if (user.referUserId != null && !"".equals(user.referUserId)) {
                        accountCode = list.get(i).getAccountCode();
                        query.setRootInstCd(user.constId);
                        query.setAccountRelateId(user.userId);
                        query.setGroupManage(user.productId);
                        query.setReferUserId(user.referUserId);
                        query.setFinAccountTypeId(AccountConstants.ACCOUNT_TYPE_CHILD);
                        List<FinanaceAccount> flist = finanaceAccountManager.queryList(query);
                        if(flist!=null && flist.size()>0){
                            return "ok";
                        }
                        break;
                    } else {
                        logger.info("该用户已经开通主账户！");
                        return "该用户已经开通主账户！";
                    }
//              }
            }
        }
        FinanaceAccount finanaceAccount = new FinanaceAccount();
        finanaceAccount.setFinAccountId(redisIdGenerator.accountEntryNo());
        finanaceAccount.setRootInstCd(user.constId);
        finanaceAccount.setGroupManage(user.productId);
        finanaceAccount.setFinAccountName(user.userName);
        finanaceAccount.setAccountRelateId(user.userId);
        finanaceAccount.setStatusId(BaseConstants.ACCOUNT_STATUS_OK);
        finanaceAccount.setAmount(Long.parseLong(BaseConstants.INITIAL_AMOUNT));
        finanaceAccount.setBalanceSettle(Long
                .parseLong(BaseConstants.INITIAL_AMOUNT));
        finanaceAccount.setBalanceFrozon(Long
                .parseLong(BaseConstants.INITIAL_AMOUNT));
        finanaceAccount.setBalanceOverLimit(Long
                .parseLong(BaseConstants.INITIAL_AMOUNT));
        if (user.referUserId != null && !"".equals(user.referUserId)) {
            finanaceAccount.setReferUserId(user.referUserId);
            finanaceAccount
            .setFinAccountTypeId(AccountConstants.ACCOUNT_TYPE_CHILD);
            finanaceAccount.setAccountCode(accountCode);
        }else{
            finanaceAccount
            .setFinAccountTypeId(AccountConstants.ACCOUNT_TYPE_BASE);
            finanaceAccount
            .setAccountCode(PartyCodeUtil.getPartyCodeByRule());//生成方式
        }
        finanaceAccount.setGroupSettle(AccountConstants.GROUP_SETTLE_BASE);
        finanaceAccount.setCurrency(BaseConstants.CURRENCY_CNY);
        finanaceAccount.setStartTime(DateUtils
                .getSysDate(Constants.DATE_FORMAT_YYYYMMDD));
        finanaceAccount.setBussControl(BaseConstants.BUSS_CONTRAL_TOTAL);
        finanaceAccount.setRemark("");
        finanaceAccount.setRecordMap(user.creditType);
        finanaceAccountManager.saveFinanaceAccount(finanaceAccount);
        String ArraysAll[][] = AccountConstants.ORGANIZATION_CONSTIDS_PRODEUCTS;
        String ArraysConstId[] = ArraysAll[0];
        if (Arrays.asList(ArraysConstId).contains(user.constId)
                && AccountConstants.ACCOUNT_TYPE_BASE.equals(finanaceAccount.getFinAccountTypeId())){
            logger.info("校验机构结果：此机构需要开通子账户");
            int productNum = Arrays.binarySearch(ArraysConstId, user.constId);
            String redCode = "";
            List<FinanaceAccount> redList = new ArrayList<FinanaceAccount>();
            FinanaceAccountQuery redQuery = new FinanaceAccountQuery();
            redQuery.setRootInstCd(user.constId);
            redQuery.setAccountRelateId(user.userId);
            redQuery.setStatusId(BaseConstants.ACCOUNT_STATUS_OK);
            redQuery.setFinAccountTypeId(AccountConstants.ACCOUNT_TYPE_BASE);
            redList = finanaceAccountManager.queryList(redQuery);
            int loopNumber = ArraysAll[productNum + 1].length;
            if (redList != null && redList.size() > 0) {
                for  (int j = 0; j < loopNumber; j++){
                    redCode = redList.get(0).getAccountCode();
                    FinanaceAccount finanaceAccountChild = new FinanaceAccount();
                    finanaceAccountChild.setFinAccountId(redisIdGenerator
                            .accountEntryNo());
                    finanaceAccountChild.setRootInstCd(user.constId);
                    finanaceAccountChild.setGroupManage(ArraysAll[productNum+1][j]);
                    finanaceAccountChild.setFinAccountName(user.userName);
                    finanaceAccountChild.setAccountRelateId(user.userId);
                    finanaceAccountChild
                            .setStatusId(BaseConstants.ACCOUNT_STATUS_OK);
                    finanaceAccountChild.setAmount(Long
                            .parseLong(BaseConstants.INITIAL_AMOUNT));
                    finanaceAccountChild.setBalanceSettle(Long
                            .parseLong(BaseConstants.INITIAL_AMOUNT));
                    finanaceAccountChild.setBalanceFrozon(Long
                            .parseLong(BaseConstants.INITIAL_AMOUNT));
                    finanaceAccountChild.setBalanceOverLimit(Long
                            .parseLong(BaseConstants.INITIAL_AMOUNT));
                    // finanaceAccountChild.setReferUserId("");
                    finanaceAccountChild
                            .setFinAccountTypeId(AccountConstants.ACCOUNT_TYPE_CHILD);
                    finanaceAccountChild.setAccountCode(redCode);
                    finanaceAccountChild
                            .setGroupSettle(AccountConstants.GROUP_SETTLE_BASE);
                    finanaceAccountChild.setCurrency(BaseConstants.CURRENCY_CNY);
                    finanaceAccountChild.setStartTime(DateUtils
                            .getSysDate(Constants.DATE_FORMAT_YYYYMMDD));
                    finanaceAccountChild
                            .setBussControl(BaseConstants.BUSS_CONTRAL_TOTAL);
                    finanaceAccountChild.setRemark("");
                    finanaceAccountChild.setRecordMap("");
                    finanaceAccountManager
                            .saveFinanaceAccount(finanaceAccountChild);
                }
            }
        }
        return "ok";
    }
}