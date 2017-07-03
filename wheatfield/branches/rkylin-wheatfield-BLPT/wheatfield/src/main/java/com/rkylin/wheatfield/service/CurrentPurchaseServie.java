package com.rkylin.wheatfield.service;

import com.rkylin.wheatfield.exception.AccountException;
import com.rkylin.wheatfield.pojo.OrderAuxiliary;
import com.rkylin.wheatfield.pojo.TransOrderInfo;
import com.rkylin.wheatfield.pojo.User;

/**
 * 活期申购
 * @author mawanxia
 * @date 2015-10-26
 *
 */
public interface CurrentPurchaseServie {
	
	/**
	 * 创建子账户（君融贷）
	 * @param user
	 * @return 
	 */
	public void createSubAccount (User user) throws AccountException;
     
	/**
	 * 申购  转入君融贷用户活期本金子账户
	 * @param user
	 * @param transOrderInfo
	 * @return
	 */
	public void transfer(User user , TransOrderInfo transOrderInfo , OrderAuxiliary orderAuxiliary) ;
}
