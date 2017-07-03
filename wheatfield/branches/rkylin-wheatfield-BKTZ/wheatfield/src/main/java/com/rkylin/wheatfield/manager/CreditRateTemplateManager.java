/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager;

import java.util.List;

import com.rkylin.wheatfield.pojo.CreditRateTemplate;
import com.rkylin.wheatfield.pojo.CreditRateTemplateQuery;
import com.rkylin.wheatfield.pojo.CreditRateTemplateRes;

public interface CreditRateTemplateManager {
	void saveCreditRateTemplate(CreditRateTemplate creditRateTemplate);

	CreditRateTemplate findCreditRateTemplateById(Long id);
	
	List<CreditRateTemplate> queryList(CreditRateTemplateQuery query);
	
	void deleteCreditRateTemplateById(Long id);
	
	void deleteCreditRateTemplate(CreditRateTemplateQuery query);
	
	void insertCreditRateTemplate(CreditRateTemplate creditRateTemplate);
	
	List<CreditRateTemplateRes> queryListWithJoin(CreditRateTemplateQuery query);
	 
	int countByExample(CreditRateTemplateQuery query);
}
