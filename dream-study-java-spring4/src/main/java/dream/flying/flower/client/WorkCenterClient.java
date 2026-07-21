package dream.flying.flower.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2026-07-21 11:27:57
 */
@HttpExchange("workCenter")
public interface WorkCenterClient {

	@GetExchange("get/{id}")
	Object get(@PathVariable Long id);
}