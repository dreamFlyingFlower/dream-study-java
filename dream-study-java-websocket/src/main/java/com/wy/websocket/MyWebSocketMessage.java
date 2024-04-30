package com.wy.websocket;

import java.io.Serializable;

import lombok.Data;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-04-29 09:46:13
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
public class MyWebSocketMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 消息类型
	 */
	private String type;

	/**
	 * 消息内容
	 */
	private String content;

	/**
	 * 消息发送方
	 */
	private String from;

	/**
	 * 消息接收方
	 */
	private String to;

	/**
	 * 频道
	 */
	private String channel;
}