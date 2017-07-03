/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao;

import java.util.List;

import com.rkylin.wheatfield.pojo.TransCode;
import com.rkylin.wheatfield.pojo.TransCodeQuery;

public interface TransCodeDao {
	int countByExample(TransCodeQuery example);
	
	int deleteByExample(TransCodeQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(TransCode record);
	
	int insertSelective(TransCode record);
	
	List<TransCode> selectByExample(TransCodeQuery example);
	
	TransCode selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(TransCode record);
	
	int updateByPrimaryKey(TransCode record);
}
