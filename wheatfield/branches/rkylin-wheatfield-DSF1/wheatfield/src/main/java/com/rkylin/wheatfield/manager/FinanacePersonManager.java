/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager;

import java.util.List;

import com.rkylin.wheatfield.pojo.FinanaceCompany;
import com.rkylin.wheatfield.pojo.FinanacePerson;
import com.rkylin.wheatfield.pojo.FinanacePersonQuery;

public interface FinanacePersonManager {
	void saveFinanacePerson(FinanacePerson finanacePerson);

	FinanacePerson findFinanacePersonById(Long id);
	
	List<FinanacePerson> queryList(FinanacePersonQuery query);
	
	List<FinanacePerson> queryListT(FinanacePersonQuery query);
	
	List<FinanacePerson> queryListBatch(FinanacePersonQuery query);
	
	void deleteFinanacePersonById(Long id);
	
	void deleteFinanacePerson(FinanacePersonQuery query);
	
	void updateFinanacePersonByFinanaceAccountId(FinanacePerson finanacePerson);
}
