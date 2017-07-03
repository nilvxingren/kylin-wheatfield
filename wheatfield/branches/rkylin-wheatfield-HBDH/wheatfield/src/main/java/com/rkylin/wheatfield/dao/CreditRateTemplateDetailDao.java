/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao;

import java.util.List;

import com.rkylin.wheatfield.pojo.CreditRateTemplateDetail;
import com.rkylin.wheatfield.pojo.CreditRateTemplateDetailQuery;

public interface CreditRateTemplateDetailDao {
	int countByExample(CreditRateTemplateDetailQuery example);
	
	int deleteByExample(CreditRateTemplateDetailQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(CreditRateTemplateDetail record);
	
	int insertSelective(CreditRateTemplateDetail record);
	
	List<CreditRateTemplateDetail> selectByExample(CreditRateTemplateDetailQuery example);
	
	CreditRateTemplateDetail selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(CreditRateTemplateDetail record);
	
	int updateByPrimaryKey(CreditRateTemplateDetail record);
	
	int updateByRateId(CreditRateTemplateDetail record);
	
	int updateStatusByRateId(CreditRateTemplateDetail record);
}
