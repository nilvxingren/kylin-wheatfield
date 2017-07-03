/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao;

import java.util.List;

import com.rkylin.wheatfield.pojo.FinanaceAccountAuth;
import com.rkylin.wheatfield.pojo.FinanaceAccountAuthQuery;

public interface FinanaceAccountAuthDao {
	int countByExample(FinanaceAccountAuthQuery example);
	
	int deleteByExample(FinanaceAccountAuthQuery example);
	
	int deleteByPrimaryKey(Long finAntAuthId);
	
	int insert(FinanaceAccountAuth record);
	
	int insertSelective(FinanaceAccountAuth record);
	
	List<FinanaceAccountAuth> selectByExample(FinanaceAccountAuthQuery example);
	
	FinanaceAccountAuth selectByPrimaryKey(Long finAntAuthId);
	
	int updateByPrimaryKeySelective(FinanaceAccountAuth record);
	
	int updateByPrimaryKey(FinanaceAccountAuth record);
}
