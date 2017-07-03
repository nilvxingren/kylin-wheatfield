/**
 * @File name : BalanceInfoManagerImpl.java
 * @Package : com.rkylin.wheatfield.manager.impl
 * @Description : TODO(用一句话描述该文件做什么)
 * @Creator : Administrator
 * @CreateTime : 2015年11月10日 下午4:32:10
 * @Version : 1.0
 * @Update records:
 *      1.1 2015年11月10日 by Administrator: 
 *      1.0 2015年11月10日 by Administrator: Created 
 * All rights served : FENGNIAN Corporation
 */
package com.rkylin.wheatfield.manager.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.rkylin.wheatfield.dao.BalanceInfoDao;
import com.rkylin.wheatfield.manager.BalanceInfoManager;
import com.rkylin.wheatfield.pojo.SHBalanceInfo;

@Component("balanceInfoManager")
public class BalanceInfoManagerImpl implements BalanceInfoManager {
	@Autowired
	@Qualifier("balanceInfoDao")
	private BalanceInfoDao balanceInfoDao;

	@Override
	public List<SHBalanceInfo> getBalanceInfoList(Map<String, String> params) {
		return balanceInfoDao.getBalanceInfoList(params);
	}

}
