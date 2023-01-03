package com.wy.crl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.wy.result.Result;
import com.wy.service.MyAsyncService01;

import lombok.extern.slf4j.Slf4j;

/**
 * 异步测试API
 * 
 * @author 飞花梦影
 * @date 2021-11-23 21:14:31
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@RestController
@RequestMapping("testAsync")
@Slf4j
public class TestAsyncCrl {

	@Autowired
	private MyAsyncService01 myAsyncService01;

	@GetMapping("test01")
	public Result<?> test01() {
		myAsyncService01.test1();
		return Result.ok();
	}

	@GetMapping("test02")
	public Result<?> test02() {
		myAsyncService01.test2();
		return Result.ok();
	}

	/**
	 * 异步调用可以使用AsyncHandlerInterceptor进行拦截,见
	 * 
	 * @return
	 */
	@GetMapping("/deferred")
	public DeferredResult<String> executeSlowTask() {
		DeferredResult<String> deferredResult = new DeferredResult<>();
		// 调用长时间执行任务
		myAsyncService01.execute(deferredResult);
		// 当长时间任务中使用deferredResult.setResult("success");这个方法时,会从长时间任务中返回,继续controller里面的流程
		log.info(Thread.currentThread().getName() + "从executeSlowTask方法返回");
		// 超时的回调方法
		deferredResult.onTimeout(new Runnable() {

			@Override
			public void run() {
				log.info(Thread.currentThread().getName() + " onTimeout");
				// 返回超时信息
				deferredResult.setErrorResult("time out!");
			}
		});

		// 处理完成的回调方法,无论是超时还是处理成功,都会进入这个回调方法
		deferredResult.onCompletion(new Runnable() {

			@Override
			public void run() {
				log.info(Thread.currentThread().getName() + " onCompletion");
			}
		});

		return deferredResult;
	}
}