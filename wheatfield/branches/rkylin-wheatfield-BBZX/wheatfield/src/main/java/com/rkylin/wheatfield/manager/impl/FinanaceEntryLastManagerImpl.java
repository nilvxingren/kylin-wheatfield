package com.rkylin.wheatfield.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.rkylin.wheatfield.dao.FinanaceEntryLastDao;
import com.rkylin.wheatfield.manager.FinanaceEntryLastManager;
import com.rkylin.wheatfield.pojo.FinanaceEntry;
import com.rkylin.wheatfield.pojo.FinanaceEntryLast;
import com.rkylin.wheatfield.pojo.FinanaceEntryLastQuery;

@Component("finanaceEntryLastManager")
public class FinanaceEntryLastManagerImpl implements FinanaceEntryLastManager {
	
	@Autowired
	@Qualifier("finanaceEntryLastDao")
	private FinanaceEntryLastDao finanaceEntryLastDao;
	
	@Override
	public void saveFinanaceEntryLast(FinanaceEntryLast finanaceEntryLast) {
		finanaceEntryLastDao.insertSelective(finanaceEntryLast);
	}
	
	@Override
	public FinanaceEntryLast findFinanaceEntryLastById(Long id) {
		return finanaceEntryLastDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<FinanaceEntryLast> queryList(FinanaceEntryLastQuery query) {
		return finanaceEntryLastDao.selectByExample(query);
	}
	
	@Override
	public void deleteFinanaceEntryLastById(Long id) {
		finanaceEntryLastDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteFinanaceEntryLast(FinanaceEntryLastQuery query) {
		finanaceEntryLastDao.deleteByExample(query);
	}
	@Override
	public void saveFinanaceEntryLastList(List<FinanaceEntryLast> finanaceEntrieLasts) {
		finanaceEntryLastDao.insertSelectiveBatch(finanaceEntrieLasts);		
	}
}

