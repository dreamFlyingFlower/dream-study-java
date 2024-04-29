package com.wy.websocket.original;

import javax.websocket.Session;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

/**
 * WebSocket客户端测试发送消息
 * 
 * @author 飞花梦影
 * @date 2021-01-12 11:15:31
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@RestController
@RequestMapping("/client")
@AllArgsConstructor
public class WebSocketController {

	/**
	 * 在建立连接时被纳入到Spring容器中的Session,名字需要和{@link WebSocketClientConfig#clientSession}相同
	 */
	private final Session clientSession;

	@GetMapping("sendMessage")
	public void sendMessage(String message) {
		clientSession.getAsyncRemote().sendText("对" + message + "的回复!");
	}
}