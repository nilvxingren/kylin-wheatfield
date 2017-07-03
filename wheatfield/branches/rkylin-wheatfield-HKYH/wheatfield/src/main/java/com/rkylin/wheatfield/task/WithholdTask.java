package com.rkylin.wheatfield.task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.service.GenerationPaymentService;
import com.rkylin.wheatfield.service.OperationServive;
import com.rkylin.wheatfield.service.PaymentAccountService;
import com.rkylin.wheatfield.service.SettlementServiceThr;
import com.rkylin.wheatfield.settlement.SettlementLogic;
import com.rkylin.wheatfield.utils.CodeEnum;
import com.rkylin.wheatfield.utils.DateUtil;

@Transactional
public class WithholdTask {
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
	 * 会堂代付
	 */	
	public void withholdHT(){ 
		settlementServiceThr.paymentGeneration(
				TransCodeConst.PAYMENT_WITHHOLD, Constants.HT_ID,
				SettleConstants.ORDER_WITHHOLD,
				SettleConstants.WITHHOLD_BATCH_CODE,1);
//				SettleConstants.WITHHOLD_BATCH_CODE_OLD,1);
		settlementServiceThr.paymentGeneration(
				TransCodeConst.PAYMENT_WITHHOLD, Constants.HT_CLOUD_ID,
				SettleConstants.ORDER_WITHHOLD,
				SettleConstants.WITHHOLD_BATCH_CODE,1);
//				SettleConstants.WITHHOLD_BATCH_CODE_OLD,1);
	}
	/**
	 * 会堂T+0代付
	 * @throws ParseException 
	 */	
	public void withholdHTT0() throws ParseException{ 
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date date0903 = sdf.parse("2015-09-03");
		Date date0904 = sdf.parse("2015-09-04");
		Date dateCur = sdf.parse(sdf.format(new Date()));
		if(dateCur.compareTo(date0903)!=0 && dateCur.compareTo(date0904)!=0){
			settlementServiceThr.paymentGeneration(
					TransCodeConst.PAYMENT_WITHHOLD, Constants.HT_ID,
					SettleConstants.ORDER_WITHHOLD,
//					SettleConstants.WITHHOLD_BATCH_CODE_OLD,0);
					SettleConstants.WITHHOLD_BATCH_CODE,0);
			settlementServiceThr.paymentGeneration(
					TransCodeConst.PAYMENT_WITHHOLD, Constants.HT_CLOUD_ID,
					SettleConstants.ORDER_WITHHOLD,
//					SettleConstants.WITHHOLD_BATCH_CODE_OLD,0);
					SettleConstants.WITHHOLD_BATCH_CODE,0);
			//生成会唐代付文件并上传
			try {
				String accountDate=generationPaymentService.getAccountDate();
				String batch=settlementLogic.getBatchNo(DateUtils.getAccountDate(Constants.DATE_FORMAT_YYYYMMDD, accountDate),SettleConstants.ROP_PAYMENT_BATCH_CODE, Constants.HT_ID);
				logger.info("-------会堂T+0代付批次号------"+batch);
//				generationPaymentService.uploadPaymentFile(7,batch, Constants.HT_ID,DateUtils.getDate(accountDate, Constants.DATE_FORMAT_YYYYMMDD));
				generationPaymentService.submitToRecAndPaySys(7,Constants.HT_ID,accountDate,DateUtils.getDate(accountDate, Constants.DATE_FORMAT_YYYYMMDD));
			} catch (AccountException e) {
				logger.error("生成会堂T+0代付提交到代收付失败"+e.getMessage());
				RkylinMailUtil.sendMailThread("会堂T+0代付提交到代收付", "生成会堂T+0提交到代收付失败"+e.getMessage(), TransCodeConst.GENERATION_PAY_ERROR_TOEMAIL);
			}
		}
	}
	/**
	 * 课栈代付
	 */	
	public void withholdKZ(){ 
		settlementServiceThr.paymentGeneration(
				TransCodeConst.PAYMENT_WITHHOLD, Constants.KZ_ID,
				SettleConstants.ORDER_WITHHOLD,
				SettleConstants.WITHHOLD_CODE,1);
//		SettleConstants.WITHHOLD_CODE_OLD,1);
	}
	
	   /**
     * 课栈代付
     */ 
//    public void withholdKZT0(){ 
//        settlementServiceThr.paymentGeneration(
//                TransCodeConst.PAYMENT_WITHHOLD, Constants.KZ_ID,
//                SettleConstants.ORDER_WITHHOLD,
//                SettleConstants.WITHHOLD_CODE,0);
////      SettleConstants.WITHHOLD_CODE_OLD,1);
//    }
	
	/**
	 * 君融贷代付
	 */	
	public void withholdJRD(){ 
		settlementServiceThr.paymentGeneration(
				TransCodeConst.PAYMENT_WITHHOLD, Constants.JRD_ID,
				SettleConstants.ORDER_WITHHOLD,
				SettleConstants.WITHHOLD_CODE,1);
//		SettleConstants.WITHHOLD_CODE_OLD,1);
	}
	/**
	 * 棉庄代付
	 */	
	public void withholdMZ(){ 
		settlementServiceThr.paymentGeneration(
				TransCodeConst.PAYMENT_WITHHOLD, Constants.MZ_ID,
				SettleConstants.ORDER_WITHHOLD,
				SettleConstants.WITHHOLD_CODE,1);
//		SettleConstants.WITHHOLD_CODE_OLD,1);
	}
	/**
	 * 食全食美代付
	 */	
	public void withholdSQSM(){ 
		settlementServiceThr.paymentGeneration(
				TransCodeConst.PAYMENT_WITHHOLD, Constants.SQSM_ID,
				SettleConstants.ORDER_WITHHOLD,
//				SettleConstants.WITHHOLD_CODE_OLD,1);
		SettleConstants.WITHHOLD_CODE,1);
	}
	
	/**
	 * 展酷代付
	 */	
	public void withholdZk(){ 
		settlementServiceThr.paymentGeneration(
				TransCodeConst.PAYMENT_WITHHOLD, Constants.ZK_ID,
				SettleConstants.ORDER_WITHHOLD,
//				SettleConstants.WITHHOLD_CODE_OLD,1);
				SettleConstants.WITHHOLD_CODE,1);
	}
	
//	/**
//	 * 通信运维代付汇总
//	 */	
	public void withholdTXYW(){ 
		settlementServiceThr.paymentGeneration(
				TransCodeConst.PAYMENT_WITHHOLD, Constants.TXYW_ID,
				SettleConstants.ORDER_WITHHOLD,
				SettleConstants.WITHHOLD_CODE,1);
	}
	
    /**
     * 汇总清结算发送的40142汇总的代付并上传
     * Discription: void
     * @author Achilles
     * @since 2016年5月4日
     */
    @Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
    public void summarizing40142() {
        logger.info("--------------------汇总清结算发送的40142汇总的代付并上传,task  开始----------------------------------");
        CommonResponse res= settlementServiceThr.summarizing40142();
        if (CodeEnum.SUCCESS.getCode().equals(res.getCode())) {
            String accountDate=generationPaymentService.getAccountDate();
            generationPaymentService.submitToRecAndPaySys(7,Constants.FN_ID,accountDate,DateUtils.getDate(accountDate, Constants.DATE_FORMAT_YYYYMMDD)); 
        }else{
            logger.info("----------汇总失败--------------"+res.getMsg()); 
        }
        logger.info("--------------------汇总清结算发送的40142汇总的代付并上传,task  结束-----------------------------------");
    }
    
    /**
     * 指尖代付汇总
     */ 
    public void withholdZJDY(){ 
        settlementServiceThr.paymentGeneration(
                TransCodeConst.PAYMENT_WITHHOLD, Constants.ZJDY_ID,
                SettleConstants.ORDER_WITHHOLD,
                SettleConstants.WITHHOLD_CODE,1);
    }
   
    /**
     * 白条代付汇总
     * Discription: void
     * @author Achilles
     * @since 2016年7月19日
     */
    public void withholdFactoring(){ 
        settlementServiceThr.paymentGeneration(
                TransCodeConst.PAYMENT_WITHHOLD, Constants.FACTORING_ID,
                SettleConstants.ORDER_WITHHOLD,
                SettleConstants.WITHHOLD_CODE,1);
    }
    /**
     * 帮帮助学代付汇总
     */ 
    public void withholdBBZX(){ 
        settlementServiceThr.paymentGeneration(
                TransCodeConst.PAYMENT_WITHHOLD, Constants.BBZX_ID,
                SettleConstants.ORDER_WITHHOLD,
                SettleConstants.WITHHOLD_CODE,1);
    }

    /**
     * 房仓代付汇总
     */ 
    public void withholdFC(){ 
        settlementServiceThr.paymentGeneration(
                TransCodeConst.PAYMENT_WITHHOLD, Constants.FC_ID,
                SettleConstants.ORDER_WITHHOLD,
                SettleConstants.WITHHOLD_CODE,1);
    }
    
    /**
     * 悦视觉代付汇总
     * Discription: void
     * @author Achilles
     * @since 2016年8月29日
     */
    public void withholdYSJ1(){ 
        settlementServiceThr.paymentGeneration(
                TransCodeConst.PAYMENT_WITHHOLD, Constants.YSJ_ID,
                SettleConstants.ORDER_WITHHOLD,
                SettleConstants.WITHHOLD_CODE,1);
    }
    
    /**
     * 融数钱包代付汇总
     * Discription: void
     * @author Achilles
     * @since 2016年9月8日
     */
    public void withholdSummaryRSQB1(){ 
        settlementServiceThr.paymentGeneration(
                TransCodeConst.PAYMENT_WITHHOLD, Constants.RSQB_ID,
                SettleConstants.ORDER_WITHHOLD,
                SettleConstants.WITHHOLD_CODE,1);
    }
    
    /**
     * 领客科技代付汇总
     * Discription: void
     * @author Achilles
     * @since 2016年9月8日
     */
    public void withholdLK1(){ 
        settlementServiceThr.paymentGeneration(
                TransCodeConst.PAYMENT_WITHHOLD, Constants.LINGKE_ID,
                SettleConstants.ORDER_WITHHOLD,
                SettleConstants.WITHHOLD_CODE,1);
    }   
}
