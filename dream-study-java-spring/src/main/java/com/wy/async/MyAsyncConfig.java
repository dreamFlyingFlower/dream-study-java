package com.wy.async;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncConfigurer;

import lombok.extern.slf4j.Slf4j;

/**
 * 自定义异步异常配置,捕获被{@link Async}修饰的方法
 *
 * @author 飞花梦影
 * @date 2025-01-15 11:30:55
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Slf4j
public class MyAsyncConfig implements AsyncConfigurer {

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return (ex, method, params) -> log.error("Exception in method {} with params {}", method, params, ex);
	}
}