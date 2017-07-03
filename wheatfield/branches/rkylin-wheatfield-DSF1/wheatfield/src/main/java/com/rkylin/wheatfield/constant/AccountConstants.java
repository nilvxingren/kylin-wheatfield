package com.rkylin.wheatfield.constant;
/**
 * 
 * @author  zhenpc@chanjet.com
 * @version 2015年3月16日 下午9:05:41
 */

public class AccountConstants {
	
	/**
	 * 1 -> 账户类型
	 * 2 -> 管理分组
	 * 3 -> 核算分组
	 * */
	/**
	 * 基本账户类型
	 * */
	public static final String ACCOUNT_TYPE_BASE="10001";
	
	/**
	 * 子账户类型
	 * */
	public static final String ACCOUNT_TYPE_CHILD="10002";

	
	/**
	 * 管理分组公共编号
	 * */
	public static final String GROUP_MANAGE_BASE="20001";
	
	/**
	 * 核算分组公共编号
	 * */
	public static final String GROUP_SETTLE_BASE="30001";
	
	/**
	 * 个人账户状态生效
	 * */
	public static final String PERSON_STATUS_ON="1";
	
	/**
	 * 个人账户状态失效
	 * */
	public static final String PERSON_STATUS_OFF="0";
	
	/**
	 * 企业账户状态生效
	 * */
	public static final String COMPANY_STATUS_ON="1";
	
	/**
	 * 企业账户状态失效
	 * */
	public static final String COMPANY_STATUS_OFF="0";
	
	/**
	 * 个人类别：个人
	 * */
	public static final String PERSON_TYPE_PER="1";
	
	/**
	 * 君融贷业务类型-投资
	 * */
	public static final String JRD_BUSITYPE_INVESTMENT="1";	
	
	/**
	 * 君融贷业务类型-回款
	 * */
	public static final String JRD_BUSITYPE_RECEIVABLE="2";
	
	/**
	 * 君融贷业务类型-现金券
	 * */
	public static final String JRD_BUSITYPE_CASH="3";
	
	/**
	 * 君融贷业务类型-债权转让
	 * */
	public static final String JRD_BUSITYPE_RIGHTS_TRANS="4";
	
	/**
	 * 君融贷投资
	 */
	public static final String JRD_BUSITYPE_INVEST = "5";
//####################密码相关             ########################
	
	/**
	 * 君融贷业务类型-活期赎回
	 * */
	public static final String JRD_BUSITYPE_REDEMPTION = "7";
	
	/**
	 * 账户预设密码
	 */
	public static final int INITIAL_PASSWORD_SET=1;
	
	/**
	 * 账户未预设密码
	 */
	public static final int INITIAL_PASSWORD_UNSET=0;
	
	/**
	 * 密码连错次数初始值
	 */
	public static final int ERROR_COUNT_INITIAL=0;
	/**
	 * 授权码状态---失效
	 */
	public static final String AUTHCODE_STATUS0="0";
	/**
	 * 授权码状态---正常
	 */
	public static final String AUTHCODE_STATUS1="1";
	
	/**
	 * 允许密码连错次数的限制未开启
	 */
	public static final int ALLOW_ERROR_UNSET =0;
	
	/**
	 * 丰年密码连错次数的限制
	 */
	public static final String PARA_CODE_FN = "AllowErrorCountFN";
	
	/**
	 * 会堂密码连错次数的限制
	 */
	public static final String PARA_CODE_HT = "AllowErrorCountHT";
	
	/**
	 * 课栈密码连错次数的限制
	 */
	public static final String PARA_CODE_KZ = "AllowErrorCountKZ";
//###########################################################
//####################账户系统数值校验########################
	/**
	 * 账户系统值类型为string
	 */
	public static final int ACCOUNT_VALUE_CHECK_TYPE1=1;
	/**
	 * 账户系统值类型为long
	 */
	public static final int ACCOUNT_VALUE_CHECK_TYPE2=2;
	/**
	 * 账户系统值类型为Integer
	 */
	public static final int ACCOUNT_VALUE_CHECK_TYPE3=3;
	/**
	 * 账户系统值类型为date
	 */
	public static final int ACCOUNT_VALUE_CHECK_TYPE4=4;
	/**
	 * 账户系统值格式类型--邮箱
	 */
	public static final int ACCOUNT_VALUE_CHECK_FORMAT1=1;
	/**
	 * 账户系统值格式类型--电话号码(包含手机号码、传真号码)
	 */
	public static final int ACCOUNT_VALUE_CHECK_FORMAT2=2;
	/**
	 * 账户系统值格式类型--邮编
	 */
	public static final int ACCOUNT_VALUE_CHECK_FORMAT3=3;
	/**
	 * 账户系统值格式类型--身份证号码
	 */
	public static final int ACCOUNT_VALUE_CHECK_FORMAT4=4;
	/**
	 * 账户系统值格式类型--密码
	 */
	public static final int ACCOUNT_VALUE_CHECK_FORMAT5=5;
	/**
	 * 账户系统值格式类型--日期
	 */
	public static final int ACCOUNT_VALUE_CHECK_FORMAT6=6;
	
	/**
	 * 储蓄卡
	 */
	public static final String DEPOSIT_CARD="00";
	/**
	 * 存折
	 */
	public static final String BANKBOOK="01";
	/**
	 * 信用卡
	 */
	public static final String CREDIT_CARD="02";
	/**
	 * 账户系统错误码--超长
	 */
	public static final String ACCOUNT_ERRORCODE1="C103001";
	/**
	 * 账户系统错误码--不能为空
	 */
	public static final String ACCOUNT_ERRORCODE2="C103002";
	/**
	 * 账户系统错误码--格式有误
	 */
	public static final String ACCOUNT_ERRORCODE3="C103003";

//###########################################################
//####################入参枚举值校验##########################
	/**
	 * 账户密码类型，如新增密码类型  只需在数组中添加相应密码类型
	 * P为支付类型
	 * Q为查询类型
	 */
	public static final String[] PASSWORD_TYPE={"P","Q"};
	
	/**
	 * 账户密码操作类型
	 * insert为新增密码
	 * update为更新密码
	 */
	public static final String[] PASSWORD_OPER={"insert","update"};

	/**
	 * 账户密码操作类型
	 * lockup为密码锁定
	 * unlock为密码解锁
	 */
	public static final String[] LOCK_OPER={"lockup","unlock"};
//###########################################################
//####################正则表达式#############################
	/**
	 * 邮箱正则
	 */
	public static final String REGULAR_EMAIL="";
	/**
	 * 电话号码正则
	 */
	public static final String REGULAR_PHONE="";
	/**
	 * 邮编正则
	 */
	public static final String REGULAR_POST="";
	/**
	 * 身份证正则
	 */
	public static final String REGULAR_IDCARD="";
	/**
	 * 密码正则
	 */
	public static final String REGULAR_PASSWORD="";
	/**
	 * 日期正则
	 */
	public static final String REGULAR_DATE="";
//###########################################################
//#####################账户预置子账户#########################
	/**
	 * 二维数组中第1行存放的是需要预置子帐户的机构码(如丰年M000001,君融贷M000005)
	 * 第2~（n-1）存放的是对应第1行机构码需要预置的子账户产品号
	 * 为需要预置的是丰年子账户产品号
	 * P000010为丰年红包子帐户产品号
	 * P000012为君融贷充值子账户产品号
	 * P000013为君融贷融资子账户产品号
	 * P000015为食全食美收款子账户产品号
	 */
	public static final String[][] ORGANIZATION_CONSTIDS_PRODEUCTS={
		{"M000001","M000005","M000006"},
		{"P000010"},
		{"P000012","P000013"},
		{"P000015"}
	};
//###########################################################
	
}
