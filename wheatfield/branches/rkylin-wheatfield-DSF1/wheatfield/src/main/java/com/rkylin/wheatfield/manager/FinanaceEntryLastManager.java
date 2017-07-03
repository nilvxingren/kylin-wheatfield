/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager;

import java.util.List;

import com.rkylin.wheatfield.pojo.FinanaceEntryLast;
import com.rkylin.wheatfield.pojo.FinanaceEntryLastQuery;

public interface FinanaceEntryLastManager {
	void saveFinanaceEntryLast(FinanaceEntryLast finanaceEntryLast);
	
	void saveFinanaceEntryLastList(List<FinanaceEntryLast> finanaceEntrieLasts);
	
	FinanaceEntryLast findFinanaceEntryLastById(Long id);
	
	List<FinanaceEntryLast> queryList(FinanaceEntryLastQuery query);
	
	void deleteFinanaceEntryLastById(Long id);
	
	void deleteFinanaceEntryLast(FinanaceEntryLastQuery query);
}
