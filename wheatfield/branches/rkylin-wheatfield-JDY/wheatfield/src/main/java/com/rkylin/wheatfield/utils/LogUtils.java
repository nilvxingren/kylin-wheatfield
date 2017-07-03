package com.rkylin.wheatfield.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.response.ErrorResponse;
import com.rkylin.wheatfield.response.Response;



public class LogUtils {

	// key@|@value;|;key@|@value
	// 键-值连接符
	private static final String KEY_VALUE_CONNECTOR = "@|@";
	// 属性连接符
	private static final String PROPERTITY_CONNECTOR = ";|;";

	private static Logger logger = Logger.getLogger(LogUtils.class);
	// 日志缓存计数器
	private static int logBufferCounter = 0;
	// 日志缓存
	private static StringBuffer logBuffer = new StringBuffer("");
	// 日志缓存上限（默认为100）
	private static int maxLogBufferSize = 100;

	static{
		Properties logBufferProperties = (Properties)SpringBeanUtils.getBean("logBufferProperties");
		maxLogBufferSize = Integer.parseInt(logBufferProperties.getProperty("maxLogBufferSize"));
	}

	public static void debug(Map<String, String[]> requestParams, Date startTime, Date endTime, Response response, String ip){
		logger.debug(getLogMessage(requestParams, startTime, endTime, response, ip));
	}

	/**
	 * 缓存日志，如果达到缓存上限则输出
	 * 
	 * @param requestParams
	 * @param startTime
	 * @param endTime
	 * @param response
	 * @param ip
	 */
	public static void info(Map<String, String[]> requestParams, Date startTime, Date endTime, Response response, String ip){
		
		// 添加日志到缓存
		logBuffer.append(getLogMessage(requestParams, startTime, endTime, response, ip));
		// 计数器递增
		logBufferCounter++;

		// 当日志缓存记录达到上限时，将缓存的日志输出到文件
		if(logBufferCounter == maxLogBufferSize){
			pushInfo();
		}

	}
	
	/**
	 * 输出缓存中剩余的日志内容
	 */
	public static void pushRestInfo(){
		if(logBufferCounter > 0){
			pushInfo();
		}
	}

	/**
	 * 将缓存中的日志全部输出并重置缓存数据
	 */
	private static void pushInfo(){

		// 输出日志
		logger.info(logBuffer.toString());
		// 清空缓存
		logBuffer.setLength(0);
		// 重置计数器
		logBufferCounter = 0;

	}
	
	public static void warn(Map<String, String[]> requestParams, Date startTime, Date endTime, Response response, String ip){
		logger.warn(getLogMessage(requestParams, startTime, endTime, response, ip));
	}
	
	public static void error(Map<String, String[]> requestParams, Date startTime, Date endTime, Response response, String ip){
		logger.error(getLogMessage(requestParams, startTime, endTime, response, ip));
	}
	
	public static void fatal(Map<String, String[]> requestParams, Date startTime, Date endTime, Response response, String ip){
		logger.fatal(getLogMessage(requestParams, startTime, endTime, response, ip));
	}
	
	/**
	 * 生成日志消息内容
	 * 
	 * @param requestParams
	 * @param startTime
	 * @param endTime
	 * @param response
	 * @param ip
	 * @return
	 */
	private static String getLogMessage(Map<String, String[]> requestParams, Date startTime, Date endTime, Response response, String ip){

    	Set<Map.Entry<String, String[]>> paramSet = requestParams.entrySet();

    	StringBuffer messageBuffer = new StringBuffer("");

    	// 写入请求参数
		for (Entry<String, String[]> param : paramSet) {
			if (StringUtils.areNotEmpty(param.getKey(), param.getValue()[0])) {
				messageBuffer.append(param.getKey());
				messageBuffer.append(KEY_VALUE_CONNECTOR);
				messageBuffer.append(param.getValue()[0]);
				messageBuffer.append(PROPERTITY_CONNECTOR);
			}
		}
		// 写入请求开始时间
		messageBuffer.append("CallBeginTime");
		messageBuffer.append(KEY_VALUE_CONNECTOR);
		messageBuffer.append(getDateTime(startTime));
		messageBuffer.append(PROPERTITY_CONNECTOR);
		// 写入请求结束时间
		messageBuffer.append("CallEndTime");
		messageBuffer.append(KEY_VALUE_CONNECTOR);
		messageBuffer.append(getDateTime(endTime));
		messageBuffer.append(PROPERTITY_CONNECTOR);
		// 写入请求结果
		messageBuffer.append("CallResult");
		messageBuffer.append(KEY_VALUE_CONNECTOR);
		messageBuffer.append(response.getCallResult());
		messageBuffer.append(PROPERTITY_CONNECTOR);
		// 如果请求失败
		if(!response.getCallResult() && ErrorResponse.class.isAssignableFrom(response.getClass())){

			ErrorResponse errorResponse = (ErrorResponse)response;

			// 写入错误代码
			messageBuffer.append("ErrorCode");
			messageBuffer.append(KEY_VALUE_CONNECTOR);
			messageBuffer.append(errorResponse.getCode());
			messageBuffer.append(PROPERTITY_CONNECTOR);
			// 写入错误原因
			messageBuffer.append("ErrorReason");
			messageBuffer.append(KEY_VALUE_CONNECTOR);
			messageBuffer.append(errorResponse.getMsg());
			messageBuffer.append(PROPERTITY_CONNECTOR);
		}
		// 写入请求IP
		messageBuffer.append("UserIPAddress");
		messageBuffer.append(KEY_VALUE_CONNECTOR);
		messageBuffer.append(ip);
		// 换行
		messageBuffer.append(StringUtils.LINE_SEPARATOR);

    	return messageBuffer.toString();
	}

    /**
     * 日期格式转换
     * 
     * @param date
     * @return
     */
    private static String getDateTime(Date date){

    	DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_YYYYMMDDHHMMSSSSS);
    	
    	String dateTime = dateFormat.format(date);

    	return dateTime;
    }
}
