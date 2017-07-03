package com.rkylin.wheatfield.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rkylin.common.RedisIdGenerator;
import com.rkylin.gaterouter.dto.ResponseDto;
import com.rkylin.gaterouter.dto.bankaccount.pad.SubAccountHandleDto;
import com.rkylin.gaterouter.dto.bankaccount.pad.SubAccountHandleRespDto;
import com.rkylin.gaterouter.dto.bankpayment.BatchPaymentRespDto;
import com.rkylin.gaterouter.dto.bankpayment.PaymentRespDto;
import com.rkylin.gaterouter.service.PabAccountService;
import com.rkylin.util.bean.BeanMapper;
import com.rkylin.wheatfield.api.AccountInfoApi;
import com.rkylin.wheatfield.api.AccountInfoDubboService;
import com.rkylin.wheatfield.common.DateUtils;
import com.rkylin.wheatfield.constant.AccountConstants;
import com.rkylin.wheatfield.constant.BaseConstants;
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.constant.GatewayConstants;
import com.rkylin.wheatfield.constant.SettleConstants;
import com.rkylin.wheatfield.constant.TransCodeConst;
import com.rkylin.wheatfield.dao.AccountInfoDao;
import com.rkylin.wheatfield.dao.FinanaceAccountDao;
import com.rkylin.wheatfield.dao.FinanaceCompanyDao;
import com.rkylin.wheatfield.dao.FinanacePersonDao;
import com.rkylin.wheatfield.dao.GenerationPaymentDao;
import com.rkylin.wheatfield.dao.GenerationPaymentHistoryDao;
import com.rkylin.wheatfield.dao.TransDaysSummaryDao;
import com.rkylin.wheatfield.dao.TransOrderInfoDao;
import com.rkylin.wheatfield.domain.AccountPasswordBean;
import com.rkylin.wheatfield.manager.AccountInfoManager;
import com.rkylin.wheatfield.manager.GenerationPaymentManager;
import com.rkylin.wheatfield.model.AccountInfoQueryResponse;
import com.rkylin.wheatfield.model.BalanceDeResponse;
import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.model.FinAccountBalanceResponse;
import com.rkylin.wheatfield.model.FinAccountResponse;
import com.rkylin.wheatfield.model.OpenSubAccountResponse;
import com.rkylin.wheatfield.model.PingAnAccountResponse;
import com.rkylin.wheatfield.model.PingAnParam;
import com.rkylin.wheatfield.model.UserResponse;
import com.rkylin.wheatfield.pojo.AccountInfo;
import com.rkylin.wheatfield.pojo.AccountInfoQuery;
import com.rkylin.wheatfield.pojo.AccountInfor;
import com.rkylin.wheatfield.pojo.AccountPassword;
import com.rkylin.wheatfield.pojo.Balance;
import com.rkylin.wheatfield.pojo.BalanceDetail;
import com.rkylin.wheatfield.pojo.FinanaceAccount;
import com.rkylin.wheatfield.pojo.FinanaceAccountQuery;
import com.rkylin.wheatfield.pojo.FinanaceCompany;
import com.rkylin.wheatfield.pojo.FinanaceCompanyQuery;
import com.rkylin.wheatfield.pojo.FinanaceEntry;
import com.rkylin.wheatfield.pojo.FinanacePerson;
import com.rkylin.wheatfield.pojo.FinanacePersonQuery;
import com.rkylin.wheatfield.pojo.GenerationPayment;
import com.rkylin.wheatfield.pojo.GenerationPaymentHistory;
import com.rkylin.wheatfield.pojo.GenerationPaymentHistoryQuery;
import com.rkylin.wheatfield.pojo.GenerationPaymentQuery;
import com.rkylin.wheatfield.pojo.TransDaysSummary;
import com.rkylin.wheatfield.pojo.TransOrderInfo;
import com.rkylin.wheatfield.pojo.TransOrderInfoQuery;
import com.rkylin.wheatfield.pojo.User;
import com.rkylin.wheatfield.response.ErrorResponse;
import com.rkylin.wheatfield.service.AccountInfoService;
import com.rkylin.wheatfield.service.AccountManageService;
import com.rkylin.wheatfield.service.AccountPasswordService;
import com.rkylin.wheatfield.service.CheckInfoService;
import com.rkylin.wheatfield.service.NewAccountPasswordService;
import com.rkylin.wheatfield.service.OperationServive;
import com.rkylin.wheatfield.service.ParameterInfoService;
import com.rkylin.wheatfield.service.PaymentAccountService;
import com.rkylin.wheatfield.service.PaymentInternalService;
import com.rkylin.wheatfield.service.TransOrderService;
import com.rkylin.wheatfield.utils.BeanUtil;
import com.rkylin.wheatfield.utils.CodeEnum;
import com.rkylin.wheatfield.utils.CommUtil;

@Service("accountInfoService")
public class AccountInfoServiceImpl implements AccountInfoService, AccountInfoDubboService,AccountInfoApi {

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
    private AccountInfoManager accountInfoManager;

    @Autowired
    CheckInfoService checkInfoService;

    @Autowired
    private PabAccountService pabAccountService;

    @Autowired
    @Qualifier("transOrderInfoDao")
    private TransOrderInfoDao transOrderInfoDao;

    @Autowired
    private OperationServive operationService;

    @Autowired
    private RedisIdGenerator redisIdGenerator;

    @Autowired
    private PaymentAccountService paymentAccountService;

    @Autowired
    private PaymentInternalService paymentInternalService;

    @Autowired
    private ParameterInfoService parameterInfoService;
    
    @Autowired
    GenerationPaymentManager generationPaymentManager;
    
    @Autowired
    private TransDaysSummaryDao transDaysSummaryDao;
    
    @Autowired
    private NewAccountPasswordService newAccountPasswordService;
    
    @Autowired
    private GenerationPaymentHistoryDao generationPaymentHistoryDao;
    
    @Autowired
    private GenerationPaymentDao generationPaymentDao;
    
    @Autowired
    private TransOrderService transOrderService;

    private static Object lock = new Object();

    /**
     * 查询账户信息
     */
    @Override
    public FinAccountResponse getFinAccount(com.rkylin.wheatfield.bean.User user) {
        logger.info("查询账户信息 入参 user=" + user);
        FinAccountResponse res = new FinAccountResponse();
        if (user == null) {
            res.setCode(CodeEnum.ERR_PARAM_NULL.getCode());
            res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
            return res;
        }
        if (user.getType() == null || user.getType().length == 0) {
            user.setType(null);
        }
        logger.info("查询账户信息 所有字段值:userId=" + user.getUserId() + ",instCode=" + user.getInstCode() + "," + "productId="
                + user.getProductId() + "type=" + Arrays.toString(user.getType()));
        if (user.getUserId() == null || "".equals(user.getUserId()) || user.getInstCode() == null
                || "".equals(user.getInstCode())) {
            res.setCode(CodeEnum.ERR_PARAM_NULL.getCode());
            res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
            return res;
        }
        List<FinanaceAccount> list = finanaceAccountDao.queryByInstCodeAndUser(user);
        logger.info("查询账户信息 查出的数据个数=" + list.size());
        if (list.size() == 0) {
            res.setCode(CodeEnum.ERR_DATA_NO_RESULT.getCode());
            res.setMsg(CodeEnum.ERR_DATA_NO_RESULT.getMessage());
            return res;
        }
        res.setFinAccList(list);
        return res;
    }

    /**
     * 查询余额（dubbo）
     * 
     * @param user
     */
    public BalanceDeResponse getBalance(com.rkylin.wheatfield.bean.User user) {
        logger.info("查询余额传入参数user==" + user);
        BalanceDeResponse res = new BalanceDeResponse();
        if (user == null) {
            res.setCode(CodeEnum.ERR_PARAM_NULL.getCode());
            res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
            return res;
        }
        logger.info("查询余额传入参数所有字段值: "
                + BeanUtil.getBeanVal(user, new String[] { "userId", "instCode", "productId", "name", "cardNo" }));
        String field = BeanUtil.validateBeanProEmpty(user, new String[] { "userId", "instCode" });
        logger.info("查询余额  传入的参数字段校验 是否有空值(field的值为是空值的字段,为null表示都不为空)  field=" + field);
        if (field != null) {
            res.setCode(CodeEnum.ERR_PARAM_NULL.getCode());
            res.setMsg(field + "不能为空");
            return res;
        }
        List<FinanaceAccount> finAccList = finanaceAccountDao.queryByInstCodeAndUser(user);
        logger.info("查询余额 查询账户信息 查出的数据个数=" + finAccList.size());
        if (finAccList.size() == 0) {
            res.setCode(CodeEnum.ERR_DATA_NO_RESULT.getCode());
            res.setMsg(CodeEnum.ERR_DATA_NO_RESULT.getMessage());
            return res;
        }
        List<BalanceDetail> balanceDeList = new ArrayList<BalanceDetail>();
        for (FinanaceAccount finanaceAccount : finAccList) {
            logger.info("查询账户信息 所有字段值: " + BeanUtil.getBeanVal(finanaceAccount, new String[] { "finAccountId",
                    "rootInstCd", "finAccountTypeId", "accountRelateId", "groupManage" }));
            Balance balance = checkInfoService.getBalance(new com.rkylin.wheatfield.pojo.User(),
                    finanaceAccount.getFinAccountId());
            logger.info("  查询账户信息  balance=" + balance);
            if (balance == null) {
                continue;
            }
            String name = null;
            if ("10001".equals(finanaceAccount.getFinAccountTypeId())) {
                FinanaceCompanyQuery companyQuery = new FinanaceCompanyQuery();
                companyQuery.setFinAccountId(finanaceAccount.getFinAccountId());
                List<FinanaceCompany> finCompanyList = finanaceCompanyDao.selectByExample(companyQuery);
                logger.info("  查询对公账户信息  finAccountId=" + finanaceAccount.getFinAccountId() + "  查出的个数="
                        + finCompanyList.size());
                if (finCompanyList.size() != 0) {
                    logger.info("  查询对公账户信息  finAccountId=" + finanaceAccount.getFinAccountId() + "  名称="
                            + finCompanyList.get(0).getCompanyName());
                    name = finCompanyList.get(0).getCompanyName();
                    if (user.getName() != null && !"".equals(user.getName())
                            && !finCompanyList.get(0).getCompanyName().equals(user.getName())) {
                        continue;
                    }
                } else {
                    FinanacePersonQuery finPersonQuery = new FinanacePersonQuery();
                    finPersonQuery.setFinAccountId(finanaceAccount.getFinAccountId());
                    List<FinanacePerson> finPersonList = finanacePersonDao.selectByExample(finPersonQuery);
                    logger.info("  查询对私账户信息  finAccountId=" + finanaceAccount.getFinAccountId() + "  查出的个数="
                            + finPersonList.size());
                    if (finPersonList.size() != 0) {
                        logger.info("  查询对私账户信息  finAccountId=" + finanaceAccount.getFinAccountId() + "  名称="
                                + finPersonList.get(0).getPersonChnName());
                        name = finPersonList.get(0).getPersonChnName();
                        if (user.getName() != null && !"".equals(user.getName())
                                && !finPersonList.get(0).getPersonChnName().equals(user.getName())) {
                            continue;
                        }
                    }
                } 
            }
//            if (name == null) {
//                continue;
//            }
            if (user.getCardNo() != null && !"".equals(user.getCardNo())) {
                AccountInfoQuery accountInfoQuery = new AccountInfoQuery();
                accountInfoQuery.setFinAccountId(finanaceAccount.getFinAccountId());
                accountInfoQuery.setAccountNumber(user.getCardNo());
                List<AccountInfo> accList = accountInfoDao.selectByConLike(accountInfoQuery);
                if (accList.size() == 0) {
                    continue;
                }
            }
            BalanceDetail balanceDetail = new BalanceDetail();
            balanceDetail.setBalance(balance);
            balanceDetail.setName(name);
            balanceDeList.add(balanceDetail);
        }
        logger.info("查询余额 查询账户信息  返回的数据个数=" + balanceDeList.size());
        if (balanceDeList.size() == 0) {
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
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public CommonResponse passwordCheck(com.rkylin.wheatfield.bean.User user) {
        logger.info("用户密码校验 入参 :" + BeanUtil.getBeanVal(user, null));
        CommonResponse res = new CommonResponse();
        res.setCode(CodeEnum.FAILURE.getCode());
        if (user == null) {
            logger.info("参数为空");
            res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
            return res;
        }
        String field = BeanUtil.validateBeanProEmpty(user,
                new String[] { "userId", "instCode", "productId","password" });
        if (field != null) {
            res.setMsg(field + "不能为空");
            logger.info(field + "不能为空");
            return res;
        }
        AccountPassword accountPassword = new AccountPassword();
        accountPassword.setUserId(user.getUserId());
        accountPassword.setRootInstCd(user.getInstCode());
        accountPassword.setPassword(user.getPassword());
        accountPassword.setPasswordType(user.getType()[0]);
        String result = accountPasswordService.checkPassword(accountPassword);
        logger.info("用户密码校验  userId=" + user.getUserId() + "    result=" + result);
        if (!"ok".equals(result)) {
            res.setMsg(result);
            return res;
        }
        res.setCode(CodeEnum.SUCCESS.getCode());
        return res;
    }

    @Override
    public CommonResponse openSubAccount(com.rkylin.wheatfield.pojo.User user, FinanaceEntry finanaceEntry) {
        logger.info("开设子账户  参数   user=" + user + ",finanaceEntry=" + finanaceEntry);
        CommonResponse res = new CommonResponse();
        if (user == null) {
            logger.info("参数为空!");
            res.setCode(CodeEnum.ERR_PARAM_NULL.getCode());
            res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
            return res;
        }
        if (user.userId==null||"".equals(user.userId)||user.constId==null||"".equals(user.constId)
                ||user.productId==null||"".equals(user.productId)||user.referUserId==null||"".equals(user.referUserId)) {
            logger.info("必输参数为空!");
            res.setCode(CodeEnum.ERR_PARAM_NULL.getCode());
            res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
            return res;
        }
        if (operationService.checkAccount(user)) {
            logger.info("该子账户已经存在!");
            res.setCode(CodeEnum.FAILURE.getCode());
            res.setMsg("该子账户已经存在!");
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

    /**
     * 运营平台调用账户,账户去多渠道开户,成功后在账户开户两个子账户 Discription:
     * 
     * @param user
     * @return CommonResponse
     * @author Achilles
     * @since 2016年4月26日
     */
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public OpenSubAccountResponse openSubAccountMice(com.rkylin.wheatfield.bean.User user) {
        logger.info("查出的主账户个数=" + BeanUtil.getBeanVal(user, null));
        OpenSubAccountResponse res = new OpenSubAccountResponse();
        res.setCode(CodeEnum.FAILURE.getCode());
        if (user == null) {
            res.setMsg("参数为空");
            return res;
        }
        String field = BeanUtil.validateBeanProEmpty(user,
                new String[] { "userId", "instCode", "productId", "name", "roleCode" });
        logger.info("传入的参数字段校验 是否有空值(field的值为是空值的字段,为null表示都不为空)  field=" + field);
        if (field != null) {
            res.setCode(CodeEnum.ERR_PARAM_NULL.getCode());
            res.setMsg(field + "不能为空");
            return res;
        }
        com.rkylin.wheatfield.pojo.User userr = new com.rkylin.wheatfield.pojo.User();
        userr.userId = user.getUserId();
        userr.constId = user.getInstCode();
        userr.productId = user.getProductId();
        boolean accountIsOK = operationService.checkAccount(userr);
        if (!accountIsOK) {
            logger.info("账户状态非正常!");
            res.setMsg("账户状态非正常!");
            return res;
        }
        userr.productId = Constants.HT_PINGAN_PAY_CHILD;
        accountIsOK = operationService.checkAccount(userr);
        if (accountIsOK) {
            logger.info("该子账户已经存在!");
            res.setMsg("该子账户已经存在!");
            return res;
        }
        userr.productId = Constants.HT_PINGAN_COLL_CHILD;
        accountIsOK = operationService.checkAccount(userr);
        if (accountIsOK) {
            logger.info("该子账户已经存在!");
            res.setMsg("该子账户已经存在!");
            return res;
        }
        SubAccountHandleDto dto = new SubAccountHandleDto();
        dto.setSysNo("zhxt001");// 业务系统号
        dto.setTransCode("16014");// 交易编码 子账户操作(增删改)
        PingAnParam pingAnParam = parameterInfoService.getPingAnOpenParam();
        if (pingAnParam == null) {
            logger.info("参数表设置异常!");
            res.setMsg("系统异常!");
            return res;
        }
        dto.setOrgNo(pingAnParam.getOpenInst());// 内部机构号
        dto.setBusiCode("16011");// 业务编码
        dto.setChannelNo("160603");// 渠道编号 平安银企子账户维护
        dto.setSignType(1);// 签名类型 固定值1 即MD5
        dto.setSignMsg(dto.sign(pingAnParam.getMd5key()));

        dto.setAccountNo(pingAnParam.getMainAccountNo());// 主账户号
        dto.setCurrency("CNY");// 币种
        dto.setAction(1);// 功能码 1-新建
        dto.setSubAccountName(user.getName());// 子帐户户名
        String seq = parameterInfoService.getSubAccountSeq("PINGAN_OPEN_SUB_SEQ");
        if (seq == null) {
            logger.info("参数表设置异常(子账户序列号)!");
            res.setMsg("系统异常!");
            return res;
        }
        // Long subAccountSeq = redisBase.getIncreLong("PINGAN_SUB_ACCOUNT_SEQ",210000l);//210000 为起始号
        dto.setSubAccountSeq(seq);// 子账户序号
        logger.info("调用多渠道转账传入参数: SysNo=" + dto.getSysNo() + ",TransCode=" + dto.getTransCode() + ",OrgNo="
                + dto.getOrgNo() + ",BusiCode=" + dto.getBusiCode() + ",ChannelNo=" + dto.getChannelNo() + ",SignType="
                + dto.getSignType() + ",SignMsg=" + dto.getSignMsg() + ",AccountNo=" + dto.getAccountNo() + ",Currency="
                + dto.getCurrency() + ",Action=" + dto.getAction() + ",SubAccountName=" + dto.getSubAccountName()
                + ",SubAccountSeq=" + dto.getSubAccountSeq());
        // 调用多渠道
        SubAccountHandleRespDto resDto = null;
        try {
            resDto = pabAccountService.subAccountHandle(dto);
        } catch (Exception e) {
            logger.error("调用多渠道开户接口异常!", e);
            res.setMsg("系统异常(多渠道)!");
            return res;
        }
        logger.info("调用多渠道到平安银行开户    returnCode=" + resDto.getReturnCode() + ",returnMsg=" + resDto.getReturnMsg() + ","
                + "SubAccountNo=" + resDto.getSubAccountNo() + ",subAccountName=" + resDto.getSubAccountName());
        if (!"100000".equals(resDto.getReturnCode())) {
            res.setMsg(resDto.getReturnMsg());
            return res;
        }
        logger.info("调用多渠道开户  status=" + resDto.getStatus());
        if (!"N".equals(resDto.getStatus())) {
            res.setMsg("调用多渠道开户失败!");
            return res;
        }
        // 创建两个字账户
        for (int i = 0; i < 2; i++) {
            userr = new com.rkylin.wheatfield.pojo.User();
            userr.userId = user.getUserId();
            userr.constId = user.getInstCode();
            userr.productId = Constants.HT_PINGAN_PAY_CHILD;
            userr.referUserId = resDto.getSubAccountNo();
            userr.userName = "MICE平安付款子账户";
            if (i == 1) {
                userr.productId = Constants.HT_PINGAN_COLL_CHILD;
                userr.userName = "MICE平安收款子账户";
            }
            userr.creditType = user.getName();
            String result = accountManageService.openSubAccount(userr);
            if (!"ok".equals(result)) {
                logger.info("result=====" + result);
                res.setMsg(result);
                return res;
            }
        }
        res = new OpenSubAccountResponse();
        res.setSubAccountNo(resDto.getSubAccountNo());
        return res;
    }

    /**
     * 平安退票
     */
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public String refundPingAn(com.rkylin.gaterouter.dto.bankpayment.PaymentRespDto dto) {
        String returnCode = "100000";
        String returnMsg = "成功!";
        CommonResponse res = refund(dto);
        if (!CodeEnum.SUCCESS.getCode().equals(res.getCode())) {
            returnCode = "0";
            returnMsg = "处理异常!";
            logger.info("----------------------退票---------------" + res.getMsg());
        }
        String backMsg = "returnCode=" + returnCode + "&returnMsg=" + returnMsg;
        return backMsg;
    }

    /**
     * 平安退票 Discription:
     * 
     * @param requestId
     * @return CommonResponse
     * @author Achilles
     * @since 2016年4月27日
     */
    private CommonResponse refund(com.rkylin.gaterouter.dto.bankpayment.PaymentRespDto dto) {
        logger.info("平安退票  输入参数  requestId=" + dto.getTransNo());
        CommonResponse res = new CommonResponse();
        res.setCode(CodeEnum.FAILURE.getCode());
        if (dto.getTransNo() == null) {
            res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
            return res;
        }
        TransOrderInfo transOrderInfo = transOrderInfoDao.selectByPrimaryKey(Integer.parseInt(dto.getTransNo()));
        if (transOrderInfo == null) {
            res.setMsg("没有查到相关订单信息");
            return res;
        }
        if (TransCodeConst.TRANS_STATUS_PAY_SUCCEED != transOrderInfo.getStatus()) {
            res.setMsg("此订单非成功状态！");
            return res;
        }
        TransOrderInfo transOrderInfoNew = new TransOrderInfo();
        transOrderInfoNew.setRequestId(transOrderInfo.getRequestId());
        transOrderInfoNew.setStatus(TransCodeConst.TRANS_STATUS_REFUND);// 将原订单置为退票状态
        transOrderInfoDao.updateByPrimaryKeySelective(transOrderInfoNew);
        // 生成新交易退票重划及对用户和备付金的充值流水
        return refundGenerateOrderAndEntry(transOrderInfo);
    }

    /**
     * 生成新交易退票重划及对用户和备付金的充值流水 Discription:
     * 
     * @param transOrderInfo
     * @return CommonResponse
     * @author Achilles
     * @since 2016年4月27日
     */
    private CommonResponse refundGenerateOrderAndEntry(TransOrderInfo transOrderInfo) {
        CommonResponse res = new CommonResponse();
        res.setCode(CodeEnum.FAILURE.getCode());
        User user = new User();
        user.constId = transOrderInfo.getMerchantCode();
        user.userId = transOrderInfo.getUserId();
        user.productId = Constants.HT_PINGAN_PAY_CHILD;
        boolean accountIsOK = operationService.checkAccount(user);
        if (!accountIsOK) {
            res.setMsg("账户状态非正常！");
            return res;
        }
        String entryId = redisIdGenerator.createRequestNo();
        // 获取每个账户记账流水
        List<FinanaceEntry> finanaceEntries = null;
        List<FinanaceEntry> finanaceEntriesAll = new ArrayList<FinanaceEntry>();
        // 记录该订单交易流水原始amount金额
        for (int i = 0; i <= 1; i++) {
            boolean flag = true;
            String userId = null;
            if (0 == i) {
                userId = transOrderInfo.getUserId();
            } else {
                userId = TransCodeConst.THIRDPARTYID_FNZZH;
                flag = false;
            }
            user.userId = userId;
            Balance balance = null;
            if (flag) {
                balance = checkInfoService.getBalance(user, "");
            } else {
                user.productId = "P000002";
                balance = checkInfoService.getBalance(user, userId);
            }
            if (balance == null) { // 无法获取用户余额信息！
                res.setMsg("无法获取用户" + userId + "余额信息");
                return res;
            }

            balance.setPulseDegree(balance.getPulseDegree() + 1);
            transOrderInfo.setFuncCode(TransCodeConst.CHARGE);
            finanaceEntries = checkInfoService.getFinanaceEntries(transOrderInfo, balance, entryId, flag);
            for (FinanaceEntry finanaceEntry : finanaceEntries) {
                finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
                finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
                if (null == finanaceEntry.getRemark() || "".equals(finanaceEntry.getRemark())) {
                    finanaceEntry.setRemark("退票重划(平安)");
                }
            }
            finanaceEntriesAll.addAll(finanaceEntries);
        }
        transOrderInfo.setRequestId(null);
        transOrderInfo.setStatus(TransCodeConst.TRANS_STATUS_NORMAL);
        transOrderInfo.setFuncCode(TransCodeConst.REFUND);
        transOrderInfo.setOrderNo("REFUND" + CommUtil.getGenerateNum(5));
        transOrderInfo.setCreatedTime(null);
        transOrderInfo.setUpdatedTime(null);
        paymentAccountService.insertFinanaceEntry(finanaceEntriesAll, transOrderInfo, null);
        return new CommonResponse();
    }

    /**
     * 转账通知
     */
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public String noticeSingleTransfer(com.rkylin.gaterouter.dto.bankpayment.PaymentRespDto dto) {
        String returnCode = "100000";
        String returnMsg = "成功!";
        CommonResponse res = null;
        try {
            synchronized (lock) {
                res = noticeSingleTrans(dto);
            }
        } catch (Exception e) {
            logger.error("----------------------转账通知----错误-----------", e);
            return "returnCode=0&returnMsg=处理异常!";
        }
        if (!CodeEnum.SUCCESS.getCode().equals(res.getCode())) {
            returnCode = "0";
            returnMsg = "处理异常!";
            logger.info("----------------------转账通知---------------" + res.getMsg());
        }
        String backMsg = "returnCode=" + returnCode + "&returnMsg=" + returnMsg;
        return backMsg;
    }

    /**
     * 
     * Discription:转账通知
     * 
     * @param dto
     * @author Achilles
     * @since 2016年5月3日
     */
    private CommonResponse noticeSingleTrans(com.rkylin.gaterouter.dto.bankpayment.PaymentRespDto dto) {
        logger.info("转账通知  输入参数  transNo=" + dto.getTransNo() + ", StatusId=" + dto.getStatusId());
        CommonResponse res = new CommonResponse();
        res.setCode(CodeEnum.FAILURE.getCode());
        if (dto.getTransNo() == null) {
            res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
            return res;
        }
        Integer requestId = null;
        String flag = null;
        try {
            flag = "REQUEST_ID";
            requestId = Integer.parseInt(dto.getTransNo());
        } catch (NumberFormatException e) {
            logger.info("转账通知  输入参数不是正常requestId(可能是40142的  ORDER_PACKAGE_NO),根据ORDER_PACKAGE_NO查询------");
            flag = "ORDER_PACKAGE_NO";
        }
        TransOrderInfo transOrderInfo = null;
        if ("REQUEST_ID".equals(flag)) {
            transOrderInfo = transOrderInfoDao.selectByPrimaryKey(requestId);
            if (transOrderInfo == null) {
                res.setMsg("没有查到相关订单信息");
                return res;
            }
        } else if ("ORDER_PACKAGE_NO".equals(flag)) {
            TransOrderInfoQuery query = new TransOrderInfoQuery();
            query.setOrderPackageNo(dto.getTransNo());
            query.setFuncCode("40142");
            List<TransOrderInfo> list = transOrderInfoDao.selectByExample(query);
            if (list.size() == 0) {
                res.setMsg("没有查到相关订单信息");
                return res;
            }
            transOrderInfo = list.get(0);
            requestId = transOrderInfo.getRequestId();
        }
        // TransCodeConst.TRANS_STATUS_NORMAL!=transOrderInfo.getStatus()
        if (dto.getStatusId() != 17 && TransCodeConst.TRANS_STATUS_NORMAL != transOrderInfo.getStatus()) {
            res.setMsg("订单状态异常,无法处理");
            return res;
        }
        if (TransCodeConst.TRANS_STATUS_PAY_SUCCEED == transOrderInfo.getStatus() && dto.getStatusId() != 17) {
            res.setMsg("成功状态的订单只能有退票!");
            return res;
        }
        if (TransCodeConst.TRANS_STATUS_PAY_SUCCEED != transOrderInfo.getStatus() && dto.getStatusId() == 17) {
            res.setMsg("不是成功状态的订单无法退票!");
            return res;
        }
        if ("40141".equals(transOrderInfo.getFuncCode())) {
            if ("16".equals(String.valueOf(dto.getStatusId()))) {// 成功
                TransOrderInfo transOrderInfoNew = new TransOrderInfo();
                transOrderInfoNew.setRequestId(transOrderInfo.getRequestId());
                transOrderInfoNew.setStatus(TransCodeConst.TRANS_STATUS_PAY_SUCCEED);
                transOrderInfoDao.updateByPrimaryKeySelective(transOrderInfoNew);
            } else if ("17".equals(String.valueOf(dto.getStatusId()))) {// 退票
                return refund(dto);
            } else {// 失败
                TransOrderInfo transOrderInfoFail = new TransOrderInfo();
                transOrderInfoFail.setFuncCode("10012");
                transOrderInfoFail.setOrderNo("40141" + CommUtil.getGenerateNum(5));
                transOrderInfoFail.setOrderPackageNo(transOrderInfo.getOrderNo());
                transOrderInfoFail.setUserIpAddress("127.0.0.1");
                transOrderInfoFail.setMerchantCode(transOrderInfo.getMerchantCode());
                ErrorResponse response = paymentAccountService.antiDeduct(transOrderInfoFail);
                if (!response.isIs_success()) {
                    transOrderInfoFail = new TransOrderInfo();
                    transOrderInfoFail.setOrderNo("40141" + CommUtil.getGenerateNum(5));
                    transOrderInfoFail.setOrderPackageNo(transOrderInfo.getOrderNo());
                    transOrderInfoFail.setMerchantCode(transOrderInfo.getMerchantCode());
                    response = paymentInternalService.wipeAccount(transOrderInfoFail);
                }
                if (!response.isIs_success()) {
                    res.setMsg(response.getMsg());
                    return res;
                }
            }
        } else if ("40142".equals(transOrderInfo.getFuncCode())) {// 主子账户转账,没有退票
            TransOrderInfo transOrderInfoNew = new TransOrderInfo();
            transOrderInfoNew.setRequestId(transOrderInfo.getRequestId());
            if ("16".equals(String.valueOf(dto.getStatusId()))) {
                transOrderInfoNew.setStatus(TransCodeConst.TRANS_STATUS_PAY_SUCCEED);
                transOrderInfoDao.updateByPrimaryKeySelective(transOrderInfoNew);
                transOrderInfo.setFuncCode("40141");
                transOrderInfo.setRequestId(null);
                Date now = new Date();
                transOrderInfo.setRequestTime(now);
                transOrderInfo.setOrderDate(now);
                transOrderInfo.setCreatedTime(null);
                transOrderInfo.setUpdatedTime(null);
                transOrderInfo.setRequestNo(transOrderInfo.getOrderNo());
                transOrderInfo.setOrderNo("40141" + CommUtil.getGenerateNum(5));
                res = paymentAccountService.withhold40141(transOrderInfo);
            } else {
                transOrderInfoNew.setStatus(TransCodeConst.TRANS_STATUS_PAY_FAILED);
                transOrderInfoDao.updateByPrimaryKeySelective(transOrderInfoNew);
            }
        } else {
            res.setMsg("该订单不是40141或40142,无法处理!");
            return res;
        }
        return new CommonResponse();
    }

    /**
     * 转账批量-通知
     * Discription:
     * @param dto
     * @return String
     * @author Achilles
     * @since 2016年7月27日
     */
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public String noticeBatchTransfer(com.rkylin.gaterouter.dto.bankpayment.BatchPaymentRespDto dto) {
        String returnCode = "100000";
        String returnMsg = "成功!";
        CommonResponse res = null;
        try {
            synchronized (lock) {
                res = noticeBatchTrans(dto);
            }
        } catch (Exception e) {
            logger.error("----------------------转账批量通知----错误-----------", e);
            return "returnCode=0&returnMsg=处理异常!";
        }
        if (!CodeEnum.SUCCESS.getCode().equals(res.getCode())) {
            returnCode = "0";
            returnMsg = "处理异常!";
            logger.info("----------------------转账批量通知---------------" + res.getMsg());
        }
        String backMsg = "returnCode=" + returnCode + "&returnMsg=" + returnMsg;
        return backMsg;
    }
    
    private CommonResponse noticeBatchTrans(com.rkylin.gaterouter.dto.bankpayment.BatchPaymentRespDto dto) {
        CommonResponse res = new CommonResponse();
        res.setCode(CodeEnum.FAILURE.getCode());
        if (dto == null) {
            res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
            return res;
        }
        logger.info("批量转账通知  参数  batchNo=" + dto.getBatchNo());
        GenerationPaymentQuery query=new GenerationPaymentQuery();
        query.setProcessResult(dto.getBatchNo());
        query.setSendType(2);
        List<GenerationPayment> generationPaymentList=generationPaymentManager.queryList(query);
        if (generationPaymentList.size()==0) {
            res.setMsg("批量转账通知  没有查出符合条件的代收付数据!");
            return res;
        }
        List<PaymentRespDto> respDtoList = dto.getPaymentRespDtoList();
        List<String> orderNoList = new ArrayList<String>();
        for (GenerationPayment generationPayment : generationPaymentList) {
            generationPayment.setUpdatedTime(null);
            orderNoList.add(generationPayment.getOrderNo());
            for (PaymentRespDto paymentRespDto : respDtoList) {
                if (generationPayment.getOrderNo().equals(paymentRespDto.getTransNo())) {
                    logger.info("批量转账通知  参数  TransNo="+paymentRespDto.getTransNo()+",StatusId=" + paymentRespDto.getStatusId()+",ChannelMsg="+paymentRespDto.getChannelMsg());
                    generationPayment.setSendType(0);
                    if (paymentRespDto.getStatusId()!=16) {
                        generationPayment.setSendType(1);
                        generationPayment.setErrorCode(paymentRespDto.getChannelMsg());
                    }
                    continue;
                 }   
            }  
        }
        generationPaymentManager.batchUpdate(generationPaymentList);
        List<TransDaysSummary> transDaysSummaryList = transDaysSummaryDao.selectByPrimaryKeys(orderNoList);
        Set<String> orderNoSet = new HashSet<String>();
        for (TransDaysSummary transDaysSummary : transDaysSummaryList) {
            orderNoSet.add(transDaysSummary.getSummaryOrders());
        }
        TransOrderInfoQuery transOrderInfoQuery = new TransOrderInfoQuery();
        transOrderInfoQuery.setMerchantCode(transDaysSummaryList.get(0).getRootInstCd());
        transOrderInfoQuery.setOrderNoSet(orderNoSet);
        List<TransOrderInfo> transOrderInfoList = transOrderInfoDao.selectByOrderNosAndInstCode(transOrderInfoQuery);
        for (TransOrderInfo transOrderInfo : transOrderInfoList) {
            transOrderInfo.setMerchantCode(transDaysSummaryList.get(0).getRootInstCd());
            boolean flag = false;
            for (TransDaysSummary transDaysSummary : transDaysSummaryList) {
                if (transDaysSummary.getSummaryOrders().equals(transOrderInfo.getOrderNo())) {
                    for (GenerationPayment generationPayment : generationPaymentList) {
                        if (generationPayment.getOrderNo().equals(transDaysSummary.getTransSumId())) {
                            transOrderInfo.setStatus(TransCodeConst.TRANS_STATUS_PAY_SUCCEED);
                            if (generationPayment.getSendType()==1) {
                                transOrderInfo.setStatus(TransCodeConst.TRANS_STATUS_PAY_FAILED);
                                transOrderInfo.setErrorMsg(generationPayment.getErrorCode());
                                flag = true;
                                break;
                            }
                        }
                    }
                    if (flag) {
                        break;
                    }
                }
            }
        }
        if (transOrderInfoList.size()==0) {
            res.setMsg("订单数据异常,请检查!");
            return res;
        }
        transOrderInfoDao.batchUpdateStatusByOrderNoAndMerCode(transOrderInfoList);
        return new CommonResponse();
    }
    
    /**
     * 查询mice子账户信息 Discription:
     * 
     * @return PingAnAccountResponse
     * @author Achilles
     * @since 2016年5月23日
     */
    public PingAnAccountResponse getSubAccountMice(List<com.rkylin.wheatfield.bean.User> userList) {
        logger.info("查询mice子账户信息  传入参数  userList==" + userList);
        PingAnAccountResponse res = new PingAnAccountResponse();
        res.setCode(CodeEnum.FAILURE.getCode());
        if (userList == null || userList.size() == 0) {
            res.setMsg("参数为空!");
            return res;
        }
        // 参数校验
        for (com.rkylin.wheatfield.bean.User user : userList) {
            if (user.getInstCode() == null || "".equals(user.getInstCode()) || user.getProductId() == null
                    || "".equals(user.getProductId())) {
                res.setMsg("必输参数为空!");
                return res;
            }
        }
        List<FinanaceAccount> finAccountList = finanaceAccountDao.batchSelectByCon(userList);
        logger.info("查询所有mice子账户信息  查出的个数=" + finAccountList.size());
        if (finAccountList.size() == 0) {
            res.setCode(CodeEnum.FAILURE.getCode());
            return res;
        }
        userList = new ArrayList<com.rkylin.wheatfield.bean.User>();
        for (FinanaceAccount finanaceAccount : finAccountList) {
            com.rkylin.wheatfield.bean.User user = new com.rkylin.wheatfield.bean.User();
            user.setCardNo(finanaceAccount.getReferUserId());
            user.setName(finanaceAccount.getRecordMap());
            user.setInstCode(finanaceAccount.getRootInstCd());
            user.setProductId(finanaceAccount.getGroupManage());
            user.setUserId(finanaceAccount.getAccountRelateId());
            userList.add(user);
        }
        res.setUserList(userList);
        res.setCode(CodeEnum.SUCCESS.getCode());
        return res;
    }

    /**
     * 获取期初余额
     * Discription:
     * @param user
     * @return FinAccountBalanceResponse
     * @author Achilles
     * @since 2016年6月23日
     */
    public FinAccountBalanceResponse getFinAccountBalance(com.rkylin.wheatfield.bean.User user) {
        logger.info("获取期初余额  传入参数:" + BeanUtil.getBeanVal(user, null));
        FinAccountBalanceResponse res = new FinAccountBalanceResponse();
        res.setCode(CodeEnum.FAILURE.getCode());
        if (user == null) {
            res.setMsg("参数为空!");
            logger.info("获取期初余额  传入参数:user == null");
            return res;
        }
        String field = BeanUtil.validateBeanProEmpty(user,
                new String[] { "userId", "instCode", "productId" });
        logger.info("获取期初余额 传入的参数字段校验 是否有空值(field的值为是空值的字段,为null表示都不为空)  field=" + field);
        if (field != null) {
            res.setMsg(field + "不能为空");
            logger.info("获取期初余额"   +field + "不能为空");
            return res;
        }
        FinanaceAccountQuery query = new FinanaceAccountQuery();
        query.setRootInstCd(user.getInstCode());
        query.setAccountRelateId(user.getUserId());
        query.setGroupManage(user.getProductId());
        query.setStatusId("1");
        query.setFinAccountId(user.getFinAccountId());
        List<FinanaceAccount> finAccountList = finanaceAccountDao.selectByExample(query);
        if (finAccountList.size()==0) {
            res.setMsg("没有查到账户数据!");
            logger.info("获取期初余额   没有查到账户数据!");
            return res;
        }
        com.rkylin.wheatfield.pojo.Balance balance = new com.rkylin.wheatfield.pojo.Balance();
        balance.setAmount(finAccountList.get(0).getAmount());
        balance.setBalanceUsable(finAccountList.get(0).getBalanceUsable());
        balance.setBalanceSettle(finAccountList.get(0).getBalanceSettle());
        balance.setBalanceFrozon(finAccountList.get(0).getBalanceFrozon());
        balance.setBalanceOverLimit(finAccountList.get(0).getBalanceOverLimit());
        balance.setBalanceCredit(finAccountList.get(0).getBalanceCredit());
        balance.setFinAccountId(finAccountList.get(0).getFinAccountId());
        res.setBalance(balance);
        res.setCode(CodeEnum.SUCCESS.getCode());
        return res;
    }
    
    /**
     * 批量开通子账户
     * Discription:
     * @param userList
     * @return CommonResponse
     * @author Achilles
     * @since 2016年7月6日
     */
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public CommonResponse openSubAccounts(List<com.rkylin.wheatfield.pojo.User> userList) {
        CommonResponse res = new CommonResponse();
        res.setCode(CodeEnum.FAILURE.getCode());
        if (userList == null || userList.size() == 0) {
            res.setMsg("参数为空!");
            return res;
        }
        // 参数校验
        StringBuffer sb = new StringBuffer();
        Set<String> paramSet = new HashSet<String>();
        for (com.rkylin.wheatfield.pojo.User user: userList) {
            String userId= user.userId;
            String constId= user.constId;
            String productId= user.productId;
            sb.append("|userId=").append(userId).append(",constId=").append(constId).
            append(",productId=").append(productId);
            if (userId==null||"".equals(userId)||constId==null||"".equals(constId)
                    ||productId==null||"".equals(productId)) {
                logger.info("必输参数为空!   传入的部分参数:"+sb.toString());
                res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
                return res;
            }
            if (paramSet.contains(userId+constId+productId)) {
                logger.info("参数重复!");
                res.setMsg("参数重复!");
                return res; 
            }
            paramSet.add(userId+constId+productId);
        }
        logger.info("开通子账户传入参数:"+sb.toString());
        for (com.rkylin.wheatfield.pojo.User user: userList) {
            if (operationService.checkAccount(user)) {
                logger.info("该子账户已经存在!");
                res.setMsg("该子账户已经存在!");
                return res;
            }
            user.referUserId="0";
            String result = accountManageService.openAccount(user, null);
            if (!"ok".equals(result)) {
                logger.info("开通子账户result="+result);
                res.setMsg(result);
                return res;
            }
        }
        return new CommonResponse();
    }

    @Override
    public UserResponse getAllUserInfo(com.rkylin.wheatfield.bean.User user) {
        logger.info("根据机构号和用户id获取用户在不同机构的信息  参数:" + BeanUtil.getBeanVal(user, null));
        UserResponse res = new UserResponse();
        res.setCode(CodeEnum.FAILURE.getCode());
        if (user == null) {
            res.setMsg("参数为空!");
            return res;
        }
        String field = BeanUtil.validateBeanProEmpty(user,
                new String[] { "userId", "instCode"});
        logger.info("传入的参数字段校验 是否有空值(field的值为是空值的字段,为null表示都不为空)  field=" + field);
        if (field != null) {
            res.setMsg(field + "不能为空");
            logger.info(field + "不能为空");
            return res;
        }  
        FinanaceAccountQuery finanaceAccountQuery = new FinanaceAccountQuery();
        finanaceAccountQuery.setRootInstCd(user.getInstCode());
        finanaceAccountQuery.setAccountRelateId(user.getUserId());
        finanaceAccountQuery.setFinAccountTypeId(AccountConstants.ACCOUNT_TYPE_BASE);
        List<FinanaceAccount>  finAccountList = finanaceAccountDao.selectByExample(finanaceAccountQuery);
        logger.info("根据机构号和用户id获取用户在不同机构的信息  根据参数查出一个账户的信息个数="+finAccountList.size());
        if (finAccountList.size()==0) {
            res.setMsg("没有查出用户账户信息!");
            return res;  
        }
        AccountInfoQuery accountInfoQuery = new AccountInfoQuery();
        accountInfoQuery.setFinAccountId(finAccountList.get(0).getFinAccountId());
        accountInfoQuery.setAccountName(user.getUserId());
        List<AccountInfo> accountInfoList = accountInfoDao.selectByExample(accountInfoQuery);
        logger.info("根据机构号和用户id获取用户在不同机构的信息  根据参数查出卡的信息个数="+accountInfoList.size());
        if (accountInfoList.size()==0) {
            res.setMsg("没有查出相关信息!");
            return res;  
        }
        FinanaceAccountQuery query = new FinanaceAccountQuery();
        query.setRecordMap(accountInfoList.get(0).getCertificateNumber());
        finAccountList = finanaceAccountDao.selectAllUserByCertificateNum(query);
        logger.info("根据机构号和用户id获取用户在不同机构的信息 查出所有账户的信息个数="+finAccountList.size());
        if (finAccountList.size()==0) {
            res.setMsg(" 没有查出用户 账户信息!");
            return res;  
        }
        res.setFinAccountList(finAccountList);
        res.setCode(CodeEnum.SUCCESS.getCode());
        return res;
    }
    
    /**
     * 新增或修改密码
     * @param user
     * @return
     */
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public CommonResponse addOrUpdatePassword(com.rkylin.wheatfield.bean.User user) {
        logger.info("新增或修改密码 入参 所有值="+BeanUtil.getBeanVal(user, null));
        CommonResponse res =new CommonResponse();
        res.setCode(CodeEnum.FAILURE.getCode());
        if (user==null) {
            res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
            return res;
        }
        String field = BeanUtil.validateBeanProEmpty(user, new String[]{"userId","constId","productId","password","type","operType"});
        if (field!=null) {
            logger.info(field+"不能为空!");
            res.setMsg(field+"不能为空!");
            return res;
        }
        AccountPassword accountPassword = new AccountPassword();
        accountPassword.setUserId(user.getUserId());
        accountPassword.setRootInstCd(user.getInstCode());
        accountPassword.setPasswordType(user.getType()[0]);
        accountPassword.setPassword(user.getPassword());
        com.rkylin.wheatfield.pojo.User userr = new com.rkylin.wheatfield.pojo.User();
        userr.constId=user.getInstCode();
        userr.userId=user.getUserId();
        userr.productId=user.getProductId();
        String result = accountPasswordService.savePassword(accountPassword, user.getOperType(), userr);
        logger.info("新增/修改密码 result="+result);
        if (!"ok".equals(result)) { 
            res.setMsg(result);
            return res;
        }
        res.setCode(CodeEnum.SUCCESS.getCode());
        return res;
    }
    
    /**
     * 校验密码是否存在
     * @param user
     * @return
     */
    @Override
    public CommonResponse checkPasswordIfExist(com.rkylin.wheatfield.bean.User user) {
        logger.info("校验密码是否存在 入参:"+BeanUtil.getBeanVal(user, null));
        CommonResponse res =new CommonResponse();
        res.setCode(CodeEnum.FAILURE.getCode());
        if (user==null) {
            res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
            return res;
        }
        if (user.getInstCode()==null||"".equals(user.getInstCode())||user.getUserId()==null||"".equals(user.getUserId())||
                user.getType()==null||"".equals(user.getType())) {
            res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
            return res;
        }
        //校验用户是否存在
        com.rkylin.wheatfield.pojo.User userr = new com.rkylin.wheatfield.pojo.User();
        userr.constId=user.getInstCode();
        userr.userId=user.getUserId();
        List<FinanaceAccount> finList =accountManageService.getAllAccount(userr, "oper");
        if(finList.size()==0){
            res.setMsg("没有查到账户信息");
            return res;
        }
        AccountPassword accountPassword = new AccountPassword();
        accountPassword.setUserId(user.getUserId());
        accountPassword.setRootInstCd(user.getInstCode());
        accountPassword.setPasswordType(user.getType()[0]);
        String result = accountPasswordService.queryPassword(accountPassword); 
        logger.info("校验密码是否存在 result="+result);
        if (!"ok".equals(result)) { 
            res.setMsg(result);
            return res;
        }
        res.setCode(CodeEnum.SUCCESS.getCode());
        return res;
    }

    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public CommonResponse passLockOrUnlock(com.rkylin.wheatfield.bean.User user) {
        logger.info("密码锁定/解锁 入参="+BeanUtil.getBeanVal(user, null));
        CommonResponse res =new CommonResponse();
        res.setCode(CodeEnum.FAILURE.getCode());
        if (user==null) {
            res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
            return res;
        }
        String field = BeanUtil.validateBeanProEmpty(user, new String[]{"userId","constId","productId","type","operType"});
        if (field!=null) {
            logger.info(field+"不能为空!");
            res.setMsg(field+"不能为空!");
            return res;
        }
        AccountPasswordBean accountPassword = new AccountPasswordBean();
        accountPassword.setUserid(user.getUserId());
        accountPassword.setConstid(user.getInstCode());
        accountPassword.setPasswordtype(user.getType()[0]);
        accountPassword.setOpertype(user.getOperType());
        com.rkylin.wheatfield.pojo.User userr = new com.rkylin.wheatfield.pojo.User();
        userr.constId=user.getInstCode();
        userr.userId=user.getUserId();
        userr.productId=user.getProductId();
        String result = newAccountPasswordService.operLock(accountPassword, userr);
        logger.info("密码锁定/解锁 result="+result);
        if (!"ok".equals(result)) { 
            res.setMsg(result);
            return res;
        }
        res.setCode(CodeEnum.SUCCESS.getCode());
        return res;
    }

    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public String noticeTransferGaterouter(ResponseDto dto) {
        String returnCode = "100000";
        String returnMsg = "成功!";
        CommonResponse res = new CommonResponse();
        res.setCode(CodeEnum.FAILURE.getCode());
        if (dto == null) {
            return "returnCode=0&returnMsg=参数为空!";
        }
        try {
            synchronized (lock) {
                if (dto instanceof com.rkylin.gaterouter.dto.bankpayment.BatchPaymentRespDto) {
                    BatchPaymentRespDto batchPaymentRespDto = (BatchPaymentRespDto) dto;
                    res = informBatchTransfer(batchPaymentRespDto); 
                }else if(dto instanceof com.rkylin.gaterouter.dto.bankpayment.PaymentRespDto){
                    PaymentRespDto paymentRespDto = (PaymentRespDto) dto;
                    res = noticeOneTrans(paymentRespDto); 
                }
            }
        } catch (Exception e) {
            logger.error("----------------------转账通知----错误-----------", e);
            return "returnCode=0&returnMsg=处理异常!";
        }
        if (!CodeEnum.SUCCESS.getCode().equals(res.getCode())) {
            returnCode = "0";
            returnMsg = "处理异常!";
            logger.info("----------------------转账通知---------------" + res.getMsg());
        }
        String backMsg = "returnCode=" + returnCode + "&returnMsg=" + returnMsg;
        return backMsg;
    }

    private CommonResponse informBatchTransfer(com.rkylin.gaterouter.dto.bankpayment.BatchPaymentRespDto dto) {
        CommonResponse res = new CommonResponse();
        res.setCode(CodeEnum.FAILURE.getCode());
        if (dto == null) {
            res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
            return res;
        }
        logger.info("批量转账通知  批次号 :" + dto.getBatchNo());
        List<PaymentRespDto> respDtoList = dto.getPaymentRespDtoList();
        logger.info("批量转账通知  详细参数 :"+BeanUtil.getBeanListVal(respDtoList, new String[]{"transNo","statusId","channelMsg"}));
        String[] orderNoArray = new String[respDtoList.size()];
        Set<String>  orderNoSet  =new HashSet<String>();
        for (int i = 0; i < respDtoList.size(); i++) {
            PaymentRespDto paymentRespDto = respDtoList.get(i);
            orderNoArray[i] =  paymentRespDto.getTransNo();
            orderNoSet.add(paymentRespDto.getTransNo());
        }
        List<GenerationPayment> genPayList = generationPaymentManager.selectByOrderNo(orderNoArray);
        logger.info("批量转账通知  GENERATION_PAYMENT  表查出的数据个数=" + genPayList.size());
        GenerationPaymentHistoryQuery query = new GenerationPaymentHistoryQuery();
        query.setProcessResult(dto.getBatchNo());
        query.setOrderNoArray(orderNoArray);
        List<GenerationPaymentHistory> genPayHisList = generationPaymentHistoryDao.selectByOrderNoAndInstcode(query);
        logger.info("批量转账通知  GENERATION_PAYMENT_HISTORY  表查出的数据个数=" + genPayHisList.size());
        if (genPayList.size()==0 && genPayHisList.size()==0) {
            res.setMsg("没有查出GP中相应的数据!");
            return res;
        }
        List<String> orderNoList = new ArrayList<String>();
        GenerationPayment genPayUpdate = null;
        List<GenerationPayment> genPayUpdateList = new ArrayList<GenerationPayment>();
        Map<String,Object[]> orderNoToStatusMap = new HashMap<String,Object[]>();
        for (GenerationPayment genPay : genPayList) {
            for (PaymentRespDto paymentRespDto : respDtoList) {
                if (genPay.getOrderNo().equals(paymentRespDto.getTransNo())) {
                    orderNoList.add(genPay.getOrderNo());
                    genPayUpdate  = new GenerationPayment();
                    genPayUpdate.setGeneId(genPay.getGeneId());
                    genPayUpdate.setOrderNo(genPay.getOrderNo());
                    if (paymentRespDto.getStatusId()==GatewayConstants.STATUS_16 && genPay.getSendType()==SettleConstants.GEN_SEND_TYPE_SENT) {//16:成功
                        genPayUpdate.setSendType(SettleConstants.SEND_NORMAL);
                        genPayUpdateList.add(genPayUpdate);
                        orderNoToStatusMap.put(genPay.getOrderNo(),new Object[]{SettleConstants.SEND_NORMAL,null});
                    }else if (paymentRespDto.getStatusId()==GatewayConstants.STATUS_14
                            && (genPay.getSendType()==SettleConstants.GEN_SEND_TYPE_SENT)) {
                        genPayUpdate.setSendType(SettleConstants.SEND_DEFEAT);
                        genPayUpdate.setErrorCode(paymentRespDto.getChannelMsg());
                        genPayUpdateList.add(genPayUpdate);
                        orderNoToStatusMap.put(genPay.getOrderNo(),new Object[]{SettleConstants.SEND_DEFEAT,paymentRespDto.getChannelMsg()});
                    }else if( paymentRespDto.getStatusId()==GatewayConstants.STATUS_17 
                            &&  genPay.getSendType()==SettleConstants.SEND_NORMAL){
                        genPayUpdate.setSendType(SettleConstants.SEND_DEFEAT);
                        genPayUpdate.setErrorCode(paymentRespDto.getChannelMsg());
                        genPayUpdateList.add(genPayUpdate);
                        orderNoToStatusMap.put(genPay.getOrderNo(),new Object[]{TransCodeConst.TRANS_STATUS_REFUND,paymentRespDto.getChannelMsg()});
                    }
                    break;
                 }   
            }  
        }
        generationPaymentManager.batchUpdate(genPayUpdateList);
        GenerationPaymentHistory genPayHisUpdate = null;
        GenerationPayment genPayment = null;
        List<GenerationPaymentHistory> genPayHisUpdateList = new ArrayList<GenerationPaymentHistory>();
        for (GenerationPaymentHistory genPayHis : genPayHisList) {
            for (PaymentRespDto paymentRespDto : respDtoList) {
                if (genPayHis.getOrderNo().equals(paymentRespDto.getTransNo())) {
                    orderNoList.add(genPayHis.getOrderNo());
                    genPayHisUpdate  = new GenerationPaymentHistory();
                    genPayHisUpdate.setGeneId(genPayHis.getGeneId());
                    genPayHisUpdate.setOrderNo(genPayHis.getOrderNo());
                    if (paymentRespDto.getStatusId()==GatewayConstants.STATUS_17 && genPayHis.getSendType()==SettleConstants.SEND_NORMAL) {//17:退票14:失败
                        genPayHisUpdate.setSendType(SettleConstants.SEND_DEFEAT);
                        genPayHisUpdate.setErrorCode(paymentRespDto.getChannelMsg());
                        genPayHisUpdateList.add(genPayHisUpdate);
                        genPayment = new GenerationPayment();
                        BeanMapper.copy(genPayHis, genPayment);
                        genPayUpdateList.add(genPayment);
                        orderNoToStatusMap.put(genPayHis.getOrderNo(),new Object[]{TransCodeConst.TRANS_STATUS_REFUND,paymentRespDto.getChannelMsg()});
                    }
                    break;
                 }   
            }  
        }
        generationPaymentHistoryDao.batchUpdate(genPayHisUpdateList); 
        if (genPayUpdateList.size()==0) {
            res.setMsg("订单信息异常,请检查!");
            return res;
        }
        List<TransDaysSummary> transDaysSummaryList = transDaysSummaryDao.selectByPrimaryKeys(orderNoList);
        orderNoSet = new HashSet<String>();
        for (TransDaysSummary transDaysSummary : transDaysSummaryList) {
            orderNoSet.add(transDaysSummary.getSummaryOrders());
        }
        TransOrderInfoQuery transOrderInfoQuery = new TransOrderInfoQuery();
        transOrderInfoQuery.setMerchantCode(transDaysSummaryList.get(0).getRootInstCd());
        transOrderInfoQuery.setOrderNoSet(orderNoSet);
        List<TransOrderInfo> transOrderInfoList = transOrderInfoDao.selectByOrderNosAndInstCode(transOrderInfoQuery);
        if (transOrderInfoList.size()==0) {
            res.setMsg("没有查出相应的订单信息!");
            return res;
        }
        List<TransOrderInfo> transOrderInfoUpdateList = new ArrayList<TransOrderInfo>();
        TransOrderInfo transOrderInfoUpdate  = null;
        for (TransOrderInfo transOrderInfo : transOrderInfoList) {
            transOrderInfo.setMerchantCode(transDaysSummaryList.get(0).getRootInstCd());
            for (TransDaysSummary transDaysSummary : transDaysSummaryList) {
                if (transDaysSummary.getSummaryOrders().equals(transOrderInfo.getOrderNo())) {
                    Object[] statusToMsg = orderNoToStatusMap.get(transDaysSummary.getTransSumId());
                    if (statusToMsg==null) {
                        continue;
                    }
                    transOrderInfoUpdate = new TransOrderInfo();
                    transOrderInfoUpdate.setMerchantCode(transOrderInfo.getMerchantCode());
                    transOrderInfoUpdate.setOrderNo(transOrderInfo.getOrderNo());
                    transOrderInfoUpdate.setErrorMsg(statusToMsg[1]==null?null:String.valueOf(statusToMsg[1]));
                    Integer status = (Integer) statusToMsg[0];
                    if (status==SettleConstants.SEND_NORMAL) {
                        transOrderInfoUpdate.setStatus(TransCodeConst.TRANS_STATUS_PAY_SUCCEED);
                  }else if(status==SettleConstants.SEND_DEFEAT){
                      transOrderInfoUpdate.setStatus(TransCodeConst.TRANS_STATUS_PAY_FAILED);
                  }else if(status==TransCodeConst.TRANS_STATUS_REFUND){
                      transOrderInfoUpdate.setStatus(TransCodeConst.TRANS_STATUS_REFUND);
                      if (TransCodeConst.PAYMENT_40144.equals(transDaysSummary.getOrderType())) {
                          res = transOrderService.refund(transOrderInfo.getMerchantCode(), transOrderInfo.getOrderNo(), transOrderInfoUpdate.getErrorMsg());
                          if (!CodeEnum.SUCCESS.getCode().equals(res.getCode())) {
                              logger.info("退票失败:MerchantCode="+transOrderInfo.getMerchantCode()+",OrderNo="+transOrderInfo.getOrderNo()+",msg="+res.getMsg());
                          }                         
                      }
                  }
                  transOrderInfoUpdateList.add(transOrderInfoUpdate);
                  break;
                }
            }
        }
        if (transOrderInfoList.size()==0) {
            res.setMsg("订单数据异常,请检查!");
            return res;
        }
        transOrderInfoDao.batchUpdateStatusByOrderNoAndMerCode(transOrderInfoUpdateList);
        res.setCode(CodeEnum.SUCCESS.getCode());
        return res;
    }
    
    private CommonResponse noticeOneTrans(PaymentRespDto dto) {
        CommonResponse res = new CommonResponse();
        res.setCode(CodeEnum.FAILURE.getCode());
        if (dto == null) {
            res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
            return res;
        }
        logger.info("转账通知(单笔)  参数 :" + BeanUtil.getBeanVal(dto, new String[]{"orgNo","transNo","statusId","channelMsg"}));
        List<GenerationPayment> genPayList = generationPaymentManager.selectByOrderNo(new String[]{dto.getTransNo()});
        logger.info("批量转账通知  GENERATION_PAYMENT  表查出的数据个数=" + genPayList.size());
        if (genPayList.size()!=0) {
            GenerationPayment genPayUpdate = new GenerationPayment();
            genPayUpdate.setGeneId(genPayList.get(0).getGeneId());
            if (dto.getStatusId()==GatewayConstants.STATUS_16 && genPayList.get(0).getSendType()==SettleConstants.GEN_SEND_TYPE_SENT) {//16:成功
                genPayUpdate.setSendType(SettleConstants.SEND_NORMAL);
            }else if (dto.getStatusId()==GatewayConstants.STATUS_14
                    && genPayList.get(0).getSendType()==SettleConstants.GEN_SEND_TYPE_SENT) {//17:退票14:失败
                genPayUpdate.setSendType(SettleConstants.SEND_DEFEAT);
                genPayUpdate.setErrorCode(dto.getChannelMsg());
            }else if(dto.getStatusId()==GatewayConstants.STATUS_17
                    && genPayList.get(0).getSendType()==SettleConstants.SEND_NORMAL){
                genPayUpdate.setSendType(SettleConstants.SEND_DEFEAT);
                genPayUpdate.setErrorCode(dto.getChannelMsg());
            }else{
                res.setMsg("订单信息异常!");
                return res;
            }  
            generationPaymentDao.updateByPrimaryKeySelective(genPayUpdate);
        }
        List<GenerationPaymentHistory> genPayHisList = null;
        if (genPayList.size()==0 && dto.getStatusId()==GatewayConstants.STATUS_17) {
            Set<String> orderNoSet = new HashSet<String>();
            orderNoSet.add(dto.getTransNo());
            GenerationPaymentHistoryQuery genHisQuery = new GenerationPaymentHistoryQuery();
            genHisQuery.setOrderNo(dto.getTransNo());
            genHisQuery.setRootInstcd(dto.getOrgNo());
            genPayHisList = generationPaymentHistoryDao.selectByExample(genHisQuery);
            logger.info("批量转账通知  GENERATION_PAYMENT_HISTORY  表查出的数据个数=" + genPayHisList.size());
            if ( genPayHisList.size()==0) {
                res.setMsg("没有查出相应GPH的数据!");
                return res;
            } 
            if (genPayHisList.get(0).getSendType()!=SettleConstants.SEND_NORMAL) {
                res.setMsg("GPH数据状态非正常,无法退票");
                return res;
            }
            GenerationPaymentHistory genPayHisUpdate  = new GenerationPaymentHistory();
            genPayHisUpdate.setGeneId(genPayHisList.get(0).getGeneId());
            genPayHisUpdate.setSendType(SettleConstants.SEND_DEFEAT);
            genPayHisUpdate.setErrorCode(dto.getChannelMsg());
            generationPaymentHistoryDao.updateByPrimaryKeySelective(genPayHisUpdate);
        }
        if (genPayList.size()==0 && (genPayHisList==null||genPayHisList.size()==0)) {
            res.setMsg("没有查出相应的GP/GPH数据!");
            return res;
        }
        List<String> idList = new ArrayList<String>();
        idList.add(dto.getTransNo());
        List<TransDaysSummary> transDaysSummaryList = transDaysSummaryDao.selectByPrimaryKeys(idList);
        TransOrderInfoQuery transOrderInfoQuery = new TransOrderInfoQuery();
        transOrderInfoQuery.setMerchantCode(transDaysSummaryList.get(0).getRootInstCd());
        transOrderInfoQuery.setOrderNo(transDaysSummaryList.get(0).getSummaryOrders());
        List<TransOrderInfo> transOrderInfoList = transOrderInfoDao.selectByExample(transOrderInfoQuery);
        if (transOrderInfoList.size()==0) {
            res.setMsg("没有查出相应的订单信息!");
            return res;
        }
        TransOrderInfo transOrderInfo = new TransOrderInfo();
        transOrderInfo.setRequestId(transOrderInfoList.get(0).getRequestId());
        if (dto.getStatusId()==GatewayConstants.STATUS_16&&transOrderInfoList.get(0).getStatus()==TransCodeConst.TRANS_STATUS_GENARATED) {
            transOrderInfo.setStatus(TransCodeConst.TRANS_STATUS_PAY_SUCCEED);
        }else if (dto.getStatusId()==GatewayConstants.STATUS_14&&transOrderInfoList.get(0).getStatus()==TransCodeConst.TRANS_STATUS_GENARATED) {
            transOrderInfo.setStatus(TransCodeConst.TRANS_STATUS_PAY_FAILED);
            transOrderInfo.setErrorMsg(dto.getChannelMsg());
        }else if (dto.getStatusId()==GatewayConstants.STATUS_17&&transOrderInfoList.get(0).getStatus()==TransCodeConst.TRANS_STATUS_PAY_SUCCEED) {
            transOrderInfo.setStatus(TransCodeConst.TRANS_STATUS_REFUND);
            transOrderInfo.setErrorMsg(dto.getChannelMsg());
            if (TransCodeConst.PAYMENT_40144.equals(transOrderInfoList.get(0).getFuncCode())) {
                res = transOrderService.refund(transOrderInfoList.get(0).getMerchantCode(), transOrderInfoList.get(0).getOrderNo(), transOrderInfo.getErrorMsg());
                if (!CodeEnum.SUCCESS.getCode().equals(res.getCode())) {
                    return res;
                }               
            }
        }
        transOrderInfoDao.updateByPrimaryKeySelective(transOrderInfo);
        res.setCode(CodeEnum.SUCCESS.getCode());
        return res;
    }
    
    /**
     * 用户和备付金加钱
     * Discription:
     * @param user
     * @return CommonResponse
     * @author Achilles
     * @since 2016年11月11日
     */
//    private CommonResponse userAndProvisionsAccountPlus(com.rkylin.wheatfield.bean.User user){
//        List<com.rkylin.wheatfield.bean.User> userList = new ArrayList<com.rkylin.wheatfield.bean.User>();
//        user.setStatus(BaseConstants.CREDIT_TYPE);
//        userList.add(user);
//        com.rkylin.wheatfield.bean.User userBf = new com.rkylin.wheatfield.bean.User();
//        userBf.setInstCode(Constants.FN_ID);
//        userBf.setUserId(TransCodeConst.THIRDPARTYID_FNZZH);
//        userBf.setProductId(Constants.FN_PRODUCT);
//        userBf.setAmount(user.getAmount());
//        userBf.setStatus(BaseConstants.CREDIT_TYPE);
//        userBf.setFinAccountId(user.getFinAccountId());
//        userList.add(userBf);
//        return  semiAutomatizationServiceApi.operateFinAccounts(userList);
//    }

    @Override
    public AccountInfoQueryResponse accountInfoQueryByDubbo(AccountInfor accountInfor) {
        logger.info("银行卡查询入参:"+BeanUtil.getBeanVal(accountInfor, null));
        AccountInfoQueryResponse res =new AccountInfoQueryResponse();
        res.setCode(CodeEnum.FAILURE.getCode());
        if (accountInfor==null) {
            logger.info("参数为空!");
            res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
            return res;
        }
        if (CommUtil.isEmp(accountInfor.getRootInstCd())) {
            logger.info("机构号不能为空!");
            res.setMsg("机构号不能为空!");
            return res;
        }
        AccountInfoQuery accountInfoQuery = new AccountInfoQuery();
        BeanMapper.copy(accountInfor, accountInfoQuery);
        if (accountInfor.getStartTime()==null && accountInfor.getEndTime()==null) {
            accountInfoQuery.setCreatedStartTime(DateUtils.getTheDayBefOrAfter(Constants.DATE_FORMAT_YYYYMMDD, 0));
            accountInfoQuery.setCreatedEndTime(DateUtils.getTheDayBefOrAfter(Constants.DATE_FORMAT_YYYYMMDD, 1));
        }
        if (accountInfor.getStartTime()!=null && accountInfor.getEndTime()!=null) {
            if (accountInfor.getStartTime().getTime()>=accountInfor.getEndTime().getTime()) {
                logger.info("时间范围有误,请检查!");
                res.setMsg("时间范围有误,请检查!");
                return res;
            }
            if (accountInfor.getEndTime().getTime()-accountInfor.getStartTime().getTime()>7*24*60*60*1000) {
                logger.info("查询范围不能超过7天!");
                res.setMsg("查询范围不能超过7天!");
                return res;
            }
            accountInfoQuery.setCreatedStartTime(accountInfor.getStartTime());
            accountInfoQuery.setCreatedEndTime(accountInfor.getEndTime());
        }
        if (accountInfor.getPageSize()==null ||accountInfor.getPageSize()==0) {
            accountInfoQuery.setDataCount(50);
        } else{
            accountInfoQuery.setDataCount(accountInfor.getPageSize());
        }
        if (accountInfor.getPageNum()==null || accountInfor.getPageNum()==0 || accountInfor.getPageNum()==1) {
            accountInfoQuery.setDataIndex(0);
        }else{
            accountInfoQuery.setDataIndex((accountInfor.getPageNum()-1)*accountInfor.getPageSize());
        }       
        accountInfoQuery.setStatus(accountInfor.getStatusId());
        List<AccountInfo> accountInfoList = accountInfoDao.selectAccInfo(accountInfoQuery);
        if (accountInfoList.size()==0) {
            logger.info("没有查到卡信息!");
            res.setMsg("没有查到卡信息!");
            return res;  
        }  
        List<AccountInfor> accountInfos = new ArrayList<AccountInfor>();
        for (AccountInfo accountInfo: accountInfoList) {
            AccountInfor accountInforr = new AccountInfor();
            BeanMapper.copy(accountInfo, accountInforr);
            accountInforr.setStatusId(accountInfo.getStatus());
            accountInfos.add(accountInforr);
        }
        res.setAccountInfos(accountInfos);;
        res.setTotalCount(accountInfoList.size());
        res.setCode(CodeEnum.SUCCESS.getCode());
        return res;
    }
}