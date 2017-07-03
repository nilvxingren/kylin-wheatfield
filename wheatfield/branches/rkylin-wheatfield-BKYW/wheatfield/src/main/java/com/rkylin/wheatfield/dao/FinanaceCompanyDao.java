/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao;

import java.util.List;

import com.rkylin.wheatfield.pojo.FinanaceCompany;
import com.rkylin.wheatfield.pojo.FinanaceCompanyQuery;

public interface FinanaceCompanyDao {
	int countByExample(FinanaceCompanyQuery example);
	
	int deleteByExample(FinanaceCompanyQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(FinanaceCompany record);
	
	int insertSelective(FinanaceCompany record);
	
	List<FinanaceCompany> selectByExample(FinanaceCompanyQuery example);
	
	List<FinanaceCompany> selectByRootInstCdOrBUSLINCEOrStatusId(FinanaceCompanyQuery example);
	
	FinanaceCompany selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(FinanaceCompany record);
	
	int updateByPrimaryKey(FinanaceCompany record);

	int updateByFinanaceAccountId(FinanaceCompany record);
	
	List<FinanaceCompany> getFinanaceCompanies(FinanaceCompanyQuery query);
}
