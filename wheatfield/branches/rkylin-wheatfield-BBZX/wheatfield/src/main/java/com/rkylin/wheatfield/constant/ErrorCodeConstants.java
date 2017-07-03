package com.rkylin.wheatfield.constant;

public class ErrorCodeConstants {

	/** 系统级错误码 */
	// S0：服务请求失败
	public static final String SYS_ERROR_CODE_S0 = "S0";
	// S1：没有传递任何参数
	public static final String SYS_ERROR_CODE_S1 = "S1";
	// S2：没有传递参数:app_key
	public static final String SYS_ERROR_CODE_S2 = "S2";
	// S3：app_key不存在
	public static final String SYS_ERROR_CODE_S3 = "S3";
	// S4：没有传递参数:method
	public static final String SYS_ERROR_CODE_S4 = "S4";
	// S5：没有传递参数:format
	public static final String SYS_ERROR_CODE_S5 = "S5";
	// S6：没有传递参数:session
	public static final String SYS_ERROR_CODE_S6 = "S6";
	// S7：没有传递参数:sign
	public static final String SYS_ERROR_CODE_S7 = "S7";
	// S8：没有传递参数:timestamp
	public static final String SYS_ERROR_CODE_S8 = "S8";
	// S9：session无效
	public static final String SYS_ERROR_CODE_S9 = "S9";
	// S10：timestamp无效
	public static final String SYS_ERROR_CODE_S10 = "S10";
	// S11：sign无效
	public static final String SYS_ERROR_CODE_S11 = "S11";
	// S12：该IP地址无访问权限，请联系相关负责人
	public static final String SYS_ERROR_CODE_S12 = "S12";
	// S13：API没有调用的权限
	public static final String SYS_ERROR_CODE_S13 = "S13";
	//日期格式为yyyy-MM-dd
	public static final String SYS_ERROR_CODE_M1="M1";
	/** 业务级错误码 */
	/**数据库访问失败*/
	public static final String ACCOUNT_ERROR_C102001="C102001";
	/**账户冻结失败 */
	public static final String ACCOUNT_ERROR_C102002="C102002";
	/**暂无账户或账户无效*/
	public static final String ACCOUNT_ERROR_C104001="C104001";
	/**密码类型输入有误*/
	public static final String ACCOUNT_ERROR_P105001="P105001";
	/**操作类型输入有误*/
	public static final String ACCOUNT_ERROR_P105002="P105002";
	/**该账户已经设置过此类型的密码*/
	public static final String ACCOUNT_ERROR_P105003="P105003";
	/**该账户没有设置过此类型的密码*/
	public static final String ACCOUNT_ERROR_P105004="P105004";
	/**该账户密码已经被锁定*/
	public static final String ACCOUNT_ERROR_P105005="P105005";
	/**该账户密码已经是解锁状态*/
	public static final String ACCOUNT_ERROR_P105006="P105006";
	/**密码错误！当前密码错误次数为：*/
	public static final String ACCOUNT_ERROR_P105007="P105007";
	/**密码错误次数超限，账户冻结*/
	public static final String ACCOUNT_ERROR_P105008="P105008";
	/**取得允许密码连续错误次数失败*/
	public static final String ACCOUNT_ERROR_P105009="P105009";
//	
}
