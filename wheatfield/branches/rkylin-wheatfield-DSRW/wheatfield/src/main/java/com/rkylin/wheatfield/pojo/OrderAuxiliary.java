package com.rkylin.wheatfield.pojo;

import java.io.Serializable;

/**
 * 订单辅助类   作用 
 * 当交易订单实体类接收参数不能满足条件的情况下，对此类进行操作
 * @author zhilei
 *
 */
public class OrderAuxiliary implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * A账户产品号/管理分组
	 */
	private String productQAA;
	/**
	 * B账户产品号/管理分组
	 */
	private String productQAB;
	/**
	 * B账户的机构号
	 */
	private String merchantQA;
	/**
	 * 冻结资金授权码
	 */
	private String authCode;
	/**
	 * 授权金额
	 */
	private long authAmount;
	/**
	 * 君融贷活期理财赎回本金金额
	 */
	private Long capitalAmount;

	/**
	 * 君融贷活期理财赎回利息金额
	 */
	private Long interestamount;
	
	/**
	 * @return the capitalAmount
	 */
	public Long getCapitalAmount() {
		return capitalAmount;
	}
	/**
	 * @param capitalAmount the capitalAmount to set
	 */
	public void setCapitalAmount(Long capitalAmount) {
		this.capitalAmount = capitalAmount;
	}
	/**
	 * @return the interestamount
	 */
	public Long getInterestamount() {
		return interestamount;
	}
	/**
	 * @param interestamount the interestamount to set
	 */
	public void setInterestamount(Long interestamount) {
		this.interestamount = interestamount;
	}
	/**
	 * 授权金额
	 */
	public long getAuthAmount() {
		return authAmount;
	}
	/**
	 * 授权金额
	 */
	public void setAuthAmount(long authAmount) {
		this.authAmount = authAmount;
	}
	/**
	 * 冻结资金授权码
	 */
	public String getAuthCode() {
		return authCode;
	}
	/**
	 * 冻结资金授权码
	 */
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	/**
	 * A账户产品号/管理分组
	 */
	public String getProductQAA() {
		return productQAA;
	}
	/**
	 * A账户产品号/管理分组
	 */
	public void setProductQAA(String productQAA) {
		this.productQAA = productQAA;
	}
	/**
	 * B账户产品号/管理分组
	 */
	public String getProductQAB() {
		return productQAB;
	}
	/**
	 * B账户产品号/管理分组
	 */
	public void setProductQAB(String productQAB) {
		this.productQAB = productQAB;
	}
	/**
	 * B账户的机构号
	 */
	public String getMerchantQA() {
		return merchantQA;
	}
	/**
	 * B账户的机构号
	 */
	public void setMerchantQA(String merchantQA) {
		this.merchantQA = merchantQA;
	}

}
