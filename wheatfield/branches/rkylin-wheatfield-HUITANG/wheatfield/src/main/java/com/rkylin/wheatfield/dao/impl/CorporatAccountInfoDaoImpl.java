/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.rkylin.wheatfield.dao.CorporatAccountInfoDao;
import com.rkylin.wheatfield.pojo.CorporatAccountInfo;
import com.rkylin.wheatfield.pojo.CorporatAccountInfoQuery;
import com.rkylin.wheatfield.pojo.CorporatAccountInfoScopeQuery;
import com.rkylin.database.BaseDao;

@Repository("corporatAccountInfoDao")
public class CorporatAccountInfoDaoImpl extends BaseDao implements CorporatAccountInfoDao {
	
	@Override
	public int countByExample(CorporatAccountInfoQuery example) {
		return super.getSqlSession().selectOne("CorporatAccountInfoMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(CorporatAccountInfoQuery example) {
		return super.getSqlSession().delete("CorporatAccountInfoMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("CorporatAccountInfoMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(CorporatAccountInfo record) {
		return super.getSqlSession().insert("CorporatAccountInfoMapper.insert", record);
	}
	
	@Override
	public int insertSelective(CorporatAccountInfo record) {
		return super.getSqlSession().insert("CorporatAccountInfoMapper.insertSelective", record);
	}
	
	@Override
	public List<CorporatAccountInfo> selectByExample(CorporatAccountInfoQuery example) {
		return super.getSqlSession().selectList("CorporatAccountInfoMapper.selectByExample", example);
	}
	
	@Override
	public CorporatAccountInfo selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("CorporatAccountInfoMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(CorporatAccountInfo record) {
		return super.getSqlSession().update("CorporatAccountInfoMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(CorporatAccountInfo record) {
		return super.getSqlSession().update("CorporatAccountInfoMapper.updateByPrimaryKey", record);
	}

	@Override
	public int batchUpdate(List<CorporatAccountInfo> list) {
		return super.getSqlSession().update("CorporatAccountInfoMapper.batchUpdate", list);
	}

	@Override
	public int insertByList(List<CorporatAccountInfo> corporatAccountInfoList) {
		return super.getSqlSession().update("CorporatAccountInfoMapper.insertByList", corporatAccountInfoList);
	}

	@Override
	public List<CorporatAccountInfo> selectByScope(CorporatAccountInfoScopeQuery scopeExample) {
		return super.getSqlSession().selectList("CorporatAccountInfoMapper.selectByScope", scopeExample);
	}

	@Override
	public int updateById(CorporatAccountInfo corporatAccountInfo) {
		return super.getSqlSession().update("CorporatAccountInfoMapper.updateById", corporatAccountInfo);
	}
	
	@Override
	public List<Object> getBalance(String name,Map<String, String> param) {
		return super.getSqlSession().selectList(name, param);
	}
}
