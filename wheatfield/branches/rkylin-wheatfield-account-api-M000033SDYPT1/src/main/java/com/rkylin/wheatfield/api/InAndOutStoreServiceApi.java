package com.rkylin.wheatfield.api;

import java.util.Map;

import com.rkylin.wheatfield.pojo.TransOrderInfo;

/**
 * 卖家云（瑞金麟入库、出库、退货相关service）
 * @author sun
 *
 */
public interface InAndOutStoreServiceApi {

	//入库出库退货总的入口方法
	public Map<String,String> inAndOutStore(TransOrderInfo transOrder );
}
