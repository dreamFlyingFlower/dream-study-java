package com.wy.websocket.spring;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * SpringBoot+WebSocket
 *
 * @author 飞花梦影
 * @date 2023-10-07 13:57:41
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class WebSocketTextServerHandler extends TextWebSocketHandler {

	/**
	 * 存储websocket客户端连接
	 */
	private static final Map<String, WebSocketSession> connections = new HashMap<>();

	/**
	 * 建立连接后触发
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println("成功建立websocket连接");
		// 建立连接后将连接以键值对方式存储,便于后期向客户端发送消息.以客户端连接的唯一标识为key,可以通过客户端发送唯一标识
		connections.put(session.getRemoteAddress().getHostName(), session);
		System.out.println("当前客户端连接数：" + connections.size());
	}

	/**
	 * websocket接收客户端消息后执行该方法
	 * 
	 * @param session WebSocketSession
	 * @param message TextMessage
	 * @throws Exception
	 */
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		System.out.println("收到消息: " + message.getPayload());

		// 收到客户端请求消息后进行相应业务处理,返回结果
		this.sendMessage(session.getRemoteAddress().getHostName(), new TextMessage("收到消息: " + message.getPayload()));
	}

	/**
	 * 传输异常处理
	 */
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		super.handleTransportError(session, exception);
	}

	/**
	 * 关闭连接时触发
	 */
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		System.out.println("触发关闭websocket连接");
		// 移除连接
		connections.remove(session.getRemoteAddress().getHostName());
	}

	@Override
	public boolean supportsPartialMessages() {
		return super.supportsPartialMessages();
	}

	/**
	 * 向连接的客户端发送消息
	 *
	 * @author lucky_fd
	 * @param clientId 客户端标识
	 * @param message 消息体
	 **/
	public void sendMessage(String clientId, TextMessage message) {
		for (String client : connections.keySet()) {
			if (client.equals(clientId)) {
				try {
					WebSocketSession session = connections.get(client);
					// 判断连接是否正常
					if (session.isOpen()) {
						session.sendMessage(message);
					}
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
				break;
			}
		}
	}
}