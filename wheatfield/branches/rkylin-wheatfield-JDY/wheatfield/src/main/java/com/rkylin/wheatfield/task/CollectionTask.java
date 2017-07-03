package com.rkylin.wheatfield.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rkylin.common.RedisIdGenerator;
import com.rkylin.utils.RkylinMailUtil;
import com.rkylin.wheatfield.common.DateUtils;
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.constant.SettleConstants;
import com.rkylin.wheatfield.constant.TransCodeConst;
import com.rkylin.wheatfield.exception.AccountException;
import com.rkylin.wheatfield.manager.AccountInfoManager;
import com.rkylin.wheatfield.manager.TransDaysSummaryManager;
import com.rkylin.wheatfield.manager.TransOrderInfoManager;
import com.rkylin.wheatfield.service.GenerationPaymentService;
import com.rkylin.wheatfield.service.OperationServive;
import com.rkylin.wheatfield.service.PaymentAccountService;
import com.rkylin.wheatfield.service.SettlementServiceThr;
import com.rkylin.wheatfield.settlement.SettlementLogic;
import com.rkylin.wheatfield.utils.DateUtil;

public class CollectionTask {
	@Autowired
	TransOrderInfoManager transOrderInfoManager;
	@Autowired
	GenerationPaymentService generationPaymentService;
	@Autowired
	PaymentAccountService paymentAccountService;
	@Autowired
	OperationServive operationServive;
	@Autowired
	TransDaysSummaryManager transDaysSummaryManager;
	@Autowired
	RedisIdGenerator redisIdGenerator;
	@Autowired
	AccountInfoManager accountInfoManager;
	@Autowired
	SettlementServiceThr settlementServiceThr;
	@Autowired
	SettlementLogic settlementLogic;
	
	DateUtil dateUtil=new DateUtil();
	/** 日志对象 */
	private static final Logger logger = LoggerFactory.getLogger(WithdrawTask.class);
	/**
	 * 会堂代收
	 */	
	@Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
	public void collection(){
		settlementServiceThr.paymentGeneration(
				TransCodeConst.PAYMENT_COLLECTION, Constants.HT_ID,
				SettleConstants.ORDER_COLLECTION,
//				SettleConstants.COLLECTION_CODE_OLD,1);
				SettleConstants.COLLECTION_CODE,1);
		//生成会唐代收文件并上传
		try {
			String accountDate=generationPaymentService.getAccountDate();
			String batch=settlementLogic.getBatchNo(DateUtils.getAccountDate(Constants.DATE_FORMAT_YYYYMMDD, accountDate),SettleConstants.ROP_RECEIVE_BATCH_CODE, Constants.HT_ID);
			logger.info("-------会堂代收批次号------"+batch);
//			generationPaymentService.uploadPaymentFile(6,batch, Constants.HT_ID,DateUtils.getDate(accountDate, Constants.DATE_FORMAT_YYYYMMDD));
			// 提交到代收付系统
//			generationPaymentService.submitToCollection(6, batch,
//					Constants.HT_ID,DateUtils.getDate(accountDate, Constants.DATE_FORMAT_YYYYMMDD));
			generationPaymentService.submitToRecAndPaySys(6,
					Constants.HT_ID,accountDate,DateUtils.getDate(accountDate, Constants.DATE_FORMAT_YYYYMMDD));
		} catch (AccountException e) {
			logger.error("提交会唐代收到代收付系统失败"+e.getMessage());
			RkylinMailUtil.sendMailThread("会唐代收", "提交会唐代收到代收付系统失败"+e.getMessage(), TransCodeConst.GENERATION_PAY_ERROR_TOEMAIL);
		}	
	}	
	/**
	 * 丰年代收
	 */	
	@Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
	public void collectionFN(){
			settlementServiceThr.paymentGeneration(
					TransCodeConst.PAYMENT_COLLECTION, Constants.FN_ID,
					SettleConstants.ORDER_COLLECTION,
					SettleConstants.COLLECTION_CODE,1);
	}
	/**
	 * 课栈代收
	 */	
	public void collectionKZ(){ 
		settlementServiceThr.paymentGeneration(
				TransCodeConst.PAYMENT_COLLECTION, Constants.KZ_ID,
				SettleConstants.ORDER_COLLECTION,
//				SettleConstants.COLLECTION_CODE_OLD,1);
				SettleConstants.COLLECTION_CODE,1);
	}
    /**
     * 课栈代收汇总/上传
     */ 
    public void collectionSumSubmitKZT0(){ 
        settlementServiceThr.paymentGeneration(
                TransCodeConst.PAYMENT_COLLECTION, Constants.KZ_ID,
                SettleConstants.ORDER_COLLECTION,
                SettleConstants.COLLECTION_CODE,0);
        
        String accountDate=generationPaymentService.getAccountDate();
        generationPaymentService.submitToRecAndPaySys(6,Constants.FN_ID,accountDate,DateUtils.getDate(accountDate, Constants.DATE_FORMAT_YYYYMMDD));
        
        generationPaymentService.submitToRecAndPaySys(6,Constants.RDBL_ID,accountDate,DateUtils.getDate(accountDate, Constants.DATE_FORMAT_YYYYMMDD));
    }	
    
    /**
     * 悦视觉代收汇总并上传
     */ 
    public void collectionSumSubmitYSJ0(){ 
        settlementServiceThr.paymentGeneration(
                TransCodeConst.PAYMENT_COLLECTION, Constants.YSJ_ID,
                SettleConstants.ORDER_COLLECTION,
                SettleConstants.COLLECTION_CODE,0);
        
        String accountDate=generationPaymentService.getAccountDate();
        generationPaymentService.submitToRecAndPaySys(6,Constants.YSJ_ID,accountDate,DateUtils.getDate(accountDate, Constants.DATE_FORMAT_YYYYMMDD));
    }
//	}
    
    /**
     * 帮帮助学代收汇总上传
     */ 
    public void collectionSumSubmitBBZXT0(){ 
        settlementServiceThr.paymentGeneration(
                TransCodeConst.PAYMENT_COLLECTION, Constants.BBZX_ID,
                SettleConstants.ORDER_COLLECTION,
                SettleConstants.COLLECTION_CODE,0);
        
        String accountDate=generationPaymentService.getAccountDate();
        generationPaymentService.submitToRecAndPaySys(6,Constants.FN_ID,accountDate,DateUtils.getDate(accountDate, Constants.DATE_FORMAT_YYYYMMDD));
    }
    
    /**
     * 旅游代收汇总上传
     */ 
    public void collectionSumSubmitLYT0(){ 
        settlementServiceThr.paymentGeneration(
                TransCodeConst.PAYMENT_COLLECTION, Constants.LY_ID,
                SettleConstants.ORDER_COLLECTION,
                SettleConstants.COLLECTION_CODE,0);
        
        String accountDate=generationPaymentService.getAccountDate();
        generationPaymentService.submitToRecAndPaySys(6,Constants.LY_ID,accountDate,DateUtils.getDate(accountDate, Constants.DATE_FORMAT_YYYYMMDD));
    }
    
    /**
     * 天联在线代收汇总上传
     */ 
    public void collectionSumSubmitTLZXT0(){ 
        settlementServiceThr.paymentGeneration(
                TransCodeConst.PAYMENT_COLLECTION, Constants.TLZX_ID,
                SettleConstants.ORDER_COLLECTION,
                SettleConstants.COLLECTION_CODE,0);
        
        String accountDate=generationPaymentService.getAccountDate();
        generationPaymentService.submitToRecAndPaySys(6,Constants.TLZX_ID,accountDate,DateUtils.getDate(accountDate, Constants.DATE_FORMAT_YYYYMMDD));
    } 
    
    /**
     * 美容分期代收汇总上传
     * Discription: void
     * @author Achilles
     * @since 2016年11月18日
     */
    public void collectionSumSubmitMRFQT0(){ 
        settlementServiceThr.paymentGeneration(
                TransCodeConst.PAYMENT_COLLECTION, Constants.MRFQ_ID,
                SettleConstants.ORDER_COLLECTION,
                SettleConstants.COLLECTION_CODE,0);
        
        String accountDate=generationPaymentService.getAccountDate();
        generationPaymentService.submitToRecAndPaySys(6,Constants.MRFQ_ID,accountDate,DateUtils.getDate(accountDate, Constants.DATE_FORMAT_YYYYMMDD));
    } 
 
    /**
     * 杏仁代收汇总上传
     * Discription: void
     * @author Achilles
     * @since 2017年1月12日
     */
    public void collectionSumSubmitXRT0(){ 
        settlementServiceThr.paymentGeneration(
                TransCodeConst.PAYMENT_COLLECTION, Constants.XR_ID,
                SettleConstants.ORDER_COLLECTION,
                SettleConstants.COLLECTION_CODE,0);
        
        String accountDate=generationPaymentService.getAccountDate();
        generationPaymentService.submitToRecAndPaySys(6,Constants.XR_ID,accountDate,DateUtils.getDate(accountDate, Constants.DATE_FORMAT_YYYYMMDD));
    }     
}
