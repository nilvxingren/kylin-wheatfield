/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.rkylin.wheatfield.dao.CityCodeDao;
import com.rkylin.wheatfield.manager.CityCodeManager;
import com.rkylin.wheatfield.pojo.CityCode;
import com.rkylin.wheatfield.pojo.CityCodeQuery;

@Component("cityCodeManager")
public class CityCodeManagerImpl implements CityCodeManager {
	
	@Autowired
	@Qualifier("cityCodeDao")
	private CityCodeDao cityCodeDao;
	
	@Override
	public void saveCityCode(CityCode cityCode) {
		if (cityCode.getCityCode() == null) {
			cityCodeDao.insertSelective(cityCode);
		} else {
			cityCodeDao.updateByPrimaryKeySelective(cityCode);
		}
	}
	
	@Override
	public CityCode findCityCodeById(Long id) {
		return cityCodeDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<CityCode> queryList(CityCodeQuery query) {
		return cityCodeDao.selectByExample(query);
	}
	
	@Override
	public void deleteCityCodeById(Long id) {
		cityCodeDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteCityCode(CityCodeQuery query) {
		cityCodeDao.deleteByExample(query);
	}
	@Override
	public List<CityCode> queryListByCode(CityCodeQuery query) {
		return cityCodeDao.selectByCode(query);
	}
}

