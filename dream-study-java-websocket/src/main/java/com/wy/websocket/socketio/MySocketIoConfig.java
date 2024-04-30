package com.wy.websocket.socketio;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;

import lombok.AllArgsConstructor;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-04-29 09:44:19
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
@AllArgsConstructor
public class MySocketIoConfig {

	private final SocketIoProperties socketIoProperties;

	@Bean
	SocketIOServer socketIoServer() {
		SocketConfig socketConfig = new SocketConfig();
		socketConfig.setTcpNoDelay(true);
		socketConfig.setSoLinger(0);
		com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
		buildSocketConfig(socketConfig, config);
		return new SocketIOServer(config);
	}

	/**
	 * 扫描netty-socketIo的注解( @OnConnect、@OnEvent等）
	 */
	@Bean
	SpringAnnotationScanner springAnnotationScanner() {
		return new SpringAnnotationScanner(socketIoServer());
	}

	private void buildSocketConfig(SocketConfig socketConfig, com.corundumstudio.socketio.Configuration config) {
		config.setSocketConfig(socketConfig);
		config.setHostname(socketIoProperties.getHost());
		config.setPort(socketIoProperties.getPort());
		config.setBossThreads(socketIoProperties.getBossCount());
		config.setWorkerThreads(socketIoProperties.getWorkCount());
		config.setAllowCustomRequests(socketIoProperties.isAllowCustomRequests());
		config.setUpgradeTimeout(socketIoProperties.getUpgradeTimeout());
		config.setPingTimeout(socketIoProperties.getPingTimeout());
		config.setPingInterval(socketIoProperties.getPingInterval());
	}
}