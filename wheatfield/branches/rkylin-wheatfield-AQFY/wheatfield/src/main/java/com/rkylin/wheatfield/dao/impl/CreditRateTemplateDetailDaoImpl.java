/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao.impl;

import java.util.List;

import com.rkylin.database.BaseDao;
import org.springframework.stereotype.Repository;

import com.rkylin.wheatfield.dao.CreditRateTemplateDetailDao;
import com.rkylin.wheatfield.pojo.CreditRateTemplateDetail;
import com.rkylin.wheatfield.pojo.CreditRateTemplateDetailQuery;

@Repository("creditRateTemplateDetailDao")
public class CreditRateTemplateDetailDaoImpl extends BaseDao implements CreditRateTemplateDetailDao {
	
	@Override
	public int countByExample(CreditRateTemplateDetailQuery example) {
		return super.getSqlSession().selectOne("CreditRateTemplateDetailMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(CreditRateTemplateDetailQuery example) {
		return super.getSqlSession().delete("CreditRateTemplateDetailMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("CreditRateTemplateDetailMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(CreditRateTemplateDetail record) {
		return super.getSqlSession().insert("CreditRateTemplateDetailMapper.insert", record);
	}
	
	@Override
	public int insertSelective(CreditRateTemplateDetail record) {
		return super.getSqlSession().insert("CreditRateTemplateDetailMapper.insertSelective", record);
	}
	
	@Override
	public List<CreditRateTemplateDetail> selectByExample(CreditRateTemplateDetailQuery example) {
		return super.getSqlSession().selectList("CreditRateTemplateDetailMapper.selectByExample", example);
	}
	
	@Override
	public CreditRateTemplateDetail selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("CreditRateTemplateDetailMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(CreditRateTemplateDetail record) {
		return super.getSqlSession().update("CreditRateTemplateDetailMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(CreditRateTemplateDetail record) {
		return super.getSqlSession().update("CreditRateTemplateDetailMapper.updateByPrimaryKey", record);
	}
	@Override
	public int updateByRateId(CreditRateTemplateDetail record){
		return super.getSqlSession().update("CreditRateTemplateDetailMapper.updateByRateId", record);		
	}

	@Override
	public int updateStatusByRateId(CreditRateTemplateDetail record){
		return super.getSqlSession().update("CreditRateTemplateDetailMapper.updateStatusByRateId", record);		
	}

}
