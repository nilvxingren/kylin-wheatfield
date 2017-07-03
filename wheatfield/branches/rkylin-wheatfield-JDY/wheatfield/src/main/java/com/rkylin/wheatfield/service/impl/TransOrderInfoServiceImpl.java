package com.rkylin.wheatfield.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.rkylin.wheatfield.common.DateUtils;
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.manager.CreditRepaymentDaysSummaryManager;
import com.rkylin.wheatfield.manager.CreditRepaymentMonthManager;
import com.rkylin.wheatfield.manager.ParameterInfoManager;
import com.rkylin.wheatfield.manager.TransOrderInfoManager;
import com.rkylin.wheatfield.pojo.CreditRepaymentMonth;
import com.rkylin.wheatfield.pojo.ParameterInfo;
import com.rkylin.wheatfield.pojo.ParameterInfoQuery;
import com.rkylin.wheatfield.pojo.TransOrderInfo;
import com.rkylin.wheatfield.pojo.TransOrderInfoQuery;
import com.rkylin.wheatfield.pojo.User;
import com.rkylin.wheatfield.response.CreditRepaymentMonthResponse;
import com.rkylin.wheatfield.response.Response;
import com.rkylin.wheatfield.service.AccountManageService;
import com.rkylin.wheatfield.service.CreditRepaymentMonthService;
import com.rkylin.wheatfield.service.IAPIService;
import com.rkylin.wheatfield.service.IErrorResponseService;
import com.rkylin.wheatfield.service.ParameterInfoService;
import com.rkylin.wheatfield.service.TransOrderInfoService;

public class TransOrderInfoServiceImpl implements TransOrderInfoService,
		IAPIService {
	private static Logger logger = LoggerFactory.getLogger(TransOrderInfoServiceImpl.class);
	@Autowired
	CreditRepaymentDaysSummaryManager creditRepaymentDaysSummaryManager;
	@Autowired
	AccountManageService accountManageService;
	@Autowired
	TransOrderInfoManager transOrderInfoManager;
	@Autowired
	CreditRepaymentMonthService creditRepaymentMonthService;
	@Autowired
	ParameterInfoManager parameterInfoManager;
	@Autowired
	ParameterInfoService parameterInfoService;
	@Autowired
	IErrorResponseService errorResponseService;
	@Override
	public Response doJob(Map<String, String[]> paramMap, String methodName) {
		CreditRepaymentMonthResponse response=new CreditRepaymentMonthResponse();
		if("ruixue.wheatfield.finanace.entry.query".equals(methodName)){
			String userId="";
			String constId="";
			String creditDate="";
			for(Object keyObj : paramMap.keySet().toArray()){
				String[] strs = paramMap.get(keyObj);
				for(String value : strs){
					if("userid".equals(keyObj)){
						userId=value;
					}else if("constid".equals(keyObj)){
						constId=value;
					}else if("creditdate".equals(keyObj)){
						creditDate=value;
					}
				}
			}
			CreditRepaymentMonth credit=creditRepaymentMonthService.getCreditRepaymentInfo(userId, constId, creditDate);
			if(credit!=null){
				CreditRepaymentMonth creditRepayment=new CreditRepaymentMonth();
				ParameterInfo parameterInfo=parameterInfoService.getParameterInfo("BillDay");
				if(parameterInfo!=null){
					creditRepayment.setAccountdate(parameterInfo.getParameterValue());
				}
				ParameterInfo parameterInfo1=parameterInfoService.getParameterInfo("RepaymentDay");
				if(parameterInfo1!=null){
					creditRepayment.setAccountdate(parameterInfo1.getParameterValue());
				}
				creditRepayment.setUserid(userId);
//				creditRepayment.setAccountdate(arg0);
				creditRepayment.setCreditdate(creditDate);
				long result=credit.getCapitalAll()+credit.getInterestAll();
				//本金--月消费金融
				creditRepayment.setCorpus(Long.toString(credit.getCapitalMonth()));
				//本息---本金+利息
				creditRepayment.setPrincipalinterest(Long.toString(result));
				creditRepayment.setIsoverdue("1");
				if(result==0){
					//未逾期
					creditRepayment.setIsoverdue("0");
					creditRepayment.setPrincipalinterest(Long.toString(credit.getCapitalMonth()));
				}
				List list=this.getTransOrderInfos(userId, constId, creditDate);
				
				if(list!=null && list.size()>0){
					List transList=new ArrayList();
					TransOrderInfo transInfo=new TransOrderInfo();
					TransOrderInfo transInfo1=new TransOrderInfo();
					TransOrderInfo transInfo2=new TransOrderInfo();
					if(result==0){
						transInfo.setTranssummary("本期利息");
						transInfo.setTransdate(DateUtils.getDateFormat(Constants.DATE_FORMAT_YYYYMMDD, credit.getUpdatedTime()));
						transInfo.setAmount(credit.getInterestFix());
						transList.add(transInfo);
						transInfo1.setTranssummary("免本期利息");
						transInfo1.setTransdate(DateUtils.getDateFormat(Constants.DATE_FORMAT_YYYYMMDD, credit.getUpdatedTime()));
						transInfo1.setAmount(credit.getInterestFix());
						transList.add(transInfo1);
						transInfo2.setTranssummary("逾期利息");
						transInfo2.setTransdate(DateUtils.getDateFormat(Constants.DATE_FORMAT_YYYYMMDD, credit.getUpdatedTime()));
						transInfo2.setAmount(Long.parseLong("0"));
						transList.add(transInfo2);
					}else{
						transInfo.setTranssummary("本期利息");
						transInfo.setTransdate(DateUtils.getDateFormat(Constants.DATE_FORMAT_YYYYMMDD, credit.getUpdatedTime()));
						transInfo.setAmount(credit.getInterestAll());
						transList.add(transInfo);
						transInfo1.setTranssummary("免本期利息");
						transInfo1.setTransdate(DateUtils.getDateFormat(Constants.DATE_FORMAT_YYYYMMDD, credit.getUpdatedTime()));
						transInfo1.setAmount(Long.parseLong("0"));
						transList.add(transInfo1);
						transInfo2.setTranssummary("逾期利息");
						transInfo2.setTransdate(DateUtils.getDateFormat(Constants.DATE_FORMAT_YYYYMMDD, credit.getUpdatedTime()));
						transInfo2.setAmount(credit.getInterestAll()-credit.getInterestFix());
						transList.add(transInfo2);
					}
					transList.addAll(list);
					creditRepayment.setTransinfos(transList);
					
					response.setCreditrepaymentmonth(creditRepayment);
				}
			}else{
				return errorResponseService.getErrorResponse("C1", "暂无数据！");  
			}
		}
		return response;
	}

	@Override
	public List<TransOrderInfo> getTransOrderInfos(String userId,
			String constId, String creditDate) {
		logger.info("-----用户ID："+userId+"--------机构号："+constId+"----信用账期："+creditDate);
		User user=new User();
		user.userId=userId;
		user.constId=constId;
		String msg=accountManageService.checkAccount(user);
		if("ok".equals(msg)){
			try{
				TransOrderInfoQuery query=new TransOrderInfoQuery();
				query.setUserId(userId);
				query.setMerchantCode(user.constId);
				query.setCreditDate(Integer.parseInt(creditDate));
				List<TransOrderInfo> transOrderList=transOrderInfoManager.queryListByCreditDate(query);
				return transOrderList;
			}catch(Exception e){
				logger.error(e.getMessage());
				logger.info(e.getMessage());
			}
		}else{
			logger.info(msg);
		}
		return null;
	}
}
