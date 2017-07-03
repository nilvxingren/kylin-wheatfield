/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager;

import java.util.List;

import com.rkylin.wheatfield.pojo.GenerationPayment;
import com.rkylin.wheatfield.pojo.GenerationPaymentQuery;

public interface GenerationPaymentManager {
	void saveGenerationPayment(GenerationPayment generationPayment);

	GenerationPayment findGenerationPaymentById(Long id);
	
	List<GenerationPayment> queryList(GenerationPaymentQuery query);
	
	void deleteGenerationPaymentById(Long id);
	
	void deleteGenerationPayment(GenerationPaymentQuery query);

	void batchUpdate(List<?> list);
	
	void batchUpdateByOrderNoRootInstCd(List<?> list);

	List<GenerationPayment> queryListByOrderType(GenerationPaymentQuery query);
	
	List<GenerationPayment> selectByOrderNo(String[]  orderNo);
}
