/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager;

import java.util.List;

import com.rkylin.wheatfield.pojo.TransCode;
import com.rkylin.wheatfield.pojo.TransCodeQuery;

public interface TransCodeManager {
	void saveTransCode(TransCode transCode);

	TransCode findTransCodeById(Long id);
	
	List<TransCode> queryList(TransCodeQuery query);
	
	void deleteTransCodeById(Long id);
	
	void deleteTransCode(TransCodeQuery query);
}
