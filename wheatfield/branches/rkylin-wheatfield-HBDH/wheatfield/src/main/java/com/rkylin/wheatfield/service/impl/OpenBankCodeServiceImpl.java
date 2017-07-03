package com.rkylin.wheatfield.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.rkylin.wheatfield.manager.OpenBankCodeManager;
import com.rkylin.wheatfield.manager.TlBankCodeManager;
import com.rkylin.wheatfield.pojo.OpenBankCode;
import com.rkylin.wheatfield.pojo.OpenBankCodeQuery;
import com.rkylin.wheatfield.pojo.TlBankCode;
import com.rkylin.wheatfield.pojo.TlBankCodeQuery;
import com.rkylin.wheatfield.response.CityCodeResponse;
import com.rkylin.wheatfield.response.Response;
import com.rkylin.wheatfield.service.IAPIService;
import com.rkylin.wheatfield.service.IErrorResponseService;
import com.rkylin.wheatfield.service.OpenBankCodeService;

public class OpenBankCodeServiceImpl implements OpenBankCodeService,
		IAPIService {
	@Autowired
	OpenBankCodeManager openBankCodeManager;
	@Autowired
	TlBankCodeManager tlBankCodeManager;
	@Autowired
	IErrorResponseService errorResponseService;
	
	private static Logger logger = LoggerFactory.getLogger(OpenBankCodeServiceImpl.class);	
	@Override
	public Response doJob(Map<String, String[]> paramMap, String methodName) {
		CityCodeResponse response=new CityCodeResponse();
		OpenBankCode openBankCode=new OpenBankCode();
		String statusStr=null;//查询状态条件
		for (Object keyObj : paramMap.keySet().toArray()) {
			String[] strs = paramMap.get(keyObj);
			for (String value : strs) {
				logger.info("入参信息：KEY="+keyObj+",value="+value);
				if ("citycode".equals(keyObj)) {
					openBankCode.setCityCode(value);
				}else if("bankcode".equals(keyObj)){
					openBankCode.setBankCode(value);
				}else if("status".equals(keyObj)){
					statusStr=value;
				}
			}
		}
		if("ruixue.wheatfield.bank.query".equals(methodName)){
			logger.info("--------获取银行列表信息开始-旧接口--------");
			List<TlBankCode> bankinfos=new ArrayList<TlBankCode>();
			if(openBankCode.getBankCode()!=null && !"".equals(openBankCode.getBankCode()) &&
					openBankCode.getCityCode()!=null && !"".equals(openBankCode.getCityCode())){
				List<OpenBankCode> obList=this.getBankList(openBankCode);
				if(obList!=null && obList.size()>0){
					for (int i = 0; i < obList.size(); i++) {
						TlBankCode bankinfo=new TlBankCode();
						OpenBankCode obc=obList.get(i);
						bankinfo.setBankCode(obc.getPayBankCode());
						bankinfo.setBankName(obc.getBankName());
						bankinfos.add(bankinfo);
					}
					response.setBankinfos(bankinfos);
				}else{
					return errorResponseService.getErrorResponse("C1", "暂无支行信息！");
				}
			}else{
				List<TlBankCode> tbList=this.getHeadBankCode();
				if(tbList!=null && tbList.size()>0){
					response.setBankinfos(tbList);
				}else{
					return errorResponseService.getErrorResponse("C2", "暂无总行信息！");
				}
			}
			logger.info("--------获取银行列表信息结束-旧接口--------");
		}else if("ruixue.wheatfield.bankn.query".equals(methodName)){//查询银行新接口
			logger.info("--------获取银行列表信息开始-新接口--------");
			List<TlBankCode> bankinfos=new ArrayList<TlBankCode>();
			if((openBankCode.getBankCode()!=null && !"".equals(openBankCode.getBankCode()))||(
					openBankCode.getCityCode()!=null && !"".equals(openBankCode.getCityCode()))){
				List<OpenBankCode> obList=this.getBankList(openBankCode);
				if(obList!=null && obList.size()>0){
					for (int i = 0; i < obList.size(); i++) {
						TlBankCode bankinfo=new TlBankCode();
						OpenBankCode obc=obList.get(i);
						bankinfo.setBankCode(obc.getPayBankCode());
						bankinfo.setBankName(obc.getBankName());
						bankinfos.add(bankinfo);
					}
					response.setBankinfos(bankinfos);
				}else{
					logger.info("根据入参信息没有获取到支行信息");
					return errorResponseService.getErrorResponse("C1", "暂无支行信息！");
				}
			}else{
				logger.info("入参银行code和城市code为null,根据状态查询总行信息并返回");
				List<TlBankCode> tbList=null;
				if(null!=statusStr){
					tbList=this.getHeadBankCode(Integer.parseInt(statusStr));
				}else{
					logger.info("入参信息全部为null,查询总行所有信息并返回");
					tbList=this.getHeadBankCode(null);
				}
				if(tbList!=null && tbList.size()>0){
					response.setBankinfos(tbList);
				}else{
					logger.info("没有获取到总行信息");
					return errorResponseService.getErrorResponse("C2", "暂无总行信息！");
				}
			}
			logger.info("--------获取银行列表信息结束-新接口--------");
		}
		return response;
	}
	@Override
	public List<OpenBankCode> getBankList(OpenBankCode bankCode){
		OpenBankCodeQuery query=new OpenBankCodeQuery();
		if(bankCode.getBankCode()!=null && !"".equals(bankCode.getBankCode())){
			query.setOpenBankCode(bankCode.getBankCode()+"%");
		}
		query.setCityCode(bankCode.getCityCode());
		List<OpenBankCode> openBankCodeList=openBankCodeManager.queryListByCode(query);
		return openBankCodeList;
		
	}
	@Override
	public List<TlBankCode> getHeadBankCode(){
		TlBankCodeQuery tlBankCodeQuery=new TlBankCodeQuery();
		tlBankCodeQuery.setStatusId(1);
		List<TlBankCode> tlBankCodeList=tlBankCodeManager.queryList(tlBankCodeQuery);
		return tlBankCodeList;
	}
	@Override
	public List<TlBankCode> getHeadBankCode(Integer status) {
		TlBankCodeQuery tlBankCodeQuery=new TlBankCodeQuery();
		tlBankCodeQuery.setStatusId(status);
		List<TlBankCode> tlBankCodeList=tlBankCodeManager.queryList(tlBankCodeQuery);
		return tlBankCodeList;
	}
}
