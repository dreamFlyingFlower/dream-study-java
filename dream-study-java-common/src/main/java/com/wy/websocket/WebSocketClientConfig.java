package com.wy.websocket;

import java.io.IOException;
import java.net.URI;

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
	Session webSocketClientSession(URI uri) throws DeploymentException, IOException {
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		return container.connectToServer(WebSocketClient.class, uri);
	}
}
