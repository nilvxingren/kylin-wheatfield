package com.rkylin.wheatfield.service;

import com.rkylin.wheatfield.model.BalanceDeResponse;
import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.model.FinAccountResponse;
import com.rkylin.wheatfield.model.OpenSubAccountResponse;
import com.rkylin.wheatfield.pojo.FinanaceEntry;
import com.rkylin.wheatfield.pojo.User;

public interface AccountInfoService {
    
    /**
     * 查询账户信息
     */
    public FinAccountResponse getFinAccount(com.rkylin.wheatfield.bean.User user);
	/**
	 * 查询余额（dubbo）
	 * @param user
	 */
	public BalanceDeResponse getBalance(com.rkylin.wheatfield.bean.User user);
	/**
	 * 用户密码校验
	 * @param user
	 * @return
	 */
	public CommonResponse passwordCheck(com.rkylin.wheatfield.bean.User user);
	/**
	 * 开子账户
	 * @param user
	 * @param finanaceEntry
	 * @return
	 */
	public CommonResponse openSubAccount(User user, FinanaceEntry finanaceEntry);
	
    /**
     * 运营平台调用账户,账户去多渠道开户,成功后在账户开户两个子账户
     * Discription:
     * @param user
     * @return CommonResponse
     * @author Achilles
     * @since 2016年4月26日
     */
    public OpenSubAccountResponse openSubAccountMice(com.rkylin.wheatfield.bean.User user);
}
