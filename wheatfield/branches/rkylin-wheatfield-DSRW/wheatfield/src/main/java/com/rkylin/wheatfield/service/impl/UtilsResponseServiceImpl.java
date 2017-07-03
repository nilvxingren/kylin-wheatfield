package com.rkylin.wheatfield.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Rop.api.ApiException;
import com.Rop.api.DefaultRopClient;
import com.Rop.api.domain.SignInfoDetail;
import com.Rop.api.domain.XqsDetail;
import com.Rop.api.request.TlpaySignMessageImportRequest;
import com.Rop.api.request.TlpaySigncheckGetRequest;
import com.Rop.api.response.TlpaySignMessageImportResponse;
import com.Rop.api.response.TlpaySigncheckGetResponse;
import com.rkylin.wheatfield.common.SessionUtils;
import com.rkylin.wheatfield.common.ValHasNoParam;
import com.rkylin.wheatfield.manager.ParameterInfoManager;
import com.rkylin.wheatfield.pojo.AccountAqsDetial;
import com.rkylin.wheatfield.pojo.AccountXqsDetial;
import com.rkylin.wheatfield.pojo.ParameterInfo;
import com.rkylin.wheatfield.response.Response;
import com.rkylin.wheatfield.response.UtilsResponse;
import com.rkylin.wheatfield.service.IAPIService;
import com.rkylin.wheatfield.service.IErrorResponseService;
import com.rkylin.wheatfield.service.UtilsResponseService;

@Service("utilsResponseService")
public class UtilsResponseServiceImpl implements UtilsResponseService,
		IAPIService {
	private static Logger logger = LoggerFactory
			.getLogger(UtilsResponseServiceImpl.class);
	@Autowired
	ParameterInfoManager parameterInfoManager;
	@Autowired
	IErrorResponseService errorResponseService;
	@Autowired
	Properties userProperties;
	
	@Override
	public Response doJob(Map<String, String[]> paramMap, String methodName) {
		UtilsResponse res = new UtilsResponse();
		if("ruixue.wheatfield.signcheck.get".equals(methodName)){
			if (!ValHasNoParam.hasParam(paramMap, "merchantid")) {
				return errorResponseService.getErrorResponse("P5", "机构码不能为空");
			}
			if (!ValHasNoParam.hasParam(paramMap, "acct")) {
				if (!ValHasNoParam.hasParam(paramMap, "agreementno")) {
					return errorResponseService.getErrorResponse("P5", "账号和协议号不能同时为空");
				}
			}
			String acct = "";
			String merchantId="";
			String agreementNo="";
			for (Object keyObj : paramMap.keySet().toArray()) {
				String[] strs = paramMap.get(keyObj);
				for (String value : strs) {
					if ("acct".equals(keyObj)) {
						acct = value;
					}else if("merchantid".equals(keyObj)){
						merchantId=value;
					}else if("agreementno".equals(keyObj)){
						agreementNo=value;
					}
				}
			}
			AccountXqsDetial accountXqsDetial=this.getAqsDetial(merchantId, acct,agreementNo);
			if(accountXqsDetial!=null){
				res.setAccountaqsdetials(accountXqsDetial.getAccountaqsdetials());
				res.setRet_code(accountXqsDetial.getRet_code());
				res.setRet_message(accountXqsDetial.getRet_message());
			}else{
				return errorResponseService.getErrorResponse("C1", "调用通联接口失败！");
			}
		}else if("ruixue.wheatfield.signmessage.import".equals(methodName)){
			if (!ValHasNoParam.hasParam(paramMap, "req_sn")) {
				return errorResponseService.getErrorResponse("P5", "交易流水号不能为空");
			}
			if (!ValHasNoParam.hasParam(paramMap, "contractno")) {
				return errorResponseService.getErrorResponse("P5", "合同号不能为空");
			}
			if (!ValHasNoParam.hasParam(paramMap, "merchant_id")) {
				return errorResponseService.getErrorResponse("P5", "商户代码不能为空");
			}
			if (!ValHasNoParam.hasParam(paramMap, "signinfodetailarray")) {
				return errorResponseService.getErrorResponse("P5", "签约信息明细集合不能为空");
			}
			//交易流水号长度不超过40位
			String req_sn = "";
			//合同号长度不超过20位
			String contractno="";
			String merchant_id="";
			String signinfodetailarray="";
			for (Object keyObj : paramMap.keySet().toArray()) {
				String[] strs = paramMap.get(keyObj);
				for (String value : strs) {
					if ("req_sn".equals(keyObj)) {
						req_sn = value;
					}else if("contractno".equals(keyObj)){
						contractno=value;
					}else if("merchant_id".equals(keyObj)){
						merchant_id=value;
					}else if("signinfodetailarray".equals(keyObj)){
						signinfodetailarray=value;
					}
				}
			}
			try{
				AccountXqsDetial accountXqsDetial = this.impSignmessage(req_sn,contractno,merchant_id,signinfodetailarray);
				if(accountXqsDetial!=null){
					res.setRet_code(accountXqsDetial.getRet_code());
					res.setRet_message(accountXqsDetial.getRet_message());
				}else{
					return errorResponseService.getErrorResponse("C1", "调用通联接口失败！");
				}
			}catch (Exception e){
				return errorResponseService.getErrorResponse("C1","签约信息导入失败");
			}
		}else{
			
			try{		
				//账单日
				String billDay = getParameterInfo(3);
				if(billDay != null && !"".equals(billDay)){
					res.setBillDay(billDay);
				}else{
					return errorResponseService.getErrorResponse("E0","账单日取得失败");
				}
				//还款日
				String repaymentDay = getParameterInfo(4);
				if(repaymentDay != null && !"".equals(repaymentDay)){
					res.setRepaymentDay(repaymentDay);
				}else{
					return errorResponseService.getErrorResponse("E1","还款日取得失败");
				}
			} catch(Exception ex) {
				return errorResponseService.getErrorResponse("E2", "异常发生，账单日和还款日取得失败"); 
			}
		}
		return res;
	}
	@Override
	public AccountXqsDetial impSignmessage(String req_sn, String contractno,
			String merchant_id, String signinfodetailarray) throws Exception {

			Document doc = DocumentHelper.parseText(signinfodetailarray);
            Element signinfodetails = doc.getRootElement();
            Iterator iter = signinfodetails.elementIterator();
            List<SignInfoDetail> signInfoDetail = new ArrayList<SignInfoDetail>();
            while(iter.hasNext()) {
            	SignInfoDetail bean = new SignInfoDetail();
            	Element signinfodetail = (Element) iter.next();
            	bean.setAgreementno(signinfodetail.elementTextTrim("agreementno"));
            	bean.setMobile(signinfodetail.elementTextTrim("mobile"));
            	bean.setAcct(signinfodetail.elementTextTrim("acct"));
            	bean.setAcname(signinfodetail.elementTextTrim("acname"));
            	bean.setBankcode(signinfodetail.elementTextTrim("bankcode"));
            	bean.setBankprov(signinfodetail.elementTextTrim("bankprov"));
            	bean.setBankcity(signinfodetail.elementTextTrim("bankcity"));
            	bean.setBankname(signinfodetail.elementTextTrim("bankname"));
            	bean.setCnapsno(signinfodetail.elementTextTrim("cnapsno"));
            	bean.setAccttype(signinfodetail.elementTextTrim("accttype"));
            	bean.setAcctprop(signinfodetail.elementTextTrim("acctprop"));
            	bean.setIdtype(signinfodetail.elementTextTrim("idtype"));
            	bean.setIdno(signinfodetail.elementTextTrim("idno"));
            	bean.setMaxsingleamt(signinfodetail.elementTextTrim("maxsingleamt"));
            	bean.setDaymaxsucccnt(signinfodetail.elementTextTrim("daymaxsucccnt"));
            	bean.setDaymaxsuccamt(signinfodetail.elementTextTrim("daymaxsuccamt"));
            	bean.setMonmaxsucccnt(signinfodetail.elementTextTrim("monmaxsucccnt"));
            	bean.setMonmaxsuccamt(signinfodetail.elementTextTrim("monmaxsuccamt"));
            	bean.setAgrdeadline(signinfodetail.elementTextTrim("agrdeadline")); 
            	signInfoDetail.add(bean);
            }
			DefaultRopClient ropClient = new DefaultRopClient(
					userProperties.getProperty("SROP_URL"),
					userProperties.getProperty("SAPP_KEY"),
					userProperties.getProperty("SAPP_SECRET"), "xml");
			TlpaySignMessageImportRequest request = new TlpaySignMessageImportRequest();
			request.setReq_sn(req_sn);
			request.setContractno(contractno);
			request.setMerchant_id(merchant_id);
			request.setSigninfodetailarray(signInfoDetail);
			
			TlpaySignMessageImportResponse response = ropClient
					.execute(request, SessionUtils.sessionGet(
							userProperties.getProperty("SROP_URL"),
							userProperties.getProperty("SAPP_KEY"),
							userProperties.getProperty("SAPP_SECRET")));
			if(response!=null){
				AccountXqsDetial accountXqsDetial=new AccountXqsDetial();
				accountXqsDetial.setRet_code(response.getRet_code());
				accountXqsDetial.setRet_message(response.getRet_message());
				return accountXqsDetial;
			} 
		return null;
	}

	@Override
	public String getParameterInfo(long parameterId) {
		ParameterInfo parameterInfo=parameterInfoManager.findParameterInfoById(parameterId);
		if( parameterInfo!=null ){
			return parameterInfo.getParameterValue().toString();
		}
		return null;
	}
	@Override
	public AccountXqsDetial getAqsDetial(String merchantId,String acct,String agreementNo){
		DefaultRopClient ropClient = new DefaultRopClient(
				userProperties.getProperty("SROP_URL"),
				userProperties.getProperty("SAPP_KEY"),
				userProperties.getProperty("SAPP_SECRET"), "xml");
		TlpaySigncheckGetRequest request=new TlpaySigncheckGetRequest();
		request.setAcct(acct);
		request.setMerchant_id(merchantId);
		request.setAgreementno(agreementNo);
		try {
			TlpaySigncheckGetResponse response = ropClient
					.execute(request, SessionUtils.sessionGet(
							userProperties.getProperty("SROP_URL"),
							userProperties.getProperty("SAPP_KEY"),
							userProperties.getProperty("SAPP_SECRET")));
			if(response!=null){
				AccountXqsDetial accountXqsDetial=new AccountXqsDetial();
				if(response.getXqsdetails()!=null && response.getXqsdetails().size()>0){
					AccountAqsDetial aDetial=new AccountAqsDetial();
					List<AccountAqsDetial> accountaqsdetials=new ArrayList<AccountAqsDetial>();
					for (int i = 0; i < response.getXqsdetails().size(); i++) {
						XqsDetail xqsDetial=response.getXqsdetails().get(i);
						aDetial.setAcct(xqsDetial.getAcct());
						aDetial.setAcctname(xqsDetial.getAcctname());
						aDetial.setAgrdeadline(xqsDetial.getAgrdeadline());
						aDetial.setAgreementno(xqsDetial.getAgreementno());
						aDetial.setContractno(xqsDetial.getContractno());
						aDetial.setDaymaxsucccnt(xqsDetial.getDaymaxsucccnt());
						aDetial.setMonmaxsucccnt(xqsDetial.getMonmaxsucccnt());
						aDetial.setDaymaxsuccamt(xqsDetial.getDaymaxsuccamt());
						aDetial.setMonmaxsuccamt(xqsDetial.getMonmaxsuccamt());
						aDetial.setErrmsg(xqsDetial.getErrmsg());
						aDetial.setMaxsingleamt(xqsDetial.getMaxsingleamt());
						aDetial.setMemid(xqsDetial.getMemid());
						aDetial.setStatus(xqsDetial.getStatus());
						accountaqsdetials.add(aDetial);
					}
					accountXqsDetial.setAccountaqsdetials(accountaqsdetials);
				}
				accountXqsDetial.setRet_code(response.getRet_code());
				accountXqsDetial.setRet_message(response.getRet_message());
				return accountXqsDetial;
			}
		} catch (ApiException e) {
			logger.error(e.getMessage());
		}
		return null;
		
	}

}
