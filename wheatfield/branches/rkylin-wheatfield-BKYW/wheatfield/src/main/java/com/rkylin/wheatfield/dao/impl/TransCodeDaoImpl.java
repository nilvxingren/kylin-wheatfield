/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.wheatfield.dao.TransCodeDao;
import com.rkylin.wheatfield.pojo.TransCode;
import com.rkylin.wheatfield.pojo.TransCodeQuery;
import com.rkylin.database.BaseDao;

@Repository("transCodeDao")
public class TransCodeDaoImpl extends BaseDao implements TransCodeDao {
	
	@Override
	public int countByExample(TransCodeQuery example) {
		return super.getSqlSession().selectOne("TransCodeMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(TransCodeQuery example) {
		return super.getSqlSession().delete("TransCodeMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("TransCodeMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(TransCode record) {
		return super.getSqlSession().insert("TransCodeMapper.insert", record);
	}
	
	@Override
	public int insertSelective(TransCode record) {
		return super.getSqlSession().insert("TransCodeMapper.insertSelective", record);
	}
	
	@Override
	public List<TransCode> selectByExample(TransCodeQuery example) {
		return super.getSqlSession().selectList("TransCodeMapper.selectByExample", example);
	}
	
	@Override
	public TransCode selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("TransCodeMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(TransCode record) {
		return super.getSqlSession().update("TransCodeMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(TransCode record) {
		return super.getSqlSession().update("TransCodeMapper.updateByPrimaryKey", record);
	}
	
}
