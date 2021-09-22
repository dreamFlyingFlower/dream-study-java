package com.wy.netty.chat.client;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wy.netty.chat.client.swing.Swingclient;

/**
 * 客户端启动
 * 
 * @author 飞花梦影
 * @date 2021-09-22 11:08:34
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class ClientStart {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		Swingclient swing = applicationContext.getBean(Swingclient.class);
		swing.setVisible(true);
	}
}