package com.rkylin.wheatfield.api;

import com.rkylin.wheatfield.bean.CityCodeQuy;
import com.rkylin.wheatfield.model.BankResponse;
import com.rkylin.wheatfield.model.CityCodeResponse;
import com.rkylin.wheatfield.model.DictionaryResponse;
import com.rkylin.wheatfield.pojo.DictionaryQuery;

/**
 * 基础数据接口
 * @author Achilles
 *
 */
public interface BaseInforDubboService {
	
	/**
	 * 字典信息查询
	 * @param query 
	 * @return
	 */
	public DictionaryResponse getDicInfor(DictionaryQuery query);
	
	/**
	 * 查询
	 * Discription:
	 * @param cityCodeQuery
	 * @return CityCodeResponse
	 * @author Achilles
	 * @since 2017年2月22日
	 */
	public CityCodeResponse getProvinceAndCityCodes(CityCodeQuy cityCodeQuery);
	
	
	public BankResponse getBanks(com.rkylin.wheatfield.bean.OpenBankCodeQuy openBankCodeQuy);
	
}
