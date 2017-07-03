/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao;

import java.util.List;

import com.rkylin.wheatfield.pojo.CreditRateTemplate;
import com.rkylin.wheatfield.pojo.CreditRateTemplateQuery;
import com.rkylin.wheatfield.pojo.CreditRateTemplateRes;

public interface CreditRateTemplateDao {
	int countByExample(CreditRateTemplateQuery example);
	
	int deleteByExample(CreditRateTemplateQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(CreditRateTemplate record);
	
	int insertSelective(CreditRateTemplate record);
	
	List<CreditRateTemplate> selectByExample(CreditRateTemplateQuery example);
	
	CreditRateTemplate selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(CreditRateTemplate record);
	
	int updateByPrimaryKey(CreditRateTemplate record);
	
	List<CreditRateTemplateRes> selectWithJoin(CreditRateTemplateQuery example);
}
