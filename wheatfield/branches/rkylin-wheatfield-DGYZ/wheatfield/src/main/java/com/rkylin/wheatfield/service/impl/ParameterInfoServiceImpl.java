package com.rkylin.wheatfield.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkylin.wheatfield.manager.ParameterInfoManager;
import com.rkylin.wheatfield.pojo.ParameterInfo;
import com.rkylin.wheatfield.pojo.ParameterInfoQuery;
import com.rkylin.wheatfield.service.ParameterInfoService;
@Service("parameterInfoService")
public class ParameterInfoServiceImpl implements ParameterInfoService {
	@Autowired
	ParameterInfoManager parameterInfoManager;
	@Override
	public ParameterInfo getParameterInfo(String parameterCode) {
		ParameterInfoQuery query =new ParameterInfoQuery();
		query.setParameterCode(parameterCode);
		List<ParameterInfo> pList=parameterInfoManager.queryList(query);
		if(pList!=null && pList.size()>0){
			return pList.get(0);
		}
		return null;
	}

}
