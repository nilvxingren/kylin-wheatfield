/**
*
*/
package com.rkylin.wheatfield.api;

import com.rkylin.wheatfield.response.AccountInfoGetResponse;

/**
 * Description:	   
 * Date:          2015年12月8日 上午11:37:45 
 * @author        sun guoxing
 * @version       1.0 
 */
public interface FinanaceEntryService {
	
	/**
	 * 查询流水信息
	*
	* @param rootinstcd 管理机构代码（查询类型为1时，必须入参）
	* @param userid 用户id（查询类型为1时，必须入参）
	* @param productid 产品号
	* @param createdtimefrom 记录创建开始时间
	* @param createdtimeto 记录创建结束时间
	* @param requestid 交易记录编码（查询类型为2时，必须入参）
	* @param querytype 查询类型（1:根据账户查询 2:根据交易记录查询）
	* @return
	 */
	public AccountInfoGetResponse queryFinanaceentryList(String rootinstcd, String userid,
					String productid, String createdtimefrom, String createdtimeto,String requestid,String querytype);

}

