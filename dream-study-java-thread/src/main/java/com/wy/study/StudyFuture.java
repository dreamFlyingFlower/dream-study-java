package com.wy.study;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 传统的线程没有返回值,自1.5之后出现了可带返回值的线程类,并且可带泛型
 * 
 * {@link Callable}:
 * 带返回值的线程,功能和{@link Runnable}相似,重写该接口的call(),在线程启动之后该方法会被调用,返回方法结果
 * Callable不能直接被Thread调用,必须先放到FutureTask中,而FutureTask实现了Runnable接口
 * {@link Callable#call()}:可以抛出任何一种校验异常,可以实现自己的执行器并重载afterExecute()来处理这些异常,由线程池执行afterExecute()
 * 
 * {@link Future#get()}:获得结果时会阻塞线程,若一直不返回结果,则线程一直阻塞,可手动打断
 * {@link Future#get(long, TimeUnit)}:获得结果时会阻塞线程,会等待指定的时间,若仍不返回结果,则抛出异常,可手动打断
 * 
 * @author 飞花梦影
 * @date 2019-05-06 22:54:52
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class StudyFuture {

	static ExecutorService fixPool = Executors.newFixedThreadPool(5);

	public static void main(String[] args) throws Exception {
		// 单线程下获得线程的返回结果,获得结果后主线程就结束
		FutureTask<String> task = new FutureTask<>(() -> "this is a test");
		Thread t = new Thread(task);
		t.start();
		try {
			// 获得返回值,会阻塞线程,直到获得结果
			System.out.println(task.get());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		// 利用线程池调用Future线程,只要线程池未关闭,主线程无法结束
		Future<String> result = fixPool.submit(() -> {
			TimeUnit.SECONDS.sleep(2);
			return "this is a test threadpool";
		});
		// 查询线程是否结束,结果是否返回
		System.out.println(result.isDone());
		try {
			// 等待返回结果,会阻塞线程.若在待指定时间无返回结果,抛出异常
			System.out.println(result.get(5, TimeUnit.SECONDS));
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			e.printStackTrace();
		}
		fixPool.shutdown();
		System.out.println("线程结束....");

		ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 5, 1, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10)) {

			@Override
			protected void afterExecute(Runnable r, Throwable t) {
				super.afterExecute(r, t);
			}
		};

		executor.submit(() -> {
			TimeUnit.SECONDS.sleep(3);
			return "this is a test threadpool";
		});
	}
}