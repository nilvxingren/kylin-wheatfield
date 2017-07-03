package com.rkylin.wheatfield.service;

import java.util.Map;

import com.rkylin.wheatfield.response.Response;


public interface IAPIService {

	public Response doJob(Map<String, String[]> paramMap,String methodName);

}
