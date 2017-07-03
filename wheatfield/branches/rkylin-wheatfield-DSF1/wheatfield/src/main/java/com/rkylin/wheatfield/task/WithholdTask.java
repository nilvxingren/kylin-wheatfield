package com.rkylin.wheatfield.task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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
//	 * 一分钱代付
//	 */	
//	public void withholdONECENT(){ 
//		settlementServiceThr.paymentGeneration(
//				TransCodeConst.PAYMENT_WITHHOLD, null,
//				SettleConstants.ORDER_WITHHOLD,
//				SettleConstants.WITHHOLD_CODE,0);
////		SettleConstants.WITHHOLD_CODE_OLD,1);
//	}
}
