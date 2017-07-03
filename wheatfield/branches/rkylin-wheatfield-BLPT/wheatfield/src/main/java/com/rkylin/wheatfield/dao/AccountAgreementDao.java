/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao;

import java.util.List;

import com.rkylin.wheatfield.pojo.AccountAgreement;
import com.rkylin.wheatfield.pojo.AccountAgreementQuery;

public interface AccountAgreementDao {
	int countByExample(AccountAgreementQuery example);
	
	int deleteByExample(AccountAgreementQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(AccountAgreement record);
	
	int insertSelective(AccountAgreement record);
	
	List<AccountAgreement> selectByExample(AccountAgreementQuery example);
	
	AccountAgreement selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(AccountAgreement record);
	
	int updateByPrimaryKey(AccountAgreement record);
}
