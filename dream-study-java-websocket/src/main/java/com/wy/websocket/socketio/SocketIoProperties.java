package com.wy.websocket.socketio;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * SocketIO配置
 *
 * @author 飞花梦影
 * @date 2024-04-29 09:41:34
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
@ConfigurationProperties("dream.socketio")
@Data
public class SocketIoProperties {

	private String host;

	private Integer port;

	private Integer bossCount;

	private Integer workCount;

	private boolean allowCustomRequests;

	private Integer upgradeTimeout;

	private Integer pingTimeout;

	private Integer pingInterval;

	private Integer maxFramePayloadLength;

	private Integer maxHttpContentLength;
}