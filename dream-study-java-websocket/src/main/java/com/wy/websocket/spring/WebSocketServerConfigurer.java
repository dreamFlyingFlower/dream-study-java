package com.wy.websocket.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * SpringBoot拦截WebSocket请求
 *
 * @author 飞花梦影
 * @date 2023-10-07 13:59:01
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
@Configuration
public class WebSocketServerConfigurer implements WebSocketConfigurer {

	@Override
	public void registerWebSocketHandlers(@NonNull WebSocketHandlerRegistry registry) {
		// 添加处理器到对应的路径.WebSocketServerHandler可以切换成WebSocketTextServerHandler
		registry.addHandler(new WebSocketServerHandler(), "/spring-websocket").setAllowedOrigins("*")
				.addInterceptors(new WebSocketShakeInterceptor());
		registry.addHandler(new WebSocketServerHandler(), "/sockjs/server").setAllowedOrigins("*")
				.addInterceptors(new WebSocketShakeInterceptor())
				// 应对浏览器不支持websocket协议的时候降级为轮询
				.withSockJS();
	}
}