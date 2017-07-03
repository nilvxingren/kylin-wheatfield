package com.rkylin.wheatfield.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkylin.wheatfield.api.FinanaceEntryService;
import com.rkylin.wheatfield.common.ValHasNoParam;
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.dao.FinanaceAccountDao;
import com.rkylin.wheatfield.dao.FinanaceEntryDao;
import com.rkylin.wheatfield.manager.AccountInfoManager;
import com.rkylin.wheatfield.manager.FinanaceCompanyManager;
import com.rkylin.wheatfield.pojo.AccountInfo;
import com.rkylin.wheatfield.pojo.AccountInfoQuery;
import com.rkylin.wheatfield.pojo.FinanaceAccountQuery;
import com.rkylin.wheatfield.pojo.FinanaceCompany;
import com.rkylin.wheatfield.pojo.FinanaceCompanyQuery;
import com.rkylin.wheatfield.pojo.FinanaceEntryDto;
import com.rkylin.wheatfield.pojo.FinanaceEntryQuery;
import com.rkylin.wheatfield.pojo.User;
import com.rkylin.wheatfield.response.AccountInfoGetResponse;
import com.rkylin.wheatfield.response.ErrorResponse;
import com.rkylin.wheatfield.response.Response;
import com.rkylin.wheatfield.service.AccountManageQueryService;
import com.rkylin.wheatfield.service.IAPIService;
import com.rkylin.wheatfield.utils.DateUtil;

@Service("accountManageQueryService")
public class AccountManageQueryServiceImpl implements AccountManageQueryService,FinanaceEntryService,IAPIService {

	@Autowired
	AccountInfoManager accountInfoManager;
	@Autowired
	FinanaceCompanyManager finanaceCompanyManager;
	@Autowired
	FinanaceEntryDao finanaceEntryDao;
	@Autowired
	FinanaceAccountDao finanaceAccountDao;
	
	private static Logger logger = LoggerFactory.getLogger(AccountManageQueryServiceImpl.class);	
	@Override
	public Response doJob(Map<String, String[]> paramMap, String methodName) {
		
		//根据不同的bean调用不同的方法
		AccountInfoGetResponse response=new AccountInfoGetResponse();
		if ("ruixue.wheatfield.finanace.entrylist.query".equals(methodName)) {//查询账户记账流水信息
			String rootinstcd = "";
			String userid = "";
			String productid = "";
			String createdtimefrom = null;
			String createdtimeto = null;
			String  requestid = "";
			String  querytype = "";
			for (Object keyObj : paramMap.keySet().toArray()) {
				String[] strs = paramMap.get(keyObj);
				for (String value : strs) {
					if (keyObj.equals("rootinstcd")) {
						rootinstcd = value;
					}else if(keyObj.equals("userid")) {
						userid = value;
					}else if(keyObj.equals("productid")) {
						productid = value;
					}else if(keyObj.equals("createdtimefrom")) {
						createdtimefrom = value;
					}else if(keyObj.equals("createdtimeto")) {
						createdtimeto = value;
					}else if(keyObj.equals("requestid")) {
						requestid = value;
					}else if(keyObj.equals("querytype")) {
						querytype = value;
					}
				}
			}
			response = this.queryFinanaceentryList(rootinstcd, userid,
					productid, createdtimefrom,createdtimeto,requestid,querytype);
			return response;
			
		}
		
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
	
	/**
	 * 查询流水信息
	*
	* @param rootinstcd 管理机构代码（查询类型为1时，必须入参）
	* @param userid 用户id（查询类型为1时，必须入参）
	* @param productid 产品号
	* @param createdtimefrom 记录创建开始时间
	* @param createdtimeto 记录创建结束时间
	* @param requestid 交易记录编码（查询类型为2时，必须入参）
	* @param querytype 查询类型（1:根据账户查询 2:根据交易记录查询）
	* @return
	 */
	@Override
	public AccountInfoGetResponse queryFinanaceentryList(String rootinstcd, String userid,
					String productid, String createdtimefrom, String createdtimeto,String requestid,String querytype) {
			logger.info("------------ 查询流水信息开始,入参列表：rootinstcd=" + rootinstcd + ",userid=" + userid + ",productid=" + productid + 
					",createdtimefrom=" + createdtimefrom + ",createdtimeto=" + createdtimeto + ",requestid=" + requestid +
					",querytype" + querytype + "-----------");
			
//			HashMap<String,String> rtnMap = new HashMap<String,String>();
			AccountInfoGetResponse response = new AccountInfoGetResponse();
			if (null == querytype || "".equals(querytype)) {
				response.setIs_success(false);
				response.setMsg("查询类型不能为空");
				return response;
			}
			//查询结果
			List<FinanaceEntryDto> finanaceEntryList = new ArrayList<FinanaceEntryDto>();
			if("1".equals(querytype)){//根据账户查询 
				if (null == rootinstcd || "".equals(rootinstcd)) {
					response.setIs_success(false);
					response.setMsg("机构号不能为空");
					return response;
				}	
				if (null == userid || "".equals(userid)) {
					response.setIs_success(false);
					response.setMsg("用户id不能为空");
					return response;
				}
				if ((null != createdtimeto && !"".equals(createdtimeto)) && (null == createdtimefrom || "".equals(createdtimefrom))) {
					response.setIs_success(false);
					response.setMsg("记录创建结束时间不为空时，记录创建开始时间不能为空");
					return response;
				}
				
				FinanaceEntryQuery query = new FinanaceEntryQuery();
				query.setRootinstcd(rootinstcd);
				query.setUserid(userid);
				if (null != createdtimefrom && !"".equals(createdtimefrom)){
					query.setCreatedtimefrom(createdtimefrom);
				}
				if (null != createdtimeto && !"".equals(createdtimeto)){
					query.setCreatedtimeto(createdtimeto);
				}
				
				//当产品号为空时，查询用户所有主子账户中符合条件的记录，否则查询给定条件的记录
				if (null == productid || "".equals(productid)) {
					FinanaceAccountQuery que = new FinanaceAccountQuery();
					que.setRootInstCd(rootinstcd);
					que.setAccountRelateId(userid);;
					List<String> productIdList = finanaceAccountDao.selectProductIdList(que);
					Iterator it = productIdList.iterator(); 
					while(it.hasNext()) {
						query.setProductid((String)it.next());
						List<FinanaceEntryDto> fList = finanaceEntryDao.queryFinDtoList(query);
						finanaceEntryList.addAll(fList);
					}
					
				}else{
					query.setProductid(productid);
					finanaceEntryList = finanaceEntryDao.queryFinDtoList(query);
				}
				
			}else if("2".equals(querytype)){//根据交易记录查询
				if (null == requestid || "".equals(requestid)) {
					response.setIs_success(false);
					response.setMsg("交易记录编码不能为空");
					return response;
				}
				
				finanaceEntryList = finanaceEntryDao.selectByReferid(requestid);
			}
			
	        response.setFinanaceEntryList(finanaceEntryList);
	        response.setIs_success(true);
	        
	        logger.info("------------ 查询流水信息结束-----------------");
			return response;
	}
}
