package com.rkylin.wheatfield.service;

import com.rkylin.wheatfield.model.DictionaryResponse;
import com.rkylin.wheatfield.pojo.DictionaryQuery;

/**
 * 基础信息相关
 * @author Achilles
 *
 */
public interface BaseInforService {
	/**
	 * 查询字典信息
	 */
	public DictionaryResponse getDicInfor(DictionaryQuery query);
}
