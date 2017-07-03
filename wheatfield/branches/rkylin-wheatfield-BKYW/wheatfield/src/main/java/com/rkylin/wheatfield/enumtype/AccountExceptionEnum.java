package com.rkylin.wheatfield.enumtype;

/**
 * 账户账务异常类
 * @author zhenpc
 *
 */
public enum AccountExceptionEnum {
	
	/**
	 * 系统、参数异常返回码
	 */
	SYS_ERROR("1001","SYS_ERROR"), /*系统异常*/
	/**
	 * 参数错误
	 */
	PARAMS_ERROR("1002","PARAMS_ERROR"), /*系统异常*/
	
	OVERDRAW_EXCEPTION("2001","OVERDRAW_EXCEPTION"),
	
	OVERDRAW_AMOUNT_EXCEPTION("2002","OVERDRAW_AMOUNT_EXCEPTION"),
	
	SUBJECT_MATCHE_EXCEPTION("3001","SUBJECT_MATCHE_EXCEPTION"),
	
	FROZEN_MORE_THAN_BALANCE("3002","FROZEN_MORE_THAN_BALANCE"),
	
	UNFROZEN_MORE_THAN_BALANCE("3003","UNFROZEN_MORE_THAN_BALANCE"),
	
	FROZEN_NEGATIVE_AMOUNT("3004","FROZEN_NEGATIVE_AMOUNT"),
	
	ACCOUNT_NOT_EXIST("4001","ACCOUNT_NOT_EXIST"),
	
	BANK_ACCOUNT_NOT_EXIST("4001","BANK_ACCOUNT_NOT_EXIST"),
	
	MULTI_ACCOUNT_NO_EXCEPTION("4001","MULTI_ACCOUNT_NO_EXCEPTION"),
	
	PASSWORD_DOESNOT_MATCH("4001","PASSWORD_DOESNOT_MATCH"),
	
	NO_ACCOUNT_PAY_AUTH("4001","NO_ACCOUNT_PAY_AUTH"),
	
	BALANCE_NOT_ENOUGH("4001","BALANCE_NOT_ENOUGH"),
	
	NO_ACCOUNT_RECEIVE_AUTH("4001","NO_ACCOUNT_PAY_AUTH"),
	
	NO_OPERATE_AUTH("4001","NO_OPERATE_AUTH"),
	
	MULTI_OWNER_EXCEPTION("4001","MULTI_OWNER_EXCEPTION"),
	
	NOT_FOUND_TRANSINFO("4001","NOT_FOUND_TRANSINFO"),
	
	
	
	/**
	 * 数据异常
	 */
	NO_RECORD_FUND("2001","NO_RECORD_FUND"),
	MULTI_RECORD_FUND("2001","MULTI_RECORD_FUND"),
	TOTAL_COUNT_MISS("2001","TOTAL_COUNT_MISS");/*记录数量不匹配异常*/

	
	private String defineCode;
	private String defineMsg;
	
	public String getDefineCode() {
		return defineCode;
	}
	public void setDefineCode(String defineCode) {
		this.defineCode = defineCode;
	}
	public String getDefineMsg() {
		return defineMsg;
	}
	public void setDefineMsg(String defineMsg) {
		this.defineMsg = defineMsg;
	}
	
	private AccountExceptionEnum(String defineCode, String defineMsg){
		this.defineCode=defineCode;
		this.defineMsg=defineMsg;
	}
}
