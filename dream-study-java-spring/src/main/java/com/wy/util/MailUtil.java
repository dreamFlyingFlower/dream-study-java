package com.wy.util;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 * Mail邮件发送
 * 
 * @author 飞花梦影
 * @date 2021-04-01 16:07:44
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Component
public class MailUtil {

	@Autowired
	private JavaMailSender javaMailSender;

	/**
	 * 发送简单邮件
	 * 
	 * @param from 邮件发送人地址
	 * @param to 邮件接收人地址
	 */
	public void sendSimple(String from, String to) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setTo(to);
		message.setSubject("txt这里是发送的邮件主题");
		message.setText("这里写的是邮件的内容");
		javaMailSender.send(message);
	}

	/**
	 * 发送带内嵌内容的html的邮件
	 */
	public void sendHtml(String from, String to, String[] cc, String bcc) {
		MimeMessage mm = javaMailSender.createMimeMessage();
		MimeMessageHelper mmh = new MimeMessageHelper(mm, StandardCharsets.UTF_8.name());
		try {
			mmh.setFrom(from);
			mmh.setTo(to);
			mmh.setReplyTo(from);
			mmh.setCc(cc);
			mmh.setBcc(bcc);
			mmh.setSentDate(new Date());
			mmh.setSubject("标题");
			// cid是一个固定前缀,testimg是一个资源名称
			String html = "<p>html测试</p><img src='cid:testimg' />";
			// 邮件内容,true代表是html代码
			mmh.setText(html, true);
			javaMailSender.send(mm);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 邮件中带图片
	 */
	public void sendImage(String from, String to, String[] cc, String bcc) {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		try {
			// multipart模式
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
			mimeMessageHelper.setTo(to);
			mimeMessageHelper.setFrom(from);
			mimeMessageHelper.setSubject("Spring Boot Mail 邮件测试【图片】");
			StringBuilder sb = new StringBuilder();
			sb.append("spring 邮件测试hello!this is spring mail test。");
			// cid为固定写法,imageId指定一个标识
			sb.append("\"cid:imageId\"/>");
			mimeMessageHelper.setText(sb.toString(), true);
			// 设置imageId
			FileSystemResource img = new FileSystemResource(new File("/Users/limbo/Desktop/1.jpg"));
			mimeMessageHelper.addInline("imageId", img);
			javaMailSender.send(mimeMessage);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送带附件的邮件
	 */
	public void sendAttach(String from, String to, String[] cc, String bcc) {
		MimeMessage mm = javaMailSender.createMimeMessage();
		try {
			// 指定html编码,参数true表示为multipart
			MimeMessageHelper mmh = new MimeMessageHelper(mm, true, StandardCharsets.UTF_8.name());
			mmh.setFrom(from);
			mmh.setTo(to);
			mmh.setReplyTo(from);
			mmh.setCc(cc);
			mmh.setBcc(bcc);
			mmh.setSentDate(new Date());
			mmh.setSubject("发送带附件的邮件");
			String html = "<p>带附件的测试</p>";
			mmh.setText(html, true);
			String attach = "test.docx";
			// 加载项目路径下资源
			// ClassPathResource resource = new ClassPathResource(attach);
			// mmh.addAttachment(MimeUtility.encodeWord(attach), resource);
			FileSystemResource file = new FileSystemResource(attach);
			mmh.addAttachment(file.getFilename(), file);
			javaMailSender.send(mm);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}