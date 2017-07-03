package com.rkylin.wheatfield.bean;

import java.io.Serializable;

/**
 * 银行卡信息查询实体封装
 * @author Achilles
 *
 */
public class CardInfoQuery  implements Serializable{

	/**
	 * 用户ID
	 * */
	private String userId;
	/**
	 * 机构号/商户号
	 * */
	private String instCode;
	/**
	 * 卡目的
	 */
	private Integer[] cardPurpose;
	/**
	 * 卡状态
	 */
	private Integer[] status;
	/**
	 * 是否是最后一条数据,1:是
	 */
	private Integer last;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getInstCode() {
		return instCode;
	}
	public void setInstCode(String instCode) {
		this.instCode = instCode;
	}
	public Integer[] getCardPurpose() {
		return cardPurpose;
	}
	public void setCardPurpose(Integer[] cardPurpose) {
		this.cardPurpose = cardPurpose;
	}
	public Integer[] getStatus() {
		return status;
	}
	public void setStatus(Integer[] status) {
		this.status = status;
	}
	public Integer getLast() {
		return last;
	}
	public void setLast(Integer last) {
		this.last = last;
	}
	
	
}
