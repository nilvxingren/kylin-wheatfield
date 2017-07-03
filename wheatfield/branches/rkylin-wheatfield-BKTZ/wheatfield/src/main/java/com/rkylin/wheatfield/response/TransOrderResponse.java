package com.rkylin.wheatfield.response;

import java.util.List;

import com.rkylin.wheatfield.pojo.TransOrder;
import com.rkylin.wheatfield.pojo.TransOrderInfoNew;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author  likun
 * @version 创建时间：2015-4-23 下午4:42:54
 * 类说明              处理订单信息
 */
public class TransOrderResponse extends Response {

	@XStreamAlias("transorders")
	private List<TransOrder> transorders;
	@XStreamAlias("transorderinfos")
	private List<TransOrderInfoNew> transOrderInfoNews;

	public List<TransOrderInfoNew> getTransOrderInfoNews() {
		return transOrderInfoNews;
	}

	public void setTransOrderInfoNews(List<TransOrderInfoNew> transOrderInfoNews) {
		this.transOrderInfoNews = transOrderInfoNews;
	}

	public List<TransOrder> getTransorder() {
		return transorders;
	}

	public void setTransorder(List<TransOrder> transorder) {
		this.transorders = transorder;
	}


}

