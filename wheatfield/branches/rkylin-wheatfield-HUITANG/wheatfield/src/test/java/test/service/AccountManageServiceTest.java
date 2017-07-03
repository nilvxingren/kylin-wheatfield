/**
 * @File name : AccountManageServiceTest.java
 * @Package : test.service
 * @Description : TODO(用一句话描述该文件做什么)
 * @Creator : Administrator
 * @CreateTime : 2015年9月16日 下午1:45:20
 * @Version : 1.0
 * @Update records:
 *      1.1 2015年9月16日 by Administrator: 
 *      1.0 2015年9月16日 by Administrator: Created 
 * All rights served : FENGNIAN Corporation
 */
package test.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.rkylin.wheatfield.common.DateUtils;
import com.rkylin.wheatfield.enumtype.UserTypeEnum;
import com.rkylin.wheatfield.pojo.AccountAgreement;
import com.rkylin.wheatfield.pojo.AccountInfo;
import com.rkylin.wheatfield.pojo.CreditApprovalInfo;
import com.rkylin.wheatfield.pojo.User;
import com.rkylin.wheatfield.service.AccountManageService;
import com.rkylin.wheatfield.service.impl.AccountManageServiceImpl;

import test.wheatfield.BaseJUnit4Test;

public class AccountManageServiceTest extends BaseJUnit4Test{

	@Autowired
  	private AccountManageService accountManageService;
	
	@Test
	public void testCreditAccountNew(){
		User user = new User();
		user.userId = "2222222222";
		user.constId = "M000005";
		user.productId = "P000006";
		user.referUserId = "M000002";
		user.creditType = "101";
		
		CreditApprovalInfo creditApprovalInfo = new CreditApprovalInfo();
		creditApprovalInfo.setAmount(100L);
		creditApprovalInfo.setBillday("15");
		creditApprovalInfo.setRootInstCd("M000005");
		creditApprovalInfo.setUserId("2222222222");
		creditApprovalInfo.setStatusId("1");
		
		
		AccountAgreement accountAgreement = new AccountAgreement();
		accountAgreement.setAgmtCode("JRDFN201504090000000018");
		accountAgreement.setStatus(1);
		
		String provideruserid = "M000002";
		
		try {
			String resultMsg = accountManageService.creditAccountNew(user, creditApprovalInfo, accountAgreement, provideruserid);
			System.out.println(resultMsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	@Test
	public void testMain(){
		
		for (int i = 0; i < 3; i++) {
			Thread t=new Thread(new Runnable() {				
				@Override
				public void run() {
					//accountManageService.execute();
				}				
			});
			t.start();
			try {
				t.sleep(10000);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			System.out.println(t.getName());
		}
		
		
//      Thread t1 = new Thread1(accountManageService);
//      Thread t2 = new Thread1(accountManageService);
//      Thread t3 = new Thread1(accountManageService);
//      Thread t4 = new Thread1(accountManageService);

//      t1.start();
//      t2.start();
//      t3.start();
//      t4.start();
//      try{
//    	  t1.join();
//    	  t2.join();
//    	  t3.join();
//    	  t4.join();
//      }catch(Exception ex){
//    	  ex.printStackTrace();
//      }
	}
	
	@Test
	public void testBindingBankAccount(){
		
		
		//accountManageService.execute();
		
		
		User user = new User();
		user.userId = "1111111111";
		user.userType = UserTypeEnum.toEnum("2");
		user.constId = "M000003";
		user.productId = "P000011";
		
		AccountInfo accountInfo = new AccountInfo();
		
		accountInfo.setAccountNumber("123456789987654321");
		accountInfo.setAccountTypeId("00");
		accountInfo.setBankBranchName("bankBranchName");
		accountInfo.setBankHeadName("BankHeadName");
		accountInfo.setCurrency("CNY");
		accountInfo.setOpenAccountDate(DateUtils.getDate("2015-9-25 14:05:17", "yyyy-mm-dd hh:mm:ss"));
		accountInfo.setOpenAccountDescription("OpenAccountDescription");
		accountInfo.setAccountPurpose("1");
		accountInfo.setAccountProperty("1");
		accountInfo.setCertificateType("0");
		accountInfo.setCertificateNumber("123456789987654321");
		accountInfo.setBankHead("BankHead");
		accountInfo.setAccountRealName("AccountRealName");
		accountInfo.setAccountName("AccountName");
		accountInfo.setBankBranch("BankBranch");
		accountInfo.setBankProvince("BankProvince");
		accountInfo.setBankCity("北京");
		
		String result = accountManageService.bindingBankAccount(user, accountInfo);
		
		System.out.println(result + "   -------------------------");
		
		
	}
}

class Thread1 extends Thread
{
	private AccountManageService accountManageService;

    public Thread1(AccountManageService example)
    {
        this.accountManageService = example;
    }

    @Override
    public void run()
    {
    	//accountManageService.execute();
    }

}