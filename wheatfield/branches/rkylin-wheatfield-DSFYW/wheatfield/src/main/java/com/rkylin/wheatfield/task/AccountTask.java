package com.rkylin.wheatfield.task;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import com.rkylin.wheatfield.model.CommonResponse;
import org.springframework.stereotype.Component;

import com.rkylin.wheatfield.api.GenerationPayDubboService;
import com.rkylin.wheatfield.common.DateUtils;
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.constant.SettleConstants;
import com.rkylin.wheatfield.constant.TransCodeConst;
import com.rkylin.wheatfield.service.AccountManageService;
import com.rkylin.wheatfield.service.GenerationPaymentService;
import com.rkylin.wheatfield.service.SettlementServiceThr;
import com.rkylin.wheatfield.service.TransOrderService;
import com.rkylin.wheatfield.utils.CodeEnum;
import com.rkylin.wheatfield.utils.DateUtil;

@Component
public class AccountTask {
    private static Logger logger = LoggerFactory.getLogger(AccountTask.class);
    @Autowired
    private AccountManageService accountManageService;
    
    @Autowired
    private SettlementServiceThr settlementServiceThr;
    
    @Autowired
    @Qualifier("generationPaymentService")
    private GenerationPayDubboService generationPayDubboService;
    
    @Autowired
    GenerationPaymentService generationPaymentService;
    @Autowired
    
    /**
     * 代付一分钱校验对公账户（定时调度） 2015-09-06
     * 
     * @return 1：正常； 其他会返回相应的错误码
     */
    public void payJudgePublicAccount() {
        logger.info("--------------------一分钱代付(新)task  开始----------------------------------");
        accountManageService.paymentJudgePublicAccount();
        logger.info("--------------------一分钱代付(新)task  结束 ----------------------------------");
    }

    /**
     * 查询对公账户一分钱代付结果，修改状态（定时调度）
     */
    public void updatePubAccByPayResult() {
        logger.info("--------------------查询对公账户一分钱代付结果，修改状态(新)task  开始----------------------------------");
        accountManageService.updatePubAccountByPayResult();
        logger.info("--------------------查询对公账户一分钱代付结果，修改状态(新)task  结束----------------------------------");
    }

    /**
     * 清结算汇总的代付结果返回后,调账（定时调度）
     */
    public void adjustWithhold40142() {
        logger.info("--------------------清结算汇总的代付结果返回后,调账,task  开始----------------------------------");
        settlementServiceThr.adjustmentWithhold40142(null,null);
        logger.info("--------------------清结算汇总的代付结果返回后,调账,task  结束----------------------------------");
    }

    /**
     * 清结算汇总的代付结果返回后,调账（定时调度）
     */
    public void sumCreditWithdrawToUpdateFactoring() {
        logger.info("---------------白条提现汇总修改保理账户,task  开始----------------------------------");
        CommonResponse res = settlementServiceThr.summaryCreditWithdrawToUpdateFactoring();
        if (!CodeEnum.SUCCESS.getCode().equals(res.getCode())) {
            logger.info("白条提现汇总修改保理账户="+res.getMsg());  
        }
        logger.info("--------------------白条提现汇总修改保理账户,task  结束----------------------------------");
    }
    
    /**
     * 汇总客栈40143交易
     */
    public void summarizing40143() {
        logger.info("--------------------汇总"+Constants.KZ_ID+"  40143交易  开始---------");
        settlementServiceThr.paymentGeneration(
                TransCodeConst.PAYMENT_40143, Constants.KZ_ID,
                SettleConstants.ORDER_WITHHOLD,
                SettleConstants.WITHHOLD_CODE,0);
        logger.info("--------------------汇总"+Constants.KZ_ID+"  40143交易  结束---------");
    }
    
    
    /**
     * 发送客栈40143交易到多渠道
     */
    public void send40143ToGateRouter() {
        logger.info("--------------------发送"+Constants.YQMS_KZ_ID+"   40143交易到多渠道  开始--------------");
        generationPayDubboService.submitToGateRouter40143(null,null,Constants.YQMS_KZ_ID);
        logger.info("--------------------发送"+Constants.YQMS_KZ_ID+"   40143交易到多渠道  结束--------------");
    }
   
    /**
     * 汇总悦视觉40143交易
     */
    public void summarizing40143YSJ() {
        logger.info("--------------------汇总"+Constants.YSJ_ID+"  40143交易  开始---------");
        settlementServiceThr.paymentGeneration(
                TransCodeConst.PAYMENT_40143, Constants.YSJ_ID,
                SettleConstants.ORDER_WITHHOLD,
                SettleConstants.WITHHOLD_CODE,1);  
        logger.info("--------------------汇总"+Constants.YSJ_ID+"  40143交易  结束---------");
    }   
    
    /**
     * 发送悦视觉40143交易到多渠道
     */
    public void send40143ToGateRouterYSJ() {
        logger.info("--------------------发送"+Constants.YQMS_YSJ_ID+"   40143交易到多渠道  开始--------------");
        String accountDate=generationPaymentService.getAccountDate();
        accountDate=DateUtils.getDateFormat(DateUtil.DEFAULT_DATE_FORMAT, DateUtils.getAccountDate(Constants.DATE_FORMAT_YYYYMMDD, accountDate));
        generationPayDubboService.submitToGateRouter40143(accountDate,null,Constants.YQMS_YSJ_ID);
        logger.info("--------------------发送"+Constants.YQMS_YSJ_ID+"   40143交易到多渠道  结束--------------");
    }
    
    /**
     * 清结算回代收付结果后,修改卡状态
     */
//    public void updateAccInfoByPayResult() {
//        logger.info("----------------  清结算回代收付结果,修改卡状态------开始--------");
//        CommonResponse res = transOrderService.updateAccountInfoByPayResult();
//        if (!CodeEnum.SUCCESS.getCode().equals(res.getCode())) {
//            logger.info("----------------  清结算回代收付结果,修改卡状态-------失败-------msg="+res.getMsg());  
//        }
//        logger.info("----------------  清结算回代收付结果,修改卡状态------结束--------");
//    }    
}
