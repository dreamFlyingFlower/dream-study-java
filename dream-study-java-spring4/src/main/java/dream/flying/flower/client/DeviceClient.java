package dream.flying.flower.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

/**
 * 客户端接口,使用 Spring 原生的 @HttpExchange 注解,代替feign
 *
 * @author 飞花梦影
 * @date 2026-07-21 11:27:57
 */
@HttpExchange("/device")
public interface DeviceClient {

	@GetExchange("get/{id}")
	Object get(@PathVariable Long id);
}