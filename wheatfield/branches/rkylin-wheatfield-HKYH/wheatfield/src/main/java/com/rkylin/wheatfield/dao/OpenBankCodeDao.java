/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao;

import java.util.List;

import com.rkylin.wheatfield.pojo.OpenBankCode;
import com.rkylin.wheatfield.pojo.OpenBankCodeQuery;

public interface OpenBankCodeDao {
	int countByExample(OpenBankCodeQuery example);
	
	int deleteByExample(OpenBankCodeQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(OpenBankCode record);
	
	int insertSelective(OpenBankCode record);
	
	List<OpenBankCode> selectByExample(OpenBankCodeQuery example);
	
	OpenBankCode selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(OpenBankCode record);
	
	int updateByPrimaryKey(OpenBankCode record);

	List<OpenBankCode> selectByCode(OpenBankCodeQuery example);
}
