/**
*
*/
package com.rkylin.wheatfield.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rkylin.common.RedisIdGenerator;
import com.rkylin.util.bean.BeanMapper;
import com.rkylin.wheatfield.api.FinanaceAccountService;
import com.rkylin.wheatfield.api.FinanaceCompanyApi;
import com.rkylin.wheatfield.api.FinanacePersonApi;
import com.rkylin.wheatfield.bean.OpenAccountCompany;
import com.rkylin.wheatfield.bean.OpenAccountPerson;
import com.rkylin.wheatfield.common.DateUtils;
import com.rkylin.wheatfield.constant.AccountConstants;
import com.rkylin.wheatfield.constant.BaseConstants;
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.constant.RedisConstants;
import com.rkylin.wheatfield.dao.FinanaceAccountDao;
import com.rkylin.wheatfield.dao.FinanaceCompanyDao;
import com.rkylin.wheatfield.dao.FinanacePersonDao;
import com.rkylin.wheatfield.manager.FinanaceCompanyManager;
import com.rkylin.wheatfield.manager.FinanacePersonManager;
import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.model.FinCompanyResponse;
import com.rkylin.wheatfield.model.FinPersonResponse;
import com.rkylin.wheatfield.model.FinanaceAccountResponse;
import com.rkylin.wheatfield.pojo.FinanaceAccount;
import com.rkylin.wheatfield.pojo.FinanaceAccountQuery;
import com.rkylin.wheatfield.pojo.FinanaceCompany;
import com.rkylin.wheatfield.pojo.FinanaceCompanyQuery;
import com.rkylin.wheatfield.pojo.FinanacePerson;
import com.rkylin.wheatfield.pojo.FinanacePersonQuery;
import com.rkylin.wheatfield.service.AccountManageService;
import com.rkylin.wheatfield.service.ParameterInfoService;
import com.rkylin.wheatfield.utils.BeanUtil;
import com.rkylin.wheatfield.utils.CodeEnum;
import com.rkylin.wheatfield.utils.CommUtil;


/**
 * Description:	   
 * Date:          2015年12月8日 下午2:04:39 
 * @author        sun guoxing
 * @version       1.0 
 */
public class FinanaceAccountServiceImpl implements FinanaceAccountService,FinanacePersonApi,FinanaceCompanyApi{
    
    private static Logger logger = LoggerFactory.getLogger(FinanaceAccountServiceImpl.class);
    
	@Autowired
	private FinanaceAccountDao finanaceAccountDao;
	
	@Autowired
	@Qualifier("finanacePersonDao")
	private FinanacePersonDao finanacePersonDao;
	
//	@Autowired
//	private FinanacePersonManager finanacePersonManager;
	
	@Autowired
    @Qualifier("finanaceCompanyDao")
    private FinanaceCompanyDao finanaceCompanyDao;
	
	@Autowired
	private ParameterInfoService parameterInfoService;
	
	@Autowired
    @Qualifier("accountManageService")
	private AccountManageService accountManageService;
	
	@Autowired
	private RedisIdGenerator redisIdGenerator;
	
	/**
	 * 修改账户状态
	 * @param userId 用户id
	 * @param instCode 机构号
	 * @param productId 产品号
	 * @param status 状态值
	 * @return
	 */
	public CommonResponse updateFinAccountStatus(String userId,String instCode,String productId,String status){
		logger.info("修改账户状态：userId=" + userId + ",instCode=" + instCode+",productId="+productId+",status="+status);
		CommonResponse res = new CommonResponse();
		if (userId==null||"".equals(userId.trim())||instCode==null||"".equals(instCode.trim())||productId==null||"".equals(productId.trim())||status==null||"".equals(status.trim())) {
			res.setCode(CodeEnum.ERR_PARAM_NULL.getCode());
			res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
			return res;
		}
		FinanaceAccountQuery query = new FinanaceAccountQuery();
		query.setAccountRelateId(userId);
		query.setRootInstCd(instCode);
		query.setGroupManage(productId);
		List<FinanaceAccount> finAccList = finanaceAccountDao.selectByExample(query);
		logger.info("修改账户状态  查出的资金账户信息个数="+finAccList.size());
		if (finAccList.size()==0) {
			res.setCode(CodeEnum.FAILURE.getCode());
			res.setMsg("没有查出资金账户信息");
			return res;
		}
		FinanaceAccount finAccount = finAccList.get(0);
		finAccount.setStatusId(status);
		try {
			finanaceAccountDao.updateByPrimaryKey(finAccount);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("修改账户状态  修改数据库异常");
			res.setCode(CodeEnum.FAILURE.getCode());
			res.setMsg("修改数据库异常，请检查传入的状态值");
			return res;
		}
		return res;
	}
	/**
	 * 
	* @Description:根据id修改账户状态
	*
	* @param finAccountId inanaceAccount表的id
	* @param status 要更改成的状态
	* @return
	 */
	@Override
	public int updateStatusById(String finAccountId, String status){
		logger.info("----------------------------------根据id修改账户状态开始，入参：finAccountId=" + finAccountId + ",status=" + status);
		
		if(null == finAccountId || "".equals(finAccountId)){
			logger.info("finAccountId不能为空");
			return -1;
		}
		if(null == status || "".equals(status)){
			logger.info("status不能为空");
			return -1;
		}
		FinanaceAccount fa = new FinanaceAccount();
		fa.setFinAccountId(finAccountId);
		fa.setStatusId(status);
		
		logger.info("----------------------------------根据id修改账户状态结束");
		return finanaceAccountDao.updateByPrimaryKeySelective(fa);
		
	}
	
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public CommonResponse accountoprPerRealNameOpenByDubbo(OpenAccountPerson openAccountPerson) {
        logger.info("个人开户入参:"+BeanUtil.getBeanVal(openAccountPerson, null));
        CommonResponse res =new CommonResponse();
        res.setCode(CodeEnum.FAILURE.getCode());
        if (openAccountPerson==null) {
            logger.info("参数为空!");
            res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
            return res;
        }
        String field = BeanUtil.validateBeanProEmpty(openAccountPerson, new String[]{"rootInstCd","userId","productId","accountCode"});
        if (field!=null) {
            logger.info(field+"不能为空!");
            res.setMsg(field+"不能为空!");
            return res;
        }
        if (!CommUtil.isEmp(openAccountPerson.getCertificateType()) && CommUtil.isEmp(openAccountPerson.getCertificateNumber())) {
            logger.info("证件类型不为空时,证件号不能为空!");
            res.setMsg("证件类型不为空时,证件号不能为空!");
            return res; 
        }
        if (CommUtil.isEmp(openAccountPerson.getCertificateType()) && !CommUtil.isEmp(openAccountPerson.getCertificateNumber())) {
            logger.info("证件号不为空时,证件类型不能为空!");
            res.setMsg("证件号不为空时,证件类型不能为空!");
            return res; 
        }        
        FinanacePersonQuery finPersonQuery = new FinanacePersonQuery();
        finPersonQuery.setCertificateNumber(openAccountPerson.getCertificateNumber());
        finPersonQuery.setCertificateType(openAccountPerson.getCertificateType());
        finPersonQuery.setRootInstCd(openAccountPerson.getRootInstCd());
        //如果是实名认证开户
        if (Constants.REAL_NAME.equals(openAccountPerson.getWhetherRealName())) {
            if (CommUtil.isEmp(openAccountPerson.getCertificateType()) || CommUtil.isEmp(openAccountPerson.getCertificateNumber())) {
                logger.info("实名认证时,证件类型和证件号码不能为空!");
                res.setMsg("实名认证时,证件类型和证件号码不能为空!");
                return res; 
            }
        }
        Set<String> instSet = parameterInfoService.getParaValSetByParamCode(RedisConstants.OPEN_ACCOUNT_INST_VERIFY);
        logger.info("开户时需要校验证件+手机唯一的机构:"+instSet);
        if (instSet.contains(openAccountPerson.getRootInstCd())) {
            if (CommUtil.isEmp(openAccountPerson.getMobileTel())){
                logger.info("此机构开户账户唯一性校验时,手机号不能为空!");
                res.setMsg("此机构开户账户唯一性校验时,手机号不能为空!");
                return res;  
            }
            List<FinanacePerson> finPersonCerList = finanacePersonDao.selectByExample(finPersonQuery);
            for (FinanacePerson finanacePerson : finPersonCerList) {
                if (openAccountPerson.getMobileTel().equals(finanacePerson.getMobileTel())) {
                    logger.info("此手机号已使用,请更换!");
                    res.setMsg("此手机号已使用,请更换!");
                    return res;    
                }
            }  
            finPersonQuery.setMobileTel(openAccountPerson.getMobileTel());
        }
        if (!CommUtil.isEmp(openAccountPerson.getCertificateNumber())) {
            List<FinanacePerson> certificateList = finanacePersonDao.selectByRootInstCdOrNumOrStatusId(finPersonQuery);
            if (certificateList.size()!=0){
                logger.info("此证件已经被注册过！");
                res.setMsg("此证件已经被注册过！");
                return res;
            }            
        }           
        FinanaceAccount finanaceAccount = new FinanaceAccount();
        finanaceAccount.setRootInstCd(openAccountPerson.getRootInstCd());
        finanaceAccount.setFinAccountName(openAccountPerson.getUserName());
        finanaceAccount.setAccountRelateId(openAccountPerson.getUserId());
        finanaceAccount.setAccountCode(openAccountPerson.getAccountCode());
        finanaceAccount.setGroupManage(openAccountPerson.getProductId());
        finanaceAccount.setReferUserId(openAccountPerson.getReferUserId());
        finanaceAccount.setRemark(openAccountPerson.getRemark());
        FinanaceAccountResponse finAccRes = openFinAccount(finanaceAccount);
        if (!CodeEnum.SUCCESS.getCode().equals(finAccRes.getCode())) {
            logger.info(finAccRes.getMsg());
            res.setMsg(finAccRes.getMsg());
            return res;  
        }
        FinanacePerson finanacePerson = new FinanacePerson();
        BeanMapper.copy(openAccountPerson, finanacePerson);
        finanacePerson.setStatusId(AccountConstants.PERSON_STATUS_ON);
        finanacePerson.setPersonCode(redisIdGenerator.createPersonCode());
        finanacePerson.setFinAccountId(finAccRes.getFinanaceAccount().getFinAccountId());
        finanacePersonDao.insertSelective(finanacePerson);
        res.setCode(CodeEnum.SUCCESS.getCode());
        return res;
    }

    private FinanaceAccountResponse openFinAccount(FinanaceAccount finanaceAccount){
        FinanaceAccountQuery query = new FinanaceAccountQuery();
        query.setRootInstCd(finanaceAccount.getRootInstCd());
        query.setAccountRelateId(finanaceAccount.getAccountRelateId());
        query.setFinAccountTypeId(AccountConstants.ACCOUNT_TYPE_BASE);
        List<FinanaceAccount> list = finanaceAccountDao.selectByExample(query);
        FinanaceAccountResponse res =new FinanaceAccountResponse();
        res.setCode(CodeEnum.FAILURE.getCode());
        if (list.size()!=0) {
            logger.info("该用户已创建主账户!");
            res.setMsg("该用户已创建主账户!");
            return res;  
        }
        finanaceAccount.setFinAccountId(redisIdGenerator.accountEntryNo());
        finanaceAccount.setStatusId(BaseConstants.ACCOUNT_STATUS_OK);
        finanaceAccount.setAmount(Long.parseLong(BaseConstants.INITIAL_AMOUNT));
        finanaceAccount.setBalanceSettle(Long
                .parseLong(BaseConstants.INITIAL_AMOUNT));
        finanaceAccount.setBalanceFrozon(Long
                .parseLong(BaseConstants.INITIAL_AMOUNT));
        finanaceAccount.setBalanceOverLimit(Long
                .parseLong(BaseConstants.INITIAL_AMOUNT));
        finanaceAccount.setFinAccountTypeId(AccountConstants.ACCOUNT_TYPE_BASE);
        finanaceAccount.setGroupSettle(AccountConstants.GROUP_SETTLE_BASE);
        finanaceAccount.setCurrency(BaseConstants.CURRENCY_CNY);
        finanaceAccount.setStartTime(DateUtils.getSysDate(Constants.DATE_FORMAT_YYYYMMDD));
        finanaceAccount.setBussControl(BaseConstants.BUSS_CONTRAL_TOTAL);
        finanaceAccountDao.insertSelective(finanaceAccount);
        res.setFinanaceAccount(finanaceAccount);
        res.setCode(CodeEnum.SUCCESS.getCode());
        return res;
    }
    
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public CommonResponse accountoprPerRealNameUpByDubbo(OpenAccountPerson openAccountPerson) {
        logger.info("个人户更新入参:"+BeanUtil.getBeanVal(openAccountPerson, null));
        CommonResponse res =new CommonResponse();
        res.setCode(CodeEnum.FAILURE.getCode());
        if (openAccountPerson==null) {
            logger.info("参数为空!");
            res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
            return res;
        }
        String field = BeanUtil.validateBeanProEmpty(openAccountPerson, new String[]{"rootInstCd","userId","productId"});
        if (field!=null) {
            logger.info(field+"不能为空!");
            res.setMsg(field+"不能为空!");
            return res;
        }
        if (!CommUtil.isEmp(openAccountPerson.getCertificateType()) && CommUtil.isEmp(openAccountPerson.getCertificateNumber())) {
            logger.info("证件类型不为空时,证件号不能为空!");
            res.setMsg("证件类型不为空时,证件号不能为空!");
            return res; 
        }
        if (CommUtil.isEmp(openAccountPerson.getCertificateType()) && !CommUtil.isEmp(openAccountPerson.getCertificateNumber())) {
            logger.info("证件号不为空时,证件类型不能为空!");
            res.setMsg("证件号不为空时,证件类型不能为空!");
            return res; 
        } 
        FinanacePersonQuery finPersonQuery = new FinanacePersonQuery();
        finPersonQuery.setRootInstCd(openAccountPerson.getRootInstCd());
        finPersonQuery.setUserId(openAccountPerson.getUserId());
        finPersonQuery.setDataIndex(0);
        finPersonQuery.setDataCount(1);
        List<FinanacePerson> finPersonList = finanacePersonDao.selectPersonAccInfo(finPersonQuery);
        if (finPersonList.size()==0) {
            logger.info("没有查到账户信息!");
            res.setMsg("没有查到账户信息!");
            return res;  
        }
        if (Constants.REAL_NAME.equals(finPersonList.get(0).getWhetherRealName())) {
            if (openAccountPerson.getCertificateType()!=null || openAccountPerson.getCertificateNumber()!=null) {
                logger.info("账户为实名认证账户时,不能修改证件信息!");
                res.setMsg("账户为实名认证账户时,不能修改证件信息!");
                return res; 
            }
        }
        finPersonQuery.setUserId(null);
        finPersonQuery.setCertificateNumber(openAccountPerson.getCertificateNumber());
        finPersonQuery.setCertificateType(openAccountPerson.getCertificateType());
        finPersonQuery.setStatusId(AccountConstants.PERSON_STATUS_ON);
        finPersonQuery.setRootInstCd(openAccountPerson.getRootInstCd());
        //如果要修改为实名认证账户
        if (Constants.REAL_NAME.equals(openAccountPerson.getWhetherRealName()) && Constants.NOT_REAL_NAME.equals(finPersonList.get(0).getWhetherRealName())) {
            if (CommUtil.isEmp(openAccountPerson.getCertificateType()) || CommUtil.isEmp(openAccountPerson.getCertificateNumber())) {
                logger.info("实名认证时,证件类型和证件号码不能为空!");
                res.setMsg("实名认证时,证件类型和证件号码不能为空!");
                return res; 
            }
        }
        Set<String> instSet = parameterInfoService.getParaValSetByParamCode(RedisConstants.OPEN_ACCOUNT_INST_VERIFY);
        logger.info("开户时需要校验证件+手机唯一的机构:"+instSet);
        if (instSet.size()!=0 && instSet.contains(openAccountPerson.getRootInstCd())) {
            if (CommUtil.isEmp(openAccountPerson.getMobileTel())){
                logger.info("此机构开户账户唯一性校验时,手机号不能为空!");
                res.setMsg("此机构开户账户唯一性校验时,手机号不能为空!");
                return res;  
            }
            List<FinanacePerson> finPersonCerList = finanacePersonDao.selectByExample(finPersonQuery);
            for (FinanacePerson finanacePerson : finPersonCerList) {
                if (finPersonCerList.get(0).getFinAccountId().equals(finanacePerson.getFinAccountId())) {
                    continue;
                }
                if (openAccountPerson.getMobileTel().equals(finanacePerson.getMobileTel())) {
                    logger.info("此手机号已使用,请更换!");
                    res.setMsg("此手机号已使用,请更换!");
                    return res;    
                }
            }  
            finPersonQuery.setMobileTel(openAccountPerson.getMobileTel());
        }
        if (!CommUtil.isEmp(openAccountPerson.getCertificateNumber())) {
            List<FinanacePerson> certificateList = finanacePersonDao.selectByRootInstCdOrNumOrStatusId(finPersonQuery);
            if (certificateList.size()!=0 && !certificateList.get(0).getFinAccountId().equals(finPersonList.get(0).getFinAccountId())){
                logger.info("此证件已经被注册过！");
                res.setMsg("此证件已经被注册过！");
                return res;
            }            
        }        
        Integer personId = finPersonList.get(0).getPersonId();
        FinanacePerson finanacePerson = new FinanacePerson();
        BeanMapper.copy(openAccountPerson, finanacePerson);
        finanacePerson.setPersonId(personId);
        finanacePersonDao.updateByPrimaryKeySelective(finanacePerson);
        if (!CommUtil.isEmp(openAccountPerson.getReferUserId())||!CommUtil.isEmp(openAccountPerson.getUserName())
                ||!CommUtil.isEmp(openAccountPerson.getAccountCode())||!CommUtil.isEmp(openAccountPerson.getRemark())) {
            FinanaceAccount finanaceAccount = new FinanaceAccount();
            finanaceAccount.setFinAccountId(finPersonList.get(0).getFinAccountId());
            finanaceAccount.setAccountCode(openAccountPerson.getAccountCode());
            finanaceAccount.setReferUserId(openAccountPerson.getReferUserId());
            finanaceAccount.setFinAccountName(openAccountPerson.getUserName());
            finanaceAccount.setRemark(openAccountPerson.getRemark());
            finanaceAccountDao.updateByPrimaryKeySelective(finanaceAccount); 
        }
        
        res.setCode(CodeEnum.SUCCESS.getCode());
        return res;
    }
    
    @Override
    public FinPersonResponse accountoprPerRealNameQueryBasicByDubbo(OpenAccountPerson openAccountPerson) {
        logger.info("个人户基本信息查询入参:"+BeanUtil.getBeanVal(openAccountPerson, null));
        FinPersonResponse res =new FinPersonResponse();
        res.setCode(CodeEnum.FAILURE.getCode());
        if (openAccountPerson==null) {
            logger.info("参数为空!");
            res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
            return res;
        }
        if (CommUtil.isEmp(openAccountPerson.getAccountCode())){
            String field = BeanUtil.validateBeanProEmpty(openAccountPerson, new String[]{"rootInstCd","userId"});
            if (field!=null) {
                logger.info(field+"不能为空!");
                res.setMsg(field+"不能为空!");
                return res;
            }   
        }
        FinanacePersonQuery finPersonQuery = new FinanacePersonQuery();
        BeanMapper.copy(openAccountPerson, finPersonQuery);
        if (openAccountPerson.getStartTime()==null && openAccountPerson.getEndTime()==null && CommUtil.isEmp(openAccountPerson.getAccountCode())) {
            finPersonQuery.setCreatedTimeFrom(DateUtils.getTheDayBefOrAfter(Constants.DATE_FORMAT_YYYYMMDD, 0));
            finPersonQuery.setCreatedTimeTo(DateUtils.getTheDayBefOrAfter(Constants.DATE_FORMAT_YYYYMMDD, 1));
        }
        if (openAccountPerson.getStartTime()!=null && openAccountPerson.getEndTime()!=null) {
            if (openAccountPerson.getStartTime().getTime()>=openAccountPerson.getEndTime().getTime()) {
                logger.info("时间范围有误,请检查!");
                res.setMsg("时间范围有误,请检查!");
                return res;
            }
            if (openAccountPerson.getEndTime().getTime()-openAccountPerson.getStartTime().getTime()>7*24*60*60*1000) {
                logger.info("查询范围不能超过7天!");
                res.setMsg("查询范围不能超过7天!");
                return res;
            }
            finPersonQuery.setCreatedTimeFrom(openAccountPerson.getStartTime());
            finPersonQuery.setCreatedTimeTo(openAccountPerson.getEndTime());
        }
        finPersonQuery.setFinAccountName(openAccountPerson.getUserName());
        if (openAccountPerson.getPageSize()==null ||openAccountPerson.getPageSize()==0) {
            finPersonQuery.setDataCount(50);
        } else{
            finPersonQuery.setDataCount(openAccountPerson.getPageSize());
        }
        if (openAccountPerson.getPageNum()==null || openAccountPerson.getPageNum()==0 || openAccountPerson.getPageNum()==1) {
            finPersonQuery.setDataIndex(0);
        }else{
            finPersonQuery.setDataIndex((openAccountPerson.getPageNum()-1)*openAccountPerson.getPageSize());
        }       
        List<FinanacePerson> finPersonList = finanacePersonDao.selectPersonAccInfo(finPersonQuery);
        if (finPersonList.size()==0) {
            logger.info("没有查到账户信息!");
            res.setMsg("没有查到账户信息!");
            return res;  
        }  
        List<OpenAccountPerson> accountPersons = new ArrayList<OpenAccountPerson>();
        for (FinanacePerson finanacePerson: finPersonList) {
            OpenAccountPerson openAccPerson = new OpenAccountPerson();
            BeanMapper.copy(finanacePerson, openAccPerson);
            openAccPerson.setUserName(finanacePerson.getFinAccountName());
            accountPersons.add(openAccPerson);
        }
        res.setAccountPersons(accountPersons);
        res.setTotalCount(finPersonList.size());
        res.setCode(CodeEnum.SUCCESS.getCode());
        return res;
    }
    
    @Override
    public CommonResponse accountoprComRealNameOpenByDubbo(OpenAccountCompany openAccountCompany) {
        logger.info("企业开户入参:"+BeanUtil.getBeanVal(openAccountCompany, null));
        CommonResponse res =new CommonResponse();
        res.setCode(CodeEnum.FAILURE.getCode());
        if (openAccountCompany==null) {
            logger.info("参数为空!");
            res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
            return res;
        }
        String field = BeanUtil.validateBeanProEmpty(openAccountCompany,
                new String[]{"companyName","buslince","productId","userId","rootInstCd","userName","accountCode"});
        if (field!=null) {
            logger.info(field+"不能为空!");
            res.setMsg(field+"不能为空!");
            return res;
        }
        FinanaceCompanyQuery buslinceQuery = new FinanaceCompanyQuery();
        buslinceQuery.setBuslince(openAccountCompany.getBuslince());
        buslinceQuery.setRootInstCd(openAccountCompany.getRootInstCd());
        //如果是实名认证开户
        Set<String> instSet = parameterInfoService.getParaValSetByParamCode(RedisConstants.OPEN_ACCOUNT_INST_VERIFY);
        logger.info("开户时需要校验证件+手机唯一的机构:"+instSet);
        if (instSet.contains(openAccountCompany.getRootInstCd())) {
            if (CommUtil.isEmp(openAccountCompany.getConnect())){
                logger.info("此机构开户账户唯一性校验时,联系方式不能为空!");
                res.setMsg("此机构开户账户唯一性校验时,联系方式不能为空!");
                return res;  
            }
            List<FinanaceCompany> finCompanyBusList = finanaceCompanyDao.selectByExample(buslinceQuery);
            for (FinanaceCompany finanaceCompany : finCompanyBusList) {
                if (openAccountCompany.getConnect().equals(finanaceCompany.getConnect())) {
                    logger.info("此营业执照/手机号已经被注册过！");
                    res.setMsg("此营业执照/手机号已经被注册过！");
                    return res;    
                }
            }   
            buslinceQuery.setConnect(openAccountCompany.getConnect());
        }
        List<FinanaceCompany> certificateList = finanaceCompanyDao.selectByRootInstCdOrBUSLINCEOrStatusId(buslinceQuery);
        if (certificateList.size()!=0){
            logger.info("此营业执照已经被注册过！");
            res.setMsg("此营业执照已经被注册过！");
            return res;
        }     
        FinanaceAccount finanaceAccount = new FinanaceAccount();
        finanaceAccount.setRootInstCd(openAccountCompany.getRootInstCd());
        finanaceAccount.setFinAccountName(openAccountCompany.getUserName());
        finanaceAccount.setAccountRelateId(openAccountCompany.getUserId());
        finanaceAccount.setAccountCode(openAccountCompany.getAccountCode());
        finanaceAccount.setGroupManage(openAccountCompany.getProductId());
        finanaceAccount.setRemark(openAccountCompany.getRemark());
        finanaceAccount.setReferUserId(openAccountCompany.getReferUserId());
        FinanaceAccountResponse finAccRes = openFinAccount(finanaceAccount);
        if (!CodeEnum.SUCCESS.getCode().equals(finAccRes.getCode())) {
            logger.info(finAccRes.getMsg());
            res.setMsg(finAccRes.getMsg());
            return res;  
        }
        FinanaceCompany finanaceCompany = new FinanaceCompany();
        BeanMapper.copy(openAccountCompany, finanaceCompany);
        finanaceCompany.setStatusId(AccountConstants.PERSON_STATUS_ON);
        finanaceCompany.setFinAccountId(finAccRes.getFinanaceAccount().getFinAccountId());
        if (CommUtil.isEmp(openAccountCompany.getCompanyType())) {
            finanaceCompany.setCompanyType(Integer.valueOf(Constants.BASE_FINANACECOMPANY_TYPE));
        }
        finanaceCompanyDao.insertSelective(finanaceCompany);
        res.setCode(CodeEnum.SUCCESS.getCode());
        return res;
    }
    
    @Override
    public CommonResponse accountoprComRealNameUpByDubbo(OpenAccountCompany openAccountCompany) {
        logger.info("企业户更新入参:"+BeanUtil.getBeanVal(openAccountCompany, null));
        CommonResponse res =new CommonResponse();
        res.setCode(CodeEnum.FAILURE.getCode());
        if (openAccountCompany==null) {
            logger.info("参数为空!");
            res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
            return res;
        }
        String field = BeanUtil.validateBeanProEmpty(openAccountCompany, new String[]{"rootInstCd","userId","productId"});
        if (field!=null) {
            logger.info(field+"不能为空!");
            res.setMsg(field+"不能为空!");
            return res;
        }
        if ("".equals(openAccountCompany.getBuslince())) {
            logger.info("营业执照非法!");
            res.setMsg("营业执照非法!");
            return res;
        }
        FinanaceCompanyQuery finComQuery = new FinanaceCompanyQuery();
        finComQuery.setRootInstCd(openAccountCompany.getRootInstCd());
        finComQuery.setAccountRelateId(openAccountCompany.getUserId());
        finComQuery.setDataIndex(0);
        finComQuery.setDataCount(1);
        List<FinanaceCompany> finCompanyList = finanaceCompanyDao.selectCompanyAccInfo(finComQuery);
        if (finCompanyList.size()==0) {
            logger.info("没有查到账户信息!");
            res.setMsg("没有查到账户信息!");
            return res;  
        }
        finComQuery.setAccountRelateId(null);
        Set<String> instSet = parameterInfoService.getParaValSetByParamCode(RedisConstants.OPEN_ACCOUNT_INST_VERIFY);
        logger.info("开户时需要校验证件+手机唯一的机构:"+instSet);
        if (instSet.contains(openAccountCompany.getRootInstCd())) {
            if (CommUtil.isEmp(openAccountCompany.getConnect())){
                logger.info("此机构开户账户唯一性校验时,联系方式不能为空!");
                res.setMsg("此机构开户账户唯一性校验时,联系方式不能为空!");
                return res;  
            }
            finComQuery.setBuslince(finCompanyList.get(0).getBuslince());
            List<FinanaceCompany> finCompanyBusList = finanaceCompanyDao.selectByExample(finComQuery);
            for (FinanaceCompany finanaceCompany : finCompanyBusList) {
                if (finCompanyList.get(0).getFinAccountId().equals(finanaceCompany.getFinAccountId())) {
                    continue;
                }
                if (openAccountCompany.getConnect().equals(finanaceCompany.getConnect())) {
                    logger.info("此手机号已使用,请更换!");
                    res.setMsg("此手机号已使用,请更换!");
                    return res;    
                }
            }  
            finComQuery.setConnect(openAccountCompany.getConnect());
        }
        if (!CommUtil.isEmp(openAccountCompany.getBuslince())) {
            finComQuery.setBuslince(openAccountCompany.getBuslince());
            List<FinanaceCompany> certificateList = finanaceCompanyDao.selectByRootInstCdOrBUSLINCEOrStatusId(finComQuery);
            if (certificateList.size()!=0 && !certificateList.get(0).getFinAccountId().equals(finCompanyList.get(0).getFinAccountId())){
                logger.info("此营业执照号已经被注册过！");
                res.setMsg("此营业执照号已经被注册过！");
                return res;
            }              
        }
        Integer companyId = finCompanyList.get(0).getCompanyId();
        FinanaceCompany finanaceCompany = new FinanaceCompany();
        BeanMapper.copy(openAccountCompany, finanaceCompany);
        finanaceCompany.setCompanyId(companyId);
        if (CommUtil.isEmp(finanaceCompany.getWhetherRealName())) {
            finanaceCompany.setWhetherRealName(null);
        }
        finanaceCompanyDao.updateByPrimaryKeySelective(finanaceCompany);
        if (!CommUtil.isEmp(openAccountCompany.getReferUserId())||!CommUtil.isEmp(openAccountCompany.getUserName())
                ||!CommUtil.isEmp(openAccountCompany.getAccountCode())||!CommUtil.isEmp(openAccountCompany.getRemark())) {
            FinanaceAccount finanaceAccount = new FinanaceAccount();
            finanaceAccount.setFinAccountId(finCompanyList.get(0).getFinAccountId());
            finanaceAccount.setAccountCode(openAccountCompany.getAccountCode());
            finanaceAccount.setReferUserId(openAccountCompany.getReferUserId());
            finanaceAccount.setFinAccountName(openAccountCompany.getUserName());
            finanaceAccount.setRemark(openAccountCompany.getRemark());
            finanaceAccountDao.updateByPrimaryKeySelective(finanaceAccount); 
        }
        res.setCode(CodeEnum.SUCCESS.getCode());
        return res;
    }
    
    @Override
    public FinCompanyResponse accountoprComRealNameQueryBasicByDubbo(OpenAccountCompany openAccountCompany) {
        logger.info("个人户基本信息查询入参:"+BeanUtil.getBeanVal(openAccountCompany, null));
        FinCompanyResponse res =new FinCompanyResponse();
        res.setCode(CodeEnum.FAILURE.getCode());
        if (openAccountCompany==null) {
            logger.info("参数为空!");
            res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
            return res;
        }
        if (CommUtil.isEmp(openAccountCompany.getAccountCode())){
            String field = BeanUtil.validateBeanProEmpty(openAccountCompany, new String[]{"rootInstCd","userId"});
            if (field!=null) {
                logger.info(field+"不能为空!");
                res.setMsg(field+"不能为空!");
                return res;
            }            
        }
        FinanaceCompanyQuery finComQuery = new FinanaceCompanyQuery();
        BeanMapper.copy(openAccountCompany, finComQuery);
        if (openAccountCompany.getStartTime()==null && openAccountCompany.getEndTime()==null && CommUtil.isEmp(openAccountCompany.getAccountCode())) {
            finComQuery.setCreatedStartTime(DateUtils.getTheDayBefOrAfter(Constants.DATE_FORMAT_YYYYMMDD, 0));
            finComQuery.setCreatedEndTime(DateUtils.getTheDayBefOrAfter(Constants.DATE_FORMAT_YYYYMMDD, 1));
        }
        if (openAccountCompany.getStartTime()!=null && openAccountCompany.getEndTime()!=null) {
            if (openAccountCompany.getStartTime().getTime()>=openAccountCompany.getEndTime().getTime()) {
                logger.info("时间范围有误,请检查!");
                res.setMsg("时间范围有误,请检查!");
                return res;
            }
            if (openAccountCompany.getEndTime().getTime()-openAccountCompany.getStartTime().getTime()>7*24*60*60*1000) {
                logger.info("查询范围不能超过7天!");
                res.setMsg("查询范围不能超过7天!");
                return res;
            }
            finComQuery.setCreatedStartTime(openAccountCompany.getStartTime());
            finComQuery.setCreatedEndTime(openAccountCompany.getEndTime());
        }
        finComQuery.setAccountRelateId(openAccountCompany.getUserId());
        finComQuery.setFinAccountName(openAccountCompany.getUserName());
        if (openAccountCompany.getPageSize()==null ||openAccountCompany.getPageSize()==0) {
            finComQuery.setDataCount(50);
        } else{
            finComQuery.setDataCount(openAccountCompany.getPageSize());
        }
        if (openAccountCompany.getPageNum()==null || openAccountCompany.getPageNum()==0 || openAccountCompany.getPageNum()==1) {
            finComQuery.setDataIndex(0);
        }else{
            finComQuery.setDataIndex((openAccountCompany.getPageNum()-1)*openAccountCompany.getPageSize());
        }
        List<FinanaceCompany> finCompanysList = finanaceCompanyDao.selectCompanyAccInfo(finComQuery);
        if (finCompanysList.size()==0) {
            logger.info("没有查到账户信息!");
            res.setMsg("没有查到账户信息!");
            return res;  
        }  
        List<OpenAccountCompany> accountCompanys = new ArrayList<OpenAccountCompany>();
        for (FinanaceCompany finanaceCompany: finCompanysList) {
            OpenAccountCompany openAccCompany = new OpenAccountCompany();
            BeanMapper.copy(finanaceCompany, openAccCompany);
            openAccCompany.setUserName(finanaceCompany.getFinAccountName());
            accountCompanys.add(openAccCompany);
        }
        res.setAccountCompanys(accountCompanys);
        res.setTotalCount(finCompanysList.size());
        res.setCode(CodeEnum.SUCCESS.getCode());
        return res;
    }
}
