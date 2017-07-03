/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.wheatfield.dao;

import java.util.List;

import com.rkylin.wheatfield.pojo.Dictionary;
import com.rkylin.wheatfield.pojo.DictionaryQuery;

public interface DictionaryDao {
	int countByExample(DictionaryQuery example);
	
	int deleteByExample(DictionaryQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(Dictionary record);
	
	int insertSelective(Dictionary record);
	
	List<Dictionary> selectByExample(DictionaryQuery example);
	
	Dictionary selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(Dictionary record);
	
	int updateByPrimaryKey(Dictionary record);
}
