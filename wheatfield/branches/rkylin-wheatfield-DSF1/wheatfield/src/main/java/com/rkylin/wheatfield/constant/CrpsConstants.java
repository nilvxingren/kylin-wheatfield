package com.rkylin.wheatfield.constant;

/**
 * 代收付系统的常量
 * @author Achilles
 *
 */
public class CrpsConstants {
	
	/**
	 * 业务类型：实时
	 */
	public static final Integer BUSSINESS_TYPE_REAL_TIME = 0;
	/**
	 * 业务类型：非实时
	 */
	public static final Integer BUSSINESS_TYPE_NON_REAL_TIME = 1;
	/**
	 * 证件类型：身份证
	 */
	public static final Integer CERTIFICATE_TYPE_ID_CARD = 0;
	/**
	 * 账号属性：私人
	 */
	public static final String ACCOUNT_PROPERTY_PRI = "10";
	/**
	 * 账号属性：公司
	 */
	public static final String ACCOUNT_PROPERTY_COM = "20";
	/**
	 * 账号类型：储蓄卡
	 */
	public static final String ACCOUNT_TYPE_DEPOSIT_CARD = "00";
	/**
	 * 账号类型：存折
	 */
	public static final String ACCOUNT_TYPE_BANKBOOK = "01";
	/**
	 * 账号类型：信用卡
	 */
	public static final String ACCOUNT_TYPE_CREDIT_CARD = "02";	
	/**
	 * 订单类型：代收
	 */
	public static final String ORDER_TYPE_COLLECTION = "1";
	/**
	 * 订单类型：代付
	 */
	public static final String ORDER_TYPE_PAYMENT = "2";
	/**
	 * 币种：人民币
	 */
	public static final String CURRENCY_CNY = "CNY";
	
	
	
	
	/**
	 * 返回码：成功
	 */
	public static final String RETURN_CODE_SUCCESS = "100000";
	/**
	 * 返回码：系统异常
	 */
	public static final String RETURN_CODE_SYS_ERROR = "4444";
	
	/**
	 * 返回状态值：处理中
	 */
	public static final Integer STATUS_PROCESSING = 12;
	/**
	 * 返回状态值：失败
	 */
	public static final Integer STATUS_FAILURE = 13;
	/**
	 * 返回状态值：成功
	 */
	public static final Integer STATUS_SUCCESS = 15;
	/**
	 * 返回状态值：退票
	 */
	public static final Integer STATUS_REFUND = 16;
}
