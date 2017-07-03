/**
 * @File name : CorporatAccountInfoService.java
 * @Package : com.rkylin.wheatfield.service
 * @Description : TODO(对公账户处理Service接口)
 * @Creator : liuhuan
 * @CreateTime : 2015年9月8日 上午10:49:46
 * @Version : 1.0
 * @Update records:
 *      1.1 2015年9月8日 by liuhuan: 
 *      1.0 2015年9月8日 by liuhuan: Created 
 */
package com.rkylin.wheatfield.service;

import java.util.List;
import java.util.Map;

import com.rkylin.wheatfield.pojo.AccountInfo;
import com.rkylin.wheatfield.pojo.CorporatAccountInfo;
import com.rkylin.wheatfield.response.ErrorResponse;

public interface CorporatAccountInfoService {
	 /**
	  * @CreateTime : 2015年9月7日 下午6:16:50
	  * @Creator : liuhuan 
	  * @Description : 批量导入对公账户信息
	  */
	 public ErrorResponse insertCorporatAccountInfo(Map<String, String[]> paramMap);
	 /**
	  * @CreateTime : 2015年9月8日 下午3:04:50
	  * @Creator : liuhuan 
	  * @Description : 查询对公账户信息
	  */
	 public List<CorporatAccountInfo> selCorporatAccountInfo(Map<String, String[]> paramMap);
	 /**
	  * @CreateTime : 2015年9月8日 下午4:59:50
	  * @Creator : liuhuan 
	  * @Description : 修改对公账户信息
	  */
	 public ErrorResponse updateCorporatAccountInfo(Map<String, String[]> paramMap);
	 /**
	  * @CreateTime : 2015年9月9日 上午9:31:50
	  * @Creator : liuhuan 
	  * @Description : 绑卡信息对公账户信息录入核对
	  */
	 public String accountInfoCheck(AccountInfo accountInfo,String constId);
}
