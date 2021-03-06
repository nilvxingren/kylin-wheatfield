package com.rkylin.wheatfield.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.rkylin.utils.CheckUtil;
import com.rkylin.utils.RkylinBeanUtil;
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.domain.M000003OpenPersonAccountBean;
import com.rkylin.wheatfield.manager.FinanacePersonManager;
import com.rkylin.wheatfield.pojo.*;
import com.rkylin.wheatfield.response.AccountCompanyInfoRespon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rkylin.common.RedisIdGenerator;
import com.rkylin.wheatfield.common.ValHasNoParam;
import com.rkylin.wheatfield.constant.AccountConstants;
import com.rkylin.wheatfield.constant.BaseConstants;
import com.rkylin.wheatfield.domain.M000003OpenEntityAccountBean;
import com.rkylin.wheatfield.exception.AccountException;
import com.rkylin.wheatfield.manager.AccountInfoManager;
import com.rkylin.wheatfield.manager.FinanaceAccountManager;
import com.rkylin.wheatfield.manager.FinanaceCompanyManager;
import com.rkylin.wheatfield.response.ErrorResponse;
import com.rkylin.wheatfield.response.OpenEntityAccountResponse;
import com.rkylin.wheatfield.response.Response;
import com.rkylin.wheatfield.service.AccountManageService;
import com.rkylin.wheatfield.service.IAPIService;
import com.rkylin.wheatfield.service.IErrorResponseService;
import com.rkylin.wheatfield.service.OpenEntityAccount;
import com.rkylin.wheatfield.utils.BeanUtil;
import com.rkylin.wheatfield.utils.DateUtil;
@Transactional
@Service("openEntityAccountService")
public class OpenEntityAccountImpl implements OpenEntityAccount,IAPIService{

	private static Logger logger = LoggerFactory.getLogger(OpenEntityAccountImpl.class);
	@Autowired
	@Qualifier("finanaceCompanyManager")
	private FinanaceCompanyManager finanaceCompanyManager;
	@Autowired
	@Qualifier("finanaceAccountManager")
	private FinanaceAccountManager finanaceAccountManager;
	@Autowired
	@Qualifier("accountInfoManager")
	private AccountInfoManager accountInfoManager;
	@Autowired
	RedisIdGenerator redisIdGenerator;
	@Autowired
	@Qualifier("accountManageService")
	AccountManageService accountManageService;
	@Autowired
	IErrorResponseService errorResponseService;
	@Autowired
	@Qualifier("finanacePersonManager")
	FinanacePersonManager finanacePersonManager;
	
	@Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
	public String saveMerchantAccount(M000003OpenEntityAccountBean openBean) {
		logger.info("企业开户————————————START————————————");
		FinanaceCompanyQuery buslinceQuery = new FinanaceCompanyQuery();
		buslinceQuery.setBuslince(openBean.getBusLince());
		buslinceQuery.setStatusId(AccountConstants.COMPANY_STATUS_ON);
		buslinceQuery.setRootInstCd(openBean.getConstId());
		List<FinanaceCompany> buslinceList = finanaceCompanyManager.queryListT(buslinceQuery);
		if (buslinceList != null && buslinceList.size() > 0){
			logger.info("此营业执照已经被注册过！");
			return ("此营业执照已经被注册过！");
		}
		User user = new User();
		user.constId = openBean.getConstId();
		user.userName = openBean.getUserName();
		user.productId = openBean.getProductId();
		user.userId = openBean.getUserId();
		String ret = accountManageService.openAccount(user, new FinanaceEntry());
		logger.info("-----------------资金账户表开户返回:" + ret + "-----------------");
		if(ret!="ok")
			return ret;
		FinanaceCompanyQuery query = new FinanaceCompanyQuery();
		query.setFinAccountId(getFinanaceAccountId(user));
		List<FinanaceCompany> finanaceCompanies = finanaceCompanyManager.queryList(query);
		if (!CheckUtil.checkNullOrEmpty(finanaceCompanies)){
			logger.info("账户ID异常，请检查");
			return "账户ID异常，请检查";
		}
		FinanaceCompany finanaceCompany = new FinanaceCompany();
		finanaceCompany.setAcuntOpnLince(openBean.getAcuntOpnLince());
		finanaceCompany.setAddress(openBean.getAddress());
		finanaceCompany.setBuslince(openBean.getBusLince());
		finanaceCompany.setBusPlaceCtf(openBean.getBusPlaceCtf());
		finanaceCompany.setCompanyCode(openBean.getCompanyCode());
		finanaceCompany.setCompanyName(openBean.getCompanyName());
		finanaceCompany.setCompanyShortName(openBean.getShortName());
		finanaceCompany.setConnect(openBean.getConnect());
		finanaceCompany.setCorporateIdentity(openBean.getCorporateIdentity());
		finanaceCompany.setCorporateName(openBean.getCorporateName());
		finanaceCompany.setFinAccountId(getFinanaceAccountId(user));
		finanaceCompany.setLoanCard(openBean.getLoanCard());
		finanaceCompany.setMcc(openBean.getMcc());
		finanaceCompany.setOrganCertificate(openBean.getOrganCertificate());
		finanaceCompany.setPost(openBean.getPost());
		finanaceCompany.setRemark(openBean.getRemark());
		finanaceCompany.setStatusId(AccountConstants.COMPANY_STATUS_ON);
		finanaceCompany.setTaxregCard1(openBean.getTaxRegCardf());
		finanaceCompany.setTaxregCard2(openBean.getTaxRegCards());
		finanaceCompany.setCompanyType(Integer.valueOf(Constants.BASE_FINANACECOMPANY_TYPE));
		finanaceCompany = BeanUtil.beanDelNullAndEmpty(finanaceCompany);
		finanaceCompanyManager.saveFinanaceCompany(finanaceCompany);
		logger.info("企业开户————————————END————————————");
		return "ok";
	}

	private String getFinanaceAccountId(User user) {
		List<FinanaceAccount> list=new ArrayList<FinanaceAccount>();
		FinanaceAccountQuery query = new FinanaceAccountQuery();
		query.setRootInstCd(user.constId);
		query.setAccountRelateId(user.userId);
		query.setStatusId(BaseConstants.ACCOUNT_STATUS_OK);
		query.setFinAccountTypeId(AccountConstants.ACCOUNT_TYPE_BASE);
		list = finanaceAccountManager.queryList(query);
		return list.get(0).getFinAccountId();
	}

	@Override
    @Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
	public String updateMerchantAccount(M000003OpenEntityAccountBean openBean) {
		logger.info("更新企业开户信息————————————START————————————");
		User user = new User();
		user.constId = openBean.getConstId();
		user.userName = openBean.getUserName();
		user.productId = openBean.getProductId();
		user.userId = openBean.getUserId();
		FinanaceAccountQuery query = new FinanaceAccountQuery();
		query.setRootInstCd(user.constId);
		query.setAccountRelateId(user.userId);
		query.setStatusId(BaseConstants.ACCOUNT_STATUS_OK);
		query.setFinAccountTypeId(AccountConstants.ACCOUNT_TYPE_BASE);
		List<FinanaceAccount> list = finanaceAccountManager.queryList(query);
		if(!CheckUtil.checkNullOrEmpty(list)){
			if(list.size()>1){
				logger.info("查到结果不唯一,无法更新");
				return "查到结果不唯一,无法更新";
			}
			FinanaceAccount finanaceAccount = list.get(0);
			FinanaceCompanyQuery companyQuery = new FinanaceCompanyQuery();
			companyQuery.setFinAccountId(finanaceAccount.getFinAccountId());
			List<FinanaceCompany> finanaceCompanyList = finanaceCompanyManager.queryList(companyQuery);
			FinanaceCompany finanaceCompany = finanaceCompanyList.get(0);
			if (!CheckUtil.checkNullOrEmpty(openBean.getAcuntOpnLince()))
				finanaceCompany.setAcuntOpnLince(openBean.getAcuntOpnLince());
			if (!CheckUtil.checkNullOrEmpty(openBean.getAddress()))
				finanaceCompany.setAddress(openBean.getAddress());
			if (!CheckUtil.checkNullOrEmpty(openBean.getBusLince())){
				FinanaceCompanyQuery buslinceQuery = new FinanaceCompanyQuery();
				buslinceQuery.setBuslince(openBean.getBusLince());
				buslinceQuery.setStatusId(AccountConstants.COMPANY_STATUS_ON);
				buslinceQuery.setRootInstCd(openBean.getConstId());
				List<FinanaceCompany> buslinceList = finanaceCompanyManager.queryListT(buslinceQuery);
				if (buslinceList != null && buslinceList.size() > 0){
					if (buslinceList.get(0).getBuslince().equals(finanaceCompany.getBuslince())){
						finanaceCompany.setBuslince(openBean.getBusLince());
					} else{
						logger.info("此营业执照已经被注册过！");
						return ("此营业执照已经被注册过！");
					}
				}else {
					finanaceCompany.setBuslince(openBean.getBusLince());
				}
			}	
			if (!CheckUtil.checkNullOrEmpty(openBean.getBusPlaceCtf()))
				finanaceCompany.setBusPlaceCtf(openBean.getBusPlaceCtf());
			if (!CheckUtil.checkNullOrEmpty(openBean.getCompanyCode()))
				finanaceCompany.setCompanyCode(openBean.getCompanyCode());
			if (!CheckUtil.checkNullOrEmpty(openBean.getCompanyName()))
				finanaceCompany.setCompanyName(openBean.getCompanyName());
			if (!CheckUtil.checkNullOrEmpty(openBean.getShortName()))
				finanaceCompany.setCompanyShortName(openBean.getShortName());
			if (!CheckUtil.checkNullOrEmpty(openBean.getConnect()))
				finanaceCompany.setConnect(openBean.getConnect());
			if (!CheckUtil.checkNullOrEmpty(openBean.getCorporateIdentity()))
				finanaceCompany.setCorporateIdentity(openBean
						.getCorporateIdentity());
			if (!CheckUtil.checkNullOrEmpty(openBean.getCorporateName()))
				finanaceCompany.setCorporateName(openBean.getCorporateName());
			finanaceCompany.setFinAccountId(finanaceAccount.getFinAccountId());
			if (!CheckUtil.checkNullOrEmpty(openBean.getLoanCard()))
				finanaceCompany.setLoanCard(openBean.getLoanCard());
			if (!CheckUtil.checkNullOrEmpty(openBean.getMcc()))
				finanaceCompany.setMcc(openBean.getMcc());
			if (!CheckUtil.checkNullOrEmpty(openBean.getOrganCertificate()))
				finanaceCompany.setOrganCertificate(openBean
						.getOrganCertificate());
			if (!CheckUtil.checkNullOrEmpty(openBean.getPost()))
				finanaceCompany.setPost(openBean.getPost());
			if (!CheckUtil.checkNullOrEmpty(openBean.getRemark()))
				finanaceCompany.setRemark(openBean.getRemark());
			if (!CheckUtil.checkNullOrEmpty(openBean.getTaxRegCardf()))
				finanaceCompany.setTaxregCard1(openBean.getTaxRegCardf());
			if (!CheckUtil.checkNullOrEmpty(openBean.getTaxRegCards()))
				finanaceCompany.setTaxregCard2(openBean.getTaxRegCards());
			finanaceCompany.setUpdatedTime(null);
			finanaceCompanyManager
					.updateFinanaceCompanyByFinanaceAccountId(finanaceCompany);
			logger.info("更新企业开户信息————————————END————————————");
			return "ok";
		}
		return "未找到该账户";
	}

	@Override
	public Response queryMerchantAccount(
			M000003OpenEntityAccountBean openBean) {
		AccountCompanyInfoRespon respon = new AccountCompanyInfoRespon();
		FinanaceAccountQuery finanaceAccountQuery = new FinanaceAccountQuery();
		finanaceAccountQuery.setRootInstCd(openBean.getConstId());
		finanaceAccountQuery.setGroupManage(openBean.getProductId());
		finanaceAccountQuery.setAccountRelateId(openBean.getUserId());
		finanaceAccountQuery.setFinAccountTypeId(AccountConstants.ACCOUNT_TYPE_BASE);
		List<FinanaceAccount> finanaceAccounts = finanaceAccountManager.queryList(finanaceAccountQuery);
		if(CheckUtil.checkNullOrEmpty(finanaceAccounts)) {
			return errorResponseService.getErrorResponse("M1","未找到该商户信息");
		}
		String finAccountId = finanaceAccounts.get(0).getFinAccountId();
		FinanaceCompanyQuery companyQuery = new FinanaceCompanyQuery();
		companyQuery.setFinAccountId(finAccountId);
		List<FinanaceCompany> finanaceCompanies = finanaceCompanyManager.queryList(companyQuery);
		finanaceCompanies = RkylinBeanUtil.emptySomeField(finanaceCompanies,new String[]{"companyId","companyCode","companyType","finAccountId","createdTime","updatedTime"});
		respon.setAccountCompanyInfos(finanaceCompanies);
		respon.setMsg("true");
		return respon;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public String savePersonalAccount(M000003OpenPersonAccountBean openBean) {
		logger.info("个人开户————————————START————————————");
		FinanacePersonQuery certificateQuery = new FinanacePersonQuery();
		certificateQuery.setCertificateNumber(openBean.getCertificateNumber());
		certificateQuery.setCertificateType(openBean.getCertificateType());
		certificateQuery.setStatusId(AccountConstants.PERSON_STATUS_ON);
		certificateQuery.setRootInstCd(openBean.getConstId());
		List<FinanacePerson> certificateList = finanacePersonManager.queryListT(certificateQuery);
		if (certificateList != null && certificateList.size() > 0){
			logger.info("此证件已经被注册过！");
			return ("此证件已经被注册过！");
		}
		User user = new User();
		user.constId = openBean.getConstId();
		user.referUserId = openBean.getReferuserId();
		user.userName = openBean.getUsername();
		user.productId = openBean.getProductId();
		user.userId = openBean.getUserId();
		String ret = accountManageService.openAccount(user, new FinanaceEntry());
		logger.info("-----------------资金账户表开户返回:"+ret+"-----------------");
		if(ret!="ok")
			return ret;
		String personCode = redisIdGenerator.createPersonCode();
		String personType = openBean.getPersonType();
		if (CheckUtil.checkNullOrEmpty(openBean.getPersonType())){
			personType = AccountConstants.PERSON_TYPE_PER;
		}
		FinanacePerson person = new FinanacePerson();
		person.setPersonCode(personCode);
		person.setFinAccountId(getFinanaceAccountId(user));
		person.setPersonChnName(openBean.getPersonChnName());
		person.setPersonEngName(openBean.getPersonEngName());
		person.setPersonType(Integer.parseInt(personType));
		person.setStatusId(AccountConstants.PERSON_STATUS_ON);
		person.setAddress(openBean.getAddress());
		person.setBirthday(openBean.getBirthday());
		person.setCertificateNumber(openBean.getCertificateNumber());
		person.setCertificateType(openBean.getCertificateType());
		person.setEmail(openBean.getEmail());
		person.setFixTel(openBean.getFixTel());
		person.setMobileTel(openBean.getMobileTel());
		person.setPersonSex(openBean.getPersonSex());
		person.setPost(openBean.getPost());
		person.setRemark(openBean.getRemark());
		finanacePersonManager.saveFinanacePerson(person);
		logger.info("个人开户————————————END————————————");
		return "ok";
	}

	@Override
	public String updatePersonalAccount(M000003OpenPersonAccountBean openBean) {
		logger.info("更新个人开户信息————————————START————————————");
		User user = new User();
		user.constId = openBean.getConstId();
		user.userName = openBean.getUsername();
		user.productId = openBean.getProductId();
		user.userId = openBean.getUserId();
		FinanaceAccountQuery query = new FinanaceAccountQuery();
		query.setRootInstCd(user.constId);
		query.setAccountRelateId(user.userId);
		query.setStatusId(BaseConstants.ACCOUNT_STATUS_OK);
		query.setFinAccountTypeId(AccountConstants.ACCOUNT_TYPE_BASE);
		List<FinanaceAccount> list = finanaceAccountManager.queryList(query);
		if (CheckUtil.checkNullOrEmpty(list)){
			logger.info("账户不存在或者已经失效");
			return "账户不存在或者已经失效";
		}
		FinanacePersonQuery finanacePersonQuery = new FinanacePersonQuery();
		finanacePersonQuery.setFinAccountId(list.get(0).getFinAccountId());
		List<FinanacePerson> finanPersonList = finanacePersonManager.queryList(finanacePersonQuery);
		if (CheckUtil.checkNullOrEmpty(finanPersonList)){
			logger.info("个人信息不存在");
			return "个人信息不存在";
		}
		FinanacePerson finanacePerson = finanPersonList.get(0);
		//如果证件类型要修改，证件号码不能为空
		if (!CheckUtil.checkNullOrEmpty(openBean.getCertificateType())){
			if (!finanacePerson.getCertificateType().equals(openBean.getCertificateType())
					&& CheckUtil.checkNullOrEmpty(openBean.getCertificateNumber())){
				logger.info("请输入新的证件号码");
				return "请输入新的证件号码";
			}
		}
		if (!CheckUtil.checkNullOrEmpty(openBean.getPersonChnName()))
			finanacePerson.setPersonChnName(openBean.getPersonChnName());
		if (!CheckUtil.checkNullOrEmpty(openBean.getPersonEngName()))
			finanacePerson.setPersonEngName(openBean.getPersonEngName());
		if (!CheckUtil.checkNullOrEmpty(openBean.getPersonType()))
			finanacePerson.setPersonType(Integer.parseInt(openBean.getPersonType()));
		if (!CheckUtil.checkNullOrEmpty(openBean.getPersonSex()))
			finanacePerson.setPersonSex(openBean.getPersonSex());
		if (!CheckUtil.checkNullOrEmpty(openBean.getBirthday()))
			finanacePerson.setBirthday(openBean.getBirthday());
		if (!CheckUtil.checkNullOrEmpty(openBean.getCertificateNumber())){
			FinanacePersonQuery hasNumberQuery = new FinanacePersonQuery();
			if (!CheckUtil.checkNullOrEmpty(openBean.getCertificateType())){
				hasNumberQuery.setCertificateType(openBean.getCertificateType());
			} else {
				hasNumberQuery.setCertificateType(finanacePerson.getCertificateType());
			}
			hasNumberQuery.setCertificateNumber(openBean.getCertificateNumber());
			hasNumberQuery.setStatusId(AccountConstants.PERSON_STATUS_ON);
			hasNumberQuery.setRootInstCd(openBean.getConstId());
			List <FinanacePerson> certificateList = finanacePersonManager.queryListT(hasNumberQuery);
			if (certificateList != null && certificateList.size() > 0){
				if (certificateList.get(0).getCertificateNumber().equals(finanacePerson.getCertificateNumber())){
					logger.info("此证件号跟旧证件号相同");
					return "此证件号跟旧证件号相同";
				}
				logger.info("此证件已经被注册过");
				return "此证件已经被注册过";
			} else {
				finanacePerson.setCertificateNumber(openBean.getCertificateNumber());
				if (!CheckUtil.checkNullOrEmpty(openBean.getCertificateType()))
					finanacePerson.setCertificateType(openBean.getCertificateType());
			}
		}
		if (!CheckUtil.checkNullOrEmpty(openBean.getMobileTel()))
			finanacePerson.setMobileTel(openBean.getMobileTel());
		if (!CheckUtil.checkNullOrEmpty(openBean.getFixTel()))
			finanacePerson.setFixTel(openBean.getFixTel());
		if (!CheckUtil.checkNullOrEmpty(openBean.getEmail()))
			finanacePerson.setEmail(openBean.getEmail());
		if (!CheckUtil.checkNullOrEmpty(openBean.getPost()))
			finanacePerson.setPost(openBean.getPost());
		if (!CheckUtil.checkNullOrEmpty(openBean.getAddress()))
			finanacePerson.setAddress(openBean.getAddress());
		if (!CheckUtil.checkNullOrEmpty(openBean.getRemark()))
			finanacePerson.setRemark(openBean.getRemark());
		finanacePerson.setFinAccountId(list.get(0).getFinAccountId());
		finanacePerson.setUpdatedTime(null);
		finanacePersonManager
		.updateFinanacePersonByFinanaceAccountId(finanacePerson);
		logger.info("更新个人开户信息————————————END————————————");
		return "ok";
	}

	@Override
	public Response queryPersonalAccount(M000003OpenPersonAccountBean openBean) {
		OpenEntityAccountResponse response = new OpenEntityAccountResponse();
		FinanaceAccountQuery finanaceAccountQuery = new FinanaceAccountQuery();
		finanaceAccountQuery.setRootInstCd(openBean.getConstId());
		finanaceAccountQuery.setAccountRelateId(openBean.getUserId());
		finanaceAccountQuery.setGroupManage(openBean.getProductId());
		finanaceAccountQuery.setFinAccountTypeId(AccountConstants.ACCOUNT_TYPE_BASE);
		List<FinanaceAccount> finAccountIdList = finanaceAccountManager.queryList(finanaceAccountQuery);
		if(CheckUtil.checkNullOrEmpty(finAccountIdList)) {
			return errorResponseService.getErrorResponse("N2","未找到该个人信息或者个人信息已经失效");
		}
		String finAccountId = finAccountIdList.get(0).getFinAccountId();
		FinanacePersonQuery finanacePersonQuery = new FinanacePersonQuery();
		finanacePersonQuery.setFinAccountId(finAccountId);
		List<FinanacePerson> finanacePersons = finanacePersonManager.queryList(finanacePersonQuery);
		finanacePersons = RkylinBeanUtil.emptySomeField(finanacePersons,new String[]{"personId","personCode","finAccountId","personType","createdTime","updatedTime"});
		response.setFinanacepersons(finanacePersons);
		response.setMsg("true");
		return response;
	}

	@Override
	public List<FinanacePerson> batchQueryPersonalAccount(M000003OpenPersonAccountBean openBean) {
		logger.info("批查个人开户信息————————————START————————————");
		try {
		    DateUtil dateUtil=new DateUtil();
			FinanacePersonQuery batchQuery = new FinanacePersonQuery();
			batchQuery.setPersonChnName(openBean.getPersonChnName());
			batchQuery.setPersonEngName(openBean.getPersonEngName());
			if (openBean.getPersonType()!=null) batchQuery.setPersonType(Integer.parseInt(openBean.getPersonType()));
			batchQuery.setPersonSex(openBean.getPersonSex());
			batchQuery.setBirthdayFrom(openBean.getBirthdayFrom());
			batchQuery.setBirthdayTo(openBean.getBirthdayTo());
			batchQuery.setCertificateType(openBean.getCertificateType());
			batchQuery.setCertificateNumber(openBean.getCertificateNumber());
			batchQuery.setMobileTel(openBean.getMobileTel());
			batchQuery.setFixTel(openBean.getFixTel());
			batchQuery.setEmail(openBean.getEmail());
			batchQuery.setPost(openBean.getPost());
			batchQuery.setAddress(openBean.getAddress());
			batchQuery.setRemark(openBean.getRemark());
			if (openBean.getCreatedTimeFrom()!=null) batchQuery.setCreatedTimeFrom(dateUtil.parse(openBean.getCreatedTimeFrom(), Constants.DATE_FORMAT_YYYYMMDDHHMMSS));
			if (openBean.getCreatedTimeTo()!=null) batchQuery.setCreatedTimeTo(dateUtil.parse(openBean.getCreatedTimeTo(), Constants.DATE_FORMAT_YYYYMMDDHHMMSS));
			batchQuery.setStatusId(openBean.getStatusId());
			batchQuery.setUserId(openBean.getUserId());
			batchQuery.setRootInstCd(openBean.getConstId());
			List<FinanacePerson> finanacePersonList = finanacePersonManager.queryListBatch(batchQuery);
			logger.info("批查个人开户信息————————————END————————————");
			return finanacePersonList;
		} catch (Exception e) {
			logger.error("查询个人信息失败"+e.getMessage());
			throw new AccountException("查询个人信息失败");
		}
	}

	@Override
	public Response doJob(Map<String, String[]> paramMap, String methodName) {
		ErrorResponse errorResponse=new ErrorResponse();
		if ("ruixue.wheatfield.enterprise.entityaccountopt".equals(methodName)) {
			if (!ValHasNoParam.hasParam(paramMap, "userid")) {
				return errorResponseService.getErrorResponse("P1", "userid不能为空");
			}
			if (!ValHasNoParam.hasParam(paramMap, "constid")) {
				return errorResponseService.getErrorResponse("P2", "constid不能为空");
			}
			if (!ValHasNoParam.hasParam(paramMap, "productid")) {
				return errorResponseService.getErrorResponse("P3", "productid不能为空");
			}
			if (!ValHasNoParam.hasParam(paramMap, "companyname")) {
				return errorResponseService.getErrorResponse("P4", "企业名称不能为空");
			}
//			if (!ValHasNoParam.hasParam(paramMap, "buslince")) {
//				return errorResponseService.getErrorResponse("P5", "营业执照不能为空");
//			}
			if (!ValHasNoParam.hasParam(paramMap, "usertype")) {
				return errorResponseService.getErrorResponse("P6", "用户类型不能为空");
			}
			if (!ValHasNoParam.hasParam(paramMap, "username")) {
				return errorResponseService.getErrorResponse("P7", "用户名称及接入机构的用户名不能为空");
			}
			M000003OpenEntityAccountBean openBean = (M000003OpenEntityAccountBean) BeanUtil.maptobean(M000003OpenEntityAccountBean.class, paramMap);
			String msg = saveMerchantAccount(openBean);
			OpenEntityAccountResponse response = new OpenEntityAccountResponse();
			response.setCallResult(true);
			if ("ok".equals(msg)) {
				response.setIs_success(true);
				return response;
			} else {
				return errorResponseService.getErrorResponse("P8", msg);
			}
		}else if("ruixue.wheatfield.enterprise.updatecompanyinfo".equals(methodName)){
			if (!ValHasNoParam.hasParam(paramMap, "userid")) {
				return errorResponseService.getErrorResponse("P1", "userid不能为空");
			}
			if (!ValHasNoParam.hasParam(paramMap, "constid")) {
				return errorResponseService.getErrorResponse("P2", "constid不能为空");
			}
			if (!ValHasNoParam.hasParam(paramMap, "productid")) {
				return errorResponseService.getErrorResponse("P3", "productid不能为空");
			}
			if (!ValHasNoParam.hasParam(paramMap, "companyname")) {
				return errorResponseService.getErrorResponse("P4", "企业名称不能为空");
			}
			if (!ValHasNoParam.hasParam(paramMap, "usertype")) {
				return errorResponseService.getErrorResponse("P6", "用户类型不能为空");
			}
			if (!ValHasNoParam.hasParam(paramMap, "username")) {
				return errorResponseService.getErrorResponse("P7", "接入机构的用户名不能为空");
			}
			String[] strs = paramMap.get("opertype");
			char[] opertypes = strs[0].toCharArray();
			if(!CheckUtil.checkNullOrEmpty(strs)){
				if (opertypes.length!=1) {
					return errorResponseService.getErrorResponse("P9", "opertype参数有误");
				}
			}else{
				return errorResponseService.getErrorResponse("P9", "opertype参数不能为空");
			}
			Character opertype = opertypes[0];
			M000003OpenEntityAccountBean openBean = (M000003OpenEntityAccountBean) BeanUtil.maptobean(M000003OpenEntityAccountBean.class, paramMap);
			String msg;
			switch (opertype){
				case '1':{msg = updateMerchantAccount(openBean);break;}
//				case '2':{return queryMerchantAccount(openBean); }
				default:{msg = "未找到opertype类型";break;}
			}
			OpenEntityAccountResponse response = new OpenEntityAccountResponse();
			response.setCallResult(true);
			if ("ok".equals(msg)) {
				response.setIs_success(true);
				return response;
			} else {
				return errorResponseService.getErrorResponse("P10", msg);
			}
		}else if ("ruixue.wheatfield.person.accountopr".equals(methodName)){
			if (!ValHasNoParam.hasParam(paramMap, "userid")) {
				return errorResponseService.getErrorResponse("P1", "userid不能为空");
			}
			if (!ValHasNoParam.hasParam(paramMap, "constid")) {
				return errorResponseService.getErrorResponse("P2", "constid不能为空");
			}
			if (!ValHasNoParam.hasParam(paramMap, "productid")) {
				return errorResponseService.getErrorResponse("P3", "productid不能为空");
			}
			//个人账户操作 根据opertype识别请求的逻辑
			String[] strs = paramMap.get("opertype");
			char[] opertypes = strs[0].toCharArray();
			if(!CheckUtil.checkNullOrEmpty(strs)){
				if (opertypes.length!=1) {
					return errorResponseService.getErrorResponse("P9", "opertype参数有误");
				}
			}else{
				return errorResponseService.getErrorResponse("P9", "opertype参数不能为空");
			}
			Character opertype = opertypes[0];

			M000003OpenPersonAccountBean openBean = (M000003OpenPersonAccountBean) BeanUtil.maptobean(M000003OpenPersonAccountBean.class,paramMap);
			String msg;
			switch (opertype){
				case '1':{
					if (!ValHasNoParam.hasParam(paramMap, "personchnname")) {
						return errorResponseService.getErrorResponse("P11", "中文姓名不能为空");
					}
//					if (!ValHasNoParam.hasParam(paramMap, "certificatetype")) {
//						return errorResponseService.getErrorResponse("P12", "证件类型不能为空");
//					}
//					if (!ValHasNoParam.hasParam(paramMap, "certificatenumber")) {
//						return errorResponseService.getErrorResponse("P13", "证件号不能为空");
//					}
					msg = savePersonalAccount(openBean);
					break;
				}
				case '2':{
					msg = updatePersonalAccount(openBean);
					break;
					}
				default:{msg = "未找到opertype类型";break;}
			}
			OpenEntityAccountResponse response = new OpenEntityAccountResponse();
			response.setCallResult(true);
			if ("ok".equals(msg)) {
				response.setIs_success(true);
				return response;
			} else {
				return errorResponseService.getErrorResponse("P14", msg);
			}
		}else if ("ruixue.wheatfield.batchquery.person".equals(methodName)){
			if (!ValHasNoParam.hasParam(paramMap, "constid")) {
				return errorResponseService.getErrorResponse("P2", "constid不能为空");
			}
			M000003OpenPersonAccountBean openBean = (M000003OpenPersonAccountBean) BeanUtil.maptobean(M000003OpenPersonAccountBean.class,paramMap);
			OpenEntityAccountResponse response = new OpenEntityAccountResponse();
			List<FinanacePerson> finanacePersonList =this.batchQueryPersonalAccount(openBean);
			if (finanacePersonList != null && finanacePersonList.size() > 0){
				response.setFinanacepersons(finanacePersonList);
				return response;
			} else {
				errorResponse.setCode("C1");
				errorResponse.setMsg("暂无需要查询的个人开户信息数据");
				logger.error("暂无需要查询的个人开户信息数据");
				return errorResponse;
			}
		}

		return  errorResponseService.getErrorResponse("N1", "未找到方法");
	}

}
