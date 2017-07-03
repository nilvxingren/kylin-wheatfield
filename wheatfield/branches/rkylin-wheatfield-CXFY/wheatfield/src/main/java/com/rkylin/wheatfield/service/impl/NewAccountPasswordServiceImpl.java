package com.rkylin.wheatfield.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rkylin.utils.CheckUtil;
import com.rkylin.wheatfield.constant.AccountConstants;
import com.rkylin.wheatfield.constant.BaseConstants;
import com.rkylin.wheatfield.constant.ErrorCodeConstants;
import com.rkylin.wheatfield.domain.AccountPasswordBean;
import com.rkylin.wheatfield.exception.AccountException;
import com.rkylin.wheatfield.manager.AccountPasswordManager;
import com.rkylin.wheatfield.manager.ParameterInfoManager;
import com.rkylin.wheatfield.pojo.AccountPassword;
import com.rkylin.wheatfield.pojo.AccountPasswordQuery;
import com.rkylin.wheatfield.pojo.ParameterInfo;
import com.rkylin.wheatfield.pojo.ParameterInfoQuery;
import com.rkylin.wheatfield.pojo.User;
import com.rkylin.wheatfield.response.AccountPasswordResponse;
import com.rkylin.wheatfield.response.ErrorResponse;
import com.rkylin.wheatfield.response.Response;
import com.rkylin.wheatfield.service.AccountManageService;
import com.rkylin.wheatfield.service.IAPIService;
import com.rkylin.wheatfield.service.IErrorResponseService;
import com.rkylin.wheatfield.service.NewAccountPasswordService;
import com.rkylin.wheatfield.utils.AccountValueCheckUtil;
import com.rkylin.wheatfield.utils.BeanUtil;

@Transactional
@Service
public class NewAccountPasswordServiceImpl implements NewAccountPasswordService,IAPIService{
	private static Logger logger = LoggerFactory.getLogger(NewAccountPasswordServiceImpl.class);
	@Autowired
	AccountPasswordManager accountPasswordManager;
	@Autowired
	IErrorResponseService errorResponseService;
	@Autowired
	AccountManageService accountManageService;
	@Autowired
	AccountValueCheckUtil accountValueCheckUtil;
	@Autowired
	ParameterInfoManager parameterInfoManager;
	@Autowired
	private Properties accountErrorCodeProperties;
	@Override
	public Response doJob(Map<String, String[]> paramMap, String methodName) {
		AccountPasswordResponse  response=new AccountPasswordResponse();
		ErrorResponse errorResponse = new ErrorResponse();
		AccountPasswordBean accountPasswordBean = (AccountPasswordBean) BeanUtil.maptobean(AccountPasswordBean.class, paramMap);
		User user = new User();
		user.constId = accountPasswordBean.getConstid();
		user.userId = accountPasswordBean.getUserid();
		user.productId = accountPasswordBean.getProductid();

		errorResponse = accountValueCheckUtil.checkValue(accountPasswordBean.getUserid(), 
				40, AccountConstants.ACCOUNT_VALUE_CHECK_TYPE1, 0, "用户ID");
		if (!errorResponse.isIs_success()){
			return errorResponse;
		}
		errorResponse = accountValueCheckUtil.checkValue(accountPasswordBean.getConstid(), 
				20, AccountConstants.ACCOUNT_VALUE_CHECK_TYPE1, 0, "机构号");
		if (!errorResponse.isIs_success()){
			return errorResponse;
		}
		errorResponse = accountValueCheckUtil.checkValue(accountPasswordBean.getProductid(), 
				0, AccountConstants.ACCOUNT_VALUE_CHECK_TYPE1, 0, "产品号");
		if (!errorResponse.isIs_success()){
			return errorResponse;
		}
		errorResponse = accountValueCheckUtil.checkValue(accountPasswordBean.getPasswordtype(), 
				4, AccountConstants.ACCOUNT_VALUE_CHECK_TYPE1, 0, "密码类型");
		if (!errorResponse.isIs_success()){
			return errorResponse;
		}
		if (!Arrays.asList(AccountConstants.PASSWORD_TYPE).contains(accountPasswordBean.getPasswordtype())){
			return errorResponseService.getAccountErrorResponse(ErrorCodeConstants.ACCOUNT_ERROR_P105001);
		}
// 改已有数据库0 -1 事务提交
		//密码格式的校验
        //log
		if("ruixue.wheatfield.password.save".equals(methodName)){
			errorResponse = accountValueCheckUtil.checkValue(accountPasswordBean.getPassword(), 
					100, AccountConstants.ACCOUNT_VALUE_CHECK_TYPE1, 
					AccountConstants.ACCOUNT_VALUE_CHECK_FORMAT5, "密码");
			if (!errorResponse.isIs_success()){
				return errorResponse;
			}
			errorResponse = accountValueCheckUtil.checkValue(accountPasswordBean.getOpertype(), 
					0, AccountConstants.ACCOUNT_VALUE_CHECK_TYPE1, 0, "操作类型");//
			if (!errorResponse.isIs_success()){
				return errorResponse;
			}
			if (!Arrays.asList(AccountConstants.PASSWORD_OPER).contains(accountPasswordBean.getOpertype())){
				return errorResponseService.getAccountErrorResponse(ErrorCodeConstants.ACCOUNT_ERROR_P105002);
			}
			String result = this.operPassword(accountPasswordBean, user);
			if ("ok".equals(result)){
				response.setIs_success(true);
			} else {
				return errorResponseService.getAccountErrorResponse(result);
			}
		}
		if("ruixue.wheatfield.pwdlock.update".equals(methodName)){
			errorResponse = accountValueCheckUtil.checkValue(accountPasswordBean.getOpertype(), 
					0, AccountConstants.ACCOUNT_VALUE_CHECK_TYPE1, 0, "操作类型");//
			if (!errorResponse.isIs_success()){
				return errorResponse;
			}
			if (!Arrays.asList(AccountConstants.PASSWORD_OPER).contains(accountPasswordBean.getOpertype())){
				return errorResponseService.getAccountErrorResponse(ErrorCodeConstants.ACCOUNT_ERROR_P105002);
			}
			String result = this.operLock(accountPasswordBean, user);
			if ("ok".equals(result)){
				response.setIs_success(true);
			} else {
				return errorResponseService.getAccountErrorResponse(result);
			}
		}
		if("ruixue.wheatfield.password.check".equals(methodName)){
			errorResponse = accountValueCheckUtil.checkValue(accountPasswordBean.getPassword(), 
					100, AccountConstants.ACCOUNT_VALUE_CHECK_TYPE1, 
					AccountConstants.ACCOUNT_VALUE_CHECK_FORMAT5, "密码");
			if (!errorResponse.isIs_success()){
				return errorResponse;
			}
			String result = this.checkPassword(accountPasswordBean, user);
			if ("ok".equals(result)){
				response.setIs_success(true);
			} else {
				if (ErrorCodeConstants.ACCOUNT_ERROR_P105007.equals(result.split(",")[0])){
					String code =result.split(",")[0];
					String msg =accountErrorCodeProperties.getProperty(code) + result.split(",")[1];
					return errorResponseService.getAccountErrorResponse(code, msg);
				}
				return errorResponseService.getAccountErrorResponse(result);
			}
		}
		return response;
	}

	/**
	 * 新增修改密码
	 * @param opertype是insert：新增密码；opertype是 update：修改密码
	 * 修改密码会把连续错误次数置成0
	 */
	@Override
	public String operPassword(AccountPasswordBean accountPasswordBean,User user) {
		logger.info("新增修改密码————————————START————————————");
		logger.info("--用户ID--：" + user.userId + "--机构号--：" + user.constId
				+ "--产品号--：" + user.productId 
				+ "--密码类型--："+ accountPasswordBean.getPasswordtype()
				+ "--操作类型--：" + accountPasswordBean.getOpertype());
		String checkAccountResult = accountManageService.checkAccount(user);
		if (!"ok".equals(checkAccountResult)) {
			return ErrorCodeConstants.ACCOUNT_ERROR_C104001;
		}
		String getCountResult=MixErrorCount(user);
		if ("ng".equals(getCountResult)){
			return ErrorCodeConstants.ACCOUNT_ERROR_P105003;
		}
		AccountPasswordQuery hasPasswordQuery = new AccountPasswordQuery();
		hasPasswordQuery.setUserId(accountPasswordBean.getUserid());
		hasPasswordQuery.setPasswordType(accountPasswordBean.getPasswordtype());
		hasPasswordQuery.setRootInstCd(accountPasswordBean.getConstid());
		List<AccountPassword> accountPasswordList = accountPasswordManager.queryList(hasPasswordQuery);
		AccountPassword accountPassword = new AccountPassword();
		if ("insert".equals(accountPasswordBean.getOpertype())){
			logger.info("----------新增密码----------");
			if (!CheckUtil.checkNullOrEmpty(accountPasswordList)){
				return ErrorCodeConstants.ACCOUNT_ERROR_P105003;
			}
			accountPassword.setUserId(accountPasswordBean.getUserid());
			accountPassword.setPasswordType(accountPasswordBean.getPasswordtype());
			accountPassword.setRootInstCd(accountPasswordBean.getConstid());
			accountPassword.setPassword(accountPasswordBean.getPassword());
			accountPassword.setProsetFlag(AccountConstants.INITIAL_PASSWORD_UNSET);
			accountPassword.setAllowErrorCount(Integer.parseInt(getCountResult));
			accountPassword.setErrorCount(AccountConstants.ERROR_COUNT_INITIAL);
			accountPassword.setRemark("");
			accountPassword.setStatusId(BaseConstants.PWD_STATUS_1);
			accountPasswordManager.saveAccountPassword(accountPassword);
		} else if ("update".equals(accountPasswordBean.getOpertype())){
			logger.info("----------修改密码----------");
			if (CheckUtil.checkNullOrEmpty(accountPasswordList)){
				return ErrorCodeConstants.ACCOUNT_ERROR_P105004;
			}
			if (BaseConstants.PWD_STATUS_1.equals(accountPasswordList.get(0).getStatusId())){
				accountPassword.setAcctPawdId(accountPasswordList.get(0).getAcctPawdId());
				accountPassword.setErrorCount(AccountConstants.ERROR_COUNT_INITIAL);
				accountPassword.setPassword(accountPasswordBean.getPassword());
				accountPasswordManager.saveAccountPassword(accountPassword);
			} else {
				return ErrorCodeConstants.ACCOUNT_ERROR_P105005;
			}
		}
		logger.info("新增修改密码————————————END————————————");
		return "ok";
	}

	/**
	 * 锁定解锁密码
	 * @param opertype是lockup：锁定密码；opertype是 unlock：解锁密码
	 */
	@Override
	public String operLock(AccountPasswordBean accountPasswordBean,User user) {
		logger.info("锁定解锁密码————————————START————————————");
		logger.info("--用户ID--：" + user.userId + "--机构号--：" + user.constId
				+ "--产品号--：" + user.productId 
				+ "--密码类型--："+ accountPasswordBean.getPasswordtype()
				+ "--操作类型--：" + accountPasswordBean.getOpertype());
		String result = accountManageService.checkAccount(user);
		if (!"ok".equals(result)) {
			return ErrorCodeConstants.ACCOUNT_ERROR_C104001;
		}
		AccountPasswordQuery hasPasswordQuery = new AccountPasswordQuery();
		hasPasswordQuery.setUserId(accountPasswordBean.getUserid());
		hasPasswordQuery.setPasswordType(accountPasswordBean.getPasswordtype());
		hasPasswordQuery.setRootInstCd(accountPasswordBean.getConstid());
		List<AccountPassword> accountPasswordList = accountPasswordManager.queryList(hasPasswordQuery);
		if (CheckUtil.checkNullOrEmpty(accountPasswordList)){
			return ErrorCodeConstants.ACCOUNT_ERROR_P105004;
		}
		AccountPassword accountPassword = new AccountPassword();
		if ("lockup".equals(accountPasswordBean.getOpertype())){
			logger.info("----------锁定密码----------");
			if (BaseConstants.PWD_STATUS_1.equals(accountPasswordList.get(0).getStatusId())){
				accountPassword.setAcctPawdId(accountPasswordList.get(0).getAcctPawdId());
				accountPassword.setStatusId(BaseConstants.PWD_STATUS_0);
				accountPasswordManager.saveAccountPassword(accountPassword);
			} else {
				return ErrorCodeConstants.ACCOUNT_ERROR_P105005;
			}
		} else if("unlock".equals(accountPasswordBean.getOpertype())) {
			logger.info("----------解锁密码----------");
			if (BaseConstants.PWD_STATUS_0.equals(accountPasswordList.get(0).getStatusId())){
				accountPassword.setAcctPawdId(accountPasswordList.get(0).getAcctPawdId());
				accountPassword.setStatusId(BaseConstants.PWD_STATUS_1);
				accountPassword.setErrorCount(AccountConstants.ERROR_COUNT_INITIAL);
				accountPasswordManager.saveAccountPassword(accountPassword);
			} else {
				return ErrorCodeConstants.ACCOUNT_ERROR_P105006;
			}
		}
		logger.info("锁定解锁密码————————————END————————————");
		return "ok";
	}
	
	/**
	 * 用户密码校验
	 * @param
	 */
	@Override
    @Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
	public String checkPassword(AccountPasswordBean accountPasswordBean,User user) throws AccountException{
		logger.info("用户密码校验————————————START————————————");//解锁权限的说明（rop）
		logger.info("--用户ID--：" + user.userId + "--机构号--：" + user.constId
				+ "--产品号--：" + user.productId 
				+ "--密码类型--："+ accountPasswordBean.getPasswordtype());
		String result = accountManageService.checkAccount(user);
		if (!"ok".equals(result)) {
			return ErrorCodeConstants.ACCOUNT_ERROR_C104001;
		}
		String chkPassword = accountPasswordBean.getPassword();
		AccountPassword accountPassword = new AccountPassword();
		AccountPasswordQuery hasPasswordQuery = new AccountPasswordQuery();
		hasPasswordQuery.setRootInstCd(accountPasswordBean.getConstid());
		hasPasswordQuery.setUserId(accountPasswordBean.getUserid());
		hasPasswordQuery.setPasswordType(accountPasswordBean.getPasswordtype());
		List<AccountPassword> accountPasswordList = accountPasswordManager.queryList(hasPasswordQuery);		
		if (CheckUtil.checkNullOrEmpty(accountPasswordList)){
			return ErrorCodeConstants.ACCOUNT_ERROR_P105004;
		}
		if (BaseConstants.PWD_STATUS_0.equals(accountPasswordList.get(0).getStatusId())){
			return ErrorCodeConstants.ACCOUNT_ERROR_P105005;
		}
		if (chkPassword.equals(accountPasswordList.get(0).getPassword())){
			accountPassword.setAcctPawdId(accountPasswordList.get(0).getAcctPawdId());
			accountPassword.setErrorCount(AccountConstants.ERROR_COUNT_INITIAL);
			accountPasswordManager.saveAccountPassword(accountPassword);
			logger.info("用户密码校验————————————END————————————");
			return "ok";
		} 
		if (null==accountPasswordList.get(0).getAllowErrorCount()
			|| accountPasswordList.get(0).getAllowErrorCount() == AccountConstants.ALLOW_ERROR_UNSET){
			logger.info("---允许密码错误次数为空或null时，关闭密码错误次数限制开关---");
			accountPassword.setAcctPawdId(accountPasswordList.get(0).getAcctPawdId());
			accountPassword.setErrorCount(accountPasswordList.get(0).getErrorCount() + 1);
			accountPasswordManager.saveAccountPassword(accountPassword);
			return ErrorCodeConstants.ACCOUNT_ERROR_P105007 + "," + accountPassword.getErrorCount();
		} else {
			logger.info("---允许密码错误次数为空或null时，打开密码错误次数限制开关---");
			if (accountPasswordList.get(0).getErrorCount() < accountPasswordList.get(0).getAllowErrorCount()){
				accountPassword.setAcctPawdId(accountPasswordList.get(0).getAcctPawdId());
				accountPassword.setErrorCount(accountPasswordList.get(0).getErrorCount() + 1);
				accountPasswordManager.saveAccountPassword(accountPassword);
				logger.info("---未达上限次数" + accountPasswordList.get(0).getErrorCount() + "次，当前错误次数为" + accountPassword.getErrorCount() + "---");
				return ErrorCodeConstants.ACCOUNT_ERROR_P105007 + "," + accountPassword.getErrorCount() ;
			} else {
				accountPassword.setAcctPawdId(accountPasswordList.get(0).getAcctPawdId());
				accountPassword.setStatusId(BaseConstants.PWD_STATUS_0);
				accountPasswordManager.saveAccountPassword(accountPassword);
				String freezeResult = accountManageService.freezeAccount(user);
				if (!"ok".equals(freezeResult)) {
					throw new AccountException(ErrorCodeConstants.ACCOUNT_ERROR_C102002);
				}
				logger.info("---错误次数达到上限，账户已经冻结---");
				return ErrorCodeConstants.ACCOUNT_ERROR_P105008;
			}
		}
	}
	
	public String MixErrorCount(User user){
		logger.info("----------取得允许密码连续错误次数----------");
		String paraVaule = "";
		ParameterInfoQuery allowErrorCountQuery = new ParameterInfoQuery();
		allowErrorCountQuery.setParameterValue(user.constId);
		List<ParameterInfo> allowErrorCountList = parameterInfoManager.queryAllowErrorCountList(allowErrorCountQuery);
		logger.info("---机构码为" + user.constId + "的允许连错次数是：" + allowErrorCountList.get(0).getParameterValue() + "---");
		if (CheckUtil.checkNullOrEmpty(allowErrorCountList)){
			return "ng";
		}
		paraVaule = allowErrorCountList.get(0).getParameterValue();
		return paraVaule;
	}
}
