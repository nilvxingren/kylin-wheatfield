package com.rkylin.wheatfield.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.rkylin.wheatfield.common.DateUtils;
import com.rkylin.wheatfield.constant.BaseConstants;
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.constant.SettleConstants;
import com.rkylin.wheatfield.service.GenerationPaymentService;
import com.rkylin.wheatfield.settlement.SettlementLogic;

public class UploadPayment {
	private static Logger logger = LoggerFactory
			.getLogger(UploadPayment.class);
	@Autowired
	GenerationPaymentService generationPaymentService;
	@Autowired
	SettlementLogic settlementLogic;

	private int type;
	private String batch;
	private String rootInstCd;
	private static Object lock = new Object();

	 /**
     * 上传昨天
     * Discription: void
     * @author Achilles
     * @since 2016年4月22日
     */
	public void submitToCollectionSys() {
		synchronized (lock) {
			try {
				//通联代收 type=6  ，代付7	
				String accountDate=generationPaymentService.getAccountDate();
//				if(type==6){
//					this.setBatch(settlementLogic.getBatchNo(DateUtils.getAccountDate(Constants.DATE_FORMAT_YYYYMMDD, accountDate), SettleConstants.ROP_RECEIVE_BATCH_CODE, rootInstCd));
//					logger.info("--------代收批次号-------"+batch);
//				}else if(type==7){
//					this.setBatch(settlementLogic.getBatchNo(DateUtils.getAccountDate(Constants.DATE_FORMAT_YYYYMMDD, accountDate), SettleConstants.ROP_PAYMENT_BATCH_CODE, rootInstCd));
//					logger.info("--------代付批次号-------"+batch);
//				}
				generationPaymentService.submitToRecAndPaySys(type, rootInstCd,
						accountDate,DateUtils.getAccountDate(Constants.DATE_FORMAT_YYYYMMDD, accountDate));
				logger.info("--------------------------扫描数据库提交到代收付系统任务结束--------------------------------------");
			} catch (Exception e) {
				logger.error(e.getMessage());
				logger.info("--------------------------扫描数据库提交到代收付系统任务异常--------------------------------------");
			}
		}
	}

	/**
	 * 上传当天
	 * Discription: void
	 * @author Achilles
	 * @since 2016年4月22日
	 */
    public void submitToCollSysT0() {
        try {
            //通联代收 type=6  ，代付7 
            String accountDate=generationPaymentService.getAccountDate();
            generationPaymentService.submitToRecAndPaySys(type, rootInstCd,
                    accountDate,DateUtils.getDate(accountDate,Constants.DATE_FORMAT_YYYYMMDD));
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
    }
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public String getRootInstCd() {
		return rootInstCd;
	}

	public void setRootInstCd(String rootInstCd) {
		this.rootInstCd = rootInstCd;
	}
	
	/**
	 * 悦视觉代付提交到代收系统
	 * Discription: void
	 * @author Achilles
	 * @since 2016年8月30日
	 */
    public void submitToCollSysPayYSJ1() {
        logger.info("---------"+Constants.YSJ_ID+"---代付--扫描数据库提交到代收付系统任务开始--------------------------------------");
        synchronized (lock) {
            String accountDate=generationPaymentService.getAccountDate();
            try {
                generationPaymentService.submitToRecAndPaySys(BaseConstants.FILE_TYPE_7, Constants.YSJ_ID,
                        accountDate,DateUtils.getAccountDate(Constants.DATE_FORMAT_YYYYMMDD, accountDate));
            } catch (Exception e) {
                logger.error(e.getMessage(),e);
                logger.info("--------------------------扫描数据库提交到代收付系统任务异常--------------------------------------");
            }
        }
        logger.info("---------"+Constants.YSJ_ID+"---代付--扫描数据库提交到代收付系统任务结束--------------------------------------");
    }
    
    /**
     * 融数钱包代付提交到代收系统
     * Discription: void
     * @author Achilles
     * @since 2016年9月8日
     */
    public void submitToCollSysPayRSQB1() {
        logger.info("---------"+Constants.RSQB_ID+"---代付--扫描数据库提交到代收付系统任务开始--------------------------------------");
        synchronized (lock) {
            String accountDate=generationPaymentService.getAccountDate();
            try {
                generationPaymentService.submitToRecAndPaySys(BaseConstants.FILE_TYPE_7, Constants.RSQB_ID,
                        accountDate,DateUtils.getAccountDate(Constants.DATE_FORMAT_YYYYMMDD, accountDate));
            } catch (Exception e) {
                logger.error(e.getMessage(),e);
                logger.info("--------------------------扫描数据库提交到代收付系统任务异常--------------------------------------");
            }
        }
        logger.info("---------"+Constants.RSQB_ID+"---代付--扫描数据库提交到代收付系统任务结束--------------------------------------");
    }  

    /**
     * 领客代付提交到代收系统
     * Discription: void
     * @author Achilles
     * @since 2016年9月8日
     */
    public void submitToCollSysPayLK1() {
        logger.info("---------"+Constants.LINGKE_ID+"---代付--扫描数据库提交到代收付系统任务开始--------------------------------------");
        synchronized (lock) {
            String accountDate=generationPaymentService.getAccountDate();
            try {
                generationPaymentService.submitToRecAndPaySys(BaseConstants.FILE_TYPE_7, Constants.LINGKE_ID,
                        accountDate,DateUtils.getAccountDate(Constants.DATE_FORMAT_YYYYMMDD, accountDate));
            } catch (Exception e) {
                logger.error(e.getMessage(),e);
                logger.info("--------------------------扫描数据库提交到代收付系统任务异常--------------------------------------");
            }
        }
        logger.info("---------"+Constants.LINGKE_ID+"---代付--扫描数据库提交到代收付系统任务结束--------------------------------------");
    }  
 
    /**
     * 旅游代付提交到代收付
     * Discription: void
     * @author Achilles
     * @since 2016年10月24日
     */
    public void submitToCollSysPayLY1() {
        logger.info("---------"+Constants.LY_ID+"---代付--扫描数据库提交到代收付系统任务开始--------------------------------------");
        synchronized (lock) {
            String accountDate=generationPaymentService.getAccountDate();
            try {
                generationPaymentService.submitToRecAndPaySys(BaseConstants.FILE_TYPE_7, Constants.LY_ID,
                        accountDate,DateUtils.getAccountDate(Constants.DATE_FORMAT_YYYYMMDD, accountDate));
            } catch (Exception e) {
                logger.error(e.getMessage(),e);
                logger.info("--------------------------扫描数据库提交到代收付系统任务异常--------------------------------------");
            }
        }
        logger.info("---------"+Constants.LY_ID+"---代付--扫描数据库提交到代收付系统任务结束--------------------------------------");
    }
    
    /**
     * 天联在线代付提交到代收付
     * Discription: void
     * @author Achilles
     * @since 2016年11月4日
     */
    public void submitToCollSysPayTLZX1() {
        logger.info("---------"+Constants.TLZX_ID+"---代付--扫描数据库提交到代收付系统任务开始--------------------------------------");
        synchronized (lock) {
            String accountDate=generationPaymentService.getAccountDate();
            try {
                generationPaymentService.submitToRecAndPaySys(BaseConstants.FILE_TYPE_7, Constants.TLZX_ID,
                        accountDate,DateUtils.getAccountDate(Constants.DATE_FORMAT_YYYYMMDD, accountDate));
            } catch (Exception e) {
                logger.error(e.getMessage(),e);
                logger.info("--------------------------扫描数据库提交到代收付系统任务异常--------------------------------------");
            }
        }
        logger.info("---------"+Constants.TLZX_ID+"---代付--扫描数据库提交到代收付系统任务结束--------------------------------------");
    }  
    
    /**
     * 房仓代付提交到代收付系统
     * Discription: void
     * @author Achilles
     * @since 2016年11月18日
     */
    public void submitToCollSysPayFC1() {
        logger.info("---------"+Constants.FC_ID+"---代付--扫描数据库提交到代收付系统任务开始--------------------------------------");
        synchronized (lock) {
            String accountDate=generationPaymentService.getAccountDate();
            try {
                generationPaymentService.submitToRecAndPaySys(BaseConstants.FILE_TYPE_7, Constants.FC_ID,
                        accountDate,DateUtils.getAccountDate(Constants.DATE_FORMAT_YYYYMMDD, accountDate));
            } catch (Exception e) {
                logger.error(e.getMessage(),e);
                logger.info("--------------------------扫描数据库提交到代收付系统任务异常--------------------------------------");
            }
        }
        logger.info("---------"+Constants.FC_ID+"---代付--扫描数据库提交到代收付系统任务结束--------------------------------------");
    }     
}
