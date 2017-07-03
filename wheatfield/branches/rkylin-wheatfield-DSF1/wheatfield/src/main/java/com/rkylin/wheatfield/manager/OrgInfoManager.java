/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager;

import java.util.List;

import com.rkylin.wheatfield.pojo.OrgInfo;
import com.rkylin.wheatfield.pojo.OrgInfoQuery;

public interface OrgInfoManager {
	void saveOrgInfo(OrgInfo orgInfo);

	OrgInfo findOrgInfoById(Long id);
	
	List<OrgInfo> queryList(OrgInfoQuery query);
	
	void deleteOrgInfoById(Long id);
	
	void deleteOrgInfo(OrgInfoQuery query);
}
