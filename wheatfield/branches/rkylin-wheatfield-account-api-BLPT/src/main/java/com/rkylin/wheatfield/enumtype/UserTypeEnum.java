package com.rkylin.wheatfield.enumtype;

/**
 * 用户种类
 * @author zhenpc
 *
 */
public enum UserTypeEnum {
	/**
	 * 普通用户
	 */
	USER("100"),
	/**
	 * 商户账户
	 */
	MERCHANT("MERCHANT"),
	/**
	 * 内部账户
	 */
	INNER("INNER");
	
	private UserTypeEnum(String category){
		this.category=category;
	}
	
	private String category;
	
	public static UserTypeEnum toEnum(String category) {
		for (UserTypeEnum item : UserTypeEnum.values()) {
			if (item.getCategory().equals(category)) {
				return item;
			}
		}
		return null;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
}
