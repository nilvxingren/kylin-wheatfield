/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao;

import java.util.List;

import com.rkylin.wheatfield.pojo.FuncCode;
import com.rkylin.wheatfield.pojo.FuncCodeQuery;

public interface FuncCodeDao {
	int countByExample(FuncCodeQuery example);
	
	int deleteByExample(FuncCodeQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(FuncCode record);
	
	int insertSelective(FuncCode record);
	
	List<FuncCode> selectByExample(FuncCodeQuery example);
	
	FuncCode selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(FuncCode record);
	
	int updateByPrimaryKey(FuncCode record);
}
