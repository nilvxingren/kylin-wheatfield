/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager;

import java.util.List;

import com.rkylin.wheatfield.pojo.FinanaceCompany;
import com.rkylin.wheatfield.pojo.FinanaceCompanyQuery;

public interface FinanaceCompanyManager {
	void saveFinanaceCompany(FinanaceCompany finanaceCompany);

	FinanaceCompany findFinanaceCompanyById(Long id);
	
	List<FinanaceCompany> queryList(FinanaceCompanyQuery query);
	
	List<FinanaceCompany> queryListT(FinanaceCompanyQuery query);
	
	void deleteFinanaceCompanyById(Long id);
	
	void deleteFinanaceCompany(FinanaceCompanyQuery query);

	void updateFinanaceCompanyByFinanaceAccountId(FinanaceCompany finanaceCompany);
	
	List<FinanaceCompany> getFinanaceCompanies(FinanaceCompanyQuery query);
}
