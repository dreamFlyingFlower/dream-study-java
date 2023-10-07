package com.wy.websocket.spring;

import java.nio.ByteBuffer;

import org.springframework.lang.NonNull;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PingMessage;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * SpringBoot+WebSocket
 *
 * @author 飞花梦影
 * @date 2023-10-07 13:57:41
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class ServletWebSocketServerHandler implements WebSocketHandler {

	@Override
	public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
		// 连接建立
	}

	@Override
	public void handleMessage(@NonNull WebSocketSession session, @NonNull WebSocketMessage<?> message)
			throws Exception {
		// 接收或发送消息
		// 发送文本消息
		session.sendMessage(new TextMessage(""));

		// 发送二进制消息
		session.sendMessage(new BinaryMessage(ByteBuffer.allocate(1024)));

		// 发送ping
		session.sendMessage(new PingMessage(ByteBuffer.allocate(1024)));

		// 发送pong
		session.sendMessage(new PongMessage(ByteBuffer.allocate(1024)));
	}

	@Override
	public void handleTransportError(@NonNull WebSocketSession session, @NonNull Throwable exception) throws Exception {
		// 异常处理
	}

	@Override
	public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus closeStatus)
			throws Exception {
		// 连接关闭
	}

	@Override
	public boolean supportsPartialMessages() {
		// 是否支持接收不完整的消息
		return false;
	}
}