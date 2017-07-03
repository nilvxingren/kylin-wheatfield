/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao;

import java.util.List;

import com.rkylin.wheatfield.pojo.FinanaceAccount;
import com.rkylin.wheatfield.pojo.FinanaceAccountQuery;

public interface FinanaceAccountDao {
	int countByExample(FinanaceAccountQuery example);
	
	int deleteByExample(FinanaceAccountQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(FinanaceAccount record);
	
	int insertSelective(FinanaceAccount record);
	
	List<FinanaceAccount> selectByExample(FinanaceAccountQuery example);
	
	public List<FinanaceAccount> selectByFinAccountId(String[] finAccountIds);
	
	FinanaceAccount selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(FinanaceAccount record);
	
	int updateByPrimaryKey(FinanaceAccount record);
	
	//��ݻ�ź��û�id��ѯ�����в�Ʒ��
	List<String> selectProductIdList(FinanaceAccountQuery example);
	
	public List<FinanaceAccount> queryByInstCodeAndUser(com.rkylin.wheatfield.bean.User user);
	
	public List<FinanaceAccount> batchSelectByCon(List<com.rkylin.wheatfield.bean.User> list);
	
	public List<FinanaceAccount> selectAllUserByCertificateNum(FinanaceAccountQuery example);
}
