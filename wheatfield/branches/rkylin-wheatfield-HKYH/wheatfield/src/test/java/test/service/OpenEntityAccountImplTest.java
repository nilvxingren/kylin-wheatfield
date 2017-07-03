package test.service;

import com.rkylin.wheatfield.domain.M000003OpenEntityAccountBean;
import com.rkylin.wheatfield.domain.M000003OpenPersonAccountBean;
import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.service.OpenEntityAccount;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by thonny on 2015-5-11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath*:spring/applicationContext.xml"})
public class OpenEntityAccountImplTest extends AbstractJUnit4SpringContextTests {

    @Autowired(required = true)
    @Qualifier("openEntityAccountService")
    private OpenEntityAccount openEntityAccountService;

    @Test
    public void openPrivateAccountTest(){
    	M000003OpenPersonAccountBean accountBean = new M000003OpenPersonAccountBean();
    	accountBean.setPersonChnName("中文姓名AAa");
    	accountBean.setCertificateType("1");
    	accountBean.setCertificateNumber("8392482394829483");
    	accountBean.setUserId("15051204040");
    	accountBean.setConstId("M000001");
    	accountBean.setProductId("P000002");;
        CommonResponse s = openEntityAccountService.openPrivateAccount(accountBean,"2");
        System.out.println(s);
    }
    
    @Test
    public void aaupdateTest(){
        M000003OpenEntityAccountBean m000003OpenEntityAccountBean = new M000003OpenEntityAccountBean();
        m000003OpenEntityAccountBean.setConstId("M0000031");
        m000003OpenEntityAccountBean.setProductId("P000007");
        m000003OpenEntityAccountBean.setUserId("1");
        m000003OpenEntityAccountBean.setPost("10000");
        m000003OpenEntityAccountBean.setBusLince("10000");
        String s = openEntityAccountService.updateMerchantAccount(m000003OpenEntityAccountBean);
        System.out.println(s);
    }
}
