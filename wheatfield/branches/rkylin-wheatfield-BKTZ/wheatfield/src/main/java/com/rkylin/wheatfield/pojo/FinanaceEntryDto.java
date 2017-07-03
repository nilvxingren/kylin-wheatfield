/**
*
*/
package com.rkylin.wheatfield.pojo;

import java.io.Serializable;
import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Description:	   
 * Date:          2015年12月1日 下午7:11:12 
 * @author        sun guoxing
 * @version       1.0 
 */
@XStreamAlias("finanaceentry")
public class FinanaceEntryDto  implements Serializable{

	private Date accountdate;
	private String finaccountid;
	private Long amount;
	private Integer direction;
	private Long balanceusable;
	private Long balancesettle;
	private Long balancegrozon;
	private Long balanceoverLimit;
	private Long balancecredit;
	private Integer reverseflag;
	private String referid;
	private Long paymentamount;
	private Date transdate;
	private String remark;
	private Date createdtime;
	private Date updatedtime;
	private String funcode;
	private String busitypeid;
	private String rootinstcd;
	private String userid;
	private String productid;
	/**
	 * @return the accountdate
	 */
	public Date getAccountdate() {
		return accountdate;
	}
	/**
	 * @param accountdate the accountdate to set
	 */
	public void setAccountdate(Date accountdate) {
		this.accountdate = accountdate;
	}
	/**
	 * @return the finaccountid
	 */
	public String getFinaccountid() {
		return finaccountid;
	}
	/**
	 * @param finaccountid the finaccountid to set
	 */
	public void setFinaccountid(String finaccountid) {
		this.finaccountid = finaccountid;
	}
	/**
	 * @return the amount
	 */
	public Long getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(Long amount) {
		this.amount = amount;
	}
	/**
	 * @return the direction
	 */
	public Integer getDirection() {
		return direction;
	}
	/**
	 * @param direction the direction to set
	 */
	public void setDirection(Integer direction) {
		this.direction = direction;
	}
	/**
	 * @return the balanceusable
	 */
	public Long getBalanceusable() {
		return balanceusable;
	}
	/**
	 * @param balanceusable the balanceusable to set
	 */
	public void setBalanceusable(Long balanceusable) {
		this.balanceusable = balanceusable;
	}
	/**
	 * @return the balancesettle
	 */
	public Long getBalancesettle() {
		return balancesettle;
	}
	/**
	 * @param balancesettle the balancesettle to set
	 */
	public void setBalancesettle(Long balancesettle) {
		this.balancesettle = balancesettle;
	}
	/**
	 * @return the balancegrozon
	 */
	public Long getBalancegrozon() {
		return balancegrozon;
	}
	/**
	 * @param balancegrozon the balancegrozon to set
	 */
	public void setBalancegrozon(Long balancegrozon) {
		this.balancegrozon = balancegrozon;
	}
	/**
	 * @return the balanceoverLimit
	 */
	public Long getBalanceoverLimit() {
		return balanceoverLimit;
	}
	/**
	 * @param balanceoverLimit the balanceoverLimit to set
	 */
	public void setBalanceoverLimit(Long balanceoverLimit) {
		this.balanceoverLimit = balanceoverLimit;
	}
	/**
	 * @return the balancecredit
	 */
	public Long getBalancecredit() {
		return balancecredit;
	}
	/**
	 * @param balancecredit the balancecredit to set
	 */
	public void setBalancecredit(Long balancecredit) {
		this.balancecredit = balancecredit;
	}
	/**
	 * @return the reverseflag
	 */
	public Integer getReverseflag() {
		return reverseflag;
	}
	/**
	 * @param reverseflag the reverseflag to set
	 */
	public void setReverseflag(Integer reverseflag) {
		this.reverseflag = reverseflag;
	}
	/**
	 * @return the referid
	 */
	public String getReferid() {
		return referid;
	}
	/**
	 * @param referid the referid to set
	 */
	public void setReferid(String referid) {
		this.referid = referid;
	}
	/**
	 * @return the paymentamount
	 */
	public Long getPaymentamount() {
		return paymentamount;
	}
	/**
	 * @param paymentamount the paymentamount to set
	 */
	public void setPaymentamount(Long paymentamount) {
		this.paymentamount = paymentamount;
	}
	/**
	 * @return the transdate
	 */
	public Date getTransdate() {
		return transdate;
	}
	/**
	 * @param transdate the transdate to set
	 */
	public void setTransdate(Date transdate) {
		this.transdate = transdate;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return the createdtime
	 */
	public Date getCreatedtime() {
		return createdtime;
	}
	/**
	 * @param createdtime the createdtime to set
	 */
	public void setCreatedtime(Date createdtime) {
		this.createdtime = createdtime;
	}
	/**
	 * @return the updatedtime
	 */
	public Date getUpdatedtime() {
		return updatedtime;
	}
	/**
	 * @param updatedtime the updatedtime to set
	 */
	public void setUpdatedtime(Date updatedtime) {
		this.updatedtime = updatedtime;
	}
	/**
	 * @return the funcode
	 */
	public String getFuncode() {
		return funcode;
	}
	/**
	 * @param funcode the funcode to set
	 */
	public void setFuncode(String funcode) {
		this.funcode = funcode;
	}
	/**
	 * @return the busitypeid
	 */
	public String getBusitypeid() {
		return busitypeid;
	}
	/**
	 * @param busitypeid the busitypeid to set
	 */
	public void setBusitypeid(String busitypeid) {
		this.busitypeid = busitypeid;
	}
	/**
	 * @return the rootinstcd
	 */
	public String getRootinstcd() {
		return rootinstcd;
	}
	/**
	 * @param rootinstcd the rootinstcd to set
	 */
	public void setRootinstcd(String rootinstcd) {
		this.rootinstcd = rootinstcd;
	}
	/**
	 * @return the userid
	 */
	public String getUserid() {
		return userid;
	}
	/**
	 * @param userid the userid to set
	 */
	public void setUserid(String userid) {
		this.userid = userid;
	}
	/**
	 * @return the productid
	 */
	public String getProductid() {
		return productid;
	}
	/**
	 * @param productid the productid to set
	 */
	public void setProductid(String productid) {
		this.productid = productid;
	}
}
