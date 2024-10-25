package dream.study.spring.mail;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * 
 * 
 * @author ParadiseWY
 * @date 2020-12-08 09:30:13
 * @git {@link https://github.com/mygodness100}
 */
@SpringBootTest
public class TestMail {

	@Autowired
	private JavaMailSenderImpl mailSender;

	@Test
	public void sendSimpleMail() {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setSubject("邮件主题");
		message.setText("邮件内容");
		message.setTo("fdsfdsfds@qq.com");
		message.setFrom("582822832@qq.com");
		mailSender.send(message);
	}

	/**
	 * 复杂件可以使用MimeMessage等,查看SimpleUtils
	 */
	@Test
	public void sendComplexMail() {
		MimeMessage message = mailSender.createMimeMessage();
		try {
			// 创建一个带附件的邮件发送类
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
			messageHelper.setSubject("邮件主题");
			// 可以发送html内容,但是后一个参数需要设为为true
			// messageHelper.setText("<h1>这是一个标题</h1>", true);
			messageHelper.setText("邮件内容");
			messageHelper.setTo("fdsfdsfds@qq.com");
			messageHelper.setFrom("582822832@qq.com");
			messageHelper.addAttachment("attach.jpg", new File("d://test1.jpg"));
			messageHelper.addAttachment("attach.jpg", new File("d://test2.jpg"));
			mailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}