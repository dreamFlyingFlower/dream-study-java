package com.wy.config;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncConfigurer;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;

/**
 * 自定义异步线程池配置,同时需要在使用{@link Async}的类或方法上添加getAsyncExecutor
 * 
 * @author 飞花梦影
 * @date 2021-01-08 10:24:53
 * @git {@link https://github.com/mygodness100}
 */
@Configuration
@Slf4j
public class AsyncPoolConfig implements AsyncConfigurer {

	/**
	 * 在需要使用{@link Async}的地方,该直接的value()值必须是getAsyncExecutor
	 */
	@Bean
	@Override
	public Executor getAsyncExecutor() {
		// 使用自定义的线程池或直接使用系统自带的线程池
		ThreadPoolLogExecutor threadPoolLogExecutor = new ThreadPoolLogExecutor();
		// ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		// 配置核心线程数
		threadPoolLogExecutor.setCorePoolSize(10);
		// 配置最大线程数
		threadPoolLogExecutor.setMaxPoolSize(20);
		// 配置队列大小
		threadPoolLogExecutor.setQueueCapacity(20);
		// 配置空闲线程的最大时间,超过该时间,线程将被回收
		threadPoolLogExecutor.setKeepAliveSeconds(60);
		// 配置线程名前缀
		threadPoolLogExecutor.setThreadNamePrefix("getAsyncExecutor_");
		// 当线程池关闭时,定时任务是否立刻关闭,清空队列,而不等待他们执行完成,默认false,立刻关闭
		threadPoolLogExecutor.setWaitForTasksToCompleteOnShutdown(true);
		// 当线程池要关闭时,仍有线程在执行,此时等待的最大时长
		threadPoolLogExecutor.setAwaitTerminationSeconds(60);
		// 当线程池中的队列满时,设置拒绝策略
		threadPoolLogExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
		threadPoolLogExecutor.initialize();
		return threadPoolLogExecutor;
	}

	/**
	 * 定义异步任务异常处理类,只会处理没有返回值的任务
	 */
	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new AsyncExceptionHandler();
	}

	class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

		@Override
		public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {
			log.info("AsyncError: {}, Method: {}, Param: {}", throwable.getMessage(), method.getName(),
					JSON.toJSONString(objects));
			throwable.printStackTrace();
			// TODO 发送邮件或者短信
		}
	}
}