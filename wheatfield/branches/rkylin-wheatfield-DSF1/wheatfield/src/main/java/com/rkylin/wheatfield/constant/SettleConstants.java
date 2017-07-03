package com.rkylin.wheatfield.constant;

public class SettleConstants {

	// 账期
	public static final String ACCOUNTDATE = "AccountTerm";
	// 账单日
	public static final String BILLDAY = "BillDay";
	// 还款日
	public static final String REPAYMENTDAY = "RepaymentDay";
	// 日终标记
	public static final String DAYEND = "DayEnd";
	/**
	 * 上传到代收付系统一批订单上限
	 */
	public static final String BATCH_LIMIT = "Batch_Limit";
	// 授信商户---丰年
	public static final String CON_FN = "FN";
	// 授信机构---君融贷
	public static final String CON_JRD = "JRD";
	
	// 授信种类-额度
	public static final String LIMIT_CN = "额度";
	public static final String LIMIT = "1";
	public static final int CREDIT_TYPE_ID_LIMIT = 101;

	// 授信种类-单笔
	public static final String SINGLE_CN = "单笔";
	public static final String SINGLE = "";
	public static final int CREDIT_TYPE_ID_SINGLE = 102;

	// 审核结果-通过
	public static final String PASS = "OK";

	// 审核结果-失败
	public static final String FAIL = "NG";

	// 审核结果-需再审
	public static final String RETRIAL = "需再审";

	// 对账每日时间
	public static final String DEDT_ACCOUNT_DATE = "{YMD} 23:30:00";
	

	// 结算种类-债权包
	public static final int SETTLE_TYPE_1 = 1;
	// 结算种类-提现
	public static final int SETTLE_TYPE_2 = 2;
	// 结算种类-还款
	public static final int SETTLE_TYPE_3 = 3;
	// 结算种类-预付金
	public static final int SETTLE_TYPE_4 = 4;
	// 结算种类-分润
	public static final int SETTLE_TYPE_5 = 5;
	// 清算状态-完成
	public static final int SETTLE_STU_1 = 1;
	// 清算状态-失败
	public static final int SETTLE_STU_0 = 0;
	// 还款逾期
	public static final int SETTLE_STU_2 = 2;
	
	// 对账状态-平账
	public static final int COLLATE_STU_1 = 1;
	// 对账状态-错帐
	public static final int COLLATE_STU_0 = 0;
	// 对账状态-长款
	public static final int COLLATE_STU_2 = 2;
	// 对账状态-短款
	public static final int COLLATE_STU_3 = 3;
	
	
	// 对账文件生成路径
	public static final String FILE_PATH = "/duizhang/download/";
	public static final String FILE_UP_PATH = "/duizhang/upload/";
//	public static final String FILE_PATH = "c:\\test\\download\\";
//	public static final String FILE_UP_PATH = "c:\\test\\upload\\";
	public static final String FILE_XML = "xml";
	public static final String FILE_CSV = "csv";
	public static final String FILE_XLS = "xls";
	public static final String FILE_XLSX = "xlsx";
	
	// 债权包取得时间
	public static final String DEDT_DATE = "{YMD} 23:30:00";
	public static final String DEDT_SPLIT = "|";
	public static final String DEDT_SPLIT2 = ",";
	public static final String DEDT_SPLIT3 = " ";
	
	
	//清算订单类型
	/**
	 * 债权包
	 */
	public static final int ORDER_BOND_PACKAGE= 1;
	/**
	 * 提现
	 */
	public static final int ORDER_WITHDRAW=2;
	/**
	 * 信用还款
	 */
	public static final int ORDER_REPAYMENT=3;
	/**
	 * 预付金
	 */
	public static final int ORDER_ADVANCE=4;
	/**
	 * 代收
	 */
	public static final int ORDER_COLLECTION=5;
	/**
	 * 代付
	 */
	public static final int ORDER_WITHHOLD=6;
	/**
	 * 贷款还款
	 */
	public static final int ORDER_LOAN_REPAYMENT=7;	
	/*发送类型*/
	/**
	 * 正常
	 */
	public static final int SEND_NORMAL=0;
	/**
	 * 代扣失败
	 */
	public static final int SEND_DEFEAT =1;
	/**
	 * 代扣延迟
	 */
	public static final int SEND_DELAY=2;
	/*状态*/
	/**
	 * 失效
	 */
	public static final int INVALIDATION =0;
	/** 
	 * 生效
	 */
	public static final int TAKE_EFFECT =1;
	/**
	 * 通联-实时/批量代收业务代码
	 */
	public static final String COLLECTION_CODE_OLD ="19900";
	public static final String COLLECTION_CODE ="09100";
	/**
	 * 通联-实时代付业务代码
	 */
	public static final String WITHHOLD_CODE_OLD ="09900";
	public static final String WITHHOLD_CODE ="11101";
	/**
	 * 通联-批量代付业务代码
	 */
	public static final String WITHHOLD_BATCH_CODE_OLD ="09500";
	public static final String WITHHOLD_BATCH_CODE ="11101";
	/**
	 * ROP-代扣/代收明细文件批次号
	 */	
	public static final String ROP_COLLECTION_BATCH_CODE ="10000001";
	/**
	 * ROP-还款明细文件批次号
	 */	
	public static final String ROP_REPAYMENT_BATCH_CODE ="10000002";	
	/**
	 * ROP-代收文件批次号
	 */	
	public static final String ROP_RECEIVE_BATCH_CODE ="10000003";
	/**
	 * ROP-代付文件批次号
	 */	
	public static final String ROP_PAYMENT_BATCH_CODE ="10000004";
	
	
	/**
	 * 发送到代收付系统数据一批上限值
	 */	
	public static final int BATCH_LIMIT_REC_PAY = 200;
}
