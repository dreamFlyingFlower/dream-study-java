package com.wy.webflux.websocket;

import java.net.URI;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;

/**
 * 注册一个WebSocketClient
 *
 * @author 飞花梦影
 * @date 2024-10-18 15:04:07
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
public class ReactiveWebSocketClientConfiguration {

	@Bean
	public WebSocketClient reactiveWebSocketClient(URI uri) {
		WebSocketClient client = new ReactorNettyWebSocketClient();
		WebSocketHandler handler = new ReactiveWebSocketClientHandler();
		client.execute(uri, handler).subscribe();
		return client;
	}
}