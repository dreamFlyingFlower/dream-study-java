package com.wy.websocket;

import java.io.IOException;
import java.nio.ByteBuffer;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.PongMessage;
import javax.websocket.Session;

/**
 * WebSocket客户端,发送消息
 *
 * @author 飞花梦影
 * @date 2023-10-07 13:42:13
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
@ClientEndpoint
public class WebSocketClient {

	@OnOpen
	public void onOpen(Session session) {
		// 连接建立
	}

	@OnClose
	public void onClose(Session session, CloseReason reason) {
		// 连接关闭
	}

	@OnMessage
	public void onMessage(Session session, String message) {
		// 发送文本消息
		session.getAsyncRemote().sendText(message);
		// 发送Object消息,会尝试使用Encoder编码
		session.getAsyncRemote().sendObject(message);
	}

	@OnMessage
	public void onMessage(Session session, PongMessage message) {
	}

	@OnMessage
	public void onMessage(Session session, ByteBuffer message) {
		// 发送二进制消息
		session.getAsyncRemote().sendBinary(message);
		try {
			// 发送ping
			session.getAsyncRemote().sendPing(message);
			// 发送pong
			session.getAsyncRemote().sendPong(message);
		} catch (IllegalArgumentException | IOException e) {
			e.printStackTrace();
		}
	}

	@OnError
	public void onError(Session session, Throwable e) {
		// 异常处理
	}
}