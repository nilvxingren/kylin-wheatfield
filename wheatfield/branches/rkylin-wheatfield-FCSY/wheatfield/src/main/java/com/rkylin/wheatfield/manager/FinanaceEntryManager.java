/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager;

import java.util.List;

import com.rkylin.wheatfield.pojo.FinanaceEntry;
import com.rkylin.wheatfield.pojo.FinanaceEntryQuery;

public interface FinanaceEntryManager {
	int saveFinanaceEntry(FinanaceEntry finanaceEntry);
	
	void saveFinanaceEntryList(List<FinanaceEntry> finanaceEntries);

	FinanaceEntry findFinanaceEntryById(Long id);
	
	List<FinanaceEntry> queryList(FinanaceEntryQuery query);
	/**
	 * 获取账户当天记账流水额
	 * @param query
	 * @return
	 */
	List<FinanaceEntry> queryListNew(FinanaceEntryQuery query);
	
	void deleteFinanaceEntryById(Long id);
	
	void deleteFinanaceEntry(FinanaceEntryQuery query);
}
