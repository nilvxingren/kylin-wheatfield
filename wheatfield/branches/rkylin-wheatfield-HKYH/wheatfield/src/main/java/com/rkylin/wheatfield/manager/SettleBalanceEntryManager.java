/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager;

import java.util.List;

import com.rkylin.wheatfield.pojo.SettleBalanceEntry;
import com.rkylin.wheatfield.pojo.SettleBalanceEntryQuery;

public interface SettleBalanceEntryManager {
	void saveSettleBalanceEntry(SettleBalanceEntry settleBalanceEntry);

	SettleBalanceEntry findSettleBalanceEntryById(Long id);
	
	List<SettleBalanceEntry> queryList(SettleBalanceEntryQuery query);
	
	void deleteSettleBalanceEntryById(Long id);
	
	void deleteSettleBalanceEntry(SettleBalanceEntryQuery query);
}
