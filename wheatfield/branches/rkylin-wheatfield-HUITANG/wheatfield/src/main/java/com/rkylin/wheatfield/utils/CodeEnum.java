package com.rkylin.wheatfield.utils;
/**
 * 返回代码，信息枚举类
 * */
public enum CodeEnum {
	
	/**
	 * 成功（简单粗暴使用）
	 */
	SUCCESS("1","成功！"),
	/**
	 * 失败（简单粗暴使用）
	 */
	 FAILURE("0","失败！"),
	
	//0**系列开头，为参数方面的异常
	/**
	 * 参数为空！
	 */
	ERR_PARAM_NULL ("Err-000","参数为空！"),
	
	//1**系列开头，为用户基本信息方面的异常
	
	
	//2**系列开头，为用户账户信息方面的异常
	/**
	 * 无法获取用户余额信息！
	 */
	ERR_ACCOUNT_NOT_EXIST("Err-200","无法获取用户余额信息！"),
	/**
	 * 账户状态非正常状态！
	 */
	ERR_ACCOUNT_NOT_NORMAL("Err-201","账户状态非正常状态！"),
	/**
	 * 没有查到用户结算卡！
	 */
	ERR_ACCOUNT_NO_DEBIT_CARD("Err-202","没有查到用户结算卡！"),
	
	//3**系列开头，为用户交易信息方面的异常
	ERR_TRADE_RECORD_NOT_IN("Err-300","账户流水数据入库失败！"),
	ERR_TRADE_RECORD_NO_RESULT("Err-301","交易信息异常！"),	
	
	//8**系列开头，一切正常，数据非法等
	ERR_DATA_NO_RESULT("Err-800","没有查到数据！"),
	ERR_DATA_NOT_NORMAL("Err-801","数据异常！"),
	
	//9**系列开头，系统级别的异常
	//90开头数据库操作异常
	/**
	 * 数据库操作异常！
	 */
	ERR_DATABASE_CALL_ERROR("Err-900","数据库操作异常！"),
	
	//P**系列开头，
	/*
	 * 调用代收付系统的异常!
	 */
	ERR_CRPS_CALL_ERROR("Err-P00","调用代收付系统的异常!");
	
	
	private String code;
	private String message;
	
	private CodeEnum(String code,String message){
		this.code = code;
		this.message = message;
	}
	
	public String getCode(){
		return code;
	}
	public String getMessage(){
		return message;
	}
}
