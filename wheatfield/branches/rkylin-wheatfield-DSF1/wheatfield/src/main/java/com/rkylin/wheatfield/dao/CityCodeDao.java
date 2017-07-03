/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao;

import java.util.List;

import com.rkylin.wheatfield.pojo.CityCode;
import com.rkylin.wheatfield.pojo.CityCodeQuery;

public interface CityCodeDao {
	int countByExample(CityCodeQuery example);
	
	int deleteByExample(CityCodeQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(CityCode record);
	
	int insertSelective(CityCode record);
	
	List<CityCode> selectByExample(CityCodeQuery example);
	
	CityCode selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(CityCode record);
	
	int updateByPrimaryKey(CityCode record);

	List<CityCode> selectByCode(CityCodeQuery example);
}
