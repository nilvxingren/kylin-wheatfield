/**
 * @File name : CreditApprovalInfoServiceImpl.java
 * @Package : com.rkylin.wheatfield.service.impl
 * @Description : TODO(授信信息表Service实现类)
 * @Creator : liuhuan
 * @CreateTime : 2015年9月18日 上午10:21:45
 * @Version : 1.0
 */
package com.rkylin.wheatfield.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkylin.wheatfield.manager.CreditApprovalInfoManager;
import com.rkylin.wheatfield.pojo.CreditApprovalInfo;
import com.rkylin.wheatfield.pojo.CreditApprovalInfoQuery;
import com.rkylin.wheatfield.pojo.User;
import com.rkylin.wheatfield.service.CreditApprovalInfoService;

@Service("creditApprovalInfoService")
public class CreditApprovalInfoServiceImpl implements CreditApprovalInfoService {
	private static Logger logger = LoggerFactory
			.getLogger(CreditApprovalInfoServiceImpl.class);
	
	@Autowired
	private CreditApprovalInfoManager creditApprovalInfoManager;

	@Override
	public List<CreditApprovalInfo> selectcreditInfo(User user, String provideruserid) {
		logger.info("------授信信息查询开始------");
		logger.info("--用户ID--:"+user.userId+"--机构号--："+user.constId+"--产品号--："+user.productId+"--用户名--"+user.userName+"第三方用户ID"+provideruserid);
		List<CreditApprovalInfo> creditInfoList = new ArrayList<CreditApprovalInfo>();
		try {
			CreditApprovalInfoQuery query = new CreditApprovalInfoQuery();
			query.setRootInstCd(user.constId);
			query.setUserId(user.userId);
			if(provideruserid != null && !"".equals(provideruserid)){
				query.setProviderId(provideruserid);	
			}
			query.setProductId(user.productId);
			query.setEndTime(null);
			creditInfoList = creditApprovalInfoManager.queryList(query);
		} catch (Exception e) {
			logger.info("------授信信息查询异常------");
			logger.error(e.getMessage());
		}
		logger.info("------授信信息查询结束------");
		return creditInfoList;
	}
}
