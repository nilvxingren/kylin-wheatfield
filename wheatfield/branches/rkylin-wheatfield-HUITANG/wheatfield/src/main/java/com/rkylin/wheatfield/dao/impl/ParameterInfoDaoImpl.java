/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.wheatfield.dao.ParameterInfoDao;
import com.rkylin.wheatfield.pojo.ParameterInfo;
import com.rkylin.wheatfield.pojo.ParameterInfoQuery;
import com.rkylin.database.BaseDao;

@Repository("parameterInfoDao")
public class ParameterInfoDaoImpl extends BaseDao implements ParameterInfoDao {
	
	@Override
	public int countByExample(ParameterInfoQuery example) {
		return super.getSqlSession().selectOne("ParameterInfoMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(ParameterInfoQuery example) {
		return super.getSqlSession().delete("ParameterInfoMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("ParameterInfoMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(ParameterInfo record) {
		return super.getSqlSession().insert("ParameterInfoMapper.insert", record);
	}
	
	@Override
	public int insertSelective(ParameterInfo record) {
		return super.getSqlSession().insert("ParameterInfoMapper.insertSelective", record);
	}
	
	@Override
	public List<ParameterInfo> selectByExample(ParameterInfoQuery example) {
		return super.getSqlSession().selectList("ParameterInfoMapper.selectByExample", example);
	}
	
	@Override
	public ParameterInfo selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("ParameterInfoMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(ParameterInfo record) {
		return super.getSqlSession().update("ParameterInfoMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(ParameterInfo record) {
		return super.getSqlSession().update("ParameterInfoMapper.updateByPrimaryKey", record);
	}

	@Override
	public List<ParameterInfo> selectAllowErrorCount(ParameterInfoQuery example) {
		return super.getSqlSession().selectList("ParameterInfoMapper.selectAllowErrorCount", example);
	}
	
}
