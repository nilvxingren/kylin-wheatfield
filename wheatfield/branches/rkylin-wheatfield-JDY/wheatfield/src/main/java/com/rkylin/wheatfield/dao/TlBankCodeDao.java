/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao;

import java.util.List;

import com.rkylin.wheatfield.pojo.TlBankCode;
import com.rkylin.wheatfield.pojo.TlBankCodeQuery;

public interface TlBankCodeDao {
	int countByExample(TlBankCodeQuery example);
	
	int deleteByExample(TlBankCodeQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(TlBankCode record);
	
	int insertSelective(TlBankCode record);
	
	List<TlBankCode> selectByExample(TlBankCodeQuery example);
	
	TlBankCode selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(TlBankCode record);
	
	int updateByPrimaryKey(TlBankCode record);
}
