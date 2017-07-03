//package test.wheatfield;
//
//import java.util.List;
//
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//
//import com.rkylin.wheatfield.pojo.AccountInfo;
//import com.rkylin.wheatfield.pojo.User;
//import com.rkylin.wheatfield.response.AccountInfoGetResponse;
//import com.rkylin.wheatfield.service.AccountManageService;
//
//
//public class AccountInfoPlusTest extends BaseJUnit4Test {
//	@Autowired
//	@Qualifier("accountManageService")
//	AccountManageService accountManageService;
//	
//	@Test
//	public void accountInfoPlus(){
//		AccountInfoGetResponse response = new AccountInfoGetResponse();
//		User user = new User();
//		user.constId = "M000001";
//		user.productId="P000002";
//		user.userId = "sr_fn01";
//		String objOrList = "1";
//		List<AccountInfo> accountList = accountManageService.getAccountListPlus(user, objOrList);
//		for (int j = 0; j < accountList.size(); j++) {
//			System.out.println("第"+j+"条"+"accountNum"+accountList.get(j).getAccountNumber()
//					+"urllogo"+accountList.get(j).getUrllogo()
//					+"cerNum"+accountList.get(j).getCertificateNumber());
//		}
//
//	}
//
//}
