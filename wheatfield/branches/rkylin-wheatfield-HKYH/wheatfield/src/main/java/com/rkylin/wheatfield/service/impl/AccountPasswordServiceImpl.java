package com.rkylin.wheatfield.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rkylin.wheatfield.common.ValHasNoParam;
import com.rkylin.wheatfield.constant.BaseConstants;
import com.rkylin.wheatfield.manager.AccountPasswordManager;
import com.rkylin.wheatfield.pojo.AccountPassword;
import com.rkylin.wheatfield.pojo.AccountPasswordQuery;
import com.rkylin.wheatfield.pojo.FinanaceAccount;
import com.rkylin.wheatfield.pojo.User;
import com.rkylin.wheatfield.response.AccountInfoGetResponse;
import com.rkylin.wheatfield.response.Response;
import com.rkylin.wheatfield.service.AccountManageService;
import com.rkylin.wheatfield.service.AccountPasswordService;
import com.rkylin.wheatfield.service.IAPIService;
import com.rkylin.wheatfield.service.IErrorResponseService;
import com.rkylin.wheatfield.utils.SignUtils;
@Service("accountPasswordService")
public class AccountPasswordServiceImpl implements AccountPasswordService,
		IAPIService {
	private static Logger logger = LoggerFactory.getLogger(AccountPasswordServiceImpl.class);
	@Autowired
	AccountPasswordManager accountPasswordManager;
	@Autowired
	IErrorResponseService errorResponseService;
	@Autowired
	AccountManageService accountManageService;
	@Override
	public Response doJob(Map<String, String[]> paramMap, String methodName) {
		if(!"ruixue.wheatfield.pwdlock.update".equals(methodName) && !"ruixue.wheatfield.password.query".equals(methodName)){
			if(!ValHasNoParam.hasParam(paramMap, "password")){
				return errorResponseService.getErrorResponse("P1","密码不能为空");
			}
		}
		if(!ValHasNoParam.hasParam(paramMap, "userid")){
			return errorResponseService.getErrorResponse("P2","用户ID不能为空");
		}
		if(!ValHasNoParam.hasParam(paramMap, "constid")){
			return errorResponseService.getErrorResponse("P3","机构号不能为空");
		}
		if(!"ruixue.wheatfield.password.query".equals(methodName)){
	        if(!ValHasNoParam.hasParam(paramMap, "productid")){
	            return errorResponseService.getErrorResponse("P4","产品号不能为空");
	        }		    
		}
		
		AccountPassword accountPassword=new AccountPassword();
		User user =new User();
		String opertype="";
		for(Object keyObj : paramMap.keySet().toArray()){
			String[] strs = paramMap.get(keyObj);
			for(String value : strs){
				if(keyObj.equals("password")){
					accountPassword.setPassword(value);
				}else if(keyObj.equals("constid")){
					accountPassword.setRootInstCd(value);
					user.constId=value;
				}else if(keyObj.equals("userid")){
					accountPassword.setUserId(value);
					user.userId=value;
				}else if(keyObj.equals("passwordtype")){
					accountPassword.setPasswordType(value);
				}else if(keyObj.equals("productid")){
					user.productId=value;
				}else if(keyObj.equals("opertype")){
					opertype=value;
				}else if(keyObj.equals("referuserid")){
					user.referUserId=value;
				}
			}
		}
//		if(ValHasNoParam.hasParam(paramMap, "prosetflag") && accountPassword.getProsetFlag()==1){
//			accountPassword.setPassword("111111");
//		}else{
//			return errorResponseService.getErrorResponse("C2", "是否预设密码为1，或者为空！");
//		}
		AccountInfoGetResponse  response=new AccountInfoGetResponse();
		if("ruixue.wheatfield.password.save".equals(methodName)){
			if (!ValHasNoParam.hasParam(paramMap, "opertype")) {
				return errorResponseService.getErrorResponse("P5", "操作类型不能为空");
			}
			if (!ValHasNoParam.hasParam(paramMap, "passwordtype")) {
				return errorResponseService.getErrorResponse("P6", "密码类型不能为空");
			}
			if (opertype.equals("update") && opertype.equals("insert")) {
				return errorResponseService.getErrorResponse("C1", "操作类型输入错误");
			}
			String msg = this.savePassword(accountPassword, opertype, user);
			if ("ok".equals(msg)) {
				response.setIs_success(true);
			} else {
				return errorResponseService.getErrorResponse("C2", msg);
			}
		}else if("ruixue.wheatfield.password.check".equals(methodName)){
			String msg=this.checkPassword(accountPassword);
			String msgs=msg.split(",")[0];
			if("ok".equals(msgs)){
				response.setIs_success(true);
			}else{
				return errorResponseService.getErrorResponse("C1", msg.split(",")[1]);
			}
		}else if("ruixue.wheatfield.pwdlock.update".equals(methodName)){
			if(!ValHasNoParam.hasParam(paramMap, "opertype")){
				return errorResponseService.getErrorResponse("P5","操作类型不能为空");
			}
			if (!ValHasNoParam.hasParam(paramMap, "passwordtype")) {
				return errorResponseService.getErrorResponse("P6", "密码类型不能为空");
			}
			if (opertype.equals("unlock") && opertype.equals("lockup")) {
				return errorResponseService.getErrorResponse("C1", "操作类型输入错误");
			}
			String msg=this.savePassword(accountPassword,opertype,user);
			if("ok".equals(msg)){
				response.setIs_success(true);
			}else{
				return errorResponseService.getErrorResponse("C2", msg);
			}
		}else if("ruixue.wheatfield.password.query".equals(methodName)){
	        if (!ValHasNoParam.hasParam(paramMap, "passwordtype")) {
	              return errorResponseService.getErrorResponse("P6", "密码类型不能为空");
	        }
	        //校验用户是否存在
	        List<FinanaceAccount> faList =accountManageService.getAllAccount(user, "oper");
	        if(faList != null && !faList.isEmpty()){
	              response.setIs_success(true);
	        }else{
	              return errorResponseService.getErrorResponse("C2", "账户信息校验失败");
	        }
		    String msg = this.queryPassword(accountPassword); 
	        if("ok".equals(msg)){
	              response.setIs_success(true);
	        }else{
	              return errorResponseService.getErrorResponse("C2", msg);
	        }
		}
		return response;
	}
	/**
	 * 新增修改密码
	 * @param newPassword  新密码，不为空的时候（修改），为空增加
	 */
	@Override
	public String savePassword(AccountPassword accountPassword,String opertype,User user) {
		logger.info("--------密码新增修改锁定解锁-------------");
		logger.info("--用户ID--:"+user.userId+"--机构号--："+user.constId+"--产品号--："+user.productId+"--用户名--"+user.userName);
		accountPassword.setPassword(SignUtils.getMD5Str(accountPassword.getPassword()));
		try {
			String result = accountManageService.checkAccount(user);
			if (!"ok".equals(result)) {
				return "该用户信息无效！";
			}
			if ("update".equals(opertype)) {
				String msgs = checkUser(accountPassword);
				if ("ok".equals(msgs.split(",")[0])) {
					accountPassword.setAcctPawdId(Integer.parseInt(msgs
							.split(",")[2]));
					accountPassword.setErrorCount(0);
					accountPasswordManager.saveAccountPassword(accountPassword);
				} else {
					if ("ng".equals(msgs.split(",")[0])) {
						return "用户密码已经被锁定";
					} else {
						return msgs.split(",")[1];
					}
				}
			} else if ("insert".equals(opertype)) {// 是否预设密码，允许连错次数暂时没有涉及
				String msguser = checkUser(accountPassword);
				if ("no".equals(msguser.split(",")[0])) {
					accountPassword.setStatusId(BaseConstants.PWD_STATUS_0);
					accountPassword.setErrorCount(0);
					accountPasswordManager.saveAccountPassword(accountPassword);
				} else {
					if ("ng".equals(msguser.split(",")[0])) {
						return "用户已经设置密码且密码被锁定！";
					} else {
						return msguser.split(",")[1];
					}
				}
			}
			if ("lockup".equals(opertype)) {
				String msgs = checkUser(accountPassword);
				String msg = msgs.split(",")[0];
				if ("ok".equals(msg)) {
					accountPassword.setAcctPawdId(Integer.parseInt(msgs
							.split(",")[2]));
					accountPassword.setStatusId(BaseConstants.PWD_STATUS_1);
					accountPasswordManager.saveAccountPassword(accountPassword);
				} else {
					return msgs.split(",")[1];
				}
			} else if ("unlock".equals(opertype)) {
				String msgs = checkUser(accountPassword);
				String msg = msgs.split(",")[0];
				if ("ng".equals(msg)) {
					accountPassword.setAcctPawdId(Integer.parseInt(msgs
							.split(",")[2]));
					accountPassword.setStatusId(BaseConstants.PWD_STATUS_0);
					accountPassword.setErrorCount(0);
					accountPasswordManager.saveAccountPassword(accountPassword);
				} else {
					if ("ok".equals(msg)) {
						return "密码已经是解锁状态！";
					} else {
						return msgs.split(",")[1];
					}
				}
			}
			return "ok";
		} catch(Exception e){
			logger.error(e.getMessage());
			return "密码相关操作失败！"+e.getMessage();
		}
	}
	@Override
	@Transactional
	public String checkPassword(AccountPassword accountPassword) {
		logger.info("------校验密码-----------");
		accountPassword.setPassword(SignUtils.getMD5Str(accountPassword.getPassword()));
		if (accountPassword.getPasswordType() == null || "".equals(accountPassword.getPasswordType())){
			accountPassword.setPasswordType("P");
		}
		AccountPasswordQuery query = new AccountPasswordQuery();
		query.setUserId(accountPassword.getUserId());
		query.setPassword(accountPassword.getPassword());
		query.setRootInstCd(accountPassword.getRootInstCd());
		query.setPasswordType(accountPassword.getPasswordType());
		List<AccountPassword> accountPasswordList = accountPasswordManager
				.queryList(query);
		if (accountPasswordList != null && accountPasswordList.size() > 0) {
			if (accountPasswordList.get(0).getStatusId() != null
					&& BaseConstants.PWD_STATUS_0.equals(accountPasswordList
							.get(0).getStatusId())) {
				AccountPassword aPassword = new AccountPassword();
				aPassword.setAcctPawdId(accountPasswordList.get(0)
						.getAcctPawdId());
				aPassword.setErrorCount(0);
				accountPasswordManager.saveAccountPassword(aPassword);
				return "ok";
			} else {
				return "no,密码为锁定状态！";
			}
		} else {
			AccountPasswordQuery aquery = new AccountPasswordQuery();
			aquery.setUserId(accountPassword.getUserId());
			aquery.setRootInstCd(accountPassword.getRootInstCd());
			aquery.setPasswordType(accountPassword.getPasswordType());
			List<AccountPassword> accountPList = accountPasswordManager
					.queryList(aquery);
			if (accountPList != null && accountPList.size() > 0) {
				AccountPassword accountP = new AccountPassword();
				accountP.setAcctPawdId(accountPList.get(0).getAcctPawdId());

				int errorCont = 0;
				if (accountPList.get(0).getErrorCount() != null) {
					errorCont = accountPList.get(0).getErrorCount();
				}
				accountP.setErrorCount(errorCont + 1);
				accountPasswordManager.saveAccountPassword(accountP);
				return "no,密码错误！密码当前错误次数为：" + accountP.getErrorCount();
			} else {
				return "no,用户没有设定此类型的密码！";
			}
		}
	}
	public String checkUser(AccountPassword accountPassword){
		try{
			AccountPasswordQuery query = new AccountPasswordQuery();
			query.setUserId(accountPassword.getUserId());
			query.setRootInstCd(accountPassword.getRootInstCd());
			query.setPasswordType(accountPassword.getPasswordType());
			List<AccountPassword> accountPList = accountPasswordManager.queryList(query);
			if (accountPList != null && accountPList.size() > 0) {
				AccountPassword accountp = accountPList.get(0);
				if (accountp.getStatusId() != null
						&& BaseConstants.PWD_STATUS_0.equals(accountp.getStatusId())) {
					return "ok,用户已经设置过密码！," + accountp.getAcctPawdId();
				} else {
					return "ng,用户密码已经被锁定！," + accountp.getAcctPawdId();
				}
			} else {
				return "no,用户暂无设置密码！";
			}
		}catch(Exception e){
			logger.error(e.getMessage());
			return "err,检验用户失败！";
		}
	}
    @Override
    public String queryPassword(AccountPassword accountPassword) {
        try{
            AccountPasswordQuery query = new AccountPasswordQuery();
            query.setUserId(accountPassword.getUserId());
            query.setRootInstCd(accountPassword.getRootInstCd());
            query.setPasswordType(accountPassword.getPasswordType());
            query.setStatusId("0");
            List<AccountPassword> accountPList = accountPasswordManager.queryList(query);
            if (accountPList != null && !accountPList.isEmpty()) {
                AccountPassword accountp = accountPList.get(0);
                if (accountp.getPassword() != null) {
                    return "ok";
                } else {
                    return "查询无密码！";
                }
            } else {
                return "查询无密码！";
            }
        }catch(Exception e){
            logger.error(e.getMessage());
            return "查询用户密码失败！";
        }
    }
}
