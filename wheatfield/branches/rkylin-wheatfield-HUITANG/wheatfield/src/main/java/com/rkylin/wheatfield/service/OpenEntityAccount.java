package com.rkylin.wheatfield.service;

import java.util.List;

import com.rkylin.wheatfield.domain.M000003OpenEntityAccountBean;
import com.rkylin.wheatfield.domain.M000003OpenPersonAccountBean;
import com.rkylin.wheatfield.pojo.FinanacePerson;
import com.rkylin.wheatfield.response.Response;

/**
 * 开设实体账户接口
 * 1.个人账户（目前没实现）
 * 2.企业账户
 * @author thonny
 *
 */
public interface OpenEntityAccount {

	/**
	 * 开设企业实体账户
	 * @param openBean
	 * @return
	 */
	String saveMerchantAccount(M000003OpenEntityAccountBean openBean);
	
	/**
	 * 更新企业实体账户
	 * @param openBean
	 * @return
	 */
	String updateMerchantAccount(M000003OpenEntityAccountBean openBean);
	
	/**
	 * 查询企业实体账户
	 * @param openBean
	 * @return
	 */
	Response queryMerchantAccount(M000003OpenEntityAccountBean openBean);

	/**
	 * 批查询企业账户信息
	 * @param openBean
	 * @return
	 */
//	List<FinanaceCompany> batchMerchantQueryAccount(M000003OpenEntityAccountBean openBean);
	
	/**
	 * 开设个人账户
	 * @param openBean
	 * @return
	 */
	String savePersonalAccount(M000003OpenPersonAccountBean openBean);

	/**
	 * 修改个人账户
	 * @param openBean
	 * @return
	 */
	String updatePersonalAccount(M000003OpenPersonAccountBean openBean);
	
	/**
	 * 查询个人账户
	 * @param openBean
	 * @return
	 */
	Response queryPersonalAccount(M000003OpenPersonAccountBean openBean);
	
	/**
	 * 批查询个人账户信息
	 * @param openBean
	 * @return
	 */
	List<FinanacePerson> batchQueryPersonalAccount(M000003OpenPersonAccountBean openBean);
}
