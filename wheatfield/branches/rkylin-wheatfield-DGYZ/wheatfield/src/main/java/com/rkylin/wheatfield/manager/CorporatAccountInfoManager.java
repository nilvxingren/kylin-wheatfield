/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager;

import java.util.List;

import com.rkylin.wheatfield.pojo.CorporatAccountInfo;
import com.rkylin.wheatfield.pojo.CorporatAccountInfoQuery;
import com.rkylin.wheatfield.pojo.CorporatAccountInfoScopeQuery;

public interface CorporatAccountInfoManager {
	void saveCorporatAccountInfo(CorporatAccountInfo corporatAccountInfo);

	CorporatAccountInfo findCorporatAccountInfoById(Long id);
	
	List<CorporatAccountInfo> queryList(CorporatAccountInfoQuery query);
	
	List<CorporatAccountInfo> queryListScope(CorporatAccountInfoScopeQuery query);
	
	void deleteCorporatAccountInfoById(Long id);
	
	void deleteCorporatAccountInfo(CorporatAccountInfoQuery query);
	
	int insertByList(List<CorporatAccountInfo> corporatAccountInfoList);
	
	int updateCorporatAccountInfo(CorporatAccountInfo corporatAccountInfo);
}
