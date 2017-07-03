package com.rkylin.wheatfield.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.rkylin.wheatfield.api.AccountInfoDubboService;
import com.rkylin.wheatfield.dao.AccountInfoDao;
import com.rkylin.wheatfield.dao.FinanaceAccountDao;
import com.rkylin.wheatfield.dao.FinanaceCompanyDao;
import com.rkylin.wheatfield.dao.FinanacePersonDao;
import com.rkylin.wheatfield.model.BalanceDeResponse;
import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.model.FinAccountResponse;
import com.rkylin.wheatfield.pojo.AccountInfo;
import com.rkylin.wheatfield.pojo.AccountInfoQuery;
import com.rkylin.wheatfield.pojo.AccountPassword;
import com.rkylin.wheatfield.pojo.Balance;
import com.rkylin.wheatfield.pojo.BalanceDetail;
import com.rkylin.wheatfield.pojo.FinanaceAccount;
import com.rkylin.wheatfield.pojo.FinanaceCompany;
import com.rkylin.wheatfield.pojo.FinanaceCompanyQuery;
import com.rkylin.wheatfield.pojo.FinanaceEntry;
import com.rkylin.wheatfield.pojo.FinanacePerson;
import com.rkylin.wheatfield.pojo.FinanacePersonQuery;
import com.rkylin.wheatfield.service.AccountInfoService;
import com.rkylin.wheatfield.service.AccountManageService;
import com.rkylin.wheatfield.service.AccountPasswordService;
import com.rkylin.wheatfield.service.CheckInfoService;
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
	
	@Autowired
    @Qualifier("finanaceCompanyDao")
	private FinanaceCompanyDao finanaceCompanyDao;
	
	@Autowired
    @Qualifier("finanacePersonDao")
	private FinanacePersonDao finanacePersonDao;
	
	@Autowired
    @Qualifier("accountInfoDao")
	private AccountInfoDao accountInfoDao;
	
	@Autowired
	CheckInfoService checkInfoService;	
	
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
	 * 查询余额（dubbo）
	 * @param user
	 */
	public BalanceDeResponse getBalance(com.rkylin.wheatfield.bean.User user) {
		logger.info("查询余额传入参数user=="+user);
		BalanceDeResponse res = new BalanceDeResponse();
		if (user==null) {
			res.setCode(CodeEnum.ERR_PARAM_NULL.getCode());
			res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
			return res;
		}
		logger.info("查询余额传入参数所有字段值: "+BeanUtil.getBeanVal(user, new String[]{"userId","instCode","productId","name","cardNo"}));
		String field = BeanUtil.validateBeanProEmpty(user, new String[]{"userId","instCode"});
		logger.info("查询余额  传入的参数字段校验 是否有空值(field的值为是空值的字段,为null表示都不为空)  field="+field);
		if (field!=null) {
			res.setCode(CodeEnum.ERR_PARAM_NULL.getCode());
			res.setMsg(field+"不能为空");
			return res;
		}
		List<FinanaceAccount> finAccList = finanaceAccountDao.queryByInstCodeAndUser(user);
		logger.info("查询余额 查询账户信息 查出的数据个数="+finAccList.size());
		if (finAccList.size()==0) {
			res.setCode(CodeEnum.ERR_DATA_NO_RESULT.getCode());
			res.setMsg(CodeEnum.ERR_DATA_NO_RESULT.getMessage());
			return res;
		}
		List<BalanceDetail> balanceDeList = new ArrayList<BalanceDetail>();
		for (FinanaceAccount finanaceAccount : finAccList) {
			logger.info("查询账户信息 所有字段值: "+BeanUtil.getBeanVal(finanaceAccount, 
					new String[]{"finAccountId","rootInstCd","finAccountTypeId","accountRelateId","groupManage"}));
			Balance balance = checkInfoService.getBalance(new com.rkylin.wheatfield.pojo.User(),finanaceAccount.getFinAccountId());
			logger.info("  查询账户信息  balance="+balance);
			if (balance==null) {
				continue;
			}
			String name = null;
			FinanaceCompanyQuery companyQuery = new FinanaceCompanyQuery();
			companyQuery.setFinAccountId(finanaceAccount.getFinAccountId());
			List<FinanaceCompany> finCompanyList = finanaceCompanyDao.selectByExample(companyQuery);
			logger.info("  查询对公账户信息  finAccountId="+finanaceAccount.getFinAccountId()+"  查出的个数="+finCompanyList.size());
			if (finCompanyList.size()!=0) {
				logger.info("  查询对公账户信息  finAccountId="+finanaceAccount.getFinAccountId()+"  名称="+finCompanyList.get(0).getCompanyName());
				name = finCompanyList.get(0).getCompanyName();
				if (user.getName()!=null&&!"".equals(user.getName())&&!finCompanyList.get(0).getCompanyName().equals(user.getName())) {
					continue;
				}
			}else{
				FinanacePersonQuery finPersonQuery = new FinanacePersonQuery();
				finPersonQuery.setFinAccountId(finanaceAccount.getFinAccountId());
				List<FinanacePerson> finPersonList = finanacePersonDao.selectByExample(finPersonQuery);
				logger.info("  查询对私账户信息  finAccountId="+finanaceAccount.getFinAccountId()+"  查出的个数="+finPersonList.size());
				if (finPersonList.size()!=0) {
					logger.info("  查询对私账户信息  finAccountId="+finanaceAccount.getFinAccountId()+"  名称="+finPersonList.get(0).getPersonChnName());
					name = finPersonList.get(0).getPersonChnName();
					if (user.getName()!=null&&!"".equals(user.getName())&&!finPersonList.get(0).getPersonChnName().equals(user.getName())) {
						continue;
					}
				}
			}
			if (name==null) {
				continue;
			}
			if (user.getCardNo()!=null&&!"".equals(user.getCardNo())) {
				AccountInfoQuery accountInfoQuery = new AccountInfoQuery();
				accountInfoQuery.setFinAccountId(finanaceAccount.getFinAccountId());
				accountInfoQuery.setAccountNumber(user.getCardNo());
				List<AccountInfo> accList = accountInfoDao.selectByConLike(accountInfoQuery);	
				if (accList.size()==0) {
					continue;
				}
			}
			BalanceDetail balanceDetail = new BalanceDetail();
			balanceDetail.setBalance(balance);
			balanceDetail.setName(name);
			balanceDeList.add(balanceDetail);
		}
		logger.info("查询余额 查询账户信息  返回的数据个数="+balanceDeList.size());
		if (balanceDeList.size()==0) {
			res.setCode(CodeEnum.ERR_DATA_NO_RESULT.getCode());
			res.setMsg(CodeEnum.ERR_DATA_NO_RESULT.getMessage());
			return res;
		}
		res.setBalanceDeList(balanceDeList);
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