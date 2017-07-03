package com.rkylin.wheatfield.service;

import com.rkylin.wheatfield.response.ErrorResponse;



public interface IErrorResponseService {
	
	public ErrorResponse getErrorResponse(String code);

	public ErrorResponse getErrorResponse(String code, String msg);

	public ErrorResponse getErrorResponse(String code, String msg, String subCode, String subMsg);

	public ErrorResponse getAccountErrorResponse(String code);
	
	public ErrorResponse getAccountErrorResponse(String code, String msg);
}
