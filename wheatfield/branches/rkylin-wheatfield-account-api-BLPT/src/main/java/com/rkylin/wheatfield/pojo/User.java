package com.rkylin.wheatfield.pojo;

import java.io.Serializable;

import com.rkylin.wheatfield.enumtype.UserTypeEnum;

/**
 * 
 * @author  zhenpc@chanjet.com
 * @version 2015年1月22日 下午3:07:37
 */

public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/**
	 * 用户ID
	 * */
	public String userId;
	
	/**
	 * 用户类型
	 * */
	public UserTypeEnum userType;
	
	/**
	 * 机构码/商户号
	 * */
	public String constId;
	
	/**
	 * 产品号
	 * */
	public String productId;
	
	/**
	 * 角色号
	 * */
	public String role;
	/**
	 * 用户类型
	 */
	public String uEType;
	
	/**
	 * 操作交易类型
	 */
	public String payType;
	/**
	 * 用户名称
	 */
	public String userName;
	/**
	 * 第三方账户Id 如P2P...
	 */
	public String referUserId;
	/**
	 * 授信类型  101额度授信    102单笔授信
	 */
	public String creditType;
	/**
	 * 账户状态  1正常  2冻结 3销户
	 */
	public String statusID="1";

}
