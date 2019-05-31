package cn.itcast.estore.utils;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * 邮件发送的工具类:
 */
public class MailUtils {
	/**
	 * 发送邮件的方法:
	 * @param to	:收件人
	 * @param code	:激活码
	 */
	public static void sendMail(String to,String code){
		// 邮件发送三个步骤:
		// 创建一个Session对象:连接对象.
		Properties props = new Properties();
		Session session = Session.getInstance(props, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("service@estore.com", "111");
			}
			
		});
		// 创建一个Message对象:邮件对象.
		Message message = new MimeMessage(session);
		// 设置发件人:
		try {
			message.setFrom(new InternetAddress("service@estore.com"));
			// 设置收件人:
			message.addRecipient(RecipientType.TO, new InternetAddress(to));
			// 设置主题:
			message.setSubject("购物天堂ESTORE的官方激活邮件");
			// 设置邮件的内容:
			message.setContent("<h1>来自购物天堂ESTORE的官方激活邮件:激活请点击以下链接</h1><h3><a href='http://localhost:8080/estore/userServlet?method=active&code="+code+"'>http://localhost:8080/estore/userServlet?method=active&code="+code+"</a></h3>", "text/html;charset=UTF-8");
			// 使用Transport对象发送邮件.
			Transport.send(message);
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		
	}
}
