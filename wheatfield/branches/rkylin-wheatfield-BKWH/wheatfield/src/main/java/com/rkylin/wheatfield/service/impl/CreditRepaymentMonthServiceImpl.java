package com.rkylin.wheatfield.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkylin.wheatfield.common.DateUtils;
import com.rkylin.wheatfield.common.ValHasNoParam;
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.manager.CreditRepaymentMonthManager;
import com.rkylin.wheatfield.pojo.CreditRepaymentMonth;
import com.rkylin.wheatfield.pojo.CreditRepaymentMonthQuery;
import com.rkylin.wheatfield.pojo.User;
import com.rkylin.wheatfield.response.CreditRepaymentMonthResponse;
import com.rkylin.wheatfield.response.Response;
import com.rkylin.wheatfield.service.AccountManageService;
import com.rkylin.wheatfield.service.CreditRepaymentMonthService;
import com.rkylin.wheatfield.service.IAPIService;
import com.rkylin.wheatfield.service.IErrorResponseService;
@Service("creditRepaymentMonthService")
public class CreditRepaymentMonthServiceImpl implements
		CreditRepaymentMonthService, IAPIService {
	private static Logger logger = LoggerFactory.getLogger(CreditRepaymentMonthServiceImpl.class);
	@Autowired
	AccountManageService accountManageService;
	@Autowired
	CreditRepaymentMonthManager creditRepaymentMonthManager; 
	@Autowired
	IErrorResponseService errorResponseService;
	@Override
	public Response doJob(Map<String, String[]> paramMap, String methodName) {
		if(!ValHasNoParam.hasParam(paramMap, "userid")){
			return errorResponseService.getErrorResponse("P1","用户ID不能为空");
		}
		if(!ValHasNoParam.hasParam(paramMap, "constid")){
			return errorResponseService.getErrorResponse("P1","机构号不能为空");
		}
		CreditRepaymentMonthResponse response=new CreditRepaymentMonthResponse();
		if("ruixue.wheatfield.creditbill.query".equals(methodName)){
			String userId="";
			String constId="";
			for(Object keyObj : paramMap.keySet().toArray()){
				String[] strs = paramMap.get(keyObj);
				for(String value : strs){
					if("userid".equals(keyObj)){
						userId=value;
					}else if("constid".equals(keyObj)){
						constId=value;
					}
				}
			}
			List list=this.creditRepaymentMonthList(userId, constId);
			if(list!=null && list.size()>0){
				response.setCreditrepaymentmonths(list);
			}else{
				return errorResponseService.getErrorResponse("C1", "暂无信息！");
			}
		}
		return response;
	}

	@Override
	public List<CreditRepaymentMonth> creditRepaymentMonthList(String userId,
			String constId) {
		User user=new User();
		user.userId=userId;
		user.constId=constId;
		String msg=accountManageService.checkAccount(user);
		if("ok".equals(msg)){
			CreditRepaymentMonthQuery creditQuery=new CreditRepaymentMonthQuery();
			creditQuery.setUserId(userId);
			try{
				List<CreditRepaymentMonth> creditList=creditRepaymentMonthManager.queryList(creditQuery);
				if(creditList!=null && creditList.size()>0){
					CreditRepaymentMonth ropCredit=new CreditRepaymentMonth();
					List list=new ArrayList();
					for (int i = 0; i < creditList.size(); i++) {
						CreditRepaymentMonth creditRepayment=creditList.get(i);
						String cdate=creditRepayment.getCreditDate().toString();
//						String cdate="201601";
//						if(cdate.substring(cdate.length()-2, cdate.length()).equals("01")){
//							cdate=(Integer.parseInt(cdate.substring(0, cdate.length()-2)))-1+"12";
//						}else{
//							cdate=((int)creditRepayment.getCreditDate()-1)+"";
//						}
						ropCredit.setCreditdate(cdate);
						long result=creditRepayment.getCapitalAll()+creditRepayment.getInterestAll();
						if(result==0){//+creditRepayment.getInterestFix()  未逾期丰年支付固定利息
							result=creditRepayment.getCapitalMonth();
						}
						ropCredit.setPrincipalinterest(Long.toString(result));
						list.add(ropCredit);
					}
					return list;
				}
			}catch(Exception e){
				logger.error(e.getMessage());
				return null;
			}
		}else{
			logger.info(msg);
		}
		return null;
	}
	@Override
	public CreditRepaymentMonth getCreditRepaymentInfo(String userId,String constId,String creditDate){
		User user=new User();
		user.userId=userId;
		user.constId=constId;
		String msg=accountManageService.checkAccount(user);
		if("ok".equals(msg)){
			CreditRepaymentMonthQuery creditQuery=new CreditRepaymentMonthQuery();
			creditQuery.setUserId(userId);
			creditQuery.setCreditDate(Integer.parseInt(creditDate));
			try{
				List<CreditRepaymentMonth> creditList=creditRepaymentMonthManager.queryList(creditQuery);
				if(creditList!=null && creditList.size()>0){
					CreditRepaymentMonth creditRepayment=creditList.get(0);
					return creditRepayment;
				}
			}catch(Exception e){
				logger.error(e.getMessage());
				return null;
			}
		}else{
			logger.info(msg);
		}
		return null;
		
	}
}
