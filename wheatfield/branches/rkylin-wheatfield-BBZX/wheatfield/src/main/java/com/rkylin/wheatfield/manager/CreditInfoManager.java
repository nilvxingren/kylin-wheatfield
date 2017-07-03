/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager;

import java.util.List;

import com.rkylin.wheatfield.pojo.CreditInfo;
import com.rkylin.wheatfield.pojo.CreditInfoQuery;

public interface CreditInfoManager {
	void saveCreditInfo(CreditInfo creditInfo);

	CreditInfo findCreditInfoById(Long id);
	
	List<CreditInfo> queryList(CreditInfoQuery query);
	
	void deleteCreditInfoById(Long id);
	
	void deleteCreditInfo(CreditInfoQuery query);
}
