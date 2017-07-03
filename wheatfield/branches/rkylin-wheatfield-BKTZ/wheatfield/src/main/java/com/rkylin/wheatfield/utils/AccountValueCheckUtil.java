package com.rkylin.wheatfield.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkylin.wheatfield.constant.AccountConstants;
import com.rkylin.wheatfield.response.ErrorResponse;
import com.rkylin.wheatfield.service.IErrorResponseService;

@Service("accountValueCheckUtil")
public class AccountValueCheckUtil{
	@Autowired
	IErrorResponseService iErrorResponseService;
	private static Logger logger = LoggerFactory.getLogger(AccountValueCheckUtil.class);
	/**
	 * 校验账户入参数据的有效性
	 * @param value           入参值【必选】
	 * @param dbSize		     数据库中字段长度【可选】
	 * @param valueType		     值类型(string/date....)【可选】
	 * @param formatType      需要校验格式类型【可选】
	 * @param chName          该入参的中文描述【可选】
	 * @return
	 */
	public ErrorResponse checkValue(String value,int dbSize,int valueType,int formatType,String chName){
		logger.info("账户系统入参校验开始，相关参数信息：");
		logger.info("入参值："+value);
		logger.info("数据库中字段长度："+dbSize);
		logger.info("值类型："+valueType);
		logger.info("需要校验格式类型："+formatType);
		logger.info("该入参的中文描述："+chName);
		ErrorResponse response=new ErrorResponse();
		if(notNullCheck(value)){
			if(formatCheck(value,formatType)){
				if(sizeCheck(value, dbSize, valueType)){
					response.setIs_success(true);
				}else{
					logger.error("入参字符超长");
					response=iErrorResponseService.getAccountErrorResponse(AccountConstants.ACCOUNT_ERRORCODE1);
					response.setMsg(chName+response.getMsg());
				}
			}else {
				logger.error("入参格式错误");
				response=iErrorResponseService.getAccountErrorResponse(AccountConstants.ACCOUNT_ERRORCODE3);
				response.setMsg(chName+response.getMsg());
			}
		}else{
			logger.error("入参值为空");
			response=iErrorResponseService.getAccountErrorResponse(AccountConstants.ACCOUNT_ERRORCODE2);
			response.setMsg(chName+response.getMsg());
		}
		
		
		
		
		return response;
	}
	/**
	 * 入参值非空校验
	 * @param value
	 * @return
	 */
	public boolean notNullCheck(String value){
		if(null!=value&&!"".equals(value)&&!"".equals(value.trim())){
			return true;
		}else {
			return false;
		}
	}
	/**
	 * 校验入参长度信息
	 * @param value         入参值
	 * @param dbSize	          库中该字段大小
	 * @param valueType     值类型
	 * @return
	 */
	public boolean sizeCheck(String value,int dbSize,int valueType){
		return true;
	}
	/**
	 * 入参格式校验
	 * @param value			入参值【必选】
	 * @param formatType	需要校验格式类型【必选】
	 * @return
	 */
	public boolean formatCheck(String value,int formatType){
		boolean result=false;
		//需要校验的格式值
		Pattern pattern;
		//匹配器，是否与正则的规则匹配
		Matcher matcher;
        String checkRegEx;
		switch (formatType) {
			case AccountConstants.ACCOUNT_VALUE_CHECK_FORMAT1://邮箱校验
				checkRegEx ="^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
				pattern = Pattern.compile(checkRegEx);
				matcher = pattern.matcher(value);
				break;
			case AccountConstants.ACCOUNT_VALUE_CHECK_FORMAT2://电话号码(包含手机号码、传真号码)
				checkRegEx ="^([1][358][0-9]{9})|([0-9]{3,4}-)?[0-9]{7,8})$";
				pattern = Pattern.compile(checkRegEx);
				matcher = pattern.matcher(value);
				break;
			case AccountConstants.ACCOUNT_VALUE_CHECK_FORMAT3://邮编
				checkRegEx ="^[1-9]d{5}(?!d)$";
				pattern = Pattern.compile(checkRegEx);
				matcher = pattern.matcher(value);
				break;
			case AccountConstants.ACCOUNT_VALUE_CHECK_FORMAT4://身份证号码
				checkRegEx ="^([1-9]{15}|[1-9]{18}|[1-9]{17}([1-9]|X|x))$";
				pattern = Pattern.compile(checkRegEx);
				matcher = pattern.matcher(value);
				break;
			case AccountConstants.ACCOUNT_VALUE_CHECK_FORMAT5://密码
//				checkRegEx ="^[A-Za-z0-9\@\!\#\$\%\^\&\*\.\~]$";
				checkRegEx ="^[a-zA-Z0-9]{6,16}$";
				pattern = Pattern.compile(checkRegEx);
				matcher = pattern.matcher(value);
				break;
			case AccountConstants.ACCOUNT_VALUE_CHECK_FORMAT6://日期
				checkRegEx ="^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))?$";
				pattern = Pattern.compile(checkRegEx);
				matcher = pattern.matcher(value);
				result = matcher.matches();
				break;
			default:
				result=true;
				break;	
		}
		return result;
	}
}
