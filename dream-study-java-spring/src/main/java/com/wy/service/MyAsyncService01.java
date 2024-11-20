package com.wy.service;

import org.springframework.web.context.request.async.DeferredResult;

/**
 * 
 * 
 * @author 飞花梦影
 * @date 2021-11-23 21:19:32
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface MyAsyncService01 {

	void test1();

	void test2();

	void execute(DeferredResult<String> deferredResult);
}