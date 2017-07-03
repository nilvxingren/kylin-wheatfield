/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager;

import java.util.List;

import com.rkylin.wheatfield.pojo.TlBankCode;
import com.rkylin.wheatfield.pojo.TlBankCodeQuery;

public interface TlBankCodeManager {
	void saveTlBankCode(TlBankCode tlBankCode);

	TlBankCode findTlBankCodeById(Long id);
	
	List<TlBankCode> queryList(TlBankCodeQuery query);
	
	void deleteTlBankCodeById(Long id);
	
	void deleteTlBankCode(TlBankCodeQuery query);
}
