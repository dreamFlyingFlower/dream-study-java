package com.wy.websocket.spring;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-04-29 17:43:56
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@RestController
@RequestMapping("websocket")
@AllArgsConstructor
public class WebSocketHandlerController {

	private final SimpMessagingTemplate messagingTemplate;

	@GetMapping("sendMessage")
	public String sendMessage(String message) {
		messagingTemplate.convertAndSend("/spring-websocket", message);
		return message;
	}
}