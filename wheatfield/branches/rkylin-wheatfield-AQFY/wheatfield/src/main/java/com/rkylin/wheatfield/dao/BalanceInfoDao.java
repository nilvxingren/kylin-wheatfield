/**
 * @File name : BalanceInfoDao.java
 * @Package : com.rkylin.wheatfield.dao.impl
 * @Description : TODO(用一句话描述该文件做什么)
 * @Creator : liuhuan
 * @CreateTime : 2015年11月10日 下午4:29:57
 * @Version : 1.0
 */
package com.rkylin.wheatfield.dao;

import java.util.List;
import java.util.Map;

import com.rkylin.wheatfield.pojo.SHBalanceInfo;

public interface BalanceInfoDao {
	List<SHBalanceInfo> getBalanceInfoList(Map<String, String> params);
}
