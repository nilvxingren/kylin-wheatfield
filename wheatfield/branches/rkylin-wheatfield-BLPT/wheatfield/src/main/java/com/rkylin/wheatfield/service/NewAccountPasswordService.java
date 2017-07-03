package com.rkylin.wheatfield.service;

import com.rkylin.wheatfield.domain.AccountPasswordBean;
import com.rkylin.wheatfield.pojo.User;

public interface NewAccountPasswordService {
	
	/**
	 * 新增修改密码
	 * @param opertype是insert：新增密码；opertype是 update：修改密码
	 */
	String operPassword (AccountPasswordBean accountPasswordBean,User user);
	
	/**
	 * 锁定解锁密码
	 * @param opertype是lockup：锁定密码；opertype是 unlock：解锁密码
	 */
	String operLock (AccountPasswordBean accountPasswordBean,User user);
	
	/**
	 * 校验用户名密码
	 * @param accountPasswordBean
	 * @return
	 */
	String checkPassword(AccountPasswordBean accountPasswordBean,User user);
	

}
