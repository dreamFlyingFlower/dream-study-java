package com.wy.mail;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.ReceivedDateTerm;

import org.springframework.boot.autoconfigure.mail.MailProperties;

import lombok.extern.slf4j.Slf4j;

/**
 * 接收指定邮箱的邮件,只能通过定时任务不停监听指定邮箱
 * 
 * 
 * 需要邮件的相关配置如下:
 * 
 * <pre>
 *  mail:
 *    default-encoding: UTF-8
 *    # 接收邮件的协议
 *    protocol: imap
 *    # 接收邮件的服务器,根据服务器不同而不同
 *    host: imap.exmail.qq.com
 *    # 接收邮件的服务器端口,根据服务器不同而不同,一般是993
 *    port: 993
 *    # 接收邮件的邮箱地址和密码
 *    username: yang_wan@bjdv.com
 *    password:
 * </pre>
 *
 * @author 飞花梦影
 * @date 2025-01-13 13:55:37
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Slf4j
public class ReceiveMail {

	private MailProperties mailProperties;

	@SuppressWarnings("resource")
	public void execute() {
		Properties props = new Properties();
		// 协议一般是iamp
		props.put("mail.store.protocol", mailProperties.getProtocol());
		// host根据协议改变
		props.put("mail.imap.host", mailProperties.getHost());

		try {
			Session session = Session.getInstance(props);
			// 获得imap协议
			Store store = session.getStore("imap");
			store.connect(mailProperties.getUsername(), mailProperties.getPassword());
			// 固定写法
			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_ONLY);

			Calendar calendar = Calendar.getInstance();
			// 搜索2天前到今天收到的的所有邮件,根据时间筛选邮件
			calendar.add(Calendar.DAY_OF_MONTH, -2);
			// 创建ReceivedDateTerm对象,ComparisonTerm.GE,Date类型的时间new
			// Date(calendar.getTimeInMillis())----表示2天前
			ReceivedDateTerm term = new ReceivedDateTerm(ComparisonTerm.GE, new Date(calendar.getTimeInMillis()));
			// 把时间筛选条件添加到收件箱文件夹里,得到2天前到今天的所有邮件
			Message[] messages = folder.search(term);

			// 获得所有邮件
			folder.getMessages();

			if (folder.getMessageCount() <= 0) {
				store.close();
				folder.close();
				return;
			}

			for (Message message : messages) {
				handlerSingle(message);
			}
			store.close();
			folder.close(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handlerSingle(Message message) throws Exception {
		// 主题
		System.out.println(message.getSubject());
		// 内容对象
		System.out.println(message.getContent());
		// 发送时间
		System.out.println(message.getSentDate());
		Object mimeMessage = message.getContent();
		if (null == mimeMessage) {
			log.info("发送内容为空,不进行操作!");
			return;
		}

		if (mimeMessage instanceof MimeMultipart) {
			handlerContent(mimeMessage);
		}
		// System.out.println("From: " + InternetAddress.toString(message.getFrom()));
	}

	private void handlerContent(Object mimeMessage) {
		// 如果是不带附件的内容,可通过MimeMultipart一行一行的读取
		MimeMultipart mimeMultipart = (MimeMultipart) mimeMessage;
		try {
			for (int i = 0; i < mimeMultipart.getCount(); i++) {
				// 一行一行读取数据
				String content = mimeMultipart.getBodyPart(i).getContent().toString();
				System.out.println("收到邮件的正文:" + content);
			}
		} catch (MessagingException | IOException e) {
			e.printStackTrace();
		}
	}
}