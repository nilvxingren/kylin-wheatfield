package com.rkylin.wheatfield.service;

import com.rkylin.wheatfield.pojo.AccountPassword;
import com.rkylin.wheatfield.pojo.User;

public interface AccountPasswordService {
	/**
	 * 保存用户密码
	 * @param user
	 * @return
	 */
	String savePassword(AccountPassword accountPassword, String newPassword,
			User user);
	/**
	 * 校验用户名密码
	 * @param accountPassword
	 * @return
	 */
	
	String checkPassword(AccountPassword accountPassword);
	
	 /**
     * 查询是否设置过密码
     * @author liuhuan
     * @param accountPassword
     * @return
     */
    String queryPassword(AccountPassword accountPassword);
}
