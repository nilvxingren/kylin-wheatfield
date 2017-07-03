package com.rkylin.wheatfield.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.rkylin.wheatfield.common.DateUtils;
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.constant.SettleConstants;
import com.rkylin.wheatfield.service.GenerationPaymentService;
import com.rkylin.wheatfield.settlement.SettlementLogic;

public class UploadPaymentFile {
	private static Logger logger = LoggerFactory
			.getLogger(UploadPaymentFile.class);
	@Autowired
	GenerationPaymentService generationPaymentService;
	@Autowired
	SettlementLogic settlementLogic;

	private int type;
	private String batch;
	private String rootInstCd;
	private static Object lock = new Object();

	public void uploadPaymentFiles() {
		synchronized (lock) {
			try {
				logger.info("-------------------------扫描数据库生成文件上传开始-------------------------------");
				//通联代收 type=6  ，代付7	
				String accountDate=generationPaymentService.getAccountDate();
				if(type==6){
					this.setBatch(settlementLogic.getBatchNo(DateUtils.getAccountDate(Constants.DATE_FORMAT_YYYYMMDD, accountDate), SettleConstants.ROP_RECEIVE_BATCH_CODE, rootInstCd));
					logger.info("--------代收批次号-------"+batch);
				}else if(type==7){
					this.setBatch(settlementLogic.getBatchNo(DateUtils.getAccountDate(Constants.DATE_FORMAT_YYYYMMDD, accountDate), SettleConstants.ROP_PAYMENT_BATCH_CODE, rootInstCd));
					logger.info("--------代付批次号-------"+batch);
				}
				generationPaymentService.uploadPaymentFile(type, batch,
						rootInstCd,DateUtils.getAccountDate(Constants.DATE_FORMAT_YYYYMMDD, accountDate));
				logger.info("--------------------------扫描数据库生成文件上传任务结束--------------------------------------");
			} catch (Exception e) {
				logger.error(e.getMessage());
				logger.info("--------------------------扫描数据库生成文件上传任务异常--------------------------------------");
			}
		}
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public String getRootInstCd() {
		return rootInstCd;
	}

	public void setRootInstCd(String rootInstCd) {
		this.rootInstCd = rootInstCd;
	}
}
