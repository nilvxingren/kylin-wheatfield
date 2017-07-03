/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.rkylin.wheatfield.dao.GenerationPaymentDao;
import com.rkylin.wheatfield.pojo.GenerationPayment;
import com.rkylin.wheatfield.pojo.GenerationPaymentQuery;
import com.rkylin.database.BaseDao;

@Repository("generationPaymentDao")
public class GenerationPaymentDaoImpl extends BaseDao implements GenerationPaymentDao {
	
	@Override
	public int countByExample(GenerationPaymentQuery example) {
		return super.getSqlSession().selectOne("GenerationPaymentMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(GenerationPaymentQuery example) {
		return super.getSqlSession().delete("GenerationPaymentMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("GenerationPaymentMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(GenerationPayment record) {
		return super.getSqlSession().insert("GenerationPaymentMapper.insert", record);
	}
	
	@Override
	public int insertSelective(GenerationPayment record) {
		return super.getSqlSession().insert("GenerationPaymentMapper.insertSelective", record);
	}
	
	public void insertBatch(List<GenerationPayment> list) {
		super.getSqlSession().insert("GenerationPaymentMapper.insertBatch", list);
	}
	
	@Override
	public List<GenerationPayment> selectByExample(GenerationPaymentQuery example) {
		return super.getSqlSession().selectList("GenerationPaymentMapper.selectByExample", example);
	}
	
	@Override
	public List<GenerationPayment> selectByOrderNo(String[]  orderNo) {
		return super.getSqlSession().selectList("GenerationPaymentMapper.selectByOrderNo", orderNo);
	}
	
	public List<GenerationPayment> selectByOrderNoAndBatch(String batch,String[]  orderNoArray){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("batch", batch);
		map.put("orderNo", orderNoArray);
		return super.getSqlSession().selectList("GenerationPaymentMapper.selectByOrderNoAndBatch", map);
	}
	
	@Override
	public List<GenerationPayment> selectByOrderType(GenerationPaymentQuery example) {
		return super.getSqlSession().selectList("GenerationPaymentMapper.selectByOrderType", example);
	}
	@Override
	public GenerationPayment selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("GenerationPaymentMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(GenerationPayment record) {
		return super.getSqlSession().update("GenerationPaymentMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(GenerationPayment record) {
		return super.getSqlSession().update("GenerationPaymentMapper.updateByPrimaryKey", record);
	}
	@Override
	public int batchUpdate( List<?> list){
		return super.batchUpdate("GenerationPaymentMapper.updateByPrimaryKeySelective", list);
	}
	
	@Override
	public int batchUpdateByOrderNoRootInstCd( List<?> list){
		return super.batchUpdate("GenerationPaymentMapper.updateByOrderNoRootInstCdSelective", list);
	}
}
