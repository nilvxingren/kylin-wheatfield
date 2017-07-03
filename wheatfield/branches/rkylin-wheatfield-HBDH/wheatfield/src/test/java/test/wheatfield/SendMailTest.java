package test.wheatfield;

import org.junit.Test;

import com.rkylin.utils.RkylinMailUtil;

public class SendMailTest {

	@Test
	public void sendEmail(){
		try {
			RkylinMailUtil.sendMailThread("test", "test", "ranzhilei@hotmail.com");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		System.out.println("*******邮件发送");
	}
}
