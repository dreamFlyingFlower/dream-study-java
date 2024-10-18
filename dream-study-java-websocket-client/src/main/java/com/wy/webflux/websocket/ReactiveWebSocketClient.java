package com.wy.webflux.websocket;

import java.net.URI;
import java.nio.ByteBuffer;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.web.reactive.socket.CloseStatus;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;

import reactor.core.publisher.FluxSink;

/**
 * 注册一个HandlerMapping同时配置路径和对应的WebSocketHandler
 *
 * @author 飞花梦影
 * @date 2024-10-18 15:04:07
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class ReactiveWebSocketClient {

	private final WebSocketSession session;

	private final FluxSink<WebSocketMessage> sender;

	public ReactiveWebSocketClient(WebSocketSession session, FluxSink<WebSocketMessage> sender) {
		this.session = session;
		this.sender = sender;
	}

	public String getId() {
		return session.getId();
	}

	public URI getUri() {
		return session.getHandshakeInfo().getUri();
	}

	public void send(Object message) {
		if (message instanceof WebSocketMessage) {
			sender.next((WebSocketMessage) message);
		} else if (message instanceof String) {
			// 发送文本消息
			sender.next(session.textMessage((String) message));
		} else if (message instanceof DataBuffer) {
			// 发送二进制消息
			sender.next(session.binaryMessage(factory -> (DataBuffer) message));
		} else if (message instanceof ByteBuffer) {
			// 发送二进制消息
			sender.next(session.binaryMessage(factory -> factory.wrap((ByteBuffer) message)));
		} else if (message instanceof byte[]) {
			// 发送二进制消息
			sender.next(session.binaryMessage(factory -> factory.wrap((byte[]) message)));
		} else {
			throw new IllegalArgumentException("Message type not match");
		}
	}

	public void ping() {
		// 发送ping
		sender.next(session.pingMessage(factory -> factory.wrap(ByteBuffer.allocate(0))));
	}

	public void pong() {
		// 发送pong
		sender.next(session.pongMessage(factory -> factory.wrap(ByteBuffer.allocate(0))));
	}

	public void close(CloseStatus reason) {
		sender.complete();
		session.close(reason).subscribe();
	}
}