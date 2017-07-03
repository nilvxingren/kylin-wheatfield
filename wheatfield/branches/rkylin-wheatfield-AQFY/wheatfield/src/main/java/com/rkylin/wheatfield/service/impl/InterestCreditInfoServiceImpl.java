package com.rkylin.wheatfield.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Rop.api.domain.ApprovedCredit;
import com.Rop.api.request.WheatfieldInterestCreditQueryRequest;
import com.Rop.api.response.WheatfieldInterestCreditQueryResponse;
import com.rkylin.utils.DateUtil;
import com.rkylin.wheatfield.common.ValHasNoParam;
import com.rkylin.wheatfield.manager.CreditApprovalInfoManager;
import com.rkylin.wheatfield.pojo.CreditApprovalInfo;
import com.rkylin.wheatfield.pojo.CreditApprovalInfoQuery;
import com.rkylin.wheatfield.response.CreditApprovalInfoResponse;
import com.rkylin.wheatfield.response.ErrorResponse;
import com.rkylin.wheatfield.response.Response;
import com.rkylin.wheatfield.service.IAPIService;
import com.rkylin.wheatfield.service.IErrorResponseService;
import com.rkylin.wheatfield.service.InterestCreditInfoService;

/**
 * 计息的授信业务类
 * @author Administrator
 *
 */
@Service("interestCreditInfoService")
public class InterestCreditInfoServiceImpl implements InterestCreditInfoService, IAPIService {
	
	@Autowired
	private IErrorResponseService errorResponseService;
	
	@Autowired
	private CreditApprovalInfoManager creditApprovalInfoManager;
	
	@Override
	public Response doJob(Map<String, String[]> paramMap, String methodName) {
		Response response = null;
		if("ruixue.wheatfield.interest.credit.query".equals(methodName)){
			if(!ValHasNoParam.hasParam(paramMap, "userid")){
				return errorResponseService.getErrorResponse("P1","userid不能为空");
			}
			if(!ValHasNoParam.hasParam(paramMap, "merchantid")){
				return errorResponseService.getErrorResponse("P2","merchantid不能为空");
			}
			if(!ValHasNoParam.hasParam(paramMap, "productid")){
				return errorResponseService.getErrorResponse("P3","productid不能为空");
			}
			if(!ValHasNoParam.hasParam(paramMap, "creditresultid")){
				return errorResponseService.getErrorResponse("P4","creditresultid不能为空");
			}
			String userId = paramMap.get("userid")[0];
			String merchantId = paramMap.get("merchantid")[0];
			String productId = paramMap.get("productid")[0];
			String creditResultId = paramMap.get("creditresultid")[0];
			//String providerId = paramMap.get("providerid")[0];
			response = this.findcreditApprovalInfo(merchantId, userId, productId, creditResultId);
			return response;
		}else if("ruixue.wheatfield.interest.credit.edit".equals(methodName)){
			if(!ValHasNoParam.hasParam(paramMap, "userid")){
				return errorResponseService.getErrorResponse("P1","userid不能为空");
			}
			if(!ValHasNoParam.hasParam(paramMap, "merchantid")){
				return errorResponseService.getErrorResponse("P2","merchantid不能为空");
			}
			if(!ValHasNoParam.hasParam(paramMap, "productid")){
				return errorResponseService.getErrorResponse("P3","productid不能为空");
			}
			if(!ValHasNoParam.hasParam(paramMap, "creditresultid")){
				return errorResponseService.getErrorResponse("P4","creditresultid不能为空");
			}
			String userId = paramMap.get("userid")[0];
			String merchantId = paramMap.get("merchantid")[0];
			String productId = paramMap.get("productid")[0];
			String creditResultId = paramMap.get("creditresultid")[0];
			String residualAmount = paramMap.get("residualamount")[0];
			response = this.editreditInfo(merchantId, userId, productId, creditResultId, residualAmount);
			return response;
		}
		return null;
	}
	
	@Override
	public CreditApprovalInfoResponse findcreditApprovalInfo(String merchantId, String userId, String productId, String creditResultId){
		CreditApprovalInfoResponse response = new CreditApprovalInfoResponse();
		//WheatfieldInterestCreditQueryResponse response = new WheatfieldInterestCreditQueryResponse();
		CreditApprovalInfoQuery query = new CreditApprovalInfoQuery();
		query.setRootInstCd(merchantId);
		query.setUserId(userId);
		query.setProductId(productId);
		query.setCreditResultId(creditResultId);
		query.setStatusId("1");//只查询成功
		//query.setProviderId(providerId);
		List<CreditApprovalInfo> list = creditApprovalInfoManager.queryList(query);
		if(list == null || list.size() == 0){
			//没有找到授信信息
			response.setIs_success(false);
			response.setRetmsg("没有找到授信信息");
			return response ;
		}
		if(list.size() > 1){
			//授信结果不唯一
			response.setIs_success(false);
			response.setRetmsg("授信结果不唯一");
			return response ;
		}
		CreditApprovalInfo credit = list.get(0);
		ApprovedCredit cr = new ApprovedCredit();
		cr.setAmount(String.valueOf(credit.getAmount()));
		cr.setAmountsingle(String.valueOf(credit.getAmountSingle()));
		cr.setCreditresultid(credit.getCreditResultId());
		cr.setCurrency(credit.getCurrency());
		cr.setDeadline(credit.getDeadLine());
		cr.setEndtime(DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", credit.getEndTime()));
		cr.setInterestdate(DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", credit.getInterestDate()));
		cr.setMerchantid(credit.getMerchantId());
		cr.setProductid(credit.getProductId());
		cr.setResidualamount(credit.getExpansion6());
		cr.setStarttime(DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", credit.getStartTime()));
		cr.setUserid(credit.getUserId());
		cr.setStatus(credit.getStatusId());
		response.setIs_success(true);
		response.setApprovedcredit(cr);
		return response;
	}
	
	
	/**
	 * 修改可用额度
	 * @param merchantId
	 * @param userId
	 * @param productId
	 * @param creditResultId
	 * @param residualAmount
	 */
	@Override
	public ErrorResponse editreditInfo(String merchantId, String userId, String productId, String creditResultId, String residualAmount){
		ErrorResponse response = new ErrorResponse();
		CreditApprovalInfoQuery query = new CreditApprovalInfoQuery();
		query.setRootInstCd(merchantId);
		query.setUserId(userId);
		query.setProductId(productId);
		query.setCreditResultId(creditResultId);
		List<CreditApprovalInfo> list = creditApprovalInfoManager.queryList(query);
		if(list == null || list.size() == 0){
			//没有找到授信信息
			response.setIs_success(false);
			response.setMsg("没有找到授信信息");
			return response;
		}
		if(list.size() > 1){
			//记录不唯一
			response.setIs_success(false);
			response.setMsg("授信信息不唯一");
			return response;
		}
		CreditApprovalInfo creditInfo = list.get(0);
		creditInfo.setExpansion6(residualAmount);
		creditApprovalInfoManager.saveCreditApprovalInfo(creditInfo);
		response.setIs_success(true);
		response.setMsg("修改成功");
		return response;
	}

}
