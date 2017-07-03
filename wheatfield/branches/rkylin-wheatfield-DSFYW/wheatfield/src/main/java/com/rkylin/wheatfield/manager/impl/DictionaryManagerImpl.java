/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.wheatfield.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.rkylin.wheatfield.dao.DictionaryDao;
import com.rkylin.wheatfield.manager.DictionaryManager;
import com.rkylin.wheatfield.pojo.Dictionary;
import com.rkylin.wheatfield.pojo.DictionaryQuery;

@Component("dictionaryManager")
public class DictionaryManagerImpl implements DictionaryManager {
	
	@Autowired
	@Qualifier("dictionaryDao")
	private DictionaryDao dictionaryDao;
	
	@Override
	public void saveDictionary(Dictionary dictionary) {
		if (dictionary.getDicId() == null) {
			dictionaryDao.insertSelective(dictionary);
		} else {
			dictionaryDao.updateByPrimaryKeySelective(dictionary);
		}
	}
	
	@Override
	public Dictionary findDictionaryById(Long id) {
		return dictionaryDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<Dictionary> queryList(DictionaryQuery query) {
		return dictionaryDao.selectByExample(query);
	}
	
	@Override
	public void deleteDictionaryById(Long id) {
		dictionaryDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteDictionary(DictionaryQuery query) {
		dictionaryDao.deleteByExample(query);
	}
}

