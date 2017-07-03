package com.rkylin.wheatfield.service.impl;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.rkylin.wheatfield.api.AccountInfoDubboService;
import com.rkylin.wheatfield.bean.User;
import com.rkylin.wheatfield.dao.FinanaceAccountDao;
import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.model.FinAccountResponse;
import com.rkylin.wheatfield.pojo.AccountPassword;
import com.rkylin.wheatfield.pojo.FinanaceAccount;
import com.rkylin.wheatfield.pojo.FinanaceEntry;
import com.rkylin.wheatfield.service.AccountInfoService;
import com.rkylin.wheatfield.service.AccountManageService;
import com.rkylin.wheatfield.service.AccountPasswordService;
import com.rkylin.wheatfield.utils.BeanUtil;
import com.rkylin.wheatfield.utils.CodeEnum;

@Service("accountInfoService")
public class AccountInfoServiceImpl implements AccountInfoService,AccountInfoDubboService{

	private static Logger logger = LoggerFactory.getLogger(AccountInfoServiceImpl.class);
	
	@Autowired
    @Qualifier("accountPasswordService")
	private AccountPasswordService accountPasswordService;
	
	@Autowired
    @Qualifier("accountManageService")
	private AccountManageService accountManageService;
	
	@Autowired
    @Qualifier("finanaceAccountDao")
	private FinanaceAccountDao finanaceAccountDao;
	
	/**
	 *  查询账户信息
	 */
	@Override
	public FinAccountResponse getFinAccount(com.rkylin.wheatfield.bean.User user) {
		logger.info("查询账户信息 入参 user="+user);
		FinAccountResponse res =new FinAccountResponse();
		if (user==null) {
			res.setCode(CodeEnum.ERR_PARAM_NULL.getCode());
			res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
			return res;
		}
		if (user.getType()==null||user.getType().length==0) {
			user.setType(null);
		}
		logger.info("查询账户信息 所有字段值:userId="+user.getUserId()+",instCode="+user.getInstCode()+","
				+ "productId="+user.getProductId()+"type="+Arrays.toString(user.getType()));
		if (user.getUserId()==null||"".equals(user.getUserId())||user.getInstCode()==null||"".equals(user.getInstCode())) {
			res.setCode(CodeEnum.ERR_PARAM_NULL.getCode());
			res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
			return res;
		}
		List<FinanaceAccount> list = finanaceAccountDao.queryByInstCodeAndUser(user);
		logger.info("查询账户信息 查出的数据个数="+list.size());
		if (list.size()==0) {
			res.setCode(CodeEnum.ERR_DATA_NO_RESULT.getCode());
			res.setMsg(CodeEnum.ERR_DATA_NO_RESULT.getMessage());
			return res;
		}
		res.setFinAccList(list);
		return res;
	}
	
	/**
	 * 用户密码校验
	 * @param user
	 * @return
	 */
	public CommonResponse passwordCheck(com.rkylin.wheatfield.bean.User user) {
		logger.info("用户密码校验 入参 user="+user);
		CommonResponse res =new CommonResponse();
		if (user==null) {
			res.setCode(CodeEnum.ERR_PARAM_NULL.getCode());
			res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
			return res;
		}
		logger.info("用户密码校验 入参 所有值="+BeanUtil.getBeanVal(user, null));
		AccountPassword accountPassword = new AccountPassword();
		accountPassword.setUserId(user.getUserId());
		accountPassword.setRootInstCd(user.getInstCode());
		accountPassword.setPassword(user.getPassword());
		accountPassword.setPasswordType(user.getOperType());
		String result = accountPasswordService.checkPassword(accountPassword);
		logger.info("用户密码校验  userId="+user.getUserId()+"    result="+result);
		if (!"ok".equals(result)) { 
			res.setCode(CodeEnum.FAILURE.getCode());
			res.setMsg(result);
			return res;
		}
		return res;
	}
	
	@Override
	public CommonResponse openSubAccount(com.rkylin.wheatfield.pojo.User user, FinanaceEntry finanaceEntry) {
		logger.info("开设子账户  参数   user="+user+",finanaceEntry="+finanaceEntry);
		CommonResponse res = new CommonResponse();
		if (user==null) {
			res.setCode(CodeEnum.ERR_PARAM_NULL.getCode());
			res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
			return res;
		}
		String result = accountManageService.openAccount(user, finanaceEntry);
		if (!"ok".equals(result)) {
			res.setCode(CodeEnum.FAILURE.getCode());
			res.setMsg(result);
			return res;
		}
		return res;
	}


}
