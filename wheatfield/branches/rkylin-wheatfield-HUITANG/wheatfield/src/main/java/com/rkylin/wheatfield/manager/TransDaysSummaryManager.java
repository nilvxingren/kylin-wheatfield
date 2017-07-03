/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager;

import java.util.List;

import com.rkylin.wheatfield.pojo.TransDaysSummary;
import com.rkylin.wheatfield.pojo.TransDaysSummaryQuery;

public interface TransDaysSummaryManager {
	void saveTransDaysSummary(TransDaysSummary transDaysSummary);

	TransDaysSummary findTransDaysSummaryById(String id);
	
	List<TransDaysSummary> queryList(TransDaysSummaryQuery query);
	
	void deleteTransDaysSummaryById(Long id);
	
	void deleteTransDaysSummary(TransDaysSummaryQuery query);

	void updateTransDaysSummary(TransDaysSummary query);
}
