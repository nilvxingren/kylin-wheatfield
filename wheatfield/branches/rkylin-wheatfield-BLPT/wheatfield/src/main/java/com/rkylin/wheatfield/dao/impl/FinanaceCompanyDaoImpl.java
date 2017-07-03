/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.database.BaseDao;
import com.rkylin.wheatfield.dao.FinanaceCompanyDao;
import com.rkylin.wheatfield.pojo.FinanaceCompany;
import com.rkylin.wheatfield.pojo.FinanaceCompanyQuery;

@Repository("finanaceCompanyDao")
public class FinanaceCompanyDaoImpl extends BaseDao implements FinanaceCompanyDao {
	
	@Override
	public int countByExample(FinanaceCompanyQuery example) {
		return super.getSqlSession().selectOne("FinanaceCompanyMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(FinanaceCompanyQuery example) {
		return super.getSqlSession().delete("FinanaceCompanyMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("FinanaceCompanyMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(FinanaceCompany record) {
		return super.getSqlSession().insert("FinanaceCompanyMapper.insert", record);
	}
	
	@Override
	public int insertSelective(FinanaceCompany record) {
		return super.getSqlSession().insert("FinanaceCompanyMapper.insertSelective", record);
	}
	
	@Override
	public List<FinanaceCompany> selectByExample(FinanaceCompanyQuery example) {
		return super.getSqlSession().selectList("FinanaceCompanyMapper.selectByExample", example);
	}
	
	@Override
	public FinanaceCompany selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("FinanaceCompanyMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(FinanaceCompany record) {
		return super.getSqlSession().update("FinanaceCompanyMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(FinanaceCompany record) {
		return super.getSqlSession().update("FinanaceCompanyMapper.updateByPrimaryKey", record);
	}

	@Override
	public int updateByFinanaceAccountId(FinanaceCompany record) {
		return super.getSqlSession().update("FinanaceCompanyMapper.updateByFinanaceAccountId",record);
	}

	@Override
	public List<FinanaceCompany> selectByRootInstCdOrBUSLINCEOrStatusId(FinanaceCompanyQuery example) {
		return super.getSqlSession().selectList("FinanaceCompanyMapper.selectByRootInstCdOrBUSLINCEOrStatusId", example);
	}

	@Override
	public List<FinanaceCompany> getFinanaceCompanies(FinanaceCompanyQuery query) {
		return super.getSqlSession().selectList("FinanaceCompanyMapper.selectCompanyList", query);
	}

	@Override
    public List<FinanaceCompany> selectCompanyAccInfo(FinanaceCompanyQuery query) {
        return super.getSqlSession().selectList("FinanaceCompanyMapper.selectCompanyAccInfo", query);
    }
}
