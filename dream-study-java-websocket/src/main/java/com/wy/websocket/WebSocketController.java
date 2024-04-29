package com.wy.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.websocket.original.WebSocketServer;

import lombok.AllArgsConstructor;

/**
 * websocket测试类
 * 
 * @author 飞花梦影
 * @date 2021-01-12 11:15:31
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@RestController
@RequestMapping("websocket")
@AllArgsConstructor
public class WebSocketController {

	@GetMapping("/push/{sid}")
	public Map<String, Object> pushToWeb(@PathVariable String sid, String message) {
		Map<String, Object> result = new HashMap<>();
		try {
			WebSocketServer.sendInfo(message, sid);
			result.put("code", sid);
			result.put("msg", message);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}