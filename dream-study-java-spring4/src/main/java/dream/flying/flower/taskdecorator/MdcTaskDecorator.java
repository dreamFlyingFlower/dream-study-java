package dream.flying.flower.taskdecorator;

import java.util.Map;

import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.core.task.TaskDecorator;
import org.springframework.stereotype.Component;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2026-09-29 11:23:32
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Component
@Order(1)
public class MdcTaskDecorator implements TaskDecorator {

	@Override
	public Runnable decorate(Runnable runnable) {
		Map<String, String> context = MDC.getCopyOfContextMap();
		return () -> {
			if (context != null)
				MDC.setContextMap(context);
			try {
				runnable.run();
			} finally {
				MDC.clear();
			}
		};
	}
}