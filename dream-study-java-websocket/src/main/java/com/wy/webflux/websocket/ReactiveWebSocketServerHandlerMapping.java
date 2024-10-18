package com.wy.webflux.websocket;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;

/**
 * 注册一个HandlerMapping同时配置路径和对应的WebSocketHandler
 *
 * @author 飞花梦影
 * @date 2024-10-18 15:04:07
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class ReactiveWebSocketServerHandlerMapping extends SimpleUrlHandlerMapping {

	public ReactiveWebSocketServerHandlerMapping() {
		Map<String, WebSocketHandler> map = new HashMap<>();
		map.put("/websocket/**", new ReactiveWebSocketServerHandler());
		setUrlMap(map);
		// order不能使用默认,否则可能被别的HandlerMapping优先匹配
		setOrder(100);
	}

}