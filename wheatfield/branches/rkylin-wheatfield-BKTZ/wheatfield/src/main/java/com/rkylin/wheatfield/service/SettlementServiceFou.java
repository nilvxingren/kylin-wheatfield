package com.rkylin.wheatfield.service;

import java.util.Map;

public interface SettlementServiceFou {
	/***
	 * 丰年&支付宝对账
	 * @param invoicedate 账期
	 * @return 提示信息
	 * @throws Exception
	 */
	public Map<String, String> collateFN2Alipay(String invoicedate) throws Exception;
}
