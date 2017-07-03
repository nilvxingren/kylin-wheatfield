package com.rkylin.wheatfield.constant;

public class RedisConstants {
    
    public static final String ALL_KEYS_PREFIX= "ACCOUNT_";
    
	/**
	 * 代收付系统推送的代收付数据所有Key的对应的key
	 */
	public static final String GENERATIONPAYMENT_PUSH= "GENERATIONPAYMENT_PUSH";
	
	/**
	 * 代收付系统推送的代收付数据的部分Key
	 */
	public static final String GENERATIONPAYMENT_ORDER_NOS_PUSH= "_ORDER_NOS_PUSH_";

    /**
     * 机构号和主账户对应关系
     */
    public static final String INSTCODE_TO_PRODUCT_KEY= ALL_KEYS_PREFIX+"INST_TO_PROD";
	public static final String PARAMETER_INFO_CODE = "ZH_YFYZ";
	
	/**
     * 获取绑卡判断该机构内开户的证件号需要相同的机构Key
     */
    public static final String PARAMETER_INFO_CODE_BK_YZJG= "BK_YZJG";
    
    /**
     * 开户时需要校验证件+手机唯一的机构
     */
    public static final String OPEN_ACCOUNT_INST_VERIFY= ALL_KEYS_PREFIX+"OP_INST_VRY";
	/**
     * 清结算系统推送的代收付数据所有Key集合的对应的key
     */
    public static final String RECEIVE_AND_PAYMENT_PUSH_ALL_KEYS= ALL_KEYS_PREFIX+"RECEIVE_AND_PAYMENT_PUSH_ALL_KEYS";
    
    /**
     * 清结算系统推送的代收付数据一次成功数据的key前缀
     */
    public static final String RECEIVE_AND_PAYMENT_PUSH_SUCCESS_KEY_PREFIX= ALL_KEYS_PREFIX+"RECEIVE_AND_PAYMENT_PUSH_SUCCESS_";
    
    /**
     * 清结算系统推送的代收付数据一次失败数据的key前缀
     */
    public static final String RECEIVE_AND_PAYMENT_PUSH_FAIL_KEY_PREFIX= ALL_KEYS_PREFIX+"RECEIVE_AND_PAYMENT_PUSH_FAIL_";
    
    /**
     * 清结算系统推送的代收付数据一次退票数据的key前缀
     */
    public static final String RECEIVE_AND_PAYMENT_PUSH_REFUND_KEY_PREFIX= ALL_KEYS_PREFIX+"RECEIVE_AND_PAYMENT_PUSH_REFUND_";
	
    /**
     * 清结算系统推送代收付结果次数的统计key
     */
    public static final String RECEIVE_AND_PAYMENT_PUSH_COUNT_KEY= ALL_KEYS_PREFIX+"RECEIVE_AND_PAYMENT_PUSH_COUNT"; 
    
    /**
     * 清结算系统推送的代收付数据"机构号_订单号"缓存
     */
    public static final String RECEIVE_AND_PAYMENT_PUSH_ORDERNO_UNIQUE_KEY= ALL_KEYS_PREFIX+"RECEIVE_AND_PAYMENT_PUSH_ORDERNO_UNIQUE"; 

}
