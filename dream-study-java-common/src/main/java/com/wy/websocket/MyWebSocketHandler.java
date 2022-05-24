package com.wy.websocket;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

/**
 * Websocket+Spring,可以直接实现{@link WebSocketHandler},也可以继承该接口的实现类
 *
 * @author 飞花梦影
 * @date 2021-06-02 15:33:35
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class MyWebSocketHandler extends AbstractWebSocketHandler {

	private String message;

	public MyWebSocketHandler(String message) {
		this.message = message;
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println("建立链接");
		// 在拦截器中加入attributes的属性可以在此处取出
		session.getAttributes().get("userId");
	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		super.handleMessage(session, message);
	}

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		System.out.println("开始处理消息");
		System.out.println("从前端WebSocket中拿到的消息:" + message.getPayload());
		// 回传消息
		session.sendMessage(new TextMessage(this.message.getBytes()));
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		System.out.println("传输异常");
		session.close(CloseStatus.SERVER_ERROR);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		System.out.println("链接关闭之后操作");
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}
}