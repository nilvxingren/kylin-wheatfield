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
}
