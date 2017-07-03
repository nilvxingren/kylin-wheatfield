/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager;

import java.util.List;

import com.rkylin.wheatfield.pojo.CurrencyInfo;
import com.rkylin.wheatfield.pojo.CurrencyInfoQuery;

public interface CurrencyInfoManager {
	void saveCurrencyInfo(CurrencyInfo currencyInfo);

	CurrencyInfo findCurrencyInfoById(Long id);
	
	List<CurrencyInfo> queryList(CurrencyInfoQuery query);
	
	void deleteCurrencyInfoById(Long id);
	
	void deleteCurrencyInfo(CurrencyInfoQuery query);
}
