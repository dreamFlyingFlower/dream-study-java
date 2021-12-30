package com.wy.study;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 传统的线程没有返回值,自1.5之后出现了可带返回值的线程类,并且可带泛型
 * 
 * 实现Callable接口,可产生带返回值的线程,重写该接口的call方法,在线程启动之后,可得到该返回值
 * Callable不能直接被Thread调用,必须先放到FutureTask中,而FutureTask实现了Runnable接口
 * 
 * Feature获得结果的方法:<br>
 * get():获得结果时会阻塞线程,若一直不返回结果,则线程一直阻塞,可手动打断
 * get(long,timeunit):获得结果时会阻塞线程,会等待指定的时间,若仍不返回结果,则抛出异常,可手动打断
 * 
 * @author 飞花梦影
 * @date 2019-05-06 22:54:52
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class S_Future {

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

		/** 一个线程执行完成之后对该线程的结果或异常进行处理,或继续执行其他线程 **/
		// 异步执行线程,但是无返回值
		CompletableFuture.runAsync(() -> {
		}, fixPool);
		// 异步执行线程,有返回值
		CompletableFuture<Object> supplyAsync = CompletableFuture.supplyAsync(() -> {
			return null;
		}, fixPool);
		// 阻塞线程来获取结果
		System.out.println(supplyAsync.get());
		// 当上一个方法调用成功之后继续调用,还是和上一个方法是同一个线程,只能处理,不能返回结果
		supplyAsync.whenComplete((k, v) -> {
			// k是上一步调用成功的结果,v是异常,但是不能返回结果
		}).exceptionally(x -> {
			// 如果有异常,如何处理,x表示异常,可以返回异常处理的结果
			return x.getMessage();
		});
		// 当上一个方法调用成功之后继续调用,和上一个方法不是同一个线程,仍然是异步调用
		supplyAsync.whenCompleteAsync((k, v) -> {
		});
		// 同whenComplete方法,但是可以处理结果并返回
		supplyAsync.handle((k, v) -> {
			return k;
		});
		// supplyAsync执行完毕之后接着执行另外的异步任务B,B和supplyAsync是同一个线程
		supplyAsync.thenRun(() -> {
		});
		// 同上,B和supplyAsync不是同一个线程
		supplyAsync.thenRunAsync(() -> {
		});
		// 和run不同的是该方法会接收上一个线程的返回结果作为参数,和上一个线程是同一个线程,但无返回值
		supplyAsync.thenAccept((m) -> {
			// m是上一个线程的结果
			System.out.println(m);
		});
		// 同thenAccept,但是是2个不同的线程
		supplyAsync.thenAcceptAsync((m) -> {
			// m是上一个线程的结果
			System.out.println(m);
		});
		// 同thenAccept,但是有返回值,同时可以继续被下一个线程进行处理
		supplyAsync.thenApply(x -> {
			return x;
		});
		// 同thenApply,和上一个线程不是同一个线程执行
		supplyAsync.thenApplyAsync(x -> {
			return x;
		});

		/** 2个线程都执行完成之后再执行另外一个线程 **/
		CompletableFuture<Object> A = CompletableFuture.supplyAsync(() -> {
			return null;
		}, fixPool);
		CompletableFuture<Object> B = CompletableFuture.supplyAsync(() -> {
			return null;
		}, fixPool);
		// A和B都完成之后才去执行最后一个线程任务C,C无返回值,也不能对A和B的结果进一步处理
		A.runAfterBoth(B, () -> {
			// A和B执行完之后才会执行本线程,无返回值,和A是同一个线程
		});
		// 同runAfterBoth,
		A.runAfterBothAsync(B, () -> {
			// A和B执行完之后才会执行本线程,无返回值,和AB不是同一个线程
		}, fixPool);
		// 同runAfterBoth,无返回值,但是可以对A和B线程的结果做进一步处理,r1是A的返回值,r2是B的返回值
		A.thenAcceptBoth(B, (r1, r2) -> {
		});
		A.thenAcceptBothAsync(B, (r1, r2) -> {
		}, fixPool);
		// 同runAfterBoth,有返回值,可以对A和B线程的结果做进一步处理
		A.thenCombine(B, (r1, r2) -> {
			return r1.toString() + r2.toString();
		});
		A.thenCombineAsync(B, (r1, r2) -> {
			return r1.toString() + r2.toString();
		}, fixPool);

		/** 2个线程中只要有一个线程执行完毕就执行第3个线程 **/
		CompletableFuture<Object> C = CompletableFuture.supplyAsync(() -> {
			return null;
		}, fixPool);
		CompletableFuture<Object> D = CompletableFuture.supplyAsync(() -> {
			return null;
		}, fixPool);
		// C和D只要有一个线程指望完毕,C就开始执行,C无返回值,也不能对C和D的结果进一步处理
		C.runAfterEither(D, () -> {
			// C或D执行完之后才会执行本线程,无返回值,和C是同一个线程
		});
		C.runAfterEitherAsync(D, () -> {
		}, fixPool);
		// 同runAfterEither,但是可以拿到先执行完的线程的结果,C或D的结果都有可能
		C.acceptEither(D, (r) -> {
		});
		C.acceptEitherAsync(D, (r) -> {
		}, fixPool);
		// 同acceptEither,但是会返回一个新的结果
		C.applyToEither(D, (r) -> {
			return r;
		});
		C.applyToEitherAsync(D, (r) -> {
			return r;
		}, fixPool);

		/** 多任务组合 **/
		// 等待所有任务执行完成
		CompletableFuture<Void> E = CompletableFuture.allOf(A, B, C, D);
		// 等待所有线程执行完毕
		E.get();
		// 只要有一个任务完成即可
		CompletableFuture.anyOf(A, B, C, D);
	}
}