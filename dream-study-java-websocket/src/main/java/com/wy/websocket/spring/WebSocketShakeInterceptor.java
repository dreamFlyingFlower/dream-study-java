package com.wy.websocket.spring;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 * WebSocket握手拦截器
 *
 * @author 飞花梦影
 * @date 2022-05-24 23:15:08
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class WebSocketShakeInterceptor implements HandshakeInterceptor {

	/**
	 * 握手之前,若返回false,则不建立连接
	 * 
	 * @param request 请求
	 * @param response 响应
	 * @param wsHandler websocket处理类
	 * @param attributes WebSocketSession中的其他属性
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		System.out.println("WebSocket...握手之前");
		HttpHeaders headers = request.getHeaders();
		List<String> tokens = headers.get("token");
		attributes.put("tokens", tokens.get(0));
		return true;
	}

	/**
	 * 握手成功之后执行
	 * 
	 * @param request 请求
	 * @param response 响应
	 * @param wsHandler websocket处理类
	 * @param exception
	 */
	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {
		System.out.println("WebSocket...握手之后");
	}
}