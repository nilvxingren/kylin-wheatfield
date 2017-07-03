/**
 * @File name : BankAccountService.java
 * @Package : com.rkylin.wheatfield.service
 * @Description : TODO(账户银行信息Service接口层)
 * @Creator : liuhuan
 * @CreateTime : 2015年11月20日 下午3:07:15
 * @Version : 1.0
 */
package com.rkylin.wheatfield.service;

public interface BankAccountService {
	/**
	 * @Description : TODO(共用银行账户信息接口)
	 * @CreateTime : 2015年11月20日 下午3:30:07
	 */
	public String shareBankCardInfo(String providerId,String shareId,String consId,String productId,String bankCardNo);
}
