/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.wheatfield.manager;

import java.util.List;

import com.rkylin.wheatfield.pojo.Dictionary;
import com.rkylin.wheatfield.pojo.DictionaryQuery;

public interface DictionaryManager {
	void saveDictionary(Dictionary dictionary);

	Dictionary findDictionaryById(Long id);
	
	List<Dictionary> queryList(DictionaryQuery query);
	
	void deleteDictionaryById(Long id);
	
	void deleteDictionary(DictionaryQuery query);
}
