package dream.study.spring.service.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import dream.study.spring.service.MyAsyncService01;

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
	@Override
	public void test1() {
		System.out.println(Thread.currentThread().getName());
		System.out.println(11111);
	}

	@Async("defaultAsyncPool")
	@Override
	public void test2() {
		System.out.println(Thread.currentThread().getName());
		System.out.println(22222);
	}

	@Override
	public void execute(DeferredResult<String> deferredResult) {
		try {
			TimeUnit.SECONDS.sleep(60000);
			deferredResult.setResult("success");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}