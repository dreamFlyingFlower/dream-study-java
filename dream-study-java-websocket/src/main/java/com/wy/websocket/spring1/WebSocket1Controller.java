package com.wy.websocket.spring1;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * {@link MessageMapping}:接收客户端消息.将发送到目标/app/sendMessage(在WebSocket1Config中配置)的客户端消息映射到此方法
 * {@link SendTo}:将消息发送到指定广播主题.任何订阅/topic/notifications的客户端都将收到通过此方法发回的消息
 * 
 * @author 飞花梦影
 * @date 2025-01-15 14:56:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Controller
public class WebSocket1Controller {

	@MessageMapping("/sendMessage")
	@SendTo("/topic/notifications")
	public String receiveMessage(String message) {
		System.out.println("Received message: " + message);
		return message;
	}
}