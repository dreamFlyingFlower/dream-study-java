package com.wy.websocket.socketio;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-04-29 09:47:39
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Component
@Slf4j
@AllArgsConstructor
public class MySocketIoHandler {

	private final SocketIOServer socketIoServer;

	@PostConstruct
	private void start() {
		try {
			socketIoServer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PreDestroy
	private void destroy() {
		try {
			socketIoServer.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 连接
	 * 
	 * @param client
	 */
	@OnConnect
	public void connect(SocketIOClient client) {
		String userFlag = client.getHandshakeData().getSingleUrlParam("userFlag");
		SocketIoHelpers.connectMap.put(userFlag, client);
		log.info("客户端userFlag: " + userFlag + "已连接");
	}

	/**
	 * 退出
	 * 
	 * @param client
	 */
	@OnDisconnect
	public void onDisconnect(SocketIOClient client) {
		String userFlag = client.getHandshakeData().getSingleUrlParam("userFlag");
		log.info("客户端userFlag:" + userFlag + "断开连接");
		SocketIoHelpers.connectMap.remove(userFlag, client);
	}
}