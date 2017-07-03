package com.rkylin.wheatfield.service;

import com.rkylin.wheatfield.response.CreditApprovalInfoResponse;
import com.rkylin.wheatfield.response.ErrorResponse;

public interface InterestCreditInfoService {

	ErrorResponse editreditInfo(String merchantId, String userId, String productId, String creditResultId, String residualAmount);

	CreditApprovalInfoResponse findcreditApprovalInfo(String merchantId, String userId, String productId, String creditResultId);

}
