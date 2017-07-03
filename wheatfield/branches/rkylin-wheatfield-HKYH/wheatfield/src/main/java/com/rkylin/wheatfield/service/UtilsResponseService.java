package com.rkylin.wheatfield.service;

import com.rkylin.wheatfield.pojo.AccountXqsDetial;

public interface UtilsResponseService {

	/**
	 * 返回 账单日&还款日
	 * @author wanghl
	 * @param parameterId 参数ID
	 * @return 参数code(账单日&还款日)
	 * 
	 * */
	String getParameterInfo(long parameterId);
	/**
	 * 签约检查
	 * @param user 用户信息
	 * @param acct  账号
	 * @param agreementNo  协议号
	 * @return
	 */
	AccountXqsDetial getAqsDetial(String merchantId, String acct,String agreementNo);
	/**
	 * 签约导入
	 * @param req_sn 交易流水号商户号+唯一标识流水
	 * @param contractno  合同号
	 * @param merchant_id  商户代码
	 * @param signinfodetailarray  签约信息明细集合
	 * @return
	 */
	AccountXqsDetial impSignmessage(String req_sn, String contractno,
			String merchant_id, String signinfodetailarray)throws Exception;
}
