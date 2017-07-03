/**
 * @File name : BalanceInfoDaoImpl.java
 * @Package : com.rkylin.wheatfield.dao.impl
 * @Description : TODO(用一句话描述该文件做什么)
 * @Creator : liuhuan
 * @CreateTime : 2015年11月10日 下午4:30:45
 * @Version : 1.0
 */
package com.rkylin.wheatfield.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.rkylin.database.BaseDao;
import com.rkylin.wheatfield.dao.BalanceInfoDao;
import com.rkylin.wheatfield.pojo.SHBalanceInfo;

@Repository("balanceInfoDao")
public class BalanceInfoDaoImpl extends BaseDao implements BalanceInfoDao {

	@Override
	public List<SHBalanceInfo> getBalanceInfoList(Map<String, String> params) {
		return super.getSqlSession().selectList("MyBatisMap.getBalanceList", params);
	}

}
