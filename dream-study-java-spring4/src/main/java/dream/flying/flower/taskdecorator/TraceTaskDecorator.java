package dream.flying.flower.taskdecorator;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.core.task.TaskDecorator;
import org.springframework.stereotype.Component;

import io.micrometer.tracing.CurrentTraceContext;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2026-09-29 11:24:08
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Component
@Order(2)
public class TraceTaskDecorator implements TaskDecorator {

	@Autowired
	private CurrentTraceContext currentTraceContext;

	@Override
	public Runnable decorate(Runnable runnable) {
		String traceId = currentTraceContext.context().traceId();
		return () -> {
			MDC.put("traceId", traceId);
			runnable.run();
		};
	}
}