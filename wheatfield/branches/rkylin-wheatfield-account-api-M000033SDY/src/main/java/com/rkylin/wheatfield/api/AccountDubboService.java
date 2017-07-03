package com.rkylin.wheatfield.api;

import java.util.Map;

import com.rkylin.wheatfield.domain.M000003OpenEntityAccountBean;
import com.rkylin.wheatfield.domain.M000003OpenPersonAccountBean;
import com.rkylin.wheatfield.model.CommonResponse;

/**
 * 与账户有关的操作
 * @author Achilles
 *
 */
public interface AccountDubboService {
	
	/**
	 * 个人开户(bubbo)
	 * @param accountBean
	 * @return
	 */
	public CommonResponse openPrivateAccount(M000003OpenPersonAccountBean accountBean);
	
	/**
	 * 企业开户
	 * @param openBean
	 * @return 
	 */
	public String saveMerchantAccount(M000003OpenEntityAccountBean accountBean);
}
