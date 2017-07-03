package com.rkylin.wheatfield.service;

import com.Rop.api.domain.CreditRepaymentInfo;

public interface CreditRepaymentHistoryService {
	/**
	 * 还款信息
	 * @return
	 */
	public CreditRepaymentInfo getCreditrepaymentHistorys(String userId,String constid,String creditDate,String tradeFlowNo);
	
}
