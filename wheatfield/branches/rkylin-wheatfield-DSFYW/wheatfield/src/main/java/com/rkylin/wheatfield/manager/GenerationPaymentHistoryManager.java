/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager;

import java.util.List;

import com.rkylin.wheatfield.pojo.GenerationPaymentHistory;
import com.rkylin.wheatfield.pojo.GenerationPaymentHistoryQuery;

public interface GenerationPaymentHistoryManager {
	void saveGenerationPaymentHistory(GenerationPaymentHistory generationPaymentHistory);

	GenerationPaymentHistory findGenerationPaymentHistoryById(Long id);
	
	List<GenerationPaymentHistory> queryList(GenerationPaymentHistoryQuery query);
	
	void deleteGenerationPaymentHistoryById(Long id);
	
	void deleteGenerationPaymentHistory(GenerationPaymentHistoryQuery query);
	
	List<GenerationPaymentHistory> selectByOnecent();
}
