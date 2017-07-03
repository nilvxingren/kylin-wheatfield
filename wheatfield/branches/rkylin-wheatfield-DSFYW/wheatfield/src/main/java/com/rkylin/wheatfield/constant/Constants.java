package com.rkylin.wheatfield.constant;

import java.util.HashMap;
import java.util.Map;

public class Constants {

	/** 日期格式 */
	// yyyy-MM-dd HH:mm:ss
	public static final String DATE_FORMAT_YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
	// yyyy-MM-dd HH:mm:ss
	public static final String DATE_FORMAT_YYYYMMDDHHMMSSSSS = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String DATE_FORMAT_YYYYMMDD = "yyyy-MM-dd";
	public static final String DATE_YYYYMMDD = "yyyyMMdd";
	/** 字符集 */
	// UTF-8
	public static final String CHARSET_UTF8 = "UTF-8";
	// GBK
	public static final String CHARSET_GBK = "GBK";

	/** 数据格式 */
	// XML
	public static final String DATA_PROTOCOL_TYPE_XML = "xml";
	// JSON
	public static final String DATA_PROTOCOL_TYPE_JSON = "json";
	
	/** 签名方式 */
	// MD5
	public static final String SIGN_TYPE_MD5 = "md5";

	/** 系统参数名 */
	// method
	public static final String SYS_PARAM_METHOD = "method";
	// format
	public static final String SYS_PARAM_FORMAT = "format";
	// session
	public static final String SYS_PARAM_SESSION = "session";
	// timestamp
	public static final String SYS_PARAM_TIMESTAMP = "timestamp";
	// app_key
	public static final String SYS_PARAM_APP_KEY = "app_key";
	// sign
	public static final String SYS_PARAM_SIGN = "sign";
	
	// 协议状态_失效
	public static final int AGMT_STATUS_0 = 0;
	// 协议状态_生效
	public static final int AGMT_STATUS_1 = 1;
	
	// 融数ID
	public static final String RS_ID = "M00000X";
	// 丰年ID
	public static final String FN_ID = "M000001";
	// P2PID
	public static final String P2P_ID = "M000002";
	// 会唐ID
	public static final String HT_ID = "M000003";
	// 课栈ID
	public static final String KZ_ID = "M000004";
	// 会唐----会场云
	public static final String HT_CLOUD_ID = "M0000031";
	// 展酷
	public static final String ZK_ID = "M000011";
	//指尖代言
	public static final String ZJDY_ID = "M000012";
	//通信运维
    public static final String TXYW_ID = "M000014";
    /**
     * 帮帮助学
     */
    public static final String BBZX_ID = "M000020";
    
    /**
     * 房仓
     */
    public static final String FC_ID = "M000016";
    /**
     *白条
     */
    public static final String FACTORING_ID = "M000017";
    /**
     *白条 汇总使用协议 
     */
    public static final String IOU_ID = "M000023";
    /**
     *领客科技 
     */
    public static final String LINGKE_ID = "M000024";
    
    /**
     * 银企民生-客栈
     */
    public static final String YQMS_KZ_ID = "YQMS_KZ";
    
    /**
     * 银企民生-悦视觉
     */
    public static final String YQMS_YSJ_ID = "YQMS_YSJ";
	//君融贷企业帐户用户号
	public static final String JRD_USERID = "141223100000013";
	/**
	 * 君融贷机构号ID
	 */
	public static final String JRD_ID="M000005";
	/**
	 * 食全食美机构号ID
	 */
	public static final String SQSM_ID="M000006";
	/**
	 * 棉庄机构号ID
	 */
	public static final String MZ_ID="M000007";

	/**
	 * 云农场构号ID
	 */
	public static final String YNC_ID="M000008";
	
	/**
	 * 指尖微客构号ID
	 */
	public static final String ZJWK_ID="M000009";
	/**
	 * 蚂蚁HR企业帐户ID
	 */
	public static final String MYHR_ID="M000010";
	
	/**
     * 悦视觉机构id
     */
    public static final String YSJ_ID="M000025";
    
    /**
     * 融数钱包机构id
     */
    public static final String RSQB_ID="M666666";
	/**
	 * 融德保理
	 */
	public static final String RDBL_ID="RS_CJZF0001";
	
	/**
	 * FN 主账户
	 */
	public static final String FN_PRODUCT = "P000002";
	/**
	 * P2P授信子账户
	 */
	public static final String USER_SUB_ACCOUNT = "P000003";
	/**
	 * 预付金子账户
	 */
	public static final String ADVANCE_ACCOUNT = "P000004";
	/**
	 * 会唐主账户
	 */
	public static final String HT_PRODUCT="P000005";
	
	/**
	 * 悦视觉主账户
	 */
	public static final String YSJ_PRODUCT="P000218";
	/**
	 * 会唐企业用户ID
	 */
	public static final String HT_USERID="141223100000010";
	/**
	 * 会唐授信子账户
	 */
	public static final String HT_CREDIT_ACCOUNT="P000006";

	/**
	 * 会唐场地方子账户
	 */
	public static  final String HT_CHANGDIFANG_ACCOUNT="P000007";
	/**
	 *课栈主账户
	 */
	public static final String KZ_PRODUCT="P000008";
	/**
	 * 课栈授信子账户
	 */
	public static final String KZ_CREDIT_ACCOUNT="P000009";
	/**
	 * 丰年红包子账户
	 */
	public static final String FN_RED_PACKET="P000010";
	/**
	 * 君融贷产品号
	 */
	public static final String JRD_PRODUCT="P000011";
	/**
	 * 君融贷充值子账户
	 */
	public static final String JRD_RECHARGE_ACCOUNT_PRODUCT="P000012";
	/**
	 * 君融贷融资子账户
	 */
	public static final String JRD_FINANCED_ACCOUNT_PRODUCT="P000013";

	/**
	 * 君融贷活期应付利息子账户
	 */
	public static final String JRD_PAYABLE_INTEREST_ACCOUNT="P000033";
	
	/**
	 * 食全食美主账户
	 */
	public static final String SQSM_PRODUCT="P000014";
	/**
	 * 食全食美收款子账户
	 */
	public static final String SQSM_COLLECTION_PRODUCT="P000015";
	/**
	 * 食全食美授信子账户
	 */
	public static final String SQSM_CREDIT_PRODUCT="P000016";
	/**
	 * 棉庄主账户
	 */
	public static final String MZ_PRODUCT="P000017";
	/**
	 * 棉庄授信子账户
	 */
	public static final String MZ_CREDIT_PRODUCT="P000018";
	/**
	 * 会唐与会唐之间的转账产品号
	 */
	public static final String HT_TRANSFER_PRODUCT_STRING="P000021";
	/**
	 * 会唐保理公司收款账户
	 */
	public static final String HT_GATHERING_PRODUCT="P000040";
	/**
     * MICE平安付款子账户
     */
    public static final String HT_PINGAN_PAY_CHILD="P000085";
    /**
     * MICE平安收款子账户
     */
    public static final String HT_PINGAN_COLL_CHILD="P000086";
    
    /**
     * 帮帮助学主账户
     */
    public static final String BBZX_PRODUCT="P000146";
	/**
	 * 会唐保理公司收款用户
	 */
	public static final String HT_GATHERING_USERID="141223100000036";

	//账号目的:
	/**
	 * 1结算卡
	 */
	public static final String ACCOUNT_PURPOSE_1 = "1";
	/**
	 * 2其他卡
	 */
	public static final String ACCOUNT_PURPOSE_2 = "2";
	/**
	 * 3提现卡
	 */
	public static final String ACCOUNT_PURPOSE_3 = "3";
	/**
	 * 4提现卡,结算卡 同一张
	 */
	public static final String ACCOUNT_PURPOSE_4 = "4";
	/**
	 * 代收付结果正常
	 */
	public static final int GENERATION_PAYMENT_SEND_TYPE_0 = 0;
	/**
	 * 代收付结果失败
	 */
	public static final int GENERATION_PAYMENT_SEND_TYPE_1 = 1;
	/**
	 * 代收付状态失效
	 */
	public static final int GENERATION_PAYMENT_STATUS_0 = 0;
	/**
	 * 代收付状态生效
	 */
	public static final int GENERATION_PAYMENT_STATUS_1 = 1;
	/**
	 * 丰年代收批次号00
	 */
	public static final String FN_FILE_BATCH_00="00";
	/**
	 * 丰年代付批次号01
	 */
	public static final String FN_FILE_BATCH_01="01";
	/**
	 * 会堂代收批次号02
	 */
	public static final String HT_FILE_BATCH_02="02";
	/**
	 * 会堂代付批次号03
	 */
	public static final String HT_FILE_BATCH_03="03";
	/**
	 * 会堂文件类型
	 */
	public static final String HT_FILE_TYPE_6="6";
	
	/**
	 * 银行卡状态失效
	 */
	public static final int ACCOUNT_NUM_STATRS_0=0;
	/**
	 * 银行卡状态生效
	 */
	public static final int ACCOUNT_NUM_STATRS_1=1;
	/**
	 * 对公账户确认时,银行卡待审核,需要一分钱完成
	 */
	public static final int ACCOUNT_NUM_STATRS_2=2;
	/**
	 * 定时任务将银行卡状态设置为审核中
	 */
   public static final int ACCOUNT_NUM_STATRS_3=3;
     /**
      * 订单返回失败
      */
	public static final int ACCOUNT_NUM_STATRS_4=4;
	/**
     *查询状态为1，2，3，4的数据用5
     */
	public static final int ACCOUNT_NUM_STATRS_OK_ALL=5;
	
	/**
	 * 预付金余额查询-以商户Id为主 查询相关联的台长余额信息
	 */
	public static final String QUERY_BALANCE_TYPE_1="1";
	/**
	 * 预付金余额查询-以台长Id为主 查询相关联的商户预付金余额信息
	 */
	public static final String QUERY_BALANCE_TYPE_2="2";
	/**
	 * 预付金余额查询-商户类型
	 */
	public static final int ADVANCE_BALANCE_TYPE_0=0;
	/**
	 * 预付金余额查询-台长类型
	 */
	public static final int ADVANCE_BALANCE_TYPE_1=1;
	/**
	 * 预付金余额查询-当日金额类型
	 */
	public static final int ADVANCE_BALANCE_TYPE_2=2;
	/**
	 * FinanaceCompany basetype 
	 */
	public static final int BASE_FINANACECOMPANY_TYPE = 1;
	/**
	 * FinanacePerson baseType
	 */
	public static  final  int BASE_FINANACEPERSON_TYPE = 1;
	/**
	 * 卡属性为对公
	 */
	public static  final String ACCOUNT_PROPERTY_PUBLIC = "1";
	/**
	 * 卡属性为对私
	 */
	public static  final String ACCOUNT_PROPERTY_PRIVATE = "2";
	/**
	 * 对公一分钱，调用代付接口，备注字段
	 */
	public static  final String TRANS_ORDER_REMARK = "qjs_tuikuan";
	/**
	 * 君融贷业务类型-活期产品申购
	 * */
	public static final String JRD_BUSITYPE_PURCHASE = "6";
	/**
	 * 君融贷-活期产品本金子账户
	 */
	public static final String JRD_PURCHASE_INTOPRODUCTID = "P000024";
	
	/**
	 * 君融贷 活期存款子账户
	 */
	public static final String JRD_CURRENT_ACCOUNT = "P000030";
	/**
	 * 君融贷 可用活期存款子账户
	 */
	public static final String JRD_QUOTA_ACCOUNT = "P000031";
	/**
	 * 君融贷 已用活期存款子账户
	 */
	public static final String JRD_USED_ACCOUNT = "P000032";
	/**
	 * 君融贷活期收益子账户
	 */
	public static final String JRD_INTEREST_ACCOUNT = "P000025";
	
	/**
	 * 君融贷 活期产品 用户子账户 P000024: 活期本金  P000025: 活期利息 P000026: 昨日活期收益
	 */
	public static final String[] subJrdAccount = {"P000024","P000025","P000026"};
	/**
	 * 去鹏元校验参数表标志
	 */
	public static final String PENGYUAN_VERIFY_INST = "PENGYUAN_VERIFY_INST";
	
	/**
	 * 订单业务类型字段:一份验证,卡信息表
	 */
	public static final String BIZ_TYPE_PENNY = "YFYZ";
	
	 /**
     * 订单业务类型字段:对公基础卡信息表
     */
	public static final String BIZ_TYPE_1 = "1";
}
