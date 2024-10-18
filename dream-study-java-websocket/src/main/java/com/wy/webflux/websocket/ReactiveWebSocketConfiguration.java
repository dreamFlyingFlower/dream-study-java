package com.wy.webflux.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

/**
 * 注入Bean
 *
 * @author 飞花梦影
 * @date 2024-10-18 15:04:42
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration(proxyBeanMethods = false)
public class ReactiveWebSocketConfiguration {

	@Bean
	public WebSocketHandlerAdapter webSocketHandlerAdapter() {
		return new WebSocketHandlerAdapter();
	}
}