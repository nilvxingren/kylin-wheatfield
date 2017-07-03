package com.rkylin.wheatfield.api;

import java.util.Map;

import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.pojo.User;

public interface AccountManageDubboService {

	/**
	 * 检查账户有效性（dubbo）
	 * @param user
	 * @return
	 */
	public CommonResponse verifyAccount(User user);
	
	/**
	 * 绑卡
	 * Discription:
	 * @param paramMap
	 * @return CommonResponse
	 * @author Achilles
	 * @since 2017年2月22日
	 */
	public CommonResponse bindCard(Map<String, String[]> paramMap);
}
