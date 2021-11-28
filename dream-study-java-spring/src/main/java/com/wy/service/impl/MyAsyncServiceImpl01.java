package com.wy.service.impl;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.wy.service.MyAsyncService01;

/**
 * 
 * 
 * @author 飞花梦影
 * @date 2021-11-23 21:19:54
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
public class MyAsyncServiceImpl01 implements MyAsyncService01 {

	@Async
	public void test1() {
		System.out.println(Thread.currentThread().getName());
		System.out.println(11111);
	}

	@Async("defaultAsyncPool")
	public void test2() {
		System.out.println(Thread.currentThread().getName());
		System.out.println(22222);
	}
}