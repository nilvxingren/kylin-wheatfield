/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager;

import java.util.List;

import com.rkylin.wheatfield.pojo.ManageHistory;
import com.rkylin.wheatfield.pojo.ManageHistoryQuery;

public interface ManageHistoryManager {
	void saveManageHistory(ManageHistory manageHistory);

	ManageHistory findManageHistoryById(Long id);
	
	List<ManageHistory> queryList(ManageHistoryQuery query);
	
	void deleteManageHistoryById(Long id);
	
	void deleteManageHistory(ManageHistoryQuery query);
}
