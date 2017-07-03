package com.rkylin.wheatfield.api;

import com.rkylin.wheatfield.model.DictionaryResponse;
import com.rkylin.wheatfield.pojo.DictionaryQuery;

/**
 * 基础数据接口
 * @author Achilles
 *
 */
public interface BaseInforDubboService {
	
	/**
	 * 字典信息查询
	 * @param query 
	 * @return
	 */
	public DictionaryResponse getDicInfor(DictionaryQuery query);
	
}
