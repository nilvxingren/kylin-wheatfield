/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager;

import java.util.List;

import com.rkylin.wheatfield.pojo.FinanaceEntryHistory;
import com.rkylin.wheatfield.pojo.FinanaceEntryHistoryQuery;

public interface FinanaceEntryHistoryManager {
	void saveFinanaceEntryHistory(FinanaceEntryHistory finanaceEntryHistory);

	FinanaceEntryHistory findFinanaceEntryHistoryById(Long id);
	
	List<FinanaceEntryHistory> queryList(FinanaceEntryHistoryQuery query);
	
	void deleteFinanaceEntryHistoryById(Long id);
	
	void deleteFinanaceEntryHistory(FinanaceEntryHistoryQuery query);
}
