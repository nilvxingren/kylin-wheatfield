/**
 * @File name : BindingCardServiceImpl.java
 * @Package : com.rkylin.wheatfield.service.impl
 * @Description : TODO(去掉对私验证的绑卡接口)
 * @Creator : liuhuan
 * @CreateTime : 2015年12月4日 下午1:40:14
 */
package com.rkylin.wheatfield.service.impl;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkylin.wheatfield.common.DateUtils;
import com.rkylin.wheatfield.common.ValHasNoParam;
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.constant.ErrorCodeConstants;
import com.rkylin.wheatfield.enumtype.UserTypeEnum;
import com.rkylin.wheatfield.pojo.AccountInfo;
import com.rkylin.wheatfield.pojo.User;
import com.rkylin.wheatfield.response.AccountInfoGetResponse;
import com.rkylin.wheatfield.response.Response;
import com.rkylin.wheatfield.service.AccountManageService;
import com.rkylin.wheatfield.service.BindingBankCardService;
import com.rkylin.wheatfield.service.IAPIService;
import com.rkylin.wheatfield.service.IErrorResponseService;

@Service("ruixue.wheatfield.bankaccount.binding.nocheck")
public class BindingBankCardServiceImpl implements IAPIService, BindingBankCardService {
	private static Logger logger = LoggerFactory.getLogger(BindingBankCardServiceImpl.class);
	@Autowired
	IErrorResponseService errorResponseService;
	@Autowired
	AccountManageService accountManageService;
	
	@Override
	public Response doJob(Map<String, String[]> paramMap, String methodName) {
		if (!ValHasNoParam.hasParam(paramMap, "userid")) {
			return errorResponseService.getErrorResponse("P1", "userid不能为空");
		}
		if (!ValHasNoParam.hasParam(paramMap, "usertype")) {
			return errorResponseService.getErrorResponse("P2", "usertype不能为空");
		}
		if (!ValHasNoParam.hasParam(paramMap, "productid")) {
			return errorResponseService.getErrorResponse("P4", "productid不能为空");
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
				} 
			}
		}
		AccountInfoGetResponse response = new AccountInfoGetResponse();
		if (!ValHasNoParam.hasParam(paramMap, "accountnumber")) {
			return errorResponseService.getErrorResponse("P5", "账号不能为空");
		}
		if (!ValHasNoParam.hasParam(paramMap, "accounttypeid")) {
			return errorResponseService.getErrorResponse("P6", "账户类型ID不能为空");
		}
		if (!ValHasNoParam.hasParam(paramMap, "bankheadname")) {
			return errorResponseService.getErrorResponse("P7","开户行总行名称不能为空");
		}
		if (!ValHasNoParam.hasParam(paramMap, "currency")) {
			return errorResponseService.getErrorResponse("P8", "币种不能为空");
		}
		if (!ValHasNoParam.hasParam(paramMap, "accountpurpose")) {
			return errorResponseService.getErrorResponse("P11", "账户目的不能为空");
		}
		if (!ValHasNoParam.hasParam(paramMap, "accountproperty")) {
			return errorResponseService.getErrorResponse("P12", "账户属性不能为空");
		}
		if(!ValHasNoParam.hasParam(paramMap, "certificatetype")){
			return errorResponseService.getErrorResponse("P13","开户证件类型不能为空");
		}
		if(!ValHasNoParam.hasParam(paramMap, "certificatenumnumber")){
			 return errorResponseService.getErrorResponse("P14","证件号不能为空");
		}
		if (!ValHasNoParam.hasParam(paramMap, "account_name")) {
			return errorResponseService.getErrorResponse("P15", "账号名不能为空");
		}
		if (!ValHasNoParam.hasParam(paramMap, "bank_code")) {
			return errorResponseService.getErrorResponse("P16", "银行编码不能为空");
		}
		AccountInfo accountInfo = new AccountInfo();
		for (Object keyObj : paramMap.keySet().toArray()) {
			String[] strs = paramMap.get(keyObj);
			for (String value : strs) {
				if ("accountnumber".equals(keyObj)) {
					accountInfo.setAccountNumber(value);
				} else if ("accounttypeid".equals(keyObj)) {
					accountInfo.setAccountTypeId(value);
				} else if ("bankbranchname".equals(keyObj)) {
					accountInfo.setBankBranchName(value);
				} else if ("bankheadname".equals(keyObj)) {
					accountInfo.setBankHeadName(value);
				} else if ("currency".equals(keyObj)) {
					accountInfo.setCurrency(value);
				} else if ("openaccountdate".equals(keyObj)) {
					try {
						Date date = DateUtils.getDate(value,Constants.DATE_FORMAT_YYYYMMDD);
						accountInfo.setOpenAccountDate(date);
					} catch (Exception e) {
						e.printStackTrace();
						return errorResponseService.getErrorResponse(ErrorCodeConstants.SYS_ERROR_CODE_M1);
					}
				} else if ("openaccountdescription".equals(keyObj)&& value != null && !"".equals(value)) {
					accountInfo.setOpenAccountDescription(value);
				} else if ("accountpurpose".equals(keyObj)) {
					accountInfo.setAccountPurpose(value);
				} else if ("accountproperty".equals(keyObj)) {
					accountInfo.setAccountProperty(value);
				} else if ("certificatetype".equals(keyObj)) {
					accountInfo.setCertificateType(value);
				} else if ("certificatenumnumber".equals(keyObj)) {
					accountInfo.setCertificateNumber(value);
				}else if ("bank_code".equals(keyObj)) {
					accountInfo.setBankHead(value);
				} else if ("account_name".equals(keyObj)) {
					accountInfo.setAccountRealName(value);
				}else if ("userid".equals(keyObj)) {
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
		String msg = accountManageService.bindingBankAccount(user, accountInfo);
		if ("ok".equals(msg)) {
			response.setIs_success(true);
		} else {
			logger.info("----C1--bind-"+msg);
			return errorResponseService.getErrorResponse("C1", msg);
		}
		return response;
	}
}
