package com.rkylin.wheatfield.exception;


import com.rkylin.exception.BaseException;
import com.rkylin.wheatfield.enumtype.AccountExceptionEnum;


public class AccountException extends BaseException {

	private static final long serialVersionUID = 1L;
	
	public AccountException(String defineCode) {
		super(defineCode);
	}

	public AccountException(AccountExceptionEnum exp) {
		super(exp.getDefineCode(), exp.getDefineMsg());
	}
    
	public AccountException(String defineCode , String defineMsg){
		super(defineCode, defineMsg);
	}
}
