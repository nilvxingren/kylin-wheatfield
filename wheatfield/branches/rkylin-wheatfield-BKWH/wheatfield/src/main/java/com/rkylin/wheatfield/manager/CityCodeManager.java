/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.manager;

import java.util.List;

import com.rkylin.wheatfield.pojo.CityCode;
import com.rkylin.wheatfield.pojo.CityCodeQuery;

public interface CityCodeManager {
	void saveCityCode(CityCode cityCode);

	CityCode findCityCodeById(Long id);
	
	List<CityCode> queryList(CityCodeQuery query);
	
	void deleteCityCodeById(Long id);
	
	void deleteCityCode(CityCodeQuery query);

	List<CityCode> queryListByCode(CityCodeQuery query);
}
