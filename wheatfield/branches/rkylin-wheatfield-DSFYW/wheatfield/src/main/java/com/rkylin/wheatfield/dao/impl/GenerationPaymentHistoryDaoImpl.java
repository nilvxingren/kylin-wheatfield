/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.wheatfield.dao.GenerationPaymentHistoryDao;
import com.rkylin.wheatfield.pojo.GenerationPaymentHistory;
import com.rkylin.wheatfield.pojo.GenerationPaymentHistoryQuery;
import com.rkylin.database.BaseDao;

@Repository("generationPaymentHistoryDao")
public class GenerationPaymentHistoryDaoImpl extends BaseDao implements GenerationPaymentHistoryDao {
	
	@Override
	public int countByExample(GenerationPaymentHistoryQuery example) {
		return super.getSqlSession().selectOne("GenerationPaymentHistoryMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(GenerationPaymentHistoryQuery example) {
		return super.getSqlSession().delete("GenerationPaymentHistoryMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("GenerationPaymentHistoryMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(GenerationPaymentHistory record) {
		return super.getSqlSession().insert("GenerationPaymentHistoryMapper.insert", record);
	}
	
	@Override
	public int insertSelective(GenerationPaymentHistory record) {
		return super.getSqlSession().insert("GenerationPaymentHistoryMapper.insertSelective", record);
	}
	
	@Override
	public List<GenerationPaymentHistory> selectByExample(GenerationPaymentHistoryQuery example) {
		return super.getSqlSession().selectList("GenerationPaymentHistoryMapper.selectByExample", example);
	}
	
	@Override
	public GenerationPaymentHistory selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("GenerationPaymentHistoryMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(GenerationPaymentHistory record) {
		return super.getSqlSession().update("GenerationPaymentHistoryMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(GenerationPaymentHistory record) {
		return super.getSqlSession().update("GenerationPaymentHistoryMapper.updateByPrimaryKey", record);
	}

	@Override
	public List<GenerationPaymentHistory> selectByOnecent() {
		return super.getSqlSession().selectList("GenerationPaymentHistoryMapper.selectByOnecent");
	}

	@Override
	public List<GenerationPaymentHistory> selectPayResOfJudgePubAccount() {
		return super.getSqlSession().selectList("GenerationPaymentHistoryMapper.selectPayResOfJudgePubAccount");
	}
	
}
