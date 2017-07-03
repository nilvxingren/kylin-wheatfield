/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager;

import java.util.List;

import com.rkylin.wheatfield.pojo.FuncCode;
import com.rkylin.wheatfield.pojo.FuncCodeQuery;

public interface FuncCodeManager {
	void saveFuncCode(FuncCode funcCode);

	FuncCode findFuncCodeById(Long id);
	
	List<FuncCode> queryList(FuncCodeQuery query);
	
	void deleteFuncCodeById(Long id);
	
	void deleteFuncCode(FuncCodeQuery query);
}
