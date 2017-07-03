package com.rkylin.wheatfield.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.service.InterestRepaymentHTService;

@Component("interesTask")
public class InteresTask {
	private static final Logger logger = LoggerFactory.getLogger(InteresTask.class);
	@Autowired
	private InterestRepaymentHTService interestRepaymentHTService;
	
	/**
	 *定时 还款任务
	 */
	public void repaymentTask(){
		logger.info("=================================开始定时还款任务 interesTask===========================");
		//通过转账还款
		interestRepaymentHTService.repayment("1", Constants.HT_ID, "", "");
		logger.info("=================================定时还款任务结束 interesTask===========================");
	}

	public void uploadHTRepaymentFileTask(){
		logger.info("===============================开始上传会唐还款明细文件任务=============================");
		interestRepaymentHTService.uploadIneterestRepaymentFile(Constants.HT_ID,Constants.P2P_ID);
		logger.info("=====================================会唐上传明细文件任务结束==============================");
	}

}
