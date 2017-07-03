/**
 * @File name : CreditApprovalInfoService.java
 * @Package : com.rkylin.wheatfield.service
 * @Description : TODO(授信信息表Service)
 * @Creator : liuhuan
 * @CreateTime : 2015年9月18日 上午10:21:04
 * @Version : 1.0
 */
package com.rkylin.wheatfield.service;

import java.util.List;

import com.rkylin.wheatfield.pojo.CreditApprovalInfo;
import com.rkylin.wheatfield.pojo.User;

public interface CreditApprovalInfoService {
	List<CreditApprovalInfo> selectcreditInfo(User user,String provideruserid);
}
