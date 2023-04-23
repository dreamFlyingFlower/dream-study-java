package com.wy.crl;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.WebAsyncTask;

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

	/**
	 * 带超时时间的异步请求,通过WebAsyncTask自定义客户端超时间
	 */
	@GetMapping("/world")
	public WebAsyncTask<String> worldController() {
		log.info(Thread.currentThread().getName() + " 进入testAsyncController方法");

		// 3s钟没返回,则认为超时
		WebAsyncTask<String> webAsyncTask = new WebAsyncTask<>(3000, new Callable<String>() {

			@Override
			public String call() throws Exception {
				log.info(Thread.currentThread().getName() + " 进入call方法");
				myAsyncService01.test1();
				log.info(Thread.currentThread().getName() + " 从myAsyncService方法返回");
				return "result";
			}
		});
		log.info(Thread.currentThread().getName() + " 从testAsyncController方法返回");

		webAsyncTask.onCompletion(new Runnable() {

			@Override
			public void run() {
				log.info(Thread.currentThread().getName() + " 执行完毕");
			}
		});

		webAsyncTask.onTimeout(new Callable<String>() {

			@Override
			public String call() throws Exception {
				log.info(Thread.currentThread().getName() + " onTimeout");
				// 超时的时候,直接抛异常,让外层统一处理超时异常
				throw new TimeoutException("调用超时");
			}
		});
		return webAsyncTask;
	}

	/**
	 * 异步调用,异常处理
	 */
	@GetMapping("/exception")
	public WebAsyncTask<String> exceptionController() {
		log.info(Thread.currentThread().getName() + " 进入testAsyncController方法");
		Callable<String> callable = new Callable<String>() {

			@Override
			public String call() throws Exception {
				log.info(Thread.currentThread().getName() + " 进入call方法");
				throw new TimeoutException("调用超时!");
			}
		};
		log.info(Thread.currentThread().getName() + " 从testAsyncController方法返回");
		return new WebAsyncTask<>(20000, callable);
	}
}