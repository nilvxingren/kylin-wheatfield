package com.rkylin.wheatfield.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rkylin.wheatfield.common.ValHasNoParam;
import com.rkylin.wheatfield.manager.CorporatAccountInfoManager;
import com.rkylin.wheatfield.pojo.AccountInfo;
import com.rkylin.wheatfield.pojo.CorporatAccountInfo;
import com.rkylin.wheatfield.pojo.CorporatAccountInfoQuery;
import com.rkylin.wheatfield.pojo.CorporatAccountInfoScopeQuery;
import com.rkylin.wheatfield.response.CorporatAccountInfoResponse;
import com.rkylin.wheatfield.response.ErrorResponse;
import com.rkylin.wheatfield.response.Response;
import com.rkylin.wheatfield.service.CorporatAccountInfoService;
import com.rkylin.wheatfield.service.IAPIService;
import com.rkylin.wheatfield.service.IErrorResponseService;
import com.rkylin.wheatfield.utils.DateUtil;

@Service("corporatAccountInfoService")
public class CorporatAccountInfoServiceImpl implements CorporatAccountInfoService,IAPIService {
	private static Logger logger = LoggerFactory.getLogger(CorporatAccountInfoServiceImpl.class);
	
	private static Object lock=new Object();
	
	@Autowired
	private CorporatAccountInfoManager corporatAccountInfoManager;
	
	@Autowired
	IErrorResponseService errorResponseService;
	
	@Override
	public Response doJob(Map<String, String[]> paramMap, String methodName) {
		CorporatAccountInfoResponse response = new CorporatAccountInfoResponse();
		
		if("ruixue.wheatfield.corporateaccount.import".equals(methodName)){
			logger.info("---------对公账户信息批量导入开始---------");
			ErrorResponse errorResponse = this.insertCorporatAccountInfo(paramMap);
			logger.info("---------对公账户信息批量导入结束---------");
			return errorResponse;
		}else if("ruixue.wheatfield.corporateaccount.query".equals(methodName)){
			logger.info("---------查询对公账户信息开始---------");
			if (!ValHasNoParam.hasParam(paramMap, "rootinstcd")) {
				return errorResponseService.getErrorResponse("P1", "机构码不能为空");
			}
			List<CorporatAccountInfo> corporatAccountInfoList = this.selCorporatAccountInfo(paramMap);
			logger.info("---------查询对公账户信息结束---------");
			if(corporatAccountInfoList == null || corporatAccountInfoList.isEmpty()){
				response.setIs_success(false);
				response.setCallResult(false);
				response.setMsg("未查询到任何记录!");
			}else{
				response.setIs_success(true);
				response.setCorporateaccounts(corporatAccountInfoList);
			}
		}else if("ruixue.wheatfield.corporateaccount.update".equals(methodName)){
			logger.info("---------修改对公账户信息开始---------");
			ErrorResponse errorResponse = this.updateCorporatAccountInfo(paramMap);
			logger.info("---------修改对公账户信息结束---------");
			if(errorResponse == null){
				response.setIs_success(true);
			}else{
				return errorResponse;
			}
		}
		return response;
	}

	/**
	 * 批量导入对公账户信息
	 * 20150907
	 * liuhuan
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public ErrorResponse insertCorporatAccountInfo(Map<String, String[]> paramMap){
		synchronized (lock) {
		ErrorResponse errorResponse = new ErrorResponse();
		
		if (!ValHasNoParam.hasParam(paramMap, "constid")) {
			return errorResponseService.getErrorResponse("P1", "机构码不能为空");
		}
		if (!ValHasNoParam.hasParam(paramMap, "corporateaccountarray")) {
			return errorResponseService.getErrorResponse("P2", "对公账户信息集合不能为空");
		}
		
		String constid = null;
		String corporatAccountInfoString = null;
		for (Object keyObj : paramMap.keySet().toArray()) {
			String[] strs = paramMap.get(keyObj);
			for (String value : strs) {
				if(("constid").equals(keyObj)){
					constid = value;
				}
				if(("corporateaccountarray").equals(keyObj)){
					corporatAccountInfoString = value;
				}
			}
		}
        try{
    		Document doc = DocumentHelper.parseText(corporatAccountInfoString);
    	    Element signinfodetails = doc.getRootElement();
    	    
    	    List<Element> list = signinfodetails.elements();
    	    if(list != null && !list.isEmpty() && list.size() >50){
        		errorResponse.setMsg("导入有效的总条数不得大于50条");
        		logger.info("导入有效的总条数不得大于50条,实际导入总条数:" + list.size());
        		return errorResponse;	
    	    }
    	    
    	    List<CorporatAccountInfo> corporatAccountInfoList = new ArrayList<CorporatAccountInfo>();
    	    //记录错误信息的结果集
    	    List<String> errorMsgList = new ArrayList<String>();
    	    
    	    for(Element element : list){
            	String errorMsg = "";
            	
            	String accountNumber = element.elementTextTrim("accountNumber");
            	if(accountNumber == null || "".equals(accountNumber)){
            		errorMsg += accountNumber + ",1," + "accountNumber不能为空;";
            	}else{
            		//如果账号不为空，则去数据库查找此账号是否已经入库
            		CorporatAccountInfoQuery corproatAccountInfoQuery = new CorporatAccountInfoQuery();
            		corproatAccountInfoQuery.setRootInstCd(constid);
            		corproatAccountInfoQuery.setAccountNumber(accountNumber);
            		List<CorporatAccountInfo> corporatAccountInfo = corporatAccountInfoManager.queryList(corproatAccountInfoQuery);
            		if(corporatAccountInfo != null && !corporatAccountInfo.isEmpty()){
            			errorMsg += accountNumber + ",2," + "该账号已存在;";
            		}
            	}
            	String accountRealName = element.elementTextTrim("accountRealName");
            	if(accountRealName == null || "".equals(accountRealName)){
            		errorMsg += "".equals(errorMsg) ? accountNumber + ",1,accountRealName不能为空;" : "accountRealName不能为空;";
            	}
            	String currency = element.elementTextTrim("currency");
            	if(currency == null || "".equals(currency)){
            		errorMsg += "".equals(errorMsg) ? accountNumber + ",1,currency不能为空;" : "currency不能为空;";
            	}
            	String bankHead = element.elementTextTrim("bankHead");
            	if(bankHead == null || "".equals(bankHead)){
            		errorMsg += "".equals(errorMsg) ? accountNumber + ",1,bankHead不能为空;" : "bankHead不能为空;";
            	}
            	String bankHeadName = element.elementTextTrim("bankHeadName");
            	if(bankHeadName == null || "".equals(bankHeadName)){
            		errorMsg += "".equals(errorMsg) ? accountNumber + ",1,bankHeadName不能为空;" : "bankHeadName不能为空;";
            	}
            	String bankBranch = element.elementTextTrim("bankBranch");
            	if(bankBranch == null || "".equals(bankBranch)){
            		errorMsg += "".equals(errorMsg) ? accountNumber + ",1,bankBranch不能为空;" : "bankBranch不能为空;";
            	}
            	String bankBranchName = element.elementTextTrim("bankBranchName");
               	if(bankBranchName == null || "".equals(bankBranchName)){
               		errorMsg += "".equals(errorMsg) ? accountNumber + ",1,bankBranchName不能为空;" : "bankBranchName不能为空;";
            	}
            	String bankProvince = element.elementTextTrim("bankProvince");
               	if(bankProvince == null || "".equals(bankProvince)){
               		errorMsg += "".equals(errorMsg) ? accountNumber + ",1,bankProvince不能为空;" : "bankProvince不能为空;";
            	}
            	String bankCity = element.elementTextTrim("bankCity");
               	if(bankCity == null || "".equals(bankCity)){
               		errorMsg += "".equals(errorMsg) ? accountNumber + ",1,bankCity不能为空;" : "bankCity不能为空;";
            	}
            	String certificateType = element.elementTextTrim("certificateType");
               	if(certificateType == null || "".equals(certificateType)){
               		errorMsg += "".equals(errorMsg) ? accountNumber + ",1,certificateType不能为空;" : "certificateType不能为空;";
            	}
            	String certificateNumber = element.elementTextTrim("certificateNumber");
            	if(certificateNumber == null || "".equals(certificateNumber)){
            		errorMsg += "".equals(errorMsg) ? accountNumber + ",1,certificateNumber不能为空;" : "certificateNumber不能为空;";
            	}
            	if(!"".equals(errorMsg)){
            		errorMsgList.add(errorMsg);
            		continue;
            	}
            	boolean exist = false;
            	for(CorporatAccountInfo corporatAccountInfo :  corporatAccountInfoList){
            		if(corporatAccountInfo.getAccountNumber().equals(accountNumber)){
            			errorMsg += accountNumber + ",2," + "该账号上传数据重复，只入一条;";
            			errorMsgList.add(errorMsg);
            			exist = true;
            			continue;
            		}
            	}
            	//如果此条数据在已经传入的数据内重复则记录错误信息，并跳出本次循环
            	if(exist){
            		continue;
            	}
            	CorporatAccountInfo corporatAccountInfo = new CorporatAccountInfo();
            	corporatAccountInfo.setRootInstCd(constid);
            	corporatAccountInfo.setAccountNumber(accountNumber);
            	corporatAccountInfo.setAccountRealName(accountRealName);
            	String openAccountDate = element.elementTextTrim("openAccountDate");
            	if(openAccountDate != null && !"".equals(openAccountDate)){
            		corporatAccountInfo.setOpenAccountDate(new DateUtil().parse(element.elementTextTrim("openAccountDate"), "yyyy-MM-dd"));
            	}
            	corporatAccountInfo.setOpenAccountDescription(element.elementTextTrim("openAccountDescription"));
            	corporatAccountInfo.setCurrency(currency);
            	corporatAccountInfo.setBankHead(bankHead);
            	corporatAccountInfo.setBankHeadName(bankHeadName);
            	corporatAccountInfo.setBankBranch(bankBranch);
            	corporatAccountInfo.setBankBranchName(bankBranchName);
            	corporatAccountInfo.setBankProvince(bankProvince);
            	corporatAccountInfo.setBankCity(bankCity);
            	corporatAccountInfo.setCertificateType(certificateType);
            	corporatAccountInfo.setCertificateNumber(certificateNumber);
            	corporatAccountInfo.setStatusId(2);
            	
            	corporatAccountInfoList.add(corporatAccountInfo);
            }
            int resultInt = 0;
            if(corporatAccountInfoList != null && !corporatAccountInfoList.isEmpty()){
            	resultInt = corporatAccountInfoManager.insertByList(corporatAccountInfoList);
            }
            if(resultInt > 0){
            	errorResponse.setArgs(errorMsgList);
            	errorResponse.setIs_success(true);
            }else{
            	errorResponse.setArgs(errorMsgList);
            	errorResponse.setIs_success(false);
            }
            logger.info("对公账户信息批量导入结果:" + resultInt);
        }catch(Exception ex){
        	logger.error("对公账户信息批量导入异常信息:" + ex.getMessage());
        	ex.printStackTrace();
        }
		return errorResponse;
		}
	}
	
	/**
	 * 查询对公账户信息
	 * 20150908
	 * liuhuan
	 */
	public List<CorporatAccountInfo> selCorporatAccountInfo(Map<String, String[]> paramMap){
		List<CorporatAccountInfo> corporatAccountInfoList =  null;
		CorporatAccountInfoScopeQuery corporatAccountInfoQuery = new CorporatAccountInfoScopeQuery();
		try{
			for (Object keyObj : paramMap.keySet().toArray()) {
				String[] strs = paramMap.get(keyObj);
				for (String value : strs) {
					if(value == null || "".equals(value)){
						continue;
					}
					if(("corporateaccountid").equals(keyObj)){
						corporatAccountInfoQuery.setCorporateAccountId(Integer.parseInt(value));
					}
					if(("rootinstcd").equals(keyObj)){
						corporatAccountInfoQuery.setRootInstCd(value);
					}
					if(("accountnumber").equals(keyObj)){
						corporatAccountInfoQuery.setAccountNumber(value);
					}
					if(("accountrealname").equals(keyObj)){
						corporatAccountInfoQuery.setAccountRealName(value);
					}
					if(("openaccountdate").equals(keyObj)){
						corporatAccountInfoQuery.setOpenAccountDate(new DateUtil().parse(value, "yyyy-MM-dd"));
					}
					if(("openaccountdescription").equals(keyObj)){
						corporatAccountInfoQuery.setOpenAccountDescription(value);
					}
					if(("currency").equals(keyObj)){
						corporatAccountInfoQuery.setCurrency(value);
					}
					if(("bankhead").equals(keyObj)){
						corporatAccountInfoQuery.setBankHead(value);
					}
					if(("bankheadname").equals(keyObj)){
						corporatAccountInfoQuery.setBankHeadName(value);
					}
					if(("bankbranch").equals(keyObj)){
						corporatAccountInfoQuery.setBankBranch(value);
					}
					if(("bankbranchname").equals(keyObj)){
						corporatAccountInfoQuery.setBankBranchName(value);
					}
					if(("bankprovince").equals(keyObj)){
						corporatAccountInfoQuery.setBankProvince(value);
					}
					if(("bankcity").equals(keyObj)){
						corporatAccountInfoQuery.setBankCity(value);
					}
					if(("certificatetype").equals(keyObj)){
						corporatAccountInfoQuery.setCertificateType(value);
					}
					if(("certificatenumber").equals(keyObj)){
						corporatAccountInfoQuery.setCertificateNumber(value);
					}
					if(("statusid").equals(keyObj)){
						corporatAccountInfoQuery.setStatusId(Integer.parseInt(value));
					}
					if(("createdtimefrom").equals(keyObj)){
						corporatAccountInfoQuery.setCreatedTimeFrom(new DateUtil().parse(value, "yyyy-MM-dd hh:mm:ss"));
					}
					if(("createdtimeto").equals(keyObj)){
						corporatAccountInfoQuery.setCreatedTimeTo(new DateUtil().parse(value, "yyyy-MM-dd hh:mm:ss"));
					}
					if(("updatedtimefrom").equals(keyObj)){
						corporatAccountInfoQuery.setUpdatedTimeFrom(new DateUtil().parse(value, "yyyy-MM-dd hh:mm:ss"));
					}
					if(("updatedtimeto").equals(keyObj)){
						corporatAccountInfoQuery.setUpdatedTimeTo(new DateUtil().parse(value, "yyyy-MM-dd hh:mm:ss"));
					}
				}
			}
			corporatAccountInfoList = corporatAccountInfoManager.queryListScope(corporatAccountInfoQuery);
		}catch(Exception ex){
	       	logger.error("查询对公账户信息异常信息:" + ex.getMessage());
        	ex.printStackTrace();
		}
		return corporatAccountInfoList;
	}
	
	/**
	 * 批量导入对公账户信息
	 * 20150908
	 * liuhuan
	 */
	@Transactional
	public ErrorResponse updateCorporatAccountInfo(Map<String, String[]> paramMap){
		synchronized (lock) {
			ErrorResponse response = null;
			
			if (!ValHasNoParam.hasParam(paramMap, "corporateaccountid")) {
				return errorResponseService.getErrorResponse("P1", "对公账户信息ID不能为空!");
			}
			
			CorporatAccountInfo corporatAccountInfo = new CorporatAccountInfo();
			try{
				for (Object keyObj : paramMap.keySet().toArray()) {
					String[] strs = paramMap.get(keyObj);
					for (String value : strs) {
						if(("corporateaccountid").equals(keyObj)){
							CorporatAccountInfo findResult = corporatAccountInfoManager.findCorporatAccountInfoById(Long.parseLong(value));
							if(findResult != null){
								if(findResult.getStatusId() == 1){
									logger.error("该记录已经审核通过，不可以修改!");
									return errorResponseService.getErrorResponse("C2", "该记录已经审核通过，不可以修改!");
								}
								if(findResult.getStatusId() == 3){
									logger.error("该记录正在审核中，不可以修改!");
									return errorResponseService.getErrorResponse("C2", "该记录正在审核中，不可以修改!");
								}
							}
							corporatAccountInfo.setCorporateAccountId(Integer.parseInt(value));
						}
						if(("accountnumber").equals(keyObj)){
							corporatAccountInfo.setAccountNumber(value);
						}
						if(("accountrealname").equals(keyObj)){
							corporatAccountInfo.setAccountRealName(value);
						}
						if(("openaccountdate").equals(keyObj) && value != null && !"".equals(value)){
							corporatAccountInfo.setOpenAccountDate(new DateUtil().parse(value, "yyyy-MM-dd"));
						}
						if(("openaccountdescription").equals(keyObj)){
							corporatAccountInfo.setOpenAccountDescription(value);
						}
						if(("currency").equals(keyObj)){
							corporatAccountInfo.setCurrency(value);
						}
						if(("bankhead").equals(keyObj)){
							corporatAccountInfo.setBankHead(value);
						}
						if(("bankheadname").equals(keyObj)){
							corporatAccountInfo.setBankHeadName(value);
						}
						if(("bankbranch").equals(keyObj) && (value == null || "".equals(value))){
							corporatAccountInfo.setBankBranch(value);
						}
						if(("bankbranchname").equals(keyObj) && (value == null || "".equals(value))){
							corporatAccountInfo.setBankBranchName(value);
						}
						if(("bankprovince").equals(keyObj)){
							corporatAccountInfo.setBankProvince(value);
						}
						if(("bankcity").equals(keyObj)){
							corporatAccountInfo.setBankCity(value);
						}
						if(("certificatetype").equals(keyObj)){
							corporatAccountInfo.setCertificateType(value);
						}
						if(("certificatenumber").equals(keyObj)){
							corporatAccountInfo.setCertificateNumber(value);
						}
					}
				}
				corporatAccountInfo.setStatusId(2);
				corporatAccountInfo.setUpdatedTime(new DateUtil().getNow());
				int result = corporatAccountInfoManager.updateCorporatAccountInfo(corporatAccountInfo);
				logger.error("更新结果:" + result);
			}catch(Exception ex){
		       	logger.error("修改对公账户信息异常信息:" + ex.getMessage());
		       	return errorResponseService.getErrorResponse("P1", "修改对公账户信息失败!");
			}
			return response;
		}
	}
	
	/**
	 * 绑卡信息对公账户信息录入核对
	 * 20150909
	 * liuhuan
	 */
	public String accountInfoCheck(AccountInfo accountInfo,String constId){
		logger.info("---------进入绑卡流程，对公账户信息查询及校验流程开始---------");
		String msg = "ok";
		
		CorporatAccountInfoQuery corporatAccountInfoQuery = new CorporatAccountInfoQuery();
		corporatAccountInfoQuery.setRootInstCd(constId);
		corporatAccountInfoQuery.setAccountNumber(accountInfo.getAccountNumber());
		
		List<CorporatAccountInfo> corporatAccountInfoList = corporatAccountInfoManager.queryList(corporatAccountInfoQuery);
		if(corporatAccountInfoList == null || corporatAccountInfoList.isEmpty()){
			msg = "none";
		}else{
			CorporatAccountInfo corporatAccountInfo = corporatAccountInfoList.get(0);
			if(1 == corporatAccountInfo.getStatusId()){
				msg = "ok";
			}else if(2 == corporatAccountInfo.getStatusId()){
				return msg = "信息正在等待审核，请等待!";
			}else if(3 == corporatAccountInfo.getStatusId()){
				return msg = "信息正在审核中，请等待!";
			}else if(4 == corporatAccountInfo.getStatusId()){
				return msg = "信息审核失败，请核实!";
			}else{
				return msg = "该对公账户状态无效，请核实!";
			}
			if(!corporatAccountInfo.getAccountRealName().equals(accountInfo.getAccountRealName())){
				return msg = "录入账户真实名称信息与校验信息不否,请重新录入";
			}
			if(!corporatAccountInfo.getCurrency().equals(accountInfo.getCurrency())){
				return msg = "录入币种信息与校验信息不否,请重新录入";
			}
			if(!corporatAccountInfo.getBankHead().equals(accountInfo.getBankHead())){
				return msg = "录入开户行总行码信息与校验信息不否,请重新录入";
			}
			if(!corporatAccountInfo.getBankHeadName().equals(accountInfo.getBankHeadName())){
				return msg = "录入开户行总行名称信息与校验信息不否,请重新录入";
			}
			if(!corporatAccountInfo.getBankBranch().equals(accountInfo.getBankBranch())){
				return msg = "录入开户行支行码信息与校验信息不否,请重新录入";
			}
			if(!corporatAccountInfo.getBankBranchName().equals(accountInfo.getBankBranchName())){
				return msg = "录入开户行支行名称信息与校验信息不否,请重新录入";
			}
			if(!corporatAccountInfo.getBankProvince().equals(accountInfo.getBankProvince())){
				return msg = "录入开户行所在省信息与校验信息不否,请重新录入";
			}
			if(!corporatAccountInfo.getBankCity().equals(accountInfo.getBankCity())){
				return msg = "录入开户行所在市信息与校验信息不否,请重新录入";
			}
			if(!corporatAccountInfo.getBankCity().equals(accountInfo.getBankCity())){
				return msg = "录入开户行所在市信息与校验信息不否,请重新录入";
			}
			if(!corporatAccountInfo.getCertificateType().equals(accountInfo.getCertificateType())){
				return msg = "录入开户证件类型信息与校验信息不否,请重新录入";
			}
			if(!corporatAccountInfo.getCertificateNumber().equals(accountInfo.getCertificateNumber())){
				return msg = "录入证件号信息与校验信息不否,请重新录入";
			}
		}
		logger.info("---------进入绑卡流程，对公账户信息查询及校验流程结束---------");
		return msg;
	}
}
