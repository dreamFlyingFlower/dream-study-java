package com.wy.config.executor;

import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.aop.interceptor.AsyncExecutionAspectSupport;
import org.springframework.aop.interceptor.AsyncExecutionInterceptor;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.AsyncResult;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;

/**
 * 自定义异步线程池配置
 * 
 * {@link AsyncConfigurer}:实现该接口的线程池在项目中只能有一个,{@link Async}修饰的类或方法默认会使用该接口的实现.
 * 当项目中同时存在默认的{@link AsyncExecutionAspectSupport#DEFAULT_TASK_EXECUTOR_BEAN_NAME}命名的Executor
 * 和实现了 AsyncConfigurer 的Executor时,默认使用实现了 AsyncConfigurer 接口的bean
 * 
 * 当存在多个Executor的bean时,优先使用实现了 AsyncConfigurer 接口的Executor,其次才会使用默认实现的Executor.
 * 当即不存在实现接口的Executor,也不存在默认的Executor时,会使用{@link SimpleAsyncTaskExecutor}
 * 
 * 使用顺序源代码:
 * 
 * <pre>
 * {@link AsyncExecutionInterceptor#getDefaultExecutor}:获得默认的Executor
 * ->{@link AsyncExecutionAspectSupport#getDefaultExecutor}:调用父类方法获得默认的Executor.
 * 		若没有设置DEFAULT_TASK_EXECUTOR_BEAN_NAME的bean,则会返回null
 * {@link AsyncExecutionInterceptor#getDefaultExecutor}:返回{@link SimpleAsyncTaskExecutor}
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2021-01-08 10:24:53
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
@Slf4j
public class MyAsyncConfig02 implements AsyncConfigurer {

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
		return (throwable, method, objects) -> {
			log.info("AsyncError: {}, Method: {}, Param: {}", throwable.getMessage(), method.getName(),
					JSON.toJSONString(objects));
			throwable.printStackTrace();
			// dosomething
		};
	}

	/**
	 * 若需要接收异步调用的返回值,可返回一个Future对象,调用该接口的isDone()判断是否完成方法
	 * 
	 * @return 异步调用结果
	 */
	@Async
	public Future<String> doAsync() {
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return new AsyncResult<>("任务完成");
	}
}