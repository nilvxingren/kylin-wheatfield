package com.rkylin.wheatfield.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rkylin.database.BaseDao;
import com.rkylin.wheatfield.common.ValHasNoParam;
import com.rkylin.wheatfield.exception.AccountException;
import com.rkylin.wheatfield.manager.BalanceInfoManager;
import com.rkylin.wheatfield.pojo.SHBalanceInfo;
import com.rkylin.wheatfield.response.Response;
import com.rkylin.wheatfield.response.SHBalanceInfoResponse;
import com.rkylin.wheatfield.service.IAPIService;
import com.rkylin.wheatfield.service.IErrorResponseService;
import com.rkylin.wheatfield.service.SHBalanceInfoService;

@Service("ruixue.wheatfield.balance.getlist")
public class SHBalanceInfoServiceImpl extends BaseDao implements SHBalanceInfoService,IAPIService{
	private static Logger logger = LoggerFactory.getLogger(SHBalanceInfoServiceImpl.class);
	
	@Autowired
	IErrorResponseService errorResponseService;
	@Autowired
	private BalanceInfoManager balanceInfoManager;
	
	@Override
	public Response doJob(Map<String, String[]> paramMap, String methodName) {
		String reCode = "P0";
		String reMsg = "成功";
		
		SHBalanceInfoResponse response = new SHBalanceInfoResponse();

		if(!ValHasNoParam.hasParam(paramMap, "userid")){
			reCode = "P1";
			reMsg = "userid不能为空";
		}else if(!ValHasNoParam.hasParam(paramMap, "rootinstcd")){
			reCode = "P1";
			reMsg = "rootinstcd不能为空";
		}
		
		String userId = "";
		String rootInstCd = "";
		for(Object keyObj : paramMap.keySet().toArray()){
			String[] strs = paramMap.get(keyObj);
			for(String value : strs){
				logger.info("参数KEY = " + keyObj + " Value = " + value);
				if(keyObj.equals("userid")){
					userId = value;
				}else if(keyObj.equals("rootinstcd")){
					rootInstCd = value;					
				}
			}
		}	
		if(!reCode.equals("P0")){
			response.setMsg(reMsg);
			return response;
		}
		
		if("ruixue.wheatfield.balance.getlist".equals(methodName)){
			logger.info("---------查询余额信息开始---------");
			if (!ValHasNoParam.hasParam(paramMap, "rootinstcd")) {
				return errorResponseService.getErrorResponse("P1", "机构码不能为空");
			}
			response = this.getBalanceInfoList(userId, rootInstCd);
			logger.info("---------查询余额信息结束---------");
		}else{
			return errorResponseService.getErrorResponse("S0");
		}
		return response;
	}

	/**
	 * @Description : TODO(查询余额)
	 * @CreateTime : 2015年11月10日 
	 * @Creator : liuhuan
	 */
	//@Override
	@Transactional
	public SHBalanceInfoResponse getBalanceInfoList(String userId,String rootInstCd) {
		SHBalanceInfoResponse response = new SHBalanceInfoResponse();
		
		logger.info("参数信息：UserId = " + userId+ ",rootInstCd = " + rootInstCd);
		//获取所有账户记账流水
		List<SHBalanceInfo> shBalanceInfoList = new ArrayList<SHBalanceInfo>();
		try {
            Map<String, String> param = new HashMap<String, String>();  
            param.put("ACCOUNT_RELATE_ID", userId); 
            param.put("ROOT_INST_CD", rootInstCd);
            shBalanceInfoList = balanceInfoManager.getBalanceInfoList(param);
            if(shBalanceInfoList != null && !shBalanceInfoList.isEmpty()){
                response.setIs_success(true);
                response.setShbalanceinfolist(shBalanceInfoList);
            }else{
                response.setIs_success(false);
                response.setMsg("无法获取该用户信息!");
                response.setCode("C2");
            }
		}catch(AccountException e) {
			logger.error(e.getMessage());
			response.setIs_success(false);
			response.setMsg("查询余额失败!");
			response.setCode("C1");
			return response;
		}
		return response;
	}
	
}
