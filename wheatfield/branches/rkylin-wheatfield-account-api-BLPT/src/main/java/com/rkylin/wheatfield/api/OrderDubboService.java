package com.rkylin.wheatfield.api;

import java.util.List;
import java.util.Map;

import com.rkylin.wheatfield.model.CommonResponse;

/**
 * 与订单相关的
 * @author Achilles
 *
 */
public interface OrderDubboService {

	/**
	 * 根据账户系统的订单状态更新订单系统
	 * @param orderNoToInstCodeMaplist  orderNo:订单号   instCode:机构号
	 * @return
	 */
	public CommonResponse updateOrderSysStatusByAccountSys(List<Map<String,String>> orderNoToInstCodeMaplist);
	
}
