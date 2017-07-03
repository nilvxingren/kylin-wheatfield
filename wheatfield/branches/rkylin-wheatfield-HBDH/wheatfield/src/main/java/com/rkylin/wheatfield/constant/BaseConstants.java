package com.rkylin.wheatfield.constant;

public class BaseConstants {
	
	/**
	 * 币种-人民币
	 * */
	public static final String CURRENCY_CNY="CNY";
	
	/**
	 * 初始化账户余额
	 * */
	public static final String INITIAL_AMOUNT="0";
	
	/**
	 * 业务状态编码 0表示无权限，1表示有权限，几个状态为分别是"充值-转账-扣款-提现-冻结-冲正-存储消费-信用消费"
	 * */
	public static final String BUSS_CONTRAL_TOTAL="11111111";
	
	/**
	 * 账户状态：未开通
	 * */
	public static final String ACCOUNT_STATUS_UNOPEN = "0";
	
	/**
	 * 账户状态：正常
	 * */
	public static final String  ACCOUNT_STATUS_OK = "1";
	
	/**
	 * 账户状态：冻结
	 * */
	public static final String ACCOUNT_STATUS_FREEZE = "2";
	
	/**
	 * 账户状态：失效
	 * */
	public static final String ACCOUNT_STATUS_OFF = "3";
	
	/*
	 * 贷记记账方式
	 * */
	public static final int CREDIT_TYPE = 1;
	
	/*
	 * 借记记账方式
	 * */
	public static final int DEBIT_TYPE = 0;
	
	/*
	 * 双向记账方式
	 * */
	public static final int DOUBLE_ENTRY_TYPE = 2;
	
	/*
	 * 冲正交易类型
	 * */
	public static final int REVERSE_TYPE = 1;
	
	/*
	 * 非冲正交易
	 * */
	public static final int NOREVERSE_TYPE = 0;
	
	/*
	 * 可用余额类型
	 * */
	public static final int TYPE_BALANCE_USABLE = 1;
	
	/*
	 * 清算余额（提现余额）类型
	 * */
	public static final int TYPE_BALANCE_SETTLE = 2;
	
	/*
	 * 冻结余额类型
	 * */
	public static final int TYPE_BALANCE_FROZON = 3;
	
	/*
	 * 透支额度
	 * */
	public static final int TYPE_BALANCE_OVERLIMIT = 4;
	
	/*
	 * 贷记余额类型
	 * */
	public static final int TYPE_BALANCE_CREDIT = 5;
	
	/*
	 * 预留余额1类型
	 * */
	public static final int TYPE_BALANCE_BONUS = 6;
	
	/*
	 * 预留余额2类型
	 * */
	public static final int TYPE_BALANCE_POINTS = 7;
	
	/*
	 * 预留余额3类型
	 * */
	public static final int TYPE_BALANCE_RESERVE1 = 8;
	
	/*
	 * 订单类型
	 * */
	public static final int REFER_TRADE = 2;
	
	/*
	 * 账户流水类型
	 * */
	public static final int REFER_FLOW = 3;
	/*
	 * 套录号类型
	 * */
	public static final int REFER_ENTRY = 1;
	/**
	 * 商户冲正类型
	 */
	public static final int REFER_REVERSAL=4;
	
	
	/**
	 * 银行卡类型-清算卡，
	 * */
	public static final String BANKCARD_TYPE_SETTLE = "1001";
	/**
	 * 6.通联代收申请资料
	 */
	public static final int FILE_TYPE_6=6;
	/**
	 * 7.通联代付申请资料
	 */
	public static final int FILE_TYPE_7=7;
	/**
	 * 6.通联代收结果文件
	 */
	public static final int FILE_TYPE_8=8;
	/**
	 * 7.通联代付结果文件
	 */
	public static final int FILE_TYPE_9=9;
	/**
	 * 密码状态正常
	 */
	public static final String PWD_STATUS_1="1";
	/**
	 * 密码状态锁定
	 */
	public static final String PWD_STATUS_0="0";
}
