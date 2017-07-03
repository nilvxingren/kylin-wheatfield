/**
*
*/
package com.rkylin.wheatfield.api;

import com.rkylin.wheatfield.bean.FinAccountInfoVo;
import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.model.FinAccountInfoResponse;

/**
 * Description:	   
 * Date:          2015年12月8日 下午2:20:47 
 * @author        sun guoxing
 * @version       1.0 
 */
public interface FinanaceAccountService {

	
	/**
	 * 修改账户状态
	 * @param userId 用户id
	 * @param instCode 机构号
	 * @param productId 产品号
	 * @param status 状态值
	 * @return
	 */
	public CommonResponse updateFinAccountStatus(String userId,String instCode,String productId,String status);
	/**
	 * 
	* @Description:根据id修改账户状态
	*
	* @param finAccountId inanaceAccount表的id
	* @param status 要更改成的状态
	* @return
	 */
	public int updateStatusById(String finAccountId, String status); 
	
	/**
     * Discription: 根据入参查询企业开户信息
     * @param params
     * @return FinAccountInfoResponse
     * @author liuhuan
     * @since 2017年4月5日
     */
    public FinAccountInfoResponse getFinAccountsInfo(com.rkylin.wheatfield.bean.User user);
    
    /**
     * Discription: 根据入参查询开户信息
     * @param params
     * @return FinAccountInfoResponse
     * @author liuhuan
     * @since 2017年4月21日
     */
    public FinAccountInfoResponse getFinAccountsInfoByCondition(FinAccountInfoVo finAccountInfoVo);
}
