package com.rkylin.wheatfield.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.rkylin.wheatfield.exception.AccountException;
import com.rkylin.wheatfield.manager.CityCodeManager;
import com.rkylin.wheatfield.pojo.CityCode;
import com.rkylin.wheatfield.pojo.CityCodeQuery;
import com.rkylin.wheatfield.response.CityCodeResponse;
import com.rkylin.wheatfield.response.Response;
import com.rkylin.wheatfield.service.CityCodeService;
import com.rkylin.wheatfield.service.IAPIService;
import com.rkylin.wheatfield.service.IErrorResponseService;

public class CityCodeServiceImpl implements CityCodeService, IAPIService {
	private static Logger logger = LoggerFactory
			.getLogger(SettlementServiceImpl.class);
	@Autowired
	CityCodeManager cityCodeManager;
	@Autowired
	IErrorResponseService errorResponseService;
	@Override
	public Response doJob(Map<String, String[]> paramMap, String methodName) {
		CityCodeResponse response=new CityCodeResponse();
		if("ruixue.wheatfield.city.query".equals(methodName)){
			CityCode cc=new CityCode();
			for (Object keyObj : paramMap.keySet().toArray()) {
				String[] strs = paramMap.get(keyObj);
				for (String value : strs) {
					if ("citycode".equals(keyObj)) {
						cc.setCityCode(value);
					}
				}
			}
			List<CityCode> cityCodeList=this.cityCodeList(cc);
			if(cityCodeList!=null && cityCodeList.size()>0){
				response.setCitycodes(cityCodeList);
			}else{
				return errorResponseService.getErrorResponse("C1", "暂无数据！");
			}

		}
		return response;
	}

	@Override
	public List<CityCode> cityCodeList(CityCode cityCode) {
		try {
			CityCodeQuery query = new CityCodeQuery();
			query.setCityCode(cityCode.getCityCode());
			query.setCityLevel(cityCode.getCityLevel());
			List<CityCode> cityCodeList = cityCodeManager.queryListByCode(query);
			return cityCodeList;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new AccountException("查找省市失败！"+e.getMessage());
		}

	}

}
