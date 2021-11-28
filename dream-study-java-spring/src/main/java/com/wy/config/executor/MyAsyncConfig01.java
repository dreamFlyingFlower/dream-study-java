package com.wy.config.executor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.aop.interceptor.AsyncExecutionAspectSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 自定义线程池,可以在单个项目中配置多个不同beanName的实例
 * 
 * @author 飞花梦影
 * @date 2021-11-23 21:09:09
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
public class MyAsyncConfig01 {

	/**
	 * 使用该方式配置的线程池,会覆盖默认的线程池,{@link Async}中不需要指定额外的beanName
	 * 
	 * 当Project中有实现了{@link AsyncConfigurer}接口的Executor时,该默认Executor优先级低于实现类
	 * 
	 * @return 线程执行器
	 */
	@Bean(AsyncExecutionAspectSupport.DEFAULT_TASK_EXECUTOR_BEAN_NAME)
	public Executor defaultAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		// 配置核心线程数
		executor.setCorePoolSize(10);
		// 配置最大线程数
		executor.setMaxPoolSize(20);
		// 配置队列大小
		executor.setQueueCapacity(20);
		// 配置空闲线程的最大时间,超过该时间,线程将被回收
		executor.setKeepAliveSeconds(60);
		// 配置线程名前缀
		executor.setThreadNamePrefix("defaultAsyncExecutor_");
		// 当线程池关闭时,定时任务是否立刻关闭,清空队列,而不等待他们执行完成,默认false,立刻关闭
		executor.setWaitForTasksToCompleteOnShutdown(true);
		// 当线程池要关闭时,仍有线程在执行,此时等待的最大时长
		executor.setAwaitTerminationSeconds(60);
		// 当线程池中的队列满时,设置拒绝策略
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
		executor.initialize();
		return executor;
	}

	/**
	 * 使用该方式配置的线程池,可以配置多个,只需要使用的beanName不同即可.同时在{@link Async}中需要指定线程池的名称
	 * 
	 * 该方式配置的Executor不受{@link AsyncConfigurer}接口实现类的影响,只和beanName有关
	 * 
	 * @return 线程执行器
	 */
	@Bean
	public Executor defaultAsyncPool() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		// 配置核心线程数
		executor.setCorePoolSize(10);
		// 配置最大线程数
		executor.setMaxPoolSize(20);
		// 配置队列大小
		executor.setQueueCapacity(20);
		// 配置空闲线程的最大时间,超过该时间,线程将被回收
		executor.setKeepAliveSeconds(60);
		// 配置线程名前缀
		executor.setThreadNamePrefix("defaultAsyncPool_");
		// 当线程池关闭时,定时任务是否立刻关闭,清空队列,而不等待他们执行完成,默认false,立刻关闭
		executor.setWaitForTasksToCompleteOnShutdown(true);
		// 当线程池要关闭时,仍有线程在执行,此时等待的最大时长
		executor.setAwaitTerminationSeconds(60);
		// 当线程池中的队列满时,设置拒绝策略
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
		executor.initialize();
		return executor;
	}
}