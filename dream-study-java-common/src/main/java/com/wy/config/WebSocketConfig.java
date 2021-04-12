package com.wy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 *	启动对websocket的支持,可不使用
 *	
 *	@author 飞花梦影
 *	@date 2021-01-12 11:06:38
 * @git {@link https://github.com/mygodness100}
 */
@Configuration
public class WebSocketConfig {

	@Bean
	public ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}
}