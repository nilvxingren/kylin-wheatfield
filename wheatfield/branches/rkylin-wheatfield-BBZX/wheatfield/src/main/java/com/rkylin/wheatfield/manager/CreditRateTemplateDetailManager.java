/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager;

import java.util.List;

import com.rkylin.wheatfield.pojo.CreditRateTemplateDetail;
import com.rkylin.wheatfield.pojo.CreditRateTemplateDetailQuery;

public interface CreditRateTemplateDetailManager {
	void saveCreditRateTemplateDetail(CreditRateTemplateDetail creditRateTemplateDetail);

	CreditRateTemplateDetail findCreditRateTemplateDetailById(Long id);
	
	List<CreditRateTemplateDetail> queryList(CreditRateTemplateDetailQuery query);
	
	void deleteCreditRateTemplateDetailById(Long id);
	
	void deleteCreditRateTemplateDetail(CreditRateTemplateDetailQuery query);
	
	void updateCreditRateTemplateDetail(CreditRateTemplateDetail creditRateTemplateDetail);
	
	void updateStatusByRateId(CreditRateTemplateDetail creditRateTemplateDetail);
}
