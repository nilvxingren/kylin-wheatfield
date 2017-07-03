/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager;

import java.util.List;

import com.rkylin.wheatfield.pojo.OpenBankCode;
import com.rkylin.wheatfield.pojo.OpenBankCodeQuery;

public interface OpenBankCodeManager {
	void saveOpenBankCode(OpenBankCode openBankCode);

	OpenBankCode findOpenBankCodeById(Long id);
	
	List<OpenBankCode> queryList(OpenBankCodeQuery query);
	
	void deleteOpenBankCodeById(Long id);
	
	void deleteOpenBankCode(OpenBankCodeQuery query);

	List<OpenBankCode> queryListByCode(OpenBankCodeQuery query);
}
