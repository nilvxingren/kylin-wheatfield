package com.rkylin.wheatfield.task;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.constant.TransCodeConst;
import com.rkylin.wheatfield.manager.ParameterInfoManager;
import com.rkylin.wheatfield.manager.TransOrderInfoManager;
import com.rkylin.wheatfield.pojo.ParameterInfo;
import com.rkylin.wheatfield.pojo.ParameterInfoQuery;
import com.rkylin.wheatfield.service.SemiAutomatizationService;
import com.rkylin.wheatfield.utils.DateUtil;

@Service
public class AccountSummaryTask {
	@Autowired
	TransOrderInfoManager transOrderInfoManager;
	@Autowired
	ParameterInfoManager parameterInfoManager;
	@Autowired
	SemiAutomatizationService semiAutomatizationService;
	
	DateUtil dateUtil=new DateUtil();
	private static final Logger logger = LoggerFactory.getLogger(AccountSummaryTask.class);
	/**
	 * 备付金金额汇总
	 */
	public void bfjSummary(){
		logger.info("备付金金额汇总开始》》》》》》");
		Map<String, String> summaryMap=new HashMap<String, String>();
		//获取当前账期
		String accountDateStr=this.dateTime("AccountTerm");
		if(accountDateStr==null){
			logger.error("取得账期为空，请确认");
			return;
		}		
		//获取当天时间
		String dateDay= dateUtil.getDate();
		//获取已汇总的时间
		String summaryDate=this.dateTime("SummaryBFJTime");		
		try {
			if(!dateDay.equals(dateUtil.getDateTime(dateUtil.parse(accountDateStr, "yyyy-MM-dd"), "yyyy-MM-dd"))){
				Date accountDate = dateUtil.parse(accountDateStr, "yyyy-MM-dd");
				Calendar cal = Calendar.getInstance();
				cal.setTime(accountDate);
				cal.add(Calendar.DATE, -1);
				accountDateStr=dateUtil.getDateTime(cal.getTime(), "yyyy-MM-dd");
			}
		} catch (ParseException e) {
			logger.error("日期比较错误："+e.getMessage());
		}
		summaryMap.put("accountDate", accountDateStr);
		summaryMap.put("funcCode", TransCodeConst.CHARGE);
		if(summaryDate!=null){
			summaryMap.put("createdTime", summaryDate);
			
		}
		List<Map<String, Object>> resultMap= transOrderInfoManager.selectSummaryInfo(summaryMap);
		System.out.println(resultMap);
		//开始补账环节
		if(resultMap!=null&& resultMap.get(0)!=null){
			try {
				List<Map<String,String>> paramsValueList = new ArrayList<Map<String,String>>();
				Map<String,String> map = new HashMap<String,String>();
				map.put("amount", resultMap.get(0).get("amount").toString());
				map.put("userid", TransCodeConst.THIRDPARTYID_FNZZH);
				map.put("productid", Constants.FN_PRODUCT);
				map.put("consid", Constants.FN_ID);
				map.put("status", "1");
				map.put("type", "1001");
				paramsValueList.add(map);
				String result=null;			
				result= semiAutomatizationService.ForAccount(paramsValueList);
				if(result.equals("OK")){
					//更新PARAMETER_INFO表
					ParameterInfo parameterInfo=new ParameterInfo();
					parameterInfo.setParameterCode("SummaryBFJTime");
					parameterInfo.setParameterValue(resultMap.get(0).get("createdTime").toString());
					parameterInfoManager.updateParameterInfoByCode(parameterInfo);
				}
			} catch (Exception e) {
				logger.debug("备付金汇总补账失败"+e.getMessage());
			}
		}else{
			logger.info("备付金金额汇总为0");
		}
		logger.info("备付金金额汇总结束《《《《《《");
	}
	
	/**
	 * 平安银行其他应收款汇总
	 */
	public void paysSummary(){
		logger.info("平安银行其他应收款汇总开始》》》》》》");
		Map<String, String> summaryMap=new HashMap<String, String>();
		//获取当前账期
		String accountDateStr=this.dateTime("AccountTerm");
		if(accountDateStr==null){
			logger.error("取得账期为空，请确认");
			return;
		}		
		//获取当天时间
		String dateDay= dateUtil.getDate();
		//获取已汇总的时间
		String summaryDate=this.dateTime("SummaryPAYSTime");		
		try {
			if(!dateDay.equals(dateUtil.getDateTime(dateUtil.parse(accountDateStr, "yyyy-MM-dd"), "yyyy-MM-dd"))){
				Date accountDate = dateUtil.parse(accountDateStr, "yyyy-MM-dd");
				Calendar cal = Calendar.getInstance();
				cal.setTime(accountDate);
				cal.add(Calendar.DATE, -1);
				accountDateStr=dateUtil.getDateTime(cal.getTime(), "yyyy-MM-dd");
			}
		} catch (ParseException e) {
			logger.error("日期比较错误："+e.getMessage());
		}
		summaryMap.put("accountDate", accountDateStr);
		summaryMap.put("funcCode", TransCodeConst.PAYMENT_PA40142);
		if(summaryDate!=null){
			summaryMap.put("createdTime", summaryDate);
			
		}
		List<Map<String, Object>> resultMap= transOrderInfoManager.selectSummaryInfo(summaryMap);
		System.out.println(resultMap);
		//开始补账环节
		if(resultMap!=null&& resultMap.get(0)!=null){
			try {
				List<Map<String,String>> paramsValueList = new ArrayList<Map<String,String>>();
				Map<String,String> map = new HashMap<String,String>();
				map.put("amount", resultMap.get(0).get("amount").toString());
				map.put("userid", TransCodeConst.THIRDPARTYID_PAYHYSZZH);
				map.put("productid", Constants.FN_PRODUCT);
				map.put("consid", Constants.FN_ID);
				map.put("status", "1");
				map.put("type", "1001");
				paramsValueList.add(map);
				String result=null;			
				result= semiAutomatizationService.ForAccount(paramsValueList);
				if(result.equals("OK")){
					//更新PARAMETER_INFO表
					ParameterInfo parameterInfo=new ParameterInfo();
					parameterInfo.setParameterCode("SummaryPAYSTime");
					parameterInfo.setParameterValue(resultMap.get(0).get("createdTime").toString());
					parameterInfoManager.updateParameterInfoByCode(parameterInfo);
				}
			} catch (Exception e) {
				logger.debug("平安银行其他应收款汇总补账失败"+e.getMessage());
			}
		}else{
			logger.info("平安银行其他应收款汇总为0");
		}
		logger.info("平安银行其他应收款汇总结束《《《《《《");
	}
	
	
	private String dateTime(String code){
		try{
			ParameterInfoQuery parameterInfoQuery=new ParameterInfoQuery();
			parameterInfoQuery.setParameterCode(code);
			List parameterList=parameterInfoManager.queryList(parameterInfoQuery);
			if(parameterList!=null && parameterList.size()>0){
				ParameterInfo parameterInfo=(ParameterInfo) parameterList.get(0);
				return parameterInfo.getParameterValue();
			}else{
				return null;
			}
		}catch(Exception e){
			logger.error(e.getMessage());
			return null;
		}
	}
}
