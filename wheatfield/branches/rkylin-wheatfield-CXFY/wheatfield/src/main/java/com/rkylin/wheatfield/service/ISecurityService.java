package com.rkylin.wheatfield.service;

import java.util.Map;

import com.rkylin.wheatfield.response.Response;

public interface ISecurityService {

	Response verifyRequest(Map<String, String[]> requestParams);

}
