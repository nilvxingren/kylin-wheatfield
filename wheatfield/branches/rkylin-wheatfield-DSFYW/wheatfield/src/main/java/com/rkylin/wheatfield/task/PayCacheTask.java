package com.rkylin.wheatfield.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.rkylin.wheatfield.service.GenerationPaymentService;
import com.rkylin.wheatfield.service.TransOrderService;
import com.rkylin.wheatfield.utils.DateUtil;

public class PayCacheTask {
	
	private static final Logger logger = LoggerFactory.getLogger(PayCacheTask.class);
	
	@Autowired
	private GenerationPaymentService generationPaymentService;
	
	@Autowired
	private TransOrderService transOrderService;
	
	/**
	 * 定时处理缓存中代收付推入的数据
	 */
	public void payCache(){
//		logger.info("处理缓存中代收付推送的数据-----------开始------------------");
		generationPaymentService.handleRecAndPayCacheData();
//		logger.info("处理缓存中代收付推送的数据-----------结束------------------");
	}
	
    /**
     * 定时处理缓存中清结算推入的数据
     */
    public void processRecAndPayCacheResults(){
        transOrderService.manageRecAndPayCacheResults(null);
    }	
}
