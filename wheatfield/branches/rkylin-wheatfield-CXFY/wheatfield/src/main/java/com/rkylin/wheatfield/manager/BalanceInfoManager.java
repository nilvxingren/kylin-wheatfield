/**
 * @File name : BalanceInfoManager.java
 * @Package : com.rkylin.wheatfield.manager
 * @Description : TODO(用一句话描述该文件做什么)
 * @Creator : Administrator
 * @CreateTime : 2015年11月10日 下午4:31:32
 * @Version : 1.0
 * @Update records:
 *      1.1 2015年11月10日 by Administrator: 
 *      1.0 2015年11月10日 by Administrator: Created 
 * All rights served : FENGNIAN Corporation
 */
package com.rkylin.wheatfield.manager;

import java.util.List;
import java.util.Map;

import com.rkylin.wheatfield.pojo.SHBalanceInfo;

public interface BalanceInfoManager {
	List<SHBalanceInfo> getBalanceInfoList(Map<String, String> params);
}
