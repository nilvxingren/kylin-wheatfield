/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao;

import java.util.List;
import java.util.Map;

import com.rkylin.wheatfield.pojo.CorporatAccountInfo;
import com.rkylin.wheatfield.pojo.CorporatAccountInfoQuery;
import com.rkylin.wheatfield.pojo.CorporatAccountInfoScopeQuery;

public interface CorporatAccountInfoDao {
	int countByExample(CorporatAccountInfoQuery example);
	
	int deleteByExample(CorporatAccountInfoQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(CorporatAccountInfo record);
	
	int insertSelective(CorporatAccountInfo record);
	
	List<CorporatAccountInfo> selectByExample(CorporatAccountInfoQuery example);
	
	List<CorporatAccountInfo> selectByScope(CorporatAccountInfoScopeQuery scopeExample);
	
	CorporatAccountInfo selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(CorporatAccountInfo record);
	
	int updateByPrimaryKey(CorporatAccountInfo record);
	/**
	 * @CreateTime : 2015年9月6日 下午4:38:00
	 * @Creator : liuhuan
	 */
	int insertByList(List<CorporatAccountInfo> corporatAccountInfoList);
	
	int batchUpdate(List<CorporatAccountInfo> list);
	
	int updateById(CorporatAccountInfo corporatAccountInfo);
	
	public List<Object> getBalance(String name,Map<String, String> param);
	
	public List<CorporatAccountInfo> queryByAccountNo(List<CorporatAccountInfoQuery> list);
}
