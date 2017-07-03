/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager;

import java.util.List;

import com.rkylin.wheatfield.pojo.FinanaceAccountAuth;
import com.rkylin.wheatfield.pojo.FinanaceAccountAuthQuery;

public interface FinanaceAccountAuthManager {
	void saveFinanaceAccountAuth(FinanaceAccountAuth finanaceAccountAuth);

	FinanaceAccountAuth findFinanaceAccountAuthById(Long finAntAuthId);
	
	List<FinanaceAccountAuth> queryList(FinanaceAccountAuthQuery query);
	
	void deleteFinanaceAccountAuthById(Long finAntAuthId);
	
	void deleteFinanaceAccountAuth(FinanaceAccountAuthQuery query);
}
