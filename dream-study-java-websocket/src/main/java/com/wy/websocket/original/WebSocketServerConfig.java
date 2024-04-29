package com.wy.websocket.original;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * 启动对websocket的支持,可不使用
 * 
 * @author 飞花梦影
 * @date 2021-01-12 11:06:38
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
@Configuration
public class WebSocketServerConfig {

	@Bean
	ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}
}