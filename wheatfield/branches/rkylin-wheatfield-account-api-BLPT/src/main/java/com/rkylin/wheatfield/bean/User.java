package com.rkylin.wheatfield.bean;

import java.io.Serializable;

public class User  implements Serializable{

	/**
	 * 用户ID
	 * */
	private String userId;
	/**
	 * 机构码/商户号
	 * */
	private String instCode;
	/**
	 * 产品号
	 * */
	private String productId;
	/**
	 * 名称
	 */
	public String name;
	/**
	 * 类型
	 */
	public String[] type;
	/**
	 * 操作类型
	 */
	public String operType;
	/**
	 * 卡号
	 */
	public String cardNo;
	   /**
     * 密码
     */
    public String password;
    /**
     * 角色
     */
    public String roleCode;
    
    private Integer last;
    
    private String finAccountId;
    
    private Long amount;
    
    private Integer status;
    
	public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public Long getAmount() {
        return amount;
    }
    public void setAmount(Long amount) {
        this.amount = amount;
    }
    public String getFinAccountId() {
        return finAccountId;
    }
    public void setFinAccountId(String finAccountId) {
        this.finAccountId = finAccountId;
    }
    public Integer getLast() {
        return last;
    }
    public void setLast(Integer last) {
        this.last = last;
    }
    public String getRoleCode() {
        return roleCode;
    }
    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }
    public String getOperType() {
		return operType;
	}
	public void setOperType(String operType) {
		this.operType = operType;
	}
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
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String[] getType() {
		return type;
	}
	public void setType(String[] type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	
}
