/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao;

import java.util.List;

import com.rkylin.wheatfield.pojo.AccountPassword;
import com.rkylin.wheatfield.pojo.AccountPasswordQuery;

public interface AccountPasswordDao {
	int countByExample(AccountPasswordQuery example);
	
	int deleteByExample(AccountPasswordQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(AccountPassword record);
	
	int insertSelective(AccountPassword record);
	
	List<AccountPassword> selectByExample(AccountPasswordQuery example);
	
	AccountPassword selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(AccountPassword record);
	
	int updateByPrimaryKey(AccountPassword record);
}
