/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao;

import java.util.List;

import com.rkylin.wheatfield.pojo.FinanacePerson;
import com.rkylin.wheatfield.pojo.FinanacePersonQuery;

public interface FinanacePersonDao {
	int countByExample(FinanacePersonQuery example);
	
	int deleteByExample(FinanacePersonQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(FinanacePerson record);
	
	int insertSelective(FinanacePerson record);
	
	List<FinanacePerson> selectByExample(FinanacePersonQuery example);
	
	List<FinanacePerson> selectByRootInstCdOrNumOrStatusId(FinanacePersonQuery example);
	
	List<FinanacePerson> selectByExampleBatch(FinanacePersonQuery example);
	
	FinanacePerson selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(FinanacePerson record);
	
	int updateByPrimaryKey(FinanacePerson record);
	
	int updateByFinanaceAccountId(FinanacePerson record);

}
