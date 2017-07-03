/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.database.BaseDao;
import com.rkylin.wheatfield.dao.FinanaceAccountDao;
import com.rkylin.wheatfield.pojo.FinanaceAccount;
import com.rkylin.wheatfield.pojo.FinanaceAccountQuery;

@Repository("finanaceAccountDao")
public class FinanaceAccountDaoImpl extends BaseDao implements FinanaceAccountDao {
	
	@Override
	public int countByExample(FinanaceAccountQuery example) {
		return super.getSqlSession().selectOne("FinanaceAccountMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(FinanaceAccountQuery example) {
		return super.getSqlSession().delete("FinanaceAccountMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("FinanaceAccountMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(FinanaceAccount record) {
		return super.getSqlSession().insert("FinanaceAccountMapper.insert", record);
	}
	
	@Override
	public int insertSelective(FinanaceAccount record) {
		return super.getSqlSession().insert("FinanaceAccountMapper.insertSelective", record);
	}
	
	@Override
	public List<FinanaceAccount> selectByExample(FinanaceAccountQuery example) {
		return super.getSqlSession().selectList("FinanaceAccountMapper.selectByExample", example);
	}
	
	@Override
	public List<FinanaceAccount> selectByFinAccountId(String[] array) {
		return super.getSqlSession().selectList("FinanaceAccountMapper.selectByFinAccountId", array);
	}
	
	@Override
	public FinanaceAccount selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("FinanaceAccountMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(FinanaceAccount record) {
		return super.getSqlSession().update("FinanaceAccountMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(FinanaceAccount record) {
		return super.getSqlSession().update("FinanaceAccountMapper.updateByPrimaryKey", record);
	}
	
	//��ݻ�ź��û�id��ѯ�����в�Ʒ��
	@Override
	public List<String> selectProductIdList(FinanaceAccountQuery example){
		return super.getSqlSession().selectList("FinanaceAccountMapper.selectProductIdList", example);
	}
	
	@Override
	public List<FinanaceAccount> queryByInstCodeAndUser(com.rkylin.wheatfield.bean.User user) {
		return super.getSqlSession().selectList("FinanaceAccountMapper.selectByInstCodeAndUser", user);
	}
	
    public List<FinanaceAccount> batchSelectByCon(List<com.rkylin.wheatfield.bean.User> list) {
       if (list==null||list.size()==0) {
           return new ArrayList<FinanaceAccount>();
       }
       list.get(list.size()-1).setLast(1);//sql组装判断
        return super.getSqlSession().selectList("FinanaceAccountMapper.batchSelectByCon", list);
    }
}
