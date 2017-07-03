/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager;

import java.util.List;

import com.rkylin.wheatfield.pojo.SettleBatchResult;
import com.rkylin.wheatfield.pojo.SettleBatchResultQuery;

public interface SettleBatchResultManager {
	void saveSettleBatchResult(SettleBatchResult settleBatchResult);

	SettleBatchResult findSettleBatchResultById(Long id);
	
	List<SettleBatchResult> queryList(SettleBatchResultQuery query);
	
	void deleteSettleBatchResultById(Long id);
	
	void deleteSettleBatchResult(SettleBatchResultQuery query);
}
