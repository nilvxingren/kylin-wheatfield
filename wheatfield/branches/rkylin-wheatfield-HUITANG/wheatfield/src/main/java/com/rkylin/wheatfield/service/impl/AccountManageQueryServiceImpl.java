package com.rkylin.wheatfield.service.impl;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkylin.wheatfield.common.ValHasNoParam;
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.manager.AccountInfoManager;
import com.rkylin.wheatfield.manager.FinanaceCompanyManager;
import com.rkylin.wheatfield.pojo.AccountInfo;
import com.rkylin.wheatfield.pojo.AccountInfoQuery;
import com.rkylin.wheatfield.pojo.FinanaceCompany;
import com.rkylin.wheatfield.pojo.FinanaceCompanyQuery;
import com.rkylin.wheatfield.pojo.User;
import com.rkylin.wheatfield.response.AccountInfoGetResponse;
import com.rkylin.wheatfield.response.ErrorResponse;
import com.rkylin.wheatfield.response.Response;
import com.rkylin.wheatfield.service.AccountManageQueryService;
import com.rkylin.wheatfield.service.IAPIService;
import com.rkylin.wheatfield.utils.DateUtil;

@Service("accountManageQueryService")
public class AccountManageQueryServiceImpl implements AccountManageQueryService,IAPIService {

	@Autowired
	AccountInfoManager accountInfoManager;
	@Autowired
	FinanaceCompanyManager finanaceCompanyManager;
	
	private static Logger logger = LoggerFactory.getLogger(AccountManageQueryServiceImpl.class);	
	@Override
	public Response doJob(Map<String, String[]> paramMap, String methodName) {
		ErrorResponse errorResponse=new ErrorResponse();
		//必填项非空判断
		if (!ValHasNoParam.hasParam(paramMap, "constid")) {
			errorResponse.setCode("P1");
			errorResponse.setMsg("constid不能为空");
			return errorResponse;
		}
		//赋值
		User user=new User();
		AccountInfoQuery accountInfoQuery=new AccountInfoQuery();
		FinanaceCompanyQuery finanaceCompanyQuery=new FinanaceCompanyQuery();
		
		DateUtil dateUtil=new DateUtil();
		for(Object keyObj : paramMap.keySet().toArray()){
			System.out.println("key:" + keyObj);
			String[] strs = paramMap.get(keyObj);
			for(String value : strs){
				logger.info("参数KEY="+keyObj+" Value="+value);
				if(keyObj.equals("userid")){
					user.userId=value;
					accountInfoQuery.setAccountName(value);
					finanaceCompanyQuery.setAccountRelateId(value);
				}else if (keyObj.equals("usertype")) {
					user.uEType=value;
				}else if (keyObj.equals("constid")) {
					user.constId=value;
					accountInfoQuery.setRootInstCd(value);
					finanaceCompanyQuery.setRootInstCd(value);
				}else if (keyObj.equals("productid")) {
					user.productId=value;
				}else if (keyObj.equals("role")) {
					user.role=value;
				}else if (keyObj.equals("accountpurpose")) {
					accountInfoQuery.setAccountPurpose(value);
				}else if (keyObj.equals("accountnumber")) {
					accountInfoQuery.setAccountNumber(value);
				}else if (keyObj.equals("accounttypeid")) {
					accountInfoQuery.setAccountTypeId(value);
				}else if (keyObj.equals("currency")) {
					accountInfoQuery.setCurrency(value);
				}else if (keyObj.equals("openaccountstartdate")) {
					try {
						accountInfoQuery.setOpenaccountStartDate(dateUtil.parse(value, Constants.DATE_FORMAT_YYYYMMDDHHMMSS));
					} catch (ParseException e) {
						errorResponse.setCode("P2");
						errorResponse.setMsg("openaccountstartdate输入日期格式错误,应为：yyyy-MM-dd HH:mm:ss");
						logger.error("openaccountstartdate输入日期格式错误:"+value);
						return errorResponse;
					}
				}else if (keyObj.equals("openaccountenddate")) {
					try {
						accountInfoQuery.setOpenaccountEndDate(dateUtil.parse(value, Constants.DATE_FORMAT_YYYYMMDDHHMMSS));
					} catch (ParseException e) {
						errorResponse.setCode("P2");
						errorResponse.setMsg("openaccountenddate输入日期格式错误,应为：yyyy-MM-dd HH:mm:ss");
						logger.error("openaccountenddate输入日期格式错误:"+value);
						return errorResponse;
					}
				}else if (keyObj.equals("accountproperty")) {
					accountInfoQuery.setAccountProperty(value);
				}else if (keyObj.equals("certificatetype")) {
					accountInfoQuery.setCertificateType(value);
				}else if (keyObj.equals("certificatenumber")) {
					accountInfoQuery.setCertificateNumber(value);
				}else if (keyObj.equals("accountname")) {
					accountInfoQuery.setAccountRealName(value);
				}else if (keyObj.equals("createdstarttime")) {
					try {
						accountInfoQuery.setCreatedStartTime(dateUtil.parse(value, Constants.DATE_FORMAT_YYYYMMDDHHMMSS));
						finanaceCompanyQuery.setCreatedStartTime(dateUtil.parse(value, Constants.DATE_FORMAT_YYYYMMDDHHMMSS));
					} catch (ParseException e) {
						errorResponse.setCode("P2");
						errorResponse.setMsg("createdstarttime输入日期格式错误,应为：yyyy-MM-dd HH:mm:ss");
						logger.error("createdstarttime输入日期格式错误:"+value);
						return errorResponse;
					}
				}else if (keyObj.equals("createdendtime")) {
					try {
						accountInfoQuery.setCreatedEndTime(dateUtil.parse(value, Constants.DATE_FORMAT_YYYYMMDDHHMMSS));
						finanaceCompanyQuery.setCreatedEndTime(dateUtil.parse(value, Constants.DATE_FORMAT_YYYYMMDDHHMMSS));
					} catch (ParseException e) {
						errorResponse.setCode("P2");
						errorResponse.setMsg("createdendtime输入日期格式错误,应为：yyyy-MM-dd HH:mm:ss");
						logger.error("createdendtime输入日期格式错误:"+value);
						return errorResponse;
					}
				}else if (keyObj.equals("status")) {
					accountInfoQuery.setStatus(Integer.parseInt(value));
					finanaceCompanyQuery.setStatusId(value);
				}else if(keyObj.equals("companyname")){
					finanaceCompanyQuery.setCompanyName(value);
				}else if(keyObj.equals("shortname")){
					finanaceCompanyQuery.setCompanyShortName(value);
				}else if(keyObj.equals("buslince")){
					finanaceCompanyQuery.setBuslince(value);
				}else if(keyObj.equals("acuntopnlince")){
					finanaceCompanyQuery.setAcuntOpnLince(value);
				}else if(keyObj.equals("companycode")){
					finanaceCompanyQuery.setCompanyCode(value);
				}else if(keyObj.equals("organcertificate")){
					finanaceCompanyQuery.setOrganCertificate(value);
				}else if(keyObj.equals("corporatename")){
					finanaceCompanyQuery.setCorporateName(value);
				}else if(keyObj.equals("corporateidentity")){
					finanaceCompanyQuery.setCorporateIdentity(value);
				}
			}
		}
		//根据不同的bean调用不同的方法
		AccountInfoGetResponse response=new AccountInfoGetResponse();
		if("ruixue.wheatfield.accountbankcard.query".equals(methodName)){
			List<AccountInfo> accountBankCardList=this.getAccountBankCardList(accountInfoQuery);
			if(null!=accountBankCardList&&0<accountBankCardList.size()){
				response.setAccountinfos(accountBankCardList);
			}else{
				errorResponse.setCode("P3");
				errorResponse.setMsg("暂无需要查询的绑卡信息数据");
				logger.error("暂无需要查询的绑卡信息数据");
				return errorResponse;
			}
		}else if("ruixue.wheatfield.batchquery.company".equals(methodName)){
			List<FinanaceCompany> finanaceCompanies=this.getAccountCompanyList(finanaceCompanyQuery);
			if(null!=finanaceCompanies&&0<finanaceCompanies.size()){
				response.setFinanaceCompanies(finanaceCompanies);
			}else{
				errorResponse.setCode("P3");
				errorResponse.setMsg("暂无需要查询的企业开户信息数据");
				logger.error("暂无需要查询的企业开户信息数据");
				return errorResponse;
			}
		}				
		return response;
	}
	
	
	@Override
	public List<AccountInfo> getAccountBankCardList(AccountInfoQuery accountInfoQuery) {
		logger.info("--------获取银行卡信息列表！--------");
		try {
			return accountInfoManager.getAccountBankCardList(accountInfoQuery);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}


	@Override
	public List<FinanaceCompany> getAccountCompanyList(FinanaceCompanyQuery finanaceCompanyQuery) {
		logger.info("--------获取企业信息列表！--------");
		try {
			return finanaceCompanyManager.getFinanaceCompanies(finanaceCompanyQuery);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}
}
