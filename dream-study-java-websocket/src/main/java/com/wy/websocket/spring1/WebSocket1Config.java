package com.wy.websocket.spring1;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * {@link EnableWebSocketMessageBroker}:激活WebSocket消息处理,它允许使用STOMP(Simple Text
 * Oriented Messaging Protocol)来处理通过WebSocket的消息
 * 
 * 此方法有极大缺陷, {@link MessageMapping}和{@link SendTo}必须同时使用,无法做到只接收消息或只发送消息
 *
 * @author 飞花梦影
 * @date 2025-01-15 14:29:46
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocket1Config implements WebSocketMessageBrokerConfigurer {

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		// 启用一个简单的内存中消息代理,该消息代理将带有/topic前缀的消息路由到订阅者
		config.enableSimpleBroker("/topic");
		// 定义从客户端发送到服务器的消息的前缀.向/app/sendMessage发送消息的客户端将触发映射到的控制器方法@MessageMapping("/sendMessage")
		config.setApplicationDestinationPrefixes("/app");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry
				// 将/ws定义为客户端用于建立连接的WebSocket终端节点,前端可以连接到ws://localhost:8080/ws以启动 WebSocket 会话
				.addEndpoint("/ws")
				// 限制允许连接的源(前端域),这对于跨域非常重要
				.setAllowedOrigins("http://localhost:63342")
				// 启用Sockjs,这是一个在浏览器不支持WebSocket时提供回退选项的库
				.withSockJS();
	}
}