package com.wy.study;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * {@link CompletableFuture}:多线程异步变成,内部使用了{@link ForkJoinPool}进行任务的执行
 * 
 * {@link CompletableFuture#runAsync}与{@link CompletableFuture#supplyAsync}是CompletableFuture的静态方法
 * {@link CompletableFuture#thenAccept},{@link CompletableFuture#thenAsync},thenApply是CompletableFutre的成员方法
 * 因为初始的时候没有CompletableFuture对象,也没有参数可传,所以提交的只能是Runnable或者Supplier,只能是静态方法
 * 通过静态方法生成CompletableFuture对象后,便可以链式地提交其他任务,这个时候就可以提交Runnable,Consumer,Function,且都是成员方法
 * 每一个CompletableFuture对象内部都有一个栈,存储着是后续依赖它的任务,这个栈也就是Treiber
 * Stack,这里的stack存储的就是栈顶指针
 * 
 * {@link CompletionStage}:所有方法的返回值都是它自己,才能实现如下的链式调用:future1.thenApply().thenApply().thenCompose().thenRun()
 * 
 * ForkJoinPool只接收ForkJoinTask,而向CompletableFuture提交的任务是Runnable/Supplier/Consumer/Function,因此需要适配器进行转换:
 * 
 * <pre>
 * {@link CompletableFuture.AsyncSupply}:在supplyAsync()中,将 Supplier 转换成一个 AsyncSupply ,然后提交给ForkJoinPool执行
 * {@link CompletableFuture.AsyncRun}:在runAsync()中,将Runnable转换成一个AsyncRun,然后提交给ForkJoinPool执行
 * {@link CompletableFuture.UniRun}:在thenRun()中将Runnable转换为UniRun提交给ForkJoinPool,和AsyncRun不同的是,该类可链式调用
 * {@link CompletableFuture.UniAccept}:在thenAccept()中将Consumer转换为UniAccept提交给ForkJoinPool
 * {@link CompletableFuture.UniApply}:在thenApply()中将Function转换为UniApply提交给ForkJoinPool
 * {@link CompletableFuture.BiRun}:组合与,3参数
 * {@link CompletableFuture.BiAccecpt}:组合与,3参数
 * {@link CompletableFuture.BiApply}:组合与,3参数
 * {@link CompletableFuture.OrRun}:组合或,3参数
 * {@link CompletableFuture.OrAccept}:组合或,3参数
 * {@link CompletableFuture.OrApply}:组合或,3参数
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2019-05-06 22:54:52
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class StudyCompletableFuture {

	static ExecutorService fixPool = Executors.newFixedThreadPool(5);

	public static void main(String[] args) throws Exception {
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
		// thenCompose会在上个任务执行完成后将结果作为方法入参然后执行指定的方法,该方法会返回一个新的CompletableFuture
		// 如果该CompletableFuture实例的result不为null,则返回一个基于该result的新的CompletableFuture实例;
		// 如果该CompletableFuture实例的result为null,则执行这个新任务
		supplyAsync.thenCompose(x -> {
			return CompletableFuture.supplyAsync(() -> {
				return x;
			});
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

		// 延迟执行,高版本可用submit,低版本只能使用execute
		CompletableFuture.delayedExecutor(Duration.ofMillis(1).toMillis(), TimeUnit.MILLISECONDS).execute(() -> {
			System.out.println(11);
		});
		// 延迟执行,高版本可用submit,低版本只能使用execute
		// CompletableFuture.delayedExecutor(Duration.ofMillis(1).toMillis(),
		// TimeUnit.MILLISECONDS).submit(() -> {
		// return null;
		// }).thenCompose(future -> future);
	}
}