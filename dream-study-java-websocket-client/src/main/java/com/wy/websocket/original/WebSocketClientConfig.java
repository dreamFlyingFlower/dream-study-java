package com.wy.websocket.original;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.TimeUnit;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * WebSocket客户端Session配置
 *
 * @author 飞花梦影
 * @date 2023-10-07 13:48:18
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
@Configuration
public class WebSocketClientConfig {

	@Bean
	Session clientSession() throws DeploymentException, IOException {
		while (true) {
			try {
				TimeUnit.SECONDS.sleep(5);
				// WebSocket服务端地址
				URI uri = URI.create("ws://localhost:12345/websocket/api/websocket/221");
				WebSocketContainer container = ContainerProvider.getWebSocketContainer();
				Session session = container.connectToServer(WebSocketClient.class, uri);
				return session;
			} catch (DeploymentException | IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}