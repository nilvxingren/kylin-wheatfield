/**
 * @File name : SHBalanceInfoService.java
 * @Package : com.rkylin.wheatfield.service
 * @Description : TODO(用一句话描述该文件做什么)
 * @Creator : Administrator
 * @CreateTime : 2015年11月10日 下午2:22:40
 * @Version : 1.0
 * @Update records:
 *      1.1 2015年11月10日 by Administrator: 
 *      1.0 2015年11月10日 by Administrator: Created 
 * All rights served : FENGNIAN Corporation
 */
package com.rkylin.wheatfield.service;


import com.rkylin.wheatfield.response.SHBalanceInfoResponse;

public interface SHBalanceInfoService {

	/**
	 * @Description : TODO(查询余额)
	 * @CreateTime : 2015年11月10日 
	 * @Creator : liuhuan
	 */
	//@Override
	public SHBalanceInfoResponse getBalanceInfoList(String userId,String rootInstCd);
}
