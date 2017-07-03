/**
 * @Creator : liuhuan
 * @CreateTime : 2015年12月29日 下午11:04:27
 * @Version : 1.0
 */
package com.rkylin.wheatfield.api;

import java.util.List;
import java.util.Map;

import com.rkylin.wheatfield.pojo.TransOrderInfo;

public interface BillsBatchUploadServiceApi {
	/**
	 * @Description : TODO(批量上传支付凭证记账接口)
	 * @CreateTime : 2015年12月29日 下午11:05:18
	 */
	List<Map<String,String>> billsBatchUpload(List<TransOrderInfo> transOrderInfos);
}
