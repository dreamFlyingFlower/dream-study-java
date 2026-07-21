package dream.flying.flower.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dream.flying.flower.client.DeviceClient;
import dream.flying.flower.client.WorkCenterClient;
import lombok.RequiredArgsConstructor;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2026-07-21 10:55:01
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("httpExchange")
public class HttpExchangeController {

	private final DeviceClient deviceClient;

	private final WorkCenterClient workCenterClient;

	@GetMapping("test1/{id}")
	public Object test1(@PathVariable Long id) {
		return deviceClient.get(id);
	}

	@GetMapping("test2/{id}")
	public Object test2(@PathVariable Long id) {
		return workCenterClient.get(id);
	}
}