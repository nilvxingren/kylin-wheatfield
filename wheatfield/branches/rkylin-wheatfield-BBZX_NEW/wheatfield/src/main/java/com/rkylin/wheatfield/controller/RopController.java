package com.rkylin.wheatfield.controller;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;


import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.constant.SpringBeanConstants;
import com.rkylin.wheatfield.response.Response;
import com.rkylin.wheatfield.service.IAPIService;
import com.rkylin.wheatfield.service.ISecurityService;
import com.rkylin.wheatfield.service.impl.SettlementServiceImpl;
import com.rkylin.wheatfield.utils.LogUtils;
import com.rkylin.wheatfield.utils.ModelAndViewUtils;
import com.rkylin.wheatfield.utils.SpringBeanUtils;



@Controller
@Scope("prototype")
public class RopController {
	private static Logger logger = LoggerFactory
			.getLogger(RopController.class);
	@Autowired
	private HttpServletRequest request;
	@Autowired
	@Resource(name=SpringBeanConstants.SECURITY_SERVICE)
	private ISecurityService securityService;

    @RequestMapping("/ropapi")
    public ModelAndView execute() {
    	Date startTime = new Date();

		@SuppressWarnings("unchecked")
		Map<String, String[]> requestParams = request.getParameterMap();

		//logger.info("-----------appkey--------------"+requestParams.get(Constants.SYS_PARAM_APP_KEY)[0]);
    	// 1. 验证request参数
    	Response response = securityService.verifyRequest(requestParams);

        String method = request.getParameter(Constants.SYS_PARAM_METHOD);
        
        String format = request.getParameter(Constants.SYS_PARAM_FORMAT);
    	
        // 如果验证通过
    	if(response == null){
        	// 2. 通过适配器调用相应API
    		IAPIService apiService = (IAPIService)SpringBeanUtils.getBean(method);

    		response = apiService.doJob(requestParams,method);
    	}

    	if(!response.getCallResult()) {
    		method = "error";
    	}

        // 3. 将API结果映射为相应数据格式并返回
        ModelAndView mav = ModelAndViewUtils.getModelAndView(method, format, response);

        String ip = request.getRemoteAddr();
        
        Date endTime = new Date();

        LogUtils.info(requestParams, startTime, endTime, response, ip);

        return mav;
    }

}
