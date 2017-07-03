/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.wheatfield.dao.CreditRateTemplateDao;
import com.rkylin.wheatfield.pojo.CreditRateTemplate;
import com.rkylin.wheatfield.pojo.CreditRateTemplateQuery;
import com.rkylin.wheatfield.pojo.CreditRateTemplateRes;
import com.rkylin.database.BaseDao;

@Repository("creditRateTemplateDao")
public class CreditRateTemplateDaoImpl extends BaseDao implements CreditRateTemplateDao {
	
	@Override
	public int countByExample(CreditRateTemplateQuery example) {
		return super.getSqlSession().selectOne("CreditRateTemplateMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(CreditRateTemplateQuery example) {
		return super.getSqlSession().delete("CreditRateTemplateMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("CreditRateTemplateMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(CreditRateTemplate record) {
		return super.getSqlSession().insert("CreditRateTemplateMapper.insert", record);
	}
	
	@Override
	public int insertSelective(CreditRateTemplate record) {
		return super.getSqlSession().insert("CreditRateTemplateMapper.insertSelective", record);
	}
	
	@Override
	public List<CreditRateTemplate> selectByExample(CreditRateTemplateQuery example) {
		return super.getSqlSession().selectList("CreditRateTemplateMapper.selectByExample", example);
	}
	
	@Override
	public CreditRateTemplate selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("CreditRateTemplateMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(CreditRateTemplate record) {
		return super.getSqlSession().update("CreditRateTemplateMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(CreditRateTemplate record) {
		return super.getSqlSession().update("CreditRateTemplateMapper.updateByPrimaryKey", record);
	}
	
	@Override
	public List<CreditRateTemplateRes> selectWithJoin(CreditRateTemplateQuery example) {
		return super.getSqlSession().selectList("CreditRateTemplateMapper.selectInnerDetail", example);
	}	
}
