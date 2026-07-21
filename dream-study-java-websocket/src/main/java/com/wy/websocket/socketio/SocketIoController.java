package com.wy.websocket.socketio;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.wy.websocket.MyWebSocketMessage;

import lombok.RequiredArgsConstructor;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-04-29 09:51:30
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@RestController
@RequiredArgsConstructor
public class SocketIoController {

	public final static String SEND_TYPE_ALL = "ALL";

	public final static String SEND_TYPE_ALONE = "ALONE";

	private final SocketIoHelpers socketIoHelpers;

	@PostMapping("/testSendMsg")
	public String testSendMsg(@RequestBody MyWebSocketMessage myMessage) {
		Map<String, Object> map = new HashMap<>();
		map.put("msg", myMessage.getContent());

		// 群发
		if (SEND_TYPE_ALL.equals(myMessage.getType())) {
			socketIoHelpers.sendToAll(map, myMessage.getChannel());
			return "success";
		}
		// 指定单人
		if (SEND_TYPE_ALONE.equals(myMessage.getType())) {
			socketIoHelpers.sendToOne(myMessage.getTo(), map, myMessage.getChannel());
			return "success";
		}

		return "fail";
	}
}