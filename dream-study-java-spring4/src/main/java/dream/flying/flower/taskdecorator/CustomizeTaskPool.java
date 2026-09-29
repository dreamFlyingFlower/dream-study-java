package dream.flying.flower.taskdecorator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 项目里如果有 @Async 或定时任务,经常需要在任务执行前后做一些通用操作,比如传递 MDC 日志上下文、注入 TraceId
 * 
 * 以前多个 TaskDecorator 只能留一个,Spring Boot 4 改成自动合并多个装饰器
 *
 * @author 飞花梦影
 * @date 2026-09-29 11:39:53
 */
@Configuration
public class CustomizeTaskPool {

	@Bean
	ThreadPoolTaskExecutor threadPoolTaskExecutor(TraceTaskDecorator traceMdcTaskDecorator,
			MdcTaskDecorator mdcTaskDecorator) {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		// ... 其他配置
		executor.setTaskDecorator(traceMdcTaskDecorator);
		executor.setTaskDecorator(mdcTaskDecorator);
		return executor;
	}
}