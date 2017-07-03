/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.database.BaseDao;
import com.rkylin.wheatfield.dao.FinanacePersonDao;
import com.rkylin.wheatfield.pojo.FinanacePerson;
import com.rkylin.wheatfield.pojo.FinanacePersonQuery;

@Repository("finanacePersonDao")
public class FinanacePersonDaoImpl extends BaseDao implements FinanacePersonDao {
	
	@Override
	public int countByExample(FinanacePersonQuery example) {
		return super.getSqlSession().selectOne("FinanacePersonMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(FinanacePersonQuery example) {
		return super.getSqlSession().delete("FinanacePersonMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("FinanacePersonMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(FinanacePerson record) {
		return super.getSqlSession().insert("FinanacePersonMapper.insert", record);
	}
	
	@Override
	public int insertSelective(FinanacePerson record) {
		return super.getSqlSession().insert("FinanacePersonMapper.insertSelective", record);
	}
	
	@Override
	public List<FinanacePerson> selectByExample(FinanacePersonQuery example) {
		return super.getSqlSession().selectList("FinanacePersonMapper.selectByExample", example);
	}
	
	@Override
	public FinanacePerson selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("FinanacePersonMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(FinanacePerson record) {
		return super.getSqlSession().update("FinanacePersonMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(FinanacePerson record) {
		return super.getSqlSession().update("FinanacePersonMapper.updateByPrimaryKey", record);
	}

	@Override
	public List<FinanacePerson> selectByRootInstCdOrNumOrStatusId(
			FinanacePersonQuery example) {
		return super.getSqlSession().selectList("FinanacePersonMapper.selectByRootInstCdOrNumOrStatusId", example);
	}

	@Override
	public int updateByFinanaceAccountId(FinanacePerson record) {
		return super.getSqlSession().update("FinanacePersonMapper.updateByFinanaceAccountId", record);
	}

	@Override
	public List<FinanacePerson> selectByExampleBatch(FinanacePersonQuery example) {
		return super.getSqlSession().selectList("FinanacePersonMapper.selectByExampleBatch", example);
	}

	@Override
    public List<FinanacePerson> selectPersonAccInfo(FinanacePersonQuery example) {
        return super.getSqlSession().selectList("FinanacePersonMapper.selectPersonAccInfo", example);
    }
}
