/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager;

import java.util.List;

import com.rkylin.wheatfield.pojo.SettleTransTab;
import com.rkylin.wheatfield.pojo.SettleTransTabQuery;

public interface SettleTransTabManager {
	void saveSettleTransTab(SettleTransTab settleTransTab);

	SettleTransTab findSettleTransTabById(Long id);
	
	List<SettleTransTab> queryList(SettleTransTabQuery query);
	
	void deleteSettleTransTabById(Long id);
	
	void deleteSettleTransTab(SettleTransTabQuery query);
	
	void saveSettleTransTabs(List<SettleTransTab> settleTransTabs);
}
