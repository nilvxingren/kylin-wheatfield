/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager;

import java.util.List;
import java.util.Map;

import com.rkylin.wheatfield.pojo.SettleSplittingEntry;
import com.rkylin.wheatfield.pojo.SettleSplittingEntryQuery;

public interface SettleSplittingEntryManager {
	void saveSettleSplittingEntry(SettleSplittingEntry settleSplittingEntry);
	
	void updateSettleSplittingEntry(SettleSplittingEntry settleSplittingEntry);

	SettleSplittingEntry findSettleSplittingEntryById(Long id);
	
	List<SettleSplittingEntry> queryList(SettleSplittingEntryQuery query);
	
	void deleteSettleSplittingEntryById(Long id);
	
	void deleteSettleSplittingEntry(SettleSplittingEntryQuery query);

	int batchUpdateByOrderNo(List<?> list);
}
