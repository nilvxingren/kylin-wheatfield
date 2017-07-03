package com.rkylin.wheatfield.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.Rop.api.domain.CreditRepaymentInfo;
import com.rkylin.wheatfield.common.DateUtils;
import com.rkylin.wheatfield.common.ValHasNoParam;
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.manager.CreditRepaymentHistoryManager;
import com.rkylin.wheatfield.pojo.CreditRepaymentHistory;
import com.rkylin.wheatfield.pojo.CreditRepaymentHistoryQuery;
import com.rkylin.wheatfield.pojo.User;
import com.rkylin.wheatfield.response.CreditRepaymentHistorytResponse;
import com.rkylin.wheatfield.response.Response;
import com.rkylin.wheatfield.service.AccountManageService;
import com.rkylin.wheatfield.service.CreditRepaymentHistoryService;
import com.rkylin.wheatfield.service.IAPIService;
import com.rkylin.wheatfield.service.IErrorResponseService;

public class CreditRepaymentHistoryServiceImpl implements
		CreditRepaymentHistoryService, IAPIService {
	private static Logger logger = LoggerFactory.getLogger(CreditRepaymentHistoryServiceImpl.class);
	@Autowired
	AccountManageService accountManageService;
	@Autowired
	CreditRepaymentHistoryManager creditRepaymentHistoryManager;
	@Autowired
	IErrorResponseService errorResponseService;
	@Override
	public Response doJob(Map<String, String[]> paramMap, String methodName) {
		if(!ValHasNoParam.hasParam(paramMap, "userid")){
			return errorResponseService.getErrorResponse("P1","用户ID不能为空");
		}
		if(!ValHasNoParam.hasParam(paramMap, "constid")){
			return errorResponseService.getErrorResponse("P2","机构号不能为空");
		}
		if(!ValHasNoParam.hasParam(paramMap, "creditdate")){
			return errorResponseService.getErrorResponse("P3","账期不能为空");
		}
		CreditRepaymentHistorytResponse response=new CreditRepaymentHistorytResponse();
		if("ruixue.wheatfield.creditrepayment.query".equals(methodName)){
			String userId="";
			String constId="";
			String creditDate="";
			String tradeFlowNo="";
			for(Object keyObj : paramMap.keySet().toArray()){
				String[] strs = paramMap.get(keyObj);
				for(String value : strs){
					if("userid".equals(keyObj)){
						userId=value;
					}else if("constid".equals(keyObj)){
						constId=value;
					}else if("creditdate".equals(keyObj)){
						creditDate=value;
					}else if("tradeflowno".equals(keyObj)){
						tradeFlowNo=value;
					}
				}
			}
			CreditRepaymentInfo creditRepaymentInfo=this.getCreditrepaymentHistorys(userId, constId, creditDate, tradeFlowNo);
			if(creditRepaymentInfo!=null){
				response.setCreditRepaymentInfo(creditRepaymentInfo);
			}else{
				return errorResponseService.getErrorResponse("C1", "暂无数据！");
			}
		}
		return response;
	}

	@Override
	public CreditRepaymentInfo getCreditrepaymentHistorys(
			String userId, String constid, String creditDate, String tradeFlowNo) {
		User user=new User();
		user.userId=userId;
		user.constId=constid;
		String msg=accountManageService.checkAccount(user);
		if("ok".equals(msg)){
			try{
				CreditRepaymentHistoryQuery query=new CreditRepaymentHistoryQuery();
				query.setAccountDate(DateUtils.getDate(creditDate, Constants.DATE_FORMAT_YYYYMMDD));
				query.setUserId(userId);
				List<CreditRepaymentHistory> historyList=creditRepaymentHistoryManager.queryList(query);
				if(historyList!=null && historyList.size()>0){
					CreditRepaymentHistory history=historyList.get(0);
					CreditRepaymentInfo creditRepaymentInfo=new CreditRepaymentInfo();
					creditRepaymentInfo.setAmount(Long.toString(history.getCapital()));
					creditRepaymentInfo.setTradeflowno(history.getRepayId());
					creditRepaymentInfo.setTransdate(DateUtils.getDateFormat(Constants.DATE_FORMAT_YYYYMMDD,history.getRepaidDate()));
					creditRepaymentInfo.setTranstype("1");
					creditRepaymentInfo.setUserid(userId);
					return creditRepaymentInfo;
				}
			}catch(Exception e){
				logger.error(e.getMessage());
			}
		}else{
			logger.info(msg);
		}
		return null;
	}

}
