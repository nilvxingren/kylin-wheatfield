package com.rkylin.wheatfield.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
/**
 * 简单邮件发送器，可单发，群发。
 *
 * @author MZULE
 *
 */
public class MailUtil extends Authenticator {
    /**
        * 发送邮件的props文件
*/
    private final transient Properties props = System.getProperties();

    /**
* 邮箱session
*/
    private  transient Session session;

    private static final String username = "rkylin_order@aliyun.com";

    private static final String password = "123qwe";

    private static final String smtpHostName = "smtp.aliyun.com";

    private static final List<String> recipients = new ArrayList<String>();

    static {
        recipients.add("huzijian@rkylin.com.cn");
        recipients.add("zhangjie@rkylin.com.cn");
        recipients.add("wangmingsheng@rkylin.com.cn");
        recipients.add("zhaoxueyu@rkylin.com.cn");
    }

    public MailUtil() {
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", smtpHostName);
        session = Session.getInstance(props, this);
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username,password);
    }

    /**
    * 发送邮件
    *
    * @param recipient
    * 收件人邮箱地址
    * @param subject
    * 邮件主题
    * @param content
    * 邮件内容
    * @throws AddressException
    * @throws MessagingException
    */
    public void send(String recipient, String subject, Object content)
                        throws AddressException, MessagingException {
        String smtpHostName = "smtp." + username.split("@")[1];
        // 创建mime类型邮件
        final MimeMessage message = new MimeMessage(session);
        // 设置发信人
        message.setFrom(new InternetAddress(username));
        // 设置收件人
        message.setRecipient(RecipientType.TO, new InternetAddress(recipient));
        // 设置主题
        message.setSubject(subject);
        // 设置邮件内容
        message.setContent(content.toString(), "text/html;charset=utf-8");
        // 发送
        Transport.send(message);
}

    /**
* 群发邮件
*
* @param recipients
* 收件人们
* @param subject
* 主题
* @param content
* 内容
* @throws AddressException
* @throws MessagingException
*/
    public void send(List<String> recipients, String subject, Object content)
        throws  MessagingException {
        // 创建mime类型邮件
        final MimeMessage message = new MimeMessage(session);
        // 设置发信人
        message.setFrom(new InternetAddress(username));
        // 设置收件人们
        final int num = recipients.size();
        InternetAddress[] addresses = new InternetAddress[num];
        for (int i = 0; i < num; i++) {
            addresses[i] = new InternetAddress(recipients.get(i));
            }
        message.setRecipients(RecipientType.TO, addresses);
        // 设置主题
        message.setSubject(subject);
        // 设置邮件内容
        message.setContent(content.toString(), "text/html;charset=utf-8");
        // 发送
        Transport.send(message);
    }

    public   void send(String subject,String content) {
    	try{
    		final MimeMessage message = new MimeMessage(session);
            // 设置发信人
            message.setFrom(new InternetAddress(username));
            // 设置收件人们
            final int num = recipients.size();
            InternetAddress[] addresses = new InternetAddress[num];
            for (int i = 0; i < num; i++) {
                addresses[i] = new InternetAddress(recipients.get(i));
            }
            message.setRecipients(RecipientType.TO, addresses);
            // 设置主题
            message.setSubject(subject);
            // 设置邮件内容
            message.setContent(content.toString(), "text/html;charset=utf-8");
            // 发送
            Transport.send(message);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
        
    }


}