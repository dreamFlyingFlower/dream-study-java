package com.wy.websocket;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-04-30 10:45:42
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum WebSocketType {

	OPEN,
	IDLE,
	CLOSE,
	MESSAGE,
	ERROR;
}