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
public class WebSocketServerHandler implements WebSocketHandler {

	/**
	 * websocket建立连接后执行afterConnectionEstablished回调接口
	 * 
	 * @param session WebSocketSession
	 * @throws Exception
	 */
	@Override
	public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
		// 连接建立
		System.out.println("WebSocket...已经建立了连接");
		Object token = session.getAttributes().get("token");
		if (token != null) {
			// 用户连接成功,放入在线用户缓存
			WebSocketSessionManager.add(token.toString(), session);
		} else {
			throw new RuntimeException("用户登录已经失效!");
		}
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

	/**
	 * websocket传输异常时执行该方法
	 * 
	 * @param session WebSocketSession
	 * @param exception Throwable
	 * @throws Exception
	 */
	@Override
	public void handleTransportError(@NonNull WebSocketSession session, @NonNull Throwable exception) throws Exception {
		// 异常处理
		System.out.println("WebSocket...发生了异常");
	}

	/**
	 * websocket关闭连接后执行afterConnectionClosed回调接口
	 * 
	 * @param session WebSocketSession
	 * @param closeStatus CloseStatus
	 * @throws Exception
	 */
	@Override
	public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus closeStatus)
			throws Exception {
		// 连接关闭
		System.out.println("WebSocket...关闭了连接");
		Object token = session.getAttributes().get("token");
		if (token != null) {
			WebSocketSessionManager.remove(token.toString());
		}
	}

	@Override
	public boolean supportsPartialMessages() {
		// 是否支持接收不完整的消息
		return false;
	}
}